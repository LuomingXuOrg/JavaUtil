/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : ListSortDefault.java
 * CreateTime: 2018/07/26 10:08:22
 * LastModifiedDate : 18-7-26 上午10:07
 */

package Annotation;

import Model.Sort;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListSortDefault
{
    String value() default "id";
    Sort.Direction Direction() default Sort.Direction.ASC;
}

