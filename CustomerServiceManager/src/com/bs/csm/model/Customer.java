package com.bs.csm.model;

public class Customer {
	private long id;
	private int userid;
	private String name;
	private String mobile;
	private String qq;
	private String address;
	private String remark;

	private String pinyin;
	private String initials;

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Customer)) {
			return false;
		}

		Customer other = (Customer) o;
		if (userid != other.userid) {
			return false;
		}

		if (!name.equals(other.name)) {
			return false;
		}

		if (!mobile.equals(other.mobile)) {
			return false;
		}

		if (!qq.equals(other.qq)) {
			return false;
		}

		if (!address.equals(other.address)) {
			return false;
		}

		if (!remark.equals(other.remark)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int h;
		h = remark.hashCode();
		h = 31 * h + address.hashCode();
		h = 31 * h + qq.hashCode();
		h = 31 * h + mobile.hashCode();
		h = 31 * h + name.hashCode();
		h = 31 * h + userid;
		h = 31 * h + (int) id;

		return h;
	}

	public long getId() {
		return id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userId) {
		this.userid = userId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinYin) {
		this.pinyin = pinYin;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

}
