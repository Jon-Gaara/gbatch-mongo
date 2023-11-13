package com.example.gbatchmongo.configuration;

import com.example.gbatchmongo.common.annotation.BatchJob;
import com.example.gbatchmongo.common.handle.IJobHandler;
import com.example.gbatchmongo.common.handle.impl.MethodJobHandler;
import com.example.gbatchmongo.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class BatchJobExecutor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {
    @Override
    public void destroy() {

    }

    @Override
    public void afterSingletonsInstantiated() {
        initJobHandlerMethodRepository(applicationContext);
    }

    public void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }
        // init job handler from method
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
            Map<Method, BatchJob> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        (MethodIntrospector.MetadataLookup<BatchJob>) method -> AnnotatedElementUtils.findMergedAnnotation(method, BatchJob.class));
            } catch (Throwable ex) {
                log.error("batch-job method-jobhandler resolve error for bean[{}].",beanDefinitionName, ex);
            }
            if (annotatedMethods==null || annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, BatchJob> methodBatchJobEntry : annotatedMethods.entrySet()) {
                Method method = methodBatchJobEntry.getKey();
                BatchJob batchJob = methodBatchJobEntry.getValue();
                if (batchJob == null) {
                    continue;
                }

                String name = batchJob.value();
                if (name.trim().length() == 0) {
                    throw new RuntimeException("batch-job method-jobhandler name invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                }
                if (loadJobHandler(name) != null) {
                    throw new RuntimeException("batch-job jobhandler[" + name + "] naming conflicts.");
                }

                // execute method
                if (!(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(String.class))) {
                    throw new RuntimeException("batch-job method-jobhandler param-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
                            "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                }
                if (!method.getReturnType().isAssignableFrom(ReturnT.class)) {
                    throw new RuntimeException("batch-job method-jobhandler return-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
                            "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                }
                ReflectionUtils.makeAccessible(method);

                // init and destory
                Method initMethod = null;
                Method destroyMethod = null;

                if (batchJob.init().trim().length() > 0) {
                    try {
                        initMethod = bean.getClass().getDeclaredMethod(batchJob.init());
                        ReflectionUtils.makeAccessible(initMethod);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("batch-job method-jobhandler initMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }
                if (batchJob.destroy().trim().length() > 0) {
                    try {
                        destroyMethod = bean.getClass().getDeclaredMethod(batchJob.destroy());
                        ReflectionUtils.makeAccessible(destroyMethod);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("batch-job method-jobhandler destroyMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }

                // registry jobhandler
                registJobHandler(name, new MethodJobHandler(bean, method, initMethod, destroyMethod));
            }
        }

    }

    // ---------------------- job handler repository ----------------------
    private static ConcurrentMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<>();

    public static IJobHandler registJobHandler(String name, MethodJobHandler jobHandler){
        log.info(">>>>>>>>>>> batch-job register jobhandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    public static IJobHandler loadJobHandler(String name){
        return jobHandlerRepository.get(name);
    }


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BatchJobExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
