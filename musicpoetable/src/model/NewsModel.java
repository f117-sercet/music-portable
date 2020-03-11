package model;

import java.io.Serializable;
import java.sql.Date;

/**
 * ÐÂÎÅÄ£¿é
 * @author 60221
 *
 */
public class NewsModel   implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String newstitle;
	private String newscontent;
	private String promulgator;
	private Date submitime;
	private int newsid;
	public String getNewstitle() {
		return newstitle;
	}
	public void setNewstitle(String newstitle) {
		this.newstitle = newstitle;
	}
	public String getNewscontent() {
		return newscontent;
	}
	public void setNewscontent(String newscontent) {
		this.newscontent = newscontent;
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
	public int getNewsid() {
		return newsid;
	}
	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}


}
