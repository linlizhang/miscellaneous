package com.zhangmagle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.zhangmagle.model.Employee;

@Component
public class EmployeeDao extends JdbcDaoSupport{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void insert(final Employee employee) {
		final String sql = "INSERT INTO employee "
				+ " (firstName, lastName, ldapUserName, gender, birthDate, title, grade, departId) "
				+ " values (?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, employee.getFirstName());
				ps.setString(2, employee.getLastName());
				ps.setString(3, employee.getLdapUserName());
				ps.setInt(4, employee.getGender().ordinal());
				ps.setDate(5, employee.getBirthDate());
				ps.setString(6, employee.getTitle());
				ps.setString(7, employee.getGrade());
				ps.setLong(8, employee.getDeptId());
				return ps;
			}
		});
	}

	public List<Employee> queryAll(){
		String querySql = "select * from employee";
		List<Employee> employeeList = jdbcTemplate.query(querySql, new EmployeeMapper());
		return employeeList;
	}
	
	public List<Employee> queryEmployeeByDept(long deptId) {
		String querySql = "select * from employee where deptId=?";
		List<Employee> employeeList = jdbcTemplate.query(querySql, new Object[]{deptId}, new EmployeeMapper());
		return employeeList;
	}

	public Employee queryEmployeeById(long id) {
		String querySql = "select * from employee where id=?";
		Employee employee = this.jdbcTemplate.query(querySql, new Object[] { id }, new PersonResultSetExtractor());
		return employee;
	}
	
	public void updateEmployee(Employee employee) {
		String sql = "update employee set where id=?";
		EmployeePreparedStatementSetter setter = new EmployeePreparedStatementSetter();
		List<Employee> list = new ArrayList<Employee>();
		list.add(employee);
		setter.setList(list);
		this.jdbcTemplate.batchUpdate(sql, setter);
	}
	
	public void deleteEmployee(long employeeId) {
		String sql = "delete from employee where id=?";
		this.jdbcTemplate.update(sql, Long.valueOf(employeeId));
	}

	private static final class EmployeeMapper implements RowMapper<Employee> {
		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee employee = new Employee();
			employee.setFirstName(rs.getString("firsName"));
			employee.setLastName(rs.getString("lastName"));
			employee.setDeptId(rs.getInt("deptId"));
			return employee;
		}
	}

	private class PersonResultSetExtractor implements ResultSetExtractor<Employee> {
		  @Override
		  public Employee extractData(ResultSet rs) throws SQLException {
			  Employee employee = new Employee();
				employee.setFirstName(rs.getString("firsName"));
				employee.setLastName(rs.getString("lastName"));
				employee.setDeptId(rs.getInt("deptId"));
		    return employee;
		  }
	}
	
	private class EmployeePreparedStatementSetter implements BatchPreparedStatementSetter {
		private List<Employee> list;
		public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, list.get(i).getFirstName());
            ps.setString(2, list.get(i).getLastName());
            ps.setString(3, list.get(i).getLdapUserName());
            ps.setString(4, list.get(i).getTitle());
            ps.setString(5, list.get(i).getGrade());
            ps.setLong(5, list.get(i).getDeptId());
            ps.setInt(7, list.get(i).getGender().ordinal());
            ps.setLong(8, list.get(i).getId());
        }

        public int getBatchSize() {
            return list.size();
        }
        
        public void setList(List<Employee> list) {
        	this.list = list;
        }
	}
}
