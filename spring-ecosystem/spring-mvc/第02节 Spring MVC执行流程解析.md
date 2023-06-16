# 第02节 Spring MVC执行流程解析

## 一、Spring MVC的执行流程

上面介绍了Spring MVC的使用，配置了一个DispatcherServlet作为所有请求的入口，
DispatcherServlet继承自抽象类FrameworkServlet，
而FrameworkServlet又继承自HttpServlet，
所以当有请求进来时，会先进入到FrameworkServlet的service()方法中，
而在该方法中又会去调用父类HttpServlet的service()方法，
该类的方法在《从Servlet到Spring MVC》中已经介绍过了，它会根据不同的请求去调用doGet()、doPost()等方法。

进入FrameworkServlet.service()方法：
```text
protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
    if (httpMethod == HttpMethod.PATCH || httpMethod == null) {
        processRequest(request, response);
    }
    else {
        // 去调用HttpServlet的service()方法，分别处理GET、POST等类型的请求
        super.service(request, response);
    }
}
```
进入HttpServlet.service()方法:
```text
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        if (method.equals(METHOD_GET)) {
            long lastModified = getLastModified(req);
            if (lastModified == -1) {
                // servlet doesn't support if-modified-since, no reason
                // to go through further expensive logic
                doGet(req, resp); // 处理GET请求
            } else {
                long ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
                if (ifModifiedSince < lastModified) {
                    // If the servlet mod time is later, call doGet()
                    // Round down to the nearest second for a proper compare
                    // A ifModifiedSince of -1 will always be less
                    maybeSetLastModified(resp, lastModified);
                    doGet(req, resp); // 处理GET请求
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                }
            }
        } else if (method.equals(METHOD_HEAD)) {
            long lastModified = getLastModified(req);
            maybeSetLastModified(resp, lastModified);
            doHead(req, resp); // 处理HEAD请求
        } else if (method.equals(METHOD_POST)) {
            doPost(req, resp); // 处理POST请求
        } else if (method.equals(METHOD_PUT)) {
            doPut(req, resp); // 处理PUT请求
        } else if (method.equals(METHOD_DELETE)) {
            doDelete(req, resp); // 处理DELETE请求
        } else if (method.equals(METHOD_OPTIONS)) {
            doOptions(req,resp); // 处理OPTIONS请求
        } else if (method.equals(METHOD_TRACE)) {
            doTrace(req,resp); // 处理TRACE请求
        } else {
            //
            // Note that this means NO servlet supports whatever
            // method was requested, anywhere on this server.
            //
            String errMsg = lStrings.getString("http.method_not_implemented");
            Object[] errArgs = new Object[1];
            errArgs[0] = method;
            errMsg = MessageFormat.format(errMsg, errArgs);
            
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, errMsg);
        }
    }
```
而FrameworkServlet中全部实现了这些doXxx()方法，而在这些doXxx()方法中，又都会去调用processRequest()方法。
进入FrameworkServlet.doXxx()方法：
```text
@Override
protected final void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // 处理请求
    processRequest(request, response);
}

@Override
protected final void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    // 处理请求
    processRequest(request, response);
}
```
进入FrameworkServlet.processRequest()方法，
在processRequest()方法中，除了加载一些上下文信息、绑定参数之后，最核心的就是去调用DispatcherServlet的doService()方法。
```text
protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
    LocaleContext localeContext = buildLocaleContext(request);

    RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());

    initContextHolders(request, localeContext, requestAttributes);
    // 【重要】处理请求
    doService(request, response);
}
```
进入DispatcherServlet.doService()方法：
```text
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logRequest(request);

		// Keep a snapshot of the request attributes in case of an include,
		// to be able to restore the original attributes after the include.
		Map<String, Object> attributesSnapshot = null;
		if (WebUtils.isIncludeRequest(request)) {
			attributesSnapshot = new HashMap<>();
			Enumeration<?> attrNames = request.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String attrName = (String) attrNames.nextElement();
				if (this.cleanupAfterInclude || attrName.startsWith(DEFAULT_STRATEGIES_PREFIX)) {
					attributesSnapshot.put(attrName, request.getAttribute(attrName));
				}
			}
		}

		// Make framework objects available to handlers and view objects.
		request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
		request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
		request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
		request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());

		if (this.flashMapManager != null) {
			FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
			if (inputFlashMap != null) {
				request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
			}
			request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
			request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
		}

		try {
		    // 【重要】请求派送
			doDispatch(request, response);
		}
		finally {
			if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
				// Restore the original attribute snapshot, in case of an include.
				if (attributesSnapshot != null) {
					restoreAttributesAfterInclude(request, attributesSnapshot);
				}
			}
		}
	}
```
在doService()方法中，会设置一些请求的参数，但最核心的是去调用doDispatch()方法，
而doDispatch()才是SpringMVC处理请求最核心的方法，
下面介绍一下doDispatch()方法的执行流程，以@RequestMapping注解为例，这个也是我们开发中用的最多的。

