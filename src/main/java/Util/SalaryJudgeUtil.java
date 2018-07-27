/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : SalaryJudgeUtil.java
 * CreateTime: 2018/07/22 18:43:55
 * LastModifiedDate : 18-7-22 下午6:31
 */

package Util;

import java.math.BigDecimal;

public class SalaryJudgeUtil
{
    //每日薪资: mrxz 0: 不限; 1: 500以下; 2: 500-1000; 3: 1001-1500; 4: 1501-2000; 5: 2001-2500; 6: 2500以上
    private static int getSalaryRange(BigDecimal salary, Long priceUnit)
    {
        double temp = salary.doubleValue();

        //if price unit is "month", divide 21.75 to get its salary/day
        if (priceUnit == 1)
        {
            temp /= 21.75;
        }

        switch (1)
        {
            case 1:
                if (temp <= 500)
                    return 1;
            case 2:
                if (temp <=1000)
                    return 2;
            case 3:
                if (temp <= 1500)
                    return 3;
            case 4:
                if (temp <= 2000)
                    return 4;
            case 5:
                if (temp <= 2500)
                    return 5;
            case 6:
                    return 6;
        }
        return 0;
    }

    public static boolean VerifySalary(BigDecimal salary, Long priceUnit, String str_range)
    {
        if (salary == null | priceUnit == null | str_range == null)
            return false;

        Integer range = Integer.parseInt(str_range);

        if (range == 0)
            return true;

        return getSalaryRange(salary, priceUnit) == range;
    }
}
