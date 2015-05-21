package com.bs.csm.adapter;

import java.util.List;

import com.bs.csm.R;
import com.bs.csm.model.Service;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ServiceAdapter extends BaseAdapter {
	private Context mContext;
	private List<Service> mList;

	public ServiceAdapter(Context context, List<Service> serviceList) {
		mContext = context;
		mList = serviceList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_servcie_listview, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Service service = mList.get(position);
		Log.d("tag", service.getName());
		viewHolder.nameText.setText(service.getName());
		viewHolder.descText.setText(service.getDesc());
		return convertView;
	}

	class ViewHolder {
		TextView nameText;
		TextView descText;

		public ViewHolder(View view) {
			nameText = (TextView) view.findViewById(R.id.tv_name);
			descText = (TextView) view.findViewById(R.id.tv_desc);
		}

	}

}
