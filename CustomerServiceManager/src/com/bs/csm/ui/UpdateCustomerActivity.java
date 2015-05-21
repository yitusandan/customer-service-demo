package com.bs.csm.ui;

import java.util.ArrayList;
import java.util.List;

import com.bs.csm.Const;
import com.bs.csm.R;
import com.bs.csm.adapter.ServiceAdapter;
import com.bs.csm.db.CSRelationTable;
import com.bs.csm.db.CustomerTable;
import com.bs.csm.db.ServiceTable;
import com.bs.csm.model.CSRelation;
import com.bs.csm.model.Customer;
import com.bs.csm.model.Service;
import com.bs.csm.util.CharacterParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCustomerActivity extends Activity implements
		View.OnClickListener {

	private EditText nameEdit;
	private EditText mobileEdit;
	private EditText qqEdit;
	private EditText addressEdit;
	private EditText remarkEdit;

	private TextView quitBtn;
	private TextView confirmBtn;
	private TextView titleText;

	private List<Service> serviceList;
	private boolean[] boolArr;
	private String[] nameArr;
	private List<Service> selectedList = new ArrayList<Service>();

	private ListView mListView;

	private ServiceAdapter mAdapter;

	private long customerId;

	private List<CSRelation> relationList;
	private List<Long> oldList = new ArrayList<Long>();
	private List<Long> newList = new ArrayList<Long>();

	private ServiceTable serviceTable;
	private CSRelationTable csRelationTable;
	private CustomerTable customerTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_customer);

		serviceTable = new ServiceTable(this);
		customerTable = new CustomerTable(this);
		csRelationTable = new CSRelationTable(this);

		quitBtn = (TextView) findViewById(R.id.btn_quit);
		confirmBtn = (TextView) findViewById(R.id.btn_confirm);
		titleText = (TextView) findViewById(R.id.tv_title);

		quitBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		titleText.setText("修改客户");

		customerId = getIntent().getLongExtra("customerId", 0);

		nameEdit = (EditText) findViewById(R.id.et_name);
		mobileEdit = (EditText) findViewById(R.id.et_mobile);
		qqEdit = (EditText) findViewById(R.id.et_qq);
		addressEdit = (EditText) findViewById(R.id.et_address);
		remarkEdit = (EditText) findViewById(R.id.et_remark);

		findViewById(R.id.btn_del).setOnClickListener(this);
		findViewById(R.id.btn_bind_service).setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.lv_selected);
		mAdapter = new ServiceAdapter(this, selectedList);
		mListView.setAdapter(mAdapter);

		serviceList = serviceTable.queryAll();
		if (serviceList == null || serviceList.size() == 0) {
			Toast.makeText(this, "没有可绑定的服务", Toast.LENGTH_SHORT).show();
		} else {
			int len = serviceList.size();
			boolArr = new boolean[len];
			nameArr = new String[len];
			for (int i = 0; i < len; i++) {
				boolArr[i] = false;
				nameArr[i] = serviceList.get(i).getName();
			}
		}

		relationList = csRelationTable.queryByCustomerId(customerId);

		for (int i = 0; i < serviceList.size(); i++) {
			Service service = serviceList.get(i);
			for (CSRelation relation : relationList) {
				if (service.getId() == relation.getServiceid()) {
					selectedList.add(service);
					boolArr[i] = true;
					oldList.add(service.getId());
					break;
				}
			}
		}
		mAdapter.notifyDataSetChanged();

		getCustomer();
	}

	private void getCustomer() {
		Customer customer = customerTable.getById(customerId);
		nameEdit.setText(customer.getName());
		mobileEdit.setText(customer.getMobile());
		qqEdit.setText(customer.getQq());
		addressEdit.setText(customer.getAddress());
		remarkEdit.setText(customer.getRemark());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_quit:
			onBackPressed();
			break;
		case R.id.btn_confirm:
			updateCustomer();
			break;
		case R.id.btn_bind_service:
			showChoice();
			return;
		case R.id.btn_del:
			customerTable.delete(customerId);
			csRelationTable.deleteByCustomerId(customerId);
			sendBroadcast(new Intent(Const.DELETE_CUSTOMER_ACTION));
			finish();
			break;
		default:
			break;
		}
	}

	private void showChoice() {

		new AlertDialog.Builder(this)
				.setTitle("请选择服务")
				.setMultiChoiceItems(nameArr, boolArr,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								boolArr[which] = isChecked;
							}
						})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						selectedList.clear();
						for (int i = 0; i < boolArr.length; i++) {
							if (boolArr[i]) {
								Service service = serviceList.get(i);
								selectedList.add(service);
								newList.add(service.getId());
							}
						}
						mAdapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();

	}

	private void updateCustomer() {
		String name = nameEdit.getText().toString();
		String mobile = mobileEdit.getText().toString();
		String qq = qqEdit.getText().toString();
		String address = addressEdit.getText().toString();
		String remark = remarkEdit.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			nameEdit.requestFocus();
			return;
		}

		if (TextUtils.isEmpty(mobile)) {
			Toast.makeText(this, "请输入手机", Toast.LENGTH_SHORT).show();
			mobileEdit.requestFocus();
			return;
		}

		Customer customer = new Customer();
		customer.setId(customerId);
		customer.setName(name);
		customer.setMobile(mobile);
		customer.setQq(qq);
		customer.setAddress(address);
		customer.setRemark(remark);
		CharacterParser parser = new CharacterParser();
		String pinYin = parser.getSelling(name);
		customer.setPinyin(pinYin);
		String initials = pinYin.substring(0, 1).toUpperCase();
		if (!initials.matches("[A-Z]")) {
			initials = "#";
		}
		customer.setInitials(initials);

		customerTable.update(customer);

		List<Long> tempList = new ArrayList<Long>(oldList);
		oldList.removeAll(newList);
		newList.removeAll(tempList);
		csRelationTable.delete(customerId, oldList);
		csRelationTable.insert2(customerId, newList);

		sendBroadcast(new Intent(Const.UPDATE_CUSTOMER_ACTION));
		finish();
	}

}
