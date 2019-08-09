/*
 *  Copyright 2018-2019 LuomingXuOrg
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

package com.github.luomingxuorg.javautil.util;

import com.github.luomingxuorg.javautil.annotation.EnableAspectScope;
import com.github.luomingxuorg.javautil.entity.MethodCallInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

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
@Slf4j
@Aspect
public class AspectLog
{
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private Map<String, MethodCallInfo> mapMethodCall = new HashMap<>();

    /**
     * 利用注解, 来确定哪些方法需要进行调用
     */
    @Pointcut("@annotation(com.github.luomingxuorg.javautil.annotation.EnableAspectScope)")
    private void methodAnnotationScope() {}

    @Before("methodAnnotationScope()")
    protected void before(JoinPoint point)
    {
        String mapKey = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();

        //以类名加方法名作为key, 保证唯一
        if (mapMethodCall.containsKey(mapKey))
        {
            MethodCallInfo entity = mapMethodCall.get(mapKey);
            //在before增加调用计数
            entity.setCallCount(entity.getCallCount() + 1L);
            mapMethodCall.put(mapKey, entity);
        }
        else
        {
            MethodCallInfo entity = new MethodCallInfo();
            //初始化调用计数和总耗时
            entity.setCallCount(1L);
            entity.setCallTotalTime(0L);
            mapMethodCall.put(mapKey, entity);
        }

        startTime.set(System.currentTimeMillis());
    }

    @Around("methodAnnotationScope()")
    protected Object around(ProceedingJoinPoint point) throws Throwable
    {
        log.info(PrintWithColor.yellow("----------------Around----------------"));

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

        log.info(String.format("%s: %s(%s)", PrintWithColor.green("class/interface"), className, classType));
        log.info(String.format("%s: %s", PrintWithColor.green("method"), methodName));
        log.info(String.format("%s: %s", PrintWithColor.green("args size"), args.length));
        for (Object item : args)
        {
            if (item != null)
            {
                log.info(String.format("\t%s: %s\t%s: %s", PrintWithColor.green("type"), item.getClass().getSimpleName(),
                        PrintWithColor.green("value"), item.toString()));
            }
        }
        log.info(String.format("%s: %s, %s: %s", PrintWithColor.green("return"), result,
                PrintWithColor.green("type"), returnType));

        return result;
    }

    @After("methodAnnotationScope()")
    protected void after(JoinPoint point)
    {
        log.info(PrintWithColor.yellow("----------------After----------------"));

        Long costTime = System.currentTimeMillis() - startTime.get();
        String mapKey = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();

        MethodCallInfo entity = mapMethodCall.get(mapKey);
        //在after里面记录调用总耗时
        entity.setCallTotalTime(entity.getCallTotalTime() + costTime);
        mapMethodCall.put(mapKey, entity);

        log.info(PrintWithColor.blue(String.format("cost %sms", costTime)));
        log.info(PrintWithColor.blue(mapMethodCall.get(mapKey).toString()));

        log.info(PrintWithColor.yellow("------------------------------------------------"));
    }
}
