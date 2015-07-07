package com.rainbowweather.app.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rainbowweather.app.R;
import com.rainbowweather.app.base.BaseActivity;
import com.rainbowweather.app.db.RainbowWeatherDB;

public class CityManagerActivity extends BaseActivity {

	ArrayAdapter<String> adapter;
	private ArrayList<String> cityManagerList;
	private String resultCityName;
	private RainbowWeatherDB db;
	private int RESULT_OK = 1;
	private int itemIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_manager_layout);
		
		cityManagerList = new ArrayList<String>();
		db = RainbowWeatherDB.getInstance(this);
		cityManagerList = db.getCityInfoByTable("City");

		Button back = (Button) findViewById(R.id.back);
		Button about = (Button) findViewById(R.id.about);
		Button addCity = (Button) findViewById(R.id.addCity);
		ListView cityListView = (ListView) findViewById(R.id.cityManagerList);
		
		Intent intent = getIntent();
		if (intent.getBooleanExtra("isNoSelectedCity", false)) {
			intent = new Intent(CityManagerActivity.this, CityListActivity.class);
			intent.putExtra("isNoSelectedCity", true);
			startActivityForResult(intent, 0);
		}

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder dialog = new AlertDialog.Builder(CityManagerActivity.this);
				dialog.setTitle("关于彩虹天气");
				dialog.setMessage("本软件完全免费，无后台服务，无内置广告，即开即用，随退随关。" +
						"\n\n后台API提供商：聚合数据\n作者：ma_chao\n联系方式：ma_chao@aliyun.com\nver：1.0.0");
				dialog.setCancelable(true);
				dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,	int which) {
								dialog.dismiss();
							}
						});

				dialog.show();
			}
		});

		addCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CityManagerActivity.this, CityListActivity.class);
				startActivityForResult(intent, 0);
			}
		});

		adapter = new ArrayAdapter<String>(CityManagerActivity.this,
				android.R.layout.simple_selectable_list_item, cityManagerList);

		cityListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemIndex = position;
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						CityManagerActivity.this);
				dialog.setCancelable(true);
				dialog.setPositiveButton("删除",	new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deleteCityInfoByCityName(cityManagerList.get(itemIndex));
						cityManagerList.remove(cityManagerList.get(itemIndex));
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						
						if (cityManagerList.size() == 0) {
							Intent intent = new Intent(
									CityManagerActivity.this, CityListActivity.class);
							intent.putExtra("isNoSelectedCity", true);
							startActivityForResult(intent, 0);
						}
					}
				});
				dialog.show();

				return false;
			}
		});
		cityListView.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				if (cityManagerList.size() >= 5) {
					Toast.makeText(this, "若想添加城市，请先删除一个城市。", Toast.LENGTH_SHORT).show();
					return;
				}
				resultCityName = data.getStringExtra("cityName");
				cityManagerList.add(resultCityName);
				db.addCityInto(cityManagerList.indexOf(resultCityName), resultCityName);
				adapter.notifyDataSetChanged();
				setResult(RESULT_OK);
			}
			break;
		default:
			break;
		}
	}
}
