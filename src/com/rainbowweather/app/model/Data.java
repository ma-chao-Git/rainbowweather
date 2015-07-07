package com.rainbowweather.app.model;

import com.rainbowweather.app.model.life.Life;
import com.rainbowweather.app.model.pm25.Pm25;
import com.rainbowweather.app.model.realtime.Realtime;
import com.rainbowweather.app.model.weather.Weather;

public class Data {

	private Realtime realtime;
	private Life life;
	private Weather[] weather;
	private Pm25 pm25;
	private String jingqu;
	private String date;
	private String isForeign;

	public Realtime getRealtime() {
		return realtime;
	}

	public void setRealtime(Realtime realtime) {
		this.realtime = realtime;
	}

	public Life getLife() {
		return life;
	}

	public void setLife(Life life) {
		this.life = life;
	}

	public Weather[] getWeather() {
		return weather;
	}

	public void setWeather(Weather[] weather) {
		this.weather = weather;
	}

	public String getJingqu() {
		return jingqu;
	}

	public Pm25 getPm25() {
		return pm25;
	}

	public void setPm25(Pm25 pm25) {
		this.pm25 = pm25;
	}

	public void setJingqu(String jingqu) {
		this.jingqu = jingqu;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIsForeign() {
		return isForeign;
	}

	public void setIsForeign(String isForeign) {
		this.isForeign = isForeign;
	}

}
