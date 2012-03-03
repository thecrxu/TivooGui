package model;
import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.joda.time.*;
import parsers.*;
import filters.*;

public class TivooModel {

    private List<TivooEvent> eventlist;
    private List<TivooEvent> filteredlist;
    private Set<TivooEventType> seentypes;
    
    public TivooModel() {
	eventlist = new ArrayList<TivooEvent>();
	filteredlist = new ArrayList<TivooEvent>();
	seentypes = new HashSet<TivooEventType>();
    }

    public List<TivooEvent> getFilteredList() {
	return new ArrayList<TivooEvent>(filteredlist);
    }
    
    public List<TivooEvent> getOriginalList() {
	return Collections.unmodifiableList(eventlist);
    }
    
    public Set<TivooEventType> getSeenTypes() {
	return Collections.unmodifiableSet(seentypes);
    }
    
    public TivooEventType[] getSeenTypesArray() {
	TivooEventType[] toreturn = new TivooEventType[seentypes.size()];
	int j = 0;
	for (Iterator<TivooEventType> i = seentypes.iterator(); i.hasNext(); j++)
	    toreturn[j] = i.next();
	Arrays.sort(toreturn);
	return toreturn;
    }
    
    public void clearFilter() {
	filteredlist = new ArrayList<TivooEvent>(eventlist);
    }
    
    public void reset() {
	eventlist.clear();
	filteredlist.clear();
	seentypes.clear();
    }
    
    public void read(File input) throws DocumentException {
	TivooParser p = new TivooReader().read(input);
	seentypes.add(p.getEventType());
	Set<TivooEvent> eventset = new HashSet<TivooEvent>(eventlist);
	eventset.addAll(p.convertToList(input));
	eventlist = new ArrayList<TivooEvent>(eventset);
    	filteredlist = new ArrayList<TivooEvent>(eventlist);
    }
    
    public void filterByTime(DateTime startdate, DateTime enddate) {
	filteredlist = TivooFilter.filterTime(filteredlist, startdate, enddate);
    }
    
    public void filterCommon(Set<String> keywords, boolean retain) {
	filteredlist = TivooFilter.filterCommon(filteredlist, keywords, retain);
    }
    
    public void filterSpecial(Set<String> keywords, TivooEventType eventtype, 
	    String attr, boolean retain) {
	filteredlist = TivooFilter.filterSpecial(filteredlist, keywords, eventtype, attr, retain);
    }
    
    public void sort(Comparator<TivooEvent> comp, boolean reverse) {
	Collections.sort(filteredlist, comp);
	if (reverse)
	    Collections.reverse(filteredlist);
    }
    
}