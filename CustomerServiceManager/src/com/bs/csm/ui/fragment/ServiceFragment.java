package com.bs.csm.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bs.csm.Const;
import com.bs.csm.R;
import com.bs.csm.adapter.ServiceAdapter;
import com.bs.csm.db.ServiceTable;
import com.bs.csm.model.Service;
import com.bs.csm.ui.AddServiceActivity;
import com.bs.csm.ui.UpdateServiceActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class ServiceFragment extends Fragment {

	private ListView mListView;
	private Button addBtn;
	private ServiceAdapter mAdapter;
	private List<Service> serviceList;
	private ServiceTable serviceTable;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_service, null);
		serviceTable = new ServiceTable(getActivity());
		
		mListView = (ListView) view.findViewById(R.id.lv_service);
		addBtn = (Button) view.findViewById(R.id.btn_add);
		serviceList = new ArrayList<Service>();
		mAdapter = new ServiceAdapter(getActivity(), serviceList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Service service = (Service) parent.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(),
						UpdateServiceActivity.class);
				intent.putExtra("service", service);
				startActivity(intent);
			}
		});
		
		addBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						AddServiceActivity.class));
			}
		});

		IntentFilter filter = new IntentFilter();
		filter.addAction(Const.ADD_SERVICE_ACTION);
		filter.addAction(Const.UPDATE_SERVICE_ACTION);
		filter.addAction(Const.DELETE_SERVICE_ACTION);
		getActivity().registerReceiver(mReceiver, filter);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		queryAll();
	}

	private void queryAll() {
		List<Service> list = serviceTable.queryAll();
		serviceList.clear();
		serviceList.addAll(list);
		mAdapter.notifyDataSetChanged();
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Const.UPDATE_SERVICE_ACTION)
					|| intent.getAction().equals(Const.ADD_SERVICE_ACTION)
					|| intent.getAction().equals(Const.DELETE_SERVICE_ACTION)) {
				queryAll();
			}
		}
	};

	public void onDestroy() {
		getActivity().unregisterReceiver(mReceiver);
		super.onDestroy();
	};

}
