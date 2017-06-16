package idao;

import java.util.HashMap;
import java.util.List;

import model.Friend;
import model.User;

public interface IFriendDAO {

	public boolean addFriend(Friend friend);
	
	public boolean deleteFriendByAccount(String user_account, String friend_account);
	
	public boolean deleteAllFriends(String user_account);
	
	public boolean updateFriend(Friend friend);
	
	public HashMap<User, String> searchAllFriend(String user_account);
	
	public User searchFriendByAccount(String user_account, String friend_account);
	
	public List<User> searchFriendsByRemark(String user_account, String friend_remark);
	
}
