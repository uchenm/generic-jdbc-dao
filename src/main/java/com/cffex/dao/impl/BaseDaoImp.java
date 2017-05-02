package com.cffex.dao.impl;

import com.cffex.dao.BaseDao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Ming on 2017/4/21.
 */
public class BaseDaoImp<T> implements BaseDao<T> {

    /** 操作常量 */
    public static final String SQL_INSERT = "insert";
    public static final String SQL_UPDATE = "update";
    public static final String SQL_DELETE = "delete";
    public static final String SQL_SELECT = "select";

    private Class<T> EntityClass; // 获取实体类

    private PreparedStatement statement;

    private String sql;

    private Object argType[];

    private ResultSet rs;

    @SuppressWarnings("unchecked")
    public BaseDaoImp() {

        /**
         *  传递User就是 com.example.daoimp.BaseDaoImp<com.example.bean.User>
         *  传递Shop就是 com.example.daoimp.BaseDaoImp<com.example.bean.Shop>
         * */
        ParameterizedType type = (ParameterizedType) getClass()
            .getGenericSuperclass();

        /**
         * 这里如果传递的是User.那么就是class com.example.bean.User
         * 如果传递的是Shop.       那么就是class com.example.bean.Shop
         * */
        EntityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    public void add(T t) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_INSERT);   //获取sql.
        // 赋值.
        try {
            argType = setArgs(t, SQL_INSERT);
            statement = JdbcDaoHelper.getPreparedStatement(sql);  //实例化PreparedStatement.
            //为sql语句赋值.
            statement = JdbcDaoHelper.setPreparedStatementParam(statement,
                argType);
            statement.executeUpdate(); //执行语句.
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            JdbcDaoHelper.release(statement, null);  //释放资源.
        }
    }

    public void delete(T t) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_DELETE);
        try {
            argType = this.setArgs(t, SQL_DELETE);
            statement = JdbcDaoHelper.getPreparedStatement(sql);
            statement = JdbcDaoHelper.setPreparedStatementParam(statement,
                argType);
            statement.executeUpdate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            JdbcDaoHelper.release(statement, null);
        }
    }

    public void update(T t) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_UPDATE);
        try {
            argType = setArgs(t, SQL_UPDATE);
            statement = JdbcDaoHelper.getPreparedStatement(sql);
            statement = JdbcDaoHelper.setPreparedStatementParam(statement,
                argType);
            statement.executeUpdate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            JdbcDaoHelper.release(statement, null);
        }
    }

    public T select(T t) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_SELECT);
        T obj = null;
        try {
            argType = setArgs(t, SQL_SELECT);
            statement = JdbcDaoHelper.getPreparedStatement(sql);
            statement = JdbcDaoHelper.setPreparedStatementParam(statement,
                argType);
            rs = statement.executeQuery();
            Field fields[] = EntityClass.getDeclaredFields();
            while (rs.next()) {
                obj = EntityClass.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(obj, rs.getObject(fields[i].getName()));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;

    }

    // sql拼接函数 形如 : insert into User(id,username,password,email,grade) values(?,?,?,?,?)
    private String getSql(String operator) {

        StringBuffer sql = new StringBuffer();
        // 通过反射获取实体类中的所有变量
        Field fields[] = EntityClass.getDeclaredFields();

        // 插入操作
        if (operator.equals(SQL_INSERT)) {
            sql.append("insert into " + EntityClass.getSimpleName());
            sql.append("(");
            for (int i = 0; fields != null && i < fields.length; i++) {
                fields[i].setAccessible(true);    //这句话必须要有,否则会抛出异常.
                String column = fields[i].getName();
                sql.append(column).append(",");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(") values (");
            for (int i = 0; fields != null && i < fields.length; i++) {
                sql.append("?,");
            }
            sql.deleteCharAt(sql.length() - 1);
            // 是否需要添加分号
            sql.append(")");
        } else if (operator.equals(SQL_UPDATE)) {
            sql.append("update " + EntityClass.getSimpleName() + " set ");
            for (int i = 0; fields != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                String column = fields[i].getName();
                if (column.equals("id")) {
                    continue;
                }
                sql.append(column).append("=").append("?,");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" where id=?");
        } else if (operator.equals(SQL_DELETE)) {
            sql.append("delete from " + EntityClass.getSimpleName()
                + " where id=?");
        } else if (operator.equals(SQL_SELECT)) {
            sql.append("select * from " + EntityClass.getSimpleName()
                + " where id=?");
        }
        return sql.toString();
    }

    // 获取参数.
    private Object[] setArgs(T entity, String operator)
        throws IllegalArgumentException, IllegalAccessException {

        Field fields[] = EntityClass.getDeclaredFields();
        if (operator.equals(SQL_INSERT)) {

            Object obj[] = new Object[fields.length];
            for (int i = 0; obj != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                obj[i] = fields[i].get(entity);
            }
            return obj;

        } else if (operator.equals(SQL_UPDATE)) {

            Object Tempobj[] = new Object[fields.length];
            for (int i = 0; Tempobj != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                Tempobj[i] = fields[i].get(entity);
            }

            Object obj[] = new Object[fields.length];
            System.arraycopy(Tempobj, 1, obj, 0, Tempobj.length - 1);
            obj[obj.length - 1] = Tempobj[0];
            return obj;

        } else if (operator.equals(SQL_DELETE)) {

            Object obj[] = new Object[1];
            fields[0].setAccessible(true);
            obj[0] = fields[0].get(entity);
            return obj;
        } else if (operator.equals(SQL_SELECT)) {

            Object obj[] = new Object[1];
            fields[0].setAccessible(true);
            obj[0] = fields[0].get(entity);
            return obj;
        }
        return null;
    }

}
