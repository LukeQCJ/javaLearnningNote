# 第01节 从Servlet到Spring MVC

## 一、原始Servlet开发
### 1.1 Servlet开发简介

在最早没有Web开发框架的时候，我们做Web开发时，首先需要导入servlet-api的包，这个包提供了Web开发的基础，
这个包里面提供了Servlet接口以及它的抽象实现类GenericServlet，
我们开发Servlet时需要继承GenericServlet类，该类提供了一个抽象service()方法，这个方法就是请求的入口，
但GenericServlet类的service()方法是一个抽象方法，如果我们的Servlet直接继承它，需要自行实现这个service()方法。

但servlet-api包中同时也提供了一个GenericServlet类的实现类HttpServlet，该类实现了service()，会按照请求方式不同，来调用不同的方法。

所以，在实际的Servlet开发中，我们一般会去实现HttpServlet类，然后重写它的doGet()或doPost()等方法。
```text
protected void service(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
    String method = req.getMethod();

    if (method.equals(METHOD_GET)) {
        doGet(req, resp);
    } else if (method.equals(METHOD_HEAD)) {
        long lastModified = getLastModified(req);
        maybeSetLastModified(resp, lastModified);
        doHead(req, resp);
    } else if (method.equals(METHOD_POST)) {
        doPost(req, resp); 
    } else if (method.equals(METHOD_PUT)) {
        doPut(req, resp);   
    } else if (method.equals(METHOD_DELETE)) {
        doDelete(req, resp);
    } else if (method.equals(METHOD_OPTIONS)) {
        doOptions(req,resp);
    } else if (method.equals(METHOD_TRACE)) {
        doTrace(req,resp); 
    }
}
```

### 1.2 Servlet开发
#### 1.2.1 继承HttpServlet抽象类，然后重写它的doGet()方法
```text
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {

        resp.getWriter().write("AnnoServlet--doGet");
    }
}
```

#### 1.2.2 配置Servlet映射
先为HelloServlet起一个别名，然后配置它的映射信息，表示以/helloServlet前缀开始的请求，都会交由HelloServlet类来处理。
```text
<servlet>
    <!-- 别名  -->
    <servlet-name>helloServlet</servlet-name>
    <!-- 指定servlet类 -->
    <servlet-class>com.lizhi.HelloServlet</servlet-class>
    <!-- 1表示容器启动时就初始化servlet -->
    <load-on-startup>1</load-on-startup>
</servlet>
<!-- 配置对应servlet 拦截url与应用中contrller的url的映射规则 -->
<servlet-mapping>
    <servlet-name>helloServlet</servlet-name>
    <url-pattern>/helloServlet</url-pattern>
</servlet-mapping>
```
其中<load-on-startup>参数表示是否在Servlet容器(也就是Web容器)启动时就加载该Servlet，为1就表示启动时就加载。

使用这种方式，有一个很不好的问题就是拓展性太差了，如果添加了不是以/helloServlet为前缀的请求，就需要再写一个新的Servlet来映射。

如果都是以/helloServlet为前缀的请求，HelloServlet类又会显得很臃肿，所以后面就出现了以SpringMVC、Struts等一系列的MVC框架。

#### 1.2.3 配置过滤器和监听器
【过滤器】和【监听器】是Web开发的两个核心组件，
【过滤器】的主要功能有设置字符编码、文本类型，过滤请求等等；
【监听器】又分为好几种，上下文监听器、Session监听器等等，可以监听上下文或Session变化。

在Servlet3.0以后，可以直接使用注解来配置Servlet、Filter和Listener，我们还是以xml的方式来配置。

先实现Filter接口，定义一个过滤器
```text
public class HelloFilter implements Filter {

	private FilterConfig config;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        System.out.println("—init-");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("—doFilter-");
        request.setCharacterEncoding(config.getInitParameter("charset"));
        response.setContentType(config.getInitParameter("contentType"));
        response.setCharacterEncoding(config.getInitParameter("charset"));
        
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        System.out.println("—destroy-");
    }
}
```

下面配置了一个helloFilter的过滤器，在过滤器设置了字符编码，以及文本类型。
```text
<!-- 配置Filter -->  
<filter>    
    <filter-name>helloFilter</filter-name>    
    <filter-class>com.lizhi.HelloFilter</filter-class>
    <init-param>
        <param-name>charset</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>contentType</param-name>
        <param-value>text/html;charset=UTF8</param-value>
    </init-param>
</filter>   

<!-- 方式一 -->
<filter-mapping>    
    <filter-name>helloFilter</filter-name>
    <!-- 表示处理根目录下的所有请求 -->      
    <url-pattern>/*</url-pattern>
</filter-mapping>

<!-- 方式二 -->
<filter-mapping>    
    <filter-name>helloFilter</filter-name>    
    <servlet-name>helloServlet</servlet-name>
</filter-mapping>
```
过滤器映射配置方式一：所有请求都需要经过该过滤器。

