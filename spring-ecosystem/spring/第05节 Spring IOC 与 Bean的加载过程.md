# 第05节 Spring IOC 与 Bean的加载过程

## 一、Spring配置IOC容器的方式
我们可以通过 new ApplicationContext() 去加载spring容器/上下文。
通过不同的配置方式又有以下两种加载容器的方式：

①：通过xml配置文件 ** new ClassPathXmlApplicationContext**
```text
context = new ClassPathXmlApplicationContext("xml");
```

②：通过注解@bean等注解 new AnnotationConfigApplicationContext
```text
context = new AnnotationConfigApplicationContext(MainConfig.class);
```

第一种通过xml配置文件获取的方式，内部的代码是耦合的。也就是说读取器读取配置类、扫描bean对象、注册bean定义这些操作是由上往下写死在代码里的。

第二种通过注解方式获取容器思想比较先进，它通过扩展BeanFactory后置处理器并可插拔的方式，也完成了上述功能，并实现了代码解耦！

所以就从new AnnotationConfigApplicationContext(MainConfig.class)开始讲ioc的加载过程吧!

注意：当使用springboot时，则采用 ServletWebServerApplicationContext() 来加载容器，
这种加载方式不改变原有IOC容器的加载过程，只不过在这个基础上做了一些扩展，比如：加载自动配置类，创建Servlet容器。

## 二、基于Java配置的Spring IOC初始化流程

### 基于Java配置 Spring项目

maven pom.xml如下
```text
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>springDemo</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>springDemo</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <spring.version>5.2.3.RELEASE</spring.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <!-- @Resource annotation -->
    <dependency>
      <groupId>javax.annotation</groupId>
      <artifactId>javax.annotation-api</artifactId>
      <version>1.3.2</version>
    </dependency>

  </dependencies>
</project>
```
Java配置项目代码
```java
@Configuration
@ComponentScan(basePackages = "org.example.*")
public class App {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(App.class);
        User user = (User) ac.getBean("user");
        System.out.println(user.getName() + ":::" + user.getAge());
    }
}

public class User {

    private String name;

    private Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}

@Configuration
public class DemoConfig {

    @Bean
    public User user() {
        User user = new User();
        user.setName("luke");
        user.setAge(10);
        return user;
    }
}
```

### ①：初始化容器DefaultListableBeanFactory
```text
// 加载spring上下文
public AnnotationConfigApplicationContext(Class... componentClasses) {
        // 【重要】调用无参构造方法，这里会初始化 容器、注解beanDefinition读取器、类路径beanDefinition扫描器
        this();
		// 把配置类注册成一个Bean定义并放到Bean定义池beanDefinitionMap中，因为后边要解析这个类
        this.register(componentClasses);
		// 【最重要】spring容器刷新接口
        this.refresh();
    }
```

this()方法会调用无参构造方法，主要有三大步：
```text
①：初始化容器DefaultListableBeanFactory
②：初始化读取器AnnotatedBeanDefinitionReader
③：初始化扫描器ClassPathBeanDefinitionScanner
```
this() 方法如下：
```text
    public AnnotationConfigApplicationContext() {
		// 【重要】在这里会隐式调用父类的构造方法，初始化DefaultListableBeanFactory
        // super();
        
    	// 【重要】初始化读取配置类reader，下文会进行读取。
    	// 在其内部初始化了一些创世纪的bean定义，是spring内部的类，用于服务其他bean！
    	// 比如: 
    	//      ConfigurationClassPostProcessor: 解析配置类、基于配置类解析出beanDefinition
    	//      AutowiredAnnotationBeanPostProcessor: 解析@Autowired、@Value等
    	//      CommonAnnotationBeanPostProcessor: 解析@Resource等
        this.reader = new AnnotatedBeanDefinitionReader(this);

		// 【重要】扫描器，它仅仅是在我们外部手动调用scan等方法才有用，常规方式是不会用到scanner对象的
        this.scanner = new ClassPathBeanDefinitionScanner(this);
    }
```

首先 隐式调用父类的构造方法，初始化DefaultListableBeanFactory作为容器
```text
this.beanFactory = new DefaultListableBeanFactory();
```
为什么容器要使用DefaultListableBeanFactory呢？可以看一下DefaultListableBeanFactory的继承体系
![DefaultListableBeanFactoryHierarchy.png](img/05/DefaultListableBeanFactoryHierarchy.png)
可以看到为什么选择DefaultListableBeanFactory作为容器了吧！因为DefaultListableBeanFactory在BeanFactory的基础上，有很大提升！


### ②：创建读取器，初始化spring IOC容器核心处理类

接下来回到this()方法的 new AnnotatedBeanDefinitionReader(this)中，它有什么作用呢？

在它的内部初始化了很多维持spring功能的最原始的Bean定义，
比如**ConfigurationClassPostProcessor**、XxxBeanPostProcessor等等！如下图所示：
![AnnotatedBeanDefinitionReader.png](img/05/AnnotatedBeanDefinitionReader.png)

```text
	public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
	    // 创建一个standardEnvironment，并传入AnnotatedBeanDefinitionReader构造器中
		this(registry, getOrCreateEnvironment(registry));
	}
	
	public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		Assert.notNull(environment, "Environment must not be null");
		this.registry = registry;
		this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);
		// 【重要】注册注解配置处理器: 
		// ConfigurationClassPostProcessor、AutowiredAnnotationBeanPostProcessor、CommonAnnotationBeanPostProcessor等
		AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
	}
```
进入AnnotationConfigUtils#registerAnnotationConfigProcessors(this.registry)方法如下：
```text
	public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
		registerAnnotationConfigProcessors(registry, null);
	}
	
	public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(
			BeanDefinitionRegistry registry, @Nullable Object source) {
		...... // 一些其他代码，省略 
		
		Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<>(8);
		// 【重要】创建 ConfigurationClassPostProcessor 的bean定义
		if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
		// 【重要】创建 AutowiredAnnotationBeanPostProcessor的bean定义
		if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
		// 【重要】创建 CommonAnnotationBeanPostProcessor 的bean定义
		// 条件jsr250Present值是当类路径下classloader加载到@Resource注解时才为true
		if (jsr250Present && !registry.containsBeanDefinition(COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
	
		..... // 创建其他 为spring提供基础功能的其他bean定义  省略
	}
```

其中ConfigurationClassPostProcessor实现BeanDefinitionRegistryPostProcessor接口， 
BeanDefinitionRegistryPostProcessor接口又扩展了BeanFactoryPostProcessor接口，
BeanFactoryPostProcessor是Spring的扩展点之一，
ConfigurationClassPostProcessor是Spring极为重要的一个类，必须牢牢的记住上面所说的这个类和它的继承关系，如下图：
![ConfigurationClassPostProcessorHierarchy.png](img/05/ConfigurationClassPostProcessorHierarchy.png)

由于ConfigurationClassPostProcessor实现了BeanDefinitionRegistryPostProcessor接口，
所以必会重写BeanDefinitionRegistryPostProcessor接口的postProcessBeanDefinitionRegistry方法，
重写该方法主要是为了：
读取配置类，解析@ComponentScan、@Import、@ImportResource、@Bean等注解，
并把他们注册为bean定义，不过该过程是放在bean工厂的后置处理器中去做，
通过refresh()方法中的【invokeBeanFactoryPostProcessors方法】回调的方式去调用！
```java
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

	// 该方法可以【注册bean定义】
	void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
```

### ③：注册配置类的bean定义(BeanDefinition)

register方法会把配置类注册成一个 Bean定义 并放到 Bean定义池beanDefinitionMap(此时的池中已有spring的创世类)中。
进入AnnotationConfigApplicationContext#register方法：
```text
    public void register(Class... componentClasses) {
        Assert.notEmpty(componentClasses, "At least one component class must be specified");
        // 上文的读取器在这里使用到，读取和注册了配置类
        this.reader.register(componentClasses);
    }
    
```
进入AnnotatedBeanDefinitionReader#register方法：
```text
	public void register(Class<?>... componentClasses) {
		for (Class<?> componentClass : componentClasses) {
			registerBean(componentClass);
		}
	}
	
	public void registerBean(Class<?> beanClass) {
		doRegisterBean(beanClass, null, null, null, null);
	}
	
	// 实际执行注册 配置类的方法
	private <T> void doRegisterBean(Class<T> beanClass, @Nullable String name,
			@Nullable Class<? extends Annotation>[] qualifiers, @Nullable Supplier<T> supplier,
			@Nullable BeanDefinitionCustomizer[] customizers) {

		AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
		if (this.conditionEvaluator.shouldSkip(abd.getMetadata())) {
			return;
		}

		abd.setInstanceSupplier(supplier);
		ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
		abd.setScope(scopeMetadata.getScopeName());
		String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry));

		AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
		if (qualifiers != null) {
			for (Class<? extends Annotation> qualifier : qualifiers) {
				if (Primary.class == qualifier) {
					abd.setPrimary(true);
				}
				else if (Lazy.class == qualifier) {
					abd.setLazyInit(true);
				}
				else {
					abd.addQualifier(new AutowireCandidateQualifier(qualifier));
				}
			}
		}
		if (customizers != null) {
			for (BeanDefinitionCustomizer customizer : customizers) {
				customizer.customize(abd);
			}
		}

		BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
		definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
		// 注册到 Spring容器的beanDefinitionMap中
		BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
	}
```
继续往下跟源码BeanDefinitionReaderUtils#registerBeanDefinition方法：
```text
    public static void registerBeanDefinition(
			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
			throws BeanDefinitionStoreException {

		// Register bean definition under primary name.
		String beanName = definitionHolder.getBeanName();
		// 注册bean定义，是在DefaultListableBeanFactory中做的实现
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

		// Register aliases for bean name, if any.
		String[] aliases = definitionHolder.getAliases();
		if (aliases != null) {
			for (String alias : aliases) {
				registry.registerAlias(beanName, alias);
			}
		}
	}
```
进入DefaultListableBeanFactory#registerBeanDefinition方法如下：
```text
    // 【重要】bean定义池
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);
    
	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {

		Assert.hasText(beanName, "Bean name must not be empty");
		Assert.notNull(beanDefinition, "BeanDefinition must not be null");

        // 如果是AbstractBeanDefinition类型，则验证bean定义
		if (beanDefinition instanceof AbstractBeanDefinition) {
			try {
				((AbstractBeanDefinition) beanDefinition).validate(); 
			}
			catch (BeanDefinitionValidationException ex) {
				throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
						"Validation of bean definition failed", ex);
			}
		}
        
		BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
		// 如果beanName已经存在，则判断是否可以重写的逻辑
		if (existingDefinition != null) {
			if (!isAllowBeanDefinitionOverriding()) {
				throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
			}
			
			...... // 一些条件输出beanDefinition被重写的代码，忽略
			
			this.beanDefinitionMap.put(beanName, beanDefinition);
		}
		else {
			if (hasBeanCreationStarted()) {
				// Cannot modify startup-time collection elements anymore (for stable iteration)
				// 【重要】同步beanDefinitionMap，并将beanDefinition注解到beanDefinitionMap中
				synchronized (this.beanDefinitionMap) {
					this.beanDefinitionMap.put(beanName, beanDefinition);
					
					List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
					updatedDefinitions.addAll(this.beanDefinitionNames);
					updatedDefinitions.add(beanName);
					this.beanDefinitionNames = updatedDefinitions;
					removeManualSingletonName(beanName);
				}
			}
			else {
				// Still in startup registration phase
				// 【重要】将beanDefinition注解到beanDefinitionMap中
				this.beanDefinitionMap.put(beanName, beanDefinition);
				this.beanDefinitionNames.add(beanName);
				
				removeManualSingletonName(beanName);
			}
			this.frozenBeanDefinitionNames = null;
		}

		if (existingDefinition != null || containsSingleton(beanName)) {
			resetBeanDefinition(beanName);
		}
	}
```
可以看到确实把bean定义放入了bean定义池中。

