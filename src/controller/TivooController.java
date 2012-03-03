package controller;

import java.io.*;
import java.util.*;
import org.dom4j.DocumentException;
import org.joda.time.*;
import writers.*;
import model.*;
import view.*;

public class TivooController {

    private TivooModel myModel;

    public TivooController() {
	myModel = new TivooModel();
        new TivooGenerator(myModel, this);
    }
    
    public void doRead(File input) {
	try {
	    myModel.read(input);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }
    
    public void doFilterByTime(DateTime startdate, DateTime enddate) {
    	myModel.filterByTime(startdate, enddate);
    }
    
    public void doFilterByKeywordsCommon(Set<String> keywords, boolean retain) {
	myModel.filterCommon(keywords, retain);
    }
    
    public void doFilterByKeywordsSpecial(Set<String> keywords, TivooEventType eventtype, 
	    String attr, boolean retain) {
	myModel.filterSpecial(keywords, eventtype, attr, retain);
    }
    
    public void doSort(Comparator<TivooEvent> comp, boolean reverse) {
	myModel.sort(comp, reverse);
    }
    
    public void doWrite(TivooWriter writer, String outputdir) {
	try {
	    writer.doWriteSummary(myModel.getFilteredList(), outputdir);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void doClearFilter() {
	myModel.clearFilter();
    }
    
    public void doReset() {
	myModel.reset();
    }
    
}