过滤器映射配置方式二：只有helloServlet处理的所有请求才会进行匹配，这种方式的配置，
可以在<filter-mapping>中添加dispatcher标签来细化过滤范围，包括有REQUEST、FORWARD、INCLUDE等。

同时，Filter与Servlet的映射配置是一种多对多的关系，即一个Filter可以配置给多个Servlet，而一个Servlet也可以配置多个Filter。

【监听器】用于监听ServletContext、HttpSession和ServletRequest等域对象的创建与销毁事件，
以及监听这些域对象中属性发生修改的事件，我们通过实现EventListener的子接口，来实现监听功能。
```text
public class HelloListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    
    }
}
```
在xml中配置监听器
```text
<listener>
   <listener-class>com.lizhi.HelloListener</listener-class>
</listener>
```

#### 1.2.4 处理请求
当HTTP请求到达Web容器以后，容器根据配置的Servlet映射，找到对应的Servlet实例，然后调用Servlet实例的service()方法，
在service()方法中，会根据请求的方法GET、POST等调用不同的方法。

在调用Servlet的service()方法之前，首先会与当前请求路径或Servlet相匹配的过滤器，在过滤器的doFilter()方法中，执行一些操作，
比如设置字符编码和文本类型、验证登录等。

## 二、Servlet到Spring MVC
前面介绍了原生Servlet的使用，开发比较简单，但拓展性很差。

SpringMVC对【原生Servlet】进行了封装，提供了一个【统一的DispatcherServlet】作为所有请求的入口，
在原生Servlet中，所有的业务都是在Servlet的方法中执行的，使得Servlet的方法显得很臃肿，
而SpringMVC的DispatcherServlet，它并不做具体的业务，它只是担任【调度器】的角色，把请求交由相应的组件来处理。

在SpringMVC中，除了【DispatcherServlet】之外，
还有【HandlerMapping(处理器映射器)】、【HandlerAdapter(处理器适配器)】、【Handler(处理器)】、【ViewResolver(视图解析器)】，
按照【责任链】的模式，他们各自分别处理不同的业务，具体的业务代码是由在处理器的方法中调用完成的。

下面开始使用SpringMVC进行实践。

### 2.1 配置Servlet
在SpringMVC的【web.xml】配置文件中，首先需要配置一个【Web容器加载监听器】，
当【Web容器上下文初始化】完成之后，需要去【初始化SpringMVC的容器】，这个过程在后面的文章会细讲。
```text
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```
接下来就是配置Servlet以及映射，配置DispatcherServlet，处理所有的请求。
```text
<servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>

    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
    
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
</servlet>

<servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```
配置过滤器解决乱码问题。
```text
<filter>
    <filter-name>encodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>encodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

### 2.2 配置处理器映射等组件
在web.xml的Servlet中，指定了spring-mvc.xml，在解析Servlet配置的时候，就回去解析该xml的配置。

在spring-mvc.xml中，首先指定Spring容器需要扫描的基础包路径；
<mvc:annotation-driven>用于支持MVC注解，比如@Controller和@RequestMapping，容器会自动将加了这些注解的类实例化成Bean。
```text
<!--扫描包-->
<context:component-scan base-package="com.lizhi" />

<mvc:annotation-driven />
```
下面配置【视图解析器】，在配置视图解析器时，可以指定视图的前缀和后面，这样Spring在解析视图时，只需要知道视图的名字即可，
可以通过前缀和后缀找到指定的视图。
```text
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" name="viewResolver">
    <property name="prefix" value="/WEB-INF/jsp/"></property>
    <property name="suffix" value=".jsp"></property>
</bean>
```
配置处理器映射器，下面配置了三个处理器映射器，
RequestMappingHandlerMapping用于处理@RequestMapping注解的url，
BeanNameUrlHandlerMapping用于处理url与beanName相等的请求，
SimpleUrlHandlerMapping通过key来指定某个Bean进行处理。
```text
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping" />

<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping" >
    <property name="mappings">
        <props>
            <prop key="/simpleController">simpleController</prop>
        </props>
    </property>
</bean>

<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
```
注：如果xml文件中没有配置映射器处理器，
SpringMVC会启动配置BeanNameUrlHandlerMapping、RequestMappingHandlerMapping和RouterFunctionMapping这三个映射器处理器，
这些默认的配置都在【spring-webmvc】包下的DispatcherServlet.properties文件中定义，该文件中包含了很多默认的配置。

### 2.3 使用SpringMVC
当SpringMVC的配置都完成之后，就可以直接使用了，以@RequestMapping注解为例：

创建一个类，然后加上@Controller和@RequestMapping注解，在@RequestMapping注解中可以指定url。
如果@RequestMapping作用在类上，它指定的url为该controller的全局请求前缀，它所有方法的url都会加上这个前缀。
```text
@Controller
@RequestMapping("/request")
public class RequestMappingController {

    @RequestMapping("/mapping")
    public ModelAndView mapping(){
        System.out.println("RequestMappingController Working.");
        ModelAndView modelAndView = new ModelAndView("a");
        modelAndView.addObject("source","RequestMappingController");
        return modelAndView;
    }
}
```