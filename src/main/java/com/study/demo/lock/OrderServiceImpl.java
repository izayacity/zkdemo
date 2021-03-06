package com.study.demo.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* 
* @Description: 在业务里面使用分布式锁
* @author leeSmall
* @date 2018年9月4日
*
*/
public class OrderServiceImpl implements Runnable {

	private static OrderCodeGenerator ong = new OrderCodeGenerator();

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	// 同时并发的线程数
	private static final int NUM = 10;
	// 按照线程数初始化倒计数器,倒计数器
	private static CountDownLatch cdl = new CountDownLatch(NUM);

	private Lock lock = new DistributedLock();

	// 创建订单接口
	public void createOrder() {
		String orderCode = null;

		//准备获取锁
		lock.lock();
		try {
			// 获取订单编号
			orderCode = ong.getOrderCode();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			//完成业务逻辑以后释放锁
			lock.unlock();
		}

		// ……业务代码
		System.out.println("insert into DB使用id：=======================>" + orderCode);
		logger.info("insert into DB使用id：=======================>" + orderCode);
	}

	
	public void run() {
		try {
			// 等待其他线程初始化
			cdl.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建订单
		createOrder();
	}

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(NUM);
		for (int i = 1; i <= NUM; i++) {
			OrderServiceImpl worker = new OrderServiceImpl();
			pool.execute(worker);
			cdl.countDown();
		}
		pool.shutdown();
	}
}
