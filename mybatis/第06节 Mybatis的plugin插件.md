# 第06节 Mybatis的plugin插件

## 一、源码分析

### InterceptorChain 拦截器链
```text
1) XMLConfigBuilder会将解析mybatis-config.xml插件的结果存储到该类中，也就是字段interceptors里面
2) SqlSession四大组件会通过pluginAll()注册拦截器
```
```java
public class InterceptorChain {
  
  // 自定义拦截器集合  这里面包含的是我们在mybatis-config里面定义的插件
  private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

  // 通知所有拦截器 对target进行个性化包装
  // 默认的是对DefaultSqlSession四大组件进行包装
  public Object pluginAll(Object target) {
    for (Interceptor interceptor : interceptors) {
      // 不断对target用拦截器包装在包装
      target = interceptor.plugin(target);
    }
    return target;
  }

  // 添加
  public void addInterceptor(Interceptor interceptor) {
    interceptors.add(interceptor);
  }
  
  // 返回
  public List<Interceptor> getInterceptors() {
    // 不可修改
    return Collections.unmodifiableList(interceptors);
  }

}
```

### Interceptor 拦截器接口
插件需要实现该接口，才能实现拦截
```java
public interface Interceptor {

  // 拦截  Invocation包含拦截的对象信息
  Object intercept(Invocation invocation) throws Throwable;

  // 包装目标对象 并且返回对象 用于进行
  Object plugin(Object target);

  // 将插件注册时的property属性设置进来
  void setProperties(Properties properties);

}

```

### Invocation 拦截对象信息
拦截器就类似于是AOP切面，我们需要添加切面代码的时候，需要做两件事
```text
调用原方法时传递的参数，需要传递到切面方法
切面代码处理以后，需要在继续调用原方法
```
该类就主要实现了这两个功能，封装参数，并且回调原方法
```text
public class Invocation {
  // 目标对象
  private final Object target;
  // 方法
  private final Method method;
  // 参数
  private final Object[] args;

  public Invocation(Object target, Method method, Object[] args) {
    this.target = target;
    this.method = method;
    this.args = args;
  }

  public Object getTarget() {
    return target;
  }

  public Method getMethod() {
    return method;
  }

  public Object[] getArgs() {
    return args;
  }

  // 反射调用方法  并且返回结果
  // 当通过插件处理后 类似于AOP切面  然后通过该方法继续执行原方法
  public Object proceed() throws InvocationTargetException, IllegalAccessException {
    return method.invoke(target, args);
  }

}

```
### @Intercepts 拦截配置
用于设置拦截器需要拦截的方法，对应的value是一个数组，一个值对应的就是一个Signature
```text
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercepts {
  // 数组 一个Signature对应一个需要拦截的内容
  Signature[] value();
}
```

### @Signature 拦截类签名
用来通知Mybatis 我的拦截器要拦截哪个对象的哪个方法
```text
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Signature {
  
  // 拦截对象的类型
  Class<?> type();

  // 拦截对象的方法
  String method();

  // 拦截对象的参数
  Class<?>[] args();
}
```

### 注解使用示例
```text
// 注解需要拦截的类
@Intercepts({
   	    // 第一个需要拦截的
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        // 第二个需要拦截的
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
// 实现接口
public class LogInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // ... 中间可以编写代码 进行AOP处理
        // proceed() 通过反射继续调用方法
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 封装插件 返回
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
```
### Plugin 插件(重要)
```text
通过wrap()创建代理
通过invoke()方法进行请求转发
```
```text
// 该类实现了 InvocationHandler 因为下面通过代理  
public class Plugin implements InvocationHandler {

  // 目标对象 也就是需要拦截的对象 默认为SqlSession四大组件
  // 如果有多个自定义拦截器，
  private final Object target;
  // 拦截器对象
  private final Interceptor interceptor;
  // 目标类--方法集  需要拦截的方法
  private final Map<Class<?>, Set<Method>> signatureMap;

  private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
    this.target = target;
    this.interceptor = interceptor;
    this.signatureMap = signatureMap;
  }

  // 根据 interceptor 
  public static Object wrap(Object target, Interceptor interceptor) {
    // 获取到注册拦截的Map  
    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
    Class<?> type = target.getClass();
    // 获取注册所有接口
    Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
    if (interfaces.length > 0) {
      // 重点在这里  创建代理
      // 调用该接口中的方法
      // 处理函数为创建了一个新的Plugin
      return Proxy.newProxyInstance(
          type.getClassLoader(),
          interfaces,
          new Plugin(target, interceptor, signatureMap));
    }
    // 如果没有接口 那么返回的就是自己
    return target;
  }

  // 反射
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      // 获取声明method的类的指定方法集
      // 也就是通过 @Intercepts 注解设置拦截的方法 
      Set<Method> methods = signatureMap.get(method.getDeclaringClass());
      // 判断该方法 是否在拦截器中注册
      // 因为通过代理 接口中所有的方法被调用 都会代理到这里  那么就需要判断哪些方法注册了拦截器
      if (methods != null && methods.contains(method)) {
        // 通过就代表注册了   执行拦截器的intercept方法
        return interceptor.intercept(new Invocation(target, method, args));
      }
      // 运行到这里 代表调用了接口中的方法 但是该接口没有注册拦截器 那只能正常调用
      return method.invoke(target, args);
    } catch (Exception e) {
      throw ExceptionUtil.unwrapThrowable(e);
    }
  }

  // 将 自定义拦截器 的配置 @Intercepts 解析
  // 返回一个Map  key是拦截的类 value是需要拦截的方法
  private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
    // 获取 自定义拦截器的 @Intercepts 注解
    Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
    // 注解为空 直接抛异常
    if (interceptsAnnotation == null) {
      throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());      
    }
    // 获取所有 @Signature 注解
    Signature[] sigs = interceptsAnnotation.value();
    // 新建Map 用于存储拦截目标对象类,以及拦截目标对象类的方法集
    Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
    for (Signature sig : sigs) {
      // 获取Signature注解中指定的拦截目标对象类对应的方法集
      Set<Method> methods = signatureMap.get(sig.type());
      if (methods == null) {
        methods = new HashSet<Method>();
        signatureMap.put(sig.type(), methods);
      }
      try {
        // 获取拦截目标对象类中对应Signature注解配置方法和参数类型的方法对象
        Method method = sig.type().getMethod(sig.method(), sig.args());
        methods.add(method);
      } catch (NoSuchMethodException e) {
        throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
      }
    }
    // 经过处理 也就是Map里面将这个类需要拦截的Method存进去
    return signatureMap;
  }

  // 获取到所有
  private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
    Set<Class<?>> interfaces = new HashSet<Class<?>>();
    while (type != null) {
      // 遍历所有实现的接口 遍历
      for (Class<?> c : type.getInterfaces()) {
        // 判断该接口是否需要拦截
        if (signatureMap.containsKey(c)) {
          interfaces.add(c);
        }
      }
      // 获取父类
      type = type.getSuperclass();
    }
    return interfaces.toArray(new Class<?>[interfaces.size()]);
  }

}
```

