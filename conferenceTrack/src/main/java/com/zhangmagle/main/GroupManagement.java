package com.zhangmagle.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.zhangmagle.model.Event;
import com.zhangmagle.model.EventGenre;
import com.zhangmagle.model.EventGroup;

public class GroupManagement {

	private List<EventGenre> regularGenreList;
	private List<EventGenre> removedRegulGenreList;
	
	private List<EventGenre> specialGenreList;
	private List<EventGenre> removedGenreList;
	private Map<EventGenre, EventGroup> groups;
	
	/**
	 * Used to calculate the sub-arrays that sum is equals to target num
	 */
	private List<Integer> eventDurationArry;
	
	public GroupManagement() {
		regularGenreList = new ArrayList<EventGenre>();
		removedRegulGenreList = new ArrayList<EventGenre>();
		eventDurationArry = new ArrayList<Integer>();
		groups = new HashMap<EventGenre, EventGroup>();
	}
	
	public EventGenre getAvailableGenre() {
		if (regularGenreList.isEmpty()) {
			return null;
		}
		Random random = new Random();
		while(!regularGenreList.isEmpty()) {
			int index = random.nextInt(regularGenreList.size());
			EventGenre eventGenre = regularGenreList.get(index);
			EventGroup group = groups.get(eventGenre);
			if (isGenreAvailable(eventGenre) && !group.isEmpty()) {
				return eventGenre;
			} else {
				removedRegulGenreList.add(regularGenreList.remove(index));
				continue;
			}
		}
		return null;
	}
	
	private boolean isGenreAvailable(EventGenre genre) {
		EventGroup group = groups.get(genre);
		if (group.isExclusive()) {
			return false;
		}
		return true;
	}
	
	public void reset() {
		if (removedRegulGenreList.size() > 0) {
			regularGenreList.addAll(removedRegulGenreList);
		}
		for(EventGenre genre: regularGenreList) {
			EventGroup eventGroup = groups.get(genre);
			eventGroup.reset();
		}
	}
	
	public void groupEvent(Event event) {
		eventDurationArry.add(event.getDuration());
		EventGenre eventGenre = event.getGenre();
		if (EventGenre.NETWORKING_EVENT == eventGenre) {
			if (specialGenreList == null) {
				specialGenreList = new ArrayList<EventGenre>();
			}
			if (specialGenreList.contains(eventGenre)) {
				EventGroup group = groups.get(eventGenre);
				group.insertEvent(event);
			} else {
				specialGenreList.add(eventGenre);
				EventGroup group = new EventGroup();
				group.setType(event.getGenre());
				group.insertEvent(event);
				groups.put(eventGenre, group);
			}
			return;
		}
		if (regularGenreList.contains(eventGenre)) {
			EventGroup group = groups.get(eventGenre);
			group.insertEvent(event);
		} else {
			regularGenreList.add(eventGenre);
			EventGroup group = new EventGroup();
			group.setType(event.getGenre());
			group.insertEvent(event);
			groups.put(eventGenre, group);
		}
		
	}
	
	public Event getEventByGenre(EventGenre genre) {
		EventGroup eventGroup = groups.get(genre);
		if (null == eventGroup) {
			return null;
		}
		Event event = eventGroup.getEvent();
		return event;
	}
	
	public boolean isEmpty() {
		return isRegularGenreEmpty() && isSpecialGenreEmpty();
	}
	
	public boolean isRegularGenreEmpty() {
		return regularGenreList.isEmpty();
	}
	
	public boolean isGenreExclusive(EventGenre eventGenre) {
		EventGroup eventGroup = groups.get(eventGenre);
		return eventGroup.isExclusive();
	}
	
	public void setGenreExclusive(EventGenre eventGenre, boolean isExclusive) {
		EventGroup eventGroup = groups.get(eventGenre);
		eventGroup.setExclusive(true);
	}
	
	public boolean isSpecialGenreEmpty() {
		return null == specialGenreList || specialGenreList.isEmpty();
	}
	
	public List<Integer> getSortedEventDurationArry() {
		Collections.sort(eventDurationArry);
		return eventDurationArry;
	}
}
