# 第07节 Spring Bean的属性填充过程--populateBean方法

## 前言
在前面的doCreateBean方法中，我们了解到，populateBean是负责填充Bean实例属性的。

此时Bean中需要依赖注入的成员已经在applyMergedBeanDefinitionPostProcessors中被对应的后置处理器进行了存储，
最终的成员被封装到了AutowiredAnnotationBeanPostProcessor#injectionMetadataCache这个集合中.

## 一、Dependencies和Dependency Injection

很多人可能还不清楚什么是依赖和依赖注入，这是理解Spring IoC的一个核心概念，
下面简单从代码的角度谈谈什么是依赖和依赖注入。

一个简单的MVC登陆入口
```text
@RestController
@RequestMapping("/web")
public class UserController {

    @Autowired
    @Qualifier("normalUserService")
    private UserService userService;

    /**
     * 用户登陆入口
     */
    @PostMapping("/login")
    public void login(User user) {
        userService.login(user);
    }
}
```
软件架构设计原则中要求面向接口编程，为此MVC架构中往往采取的是上面代码展示的这种结构，
UserController 依赖于 UserService接口 的实现类.此时,UserController就与UserService直接形成了依赖关系。

此时，如果没有Spring，我们有三种方式去将UserService注入进来:
```text
1) 每次调用login的时候,new UserService();
2) 通过setter的方式进行注入.即UserController中提供setUserService(UserService userService);
3) 通过构造方法进行注入,public UserController(UserService userService);
```

其中,new的方式是耦合度较高的做法。
所以在Spring中，提供了构造注入和setter注入的方式来落地IoC思想(Inversion of Control)。
与用户主动调用的方式不同，在Spring框架中，往往只需要声明好 依赖关系，
框架就会自动生成好用户需要的Java对象, 这就是所谓的Bean。
我们一般将UserController中去注入UserService这个过程称为依赖注入，即专业术语上的DI(Dependency Injection)。

## 二、自动装配

@Autowired注解和@Resource注解都是自动装配的代表,思考一下,自动装配大概是什么过程?

从前面的文章中我们学习了，Spring将Java对象中的信息抽象成了BeanDefinition,
最后通过getBean方法来加载非延迟加载的单例Bean,每当getBean装配完一个Bean之后，就会添加到单例缓存中。

那么自动装配的过程，就是按规则(如果指定)从容器中获取到依赖的Bean，然后通过反射进行赋值完成装配的的过程。

## 三、populateBean的总体流程

```text
1) 激活InstantiationAwareBeanPostProcessor后置处理器的InstantiationAwareBeanPostProcessor方法: 
    在实例化bean之后,Spring属性填充之前执行的钩子方法,
   这是在Spring的自动装配开始之前对该bean实例执行自定义字段注入的回调,也是最后一次机会在自动装配前修改Bean的属性值。

2) 解析依赖注入的方式,将属性装配到PropertyValues中: resolvedAutowireMode。

3) 激活InstantiationAwareBeanPostProcessor#postProcessProperties: 对@AutoWired标记的属性进行依赖注入。

4) 依赖检查: checkDependencies。

5) 将解析的值用BeanWrapper进行包装: applyPropertyValues。 
```

### 3.1 InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation
**AbstractAutowireCapableBeanFactory#populateBean**
```text
        // InstantiationAwareBeanPostProcessors最后一次进行对bean的属性修改
        // 采用职责链的方式对所有实现了InstantiationAwareBeanPostProcessor的后置处理器调用.
        // 直到某个InstantiationAwareBeanPostProcessor在postProcessAfterInstantiation中返回了false
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof InstantiationAwareBeanPostProcessor) {
                    InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                    // 如果返回了false,直接中断,不进行下面的操作
                    if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                        return;
                    }
                }
            }
        }
```
populateBean中，首先会经过判空校验，校验通过后.
检查用户是否有注册 InstantiationAwareBeanPostProcessor,
如果有，使用责任链模式激活这些后置器中的postProcessAfterInstantiation方法,
如果某个后置处理器返回了false,那么Spring就不会执行框架的自动装配逻辑了。
官方的建议是不建议去扩展此后置处理器，而是推荐扩展自BeanPostProcessor或从InstantiationAwareBeanPostProcessorAdapter派生.

