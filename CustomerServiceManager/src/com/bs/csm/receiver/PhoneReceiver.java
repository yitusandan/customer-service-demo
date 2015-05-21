package com.bs.csm.receiver;

import com.bs.csm.R;
import com.bs.csm.db.CSRelationTable;
import com.bs.csm.db.CustomerTable;
import com.bs.csm.db.ServiceTable;
import com.bs.csm.db.provider.CSRelationContract;
import com.bs.csm.db.provider.CustomerContract;
import com.bs.csm.db.provider.ServiceContract;
import com.bs.csm.model.Customer;
import com.bs.csm.ui.UpdateCustomerActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

public class PhoneReceiver extends BroadcastReceiver {
	private static final String TAG = "PhoneReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			// String phoneNum =
			// intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		} else {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
			tm.listen(new PhoneStateListener() {
				@Override
				public void onCallStateChanged(int state, String incomingNumber) {
					super.onCallStateChanged(state, incomingNumber);
					switch (state) {
					case TelephonyManager.CALL_STATE_IDLE:
						break;
					case TelephonyManager.CALL_STATE_OFFHOOK:
						break;
					case TelephonyManager.CALL_STATE_RINGING:
						showNotification(context, incomingNumber);
						break;
					default:
						break;
					}
				}
			}, PhoneStateListener.LISTEN_CALL_STATE);
		}

	}

	private void showNotification(Context context, String incomingNumber) {

		Cursor cursor = context.getContentResolver().query(
				CustomerContract.CONTENT_URI, null,
				CustomerTable.COLUMN_MOBILE + " = ?",
				new String[] { incomingNumber }, null);

		if (cursor.moveToNext()) {
			Customer customer = CustomerTable.getCustomer(cursor);

			String title = customer.getName();

			String content = getContent(context, customer.getId());

			NotificationManager nm = (NotificationManager) context
					.getSystemService(Service.NOTIFICATION_SERVICE);
			Intent intent = new Intent(context, UpdateCustomerActivity.class);
			intent.putExtra("phoneNum", incomingNumber);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
					intent, Notification.FLAG_AUTO_CANCEL);
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.notification_view);
			views.setImageViewResource(R.id.custom_icon, R.drawable.ic_launcher);
			views.setTextViewText(R.id.tv_custom_title, title);
			views.setTextViewText(R.id.tv_custom_time,
					new Time().format("mm.ss"));
			views.setTextViewText(R.id.tv_custom_content, content);

			NotificationCompat.Builder builder = new Builder(context);
			builder.setContent(views).setContentIntent(pendingIntent)
					.setPriority(Notification.PRIORITY_DEFAULT)
					.setDefaults(Notification.DEFAULT_VIBRATE)
					.setSmallIcon(R.drawable.ic_launcher);
			nm.notify(0, builder.build());

		}
	}

	private String getContent(Context context, long customerId) {
		Cursor c2 = context.getContentResolver().query(
				CSRelationContract.CONTENT_URI,
				new String[] { CSRelationTable.COLUMN_SERVICE_ID },
				CSRelationTable.COLUMN_CUSTOMER_ID + " = ?",
				new String[] { String.valueOf(customerId) }, null);
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("推荐服务：");
		if (c2.getCount() == 0) {
			strBuilder.append("无");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			while (c2.moveToNext()) {
				long serviceId = c2.getLong(c2
						.getColumnIndex(CSRelationTable.COLUMN_SERVICE_ID));
				sb.append(serviceId).append(",");
			}
			String args = sb.substring(0, sb.length() - 1) + ")";
			Cursor c3 = context.getContentResolver().query(
					ServiceContract.CONTENT_URI,
					new String[] { ServiceTable.COLUMN_NAME },
					ServiceTable.COLUMN_ID + " in ?", new String[] { args },
					null);
			while (c3.moveToNext()) {
				String serviceName = c3.getString(c3
						.getColumnIndex(ServiceTable.COLUMN_NAME));
				strBuilder.append(serviceName).append("  ");
			}
		}
		return strBuilder.toString();
	}
}
