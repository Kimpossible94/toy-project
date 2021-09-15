package com.kh.toy.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public class JDBCTemplate {

	// Singleton 패턴
	// 해당 클래스의 인스턴스가 하나만 생성되어야 할 때 사용하는 디자인 패턴
	// 1. 생성자를 private로 처리해 외부에서 JDBCTemplate의 생성을 차단
	// 2. 외부에서 JDBCTemplate의 인스턴스를 생성하지 않고도 사용할 수 있는 JDBCTemplate의 인스턴스를 반환하는 static
	// 메서드를 만든다.
	// (static 메서드 : 메모리에 올라가서 종료될 때 까지 내려오지않음)

	private static JDBCTemplate instance;
	private static PoolDataSource pds;

	// 1. oracle jdbc Driver를 JVM에 등록
	private JDBCTemplate() {
		// 사실 오라클 드라이버는 한 번 등록되면 종료되기 전까지는 계속 등록되어 있다.
		// 그래서 여기에서 한 번 등록해줌
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			final String DB_URL = "jdbc:oracle:thin:@pclassmdb_high?TNS_ADMIN=C:/CODE/Wallet_PCLASSMDB";
			// Use the TNS Alias name along with the TNS_ADMIN - For ATP and ADW
			// final static String
			// DB_URL="jdbc:oracle:thin:@dbname_alias?TNS_ADMIN=/Users/test/wallet_dbname";
			final String DB_USER = "ADMIN";
			final String DB_PASSWORD = "Semimm123123";
			final String CONN_FACTORY_CLASS_NAME = "oracle.jdbc.pool.OracleDataSource";
			
			pds = PoolDataSourceFactory.getPoolDataSource();
			pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
			pds.setURL(DB_URL);
			pds.setUser(DB_USER);
			pds.setPassword(DB_PASSWORD);
			pds.setConnectionPoolName("JDBC_UCP_POOL");
			
			// Default is 0. Set the initial number of connections to be created
			// when UCP is started.
			pds.setInitialPoolSize(5);
			
			// Default is 0. Set the minimum number of connections 
			// that is maintained by UCP at runtime.
			pds.setMinPoolSize(5);
			
			// Default is Integer.MAX_VALUE (2147483647). Set the maximum number of
			// connections allowed on the connection pool.
			pds.setMaxPoolSize(20);
			
			// Default is 30secs. Set the frequency in seconds to enforce the timeout
			// properties. Applies to inactiveConnectionTimeout(int secs),
			// AbandonedConnectionTimeout(secs)& TimeToLiveConnectionTimeout(int secs).
			// Range of valid values is 0 to Integer.MAX_VALUE. .
			pds.setTimeoutCheckInterval(5);
			
			// Default is 0. Set the maximum time, in seconds, that a
			// connection remains available in the connection pool.
			pds.setInactiveConnectionTimeout(10);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 2. 데이터베이스와 연결 수립
	public Connection getConnection() {
		Connection conn = null;
		
		try {
			conn = pds.getConnection();
			// jdbc는 자동으로 커밋/롤백을 한다.
			// 트랙잰션(commit, rollback)관리를 개발자가 하기 위해 Auto Commit 설정을 끄기
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;

	}

	// 3. commit / rollback
	public void commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 4. 시스템 자원(Connection, Statement, ResultSet) 반환
	public void close(Connection conn) {
		try {
			if (conn != null && conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(Statement stmt) {
		try {
			if (stmt != null && stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(ResultSet rset) {
		try {
			if (rset != null && rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(ResultSet rset, Statement stmt, Connection conn) {
		close(rset);
		close(stmt);
		close(conn);
	}

	public void close(Statement stmt, Connection conn) {
		close(stmt);
		close(conn);
	}

	public void close(ResultSet rset, Statement stmt) {
		close(rset);
		close(stmt);
	}

	// JDBCTemplate를 반환하기 위한 static 메서드
	public static JDBCTemplate getInstance() {
		// intance가 null이다는 한 번도 초기화 된 적이 없다.
		// 즉, 한 번도 사용된 적이 없다는 뜻 (static에 올라가 있지 않다.)
		if (instance == null) {
			instance = new JDBCTemplate();
		}

		return instance;
	}

}