### ④：refresh()方法: Spring IOC容器 初始化方法

其实到这里，Spring还没有进行扫描，只是实例化了一个工厂，注册了一些内置的Bean和我们传进去的配置类，真正的大头是在第三行代码refresh()。

AbstractApplicationContext#refresh()方法：
```text
    public void refresh() throws BeansException, IllegalStateException {
        synchronized(this.startupShutdownMonitor) {
        	// 1 刷新预处理，和主流程关系不大，就是保存了容器的启动时间，启动标志等
            this.prepareRefresh();
            
            // 2 这个方法和主流程关系也不是很大，可以简单的认为，就是把beanFactory取出来而已。
            /       【重要: 基于XML配置模式下，解析XML配置文件和注册BeanDefinition】
            ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();

			// 3 还是一些准备工作，添加了两个后置处理器：ApplicationContextAwareProcessor，ApplicationListenerDetector ,
			// 还设置了忽略自动装配 和 允许自动装配的接口,如果不存在某个bean的时候，spring就自动注册singleton bean
			// 还设置了bean表达式解析器等
            this.prepareBeanFactory(beanFactory);

            try {	
            	// 4 空方法，留给我们做实现,可能以后Spring会进行扩展
                this.postProcessBeanFactory(beanFactory);
                
                // 5 执行自定义的BeanFactoryProcessor和内置的BeanFactoryProcessor，并实例化，
                // 利用ConfigurationClassPostProcessor帮我们解析配置类和注册BeanDefinition，
                // 解析@Configuration、@Component、@PropertySources(@PropertySource)、@ComponentScans(@ComponentScan)、
                //    @Import、ImportResource、@Bean等
                // 【重要：基于Java配置模式下，解析配置类和注册BeanDefinition】
                this.invokeBeanFactoryPostProcessors(beanFactory);

				/** 6 在生成bean之前需要先注册bean后置处理器
				  * 例如： 
				  * AutowiredAnnotationBeanPostProcessor(处理被@Autowired注解修饰的bean并注入) 
				  * RequiredAnnotationBeanPostProcessor(处理被@Required注解修饰的方法) 
				  * CommonAnnotationBeanPostProcessor(处理@PreDestroy、@PostConstruct、@Resource等多个注解的作用)等。
				  */
                this.registerBeanPostProcessors(beanFactory);

				// 7 初始化国际化资源处理器
                this.initMessageSource();

				// 8 创建事件多播器
                this.initApplicationEventMulticaster();

				// 9 模板方法，在容器刷新的时候可以自定义逻辑，不同的Spring容器做不同的事情。
                this.onRefresh();

				// 10 注册监听器，广播early application events
                this.registerListeners();		
                
                // 11 循环bean定义池，实例化剩余的单例bean，这里是真正的ioc和bean的加载过程!!!!!	
                // 【重要: 实例化、初始化bean】
                this.finishBeanFactoryInitialization(beanFactory);
                
                this.finishRefresh();
            } catch (BeansException var9) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + var9);
                }
                this.destroyBeans();
                this.cancelRefresh(var9);
                throw var9;
            } finally {
                this.resetCommonCaches();
            }
        }
    }
```
refresh方法中有两个非常重要的方法
```text
invokeBeanFactoryPostProcessors(beanFactory); // 注册beanDefinition

finishBeanFactoryInitialization(beanFactory); // 实例化、初始化bean
```

#### invokeBeanFactoryPostProcessors(beanFactory)方法

调用**BeanFactoryPostProcessor**后置处理器的实现类**ConfigurationClassPostProcessor**，
去解析配置类上边的注解，解析完成后扫描配置类上所有的@Import、@ComponentScan等注解所包含的类，
通过Component过滤器过滤掉不需要的类后，把有用的类注册成bean定义！

注意：
   ```
      1) @ComponentScan扫描的类会被优先注册bean定义;
      2) @Import、@ImportResource导入的类首先被加入到Map中，并延迟注册！ 
         注意：通过@Import可以为容器中导入bean定义，多用于集成其他组件，
         例如springboot的自动配置，就是用的@Import导入的多种组件bean定义到容器。
      3) 所有的 bean定义 被存储在一个Map的数据结构中 
         （key = “bean名字首字母小写，例如userController” ， value =userController的bean定义 ），
         每个对象对应一个bean定义，bean定义包含的属性在AbstractBeanDenifition中，
         包括Class信息、是否抽象、是否懒加载等等！
   ```

**invokeBeanFactoryPostProcessors方法核心逻辑：**

AbstractApplicationContext.invokeBeanFactoryPostProcessors(beanFactory)方法：
```text
	protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
		// 【调用BeanFactory后置处理器】
		PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

		// Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
		// (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
		if (!NativeDetector.inNativeImage() && beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}
	}
```
进入PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory)方法：
```text
	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// 定义了一个Set，装载BeanName，后面会根据这个Set，来判断后置处理器是否被执行过了。
		Set<String> processedBeans = new HashSet<>();
		
		// 判断beanFactory是不是BeanDefinitionRegistry的实例，当然肯定是的
		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			// 定义了两个List:
			// 一个是regularPostProcessors，用来装载BeanFactoryPostProcessor，一般情况下，这里永远都是空的，只有手动add beanFactoryPostProcessor，这里才会有数据
			// 一个是registryProcessors，用来装载BeanDefinitionRegistryPostProcessor
			// 其中BeanDefinitionRegistryPostProcessor扩展了BeanFactoryPostProcessor。
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
			
			// 循环传进来的beanFactoryPostProcessors，一般情况下，这里永远都是空的,只有手动add beanFactoryPostProcessor，这里才会有数据
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				//判断postProcessor是不是BeanDefinitionRegistryPostProcessor
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					// 是的话，执行postProcessBeanDefinitionRegistry方法
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					// 然后把对象装到registryProcessors里面去
					registryProcessors.add(registryProcessor);
				}
				else {
					// 不是的话，就装到regularPostProcessors。
					regularPostProcessors.add(postProcessor);
				}
			}

			// 定义了一个临时变量：currentRegistryProcessors，用来装载BeanDefinitionRegistryPostProcessor。
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// 【重要】是根据类型查到BeanNames,般情况下，只会找到一个，就是ConfigurationAnnotationProcessor
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					// 判断此后置处理器是否实现了PriorityOrdered接口,
					// 如果实现了，把它添加到currentRegistryProcessors这个临时变量中
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					// 放入processedBeans，代表这个后置处理已经被处理过了。当然现在还没有处理，但是马上就要处理了。。。
					processedBeans.add(ppName);
				}
			}
			// 进行排序，PriorityOrdered是一个排序接口，如果实现了它，就说明此后置处理器是有顺序的，所以需要排序
			// 当然目前这里只有一个后置处理器，就是ConfigurationClassPostProcessor。
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			// 合并
			registryProcessors.addAll(currentRegistryProcessors);
			
			// 【非常重要】在此处回调的currentRegistryProcessors中的ConfigurationClassPostProcessor中的postProcessBeanDefinitionRegistry方法，
			// 依次解析`@PropertySource`、`@ComponentScan`、`@Import`、`@ImportResource`、`@Bean`等注解,并注册成bean定义
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			
			// 清空currentRegistryProcessors，因为currentRegistryProcessors是一个临时变量，已经完成了目前的使命，所以需要清空，当然后面还会用到。
			currentRegistryProcessors.clear();

			// 然后处理实现了Ordered接口的BeanDefinitionRegistryPostProcessor
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);
			
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			
			currentRegistryProcessors.clear();

			// 最后,处理没有实现Ordered接口的BeanDefinitionRegistryPostProcessor
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}

			// 上面的代码是执行子类独有的方法，这里需要再把父类的方法也执行一次
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// Invoke factory processors registered with the context instance.
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// 上面处理的是实现了BeanDefinitionRegistryPostProcessor接口的类
		
		// ==================================================================================
		
		// 下面处理实现了BeanFactoryPostProcessor接口实现类，其过程与上边的类似！
		// 找到BeanFactoryPostProcessor实现类的BeanName数组
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// 创建三个集合，用来保存实现了PriorityOrdered、Ordered、以及无实现 三种bean
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			// 如果实现了PriorityOrdered接口，加入到priorityOrderedPostProcessors
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			// 如果实现了Ordered接口，加入到orderedPostProcessorNames
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
			// 如果既没有实现PriorityOrdered，也没有实现Ordered。加入到nonOrderedPostProcessorNames
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// 分别处理三个数组！
		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		beanFactory.clearMetadataCache();
	}
```
如上就是回调 bean工厂 后置处理器invokeBeanFactoryPostProcessors的具体逻辑。

