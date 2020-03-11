package model;

import java.io.Serializable;
import java.sql.Date;
/**
 * “Ù¿÷π‹¿Ìƒ£øÈ
 * @author 60221
 *
 */
public class MusicModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int musicid;
	private String musicname;
	private String musictype;
	private String singername;
	private Date submitime;
	public int getMusicid() {
		return musicid;
	}
	public void setMusicid(int musicid) {
		this.musicid = musicid;
	}
	public String getMusicname() {
		return musicname;
	}
	public void setMusicname(String musicname) {
		this.musicname = musicname;
	}
	public String getMusictype() {
		return musictype;
	}
	public void setMusictype(String musictype) {
		this.musictype = musictype;
	}
	public String getSingername() {
		return singername;
	}
	public void setSingername(String singername) {
		this.singername = singername;
	}
	public Date getSubmitime() {
		return submitime;
	}
	public void setSubmitime(Date submitime) {
		this.submitime = submitime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
