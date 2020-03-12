package control.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @用于转换请求字符编码的filter
 * 
 */

public class CharacterEncoding implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		return false;}
	
		protected FilterConfig filterConfig = null;
		protected String encoding = "";
		
		public void init(FilterConfig filterConfig) throws ServletException
		{
			this.filterConfig = filterConfig;
			this.encoding = filterConfig.getInitParameter("encoding");
		}
		public void doFilter(final ServletRequest req,final ServletResponse
							resp, FilterChain chain) 
				throws IOException, ServletException
		{
			if(encoding != null)
				req.setCharacterEncoding(encoding);
			chain.doFilter(req, resp);
			return;
		}
		public void destroy()
		{
			filterConfig = null;
			encoding = null;
		}
		
	
	}
	


