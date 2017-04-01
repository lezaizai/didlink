package com.didlink.db;

import com.didlink.app.AppServer;
import com.didlink.rest.bean.LoginAuth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    public LoginAuth saveUser(String username, String password) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        LoginAuth loginAuth = null;

        try {
            con = getConnection();
            String sQry = "insert into USER(USERNAME,PASSWORD) values (?,?)";

            System.out.println("Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();
            loginAuth = new LoginAuth();
            loginAuth.setStatus(true);
            loginAuth.setUsername(username);
            return loginAuth;
        } catch (Exception ex) {

            //LOGGER.log(Level.INFO, "ERROR saving Preview Record", ex);
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

            String sQry = "select USERNAME,PASSWORD from USER where USERNAME = ?";

            System.out.println("Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setString(1, username);


            resultSet = statement.executeQuery();

            if (resultSet.first()) {
                loginAuth = new LoginAuth();
                loginAuth.setUsername(resultSet.getString("USERNAME"));
                loginAuth.setPassword(resultSet.getString("PASSWORD"));
            }

            return loginAuth;

        } catch (Exception ex) {

            //LOGGER.log(Level.INFO, "ERROR saving Preview Record", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public Connection getConnection() throws Exception {
        Connection con = null;
        try {

            con = AppServer.getJdbcDatabase().getConnection();

        } catch (Exception ex) {
            System.out.println("ERROR getting DB Connection" + ex);
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
                        System.out.println("DB ResultSet Closed");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (statement != null) {
                    statement.close();
                    System.out.println("DB Statement Closed");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR Closing DB Connection" + ex);
        } finally {
            if (connection != null) {
                connection.close();
                System.out.println("DB Connection Closed");
            }

        }

    }

}
