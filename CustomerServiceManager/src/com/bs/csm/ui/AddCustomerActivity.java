package com.bs.csm.ui;

import java.util.ArrayList;
import java.util.List;

import com.bs.csm.Const;
import com.bs.csm.R;
import com.bs.csm.adapter.ServiceAdapter;
import com.bs.csm.db.CSRelationTable;
import com.bs.csm.db.CustomerTable;
import com.bs.csm.db.ServiceTable;
import com.bs.csm.model.Customer;
import com.bs.csm.model.Service;
import com.bs.csm.util.CharacterParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCustomerActivity extends Activity implements
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_customer);

		quitBtn = (TextView) findViewById(R.id.btn_quit);
		confirmBtn = (TextView) findViewById(R.id.btn_confirm);
		titleText = (TextView) findViewById(R.id.tv_title);

		quitBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		titleText.setText("新建客户");

		nameEdit = (EditText) findViewById(R.id.et_name);
		mobileEdit = (EditText) findViewById(R.id.et_mobile);
		qqEdit = (EditText) findViewById(R.id.et_qq);
		addressEdit = (EditText) findViewById(R.id.et_address);
		remarkEdit = (EditText) findViewById(R.id.et_remark);

		findViewById(R.id.btn_bind_service).setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.lv_selected);
		mAdapter = new ServiceAdapter(this, selectedList);
		mListView.setAdapter(mAdapter);
		
		ServiceTable serviceTable = new ServiceTable(this);
		serviceList = serviceTable.queryAll();
		if (serviceList == null || serviceList.size() == 0) {
			Toast.makeText(this, "没有可绑定的服务", Toast.LENGTH_SHORT).show();
			return;
		} else {
			int len = serviceList.size();
			boolArr = new boolean[len];
			nameArr = new String[len];
			for (int i = 0; i < len; i++) {
				boolArr[i] = false;
				nameArr[i] = serviceList.get(i).getName();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_quit:
			onBackPressed();
			break;
		case R.id.btn_confirm:
			addCustomer();
			break;
		case R.id.btn_bind_service:
			showChoice();
			return;
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
								Log.d("tag", i + " : " + boolArr[i]);
								selectedList.add(serviceList.get(i));
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

	private void addCustomer() {
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
		customer.setUserid(Const.userId);
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

		CustomerTable customerTable = new CustomerTable(this);
		long customerId = customerTable.insert(customer);
		CSRelationTable csRelationTable = new CSRelationTable(this);
		csRelationTable.insert(customerId, selectedList);

		sendBroadcast(new Intent(Const.ADD_CUSTOMER_ACTION));
		finish();
	}

}
