# 第02节 Mybatis 配置文件解析

## 一、Mybatis配置

### 1.1 配置全局文件
在使用MyBatis的时候，我们首先需要配置一个全局配置文件，在这个配置文件中，我们可以配置MyBatis的属性、数据源、插件、别名、基础设置以及SQL配置文件的路径等。

其中在<mappers>标签中，可以定义SQL配置文件的信息，
Mybatis提供了四种配置SQL文件的方式，但第四种我们通常都不会用，
而第一种和第三种，指定包路径或Mapper接口路径的用法，都需要xml的文件名和接口名一样；
而第二种注解指定xml，不需要一样，是因为在xml中的namespace属性可以指定xml对用的接口是哪一个。

基础配置如下：
```text
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--properties 扫描属性文件.properties  -->
    <properties resource="db.properties"></properties>

    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>

    <typeAliases>
        <typeAlias alias="TestEntity" type="org.example.entity.TestEntity"/>
    </typeAliases>

    <plugins>
        <plugin interceptor="org.example.plugins.ExamplePlugin" >
        </plugin>
    </plugins>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <!--//  mybatis内置了JNDI、POOLED、UNPOOLED三种类型的数据源,其中POOLED对应的实现为org.apache.ibatis.datasource.pooled.PooledDataSource,它是mybatis自带实现的一个同步、线程安全的数据库连接池 一般在生产中,我们会使用c3p0或者druid连接池-->
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${userName}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <!--1.必须保证接口名（例如ITestMapper）和xml名（ITestMapper.xml）相同，还必须在同一个包中-->
        <package name="org.example.mapper"/>
        <!--2.不用保证同接口同包同名-->
        <mapper resource="org/example/mapper/ITestMapper.xml"/>
        <!--3.保证接口名（例如ITestMapper）和xml名（ITestMapper.xml）相同，还必须在同一个包中-->
        <mapper class="org.example.mapper.ITestMapper"/>
        <!--4.不推荐:引用网路路径或者磁盘路径下的sql映射文件 file:///var/mappers/ITestMapper.xml-->
        <mapper url="file:E:/Study/myeclipse/_03_Test/src/cn/sdut/pojo/ITestMapper.xml"/>
    </mappers>
</configuration>
```
db.properties:
```text
url=jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
driver=com.mysql.cj.jdbc.Driver
userName=root
password=root
```

### 1.2 配置SQL文件
在上面，我们使用的是通过<package>标签来指定SQL配置，所以SQL配置文件的名称和Mapper接口的名称必须保持一致

SQL配置文件：
```text
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.example.mapper.ITestMapper">
    <select id="selectTestById" resultType="TestEntity">
        select * from t_test where id = #{id}
    </select>
</mapper>
```
Mapper接口：
```java
public interface ITestMapper {

    TestEntity selectTestById(int id);
}

public class TestEntity {

    private int id;

    private String name;

    public String getName() {
        return this.name;
    }
}
```

### 1.3 使用Mybatis
上面已经完成了Mybatis的基本配置，下面就可以直接使用了，
首先就是去加载配置文件，生成一个SqlSessionFactory，然后再根据SqlSessionFactory创建一个SqlSession，
然后通过动态代理的方式获取Mapper接口的代理对象，然后就是调用具体的方法。
Mybatis最核心的东西就包括两部分内容：扫描配置类和数据操作，后面会有文件详解介绍Mybatis数据操作的流程，
这边文章主要介绍Mybatis是再配置类扫描时，都做了什么，最后生成一个SqlSessionFactory。
```java
public class mybatisApp {
    public static void main(String[] args) {
        Reader resourceAsReader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resourceAsReader);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        ITestMapper testMapper = sqlSession.getMapper(ITestMapper.class);
        TestEntity testEntity = testMapper.selectTestById(1);
        System.out.println("=============================" + testEntity.getName());
    }
}
```

## 二、解析全局配置文件
通过使用Mybatis的代码可以看出，主要是通过SqlSessionFactoryBuilder类的build()方法来实现的，
接下来我们详细看下这个build()方法是如何解析的。

在Mybatis中，提供了好多种解析类，它们都是继承自BaseBuilder类，在BaseBuilder类中，有一个Configuration属性，
它就是用来存放Mybatis所有的配置信息。

全局配置文件使用XMLConfigBuilder来解析，调用parse()进行解析返回一个Configuration对象，
然后再调用build()方法生成一个SqlSessionFactory，我们重点看parse()方法是如何解析的。
```text
  public SqlSessionFactory build(Reader reader) {
    return build(reader, null, null);
  }
  
  public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }
  
  public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
  }
```
需要注意的是，Mybatis会把xml配置文件解析成一个XNode的类型，这个类型是Mybatis内部定义的，就不具体去看了，
调用解析器的evalNode()方法，把<configuration></configuration>标签解析成一个XNode对象，
然后调用XMLConfigBuilder.parseConfiguration()解析XNode对象的信息。
```text
  public Configuration parse() {
    // 若已经解析过了 就抛出异常
    if (parsed) {
      throw new BuilderException("Each XMLConfigBuilder can only be used once.");
    }
    // 设置解析标志位
    parsed = true;
    // 解析我们的mybatis-config.xml的节点 <configuration></configuration>
    parseConfiguration(parser.evalNode("/configuration"));
    return configuration;
  }
```
在XMLConfigBuilder.parseConfiguration()方法中，就是按顺序来对各个节点进行解析了，下面详细介绍各个节点的解析：
```text
  private void parseConfiguration(XNode root) {
    try {
      // issue #117 read properties first
      propertiesElement(root.evalNode("properties"));
      Properties settings = settingsAsProperties(root.evalNode("settings"));
      /**
       * 基本没有用过该属性
       * VFS含义是虚拟文件系统；主要是通过程序能够方便读取本地文件系统、FTP文件系统等系统中的文件资源。
       * Mybatis中提供了VFS这个配置，主要是通过该配置可以加载自定义的虚拟文件系统应用程序
       * 解析到：org.apache.ibatis.session.Configuration#vfsImpl
       */
      loadCustomVfs(settings);
      loadCustomLogImpl(settings);
      typeAliasesElement(root.evalNode("typeAliases"));
      pluginElement(root.evalNode("plugins"));
      objectFactoryElement(root.evalNode("objectFactory"));
      objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
      reflectorFactoryElement(root.evalNode("reflectorFactory"));
      settingsElement(settings);
      // read it after objectFactory and objectWrapperFactory issue #631
      environmentsElement(root.evalNode("environments"));
      databaseIdProviderElement(root.evalNode("databaseIdProvider"));
      typeHandlerElement(root.evalNode("typeHandlers"));
      mapperElement(root.evalNode("mappers"));
    } catch (Exception e) {
      throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
    }
  }
```

### 2.1 属性解析
将<properties/>标签解析成一个XNode节点，<properties/>提供了三种配置属性的方式，分别是：
```text
<properties resource="db.properties"></properties>
<properties url="http://www.baidu.com/db.properties"></properties>
<properties>
<property name="password" value="root"/>
</properties>
```
针对这三种方式的属性配置，Mybatis通过不同的方法来解析，调用getChildrenAsProperties()来解析第三种配置，
就是首先获取<properties/>下的子节点，然后把子节点的name和value属性拿出来，作为Properties的key和value；
而第一种和第三种方式，都是先获取文件或URL的输入流，然后调用Properties的load()方法来加载。

