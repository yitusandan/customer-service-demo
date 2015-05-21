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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddServiceActivity extends Activity implements
		View.OnClickListener {

	private TextView quitText;
	private TextView confirmText;
	private TextView titleText;

	private EditText nameEdit;
	private EditText priceEdit;
	private EditText descEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_service);

		quitText = (TextView) findViewById(R.id.btn_quit);
		confirmText = (TextView) findViewById(R.id.btn_confirm);
		titleText = (TextView) findViewById(R.id.tv_title);
		quitText.setOnClickListener(this);
		confirmText.setOnClickListener(this);
		titleText.setText("新建服务");

		nameEdit = (EditText) findViewById(R.id.et_name);
		priceEdit = (EditText) findViewById(R.id.et_price);
		descEdit = (EditText) findViewById(R.id.et_desc);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_quit:
			onBackPressed();
			break;
		case R.id.btn_confirm:
			addService();
		default:
			break;
		}
	}

	private void addService() {
		String name = nameEdit.getText().toString();
		String price = priceEdit.getText().toString();
		String desc = descEdit.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "请输入服务名称", Toast.LENGTH_SHORT).show();
			nameEdit.requestFocus();
			return;
		}
		Service service = new Service();
		service.setUserid(Const.userId);
		service.setName(name);
		service.setPrice(price);
		service.setDesc(desc);
		ServiceTable serviceTable = new ServiceTable(this);
		serviceTable.insert(service);

		sendBroadcast(new Intent(Const.ADD_SERVICE_ACTION));
		finish();
	}

}