### 3.2 根据注入方式解析属性到PropertyValues
```text
        // 这里的pvs其实是一个MutablePropertyValues实例
        // 提供对属性的读写操作实现，同时可以通过构造函数实现深拷贝
        // 获取BeanDefinition里面为Bean设置的属性值
        PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null);
        // 根据Bean配置的依赖注入方式完成注入,默认是0，即不走以下逻辑
        // 如果设置了相关的依赖装配方式，会遍历Bean的属性，根据type或者name完成相应注入
        int resolvedAutowireMode = mbd.getResolvedAutowireMode();
        if (resolvedAutowireMode == AUTOWIRE_BY_NAME || resolvedAutowireMode == AUTOWIRE_BY_TYPE) {
            MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
            // Add property values based on autowire by name if applicable.
            // 根据beanName进行autowired自动装配逻辑
            if (resolvedAutowireMode == AUTOWIRE_BY_NAME) {
                autowireByName(beanName, mbd, bw, newPvs);
            }
            // Add property values based on autowire by type if applicable.
            // 根据Type进行autowired自动装配逻辑
            if (resolvedAutowireMode == AUTOWIRE_BY_TYPE) {
                autowireByType(beanName, mbd, bw, newPvs);
            }
            pvs = newPvs;
        }
```

I) 首先从BeanDefinition中取出propertyValues,具体的调用方法在AbstractBeanDefinition#getPropertyValues中,
    返回的类型为MutablePropertyValues.

II) 解析依赖装配入的方式。在AutowireCapableBeanFactory接口中声明了5种依赖注入的方式:

| resolvedAutowireMode | 	依赖注入方式               | 	描述                                            |
|----------------------|-----------------------|------------------------------------------------|
| 0                    | 	AUTOWIRE_NO          | 	没有显式配置上装配的方式                                  |
| 1                    | 	AUTOWIRE_BY_NAME     | 	按beanName进行装配                                 |
| 2                    | 	AUTOWIRE_BY_TYPE     | 	按type进行装配                                     |
| 3                    | 	AUTOWIRE_CONSTRUCTOR | 	在构造函数中进行装配                                    |
| 4                    | 	AUTOWIRE_AUTODETECT  | 	通过内省bean类确定适当的自动装配策略,Spring已经将其标注为@Deprecated |

一般以注解的形式，默认都解析为0，也就是没有显式配置自动装配策略.

什么情况会进入if条件中的代码块，通常是在XML配置文件中显式指定了autowired或者在Java配置类中@Bean上，声明autowired.
简单给个示例:
```text
@Bean(autowire = Autowire.BY_NAME)
```
    
### 3.3 InstantiationAwareBeanPostProcessor#postProcessProperties

如果没有显式声明自动装配的方式(@Autowired注解)，那么就会使用到InstantiationAwareBeanPostProcessor
这个后置处理器的postProcessProperties方法.
```text
        // 容器是否注册了InstantiationAwareBeanPostProcessors
        boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
        // 是否进行依赖检查，默认为false
        boolean needsDepCheck = (mbd.getDependencyCheck() != AbstractBeanDefinition.DEPENDENCY_CHECK_NONE);

        PropertyDescriptor[] filteredPds = null;
        if (hasInstAwareBpps) {
            if (pvs == null) {
                pvs = mbd.getPropertyValues();
            }
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof InstantiationAwareBeanPostProcessor) {
                    InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                    // 对@AutoWired标记的属性进行依赖注入
                    PropertyValues pvsToUse = ibp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
                    if (pvsToUse == null) {
                        if (filteredPds == null) {
                            filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
                        }
                        // 对解析完未设置的属性再进行处理
                        pvsToUse = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
                        if (pvsToUse == null) {
                            return;
                        }
                    }
                    pvs = pvsToUse;
                }
            }
        }
```
AutowiredAnnotationBeanPostProcessor#postProcessProperties
```text
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
        // 获取指定类中被@Autowired注解标记的metadata.
        // metadata在实例化后的applyMergedBeanDefinitionPostProcessors中进行了存储
        // 此时的findAutowiringMetadata是从injectionMetadataCache缓存中读取metadata
        InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
        try {
            // 对Bean的属性进行自动注入
            metadata.inject(bean, beanName, pvs);
        }
        catch (BeanCreationException ex) {
            throw ex;
        }
        catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
        }
        return pvs;
    }
```
在前面的doCreateBean中我们对applyMergedBeanDefinitionPostProcessors进行了分析
，在它的postProcessMergedBeanDefinition中已经调用了 findAutowiringMetadata这个方法
对Bean上被@Autowired标记的成员进行了存储，此时已经进入到了属性填充阶段，
从injectionMetadataCache这个缓存区即可获取InjectionMetadata类型的metadata,即依赖注入的元数据.

