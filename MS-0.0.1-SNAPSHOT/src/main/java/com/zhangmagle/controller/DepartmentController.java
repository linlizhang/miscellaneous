package com.zhangmagle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhangmagle.dao.DepartmentDao;
import com.zhangmagle.model.Department;

/**
 * Providing RESTful API for department operations, including 
 * 	1. create a sub-department according to an existed department
 * 	2. Query all department information
 *  3. query all sub-departments of a department
 *  4. delete a department
 *  5. update information of a department
 *  6. query parent department of another
 * @author my
 *
 */

@RestController
@RequestMapping("/deparment")
public class DepartmentController {

	@Autowired
	DepartmentDao departmentDao;
	
	/**
	 * Only support HTTP POST
	 * @param dept
	 */
	@RequestMapping(value="/createDept", method=RequestMethod.POST)
	@ResponseBody
	public void createDept(@RequestBody Department dept) {
		departmentDao.createDept(dept);
	}
	
	@RequestMapping("/queryAll")
	@ResponseBody
	public List<Department> queryAll() {
		return departmentDao.queryAll();
	}
	
	@RequestMapping("/querySubDept")
	@ResponseBody
	public List<Department> querySubDepartment(@RequestParam long deptId) {
		return departmentDao.querySubDepartment(deptId);
	}
	
	@RequestMapping("/queryParentDept")
	@ResponseBody
	public Department queryParentDept(@RequestParam long deptId) {
		return departmentDao.queryParentDept(deptId);
	}
	
	/**
	 * Only support HTTP POST
	 * @param dept
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public void updateDept(@RequestBody Department dept) {
		departmentDao.updateDept(dept);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public void deleteDept(@RequestParam long deptId) {
		departmentDao.deleteDept(deptId);
	}
}