最后把这些属性加入到当前的解析器和configuration属性中。
进入XMLConfigBuilder.propertiesElement()方法：
```text
/**
 * 解析 properties节点
 *     <properties resource="mybatis/db.properties" />
 *     解析到org.apache.ibatis.parsing.XPathParser#variables
 *           org.apache.ibatis.session.Configuration#variables
 */
propertiesElement(root.evalNode("properties"));

private void propertiesElement(XNode context) throws Exception {
    if (context != null) {
        Properties defaults = context.getChildrenAsProperties();
        String resource = context.getStringAttribute("resource");
        String url = context.getStringAttribute("url");

        if (resource != null) {
            defaults.putAll(Resources.getResourceAsProperties(resource));
        } else if (url != null) {
            defaults.putAll(Resources.getUrlAsProperties(url));
        }
        Properties vars = configuration.getVariables();
        if (vars != null) {
            defaults.putAll(vars);
        }
        parser.setVariables(defaults);
        configuration.setVariables(defaults);
    }
}
```

### 2.2 基础设置解析
settings节点下的配置，都是Mybatis提供的，如果没有配置，Mybatis会使用默认的配置，这一步只是把检验配置是否正确，
然后把配置存在settings属性中，后面才会把这些属性设置到Configuration属性当中。
```text
/**
* 解析我们的mybatis-config.xml中的settings节点
* 具体可以配置哪些属性:http://www.mybatis.org/mybatis-3/zh/configuration.html#settings
* <settings>
*   <setting name="cacheEnabled" value="true"/>
*   <setting name="lazyLoadingEnabled" value="true"/>
*   <setting name="mapUnderscoreToCamelCase" value="false"/>
*   <setting name="localCacheScope" value="SESSION"/>
*   <setting name="jdbcTypeForNull" value="OTHER"/>
*    ..............
* </settings>
*
*/
Properties settings = settingsAsProperties(root.evalNode("settings"));
```
检验配置参数是否正确，localReflectorFactory是ReflectorFactory的一个实例，ReflectorFactory可以根据传入的Class类型，
调用findForClass()生成一个Reflector实例，而Reflector实例中包含了该类的类型，可以参数名，可写参数名，
以及所有的getter/setter方法。

MetaClass对象里面就包含了ReflectorFactory和Reflector两个实例，所以遍历所有的配置，
然后判断Configuration类里面是否有这些属性的setter方法，判断逻辑也很简单，就是在创建Reflector实例的时候，
会把所有只有一个参数且方法名是以set开始的方法都拿出来，然后截取set后面的字符串，同时把首字母变成小写，
然后就存在Reflector的setMethods属性中，key就是转换后的方法名，value是把方法封装成了一个MethodInvoker对象，
方便通过反射直接调用方法。
```text
private Properties settingsAsProperties(XNode context) {
  if (context == null) {
    return new Properties();
  }
  Properties props = context.getChildrenAsProperties();
  // Check that all settings are known to the configuration class
  // 其实就是去configuration类里面拿到所有setter方法， 看看有没有当前的配置项
  MetaClass metaConfig = MetaClass.forClass(Configuration.class, localReflectorFactory);
  for (Object key : props.keySet()) {
    if (!metaConfig.hasSetter(String.valueOf(key))) {
        throw new BuilderException("The setting " + key + " is not known.  Make sure you spelled it correctly (case sensitive).");
    }
  }
  return props;
}
```
```text
public class Reflector {

    private final Class<?> type;
    private final String[] readablePropertyNames;
    private final String[] writablePropertyNames;
    private final Map<String, Invoker> setMethods = new HashMap<>();
    private final Map<String, Invoker> getMethods = new HashMap<>();
    private final Map<String, Class<?>> setTypes = new HashMap<>();
    private final Map<String, Class<?>> getTypes = new HashMap<>();
    private Constructor<?> defaultConstructor;
}
```
这一步只是对配置项进行了校验，以及缓存属性值，而在后面的方法中，会直接调用configuration的set方法来赋值。
```text
// 设置settings 和默认值到configuration
settingsElement(settings);

private void settingsElement(Properties props) {
  configuration.setAutoMappingBehavior(AutoMappingBehavior.valueOf(props.getProperty("autoMappingBehavior", "PARTIAL")));
  configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.valueOf(props.getProperty("autoMappingUnknownColumnBehavior", "NONE")));
  configuration.setCacheEnabled(booleanValueOf(props.getProperty("cacheEnabled"), true));
  ......
}
```

### 2.3 日志配置
我们可以通过logImpl来配置Log的实现类，可以是具体的类，也可以是类的别名。
首先就会从别名注册器中直接去拿，如果没有，再通过Class.forName()方法来加载该类，然后设置到configuration中。
```text
  /**
  * 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。
  * SLF4J | LOG4J | LOG4J2 | JDK_LOGGING | COMMONS_LOGGING | STDOUT_LOGGING | NO_LOGGING
  * 解析到org.apache.ibatis.session.Configuration#logImpl
  */
  loadCustomLogImpl(settings);

private void loadCustomLogImpl(Properties props) {
  Class<? extends Log> logImpl = resolveClass(props.getProperty("logImpl"));
  configuration.setLogImpl(logImpl);
}
```

### 2.4 别名配置
在Configuration类实例化的时候，就回去实例化TypeAliasRegistry(别名处理器)、TypeHandlerRegistry(类型处理器)、
MapperRegistry(mapper接口注册器)等等这些属性，而这些属性在实例化的时候都有一些默认的值，以别名处理器TypeAliasRegistry为例，
就会添加默认的一些别名：
```text
  /**
  * mybaits对我们默认的别名支撑
  */
  public TypeAliasRegistry() {
    registerAlias("string", String.class);
  
    registerAlias("byte", Byte.class);
    registerAlias("long", Long.class);
    ......
  }

public void registerAlias(String alias, Class<?> value) {
  if (alias == null) {
    throw new TypeException("The parameter alias cannot be null");
  }
  // issue #748
  String key = alias.toLowerCase(Locale.ENGLISH);
  if (typeAliases.containsKey(key) && typeAliases.get(key) != null && !typeAliases.get(key).equals(value)) {
    throw new TypeException("The alias '" + alias + "' is already mapped to the value '" + typeAliases.get(key).getName() + "'.");
  }
  typeAliases.put(key, value);
}
```
但是在使用Mybatis的时候，也可以通过<typeAliases></typeAliases>节点来配置别名。

该节点提供了两种配置别名的方式，如果是通过<package>来指定某个类包，则会去遍历包下面所有类，
除了接口、匿名类、内部类等，其他都会去加载，然后缓存在别名解析器中。

如果是通过alias和type配置别名，就直接加载type指定的类。

对于这两种配置方式，如果没有指定别名的名称，Mybatis首先会去看类上是否有@Alias注解，把它的value()值最为别名，
否则就调用Class类的getSimpleName()方法，返回值作为别名。

