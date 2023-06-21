# 第03节 Mybatis SQL执行流程

在前面三篇关于Mybatis配置文件解析的文章中，已经介绍过了，Mybatis会把全局配置文件和SQL配置文件解析的结果全部存在Configuration类的实例中。

而我们在使用Mybatis执行SQL是，首先需要创建一个SqlSessionFactory的工厂实例，
在SqlSessionFactoryBuilder的build(Configuration config)方法中，就会把解析得到的配置作为入参，
构造一个DefaultSqlSessionFactory实例。
```text
// 到这里配置文件已经解析成了Configuration
public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
}
```
然后通过DefaultSqlSessionFactory就可以调用openSession()来获得一个SqlSession实例，就可以直接执行SQL了，
那么这个SqlSession是一个什么东西，它调用方法执行SQL的流程又是怎样的呢？

## 一、创建Executor实例
其实Mybatis的SqlSession是采用【门面】设计模式来实现的，它自身并不具备执行SQL的能力，而是由它内部的Executor实例来执行相关方法。
我们通过SqlSessionFactory的openSession()方法创建SqlSession，其实内部就是在创建一个Executor实例，我们具体看源码：
```text
private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, 
                                                boolean autoCommit) {
    Transaction tx = null;
    // 获取环境变量
    final Environment environment = configuration.getEnvironment();
    // 获取事务工厂
    final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
    // 创建事务
    tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
    /**
    * 创建一个sql执行器对象
    * 一般情况下 若我们的mybatis的全局配置文件的cacheEnabled默认为ture就返回
    * 一个cacheExecutor,若关闭的话返回的就是一个SimpleExecutor
    */
    final Executor executor = configuration.newExecutor(tx, execType);
    // 创建返回一个DefaultSqlSession对象返回
    return new DefaultSqlSession(configuration, executor, autoCommit);
}
```
在解析数据源配置的时候，就会在Environment实例中设置事务工厂，如果没有配置则使用默认的ManagedTransactionFactory，
然后创建一个事务，这个时候只是创建了一个事务实例对象，并没有创建与数据库的连接。

### 1.1 Executor类型
在创建Executor实例的时候，需要指定其类型，Executor提供了三种类型的实现：
```text
public enum ExecutorType {
    SIMPLE, REUSE, BATCH
}
```
如果没有指定，就会使用SIMPLE作为默认的类型，这三种类型的Executor有什么区别呢？
```text
SIMPLE：每次执行SQL语句，就单独开启一个Statement对象，用完立即关闭Statement对象。

REUSE：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，
        而是放置于Map<String, Statement>内，供下一次使用。简言之，就是重复使用Statement对象。

BATCH：执行update，将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，
        每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。
```
注：Executor的这些特点，都严格限制在SqlSession生命周期范围内。

这三类是Mybatis提供的基本的Executor类型，但是在Mybatis中，还有一种特殊的Executor类型，就是CacheExecutor，
我们Mybatis的配置文件中，可以通过cacheEnabled属性来开启，如果该属性为true，在创建Executor的时候，
就会把基本的Executor封装成一个CacheExecutor。

### 1.2 创建Executor实例
根据指定的Executor类型创建对应的Executor实例，然后判断是否需要封装成CachingExecutor实例：
```text
public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    executorType = executorType == null ? defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Executor executor;
    //【重要】根据类型创建匹配的执行器
    if (ExecutorType.BATCH == executorType) {
        executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
        executor = new ReuseExecutor(this, transaction);
    } else {
        executor = new SimpleExecutor(this, transaction);
    }
    // 【重要】判断mybatis的全局配置文件是否开启缓存
    if (cacheEnabled) {
        // 【重要】把当前的简单的执行器包装成一个CachingExecutor
        executor = new CachingExecutor(executor);
    }
    // TODO:调用所有的拦截器对象plugin方法  
    // 【重要】插件： 责任链 + 装饰器模式（动态代理）
    executor = (Executor) interceptorChain.pluginAll(executor);
    return executor;
}
```

