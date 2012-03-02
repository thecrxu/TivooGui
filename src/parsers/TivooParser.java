package parsers;

import java.io.*;
import java.util.*;
import model.*;
import org.dom4j.*;
import org.dom4j.io.*;

public abstract class TivooParser {
    
    @SuppressWarnings("unused")
    private TivooEventType eventtype;
    private Map<String, String> nodenamemap = new HashMap<String, String>();
    protected Map<String, Object> grabdatamap = new HashMap<String, Object>();
    protected List<TivooEvent> eventlist;
    
    public abstract String getRootName();

    public abstract TivooEventType getEventType();

    protected abstract void setUpHandlers(SAXReader reader);
    
    protected void setEventType(TivooEventType type) {
	eventtype = type;
    }
    
    protected void updateNodeNameMap(String key, String value) {
	nodenamemap.put(key, value);
    }
    
    @SuppressWarnings("serial")
    private static HashMap<String, String> pollutant = new HashMap<String, String>() {{
	put("&lt;br /&gt;", " "); put("<br />", " "); put("&amp;", "&"); put("&#39;", "'");
	put("&nbsp;", " ");  put("<br>", " ");
    }};

    protected String sanitizeString(String polluted) {
	for (String s: pollutant.keySet())
	    polluted = polluted.replaceAll(s, pollutant.get(s));
	return polluted;
    }
    
    protected String getElementFieldValue(Element e, String field) {
	return sanitizeString(((Element) e.elements(field).get(0)).getStringValue());
    }
    
    public List<TivooEvent> convertToList(File input) {
	eventlist = new ArrayList<TivooEvent>();
        SAXReader reader = TivooReader.getReader();
        setUpHandlers(reader);
        try {
            reader.read(input);
            nodenamemap.clear();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
	return new ArrayList<TivooEvent>(eventlist);
    }
    
    protected class GetStringValueHandler implements ElementHandler {
	
	public void onStart(ElementPath elementPath) {}

	public void onEnd(ElementPath elementPath) {
	    Element e = elementPath.getCurrent();
	    String str = sanitizeString(e.getStringValue());
	    String relpath = e.getPath(e.getParent());
	    String attr = nodenamemap.get(relpath);
	    grabdatamap.put(attr, str);
	    elementPath.getCurrent().detach();
	}
	
    }
    
}