最后就是把别名和对应的类注册到configuration属性的TypeAliasRegistry属性中即可。
```text
/**
* 解析我们的别名
* <typeAliases>
    <typeAlias alias="Author" type="cn.tulingxueyuan.pojo.Author"/>
  </typeAliases>
  <typeAliases>
    <package name="cn.tulingxueyuan.pojo"/>
  </typeAliases>
  解析到oorg.apache.ibatis.session.Configuration#typeAliasRegistry.typeAliases
  除了自定义的，还有内置的
 */
typeAliasesElement(root.evalNode("typeAliases"));

// 注册别名
private void typeAliasesElement(XNode parent) {
  if (parent != null) {
    for (XNode child : parent.getChildren()) {
      if ("package".equals(child.getName())) {
        String typeAliasPackage = child.getStringAttribute("name");
        configuration.getTypeAliasRegistry().registerAliases(typeAliasPackage);
      } else {
        String alias = child.getStringAttribute("alias");
        String type = child.getStringAttribute("type");
        Class<?> clazz = Resources.classForName(type);
        if (alias == null) {
            typeAliasRegistry.registerAlias(clazz);
        } else {
            typeAliasRegistry.registerAlias(alias, clazz);
        }
      }
    }
  }
}
```

### 2.5 插件配置
Mybatis中提供了【四大类型】的插件，分别作用于Executor、ParameterHandler、StatementHandler和ResultSetHandler，
Executor是一个执行器，SqlSession内部真正工作的就是一个Executor对象。

遍历plugins节点下所有子节点，获取这些子节点的属性值，创建一个拦截器的实例，然后把属性添加到实例中，
最后把这些拦截器添加到configuration属性的拦截器链中。
```text
/**
 * 解析我们的插件(比如分页插件)
 * mybatis自带的
 * Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
   ParameterHandler (getParameterObject, setParameters)
   ResultSetHandler (handleResultSets, handleOutputParameters)
   StatementHandler (prepare, parameterize, batch, update, query)
   解析到：org.apache.ibatis.session.Configuration#interceptorChain.interceptors
  */
  pluginElement(root.evalNode("plugins"));

private void pluginElement(XNode parent) throws Exception {
  if (parent != null) {
    for (XNode child : parent.getChildren()) {
      String interceptor = child.getStringAttribute("interceptor");
      Properties properties = child.getChildrenAsProperties();
      Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).getDeclaredConstructor().newInstance();
      interceptorInstance.setProperties(properties);
      configuration.addInterceptor(interceptorInstance);
    }
  }
}

public void addInterceptor(Interceptor interceptor) {
  interceptorChain.addInterceptor(interceptor);
}
```
我们通过实现Interceptor接口来定义一个拦截器，但在类上面，需要使用@Intercepts注解来表明该拦截器的类型，以及需要拦截的方法。
```text
@Intercepts({
    @Signature( type= Executor.class,  method = "query", args ={
        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
    })
})
public class ExamplePlugin implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("代理");
        Object[] args = invocation.getArgs();
        MappedStatement ms= (MappedStatement) args[0];
        // 执行下一个拦截器、直到尽头
        return invocation.proceed();
    }

}
```

### 2.6 数据源环境配置
在配置文件中，可以配置多个数据源，但只有一个会生效，也就是environments的default指定的一个才会生效。
```text
/**
* 解析我们的mybatis环境
  <environments default="dev">
  <environment id="dev">
  <transactionManager type="JDBC"/>
  <dataSource type="POOLED">
  <property name="driver" value="${jdbc.driver}"/>
  <property name="url" value="${jdbc.url}"/>
  <property name="username" value="root"/>
  <property name="password" value="Zw726515"/>
  </dataSource>
  </environment>
  </environments>
*  解析到：org.apache.ibatis.session.Configuration#environment
*  在集成spring情况下由 spring-mybatis提供数据源 和事务工厂
   */
   environmentsElement(root.evalNode("environments"));
```

通过transactionManagerElement()方法来解析transactionManager节点，生成一个事务工厂，用于创建事务

然后通过dataSourceElement()方法解析dataSource节点，生成一个数据库连接工厂，然后调用getDataSource()方法获得数据库连接信息，
最后把这些信息，封装成一个Environment实例，设置到configuration中。
```text
private void environmentsElement(XNode context) throws Exception {
  if (context != null) {
    if (environment == null) {
        environment = context.getStringAttribute("default");
    }
    for (XNode child : context.getChildren()) {
      String id = child.getStringAttribute("id");
      if (isSpecifiedEnvironment(id)) {
        TransactionFactory txFactory = transactionManagerElement(child.evalNode("transactionManager"));
        DataSourceFactory dsFactory = dataSourceElement(child.evalNode("dataSource"));
        DataSource dataSource = dsFactory.getDataSource();
        Environment.Builder environmentBuilder = new Environment.Builder(id)
                                                        .transactionFactory(txFactory)
                                                        .dataSource(dataSource);
        configuration.setEnvironment(environmentBuilder.build());
      }
    }
  }
}
```
在解析数据库事务类型和连接池类型时，都会用别名去获取具体的类，以事务为例：

我们通常使用的时候，直接指定别名就行了，获得指定的type类型后，会调用resolveClass()方法来解析对应的Class，其实在Configuration类实例化的时候，可会注册一些别名
```text
private TransactionFactory transactionManagerElement(XNode context) throws Exception {
  if (context != null) {
    String type = context.getStringAttribute("type");  // JDBC
    Properties props = context.getChildrenAsProperties();
    TransactionFactory factory = (TransactionFactory) resolveClass(type).getDeclaredConstructor().newInstance();
    factory.setProperties(props);
    return factory;
  }
  throw new BuilderException("Environment declaration requires a TransactionFactory.");
}
```
```text
public Configuration() {
    typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
    typeAliasRegistry.registerAlias("MANAGED", ManagedTransactionFactory.class);

    typeAliasRegistry.registerAlias("JNDI", JndiDataSourceFactory.class);
    typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
    ......
}
```

### 2.7 数据库厂商配置
解析所有databaseIdProvider节点的属性，然后通过上面配置的数据源，创建一个数据库连接来获取数据库产品的名称(使用完就关掉)，
然后于节点的name属性匹配，将value值设置到configuration中。
```text
/**
* 解析数据库厂商
*     <databaseIdProvider type="DB_VENDOR">
         <property name="SQL Server" value="sqlserver"/>
         <property name="DB2" value="db2"/>
         <property name="Oracle" value="oracle" />
         <property name="MySql" value="mysql" />
      </databaseIdProvider>
*  解析到：org.apache.ibatis.session.Configuration#databaseId
   */
   databaseIdProviderElement(root.evalNode("databaseIdProvider"));

private void databaseIdProviderElement(XNode context) throws Exception {
    DatabaseIdProvider databaseIdProvider = null;
    if (context != null) {
        String type = context.getStringAttribute("type");
        // awful patch to keep backward compatibility
        if ("VENDOR".equals(type)) {
            type = "DB_VENDOR";
        }
        Properties properties = context.getChildrenAsProperties();
        databaseIdProvider = (DatabaseIdProvider) resolveClass(type).getDeclaredConstructor().newInstance();
        databaseIdProvider.setProperties(properties);
    }
    Environment environment = configuration.getEnvironment();
    if (environment != null && databaseIdProvider != null) {
        String databaseId = databaseIdProvider.getDatabaseId(environment.getDataSource());
        configuration.setDatabaseId(databaseId);
    }
}
```

