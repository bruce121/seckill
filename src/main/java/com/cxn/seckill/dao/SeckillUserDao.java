package com.cxn.seckill.dao;

import com.cxn.seckill.model.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 15:23
 * @Version v1.0
 */
@Mapper
public interface SeckillUserDao {

    @Select("select * from seckill_user where id = #{id}")
    public SeckillUser getById(@Param("id") long id);

    @Update("update seckill_user set password = #{password} where id = #{id}")
    void update(SeckillUser toBeUpdate);
}
