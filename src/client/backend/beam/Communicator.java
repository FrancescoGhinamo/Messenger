package client.backend.beam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

import common.Commons;
import common.service.CommServiceFactory;
import common.service.ICommService;

@SuppressWarnings("deprecation")
public class Communicator extends Observable implements Runnable {
	
	private Socket channel;
	
	private String myName;
	
	private boolean stopped;
	
	private ICommService comm;
	
	private static Communicator me;
	
	public static Communicator getInstance(InetAddress address, int port, String name) throws IOException {
		if(me == null) {
			try {
				me = new Communicator(address, port, name);
			} catch (IOException e) {
				throw e;
			}
		}
		return me;
	}
	
	public static Communicator getInstance() {
		
		return me;
	}

	private Communicator(InetAddress address, int port, String name) throws IOException {
		super();
		try {
			this.channel = new Socket(address, port);
			this.myName = name;
			this.stopped = false;
			this.comm = CommServiceFactory.getCommServiceInstance();
		} catch (IOException e) {
			throw e;
		}
		
	}
	
	
	



	@Override
	public void run() {
		this.stopped = false;
		//share my name:
		comm.writeBytes(channel, myName.getBytes());
		//my actual name:
		String _r = new String(comm.readBytes(channel));
		myName = _r.substring(_r.indexOf(":") + 1);
		
		setChanged();
		notifyObservers(Commons.ACTUAL_NAME_NOTIFICATION + myName);
		
		while(!stopped) {
			String res = new String(comm.readBytes(channel));
			setChanged();
			notifyObservers(res);
		}
		
		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMessage(String dest, String mesCont) {
		String mes = Commons.makeMessage(dest, mesCont);
		comm.writeBytes(channel, mes.getBytes());
		
	}




	public void setStopped(boolean stopped) {
		comm.writeBytes(channel, Commons.CLOSE_REQUEST.getBytes());
		this.stopped = stopped;
	}
	
	

}
