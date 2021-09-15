package com.kh.toy.member.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.member.model.dto.Member;

public class MemberDao {

	// 드라이버를 등록함 !
	JDBCTemplate template = JDBCTemplate.getInstance();

	public MemberDao() {
	}

	// 사용자의 아이디와 password를 전달 받아서
	// 아이디와 password가 일치하는 회원을 조회하는 메서드
	public Member memberAuthenticate(String userId, String password, Connection conn) {
		Member member = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;

		try {
			String query = "select * from member where user_id = ? and password = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, userId);
			pstm.setString(2, password);
			rset = pstm.executeQuery();

			if (rset.next()) {
				member = convertRowToMember(rset);
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(rset, pstm);
		}

		return member;
	}

	// 회원검색 기능 만들기
	public Member selectMemberById(String userId, Connection conn) {
		Member member = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;

		String query = "select * from member where user_id like '%'||?||'%'";

		try {
			pstm = conn.prepareStatement(query);	
			pstm.setString(1, userId);
			rset = pstm.executeQuery();

			if (rset.next()) {
				member = convertRowToMember(rset);
			}

		} catch (SQLException e) {
			// SQLException이 발생하면 RuntimeException이 우리가 만든 DataAccessExcetion을 던진다.
			// 예외처리가 강제되지 않는다는 장점이 있다.
			throw new DataAccessException(e);
		} finally {
			template.close(rset, pstm);
		}

		return member;
	}

	// 전체 회원 조회 메서드
	public List<Member> selectMemberList(Connection conn) {
		List<Member> memberList = new ArrayList<Member>();
		PreparedStatement pstm = null;
		ResultSet rset = null;

		try {
			String query = "select * from member";
			pstm = conn.prepareStatement(query);
			rset = pstm.executeQuery();

			while (rset.next()) {
				Member member = convertRowToMember(rset);
				memberList.add(member);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(rset, pstm);
		}

		return memberList;
	}

	public int insertMember(Member member, Connection conn) {
		int res = 0;
		PreparedStatement pstm = null;

		try {
			String query = "insert into member(user_id, password, email, tell) values(?,?,?,?)";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, member.getUserId());
			pstm.setString(2, member.getPassword());
			pstm.setString(3, member.getEmail());
			pstm.setString(4, member.getTell());

			res = pstm.executeUpdate();

		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}
		return res;
	}

	public int changeMemberPw(Member member, Connection conn) {
		int res = 0;
		PreparedStatement pstm = null;
		String query = "update member set password = ? where user_id = ?";

		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1, member.getPassword());
			pstm.setString(2, member.getUserId());
			res = pstm.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}

		return res;
	}

	public int deleteMember(Member member, Connection conn) {
		int res = 0;
		PreparedStatement pstm = null;
		String query = "delete from member where user_id = ?";
		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1, member.getUserId());
			res = pstm.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}

		return res;
	}

	private Member convertRowToMember(ResultSet rset) throws SQLException {
		Member member = new Member();
		member.setUserId(rset.getString("user_id"));
		member.setPassword(rset.getString("password"));
		member.setGrade(rset.getString("grade"));
		member.setTell(rset.getString("tell"));
		member.setRegDate(rset.getDate("reg_date"));
		member.setEmail(rset.getString("email"));
		member.setRentableDate(rset.getDate("rentable_date"));
		member.setIsLeave(rset.getInt("is_leave"));

		return member;
	}

}
