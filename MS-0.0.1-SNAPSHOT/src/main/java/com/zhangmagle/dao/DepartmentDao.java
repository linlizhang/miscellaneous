package com.zhangmagle.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.zhangmagle.model.Department;

@Component
public class DepartmentDao extends JdbcDaoSupport{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void createDept(final Department dept) {
		final String sql = "insert into department "
				+ " (name, location, managerId, parentDeptId, openPosition?)"
				+ " values(?,?,?,?,?)";
		this.jdbcTemplate.update(sql, new Object[]{dept.getName(), dept.getLocation(), 
				dept.getManagerId(), dept.getParentDeptId(), dept.getOpenPositions()});
	}

	public List<Department> queryAll() {
		String sql = "select * from department";
		return this.jdbcTemplate.query(sql, new DepartmentMapper());
	}
	
	public Department queryParentDept(long deptId) {
		String sql = "select * from department where id= (select parentDeptId from department where id=?)";
		return this.jdbcTemplate.query(sql, 
				new Object[]{deptId}, new PersonResultSetExtractor());
	}
	
	public List<Department> querySubDepartment(long deptId) {
		String sql = "select * from department where parentDeptId=?";
		return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<Department>(Department.class));
	}
	
	public Department queryDeptById(long id) {
		String sql = "select * from department where id=?";
		return this.jdbcTemplate.query(sql, new Object[]{id}, new PersonResultSetExtractor());
	}
	
	public void updateDept(Department dept) {
		String sql = "update department set name=?, location=?, managerId=?, parentDeptId=?, openPosition=?"
				 + " where id=?";
		DepartmentPreparedStatementSetter setter = new DepartmentPreparedStatementSetter();
		List<Department> list = new ArrayList<Department>();
		list.add(dept);
		setter.setList(list);
		this.jdbcTemplate.batchUpdate(sql, setter);
	}
	
	public void deleteDept(long deptId) {
		String sql = "delete from department where id=?";
		this.jdbcTemplate.update(sql, Long.valueOf(deptId));
	}

	private static final class DepartmentMapper implements RowMapper<Department> {
		public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
			Department dept = new Department();
			dept.setId(rs.getLong("id"));
			dept.setLocation(rs.getString("location"));
			dept.setName(rs.getString("name"));
			return dept;
		}
	}

	private class PersonResultSetExtractor implements ResultSetExtractor<Department> {
		@Override
		public Department extractData(ResultSet rs) throws SQLException {
			Department dept = new Department();
			dept.setId(rs.getLong("id"));
			dept.setLocation(rs.getString("location"));
			dept.setName(rs.getString("name"));
			return dept;
		}
	}
	
	private class DepartmentPreparedStatementSetter implements BatchPreparedStatementSetter {
		private List<Department> list;
		public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, list.get(i).getName());
            ps.setString(2, list.get(i).getLocation());
            ps.setLong(3, list.get(i).getManager().getId());
            ps.setLong(4, list.get(i).getParentDeptId());
            ps.setInt(5, list.get(i).getOpenPositions());
            ps.setLong(6, list.get(i).getId());
        }

        public int getBatchSize() {
            return list.size();
        }
        
        public void setList(List<Department> list) {
        	this.list = list;
        }
	}
}
