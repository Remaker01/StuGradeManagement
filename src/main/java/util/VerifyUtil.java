package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;

public class VerifyUtil {
    public static final int PASS_MIN_LEN = 7,PASS_MAX_LEN = 30;
    public static final String PASSWORD_REQUIREMENT = String.format("密码长度不能小于%d位或大于%d位,且至少含3个不同字符",PASS_MIN_LEN,PASS_MAX_LEN);
    public static boolean verifyTimestamp(String timestamp,Class<?> clazz) {
        long cur = System.currentTimeMillis();
        try {
            long tm = Long.parseLong(timestamp);
            cur /= 1000L;
            LogUtil.log(Level.INFO,String.format("%s:cur=%d,tp=%d",clazz.getName(),cur,tm));
            return (cur >= tm-20L&&cur - tm <= 120L); //防止客户端时间偏快
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean verifyPassword(String password) {
        int len = password.length();
        if(len < PASS_MIN_LEN||len > PASS_MAX_LEN)
            return false;
        char[] chars = password.toCharArray();
        Arrays.sort(chars);
        int different = 1;
        for (int i = 1; i < len; i++) {
            if (chars[i] != chars[i-1]) {
                different++;
                if (different >= 3)
                    return true;
            }
        }
        return false;
    }
}