# Feign是什么
Feign是Netflix公司开发的一个声明式的REST调用客户端；

Ribbon负载均衡、Hystrix服务熔断是我们Spring Cloud中进行微服务开发非常基础的组件，
在使用的过程中我们也发现它们一般都是同时出现，而且配置很相似，每次开发都有很多相同的代码，
因此Spring Cloud基于Netflix Feign整合了Ribbon和Hystrix两个组件，让我们的开发工作变得更加简单， 
就像Spring Boot是对 SpringFramework+SpringMVC的简化一样，
Spring Cloud Feign 对 Ribbon 负载均衡、Hystrix 服务熔断进行简化，在其基础上进行了进一步的封装，
不仅在配置上大大简化了开发工作，同时还提供了一种声明式的Web服务客户端定义方式。

# 使用Feign实现消费者
使用Feign实现消费者，我们通过下面步骤进行：

第一步：创建Spring Boot工程

第二步：添加依赖
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-feign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
    <version>1.4.7.RELEASE</version>
</dependency>
```

第三步：添加注解

在项目入口类上添加@EnableFeignClients注解表示开启Spring Cloud Feign的支持功能；
```text
@EnableFeignClients
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

第四步：声明服务

定义一个HelloService接口，通过@FeignClient注解来指定服务名称，进而绑定服务，
然后再通过SpringMVC中提供的注解来绑定服务提供者提供的接口，如下：
```text
// 服务名称是配置服务提供者的配置spring.application.name的名称
@FeignClient("01-springcloud-service-provider")
public interface HelloService {

    /**
     * 声明一个方法，这个方法就是远程的服务提供者的方法
     * @return
     */
    @RequestMapping("/service/hello")
    public String hello();
}
```

这相当于绑定了一个名叫 01-springcloud-service-provider 的服务提供者提供的/service/hello接口；
```text
@RequestMapping("/service/hello")
public String hello() {
    return "hello spring cloud provider 1";
}
```

第五步：使用Controller中调用服务

创建Controller来调用上面的服务，如下：
```text
@RestController
public class FeignController {
    @Autowired
    HelloService helloService;

    @RequestMapping("/web/hello")
    public String hello(){
        return helloService.hello();
    }
}
```

第六步：属性配置
```text
# 访问端口
server.port=8083
# 服务名称
spring.application.name=05-springcloud-service-feign
# eureka注册中心地址
eureka.client.service-url.defaultZone=http://eureka8761:8761/eureka,http://eureka8762:8762/eureka
```

第七步：测试

依次启动注册中心、服务提供者和feign实现服务消费者，然后访问:http://localhost:8083/web/hello

# 使用Feign实现消费者的测试
## 1、负载均衡
Spring Cloud 提供了Ribbon来实现负载均衡，
使用Ribbon直接注入一个RestTemplate对象即可RestTemplate已经做好了负载均衡的配置；

在Spring Cloud下，使用Feign也是直接可以实现负载均衡的，定义一个注解有@FeignClient注解的接口，
然后使用@RequestMapping 注解到方法上映射远程的REST服务，此方法也是做好负责均衡配置的。

## 2、服务熔断
第一步、在application.properties文件开启hystrix功能
```text
# 开启服务熔断
feign.hystrix.enabled=true
```

第二步、指定熔断回调逻辑

(1)常见一个类用于编写回调方法
```text
@Component
public class MyFallback implements HelloService{
    /**
    * 覆盖HelloService接口，重写hello方法
    * @return
    */
    @Override
    public String hello() {
        return "远程调用服务失败，调用熔断降级方法";
    }
}
```

(2)接口类添加fallback属性指定回调逻辑
```text
/**
* 服务名称是配置服务提供者的配置spring.application.name的名称
* 当远程调用服务失败时，会调用fallback属性指定的类的同名方法为回调方法
*/
@FeignClient(name = "01-springcloud-service-provider", fallback = MyFallback.class)
public interface HelloService {

    /**
    * 声明一个方法，这个方法就是远程的服务提供者的方法
    * @return
    */
    @RequestMapping("/service/hello")
    public String hello();
}
```

## 3、服务熔断获取异常信息

为@FeignClient修饰的接口加上fallback方法可以实现远程服务发生异常后进行服务的熔断，
但是不能获取到远程服务的异常信息，如果要获取远程服务的异常信息，此时可以使用fallbackFactory：
```text
/**
* 实现FallbackFactory接口，泛型为远程调用方法的接口
*/
@Component
public class MyFallbackFactory implements FallbackFactory<HelloService>{
    // 重写create方法，由于返回值是泛型，所以需要返回接口对象
    @Override
    public HelloService create(Throwable throwable) {
        return new HelloService() {
            @Override
            public String hello() {
                return throwable.getMessage();
            }
        };
    }
}
```

```text
/**
* 服务名称是配置服务提供者的配置spring.application.name的名称
*/
@FeignClient(name = "01-springcloud-service-provider", 
                fallbackFactory= MyFallbackFactory.class 
                /** fallback = MyFallback.class **/
                )
public interface HelloService {

    /**
    * 声明一个方法，这个方法就是远程的服务提供者的方法
    * @return
    */
    @RequestMapping("/service/hello")
    public String hello();
}
```
