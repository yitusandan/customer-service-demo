package com.bs.csm.syncadapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bs.csm.Const;
import com.bs.csm.db.CSRelationTable;
import com.bs.csm.db.CustomerTable;
import com.bs.csm.db.ServiceTable;
import com.bs.csm.db.provider.CSRelationContract;
import com.bs.csm.db.provider.CustomerContract;
import com.bs.csm.db.provider.ServiceContract;
import com.bs.csm.model.CSRelation;
import com.bs.csm.model.Customer;
import com.bs.csm.model.Service;
import com.bs.csm.net.OkHttp;
import com.bs.csm.net.model.RemoteAllRes;
import com.bs.csm.util.AccountGeneral;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class CSSyncAdapter extends AbstractThreadedSyncAdapter {
	private final String TAG = "CSSyncAdapter";
	private final AccountManager mAccountManager;

	public CSSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mAccountManager = AccountManager.get(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
//		StringBuilder sb = new StringBuilder();
//		if (extras != null) {
//			for (String key : extras.keySet()) {
//				sb.append(key + "[" + extras.get(key) + "] ");
//			}
//		}
//
//		Log.d("udinic", TAG + "> onPerformSync for account[" + account.name
//				+ "]. Extras: " + sb.toString());

		try {
			String authToken = mAccountManager.blockingGetAuthToken(account,
					AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
			String userObjectId = mAccountManager.getUserData(account,
					AccountGeneral.USERDATA_USER_OBJ_ID);
			Log.d(TAG, "userId = " + Const.userId);
			Log.d(TAG, "authToken = " + authToken);
			Log.d(TAG, "userObjectId = " + userObjectId);

			RemoteAllRes remoteAllRes = getRemoteAll(authToken, Const.userId);

			List<Customer> remoteCustomerList = remoteAllRes.getCustomerList();
			List<Service> remoteServiceList = remoteAllRes.getServcieList();
			List<CSRelation> remoteRelationList = remoteAllRes
					.getCsrelationList();

			List<Customer> localCustomerList = new ArrayList<Customer>();
			Cursor customerCursor = provider.query(
					CustomerContract.CONTENT_URI, null, null, null, null);
			while (customerCursor.moveToNext()) {
				localCustomerList
						.add(CustomerTable.getCustomer(customerCursor));
			}
			customerCursor.close();

			List<Service> localServiceList = new ArrayList<Service>();
			Cursor serviceCursor = provider.query(ServiceContract.CONTENT_URI,
					null, null, null, null);
			while (serviceCursor.moveToNext()) {
				localServiceList.add(ServiceTable.getService(serviceCursor));
			}
			serviceCursor.close();

			List<CSRelation> localRelationList = new ArrayList<CSRelation>();
			Cursor relationCursor = provider.query(
					CSRelationContract.CONTENT_URI, null, null, null, null);
			while (relationCursor.moveToNext()) {
				localRelationList.add(CSRelationTable
						.getRelation(relationCursor));
			}
			relationCursor.close();

			List<Customer> customerToLocalList = new ArrayList<Customer>();
			for (Customer customer : remoteCustomerList) {
				if (!localCustomerList.contains(customer)) {
					customerToLocalList.add(customer);
				}
			}
			List<Customer> customerToRemoteList = new ArrayList<Customer>();
			for (Customer customer : localCustomerList) {
				if (!remoteCustomerList.contains(customer)) {
					customerToRemoteList.add(customer);
				}
			}

			List<Service> serviceToLocalList = new ArrayList<Service>();
			for (Service service : remoteServiceList) {
				if (!localServiceList.contains(service)) {
					serviceToLocalList.add(service);
				}
			}
			List<Service> serviceToRemoteList = new ArrayList<Service>();
			for (Service service : localServiceList) {
				if (!remoteServiceList.contains(service)) {
					serviceToRemoteList.add(service);
				}
			}

			List<CSRelation> relationToLocalList = new ArrayList<CSRelation>();
			for (CSRelation relation : remoteRelationList) {
				if (!localRelationList.contains(relation)) {
					relationToLocalList.add(relation);
				}
			}
			List<CSRelation> relationToRemoteList = new ArrayList<CSRelation>();
			for (CSRelation relation : localRelationList) {
				if (!remoteRelationList.contains(relation)) {
					relationToRemoteList.add(relation);
				}
			}

			for (Customer customer : customerToLocalList) {
				Uri uri = provider.insert(CustomerContract.CONTENT_URI,
						CustomerTable.getValues(customer));
				Log.d(TAG, "uri = " + uri.toString());
			}

			for (Service service : serviceToLocalList) {
				Uri uri = provider.insert(ServiceContract.CONTENT_URI,
						ServiceTable.getValues(service));
				Log.d(TAG, "uri = " + uri.toString());
			}

			for (CSRelation relation : relationToLocalList) {
				Uri uri = provider.insert(CSRelationContract.CONTENT_URI, CSRelationTable
						.getValues(relation.getCustomerid(),
								relation.getServiceid()));
				Log.d(TAG, "uri = " + uri.toString());
			}

			toRemoteAll(authToken, customerToRemoteList, serviceToRemoteList,
					relationToRemoteList);
			
			getContext().sendBroadcast(new Intent(Const.ADD_CUSTOMER_ACTION));
			getContext().sendBroadcast(new Intent(Const.ADD_SERVICE_ACTION));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void toRemoteAll(String token, List<Customer> customerList,
			List<Service> serviceList, List<CSRelation> relationList) {
		Gson gson = new Gson();
		String customerStr = gson.toJson(customerList);
		String serviceStr = gson.toJson(serviceList);
		String relationStr = gson.toJson(relationList);

		RequestBody body = new FormEncodingBuilder().add("token", token)
				.add("customerStr", customerStr).add("serviceStr", serviceStr)
				.add("relationStr", relationStr).build();
		Request request = new Request.Builder().url(Const.URL_ADD).post(body)
				.build();
		try {
			Response response = OkHttp.getClient().newCall(request).execute();
			int code = response.code();
			boolean bool = response.isSuccessful();
			String resStr = response.body().string();
			Log.d(TAG, "code = " + code + ", bool = " + bool + ", resStr = "
					+ resStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private RemoteAllRes getRemoteAll(String token, int userId) {
		try {
			RequestBody body = new FormEncodingBuilder().add("token", token)
					.add("userId", String.valueOf(userId)).build();
			Request request = new Request.Builder().url(Const.URL_GET_ALL)
					.post(body).build();
			Response response = OkHttp.getClient().newCall(request).execute();
			String str = response.body().string();
			int code = response.code();
			boolean bool = response.isSuccessful();
			Log.d(TAG, "code = " + code + ", bool = " + bool + ", str = " + str);

			RemoteAllRes res = new Gson().fromJson(str, RemoteAllRes.class);

			return res;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
