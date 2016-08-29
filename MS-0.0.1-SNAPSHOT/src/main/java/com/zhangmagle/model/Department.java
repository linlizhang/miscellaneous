package com.zhangmagle.model;

public class Department {

	private long id;
	private long parentDeptId;
	private String name;
	private String location;
	private Employee manager;
	private long managerId;
	private int openPositions;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentDeptId() {
		return parentDeptId;
	}
	public void setParentDeptId(long parentDeptId) {
		this.parentDeptId = parentDeptId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Employee getManager() {
		return manager;
	}
	public void setManager(Employee manager) {
		this.manager = manager;
	}
	public int getOpenPositions() {
		return openPositions;
	}
	public void setOpenPositions(int openPositions) {
		this.openPositions = openPositions;
	}
	public long getManagerId() {
		return managerId;
	}
	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
}
