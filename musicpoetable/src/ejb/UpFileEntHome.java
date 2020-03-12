package ejb;
/**
 * 实体beanHome接口
 */
import java.rmi.RemoteException;

public interface UpFileEntHome extends EJBHome{

	UpFileEntRemote Create(String finalpath,String musicname,
			String musictype,String singername) 
					throws CreateException;	
	UpFileEntRemote findByPrimaryKey(String musicname) throws FinderException, RemoteException;
}