InjectionMetadata中提供了一个inject方法，执行自动注入依赖的逻辑.

#### 3.3.1 org.springframework.beans.factory.annotation.InjectionMetadata#inject
```text
    public void inject(Object target, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
        Collection<InjectedElement> checkedElements = this.checkedElements;
        Collection<InjectedElement> elementsToIterate =
                (checkedElements != null ? checkedElements : this.injectedElements);
        if (!elementsToIterate.isEmpty()) {
            for (InjectedElement element : elementsToIterate) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Processing injected element of bean '" + beanName + "': " + element);
                }
                // 调用InjectedElement#inject,这里是多态的实现
                // @Autowired关注AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement.inject
                element.inject(target, beanName, pvs);
            }
        }
    }
```
这里逻辑比较清晰，查看当前的checkedElements是否为空，
如果为空,解析injectedElements.然后遍历对element执行inject操作.

AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement#inject
```text
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            // 当前存储的需要注入的成员
            Field field = (Field) this.member;
            Object value;
            // 如果该成员的值被缓存了，从缓存中获取
            if (this.cached) {
                // 最终调用DefaultListableBeanFactory的resolveDependency
                value = resolvedCachedArgument(beanName, this.cachedFieldValue);
            }
            else {
                // 为该成员创建一个DependencyDescriptor.
                DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
                // 设置当前bean的Class
                desc.setContainingClass(bean.getClass());
                Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
                Assert.state(beanFactory != null, "No BeanFactory available");
                // 获取类型转换器
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                try {
                    // 最终调用DefaultListableBeanFactory的resolveDependency
                    value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                }
                catch (BeansException ex) {
                    throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
                }
                synchronized (this) {
                    // 如果成员变量的值没有缓存
                    if (!this.cached) {
                        // 成员变量的值不为null,并且required==true
                        if (value != null || this.required) {
                            this.cachedFieldValue = desc;
                            // 注册依赖关系
                            registerDependentBeans(beanName, autowiredBeanNames);
                            if (autowiredBeanNames.size() == 1) {
                                String autowiredBeanName = autowiredBeanNames.iterator().next();
                                // 依赖对象类型和字段类型匹配，默认按类型注入
                                if (beanFactory.containsBean(autowiredBeanName) &&
                                        beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                                    this.cachedFieldValue = new ShortcutDependencyDescriptor(
                                            desc, autowiredBeanName, field.getType());
                                }
                            }
                        }
                        else {
                            this.cachedFieldValue = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                // 调用反射进行赋值
                field.set(bean, value);
            }
        }
    }
```
OK,来到解析依赖的关键步骤了.
```text
1) 先尝试从缓存中获取该依赖对应的Bean.
2) 如果BeanFactory中没有该依赖对应的Bean.为该成员创建一个DependencyDescriptor,
    然后调用beanFactory.resolveDependency来加载Bean.
3) 注册Bean之间的依赖关系.
4) 将获取到的Bean调用反射进行填充.
    field.set(bean, value)，注意在这一步之前，Spring对field的权限进行了设置,field.setAccessible(true)。
```
其中，自动装配的逻辑就封装在了beanFactory.resolveDependency中.继续前进一探究竟.

