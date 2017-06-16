package idao;

import dao.FriendDAO;
import dao.GroupDAO;
import dao.MessageCacheDAO;
import dao.UserDAO;

public class DAOFactory {
	
	public static IUserDAO createUserDAO() {
		return new UserDAO();
	}
	
	public static IFriendDAO createFriendDAO()	 {
		return new FriendDAO();
	}
	
	public static IMessageCacheDAO createMessageCacheDAO() {
		return new MessageCacheDAO();
	}
	
	public static IGroupDAO createGroupDAO() {
		return new GroupDAO();
	}
}
