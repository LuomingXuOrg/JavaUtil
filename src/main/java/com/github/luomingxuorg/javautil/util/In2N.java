/*
 *           Copyright 2018-2020 LuomingXu
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
 * File Name : In2N.java
 * Repo: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

/**
 * 一个数c属于(0, 2^n], 计算n
 * 在n为0时需特殊处理
 */
public class In2N
{
    public static Integer calculate(int c)
    {
        c--;
        int n = 0;
        if ((c & 0xffff0000) != 0)
        {
            n += 16;
            c >>= 16;
        }
        if ((c & 0xff00) != 0)
        {
            n += 8;
            c >>= 8;
        }
        if ((c & 0xf0) != 0)
        {
            n += 4;
            c >>= 4;
        }
        if ((c & 0xc) != 0)
        {
            n += 2;
            c >>= 2;
        }
        if ((c & 0x2) != 0)
        {
            n++;
            c >>= 1;
        }
        if (c != 0)
        {
            n++;
        }
        return n;
    }
}
