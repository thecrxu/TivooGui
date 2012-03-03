package parsers;

import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;
import org.joda.time.*;
import org.joda.time.format.*;
import model.*;

public class TVParser extends TivooParser {
    
    private Map<String, Set<String>> channelmap;

    protected void setUpHandlers(SAXReader reader) {
	channelmap = new HashMap<String, Set<String>>();
	setEventType(new TVEventType());
	updateNodeNameMap("title", "Title");
	updateNodeNameMap("desc", "Description");
	reader.addHandler("/tv/programme/title", new GetStringValueHandler());
	reader.addHandler("/tv/programme/desc", new GetStringValueHandler());
	reader.addHandler("/tv/channel", new TopLevelHandler());
	reader.addHandler("/tv/programme", new EventLevelHandler());
    }
    
    public TivooEventType getEventType() {
	return new TVEventType();
    }
    
    public String getRootName() {
    	return "tv";
    }
    
    private DateTime parseTime(String timestring) {
	DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYYMMddHHmmss Z");
	return formatter.parseDateTime(timestring);
    }
    
    private class TVEventType extends TivooEventType {
	
	private TVEventType() {
	    @SuppressWarnings("serial")
	    Set<String> localSpecialAttributes = new HashSet<String>() {{
		add("Channel(s)"); add("Actor(s)"); 
	    }};
	    addSpecialAttributes(localSpecialAttributes);
	}
	
	public String toString() {
	    return "TV";
	}
	
    }
    
    private class TopLevelHandler implements ElementHandler {

	public void onStart(ElementPath elementPath) {}

	public void onEnd(ElementPath elementPath) {
	    Element e = elementPath.getCurrent();
	    String currentchannel = e.attributeValue("id");
	    channelmap.put(currentchannel, new HashSet<String>());
	    for (Iterator<?> iter = e.elementIterator(); iter.hasNext(); )
		channelmap.get(currentchannel).add(((Element) iter.next()).getStringValue());
	    e.detach();
	}
	
    }
    
    private class EventLevelHandler implements ElementHandler {

	public void onStart(ElementPath elementPath) {
	    Element e = elementPath.getCurrent();
	    DateTime starttime = parseTime(e.attributeValue("start"));
	    DateTime endtime = parseTime(e.attributeValue("stop"));
	    String channel = e.attributeValue("channel");
	    grabdatamap.put("Start Time", starttime);
	    grabdatamap.put("End Time", endtime);
	    grabdatamap.put("Channel(s)", channelmap.get(channel));
	    elementPath.addHandler("credits", new CreditsHandler());
	}

	public void onEnd(ElementPath elementPath) {
            eventlist.add(new TivooEvent(getEventType(), 
        	    new HashMap<String, Object>(grabdatamap)));
	    grabdatamap.clear();
	    elementPath.getCurrent().detach();
	}
	
    }

    private class CreditsHandler implements ElementHandler {
	
	public void onStart(ElementPath elementPath) {}

	public void onEnd(ElementPath elementPath) {
	    Element e = elementPath.getCurrent();
	    Set<String> actors = new HashSet<String>();
	    for (Iterator<?> iter = e.elementIterator("actor"); iter.hasNext(); )
		actors.add(((Element) iter.next()).getStringValue());
	    grabdatamap.put("Actor(s)", actors);
	    e.detach();
	}
	
    }
    
}