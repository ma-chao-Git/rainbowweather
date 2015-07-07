package com.rainbowweather.app.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rainbowweather.app.model.City;

public class RainbowWeatherDB {

	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "Weather";
	
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	
	public static RainbowWeatherDB rainbowWeatherDB;
	
	private SQLiteDatabase db;
	
	/**
	 * 将构造方法私有化
	 */
	private RainbowWeatherDB(Context context) {
		RainbowWeatherOpenHelper dbHelper = new RainbowWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 获取RainbowWeatherDB实例
	 */
	public synchronized static RainbowWeatherDB getInstance(Context context) {
		if (rainbowWeatherDB == null) {
			rainbowWeatherDB = new RainbowWeatherDB(context);
		}
		return rainbowWeatherDB;
	}
	
	/**
	 * 从DB中查询城市信息
	 */
	public ArrayList<City> getCityInfo() {
		ArrayList<City> cityInfoList = new ArrayList<City>();
		Cursor cursor = db.rawQuery("select * from City", null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setViewId(cursor.getInt(cursor.getColumnIndex("view_id")));
				cityInfoList.add(city);
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		return cityInfoList;
	}
	
	/**
	 * 从DB中查询城市信息
	 */
	public ArrayList<String> getCityInfoByTable(String table) {
		ArrayList<String> cityInfoList = new ArrayList<String>();
		Cursor cursor = db.query(table, null, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String cityName = cursor.getString(cursor.getColumnIndex("city_name"));
				cityInfoList.add(cityName);
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		return cityInfoList;
	}
	
	/**
	 * 将城市信息插入DB
	 */
	public void addCityInto(int viewId, String cityName) {
		ContentValues values = new ContentValues();
		values.put("view_id", viewId);
		values.put("city_name", cityName);
		db.insert("City", null, values);
		values.clear();
	}
	
	/**
	 * 将城市信息从DB中删除
	 */
	public void deleteCityInfoByCityName(String cityName) {
		db.delete("City", "city_name = ?", new String[]{cityName});
	}
}
