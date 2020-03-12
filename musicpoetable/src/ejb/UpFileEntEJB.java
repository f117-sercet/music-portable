package ejb;


import java.io.*;
import java.sql.*;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.el.ELContext;

import control.DateBaseConnector;

import javax.ejb.CreateException;
/**
/**
 * 主要实现
 * @author 60221
 *
 */
public class UpFileEntEJB implements EntityBean  {
	private DateBaseConnector db = null;
	private Connection con = null;
	private ELContext context;
	private String dbmusicname = "";

	public void ejbCreate(String finalpath,String musicname,
			String musictype,String singername){
		try{
			writeMusic(finalpath,musicname,musictype,singername);
		}catch(SQLException e){
			e.printStackTrace();
		}catch(FileNotFoundException fe){
			fe.printStackTrace();
		}
		
	}
	public void writeMusic(String finalpath,String musicname,
			String musictype,String singername)
					throws SQLException,FileNotFoundException{
		con = db.getConnection();
		String finalsingername = "";
		String query = "select dbMusicName from dbMusicInfo where dbMusicName=?";
		int rowaffect = 0;
		try{
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1,singername);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()){
				String insertmusic = "insert into dbMusicInfo values(?,?,?,?)";
				File file = new java.io.File(finalpath);
				InputStream in = new FileInputStream(file);
				PreparedStatement pst2 = con.prepareStatement(insertmusic);
				pst2.setString(1,musicname);
				pst2.setString(2,musictype);
				pst2.setString(3,finalsingername);
				pst2.setBinaryStream(4,in,(int)file.length());
				rowaffect = pst2.executeUpdate();
				if(rowaffect<1){
					throw new SQLException("添加歌曲信息失败");
				}
			}
		}catch(SQLException e){
				e.getMessage();
			}
			if(con!=null){
				con.close();
			}
		//return musicname;
	}
	 public String ejbFindByPrimaryKey(String primaryKey) 
	 					throws FinderException{
		 boolean result;
	     try{
	          result = selectByPrimaryKey(primaryKey);
	     }catch(Exception ex){
	           throw new EJBException("ejbFindByPrimaryKey: " + ex.getMessage());
	     }
	     if (result) 
	     {
	         return primaryKey;
	     }else{
	           throw new ObjectNotFoundException("Row for id " + primaryKey + " not found.");
	       }
	 }
	 private boolean selectByPrimaryKey(String primaryKey) 
	 				throws SQLException {
		 con = db.getConnection();
		 String query = "select dbMusicName from dbMusicInfo where dbMusicName=?";
		 PreparedStatement pst = con.prepareStatement(query);
		 pst.setString(1,primaryKey);
		 ResultSet rs = pst.executeQuery();
		 boolean result = rs.next();
		 return result;
	 }
	public void ejbPostCreate() throws CreateException {
	}
	public void ejbActivate() throws EJBException, RemoteException {
		dbmusicname = (String)context.getPrimaryKey();
	}
	public void ejbLoad() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
	}
	public void ejbPassivate() throws EJBException, RemoteException {
		dbmusicname = null;
	}
	public void ejbRemove()throws RemoveException,EJBException,RemoteException {
		// TODO Auto-generated method stub
	}
	public void ejbStore() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
	}
	public void unsetEntityContext() throws EJBException {
		context = null;
		try{
			if(con!=null){
				con.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	public void setEntityContext(EntityContext newContext) throws EJBException {
		context = newContext;
		db = new DateBaseConnector();
	}
}

