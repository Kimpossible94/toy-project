package com.kh.toy.common.exception.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.common.exception.HandlableException;

@WebServlet("/exception-handler")
public class ExceptionHandler extends HttpServlet {
	//DataAccessException으로 발생한 에러를 web.xml을 통해 ExceptionHandler로 요청을 던질 수 있다.
	private static final long serialVersionUID = 1L;
       
    public ExceptionHandler() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//servlet container는 HandlableException이 발생하면 web.xml을 통해 요청을 HandlerException으로 재지정
		//이때 ExceptionHandler 서비스 메서드로 넘겨주는 request의 속성(javax.servlet.error.exception)에 발생한 예외 객체를 함께 넘겨준다.
		//즉, 아래처럼 예외 객체를 꺼내서 쓸 수 있다.
		
		HandlableException e = (HandlableException) request.getAttribute("javax.servlet.error.exception");
		
		System.out.println(e);
		
		request.setAttribute("msg", e.error.MSG);
		request.setAttribute("url", e.error.URL);
		request.getRequestDispatcher("WEB-INF/views/error/result.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
