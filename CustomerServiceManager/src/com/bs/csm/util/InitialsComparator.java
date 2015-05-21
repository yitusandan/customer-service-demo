package com.bs.csm.util;

import java.util.Comparator;

import android.text.TextUtils;

import com.bs.csm.model.Customer;

public class InitialsComparator implements Comparator<Customer>{

	@Override
	public int compare(Customer lhs, Customer rhs) {
		if (TextUtils.isEmpty(lhs.getInitials()) || TextUtils.isEmpty(rhs.getInitials())) {
			return 0;
		}
		if ("@".equals(lhs.getInitials()) || "#".equals(rhs.getInitials())) {
			return -1;
		} else if ("#".equals(lhs.getInitials()) || "@".equals(rhs.getInitials())) {
			return 1;
		} else {
			return lhs.getInitials().compareTo(rhs.getInitials());
		}
	}
	
}
