package conferenceTrack;

import java.util.List;

import org.junit.Test;

import com.zhangmagle.loader.ResourceLoader;
import com.zhangmagle.loader.impl.FileResourceLoaderImpl;
import com.zhangmagle.main.ConferenceSchedule;
import com.zhangmagle.model.Event;

import junit.framework.Assert;

public class ConferenceTrackTest {

	@Test
	public void testConferenceSchedule() throws Exception {
		String fileName = "src/test/java/conferenceTrack/events.txt";
		ResourceLoader resourceLoader = new FileResourceLoaderImpl(fileName);
		ConferenceSchedule schedule = new ConferenceSchedule(resourceLoader);
		List<Event> agenda = schedule.schedualConference();
		Assert.assertNotNull(agenda);
		System.out.println("*******events -- track1**********");
		for(Event event: agenda) {
			System.out.println(event);
		}
		System.out.println("*******events -- track1**********");
		System.out.println();
		System.out.println();
		
		List<Event> agenda2 = schedule.schedualConference();
		System.out.println("*******events -- track2**********");
		for(Event event: agenda2) {
			System.out.println(event);
		}
		System.out.println("*******events -- track2**********");
		System.out.println();
		System.out.println();
	}
	
//	@Test
	public void testNetworkingEvent() throws Exception {
		String fileName = "src/test/java/conferenceTrack/special_event.txt";
		ResourceLoader resourceLoader = new FileResourceLoaderImpl(fileName);
		ConferenceSchedule schedule = new ConferenceSchedule(resourceLoader);
		List<Event> agenda = schedule.schedualConference();
		Assert.assertNotNull(agenda);
		Assert.assertEquals(1, agenda.size());
		System.out.println("*******special_event**********");
		for(Event event: agenda) {
			System.out.println(event);
		}
		System.out.println("*******special_event**********");
		System.out.println();
		System.out.println();
	}
	
//	@Test
	public void testSameGenreEvent() throws Exception {
		String fileName = "src/test/java/conferenceTrack/same_event_genre.txt";
		ResourceLoader resourceLoader = new FileResourceLoaderImpl(fileName);
		ConferenceSchedule schedule = new ConferenceSchedule(resourceLoader);
		List<Event> agenda = schedule.schedualConference();
		Assert.assertNotNull(agenda);
		System.out.println("*******same_event_genre**********");
		Assert.assertEquals(5, agenda.size());
		for(Event event: agenda) {
			System.out.println(event);
		}
		System.out.println("*******same_event_genre**********");
		System.out.println();
		System.out.println();
	}
}