### 1.3 创建插件的代理对象
创建完Executor实例之后，接着会去匹配Executor类型的拦截器，遍历所有拦截器的plugin()方法:
```text
public Object pluginAll(Object target) {
    for (Interceptor interceptor : interceptors) {
        target = interceptor.plugin(target);
    }
    return target;
}
```
拦截器的使用在《Mybatis 配置文件解析(一)》的插件配置部分有介绍。

Interceptor接口中，提供了plugin()方法的默认实现，如果自定义拦截器没有实现plugin()方法，就会调用默认plugin()方法：
```text
default Object plugin(Object target) {
    return Plugin.wrap(target, this);
}
```
Plugin的wrap()方法会获取拦截器上面@Intercepts注解的@Signature注解的信息(拦截的Executor类型，具体的方法)。

调用getAllInterfaces()来匹配当前代理类型与@Signature注解指定的类型是否匹配，
如果匹配就可以生成一个代理对象，原始对象是Executor实例，当调用代理对象的方法时(也就是执行Executor的方法)，
会去调用Plugin类的invoke()方法。
```text
public static Object wrap(Object target, Interceptor interceptor) {
    // 获得interceptor配置的@Signature的type
    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
    // 当前代理类型
    Class<?> type = target.getClass();
    // 根据当前代理类型 和 @signature指定的type进行配对， 配对成功则可以代理
    Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
    if (interfaces.length > 0) {
        return Proxy.newProxyInstance(
                        type.getClassLoader(),
                        interfaces,
                        new Plugin(target, interceptor, signatureMap)
                    );
    }
    return target;
}
```
至此就生成了最后的Executor实例，然后对Executor实例进行封装，生成一个DefaultSqlSession实例返回，
这是就是使用SqlSession来操作SQL了。

## 二、获取Mapper接口代理对象
在《Mybatis 配置文件解析(二)》的生成代理工厂部分，以及详细介绍了创建代理工厂的过程，
在我们使用的时候，直接调用SqlSession的getMapper()方法就可以获得Mapper接口的代理对象。

我们以UserMapper为例，看一下getMapper()方法的源码是如何实现的
```text
// 创建动态代理
UserMapper mapper = session.getMapper(UserMapper.class);
```
会去调用Configuration的getMapper()方法，我们前面配置文件解析的文章已经讲过，
Mybatis解析得到的配置信息都在configuration里面放着。
```text
public <T> T getMapper(Class<T> type) {
    return configuration.getMapper(type, this);
}
```
在Configuration的getMapper()方法中，会调用mapperRegistry的getMapper()方法，
这个MapperRegistry里面存放的就是所以Mapper接口对应的代理工厂MapperProxyFactory。
```text
public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    return mapperRegistry.getMapper(type, sqlSession);
}
```
在MapperRegistry的getMapper()方法中，MapperRegistry的knownMappers属性中缓存了Mapper接口和代理工厂的映射，
根据接口类型拿到对应的代理工厂，然后调用代理工厂的newInstance()方法就可以拿到具体的代理对象，
关于MapperProxyFactory在前面的文章已经详细讲过了，这里不做过多赘述。
```text
public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    // 直接去缓存knownMappers中通过Mapper的class类型去找我们的mapperProxyFactory
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    // 缓存中没有获取到 直接抛出异常
    if (mapperProxyFactory == null) {
        throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }
    try {
        // 通过MapperProxyFactory来创建我们的实例
        return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
        throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
}
```

在《Mybatis SQL执行流程(一)》中我们已经通过SqlSession的getMapper()方法获得了Mapper接口的代理对象，
此时就可以直接通过调用代理对象的方法来执行SQL语句了，具体又是怎么执行的呢？这一节将重点介绍SQL的执行流程。

Mapper接口代理对象对应的InvocationHandler实现类是MapperProxy，所以当调用接口的方法时，
首先就会进入到MapperProxy的invoke()方法中，我们先看下MapperProxy的invoke()方法实现。

