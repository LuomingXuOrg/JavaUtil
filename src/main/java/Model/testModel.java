/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : testModel.java
 * CreateTime: 2018/07/26 14:46:31
 * LastModifiedDate : 18-7-26 下午2:46
 */

package Model;

public class testModel
{
    private Integer id;
    private Integer number;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    @Override
    public String toString()
    {
        return "testModel{" +
                "id=" + id +
                ", number=" + number +
                '}';
    }
}
