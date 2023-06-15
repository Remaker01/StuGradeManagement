package util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 密码加密工具类。用Hash算法给密码加密
 */
public class EncryptUtil {
    private static MessageDigest digest;
    private EncryptUtil() {}
    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) { //这个正常情况下不可能
            digest = null;
            LogUtil.log(e);
        }
    }
    public static String base64Decode(String base64) {
        base64 = new StringBuilder(base64).reverse().toString();
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            try {
                base64 += '=';
                bytes = Base64.getDecoder().decode(base64);
            } catch (IllegalArgumentException ex) {
                bytes = Base64.getDecoder().decode(base64+'=');
            }
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }
    /**
     * 对指定字节串进行加密
     * @param message 待加密数据
     * @return byte[]:加密后的数据。若发生异常(几乎不可能)，则返回null
     */
    public static byte[] encrypt(byte[] message) {
        try {
            return digest.digest(message);
        } catch (NullPointerException e) {
            LogUtil.log(e);
            return null;
        }
    }
    /**
     * 对以charset编码的字符串进行加密
     * @param message 待加密数据
     * @param charset 编码
     * @return String:加密后的数据，以16进制字符串展示
     */
    public static String encrypt(String message, Charset charset) {
        byte[] bytes = encrypt(message.getBytes(charset));
        return byteArrayToHexString(bytes);
    }

    private static String byteArrayToHexString(byte[] bytes) {
        if (bytes == null)
            return null;
        StringBuilder bf = new StringBuilder(bytes.length);
        for (byte b : bytes) {
            //java.lang.Integer.toHexString() 方法的参数是int(32位)类型，
            //如果输入一个byte(8位)类型的数字，这个方法会把这个数字的高24为也看作有效位，可能会出现错误
            //如果使用& 0XFF操作，可以把高24位置0以避免这样错误
            String temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                bf.append('0');
            }
            bf.append(temp);
        }
        return bf.toString();
    }
}