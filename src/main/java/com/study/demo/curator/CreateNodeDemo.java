package com.study.demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
* 
* @Description: curator递归创建顺序节点
* @author leeSmall
* @date 2018年9月2日
*
*/
public class CreateNodeDemo {
	public static void main(String[] args) throws Exception {
		String path = "/zk-curator/c1";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.152.130:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, "test".getBytes());
	}
}
