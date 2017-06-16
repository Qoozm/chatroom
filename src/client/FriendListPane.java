package client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


import model.User;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FriendListPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel listName = null;
	private JLabel friendLabel = null;
	private int clickF = 0;

	private String userAccount;
	private User mySelf;
	private Map<String, JLabel> userLabelMap = new HashMap<>();
	private Map<String, MemberModel> friendsMap = new HashMap<>();
	private SocketChannel socketChannel;

	private JPopupMenu pop;
	private JMenuItem item1;
	private JMenuItem item2;

	public FriendListPane(String userAccount, SocketChannel socketChannel) {
		super();
		initialize();
		this.userAccount = userAccount;
		this.socketChannel = socketChannel;
	}

	private void initialize() {

		listName = new JLabel();
		listName.setText("我的好友");
		// listName.setIcon(new ImageIcon("icon/ico.jpg"));
		listName.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		listName.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				clickF += 1;
				if (clickF % 2 == 1) {
					for (String userAccount : userLabelMap.keySet()) {
						userLabelMap.get(userAccount).setVisible(false);
					}
					// listName.setIcon(new ImageIcon("icon/ico2.jpg"));
					update();
				} else {
					for (String userAccount : userLabelMap.keySet()) {
						userLabelMap.get(userAccount).setVisible(true);
					}
					// listName.setIcon(new ImageIcon("icon/ico.jpg"));
					update();
				}
			}
		});
		this.add(listName, null);
	}

	private void update() {// 更新UI界面；
		this.updateUI();
	}

	public void addFriends(Map<User, String> friends) {
		
		Iterator<User> iterator = friends.keySet().iterator();
		while (iterator.hasNext()) {
			User user = iterator.next();
			if (user.getUser_account().equals(userAccount)) {
				addFriend(user, user.getUser_name());
				iterator.remove();
				break;
			}
		}

		String friendName;
		for (User friend : friends.keySet()) {			
			if (friends.get(friend).equals(" "))
				friendName = friend.getUser_name();
			else
				friendName = friends.get(friend);
			addFriend(friend, friendName);
		}
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(200, 408);
		this.setLocation(20, 5);
	}
	
	private void addFriend(User friend, String friendName) {
		friendLabel = new JLabel();
		MemberModel memberModel;
		friendLabel.setIcon(new ImageIcon("icon/bg.jpg"));
		if (friend.getIsOnline().equals("online")) {
			memberModel = new MemberModel(friend.getUser_icon(), friendName, 200, true, userAccount,
					friend.getUser_account(), socketChannel);
			friendLabel.add(memberModel.jPanel);
		} else {
			memberModel = new MemberModel(friend.getUser_icon(), friendName, 200, false, userAccount,
					friend.getUser_account(), socketChannel);
			friendLabel.add(memberModel.jPanel);
		}

		friendsMap.put(friend.getUser_account(), memberModel);
		friendLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		userLabelMap.put(friend.getUser_account(), friendLabel);
		
		this.add(friendLabel, null);

		if (friend.getUser_account().equals(userAccount)) {
			
			mySelf = new User();
			mySelf.setUser_account(friend.getUser_account());
			mySelf.setUser_name(friend.getUser_name());
			mySelf.setUser_passwd(friend.getUser_passwd());
			mySelf.setUser_icon(friend.getUser_icon());
			mySelf.setUser_tel(friend.getUser_tel());
			mySelf.setUser_email(friend.getUser_email());
			
			item1 = new JMenuItem("修改个人信息");
			pop = new JPopupMenu();
			pop.add(item1);
			item1.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JPasswordField pass = new JPasswordField();
							JOptionPane.showMessageDialog(null, pass, "请输入密码", JOptionPane.PLAIN_MESSAGE);
							if (mySelf.getUser_passwd().equals(pass.getText())) {
								ChangeUserInformation changeUserInformation = new ChangeUserInformation(
										socketChannel, mySelf);
								changeUserInformation.setLocationRelativeTo(null);
								changeUserInformation.setVisible(true);
							}
							else {
								JOptionPane.showMessageDialog(null, "密码错误");
							}
						}
					});
				}
			});
		} else {
			item1 = new JMenuItem("修改好友信息");
			item2 = new JMenuItem("删除好友");
			pop = new JPopupMenu();
			pop.add(item1);
			pop.add(item2);
			item1.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					String friendRemark = JOptionPane.showInputDialog("请输入新昵称");
					if (friendRemark != null) {
						ByteBuffer buffer = ByteBuffer.allocate(512);
						String modifyFriendRemarkMessage = userAccount + "-modifyfriend-update-"
								+ friend.getUser_account() + "-" + friendRemark;
						buffer.putInt(modifyFriendRemarkMessage.getBytes(Charset.forName("UTF-8")).length);
						buffer.put(Charset.forName("UTF-8").encode(modifyFriendRemarkMessage).array(), 0,
								modifyFriendRemarkMessage.getBytes(Charset.forName("UTF-8")).length);
						buffer.flip();
						while (buffer.hasRemaining()) {
							try {
								socketChannel.write(buffer);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						buffer.clear();
					}
				}
			});
			item2.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int isDelete = JOptionPane.showConfirmDialog(null, "确定删除吗？", "提示",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (isDelete == JOptionPane.YES_OPTION) {
						ByteBuffer buffer = ByteBuffer.allocate(512);
						String modifyFriendRemarkMessage = userAccount + "-modifyfriend-delete-"
								+ friend.getUser_account() + "-" + " ";
						int messageLength = modifyFriendRemarkMessage.getBytes(Charset.forName("UTF-8")).length;
						buffer.putInt(messageLength);
						buffer.put(Charset.forName("UTF-8").encode(modifyFriendRemarkMessage).array(), 0,
								messageLength);
						buffer.flip();
						while (buffer.hasRemaining()) {
							try {
								socketChannel.write(buffer);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						buffer.clear();
					}
				}
			});
		}
		memberModel.jPanel.setComponentPopupMenu(pop);
		memberModel.lb_online.setComponentPopupMenu(pop);
		memberModel.lb_nickName.setComponentPopupMenu(pop);
		memberModel.jButton.setComponentPopupMenu(pop);
	}

	public void setChating(String friendAccount, String content, SocketChannel fileSockeChannel) {
		friendsMap.get(friendAccount).setChating(fileSockeChannel);
		friendsMap.get(friendAccount).receiveMessage(content);
	}

	public void friendLogout(String friendAccount) {
		if (friendsMap.containsKey(friendAccount))
			friendsMap.get(friendAccount).changeOnlineStatus(false);
	}

	public void friendLogin(String friendAccount) {
		if (friendsMap.containsKey(friendAccount))
			friendsMap.get(friendAccount).changeOnlineStatus(true);
	}

	public void updateFriendRemark(String friendAccount, String friendRemark, boolean status) {
		if (status)
			friendsMap.get(friendAccount).lb_nickName.setText(friendRemark);
		else
			JOptionPane.showMessageDialog(null, "修改昵称失败");
	}

	public void deleteFriend(String friendAccount, boolean status) {
		if (status) {
			this.remove(userLabelMap.get(friendAccount));
			update();
		} else {
			JOptionPane.showMessageDialog(null, "删除好友失败");
		}
	}
	
	public void updateFriends(Map<User, String> friendMap) {
		addFriends(friendMap);
	}
	
	public void updateUserInformation(User user) {
		
		mySelf.setUser_account(user.getUser_account());
		mySelf.setUser_name(user.getUser_name());
		mySelf.setUser_passwd(user.getUser_passwd());
		mySelf.setUser_icon(user.getUser_icon());
		mySelf.setUser_tel(user.getUser_tel());
		mySelf.setUser_email(user.getUser_email());
		mySelf.setIsOnline(user.getIsOnline());
		
		friendsMap.get(mySelf.getUser_account()).jButton.setIcon(new ImageIcon(mySelf.getUser_icon()));
		friendsMap.get(mySelf.getUser_account()).lb_nickName.setText(mySelf.getUser_name());
		if (mySelf.getIsOnline().equals("online"))
			friendsMap.get(mySelf.getUser_account()).changeOnlineStatus(true);
		else 
			friendsMap.get(mySelf.getUser_account()).changeOnlineStatus(false);
	}
}
