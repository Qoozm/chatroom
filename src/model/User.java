package model;

import java.nio.channels.SocketChannel;

public class User {
	
	private int user_id;
	private String user_account;
	private String user_name;
	private String user_passwd;
	private String user_icon;
	private String user_tel;
	private String user_email;
	private SocketChannel client;
	private String isOnline;
	
	

	public User() {
		
	}
	
	public String getIsOnline() {
		return isOnline;
	}


	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public SocketChannel getClient() {
		return client;
	}

	public void setClient(SocketChannel client) {
		this.client = client;
	}

	public String getUser_tel() {
		return user_tel;
	}

	public void setUser_tel(String user_tel) {
		this.user_tel = user_tel;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_account() {
		return user_account;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_passwd() {
		return user_passwd;
	}

	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}



	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("account -> ");
		result.append(user_account);
		result.append(" name -> ");
		result.append(user_name);
		return result.toString();
	}
	
	
	public int hashCode() {
		return new Integer(user_account).hashCode();
	}
	
	
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object != null && object.getClass() == User.class) {
			User target = (User) object;
			return user_account == target.getUser_account();
		}
		return false;
	}
	
}
