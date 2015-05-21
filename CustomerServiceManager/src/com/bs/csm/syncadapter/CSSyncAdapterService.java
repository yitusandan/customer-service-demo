package com.bs.csm.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CSSyncAdapterService extends Service {

	private static final Object mSyncAdapterLock = new Object();
	private static CSSyncAdapter mSyncAdapter;

	@Override
	public void onCreate() {
		super.onCreate();
		synchronized (mSyncAdapterLock) {
			if (mSyncAdapter == null) {
				mSyncAdapter = new CSSyncAdapter(getApplicationContext(), true);
			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return mSyncAdapter.getSyncAdapterBinder();
	}

}