在方法调用时，如果是调用Object的方法或者接口中的默认方法，直接通过反射调用，调用接口中的其他方法，才会去走执行SQL的逻辑。
```text
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
        // 判断我们的方法是不是我们的Object类定义的方法，若是直接通过反射调用
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else if (method.isDefault()) {   //是否接口的默认方法
            // 调用我们的接口中的默认方法
            return invokeDefaultMethod(proxy, method, args);
        }
    } catch (Throwable t) {
        throw ExceptionUtil.unwrapThrowable(t);
    }
    // 真正的进行调用，做了二个事情
    // 第一步: 把我们的方法对象封装成一个MapperMethod对象(带有缓存作用的)
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    // 第二步: 执行
    return mapperMethod.execute(sqlSession, args);
}
```

## 三、 创建并缓存MapperMethod
执行SQL前，第一步就是去把调用的Method对象封装成一个MapperMethod，然后进行缓存。
```text
private MapperMethod cachedMapperMethod(Method method) {
  /**
  * 相当于这句代码.jdk8的新写法
  * if(methodCache.get(method)==null){
  *     methodCache.put(new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()))
  * }
  */
  return methodCache.computeIfAbsent(method, k -> new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
}
```
下面看一下MapperMethod里面都有哪些内容：
```text
public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
    // 创建【SqlCommand对象】
    this.command = new SqlCommand(config, mapperInterface, method);
    // 创建【方法签名对象】
    this.method = new MethodSignature(config, mapperInterface, method);
}
```
其中SqlCommand对象里面会存接口的全限定名和接口对应的SQL操作类型。
```text
public static class SqlCommand {
    // 接口的方法名全路径比如:com.lizhi.mapper.UserMapper.selectById
    private final String name;
    // 对应接口方法操作的sql类型(是insert|update|delete|select)
    private final SqlCommandType type;
}
```
而MethodSignature方法签名中存了方法的返回值类型和参数解析器，参数解析器初始的时候，就回去解析是方法的方法，
然后按照参数下标索引存放在ParamNameResolver的names属性中，这是一个SortedMap，
在参数解析的时候，会过滤掉逻辑分页RowBounds类型的参数和ResultHandler类型的参数。
```text
public MethodSignature(Configuration configuration, Class<?> mapperInterface, Method method) {
    // 解析方法的返回值类型
    Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface);
    // 判断返回值是不是class类型的
    if (resolvedReturnType instanceof Class<?>) {
        this.returnType = (Class<?>) resolvedReturnType;
    } else if (resolvedReturnType instanceof ParameterizedType) {
        // 是不是参数泛型的
        this.returnType = (Class<?>) ((ParameterizedType) resolvedReturnType).getRawType();
    } else {
        // 普通的
        this.returnType = method.getReturnType();
    }
    // 返回值是不是为空
    this.returnsVoid = void.class.equals(this.returnType);
    // 返回是是不是集合类型
    this.returnsMany = configuration.getObjectFactory().isCollection(this.returnType) || this.returnType.isArray();
    // 返回值是不是游标
    this.returnsCursor = Cursor.class.equals(this.returnType);
    // 返回值是不是optional类型的
    this.returnsOptional = Optional.class.equals(this.returnType);
    // 初始化我们参数解析器对象
    this.paramNameResolver = new ParamNameResolver(configuration, method);
}
```

## 四、根据操作类型进行调用
得到MapperMethod对象后，就去调用它的execute()方法来执行SQL。
进入MapperMethod.execute()方法：
```text
  public Object execute(SqlSession sqlSession, Object[] args) {
    Object result;
    switch (command.getType()) {
      case INSERT: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.insert(command.getName(), param));
        break;
      }
      case UPDATE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.update(command.getName(), param));
        break;
      }
      case DELETE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.delete(command.getName(), param));
        break;
      }
      case SELECT:
        if (method.returnsVoid() && method.hasResultHandler()) {
          executeWithResultHandler(sqlSession, args);
          result = null;
        } else if (method.returnsMany()) {
          result = executeForMany(sqlSession, args);
        } else if (method.returnsMap()) {
          result = executeForMap(sqlSession, args);
        } else if (method.returnsCursor()) {
          result = executeForCursor(sqlSession, args);
        } else {
          Object param = method.convertArgsToSqlCommandParam(args);
          result = sqlSession.selectOne(command.getName(), param);
          if (method.returnsOptional() && (result == null || !method.getReturnType().equals(result.getClass()))) {
            result = Optional.ofNullable(result);
          }
        }
        break;
      case FLUSH:
        result = sqlSession.flushStatements();
        break;
      default:
        throw new BindingException("Unknown execution method for: " + command.getName());
    }
    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
      throw new BindingException("Mapper method '" + command.getName()
          + "' attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
    }
    return result;
  }
```
在execute()方法中，会根据方法对应的SQL操作类型，执行对应的方法。
所有的操作最后都换转换成去调用SqlSession中的方法。
我们以调用UseMapper的selectById()方法为例，它的返回值是一个User对象，所以会去调用SELECT下的最后一个分支，下面具体看一下执行逻辑。

