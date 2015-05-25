package com.bs.csm.ui;

import com.bs.csm.Const;
import com.bs.csm.R;
import com.bs.csm.db.ServiceTable;
import com.bs.csm.model.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateServiceActivity extends Activity implements
		View.OnClickListener {

	private TextView quitText;
	private TextView confirmText;
	private TextView titleText;

	private EditText nameEdit;
	private EditText priceEdit;
	private EditText descEdit;

	private Service service;

	private ServiceTable serviceTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update_service);
		serviceTable = new ServiceTable(this);

		quitText = (TextView) findViewById(R.id.btn_quit);
		confirmText = (TextView) findViewById(R.id.btn_confirm);
		titleText = (TextView) findViewById(R.id.tv_title);
		quitText.setOnClickListener(this);
		confirmText.setOnClickListener(this);
		titleText.setText("修改服务");

		service = getIntent().getParcelableExtra("service");

		nameEdit = (EditText) findViewById(R.id.et_name);
		priceEdit = (EditText) findViewById(R.id.et_price);
		descEdit = (EditText) findViewById(R.id.et_desc);
		nameEdit.setText(service.getName());

		if (!TextUtils.isEmpty(service.getPrice())) {
			priceEdit.setText(service.getPrice());
		}
		if (!TextUtils.isEmpty(service.getDesc())) {
			descEdit.setText(service.getDesc());
		}
		findViewById(R.id.btn_del).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_quit:
			onBackPressed();
			break;
		case R.id.btn_confirm:
			updateService();
			break;
		case R.id.btn_del:
			serviceTable.delete(service);
			sendBroadcast(new Intent(Const.DELETE_SERVICE_ACTION));
			finish();
			break;
		default:
			break;
		}
	}

	private void updateService() {
		String name = nameEdit.getText().toString();
		String price = priceEdit.getText().toString();
		String desc = descEdit.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "请输入服务名称", Toast.LENGTH_SHORT).show();
			nameEdit.requestFocus();
			return;
		}
		service.setName(name);
		service.setPrice(price);
		service.setDesc(desc);
		service.setUserid(Const.userId);

		ServiceTable serviceTable = new ServiceTable(this);
		serviceTable.update(service);
		sendBroadcast(new Intent(Const.UPDATE_SERVICE_ACTION));
		finish();
	}

}
