package com.kh.toy.common.mail.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mail")
public class MailHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    public MailHandler() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//MailHandler에 mail-template를 요청 할 때, 원하는 template 이름을 요청 파라미터로 전송
		//MailHandler는 해당 template로 요청을 재지정
		String template = request.getParameter("mail-template");
		request.getRequestDispatcher("/mail-template/" + template).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
