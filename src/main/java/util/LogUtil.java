package util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

class LogFormatter extends Formatter {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
    @Override
    public String format(LogRecord record) {
        //创建StringBuilder对象来存放后续需要打印的日志内容
        StringBuilder builder = new StringBuilder();
        //获取时间
        String dateStr = sdf.format(new Date());

        builder.append(dateStr);
        builder.append(" - ");
        //拼接日志级别
        builder.append(record.getLevel()).append(" - ");
        //拼接方法名
        builder.append(record.getSourceMethodName()).append(" - ");
        //拼接日志内容
        builder.append(record.getMessage().trim());
        //日志换行
        builder.append('\n');

        return builder.toString();
    }
}
/**
 * 日志记录器类
 */
public class LogUtil {
    private static Logger logger = Logger.getLogger("StuGradeManagement");
    static {
        try {
            setFile("stu.log");
        } catch (IOException e) {
            System.err.println("日志文件初始化失败~日志不能记录到文件");
            e.printStackTrace();
        }
    }

    public static void log(Level level,String msg) {logger.log(level,msg);}
    public static void log(Throwable e) {
        StackTraceElement[] traceElements = e.getStackTrace();
        StringBuffer message = new StringBuffer(e.getClass().getName() + ':');
        message.append(e.getMessage()).append('\n');
        boolean flag = false; //指示上一步是否为自带方法
        for(StackTraceElement trace:traceElements) {
            //注意排除java自带方法
            String className = trace.getClassName();
            if(className.startsWith("java.")||className.startsWith("sun.")||className.startsWith("javax.")) {
                message.append("...");
                flag = true;
            }
            else {
                if (flag) {
                    message.append('\n');
                    flag = false;
                }
                message.append(trace).append('\n');
            }
        }
        logger.log(Level.SEVERE,message.toString());
    }
    public static void setFile(String file) throws IOException {
        FileHandler fHandler = new FileHandler(file,true);
        fHandler.setFormatter(new LogFormatter());
        logger.addHandler(fHandler);
    }
    /**设置全局日志等级*/
    public static void setLevel(Level level) {logger.setLevel(level);}
    /**获取全局日志等级*/
    public static Level getLevel() {return logger.getLevel();}
    @Override
    protected void finalize() throws Throwable {
        Handler[] handlers = logger.getHandlers();
        for(Handler h:handlers) {
            if(h instanceof FileHandler) {
                h.close();
            }
        }
        super.finalize();
    }
}
