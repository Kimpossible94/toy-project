package com.kh.toy.member.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.member.model.dto.Member;
import com.kh.toy.member.model.service.MemberService;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	public MemberController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] uriArr = request.getRequestURI().split("/");

		switch (uriArr[uriArr.length - 1]) {
		case "login-form":
			loginForm(request, response);
			break;
		case "login":
			login(request, response);
			break;
		case "logout":
			logout(request, response);
			break;
		case "join-form":
			joinForm(request, response);
			break;
		case "join":
			join(request, response);
			break;
		case "join-impl":
			joinImpl(request, response);
			break;
		case "id-check":
			checkID(request, response);
			break;
		case "mypage":
			mypage(request, response);
			break;
		default:
			throw new PageNotFoundException();
		}

	}

	private void mypage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/member/mypage");
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("authentication");
		response.sendRedirect("/");
	}

	private void joinImpl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		memberService.insertMember((Member) session.getAttribute("persistUser"));

		// 같은 persisUser값이 두 번 DB에 입력되지 않도록 사용자 정보와 인증을 만료시킴
		session.removeAttribute("persistUser");
		session.removeAttribute("persist-token");
		response.sendRedirect("/member/login-form");

	}

	private void checkID(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("userId");

		Member member = memberService.selectMemberById(userId);

		if (member == null) {
			response.getWriter().print("available");
		} else {
			response.getWriter().print("disable");
		}

	}

	private void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String tell = request.getParameter("tell");
		String email = request.getParameter("email");

		Member member = new Member();
		member.setUserId(userId);
		member.setPassword(password);
		member.setTell(tell);
		member.setEmail(email);

		String psersistToken = UUID.randomUUID().toString(); // random하게 유니크ID를 생성해줌(절대중복 될 일 없음)


		request.getSession().setAttribute("persistUser", member);
		request.getSession().setAttribute("psersistToken", psersistToken);

		memberService.authenticateEmail(member, psersistToken);
		
		request.setAttribute("msg", "회원 가입을 위한 이메일이 발송되었습니다.");
		request.setAttribute("url", "/member/login-form");
		request.getRequestDispatcher("/common/result").forward(request, response);
	}

	private void joinForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/member/join-form").forward(request, response);

	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");

		Member member = null;

		member = memberService.memberAuthenticate(userId, password);
		// 예외처리 부분
		// 1. 시스템(웹어플리케이션 내부)에서 문제가 생겼을 때 (ex : DB가 뻗음, 외부 API서버가 죽었다)
		// 예외를 service 단에서 처리

		/*
		 * try { member = memberService.memberAuthenticate(userId, password); } catch
		 * (Exception e) { request.setAttribute("msg", "회원정보를 조회하는 도중 예외가 발생했습니다.");
		 * request.setAttribute("back", "back");
		 * request.getRequestDispatcher("/common/result").forward(request, response);;
		 * return; }
		 */

		// 2. 사용자가 잘못된 입력값(아이디, 비밀번호 등)을 입력했을 때
		// 사용자에게 아이디나 비밀번호가 틀렸음을 알리고, login-form으로 redirect
		// 1번의 예외처리가 완벽하게 처리됬을 때만 처리
		if (member == null) {
			response.sendRedirect("/member/login-form?err=1");
			return;
		}

		request.getSession().setAttribute("authentication", member);
		response.sendRedirect("/index");

	}

	private void loginForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/member/login").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
