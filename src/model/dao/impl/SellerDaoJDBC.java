package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate,BaseSalary, DepartmentId) "
					+"VALUES "
					+"(?,?,?,?,?)"
					);
					
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());		
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			System.out.println("Houve algo no Insert: "+ e);
		}

	}

	@Override
	public void update(Seller obj) {


		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?,Email = ?, BirthDate = ?, BaseSalary =?, DepartmentId =? "
					+"WHERE id = ?"
					);
					
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());		
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			st.executeUpdate();
			
		}catch(SQLException e) {
			System.out.println("Houve algo no Update: "+ e);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"DELETE FROM seller WHERE id = ?");
			
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			System.out.println("Erro no Delete: "+ e);
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
	
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE seller.id = ?"
					);
				
				pst.setInt(1, id);	
				rs = pst.executeQuery();
				if(rs.next()) {
					Department dep = instantiateDepartment(rs);
					Seller obj = instantiateSeller(rs,dep);
					return obj;
				}
				return null;
				
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pst);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {


		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
		pst = conn.prepareStatement(
				"SELECT seller.*, department.Name as DepName "
				+"FROM seller INNER JOIN department "
				+"ON seller.DepartmentId = department.Id "
				+"ORDER BY Name");
			
			rs = pst.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs,dep);
				list.add(obj);
			}
			return list;
	
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pst);
		}

	
	
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
		pst = conn.prepareStatement(
				"SELECT seller.*, department.Name as DepName "
				+"FROM seller INNER JOIN department "
				+"ON seller.DepartmentId = department.Id "
				+"WHERE DepartmentId = ? "
				+"ORDER BY Name");
			
			pst.setInt(1, department.getId());	
			rs = pst.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs,dep);
				list.add(obj);
			}
			return list;
	
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pst);
		}
	}	
}	

