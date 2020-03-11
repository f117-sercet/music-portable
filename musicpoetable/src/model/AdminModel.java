package model;

import java.io.Serializable;
import java.sql.Date;

/**
 * ¹ÜÀíÄ£¿é
 * @author ÓÀÔ¶Ï²»¶ÑÇÀòÉ¯
 *
 */
public class AdminModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String adminname;
	private String adminpwd;
	private int adminid;
	private Date regtime;
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getAdminpwd() {
		return adminpwd;
	}
	public void setAdminpwd(String adminpwd) {
		this.adminpwd = adminpwd;
	}
	public int getAdminid() {
		return adminid;
	}
	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}
	public Date getRegtime() {
		return regtime;
	}
	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}


}