其中**invokeBeanDefinitionRegistryPostProcessors**方法中
会依次解析@PropertySource、@ComponentScan、@Import、@ImportResource、@Bean等注解,并注册成bean定义。

进入PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(registry)方法：
```text
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry, ApplicationStartup applicationStartup) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			StartupStep postProcessBeanDefRegistry = applicationStartup.start("spring.context.beandef-registry.post-process")
					.tag("postProcessor", postProcessor::toString);
			postProcessor.postProcessBeanDefinitionRegistry(registry);
			postProcessBeanDefRegistry.end();
		}
	}
```
进入ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry(registry)方法：
```text
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		int registryId = System.identityHashCode(registry);
		if (this.registriesPostProcessed.contains(registryId)) {
			throw new IllegalStateException(
					"postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);
		}
		if (this.factoriesPostProcessed.contains(registryId)) {
			throw new IllegalStateException(
					"postProcessBeanFactory already called on this post-processor against " + registry);
		}
		this.registriesPostProcessed.add(registryId);
        // 【重要: 处理配置类】
		processConfigBeanDefinitions(registry);
	}
```
进入ConfigurationClassPostProcessor#processConfigBeanDefinitions(registry)方法：
```text
	public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
		List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
		String[] candidateNames = registry.getBeanDefinitionNames();

		for (String beanName : candidateNames) {
			BeanDefinition beanDef = registry.getBeanDefinition(beanName);
			if (beanDef.getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Bean definition has already been processed as a configuration class: " + beanDef);
				}
			}
			else if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
				configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
			}
		}

		// Return immediately if no @Configuration classes were found
		if (configCandidates.isEmpty()) {
			return;
		}

		// Sort by previously determined @Order value, if applicable
		configCandidates.sort((bd1, bd2) -> {
			int i1 = ConfigurationClassUtils.getOrder(bd1.getBeanDefinition());
			int i2 = ConfigurationClassUtils.getOrder(bd2.getBeanDefinition());
			return Integer.compare(i1, i2);
		});

		// Detect any custom bean name generation strategy supplied through the enclosing application context
		SingletonBeanRegistry sbr = null;
		if (registry instanceof SingletonBeanRegistry) {
			sbr = (SingletonBeanRegistry) registry;
			if (!this.localBeanNameGeneratorSet) {
				BeanNameGenerator generator = (BeanNameGenerator) sbr.getSingleton(
						AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR);
				if (generator != null) {
					this.componentScanBeanNameGenerator = generator;
					this.importBeanNameGenerator = generator;
				}
			}
		}

		if (this.environment == null) {
			this.environment = new StandardEnvironment();
		}

		// Parse each @Configuration class 
		// 1、设置【@Configuration注解解析器】
		ConfigurationClassParser parser = new ConfigurationClassParser(
				this.metadataReaderFactory, this.problemReporter, this.environment,
				this.resourceLoader, this.componentScanBeanNameGenerator, registry);

		Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
		Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
		do {
			StartupStep processConfig = this.applicationStartup.start("spring.context.config-classes.parse");
			// 2、【重要: 解析 配置类】
			parser.parse(candidates);
			// 3、【重要: 验证 配置类】
			parser.validate();

			Set<ConfigurationClass> configClasses = new LinkedHashSet<>(parser.getConfigurationClasses());
			configClasses.removeAll(alreadyParsed);

			// Read the model and create bean definitions based on its content
			if (this.reader == null) {
			    // 4、配置类 beanDefinition 读取器
				this.reader = new ConfigurationClassBeanDefinitionReader(
						registry, this.sourceExtractor, this.resourceLoader, this.environment,
						this.importBeanNameGenerator, parser.getImportRegistry());
			}
			// 5、【重要: 根据读取器 加载beanDefinition】
			this.reader.loadBeanDefinitions(configClasses);
			
			alreadyParsed.addAll(configClasses);
			processConfig.tag("classCount", () -> String.valueOf(configClasses.size())).end();

			candidates.clear();
			if (registry.getBeanDefinitionCount() > candidateNames.length) {
				String[] newCandidateNames = registry.getBeanDefinitionNames();
				Set<String> oldCandidateNames = new HashSet<>(Arrays.asList(candidateNames));
				Set<String> alreadyParsedClasses = new HashSet<>();
				for (ConfigurationClass configurationClass : alreadyParsed) {
					alreadyParsedClasses.add(configurationClass.getMetadata().getClassName());
				}
				for (String candidateName : newCandidateNames) {
					if (!oldCandidateNames.contains(candidateName)) {
						BeanDefinition bd = registry.getBeanDefinition(candidateName);
						if (ConfigurationClassUtils.checkConfigurationClassCandidate(bd, this.metadataReaderFactory) &&
								!alreadyParsedClasses.contains(bd.getBeanClassName())) {
							candidates.add(new BeanDefinitionHolder(bd, candidateName));
						}
					}
				}
				candidateNames = newCandidateNames;
			}
		}
		while (!candidates.isEmpty());

		// Register the ImportRegistry as a bean in order to support ImportAware @Configuration classes
		if (sbr != null && !sbr.containsSingleton(IMPORT_REGISTRY_BEAN_NAME)) {
			sbr.registerSingleton(IMPORT_REGISTRY_BEAN_NAME, parser.getImportRegistry());
		}

		if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory) {
			// Clear cache in externally provided MetadataReaderFactory; this is a no-op
			// for a shared cache since it'll be cleared by the ApplicationContext.
			((CachingMetadataReaderFactory) this.metadataReaderFactory).clearCache();
		}
	}
```
进入ConfigurationClassParser#parse(configCandidates)方法：
```text
	public void parse(Set<BeanDefinitionHolder> configCandidates) {
		for (BeanDefinitionHolder holder : configCandidates) {
			BeanDefinition bd = holder.getBeanDefinition();
			try {
				if (bd instanceof AnnotatedBeanDefinition) {
					parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
				}
				else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
					parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
				}
				else {
					parse(bd.getBeanClassName(), holder.getBeanName());
				}
			}
			catch (BeanDefinitionStoreException ex) {
				throw ex;
			}
			catch (Throwable ex) {
				throw new BeanDefinitionStoreException(
						"Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
			}
		}

		this.deferredImportSelectorHandler.process();
	}
	
	protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
		// 【解析 配置类】
		processConfigurationClass(new ConfigurationClass(metadata, beanName), DEFAULT_EXCLUSION_FILTER);
	}
```
进入ConfigurationClassParser#parse(configClass)方法：
```text
	protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) throws IOException {
		if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {
			return;
		}

		ConfigurationClass existingClass = this.configurationClasses.get(configClass);
		if (existingClass != null) {
			if (configClass.isImported()) {
				if (existingClass.isImported()) {
					existingClass.mergeImportedBy(configClass);
				}
				// Otherwise ignore new imported config class; existing non-imported class overrides it.
				return;
			}
			else {
				// Explicit bean definition found, probably replacing an import.
				// Let's remove the old one and go with the new one.
				this.configurationClasses.remove(configClass);
				this.knownSuperclasses.values().removeIf(configClass::equals);
			}
		}

		// Recursively process the configuration class and its superclass hierarchy.
		SourceClass sourceClass = asSourceClass(configClass, filter);
		do {
		    // 【重要: 实际解析 配置类】
			sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);
		}
		while (sourceClass != null);

		this.configurationClasses.put(configClass, configClass);
	}
```
进入ConfigurationClassParser#doProcessConfigurationClass(configClass)方法：
```text
	@Nullable
	protected final SourceClass doProcessConfigurationClass(
			ConfigurationClass configClass, SourceClass sourceClass, Predicate<String> filter)
			throws IOException {
		// 1、递归处理内部类，一般不会写内部类
		if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
			processMemberClasses(configClass, sourceClass, filter);
		}

		// 2、处理@PropertySource注解，@PropertySource注解用来加载properties文件
		for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), PropertySources.class,
				org.springframework.context.annotation.PropertySource.class)) {
			if (this.environment instanceof ConfigurableEnvironment) {
				processPropertySource(propertySource);
			}
			else {
				logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
						"]. Reason: Environment must implement ConfigurableEnvironment");
			}
		}

        // 3、处理ComponentScan注解
		// 获得ComponentScan注解具体的内容，
		// ComponentScan注解除了最常用的basePackage之外，还有includeFilters，excludeFilters等
		Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
		if (!componentScans.isEmpty() &&
				!this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
			// 循环处理componentScans
			for (AnnotationAttributes componentScan : componentScans) {
				// 核心方法！执行扫描解析 parse，把扫描出来的放入set
				Set<BeanDefinitionHolder> scannedBeanDefinitions =
						this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
				// 循环set，判断是否是配置类
				// 是的话，递归调用parse方法，因为被扫描出来的类，还是一个配置类，有@ComponentScans注解，或者其中有被@Bean标记的方法 等等，所以需要再次被解析。
				for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
					BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
					if (bdCand == null) {
						bdCand = holder.getBeanDefinition();
					}
					// 如果是配置类，有@ComponentScans注解，递归解析
					if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
						parse(bdCand.getBeanClassName(), holder.getBeanName());
					}
				}
			}
		}

		// 4、处理@Import注解，@Import是Spring中很重要的一个注解，
		// 正是由于它的存在，让Spring非常灵活，不管是Spring内部，还是与Spring整合的第三方技术，都大量的运用了@Import注解
		// @Import有三种情况：
		// 1.Import 普通类，将其作为 @Configuration 类
		// 2.Import ImportSelector，【非常重要: SpringBoot自动配置原理就是通过这种方式实现的】
		// 3.Import ImportBeanDefinitionRegistrar【注册 特定的beanDefinition】
		processImports(configClass, sourceClass, getImports(sourceClass), filter, true);

		// 处理@ImportResource注解
		AnnotationAttributes importResource =
				AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
		if (importResource != null) {
			String[] resources = importResource.getStringArray("locations");
			Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
			for (String resource : resources) {
				String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
				configClass.addImportedResource(resolvedResource, readerClass);
			}
		}

		// 5、处理@Bean的方法，可以看到获得了带有@Bean的方法后，不是马上转换成BeanDefinition，而是先用一个set接收
		Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
		for (MethodMetadata methodMetadata : beanMethods) {
			configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
		}

		// Process default methods on interfaces
		processInterfaces(configClass, sourceClass);

		// 处理父类的东西
		if (sourceClass.getMetadata().hasSuperClass()) {
			String superclass = sourceClass.getMetadata().getSuperClassName();
			if (superclass != null && !superclass.startsWith("java") &&
					!this.knownSuperclasses.containsKey(superclass)) {
				this.knownSuperclasses.put(superclass, configClass);
				// Superclass found, return its annotation metadata and recurse
				return sourceClass.getSuperClass();
			}
		}

		// No superclass -> processing is complete
		return null;
	}
```

