
## 测试分类
* 单元测试：测试单个类的功能。
* 集成测试：测试多个类的协同工作。
* 端到端测试：测试整个程序的功能和流程。

## 测试分层
Controller层可以进行单元测试、集成测试（@WebMvcTest）和端到端测试。
* 在单元测试中，我们可以mock服务层的行为。
* 在集成测试中，我们可以使用@MockMvc来模拟HTTP请求。
* 在端到端测试中，我们则可以测试整个系统的工作流程。

Service层可以进行单元测试和集成测试。
* 在单元测试中，我们可以mock Repository层的行为。
* 在集成测试中，我们则需要确保Service层和其他层之间的交互是正确的。

Repository层可以进行集成测试（@DataJpaTest）。
* 在集成测试中，我们通常会使用内存数据库以模拟真实数据库的行为。

工具类可以进行单元测试。
* 工具类通常是独立的，我们可以直接进行单元测试，验证其功能是否正确。

## 测试策略

### 选择测试类型
选择适当的测试类型，根据需要选择单元测试、集成测试、端到端测试。

### 确定测试目标
明确你需要测试的具体功能或方法，理解其预期的行为。

### 准备测试环境和数据
根据测试需求设置适当的测试环境。
* 对于单元测试，你可能需要使用Mockito等库来创建mock对象，以模拟对其他类或接口的依赖。
* 对于集成测试，你可能需要配置一个嵌入式的数据库如H2。
* 对于端到端测试，你可能需要使用Selenium等库来模拟浏览器行为。

### 编写测试用例
根据测试目标编写对应的测试用例，包括正常场景、边界条件和异常场景。

### 执行测试和结果验证
* 执行测试：使用测试框架（如JUnit）运行你的测试。
* 结果断言：利用断言（assert）功能，验证你的代码产生的结果是否符合预期。
* 依赖验证：如果你的代码依赖于其他方法或对象，你需要使用verify方法来校验这些依赖项是否被正确地调用。

### 测试报告分析
评估测试结果，如果测试失败，进行bug修复，重新测试，直到所有测试用例都通过。

### 重构和优化
根据测试结果，进行代码的重构和优化，提高代码质量。

### 测试代码的维护
确保测试代码的可读性和可维护性，同时保证在项目构建过程中能够自动执行测试。

## 单元测试
### 定义
单元测试用于验证单个类的功能是否正确。

其特点是独立于其他类、数据库、网络或其他外部资源。

### 适用对象
在实际工作中，通常对Spring Boot中的Controller、Service等类进行单元测试。

## JUnit
JUnit是Java中最流行的单元测试框架，用于编写和执行单元测试。

### 基本注解
* @Test: 标记一个测试方法，用于执行单元测试。
* @Before: 在每个测试方法执行之前运行，用于准备测试数据或初始化资源。
* @After: 在每个测试方法执行之后运行，用于清理测试数据或释放资源。
* @BeforeClass: 在所有测试方法执行前运行，通常用于执行一次性的初始化操作。
* @AfterClass: 在所有测试方法执行后运行，通常用于执行一次性的清理操作。
* @Ignore: 标记一个测试方法，用于暂时忽略这个测试。
* @RunWith(SpringRunner.class)用于运行Spring Boot的测试。

#### 基本测试
```text
@Test
public void testCount() {
    // 测试代码
}
```

#### 异常测试
有时候需要测试代码是否能正确地抛出异常，可以使用@Test的expected属性：
```text
@Test(expected = SomeException.class)
public void testException() {
    // 测试代码，期望抛出SomeException异常
}
```

#### 超时测试
有时候需要测试某个操作是否能在规定时间内完成，可以使用@Test的timeout属性：
```text
@Test(timeout = 1000)
public void testTimeout() {
    // 测试代码，期望在1000毫秒内执行完毕
}
```

#### 忽略测试
有时候暂时不想执行某个测试方法，可以使用@Ignore注解：
```text
@Ignore("暂时忽略这个测试")
@Test
public void testIgnore() {
    // 测试代码
}
```

### Mockito
Mockito库用于模拟对象，隔离被测类与其他类的依赖关系。

#### 基本概念
* Mock对象：使用Mockito创建的模拟对象，用于替代真实对象，以模拟对应的行为和返回值。 
* Stubbing：为Mock对象设置方法调用的返回值，以模拟真实对象的行为。
* 验证：Mock对象的方法是否被调用，以及调用次数是否符合预期。

