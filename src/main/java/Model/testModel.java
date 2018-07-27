/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : testModel.java
 * CreateTime: 2018/07/26 14:46:31
 * LastModifiedDate : 18-7-26 下午2:46
 */

package Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class testModel
{
    private Integer intTemp;
    private Date dateTemp;
    private Long longTemp;
    private Double doubleTemp;
    private testModel testModelTemp;

    public Integer getIntTemp()
    {
        return intTemp;
    }

    public void setIntTemp(Integer intTemp)
    {
        this.intTemp = intTemp;
    }

    public Date getDateTemp()
    {
        return dateTemp;
    }

    public void setDateTemp(Date dateTemp)
    {
        this.dateTemp = dateTemp;
    }

    public Long getLongTemp()
    {
        return longTemp;
    }

    public void setLongTemp(Long longTemp)
    {
        this.longTemp = longTemp;
    }

    public Double getDoubleTemp()
    {
        return doubleTemp;
    }

    public void setDoubleTemp(Double doubleTemp)
    {
        this.doubleTemp = doubleTemp;
    }

    public testModel getTestModelTemp()
    {
        return testModelTemp;
    }

    public void setTestModelTemp(testModel testModelTemp)
    {
        this.testModelTemp = testModelTemp;
    }

    @Override
    public String toString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "testModel{" +
                "intTemp=" + intTemp +
                ", dateTemp=" + sdf.format(dateTemp) +
                ", longTemp=" + longTemp +
                ", doubleTemp=" + doubleTemp +
                '}';
    }
}
