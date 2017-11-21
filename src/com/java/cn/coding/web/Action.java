package com.java.cn.coding.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.cn.coding.dto.Exposer;
import com.java.cn.coding.dto.SeckillExecution;
import com.java.cn.coding.dto.SeckillResult;
import com.java.cn.coding.entity.Seckill;
import com.java.cn.coding.enums.SeckillStatEnum;
import com.java.cn.coding.exception.RepeatKillException;
import com.java.cn.coding.exception.SeckillCloseException;
import com.java.cn.coding.service.impl.ServiceImpl;

@Controller
public class Action {

	@Autowired
	private ServiceImpl serviceImpl;
	
	/**
	 * 商品列表页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String getSeckillList(Model model){
		List<Seckill> list = serviceImpl.getSeckillList();
		model.addAttribute("list", list);
		return "/jsp/list";
	}
	
	public List<Seckill> getSeckillList(){
		return serviceImpl.getSeckillList();
	}
	
	/**
	 * 详情页
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}

		Seckill seckill = serviceImpl.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}

		model.addAttribute("seckill", seckill);

		return "/jsp/detail";
	}
	
	// ajax ,json暴露秒杀接口的方法
		@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.GET, produces = {
				"application/json;charset=UTF-8" })
		@ResponseBody
		public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
			SeckillResult<Exposer> result;
			try {
				Exposer exposer = serviceImpl.exportSeckillUrl(seckillId);
				result = new SeckillResult<Exposer>(true, exposer);
			} catch (Exception e) {
				e.printStackTrace();
				result = new SeckillResult<Exposer>(false, e.getMessage());
			}

			return result;
		}

		@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
				"application/json;charset=UTF-8" })
		@ResponseBody
		public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
				@PathVariable("md5") String md5, @CookieValue(value = "userPhone", required = false) Long userPhone) {
			if (userPhone == null) {
				return new SeckillResult<SeckillExecution>(false, "未注册");
			}
			SeckillResult<SeckillExecution> result;

			try {
				SeckillExecution execution = serviceImpl.executeSeckill(seckillId, userPhone, md5);
				return new SeckillResult<SeckillExecution>(true, execution);
			} catch (RepeatKillException e1) {
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
				return new SeckillResult<SeckillExecution>(true, execution);
			} catch (SeckillCloseException e2) {
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
				return new SeckillResult<SeckillExecution>(true, execution);
			} catch (Exception e) {
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
				return new SeckillResult<SeckillExecution>(true, execution);
			}

		}

		// 获取系统时间
		@RequestMapping(value = "/time/now", method = RequestMethod.GET)
		@ResponseBody
		public SeckillResult<Long> time() {
			Date now = new Date();
			return new SeckillResult<Long>(true, now.getTime());
		}	
		
		public List<Seckill> get(){
			return serviceImpl.getSeckillList();
		}
	
	public static void main(String[] args) {
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		Action action = (Action) beanFactory.getBean("action");
		System.out.println(action.get());
		
	}
}
