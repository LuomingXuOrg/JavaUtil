/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : RequestToJsonUtil.java
 * CreateTime: 2018/07/22 18:43:55
 * LastModifiedDate : 18-7-22 下午6:43
 */

package Util;/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : Util.RequestToJsonUtil.java
 * CreateTime: 2018/07/22 18:31:41
 * LastModifiedDate : 18-7-20 下午12:42
 */

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RequestToJsonUtil
{
    /**
     * get json form http request
     *
     * @param request HttpServletRequest request
     * @return JSONObject data
     */
    public static JSONObject getJSONParam(HttpServletRequest request)
    {
        JSONObject json = null;
        try
        {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = streamReader.readLine()) != null)
            {
                sb.append(line);
            }
            json = JSONObject.parseObject(sb.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return json;
    }

    public static List<String> JsonToListByParam(JSONObject jsonObject, String[] param)
    {
        List<String> lists = new ArrayList<>();
        String temp;

        for (String item : param)
        {
            temp = jsonObject.getString(item);

            if (temp == null || temp.equals(""))
            {
                temp = null;
            }

            lists.add(temp);
        }

        return lists;
    }
}
