package com.bs.csm.ui.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.csm.Const;
import com.bs.csm.R;
import com.bs.csm.util.AccountGeneral;

public class UserFragment extends Fragment {

	private String TAG = this.getClass().getSimpleName();
	private AccountManager mAccountManager;
	private String authToken = null;
	private Account mConnectedAccount;

	SyncStatusObserver syncObserver = new SyncStatusObserver() {
		@Override
		public void onStatusChanged(final int which) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refreshSyncStatus();
				}
			});
		}
	};

	Object handleSyncObserver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		mAccountManager = AccountManager.get(getActivity());

		getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE,
				AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);

		view.findViewById(R.id.btnSync).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (mConnectedAccount == null) {
							Toast.makeText(getActivity(),
									"Please connect first", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						Bundle bundle = new Bundle();
						bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,
								true);
						// Performing a sync no matter if it's off
						bundle.putBoolean(
								ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
						ContentResolver.requestSync(mConnectedAccount,
								Const.CONTENT_AUTHORITY, bundle);
					}
				});

		((CheckBox) view.findViewById(R.id.cbIsSyncable))
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (mConnectedAccount == null) {
							Toast.makeText(getActivity(),
									"Please connect first", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						// Setting the syncable state of the sync adapter
						String authority = Const.CONTENT_AUTHORITY;
						ContentResolver.setIsSyncable(mConnectedAccount,
								authority, isChecked ? 1 : 0);
					}
				});

		((CheckBox) view.findViewById(R.id.cbAutoSync))
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (mConnectedAccount == null) {
							Toast.makeText(getActivity(),
									"Please connect first", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						// Setting the autosync state of the sync adapter
						String authority = Const.CONTENT_AUTHORITY;
						ContentResolver.setSyncAutomatically(mConnectedAccount,
								authority, isChecked);
					}
				});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		handleSyncObserver = ContentResolver.addStatusChangeListener(
				ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE
						| ContentResolver.SYNC_OBSERVER_TYPE_PENDING,
				syncObserver);
	}

	@Override
	public void onPause() {
		if (handleSyncObserver != null)
			ContentResolver.removeStatusChangeListener(handleSyncObserver);
		super.onStop();
	}

	/**
	 * Get an auth token for the account. If not exist - add it and then return
	 * its auth token. If one exist - return its auth token. If more than one
	 * exists - show a picker and return the select account's auth token.
	 * 
	 * @param accountType
	 * @param authTokenType
	 */
	private void getTokenForAccountCreateIfNeeded(String accountType,
			String authTokenType) {
		Log.d(TAG, "getTokenForAccountCreateIfNeeded");
		final AccountManagerFuture<Bundle> future = mAccountManager
				.getAuthTokenByFeatures(accountType, authTokenType, null,
						getActivity(), null, null,
						new AccountManagerCallback<Bundle>() {
							@Override
							public void run(AccountManagerFuture<Bundle> future) {
								Bundle bundle = null;
								try {
									bundle = future.getResult();
									authToken = bundle
											.getString(AccountManager.KEY_AUTHTOKEN);
									Log.d(TAG, "authToken = " + authToken);
									if (authToken != null) {
										String accountName = bundle
												.getString(AccountManager.KEY_ACCOUNT_NAME);
										Log.d(TAG, "accountName = "
												+ accountName);
										mConnectedAccount = new Account(
												accountName,
												AccountGeneral.ACCOUNT_TYPE);
										initButtonsAfterConnect();
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}, null);
	}

	private void initButtonsAfterConnect() {
		String authority = Const.CONTENT_AUTHORITY;

		// Get the syncadapter settings and init the checkboxes accordingly
		int isSyncable = ContentResolver.getIsSyncable(mConnectedAccount,
				authority);
		boolean autSync = ContentResolver.getSyncAutomatically(
				mConnectedAccount, authority);

		((CheckBox) getView().findViewById(R.id.cbIsSyncable))
				.setChecked(isSyncable > 0);
		((CheckBox) getView().findViewById(R.id.cbAutoSync))
				.setChecked(autSync);

		getView().findViewById(R.id.cbIsSyncable).setEnabled(true);
		getView().findViewById(R.id.cbAutoSync).setEnabled(true);
		getView().findViewById(R.id.status).setEnabled(true);
		getView().findViewById(R.id.btnSync).setEnabled(true);

		refreshSyncStatus();
	}

	private void refreshSyncStatus() {
		String status;

		if (ContentResolver.isSyncActive(mConnectedAccount,
				Const.CONTENT_AUTHORITY))
//			status = "Status: Syncing..";
			status = "状态：同步...";
		else if (ContentResolver.isSyncPending(mConnectedAccount,
				Const.CONTENT_AUTHORITY))
//			status = "Status: Pending..";
			status = "状态：挂起...";
		else
//			status = "Status: Idle";
			status = "状态：闲置";

		((TextView) getView().findViewById(R.id.status)).setText(status);
	}
}
