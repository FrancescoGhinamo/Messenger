package server.backend.beam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import common.Commons;

@SuppressWarnings("deprecation")
public class MessengerServer extends Observable implements Runnable {

	private Observer obs;

	private boolean acceptConnections;
	private boolean runningServer;

	private volatile static MessengerServer me;

	private ServerSocket serverSocket;

	private volatile Hashtable<String, ClientHandler> connectedClients;
	
	private InetAddress address;

	public synchronized static MessengerServer getInstance() {
		if(me == null) {
			me = new MessengerServer();
		}
		return me;
	}

	private MessengerServer() {
		this.connectedClients = new Hashtable<String, ClientHandler>();
		this.acceptConnections = true;
		this.runningServer = true;
		this.address = null;
	}

	@Override
	public void run() {
		setAcceptConnections(true);
		
		try {
			serverSocket = new ServerSocket(Commons.PORT);
			address = serverSocket.getInetAddress();
			setChanged();
			notifyObservers(Commons.OBS_SERVER_STARTED + Commons.PORT);
			while(runningServer) {
				if(acceptConnections) {
					Socket s = null;
					try {
						s = serverSocket.accept();
						setChanged();
						notifyObservers(Commons.OBS_NEW_CONNECTION + s.getInetAddress().getHostAddress());
						ClientHandler c = new ClientHandler(s);
						if(obs != null) {
							c.addObserver(obs);
						}

						new Thread(c).start();
					}
					catch(IOException e) {
						System.out.println(e.getMessage());
						setChanged();
						notifyObservers(Commons.OBS_ERROR);
					}
				}

			}
			
			//close all the ClientHandler
			Set<String> keys = connectedClients.keySet();
			for(String s: keys) {
				connectedClients.get(s).parseCloseRequest();
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			setChanged();
			notifyObservers(Commons.OBS_ERROR);
		}
		finally {
			try {
				if(serverSocket != null) {
					serverSocket.close();
					setChanged();
					notifyObservers(Commons.OBS_SERVER_STOPPED);
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
				setChanged();
				notifyObservers(Commons.OBS_ERROR);
			}
		}


	}

	public void addObserver(Observer o) {
		super.addObserver(o);
		this.obs = o;
	}

	public boolean isAcceptConnections() {
		return acceptConnections;
	}

	public void setAcceptConnections(boolean acceptConnections) {
		this.acceptConnections = acceptConnections;
	}

	public synchronized Hashtable<String, ClientHandler> getConnectedClients() {
		return connectedClients;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public boolean isRunningServer() {
		return runningServer;
	}

	public void setRunningServer(boolean runningServer) {
		this.runningServer = runningServer;
	}

	public InetAddress getAddress() {
		return address;
	}

	






}
