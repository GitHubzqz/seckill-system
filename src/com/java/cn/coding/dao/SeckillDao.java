package com.java.cn.coding.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.java.cn.coding.entity.Seckill;

public interface SeckillDao {
	
	/**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1，表示更新库存的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀的商品信息
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

	/**
	 * 查询秒杀商品列表
	 * @return
	 */
	//@Select("select * from seckill")
	//public List<Map<String, Object>> queryAll();
	
    
	/**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);
}
