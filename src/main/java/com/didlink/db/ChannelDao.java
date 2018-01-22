package com.didlink.db;

import com.didlink.rest.bean.Channel;
import com.didlink.rest.bean.LoginAuth;
import com.mysql.cj.api.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelDao {

    private static final Logger LOGGER = Logger
            .getLogger(ChannelDao.class.getName());

    public Channel add(Channel channel) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = getConnection();
            String sQry = "insert into CHANNELS(UID,TYPE,STATUS,NAME,DESCRIPTION) values values (?,?,?,?,?)";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, channel.getUid());
            statement.setByte(2, channel.getType());
            statement.setByte(3, channel.getStatus());
            statement.setString(4, channel.getName());
            statement.setString(5, channel.getDescription());

            statement.executeUpdate();

            long key = -1L;
            ResultSet rs = statement.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = rs.getLong(1);
            }

            Channel newCh = new Channel();
            newCh.setChid(key);
            newCh.setType(channel.getType());
            newCh.setStatus(channel.getStatus());
            newCh.setName(channel.getName());
            newCh.setDescription(channel.getDescription());

            return newCh;

        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "ERROR saving Preview Record", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public Channel findById(long chid) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Channel> channels = new ArrayList<>();

        try {
            con = getConnection();
            String sQry = "select CHID,UID,TYPE,STATUS,NAME,DESCRIPTION from CHANNELS where CHID=?";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setLong(1, chid);


            resultSet = statement.executeQuery();

            Channel channel = new Channel();
            if (resultSet.first()) {
               channel.setChid(resultSet.getLong("CHID"));
                channel.setUid(resultSet.getLong("UID"));
                channel.setType(resultSet.getByte("TYPE"));
                channel.setStatus(resultSet.getByte("STATUS"));
                channel.setName(resultSet.getString("NAME"));
                channel.setDescription(resultSet.getString("DESCRIPTION"));

                channels.add(channel);
            }

            return channel;

        } catch (Exception ex) {

            LOGGER.log(Level.INFO, "ERROR saving Preview Record", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public List<Channel> findByName(String name) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Channel> channels = new ArrayList<>();

        try {
            con = getConnection();
            String sQry = "select CHID,UID,TYPE,STATUS,NAME,DESCRIPTION from CHANNELS where NAME like ?";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setString(1, "%" + name + "%");


            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Channel channel = new Channel();
                channel.setChid(resultSet.getLong("CHID"));
                channel.setUid(resultSet.getLong("UID"));
                channel.setType(resultSet.getByte("TYPE"));
                channel.setStatus(resultSet.getByte("STATUS"));
                channel.setName(resultSet.getString("NAME"));
                channel.setDescription(resultSet.getString("DESCRIPTION"));

                channels.add(channel);
            }

            return channels;

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
