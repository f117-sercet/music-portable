package control.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.servlet4preview.RequestDispatcher;

import control.DateBaseConnector;
import control.ExceptionHandler;

//**请求处理
public class Register extends HttpServlet {

	
	private RequestDispatcher rd = null;
	private Connection con = null;
	private DateBaseConnector db = null;
	private static final String message = "注册失败";
	
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		db = new DateBaseConnector();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
		String action = action = req.getParameter("action");
		if("apply".equals(action)){
			 rd = (RequestDispatcher) getServletContext().getRequestDispatcher("/regpage.jsp");
			 rd.forward(req,resp);
			 return;
		}
		if("confirm".equals(action)){
			rd = (RequestDispatcher) getServletContext().getRequestDispatcher("/register.jsp");
			rd.forward(req,resp);
			return;
		}
		if("save".equals(action)){
			String username = addUser(req,resp);
			if(username!=null){
				HttpSession session=req.getSession(true);
				session.setAttribute("username",username);
			/*	rd = getServletContext().getRequestDispatcher("/index.jsp");
				rd.forward(req,resp);*/
				resp.sendRedirect(req.getContextPath()+"/index.jsp");
				return;
			}
		}
	}
	/**
	*检查用户输入是否符合要求
	*/
	public boolean isAccord(HttpServletRequest req, HttpServletResponse resp,
            String username,String password,String confirmpwd,
            String useremail)
            throws ServletException, IOException {
	    boolean accord = true;
	    try{
	    	 if(username==null||username.equals("")){
	 	    	accord = false;
	 	    	throw new ExceptionHandler("用户名不能为空,请重新填写");
	 	    }
	 		if(password==null||password.equals("")){
	 			accord = false;
	 			throw new ExceptionHandler("密码不能为空，请重新输入！");
	 		}
	 		if(confirmpwd==null||confirmpwd.equals("")){
	 			accord = false;
	 			throw new ExceptionHandler("确认密码不能为空，请重新输入");
	 		}
	 		if(!password.equals(confirmpwd)){
	 			accord = false;
	 			throw new ExceptionHandler("密码不一致,请重新填写!");
	 		}
	 		if(useremail==null||useremail==""){
	 			accord = false;
	 			throw new ExceptionHandler("E-Mail不能为空,请重新填写!");
	 		}
	 		String regEx = "^([a-z0-9_-])+@([a-zZ0-9_-])+(\\.[a-z0-9_-])+[a-z]{2,3}$";
	 		Pattern p = Pattern.compile(regEx);
	 		Matcher m =p.matcher(useremail);
	        if(!m.find()){
	        	accord = false;
	 			throw new ExceptionHandler("E-Mail格式不正确,请重新填写!");
	        }

	    }catch(ExceptionHandler e){
	    	doError(req,resp,message,e);
	    	return false;
	    }
	   return accord;
	}/**
		*添加新用户
		*/
	public String addUser(HttpServletRequest req,HttpServletResponse resp)
	  throws ServletException,IOException{
		String username = req.getParameter("username").trim();
		String password = req.getParameter("password").trim();
		String confirmpwd = req.getParameter("confirmpwd").trim();
		String useremail = req.getParameter("useremail").trim();
		String usersex = req.getParameter("usersex").trim();
		String userqq = req.getParameter("userqq").trim();
		PreparedStatement pst = null;
		try{
			if(isAccord(req,resp,username,password,confirmpwd,useremail)&&isRegister(req,resp,username)){
				con = db.getConnection();
				String insert = "insert into UserInfo(UserName,UserPassword,UserEmail,UserSex,UserQQ) VALUES(?,?,?,?,?)";
				pst = con.prepareStatement(insert);
				pst.setString(1,username);
				pst.setString(2,password);
				pst.setString(3,useremail);
				pst.setString(4,usersex);
				pst.setString(5,userqq);
				int rowaffect = pst.executeUpdate();
				if(rowaffect==0){
					throw new ExceptionHandler("出错啦");
				}
				if(con!=null)
				con.close();
			}
		}catch(SQLException e){
				e.getMessage();
			}
		catch(ExceptionHandler e){
			doError(req,resp,message,e);
	  		}
		return username;
	}
	/***
	 *
	 * 检查该用户是否已经注册
	 */
	public boolean isRegister(HttpServletRequest req,HttpServletResponse resp,
			String username) 
		throws ServletException,IOException,SQLException{   
	  	boolean canreg = true;
	  	con = db.getConnection();
	  	String query = "select UserName from UserInfo where UserName =?";
	  	PreparedStatement pst = null;
	  	try{
		  	pst = con.prepareStatement(query);
		  	pst.setString(1,username);
		  	ResultSet rs = pst.executeQuery();;
		  	if(rs.next()){ //
		  		canreg=false;
		  		throw new ExceptionHandler("对不起,该用户已被注册,请重新注册!");
		  		}else{
		  			canreg = true;
		  		}
		}catch(ExceptionHandler e){
			doError(req,resp,message,e);
	  		}
		catch(SQLException e){
			doError(req,resp,message,e);
		}
		if(con!=null)
		con.close();
	  	return canreg;
	  }
	/**
	*错误处理
	*/
	public void doError(HttpServletRequest req,
			HttpServletResponse resp,String errorstr,Exception e)
	    throws ServletException,IOException {
        req.setAttribute("error", errorstr+"<br/>"+e.getMessage()); 
        RequestDispatcher rd = (RequestDispatcher) getServletContext().getRequestDispatcher("/errorpage.jsp"); 
        rd.forward(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	  throws ServletException, IOException {
			doGet(req, resp);
	}

}
