package com.didlink.db;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.ConnectionPoolDataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class MysqlDBManager {

	private static MysqlDBManager m_oDBManager = null;
	private static Object m_oLock = new Object();

	private static final Logger LOGGER = Logger.getLogger(MysqlDBManager.class
			.getName());
	private HashMap<String, MiniConnectionPoolManager> m_hmConnectionManagers = null;

	private static String DATASOURCE_DB_URL = "jdbc:mysql://localhost:3306/didlink";
	private static String DATASOURCE_USER_NAME = "root";
	private static String DATASOURCE_DECODED_PASSWORD = "root";
	private static int DATASOURCE_POOL_SIZE = 20;

	private String m_sDB_String;
	private String m_sDB_Integer;
	private String m_sDB_Date;
	private String m_sDB_Char;

	private MiniConnectionPoolManager poolMgr = null;

	public static MysqlDBManager getInstance()
			throws Exception {
		if (m_oDBManager == null) {
			synchronized (MysqlDBManager.class) {
				if (m_oDBManager == null)
					m_oDBManager = new MysqlDBManager();
			}
		}
		return m_oDBManager;
	}

	private MysqlDBManager() throws Exception {

		m_hmConnectionManagers = new HashMap<String, MiniConnectionPoolManager>();
	}

	public Connection getConnection(String sPoolName) throws Exception {

		if ((sPoolName == null) || (sPoolName.equals(""))) {
			throw new Exception("Invalid Pool Name[" + sPoolName + "]");
		}

		MiniConnectionPoolManager oMiniConnectionPoolManager = (MiniConnectionPoolManager) m_hmConnectionManagers
				.get(sPoolName);

		if (oMiniConnectionPoolManager == null) {
			synchronized (m_oLock) {

				oMiniConnectionPoolManager = (MiniConnectionPoolManager) m_hmConnectionManagers
						.get(sPoolName);

				if (oMiniConnectionPoolManager == null) {
					initDB(sPoolName);
				}

			}
		}

		oMiniConnectionPoolManager = (MiniConnectionPoolManager) m_hmConnectionManagers
				.get(sPoolName);

		return oMiniConnectionPoolManager.getConnection();
	}

	public void cleanup() throws Exception {
		poolMgr.dispose();

		LOGGER.info("Released DB Connections");
	}

	private void initDB(String sPoolName) throws SQLException, Exception {

		ConnectionPoolDataSource dataSource = null;

		dataSource = createMysqlDataSource();

		poolMgr = new MiniConnectionPoolManager(dataSource, DATASOURCE_POOL_SIZE);

		m_hmConnectionManagers.put(sPoolName, poolMgr);

		Connection conn = null;
		try {
			conn = poolMgr.getConnection();

		} catch (SQLException ex) {
			LOGGER.info("Could not connect to database::" + ex.getMessage());
			throw new Exception("Could not connect to database::"
					+ ex.getMessage());
		} finally {

			if (conn != null)
				conn.close();
		}
	}

	private ConnectionPoolDataSource createMysqlDataSource()
			throws SQLException, Exception {

		MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setURL(DATASOURCE_DB_URL);
		dataSource.setUser(DATASOURCE_USER_NAME);
		dataSource.setPassword(DATASOURCE_DECODED_PASSWORD);

		dataSource.setLogWriter(new PrintWriter(System.out));
		return dataSource;
	}

	public void setDBString(String m_sDB_String) {
		this.m_sDB_String = m_sDB_String;
	}

	public String getDBString() {
		return m_sDB_String;
	}

	public void setDBInteger(String m_sDB_Integer) {
		this.m_sDB_Integer = m_sDB_Integer;
	}

	public String getDBInteger() {
		return m_sDB_Integer;
	}

	public void setDBDate(String m_sDB_Date) {
		this.m_sDB_Date = m_sDB_Date;
	}

	public String getDBDate() {
		return m_sDB_Date;
	}

	public void setDBChar(String m_sDB_Char) {
		this.m_sDB_Char = m_sDB_Char;
	}

	public String getDBChar() {
		return m_sDB_Char;
	}
}