#### 常用注解
* @Mock: 标记一个模拟对象。
* @InjectMocks: 标记一个被测类，用于注入模拟对象。
* @RunWith(MockitoJUnitRunner.class): 指定运行器，用于运行Mockito的测试。

常用方法Mockito 提供了一系列方法，用于在单元测试中创建和设置模拟对象的行为：
* when(mock.method()).thenReturn(value): 设置当 mock 对象的指定方法被调用时，返回预设的值。
* any(): 表示任何值，用于 when 或 verify 方法的参数匹配。
* doReturn(value).when(mock).method(): 与when-thenReturn类似，但适用于无法通过when-thenReturn 语句模拟的情况，如void方法。
* doThrow(Exception).when(mock).method(): 用于模拟当 mock 对象的指定方法被调用时，抛出异常。
* spy(object): 用于创建一个 spy 对象，它是对真实对象的包装，所有未被 stub 的方法都会调用真实的方法。

Mockito 还提供了一系列的方法，用于验证模拟对象的行为：
* verify(mock).method(): 验证 mock 对象的指定方法是否被调用。
* never(): 验证 mock 对象的指定方法从未被调用。
* times(n): 验证 mock 对象的指定方法被调用了 n 次。
* atLeast(n): 验证 mock 对象的指定方法至少被调用了 n 次。
* atMost(n): 验证 mock 对象的指定方法最多被调用了 n 次。

#### 示例代码
```text
@RunWith(MockitoJUnitRunner.class)
public class TeacherApiTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherApi teacherApi;

    @Test
    public void testCount() {
        when(teacherService.countByName("张三")).thenReturn(1);
        TeacherCount result = teacherApi.count("张三");
        verify(teacherService).countByName("张三"); // 验证方法是否被调用
        assertEquals(1, result.getCount()); // 验证返回值是否符合预期
    }
}
```

### AssertJ
AssertJ库提供了丰富的断言方法，可以更简洁地编写测试代码。

#### 常用断言
* assertThat(actual).isEqualTo(expected): 验证实际值是否等于预期值。
* assertThat(actual).isNotEqualTo(expected): 验证实际值是否不等于预期值。
* assertThat(actual).isNotNull(): 验证实际值是否不为null。
* assertThat(actual).isNull(): 验证实际值是否为null。
* assertThat(actual).isTrue(): 验证实际值是否为true。
* assertThat(actual).isFalse(): 验证实际值是否为false。
* assertThat(actual).isInstanceOf(ExpectedClass.class): 验证实际值是否是指定类的实例。
* assertThat(actual).isNotInstanceOf(UnexpectedClass.class): 验证实际值是否不是指定类的实例。
* assertThat(actual).isGreaterThan(expected): 验证实际值是否大于预期值。
* assertThat(actual).isGreaterThanOrEqualTo(expected): 验证实际值是否大于等于预期值。
* assertThat(actual).isLessThan(expected): 验证实际值是否小于预期值。
* assertThat(actual).isLessThanOrEqualTo(expected): 验证实际值是否小于等于预期值。
* assertThat(actual).contains(expected): 验证实际值是否包含指定字符串。
* assertThat(actualList).contains(expected): 验证集合是否包含指定元素。
* assertThat(actualMap).containsKey(expected): 验证Map是否包含指定键。
* assertThat(actualMap).containsValue(expected): 验证Map是否包含指定值。
* assertThat(actualMap).containsEntry(key, value): 验证Map是否包含指定元素。
* assertThat(actualException).isInstanceOf(ExpectedException.class).hasMessage(expectedMessage): 验证异常是否符合预期。

### JSONAssert
JSONAssert库用于测试JSON字符串是否符合预期。

#### 常用方法
* assertEquals(expected, actual, strict): 验证实际的JSON值是否等于预期的JSON值。这里的strict参数指定了比较的模式。
  如果设置为false，将会使用非严格模式比较，这意味着预期JSON字符串中没有的字段会被忽略；
  如果设置为true，将会使用严格模式比较，预期JSON字符串与实际JSON字符串必须完全匹配。 
* assertNotEquals(expected, actual, strict): 验证实际的JSON值是否不等于预期的JSON值。 
* assertJSONEquals(expected, actual, jsonComparator): 使用自定义的JSONComparator来验证实际的JSON值是否等于预期的JSON值。 

