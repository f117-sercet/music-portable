package control.bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import control.DateBaseConnector;
import model.NewsModel;

/**
 * 新闻管理
 * 
 * @author 60221
 *
 */
public class ManageNews {

	private Connection con;
	private DateBaseConnector db;
	/**
	 * 构造函数
	 */
	public ManageNews(){
		db = new DateBaseConnector();
	}
	public Vector displayQuery(int noteperpage,int pagecode,
			String condition,String keyfield) 
				throws SQLException{
		con = db.getConnection();
		CallableStatement cst = null;
		String query = "{call sp_getpage(?,?,?,?)}";
		Vector newsvector = null;
		try{
			cst = con.prepareCall(query);
			cst.setInt(1,noteperpage);
			cst.setInt(2,pagecode);
			cst.setString(3,condition);
			cst.setString(4,keyfield);
			ResultSet rs = cst.executeQuery();
			newsvector = new Vector();
			while(rs.next())
			{ 
				NewsModel newsmodel = new NewsModel();
				newsmodel.setNewsid(rs.getInt(1));
				newsmodel.setNewstitle(rs.getString(2));
			  	newsmodel.setPromulgator(rs.getString(3));
			  	newsmodel.setSubmitime(rs.getDate(4));
			  	newsmodel.setNewscontent(rs.getString(5));
			  	newsvector.add(newsmodel);
			}
		}catch(SQLException e){
			e.getMessage();
		 }
		if(con!=null)
		con.close();
		return newsvector;
	}
	/**
	 * 返回表的总行数
	 */
	public int getTotalrow() throws SQLException{
		con = db.getConnection();
		PreparedStatement pst = null;
		String query = "select count(*) from NewsInfo";
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
	 * 修改新闻
	 */
	public int updateNews(int newsid,String newstitle,String newscontent)
					throws SQLException{
		con = db.getConnection();
		PreparedStatement pst = null;
		String update = "update NewsInfo set NewsTitle=?,NewsContent=? where NewsID = ?";
		int rowaffect = 0;
		try{
			pst = con.prepareStatement(update);
			pst.setString(1,newstitle);
			pst.setString(2,newscontent);
			pst.setInt(3,newsid);
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
	 * 添加新闻
	 */
	public int addNews(String newstitle,String adminname,String newscontent)throws SQLException{
		con = db.getConnection();
		PreparedStatement pst = null;
		String insert = "insert into NewsInfo (NewsTitle,AdminName,NewsContent)values(?,?,?)";
		int rowaffect = 0;
		try{
			pst = con.prepareStatement(insert);
			pst.setString(1,newstitle);
			pst.setString(2,adminname);
			pst.setString(3,newscontent);
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

