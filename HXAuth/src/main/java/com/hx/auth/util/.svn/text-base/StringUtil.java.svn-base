package com.hx.auth.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Renyulin on 14-4-9 下午2:42.
 */
public class StringUtil {
    public static SimpleDateFormat yyyyMMddHHmmssSSSFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static String getDateTimeRadomStr(int length) {
        return yyyyMMddHHmmssSSSFormat.format(new Date())+getRadomStr(length);
    }
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String getRadomStr(int length){
        StringBuilder result = new StringBuilder("");
        char[] allChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'X'};
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(allChars.length);
            result.append(allChars[index]);
        }
        return result.toString();
    }

    /**
     * 将int转换为指定长度的字符串，前缀以0补位
     *
     * @param intValue
     * @param strLength
     * @return
     */
    public static String int2Str(int intValue, int strLength) {
        if (strLength > 0) {
            char padding = '0';
            StringBuilder result = new StringBuilder();
            String fromStr = String.valueOf(intValue);
            for (int i = fromStr.length(); i < strLength; i++) {
                result.append(padding);
            }
            result.append(fromStr);
            return result.toString();
        } else {
            return null;
        }
    }
}
