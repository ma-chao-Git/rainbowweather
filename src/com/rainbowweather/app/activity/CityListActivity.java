package com.rainbowweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rainbowweather.app.R;
import com.rainbowweather.app.base.BaseActivity;
import com.rainbowweather.app.db.RainbowWeatherDB;
import com.rainbowweather.app.util.HttpCallbackListener;
import com.rainbowweather.app.util.HttpUtil;
import com.rainbowweather.app.util.Utility;

public class CityListActivity extends BaseActivity {

	private ArrayAdapter<String> adapter;
	private boolean isAllowBack;
	private List<String> cityList;
	private List<String> tempCityList;
	private String cityName;
	private EditText inputCity;
	private Button clearCity;
	private int RESULT_OK = 1;
	private ListView cityListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.citylist_layout);
		tempCityList = new ArrayList<String>();
		cityList = new ArrayList<String>();
		isAllowBack = true;
		RainbowWeatherDB db = RainbowWeatherDB.getInstance(this);
		
		Intent intent = getIntent();
		if (intent.getBooleanExtra("isNoSelectedCity", false)) {
			Toast.makeText(CityListActivity.this, "请选择一个城市", Toast.LENGTH_SHORT).show();
			isAllowBack = false;
		}
		
		inputCity = (EditText)findViewById(R.id.inputCity);
		clearCity = (Button)findViewById(R.id.clearCity);
		cityListView = (ListView)findViewById(R.id.cityList);
		
		ArrayList<String> aLTcityList = db.getCityInfoByTable("ALT_City");
		ArrayList<String> selectedCityList = db.getCityInfoByTable("City");
		for (String cityName : selectedCityList) {
			if (aLTcityList.contains(cityName)) {
				aLTcityList.remove(cityName);
			}
		}
		tempCityList.addAll(aLTcityList);
		cityList.addAll(tempCityList);
		
		inputCity.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				cityName = s.toString();
				if ("".equals(cityName)) {
					cityList.clear();
					cityList.addAll(tempCityList);
					adapter.notifyDataSetChanged();
					cityListView.setAdapter(adapter);
					return;
				}
				
				HttpUtil.sendHttpRequestByHttpURLConnection(cityName, new HttpCallbackListener() {
				
					@Override
					public void onFinish(String response) {
						boolean isExist = Utility.isCityExistByApiCheck(response);
						if (isExist) {
							cityList.clear();
							cityList.add(cityName);
							adapter.notifyDataSetChanged();
							cityListView.setAdapter(adapter);
						} else {
							cityList.clear();
							adapter.notifyDataSetChanged();
							cityListView.setAdapter(adapter);
						}
					}
					
					@Override
					public void onError(Exception e) {
					}
				});
			}
		});
		
		clearCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inputCity.setText("");
				
			}
		});
		
		adapter = new ArrayAdapter<String>(
				CityListActivity.this, android.R.layout.simple_list_item_1, cityList);
		
		cityListView.setAdapter(adapter);
		cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cityName = cityList.get(position).toString();
				Intent intent = getIntent();
				intent.putExtra("cityName", cityName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (!isAllowBack) {
			Toast.makeText(CityListActivity.this, "请选择一个城市", Toast.LENGTH_SHORT).show();
		} else {
			finish();
		}
	return;
	}
}