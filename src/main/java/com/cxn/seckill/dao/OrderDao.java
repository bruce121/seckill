package com.cxn.seckill.dao;

import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillOrder;
import org.apache.ibatis.annotations.*;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 17:58
 * @Version v1.0
 */
@Mapper
public interface OrderDao {

    @Select("select * from seckill_order where user_id = #{userId} and goods_id = #{goodsId}")
    SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userId") Long userId,@Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into seckill_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    long insertSeckillOrder(SeckillOrder seckillOrder);


    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(long orderId);
}
