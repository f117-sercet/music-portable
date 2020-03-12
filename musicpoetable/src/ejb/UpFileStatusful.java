package ejb;

import java.io.File;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
/**
 * sessionbean主要实现
 */
public class UpFileStateful implements SessionBean {

	private String finalpath = "";
	private String musicname="";
	private String musictype="";
	private String singername="";
	
	public void ejbCreate(String finalpath ,String musicname,String musictype,String singername)
					throws CreateException{
		this.finalpath = finalpath;
		this.musicname = musicname;
		this.musictype = musictype;
		this.singername = singername;
	}
	/*
	public boolean checkExist(String musicname){
		boolean isexist = false;
		try{
			Context ic = new InitialContext();
	   	    Object obj = ic.lookup("UpEntityBean");
	   	    UpFileEntHome enthome = (UpFileEntHome)PortableRemoteObject.narrow(obj,UpFileEntHome.class);
	   	    UpFileEntRemote entremote = enthome.findByPrimaryKey(musicname);
	   	    if(entremote.getDbmusicname()!=null){
	   	    	isexist = true;
	   	    }
		}catch(Exception e){
			e.printStackTrace();
	           }
	   return isexist;   	
	}*/
	public boolean startEntBean(String finalpath ,String musicname,String musictype,String singername){
		boolean isexist = false;
		try{
			Context ic = new InitialContext();
	   	    java.lang.Object obj =  ic.lookup("UpEntityBean");
	   	    UpFileEntHome enthome = (UpFileEntHome)PortableRemoteObject.narrow(obj,UpFileEntHome.class);
	   	    UpFileEntRemote entremote = enthome.Create(finalpath,musicname,musictype,singername);
	   	    if(entremote.getPrimaryKey()!=null){
	   	    	isexist = true;
	   	    }
	   	    
		}catch(Exception e){
	           	e.printStackTrace();
	       }
	       return isexist;   	
	}
	public void ejbRemove() {}
    public void ejbActivate() {}
    public void ejbPassivate() {}
    public void setSessionContext(SessionContext context) {}

}