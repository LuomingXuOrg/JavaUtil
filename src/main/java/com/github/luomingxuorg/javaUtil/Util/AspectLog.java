/*
 *  Copyright 2018-2018 LuomingXuOrg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Author : Luoming Xu
 *  File Name : AspectLog.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Util;

import com.github.luomingxuorg.javaUtil.Entity.MethodCallAspectE;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用方式
 * <pre>
 * <code>@Bean</code>
 * public AspectLog aspectLog()
 * {
 *     return new AspectLog();
 * }
 * </pre>
 */
@Aspect
@Component
public class AspectLog
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ThreadLocal<Long> startTime = new ThreadLocal<>();

    protected Map<String, MethodCallAspectE> mapMethodCall = new HashMap<>();

    @Pointcut("@annotation(com.github.luomingxuorg.javaUtil.Annotation.AspectScopeTrue)")
    private void methodAnnotationScope() {}

    @Before("methodAnnotationScope()")
    protected void before()
    {
        startTime.set(System.currentTimeMillis());
    }

    @Around("methodAnnotationScope()")
    protected Object around(ProceedingJoinPoint point) throws Throwable
    {
        Object result = point.proceed();

        logger.info("--------------------------------------");
        //类名
        String className = point.getSignature().getDeclaringTypeName();
        //是class还是interface
        String classType = point.getSignature().getDeclaringType().toString().split(" ")[0];
        //方法名
        String methodName = point.getSignature().getName();
        //返回类型
        String returnType = point.getSignature().toString().split(" ")[0];
        //参数
        Object[] args = point.getArgs();

        logger.info(String.format("class/interface: %s(%s)", className, classType));
        logger.info(String.format("method: %s", methodName));
        logger.info(String.format("args size: %s", args.length));
        for (Object item : args)
        {
            logger.info(String.format("\ttype: %s\tvalue: %s", item.getClass().getSimpleName(), item));
        }
        logger.info(String.format("return type: %s", returnType));

        //以类名加方法名作为key, 保证唯一
        String mapKey = className + "." + methodName;
        if (mapMethodCall.containsKey(mapKey))
        {
            MethodCallAspectE entity = mapMethodCall.get(mapKey);
            entity.setCallCount(entity.getCallCount() + 1L);
            mapMethodCall.put(mapKey, entity);
        }
        else
        {
            MethodCallAspectE entity = new MethodCallAspectE();
            entity.setCallCount(1L);
            entity.setCallTotalTime(0L);
            mapMethodCall.put(mapKey, entity);
        }

        return result;
    }

    @After("methodAnnotationScope()")
    protected void after(JoinPoint point)
    {
        Long costTime = System.currentTimeMillis() - startTime.get();
        String mapKey = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();

        MethodCallAspectE entity = mapMethodCall.get(mapKey);
        entity.setCallTotalTime(entity.getCallTotalTime() + costTime);
        mapMethodCall.put(mapKey, entity);

        logger.info(String.format("cost %sms", costTime));
        logger.info(mapMethodCall.get(mapKey).toString());

        logger.info("--------------------------------------");
    }
}
