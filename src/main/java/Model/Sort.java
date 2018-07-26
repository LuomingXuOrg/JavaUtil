/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : Sort.java
 * CreateTime: 2018/07/26 10:10:22
 * LastModifiedDate : 18-7-26 上午10:10
 */

package Model;

public class Sort
{
    public Sort(){}

    public Sort(Direction direction, String fieldName)
    {
        this.direction = direction;
        this.fieldName = fieldName;
    }

    public enum Direction
    {
        DESC,
        ASC
    }

    private Direction direction=Direction.DESC;

    private String fieldName = "id";

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