#### 示例代码
```text
@Test
public void testJsonAssert() throws JSONException {
String expected = "{\"id\":1,\"name\":\"张三\"}";
String actual = "{\"id\":1,\"name\":\"张三\"}";

    // 使用非严格模式进行比较
    JSONAssert.assertEquals(expected, actual, false);

    // 使用严格模式进行比较，预期字符串与实际字符串必须严格匹配
    JSONAssert.assertEquals(expected, actual, true);

    // 验证实际的JSON值是否不等于预期的JSON值
    String unexpected = "{\"id\":2,\"name\":\"李四\"}";
    JSONAssert.assertNotEquals(unexpected, actual, false);

    // 使用自定义的JSONComparator进行比较
    JSONAssert.assertJSONEquals(expected, actual, JSONCompareMode.LENIENT);
}
```

### @JsonTest
SpringBoot的@JsonTest注解用于测试JSON序列化和反序列化。

#### 示例代码
```text
@JsonTest
public class TeacherJsonTest {

    @Autowired
    private JacksonTester<Teacher> json;

    String content = "{\"id\":1,\"name\":\"张三\"}";

    @Test
    public void testSerialize() throws Exception {
        Teacher teacher = new Teacher();
        teacher.id = 1L;
        teacher.name = "张三";

        assertThat(json.write(teacher)).isEqualToJson(content);
    }

    @Test
    public void testDeserialize() throws Exception {
        Teacher teacher = new Teacher();
        teacher.id = 1L;
        teacher.name = "张三";

        Teacher teacher1 = json.parseObject(content);

        assertThat(teacher1.id).isEqualTo(teacher.id);
        assertThat(teacher1.name).isEqualTo(teacher.name);
    }
}
```

## 集成测试
### 定义
集成测试验证Spring Boot应用内各组件（Controller、Service、Repository等）之间的交互和协作。

其特点是需要启动Spring上下文环境（不一定需要启动Web服务器），以便在测试代码中直接使用 @Autowired 等注解。

### 适用对象
在实际工作中，通常对Spring Boot项目的各个组件进行集成测试，包括但不限于：
验证Controller层能否正确调用Service层方法，并处理返回的数据。
验证Service层能否正确地与Repository层交互，实现数据库的读写操作。
验证Repository层能否正确地与数据库交互，实现数据的读写操作。

### @SpringBootTest
@SpringBootTest 注解在测试开始前创建一个完整的Spring应用程序上下文。

#### 示例代码
```text
@SpringBootTest
@ActiveProfiles("test") // 指定运行环境
public class TeacherServiceIntegrationTest {

    @Autowired
    private TeacherService teacherService; // 注入TeacherService

    @Test
    public void testCount() {
        int result = teacherService.countByName("张三"); // 调用Service的方法
        assertEquals(1, result); // 断言结果
    }
}
```

### @WebMvcTest
@WebMvcTest 注解在测试开始前启动一个针对Spring MVC的测试环境。

仅加载指定的Controller，而不会加载应用程序的其他组件。如果测试中需要这些其他组件，使用@MockBean来模拟这些组件的行为。

使用 MockMvc 对象模拟发送HTTP请求，并验证控制器的响应。

#### 示例代码
```text
@WebMvcTest(TeacherApi.class) // 指定需要测试的Controller
@ActiveProfiles("test") // 指定运行环境
public class TeacherApiWebMvcTest {

    @Autowired
    private MockMvc mockMvc; // 注入MockMvc

    @MockBean
    private TeacherService teacherService; // Mock掉Service层的接口

    @Test
    public void testCount() throws Exception {
        when(teacherService.countByName("张三")).thenReturn(1); // Mock掉Service的方法，指定返回值

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/count").param("name", "张三")) // 执行一个GET请求
                .andExpect(MockMvcResultMatchers.status().isOk()) // 验证响应状态码为200
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1)); // 断言响应结果中JSON属性"count"的值为1
    }
}
```

