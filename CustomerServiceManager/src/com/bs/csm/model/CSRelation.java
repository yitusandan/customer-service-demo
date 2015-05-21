package com.bs.csm.model;

public class CSRelation {
	private long id;
	private long customerid;
	private int userid;
	private long serviceid;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof CSRelation)) {
			return false;
		}

		CSRelation relation = (CSRelation) o;

		if (userid != relation.userid) {
			return false;
		}

		if (customerid != relation.customerid) {
			return false;
		}

		if (serviceid != relation.serviceid) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int h = (int) serviceid;
		h = 31 * h + (int) customerid;
		h = 31 * h + (int) id;
		return h;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public long getServiceid() {
		return serviceid;
	}

	public void setServiceid(long serviceid) {
		this.serviceid = serviceid;
	}

}
