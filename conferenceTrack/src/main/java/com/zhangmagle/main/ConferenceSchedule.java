package com.zhangmagle.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.zhangmagle.loader.ResourceLoader;
import com.zhangmagle.model.Event;
import com.zhangmagle.model.EventGenre;
import com.zhangmagle.model.TimeSlot;

public class ConferenceSchedule {

	private final Logger LOGGER = Logger.getLogger("ConferenceSchedule");

	private final int START_WORKING_TIME = 9;
	private final int LUNCH_TIME = 12;
	private final int WORKING_TIME_AFTERNOON = 1;
	private final int OFF_WORK = 5;
	private final int NETWORKING_AVAIALBE_TIME = 4;

	private ResourceLoader resourceLoader;
	private List<Event> agendaOfToday;
	private List<Integer> sortedEventList;
	private GroupManagement groupM;
	
	private GetAllSubsetByStack allSubSet;
	
	public ConferenceSchedule(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		init();
	}

	private void init() {
		
		resourceLoader.loadResource();
		groupM = resourceLoader.resultSet();
		sortedEventList = groupM.getSortedEventDurationArry();
		allSubSet = new GetAllSubsetByStack();
	}

	public List<Event> schedualConference() throws Exception {
		agendaOfToday = new LinkedList<Event>();
		if (groupM.isEmpty()) {
			LOGGER.severe("no event available");
			throw new Exception("no event available");
		}

		schedualMorningAgenda();
		shedualAfternoonAgenda();
		groupM.reset();
		return agendaOfToday;
	}

	public void schedualMorningAgenda() {
		Calendar startCal = GregorianCalendar.getInstance();
		startCal.set(Calendar.HOUR_OF_DAY, START_WORKING_TIME);
		startCal.set(Calendar.MINUTE, 0);

		Calendar endCal = GregorianCalendar.getInstance();
		endCal.set(Calendar.HOUR_OF_DAY, LUNCH_TIME);
		endCal.set(Calendar.MINUTE, 0);
		
		int targetNum = (endCal.get(Calendar.HOUR_OF_DAY) - startCal.get(Calendar.HOUR_OF_DAY)) * 60
				+ (endCal.get(Calendar.MINUTE) - startCal.get(Calendar.MINUTE));
		
		allSubSet.setTargetSum(targetNum);
		allSubSet.populateSubset(sortedEventList.toArray(new Integer[sortedEventList.size()]), 0, sortedEventList.size(), 0);
		List<List<Integer>> allSubArray = allSubSet.getAllSub();
		if(allSubArray != null && allSubArray.size() > 0) {
			initAgendaOfToday(allSubArray, startCal);
		}
	}
	
	public void shedualAfternoonAgenda() {
		Calendar offWorkCal = GregorianCalendar.getInstance();
		offWorkCal.set(Calendar.HOUR_OF_DAY, OFF_WORK);
		offWorkCal.set(Calendar.MINUTE, 0);

		Calendar networkingCal = GregorianCalendar.getInstance();
		networkingCal.set(Calendar.HOUR_OF_DAY, NETWORKING_AVAIALBE_TIME);
		networkingCal.set(Calendar.MINUTE, 0);

		Calendar startCalA = GregorianCalendar.getInstance();
		startCalA.set(Calendar.HOUR_OF_DAY, WORKING_TIME_AFTERNOON);
		startCalA.set(Calendar.MINUTE, 0);

		int targetNum = (networkingCal.get(Calendar.HOUR_OF_DAY) - startCalA.get(Calendar.HOUR_OF_DAY)) * 60
				+ (networkingCal.get(Calendar.MINUTE) - startCalA.get(Calendar.MINUTE));
		
		allSubSet.setTargetSum(targetNum);
		allSubSet.populateSubset(sortedEventList.toArray(new Integer[sortedEventList.size()]), 0, sortedEventList.size(), 0);
		List<List<Integer>> allSubArray = allSubSet.getAllSub();
		if(allSubArray != null && allSubArray.size() > 0) {
			initAgendaOfToday(allSubArray, startCalA);
		}
		appendNetworkingEvent(startCalA, networkingCal, offWorkCal);
	}
	
	private void appendNetworkingEvent(Calendar startCal, Calendar networkingCal, Calendar offWorkCal) {
		if (!groupM.isSpecialGenreEmpty()) {
			if (startCal.before(networkingCal)) {
				startCal = networkingCal;
			}
			if ((startCal.after(networkingCal) || startCal.equals(networkingCal))
				&& (startCal.equals(offWorkCal) || startCal.before(offWorkCal))) {
				Event event = groupM.getEventByGenre(EventGenre.NETWORKING_EVENT);
				if (event == null) {
					return;
				}
				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
				event.setStartTime(dateFormat.format(startCal.getTime()));
				agendaOfToday.add(event);
			}
		}
	}

	private void initAgendaOfToday(List<List<Integer>> allSubArray, Calendar startCal) {
		Random random = new Random();
		int index = random.nextInt(allSubArray.size());
		List<Integer> subArray = allSubArray.get(index);
		queryEvent(subArray, startCal);
	}
	
	private void queryEvent(List<Integer> subArray, Calendar startCal) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		EventGenre genre = null;
		for(Integer duration: subArray) {
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
			}
			Event event = groupM.getEventByGenre(genre);
			if (null == event) {
				return;
			}
			event.setStartTime(dateFormat.format(startCal.getTime()));
			startCal.add(Calendar.MINUTE, event.getDuration());
			agendaOfToday.add(event);
		}
	}
}
