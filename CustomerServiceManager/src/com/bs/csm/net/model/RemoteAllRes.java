package com.bs.csm.net.model;

import java.util.List;

import com.bs.csm.model.CSRelation;
import com.bs.csm.model.Customer;
import com.bs.csm.model.Service;

public class RemoteAllRes extends BaseRes {
	private List<Customer> customerList;

	private List<Service> servcieList;

	private List<CSRelation> csrelationList;

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	public List<Service> getServcieList() {
		return servcieList;
	}

	public void setServcieList(List<Service> servcieList) {
		this.servcieList = servcieList;
	}

	public List<CSRelation> getCsrelationList() {
		return csrelationList;
	}

	public void setCsrelationList(List<CSRelation> csrelationList) {
		this.csrelationList = csrelationList;
	}

}
