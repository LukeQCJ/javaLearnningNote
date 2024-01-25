# 五、Curator客户端的使用

## 1、Curator介绍
Curator是Netflix公司开源的一套zookeeper客户端框架，Curator是对Zookeeper支持最好的客户端框架。
Curator封装了大部分Zookeeper的功能，比如Leader选举、分布式锁等，减少了技术人员在使用Zookeeper时的底层细节开发工作。

## 2、引入依赖
```text
<!--Curator-->
<dependency>
	<groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.12.0</version>
</dependency>
<dependency>
	<groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.12.0</version>
</dependency>
<!--Zookeeper-->
<dependency>
	<groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.7.1</version>
</dependency>
```

## 3、application.properties配置文件
```yaml
curator.retryCount=5 # 重试次数
curator.elapsedTimeMs=5000 # 临时节点的超时时间
curator.connectionString=172.0.0.1:2181 # 连接地址
curator.sessionTimeoutMs=60000 # session超时时间
curator.connectionTimeoutMs=5000 # 连接超时时间
```

## 4、编写配置curator配置类
```java
@Data
@Component
@ConfigurationProperties(prefix = "curator")
public class WrapperZK {
    private int retryCount;
    
    private int elapsedTimeMs;
    
    private String connectionString;
    
    private int sessionTimeoutMs;
    
    private int connectionTimeoutMs;
}

//引用配置类
@Configuration
public class CuratorConfig {

    @Autowired
    private WrapperZK wrapperZK;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework(){
        return CuratorFrameworkFactory.newClient(
            wrapperZK.getConnectionString(),
            wrapperZK.getSessionTimeoutMs(),
            wrapperZK.getConnectionTimeoutMs(),
            new RetryNTimes(wrapperZK.getRetryCount(), wrapperZK.getElapsedTimeMs())
        );
    }

}
```

## 5、测试
```text
@Autowired
private CuratorFramework curatorFramework;

@Test
//添加节点
void createNode() throws Exception{
    //添加默认(持久)节点
    String path = curatorFramework.create().forPath("/curator-node");
    //添加临时序号节点
    //String path2 = curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                        .forPath("/curator-nodes", "messageDate".getBytes());
    System.out.println(String.format("curator create node :%s  successfully!", path));
    //		System.in.read();
}

@Test
//获取节点值
void getDate() throws Exception {
    byte[] bttes = curatorFramework.getData().forPath("/curator-node");
    System.out.println("bttes = " + bttes);
}

@Test
//设置节点值
void setDate() throws Exception {
    curatorFramework.setData().forPath("/curator-node", "newMessage".getBytes());
    byte[] bytes = curatorFramework.getData().forPath("/curator-node");
    System.out.println("bytes = " + bytes);
}

@Test
//创建多级节点
void createWithParent() throws Exception {
    String pathWithParent = "/node-parent/sub-node-1";
    String path = curatorFramework.create().creatingParentContainersIfNeeded().forPath(pathWithParent);
    System.out.println(String.format("curator create node :%s success!", path));
}

@Test
//删除节点
void delete() throws Exception {
    String path = "/node-parent";
    //删除节点的同时一并删除子节点
    curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
}
```