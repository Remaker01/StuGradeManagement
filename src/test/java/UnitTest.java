import org.junit.Test;
import util.EncryptUtil;
import util.JDBCUtils;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;

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
}