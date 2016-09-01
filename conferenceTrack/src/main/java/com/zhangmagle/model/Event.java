package com.zhangmagle.model;

public class Event implements Comparable<Event>{

	private String name;
	private int duration;
	private String startTime;
	private EventGenre genre;
	private boolean isInclusive;
	
	public Event(String name, int duration) {
		this.name = name;
		this.duration = duration;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public boolean isInclusive() {
		return isInclusive;
	}

	public void setInclusive(boolean isInclusive) {
		this.isInclusive = isInclusive;
	}

	public EventGenre getGenre() {
		switch(duration) {
		case 60:
			genre = EventGenre.ONE_HOUR;
			break;
		case 45:
			genre = EventGenre.THREE_QUARTER;
			break;
		case 30:
			genre = EventGenre.HALF_AN_HOUR;
			break;
		case 5:
			genre = EventGenre.LIGHTNING;
			break;
		case 999999:
			genre = EventGenre.NETWORKING_EVENT;
			break;
		}
		return genre;
	}
	
	public int compareTo(Event o) {
		if (this.duration > o.getDuration()) {
			return 1;
		} else if (this.duration < o.getDuration()) {
			return -1;
		}
		return 0;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder(startTime);
		buffer.append(" ").append(name).append(" ");
		switch(duration) {
		case 5:
			buffer.append("Lightning");
			break;
		case 999999:
			break;
		default:
			buffer.append(duration).append(" ").append("min");
		}
		return buffer.toString();
	}
}
