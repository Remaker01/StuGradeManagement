import org.junit.Test;
import util.*;
import util.cache.Cache;
import util.cache.LRUCache;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class UnitTest {
    @Test
    public void testEncrypt() {
        String compared = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";
        String result = EncryptUtil.encrypt("hello", StandardCharsets.ISO_8859_1);
        System.out.println(result);
        if (!result.equals(compared))
            throw new AssertionError();
    }
    @Test
    public void testLog() {
        DataSource source = JDBCUtils.getDataSource();
        File file = new File("stu.log");
        if (!file.exists())
            throw new AssertionError();
    }
    @Test
    public void testVerifyPass() {
        //用例1：只有1种
        if (!VerifyUtil.verifyPassword("06612c0d9c73d47a7042afd7024d7c82"))
            throw new AssertionError();
        if (VerifyUtil.verifyPassword("06612c0d9c73d47a7042afd7024d7c8。"))
            throw new AssertionError();
        if (VerifyUtil.verifyPassword("21111133"))
            throw new AssertionError();
    }
    @Test
    public void testCacheLRU() {
        LRUCache<Integer,Object> cache = new LRUCache<>(3);
        int[] a = new int[]{7, 0, 1, 2, 0, 3, 0, 4};
        boolean[] miss = new boolean[]{true,true,true,true,false,true,false,true};
        for(int i = 0; i < a.length; i++) {
            boolean result = (cache.get(a[i]) == null);
            if (result != miss[i])
                throw new AssertionError(String.format("error on %d.read %b,expected %b",i,result,miss[i]));
            cache.put(a[i],new Object());
        }
    }
    @Test
    public void testCacheMiss() {
        Cache<Integer,Object> cache = new LRUCache<>();
        Object o = new Object();
        Random r = new Random();
        int missLRU=0;
        for(int i = 0; i < 200; i++) {
            int j = r.nextInt(50);
            if(cache.get(j) == null)
                missLRU++;
            cache.put(j,o);
        }
        System.out.printf("\nmiss_rate=%.3f",missLRU/200.0f);
    }
    @Test
    public void testCacheWriteback() {
        LRUCache<Integer,Object> cache = new LRUCache<>(3);
        Object o1 = new Object();
        Object o2 = new Object();
        for (int i = 1; i <= 3; i++) {
            cache.put(i,o1);cache.put(i,o2);
        }
        Object o3=cache.put(4,o2);
        if (!o3.equals(o2))
            throw new AssertionError();
    }
}