### 4.1 参数解析
参数解析时，会利用MethodSignature中参数解析器的getNamedParams()方法进行解析。
进入MapperMethod.convertArgsToSqlCommandParam()方法：
```text
    Object param = method.convertArgsToSqlCommandParam(args);

    public Object convertArgsToSqlCommandParam(Object[] args) {
      return paramNameResolver.getNamedParams(args);
    }
```
进入ParamNameResolver.getNamedParams()方法：
```text
public Object getNamedParams(Object[] args) {
    // 获取参数的个数
    // names的数据结构为map ({key="0",value="id"},{key="1",value="name"})
    final int paramCount = names.size();
    // 若参数的个数为空或者个数为0直接返回
    if (args == null || paramCount == 0) {
        return null;
    } else if (!hasParamAnnotation && paramCount == 1) {
        // 若有且只有一个参数 而且没有标注了@Param指定方法方法名称
        return wrapToMapIfCollection(value, useActualParamName ? names.get(names.firstKey()) : null);
    } else {
        final Map<String, Object> param = new ParamMap<>();
        int i = 0;
        // 循坏我们所有的参数的个数
        for (Map.Entry<Integer, String> entry : names.entrySet()) {
            // 把key为id,value为1加入到param中
            param.put(entry.getValue(), args[entry.getKey()]);
            // add generic param names (param1, param2, ...)
            // 加入通用的参数：名称为param + 0,1,2,3......
            final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
            // ensure not to overwrite parameter named with @Param
            if (!names.containsValue(genericParamName)) {
                // 把key为param + 0,1,2,3.....,value值加入到param中
                param.put(genericParamName, args[entry.getKey()]);
            }
            i++;
        }
        return param;
    }
}
```
names属性就是参数解析器实例化的时候，解析方法参数得到的一个SortMap，其中KEY为参数的索引下标，VALUE为参数名。
如果只有一个没有加@Param注解的参数，就直接返回该参数值。
其他有参数的情况，会把这些参数全部封装到一个Map里面，然后返回整个Map对象。

### 4.2 调用SqlSession方法
参数解析完成之后，就会去调用SqlSession的方法了，我们看DefaultSqlSession的selectOne()方法：
```text
@Override
public <T> T selectOne(String statement, Object parameter) {
  // 这里selectOne调用也是调用selectList方法
  List<T> list = this.selectList(statement, parameter);
  // 若查询出来有且有一个一个对象，直接返回要给
  if (list.size() == 1) {
    return list.get(0);
  } else if (list.size() > 1) {
    // 查询的有多个,那么就抛出我们熟悉的异常
    throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
  } else {
    return null;
  }
}

@Override
public <E> List<E> selectList(String statement, Object parameter) {
    return this.selectList(statement, parameter, RowBounds.DEFAULT);
}

@Override
public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
  return selectList(statement, parameter, rowBounds, Executor.NO_RESULT_HANDLER);
}

private <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
  try {
    // 第一步：通过我们的statement去我们的全局配置类中获取MappedStatement
    MappedStatement ms = configuration.getMappedStatement(statement);
    dirty |= ms.isDirtySelect();
    /**
    * 【重要】
    * 通过执行器去执行我们的sql对象
    * 第一步:包装我们的集合类参数
    * 第二步:一般情况下是executor为cacheExecutor对象
    */
    return executor.query(ms, wrapCollection(parameter), rowBounds, handler);
  } catch (Exception e) {
    throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
  } finally {
    ErrorContext.instance().reset();
  }
}
```
在具体的selectList()方法中的，会根据方法名拿到Configuration中的MappedStatement对象，
这个MappedStatement对象就是在解析SQL的xml文件，把每一个SQL最终解析成了一个MappedStatement对象，
具体的解析过程可以在《Mybatis 配置文件解析(二)》中查看，然后去调用Executor实例的query()方法。

