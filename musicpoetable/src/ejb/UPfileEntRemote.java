package ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
/**
 *entity bean remote interface
 */
public interface UpFileEntRemote extends EJBObject{
	String getDbmusicname()throws RemoteException;
}