### 1.1 获取处理器执行链
在doDispatch()方法中，首先会去解析请求，得到一个处理器执行链，包含了拦截器集合和处理方法，
而在映射的时候，就会用得到HandlerMapping，
在《从Servlet到Spring MVC》中，介绍了如何配置HandlerMapping，如果没有配置，SpringMVC提供了四个默认的配置。

进入DispatcherServlet.doDispatch()方法：
```text
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		boolean multipartRequestParsed = false;

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

		try {
			ModelAndView mv = null;
			Exception dispatchException = null;

			try {
				processedRequest = checkMultipart(request);
				multipartRequestParsed = (processedRequest != request);

				// Determine handler for the current request.
				// 【重要】获取请求处理器映射器
				mappedHandler = getHandler(processedRequest);
				if (mappedHandler == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				// 【重要】获取请求处理器适配器
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

				// Process last-modified header, if supported by the handler.
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}

				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
					return;
				}

				// Actually invoke the handler.
				// 【重要】进行处理器 处理操作
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				if (asyncManager.isConcurrentHandlingStarted()) {
					return;
				}

				applyDefaultViewName(processedRequest, mv);
				mappedHandler.applyPostHandle(processedRequest, response, mv);
			}
			catch (Exception ex) {
				dispatchException = ex;
			}
			catch (Throwable err) {
				// As of 4.3, we're processing Errors thrown from handler methods as well,
				// making them available for @ExceptionHandler methods and other scenarios.
				dispatchException = new NestedServletException("Handler dispatch failed", err);
			}
			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
		}
		catch (Exception ex) {
			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
		}
		catch (Throwable err) {
			triggerAfterCompletion(processedRequest, response, mappedHandler,
					new NestedServletException("Handler processing failed", err));
		}
		finally {
			if (asyncManager.isConcurrentHandlingStarted()) {
				// Instead of postHandle and afterCompletion
				if (mappedHandler != null) {
					mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
				}
			}
			else {
				// Clean up any resources used by a multipart request.
				if (multipartRequestParsed) {
					cleanupMultipart(processedRequest);
				}
			}
		}
	}
```
调用getHandler()方法就是去遍历所有的映射器处理器，看哪个能根据请求的url匹配到处理器方法。
进入DispatcherServlet.getHandler()方法：
```text
protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
    if (this.handlerMappings != null) {
        /** 
         * 拿到所有handlerMappings （容器启动阶段初始化：拿到所有实现了HandlerMapping的Bean）
         * @see DispatcherServlet#initHandlerMappings
         * 测试发现：不同的HandlerMapping可以有相同path，谁先解析到就用哪个。
         */
        for (HandlerMapping mapping : this.handlerMappings) {
            // 【重要】获取具体的 处理器执行链 mapping是RequestMappingHandlerMapping
            HandlerExecutionChain handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
    }
    return null;
}
```

#### 1.1.1 获取处理器

