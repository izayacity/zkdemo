package com.study.demo.curator;

import com.study.demo.envConfig.EnvConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
* 
* @Description: curator删除节点数据
* @author leeSmall
* @date 2018年9月2日
*
*/
public class DeleteNodeDemo {

	public static void main(String[] args) throws Exception {
		EnvConfig envConfig = new EnvConfig();
		String path = "/zk-curator/c1";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(envConfig.get(EnvConfig.Key.ZK_URL))
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "test".getBytes());
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath(path);
		client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
	}
}
