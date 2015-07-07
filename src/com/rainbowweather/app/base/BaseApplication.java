package com.rainbowweather.app.base;
import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

	private static Context context;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
	}
	
	public static Context getContext() {
		return context;
	}

}
