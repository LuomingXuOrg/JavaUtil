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

import com.github.luomingxuorg.javaUtil.Entity.MethodCallInfo;
import com.github.luomingxuorg.javaUtil.Annotation.EnableAspectScope;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 要是方法被此类发现, 需要在方法前加{@link EnableAspectScope}
 * <br>
 * 使用方式
 * <pre>
 * <code>@Bean</code>
 * public AspectLog aspectLog()
 * {
 *     return new AspectLog();
 * }
 * </pre>
 * 可以继承此类, 来覆写{@link #before}, {@link #around}, {@link #after}
 * <br>
 * 具体如何使用请参照{@link Before}, {@link Around}, {@link After}
 */
@Aspect
public class AspectLog
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private Map<String, MethodCallInfo> mapMethodCall = new HashMap<>();

    /**
     * 利用注解, 来确定哪些方法需要进行调用
     */
    @Pointcut("@annotation(com.github.luomingxuorg.javaUtil.Annotation.EnableAspectScope)")
    private void methodAnnotationScope() {}

    @Before("methodAnnotationScope()")
    protected void before()
    {
        startTime.set(System.currentTimeMillis());
    }

    @Around("methodAnnotationScope()")
    protected Object around(ProceedingJoinPoint point) throws Throwable
    {
        logger.info(StrInColor.yellow("----------------Around----------------"));

        Object result = point.proceed();

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

        logger.info(String.format("%s: %s(%s)", StrInColor.green("class/interface"), className, classType));
        logger.info(String.format("%s: %s", StrInColor.green("method"), methodName));
        logger.info(String.format("%s: %s", StrInColor.green("args size"), args.length));
        for (Object item : args)
        {
            logger.info(String.format("\t%s: %s\t%s: %s", StrInColor.green("type"), item.getClass().getSimpleName(),
                    StrInColor.green("value"), item));
        }
        logger.info(String.format("%s: %s, %s: %s", StrInColor.green("return"), result,
                StrInColor.green("type"), returnType));

        //以类名加方法名作为key, 保证唯一
        String mapKey = className + "." + methodName;
        if (mapMethodCall.containsKey(mapKey))
        {
            MethodCallInfo entity = mapMethodCall.get(mapKey);
            entity.setCallCount(entity.getCallCount() + 1L);
            mapMethodCall.put(mapKey, entity);
        }
        else
        {
            MethodCallInfo entity = new MethodCallInfo();
            entity.setCallCount(1L);
            entity.setCallTotalTime(0L);
            mapMethodCall.put(mapKey, entity);
        }

        return result;
    }

    @After("methodAnnotationScope()")
    protected void after(JoinPoint point)
    {
        logger.info(StrInColor.yellow("----------------After----------------"));

        Long costTime = System.currentTimeMillis() - startTime.get();
        String mapKey = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();

        MethodCallInfo entity = mapMethodCall.get(mapKey);
        entity.setCallTotalTime(entity.getCallTotalTime() + costTime);
        mapMethodCall.put(mapKey, entity);

        logger.info(StrInColor.blue(String.format("cost %sms", costTime)));
        logger.info(StrInColor.blue(mapMethodCall.get(mapKey).toString()));

        logger.info(StrInColor.yellow("------------------------------------------------"));
    }
}
