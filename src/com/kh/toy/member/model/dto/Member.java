package com.kh.toy.member.model.dto;


import java.sql.Date;


// DTO(DATA TRANFER OBJECT)
// 데이터 전송 객체
// 데이터베이스로부터 얻어 온 데이터를 service(비지니스로직)으로 보낼 때 사용하는 객체
// 비지니스 로직을 포함하고 있지 않은, 순수하게 데이터 전송만을 위한 객체
// 그렇기 때문에 기본적으로 가질 수 있는 메서드는 필수적으로 getter/setter 그리고 필요에 따라 hashCode, toString, equals 메서드만을 가질 수 있다.

// 참고 ***
// 	DTO는 도메인 객체이다.
// 	도메인 객체 : 데이터베이스 테이블에서 조회해 온 한 행(row)의 값을 저장하는 용도로 사용하는 객체
// 		종류 : DOMAIN OBEJCT, VALUE OBJECT(VO), DTO, ENTITY, BEAN

// DTO의 조건 (JAVA BEAN 규약)
// 1. 모든 필드변수는 PRIVATE일 것( 캡슐화 )
// 2. 반드시 기본 생성자가 존재할 것. (매개변수가 있는 생성자가 있더라도, 기본 생성자는 있어야 한다.)
// 3. 모든 필드 변수는 getter/setter 메서드를 가져야 한다.


// 오라클 - 자바 타입 매핑
// CHAR, VARCHAR2 -> String
// DATA -> java.util.date, java.sql.Date
// NUMBER -> int, double

public class Member {

//	USER_ID	VARCHAR2(36 CHAR)
//	PASSWORD	VARCHAR2(60 CHAR)
//	EMAIL	VARCHAR2(50 CHAR)
//	GRADE	CHAR(4 CHAR)
//	TELL	VARCHAR2(15 CHAR)
//	REG_DATE	DATE
//	RENTABLE_DATE	DATE
//	IS_LEAVE	NUMBER
	
	private String userId;
	private String password;
	private String email;
	private String grade;
	private String tell;
	private Date regDate;
	private Date rentableDate;
	private int isLeave;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getTell() {
		return tell;
	}
	public void setTell(String tell) {
		this.tell = tell;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getRentableDate() {
		return rentableDate;
	}
	public void setRentableDate(Date rentableDate) {
		this.rentableDate = rentableDate;
	}
	public int getIsLeave() {
		return isLeave;
	}
	public void setIsLeave(int isLeave) {
		this.isLeave = isLeave;
	}
	
	@Override
	public String toString() {
		// ME00 ME01이런 것은 우리만 알아볼 수 있고 사용자는 모른다.
		// 그러므로 사용자가 알아볼 수 있게 바꿔줘보자.
		
		// 이렇게 switch 문으로 사용자가 알아볼 수 있게 바꿀수 있지만 우리가 만드는 기능은 여러가지 이기때문에 추후에 문제가 생길 수 있다.
//		String info = "";
//		switch (this.grade) {
//		case "ME00" : info = "일반"; 
//			break;
//		case "ME01" : info = "성실"; 
//			break;
//		case "ME02" : info = "우수"; 
//			break;
//		case "ME03" : info = "vip"; 
//			break;
//		}
		
		//다른 방법으로는 변수만 있는 클래스(common.code.Code)를 만들어서 적용하는 방법이다.
		//하지만 이런 방식으로 하는 것도 우리가 원할 때 값을 유동적으로 변경할 수 없다는 것이다. 즉, 코드가 100개라면 스위치케이스문이 100개가 필요한 것이다
//		String info = "";
//		switch (this.grade) {
//		case "ME00" : info = Code.ME00; 
//			break;
//		case "ME01" : info = Code.ME01; 
//			break;
//		case "ME02" : info = Code.ME02; 
//			break;
//		case "ME03" : info = Code.ME03; 
//			break;
//		}
		
		// *** 따라서 위처럼의 방법이 아닌 Enum이라는 것을 이용한다.(common.code.MemberGrade)
		
		return "Member [userId=" + userId + ", password=" + password + ", email=" + email + ", grade=" + grade
				+ ", tell=" + tell + ", regDate=" + regDate + ", rentableDate=" + rentableDate + ", isLeave=" + isLeave
				+ "]";
	}
	
}
