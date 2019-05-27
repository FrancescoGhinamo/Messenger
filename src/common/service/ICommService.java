package common.service;

import java.net.Socket;

public interface ICommService {

	public byte[] readBytes(Socket s);
	
	public void writeBytes(Socket s, byte[] cont);
}
