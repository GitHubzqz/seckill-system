package com.java.cn.coding.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.java.cn.coding.dao.SeckillDao;
import com.java.cn.coding.dao.SuccessKilledDao;
import com.java.cn.coding.dto.Exposer;
import com.java.cn.coding.dto.SeckillExecution;
import com.java.cn.coding.entity.Seckill;
import com.java.cn.coding.entity.SuccessKilled;
import com.java.cn.coding.enums.SeckillStatEnum;
import com.java.cn.coding.exception.RepeatKillException;
import com.java.cn.coding.exception.SeckillCloseException;
import com.java.cn.coding.exception.SeckillException;
import com.java.cn.coding.service.ServiceInterface;

@Repository
@Service
public class ServiceImpl implements ServiceInterface {

	// ��־����
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// ����һ�������ַ���(��ɱ�ӿ�)��salt��Ϊ���ұ����û��³����ǵ�md5ֵ��ֵ�������Խ����Խ��
	private final String salt = "shsdssljdd'l.";

	// ע��Service����
	@Autowired // @Resource
	private SeckillDao seckillDao;

	@Autowired // @Resource
	private SuccessKilledDao successKilledDao;

	// @Autowired
	// private RedisDao redisDao;

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	public static void main(String[] args) {
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		ServiceImpl serviceImpl = (ServiceImpl) beanFactory.getBean("serviceImpl");
		System.out.println(serviceImpl.getSeckillList());
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		 //������ɱδ����
		Seckill seckill = seckillDao.queryById(seckillId);
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        //ϵͳ��ǰʱ��
        Date nowTime=new Date();
        if (startTime.getTime()>nowTime.getTime() || endTime.getTime()<nowTime.getTime())
        {
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        //��ɱ������������ɱ��Ʒ��id���ø��ӿڼ��ܵ�md5
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	 //��ɱ�Ƿ�ɹ����ɹ�:����棬������ϸ��ʧ��:�׳��쳣������ع�
    //@Transactional
    /**
     * ʹ��ע��������񷽷����ŵ�:
     * 1.�����ŶӴ��һ��Լ������ȷ��ע���񷽷��ı�̷��
     * 2.��֤���񷽷���ִ��ʱ�価���̣ܶ���Ҫ���������������RPC/HTTP������߰��뵽���񷽷��ⲿ
     * 3.�������еķ�������Ҫ������ֻ��һ���޸Ĳ�����ֻ��������Ҫ�������
     */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if (md5==null||!md5.equals(getMD5(seckillId)))
        {
            throw new SeckillException("seckill data rewrite");//��ɱ���ݱ���д��
        }
        //ִ����ɱ�߼�:�����+���ӹ�����ϸ
        Date nowTime=new Date();

        try{

            //��������˿�棬��ɱ�ɹ�,������ϸ
            int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
            //���Ƿ����ϸ���ظ����룬���û��Ƿ��ظ���ɱ
            if (insertCount<=0)
            {
                throw new RepeatKillException("seckill repeated");
            }else {

                //�����,�ȵ���Ʒ����
                int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
                if (updateCount<=0)
                {
                    //û�и��¿���¼��˵����ɱ���� rollback
                    throw new SeckillCloseException("seckill is closed");
                }else {
                    //��ɱ�ɹ�,�õ��ɹ��������ϸ��¼,�����سɹ���ɱ����Ϣ commit
                    SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }

            }


        }catch (SeckillCloseException e1)
        {
            throw e1;
        }catch (RepeatKillException e2)
        {
            throw e2;
        }catch (Exception e)
        {
            logger.error(e.getMessage(),e);
            //���Ա������쳣ת��Ϊ�������쳣
            throw new SeckillException("seckill inner error :"+e.getMessage());
        }

	}
}
