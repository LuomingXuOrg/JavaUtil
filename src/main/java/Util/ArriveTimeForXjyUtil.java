/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : ArriveTimeForXjyUtil.java
 * CreateTime: 2018/07/22 18:43:55
 * LastModifiedDate : 18-7-22 下午6:38
 */

package Util;

import java.math.BigDecimal;
import java.util.*;

public class ArriveTimeForXjyUtil
{
    private final static Date today = new Date(System.currentTimeMillis());

    /**
     * 0为到岗时限, 单位为"天"; 1为range
     *
     * @param date
     * @return
     */
    private static Map<String, Object> getArriveTimeRange(Date date)
    {
        Map<String, Object> lists = new HashMap<>();

        long start = today.getTime();
        long finish = date.getTime();

        //计算二者之间的市场, 有几天, 精确到小数点后一位
        BigDecimal temp = BigDecimal.valueOf(((double)finish - start) / (1000 * 60 * 60 * 24)).setScale(1,BigDecimal.ROUND_HALF_UP);
        double days = temp.doubleValue();
        lists.put("days", days);

        switch (0)
        {
            case 0:
                if (days <= 0)
                {
                    lists.put("range", -1);
                    return lists;
                }
            case 1:
                if (days <= 7)
                {
                    lists.put("range", 1);
                    return lists;
                }
            case 2:
                if (days <= 14)
                {
                    lists.put("range", 2);
                    return lists;
                }
            case 3:
                lists.put("range", 3);
                return lists;
        }

        return lists;
    }

    /**
     * 0为boolean, 1为天数
     *
     * @param date
     * @param str_range
     * @return
     */
    public static Map<Integer, Object> VerifyArriveTime(Date date, String str_range)
    {
        Map<Integer, Object> lists = new HashMap<>();

        //if has null return false
        if (date == null | str_range == null)
        {
            lists.put(0, false);
            return lists;
        }

        Integer range = Integer.parseInt(str_range);
        Map<String, Object> temp = getArriveTimeRange(date);

        lists.put(1, temp.get("days"));

        if (range == 0)
        {
            lists.put(0, true);
            return lists;
        }

        lists.put(0, temp.get("range").equals(range));

        return lists;
    }

}