### 2.8 类型处理器配置
类型处理器的用途就很多了，尤其是在处理查询的结果集的时候，就需要用到类型处理器，而Mybatis也给我们提供了很多内置的类型处理器，
都在Configuration的TypeHandlerRegistry实例中，在TypeHandlerRegistry实例化的时候，会添加默认的类型处理器。
```text
public TypeHandlerRegistry() {
    register(Boolean.class, new BooleanTypeHandler());
    register(boolean.class, new BooleanTypeHandler());
    ......
    register(String.class, JdbcType.CHAR, new StringTypeHandler());
    register(String.class, JdbcType.CLOB, new ClobTypeHandler());
    register(String.class, JdbcType.VARCHAR, new StringTypeHandler());
    ......
}
```
自定义类型转换器需要实现TypeHandler接口，并通过@MappedJdbcTypes和@MappedTypes两个注解来指定相互转换的类型
```text
@MappedTypes(JSONObject.class)
@MappedJdbcTypes(JdbcType.JSON)
public class JsonTypeHandler implements TypeHandler {
    ......
}
```
一般情况下，这些默认的类型处理器就够用了，但我们也可以自定义类型处理器，类型转换器的解析于别名配置的解析基本一样。
```text
/**
* 解析我们的类型处理器节点
* <typeHandlers>
     <typeHandler handler="org.mybatis.example.ExampleTypeHandler"/>
   </typeHandlers>
   解析到：org.apache.ibatis.session.Configuration#typeHandlerRegistry.typeHandlerMap
*/
typeHandlerElement(root.evalNode("typeHandlers"));

private void typeHandlerElement(XNode parent) {
  if (parent != null) {
    for (XNode child : parent.getChildren()) {
      if ("package".equals(child.getName())) {
        String typeHandlerPackage = child.getStringAttribute("name");
        typeHandlerRegistry.register(typeHandlerPackage);
      } else {
        String javaTypeName = child.getStringAttribute("javaType");
        String jdbcTypeName = child.getStringAttribute("jdbcType");
        String handlerTypeName = child.getStringAttribute("handler");
        Class<?> javaTypeClass = resolveClass(javaTypeName);
                        JdbcType jdbcType = resolveJdbcType(jdbcTypeName);
                        Class<?> typeHandlerClass = resolveClass(handlerTypeName);
        if (javaTypeClass != null) {
          if (jdbcType == null) {
            typeHandlerRegistry.register(javaTypeClass, typeHandlerClass);
          } else {
            typeHandlerRegistry.register(javaTypeClass, jdbcType, typeHandlerClass);
          }
        } else {
          typeHandlerRegistry.register(typeHandlerClass);
        }
      }
    }
  }
}
```

## 一、解析前操作
在上一篇文章《Mybatis配置文件解析(一)》中，介绍了Mybatis一些基础的解析，那些都是很基础的设置，MyBatis最重要的文件解析是SQL配置文件的解析，这篇文章将重点介绍Mybatis是如何解析SQL配置文件的

Mybatis中提供了四种配置SQL文件的方式，但最后一种用的比较少，主要是前面三种
```text
<mappers>
    <!--1.必须保证接口名（例如IUserDao）和xml名（IUserDao.xml）相同，还必须在同一个包中-->
    <package name="com.lizhi.mapper"/>

    <!--2.不用保证同接口同包同名-->
     <mapper resource="com/mybatis/mappers/EmployeeMapper.xml"/>

    <!--3.保证接口名（例如IUserDao）和xml名（IUserDao.xml）相同，还必须在同一个包中-->
    <mapper class="com.mybatis.dao.EmployeeMapper"/>

    <!--4.不推荐:引用网路路径或者磁盘路径下的sql映射文件 file:///var/mappers/AuthorMapper.xml-->
     <mapper url="file:E:/Study/myeclipse/_03_Test/src/cn/sdut/pojo/PersonMapper.xml"/>
</mappers>
```
Mybatis针对这四种不同的配置方式，提供了不同的解析方式，但是在对xml文件的解析是一样，
都会调用XMLMapperBuilder的parse()方法来进行解析，下面我们主要介绍通过package方式配置的SQL文件，这方配置方式的解析是最复杂的。
```text
/**
* package
*     ·解析mapper接口代理工厂（传入需要代理的接口） 解析到：org.apache.ibatis.session.Configuration#mapperRegistry.knownMappers
      ·解析mapper.xml  最终解析成MappedStatement 到：org.apache.ibatis.session.Configuration#mappedStatements
*/
mapperElement(root.evalNode("mappers"));

private void mapperElement(XNode parent) throws Exception {
    if (parent != null) {
        // 获取mappers节点下所有的mapper节点
        for (XNode child : parent.getChildren()) {
            // 判断我们mapper是不是通过批量注册的 <package name="com.lizhi.mapper"></package>
            if ("package".equals(child.getName())) {
                String mapperPackage = child.getStringAttribute("name");
                configuration.addMappers(mapperPackage);
            } else {
                // 判断从classpath下读取我们的mapper <mapper resource="mybatis/mapper/EmployeeMapper.xml"/>
                String resource = child.getStringAttribute("resource");
                // 判断是不是从我们的网络资源读取(或者本地磁盘得) <mapper url="D:/mapper/EmployeeMapper.xml"/>
                String url = child.getStringAttribute("url");
                // 解析这种类型(要求接口和xml在同一个包下) <mapper class="com.tuling.mapper.DeptMapper"></mapper>
                String mapperClass = child.getStringAttribute("class");

                // 我们得mappers节点只配置了 <mapper resource="mybatis/mapper/EmployeeMapper.xml"/>
                if (resource != null && url == null && mapperClass == null) {
                    ErrorContext.instance().resource(resource);
                    InputStream inputStream = Resources.getResourceAsStream(resource);
                    XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
                    mapperParser.parse();
                } else if (resource == null && url != null && mapperClass == null) {
                    ErrorContext.instance().resource(url);
                    InputStream inputStream = Resources.getUrlAsStream(url);
                    XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, url, configuration.getSqlFragments());
                    mapperParser.parse();
                } else if (resource == null && url == null && mapperClass != null) {
                    Class<?> mapperInterface = Resources.classForName(mapperClass);
                    configuration.addMapper(mapperInterface);
                } else {
                    throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
                }
            }
        }
    }
}
```

