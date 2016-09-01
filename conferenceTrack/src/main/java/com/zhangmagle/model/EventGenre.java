package com.zhangmagle.model;

public enum EventGenre {

	ONE_HOUR(0, 60), 
	THREE_QUARTER(1, 45),
	HALF_AN_HOUR(2, 30),
	LIGHTNING(3, 5),
	NETWORKING_EVENT(4, -1);

	private int code;
	private int duration;
	
	EventGenre(int code, int duration) {
		this.code = code;
		this.duration = duration;
	}
	
	public int getCode() {
		return code;
	}
	public int getDuration() {
		return duration;
	}
}
