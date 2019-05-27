package server.backend.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogServiceImpl implements ILogService {

	public synchronized void writeLogLine(String line) {
		
		File f = new File("log\\serverLog.txt");
		FileWriter fW = null;

		try {
			fW = new FileWriter(f, true);
			fW.write(line);
			fW.write("\r\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(fW != null) {
				if(fW != null) {
					try {
						fW.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						try {
							fW.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		}
	}
}
