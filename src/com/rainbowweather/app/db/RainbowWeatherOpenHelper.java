package com.rainbowweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RainbowWeatherOpenHelper extends SQLiteOpenHelper {
	
	/**
	 * City表建表语句
	 */
	public static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement, "
			+ "view_id integer,"
			+ "city_name text, "
			+ "city_code text)";
	
	public static final String CREATE_ALT_CITY = "create table ALT_City ("
			+ "id integer primary key autoincrement, "
			+ "city_name text, "
			+ "city_code text)";
	
	public static final String INSERT_CITY_INFO_INTO_ALT_CITY = "insert into ALT_City(city_name, city_code)"
			+ " values"
			+ "('北京', null),"
			+ "('上海', null),"
			+ "('天津', null),"
			+ "('重庆', null),"
			+ "('哈尔滨', null),"
			+ "('长春', null),"
			+ "('沈阳', null),"
			+ "('大连', null),"
			+ "('石家庄', null),"
			+ "('唐山', null),"
			+ "('郑州', null),"
			+ "('济南', null),"
			+ "('青岛', null),"
			+ "('淄博', null),"
			+ "('烟台', null),"
			+ "('太原', null),"
			+ "('南京', null),"
			+ "('苏州', null),"
			+ "('无锡', null),"
			+ "('合肥', null),"
			+ "('西安', null),"
			+ "('武汉', null),"
			+ "('长沙', null),"
			+ "('杭州', null),"
			+ "('宁波', null),"
			+ "('温州', null),"
			+ "('南昌', null),"
			+ "('福州', null),"
			+ "('厦门', null),"
			+ "('成都', null),"
			+ "('广州', null),"
			+ "('深圳', null),"
			+ "('东莞', null),"
			+ "('珠海', null),"
			+ "('佛山', null),"
			+ "('南宁', null),"
			+ "('昆明', null)";
	
	public RainbowWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CITY); // 创建City表
		db.execSQL(CREATE_ALT_CITY); // 创建City表
		db.execSQL(INSERT_CITY_INFO_INTO_ALT_CITY); // 插入备选城市信息
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
