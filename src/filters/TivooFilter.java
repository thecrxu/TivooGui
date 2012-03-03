package filters;

import java.util.*;
import model.*;
import org.joda.time.*;

public class TivooFilter {
    
    public static List<TivooEvent> filterTime(List<TivooEvent> eventlist, DateTime start, DateTime end) {
	List<TivooEvent> filtered = new ArrayList<TivooEvent>();
	for (TivooEvent e: eventlist) {
	    DateTime eventstart = e.getStart();
	    DateTime eventend = e.getEnd();
	    DateTimeComparator comp = DateTimeComparator.getInstance();
	    if (!(comp.compare(end, eventstart) < 0 || comp.compare(eventend, start) < 0))
		filtered.add(e);
	}
	return filtered;
    }
    
    public static List<TivooEvent> filterCommon(List<TivooEvent> eventlist,
	    Set<String> keywords, boolean reverse) {
	List<TivooEvent> filtered = new ArrayList<TivooEvent>();
	for (TivooEvent e: eventlist) {
	    int count = 0;
	    for (String keyword: keywords) {
		if ((e.hasKeyWordCommon(keyword) && !reverse) ||
			(!e.hasKeyWordCommon(keyword) && reverse))
		    count++;
	    }
	    if (count == keywords.size())
		filtered.add(e);
	}
	return filtered;
    }
    
    public static List<TivooEvent> filterSpecial(List<TivooEvent> eventlist, 
	    Set<String> keywords, TivooEventType eventtype, String attr, boolean reverse) {
	List<TivooEvent> filtered = new ArrayList<TivooEvent>();
	for (TivooEvent e: eventlist) {
	    int count = 0;
	    for (String keyword: keywords) {
		if ((e.hasKeyWordSpecial(keyword, eventtype, attr) && !reverse) || 
			(!e.hasKeyWordSpecial(keyword, eventtype, attr) && reverse))
		    count++;
	    }
	    if (count == keywords.size())
		filtered.add(e);
	}
	return filtered;
    }
    
}