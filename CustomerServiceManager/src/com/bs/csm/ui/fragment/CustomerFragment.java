package com.bs.csm.ui.fragment;

import java.util.Collections;
import java.util.List;

import com.bs.csm.Const;
import com.bs.csm.R;
import com.bs.csm.adapter.SortAdapter;
import com.bs.csm.db.CustomerTable;
import com.bs.csm.model.Customer;
import com.bs.csm.ui.AddCustomerActivity;
import com.bs.csm.ui.UpdateCustomerActivity;
import com.bs.csm.util.InitialsComparator;
import com.bs.csm.widget.ClearEditText;
import com.bs.csm.widget.SideBar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerFragment extends Fragment {

	private ListView mListView;
	private SideBar mSideBar;
	private TextView dialogText;
	private SortAdapter mSortAdapter;
	private ClearEditText mClearEditText;

	private List<Customer> customerList;
	private InitialsComparator mComparator;
	private CustomerTable customerTable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_customer, container,
				false);

		mComparator = new InitialsComparator();
		customerTable = new CustomerTable(getActivity());
		customerList = customerTable.query(null);

		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				queryCustomers(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		mListView = (ListView) view.findViewById(R.id.lv_customer);
		dialogText = (TextView) view.findViewById(R.id.dialog);
		mSideBar = (SideBar) view.findViewById(R.id.side_bar);
		mSideBar.setTextView(dialogText);
		mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = mSortAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});

		Collections.sort(customerList, mComparator);
		mSortAdapter = new SortAdapter(getActivity(), customerList);
		mListView.setAdapter(mSortAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Customer customer = (Customer) parent
						.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(),
						UpdateCustomerActivity.class);
				intent.putExtra("customerId", customer.getId());
				startActivity(intent);
			}
		});

		view.findViewById(R.id.btn_add).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(getActivity(),
								AddCustomerActivity.class));
					}
				});

		IntentFilter filter = new IntentFilter();
		filter.addAction(Const.ADD_CUSTOMER_ACTION);
		filter.addAction(Const.UPDATE_CUSTOMER_ACTION);
		filter.addAction(Const.DELETE_CUSTOMER_ACTION);
		getActivity().registerReceiver(mReceiver, filter);

		return view;
	}

	private void queryCustomers(String query) {
		customerList = customerTable.query(query);
		mSortAdapter.updateListView(customerList);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Const.ADD_CUSTOMER_ACTION)
					|| intent.getAction().equals(Const.UPDATE_CUSTOMER_ACTION)
					|| intent.getAction().equals(Const.DELETE_CUSTOMER_ACTION)) {
				queryCustomers(null);
			}
		}
	};

	public void onDestroy() {
		getActivity().unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}
