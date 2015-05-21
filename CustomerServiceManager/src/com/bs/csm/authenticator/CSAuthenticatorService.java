package com.bs.csm.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CSAuthenticatorService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		CSAuthenticator csa = new CSAuthenticator(this);
		return csa.getIBinder();
	}

}
