package server.backend.service;

public class LogServiceFactory {

	public static ILogService getLogService() {
		return (ILogService)new LogServiceImpl();
	}
}
