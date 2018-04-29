package com.cxn.seckill.util;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 15:15
 * @Version v1.0
 */
public class ValidateUtil {

    private static final Pattern mobile_patten = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher matcher = mobile_patten.matcher(src);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("13846702003"));
        System.out.println(isMobile("138467003"));
        System.out.println(isMobile("138aaa702003"));
    }
}