进入AbstractHandlerMapping.getHandler()方法：
```text
	@Override
	@Nullable
	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
	    // 【重要】获取内部处理器
		Object handler = getHandlerInternal(request);
		if (handler == null) {
			handler = getDefaultHandler();
		}
		if (handler == null) {
			return null;
		}
		// Bean name or resolved handler?
		if (handler instanceof String) {
			String handlerName = (String) handler;
			handler = obtainApplicationContext().getBean(handlerName);
		}

        // 【重要】获取【处理器执行链】
		HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);

		if (logger.isTraceEnabled()) {
			logger.trace("Mapped to " + handler);
		} else if (logger.isDebugEnabled() && !request.getDispatcherType().equals(DispatcherType.ASYNC)) {
			logger.debug("Mapped to " + executionChain.getHandler());
		}
        // 跨站请求配置
		if (hasCorsConfigurationSource(handler) || CorsUtils.isPreFlightRequest(request)) {
			CorsConfiguration config = (this.corsConfigurationSource != null ? this.corsConfigurationSource.getCorsConfiguration(request) : null);
			CorsConfiguration handlerConfig = getCorsConfiguration(handler, request);
			config = (config != null ? config.combine(handlerConfig) : handlerConfig);
			executionChain = getCorsHandlerExecutionChain(request, executionChain, config);
		}

		return executionChain;
	}
```
回到RequestMappingHandlerMapping.getHandlerInternal()方法：
```text
	@Override
	@Nullable
	protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
		request.removeAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		try {
			return super.getHandlerInternal(request);
		}
		finally {
			ProducesRequestCondition.clearMediaTypesAttribute(request);
		}
	}
```
进入AbstractHandlerMethodMapping.getHandlerInternal()方法：
```text
protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
    // 【简单】通过UrlPathHelper对象，用于来解析从request中解析出【请求映射路径】，不包括context/
    String lookupPath = initLookupPath(request);
    this.mappingRegistry.acquireReadLock();
    try {
        // 【重要】通过lookupPath解析最终的handler——HandlerMethod对象
        HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
        return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
    }
    finally {
        this.mappingRegistry.releaseReadLock();
    }
}
```
进入AbstractHandlerMapping.initLookupPath()方法：
```text
	protected String initLookupPath(HttpServletRequest request) {
		if (usesPathPatterns()) {
			request.removeAttribute(UrlPathHelper.PATH_ATTRIBUTE);
			// 【简单】从请求中解析出请求路径，比如 /app/testController/save
			RequestPath requestPath = ServletRequestPathUtils.getParsedRequestPath(request);
			String lookupPath = requestPath.pathWithinApplication().value();
			return UrlPathHelper.defaultInstance.removeSemicolonContent(lookupPath);
		}
		else {
			return getUrlPathHelper().resolveAndCacheLookupPath(request);
		}
	}
```
进入AbstractHandlerMethodMapping.lookupHandlerMethod()方法：
```text
	@Nullable
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		// 【重要】用于保存处理器方法映射的集合
		List<Match> matches = new ArrayList<>();
		// 【重要】从【处理器映射注册中心mappingRegistry】根据【请求路径lookupPath】【请求映射信息(RequestMappingInfo)】
		List<T> directPathMatches = this.mappingRegistry.getMappingsByDirectPath(lookupPath);
		if (directPathMatches != null) {
		    // 将 directPathMatches集合中item与request中的路径匹配，然后放入matches中
			addMatchingMappings(directPathMatches, matches, request);
		}
		if (matches.isEmpty()) {
			addMatchingMappings(this.mappingRegistry.getRegistrations().keySet(), matches, request);
		}
		if (!matches.isEmpty()) {
			Match bestMatch = matches.get(0);
			if (matches.size() > 1) {
				Comparator<Match> comparator = new MatchComparator(getMappingComparator(request));
				matches.sort(comparator);
				bestMatch = matches.get(0);
				if (logger.isTraceEnabled()) {
					logger.trace(matches.size() + " matching mappings: " + matches);
				}
				if (CorsUtils.isPreFlightRequest(request)) {
					for (Match match : matches) {
						if (match.hasCorsConfig()) {
							return PREFLIGHT_AMBIGUOUS_MATCH;
						}
					}
				}
				else {
					Match secondBestMatch = matches.get(1);
					if (comparator.compare(bestMatch, secondBestMatch) == 0) {
						Method m1 = bestMatch.getHandlerMethod().getMethod();
						Method m2 = secondBestMatch.getHandlerMethod().getMethod();
						String uri = request.getRequestURI();
						throw new IllegalStateException(
								"Ambiguous handler methods mapped for '" + uri + "': {" + m1 + ", " + m2 + "}");
					}
				}
			}
			// 将 最匹配的处理器方法 的 完全路径名(比如com.luke.controller#saveTest) 放入 request中
			request.setAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE, bestMatch.getHandlerMethod());
			// 也是在request中添加一些请求路径相关的属性
			handleMatch(bestMatch.mapping, lookupPath, request);
			return bestMatch.getHandlerMethod();
		}
		else {
			return handleNoMatch(this.mappingRegistry.getRegistrations().keySet(), lookupPath, request);
		}
	}
```
在lookupHandlerMethod()中，根据url来匹配mappingRegistry.pathLookup，
pathLookup是一个MultiValueMap，它最大的特点是value可以重复，所以同一个url可能会匹配到多个RequestMappingInfo。