#### 3.3.2 DefaultListableBeanFactory#resolveDependency
```text
    public Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName,
            @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException {

        descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
        if (Optional.class == descriptor.getDependencyType()) {
            return createOptionalDependency(descriptor, requestingBeanName);
        }
        else if (ObjectFactory.class == descriptor.getDependencyType() ||
                ObjectProvider.class == descriptor.getDependencyType()) {
            return new DependencyObjectProvider(descriptor, requestingBeanName);
        }
        else if (javaxInjectProviderClass == descriptor.getDependencyType()) {
            return new Jsr330Factory().createDependencyProvider(descriptor, requestingBeanName);
        }
        else {
            Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(
                    descriptor, requestingBeanName);
            if (result == null) {
                // 解析依赖
                result = doResolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
            }
            return result;
        }
    }
```
这里Spring会对依赖做一些适配，我们主要看doResolveDependency这个解析依赖的方法.

DefaultListableBeanFactory#doResolveDependency
```text
    public Object doResolveDependency(DependencyDescriptor descriptor, @Nullable String beanName,
            @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException {

        InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint(descriptor);
        try {
            // 从容器中获取依赖,在debug环境下点进去会发现，会到达beanFactory.getBean()中
            Object shortcut = descriptor.resolveShortcut(this);
            // 如果可以从容器中获取到bean，直接返回
            if (shortcut != null) {
                return shortcut;
            }

            Class<?> type = descriptor.getDependencyType();
            // 处理@Value注解
            Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
            if (value != null) {
                if (value instanceof String) {
                    String strVal = resolveEmbeddedValue((String) value);
                    BeanDefinition bd = (beanName != null && containsBean(beanName) ?
                            getMergedBeanDefinition(beanName) : null);
                    value = evaluateBeanDefinitionString(strVal, bd);
                }
                TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
                try {
                    return converter.convertIfNecessary(value, type, descriptor.getTypeDescriptor());
                }
                catch (UnsupportedOperationException ex) {
                    // A custom TypeConverter which does not support TypeDescriptor resolution...
                    return (descriptor.getField() != null ?
                            converter.convertIfNecessary(value, type, descriptor.getField()) :
                            converter.convertIfNecessary(value, type, descriptor.getMethodParameter()));
                }
            }
            // 如果标识@Autowired注解的成员变量是复合类型,如:数组、List、Map等.
            // 从这里获取@Autowired中的值
            Object multipleBeans = resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter);
            if (multipleBeans != null) {
                return multipleBeans;
            }
            // 如果被@Autowired标注的成员并非复合对象
            Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
            if (matchingBeans.isEmpty()) {
                // 如果找不到,校验当前是否标注了required为true.
                if (isRequired(descriptor)) {
                    // 如果@Autowired标注了(required = true),但是无法匹配到相应的bean,抛出NoSuchBeanDefinitionException异常
                    raiseNoMatchingBeanFound(type, descriptor.getResolvableType(), descriptor);
                }
                return null;
            }

            String autowiredBeanName;
            Object instanceCandidate;
            // 如果匹配到了不止一个Bean,看看是否标注了@Primary和@Priority
            if (matchingBeans.size() > 1) {
                autowiredBeanName = determineAutowireCandidate(matchingBeans, descriptor);
                if (autowiredBeanName == null) {
                    if (isRequired(descriptor) || !indicatesMultipleBeans(type)) {
                        // 如果没有声明，则直接抛出NoUniqueBeanDefinitionException
                        return descriptor.resolveNotUnique(descriptor.getResolvableType(), matchingBeans);
                    }
                    else {
                        // In case of an optional Collection/Map, silently ignore a non-unique case:
                        // possibly it was meant to be an empty collection of multiple regular beans
                        // (before 4.3 in particular when we didn't even look for collection beans).
                        return null;
                    }
                }
                instanceCandidate = matchingBeans.get(autowiredBeanName);
            }
            else {
                // We have exactly one match.
                Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
                // key为被依赖的候选者名称,例如:UserController依赖UserService.
                // 此时autowiredBeanName=userService
                autowiredBeanName = entry.getKey();
                // Class,先选举，选举结束之后再进行实例化
                instanceCandidate = entry.getValue();
            }

            if (autowiredBeanNames != null) {
                autowiredBeanNames.add(autowiredBeanName);
            }
            if (instanceCandidate instanceof Class) {
                // 将获取到的候选者Class进行getBean
                instanceCandidate = descriptor.resolveCandidate(autowiredBeanName, type, this);
            }
            Object result = instanceCandidate;
            if (result instanceof NullBean) {
                if (isRequired(descriptor)) {
                    raiseNoMatchingBeanFound(type, descriptor.getResolvableType(), descriptor);
                }
                result = null;
            }
            if (!ClassUtils.isAssignableValue(type, result)) {
                throw new BeanNotOfRequiredTypeException(autowiredBeanName, type, instanceCandidate.getClass());
            }
            return result;
        }
        finally {
            ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
        }
    }
```
这里的代码逻辑就跟我们平时编程是息息相关的了。
```text
1) 调用descriptor.resolveShortcut查看当前工厂是否已经加载过相同的Bean,如果是则直接返回.
2) 处理@Value解析的逻辑.
3) 如果当前注入的是复合对象,调用resolveMultipleBeans.
4) 如果只是注入普通的Bean对象,查找符合条件的候选名单.返回一个Map.
    为什么返回一个Map呢,这是因为一个接口可以有多个实现类，按照类型查找，就会把实现该接口的实现类返回.
    在应对这种多个候选Bean的时候，Spring会去判断是是否声明了@Primary注解或者@Order注解来决定注入哪个Bean.
    如果没有声明，再判断是否声明了required=false，
    如果required为默认的,则抛出NoUniqueBeanDefinitionException异常。
5) 匹配完候选名单后，对候选名单进行resolveCandidate操作，点进去方法会发现，
    其实是调用了beanFactory.getBean(beanName).
```
什么是复合对象?
Spring不仅仅只可以注入单一的Bean对象，还支持数组、集合、Stream、Map等方式的注入.

