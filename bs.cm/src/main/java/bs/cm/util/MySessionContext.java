package bs.cm.util;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class MySessionContext {
	private static HashMap<String, HttpSession> hashMap = new HashMap<String, HttpSession>();

	public static synchronized void addSession(HttpSession session) {
		hashMap.put(session.getId(), session);
	}

	public static synchronized void delSession(HttpSession session) {
		if (session != null) {
			hashMap.remove(session.getId());
		}
	}

	public static synchronized HttpSession getSession(String id) {
		if (id == null) {
			return null;
		}
		return hashMap.get(id);
	}

}
