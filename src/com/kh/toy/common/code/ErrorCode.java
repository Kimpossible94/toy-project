package com.kh.toy.common.code;

public enum ErrorCode {

	DATABASE_ACCESS_ERROR("데이터베이스와 통신 중 에러가 발생하였습니다."),
	FAILED_VALIDATED_ERROR("데이터의 양식이 적합하지 않습니다."),
	MAIL_SEND_FAIL_ERROR("이메일 발송 중 에러가 발생하였습니다."),
	HTTP_CONNEECT_ERROR("HTTP 통신 중 에러가 발생하였습니다."),
	AUTHENTICATION_FAILED_ERROR("유효하지 않은 인증입니다."),
	UNAUTHRIZED_PAGE("접근 권한이 없는 페이지 입니다."),
	REDIRECT_TO_LOGING_PAGE("","/member/login-form"),
	FAILED_FILE_UPLOAD_ERROR("파일업로드에 실패했습니다.");
	
	public final String MSG;
	public String URL = "/index";
	
	ErrorCode(String msg) {
		this.MSG = msg;
		this.URL = "/index";
	}
	
	ErrorCode(String msg, String url){
		this.MSG = msg;
		this.URL = url;
	}
	
}
