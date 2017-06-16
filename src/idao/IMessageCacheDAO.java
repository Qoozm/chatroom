package idao;

import java.util.List;

import model.MessageCache;

public interface IMessageCacheDAO {
	
	public boolean insert(MessageCache messageCache);
	
	public List<MessageCache> searchMessageCache(String to_account);
	
}
