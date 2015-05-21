package com.bs.csm.authenticator;

import com.bs.csm.Const;
import com.bs.csm.model.User;
import com.bs.csm.net.OkHttp;
import com.bs.csm.net.model.UserRes;
import com.bs.csm.ui.AuthenticatorActivity;
import com.bs.csm.util.AccountGeneral;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class CSAuthenticator extends AbstractAccountAuthenticator {

	private static final String TAG = "CSAuthenticator";
	private Context mContext;

	public CSAuthenticator(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {

		final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
		intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);
		intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
		intent.putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
				response);

		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;

	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		// TODO Auto-generated method stub

		final AccountManager am = AccountManager.get(mContext);

		String authToken = am.peekAuthToken(account, authTokenType);
		int userId = 0; // User identifier, needed for creating ACL on our
						// server-side

		Log.d("udinic", TAG + "> peekAuthToken returned - " + authToken);

		// Lets give another try to authenticate the user
		if (TextUtils.isEmpty(authToken)) {
			final String password = am.getPassword(account);
			if (password != null) {
				try {
					Log.d("udinic", TAG
							+ "> re-authenticating with the existing password");
					// User user = sServerAuthenticate.userSignIn(account.name,
					// password, authTokenType);

					RequestBody body = new FormEncodingBuilder()
							.add("username", account.name)
							.add("password", password).build();
					Request request = new Request.Builder()
							.url(Const.URL_LOGIN).post(body).build();
					Response res = OkHttp.getClient().newCall(request)
							.execute();
					UserRes userRes = new Gson().fromJson(res.body().string(),
							UserRes.class);
					User user = userRes.getUser();
					if (user != null) {
						authToken = user.getSessionToken();
						userId = user.getId();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// If we get an authToken - we return it
		if (!TextUtils.isEmpty(authToken)) {
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
			result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			return result;
		}

		// If we get here, then we couldn't access the user's password - so we
		// need to re-prompt them for their credentials. We do that by creating
		// an intent to display our AuthenticatorActivity.
		final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
				response);
		intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, account.type);
		intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;

	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		if (AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
			return AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
		else if (AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType))
			return AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
		else
			return authTokenType + " (Label)";
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		final Bundle result = new Bundle();
		result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return result;
	}

}