### 1.1 遍历所有Mapper接口
下面，我们重点看一下Configuration的addMappers()方法，在该方法中会去调用MapperRegistry类的addMappers()，
在该方法中，会根据指定的包名，得到包下面所有的class文件，然后加载这些Class文件，最后遍历所有的Class文件，
调用MapperRegistry的addMapper()方法进行解析。
```text
public void addMappers(String packageName) {
    mapperRegistry.addMappers(packageName);
}

// MapperRegistry类方法
public void addMappers(String packageName) {
    addMappers(packageName, Object.class);
}

public void addMappers(String packageName, Class<?> superType) {
    // 根据包找到所有类
    ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
    resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
    Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
    // 循环所有的类
    for (Class<?> mapperClass : mapperSet) {
        addMapper(mapperClass);
    }
}
```
在上面，最核心的addMapper()方法位于MapperRegistry类中，那么MapperRegistry又是干什么的呢，我们看下它的源码就一目了然了。
该类最重要属性就是knownMappers，它记录了每个Mapper接口，以及为这个接口生成代理对象的代理工厂MapperProxyFactory，
我们继续往下面看，在解析的时候，就会去为每个接口生成代理工厂。
```text
public class MapperRegistry {

    private final Configuration config;
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }
}
```

### 1.2 生成代理工厂
在addMapper()方法中，首先会判断该类是否为接口，只有接口才会进行下一步解析。

然后会生成一个MapperProxyFactory实例的代理工厂放入到knownMappers，然后生成一个MapperAnnotationBuilder对象，
来解析对应的xml文件和接口方法的注解。
```text
public <T> void addMapper(Class<T> type) {
    // 判断我们传入进来的type类型是不是接口
    if (type.isInterface()) {
        // 判断我们的缓存中有没有该类型
        if (hasMapper(type)) {
            throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
        }
        boolean loadCompleted = false;
        try {
            // 创建一个MapperProxyFactory 把我们的Mapper接口保存到工厂类中， 该工厂用于创建 MapperProxy
            knownMappers.put(type, new MapperProxyFactory<>(type));
            // mapper注解构造器
            MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
            // 进行解析, 将接口完整限定名作为xml文件地址去解析
            parser.parse();
            loadCompleted = true;
        } finally {
            if (!loadCompleted) {
                knownMappers.remove(type);
            }
        }
    }
}
```
代理工厂具体的用途，我们通过看MapperProxyFactory源码可以看出来，其中mapperInterface属性存的是接口的Class对象，
然后methodCache中的MapperMethod里面是对方法信息的封装，
包括方法全限定名、该方法的操作的SQL类型(insert|update|delete|select)以及方法的签名这些信息。
```text
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        // 创建我们的代理对象
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        // 创建我们的Mapper代理对象返回
        return newInstance(mapperProxy);
    }

}
```
其中第一个newInstance(MapperProxy mapperProxy)方法用于创建Mapper接口的代理对象，
第二个newInstance(SqlSession sqlSession)方法是供外部使用的，
我们通过SqlSession的getMapper()方法调用时，就会调用到该方法，其中MapperProxy实现了InvocationHandler接口，
所以在方法调用时，会调用到MapperProxy的invoke()方法，具体的调用流程在后面的文章详细介绍。

简单介绍一下SqlSession的getMapper()方法，以DefaultSqlSession类为例。

会从configuration的mapperRegistry中，根据接口类型，把扫描时生成的MapperProxyFactory拿出来，
然后调用它的newInstance(sqlSession)方法来创建代理对象。
```text
public <T> T getMapper(Class<T> type) {
    return configuration.getMapper(type, this);
}

public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    return mapperRegistry.getMapper(type, sqlSession);
}

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

## 二、解析SQL配置文件
创建完Mapper接口的代理工厂之后，就是真正要来解析这些文件了，生成一个MapperAnnotationBuilder实例，调用parse()方法进行解析。
```text
// mapper注解构造器
MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
// 进行解析, 将接口完整限定名作为xml文件地址去解析
parser.parse();
```
而在parse()方法内部，首先也是调用loadXmlResource()方法去解析Mapper接口对应的xml文件。
```text
public void parse() {
    String resource = type.toString();
    // 是否已经解析mapper接口对应的xml
    if (!configuration.isResourceLoaded(resource)) {
        // 根据mapper接口名获取 xml文件并解析，  解析<mapper></mapper>里面所有东西放到configuration
        loadXmlResource();
        // 添加已解析的标记
        configuration.addLoadedResource(resource);
        assistant.setCurrentNamespace(type.getName());
        ......
    }
    parsePendingMethods();
}
```

### 2.1 加载XML配置
在loadXmlResource()方法中，会根据接口的名称，来拼接xml配置文件的全限定名，这就是为什么在通过package配置mapper接口的时候，
需要让接口和xml文件的路径和名称一模一样。

然后创建一个XMLMapperBuilder的实例，调用它的parse()方法，在这里方法里面才是真正在解析xml文件。
```text
private void loadXmlResource() {
    if (!configuration.isResourceLoaded("namespace:" + type.getName())) {
        String xmlResource = type.getName().replace('.', '/') + ".xml";
        InputStream inputStream = type.getResourceAsStream("/" + xmlResource);
        if (inputStream == null) {
            inputStream = Resources.getResourceAsStream(type.getClassLoader(), xmlResource);
        }
        if (inputStream != null) {
            XMLMapperBuilder xmlParser = new XMLMapperBuilder(inputStream, assistant.getConfiguration(), xmlResource, configuration.getSqlFragments(), type.getName());
            xmlParser.parse();
        }
    }
}
```

### 2.2 解析命名空间
我们在创建XMLMapperBuilder实例的时候，会创建一个MapperBuilderAssistant实例，该实例相当于一个工具类，会把xml解析出来的属性，
封装成对应实例，放入到configuration属性中。

而在创建XMLMapperBuilder实例的时候，会先把当前的接口名设置为命名空间的名字，后面再去xml中定义的命名空间作比较。
```text
public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, Map<String, XNode> sqlFragments, String namespace) {
    this(inputStream, configuration, resource, sqlFragments);
    this.builderAssistant.setCurrentNamespace(namespace);
}
```
取出xml文件mapper节点的namespace属性的值，该值不能为空，然后再判断该值是否于之前设置接口名称一致，不满足就会抛异常。
```text
    // 解析我们的namespace属性 <mapper namespace="com.tuling.mapper.EmployeeMapper">
    String namespace = context.getStringAttribute("namespace");
    if (namespace == null || namespace.equals("")) {
        throw new BuilderException("Mapper's namespace cannot be empty");
    }
    // 保存我们当前的namespace  并且判断接口完全类名==namespace
    builderAssistant.setCurrentNamespace(namespace);
    
    public void setCurrentNamespace(String currentNamespace) {
    if (currentNamespace == null) {
        throw new BuilderException("The mapper element requires a namespace attribute to be specified.");
    }
    // 判断接口名称于xml中配置的命名空间是否一致
    if (this.currentNamespace != null && !this.currentNamespace.equals(currentNamespace)) {
      throw new BuilderException("Wrong namespace. Expected '"
                    + this.currentNamespace + "' but found '" + currentNamespace + "'.");
    }

    this.currentNamespace = currentNamespace;
}
```

### 2.3 解析缓存配置
#### 2.3.1 解析缓存引用
在SQL的xml文件中，可以通过cache-ref节点来引用其他命名空间的缓存配置，通过namespace属性来指定引用的缓存。
```text
  /**
  * 解析我们的缓存引用
  * 说明我当前的缓存引用和DeptMapper的缓存引用一致
  * <cache-ref namespace="com.lizhi.mapper.DeptMapper"></cache-ref>
  * 解析到org.apache.ibatis.session.Configuration#cacheRefMap<当前namespace,ref-namespace>
  * 异常下（引用缓存未使用缓存）：org.apache.ibatis.session.Configuration#incompleteCacheRefs
  */
  cacheRefElement(context.evalNode("cache-ref"));
