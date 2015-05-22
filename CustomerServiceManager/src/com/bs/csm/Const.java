package com.bs.csm;

public class Const {
	public static final String ADD_CUSTOMER_ACTION = "com.bs.csm.add_customer";
	public static final String UPDATE_CUSTOMER_ACTION = "com.bs.csm.update_customer";
	public static final String DELETE_CUSTOMER_ACTION = "com.bs.csm.delete_customer";

	public static final String ADD_SERVICE_ACTION = "com.bs.csm.add_service";
	public static final String UPDATE_SERVICE_ACTION = "com.bs.csm.update_service";
	public static final String DELETE_SERVICE_ACTION = "com.bs.csm.delete_service";

	public static final String HOST = "http://192.168.5.106:8080/bs.cm/mobile/";
//	public static final String HOST = "http://10.0.2.2:8080/bs.cm/mobile/";

	public static final String URL_LOGIN = HOST + "login.do";
	public static final String URL_SING_UP = HOST + "signup.do";
	public static final String URL_GET_ALL = HOST + "getAll.do";
	public static final String URL_ADD = HOST + "add.do";

	public static final String CONTENT_AUTHORITY = "com.bs.csm.provider";

	public static int userId = 0;
}
