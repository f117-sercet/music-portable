package ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
/**
 * @有状态SessionBean的remote接口
 */
public interface UpFileRemote extends EJBObject{
	public boolean startEntBean(String finalpath ,String musicname,String musictype,String singername)
				throws RemoteException;
	
}
