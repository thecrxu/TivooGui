package parsers;

import java.util.*;
import model.*;
import org.dom4j.*;
import org.dom4j.io.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DukeBasketBallParser extends TivooParser {

    public DukeBasketBallParser() {
	setEventType(new DukeBasketBallEventType());
	updateNodeNameMap("Subject", "Title");
	updateNodeNameMap("Description", "Description");
	updateNodeNameMap("Location", "Location");
    }

    protected void setUpHandlers(SAXReader reader) {
	reader.addHandler("/dataroot/Calendar/Subject", new GetStringValueHandler());
	reader.addHandler("/dataroot/Calendar/Description", new GetStringValueHandler());
	reader.addHandler("/dataroot/Calendar/Location", new GetStringValueHandler());
	reader.addHandler("/dataroot/Calendar", new EventLevelHandler());
    }
    
    public TivooEventType getEventType() {
	return new DukeBasketBallEventType();
    }
	
    public String getRootName() {
    	return "dataroot";
    }
    
    private DateTime parseTime(String timestring) {
	DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/YYYY hh:mm:ss aa");
	return formatter.parseDateTime(timestring);
    }

    private class EventLevelHandler implements ElementHandler {

	public void onStart(ElementPath elementPath) {}

	public void onEnd(ElementPath elementPath) {
	    Element e = elementPath.getCurrent();
	    String startdate = getElementFieldValue(e, "StartDate");
	    String starttime = getElementFieldValue(e, "StartTime");
	    String enddate = getElementFieldValue(e, "EndDate");
	    String endtime = getElementFieldValue(e, "EndTime");
	    DateTime start = parseTime(startdate.concat(" " + starttime));
	    DateTime end = parseTime(enddate.concat(" " + endtime));
	    grabdatamap.put("Start Time", start);
	    grabdatamap.put("End Time", end);
            eventlist.add(new TivooEvent(getEventType(), new HashMap<String, Object>(grabdatamap)));
	    grabdatamap.clear();
	    e.detach();
	}
	
    }
    
    private class DukeBasketBallEventType extends TivooEventType {

	private DukeBasketBallEventType() {
	    @SuppressWarnings("serial")
	    Set<String> toadd = new HashSet<String>() {{
		    add("Location");
	    }};
	    addSpecialAttributes(toadd);
	}
	
	public String toString() {
	    return "Duke Basketball";
	}
	
    }

}