**@Controller、@Service、@Component等注解标注的类是如何被注册成bean定义的？**
```text
想要将@Controller、@Service、@Component等注解标注的类注册成bean定义的，首先要被扫描到！
如上代码所示！这些注解的扫描是发生在Spring解析@ComponentScan时触发的！
这些注解虽然名字不同，但都继承于@Component注解。
```

进入解析@ComponentScan的核心方法parse():
```text
	public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
		// 可以看到上来就初始化了一个路径扫描器！
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,
				componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);
				
		。。。。。。 // 省略
```
这个路径扫描器ClassPathBeanDefinitionScanner中定义了两种过滤器：
```text
包含过滤器：includeFilters ，可根据注解过滤，必须扫描某些注解
排除过滤器：excludeFilters，可根据注解过滤，使其不扫描@Controller等注解
```
后续会把扫描到的类与这两个过滤器做匹配，匹配成功的才可以注册bean定义！
而在new ClassPathBeanDefinitionScanner初始化扫描器时，默认把@Component注解加入了包含过滤器includeFilters中，
所以后续扫描就可以扫描到@Controller、@Service、@Component等注解标注的类。
```text
	protected void registerDefaultFilters() {
		// 把`@Component`注解加入了包含过滤器`includeFilters `中
		this.includeFilters.add(new AnnotationTypeFilter(Component.class));
		ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
	}
```

再扫描时会进入findCandidateComponents，寻找候选者类
```text
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
		if (this.componentsIndex != null && indexSupportsIncludeFilters()) {
			return addCandidateComponentsFromIndex(this.componentsIndex, basePackage);
		} else {
			// 会进入到这个if
			return scanCandidateComponents(basePackage);
		}
	}
```

scanCandidateComponents（）方法如下：
```text
private Set<BeanDefinition> scanCandidateComponents(String basePackage) {
		Set<BeanDefinition> candidates = new LinkedHashSet<>();
		try {
			// 把 传进来的类似 命名空间形式的字符串转换成类似类文件地址的形式，然后在前面加上classpath*:
			// 即：com.xx=>classpath*:com/xx/**/*.class
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(basePackage) + '/' + this.resourcePattern;
			// 根据packageSearchPath，获得符合要求的文件
			Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
			boolean traceEnabled = logger.isTraceEnabled();
			boolean debugEnabled = logger.isDebugEnabled();
			// 循环资源
			for (Resource resource : resources) {
				if (traceEnabled) {
					logger.trace("Scanning " + resource);
				}

				if (resource.isReadable()) {// 判断资源是否可读，并且不是一个目录
					try {
						// metadataReader 元数据读取器，解析resource，也可以理解为描述资源的数据结构
						MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
						
						// 核心逻辑！在isCandidateComponent方法内部会真正执行匹配规则
						// 注册配置类自身会被排除，不会进入到这个if
						if (isCandidateComponent(metadataReader)) {
							ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
							sbd.setResource(resource);
							sbd.setSource(resource);
							if (isCandidateComponent(sbd)) {
								if (debugEnabled) {
									logger.debug("Identified candidate component class: " + resource);
								}
								// 把符合条件的放入结合
								candidates.add(sbd);
							}
							
							...... // 省略
				// 返回集合！	
				return candidates;
		}

```

核心匹配规则isCandidateComponent()如下：
```text
	protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
		// 匹配excludeFilters排除过滤器中的类
		for (TypeFilter tf : this.excludeFilters) {
			if (tf.match(metadataReader, getMetadataReaderFactory())) {
				return false;
			}
		}
		// 匹配 includeFilters 包含过滤器中的类
		for (TypeFilter tf : this.includeFilters) {
			if (tf.match(metadataReader, getMetadataReaderFactory())) {
				return isConditionMatch(metadataReader);
			}
		}
		return false;
	}
```

**如何修改bean定义？**

我们可以通过实现BeanFactoryPostProcessor来修改bean定义的属性，使类根据我们的要求完成初始化，如下所示:
```text
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		// 获取bean定义
		GenericBeanDefinition rootBeanDefinition = (GenericBeanDefinition) beanFactory.getBeanDefinition("instA");

		/**
		 * 修改自动注入模型
		 * 属性默认使用AutoWired自动注入
		 * 下面修改为按照类型自动注入
 		 */
		rootBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

		/**
		 * 修改beanclass，实例化时根据此处设置的class进行实例化
		 */
		rootBeanDefinition.setBeanClass(InstD.class);
		
		/**
		 * 修改懒加载方式
		 */
		rootBeanDefinition.setLazyInit(true);

		/**
		 * 修改默认调用构造器方式
		 * 以前是默认空参构造器，现在是默认调用带有String参数的构造器
		 */
		GenericBeanDefinition genericBeanDefinition =
				(GenericBeanDefinition) beanFactory.getBeanDefinition("person");

		ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();

		constructorArgumentValues.addIndexedArgumentValue(0,"adc");

		genericBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);
	}
}
```
该自定义的bean工厂后置处理器MyBeanFactoryPostProcessor就在invokeBeanFactoryPostProcessors方法中被执行！
类比形象图
![beanUpdateSimpleLifecycle.png](img/05/beanUpdateSimpleLifecycle.png)

**解析@Import注解过程：**
![parseImportAnnotationProcess.png](img/05/parseImportAnnotationProcess.png)
注意：
```text
1) @ComponentScan所扫描到的类(@Component)会立刻注册成bean定义;
2) 但是对于@Bean、@Import注解所标注的类，在parser.parse(candidates)解析时,会先将其放在一个Set集合中，延迟注册。
    等到parser.parse(candidates)方法执行完毕后，在reader.loadBeanDefinitions(configClasses)方法中进行注册bean定义！
3) 对于DeferredImportSelector类，则是在最后进行处理的（@Import之后），
    spring boot加载自动配置类时就用的是DeferredImportSelector类。
    它在parser.parse(candidates)方法中，处理它的方法：
    deferredImportSelectorHandler.process()是放在最后执行的！
```

#### finishBeanFactoryInitialization(beanFactory)方法

为什么要单独介绍这个方法呢，因为这个方法里边涵盖了bean的加载过程，是本章的重点！
它的主要作用是实例化所有剩余的(非懒加载、单例)的bean。
比如上文第5个方法invokeBeanFactoryPostProcessors方法中根据各种注解解析出来的类，在这个时候都会被初始化。 
实例化的过程各种BeanPostProcessor开始起作用。

首先从Bean定义池中拿到所有的bean定义，把bean定义合并成RootBeanDefinition，因为各种bean定义是不一样的，
所以要合成统一类型的RootBeanDefinition，方便后续判断，并检验是否是单例、是否抽象、是否是工厂bean等是否符合生产标准，
然后执行doGetBean方法创建实例。实例又分两种：
```text
如果是FactoryBean，则通过getObject()方法获取实例，源码会在第4个标题中讲解
如果是普通bean，则通过createBean()方法去创建实例
```
```text
protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
    @Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {

		// transformedBeanName: 去掉 & ，获取真正的实例名 ，而不是 “&实例名“，
		// 注意： “&实例名“ 是用来获取FactoryBean对象本身的，而不是获取FactoryBean的getObject()方法
		final String beanName = transformedBeanName(name);
		Object bean;

		// 从单例池中拿对象
		Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null && args == null) {
			if (logger.isTraceEnabled()) {
				if (isSingletonCurrentlyInCreation(beanName)) {
					logger.trace("Returning eagerly cached instance of singleton bean '" + beanName +
							"' that is not fully initialized yet - a consequence of a circular reference");
				}
				else {
					logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
				}
			}
			// 如果是FactoryBean的话，就调用getObject方法获取实例，下文会讲
			// 如果不是，返回实例本身
			bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
		}

			...... // 省略代码

			try {
			    // 合成统一类型的RootBeanDefinition
				final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
				
				// 检查是否抽象
				checkMergedBeanDefinition(mbd, beanName, args);

				// 检查是否有其他依赖的类
				String[] dependsOn = mbd.getDependsOn();
				
					...... // 省略代码
					
					try {
					    // 【重要】这个getSingleton并不是去单例池中获取，而是创建bean的方法
						Object scopedInstance = scope.get(beanName, () -> {
							beforePrototypeCreation(beanName);
							try {
								// 钩子函数创建bean
								return createBean(beanName, mbd, args);
							}
							finally {
								afterPrototypeCreation(beanName);
							}
						});
						bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
					}
					
			...... // 省略代码
			
		return (T) bean;
	}
```

