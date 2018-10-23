/*
 *  Copyright 2018-2018 LuomingXuOrg
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
 *  File Name : UserDetailsUtil.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Security.Util;

import com.github.luomingxuorg.javaUtil.Security.UserDetailsExtend;

public class UserDetailsUtil
{
    private static Object object = new Object();

    public static <T extends UserDetailsExtend> void setUserDetail(T user)
    {
        object = user;
    }

    public static Object getUserDetail()
    {
        return object;
    }
}
