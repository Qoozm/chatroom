package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import idao.DAOFactory;
import idao.IFriendDAO;
import model.Friend;
import model.User;
import util.ConnectionManager;

public class FriendDAO implements IFriendDAO{

	@SuppressWarnings("finally")
	@Override
	public boolean addFriend(Friend friend) {
		
		boolean result = false;	
		if (friend == null || friend.getUser_account().equals(friend.getFriend_account())) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		
		String sql = "INSERT INTO friends(user_account, friend_account, friend_remark) VALUES("
				+ "?, ?, null)";
		try {
			connection.setAutoCommit(false);
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, friend.getUser_account());
			pStatement.setString(2, friend.getFriend_account());
			
			pStatement.executeUpdate();
			pStatement.close();
			
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, friend.getFriend_account());
			pStatement.setString(2, friend.getUser_account());
			pStatement.executeUpdate();
			
			connection.commit();
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
	public boolean deleteFriendByAccount(String user_account, String friend_account) {
		
		boolean result = false;
		if (friend_account == null) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		
		String sql = "DELETE FROM friends WHERE user_account = ? AND friend_account = ?";
		
		try {
			
			connection.setAutoCommit(false);
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, user_account);
			pStatement.setString(2, friend_account);
			
			pStatement.executeUpdate();
			pStatement.close();
			
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, friend_account);
			pStatement.setString(2, user_account);
			
			pStatement.executeUpdate();
			
			connection.commit();
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
	public boolean deleteAllFriends(String user_account) {

		boolean result = false;
		if (user_account == null) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		
		String sql1 = "DELETE FROM friends WHERE user_account = ?";
		
		String sql = "DELETE FROM friends WHERE user_account in (SELECT temp.friend_account FROM "
				+ "(SELECT friend_account FROM friends WHERE user_account = ?) temp)";
		try {
			connection.setAutoCommit(false);
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, user_account);
			
			pStatement.executeUpdate();
			pStatement.close();
			
			pStatement = connection.prepareStatement(sql1);
			pStatement.setString(1, user_account);
			
			pStatement.executeUpdate();
			connection.commit();
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
	public boolean updateFriend(Friend friend) {

		boolean result = false;
		if (friend == null) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		
		String sql = "UPDATE friends SET friend_remark = ? WHERE user_account = ? AND friend_account = ?";
		
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, friend.getFriend_remark());
			pStatement.setString(2, friend.getUser_account());
			pStatement.setString(3, friend.getFriend_account());
			
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
	public User searchFriendByAccount(String user_account, String friend_account) {

		User result = null;
		if (user_account == null || friend_account == null) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		
		String sql = "SELECT * FROM cruser WHERE user_account in ("
				+ "SELECT friend_account FROM friends WHERE user_account = ? AND friend_account = ?)";
		
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, user_account);
			pStatement.setString(2, friend_account);
			
			resultSet = pStatement.executeQuery();
			if (resultSet.next()) {
				result = new User();
				
				result.setUser_account(resultSet.getString("user_account"));
				result.setUser_icon(resultSet.getString("user_icon"));
				result.setUser_name(resultSet.getString("user_name"));
				result.setUser_tel(resultSet.getString("user_tel"));
				result.setUser_email(resultSet.getString("user_email"));
				
			}
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			
			ConnectionManager.close(resultSet, pStatement, connection);
			return result;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public List<User> searchFriendsByRemark(String user_account, String friend_remark) {
		
		List<User> result = new ArrayList<>();
		if (user_account == null || friend_remark == null) {
			return result;
		}
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		
		
		String sql = "SELECT * FROM cruser WHERE user_account in ("
				+ "SELECT friend_account FROM friends WHERE user_account = ? AND friend_remark = ?)";
		
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, user_account);
			pStatement.setString(2, friend_remark);
			
			resultSet = pStatement.executeQuery();
			User user = null;
			while (resultSet.next()) {
				
				user = new User();
				user.setUser_account(resultSet.getString("user_account"));
				user.setUser_icon(resultSet.getString("user_icon"));
				user.setUser_name(resultSet.getString("user_name"));
				user.setUser_tel(resultSet.getString("user_tel"));
				user.setUser_email(resultSet.getString("user_email"));
				
				result.add(user);
				
			}
			
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			
			ConnectionManager.close(resultSet, pStatement, connection);
			return result;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public HashMap<User, String> searchAllFriend(String user_account) {

		HashMap<User, String> result = new HashMap<>();
		if (user_account == null) {
			return result;
		}

		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		String sql = "SELECT cruser.user_account, user_name, user_tel, user_email, user_icon, friend_remark "
				+ "FROM cruser, friends "
				+ "WHERE cruser.user_account IN (SELECT friend_account FROM friends WHERE friends.user_account = ?) "
				+ "and friends.friend_account = cruser.user_account "
				+ "and friends.user_account = ?";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, user_account);
			pStatement.setString(2, user_account);

			resultSet = pStatement.executeQuery();
			User user = null;
			String friendRemark = null;
			while (resultSet.next()) {

				user = new User();
				user.setUser_account(resultSet.getString("user_account"));
				user.setUser_icon(resultSet.getString("user_icon"));
				user.setUser_name(resultSet.getString("user_name"));
				user.setUser_tel(resultSet.getString("user_tel"));
				user.setUser_email(resultSet.getString("user_email"));
				friendRemark = resultSet.getString("friend_remark");

				result.put(user, friendRemark);

			}

		} catch (SQLException e) {
			System.err.println(e);
		} finally {

			ConnectionManager.close(resultSet, pStatement, connection);
			return result;
		}
	}

}

