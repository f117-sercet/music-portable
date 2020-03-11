package model;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * ÁôÑÔÄ£¿é
 *
 */
public class MessageModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messagetitle;
	private String messagecontent;
	private String promulgator;
	private Date submitime;
	private int messageid;
	public String getMessagetitle() {
		return messagetitle;
	}
	public void setMessagetitle(String messagetitle) {
		this.messagetitle = messagetitle;
	}
	public String getMessagecontent() {
		return messagecontent;
	}
	public void setMessagecontent(String messagecontent) {
		this.messagecontent = messagecontent;
	}
	public String getPromulgator() {
		return promulgator;
	}
	public void setPromulgator(String promulgator) {
		this.promulgator = promulgator;
	}
	public Date getSubmitime() {
		return submitime;
	}
	public void setSubmitime(Date submitime) {
		this.submitime = submitime;
	}
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
