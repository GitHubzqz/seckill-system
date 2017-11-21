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

	//����
	//public List<Map<String, Object>> getSeckillList();
	
	/**
	 * ��ѯ�б�ҳ
	 * @return
	 */
	public List<Seckill> getSeckillList();
	
	/**
     *��ѯ������ɱ��¼
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);


    //�����£�����������Ҫ����Ϊ��һЩ�ӿ�

    /**
     * ����ɱ����ʱ�����ɱ�ӿڵĵ�ַ���������ϵͳʱ�����ɱʱ��
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);


    /**
     * ִ����ɱ�������п���ʧ�ܣ��п��ܳɹ�������Ҫ�׳�����������쳣
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;
}
