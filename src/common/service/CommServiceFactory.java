package common.service;

public class CommServiceFactory {

	public static ICommService getCommServiceInstance() {
		return (ICommService)new CommServiceImpl();
	}
}
