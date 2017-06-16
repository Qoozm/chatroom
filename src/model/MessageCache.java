package model;

public class MessageCache {

	private String from_account;
	private String to_account;
	private String message_date;
	private String content;
	private boolean message_status;
	private int message_type; //-1: 进群验证 	0： 好友验证	1：普通消息		2:群消息
	
	public MessageCache() {
		
	}
	
	public int getMessage_type() {
		return message_type;
	}

	public void setMessage_type(int message_type) {
		this.message_type = message_type;
	}
	
	public boolean isMessage_status() {
		return message_status;
	}

	public void setMessage_status(boolean message_status) {
		this.message_status = message_status;
	}

	public String getFrom_account() {
		return from_account;
	}
	
	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}
	
	public String getTo_account() {
		return to_account;
	}
	
	public void setTo_account(String to_account) {
		this.to_account = to_account;
	}
	
	public String getMessage_date() {
		return message_date;
	}
	
	public void setMessage_date(String message_date) {
		this.message_date = message_date;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@SuppressWarnings("deprecation")
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(from_account);
		stringBuilder.append(" sent ");
		stringBuilder.append(to_account);
		stringBuilder.append(" at ");
		stringBuilder.append(message_date);
		stringBuilder.append("#");
		stringBuilder.append(content);
		return stringBuilder.toString();		
	}
	
}
