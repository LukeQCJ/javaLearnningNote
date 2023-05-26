# 第一节 Filter过滤器和FilterChain过滤器链

> Filter和FilterChain的关系咋看之下，是FilterChain中包含一个List<Filter>，
> 然后去遍历List来执行所有的Filter。真的是图样图森破。
> 
> 先装个逼，贴个源码：
```java
/**
 * A filter is an object that performs filtering tasks on either the request to
 * a resource (a servlet or static content), or on the response from a resource,
 * or both.
 * Filters perform filtering in the <code>doFilter</code> method.
 * @since Servlet 2.3
 */
public interface Filter {
    public void init(FilterConfig filterConfig) throws ServletException;
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException;
    public void destroy();
}
```
```java
/**
 * A FilterChain is an object provided by the servlet container to the developer
 * giving a view into the invocation chain of a filtered request for a resource.
 * Filters use the FilterChain to invoke the next filter in the chain, or if the
 * calling filter is the last filter in the chain, to invoke the resource at the
 * end of the chain.
 **/
public interface FilterChain {
    /**
     * Causes the next filter in the chain to be invoked, or if the calling
     * filter is the last filter in the chain, causes the resource at the end of
     * the chain to be invoked.
     */
    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException;

}
```

> 说了一堆，大概意思总结一下：
> Filter用来执行过滤任务。FilterChain用来查看资源过滤请求的调用链。
> Filter通过FilterChain来调用过滤链中的下一个Filter，如果当前执行的Filter是过滤链中的最后一个，则可以继续调用资源。
> 一脸懵逼，这是什么实现逻辑。先来看看下面这段代码的逻辑：
```java
public class IAClass {
    public void doSomething(IAChainClass chain) {
        System.out.println("i am IAClass"+chain.position);
        chain.doSomething();
    }

    static class IAChainClass {
        List<IAClass> IAChains = new ArrayList<IAClass>();

        public IAChainClass() {
            IAChains.add(new IAClass());
            IAChains.add(new IAClass());
            IAChains.add(new IAClass());
        }

        int position = 0;

        public void doSomething() {
            if (position == IAChains.size()) {
                System.out.println("end");
                return;
            }
            IAClass ia = IAChains.get(position++);
            ia.doSomething(this);
        }
    }

    public static void main(String[] args){
        new IAChainClass().doSomeThing();
    }
}
```
> 打印结果是：
```
i am IAClass1
i am IAClass2
i am IAClass3
end
``` 
在IAChainClass中，如果当前节点不是过滤链中最后一个节点，则调用当前节点的doSomething()时，同时做两件事：
1）将当前过滤器链继续传入过滤器，
2）将过滤器链的当前节点后移一位；直到所有过滤器执行完成再退出。

作者：wencai
链接：https://www.jianshu.com/p/76d7ae3bf398
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。