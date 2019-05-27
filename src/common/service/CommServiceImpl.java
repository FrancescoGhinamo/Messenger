package common.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class CommServiceImpl implements ICommService {
	
	private static final byte FLOW_SEPARATOR = (byte) 216;

	CommServiceImpl() {
		
	}
	
	@Override
	public byte[] readBytes(Socket s) {
		InputStream dis = null;
		ByteArrayOutputStream bArrStr = new ByteArrayOutputStream();
		try {
			dis = s.getInputStream();
			
//			byte[] buffer = new byte[4096];
//			int nRead = 0;
//			
//
//		
//			do {
//				if((nRead = dis.read(buffer, 0, buffer.length)) != -1) {
//					bArrStr.write(buffer, 0, nRead);
//				}
//			}
//			while(nRead == buffer.length);
			
			byte[] read = new byte[1];
			
			while(dis.read(read, 0, read.length) != -1) {
				if(read[0] != FLOW_SEPARATOR) {
					bArrStr.write(read[0]);
				}
				else {
					break;
				}
			}
			
			
		
			
		} catch (IOException e) {
			
		}
		
		
		
		return bArrStr.toByteArray();
	}

	@Override
	public synchronized void writeBytes(Socket s, byte[] cont) {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(s.getOutputStream());

			dos.write(cont);
			dos.write(FLOW_SEPARATOR);
			dos.flush();
			
		} catch (IOException e) {
			
		}
		
		
	}

}
