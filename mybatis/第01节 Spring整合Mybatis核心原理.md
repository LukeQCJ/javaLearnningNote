# 第01节 Spring整合Mybatis核心原理

## 一、Spring集成Mybatis原理
Mybatis是一个单独的半ORM框架(https://mybatis.net.cn/)，可以与Spring集成使用，也可以单独使用，单独使用代码如下:
```text
    // 1、加载配置文件
    InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
    // 2、解析配置文件，并创建SqlSessionFactory对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    // 3、打开一个数据库连接会话
    SqlSession sqlSession = sqlSessionFactory.openSession();
    // 4、获取Mapper接口对应的MapperProxy的代理对象
    OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
    // 5、直接调用这个Mybatis的代理对象，执行具体的方法来执行SQL方法
    System.out.println(orderMapper.selectOrder());
    
    sqlSession.commit();
    sqlSession.flushStatements();
    sqlSession.close();
```
```text
public interface OrderMapper {

    @Select("select from 'order'")
    String selectOrder();
}
```
如上是手动编程调用mybatis的代码，具体步骤如下：
```text
1) 加载mybatis.xml的配置文件，
2) 然后解析配置文件，创建一个DefaultSqlSessionFactory的工厂对象，
3) 通过openSession()打开一个数据库连接的会话，
4) 通过getMapper()方法可以为对应的Mapper接口生成一个MapperProxy的代理对象，
5) 然后直接调用这个Mybatis的代理对象，就可以执行其中定义的SQL方法。
```
从上面的Mybatis单独使用的方式来看，如果集成Spring，需要做几件事呢？
```text
1) 最重要的就是去 扫描Mapper接口，
2) 然后生成Mybatis的代理对象，由Spring进行统一管理。
```
在Spring中要想初始Bean实例对象，首先肯定需要有BeanDefinition才行，
而Spring本身在扫描指定包生成BeanDefinition的时候，只会把带有@Component注解的类生成BeanDefinition，
而Mybatis中使用的都是接口，肯定无法扫描，所以在spring整合mybatis的时候，需要自己定义一套属于自己的扫描逻辑。

就算是能够扫描接口了，也不能直接为接口去生成BeanDefinition，因为接口没有构造方法，没法进行实例化，这个时候，就会用到FactoryBean，
为每一个Mapper接口都构造一个FactoryBean类型的BeanDefinition，这样就能通过BeanDefinition去先去实例化得到一个FactoryBean对象，
然后在getBean()的时候，再去调用FactoryBean实例的getObject()方法生成mapper接口的代理对象。

从上面的说明可以看出，我们需要为每一个mapper接口来创建一个FactoryBean的BeanDefinition，但只需要定义一个FactoryBean的实现类即可，
在该实现类中通过一个Class类型参数来指定mapper接口的类型，这样就可以用同一个FactoryBean来创建mapper接口的BeanDefinition。

注册BeanDefinition有两种方式：
```text
1) BeanDefinitionRegistry接口: registerBeanDefinition()方法
2) ImportBeanDefinitionRegistrar接口：registerBeanDefinitions()方法
```
但对于mybatis来说，只需要对mapper接口进行操作，那么在注册BeanDefinition的时候，首先需要获取mapper接口对应的包，
才能生成BeanDefinition，有了这样一个要求，mybatis就只能通过ImportBeanDefinitionRegistrar来实现BeanDefinition的注册。

## 二、扫描Mapper接口生成BeanDefinition

### 2.1 导入配置类
mybatis-spring中通过@MapperScan或@MapperScans来指定扫描的包，以@MapperScan为例，源码如下：
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
@Repeatable(MapperScans.class)
public @interface MapperScan {
    String[] value() default {};
    
    String[] basePackages() default {};
    
    Class<?>[] basePackageClasses() default {};
    
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;
    
    Class<? extends Annotation> annotationClass() default Annotation.class;
    
    Class<?> markerInterface() default Class.class;
    
    String sqlSessionTemplateRef() default "";
    
    String sqlSessionFactoryRef() default "";
    
    Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;
    
    String lazyInitialization() default "";
    
    String defaultScope() default AbstractBeanDefinition.SCOPE_DEFAULT;
}
```
该注解最核心的点是通过【@Import注解】导入了【MapperScannerRegistrar类】，Spring在启动的时候，会把该类当作配置类进行解析，
解析的时候执行该类的【registerBeanDefinitions()方法】。

### 2.2 配置类解析
在《配置类解析(中)》文章中，详细介绍了@Import注解的扫描，MapperScannerRegistrar实现了【ImportBeanDefinitionRegistrar】接口，
所以会在每轮配置类解析完成之后，执行该类的registerBeanDefinitions()方法。

MapperScannerRegistrar.registerBeanDefinitions()方法如下：
```text
@Override
public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    // 解析出@MapperScan注解的属性
    AnnotationAttributes mapperScanAttrs = AnnotationAttributes
            .fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
    if (mapperScanAttrs != null) {
        // 注册mybatis相关的bean定义: MapperScannerConfigurer
        registerBeanDefinitions(importingClassMetadata, mapperScanAttrs, registry,
            generateBaseBeanName(importingClassMetadata, 0));
    }
}
```
在下面的registerBeanDefinitions()方法中，会构建一个MapperScannerConfigurer类的BeanDefinition，
然后注册到BeanFactory中，其中会把@MapperScan注解中的的属性值全部加入到这个类的BeanDefinition中，
其中最重要的两个属性分别是mapperFactoryBeanClass和basePackage。

mapperFactoryBeanClass的值通过@MapperScan注解的factoryBean属性指定，该属性默认值是MapperFactoryBean，
这个类实现了FactoryBean接口，就是在mybatis集成到spring原理中介绍到的那个factoryBean。

basePackage的值汇总了@MapperScan注解中value、basePackages和basePackageClasses属性的值。

```text
void registerBeanDefinitions(AnnotationMetadata annoMeta, AnnotationAttributes annoAttrs,
        BeanDefinitionRegistry registry, String beanName) {

    // 生成MapperScannerConfigurer的bean定义
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
    builder.addPropertyValue("processPropertyPlaceHolders", true);
    
	...... // 非核心代码，省略
	
    // 获取FactoryBean
    Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
    if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
        builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
    }
    
	...... // 非核心代码，省略
	
    List<String> basePackages = new ArrayList<>();
    basePackages.addAll(
        Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

    basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
                        .collect(Collectors.toList()));

    basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
                        .collect(Collectors.toList()));

    if (basePackages.isEmpty()) {
        basePackages.add(getDefaultBasePackage(annoMeta));
    }
    
	...... // 非核心代码，省略
	
    builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
    // 【重要】将MapperScannerConfigurer的beanDefinition注册到BeanFactory的beanDefinitionMap中
    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

}
```
注：MapperScannerConfigurer实现了【BeanDefinitionRegistryPostProcessor】，
在《Spring容器启动(下)》介绍了，在容器启动的时候，会扫描所有实现了BeanDefinitionRegistryPostProcessor接口的类，
然后调用它的【postProcessBeanDefinitionRegistry()来注册新的BeanDefinition】，
在mybatis-spring，主要就是用来【注册mapper接口】对应的【FactoryBean的BeanDefinition】。

### 2.3 注册BeanDefinition
在第一部分也提到了，mybatis需要一个自定义的扫描器，来完成自定义逻辑的扫描，
所以在MapperScannerConfigurer.postProcessBeanDefinitionRegistry()方法中，
首先创建一个扫描器，然后设置自定义扫描器的逻辑。
```text
public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
    if (this.processPropertyPlaceHolders) {
        processPropertyPlaceHolders();
    }
    
    // 创建类路径映射扫描器
    ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
    scanner.setAddToConfig(this.addToConfig);
    scanner.setAnnotationClass(this.annotationClass);
    scanner.setMarkerInterface(this.markerInterface);
    scanner.setSqlSessionFactory(this.sqlSessionFactory);
    
    ...... // 非核心代码，省略
    
    scanner.registerFilters();
    
    // 【重要】扫描包生成BeanDefinition
    scanner.scan(
        StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
}
```
自定义扫描器，主要的用处就在于添加自定义的IncludeFilter和ExcludeFilter。
scanner.registerFilters()实现如该功能，如果没有指定只扫描哪个Mapper接口，
那么acceptAllInterfaces就等于true，然后添加的IncludeFilter也是允许扫描包下的所有文件。
```text
public void registerFilters() {
    boolean acceptAllInterfaces = true;
    
    // if specified, use the given annotation and / or marker interface
    if (this.annotationClass != null) {
        addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        acceptAllInterfaces = false;
    }
    
    ...... // 非核心代码，省略
    
    if (acceptAllInterfaces) {
        // default include filter that accepts all classes
        addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
    }
    
    // exclude package-info.java
    addExcludeFilter((metadataReader, metadataReaderFactory) -> {
        String className = metadataReader.getClassMetadata().getClassName();
        return className.endsWith("package-info");
    });
}
```
然后调用ClassPathMapperScanner.scan()方法扫描指定包下的所有文件，生成BeanDefinition。
```text
public int scan(String... basePackages) {
    int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
    // 执行扫描
    doScan(basePackages);
    // Register annotation config processors, if necessary.
    if (this.includeAnnotationConfig) {
        // 注册注解配置后置处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
    }

    return (this.registry.getBeanDefinitionCount() - beanCountAtScanStart);
}
```

## 三、设置BeanDefinition
调用ClassPathMapperScanner的scan()方法扫描时，会去调用doScan()方法，其中ClassPathMapperScanner的doScan()方法如下：
```text
public Set<BeanDefinitionHolder> doScan(String... basePackages) {
    // 【重要】扫描bean定义holder
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
    if (beanDefinitions.isEmpty()) {
        LOGGER.warn(() -> "No MyBatis mapper was found in '" + Arrays.toString(basePackages)
            + "' package. Please check your configuration.");
    } else {
        // 处理bean定义
        processBeanDefinitions(beanDefinitions);
    }
    return beanDefinitions;
}
```
ClassPathMapperScanner集成自Spring的ClassPathBeanDefinitionScanner，所以调用父类的doScan()方法进行扫描，
得到mapper接口对应的BeanDefinition，而此时的BeanDefinition还只是的BeanDefinition，
接下来把扫描得到的BeanDefinition进行修改，把BeanClass修改为MapperFactoryBean，把AutowireMode修改为byType，
这样就变成了一个FactoryBean的BeanDefinition。

processBeanDefinitions()方法最重要的工作就是改变BeanDefinition的属性，将其变成一个MapperFactoryBean的BeanDefinition，
而MapperFactoryBean中需要有一个Mapper接口类型的属性值，所以MapperFactoryBean提供了通过构造方法的方式来注入mapperInterface的值。
而这个值是通过definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName)来指定的构造方法的参数值指定的，
这样每一个FactoryBean的实例，都对应于一个Mapper接口的类型。 同时设置MapperFactoryBean的注入方式为根据类型进行注入。
```text
private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    AbstractBeanDefinition definition;
    // 注册中心
    BeanDefinitionRegistry registry = getRegistry();
    for (BeanDefinitionHolder holder : beanDefinitions) {
        definition = (AbstractBeanDefinition) holder.getBeanDefinition();
        boolean scopedProxy = false;
        if (ScopedProxyFactoryBean.class.getName().equals(definition.getBeanClassName())) {
            definition = (AbstractBeanDefinition) Optional
                .ofNullable(((RootBeanDefinition) definition).getDecoratedDefinition())
                .map(BeanDefinitionHolder::getBeanDefinition).orElseThrow(() -> new IllegalStateException(
                    "The target bean definition of scoped proxy bean not found. Root bean definition[" + holder + "]"));
            scopedProxy = true;
        }
        String beanClassName = definition.getBeanClassName();

        // the mapper interface is the original class of the bean
        // but, the actual class of the bean is MapperFactoryBean
        // 指定MapperFactoryBean构造方法的参数值，为当前BeanDefinition对应的Mapper接口的类型
        definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName); // issue #59
        // 改变BeanDefinition为FactoryBean的BeanDefinition
        definition.setBeanClass(this.mapperFactoryBeanClass);

        definition.getPropertyValues().add("addToConfig", this.addToConfig);

		...... // 非核心逻辑代码，省略

        // 设置根据类型进行注入
        if (!explicitFactoryUsed) {
            LOGGER.debug(() -> "Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
        
        ...... // 非核心逻辑代码，省略
    }
}
```
然后Spring容器启动完成之后，需要实例化非懒加载的单例Bean，这个时候，这些FactoryBean的BeanDefinition都会进行实例化，
生成一个单例的FactoryBean实例。

而Mapper接口的代理实例通常是通过注入的时候才生成，在业务代码中，需要注入Mapper接口实例，操作数据库，
Spring会根据依赖的类型注入实例，这时候就回去调用getBean()来生成实例，
在生成实例的过程中，首先会从单例池中拿到FactoryBean的实例，然后再调用FactoryBean的getObject()方法来生成MapperProxy的代理对象。

此时MapperFactoryBean实例的mapperInterface参数就是Mapper接口的类型，
这样就能拿到一个MapperProxy的代理对象，执行SQL，至此mybatis整合spring的主要核心工作就做完了，剩下的就是mybatis自己的执行逻辑。

MapperFactoryBean.getObject()方法：
```text
@Override
public T getObject() throws Exception {
    return getSqlSession().getMapper(this.mapperInterface);
}
```
注：mybatis整合spring还不止这些内容，其中关于sqlSession的部分，将在介绍完mybatis的源码后，用单独的文章介绍sqlSession的整合。
