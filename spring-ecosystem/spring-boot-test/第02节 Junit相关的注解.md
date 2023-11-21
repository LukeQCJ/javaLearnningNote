## 一 ：@Mock 和 @MockBean的区别：

@Mock：
```text
Mockito的包；如果是简单的不依赖Spring Boot container的测试，那么mock就够用了。
```
@MockBean：
```text
spring的包（spring-boot-test包，算是对Mockito包的包装）；
如果需要依赖Spring Boot container，且需要添加或者mock一个container bean，那么就用mockbean。

@WebMvcTest通常和MockBean组合使用。
InjectMock 换成 MockBean，在junit4升级到junit5的时候。
```


## 二. @RunWith(MockitoJUnitRunner.class)

是为了激活Mockito的注解，让它们可用。

这是junit4的用法， 如果junit5，需要换成@ExtendWith(MockitoExtension.class)


## 三. @Import注解

功能和Spring XML文件一样，用来导入配置类或者一些需要前置加载的类。

@Import支持 三种方式：
```text
1.带有@Configuration的配置类
2.ImportSelector 的实现
3.ImportBeanDefinitionRegistrar 的实现
```

@Import 标记的类注册成 bean


## 四. @Configuration

@Configuration 注释的类 类似于于一个 xml 配置文件的存在.

如果需要导入XML或其他非@Configuration bean定义资源，请改用@ImportResource注释


## 五. @SpyBean

表示一个“间谍对象”，允许它的某些方法被模拟，而剩下的方法仍然是真实的方法。


## 六. Junit4 升级到Junit5，常见的变化：

step1：
```text
@RunWith(MockitoJUnitRunner.class) 变为@ExtendWith(MockitoExtension.class)
public class SomeUnitTest {
    ...
    @BeforeClass 变为 @BeforeAll
    public void beforeAll()
    ...
    @AfterClass  变为 @AfterAll
    public void afterAll()
    ...
    @Before 变为 @BeforeEach
    public void beforeEach()
    ...
    @After 变为 @AfterEach
    public void afterEach()
```

step2：
```text
@RunWith(SpringRunner.class) 变为@ExtendWith(SpringExtension.class)
```

step3：
```text
对于@Test，使用import org.junit.jupiter.api.Test;
```

step4:
```text
Annotations reside in the org.junit.jupiter.api package.
Assertions reside in org.junit.jupiter.api.Assertions.

Note that you may continue to use assertion methods from org.junit.Assert 
or any other assertion library such as AssertJ, Hamcrest, Truth, etc.

Assumptions reside in org.junit.jupiter.api.Assumptions.

Note that JUnit Jupiter 5.4 and later versions support methods from JUnit 4’s org.junit.
Assume class for assumptions. Specifically, 
JUnit Jupiter supports JUnit 4’s AssumptionViolatedException to signal 
that a test should be aborted instead of marked as a failure.

@Before and @After no longer exist; use @BeforeEach and @AfterEach instead.
@BeforeClass and @AfterClass no longer exist; use @BeforeAll and @AfterAll instead.
@Ignore no longer exists: use @Disabled or one of the other built-in execution conditions instead

See also JUnit 4 @Ignore Support.

@Category no longer exists; use @Tag instead.
@RunWith no longer exists; superseded by @ExtendWith.
@Rule and @ClassRule no longer exist; superseded by @ExtendWith and @RegisterExtension
```

step5:
```text
（1）把下面的
@ClassRule
public static WireMockClassRule WIRE_MOCK_RULE = 
    new WireMockClassRule(options().dynamicPort().dynamicHttpsPort());
改为：
@Autowired
private WireMockServer wireMockServer;

把@Rule改成@TempDir


（2）把下面的
@AfterClass
public static void afterClass() {
    if (WIRE_MOCK_RULE != null) {
        WIRE_MOCK_RULE.stop();
    }
}
改为：
@AfterEach
public void afterEach() {
    wireMockServer.resetAll();
}


public static class PropertyOverrideContextInitializer 
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

（3）把下面的
    @Override
    public void initialize(@NotNull final ConfigurableApplicationContext configurableApplicationContext) {
        addInlinedPropertiesToEnvironment(configurableApplicationContext, 
            "pyaService.baseURL=http://localhost:" + WIRE_MOCK_RULE.port());

    改为：
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
      wireMockServer.start();

      configurableApplicationContext.getBeanFactory()
        .registerSingleton("wireMockServer", wireMockServer);

      configurableApplicationContext.addApplicationListener(applicationEvent -> {
        if (applicationEvent instanceof ContextClosedEvent) {
          wireMockServer.stop();
        }
      });

      addInlinedPropertiesToEnvironment(configurableApplicationContext, 
            "pyaService.baseURL=http://localhost:" + wireMockServer.port());
    }
}
```
