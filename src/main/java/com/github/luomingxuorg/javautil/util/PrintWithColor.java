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
 *  File Name : PrintWithColor.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

/**
 * print {@link String} in color
 */
public class PrintWithColor
{
    public static void printBlack(String temp)
    {
        System.out.println(balck(temp));
    }

    public static void printRed(String temp)
    {
        System.out.println(red(temp));
    }

    public static void printGreen(String temp)
    {
        System.out.println(green(temp));
    }

    public static void printYellow(String temp)
    {
        System.out.println(yellow(temp));
    }

    public static void printBlue(String temp)
    {
        System.out.println(blue(temp));
    }

    public static void printPurple(String temp)
    {
        System.out.println(purple(temp));
    }

    public static void printSkyBlue(String temp)
    {
        System.out.println(skyBlue(temp));
    }

    public static void printWhite(String temp)
    {
        System.out.println(white(temp));
    }

    public static String balck(String temp)
    {
        return "\033[30m" + temp + "\033[0m";
    }

    public static String red(String temp)
    {
        return "\033[31m" + temp + "\033[0m";
    }

    public static String green(String temp)
    {
        return "\033[32m" + temp + "\033[0m";
    }

    public static String yellow(String temp)
    {
        return "\033[33m" + temp + "\033[0m";
    }

    public static String blue(String temp)
    {
        return "\033[34m" + temp + "\033[0m";
    }

    public static String purple(String temp)
    {
        return "\033[35m" + temp + "\033[0m";
    }

    public static String skyBlue(String temp)
    {
        return "\033[36m" + temp + "\033[0m";
    }

    public static String white(String temp)
    {
        return "\033[37m" + temp + "\033[0m";
    }
}
