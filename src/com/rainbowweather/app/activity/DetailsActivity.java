package com.rainbowweather.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;

import com.rainbowweather.app.R;
import com.rainbowweather.app.base.BaseActivity;

public class DetailsActivity extends BaseActivity {
	
	private TextView humidity;
	private TextView windDirect;
	private TextView windPower;
	
	private TextView chuanyi;
	private TextView ganmao;
	private TextView kongtiao;
	private TextView wuran;
	private TextView xiche;
	private TextView yundong;
	private TextView ziwaixian;
	
	private TextView pm25;
	private TextView pm10;
	private TextView quality;
	private TextView des;
	
	private int viewId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_layout);
		
		humidity = (TextView)findViewById(R.id.humidity);
		windDirect = (TextView)findViewById(R.id.windDirect);
		windPower = (TextView)findViewById(R.id.windPower);
		
		chuanyi = (TextView)findViewById(R.id.chuanyi);
		ganmao = (TextView)findViewById(R.id.ganmao);
		kongtiao = (TextView)findViewById(R.id.kongtiao);
		wuran = (TextView)findViewById(R.id.wuran);
		xiche = (TextView)findViewById(R.id.xiche);
		yundong = (TextView)findViewById(R.id.yundong);
		ziwaixian = (TextView)findViewById(R.id.ziwaixian);
		
		pm25 = (TextView)findViewById(R.id.pm25);
		pm10 = (TextView)findViewById(R.id.pm10);
		quality = (TextView)findViewById(R.id.quality);
		des = (TextView)findViewById(R.id.des);
		
		Intent intent = getIntent();
		viewId = intent.getIntExtra("viewId", 0);
		showDetailsWeather(viewId);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void showDetailsWeather(int viewId) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		humidity.setText(prefs.getString(viewId + "humidity", "") + "%RH");
		windDirect.setText(prefs.getString(viewId + "direct", ""));
		windPower.setText(prefs.getString(viewId + "power", ""));
		
		chuanyi.setText(prefs.getString(viewId + "chuanyi", ""));
		ganmao.setText(prefs.getString(viewId + "ganmao", ""));
		kongtiao.setText(prefs.getString(viewId + "kongtiao", ""));
		wuran.setText(prefs.getString(viewId + "wuran", ""));
		xiche.setText(prefs.getString(viewId + "xiche", ""));
		yundong.setText(prefs.getString(viewId + "yundong", ""));
		ziwaixian.setText(prefs.getString(viewId + "ziwaixian", ""));
		
		pm25.setText(prefs.getString(viewId + "pm25", ""));
		pm10.setText(prefs.getString(viewId + "pm10", ""));
		quality.setText(prefs.getString(viewId + "quality", ""));
		des.setText(prefs.getString(viewId + "des", ""));
	}

}
