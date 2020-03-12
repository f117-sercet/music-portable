package control.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import control.DateBaseConnector;
import control.ExceptionHandler;

/**
 * �����¼����
 * @author 60221
 *
 */
public class Login extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2644673894829698324L;
	
	private RequestDispatcher rd = null;
	private Connection con = null;
	private DateBaseConnector db = null;
	private static final String message = "��½ʧ��";
	/**
	 * 
	 * @�����¼����
	 */
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		db = new DateBaseConnector();
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = action = req.getParameter("action");
		HttpSession session =req.getSession(true);
		if("attempt".equals(action)){
			String queryuser = "select UserName from UserInfo where UserName=? and UserPassword=?";
			String username = validateUser(req,resp,queryuser,1);
			try{
				if(username==null){
					throw new ExceptionHandler("�ʺ��������,����������");
				}
			}catch(ExceptionHandler e){
				doError(req,resp,message,e);
				return;
			}
			session.setAttribute("username",username);
			resp.sendRedirect(req.getContextPath()+"/index.jsp");
			return;
		}
		if("adminlogin".equalsIgnoreCase(action)){
			String queryadmin = "select AdminName from AdminInfo where AdminName=? and AdminPassword=?";
			String adminname = validateUser(req,resp,queryadmin,2);
			session.setAttribute("adminname",adminname);
			resp.sendRedirect(req.getContextPath()+"/admin/index.jsp");
			return;
		}
		if("logout".equals(action)){
			session.invalidate();
			resp.sendRedirect(req.getContextPath()+"/index.jsp");
			return;
		}
	}
	/**
	*����û������Ƿ����Ҫ��
	 * @throws ExceptionHandler 
	*/
	public boolean isAccord(HttpServletRequest req, HttpServletResponse resp,
            String username,String password)
            throws ServletException, IOException{
	    boolean accord = true;                     	
	    String message ="";
	    try{
	    	if(username==null){
		    	accord = false;
			    message="�û�������Ϊ��,��������д";
			    throw new ExceptionHandler("��½ʧ��");
			
		    }
			if(password==null){
				accord = false;
				message="���벻��Ϊ�գ���������д��";
				throw new ExceptionHandler("��½ʧ��");
			}
	    }catch(ExceptionHandler e){
	    	doError(req,resp,message,e);
	    }
	    
		return accord;
	}
	/**
	 * ��֤�û�
	 */
	public String validateUser(HttpServletRequest req,HttpServletResponse resp,
								String query,int state)
	  throws ServletException,IOException{
		PreparedStatement pst = null;
		String username = "";
		String password = "";
		if(state==1){//��ͨ�û�
			username = req.getParameter("username").trim();
			password = req.getParameter("password").trim();
		}else{//����Ա
			username = req.getParameter("adminname").trim();
			password = req.getParameter("adminpassword").trim();
		}try{
			if(isAccord(req,resp,username,password)){
				con = db.getConnection();
				pst = con.prepareStatement(query);
				pst.setString(1,username);
				pst.setString(2,password);
				ResultSet rs = pst.executeQuery();
				if(rs.next()){
					username = rs.getString(1);
				}else{
					username= null;
				}
			}
			con.close();
		}catch(SQLException e){
			doError(req,resp,message,e);
			}
		return username;
	}
	/**
	*������
	*/
	public void doError(HttpServletRequest req,
			HttpServletResponse resp,String message,Exception e)
	    throws ServletException,IOException {
        req.setAttribute("error", message+"<br/>"+e.getMessage()); 
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/errorpage.jsp"); 
        rd.forward(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}

	

}
