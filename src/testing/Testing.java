package testing;

import java.net.InetAddress;

import common.service.CommServiceFactory;
import common.service.ICommService;

import java.net.*;

public class Testing {

	public static void main(String[] args) {
		try {
			
			InetAddress addr = InetAddress.getLocalHost();
			ICommService c = CommServiceFactory.getCommServiceInstance();
			Socket s = new Socket(addr, 8080);
			
			c.writeBytes(s, "Pippo".getBytes());
			System.out.println(new String(c.readBytes(s)));
			c.writeBytes(s, "Pippo:test".getBytes());
			while(true) {
				String res = new String(c.readBytes(s));
				if(!res.equals("")) {
					System.out.println(res);
				}
			}
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
