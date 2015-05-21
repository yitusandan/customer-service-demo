package com.bs.csm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {

	private long id;
	private int userid;
	private String name;
	private String price;
	private String desc;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Service)) {
			return false;
		}

		Service service = (Service) o;

		if (userid != service.userid) {
			return false;
		}

		if (!name.equals(service.name)) {
			return false;
		}

		if (!price.equals(service.price)) {
			return false;
		}

		if (!desc.equals(service.desc)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int h = desc.hashCode();
		h = 31 * h + price.hashCode();
		h = 31 * h + name.hashCode();
		h = 31 * h + userid;
		h = 31 * h + (int) id;
		return h;
	}

	public Service() {

	}

	public static final Parcelable.Creator<Service> CREATOR = new Creator<Service>() {

		@Override
		public Service[] newArray(int size) {
			return new Service[size];
		}

		@Override
		public Service createFromParcel(Parcel source) {
			return new Service(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeInt(userid);
		dest.writeString(name);
		dest.writeString(price);
		dest.writeString(desc);
	}

	public Service(Parcel in) {
		id = in.readLong();
		userid = in.readInt();
		name = in.readString();
		price = in.readString();
		desc = in.readString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userId) {
		this.userid = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
