package com.study.demo.election;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
* 
* @Description: 两个tomcat模拟master选举
* @author liguangsheng
* @date 2018年9月6日
*
*/
public class ZkTomcatMaster extends ValveBase {

	private static CuratorFramework client;
	// zk临时节点路径（主master）
	private final static String zkPath = "/Tomcat/ActiveLock";
	//Curator事件监听
	private static TreeCache cache;

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		client = CuratorFrameworkFactory.builder().connectString("192.168.152.130:2181").connectionTimeoutMs(1000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		try {
			createZKNode(zkPath);
		} catch (Exception e1) {
			System.out.println("=========== 抢夺成为master失败，对master进行监控！");
			try {
				addZKNodeListener(zkPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//创建临时节点zkPath = "/Tomcat/ActiveLock"
	private void createZKNode(String path) throws Exception {
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
		System.out.println("=========== 创建成功，节点当选为master");
	}

	//创建临时节点zkPath = "/Tomcat/ActiveLock"失败时对创建成功的节点(主master)进行监听
	private void addZKNodeListener(final String path) throws Exception {
		cache = new TreeCache(client, path);
		cache.start();
		cache.getListenable().addListener(new TreeCacheListener() {
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				if (event.getData() != null && event.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
					System.out.println("=========== master挂了，赶紧抢master权限！");
					createZKNode(path);
				}
			}
		});
		
		System.out.println("=========== 已经对master进行监控");
	}
}
