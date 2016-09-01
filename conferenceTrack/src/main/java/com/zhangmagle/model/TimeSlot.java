package com.zhangmagle.model;

import java.util.Calendar;

public class TimeSlot {

	private Calendar startTime;
	private Calendar endTime;
	/*
	 * breakPoint is allowed to special event
	 */
	private Calendar breakPoint;

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public Calendar getBreakPoint() {
		return breakPoint;
	}

	public void setBreakPoit(Calendar breakPoint) {
		this.breakPoint = breakPoint;
	}
	
	
}
