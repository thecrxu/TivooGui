package parsers;

import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import org.joda.time.*;
import model.*;

public class DukeCalParser extends TivooParser {
    
    protected void setUpHandlers(SAXReader reader) {
	setEventType(new DukeCalEventType());
	updateNodeNameMap("summary", "Title");
	updateNodeNameMap("description", "Description");
	updateNodeNameMap("address", "Location");
	reader.addHandler("/events/event/summary", new GetStringValueHandler());
	reader.addHandler("/events/event/description", new GetStringValueHandler());
	reader.addHandler("/events/event/location/address", new GetStringValueHandler());
	reader.addHandler("/events/event", new EventLevelHandler());
    }
    
    public TivooEventType getEventType() {
	return new DukeCalEventType();
    }
    
    public String getRootName() {
    	return "events";
    }
    
    private DateTime parseTime(Element e) {
	String timestring = e.getStringValue();
	return TivooTimeUtils.createTimeUTC(timestring);
    }

    private class DukeCalEventType extends TivooEventType {
	
	private DukeCalEventType() {
	    @SuppressWarnings("serial")
	    Set<String> localSpecialAttributes = new HashSet<String>() {{
		add("Location");
	    }};
	    addSpecialAttributes(localSpecialAttributes);
	}
	
	public String toString() {
	    return "Duke Calendar";
	}
	
    }
    
    private class EventLevelHandler implements ElementHandler {

	public void onStart(ElementPath elementPath) {
	    elementPath.addHandler("start/utcdate", new TimeHandler());
	    elementPath.addHandler("end/utcdate", new TimeHandler());
	}

	public void onEnd(ElementPath elementPath) {
            eventlist.add(new TivooEvent(getEventType(), new HashMap<String, Object>(grabdatamap)));
	    grabdatamap.clear();
	    elementPath.getCurrent().detach();
	}
	
    }

    private class TimeHandler implements ElementHandler {
	
	public void onStart(ElementPath elementPath) {}

	public void onEnd(ElementPath elementPath) {
	    Element e = elementPath.getCurrent();
	    if (e.getPath().contains("start")) {
		DateTime starttime = parseTime(e);
		grabdatamap.put("Start Time", starttime);
	    }
	    else {
		DateTime endtime = parseTime(e);
		grabdatamap.put("End Time", endtime);
	    }
	    e.detach();
	}
	
    }
    
}