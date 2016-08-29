package com.zhangmagle.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhangmagle.dao.EmployeeDao;
import com.zhangmagle.model.Employee;

/**
 * Providing RESTful API for employee operations, including
 * 1. delete an employee 
 * 2. insert new employee belonging to one department
 * 3. query all employee information
 * 4. query all employee in a department
 * 5. query an employee via employee id
 * 6. update an employee
 * @author my
 *
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	EmployeeDao employeeDao;
	
	/**
	 * Create an employee belonging to a department
	 * Only support HTTP POST
	 * @param employee
	 */
	@RequestMapping(value="/createEmployeeOfDept", method = RequestMethod.POST)
	@ResponseBody
	public void createEmployee(@RequestBody Employee employee) {
		employeeDao.insert(employee);
	}
	
	/**
	 * Query all employees in database
	 * @return
	 */
	@RequestMapping(value="/queryAll", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> queryAllEmployees(){
		return employeeDao.queryAll();
	}
	
	@RequestMapping(value="/queryAllByDept", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> queryEmployeeByDept(@RequestParam long deptId) {
		return employeeDao.queryEmployeeByDept(deptId);
	}
	
	@RequestMapping(value="/queryOneById/", method = RequestMethod.GET)
	@ResponseBody
	public Employee queryEmployeeById(@RequestParam Long employeeId) {
		
		return employeeDao.queryEmployeeById(employeeId);
	}
	
	/**
	 * Only support HTTP POST
	 * @param employee
	 */
	@RequestMapping(value="/update", method = RequestMethod.POST)
	public void updateEmployee(@RequestBody Employee employee) {
		employeeDao.updateEmployee(employee);
	}
	
	@RequestMapping("/delete")
	public void deleteEmployee(@RequestParam long employeeId) {
		employeeDao.deleteEmployee(employeeId);
	}
}
