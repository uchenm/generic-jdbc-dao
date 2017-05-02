package com.cffex.dao;

import com.cffex.entity.User;

import java.util.List;

/**
 * Created by Ming on 2017/4/21.
 */
public interface UserDao extends BaseDao<User> {

    //按照行查询,额外的业务逻辑.
    List<User> findAll();
}
