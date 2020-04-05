package com.study.demo.curator;

import com.google.gson.Gson;
import com.study.demo.envConfig.EnvConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
* 
* @Description: curator事件监听
* @author leeSmall
* @date 2018年9月2日
*
*/
public class NodeCacheDemo {

	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		EnvConfig envConfig = new EnvConfig();
		String path = "/zk-curator/nodecache";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(envConfig.get(EnvConfig.Key.ZK_URL))
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "test".getBytes());

		final NodeCache nc = new NodeCache(client, path, false);
		nc.start();
		//通过回调函数监听事件
		nc.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				ChildData currData = nc.getCurrentData();
				System.out.println("update--current data: " + new String(currData.getData()) +
						", stats: " + gson.toJson(currData.getStat()));
			}
		});
		
		client.setData().forPath(path, "test123".getBytes());
		Thread.sleep(1000);
		client.delete().deletingChildrenIfNeeded().forPath(path);
		Thread.sleep(5000);
		nc.close();
	}
}
