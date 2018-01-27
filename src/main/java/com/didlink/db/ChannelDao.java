package com.didlink.db;

import com.didlink.rest.bean.Channel;
import com.didlink.rest.bean.Contact;
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
    private static byte TYPE = 0;
    private static byte STATUS = 0;

    public Channel add(Channel channel) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = getConnection();
            String sQry = "insert into CHANNELS(UID,TYPE,STATUS,NAME,CONTACTS_NUM,DESCRIPTION) values (?,?,?,?,?,?)";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, channel.getOwner().getUid());
            statement.setByte(2, channel.getType());
            statement.setByte(3, channel.getStatus());
            statement.setString(4, channel.getName());
            statement.setInt(5, channel.getContacts_num());
            statement.setString(6, channel.getDescription());

            statement.executeUpdate();

            long key = -1L;
            ResultSet rs = statement.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = rs.getLong(1);
            }

            String sQry_1 = "insert into CHANNEL_USERS(CHID,UID,TYPE,STATUS) values (?,?,?,?)";

            LOGGER.log(Level.INFO, "Save Qry::[" + sQry_1 + "]");

            statement = con.prepareStatement(sQry_1, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, key);
            statement.setLong(2, channel.getOwner().getUid());
            statement.setByte(3, TYPE);
            statement.setByte(4, STATUS);

            statement.executeUpdate();

            Channel newCh = new Channel();
            newCh.setChid(key);
            newCh.setType(channel.getType());
            newCh.setStatus(channel.getStatus());
            newCh.setName(channel.getName());
            newCh.setDescription(channel.getDescription());
            newCh.setContacts_num(0);

            Contact owner = channel.getOwner();
            newCh.setOwner(owner);
            newCh.addContact(owner);

            return newCh;

        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "ERROR saving channel_users", ex);
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
            String sQry = "select CHID,UID,TYPE,STATUS,NAME,CONTACTS_NUM,DESCRIPTION from CHANNELS where CHID=?";

            LOGGER.log(Level.INFO, "Search Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setLong(1, chid);


            resultSet = statement.executeQuery();

            Channel channel = new Channel();
            if (resultSet.first()) {
               channel.setChid(resultSet.getLong("CHID"));
                channel.setOwner(getOwner(resultSet.getLong("UID")));
                channel.setType(resultSet.getByte("TYPE"));
                channel.setStatus(resultSet.getByte("STATUS"));
                channel.setName(resultSet.getString("NAME"));
                channel.setContacts_num(resultSet.getInt("CONTACTS_NUM"));
                channel.setDescription(resultSet.getString("DESCRIPTION"));

                channels.add(channel);
            }

            return channel;

        } catch (Exception ex) {

            LOGGER.log(Level.INFO, "ERROR searching channels", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public List<Channel> getSimpleList(String name) throws Exception {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Channel> channels = new ArrayList<>();

        try {
            con = getConnection();
            String sQry = "select CHID,UID,TYPE,STATUS,NAME,CONTACTS_NUM,DESCRIPTION from CHANNELS where NAME like ?";

            LOGGER.log(Level.INFO, "Search Qry::[" + sQry + "]");

            statement = con.prepareStatement(sQry);

            statement.setString(1, "%" + name + "%");


            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Channel channel = new Channel();
                channel.setChid(resultSet.getLong("CHID"));
                channel.setOwner(getOwner(resultSet.getLong("UID")));
                channel.setType(resultSet.getByte("TYPE"));
                channel.setStatus(resultSet.getByte("STATUS"));
                channel.setName(resultSet.getString("NAME"));
                channel.setContacts_num(resultSet.getInt("CONTACTS_NUM"));
                channel.setDescription(resultSet.getString("DESCRIPTION"));

                channels.add(channel);
            }

            return channels;

        } catch (Exception ex) {

            LOGGER.log(Level.INFO, "ERROR searching channels", ex);
            throw ex;
        } finally {
            closeConnection(con, statement, null);
        }
    }

    public Contact getOwner(long uid) throws Exception {
        try {
            UserDao userDao = new UserDao();
            return userDao.findUserById(uid).toContact();

        } catch (Exception ex) {

            LOGGER.log(Level.INFO, "ERROR fetch owner", ex);
            throw ex;
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
