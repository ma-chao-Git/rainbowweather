package com.rainbowweather.app.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbowweather.app.R;
import com.rainbowweather.app.base.BaseApplication;
import com.rainbowweather.app.util.HttpUtil;
import com.rainbowweather.app.util.RefreshableView;
import com.rainbowweather.app.util.RefreshableView.PullToRefreshListener;

public class WeatherFragment extends Fragment {

	RefreshableView refreshableView;

	public static final int SHOW_RESPONSE = 0;
	public static final int SHOW_RESPONSE_FAILED = 1;
	
	private ImageView backgroupImage;
	
	private TextView cityNameText;
	private TextView realtimeTempText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	
	private TextView group1Date;
	private TextView group1Temp;
	private ImageView group1Image;
	
	private TextView group2Date;
	private TextView group2Temp;
	private ImageView group2Image;

	private TextView group3Date;
	private TextView group3Temp;
	private ImageView group3Image;
 
	private TextView group4Date;
	private TextView group4Temp;
	private ImageView group4Image;

	private TextView group5Date;
	private TextView group5Temp;
	private ImageView group5Image;
	
	private View weatherView;
	
	private String cityName;
	
	private int viewId;
	
	private boolean loadOnce;
	
    static WeatherFragment newInstance(String cityName, int viewId) {
    	WeatherFragment newFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cityName", cityName);
        bundle.putInt("viewId", viewId);
        newFragment.setArguments(bundle);
        
        //bundle还可以在每个标签里传送数据
        return newFragment;

    }
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				showWeather();
				break;
			case SHOW_RESPONSE_FAILED:
				showWeatherFailed();
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		weatherView = inflater.inflate(R.layout.weather_layout, container, false);//关联布局文件
		Bundle args = getArguments();
		cityName = args.getString("cityName");
		viewId = args.getInt("viewId");
		initControls();
		if (!loadOnce) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sendHttpRequest(cityName, viewId);
				}
			}).start();
			
			loadOnce = true;
		} 
		showWeather();
		
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				sendHttpRequest(cityName, viewId);
				refreshableView.finishRefreshing();
			}
		}, viewId);

		return weatherView;
	}
	
	private void initControls() {
		backgroupImage = (ImageView)weatherView.findViewById(R.id.backgroup_image);
		
        cityNameText = (TextView)weatherView.findViewById(R.id.city_name);
        realtimeTempText = (TextView)weatherView.findViewById(R.id.realtimeTemp);
		weatherDespText = (TextView)weatherView.findViewById(R.id.weather_desp);

		temp1Text = (TextView)weatherView.findViewById(R.id.temp1);
		temp2Text = (TextView)weatherView.findViewById(R.id.temp2);
		
		group1Date = (TextView)weatherView.findViewById(R.id.group1_date);
		group1Image = (ImageView)weatherView.findViewById(R.id.group1_image);
		group1Temp = (TextView)weatherView.findViewById(R.id.group1_temp);
		
		group2Date = (TextView)weatherView.findViewById(R.id.group2_date);
		group2Image = (ImageView)weatherView.findViewById(R.id.group2_image);
		group2Temp = (TextView)weatherView.findViewById(R.id.group2_temp);

		group3Date = (TextView)weatherView.findViewById(R.id.group3_date);
		group3Image = (ImageView)weatherView.findViewById(R.id.group3_image);
		group3Temp = (TextView)weatherView.findViewById(R.id.group3_temp);

		group4Date = (TextView)weatherView.findViewById(R.id.group4_date);
		group4Image = (ImageView)weatherView.findViewById(R.id.group4_image);
		group4Temp = (TextView)weatherView.findViewById(R.id.group4_temp);

		group5Date = (TextView)weatherView.findViewById(R.id.group5_date);
		group5Image = (ImageView)weatherView.findViewById(R.id.group5_image);
		group5Temp = (TextView)weatherView.findViewById(R.id.group5_temp);
		
		refreshableView = (RefreshableView)weatherView.findViewById(R.id.refreshable_view);
	}
	
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
		
		backgroupImage.setImageResource(prefs.getInt(viewId + "backgroudImage", 0));
		cityNameText.setText(prefs.getString(viewId + "city_name", "") + "市");
		realtimeTempText.setText(prefs.getString(viewId + "realtimeTemp", "") + "℃");
		weatherDespText.setText(prefs.getString(viewId + "weather_desp", ""));
		temp1Text.setText(prefs.getString(viewId + "temp1", "") + "℃");
		temp2Text.setText(prefs.getString(viewId + "temp2", "") + "℃");
		
		group1Date.setText(prefs.getString(viewId + "group1Date", ""));
		group1Image.setImageResource(prefs.getInt(viewId + "group1resID", 0));
		group1Temp.setText(prefs.getString(viewId + "group1Temp", ""));
		
		group2Date.setText(prefs.getString(viewId + "group2Date", ""));
		group2Image.setImageResource(prefs.getInt(viewId + "group2resID", 0));
		group2Temp.setText(prefs.getString(viewId + "group2Temp", ""));

		group3Date.setText(prefs.getString(viewId + "group3Date", ""));
		group3Image.setImageResource(prefs.getInt(viewId + "group3resID", 0));
		group3Temp.setText(prefs.getString(viewId + "group3Temp", ""));

		group4Date.setText(prefs.getString(viewId + "group4Date", ""));
		group4Image.setImageResource(prefs.getInt(viewId + "group4resID", 0));
		group4Temp.setText(prefs.getString(viewId + "group4Temp", ""));

		group5Date.setText(prefs.getString(viewId + "group5Date", ""));
		group5Image.setImageResource(prefs.getInt(viewId + "group5resID", 0));
		group5Temp.setText(prefs.getString(viewId + "group5Temp", ""));
	}
	
	private void showWeatherFailed() {
		
		cityNameText.setText("刷新失败");
		realtimeTempText.setText("N/A");
		weatherDespText.setText("N/A");
		temp1Text.setText("N/A");
		temp2Text.setText("N/A");
		
		group1Date.setText("N/A");
		group1Image.setImageResource(R.drawable.icon_undefined);
		group1Temp.setText("N/A");
		
		group2Date.setText("N/A");
		group2Image.setImageResource(R.drawable.icon_undefined);
		group2Temp.setText("N/A");

		group3Date.setText("N/A");
		group3Image.setImageResource(R.drawable.icon_undefined);
		group3Temp.setText("N/A");

		group4Date.setText("N/A");
		group4Image.setImageResource(R.drawable.icon_undefined);
		group4Temp.setText("N/A");

		group5Date.setText("N/A");
		group5Image.setImageResource(R.drawable.icon_undefined);
		group5Temp.setText("N/A");
	}

	private void sendHttpRequest(String cityName, int viewId) {
		
		Message message = new Message();
		try {
			// JSON
			boolean isSuccess = HttpUtil.sendHttpRequestByHttpClient(cityName, viewId);
			if (isSuccess) {
				message.what = SHOW_RESPONSE;
				// 将服务器返回的结果存放到Message中
			} else {
				message.what = SHOW_RESPONSE_FAILED;
			}
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			message.what = SHOW_RESPONSE_FAILED;
			e.printStackTrace();
		}
		handler.sendMessage(message);
	}
	
}