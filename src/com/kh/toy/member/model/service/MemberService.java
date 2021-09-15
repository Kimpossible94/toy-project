package com.kh.toy.member.model.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.common.mail.MailSender;
import com.kh.toy.member.model.dao.MemberDao;
import com.kh.toy.member.model.dto.Member;



// *** 총정리 ***
// 1. Transaction관리를 Service 클래스가 담담하도록 코드를 수정
// 2. commit/rollback 메서드는 Connection 객체의 메서드이기 때문에 Transaction관리를 Service에서 하기 위해서 Connection 객체를 Service에서 생성함.
//		Connection의 생성, close처리, commit, rollback 처리를 Service에서만 관리!
// 3. DAO는 오직 Service가 생성한 Connection객체를 사용해 Database에 쿼리를 요청만 함
// 4. Transaction 관리를 Service에서 진행하기 위해서는 DAO의 메서드 수행 도중 문제가 발생했는지 Service가 알아야 rollback을 할지 commit을 할지 판단 할 수 있음.
// 5. DAO에서 SQLException이 발생하면 DAO에서 처리하는 것이 아니라 Service에게 throws해 예외가 발생했음을 Service에게 알림
//		여기까지만 해도 transaction은 완벽하게 돌아가지만, SQLException은 예외처리가 강제되는 checkedException이기 때문에
//		DAO를 호출한 모든 Service 메서드에서 딱히 예외처리를 할 수 있는 사항이 없음에도 불구하고, SQLException에 대한 예외처리가 강제되었다.
// 6. 그래서 SQLException이 발생 했을 때 해당 예외를 UnCheckedException인 DataAccessException으로 변환해서 throws처리하고
//		Service 단에서는 예외처리가 필요한 메서드에서만 예외처리 코드를 작성할 수 있게 되었음.


public class MemberService {
	
	private MemberDao memberDao = new MemberDao();
	private JDBCTemplate template = JDBCTemplate.getInstance();
	
	public Member memberAuthenticate(String userId, String password) {
		Member member = null;
		
		// connection의 생성과 종료도 서비스단에서 해주어야함
		// 서비스에서 트랜잭션(커밋/롤백)을 하기위해 서비스단에서 connection을 해줌
		Connection conn = template.getConnection();
		
		try {
			// 생성한 connection을 dao에 전달해줌
			member = memberDao.memberAuthenticate(userId, password, conn);
		} finally {
			template.close(conn);
		}
		return member;
	}

	public Member selectMemberById(String userId){
		Connection conn = template.getConnection();
		Member member = null;
		
		try {
			member = memberDao.selectMemberById(userId, conn);
		} finally {
			template.close(conn);
		}
		
		return member;
	}
	
	public List<Member> selectMemberList() {
		List<Member> memberList = null;
		Connection conn = template.getConnection();
		
		try {
			memberList = memberDao.selectMemberList(conn);
		} finally {
			template.close(conn);
		}
		
		return memberList;
	}
	
	public int insertMember(Member member) {
		Connection conn = template.getConnection();
		int res = 0;
		
		// memberDao.insertMember가 제대로 수행되면 commit을 하고
		// 제대로 수행되지 않으면 memberDao.insertMember에서 throws를 해 다시 이곳 insertMember로 넘어와 catch블럭으로 예외 처리를 해준다.
		// 예외 발생 여부와 상관없이 Connection은 닫혀야 하므로 finally 블럭으로 닫아준다.
		try {
			res = memberDao.insertMember(member, conn);
			// 새로운 멤버 등록이후 자동 로그인처리 하려한다.
			// 방금 가입한 회원의 아이디로 정보를 다시 조회
			// memberDao.selectMemberById(member.getUserId());
			// 다오를 통해 사용자 정보를 받아 해당 정보로 로그인 처리 진행
			// 이러한 로직으로 작동할 탠데, memberDao.selectMemberById에서 SQLException이 발생한다면, SQLException을 거기서 처리할 것이다.
			// 그래서 insertMember에서는 에러가 발생했지만 발생하지 않은것처럼 작동해 rollback를 하지 않는다는 문제가 생긴다.
			// 그렇기 때문에, selectMemberById에서도 throws를 넣고 서비스단에서 catch를 해준다.
			// 이러한 checked exception(강제 예외처리)을 해줘야 하는 귀찮음때문에 unchecked exception을 사용하기 시작했다. (common.exception)
			
			Member m = memberDao.selectMemberById(member.getUserId(), conn);
			
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			throw e;
		} finally {
			template.close(conn);
		}
		
		return res;
	}

	public int changeMemberPw(Member member) {
		int res = 0;
		Connection conn = template.getConnection();
		
		try {
			res = memberDao.changeMemberPw(member, conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			throw e;
		} finally {
			template.close(conn);
		}
		return res;
	}
	
	public int deleteMember(Member member) {
		Connection conn = template.getConnection();
		int res = 0;
		try {
			res = memberDao.deleteMember(member, conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			throw e;
		} finally {
			template.close(conn);
		}
		return res;
	}

	public void authenticateEmail(Member member, String psersistToken) {
		HttpConnector conn = new HttpConnector();
		
		String queryString = conn.urlEncodedForm(RequestParams.builder()
													.param("mail-template", "join-auth-email")
													.param("psersistToken", psersistToken)
													.param("userId",  member.getUserId())
													.build());
		
		String mailTemplate = conn.get("http://localhost:7070/mail?" + queryString);
		MailSender mailSender = new MailSender();
		mailSender.sendEmail(member.getEmail(), "환영합니다 " + member.getUserId() + "님", mailTemplate);

	}
}