文章前面介绍了Executor对象的创建，如果开启了缓存，会生成一个CachingExecutor实例封装到SqlSession中，
这里我们开启了缓存，所以会去执行CachingExecutor的query()方法。

### 4.3 解析动态SQL
进入CachingExecutor.query()方法：
```text
public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
  /**
  * 通过参数对象解析我们的sql详细信息1339025938:1570540512:com.lizhi.mapper.selectById:0:2147483647:select id,user_name,create_time from t_user where id=?:1:development
  */
  BoundSql boundSql = ms.getBoundSql(parameterObject);
  CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
  return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
}
```
在Executor的query()方法中，首先会去调用MappedStatement的getBoundSql()方法对动态SQL进行解析，在SQL解析的时候，只是按照if、where等这些标签把它们解析成了一个个的SqlNode，因为不知道参数值，所以不知道哪些节点需要保留

而getBoundSql()方法会把方法请求的参数传进去，有了参数值，就可以对这些SqlNode进行解析，最后生成一个完成的SQL。

#### 4.3.1 拼接SQL
进入MappedStatement.getBoundSql()方法：
```text
  public BoundSql getBoundSql(Object parameterObject) {
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
    if (parameterMappings == null || parameterMappings.isEmpty()) {
      boundSql = new BoundSql(configuration, boundSql.getSql(), parameterMap.getParameterMappings(), parameterObject);
    }

    // check for nested result maps in parameter mappings (issue #30)
    for (ParameterMapping pm : boundSql.getParameterMappings()) {
      String rmId = pm.getResultMapId();
      if (rmId != null) {
        ResultMap rm = configuration.getResultMap(rmId);
        if (rm != null) {
          hasNestedResultMaps |= rm.hasNestedResultMaps();
        }
      }
    }

    return boundSql;
  }
```
在getBoundSql()方法中，根据SQL配置解析时生成的SqlSource类型，调用它的getBoundSql()方法，动态SQL对应DynamicSqlSource。

进入DynamicSqlSource.getBoundSql()方法：
```text
  @Override
  public BoundSql getBoundSql(Object parameterObject) {
    DynamicContext context = new DynamicContext(configuration, parameterObject);
    // 处理责任链
    rootSqlNode.apply(context);
    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
    context.getBindings().forEach(boundSql::setAdditionalParameter);
    return boundSql;
  }
```
rootSqlNode为SqlSource的根节点，是一个MixedSqlNode，会去调用它的apply()方法。
进入
```text
  // MixedSqlNode
  public boolean apply(DynamicContext context) {
    contents.forEach(node -> node.apply(context));
    return true;
  }
```
MixedSqlNode下面会有多个SqlNode，就回去遍历所有SqlNode的apply()方法。

