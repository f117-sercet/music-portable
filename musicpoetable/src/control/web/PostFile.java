package control.web;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.RequestDispatcher;
import org.apache.commons.fileupload.DiskFileUpload;

import control.DateBaseConnector;
import control.ExceptionHandler;

/**
 * 处理上传请求
 * @author 60221
 *
 */
public class PostFile extends HttpServlet {
	private Connection con = null;
	private DateBaseConnector db = null;
	private String realpath = "";
	private String uploadpath = "";
	private String temppath = ""; 
	private static final String message = "上传失败";
	
	
	public void init() throws ServletException {
		db = new DateBaseConnector();//连接数据库
		
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = action = req.getParameter("action");
		if("open".equals(action)){
			req.setAttribute("upstate","open");
			RequestDispatcher rd = (RequestDispatcher) getServletContext().getRequestDispatcher("/upload.jsp");
			if(rd!=null){
				rd.forward(req,resp);
				return;
			}
		}
		if("upfile".equals(action)){
			if(doUpFile(req,resp)){
				req.setAttribute("upstate","upsucessed");
				RequestDispatcher rd = (RequestDispatcher) getServletContext().getRequestDispatcher("/upload.jsp");
				rd.forward(req,resp);
				return;
			}
		/*		resp.sendRedirect(req.getContextPath()+"/upload.jsp?state=upsucessed");
			return;*/
		}
	}
	/**
	 * @取得文件扩展名
	 */
	public String getTypePart(String filename) { 
	    int point = filename.lastIndexOf('.'); 
	    int length = filename.length(); 
	    if (point == -1 || point == length - 1) { 
	      return ""; 
	    } 
	    else { 
	      return filename.substring(point + 1, length); 
	    } 
	  } 
	/**
	 * 
	 * @上传文件
	 */
	  public boolean doUpFile(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		  boolean upsucceed = true;
		  try{//根目录的绝对路径
			//  realpath = new File(req.getSession().getServletContext().getRealPath(req.getRequestURI())).getParent();
			  realpath = new File(req.getSession().getServletContext().getRealPath(req.getContextPath())).getParent();
			  //realpath = req.getContextPath();
			  String extname = "";
			  String musicname = "";
			  String singername = "";
			  String singersex = "";
			  uploadpath = realpath+"/Upload/";
			  temppath = realpath+"/Upload/temp/"; 
			  if(!new File(uploadpath).isDirectory())
					new File(uploadpath).mkdirs();
			  if(!new File(temppath).isDirectory())
					new File(temppath).mkdirs();
			  DiskFileUpload fileupload = new DiskFileUpload();
			  fileupload.setSizeMax(10240000);//上传文件最大为10MB
			  fileupload.setSizeThreshold(10240);//缓冲区为1MB
			  fileupload.setRepositoryPath(temppath);//设置临时文件夹
			  List list = fileupload.parseRequest(req);
			  //得到所有文件
			  Iterator iter = list.iterator();
			  while(iter.hasNext())//依次处理每一个文件
			  { 
				  FileItem item = (FileItem)iter.next();
				  if(!item.isFormField()){//如果不是表单域,则取得文件信息
					  String filename = item.getName().trim();
					  long filesize = item.getSize();
					  extname = getTypePart(filename);
					  if(filename==null&&filesize==0)
						  continue;
				  }else if(item.isFormField()){//取得表单域的信息
					  		String fieldname = item.getFieldName();
					  		if("musicname".equals(fieldname))
					  			musicname = item.getString().trim();
					  		if("singername".equals(fieldname))
					  			singername = item.getString().trim();
					  		if("singersex".equals(fieldname))
					  			singersex = item.getString().trim();
				  }
				  if(extname.equalsIgnoreCase("wav")||extname.equalsIgnoreCase("wma")||extname.equalsIgnoreCase("mp3"))
				{//将文件写到指定目录
					String finalname = addMusicInfo(req,resp,extname,musicname,singername,singersex);
					item.write(new File(uploadpath+finalname+"."+extname));
				}
			}
		  }catch(Exception e){
			doError(req,resp,message,e);
			upsucceed = false;
		}
		return upsucceed;
	}
			/**
			 *@将相关歌手和歌曲信息添加到数据库
			 */
	public String addMusicInfo(HttpServletRequest req,HttpServletResponse resp,
			String musictype,String musicname,String singername,String singersex)
			throws ServletException,IOException,SQLException{
		int rowaffect = 0;
		String finalsingername = "";
		try{//将歌手信息写进数据库
			con = db.getConnection();
			String query = "select singername from singerinfo where singername=?";
			PreparedStatement pst1 = con.prepareStatement(query);
			pst1.setString(1,singername);
			ResultSet rs1 = pst1.executeQuery();
			if(!rs1.next()){
				String insertsinger = "insert into SingerInfo(SingerName,SingerSex) VALUES(?,?)";
				PreparedStatement pst = con.prepareStatement(insertsinger);
				pst.setString(1,singername);
				pst.setString(2,singersex);
				rowaffect = pst.executeUpdate();
				if(rowaffect<1){
					throw new ExceptionHandler("添加歌手信息失败");
				}
			}
		}catch(SQLException e){
			doError(req,resp,message,e);
		}
		catch(ExceptionHandler e1){
			doError(req,resp,message,e1);
		}
		finally{ 
			finalsingername = singername;
		}
			
		if(rowaffect>=1){//将歌曲信息写到数据库
			try{
				String insertmusic = "insert into MusicInfo(MusicName,MusicType,SingerName) VALUES(?,?,?)";
				PreparedStatement pst = con.prepareStatement(insertmusic);
				pst.setString(1,musicname);
				pst.setString(2,musictype);
				pst.setString(3,finalsingername);
				rowaffect = pst.executeUpdate();
				if(rowaffect<1){
						throw new ExceptionHandler("添加歌曲信息失败");
					}
				}catch(SQLException e){
					doError(req,resp,message,e);
				}
				catch(ExceptionHandler e1){
					doError(req,resp,message,e1);
				}
		}
		if(con!=null)
			con.close();
		return singername+musicname;
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
        return;
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}

}

