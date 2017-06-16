package idao;

import java.util.List;

import model.User;

public interface IUserDAO {

	public String insert(User user);
	
	public boolean delete(String user_account);
	
	public boolean update(User user);
	
	public boolean verify(String user_lognIn, String user_passwd);
	
	public List<User> searchAllUsers();
	
	public User searchUserByCondition(String demand);
	
}
