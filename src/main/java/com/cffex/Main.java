package com.cffex;

import com.cffex.dao.impl.UserDaoImp;
import com.cffex.entity.User;

import java.util.List;

/**
 * Created by Ming on 2017/4/21.
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        List<User> list = null;
        UserDaoImp imp = new UserDaoImp();
        list = imp.findAll();
        for(User user:list){
            System.out.println(user.getId()+" "+user.getUsername()+" "+user.getPassword()+" "+user.getEmail()+" "+user.getGrade());
        }
        //insert操作.
        User user = new User();
        user.setId(1);
        user.setUsername("代码如风");
        user.setPassword("123456");
        user.setEmail("123");
        user.setGrade(5);
        imp.add(user);
        //update操作.
        User user_1 = new User();
        user.setId(1);
        user.setUsername("心静如水");
        user.setPassword("123456");
        user.setEmail("123");
        user.setGrade(5);
        imp.update(user_1);
    }

}
