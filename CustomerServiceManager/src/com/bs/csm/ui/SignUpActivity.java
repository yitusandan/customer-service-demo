package com.bs.csm.ui;

import java.io.IOException;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.csm.Const;
import com.bs.csm.R;
import com.bs.csm.model.User;
import com.bs.csm.net.OkHttp;
import com.bs.csm.net.model.UserRes;
import com.bs.csm.util.AccountGeneral;
import com.bs.csm.util.MD5;
import com.bs.csm.util.Utils;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class SignUpActivity extends Activity {
	private String TAG = getClass().getSimpleName();
	private String mAccountType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		mAccountType = getIntent().getStringExtra(
				AuthenticatorActivity.ARG_ACCOUNT_TYPE);

		((TextView) findViewById(R.id.tv_title)).setText("注册");

		findViewById(R.id.alreadyMember).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		findViewById(R.id.submit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						createAccount();
					}
				});

	}

	private void createAccount() {

		final String name = ((TextView) findViewById(R.id.name)).getText()
				.toString().trim();
		final String accountName = ((TextView) findViewById(R.id.accountName))
				.getText().toString().trim();
		final String accountPassword = ((TextView) findViewById(R.id.accountPassword))
				.getText().toString().trim();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(accountName)) {
			Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(accountPassword)) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		RequestBody body = new FormEncodingBuilder().add("username", name)
				.add("accountname", accountName)
				.add("password", MD5.getHexStr(accountPassword)).build();
		Request request = new Request.Builder().url(Const.URL_SING_UP)
				.post(body).build();
		Utils.showDialog(this);
		OkHttp.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				Utils.dismissDialog();
				String resStr = arg0.body().string();
				Log.d(TAG, resStr);
				UserRes res = new Gson().fromJson(resStr, UserRes.class);
				if (res.isSuccess()) {
					User user = res.getUser();
					String authtoken = null;
					Bundle data = new Bundle();
					if (user != null)
						authtoken = user.getSessionToken();

					data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
					data.putString(AccountManager.KEY_ACCOUNT_TYPE,
							mAccountType);
					data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
					data.putString(AuthenticatorActivity.PARAM_USER_PASS,
							accountPassword);
					// We keep the user's object id as an extra data on the
					// account.
					// It's used later for determine ACL for the data we send to
					// the Parse.com service
					// Bundle userData = new Bundle();
					// userData.putInt(AccountGeneral.USERDATA_USER_OBJ_ID,
					// user.getId());
					// data.putBundle(AccountManager.KEY_USERDATA, userData);
					data.putInt("userId", user.getId());
					final Intent intent = new Intent();
					intent.putExtras(data);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(SignUpActivity.this, res.getRetMsg(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Utils.dismissDialog();
				Log.d(TAG, "arg1 = " + arg1.getMessage());
			}
		});

	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

}
