package com.bs.csm.ui;

import java.util.ArrayList;
import java.util.List;

import com.bs.csm.R;
import com.bs.csm.ui.fragment.CustomerFragment;
import com.bs.csm.ui.fragment.ServiceFragment;
import com.bs.csm.ui.fragment.UserFragment;
import com.bs.csm.widget.ColorTrackView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private String[] mTitles = { "个人", "客户", "服务" };
	private List<ColorTrackView> mTabs = new ArrayList<ColorTrackView>();
	private List<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mTabs.add((ColorTrackView) findViewById(R.id.ctv_user));
		mTabs.add((ColorTrackView) findViewById(R.id.ctv_customer));
		mTabs.add((ColorTrackView) findViewById(R.id.ctv_service));

		fragments = new ArrayList<Fragment>();
		fragments.add(new UserFragment());
		fragments.add(new CustomerFragment());
		fragments.add(new ServiceFragment());

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTitles.length;
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		};
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						if (arg1 > 0) {
							ColorTrackView left = mTabs.get(arg0);
							ColorTrackView right = mTabs.get(arg0 + 1);
							left.setDirection(1);
							right.setDirection(0);
							left.setProgress(1 - arg1);
							right.setProgress(arg1);
						}
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
	}

}
