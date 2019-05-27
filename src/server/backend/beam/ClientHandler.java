package server.backend.beam;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Set;

import common.Commons;
import common.service.CommServiceFactory;
import common.service.ICommService;

@SuppressWarnings("deprecation")
public class ClientHandler extends Observable implements Runnable {

	private Socket socket;

	private String clientName;

	private boolean stop;

	private ICommService commService;

	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
		this.clientName = "No-name";
		this.stop = false;
		this.commService = CommServiceFactory.getCommServiceInstance();
	}




	public void run() {
		String _name = new String(commService.readBytes(socket));
		Set<String> keys = MessengerServer.getInstance().getConnectedClients().keySet();
		for(String h: keys) {
			if(h.equals(_name)) {
				_name = _name + "1";
			}
		}
		this.clientName = _name;


		setChanged();
		notifyObservers(Commons.OBS_USER_CONNECTED + clientName);


		keys = MessengerServer.getInstance().getConnectedClients().keySet();
		for(String h: keys) {
			MessengerServer.getInstance().getConnectedClients().get(h).notifyNewUser(clientName);
		}

		MessengerServer.getInstance().getConnectedClients().put(clientName, this);


		commService.writeBytes(socket, (Commons.ACTUAL_NAME_NOTIFICATION + clientName).getBytes());
		//QUI INVIO ROBACCIA..METTERE APPOSTO in debug funziona....
		//va gia meglio ma se uso due pc diversi da ancora problemi
		//potrebbe essere che invio più veloce di quanto il client riceva e dunque tutto si accoda e diventa come un unico flusso


//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		for(String h: keys) {

			notifyNewUser(h);
		}

		while(!stop) {
			String req = new String(commService.readBytes(socket));
			if(!req.equals("")) {
				parseRequest(req);
			}

		}


	}

	private void parseRequest(String req) {
		if(req.equals(Commons.CLOSE_REQUEST)) {
			parseCloseRequest();
		}
		else {

			parseSendMessage(req);


		}
	}
	
	public void parseCloseRequest() {
		try {

			MessengerServer.getInstance().getConnectedClients().remove(clientName);
			Set<String> keys = MessengerServer.getInstance().getConnectedClients().keySet();
			for(String h: keys) {
				MessengerServer.getInstance().getConnectedClients().get(h).notifyLostUser(clientName);
			}
			socket.close();

			setChanged();
			notifyObservers(clientName + Commons.OBS_USE_LOST);



		} catch (IOException e) {
			setChanged();
			notifyObservers(Commons.OBS_ERROR);
		}
		finally {
			stop = true;
		}
	}

	private void parseSendMessage(String ms) {
		String[] mesSpec = Commons.getData(ms);

		String dest = mesSpec[0];
		String content = mesSpec[1];

		String msg = Commons.makeMessage(this.clientName, content);

		setChanged();
		notifyObservers(clientName + " sent " + msg.length() + " byte of data to " + dest);

		MessengerServer.getInstance().getConnectedClients().get(dest).sendMessage(msg);
	}



	public synchronized void sendMessage(String content) {
		commService.writeBytes(socket, content.getBytes());
	}

	public synchronized void notifyNewUser(String name) {
		String not = Commons.NEW_USER_NOTIFICATION + name;

		commService.writeBytes(socket, not.getBytes());
	}

	public synchronized void notifyLostUser(String name) {
		commService.writeBytes(socket, (Commons.LOST_USER_NOTIFICATION + name).getBytes());
	}


	public String getClientName() {
		return clientName;
	}

	public String toString() {
		return this.clientName;
	}



}
