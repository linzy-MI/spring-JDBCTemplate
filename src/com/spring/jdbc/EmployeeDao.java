package com.spring.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public Employee get(Integer id)
	{
		String sql="select id,last_name,email from employees where id=?";
		BeanPropertyRowMapper<Employee> rowMapper=new BeanPropertyRowMapper<>(Employee.class);
		Employee employee=jdbcTemplate.queryForObject(sql, rowMapper,id);
		return employee;
	}
	

}
