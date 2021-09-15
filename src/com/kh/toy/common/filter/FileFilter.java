package com.kh.toy.common.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileFilter implements Filter {

    public FileFilter() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String originFileName = URLEncoder.encode(httpRequest.getParameter("originName"),"UTF-8");
		
		if(originFileName != null) {
			URLEncoder.encode(httpRequest.getParameter("originName"),"UTF-8");
			httpResponse.setHeader("Content-Disposition", "attachment" + originFileName);
		}
		chain.doFilter(request, httpResponse);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
