package com.didlink.db;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.Statement;

public class JdbcDatabase {
    private JdbcConnectionPool cp = null;
    private static String url = "jdbc:h2:file:./didlink;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static String user = "sa";
    private static String password = "sa";
    private static int maxConnections = 20;

    public JdbcDatabase() throws Exception {
        cp = JdbcConnectionPool.create(url, user, password);
        cp.setMaxConnections(maxConnections);

        Connection con = null;
        Statement stmt = null;
        try {
            con=cp.getConnection();
            stmt=con.createStatement();
            stmt.execute("create sequence if not exists MEDIA_ID");
            stmt.execute("create table if not exists MEDIA(ID bigint default MEDIA_ID.nextval primary key, SOURCE varchar(255), PATH varchar(255), TIME timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("create table if not exists USER(USERNAME varchar(50), PASSWORD varchar(255), TIME timestamp DEFAULT CURRENT_TIMESTAMP)");
        } finally {
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();
        }
    }

    public Connection getConnection() throws Exception{
        return cp.getConnection();
    }

    public void close() {
        if (cp != null) {
            cp.dispose();
        }
    }
}