#### MockMvc的常用方法
* perform(request): 执行一个HTTP请求。
* andExpect(status().isOk()): 断言响应状态码为200（HttpStatus.OK）。
* andExpect(content().contentType(MediaType.APPLICATION_JSON)): 断言响应内容类型为JSON。
* andExpect(content().string(containsString("expectedString")): 断言响应内容包含指定的字符串。
* andExpect(jsonPath("$.key").value("expectedValue")): 断言响应内容中JSON属性"key"的值为"expectedValue"。
* andExpect(content().json("expectedJson")): 断言响应内容为"expectedJson"。
* andReturn(): 获取MvcResult实例，可以在断言完成后获取详细的响应内容。
* print(): 在控制台上打印执行请求的结果。

#### jsonPath的常用表达式
* $.key: 获取根节点属性"key"的值。
* $..key: 获取所有名为"key"的属性的值，无论它们在哪一层。
* $.array[0]: 获取名为"array"的数组属性中的第一个元素的值。
* $.array[:2]: 获取名为"array"的数组属性中的前两个元素的值。
* $.array[-1:]: 获取名为"array"的数组属性中的最后一个元素的值。

### @DataJpaTest
@DataJpaTest注解在测试开始前启动一个Spring上下文环境，只加载JPA相关的组件，如实体、仓库等。

配置一个内存数据库，以避免对真实数据库的影响，并提高测试的速度。默认开启事务，并在测试完成后回滚事务。

#### 示例代码
```text
@DataJpaTest
@ActiveProfiles("test") // 指定运行环境
public class TeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao; // 注入Repository

    @Test
    public void testCount() {
        int result = teacherDao.countByName("张三"); // 调用Repository的方法
        assertEquals(1, result); // 断言结果
    }
}
```

### TestEntityManager
TestEntityManager是Spring提供的一个用于测试的EntityManager，用于在测试中对实体进行增删改查操作。

```text
@DataJpaTest
@ActiveProfiles("test") // 指定运行环境
public class TeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao; // 注入Repository

    @Autowired
    private TestEntityManager entityManager; // 注入TestEntityManager

    @Test
    public void testCount() {
        // 使用TestEntityManager创建一个Teacher实体
        Teacher teacher = new Teacher();
        teacher.name = "张三";
        teacher.age = 20;
        entityManager.persist(teacher);

        // 调用Repository的方法
        int result = teacherDao.countByName("张三");

        // 断言结果
        assertEquals(1, result);
    }
}
```

### 测试数据库
集成测试通常需要使用数据库。

如果使用真实的数据库，每次测试都会对数据库进行读写操作，这样会导致测试变得缓慢，而且会对数据库造成影响。

为了解决这个问题，可以使用内存数据库。
内存数据库是一种特殊的数据库，它将数据存储在内存中，而不是磁盘上，因此它的读写速度非常快，适合用于测试。

#### 配置
在Spring Boot项目中使用H2数据库，需要在pom.xml中添加以下依赖：
```text
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```
#### 使用
在application-test.properties中添加以下配置：
```text
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.datasource.schema=classpath:test-schema.sql
spring.datasource.data=classpath:test-data.sql
```
在application-test.properties中，指定了H2数据库的连接信息。
还指定了初始化脚本test-schema.sql和test-data.sql，用于初始化数据库的表结构和数据。

test-schema.sql
```text
CREATE TABLE teacher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL
);
```

test-data.sql
```text
INSERT INTO teacher (name, age) VALUES ('张三', 20);
INSERT INTO teacher (name, age) VALUES ('李四', 30);
```

## 端到端测试

### 定义
端到端测试验证整个程序的功能是否按照预期工作。

其特点是需要启动完整的应用程序上下文，并模拟客户端与HTTP接口进行交互。

### 适用对象
在实际工作中，我们通常对使用SpringBoot开发的整个应用程序进行端到端测试。

### RANDOM_PORT
端到端测试需要启动完整的应用程序上下文。

使用@SpringBootTest注解的webEnvironment属性，将应用程序上下文设置为随机端口启动。

### TestRestTemplate
TestRestTemplate是Spring提供的类，用于发送HTTP请求并验证接口的响应是否符合预期。

#### 示例代码
```text
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 将应用上下文设置为随机端口启动
@ActiveProfiles("test")
public class TeacherApiEndToEndTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSearchTeacher() {
        // 发送GET请求，访问"/teachers?name=John"
        ResponseEntity<TeacherCount> response = restTemplate.getForEntity("/teachers?name=John", TeacherCount.class);

        // 验证响应状态码是否为200（HttpStatus.OK）
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        // 验证响应中JSON属性"count"的值是否为2
        Assertions.assertEquals(2, response.getBody().getCount());
    }
}
```

### Jsoup
Jsoup库用于HTML解析。

#### 基本使用
以下是一些Jsoup的基本使用方法：

解析HTML字符串：
```text
String html = "<html><head><title>测试页面</title></head>"
            + "<body><p>这是一个测试页面</p></body></html>";
Document doc = Jsoup.parse(html);
```

获取HTML元素：
```text
Element titleElement = doc.select("title").first();
```

获取元素的文本内容：
```text
String titleText = titleElement.text();
```

获取元素的属性值：
```text
Element linkElement = doc.select("a").first();
String href = linkElement.attr("href");
```

#### 测试网页
以下是一个使用Jsoup测试网页的例子：
```text
@Test
public void testHtmlPage() {
  // 测试HTML页面的标题
  Document doc = Jsoup.connect("http://www.example.com").get();
  String title = doc.title();
  assertThat(title).isEqualTo("Example Domain");
}
```

### Selenium
Selenium用于模拟浏览器行为。

通过Selenium，程序可以自动化地操作浏览器，模拟用户在浏览器中的交互行为。

#### 核心类
* WebDriver：用于模拟浏览器的行为，如打开网页、输入内容、点击按钮等。
* WebElement：用于表示网页中的元素，如文本框、按钮等。
* By：用于定位网页中的元素，如根据ID、CSS选择器等。

#### WebDriver的常用方法
get(url): 打开指定的网页。
findElement(by): 根据定位器定位网页中的元素。
sendKeys(text): 在文本框中输入文本。
click(): 点击按钮。
getText(): 获取元素的文本内容。

#### 测试网页
以下是一个使用Selenium测试网页的例子：
```text
public class TeacherSearchFrontendTest {

    @Test
    public void testSearchTeacher() {
        // 设置ChromeDriver的路径（注意根据实际情况修改路径）
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        // 创建Chrome浏览器的WebDriver
        WebDriver driver = new ChromeDriver();

        // 打开目标网页
        driver.get("http://localhost:8080");

        // 找到搜索框并输入教师姓名
        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.sendKeys("John");

        // 找到搜索按钮并点击
        WebElement searchButton = driver.findElement(By.id("search-button"));
        searchButton.click();

        // 验证搜索结果数量是否符合预期
        WebElement resultCount = driver.findElement(By.id("result-count"));
        Assertions.assertEquals("Found 2 teachers", resultCount.getText());

        // 关闭浏览器
        driver.quit();
    }
}
```

## 附：测试思想

### 测试金字塔 
单元测试：
```text
测试金字塔的基础，最为常见也最为频繁。
针对代码中的最小单元进行测试，如方法和类。

单元测试的重点在于找出方法的逻辑错误，确保所有的方法达到预期的结果。
单元测试的粒度应保证足够小，有助于精确定位问题。 
``` 

集成测试：
```text
测试金字塔的中层，数量通常少于单元测试，多于端对端测试。
集成测试的重点是检查几个单元组合在一起后是否能正常工作。
```  

端对端测试：
```text
测试金字塔的顶层，数量最少。
端对端测试的目的是从用户的角度模拟完整的应用场景，验证多个单元/模块是否可以协同工作。 
```

### 测试原则 
单一职责：
```text
每个测试都应当专注于一个具体的功能或者逻辑，避免在一个测试中试图验证多个逻辑。 
```

独立性：
```text
每个单元测试都应该独立于其他测试，不能互相调用，也不能依赖执行的顺序。 
```

一致性：
```text
测试应当能够在任何时间、任何环境下得出相同的结果。
```  

全面覆盖：
```text
尽可能覆盖所有可能的路径和情况。包括所有正常情况、边界情况以及可能的错误情况。
```  

自动化：
```text
测试应当是自动化的，可以无人值守地运行。 
```

易于理解：
```text
测试代码也是代码，应当遵循良好的代码编写实践。
```  

快速反馈：
```text
优秀的测试应该可以在短时间内完成并给出反馈。
``` 

BCDE原则：
```text
Border，边界值测试，
Correct，正确的输入，并得到预期的结果，
Design，与设计文档相结合，来编写单元测试，
Error，强制错误信息输入，以及得到预期的结果。
```  

测试数据：
```text
测试数据应由程序插入或导入，以准备数据。
```