在这里先来分析普通bean的创建过程，主要是以下代码：
```text
	 if (mbd.isSingleton()) {
	 		         // 这个getSingleton并不是去单例池中获取，而是创建bean的方法
	                 sharedInstance = this.getSingleton(beanName, () -> {
	                     try {
	                     	 //钩子函数创建bean
	                         return this.createBean(beanName, mbd, args);
	                     } catch (BeansException var5) {
	                         this.destroySingleton(beanName);
	                         throw var5;
	                     }
	                 });
	                 bean = this.getObjectForBeanInstance(sharedInstance, name, beanName, mbd);

```

接下来看一下getSingleton方法！
```text
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
   			
   				......  // 省略代码
   				
				// 标记为正在创建
                this.beforeSingletonCreation(beanName);
                boolean newSingleton = false;
                boolean recordSuppressedExceptions = this.suppressedExceptions == null;
                if (recordSuppressedExceptions) {
                    this.suppressedExceptions = new LinkedHashSet();
                }

                try {
                	// 函数接口调用，获取创建的对象，此时调用的lambda式创建的createBean方法
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                } catch (IllegalStateException var16) {
                
              	......  // 省略代码
```

通过singletonFactory.getObject()获取创建的对象，此时调用的lambda式创建的createBean方法，
再继续深入doCreateBean方法，这个方法又做了一堆一堆的事情，但是值得开心的事情就是 我们已经找到了我们要寻找的东西了。

**bean实例的创建分为以下几步：**

①：创建实例
```text
instanceWrapper = createBeanInstance(beanName, mbd, args); // 创建bean的实例。核心
```
创建实例的源码解读！
创建实例的步骤如下：
```text
1) 调用 Supplier 接口创建实例 - obtainFromSupplier
    I) 若 RootBeanDefinition 中设置了 Supplier 则使用 Supplier 提供的bean替代Spring要生成的bean。
        主要是为了替代工厂方法（包含静态工厂）或者构造器创建对象，但是其后面的生命周期回调不影响。
    II) 因为不管是静态工厂还是工厂方法，都需要通过反射调用目标方法创建对象，反射或多或少影响性能，如果不使用反射呢？
        Supplier就是面向java8函数式接口编程，就是提供一个回调方法，直接调用回调方法即可，不需要通过反射了。
        主要是考虑反射调用目标方法不如直接调用目标方法效率高。
2) 使用 factory-method 属性创建实例 - instantiateUsingFactoryMethod
    I) 如果RootBeanDefinition 中存在 factoryMethodName 属性，或者在配置文件中配置了factory-method，
        Spring会尝试使用 instantiateUsingFactoryMethod 方法;
    II) 在 xml配置中，可以使用 factory-bean 和 factory-method 两个标签可以指定一个类中的方法，
        Spring会将这个指定的方法的返回值作为bean返回;
    III) 在注解配置中， 会解析@Bean修饰的方法。并将返回结果注入到Spring容器中。
        解析时BeanDefinition 的 factoryMethodName 正是 @Bean修饰的方法本身。
3) 使用构造器创建实例
    I) Spring则打算通过bean的构造函数来创建bean。
        但是一个bean可能会存在多个构造函数，这时候Spring会根据参数列表的来判断使用哪个构造函数进行实例化。
        但是判断过程比较消耗性能，所以Spring将判断好的构造函数缓存到RootBeanDefinition 中;
    II) 如果缓存中不存在，则进行构造函数的筛选并缓存解析结果。过程如下:
         解析构造函数参数
         获取候选的构造函数列表
         解析构造函数参数个数
         寻找最匹配的构造函数
    III) 如果无参数传入，则使用无参构造器实例化！
    IV) 使用构造器创建实例，主要两种实例化 SimpleInstantiationStrategy 和 CglibSubclassingInstantiationStrategy，
        简单实例化策略(直接反射) 和 Cglib 动态代理策略(通过cglib 代理)，默认第二种。
```

注意：如果有两个类型相同的bean
```text
在spring中：后创建的覆盖先创建的，@Bean覆盖@Component。
而在springBoot中：由于在springBoot启动时设置了不允许覆盖，所以有多个bean时，会抛异常！
```

**②：加入三级缓存，解决循环依赖**
```text
		boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
				isSingletonCurrentlyInCreation(beanName));
		if (earlySingletonExposure) {
			if (logger.isTraceEnabled()) {
				logger.trace("Eagerly caching bean '" + beanName +
						"' to allow for resolving potential circular references");
			}
			// 加入三级缓存
			addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
		}
```

**③：填充属性**
```text
populateBean(beanName, mbd, instanceWrapper);//填充属性
exposedObject = this.initializeBean(beanName, exposedObject, mbd);//aware系列接口的回调
```
填充属性源码分析！

属性注入源码中有如下五种方式

| 注入方式	                                         | 变量	                   | 变量值 |
|-----------------------------------------------|-----------------------|-----|
| 没有显式配置上装配的方式                                  | 	AUTOWIRE_NO          | 	0  |
| 按beanName进行装配                                 | 	AUTOWIRE_BY_NAME     | 	1  |
| 按type进行装配                                     | 	AUTOWIRE_BY_TYPE     | 	2  |
| 在构造函数中进行装配                                    | 	AUTOWIRE_CONSTRUCTOR | 	3  |
| 通过内省bean类确定适当的自动装配策略,Spring已经将其标注为@Deprecated | 	AUTOWIRE_AUTODETECT  | 	4  |

一般以注解的形式（@Autowired,@Bean）注入的属性，都会由bean的后置处理器进行注入，
在进行自动装配的过程中，默认按照"byType"的方式进行Bean加载，
如果出现无法挑选出合适的Bean的情况，再将属性名与候选Bean名单中的beanName进行对比。
当做完这一步，Bean对象基本是完整的了，可以理解为Autowired注解已经解析完毕，依赖注入完成了。

**④：回调各种 Aware 接口**
```text
populateBean(beanName, mbd, instanceWrapper); // 填充属性
exposedObject = this.initializeBean(beanName, exposedObject, mbd); // aware系列接口的回调
```

可以看到在populateBean填充属性方法后边还有一个initializeBean方法，里边主要回调了一些aware接口！
如果bean实现了以下三种aware接口，即可在这里执行aware回调逻辑！
```text
bean implements BeanNameAware
bean implements BeanClassLoaderAware
bean implements BeanFactoryAware
```

进入initializeBean方法，
```text
	protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
		if (System.getSecurityManager() != null) {
			AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
				invokeAwareMethods(beanName, bean);
				return null;
			}, getAccessControlContext());
		}
		else {
			// 1.回调各种 Aware 接口
			invokeAwareMethods(beanName, bean);
		}

		Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
			// 2.调用 BeanPostProcessorsBeforeInitialization 扩展
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}

		try {
			// 3.调用实现InitializingBean的afterPropertiesSet方法
			// 调用xml方式的 bean标签里配置init-mothod属性
			invokeInitMethods(beanName, wrappedBean, mbd);
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					(mbd != null ? mbd.getResourceDescription() : null),
					beanName, "Invocation of init method failed", ex);
		}
		if (mbd == null || !mbd.isSynthetic()) {
			// 4.调用 BeanPostProcessorsAfterInitialization 扩展
			wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		}

		return wrappedBean;
	}
```

回调各种 Aware 接口主要是在invokeInitMethods这个方法中回调的
```text
    private void invokeAwareMethods(String beanName, Object bean) {
        if (bean instanceof Aware) {
        
        	// 1.回调了BeanNameAware
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware)bean).setBeanName(beanName);
            }
            
        	// 2.回调了BeanClassLoaderAware
            if (bean instanceof BeanClassLoaderAware) {
                ClassLoader bcl = this.getBeanClassLoader();
                if (bcl != null) {
                    ((BeanClassLoaderAware)bean).setBeanClassLoader(bcl);
                }
            }
            
        	// 3.回调了BeanFactoryAware
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware)bean).setBeanFactory(this);
            }
        }

    }
```

**⑤：调用 BeanPostProcessorsBeforeInitialization**
```text
   if (mbd == null || !mbd.isSynthetic()) {
       wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
   }
```

可以看到第③步的invokeAwareMethods最多只调用了三个Aware，那么其他的Aware在哪里调用呢？
在bean后置处理器的BeanPostProcessorsBeforeInitialization方法中调用的。
```text
 /*  各种 Aware 接口
 * @see org.springframework.context.EnvironmentAware
 * @see org.springframework.context.EmbeddedValueResolverAware
 * @see org.springframework.context.ResourceLoaderAware
 * @see org.springframework.context.ApplicationEventPublisherAware
 * @see org.springframework.context.MessageSourceAware
 * @see org.springframework.context.ApplicationContextAware
 * @see org.springframework.context.support.AbstractApplicationContext#refresh()
 */
 //ApplicationContextAwareProcessor 实现了 bean后置处理器
class ApplicationContextAwareProcessor implements BeanPostProcessor {

	//在postProcessBeforeInitialization 方法中调用各种Aware接口！
	@Override
	@Nullable
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (!(bean instanceof EnvironmentAware || bean instanceof EmbeddedValueResolverAware ||
				bean instanceof ResourceLoaderAware || bean instanceof ApplicationEventPublisherAware ||
				bean instanceof MessageSourceAware || bean instanceof ApplicationContextAware)){
			return bean;
		}
		// ...... 省略代码
		return bean;
	}
}
```
图示如下：
![beanProcessAware.png](img/05/beanProcessAware.png)

