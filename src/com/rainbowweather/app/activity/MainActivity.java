package com.rainbowweather.app.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.rainbowweather.app.R;
import com.rainbowweather.app.base.BaseApplication;
import com.rainbowweather.app.db.RainbowWeatherDB;
import com.rainbowweather.app.model.City;
import com.rainbowweather.app.util.ActivityCollector;
import com.rainbowweather.app.util.MyFragmentPagerAdapter;

public class MainActivity extends FragmentActivity {
	
	private RainbowWeatherDB db;
	private MyFragmentPagerAdapter adapter;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private ArrayList<City> cityList;
	private ImageView[] imageViews;
	private ImageView imageView;
	private int currIndex;//当前页卡编号
	private int bmpW;//横线图片宽度
	private int offset;//图片移动的偏移量
	private int RESULT_OK = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.viewpager_layout);
		
		mPager = (ViewPager)findViewById(R.id.viewPager);
		Button backButton = (Button)findViewById(R.id.back);
		Button detailsButton = (Button)findViewById(R.id.details);
		Button cityManagerButton = (Button)findViewById(R.id.cityManager);
		
		fragmentList = new ArrayList<Fragment>();
		db = RainbowWeatherDB.getInstance(this);
		
		cityList = db.getCityInfo();
		if (cityList.size() ==0 || cityList == null) {
			Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
			intent.putExtra("isNoSelectedCity", true);
			startActivityForResult(intent, 1);
		}
		// 
		createFragmentListByCityList();
		//给ViewPager设置适配器
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		
		InitViewPager();
	
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
				dialog.setTitle("警告");
				dialog.setMessage("真的要退出吗？");
				dialog.setCancelable(true);
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ActivityCollector.finishAll();
						finish();
					}
				});
				
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				dialog.show();
			}
		});
		
		detailsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	            Intent intent = new Intent(BaseApplication.getContext(), DetailsActivity.class);
	            intent.putExtra("viewId", currIndex);
	            (MainActivity.this).startActivity(intent);
			}
		});
		
		cityManagerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	            Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
	            startActivityForResult(intent, 1);
			}
		});
		
	}
	
	/**
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		InitImage();
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}
	
	/**
	 * 初始化图片的位移像素
	 */
	public void InitImage(){
		imageViews = new ImageView[fragmentList.size()];
	    ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
	    group.removeAllViews();
	    
	    for(int i=0; i<fragmentList.size(); i++){
	    	imageView = new ImageView(this);
	    	imageView.setLayoutParams(new LayoutParams(20,20));
	    	imageViews[i] = imageView;
	    	if(i == 0){
	    		imageView.setBackgroundResource(R.drawable.page_indicator_focused);
	    	}else{
	    		imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
	    	}
	    	
	    	group.addView(imageView);
	    }

	}

	public class MyOnPageChangeListener implements OnPageChangeListener{
		private int one = offset *2 +bmpW;//两个相邻页面的偏移量
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画
			currIndex = arg0;
			animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);//动画持续时间0.2秒
			setImageBackground(arg0%fragmentList.size());
		}
	}
	
	private void setImageBackground( int selectItems){
		for(int i=0; i<imageViews.length; i++){
			if(i == selectItems){
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
			}else{
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				createFragmentListByCityList();
				adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
				InitViewPager();
			}
			break;
		default:
			break;
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void createFragmentListByCityList() {
		fragmentList.clear();
		cityList = db.getCityInfo();
		for (City city : cityList) {
			fragmentList.add(WeatherFragment.newInstance(city.getCityName(), city.getViewId()));
		}
	}

}