package model;

public class Group {
	private int group_id;
	private String user_account;
	private String group_name;
	private int user_authory;
	
	public int getUser_authory() {
		return user_authory;
	}
	
	public void setUser_authory(int user_authory) {
		this.user_authory = user_authory;
	}
	
	public int getGroup_id() {
		return group_id;
	}
	
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	
	public String getUser_account() {
		return user_account;
	}
	
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	
	public String getGroup_name() {
		return group_name;
	}
	
	public void setGroup_name(String grouo_name) {
		this.group_name = grouo_name;
	}
	
	public String toString(){
		return "group_name: " + group_name + "user_account: " + user_account;
	}
}
