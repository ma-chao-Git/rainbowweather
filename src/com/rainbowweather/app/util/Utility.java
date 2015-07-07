package com.rainbowweather.app.util;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rainbowweather.app.base.BaseApplication;
import com.rainbowweather.app.model.WeatherInfo;
import com.rainbowweather.app.model.life.Info;
import com.rainbowweather.app.model.pm25.Pm25;
import com.rainbowweather.app.model.realtime.Realtime;
import com.rainbowweather.app.model.weather.Weather;

public class Utility {

	/**
	 * 解析服务器返回的JSON数据， 并将解析出来的数据存储到本地
	 */
	public static void parseJSONToMapWithGSON(String jsonData, int viewId) {
		Gson gson = new Gson();
		WeatherInfo weatherInfo = gson.fromJson(jsonData, new TypeToken<WeatherInfo>() {}.getType());
		saveWeatherInfo(weatherInfo, viewId);
	}

	/**
	 * 将服务器返回的天气信息存储到SharedPreferences中
	 */
	public static void saveWeatherInfo(WeatherInfo weatherInfo, int viewId) {

		Realtime realtime = weatherInfo.getResult().getData().getRealtime();
		Info lifeInfo = weatherInfo.getResult().getData().getLife().getInfo();
		Pm25 pm25Info = weatherInfo.getResult().getData().getPm25();
		Weather[] futureWeatherArray = weatherInfo.getResult().getData().getWeather();
		String[] day = futureWeatherArray[0].getInfo().getDay();
		String[] night = futureWeatherArray[0].getInfo().getNight();

		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(BaseApplication.getContext()).edit();
		
		editor.putString(viewId + "city_name", realtime.getCity_name());
		editor.putString(viewId + "realtimeTemp", realtime.getWeather().getTemperature());
		editor.putString(viewId + "weather_desp", realtime.getWeather().getInfo());
		editor.putString(viewId + "temp1", day[2]);
		editor.putString(viewId + "temp2", night[2]);
		
		// detailsInfo
		editor.putString(viewId + "humidity", "湿度：" + realtime.getWeather().getHumidity());
		editor.putString(viewId + "direct", "风向：" + realtime.getWind().getDirect());
		editor.putString(viewId + "power", "风力：" + realtime.getWind().getPower());
		
		editor.putString(viewId + "chuanyi", "穿衣指数：" + (lifeInfo.getChuanyi())[0] + "，" + (lifeInfo.getChuanyi())[1]);
		editor.putString(viewId + "ganmao", "感冒指数：" + (lifeInfo.getGanmao())[0] + "，" + (lifeInfo.getGanmao())[1]);
		editor.putString(viewId + "kongtiao", "空调指数：" + (lifeInfo.getKongtiao())[0] + "，" + (lifeInfo.getKongtiao())[1]);
		editor.putString(viewId + "wuran", "污染指数：" + (lifeInfo.getWuran())[0] + "，" + (lifeInfo.getWuran())[1]);
		editor.putString(viewId + "xiche", "洗车指数：" + (lifeInfo.getXiche())[0] + "，" + (lifeInfo.getXiche())[1]);
		editor.putString(viewId + "yundong", "运动指数：" + (lifeInfo.getYundong())[0] + "，" + (lifeInfo.getYundong())[1]);
		editor.putString(viewId + "ziwaixian", "紫外线指数：" + (lifeInfo.getZiwaixian())[0] + "，" + (lifeInfo.getZiwaixian())[1]);
		
		editor.putString(viewId + "pm25", "PM2.5指数：" + pm25Info.getPm25().getPm25());
		editor.putString(viewId + "pm10", "PM10指数：" + pm25Info.getPm25().getPm10());
		editor.putString(viewId + "quality", "空气质量：" + pm25Info.getPm25().getQuality());
		editor.putString(viewId + "des", "外出指数：" + pm25Info.getPm25().getDes());
		//
		saveBackgroudHandler(viewId, realtime.getWeather().getImg(), editor);
		//
		for (int i = 1; i <= 5; i++) {
			//
			saveFutureWeatherInfo(viewId, i, editor, futureWeatherArray[i]);
		}

		//
		editor.commit();
	}

	private static void saveFutureWeatherInfo(int viewId, int index,
			SharedPreferences.Editor editor, Weather futureWeatherArray) {

		String[] futureDawn = futureWeatherArray.getInfo().getDawn();
		String[] futureDay = futureWeatherArray.getInfo().getDay();
		int resID = BaseApplication
				.getContext()
				.getResources()
				.getIdentifier("icon_" + futureDay[0].toString(), "drawable",
						"com.rainbowweather.app");

		editor.putString(viewId + "group" + index + "Date",
				"周" + futureWeatherArray.getWeek());
		editor.putString(viewId + "group" + index + "Temp", futureDawn[2] + "°" + "~"
				+ futureDay[2] + "°");
		editor.putInt(viewId + "group" + index + "resID", resID);
	}

	private static void saveBackgroudHandler(int viewId, String imgIndex, SharedPreferences.Editor editor) {

		int imgCode = Integer.parseInt(imgIndex);
		String backgroudImageCode = "00";

		if (imgCode == 0) {
			backgroudImageCode = "01";
		}
		if (imgCode == 1) {
			backgroudImageCode = "02";
		}
		if (imgCode == 2) {
			backgroudImageCode = "03";
		}
		if (imgCode ==3 || (imgCode >= 21 && imgCode <= 25) || (imgCode >= 7 && imgCode <= 12)) {
			backgroudImageCode = "04";
		}
		if (imgCode == 4 || imgCode == 5) {
			backgroudImageCode = "05";
		}
		if ((imgCode >= 13 && imgCode <= 17)
				|| (imgCode >= 26 && imgCode <= 28)) {
			backgroudImageCode = "06";
		}

		int resID = BaseApplication
				.getContext()
				.getResources()
				.getIdentifier("backgroud_" + backgroudImageCode, "drawable",
						"com.rainbowweather.app");
		
		editor.putInt(viewId + "backgroudImage", resID);
	}
	
	public static boolean isCityExistByApiCheck(String jsonData) {
    	try {
    		JSONObject jsonObject = new JSONObject(jsonData);
			String errNum = jsonObject.getString("errNum");
			if ("0".equals(errNum)) {
				return true;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }

}