当然我们也可以实现bean的后置处理器，重写postProcessBeforeInitialization方法，执行某些业务逻辑

**⑥：初始化回调**
```text
invokeInitMethods(beanName, wrappedBean, mbd);
```
一个bean想要在初始化完成时，做某些操作，有以下三种方法，执行顺序自上向下！
```text
类中某方法加注解@PostConstruct
实现InitializingBean接口，实现afterPropertiesSet方法
xml方式，bean标签里配置init-mothod属性，指向类中的方法。
```

**⑦：调用 BeanPostProcessorsAfterInitialization**
```text
	if (mbd == null || !mbd.isSynthetic()) {
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }
```

在第④步调用的是BeanPostProcessors**Before**Initialization的postProcessBeforeInitialization方法，
现在调用的是BeanPostProcessors**After**Initialization的postProcessAfterInitialization方法！

注意：无论是实例化、属性赋值都调用了bean后置处理器，对功能进行增强！

**⑦：最后把对象加入一级缓存中**
```text
this.addSingleton(beanName, singletonObject);
```

经过上边的几步，单例对象创建才创建完成！

如何自定义一个BeanPostProcessor ？
```text
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

	//重写 postProcessBeforeInitialization 方法
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    	
        if(beanName.equals("car")) {
            System.out.println("postProcessBeforeInitialization");
        }
        return bean;
    }
    
	//重写 postProcessAfterInitialization方法
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    	
        if(beanName.equals("car")) {
            System.out.println("postProcessAfterInitialization");
        }
        return bean;
    }
}
```

如果获取car这个类时，就会打印
```text
postProcessBeforeInitialization
postProcessAfterInitialization
```

## IOC 和 bean 从加载到初始化过程总结
首先从new AnnotationConfigApplicationContext(MainConfig.class)进入初始化。
```text
1) 在构造器中实例化一个强大的bean工厂DefaultListableBeanFactory。

2) 在构造器中会实例化一个读取器reader，reader中注册了一些spring内部所需要的类，
    比如ConfigurationClassPostProcessor、处理事件的类、处理@Autoworied的类等。

3) 实例化一个扫描器scanner，用于手动扫描包，无关紧要。真正扫描我们系统的bean的其实不是它。

4) 调用invokeBeanFactoryPostProcessors方法解析配置类所包含的所有@ConmponentScan、@Import中所涉及的bean，
    把它们注册成bean定义，放进bean定义池中。

5) 执行完invokeBeanFactoryPostProcessors后，bean定义注册完毕。

6) 然后调用finishBeanFactoryInitialization方法，循环bean定义池，
    并判断是否符合生产标准，如果符合，就生产bean对象。

7) 在生产bean之前先从单例池中拿一次bean，没有的话，调用doCreateBean方法，
    通过反射创建bean、填充属性、执行生命周期回调等方式创建一个单例bean，最后放入单例池中。
```

## 基于XML配置的Spring IOC初始化过程

### 基于XML配置 Spring项目

maven pom.xml
```text
(同 基于Java配置 Spring项目)
```

Spring配置文件 spring-config.xml
```text
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
	    http://www.springframework.org/schema/aop
	    http://www.springframework.org/schema/aop/spring-aop.xsd
	    http://www.springframework.org/schema/mvc
	    http://www.springframework.org/schema/mvc/spring-mvc.xsd
	    http://www.springframework.org/schema/tx
	    http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!-- open annotation-driven -->
    <context:annotation-config />
    <context:component-scan base-package="org.example.component"/>

    <bean id="user" class="org.example.bean.User">
        <property name="name" value="luke" />
        <property name="age" value="33" />
    </bean>
</beans>
```

Java 代码
```java
public class App {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-config.xml");
        User user = (User) ac.getBean("user");
        System.out.println(user.getName() + ":::" + user.getAge());
    }
}

public class User {

    private String name;

    private Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}
```

### Spring IOC的创建

