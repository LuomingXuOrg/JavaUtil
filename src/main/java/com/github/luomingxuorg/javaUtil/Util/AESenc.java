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
 *  File Name : AESenc.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Util;

import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class AESenc
{
    private static final String ALGO = "AES";
    private static byte[] keyValue;

    static
    {
        try
        {
            String str;
            ClassPathResource resource = new ClassPathResource("aes.txt");
            resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            while ((str = reader.readLine()) != null)
            {
                sb.append(str);
            }
            keyValue = sb.toString().getBytes(StandardCharsets.UTF_8);
            if (keyValue.length % 16 != 0)
            {
                System.out.println(StrInColor.red("AES key is't divisible by 16. Set default key"));
                keyValue = new byte[]{'Q', 't', 'o', 'm',
                                      '5', 'n', 'g', '7',
                                      'u', 't', 'e', 'g',
                                      'i', 't', 'p', 'R'};
            }
        }
        catch(Exception e)
        {
            keyValue = new byte[]{'Q', 't', 'o', 'm',
                                  '5', 'n', 'g', '7',
                                  'u', 't', 'e', 'g',
                                  'i', 't', 'p', 'R'};
        }
    }

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data)
    {
        try
        {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encVal);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData)
    {
        try
        {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            return new String(decValue);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey()
    {
        return new SecretKeySpec(keyValue, ALGO);
    }

}