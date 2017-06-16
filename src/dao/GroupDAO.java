package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import idao.IGroupDAO;
import model.Group;
import model.User;
import util.ConnectionManager;

public class GroupDAO implements IGroupDAO {
	
	private static final int GROUP_ADMIN = 1;
	private static final int GROUP_LORD = 0;
	private static final int GROUP_MEMBER = 2;
	private static final int NOTIN_GROUP = -1;
	
	@SuppressWarnings("finally")
	@Override
	public boolean createGroup(Group group) {
		boolean result = false;
		if (cantainsGroup(group.getGroup_id()))
			return result;
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		String sql = "insert into crgroup (group_id, user_account, group_name, user_authory) values "
				+ "(?, ?, ?, ?)";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, group.getGroup_id());
			pStatement.setString(2, group.getUser_account());
			pStatement.setString(3, group.getGroup_name());
			pStatement.setInt(4, GROUP_LORD);
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
	public boolean deleteGroup(int group_id, String user_account) {
		boolean result = false;
		int userAuth = userAuthory(group_id, user_account);
		if (userAuth != GROUP_LORD)
			return result;
		
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		String sql = "delete from crgroup where group_id = ?";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, group_id);
			
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
	public boolean updateGroup(Group group) {
		boolean result = false;
		int userAuth = userAuthory(group.getGroup_id(), group.getUser_account());
		if (userAuth == NOTIN_GROUP || userAuth == GROUP_MEMBER)
			return result;
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		String sql = "update crgroup set group_name = ? where group_id = ?";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, group.getGroup_name());
			pStatement.setInt(2, group.getGroup_id());
			
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
	private boolean cantainsGroup(int group_id) {
		boolean result = false;
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		String sql = "select group_id from crgroup where group_id = ?";
		ResultSet resultSet = null;
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, group_id);
			
			resultSet = pStatement.executeQuery();
			if (!resultSet.next())
				return result;
			
			result = true;
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			ConnectionManager.close(resultSet, pStatement, connection);
			return result;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public User searchUserInGroup(int group_id, String user_account, String find_account) {
		User user = null;
		if (userAuthory(group_id, user_account) == NOTIN_GROUP || userAuthory(group_id, find_account) == NOTIN_GROUP)
			return user;
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		String sql = "select * from cruser where user_account = ?";
		ResultSet resultSet = null;
		user = new User();
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, find_account);
			
			resultSet = pStatement.executeQuery();
			
			if (resultSet.next()){
				user.setUser_account(resultSet.getString("user_account"));
				user.setUser_email(resultSet.getString("user_email"));
				user.setUser_icon(resultSet.getString("user_icon"));
				user.setUser_name(resultSet.getString("user_name"));
				user.setUser_tel(resultSet.getString("user_tel"));
			}
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			ConnectionManager.close(resultSet, pStatement, connection);
			return user;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public List<User> searchAllUsersByGroup(int group_id, String user_account) {
		List<User> userlist = new ArrayList<>();
		int userAuthory = userAuthory(group_id, user_account);
		if (userAuthory == NOTIN_GROUP)
			return userlist;
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		String sql = "select * from cruser where user_account in (select user_account from crgroup where group_id = ?)";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, group_id);
			
			resultSet = pStatement.executeQuery();
			while (resultSet.next()){
				User user = new User();
				
				user.setUser_account(resultSet.getString("user_account"));
				user.setUser_email(resultSet.getString("user_email"));
				user.setUser_icon(resultSet.getString("user_icon"));
				user.setUser_name(resultSet.getString("user_name"));
				user.setUser_tel(resultSet.getString("user_tel"));
				
				userlist.add(user);
			}
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			ConnectionManager.close(resultSet, pStatement, connection);
			return userlist;
		}
	}

	@SuppressWarnings("finally")
	private int userAuthory(int group_id, String user_account) {
		int userAuthory = NOTIN_GROUP;
		if (user_account == null)
			return userAuthory;
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement pStatement = null;
		String sql = "select user_authory from crgroup where group_id = ? and user_account = ?";
		ResultSet resultSet = null;
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, group_id);
			pStatement.setString(2, user_account);
			
			resultSet = pStatement.executeQuery();
			if (!resultSet.next())
				return userAuthory;
			else {
				userAuthory = resultSet.getInt("user_authory");
				
			}
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			ConnectionManager.close(resultSet, pStatement, connection);
			return userAuthory;
		}
	}

}