使用ClassPathXmlApplicationContext直接创建：
```text
    ApplicationContext ac = new ClassPathXmlApplicationContext("spring-config.xml");
```
进入ClassPathXmlApplicationContext构造器：
```text
	public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[] {configLocation}, true, null);
	}
	
	// 内部调用自身构造器
    public ClassPathXmlApplicationContext(
            String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
            throws BeansException {
    
        // 一系列父类构造器调用过程，父类实例变量初始化
        // 其中在AbstractApplicationContext类中创建了PathMatchingResourcePatternResolver对象用于对传入的资源文件加载进行处理
        super(parent);
        
        /**
         * 创建系统环境变量和jvm环境变量属性对象，
         * MutablePropertySources对象中存储了systemEnvironment(系统环境变量)、systemProperties(java启动参数)配置信息，
         * 并通过配置参数对传入的配置文件进行转换的过程，例如：application-${USER}.xml
         * 实现对${USER}的值处理过程，并把处理后的配置文件路径存储起来后续使用，
         * 转换过程是在PropertyPlaceholderHelper.parseStringValue方法实现的，不具体展开了。
         */
        setConfigLocations(configLocations);
        
        if (refresh) {
            // 【spring最重要的方法，调用父类AbstractApplicationContext.refresh()】
            refresh();
        }
    }
```
进入refresh方法：
```text
@Override
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        // Prepare this context for refreshing.
        /*
        1. 容器启动时间设置
        2. 容器启动标识设置为true
        3. 自定义属性配置加载，子类重写使用
        4. 必需的属性配置校验
        5. 父子容器，容器多次启动时的监听器配置工作
        */
        prepareRefresh();

        // Tell the subclass to refresh the internal bean factory.
        /*
        【此处是我们本次解析的重点：解析XML配置文件并注册Spring BeanDefinition】
        1. 完成对BeanFactory的创建
        2. 完成对xml文件的加载，解析
        3. 根据xml配置生成BeanDefinition信息的创建和注册
        */
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // Prepare the bean factory for use in this context.
        // 完成对beanFactory的配置工作，spel表达式处理类，环境配置对象的设置
        prepareBeanFactory(beanFactory);

        try {
            // Allows post-processing of the bean factory in context subclasses.
            // 留给子类重写使用的，可以做一些自定义的工作
            postProcessBeanFactory(beanFactory);

            // Invoke factory processors registered as beans in the context.
            // 完成对BeanFactoryPostProcess的调用(重要)，
            // 通过时间BeanFactoryPostProcess接口可以对BeanDefinition信息进行增加，修改等处理操作
            invokeBeanFactoryPostProcessors(beanFactory);

            // Register bean processors that intercept bean creation.
            // 执行对BeanPostProcess接口实现类的查找，创建，并注册进容器。
            // 后续创建对象时使用这些类对象完成对待创建对象的扩展工作
            registerBeanPostProcessors(beanFactory);

            // Initialize message source for this context.
            //国际化i18n支持
            initMessageSource();

            // Initialize event multicaster for this context.
            // 完成事件多播器的注册，观察者模式，用来管理事件监听器
            initApplicationEventMulticaster();

            // Initialize other special beans in specific context subclasses.
            // 留给子类重写使用，便于扩展
            onRefresh();

            // Check for listener beans and register them.
            // 完成对事件监听器查找，创建，注册功能
            registerListeners();

            // Instantiate all remaining (non-lazy-init) singletons.
            /*
            1. 注册值转换服务；ConversionService 类型的转换
            2. 注册值处理器；${}占位符的处理
            3. 冻结BeanDefition配置信息
            4. 完成对单例对象的创建过程（过程中会触发对BeanPostProcess的调用）（重要）
            */
            finishBeanFactoryInitialization(beanFactory);

            // Last step: publish corresponding event.
            /*
            1. 完成资源的清理
            2. 完成对bean生命周期的处理工作
            3. 发布容器刷新时间
            */
            finishRefresh();
        }

        catch (BeansException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Exception encountered during context initialization - " +
                        "cancelling refresh attempt: " + ex);
            }

            // Destroy already created singletons to avoid dangling resources.
            destroyBeans();

            // Reset 'active' flag.
            cancelRefresh(ex);

            // Propagate exception to caller.
            throw ex;
        }

        finally {
            // Reset common introspection caches in Spring's core, since we
            // might not ever need metadata for singleton beans anymore...
            // 完成对容器启动过程中生成的临时对象的清理工作
            resetCommonCaches();
        }
    }
}
```
进入AbstractApplicationContext.obtainFreshBeanFactory()方法：
```text
	protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
	    // 刷新BeanFactory【关键逻辑就在此处】
		refreshBeanFactory();
		// 只是单纯获取BeanFactory，没有重点
		return getBeanFactory();
	}
```
进入AbstractRefreshableApplicationContext.refreshBeanFactory()方法：
```text
	@Override
    protected final void refreshBeanFactory() throws BeansException {
        if (hasBeanFactory()) {
            destroyBeans();
            closeBeanFactory();
        }
        try {
            // 【非重点】创建beanFactory，而外部的ApplicationContext对象其实是对工厂对象提供的Faced模式对象，提供了更强大的功能和更简便的使用方式
            // 直接new DefaultListableBeanFactory
            DefaultListableBeanFactory beanFactory = createBeanFactory();
            beanFactory.setSerializationId(getId());
            
            // 【非重点】设置BeanFactory是否运行bean重写和循环依赖
            customizeBeanFactory(beanFactory);
            
            // 【重点】加载解析xml文件，创建BeanDefinition对象，BeanDefinition可以理解为对xml文件内配置信息的另一种存储格式
            // xml的配置方式和注解配置方式的配置信息不一致，为了有一个统一的bean创建过程
            // 所以对不同配置方式产生的配置信息使用了一种统一的数据格式。
            loadBeanDefinitions(beanFactory);
            
            this.beanFactory = beanFactory;
        } 
        catch (IOException ex) {
            throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
        }
    }
```
进入AbstractXmlApplicationContext.loadBeanDefinitions(beanFactory)方法：
```text
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        // 创建xml解析类
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
            
        // 加载xml所需的一些数据
        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);
        // 设置BeansDtdResolver beans元素 解析器
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
        
        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        initBeanDefinitionReader(beanDefinitionReader);
        // 【重要】加载xml，创建BeanDefinition对象
        loadBeanDefinitions(beanDefinitionReader);
    }
```
进入AbstractXmlApplicationContext.loadBeanDefinitions(beanDefinitionReader)方法：
```text
	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		Resource[] configResources = getConfigResources();
		if (configResources != null) {
			reader.loadBeanDefinitions(configResources);
		}
		// 【重要】根据我们在ClassPathXMLApplicationContext(String configLocation)来解析spring-config.xml文件
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			reader.loadBeanDefinitions(configLocations);
		}
	}
```
进入AbstractBeanDefinitionReader.loadBeanDefinitions(configLocations)方法：
```text
	@Override
	public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
		Assert.notNull(locations, "Location array must not be null");
		int count = 0;
		for (String location : locations) {
			count += loadBeanDefinitions(location);
		}
		return count;
	}
	
	@Override
	public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(location, null);
	}
	
	public int loadBeanDefinitions(String location, @Nullable Set<Resource> actualResources) throws BeanDefinitionStoreException {
		// 获取资源加载器
		ResourceLoader resourceLoader = getResourceLoader();
		if (resourceLoader == null) {
			throw new BeanDefinitionStoreException(
					"Cannot load bean definitions from location [" + location + "]: no ResourceLoader available");
		}

		if (resourceLoader instanceof ResourcePatternResolver) {
			// Resource pattern matching available.
			try {
			    // 【重要】将spring-config.xml配置文件加载为Resource
				Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
				// 【重要】在Resource中加载BeanDefinition
				int count = loadBeanDefinitions(resources);
				if (actualResources != null) {
					Collections.addAll(actualResources, resources);
				}
				if (logger.isTraceEnabled()) {
					logger.trace("Loaded " + count + " bean definitions from location pattern [" + location + "]");
				}
				return count;
			}
			catch (IOException ex) {
				throw new BeanDefinitionStoreException(
						"Could not resolve bean definition resource pattern [" + location + "]", ex);
			}
		}
		else {
		    // 同上，只是这里解析单个路径
			// Can only load single resources by absolute URL.
			Resource resource = resourceLoader.getResource(location);
			int count = loadBeanDefinitions(resource);
			if (actualResources != null) {
				actualResources.add(resource);
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Loaded " + count + " bean definitions from location [" + location + "]");
			}
			return count;
		}
	}
	
	@Override
	public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
		Assert.notNull(resources, "Resource array must not be null");
		int count = 0;
		for (Resource resource : resources) {
		    // 【重要】在Resource中加载BeanDefinition
			count += loadBeanDefinitions(resource);
		}
		return count;
	}
```
进入XmlBeanDefinitionReader.loadBeanDefinitions(resource)方法：
```text
	@Override
	public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(new EncodedResource(resource));
	}
	
	public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
        
        ...... // 非核心代码，省略
		
		try {
			InputStream inputStream = encodedResource.getResource().getInputStream();
			try {
				InputSource inputSource = new InputSource(inputStream);
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
				// 【重要】执行解析beanDefinition
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
			}
			finally {
				inputStream.close();
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(
					"IOException parsing XML document from " + encodedResource.getResource(), ex);
		}
		finally {
			currentResources.remove(encodedResource);
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();
			}
		}
	}
	
	protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
			throws BeanDefinitionStoreException {

		try {
		    // 【重要】加载文件为Document
			Document doc = doLoadDocument(inputSource, resource);
			// 【非常重要】解析doc并注册beanDefinition
			int count = registerBeanDefinitions(doc, resource);
			if (logger.isDebugEnabled()) {
				logger.debug("Loaded " + count + " bean definitions from " + resource);
			}
			return count;
		}
		
		...... // 进行一些异常捕获，省略
	}
	
	public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
		// 创建BeanDefinitionDocumentReader
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
		int countBefore = getRegistry().getBeanDefinitionCount();
		// 【重要】解析注册beanDefinition
		documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
		return getRegistry().getBeanDefinitionCount() - countBefore;
	}
```
我们先进入XmlBeanDefinitionReader.createReaderContext(resource)方法：
```text
	public XmlReaderContext createReaderContext(Resource resource) {
		return new XmlReaderContext(resource, this.problemReporter, this.eventListener,
				this.sourceExtractor, this, getNamespaceHandlerResolver());
	}
	
	// 获取Spring中命名空间处理解析器，用来解析专门的DTD格式
	public NamespaceHandlerResolver getNamespaceHandlerResolver() {
		if (this.namespaceHandlerResolver == null) {
			this.namespaceHandlerResolver = createDefaultNamespaceHandlerResolver();
		}
		return this.namespaceHandlerResolver;
	}
	
	protected NamespaceHandlerResolver createDefaultNamespaceHandlerResolver() {
		ClassLoader cl = (getResourceLoader() != null ? getResourceLoader().getClassLoader() : getBeanClassLoader());
		return new DefaultNamespaceHandlerResolver(cl);
	}
```
进入DefaultNamespaceHandlerResolver构造器方法：
```text
    // spring命名空间处理器的位置，每个spring-xxx.jar包中里面有，例如spring-context、spring-aop、spring-tx、spring-beans等
    public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";

	public DefaultNamespaceHandlerResolver(@Nullable ClassLoader classLoader) {
		this(classLoader, DEFAULT_HANDLER_MAPPINGS_LOCATION);
	}
	
	public DefaultNamespaceHandlerResolver(@Nullable ClassLoader classLoader, String handlerMappingsLocation) {
		Assert.notNull(handlerMappingsLocation, "Handler mappings location must not be null");
		this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
		this.handlerMappingsLocation = handlerMappingsLocation;
	}
```
进入DefaultBeanDefinitionDocumentReader.registerBeanDefinitions(doc, createReaderContext(resource)方法：
```text
	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
		this.readerContext = readerContext;
		doRegisterBeanDefinitions(doc.getDocumentElement());
	}

	protected void doRegisterBeanDefinitions(Element root) {
		
		this.delegate = createDelegate(getReaderContext(), root, parent);
        // 判断 根标签NameSpace是http://www.springframework.org/schema/beans，不是则直接返回
		if (this.delegate.isDefaultNamespace(root)) {
			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
			if (StringUtils.hasText(profileSpec)) {
				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
						profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
				// We cannot use Profiles.of(...) since profile expressions are not supported
				// in XML config. See SPR-12458 for details.
				if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec +
								"] not matching: " + getReaderContext().getResource());
					}
					return;
				}
			}
		}

		preProcessXml(root);
		
		// 【重要】解析xml标签，创建BeanDefinition对象
		parseBeanDefinitions(root, this.delegate);
		
		postProcessXml(root);

		this.delegate = parent;
	}
                                            
    protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
        // 再次判断 根标签NameSpace是http://www.springframework.org/schema/beans
        if (delegate.isDefaultNamespace(root)) {
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                // 过滤掉 注释和空白元素，只处理特定的Element元素
                if (node instanceof Element) {
                    Element ele = (Element) node;
                    if (delegate.isDefaultNamespace(ele)) {
                        // 【重要】内置标签对bean，beans，alis，import的处理，不展开说明了，请自行debug
                        parseDefaultElement(ele, delegate);
                    }
                    else {
                        // 【重要】自定义标签处理
                        delegate.parseCustomElement(ele);
                    }
                }
            }
        }
        else {
            // 非Beans根标签下的自定义标签`
            delegate.parseCustomElement(root);
        }
    }
```
由上处理标签可见，xml标签分类 如下：
```text
spring对标签的分类
    1) 内置标签，spring的内置标签只有一种，即http://www.springframework.org/schema/beans nameSpace下的标签，包括bean，beans，alis，import；其他spring提供的一些标签也都被认为是自定义标签；
    2) 自定义标签
        spring 提供的一些标签，例如：context，aop
        三方提供的，例如：dubbo，apollo
        创建自己的标签
```
下面来看看是怎么处理这些标签。

进入BeanDefinitionParserDelegate.parseCustomElement方法进行【自定义标签解析】：
```text
	@Nullable
	public BeanDefinition parseCustomElement(Element ele) {
		return parseCustomElement(ele, null);
	}
	
    public BeanDefinition parseCustomElement(Element ele, @Nullable BeanDefinition containingBd) {
        String namespaceUri = getNamespaceURI(ele);
        if (namespaceUri == null) {
            return null;
        }
        /**
         * 1. readerContext对象在XmlBeanDefinitionReader.registerBeanDefinitions(Document doc, Resource resource)方法中创建;
         * 2. 创建readerContext时同时创建了NamespaceHandlerResolver对象;
         * 3. NamespaceHandlerResolver对象指定了自定义标签处理器类对应的配置文件位置；配置文件在：META-INF/spring.handlers;
         * 4. 配置文件是namespaceUri到类名称的映射，且处理类必需实现NamespaceHandler接口;
         * 5. resolve时会解析文件，加载配置文件，通过namespaceUri查找对应的类，并反射创建对象;
         * 5. 然后通过parse方法完成对文档节点的解析工作;
         * 6. spring 提供了NamespaceHandlerSupport抽象类提供了一种处理范例;
         * 7. -------
         * 8 .NamespaceHandlerSupport实现类反射生成类对象后会调用init()方法，且需要在init方法中指定每一个标签对应的处理类；
         * 8. 每一个标签的处理类应实现BeanDefinitionParser接口
         * 9. ContextNamespaceHandler就是一个NamespaceHandlerSupport的实现类，可以参考该类的实现方式。
         */
        NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
        if (handler == null) {
            error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
            return null;
        }
        // 【重要】parse方法完成对节点的解析
        return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
    }
