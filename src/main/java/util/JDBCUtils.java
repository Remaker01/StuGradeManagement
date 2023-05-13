package util;

import javax.sql.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class JDBCUtils {
    private static DataSource ds;
    static {
        try {
            //1.加载配置文件
            Context initContext = new InitialContext();
            Context context = (Context)initContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("/jdbc/stu");
            LogUtil.log(Level.INFO,"JDBCUtils:数据源加载成功");
        } catch (NamingException|ClassCastException e) {
            ds = null;
            LogUtil.log(e);
        }
    }
    /**
     * 获取连接池对象
     */
    public static DataSource getDataSource(){
        return ds;
    }
    /**
     * 获取连接Connection对象
     */
    public static Connection getConnection() throws SQLException {
        return  ds.getConnection();
    }
}
