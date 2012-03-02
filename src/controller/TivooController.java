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
    private TivooGenerator myView;

    public TivooController() {
	myModel = new TivooModel();
        myView = new TivooGenerator(myModel, this);
    }
    
    public void initialize() {
	//myView.showPage(DEFAULT_START_PAGE);
	String input = "dukecal.xml", outputdir = "output";
	//String input = "DukeBasketBall.xml", outputsummary = "output/testhtml_basketball.html", 
	//	outputdetails = "output/details_basketball/";
	//DateTime startdate = TivooTimeHandler.createTimeUTC("20110601T000000Z");
	//DateTime enddate = startdate.plusDays(180);
	//doRead(new File(input));
	//doFilterByTime(startdate, enddate);
	//doFilterByKeywordTitle("Meet");
	//doWrite(new SortedListWriter(), outputdir);
	//doWrite(new DailyCalendarWriter(), outputdir);
	//doWrite(new WeeklyCalendarWriter(), outputdir);
	//doWrite(new MonthlyCalendarWriter(), outputdir);
	//doWrite(new ConflictingEventsWriter(), outputdir);

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
    
    public void doFilterByKeywordTitle(String keyword) {
    	myModel.filterByKeywordTitle(keyword);

    }
    
    public void doFilterByKeywordsAttributes(Set<String> keywords, boolean retain) {
	myModel.filterByKeywordsAttributes(keywords, retain);
    }
    
    public void doWrite(TivooWriter writer, String outputdir) {
	try {
	    writer.doWriteSummary(myModel.getFilteredList(), outputdir);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}