```
介绍一下spring内置的自定义标签处理器：NamespaceHandler

NamespaceHandler的接口注释介绍：
```text

```
```java
/**
 * DefaultBeanDefinitionDocumentReader使用的基本接口，用于处理Spring XML配置文件中的自定义名称空间。
 * 实现应该返回用于自定义顶级标记的BeanDefinitionParser接口的实现，以及用于自定义嵌套标记的BeanDefinitionDecorator接口的实现。
 * 解析器在遇到直接在<beans>标记下的自定义标记时调用parse，在遇到直接在<bean>标记下的自定义标记时调用装饰。
 * 编写自己的自定义元素扩展的开发人员通常不会直接实现这个接口，而是使用提供的NamespaceHandlerSupport类。
 */
public interface NamespaceHandler {
    
	void init();
    
	@Nullable
	BeanDefinition parse(Element element, ParserContext parserContext);
    
	@Nullable
	BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext);

}
```
我们常用到的NamespaceHandler都有：

ContextNamespaceHandler
```java
public class ContextNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
		registerBeanDefinitionParser("property-override", new PropertyOverrideBeanDefinitionParser());
		registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
		registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
		registerBeanDefinitionParser("load-time-weaver", new LoadTimeWeaverBeanDefinitionParser());
		registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-export", new MBeanExportBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-server", new MBeanServerBeanDefinitionParser());
	}

}
```
AopNamespaceHandler
```java
public class AopNamespaceHandler extends NamespaceHandlerSupport {
    public AopNamespaceHandler() {
    }

    public void init() {
        this.registerBeanDefinitionParser("config", new ConfigBeanDefinitionParser());
        this.registerBeanDefinitionParser("aspectj-autoproxy", new AspectJAutoProxyBeanDefinitionParser());
        this.registerBeanDefinitionDecorator("scoped-proxy", new ScopedProxyBeanDefinitionDecorator());
        this.registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
    }
}
```
TxNamespaceHandler
```java
public class TxNamespaceHandler extends NamespaceHandlerSupport {
    static final String TRANSACTION_MANAGER_ATTRIBUTE = "transaction-manager";
    static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";

    public TxNamespaceHandler() {
    }

    static String getTransactionManagerName(Element element) {
        return element.hasAttribute("transaction-manager") ? element.getAttribute("transaction-manager") : "transactionManager";
    }

    public void init() {
        this.registerBeanDefinitionParser("advice", new TxAdviceBeanDefinitionParser());
        this.registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
        this.registerBeanDefinitionParser("jta-transaction-manager", new JtaTransactionManagerBeanDefinitionParser());
    }
}
```
MvcNamespaceHandler
```java
public class MvcNamespaceHandler extends NamespaceHandlerSupport {
    public MvcNamespaceHandler() {
    }

    public void init() {
        this.registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
        this.registerBeanDefinitionParser("default-servlet-handler", new DefaultServletHandlerBeanDefinitionParser());
        this.registerBeanDefinitionParser("interceptors", new InterceptorsBeanDefinitionParser());
        this.registerBeanDefinitionParser("resources", new ResourcesBeanDefinitionParser());
        this.registerBeanDefinitionParser("view-controller", new ViewControllerBeanDefinitionParser());
        this.registerBeanDefinitionParser("redirect-view-controller", new ViewControllerBeanDefinitionParser());
        this.registerBeanDefinitionParser("status-controller", new ViewControllerBeanDefinitionParser());
        this.registerBeanDefinitionParser("view-resolvers", new ViewResolversBeanDefinitionParser());
        this.registerBeanDefinitionParser("tiles-configurer", new TilesConfigurerBeanDefinitionParser());
        this.registerBeanDefinitionParser("freemarker-configurer", new FreeMarkerConfigurerBeanDefinitionParser());
        this.registerBeanDefinitionParser("groovy-configurer", new GroovyMarkupConfigurerBeanDefinitionParser());
        this.registerBeanDefinitionParser("script-template-configurer", new ScriptTemplateConfigurerBeanDefinitionParser());
        this.registerBeanDefinitionParser("cors", new CorsBeanDefinitionParser());
    }
}
```
可以看出实际逻辑还是依靠具体的NamespaceHandler来解析元素标签。
进入NamespaceHandlerSupport.parse方法：
```text
	@Override
	@Nullable
	public BeanDefinition parse(Element element, ParserContext parserContext) {
	    // 根据具体的元素标签，获取具体的解析器
		BeanDefinitionParser parser = findParserForElement(element, parserContext);
		// 【重要】调用具体的元素解析器，解析元素内容
		return (parser != null ? parser.parse(element, parserContext) : null);
	}
```
进入具体的元素标签解析器，解析元素内容。

由于本次元素标签为<context:annotation-config/>，所以
进入AnnotationConfigBeanDefinitionParser.parse方法：
```text
	@Override
	@Nullable
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		Object source = parserContext.extractSource(element);

		// Obtain bean definitions for all relevant BeanPostProcessors.
		// 【重要】注册通用的注解相关的BeanFactoryPostProcessor和BeanPostProcessor，以便后续进行配置类注解和类扫描
		Set<BeanDefinitionHolder> processorDefinitions =
				AnnotationConfigUtils.registerAnnotationConfigProcessors(parserContext.getRegistry(), source);

		// Register component for the surrounding <context:annotation-config> element.
		CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
		parserContext.pushContainingComponent(compDefinition);

		// Nest the concrete beans in the surrounding component.
		for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
			parserContext.registerComponent(new BeanComponentDefinition(processorDefinition));
		}

		// Finally register the composite component.
		parserContext.popAndRegisterContainingComponent();

		return null;
	}
```
进入AnnotationConfigUtils.registerAnnotationConfigProcessors方法：
```text
	public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(
			BeanDefinitionRegistry registry, @Nullable Object source) {

		DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(registry);
		if (beanFactory != null) {
			if (!(beanFactory.getDependencyComparator() instanceof AnnotationAwareOrderComparator)) {
				beanFactory.setDependencyComparator(AnnotationAwareOrderComparator.INSTANCE);
			}
			if (!(beanFactory.getAutowireCandidateResolver() instanceof ContextAnnotationAutowireCandidateResolver)) {
				beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
			}
		}

		Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<>(8);
        // 【重要】注册ConfigurationClassPostProcessor
		if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
        // 【重要】注册AutowiredAnnotationBeanPostProcessor
		if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
		// Check for JSR-250 support, and if present add the CommonAnnotationBeanPostProcessor.
        // 【重要】注册CommonAnnotationBeanPostProcessor
		if (jsr250Present && !registry.containsBeanDefinition(COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));
		}

	    ...... // 注册其他组件，省略

		return beanDefs;
	}
```
可以看出解析<context:annotation-config/>的结果就是，即为后续beanDefinition的注解解析和注册其他beanDefinition创建一些特殊组件，
如ConfigurationClassPostProcessor、CommonAnnotationBeanPostProcessor、AutowiredAnnotationBeanPostProcessor等。

解析 <context:component-scan/>组件扫描元素标签，主要逻辑同上。
只是调用了不同的BeanDefinitionParser，即ComponentScanBeanDefinitionParser。
进入ComponentScanBeanDefinitionParser.parse方法：
```text
	@Override
	@Nullable
	public BeanDefinition parse(Element element, ParserContext parserContext) {
	    // 获取包扫描路径basePackage
		String basePackage = element.getAttribute(BASE_PACKAGE_ATTRIBUTE);
		basePackage = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(basePackage);
		String[] basePackages = StringUtils.tokenizeToStringArray(basePackage,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		// Actually scan for bean definitions and register them.
		// 创建 类路径扫描器
		ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
		// 【重要】执行类路径扫描组件
		Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
		// 【重要】注册扫描出来的组件到BeanFactory中
		registerComponents(parserContext.getReaderContext(), beanDefinitions, element);

		return null;
	}
```
至于具体的类路径扫描器doScan解析流程就不详述了，有兴趣的同学可以自己了解。

然后来看看Spring默认命名空间的标签元素(import、alias、bean、嵌套bean)解析过程。

进入DefaultBeanDefinitionDocumentReader.parseDefaultElement方法：
```text
	private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
		if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
			importBeanDefinitionResource(ele);
		}
		else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
			processAliasRegistration(ele);
		}
		else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
			processBeanDefinition(ele, delegate);
		}
		else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
			// recurse
			doRegisterBeanDefinitions(ele);
		}
	}
```
进入DefaultBeadDefinitionDocumentReader.processBeanDefinition方法：
```text
	protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
	    // 【重要】解析标签
		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// Register the final decorated instance.
				// 【重要】注册beanDefinition
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error("Failed to register bean definition with name '" +
						bdHolder.getBeanName() + "'", ele, ex);
			}
			// Send registration event.
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}
```
如上，就是XML配置文件的解析过程，具体的解析元素标签没有看，有感兴趣的同学自行了解。

综上，在refresh()方法中，XML配置文件解析是先于Java配置文件解析的。