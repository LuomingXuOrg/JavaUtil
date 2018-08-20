/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : Sort.java
 * CreateTime: 2018/07/26 10:10:22
 * LastModifiedDate : 18-7-26 上午10:10
 */

package Model;

/**
 * 排序的定义类
 */
public class Sort
{
    public Sort() {}

    public Sort(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public Sort(String fieldName, Direction direction)
    {
        this.direction = direction;
        this.fieldName = fieldName;
    }

    public enum Direction
    {
        DESC,
        ASC
    }

    /**
     * 排序方式, 默认倒序
     */
    private Direction direction = Direction.DESC;

    //需要进行排序的字段名
    private String fieldName;

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName.equals("") ? null : fieldName;
    }
}
