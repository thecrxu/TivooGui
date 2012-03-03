package parsers;

import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import org.joda.time.*;
import org.joda.time.format.*;
import model.*;

public class NFLParser extends TivooParser {

    protected void setUpHandlers(SAXReader reader) {
	setEventType(new NFLEventType());
	updateNodeNameMap("Col1", "Title");
	updateNodeNameMap("Col2", "Description");
	updateNodeNameMap("Col15", "Location");
	reader.addHandler("/document/row/Col1", new GetStringValueHandler());
	reader.addHandler("/document/row/Col2", new GetStringValueHandler());
	reader.addHandler("/document/row/Col15", new GetStringValueHandler());
	reader.addHandler("/document/row", new EventLevelHandler());
    }
    
    public String getRootName() {
    	return "document";
    }
    
    public TivooEventType getEventType() {
	return new NFLEventType();
    }

    private DateTime parseTime(Element e) {
	String timestring = e.getStringValue();
	DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
	return formatter.parseDateTime(timestring).plusHours(12);
    }
    
    private class NFLEventType extends TivooEventType {
	
	private NFLEventType() {
	    @SuppressWarnings("serial")
	    Set<String> localSpecialAttributes = new HashSet<String>() {{
		add("Location");
	    }};
	    addSpecialAttributes(localSpecialAttributes);
	}

	public String toString() {
	    return "NFL";
	}
	
    }
    
    private class EventLevelHandler implements ElementHandler {

	public void onStart(ElementPath elementPath) {
	    elementPath.addHandler("Col8", new TimeHandler());
	    elementPath.addHandler("Col9", new TimeHandler());
	}

	public void onEnd(ElementPath elementPath) {
            eventlist.add(new TivooEvent(getEventType(), 
        	    new HashMap<String, Object>(grabdatamap)));
	    grabdatamap.clear();
	    elementPath.getCurrent().detach();
	}
	
    }

    private class TimeHandler implements ElementHandler {
	
	public void onStart(ElementPath elementPath) {}

	public void onEnd(ElementPath elementPath) {
	    Element e = elementPath.getCurrent();
	    if (e.getName().equals("Col8")) {
		DateTime starttime = parseTime(e);
		grabdatamap.put("Start Time", starttime);
	    }
	    else {
		DateTime endtime = parseTime(e);
		grabdatamap.put("End Time", endtime);
	    }
	    elementPath.getCurrent().detach();
	}
	
    }

}