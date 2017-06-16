package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import idao.IMessageCacheDAO;
import model.MessageCache;
import util.ConnectionManager;

public class MessageCacheDAO implements IMessageCacheDAO{

	@SuppressWarnings("finally")
	@Override
	public boolean insert(MessageCache messageCache) {
		
		boolean result = false;
		if (messageCache == null) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		
		String sql = "INSERT INTO messagecache (from_account, to_account, message_date, content, message_status, message_type)"
				+ "VALUES (?, ?, now(), ?, ?, ?)";
		
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, messageCache.getFrom_account());
			pStatement.setString(2, messageCache.getTo_account());
			pStatement.setString(3, messageCache.getContent());
			pStatement.setBoolean(4, false);
			pStatement.setInt(5, messageCache.getMessage_type());
			
			pStatement.executeUpdate();
			result = true;
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			
			ConnectionManager.close(null, pStatement, connection);
			return result;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public List<MessageCache> searchMessageCache(String to_account) {

		List<MessageCache> result = new ArrayList<>();
		if (to_account == null) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		MessageCache message = null;
		ResultSet resultSet = null;
		
		String sql = "CALL messageCacheHandle(?)";
		
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, to_account);
			
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				
				message = new MessageCache();
				message.setFrom_account(resultSet.getString("from_account"));
				message.setTo_account(to_account);
				message.setContent(resultSet.getString("content"));
				message.setMessage_date(resultSet.getDate("message_date") + " " + resultSet.getTime("message_date"));
				message.setMessage_type(resultSet.getInt("message_type"));
				result.add(message);
			}
			
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			
			ConnectionManager.close(resultSet, pStatement, connection);
			return result;
		}
	}

}
