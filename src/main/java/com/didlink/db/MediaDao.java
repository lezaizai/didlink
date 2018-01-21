package com.didlink.db;

import com.didlink.app.AppServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaDao {

    private static final Logger LOGGER = Logger
            .getLogger(MediaDao.class.getName());

    public void saveMedia() throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        try {

            con = getConnection();

            String sQry = "insert into MEDIA(SOURCE,PATH) values (?,?)";

            System.out.println("Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setString(1, "iphone");
            statement.setString(2, "dcim/IODFD198444.JPG");

            statement.executeUpdate();

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
