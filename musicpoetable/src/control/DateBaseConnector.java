package control;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * ¿ØÖÆÊý¾Ý¿â
 */
import java.util.Properties;

public class DateBaseConnector {
	
	private String url = null;
	private Properties info = null;

	public DateBaseConnector() {
		   info = new Properties();
		   info.setProperty("proxool.maximum-connection-count", "20");
		   info.setProperty("proxool.house-keeping-test-sql", "select CURRENT_DATE");
		   info.setProperty("user", "music");
		   info.setProperty("password", "music");
		   String alias = "db";
		   String driverClass = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		   String driverUrl = "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=PowerMusic";
		   url = "proxool." + alias + ":" + driverClass + ":" + driverUrl;
		   try {
		       Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
		   } catch(Exception e) {}
		
		   }	
		public Connection getConnection() {
		    Connection conn = null;
		    try{
		        conn = DriverManager.getConnection(url, info);
		    } catch(Exception e) {}
		    return conn;
		}
	}

