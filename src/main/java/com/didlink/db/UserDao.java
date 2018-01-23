package com.didlink.db;

import com.didlink.rest.bean.LoginAuth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {

    private static final Logger LOGGER = Logger
            .getLogger(UserDao.class.getName());

    public LoginAuth saveUser(String username, String password) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        LoginAuth loginAuth = null;

        try {
            con = getConnection();
            String sQry = "insert into USER(USERNAME,PASSWORD) values (?,?)";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();
            return findUser(username);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "ERROR saving Preview Record", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public LoginAuth findUser(String username) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        LoginAuth loginAuth = null;

        try {
            con = getConnection();
            String sQry = "select UID,STATUS,USERNAME,NICKNAME,PASSWORD from USER where USERNAME = ?";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setString(1, username);


            resultSet = statement.executeQuery();

            if (resultSet.first()) {
                loginAuth = new LoginAuth();
                loginAuth.setUid(resultSet.getLong("UID"));
                loginAuth.setStatus(resultSet.getByte("STATUS"));
                loginAuth.setUsername(resultSet.getString("USERNAME"));
                loginAuth.setPassword(resultSet.getString("PASSWORD"));
            }

            return loginAuth;

        } catch (Exception ex) {

            LOGGER.log(Level.INFO, "ERROR saving Preview Record", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public LoginAuth findUserById(long uid) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        LoginAuth loginAuth = null;

        try {
            con = getConnection();
            String sQry = "select UID,STATUS,USERNAME,NICKNAME,PASSWORD from USER where UID = ?";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setLong(1, uid);


            resultSet = statement.executeQuery();

            if (resultSet.first()) {
                loginAuth = new LoginAuth();
                loginAuth.setUid(resultSet.getLong("UID"));
                loginAuth.setStatus(resultSet.getByte("STATUS"));
                loginAuth.setUsername(resultSet.getString("USERNAME"));
                loginAuth.setPassword(resultSet.getString("PASSWORD"));
            }

            return loginAuth;

        } catch (Exception ex) {

            LOGGER.log(Level.INFO, "ERROR saving Preview Record", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public Connection getConnection() throws Exception {

        Connection con = null;
        try {

            con = MysqlDBManager.getInstance().getConnection(
                    "DIDLINK");

            LOGGER.finest("DB Connection Opened");
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "ERROR getting DB Connection", ex);

            throw ex;
        }
        return con;
    }

    public void closeConnection(Connection connection,
                                PreparedStatement statement, ResultSet resultSet) throws Exception {

        try {
            try {
                try {
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (resultSet != null) {
                        resultSet.close();
                        LOGGER.finest("DB ResultSet Closed");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (statement != null) {
                    statement.close();
                    LOGGER.finest("DB Statement Closed");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.INFO, "ERROR Closing DB Connection", ex);
        } finally {
            if (connection != null) {
                connection.close();
                LOGGER.finest("DB Connection Closed");
            }

        }

    }

}
