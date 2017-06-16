package client;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm;

/**
 *
 * @author Charley
 */
public class AddFriendView extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame1
     */
    public AddFriendView() {
        initComponents();
    }
    public AddFriendView(String user_account, SocketChannel socketChannel) {
    	this.user_account = user_account;
    	this.socketChannel = socketChannel;
    	initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        addFriendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(user_account + " 添加好友...");
        this.getRootPane().setDefaultButton(searchButton);
        jLabel1.setText("搜索用户：");

        searchButton.setText("搜索");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
       

        messageTextArea.setEditable(false);
        messageTextArea.setColumns(20);
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        jScrollPane1.setViewportView(messageTextArea);

        addFriendButton.setText("加为好友");
        addFriendButton.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent evt) {
        		addFriendButtonActionPerformed(evt);
        	}
		});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addFriendButton)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(searchButton))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addFriendButton)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>                        


    private void searchButtonActionPerformed(ActionEvent event) {                                             
  
    	StringBuilder strRequest = new StringBuilder();
    	strRequest.append(user_account);//TODO
    	strRequest.append("-");
    	strRequest.append("searchperson-");
    	if (searchTextField.getText().equals("")) {
    		JOptionPane.showMessageDialog(null, "请输入查询账号");
    	}
    	else {
    		strRequest.append(searchTextField.getText());
    		try {
				byte[] byteRequest = strRequest.toString().getBytes("UTF-8");
				int length = byteRequest.length;
				ByteBuffer buffer = ByteBuffer.allocate(4 + length);
				buffer.putInt(length);
				buffer.put(byteRequest);
				buffer.flip();
				while (buffer.hasRemaining()) {
					socketChannel.write(buffer);
				}
			} catch (IOException e) {
				System.err.println(e);
			}
    		
    	}
    	
    }                                            

    private void addFriendButtonActionPerformed(MouseEvent evt) {                                                
    	
    	if (search_account != null) {
    		
    		if (!search_account.equals(user_account)) {
    			StringBuilder strRequest = new StringBuilder();
            	strRequest.append(user_account); //TODO
            	strRequest.append("-modifyfriend-add-");
            	strRequest.append(search_account);
            	strRequest.append("- -ask");
            	try {
        			byte[] byteRequest = strRequest.toString().getBytes("UTF-8");
        			int length = byteRequest.length;
        			ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        			buffer.putInt(length);
        			buffer.put(byteRequest);
        			buffer.flip();
        			while (buffer.hasRemaining()) {
        				socketChannel.write(buffer);
        			}
        			JOptionPane.showMessageDialog(null, "您的好友请求已发送给对方");
        			this.dispose();
        		} catch (IOException e) {
        			System.err.println(e);
        		}
    		} else {
    			JOptionPane.showMessageDialog(null, "你有个双胞胎...想加你为好友");
    		}
    		
    	} else {
    		
    		JOptionPane.showMessageDialog(null, "请查询后添加");
    	}
    	
    }     
    
    public void updateMessageTextArea(String searchResult) {
    	
    	StringBuilder strShow = new StringBuilder();
    	String[] arrayResult = searchResult.split("-");
    	
    	if (arrayResult.length != 1) {
    		search_account = arrayResult[0];
    		strShow.append("\n\n\naccount:	");
        	strShow.append(search_account);
        	strShow.append("\nname:	");
        	strShow.append(arrayResult[1]);
        	strShow.append("\nE-mail:	");
        	strShow.append(arrayResult[2]);
    	}
    	else {
    		
    		strShow.append("\n\n\n" + searchResult);
    		search_account = null;
    	}
    
    	messageTextArea.setText(strShow.toString());
    }
    

    // Variables declaration - do not modify                     
    private javax.swing.JButton addFriendButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTextField;
    private String user_account = null;
    private String search_account =  null;
    private SocketChannel socketChannel = null;
    // End of variables declaration                   
}