```
解析缓存引用的时候，首先把缓存引用的依赖关系保存在configuration的cacheRefMap属性，
key:当前的mapper的接口名称,value:缓存引用到的mapper的接口名称。

然后调用resolveCacheRef()方法去设置缓存，如果根据接口名称找不到缓存配置，就会抛出异常，找到了就设置当前mapper的缓存。
```text
private void cacheRefElement(XNode context) {
    if (context != null) {
        configuration.addCacheRef(builderAssistant.getCurrentNamespace(), context.getStringAttribute("namespace"));
        CacheRefResolver cacheRefResolver = new CacheRefResolver(builderAssistant, context.getStringAttribute("namespace"));
        try {
            cacheRefResolver.resolveCacheRef();
        } catch (IncompleteElementException e) {
            configuration.addIncompleteCacheRef(cacheRefResolver);
        }
    }
}
```

#### 2.3.2 解析缓存配置
可以通过cache节点来开启缓存，只要配置了该节点，就相当于启用了二级缓存，如果没有配置缓存的属性，Mybatis会使用默认的配置
```text
  /**
  * 解析我们的cache节点
  * <cache ></cache>
    解析到：org.apache.ibatis.session.Configuration#caches
    org.apache.ibatis.builder.MapperBuilderAssistant#currentCache
  */
  cacheElement(context.evalNode("cache"));
```
可以通过type属性设置缓存的类型，Mybatis中的二级缓存分为分为好几种类型，采用装饰器的模式，文章后面会对Mybatis的二级缓存做详细说明。
```text
private void cacheElement(XNode context) {
    if (context != null) {
        // 解析cache节点的type属性
        String type = context.getStringAttribute("type", "PERPETUAL");
        // 根据别名（或完整限定名）加载为Class
        Class<? extends Cache> typeClass = typeAliasRegistry.resolveAlias(type);
        // 获取缓存过期策略:默认是LRU
        String eviction = context.getStringAttribute("eviction", "LRU");
        Class<? extends Cache> evictionClass = typeAliasRegistry.resolveAlias(eviction);
        // flushInterval（刷新间隔）属性可以被设置为任意的正整数，设置的值应该是一个以毫秒为单位的合理时间量。 默认情况是不设置，也就是没有刷新间隔，缓存仅仅会在调用语句时刷新。
        Long flushInterval = context.getLongAttribute("flushInterval");
        // size（引用数目）属性可以被设置为任意正整数，要注意欲缓存对象的大小和运行环境中可用的内存资源。默认值是 1024。
        Integer size = context.getIntAttribute("size");
        // （只读）属性可以被设置为 true 或 false。只读的缓存会给所有调用者返回缓存对象的相同实例。 因此这些对象不能被修改。这就提供了可观的性能提升。而可读写的缓存会（通过序列化）返回缓存对象的拷贝。 速度上会慢一些，但是更安全，因此默认值是 false
        boolean readWrite = !context.getBooleanAttribute("readOnly", false);
        boolean blocking = context.getBooleanAttribute("blocking", false);
        Properties props = context.getChildrenAsProperties();
        // 把缓存节点加入到Configuration中
        builderAssistant.useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props);
    }
}
```
获得这些属性之后，builderAssistant实例会把这些属性，封装成一个Cache对象，然后存在configuration的caches属性中，
这是一个Map，KEY为mapper引用的接口名。
```text
public Cache useNewCache(Class<? extends Cache> typeClass,Class<? extends Cache> evictionClass,Long flushInterval,Integer size,boolean readWrite,boolean blocking,Properties props) {
  Cache cache = new CacheBuilder(currentNamespace).implementation(valueOrDefault(typeClass, PerpetualCache.class)).addDecorator(valueOrDefault(evictionClass, LruCache.class))
                      .clearInterval(flushInterval).size(size).readWrite(readWrite).blocking(blocking).properties(props).build();
                      configuration.addCache(cache);
  currentCache = cache;
  return cache;
}
```

### 2.4 解析resultMap
我们可以通过resultMap节点来定义数据库查询字段名与JavaBean中字段名的映射，以及类型处理器。

在一个xml文件中，可以定义多个resultMap节点，只要它们的属性id不一样即可，所以在解析的时候，也是去遍历所有的节点，依次解析
```text
// 解析获取到的所有<resultMap>
resultMapElements(context.evalNodes("/mapper/resultMap"));

// 依次解析
private void resultMapElements(List<XNode> list) throws Exception {
  for (XNode resultMapNode : list) {
    resultMapElement(resultMapNode);
  }
}
```
首先获取resultMap对应的JavaBean类型，有四种方式可以设置。
```text
private ResultMap resultMapElement(XNode resultMapNode, List<ResultMapping> additionalResultMappings, Class<?> enclosingType) throws Exception {
    ErrorContext.instance().activity("processing " + resultMapNode.getValueBasedIdentifier());
    // 从这里可以看出， 类型可以通过这4个属性设置
    String type = resultMapNode.getStringAttribute("type",
                                                   resultMapNode.getStringAttribute("ofType",
                                                   resultMapNode.getStringAttribute("resultType",
                                                   resultMapNode.getStringAttribute("javaType"))));
    // 根据别名 或 完全类名  获取类型
    Class<?> typeClass = resolveClass(type);
    if (typeClass == null) {
        typeClass = inheritEnclosingType(resultMapNode, enclosingType);
    }
    ......
}
```
resultMap节点提供了通过指定构造方法的参数来进行映射，这种方式平时不怎么用，主要还是通过id节点来配置。

得到所有resultMap节点的子节点之后，遍历这些子节点，然后在buildResultMappingFromContext()方法中获取节点的各种配置，
然后封装成一个ResultMapping对象，然后把这些对象先添加到resultMappings列表里面。
```text
    List<ResultMapping> resultMappings = new ArrayList<>();
    List<XNode> resultChildren = resultMapNode.getChildren();
    for (XNode resultChild : resultChildren) {
        if ("constructor".equals(resultChild.getName())) {
            processConstructorElement(resultChild, typeClass, resultMappings);
        } else if ("discriminator".equals(resultChild.getName())) {
            discriminator = processDiscriminatorElement(resultChild, typeClass, resultMappings);
        } else {
            List<ResultFlag> flags = new ArrayList<>();
            if ("id".equals(resultChild.getName())) {
                flags.add(ResultFlag.ID);
            }
            // 解析出所有属性构建为ResultMapping添加到resultMappings中（包括重要的： javaType,jdbcType,column,typeHandler)
            resultMappings.add(buildResultMappingFromContext(resultChild, typeClass, flags));
        }
    }
