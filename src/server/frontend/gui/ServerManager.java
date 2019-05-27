package server.frontend.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import common.Commons;
import server.backend.beam.ClientHandler;
import server.backend.beam.MessengerServer;
import server.backend.service.LogServiceFactory;

@SuppressWarnings("deprecation")
public class ServerManager extends JFrame implements ActionListener, Observer {

	
	private static final long serialVersionUID = 5459133347595078106L;
	
	private DefaultListModel<String> lstLog;
	
	private JMenuItem itemStartServer;
	private JMenuItem itemStopServer;
	private JMenuItem itemResumeAccepting;
	private JMenuItem itemStopAccepting;
	
	public ServerManager() {
		super("Messenger Server Manager");
		LogServiceFactory.getLogService().writeLogLine("\r\nSession started: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		setMinimumSize(new Dimension(350, 200));
		setSize(new Dimension(800, 500));
		this.addWindowListener(new WinMan());
		MessengerServer.getInstance().addObserver(this);
		initComponents();
	}

	private void initComponents() {
		setJMenuBar(initJMenuBar());
		
		setLayout(new BorderLayout());
		add(initLogPane(), BorderLayout.CENTER);
		
	}
	
	private JMenuBar initJMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		bar.add(initServerMenu());

		return bar;
	}
	
	private JMenu initServerMenu() {
		JMenu serverMnu = new JMenu("Server");
		
		itemStartServer = new JMenuItem("Start server");
		itemStartServer.addActionListener(this);
		itemStartServer.setEnabled(true);
		
		itemStopServer = new JMenuItem("Stop server");
		itemStopServer.addActionListener(this);
		itemStopServer.setEnabled(false);
		
		itemResumeAccepting = new JMenuItem("Resume accepting connections");
		itemResumeAccepting.addActionListener(this);
		itemResumeAccepting.setEnabled(false);
		
		itemStopAccepting = new JMenuItem("Stop accepting connections");
		itemStopAccepting.setEnabled(false);
		itemStopAccepting.addActionListener(this);
		
		serverMnu.add(itemStartServer);
		serverMnu.add(itemStopServer);
		serverMnu.addSeparator();
		serverMnu.add(itemResumeAccepting);
		serverMnu.add(itemStopAccepting);
		
		return serverMnu;
	}
	
	private JScrollPane initLogPane() {
		
		lstLog = new DefaultListModel<String>();
		JList<String> lst = new JList<String>(lstLog);
		JScrollPane res = new JScrollPane(lst);
		
		return res;
	}

	public static void main(String[] args) {
		new ServerManager().setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(itemStartServer)) {
			performStartServer();
		}
		else if(e.getSource().equals(itemStopServer)) {
			performStopServer();
		}
		else if(e.getSource().equals(itemResumeAccepting)) {
			performResumeAccepting();
		}
		else if(e.getSource().equals(itemStopAccepting)) {
			performStopAccepting();
		}
		
	}

	public void performStartServer() {
		int port = Integer.valueOf(JOptionPane.showInputDialog(this, "Port to activate the server on:", "Starting server", JOptionPane.QUESTION_MESSAGE));
		Commons.PORT = port;
		new Thread(MessengerServer.getInstance()).start();
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setTitle(this.getTitle() + " - " + ip + " - " + port);
//		this.setTitle(this.getTitle() + " - " + port);
		this.itemStartServer.setEnabled(false);
		this.itemStopServer.setEnabled(true);
		this.itemResumeAccepting.setEnabled(false);
		this.itemStopAccepting.setEnabled(true);
	}
	
	public void performStopServer() {
		int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to stop the server?", "Messenger server", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if(res == JOptionPane.YES_OPTION) {
			MessengerServer.getInstance().setAcceptConnections(false);
			MessengerServer.getInstance().setRunningServer(false);
			this.itemStartServer.setEnabled(true);
			this.itemStopServer.setEnabled(false);
			this.itemResumeAccepting.setEnabled(false);
			this.itemStopAccepting.setEnabled(false);
			this.setTitle("Messenger Server Manager");
		}
	}
	
	public void performResumeAccepting() {
		MessengerServer.getInstance().setAcceptConnections(true);
		this.itemStartServer.setEnabled(false);
		this.itemStopServer.setEnabled(true);
		this.itemResumeAccepting.setEnabled(false);
		this.itemStopAccepting.setEnabled(true);
	}
	
	public void performStopAccepting() {
		int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to stop accepting connections?", "Stop server", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if(res == JOptionPane.YES_OPTION) {
			MessengerServer.getInstance().setAcceptConnections(false);
			this.itemStartServer.setEnabled(false);
			this.itemStopServer.setEnabled(true);
			this.itemResumeAccepting.setEnabled(true);
			this.itemStopAccepting.setEnabled(false);
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof MessengerServer) {
			String line = "MessengerServer: " + (String) arg;
			lstLog.addElement(line);
			LogServiceFactory.getLogService().writeLogLine(line);
		}
		else if(o instanceof ClientHandler) {
			String line = "ClientHandler: " + (String) arg;
			lstLog.addElement(line);
			LogServiceFactory.getLogService().writeLogLine(line);
		}
	}

}
