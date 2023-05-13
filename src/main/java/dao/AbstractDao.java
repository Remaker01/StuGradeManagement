package dao;

import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

public abstract class AbstractDao {
    protected final JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    public abstract void add(Object obj); //增
    public abstract void delete(int id); //删
    public abstract void update(Object obj); //改
}
