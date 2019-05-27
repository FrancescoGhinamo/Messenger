package common;

public class Commons {

	public static int PORT = 8080;
	public static final String CLOSE_REQUEST = "req=%&close_request&%eor";
	public static final String NEW_USER_NOTIFICATION = "not=%&contacts_new&%eon:";
	public static final String LOST_USER_NOTIFICATION = "not=%&contacts_lost&%eon:";
	public static final String ACTUAL_NAME_NOTIFICATION = "not=%&actual_name&%eon:";
	
	public static final String OBS_USER_CONNECTED = "New user connected: ";
	public static final String OBS_USE_LOST = " left chat";
	public static final String OBS_SERVER_STARTED = "Server started on port: ";
	public static final String OBS_NEW_CONNECTION = "New connection from IP: ";
	public static final String OBS_ERROR = "Error";
	public static final String OBS_SERVER_STOPPED = "Server stopped";
	
	public static String makeMessage(String source, String content) {
		return source + ":" + content;
	}
	
	public static String[] getData(String message) {
		System.out.println(message);
		int colonI = message.indexOf(":");
		String[] ris = new String[2];
		ris[0] = message.substring(0, colonI);
		ris[1] = message.substring(colonI + 1);
		return ris;
	}
}
