package com.kh.toy.common.code;

public enum Config {

	//DOMAIN("http://www.pclass.com"),
	DOMAIN("http://localhost:7070"),
	SMTP_AUTHENTICATION_ID("dudqja115@gmail.com"),
	SMTP_AUTHENTICATION_PW("a"),
	COMPANY_EMAIL("dudqja115@gmail.com"),
	
	//UPLOAD_PATH("운영서버 경로이름"), 운영서버용 경로
	UPLOAD_PATH("C:\\CODE\\upload\\"); //개발서버용 경로
	
	public final String DESC;
	
	private Config(String desc) {
		this.DESC = desc;
	}
	
}