pathLookup的数据，是在SpringMVC容器启动的时候，就回去加载解析的，
以@RequestMapping注解为例，在Bean实例化的过程中，就回去解析类中的方法是否有@RequestMapping注解，
然后拼接url作为pathLookup的key，将类以及方法封装成RequestMappingInfo。

如果通过pathLookup找到了url相匹配的处理器，这个时候还是不够，还需要去解析@RequestMapping注解中的method、header等属性是否匹配，
RequestMappingInfo中包含了【@RequestMapping注解所有配置的条件匹配器】，
比如ParamsRequestCondition、HeadersRequestCondition等。

如果直接通过url没有在pathLookup找到，则会去调用getMatchingMapping()方法，
通过pathMatcher来匹配，pathMatcher是一个AntPathMatcher类的实例，提供了按照通配符? * {匹配的逻辑。

如果匹配到多个按照? > * > {} >**进行排序，然后取最匹配的那一个
```text
protected String getMatchingMapping(String pattern, HttpServletRequest request) {
    String lookupPath = this.pathHelper.getLookupPathForRequest(request);
    String match = (this.pathMatcher.match(pattern, lookupPath) ? pattern : null);
    if (match != null) {
        this.matches.add(match);
    }
    return match;
}
```

#### 1.1.2 封装处理器执行链
得到处理器之后，还需要取获取配置的拦截器，然后封装成一个执行器链。
```text
public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {

    // 【重要】获取 处理器
    Object handler = getHandlerInternal(request);
    if (handler == null) {
        handler = getDefaultHandler();
    }
    if (handler == null) {
        return null;
    }
    
    ...... // 非核心代码，省略
    
    // 【重要】获取 处理器执行链
    HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);
    
    ...... // 非核心代码，省略
}
```
进入AbstractHandlerMapping.getHandlerExecutionChain()方法：
```text
	protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
		HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ?
				(HandlerExecutionChain) handler : new HandlerExecutionChain(handler));

		for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
			if (interceptor instanceof MappedInterceptor) {
				MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptor;
				if (mappedInterceptor.matches(request)) {
					chain.addInterceptor(mappedInterceptor.getInterceptor());
				}
			}
			else {
				chain.addInterceptor(interceptor);
			}
		}
		return chain;
	}
```
调用getHandlerExecutionChain()，首先将【处理器】封装成一个【HandlerExecutionChain】，然后遍历配置的所有【拦截器】，
只有与当前处理器匹配的，就加入到HandlerExecutionChain的【interceptorList】中，在执行【执行器】的方法前后，会调用拦截器的方法。

### 1.2 获取处理器适配器
获取完处理器之后，就要为处理器匹配最合适的适配器，那么适配器是干嘛的，简单的来说就是【解析参数】。

以@RequestMapping为例，它的方法参数，可以通过@RequestBody、@RequestParam等注解来获取参数，
那么肯定就要有能够解析这些注解的适配器。

如果没有通过Spring MVC的配置文件进行配置， 默认有HttpRequestHandlerAdapter、SimpleControllerHandlerAdapter、
RequestMappingHandlerAdapter和HandlerFunctionAdapter四种。
```text
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    
    ...... // 非核心代码，省略
    
    // 【重要】获取【处理器映射】
    mappedHandler = getHandler(processedRequest);
    if (mappedHandler == null) {
        noHandlerFound(processedRequest, response);
        return;
    }
    
    ...... // 非核心代码，省略
    
    // 【重要】获取【处理器适配器】
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

    ...... // 非核心代码，省略
    
    // 【重要】实际执行 处理器
    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
    
    ...... // 非核心代码，省略
}
```
进入DispatcherServlet.getHandlerAdapter()方法：
```text
	protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
		if (this.handlerAdapters != null) {
			for (HandlerAdapter adapter : this.handlerAdapters) {
			    // 【重要】判断处理器适配器 是否支持 处理器
				if (adapter.supports(handler)) {
					return adapter;
				}
			}
		}
		throw new ServletException("No adapter for handler [" + handler +
				"]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
	}
```
寻找最合适的处理器适配器也很简单，遍历所有的是适配器，然后调用它们的support()方法进行匹配。

进入AbstractHandlerMethodAdapter.supports()方法：
```text
	@Override
	public final boolean supports(Object handler) {
	    // 【简单】处理器 是 HandlerMethod 的实例 且 是内部支持
		return (handler instanceof HandlerMethod && supportsInternal((HandlerMethod) handler));
	}
```
进入RequestMappingHandlerAdapter.supportsInternal()方法：
```text
	@Override
	protected boolean supportsInternal(HandlerMethod handlerMethod) {
		return true;
	}
```
supports()的方法也很简单，以HttpRequestHandlerAdapter为例：

只需要判断当前处理器是不是HttpRequestHandler的实现类即可，而RequestMappingHandlerAdapter的supports()方法永远返回true
```text
public boolean supports(Object handler) {
    return (handler instanceof HttpRequestHandler);
}
```

### 1.3 执行处理器方法

在获取了【处理器适配器】后，在执行【处理器】的方法前，首先会去执行【拦截器】的前置方法。
在DispatcherServlet.doDispatch()方法中的关于执行拦截器的前置方法的代码块如下：
```text
    // 前置拦截器
    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
        // 返回false就不进行后续处理了
        return;
    }
```
进入HandlerExecutionChain.applyPreHandle()方法：
```text
	boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		for (int i = 0; i < this.interceptorList.size(); i++) {
			HandlerInterceptor interceptor = this.interceptorList.get(i);
			// 【重要】执行拦截器的前置方法preHandle
			if (!interceptor.preHandle(request, response, this.handler)) {
				triggerAfterCompletion(request, response, null);
				return false;
			}
			this.interceptorIndex = i;
		}
		return true;
	}
	
	void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable Exception ex) {
		for (int i = this.interceptorIndex; i >= 0; i--) {
			HandlerInterceptor interceptor = this.interceptorList.get(i);
			try {
				interceptor.afterCompletion(request, response, this.handler, ex);
			}
			catch (Throwable ex2) {
				logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
			}
		}
	}
```
执行完拦截器的前置方法后，就要正式执行处理器了。
```text
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    ModelAndView mv = null;
    
    ...... // 非核心代码，省略
    
    // 【重要】执行 处理器
    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
    
    ...... // 非核心代码，省略
}
```
由【处理器适配器】执行【处理器】业务逻辑。

进入AbstractHandlerMethodAdapter.handle()方法：
```text
	@Override
	@Nullable
	public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return handleInternal(request, response, (HandlerMethod) handler);
	}
```
进入RequestMappingHandlerAdapter.handleInternal()方法：
```text
	@Override
	protected ModelAndView handleInternal(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

		ModelAndView mav;
		checkRequest(request);

		// Execute invokeHandlerMethod in synchronized block if required.
		if (this.synchronizeOnSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Object mutex = WebUtils.getSessionMutex(session);
				synchronized (mutex) {
				    // 【重要】有session 同步执行 处理器
					mav = invokeHandlerMethod(request, response, handlerMethod);
				}
			}
			else {
				// No HttpSession available -> no mutex necessary
				// 【重要】没有session 执行 处理器
				mav = invokeHandlerMethod(request, response, handlerMethod);
			}
		}
		else {
			// No synchronization on session demanded at all...
			// 【重要】非同步执行 处理器
			mav = invokeHandlerMethod(request, response, handlerMethod);
		}

		if (!response.containsHeader(HEADER_CACHE_CONTROL)) {
			if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
				applyCacheSeconds(response, this.cacheSecondsForSessionAttributeHandlers);
			}
			else {
				prepareResponse(response);
			}
		}

		return mav;
	}
```
进入RequestMappingHandlerAdapter.invokeHandlerMethod()方法：
```text
protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
    HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
    // 把我们的请求req resp包装成 ServletWebRequest
    ServletWebRequest webRequest = new ServletWebRequest(request, response);
    // 获取容器中全局配置的InitBinder和当前HandlerMethod所对应的Controller中
    // 配置的InitBinder，用于进行参数的绑定
    WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);

    // 获取容器中全局配置的ModelAttribute和当前HandlerMethod所对应的Controller 中配置的ModelAttribute，
    // 这些配置的方法将会在目标方法调用之前进行调用
    ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);

    // 【重要】封装handlerMethod，会在调用前解析参数、调用后对返回值进行处理
    ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
    
    if (this.argumentResolvers != null) {
        // 让invocableMethod拥有参数解析能力
        invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
    }
    if (this.returnValueHandlers != null) {
        // 让invocableMethod拥有返回值处理能力
        invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
    }
    // 让invocableMethod拥有InitBinder解析能力
    invocableMethod.setDataBinderFactory(binderFactory);
    // 设置ParameterNameDiscoverer，该对象将按照一定的规则获取当前参数的名称
    invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
    // ModelAndView处理容器
    ModelAndViewContainer mavContainer = new ModelAndViewContainer();
    // 将request的Attribute复制一份到ModelMap
    mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
    // *调用我们标注了@ModelAttribute的方法,主要是为我们的目标方法预加载
    modelFactory.initModel(webRequest, mavContainer, invocableMethod);
    // 重定向的时候，忽略model中的数据 默认false
    mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
    
    ...... // 非关键逻辑，省略

    // 【非常重要】对【请求参数】进行处理，调用【目标HandlerMethod】，并且将返回值封装为一个【ModelAndView对象】
    invocableMethod.invokeAndHandle(webRequest, mavContainer);

    // 对封装的ModelAndView进行处理，主要是判断当前请求是否进行了重定向，如果进行了重定向，
    // 还会判断是否需要将FlashAttributes封装到新的请求中
    return getModelAndView(mavContainer, modelFactory, webRequest);
}
```
调用处理器的handle()方法，然后调用invokeHandlerMethod()方法设置一些常用的配置，
比如：请求参数解析器、返回参数解析器、数据绑定器等，
然后调用ServletInvocableHandlerMethod.invokeAndHandle()方法，【执行处理器方法】。
```text
	public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
        // 【重要】请求参数解析 并 执行请求
		Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
		setResponseStatus(webRequest);

		if (returnValue == null) {
			if (isRequestNotModified(webRequest) || getResponseStatus() != null || mavContainer.isRequestHandled()) {
				disableContentCachingIfNecessary(webRequest);
				mavContainer.setRequestHandled(true);
				return;
			}
		}
		else if (StringUtils.hasText(getResponseStatusReason())) {
			mavContainer.setRequestHandled(true);
			return;
		}

		mavContainer.setRequestHandled(false);
		Assert.state(this.returnValueHandlers != null, "No return value handlers");
		try {
			this.returnValueHandlers.handleReturnValue(
					returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
		}
		catch (Exception ex) {
			if (logger.isTraceEnabled()) {
				logger.trace(formatErrorForReturnValue(returnValue), ex);
			}
			throw ex;
		}
	}
```

#### 1.3.1 请求参数解析
在执行处理器方法之前，需要先解析参数，得到处理器方法参数列表的参数值。
进入InvocableHandlerMethod.invokeForRequest()方法：
```text
	@Nullable
	public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
        // 【重要】解析参数
		Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
		if (logger.isTraceEnabled()) {
			logger.trace("Arguments: " + Arrays.toString(args));
		}
		// 【重要】执行调用处理器
		return doInvoke(args);
	}
```
参数解析的时候，先获取方法的参数列表，然后遍历这些参数，
在遍历这些参数的时候，需要添加ParameterNameDiscoverer对象，才能得到参数的名称。

然后获取所有的参数解析器，判断是否可以解析当前参数，
以RequestParamMethodArgumentResolver参数解析器为例，就是判断当前参数是否有@RequestParam注解，
如果可以解析，就直接调用resolveArgument()进行解析和数据绑定。

进入InvocableHandlerMethod.getMethodArgumentValues()方法：
```text
	protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
        // 获取目标方法参数的描述数组对象
		MethodParameter[] parameters = getMethodParameters();
		if (ObjectUtils.isEmpty(parameters)) {
			return EMPTY_ARGS;
		}
        // 用来初始化我们对应参数名称的参数值得数组
		Object[] args = new Object[parameters.length];
		// 循环我们的参数名数组
		for (int i = 0; i < parameters.length; i++) {
			MethodParameter parameter = parameters[i];
			// 为我们的MethodParameter设置参数名称探测器对象
			parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
			args[i] = findProvidedArgument(parameter, providedArgs);
			if (args[i] != null) {
				continue;
			}
			// 【重要】获取所有的参数解析器，然后筛选出合适的解析器
			if (!this.resolvers.supportsParameter(parameter)) {
				throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
			}
			try {
				// 【重要】通过上面筛选的 参数解析器来解析我们的参数
				args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
			}
			catch (Exception ex) {
				// Leave stack trace for later, exception may actually be resolved and handled...
				if (logger.isDebugEnabled()) {
					String exMsg = ex.getMessage();
					if (exMsg != null && !exMsg.contains(parameter.getExecutable().toGenericString())) {
						logger.debug(formatArgumentError(parameter, exMsg));
					}
				}
				throw ex;
			}
		}
		return args;
	}
```

#### 1.3.2 执行处理器方法

解析完参数之后，利用【反射】，调用处理器的方法。
进入InvocableHandlerMethod.invokeForRequest()方法：
```text
public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
        Object... providedArgs) throws Exception {
    // 【重要】获取我们目标方法入参的值
    Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
    if (logger.isTraceEnabled()) {
        logger.trace("Arguments: " + Arrays.toString(args));
    }
    // 【非常重要】调用我们的目标方法
    return doInvoke(args);
}
```
进入doInvoke()方法：
```text
	@Nullable
	protected Object doInvoke(Object... args) throws Exception {
	    // 获取目标方法
		Method method = getBridgedMethod();
		try {
			if (KotlinDetector.isSuspendingFunction(method)) {
				return CoroutinesUtils.invokeSuspendingFunction(method, getBean(), args);
			}
			// 【非常重要】利用反射，执行getBean()获得的对象的method方法
			return method.invoke(getBean(), args);
		}
		catch (IllegalArgumentException ex) {
			assertTargetBean(method, getBean(), args);
			String text = (ex.getMessage() != null ? ex.getMessage() : "Illegal argument");
			throw new IllegalStateException(formatInvokeError(text, args), ex);
		}
		catch (InvocationTargetException ex) {
			// Unwrap for HandlerExceptionResolvers ...
			Throwable targetException = ex.getTargetException();
			if (targetException instanceof RuntimeException) {
				throw (RuntimeException) targetException;
			}
			else if (targetException instanceof Error) {
				throw (Error) targetException;
			}
			else if (targetException instanceof Exception) {
				throw (Exception) targetException;
			}
			else {
				throw new IllegalStateException(formatInvokeError("Invocation failure", args), targetException);
			}
		}
	}
```

#### 1.3.3 返回参数解析
执行完处理器的方法之后，会得到返回值，返回值可能是Json，也可以能是ModelAndView，把返回值封装在ModelAndViewContainer中
```text
public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,
        Object... providedArgs) throws Exception {

    // 【很重要】真正的调用我们的目标对象
    Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
    
    ......
    
    // 遍历当前容器中所有ReturnValueHandler，判断哪种handler支持当前返回值的处理，
    // 如果支持，则使用该handler处理该返回值
    this.returnValueHandlers.handleReturnValue(
        returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
}
```

最后，调用getModelAndView()处理之前的ModelAndViewContainer，封装成一个ModelAndView对象
```text
// 对封装的ModelAndView进行处理，主要是判断当前请求是否进行了重定向，如果进行了重定向，
// 还会判断是否需要将FlashAttributes封装到新的请求中
return getModelAndView(mavContainer, modelFactory, webRequest);
```

### 1.4 视图解析
执行完处理器方法之后，接着是去执行所有拦截器的postHandle()方法：
```text
    void applyPostHandle(HttpServletRequest request, HttpServletResponse response, @Nullable ModelAndView mv)
    throws Exception {
    
        for (int i = this.interceptorList.size() - 1; i >= 0; i--) {
            HandlerInterceptor interceptor = this.interceptorList.get(i);
            interceptor.postHandle(request, response, this.handler, mv);
        }
    }
```

最后才是去渲染视图，在render()方法中，调用resolveViewName()方法去解析视图，然后返回一个View对象，最后调用View的render()方法去渲染视图
```text
    // 渲染视图
    processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    
    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
            @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
            @Nullable Exception exception) throws Exception {
        if (mv != null && !mv.wasCleared()) {
            // 解析、渲染视图
            render(mv, request, response);
            if (errorView) {
                WebUtils.clearErrorRequestAttributes(request);
            }
        }
    }
    
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Determine locale for request and apply it to the response.
        Locale locale =
            (this.localeResolver != null ? this.localeResolver.resolveLocale(request) : request.getLocale());
        response.setLocale(locale);
        View view;
        String viewName = mv.getViewName();
        if (viewName != null) {
            // 解析视图名
            view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
        }
        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().value());
        }
        view.render(mv.getModelInternal(), request, response);
    }
```