## 二、Mybatis拦截器注册流程
### 1.四大组件注册拦截器链
SqlSession的四大组件会默认注册拦截器链，四大组件的注册会在Configuration里面进行
```text
public class Configuration {
    
  // 创建拦截器链
  protected final InterceptorChain interceptorChain = new InterceptorChain();
    
  // 创建参数处理器
  public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
    ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
    // ParameterHandler 添加到拦截器链
    parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
    return parameterHandler;
  }

  // 创建结果集处理器
  public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler,
      ResultHandler resultHandler, BoundSql boundSql) {
    ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
    // ResultSetHandler 添加到拦截器链
    resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
    return resultSetHandler;
  }

  // 创建Statement处理器
  public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
    StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
    // StatementHandler 添加到拦截器链
    statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
    return statementHandler;
  }

  public Executor newExecutor(Transaction transaction) {
    return newExecutor(transaction, defaultExecutorType);
  }

  // 创建执行器
  public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    executorType = executorType == null ? defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Executor executor;
    if (ExecutorType.BATCH == executorType) {
      executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
      executor = new ReuseExecutor(this, transaction);
    } else {
      executor = new SimpleExecutor(this, transaction);
    }
    if (cacheEnabled) {
      executor = new CachingExecutor(executor);
    }
    // Executor 添加到拦截器链
    executor = (Executor) interceptorChain.pluginAll(executor);
    return executor;
  }
}

```

### 2. mybaits-config.xml配置拦截器
```text
<plugins>
    <plugin interceptor="com.jianan.springtest.plugin.LogInterceptor"/>
    <plugin interceptor="com.jianan.springtest.plugin.PageInterceptor"/>
</plugins>
```

## 三、插件示例
### 1. SQL日志
```text
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class LogInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
        logger.info("h3c--mapper:" + mappedStatement.getId());
        logger.info("h3c--sql:" + boundSql.getSql());
        logger.info("h3c--parameter:" + invocation.getArgs()[1].toString());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
```

### 2.分页
```text
public class PageUtil {

	// Page通过ThreadLocal传递
	static final ThreadLocal<Page> THREAD_LOCAL_PAGE = new ThreadLocal<>();

	public static Page startPage(Integer page, Integer size) {
		Page pageResult = new Page();
		pageResult.setPage(page);
		pageResult.setSize(size);
		THREAD_LOCAL_PAGE.set(pageResult);
		return pageResult;
	}

	public static Page localPage() {
		return THREAD_LOCAL_PAGE.get();
	}

	public static void remove() {
		THREAD_LOCAL_PAGE.remove();
	}
}

// 拦截StatementHandler类的prepare方法
@Intercepts({@Signature(
		type = StatementHandler.class,
		method = "prepare",
		args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {
	private Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Page page = PageUtil.localPage();
		// 没有调用PageUtil.startPage()，则不分页
		if (page == null) {
			return invocation.proceed();
		}
		
		// 【取出原sql，根据page拼接分页sql】
		RoutingStatementHandler target = (RoutingStatementHandler) invocation.getTarget();
		BoundSql boundSql = target.getBoundSql();
		String sql = boundSql.getSql();
		int limit = (page.getPage() - 1) * page.getSize();
		ReflectUtil.setFieldValue(boundSql, "sql", sql + " limit " + limit + "," + page.getSize());
		
		// 删除当前线程的page实例
		PageUtil.remove();
		// 执行目标方法
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
```
