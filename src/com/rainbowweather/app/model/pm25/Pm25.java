package com.rainbowweather.app.model.pm25;

public class Pm25 {

	private String key;
	private String show_desc;
	private Pm25Info pm25;
	private String dateTime;
	private String cityName;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getShow_desc() {
		return show_desc;
	}

	public void setShow_desc(String show_desc) {
		this.show_desc = show_desc;
	}

	public Pm25Info getPm25() {
		return pm25;
	}

	public void setPm25(Pm25Info pm25) {
		this.pm25 = pm25;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
