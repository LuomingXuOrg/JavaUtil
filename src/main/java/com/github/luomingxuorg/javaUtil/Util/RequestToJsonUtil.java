/*
 * Copyright 2018-2018 LuomingXuOrg
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
 * limitations under the License.
 *
 * Author : Luoming Xu
 * File Name : RequestToJsonUtil.java
 * Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Util;

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
