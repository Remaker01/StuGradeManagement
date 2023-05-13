package util;

import java.util.logging.Level;

public class VerifyUtil {
    public static final long EXPIRE = 30L;
    public static final int PASS_MIN_LEN = 7,PASS_MAX_LEN = 30;
    public static boolean verifyTimestamp(String timestamp,Class<?> clazz) {
        long cur = System.currentTimeMillis();
        try {
            long tm = Long.parseLong(timestamp);
            cur /= 1000L;
            LogUtil.log(Level.INFO,String.format("%s:cur=%d,tp=%d",clazz.getName(),cur,tm));
            return (cur >= tm&&cur - tm <= EXPIRE);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean verifyPassword(String password) {
        int len = password.length();
        return len >= PASS_MIN_LEN&&len <= PASS_MAX_LEN;
    }
}