#### 3.3.3 如果Autowired按byType的方式无法挑选出最合适的Bean如何进行降级处理

答案在determineAutowireCandidate中,在通过byType的方式无法选出最合适的Bean后，
Spring会用byName的方式对比出当前属性名与候选Bean名单中的candidateName是否匹配来做最终的处理.

DefaultListableBeanFactory#determineAutowireCandidate
```text
    protected String determineAutowireCandidate(Map<String, Object> candidates, DependencyDescriptor descriptor) {
        Class<?> requiredType = descriptor.getDependencyType();
        // 根据@Primary注解标签来选择最优解
        String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
        if (primaryCandidate != null) {
            return primaryCandidate;
        }
        // 根据@Order、@Priority以及实现了Order接口的序号来最合适的Bean(序号越小越合适)进行注入
        String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
        if (priorityCandidate != null) {
            return priorityCandidate;
        }
        // Fallback
        for (Map.Entry<String, Object> entry : candidates.entrySet()) {
            String candidateName = entry.getKey();
            Object beanInstance = entry.getValue();
            // 如果无法通过上面两个方法找到最优解的Bean:
            // 如果类型已经在resolvableDependencies中，直接返回已经注册的对象.
            // 如果byType的方式找不到,尝试使用byName的方式寻找依赖
            // 如果属性名称和某个候选者的Bean名称或者别名一致,则直接将该Bean返回
            if ((beanInstance != null && this.resolvableDependencies.containsValue(beanInstance)) ||
                    matchesBeanName(candidateName, descriptor.getDependencyName())) {
                return candidateName;
            }
        }
        return null;
    }
```
假设UserService有两个实现类,VipUserService的beanName被主动声明为vip,NormalUserService声明为normal,
那么你在Controller中可以这样写来注入VipUserService:
```text
@Autowired
UserService vip;
```
### 4 依赖检查
   Spring在最新的版本已经不推荐使用，所以这里我们也不做重点讲解.

### 5 将解析的值用BeanWrapper进行包装-applyPropertyValues.

