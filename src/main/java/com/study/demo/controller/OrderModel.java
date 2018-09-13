package com.study.demo.controller;

import java.io.Serializable;

public class OrderModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private int orderId;
	private int brandId;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

}