```
最后获取resultMap节点id属性的值，如果没有指定id属性，通过拼装节点名作为id的属性值。

最后调用resolve()方法，把resultMappings封装成一个ResultMap对象，添加到configuration的resultMaps属性中，
其中KEY为resultMap节点id的属性值。
```text
String id = resultMapNode.getStringAttribute("id",
resultMapNode.getValueBasedIdentifier());
ResultMapResolver resultMapResolver = new ResultMapResolver(builderAssistant, id, typeClass, extend, discriminator, resultMappings, autoMapping);
// 解析到configuration中
return resultMapResolver.resolve();
```

### 2.5 解析SQL片段
我们在使用xml的时候，可以把有些SQL的公共部分抽离，作为一个SQL片段，然后再SQL中通过引用片段来降低冗余；SQL片段还可以配置数据库厂商，通一个SQL语句，使用不同的数据，它们的语法可能是不同，也可以通过SQL片段来定义。具体使用通过sql节点来定义片段
```text
    /**
    * 解析我们通过sql片段
    *  解析到org.apache.ibatis.builder.xml.XMLMapperBuilder#sqlFragments
    *   其实等于 org.apache.ibatis.session.Configuration#sqlFragments
    *   因为他们是同一引用，在构建XMLMapperBuilder 时把Configuration.getSqlFragments传进去了
    */
    sqlElement(context.evalNodes("/mapper/sql"));
```
把这些SQL片段添加到XMLMapperBuilder对象的sqlFragments属性中，在使用的时候再解析具体节点的内容。
```text
private void sqlElement(List<XNode> list) {
    if (configuration.getDatabaseId() != null) {
        sqlElement(list, configuration.getDatabaseId());
    }
    sqlElement(list, null);
}

// 与Mybatis定义的数据库厂商id做比较,相同或者SQL片段没有指定数据库厂商时,就进行缓存
private void sqlElement(List<XNode> list, String requiredDatabaseId) {
    for (XNode context : list) {
        String databaseId = context.getStringAttribute("databaseId");
        String id = context.getStringAttribute("id");
        id = builderAssistant.applyCurrentNamespace(id, false);
        if (databaseIdMatchesCurrent(id, databaseId, requiredDatabaseId)) {
            sqlFragments.put(id, context);
        }
    }
}
```

### 2.6 解析SQL语句
获取所有select|insert|update|delete类型的节点，然后遍历这些节点，对节点配置的参数进行解析
```text
  /**
  * 解析我们的select | insert |update |delete节点
  * 解析到org.apache.ibatis.session.Configuration#mappedStatements
  */
  buildStatementFromContext(context.evalNodes("select|insert|update|delete"));
```
如果全局配置文件配置了数据库厂商ID，那么在解析SQL语句的时候，
也要判断select|insert|update|delete这些节点配置的数据库厂商ID是否匹配，只有匹配了才会继续解析。
```text
private void buildStatementFromContext(List<XNode> list) {
    // 判断有没有配置数据库厂商ID
    if (configuration.getDatabaseId() != null) {
        buildStatementFromContext(list, configuration.getDatabaseId());
    }
    buildStatementFromContext(list, null);
}
```
创建一个xmlStatement的构建器对象，对SQL节点进行解析。
```text
private void buildStatementFromContext(List<XNode> list, String requiredDatabaseId) {
    // 循环我们的select|delete|insert|update节点
    for (XNode context : list) {
        final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, context, requiredDatabaseId);
        try {
            statementParser.parseStatementNode();
        } catch (IncompleteElementException e) {
            configuration.addIncompleteStatement(statementParser);
        }
    }
}
```

#### 2.6.1 检验数据库商场是否匹配
```text
public void parseStatementNode() {
    // insert|delte|update|select 语句的sqlId
    String id = context.getStringAttribute("id");
    // 判断我们的insert|delte|update|select  节点是否配置了数据库厂商标注,匹配当前的数据库厂商id是否匹配当前数据源的厂商id
    String databaseId = context.getStringAttribute("databaseId");
    if (!databaseIdMatchesCurrent(id, databaseId, this.requiredDatabaseId)) {
        return;
    }
}
```

#### 2.6.2 获取缓存相关属性
如果没有配置flushCache和useCache属性，则根据操作类型来取默认值，如果操作类型为Select，那么flushCache就为false，
表示不需要刷新缓存，其他类型就需要刷新缓存。

如果没有配置useCache，同样根据操作类型来取默认值，Select操作默认使用缓存，其他操作不使用缓存。
```text
  // 获得节点名称：select|insert|update|delete
  String nodeName = context.getNode().getNodeName();
  // 根据nodeName 获得 SqlCommandType枚举
  SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
  // 判断是不是select语句节点
  boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
  // 获取flushCache属性,默认值为isSelect的反值：查询：flushCache=false   增删改：flushCache=true
  boolean flushCache = context.getBooleanAttribute("flushCache", !isSelect);
  // 获取useCache属性,默认值为isSelect：查询：useCache=true   增删改：useCache=false
  boolean useCache = context.getBooleanAttribute("useCache", isSelect);
```

#### 2.6.3 解析SQL公用片段
创建一个XMLIncludeTransformer实例，调用applyIncludes()方法对SQL语句引入的SQL片段进行解析
```text
/**
* 解析我们的sql公用片段
*     <select id="qryEmployeeById" resultType="Employee" parameterType="int">
         <include refid="selectInfo"></include>
         employee where id=#{id}
     </select>
   将 <include refid="selectInfo"></include> 解析成sql语句 放在<select>Node的子节点中
*/
XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
includeParser.applyIncludes(context.getNode());
```
SQL片段的值，可以通过全局配置文件来配置。

而在SQL片段内部，也可以使用include标签来导入公共SQL片段，所以在解析SQL片段的时候，要递归进行解析
```text
public void applyIncludes(Node source) {
    Properties variablesContext = new Properties();
    // 拿到之前配置文件解析的<properties>
    Properties configurationVariables = configuration.getVariables();
    // 放入到variablesContext中
    Optional.ofNullable(configurationVariables).ifPresent(variablesContext::putAll);
    // 替换Includes标签为对应的sql标签里面的值
    applyIncludes(source, variablesContext, false);
}

