package model;

public class Friend {

	private String user_account;
	private String friend_account;
	private String friend_remark;
	
	public Friend() {
		
	}
	
	public String getUser_account() {
		return user_account;
	}
	
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	
	public String getFriend_account() {
		return friend_account;
	}
	
	public void setFriend_account(String friend_account) {
		this.friend_account = friend_account;
	}
	
	public String getFriend_remark() {
		return friend_remark;
	}
	
	public void setFriend_remark(String friend_remark) {
		this.friend_remark = friend_remark;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("account: ");
		result.append(friend_account);
		result.append("remark: ");
		result.append(friend_remark);
		return result.toString();
	}
	
	
}
