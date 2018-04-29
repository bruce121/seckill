package com.cxn.seckill.dao;

import com.cxn.seckill.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 21:34
 * @Version v1.0
 */
@Mapper
public interface UserDao {

    @Select("select id,name from user where id = #{id}")
    User getById(Long id);

    @Insert("insert into user(id, name) values (#{id}, #{name})")
    long insert(User user);
}