private void applyIncludes(Node source, final Properties variablesContext, boolean included) {
    if (source.getNodeName().equals("include")) {
        // 拿到之前解析的<sql>
        Node toInclude = findSqlFragment(getStringAttribute(source, "refid"), variablesContext);
        Properties toIncludeContext = getVariablesContext(source, variablesContext);
        // 递归， included=true
        applyIncludes(toInclude, toIncludeContext, true);
        if (toInclude.getOwnerDocument() != source.getOwnerDocument()) {
        toInclude = source.getOwnerDocument().importNode(toInclude, true);
        }
        // <include的父节点=select 。  将<select>里面的<include>替换成 <sql> ，那<include>.getParentNode就为Null了
        source.getParentNode().replaceChild(toInclude, source);
        while (toInclude.hasChildNodes()) {
        // 接下来<sql>.getParentNode()=select.  在<sql>的前面插入<sql> 中的sql语句   ,
        toInclude.getParentNode().insertBefore(toInclude.getFirstChild(), toInclude);
        }
        // <sql>.getParentNode()=select  , 移除select中的<sql> Node 。
        //  不知道为什么不直接replaceChild呢？还做2步 先插再删，
        toInclude.getParentNode().removeChild(toInclude);
        int i=0;
    } else if (source.getNodeType() == Node.ELEMENT_NODE) { // 0
        if (included && !variablesContext.isEmpty()) {
            // replace variables in attribute values
            NamedNodeMap attributes = source.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                attr.setNodeValue(PropertyParser.parse(attr.getNodeValue(), variablesContext));
            }
        }
        NodeList children = source.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            // 递归
            applyIncludes(children.item(i), variablesContext, included);
        }
        // included=true 说明是从include递归进来的
    } else if (included && (source.getNodeType() == Node.TEXT_NODE || source.getNodeType() == Node.CDATA_SECTION_NODE)
    && !variablesContext.isEmpty()) {
        // 替换sql片段中的 ${<properties解析到的内容>}
        source.setNodeValue(PropertyParser.parse(source.getNodeValue(), variablesContext));
    }
}
```

#### 2.6.4 解析SQL语句
在解析SQL语句之前，首先需要获得sql脚本语言驱动，可以在select等操作节点，通过lang属性进行配置，如果不配置默认使用XMLLanguageDriver来进行解析，一般也不需要配置
```text
/*
<settings>
<setting name="defaultScriptingLanguage" value="lizhiLang"/>
</settings>
*/
String lang = context.getStringAttribute("lang");
// 获取自定义sql脚本语言驱动 默认:class org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
LanguageDriver langDriver = getLanguageDriver(lang);
```
然后调用createSqlSource()方法来解析SQL，这个时候并不会直接就把SQL解析成可执行的SQL语句，因为这个时候，SQL语句的参数还没确定。

在这一步，只是将SQL语句解析成层次分明的SqlNode对象
```text
  /**
  * 通过class org.apache.ibatis.scripting.xmltags.XMLLanguageDriver来解析我们的
  * sql脚本对象  .  解析SqlNode. 注意， 只是解析成一个个的SqlNode， 并不会完全解析sql,因为这个时候参数都没确定，动态sql无法解析
  */
  SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
```
真正解析SQL语句的是XMLScriptBuilder，将SQL语句解析后，生成一个MixedSqlNode，然后判断该SQL是动态SQL还是静态SQL，
分别生成不同的SqlSource对象。
```text
public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
    XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
    return builder.parseScriptNode();
}

public SqlSource parseScriptNode() {
    MixedSqlNode rootSqlNode = parseDynamicTags(context);
    SqlSource sqlSource;
    if (isDynamic) {
        // 动态Sql 就是还需要后续执行时根据传入参数动态解析Sql（因为有<if>等,还要拼接${}sql）和参数ParameterMappings   也会在后续执行解析，因为动态条件肯定会有动态参数
        sqlSource = new DynamicSqlSource(configuration, rootSqlNode);
    } else {
        // 静态Sql源  如果没有动态标签(<if>、<where>等) 以及 没有${}  就是静态Sql源，静态Sql 就是在这里就解析了Sql  和参数ParameterMappings   后续执行就不用解析了
        sqlSource = new RawSqlSource(configuration, rootSqlNode, parameterType);
    }
    // 其实他们的区别就是动态sql 需要在查询的时候解析 因为有动态sql 和拼接${}
    // 静态sql 已经在这里确定好sql. 和参数ParameterMapping,
    return sqlSource;
}
```
下面介绍一下，parseDynamicTags()方法是如何把SQL解析成SqlNode的。

在Mybatis中，它支持动态SQL，所以SqlNode的类型就包括了StaticTextSqlNode、TextSqlNode、ChooseSqlNode、
IfSqlNode、TrimSqlNode(SetSqlNode和WhereSqlNode)、ForEachSqlNode、MixedSqlNode。

XMLScriptBuilder在实例化的时候，就为这些SqlNode添加了SqlNode的处理器，这些处理器就是为了解析每个标签下的子标签，
最终返回一个树型结构的SqlNode，最后再把这些SqlNode，封装成一个MixedSqlNode。
```text
protected MixedSqlNode parseDynamicTags(XNode node) {
    List<SqlNode> contents = new ArrayList<>();
    NodeList children = node.getNode().getChildNodes();  //获得<select>的子节点
    for (int i = 0; i < children.getLength(); i++) {
        XNode child = node.newXNode(children.item(i));
        if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE 
                || child.getNode().getNodeType() == Node.TEXT_NODE) {
            String data = child.getStringBody(""); // 获得sql文本
            TextSqlNode textSqlNode = new TextSqlNode(data);
            if (textSqlNode.isDynamic()) {  // 怎样算Dynamic? 其实就是判断sql文本中有${}
                contents.add(textSqlNode);
                isDynamic = true;
            } else {
                contents.add(new StaticTextSqlNode(data));  //静态文本
            }
        } else if (child.getNode().getNodeType() == Node.ELEMENT_NODE) { // issue #628
            String nodeName = child.getNode().getNodeName();

            /*** 判断当前节点是不是动态sql节点{@link XMLScriptBuilder#initNodeHandlerMap()}*/
            NodeHandler handler = nodeHandlerMap.get(nodeName);
            if (handler == null) {
                throw new BuilderException("Unknown element <" + nodeName + "> in SQL statement.");
            }
            handler.handleNode(child, contents);  // 不同动态节点有不用的实现
            isDynamic = true;     // 怎样算Dynamic? 其实就是判断sql文本动态sql节点
        }
    }
    return new MixedSqlNode(contents);
}
```

### 2.7 解析返回类型
解析resultType或resultMap或resultSetType的属性值。
```text
  String resultType = context.getStringAttribute("resultType");
  /**解析我们查询结果集返回的类型     */
  Class<?> resultTypeClass = resolveClass(resultType);
  /**
  * 外部 resultMap 的命名引用。结果集的映射是 MyBatis 最强大的特性，如果你对其理解透彻，许多复杂映射的情形都能迎刃而解。
  * 可以使用 resultMap 或 resultType，但不能同时使用。
  */
  String resultMap = context.getStringAttribute("resultMap");

  String resultSetType = context.getStringAttribute("resultSetType");
  ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);
  if (resultSetTypeEnum == null) {
    resultSetTypeEnum = configuration.getDefaultResultSetType();
  }
```

### 2.8 封装MappedStatement
SQL解析完成之后，就会调用addMappedStatement()方法来生成一个MappedStatement对象，MappedStatement对象的id属性值是由mapper接口名+’.’+insert|delte|update|select节点的id属性构成

最后把MappedStatement对象添加到configuration的mappedStatements中，其中KEY为MappedStatement的id属性值
```text
  /**
  * 为insert|delete|update|select节点构建成我们的mappedStatment对象
  */
  builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType,
  fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass,
  resultSetTypeEnum, flushCache, useCache, resultOrdered,
  keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);
```
