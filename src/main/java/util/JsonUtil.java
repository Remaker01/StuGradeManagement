package util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 写入Json的工具类。<br>
 * User按如下格式组织：<br>
 * {'infoValid':[bool],'userInfo':[User|null]}<br>
 * Student类似。Course和Grade都是直接输出，null时输出new Course()或new Grade()的值
 */
public class JsonUtil {
    private static final JsonFactory FACTORY = new JsonFactory();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {}

    public static void writeUser(User u, OutputStream stream) throws IOException {
        JsonGenerator generator = newGenerator(stream);
        generator.writeBooleanField("infoValid",u != null);
        if(u != null) {
            generator.writeObjectFieldStart("userInfo");
            generator.writeNumberField("id",u.getId());
            generator.writeStringField("username",u.getUsername());
            generator.writeBooleanField("admin",u.isAdmin());
        } else {
            generator.writeNullField("userInfo");
        }
        writeEndAndClose(generator);
    }

    public static void writeStudent(Student student,OutputStream stream) throws IOException {
        JsonGenerator generator = newGenerator(stream);
        generator.writeBooleanField("infoValid",student!=null);
        if(student != null) {
            generator.writeObjectFieldStart("stuInfo");
            generator.writeNumberField("id",student.getId());
            generator.writeStringField("sname",student.getSname());
            generator.writeNumberField("age",student.getAge());
            generator.writeStringField("gender", student.getGender());
            generator.writeStringField("address", student.getAddress());
            generator.writeStringField("phone", student.getPhone());
            generator.writeStringField("qq",student.getQQ());
        } else {
            generator.writeNullField("stuInfo");
        }
        writeEndAndClose(generator);
    }

    public static void writeCourse(Course c,OutputStream stream) throws IOException {
        if(c == null)
            c = new Course();
        MAPPER.writeValue(stream,c);
    }

    public static void writeGrade(Grade g, OutputStream stream) throws IOException {
        if(g == null)
            g = new Grade();
        MAPPER.writeValue(stream,g);
    }

    private static JsonGenerator newGenerator(OutputStream stream) throws IOException {
        JsonGenerator generator = FACTORY.createGenerator(stream);
        generator.writeStartObject();
        return generator;
    }

    private static void writeEndAndClose(JsonGenerator generator) throws IOException {
        if (generator.isClosed())
            return;
        generator.writeEndObject();
        generator.close();
    }
}