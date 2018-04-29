package com.cxn.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 14:18
 * @Version v1.0
 */
public class MD5Util {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1q2w3e4r";

    public static String inputPassFormPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(6);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt){
        String str = ""+ salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(6);
        return md5(str);
    }

    public static String inputPassToDBPass(String input, String saltDB){
        String formPass = inputPassFormPass(input);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        String result = inputPassFormPass("123456");
        System.out.println(result);

        String result2 = formPassToDBPass(inputPassFormPass("123456"), "randSalt");
        System.out.println(result2);

        String result3 = inputPassToDBPass("123456", "randSalt");
        System.out.println(result3);

    }
}
