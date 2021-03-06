package com.study.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	@Autowired
	private OrderDao dao;

	public OrderModel getById() {
		return dao.findById();
	}
}
