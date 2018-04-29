package com.cxn.seckill.service;

import com.cxn.seckill.dao.UserDao;
import com.cxn.seckill.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ListIterator;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 21:35
 * @Version v1.0
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getById(Long id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx(){
        User user1 = new User();
        user1.setId(2L);
        user1.setName("lisi");

        userDao.insert(user1);


        User user2 = new User();
        user2.setId(1L);
        user2.setName("zhangsan");

        userDao.insert(user2);

        return true;
    }

}
