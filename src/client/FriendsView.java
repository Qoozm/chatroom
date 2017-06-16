package client;

import java.awt.BorderLayout;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.User;

public class FriendsView {

	private JFrame jFrame = null;
	private JPanel jContentPane = null;
	private JScrollPane scrollPane = null;
	private String userAccount;
	private SocketChannel socketChannel;
	FriendListPane friendListPane;
	private AddFriendView addFriendView = null;
	private GroupChatView groupChatView = null;
	private boolean groupChating = false;

	public FriendsView(String userAccount, SocketChannel socketChannel) {
		this.userAccount = userAccount;
		this.socketChannel = socketChannel;
	}

	public JFrame getJFrame() {
		
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setSize(230, 585);
			jFrame.setTitle("好友列表");
			jFrame.setContentPane(getJContentPane());
		}
		return jFrame;
	}

	private JScrollPane getScrollPane() {// 给添加好友的容器JPanel添加滚动条；
		if (scrollPane == null) {
			friendListPane = new FriendListPane(userAccount, socketChannel);
			scrollPane = new JScrollPane(friendListPane);
			scrollPane.setBounds(0, 0, 230, 500);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);// 不显示水平滚动条；
		}
		return scrollPane;
	}

	private JPanel getJContentPane() {// 实例化底层的容器JPanel；
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getScrollPane());
			
			JButton groupButton = new JButton("群发");
			groupButton.setBounds(0, 500, 230, 30);
			groupButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (!groupChating) {
						groupChatView = new GroupChatView(userAccount, socketChannel, "GROUP");
						groupChatView.setLocationRelativeTo(null);
						groupChatView.setVisible(true);
						groupChating = true;
					}
					else {
						groupChatView.show();
						groupChatView.getRootPane().setDefaultButton(groupChatView.jButton1);
					}
					
				}
			});
			jContentPane.add(groupButton);
			
			JButton addFriendButton = new JButton("添加好友");
			addFriendButton.setBounds(0, 530, 230, 30);
			addFriendButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					addFriendView = new AddFriendView(userAccount, socketChannel);
					addFriendView.setLocationRelativeTo(null);
					addFriendView.setVisible(true);
				}
			});
			jContentPane.add(addFriendButton);
		}
		return jContentPane;
	}
	
	public void setChating(String account, String content, SocketChannel fileSockeChannel){
		friendListPane.setChating(account, content, fileSockeChannel);
	}
	
	public void setGroupChating(String account, String content) {
		
		if (groupChating) {
			groupChatView.receiveMessage(account, content);
		}
		else {
			groupChatView = new GroupChatView(userAccount, socketChannel, "GROUP");
			groupChatView.setLocationRelativeTo(null);
			groupChatView.setVisible(true);
			groupChating = true;
			groupChatView.receiveMessage(account, content);
		}
	}
	
	public void addFriends(Map<User, String> friends) {
		friendListPane.addFriends(friends);
	}
	
	public void setLogoutFriend(String account){
		friendListPane.friendLogout(account);
	}
	
	public void setLoginFriend(String account){
		friendListPane.friendLogin(account);
	}
	
	public void updateFriendRemark(String friendAccount, String friendRemark, boolean status) {
		friendListPane.updateFriendRemark(friendAccount, friendRemark, status);
	}
	
	public void deleteFriend(String friendAccount, boolean status) {
		friendListPane.deleteFriend(friendAccount, status);
	}
	
	public void setSearchPerson(String searchResult) {
		addFriendView.updateMessageTextArea(searchResult);
	}
	
	public void updateFriends(Map<User, String> friend, boolean status) {
		if (status)
			friendListPane.updateFriends(friend);
		else
			JOptionPane.showMessageDialog(null, "对方拒绝了你的邀请");
	}
	
	public void updateUserInformation(User user, boolean status) {
		if (status) {
			friendListPane.updateUserInformation(user);
		} else {
			JOptionPane.showMessageDialog(null, "修改个人信息失败");
		}
	} 
}
