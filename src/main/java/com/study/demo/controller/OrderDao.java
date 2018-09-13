package com.study.demo.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public OrderModel findById() {
		String sql = "select * from tbl_order where order_id = 1";
		return jdbcTemplate.queryForObject(sql, new RowMapper<OrderModel>() {
			public OrderModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				OrderModel payment = new OrderModel();
				payment.setOrderId(rs.getInt("order_id"));
				payment.setBrandId(rs.getInt("brand_id"));
				return payment;
			}
		});
	}
}
