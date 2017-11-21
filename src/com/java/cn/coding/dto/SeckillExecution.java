package com.java.cn.coding.dto;

import com.java.cn.coding.entity.SuccessKilled;
import com.java.cn.coding.enums.SeckillStatEnum;

public class SeckillExecution {

	   private long seckillId;

	    //��ɱִ�н����״̬
	    private int state;

	    //״̬�����ı�ʶ
	    private String stateInfo;

	    //����ɱ�ɹ�ʱ����Ҫ������ɱ�ɹ��Ķ����ȥ
	    private SuccessKilled successKilled;

	    //��ɱ�ɹ�����������Ϣ
	    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
	        this.seckillId = seckillId;
	        this.state = statEnum.getState();
	        this.stateInfo = statEnum.getInfo();
	        this.successKilled = successKilled;
	    }

	    //��ɱʧ��
	    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
	        this.seckillId = seckillId;
	        this.state = statEnum.getState();
	        this.stateInfo = statEnum.getInfo();
	    }

	    public long getSeckillId() {
	        return seckillId;
	    }

	    public void setSeckillId(long seckillId) {
	        this.seckillId = seckillId;
	    }

	    public int getState() {
	        return state;
	    }

	    public void setState(int state) {
	        this.state = state;
	    }

	    public String getStateInfo() {
	        return stateInfo;
	    }

	    public void setStateInfo(String stateInfo) {
	        this.stateInfo = stateInfo;
	    }

	    public SuccessKilled getSuccessKilled() {
	        return successKilled;
	    }

	    public void setSuccessKilled(SuccessKilled successKilled) {

	        this.successKilled = successKilled;
	    }

	    @Override
	    public String toString() {
	        return "SeckillExecution{" +
	                "seckillId=" + seckillId +
	                ", state=" + state +
	                ", stateInfo='" + stateInfo + '\'' +
	                ", successKilled=" + successKilled +
	                '}';
	    }
}