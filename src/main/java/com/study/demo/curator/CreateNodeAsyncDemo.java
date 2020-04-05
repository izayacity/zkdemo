package com.study.demo.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.study.demo.envConfig.EnvConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
* 
* @Description: curator异步创建临时节点
* @author leeSmall
* @date 2018年9月2日
*
*/
public class CreateNodeAsyncDemo {

	static CountDownLatch countDownLatch = new CountDownLatch(2);
	static ExecutorService executorService = Executors.newFixedThreadPool(2);

	public static void main(String[] args) throws Exception {
		EnvConfig envConfig = new EnvConfig();
		String path = "/zk-curator";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(envConfig.get(EnvConfig.Key.ZK_URL))
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		
		//创建临时节点
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
			//回调事件处理
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("event code: " + event.getResultCode() + ", type: " + event.getType());
				countDownLatch.countDown();
			}
		}, executorService).forPath(path, "test".getBytes());

		//创建临时节点
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("event code: " + event.getResultCode() + ", type: " + event.getType());
				countDownLatch.countDown();
			}
		}).forPath(path, "test".getBytes());

		countDownLatch.await();
		executorService.shutdown();
	}
}
