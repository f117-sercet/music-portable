package control;

import java.io.Serializable;


/**
 * �쳣����
 * @author 60221
 *
 */
public class ExceptionHandler extends Exception implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7244468830497246379L;

	public ExceptionHandler(String s){
		super(s);
	}
}


