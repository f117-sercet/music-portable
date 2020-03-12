package control.bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import control.DateBaseConnector;
import model.MessageModel;

public class ManageGuestBook {

	private Connection con;
	private DateBaseConnector db;
	/**
	 * 构造函数
	 */
	public ManageGuestBook(){
		db = new DateBaseConnector();
	}
	public Vector displayQuery(int noteperpage,int pagecode,
			String condition,String keyfield) 
				throws SQLException{
		con = db.getConnection();
		CallableStatement cst = null;
		String query = "{call sp_getpage(?,?,?,?)}";
		Vector msgvector = null;
		try{
			cst = con.prepareCall(query);
			cst.setInt(1,noteperpage);
			cst.setInt(2,pagecode);
			cst.setString(3,condition);
			cst.setString(4,keyfield);
			ResultSet rs = cst.executeQuery();
			msgvector = new Vector();
			while(rs.next())
			{ 
				MessageModel msgmodel = new MessageModel();
				msgmodel.setMessageid(rs.getInt(1));
				msgmodel.setMessagetitle(rs.getString(2));
				msgmodel.setPromulgator(rs.getString(3));
				msgmodel.setSubmitime(rs.getDate(4));
				msgmodel.setMessagecontent(rs.getString(5));
				msgvector.add(msgmodel);
			}
		}catch(SQLException e){
			e.printStackTrace();
		 }
		if(con!=null)
		con.close();
		return msgvector;
	}
	/**
	 * 返回表的总行数
	 */
	public int getTotalrow() throws SQLException{
		con = db.getConnection();
		PreparedStatement pst = null;
		String query = "select count(*) from MessageInfo";
		int totalrow = 0;
		try{
			pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				totalrow = rs.getInt(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		if(con!=null)
			con.close();
		return totalrow;
	}
	/**
	 * 修改留言
	 */
	public int updateMessage(int msgid,String msgtitle,String msgcontent)
					throws SQLException{
		con = db.getConnection();
		PreparedStatement pst = null;
		String update = "update MessageInfo set MessageTitle=?,MessageContent=? where MessageID = ?";
		int rowaffect = 0;
		try{
			pst = con.prepareStatement(update);
			pst.setString(1,msgtitle);
			pst.setString(2,msgcontent);
			pst.setInt(3,msgid);
			rowaffect = pst.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
		if(con!=null){
			con.close();
		}
		return rowaffect;
	}
	/**
	 * 添加留言
	 */
	public int addMessage(String msgtitle,String username,String msgcontent)
				throws SQLException{
		con = db.getConnection();
		PreparedStatement pst = null;
		String insert = "insert into MessageInfo(MessageTitle,UserName,MessageContent)values(?,?,?)";
		int rowaffect = 0;
		try{
			pst = con.prepareStatement(insert);
			pst.setString(1,msgtitle);
			pst.setString(2,username);
			pst.setString(3,msgcontent);
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
	 * 删除留言
	 * 
	 */
	public int deleteMessage(int msgid)throws SQLException{
		con = db.getConnection();
		PreparedStatement pst = null;
		String delete = "delete from MessageInfo where MessageID = ?";
		int rowaffect = 0;
		try{
			pst = con.prepareStatement(delete);
			pst.setInt(1,msgid);
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

