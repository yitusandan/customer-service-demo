package com.bs.csm.ui;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
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

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	private static final String TAG = "AuthenticatorActivity";

	public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
	public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
	public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
	public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
	public static final String PARAM_USER_PASS = "USER_PASS";

	private final int REQ_SIGNUP = 1;

	private AccountManager mAccountManager;
	private String mAuthTokenType;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_login);

		mAccountManager = AccountManager.get(getBaseContext());
		String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
		mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
		if (mAuthTokenType == null) {
			mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
		}

		((TextView) findViewById(R.id.tv_title)).setText("登陆");

		if (accountName != null) {
			((TextView) findViewById(R.id.accountName)).setText(accountName);
		}

		findViewById(R.id.submit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						submit();
					}
				});

		findViewById(R.id.signUp).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Since there can only be one AuthenticatorActivity, we
						// call the sign up activity, get his results,
						// and return them in setAccountAuthenticatorResult().
						// See finishLogin().
						Intent signup = new Intent(getBaseContext(),
								SignUpActivity.class);
						signup.putExtras(getIntent().getExtras());
						startActivityForResult(signup, REQ_SIGNUP);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// The sign up activity returned that the user has successfully created
		// an account
		if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
			finishLogin(data);
		} else
			super.onActivityResult(requestCode, resultCode, data);

	}

	public void submit() {

		final String accountname = ((TextView) findViewById(R.id.accountName))
				.getText().toString();
		final String password = ((TextView) findViewById(R.id.accountPassword))
				.getText().toString();
		final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

		RequestBody body = new FormEncodingBuilder()
				.add("accountname", accountname)
				.add("password", MD5.getHexStr(password)).build();
		Request request = new Request.Builder().url(Const.URL_LOGIN).post(body)
				.build();

		Utils.showDialog(this);

		OkHttp.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				Utils.dismissDialog();
				String resStr = arg0.body().string();

				Log.d("onResponse", resStr);
				final UserRes res = new Gson().fromJson(resStr, UserRes.class);
				if (res.isSuccess()) {
					User user = res.getUser();
					Bundle data = new Bundle();
					data.putString(AccountManager.KEY_ACCOUNT_NAME, accountname);
					data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
					data.putString(AccountManager.KEY_AUTHTOKEN,
							user.getSessionToken());
					data.putString(PARAM_USER_PASS, password);
					// Bundle userData = new Bundle();
					// // userData.putInt(AccountGeneral.USERDATA_USER_OBJ_ID,
					// // user.getId());
					// userData.putString(AccountGeneral.USERDATA_USER_OBJ_ID,
					// String.valueOf(user.getId()));
					// data.putBundle(AccountManager.KEY_USERDATA, userData);
					data.putInt("userId", user.getId());
					Log.d("tag", "userId = " + user.getId());
					Intent intent = new Intent();
					intent.putExtras(data);
					finishLogin(intent);
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(AuthenticatorActivity.this,
									res.getRetMsg(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Utils.dismissDialog();
				Log.d("onFailure", arg1.getMessage());
			}
		});

	}

	private void finishLogin(Intent intent) {
		Log.d("udinic", TAG + "> finishLogin");

		String accountName = intent
				.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
		String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
		final Account account = new Account(accountName,
				intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
		int userId = intent.getIntExtra("userId", 0);
		Const.userId = userId;
		Log.d("finishLogin tag ", "userId = " + userId);
		mAccountManager.setUserData(account,
				AccountGeneral.USERDATA_USER_OBJ_ID, String.valueOf(userId));
		if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
			Log.d("udinic", TAG + "> finishLogin > addAccountExplicitly");
			String authtoken = intent
					.getStringExtra(AccountManager.KEY_AUTHTOKEN);
			String authtokenType = mAuthTokenType;

			// Creating the account on the device and setting the auth token we
			// got
			// (Not setting the auth token will cause another call to the server
			// to authenticate the user)
			mAccountManager
					.addAccountExplicitly(account, accountPassword, null);
			mAccountManager.setAuthToken(account, authtokenType, authtoken);
		} else {
			Log.d("udinic", TAG + "> finishLogin > setPassword");
			mAccountManager.setPassword(account, accountPassword);
		}

		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}
}
