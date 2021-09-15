package com.kh.toy.common.code.member;


//enum(enumerated type) : 열거형
//서로 연관된 상수들의 집합
//서로 연관된 상수들을 하나의 묶음으로 다루기 위한 enum만의 문법적 형식과 메서드를 제공해준다.
public enum UserGrade {
	ME00("일반", "user", 1),
	ME01("성실", "user", 2),
	ME02("우수", "user", 3),
	ME03("vip", "user", 4),
	
	AD00("super", "admin"),
	AD01("member", "admin"),
	AD02("board", "admin");
	
	public final String DESC;
	public final String ROLL;
	public final int extendableCnt;
	
	private UserGrade(String desc, String roll) {
		this.DESC = desc;
		this.ROLL = roll;
		this.extendableCnt = 9999;
	}
	
	private UserGrade(String desc, String roll, int extendableCnt) {
		this.DESC = desc;
		this.ROLL = roll;
		this.extendableCnt = extendableCnt;
	}
}