下面我们以if标签生成的SqlNode为例：
```text
public boolean apply(DynamicContext context) {
  if (evaluator.evaluateBoolean(test, context.getBindings())) {
    // 处理完<if 递归回去继续处理
    contents.apply(context);
    return true;
  }
  return false;
}
```
在if对应的SqlNode中，会判断它的test表达式是否为true，如果为true，会去调用它下面的SqlNode(and id=#{id})，
这SqlNode是一个TextSqlNode。
```text
public boolean apply(DynamicContext context) {
  GenericTokenParser parser = createParser(new BindingTokenParser(context, injectionFilter));
  context.appendSql(parser.parse(text));
  return true;
}

public void appendSql(String sql) {
    sqlBuilder.add(sql);
}

private final StringJoiner sqlBuilder = new StringJoiner(" ");
```
在TextSqlNode中，会调用解析器的parse()方法判断方法传进来的参数中是否有这么一个名字的参数，
最后把TextSqlNode对应的内容通过appliedSql()方法进行拼接。

注：所有if、where等对应的动态节点，它们最底层的节点一定是TextSqlNode或者StaticTextSqlNode，
会根据前面节点解析表达式的结果来决定是否拼接TextSqlNode或StaticTextSqlNode的内容。

#### 4.3.2 替换参数
在getBoundSql()方法中，将SqlNode拼接成完整的SQL语句之后，就会调用SqlSource解析器来解析SQL中的#{}占位符
```text
public BoundSql getBoundSql(Object parameterObject) {
  DynamicContext context = new DynamicContext(configuration, parameterObject);
  // 1、责任链 处理一个个SqlNode 编译出一个完整sql
  rootSqlNode.apply(context);
  SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
  
  // 2、接下来处理 处理sql中的#{...}
  Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
  // 怎么处理呢？很简单，就是拿到#{}中的内容 封装为parameterMapper，替换成?
  SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
  
  BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
  context.getBindings().forEach(boundSql::setAdditionalParameter);
  return boundSql;
}
```
在SqlSourceBuilder的parse()方法中，首先会创建一个ParameterMappingTokenHandler类型的处理器，
它负责把#{}占位符里面的参数解析成一个ParameterMapping对象，这一步是在ParameterMappingTokenHandler处理器的parse()方法中完成，
调用parse()同时会把#{}替换成?占位符，然后返回替换后的SQL语句，此时的SQL语句与原生JDBC的SQL就没什么两样了，
然后封装成一个新的StaticSqlSource对象。
```text
  public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
    ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType,
        additionalParameters);
    // 替换sql中的#{}  替换成问号， 并且会顺便拿到#{}中的参数名解析成ParameterMapping
    GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
    String sql;
    if (configuration.isShrinkWhitespacesInSql()) {
      sql = parser.parse(removeExtraWhitespaces(originalSql));
    } else {
      sql = parser.parse(originalSql);
    }
    
    return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
  }
```
然后在调用getBoundSql把方法参数也设置进去，得到一个新的BoundSql对象并返回。

### 4.4 执行查询

#### 4.4.1 查询二级缓存
我们再回到最前面CachingExecutor的query()方法，前面我们已经根据参数把动态SQL解析成了一个静态的SQL了，
接下来会根据SQL的信息生成一个二级缓存对应的KEY–CacheKey实例，这个CacheKey的id就是SQL对应的Mapper接口的全限定名。
```text
public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
  // 【重要】将SQL解析成真实发送到数据库执行的SQL
  BoundSql boundSql = ms.getBoundSql(parameterObject);
  CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
  // 【重要】实际执行SQL
  return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
}
```
最后就是去调用重载的CachingExecutor.query()方法进行查询：
```text
public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, 
        ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
  // 判断mapper中是否开启了二级缓存<cache/>
  Cache cache = ms.getCache();
  if (cache != null) {
    // 判断是否需要刷新缓存
    flushCacheIfRequired(ms);
    if (ms.isUseCache() && resultHandler == null) {
      ensureNoOutParams(ms, boundSql);
      // 先去二级缓存中获取
      List<E> list = (List<E>) tcm.getObject(cache, key);
      // 二级缓存中没有获取到
      if (list == null) {
        // 【重要】通过查询数据库去查询
        list = delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
        // 加入到二级缓存中
        tcm.putObject(cache, key, list); // issue #578 and #116
      }
      return list;
    }
  }
  // 【重要】没有整合二级缓存，直接去数据库查询
  return delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
}
```
这个方法的功能就很明显了，因为当前的Executor是一个CachingExecutor，所以它会去判断是否开启了二级缓存。
如果开启了二级缓存，就先从二级缓存里面拿，拿不到再去查询数据库，最后再把查询出来的结果放入到二级缓存中；
如果没有开启二级缓存，就直接去查询数据库。

其中delegate属性就是被CachingExecutor包装的SimpleExecutor|ReuseExecutor|BatchExecutor中的某一个。

#### 4.4.2 查询数据库
在BaseExecutor.query()方法中，首先会去一级缓存中去拿，一级缓存没有才会去真正查询数据库，
从localCache这个成员变量也可以看出来，它位于Executor中，意味着它只是SqlSession级别的缓存，当SqlSession提交或关闭时，它就失效了。
```text
public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
  ErrorContext.instance().resource(ms.getResource()).activity("executing a query").object(ms.getId());
  // 已经关闭，则抛出 ExecutorException 异常
  if (closed) {
    throw new ExecutorException("Executor was closed.");
  }
  // <2> 清空本地缓存，如果 queryStack 为零，并且要求清空本地缓存。
  if (queryStack == 0 && ms.isFlushCacheRequired()) {
    clearLocalCache();
  }
  List<E> list;
  try {
    // <4.1> 从一级缓存中，获取查询结果
    queryStack++;
    list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
    // <4.2> 获取到，则进行处理
    if (list != null) {
      // 处理存过的
      handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
    } else {
      // 获得不到，则从数据库中查询
      list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }
  } finally {
    queryStack--;
  }
  ......
  return list;
}
```
在BaseExecutor.queryFromDatabase()方法去调用doQuery()方法执行SQL：
```text
private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds, 
        ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
  List<E> list;
  localCache.putObject(key, EXECUTION_PLACEHOLDER);
  try {
    // 【重要】执行查询
    list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
  } finally {
    localCache.removeObject(key);
  }
  localCache.putObject(key, list);
  if (ms.getStatementType() == StatementType.CALLABLE) {
    localOutputParameterCache.putObject(key, parameter);
  }
  return list;
}
```
以SimpleExecutor为准来看一下具体实现：
```text
  @Override
  public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
    Statement stmt = null;
    try {
      Configuration configuration = ms.getConfiguration();
      // 创建StatementHandler，此时的boundSql还有占位符?，比如：select * from user where id = ?
      StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds,
                                                    resultHandler, boundSql);
      // 【重要】拿到连接和statement，将SQL解析为真实执行的SQL，比如：select * from user where id = 1
      stmt = prepareStatement(handler, ms.getStatementLog());
      // 【重要】执行查询
      return handler.query(stmt, resultHandler);
    } finally {
        closeStatement(stmt);
    }
  }
```
首先创建一个StatementHandler的实例，在newStatementHandler()方法中，会去处理Statement类型的拦截器，
与处理Executor类型的拦截器逻辑一样，不复述了。
```text
  // Configuration类
  public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement,
      Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
    // 【重要】创建RoutingStatementHandler
    StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject,
        rowBounds, resultHandler, boundSql);
    // 【重要】执行Statement类型的拦截器
    return (StatementHandler) interceptorChain.pluginAll(statementHandler);
  }
```
然后在prepareStatement()方法中，会去获取数据库连接，创建statement对象并进行预处理，最后处理SQL语句的参数。
```text
  // SimpleExecutor类
  private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
    Statement stmt;
    Connection connection = getConnection(statementLog);
    // 【重要】通过获取的connection，去数据库 预编译SQL
    stmt = handler.prepare(connection, transaction.getTimeout());
    // 【重要】处理参数，将占位符?替换成参数值
    handler.parameterize(stmt);
    return stmt;
  }
```
根据参数类型拿到对应的参数类型处理器，调用参数处理器的setParameter()方法来设置参数。

以selectById(int id)方法为例，会去调用IntegerTypeHandler的setNonNullParameter()方法。
最后会去调用setNonNullParameter()方法设置参数，这就回到了我们数据的JDBC操作数据库的写法了。
```text
public void setNonNullParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType)
    throws SQLException {
  ps.setInt(i, parameter);
}
```
最后调用PreparedStatementHandler的query()方法，执行SQL，处理结果集。
```text
  @Override
  public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
    PreparedStatement ps = (PreparedStatement) statement;
    // 执行SQL
    ps.execute();
    // 【重要】处理结果集
    return resultSetHandler.handleResultSets(ps);
  }
```

