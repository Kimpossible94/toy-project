package com.kh.toy.common.exception;

public class PageNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = -930316220873109881L;

	public PageNotFoundException() {
		//아래의 코드에는 원래 PageNotFoundException이 뜨기까지의 모든 에러발생 경로가 들어있는 배열이 StackTrace에 있다.
		// this.setStackTrace(stackTrace);
		// 그래서 StackTrace를 비워주기 위해 새로운 StackTraceElement를 만들어 보내준다.
		// 즉, StackTrace의 0번 배열에 있는 로그만 가져와서 StackTrace를 만들어 준 것.
		this.setStackTrace(new StackTraceElement[0]);
	}
	
	public PageNotFoundException(String message) {
		super(message);
	}
	
}
