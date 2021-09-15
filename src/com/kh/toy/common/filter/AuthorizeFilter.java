package com.kh.toy.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.code.member.UserGrade;
import com.kh.toy.common.exception.HandlableException;
import com.kh.toy.member.model.dto.Member;

public class AuthorizeFilter implements Filter {

    public AuthorizeFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String[] uriArr = httpRequest.getRequestURI().split("/");
		
		if(uriArr.length != 0) {
			switch (uriArr[1]) {
				case "member":
					memberAuthorize(httpRequest, httpResponse, uriArr);
					break;
				case "admin":
					adminAuthorize(httpRequest, httpResponse, uriArr);
					break;
				case "board":
					boardAuthrize(httpRequest, httpResponse, uriArr);
					break;
				default:
					break;
			}
		}
		
		chain.doFilter(request, response);
	}
	
	private void boardAuthrize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) {
		
		Member member = (Member) httpRequest.getSession().getAttribute("authentication");
		
		switch (uriArr[2]) {
		case "board-form":
			if(member == null) {
				throw new HandlableException(ErrorCode.UNAUTHRIZED_PAGE);
			}
			break;
		case "upload":
			if(member == null) {
				throw new HandlableException(ErrorCode.UNAUTHRIZED_PAGE);
			}
			break;
		default:
			break;
		}
		
	}

	private void adminAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) {
		
		Member member = (Member) httpRequest.getSession().getAttribute("authentication");
		
		
		if(member == null || UserGrade.valueOf(member.getGrade()).ROLL == "user") {
			throw new HandlableException(ErrorCode.UNAUTHRIZED_PAGE);
		}
		
		UserGrade adminGrade = UserGrade.valueOf(member.getGrade());
		
		//super일 경우에는 접근제한안해도 되므로 바로 리턴
		if(adminGrade.DESC.equals("super")) {
			return;
		}
		
		switch (uriArr[2]) {
		case "member":
			//회원과 관련된 관리를 수행하는 admin 확인
			if(!adminGrade.DESC.equals("member")) {
				throw new HandlableException(ErrorCode.UNAUTHRIZED_PAGE);
			}
			break;
		case "board":
			//게시판과 관련된 관리를 수행하는 admin 확인
			if(!adminGrade.DESC.equals("board")) {
				throw new HandlableException(ErrorCode.UNAUTHRIZED_PAGE);
			}
			break;
		default:
			break;
		}
	}

	private void memberAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) throws ServletException, IOException {
		String serverToken = (String) httpRequest.getSession().getAttribute("persist-token");
		String clientToken = httpRequest.getParameter("persist-token");
		
		switch (uriArr[2]) {
		case "join-impl":
			if(serverToken == null || !serverToken.equals(clientToken)){
				throw new HandlableException(ErrorCode.AUTHENTICATION_FAILED_ERROR);
			}
			break;
		case "mypage":
			if(httpRequest.getSession().getAttribute("authentication") == null){
				throw new HandlableException(ErrorCode.REDIRECT_TO_LOGING_PAGE);
			}
			break;
		default:
			break;
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
