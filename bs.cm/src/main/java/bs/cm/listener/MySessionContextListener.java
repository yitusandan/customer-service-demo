package bs.cm.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import bs.cm.util.MySessionContext;

public class MySessionContextListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent paramHttpSessionEvent) {
		// TODO Auto-generated method stub
		System.out.println("sessionCreated...");
		MySessionContext.addSession(paramHttpSessionEvent.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent paramHttpSessionEvent) {
		// TODO Auto-generated method stub
		System.out.println("sessionDestroyed...");
		MySessionContext.delSession(paramHttpSessionEvent.getSession());
	}

}