通过注解形式配置的Bean并不会往pvs中填充值，笔者在一些书上看到都是重点解析这个方法，
目前Spring5.1的版本进行debug未发现进入到这个方法中，所以也不做详细的讲解。
我更倾向于这是为了兼容XML的做法留下的方法.
```text
protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs) {
        if (pvs.isEmpty()) {
            return;
        }

        if (System.getSecurityManager() != null && bw instanceof BeanWrapperImpl) {
            ((BeanWrapperImpl) bw).setSecurityContext(getAccessControlContext());
        }

        MutablePropertyValues mpvs = null;
        List<PropertyValue> original;

        if (pvs instanceof MutablePropertyValues) {
            mpvs = (MutablePropertyValues) pvs;
            if (mpvs.isConverted()) {
                // Shortcut: use the pre-converted values as-is.
                try {
                    bw.setPropertyValues(mpvs);
                    return;
                }
                catch (BeansException ex) {
                    throw new BeanCreationException(
                            mbd.getResourceDescription(), beanName, "Error setting property values", ex);
                }
            }
            original = mpvs.getPropertyValueList();
        }
        else {
            original = Arrays.asList(pvs.getPropertyValues());
        }

        TypeConverter converter = getCustomTypeConverter();
        if (converter == null) {
            converter = bw;
        }
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, converter);

        // Create a deep copy, resolving any references for values.
        List<PropertyValue> deepCopy = new ArrayList<>(original.size());
        boolean resolveNecessary = false;
        for (PropertyValue pv : original) {
            if (pv.isConverted()) {
                deepCopy.add(pv);
            }
            else {
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
                Object convertedValue = resolvedValue;
                boolean convertible = bw.isWritableProperty(propertyName) &&
                        !PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName);
                if (convertible) {
                    convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);
                }
                // Possibly store converted value in merged bean definition,
                // in order to avoid re-conversion for every created bean instance.
                if (resolvedValue == originalValue) {
                    if (convertible) {
                        pv.setConvertedValue(convertedValue);
                    }
                    deepCopy.add(pv);
                }
                else if (convertible && originalValue instanceof TypedStringValue &&
                        !((TypedStringValue) originalValue).isDynamic() &&
                        !(convertedValue instanceof Collection || ObjectUtils.isArray(convertedValue))) {
                    pv.setConvertedValue(convertedValue);
                    deepCopy.add(pv);
                }
                else {
                    resolveNecessary = true;
                    deepCopy.add(new PropertyValue(pv, convertedValue));
                }
            }
        }
        if (mpvs != null && !resolveNecessary) {
            mpvs.setConverted();
        }

        // Set our (possibly massaged) deep copy.
        try {
            bw.setPropertyValues(new MutablePropertyValues(deepCopy));
        }
        catch (BeansException ex) {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Error setting property values", ex);
        }
    }

    /**
     * Convert the given value for the specified target property.
     */
    @Nullable
    private Object convertForProperty(
            @Nullable Object value, String propertyName, BeanWrapper bw, TypeConverter converter) {

        if (converter instanceof BeanWrapperImpl) {
            return ((BeanWrapperImpl) converter).convertForProperty(value, propertyName);
        }
        else {
            PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
            MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
            return converter.convertIfNecessary(value, pd.getPropertyType(), methodParam);
        }
    }
```
从XML中配置的<bean>将所有的属性声明为了字符串，因此在这里需要做一些类型的解析和强转.核心方法:
```text
解析Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
注入bw.setPropertyValues(new MutablePropertyValues(deepCopy));
```

## 总结
```text
1) 注解驱动的Bean执行属性填充并不在autowireByName和autowireByType中，
    而是在AutowiredAnnotationBeanPostProcessor这个后置处理器的postProcessProperties中.
2) 在做属性填充时，如果当前的Bean实例依赖的成员(另一个Bean)未被加载，会进入选举候选名单的逻辑中,进行各种判断后，
    选出最适合的Bean实例进行getBean操作.
3) @Autowired在进行自动装配的过程中，默认按照"byType"的方式进行Bean加载，
    如果出现无法挑选出合适的Bean的情况，再将属性名与候选Bean名单中的beanName进行对比.
4) 正确地声明@Primary和Order等注解让Bean在多态的选举中优选胜出.
5) required=false可以让程序在找不到Bean的时候不抛出异常，但是调用期间还是会报错(缓兵之计),不建议这种使用.
6) XML的自动装配模式与注解驱动的模式在代码上是不同的分岔.
```
