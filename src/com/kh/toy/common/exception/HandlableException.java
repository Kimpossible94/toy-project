package com.kh.toy.common.exception;

import com.kh.toy.common.code.ErrorCode;

public class HandlableException extends RuntimeException{

	private static final long serialVersionUID = -3930409458039432875L;

	public ErrorCode error;
	
	//log를 남기지 않고 사용자에게 알림만 전달하는 용도의 생성자
	public HandlableException(ErrorCode error) {
		this.error = error;
		this.setStackTrace(new StackTraceElement[0]);
	}
	
	//log를 남기는 생성자
	public HandlableException(ErrorCode error, Exception e) {
		this.error = error;
		e.printStackTrace();
		this.setStackTrace(new StackTraceElement[0]);
	}
	
}
