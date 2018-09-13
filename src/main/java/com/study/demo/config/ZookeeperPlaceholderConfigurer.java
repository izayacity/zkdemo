package com.study.demo.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
* 
* @Description: 加载props里面的数据库配置,这个类等价于以前在xml文件里面的配置：
* <context:property-placeholder location="classpath:config/jdbc_conf.properties"/>
* @author leeSmall
* @date 2018年9月10日
*
*/
public class ZookeeperPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private ZookeeperConfigurerCentral zkConfigurerCentral;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		System.out.println(zkConfigurerCentral.getProps());
		super.processProperties(beanFactoryToProcess, zkConfigurerCentral.getProps());
	}

	public void setzkConfigurerCentral(ZookeeperConfigurerCentral zkConfigurerCentral) {
		this.zkConfigurerCentral = zkConfigurerCentral;
	}
}
