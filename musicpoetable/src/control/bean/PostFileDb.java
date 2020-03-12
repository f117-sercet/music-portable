package control.bean;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;

import control.DateBaseConnector;
import ejb.UpFileHome;
import ejb.UpFileRemote;

/**
 * 上产文件到数据库
 * @author 60221
 *
 */
public class PostFileDb extends HttpServlet {
	private Connection con = null;
	private DateBaseConnector db = null;
	private String realpath = "";
	private String uploadpath = "";
	private String temppath = ""; 
	private String finalmusicname = "";
	private String finalsingername = "";
	private String finalsingersex = "";
	private String extname = "";
	private UpFileHome sessionhome;
	private UpFileRemote sessionremote;
	private static final String message = "上传失败";
	
	
	public void init() throws ServletException {
		db = new DateBaseConnector();//连接数据库
		try{
			Context ic = new InitialContext();
	   	    java.lang.Object obj = ic.lookup("UpSessionBean");
	   	    sessionhome = (UpFileHome)PortableRemoteObject.narrow(obj,UpFileHome.class);   
	    }catch(Exception e){
	         e.printStackTrace();
	     }   
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = action = req.getParameter("action");
		if("upfile".equals(action)){
		/*	if(doUpFile(req,resp)){
				req.setAttribute("upstate","upsucessed");
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/upload.jsp");
				rd.forward(req,resp);
				return;
			}
			resp.sendRedirect(req.getContextPath()+"/upload.jsp?state=upsucessed");
			return;*/
		}
	}
	/**
	 * 将文件写入数据库
	 */
	public boolean PostFileDb(HttpServletRequest req, HttpServletResponse resp,String finalpath,
					String musicname,String singername,String musictype){
		boolean succeed = false;
		try{
			sessionremote = sessionhome.Create(finalpath,musicname,musictype,singername);
			if(sessionremote.startEntBean(finalpath,musicname,musictype,singername)){
				succeed = true;
			}
	     }catch(Exception e){
	           e.printStackTrace();
	      }
	     return succeed;
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
	 * @throws Exception 
	 * @throws SQLException 
	 * @上传文件
	 */
	  public String doUpFile(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, Exception{
		  String finalpath = "";
		  try{//根目录的绝对路径
			//  realpath = new File(req.getSession().getServletContext().getRealPath(req.getRequestURI())).getParent();
			  realpath = new File(req.getSession().getServletContext().getRealPath(req.getContextPath())).getParent();
			  //realpath = req.getContextPath();
			  
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
					  		if("dbmusicname".equals(fieldname))
					  			finalmusicname = item.getString().trim();
					  		if("dbsingername".equals(fieldname))
					  			finalsingername = item.getString().trim();
					  		if("dbsingersex".equals(fieldname))
					  			finalsingersex = item.getString().trim();
				  }
				  if(extname.equalsIgnoreCase("wav")||extname.equalsIgnoreCase("wma")||extname.equalsIgnoreCase("mp3"))
				{//将文件写到指定目录
					  if(checkMusicInfo(req,resp,extname,finalmusicname,finalsingername)){
						  String finalname = finalsingername+finalmusicname;
						  finalpath = uploadpath+finalname+"."+extname;
					      item.write(new File(finalpath));
					  }else{
						  throw new ExceptionHandler("文件已存在!");
					  }
				}
			}
		  }catch(ExceptionHandler e){
			doError(req,resp,message,e);
		}
		return finalpath;
	}
			/**
			 *@查找音乐是否已存在
			 *@条件:同名,同歌手,同后缀名
			 */
	public boolean checkMusicInfo(HttpServletRequest req,HttpServletResponse resp,
			String musictype,String musicname,String singername)
			throws ServletException,IOException,SQLException{
		boolean canupload = true;
		String cmusicname = "";
		String csingername = "";
		String cmusictype = "";
		try{
			con = db.getConnection();
			String query = "select * from singerinfo where singername=?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1,singername);
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				cmusicname = rs.getString("MusicName");
				csingername = rs.getString("SingerName");
				cmusictype = rs.getString("MusicType");
				if(cmusicname.equalsIgnoreCase(musicname)||csingername.equalsIgnoreCase(singername)||cmusictype.equalsIgnoreCase(musictype))
					canupload = false;
			}
		}catch(SQLException e){
			doError(req,resp,message,e);
		}
		return canupload;
	}
	/**
	*错误处理
	*/
	public void doError(HttpServletRequest req,
			HttpServletResponse resp,String errorstr,Exception e)
	    throws ServletException,IOException {
        req.setAttribute("error", errorstr+"<br/>"+e.getMessage()); 
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/errorpage.jsp"); 
        rd.forward(req,resp);
        return;
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}

}

