package dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import util.JDBCUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractDao {
    protected final JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    public abstract void add(Object obj); //增
    public abstract void delete(int id); //删
    public abstract int update(Object obj); //改
    /**
     * 分页模糊查询
     * @param sql 基础语句
     * @param condition 条件。按key like %value%组织
     */
    protected <T> List<T> findByPage(String sql, RowMapper<T> rowMapper,int start, int rows, Map<String, String[]> condition) { //查
        StringBuilder sb = new StringBuilder(sql);
        //2.遍历map
        Set<String> keySet = condition.keySet();
        //定义参数的集合
        ArrayList<Object> params = new ArrayList<>(condition.size());
        for (String key : keySet) {
            //排除分页条件参数
            if("currentPage".equals(key) || "rows".equals(key)){
                continue;
            }
            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if(value != null && !"".equals(value)){
                //有值
                sb.append(" and ").append(key).append(" like ? ");
                params.add("%"+value+"%");//？条件的值
            }
        }
        //添加分页查询
        sb.append(" limit ?,? ");
        //添加分页查询参数值
        params.add(start);
        params.add(rows);
        return template.query(sb.toString(), rowMapper,params.toArray());
    }
}
