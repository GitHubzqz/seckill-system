package com.java.cn.coding.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.java.cn.coding.entity.Seckill;

public interface SeckillDao {
	
	/**
     * �����
     * @param seckillId
     * @param killTime
     * @return ���Ӱ������>1����ʾ���¿��ļ�¼����
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * ����id��ѯ��ɱ����Ʒ��Ϣ
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

	/**
	 * ��ѯ��ɱ��Ʒ�б�
	 * @return
	 */
	//@Select("select * from seckill")
	//public List<Map<String, Object>> queryAll();
	
    
	/**
     * ����ƫ������ѯ��ɱ��Ʒ�б�
     * @param offset
     * @param limit
     * @return
     */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);
}
