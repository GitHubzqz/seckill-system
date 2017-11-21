package com.java.cn.coding.service;

import java.util.List;
import java.util.Map;

import com.java.cn.coding.dto.Exposer;
import com.java.cn.coding.dto.SeckillExecution;
import com.java.cn.coding.entity.Seckill;
import com.java.cn.coding.exception.RepeatKillException;
import com.java.cn.coding.exception.SeckillCloseException;
import com.java.cn.coding.exception.SeckillException;

public interface ServiceInterface {

	//测试
	//public List<Map<String, Object>> getSeckillList();
	
	/**
	 * 查询列表页
	 * @return
	 */
	public List<Seckill> getSeckillList();
	
	/**
     *查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);


    //再往下，是我们最重要的行为的一些接口

    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);


    /**
     * 执行秒杀操作，有可能失败，有可能成功，所以要抛出我们允许的异常
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;
}
