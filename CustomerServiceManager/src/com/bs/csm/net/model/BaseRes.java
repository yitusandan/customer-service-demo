package com.bs.csm.net.model;

public class BaseRes {
	protected int retCode;
	protected String retMsg;

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public boolean isSuccess() {
		return retCode == 0;
	}
}
