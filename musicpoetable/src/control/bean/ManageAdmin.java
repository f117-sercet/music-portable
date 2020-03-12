package control.bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import control.DateBaseConnector;
import model.AdminModel;


/**
 * 管理员信息
 * @author 60221
 *
 */
public class ManageAdmin {
	private Connection con;
	private DateBaseConnector db;
	/**
	 * 构造函数
	 */
	public Vector displayQuery(int noteperpage,int pagecode,
			String condition,String keyfield) 
				throws SQLException{
	con = db.getConnection();
	CallableStatement cst = null;
	String query = "{call sp_getpage(?,?,?,?)}";
	Vector adminvector = null;
	try{
		cst = con.prepareCall(query);
		cst.setInt(1,noteperpage);
		cst.setInt(2,pagecode);
		cst.setString(3,condition);
		cst.setString(4,keyfield);
		ResultSet rs = cst.executeQuery();
		adminvector = new Vector();
		while(rs.next())
		{ 
			AdminModel adminmodel = new AdminModel();
			adminmodel.setAdminid(rs.getInt(1));
			adminmodel.setAdminname(rs.getString(2));
		  	adminmodel.setAdminpwd(rs.getString(3));
		  	adminmodel.setRegtime(rs.getDate(4));
		  	adminvector.add(adminmodel);
		}
	}catch(SQLException e){
		e.getMessage();
	 }
	if(con!=null)
	con.close();
	return adminvector;
}
/**
 * 返回表的总行数
 */
public int getTotalrow() throws SQLException{
	con = db.getConnection();
	PreparedStatement pst = null;
	String query = "select count(*) from AdminInfo";
	int totalrow = 0;
	try{
		pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			totalrow = rs.getInt(1);
		}
	}catch(SQLException e){
		e.getMessage();
	}
	if(con!=null)
		con.close();
	return totalrow;
}
/**
 * 更新管理员信息
 */
public int updateAdmin(String adminid,String adminname,String adminpwd)
				throws SQLException{
	con = db.getConnection();
	PreparedStatement pst = null;
	String update = "update AdminInfo set AdminName=?,AdminPassword=? where AdminID = ?";
	int rowaffect = 0;
	try{
		pst = con.prepareStatement(update);
		pst.setString(1,adminname);
		pst.setString(2,adminpwd);
		pst.setInt(3,Integer.parseInt(adminid));
		rowaffect = pst.executeUpdate();
	}catch(SQLException e){
		e.getMessage();
	}
	if(con!=null){
		con.close();
	}
	return rowaffect;
}
/**
 * 添加管理员
 */
public int addAdmin(String adminname,String adminpwd)throws SQLException{
	con = db.getConnection();
	PreparedStatement pst = null;
	String insert = "insert into AdminInfo (AdminName,AdminPassword)values(?,?)";
	int rowaffect = 0;
	try{
		pst = con.prepareStatement(insert);
		pst.setString(1,adminname);
		pst.setString(2,adminpwd);
		rowaffect = pst.executeUpdate();
	}catch(SQLException e){
		e.getMessage();
	}
	if(con!=null){
		con.close();
	}
	return rowaffect;
}
}
