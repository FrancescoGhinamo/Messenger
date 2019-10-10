package client.frontend.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import client.backend.beam.Communicator;
import common.Commons;

@SuppressWarnings("deprecation")
public class ClientManager extends JFrame implements ActionListener, Observer {


	private static final long serialVersionUID = -8047216969854792092L;

	private JTabbedPane chatTabs;

	private Hashtable<String, ChatPanel> chatPanels;


	private ArrayList<String> connectedUsers;

	private JMenuItem itemConnect;
	private JMenuItem itemDisconnect;
	private JMenuItem itemNewChat;

	public ClientManager() {
		super("Messenger client");
		connectedUsers = new ArrayList<String>();
		chatPanels = new Hashtable<String, ChatPanel>();
		addWindowListener(new CWinMan());
		setMinimumSize(new Dimension(300, 400));
		setSize(new Dimension(400, 500));
		initComponents();

	}

	private void initComponents() {

		setJMenuBar(initJMenuBar());

		chatTabs = new JTabbedPane();
		setLayout(new BorderLayout());
		add(chatTabs, BorderLayout.CENTER);

	}

	private JMenuBar initJMenuBar() {
		JMenuBar bar = new JMenuBar();
		bar.add(initChatMenu());
		return bar;
	}

	private JMenu initChatMenu() {
		JMenu mnuChat = new JMenu("Chat");

		itemConnect = new JMenuItem("Connect to server");
		itemConnect.addActionListener(this);

		itemDisconnect = new JMenuItem("Disconnect");
		itemDisconnect.addActionListener(this);
		itemDisconnect.setEnabled(false);

		itemNewChat = new JMenuItem("New chat");
		itemNewChat.addActionListener(this);
		itemNewChat.setEnabled(false);

		mnuChat.add(itemConnect);
		mnuChat.add(itemDisconnect);
		mnuChat.addSeparator();
		mnuChat.add(itemNewChat);

		return mnuChat;

	}



	public void performNewMessage(String mes) {

		Toolkit.getDefaultToolkit().beep();
		this.toFront();

		String[] source = Commons.getData(mes);

		ChatPanel pan = chatPanels.get(source[0]);
		if(pan == null) {
			pan = new ChatPanel(source[0]);
			chatPanels.put(source[0], pan);
			chatTabs.addTab(source[0], pan);
		}
		pan.setEnabled(true);

		pan.addMessage(mes);
	}

	public void performNewChat() {
		MateChooserDialog d = new MateChooserDialog(this, true, connectedUsers);
		d.setVisible(true);
		String name = d.getResult();
		if(!name.equals("")) {
			ChatPanel pan = new ChatPanel(name);
			chatPanels.put(name, pan);
			chatTabs.addTab(name, pan);
		}

	}

	public void performConnect() {

		try {
			String ip = JOptionPane.showInputDialog(this, "Insert host address: ", "Connection", JOptionPane.QUESTION_MESSAGE);
			int port = Integer.valueOf(JOptionPane.showInputDialog(this, "Insert port", "Connection", JOptionPane.QUESTION_MESSAGE));
			String name = JOptionPane.showInputDialog(this, "Insert your name ", "Connection", JOptionPane.QUESTION_MESSAGE);
			Communicator c = Communicator.getInstance(InetAddress.getByName(ip), port, name);
			c.addObserver(this);
			new Thread(c).start();
			itemConnect.setEnabled(false);
			itemNewChat.setEnabled(true);
			itemDisconnect.setEnabled(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Problem while connecting", "Error", JOptionPane.ERROR_MESSAGE);
		}


	}

	public void performDisconnect() {
		int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to disconnect?", "Disconnect", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if(res == JOptionPane.YES_OPTION) {
			Communicator.getInstance().setStopped(true);
			itemConnect.setEnabled(true);
			itemNewChat.setEnabled(false);
			itemDisconnect.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(itemConnect)) {
			performConnect();
		}
		else if(e.getSource().equals(itemNewChat)) {
			performNewChat();
		}
		else if(e.getSource().equals(itemDisconnect)) {
			performDisconnect();
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Communicator) {
			String rec = (String)arg;
			if(!rec.equals("")) {


				if(rec.contains(Commons.ACTUAL_NAME_NOTIFICATION)) {
					performActualNameNotification(rec);
				}
				else if(rec.contains(Commons.CLOSE_REQUEST)) {
					performCloseRequest(rec);
				}
				else if(rec.contains(Commons.LOST_USER_NOTIFICATION)) {
					performLostUserNotitfication(rec);
				}
				else if(rec.contains(Commons.NEW_USER_NOTIFICATION)) {
					performNewUserNotification(rec);
				}
				else {
					performNewMessage(rec);

				}

			}
		}

	}

	private void performActualNameNotification(String rec) {
		String name = rec.substring(rec.indexOf(":") + 1);
		JOptionPane.showMessageDialog(this, "Registered name: " + name, "Name", JOptionPane.INFORMATION_MESSAGE);
		this.setTitle(this.getTitle() + " - " + name);
	}

	private void performCloseRequest(String req) {
		JOptionPane.showMessageDialog(this, "Closing communicaition", "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	private void performLostUserNotitfication(String rec) {
		String name = rec.substring(rec.indexOf(":") + 1);
		connectedUsers.remove(name);
		chatPanels.get(name).setEnabled(false);
	}

	private void performNewUserNotification(String rec) {
		String name = rec.substring(rec.indexOf(":") + 1);
		connectedUsers.add(name);
	}

	public static void main(String[] args) {

		ClientManager c = new ClientManager();
		c.setVisible(true);


	}

}
