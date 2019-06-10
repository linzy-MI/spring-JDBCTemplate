package com.spring.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.tree.RowMapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

class JDBCTest {
    private ApplicationContext ctx=null;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    {
    	ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
    	jdbcTemplate=(JdbcTemplate)ctx.getBean("jdbcTemplate");
    	namedParameterJdbcTemplate=(NamedParameterJdbcTemplate)ctx.getBean("NamedParameterJdbcTemplate");
    }
	
    public void getNowDbName(){
    	  String sql = "Select Name From employees Where DbId=(Select Dbid From Master..SysProcesses Where Spid = @@spid)";
    	  System.out.println(jdbcTemplate.queryForObject(sql, String.class));
    	 }

    
	@Test
	public void testDataSource() throws SQLException {
		DataSource dataSource=(DataSource)ctx.getBean("dataSource");
		System.out.println(dataSource.getConnection());
	}
	
	 /**
     * 执行批量更新：批量的insert、update、delete
     * 最后一个参数是Object[]类型
     */
	@Test
    public void testBatchUpdate() {
    	String sql="INSERT INTO employees(LAST_NAME,EMAIL,DEPT_ID)values(?,?,?)";
    	List<Object[]> batchArgs=new ArrayList<>();
    	batchArgs.add(new Object[] {"AA","AA@136.com",1});
    	batchArgs.add(new Object[] {"BB","BB@126.com",2});
    	batchArgs.add(new Object[] {"CC","CC@qq.com",1});
    	batchArgs.add(new Object[] {"DD","DD@136.com",4});
    	batchArgs.add(new Object[] {"EE","EE@.com",3});
    	batchArgs.add(new Object[] {"FF","FF@136.com",2});
    	jdbcTemplate.batchUpdate(sql,batchArgs);
    }
    
    /**
     * 执行insert,update,delete
     * @throws SQLException
     */
	@Test
    public void testUpdate()
    {
    	
    	String sql="UPDATE employees SET LAST_NAME=? WHERE id=?";
    	jdbcTemplate.update(sql,"Jimmy",5);
    }
    
//	/**
//	 * 从数据库中获取一条记录，实际得到对应的一个对象
//	 * 不支持级联属性
//	 */
	@Test
	public void testQueryForObject()
	{
		String sql="select id,last_name,email from employees where id=?";
		BeanPropertyRowMapper<Employee> rowMapper=new BeanPropertyRowMapper<>(Employee.class);
		Employee employee=jdbcTemplate.queryForObject(sql, rowMapper,1);
		System.out.println(employee);
		
	}
	
	/**
	 * 查到实体类的集合
	 */
	@Test
	public void testQueryForList() {
		String sql="select id,last_name,email ,dept_id from employees where id>?";
		BeanPropertyRowMapper<Employee> rowMapper=new BeanPropertyRowMapper<>(Employee.class);
		List<Employee> employees=jdbcTemplate.query(sql, rowMapper,3);
		System.out.println(employees);
	}
	
    /**
     * 可以为参数起名字
     */
    @Test
    public void testNamedParameterJdbcTemplate()
    {
    	String sql="insert into employees (last_name,email,dept_id) values(:ln,:email,:dept_id)";
    	Map<String,Object> paramMap=new HashMap<>();
    	paramMap.put("ln","TT");
    	paramMap.put("email", "TT@136.com");
    	paramMap.put("dept_id", "4");
        namedParameterJdbcTemplate.update(sql, paramMap);
    }
	
    /**
     * 使用具名参数可以使用update(sql, paramSource)方法
     * sql语句中的参数名和类的属性一致
     * 可以使用BeanPropertySqlParameterSource的实现类作为参数
     */
    @Test
    public void testNamedParameterJdbcTemplate2()
    {
    	String sql="insert into employees (last_name,email,dept_id) +"
    			+ "values(:ln,:email,:dept_id)";
    	Employee employee=new Employee();
    	employee.setLastName("XYZ");
    	employee.setEmail("xyz@sina.com");
    	employee.setLastName("Lin");
    	
    	SqlParameterSource paramSource=new BeanPropertySqlParameterSource(employee);
    	namedParameterJdbcTemplate.update(sql, paramSource);
    	
    }
}
