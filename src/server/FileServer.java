package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import idao.DAOFactory;
import model.MessageCache;
import model.User;

public class FileServer implements Runnable {

	private static final int DEFAULT_PORT = 6790;
	private static final int SIZEOFBUFF = 2048;
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private int prot = DEFAULT_PORT;
	private Map<String, User> userOnline;
	private ExecutorService executorService;
	private ByteBuffer sendBuff = ByteBuffer.allocate(SIZEOFBUFF);
	private ByteBuffer receiveBuff = ByteBuffer.allocate(SIZEOFBUFF);
	private Charset charset = Charset.forName("UTF-8");

	public FileServer(Map<String, User> userOnline, ExecutorService executorService) throws IOException {
		this.userOnline = userOnline;
		this.executorService = executorService;
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(prot));
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				selector.select();

				Set<SelectionKey> selectionSet = selector.selectedKeys();
				Iterator<SelectionKey> selecttionkeys = selectionSet.iterator();

				while (selecttionkeys.hasNext()) {

					SelectionKey selectionKey = selecttionkeys.next();
					selecttionkeys.remove();

					if (selectionKey.isAcceptable()) {
						SocketChannel socketChannel = serverSocketChannel.accept();
						socketChannel.configureBlocking(false);
						SelectionKey keyChannel = socketChannel.register(selector, SelectionKey.OP_READ);
						boolean finished = true;
						keyChannel.attach(finished);
					}

					if (selectionKey.isReadable()) {
						boolean finished = (boolean) selectionKey.attachment();
						if (finished) {
							finished = false;
							selectionKey.attach(finished);
							executorService.execute(new handleFileInformation(selectionKey));
							selectionKey.interestOps(SelectionKey.OP_READ);
						}
					}

					if (selectionKey.isWritable()) {

					}
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	private boolean personIsOnline(String friendAccount) {
		return userOnline.containsKey(friendAccount);
	}

	private class handleFileInformation implements Runnable {

		private SelectionKey selectionKey;
		private SocketChannel keyChannel;

		public handleFileInformation(SelectionKey selectionKey) {
			this.selectionKey = selectionKey;
			keyChannel = (SocketChannel) selectionKey.channel();
		}

		@Override
		public void run() {
			try {
				int rlen = 0;
				receiveBuff.clear();
				receiveBuff.limit(4);
				int tmp = 0;
				while (true) {
					rlen = keyChannel.read(receiveBuff);
					if (rlen != 0) {
						receiveBuff.flip();
						tmp += rlen;
					}
					if (tmp == 4)
						break;
					if (rlen == -1) {
						return;
					}
				}

				int fileDescribeLength = receiveBuff.getInt();
				receiveBuff.clear();
				receiveBuff.limit(fileDescribeLength);
				rlen = 0;
				tmp = 0;
				String line = "";

				while (true) {
					rlen = keyChannel.read(receiveBuff);
					if (rlen != 0) {
						tmp += rlen;
						receiveBuff.flip();
						line += charset.decode(receiveBuff);
						receiveBuff.clear();
					}
					if (tmp == fileDescribeLength)
						break;
					if (rlen == -1)
						return;
				}


				if (line.split("-").length > 2) {

					if (line.split("-")[1].equals("receivefile")) { // 给指定用户传文件

						String[] fileHead = line.split("-");
//						String toAccount = fileHead[0];
//						String fromAccount = fileHead[2];
						String filePath = fileHead[3];

						FileChannel fileChannel = new FileInputStream(filePath).getChannel();

						sendBuff.clear();
						while (fileChannel.read(sendBuff) > 0) {
							sendBuff.flip();
							while (sendBuff.hasRemaining()) {
								keyChannel.write(sendBuff);
							}
							sendBuff.clear();
						}
						fileChannel.close();
						keyChannel.close();

						File file = new File(filePath);
						if (file.exists())
							file.delete();

					} else {

						String[] fileHead = line.split("-");
						String fromAccount = fileHead[0];
						String toAccount = fileHead[1];
						String fileName = fileHead[2];
						String fileCachePath = "fileCache/" + fromAccount + toAccount + fileName;

						FileChannel fileChannel = new FileOutputStream(fileCachePath).getChannel();

						receiveBuff.clear();
						while (keyChannel.read(receiveBuff) != -1) {
							receiveBuff.flip();
							while (receiveBuff.hasRemaining()) {
								fileChannel.write(receiveBuff);
							}
							receiveBuff.clear();
						}
						
						fileChannel.close();

						if (personIsOnline(toAccount)) { // 转发文件给用户

							String strTell = "receivefile-" + fromAccount + ":" + fileCachePath;
							sendBuff.clear();
							sendBuff.putInt(strTell.getBytes(charset).length);
							sendBuff.put(strTell.getBytes(charset));
							sendBuff.flip();

							while (sendBuff.hasRemaining()) {
								userOnline.get(toAccount).getClient().write(sendBuff);
							}

						} else { // 缓存文件到服务器

							MessageCache messageCache = new MessageCache();
							messageCache.setFrom_account(fromAccount);
							messageCache.setTo_account(toAccount);
							messageCache.setMessage_type(9);
							messageCache.setContent(fileCachePath);

							DAOFactory.createMessageCacheDAO().insert(messageCache);
						}
					}
				} else
					System.out.println("Protocol wrong!");
				boolean finished = (boolean) selectionKey.attachment();
				finished = true;
				selectionKey.attach(finished);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Thread.interrupted();
			}

		}
	}
}
