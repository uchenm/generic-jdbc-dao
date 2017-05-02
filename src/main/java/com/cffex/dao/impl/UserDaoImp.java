package com.cffex.dao.impl;

import com.cffex.dao.UserDao;
import com.cffex.entity.User;

import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming on 2017/4/21.
 */
public class UserDaoImp extends BaseDaoImp<User> implements UserDao {

    private Class<?> EntityClass;

    private String sql;

    private PreparedStatement statement;

    private ResultSet rs;

    private List<User> list;

    public UserDaoImp() {

        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        EntityClass = (Class<?>) type.getActualTypeArguments()[0];
    }

    public List<User> findAll() {
        // TODO Auto-generated method stub
        StringBuffer b = new StringBuffer();
        list = new ArrayList<User>();
        sql = b.append("select * from " + EntityClass.getSimpleName())
            .toString();
        try {
            statement = JdbcDaoHelper.getPreparedStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setGrade(rs.getInt("grade"));
                list.add(user);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

}
