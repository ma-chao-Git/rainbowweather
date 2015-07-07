package com.rainbowweather.app.model.weather;

public class Weather {

	private String date;
	private Info info;
	private String week;
	private String nongli;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getNongli() {
		return nongli;
	}

	public void setNongli(String nongli) {
		this.nongli = nongli;
	}

}
