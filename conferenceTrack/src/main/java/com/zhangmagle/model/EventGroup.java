package com.zhangmagle.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EventGroup {
	
	/*
	 * 
	 */
	private EventGenre type; 
	/*
	 * containing all the events belonging to this type
	 */
	private List<Event> events; 
	
	private List<Event> removedEvents;
	
	/*
	 *  whether this type has been removed from candidates events
	 *  default false
	 */
	private boolean isExclusive;
	
	public EventGroup() {
		events = new LinkedList<Event>();
		removedEvents = new ArrayList<Event>();
	}
	
	public EventGenre getType() {
		return type;
	}
	public void setType(EventGenre type) {
		this.type = type;
	}
	
	/**
	 * select an event from eventList randomly
	 * @param eventList
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public Event getEvent() {
		if (events.isEmpty()) {
			return null;
		}
		Random random = new Random();
		int index = random.nextInt(events.size());
		Event event = events.remove(index);
		removedEvents.add(event);
		return event;
	}
	
	public List<Event> getEventList() {
		return events;
	}
	
	public void insertEvent(Event event) {
		this.events.add(event);
	}
	
	public boolean isEmpty() {
		return events.isEmpty();
	}
	
	public boolean isExclusive() {
		return isEmpty() || isExclusive;
	}
	
	public void setExclusive(boolean isExclusive) {
		this.isExclusive = isExclusive;
	}
	
	public void reset() {
		isExclusive = false;
		if (removedEvents != null) {
			events.addAll(removedEvents);
		}
		for (Event event: events) {
			event.setInclusive(false);
		}
	}
}
