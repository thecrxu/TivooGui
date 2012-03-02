package writers;

import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.rendersnake.*;
import model.*;

public class WeeklyCalendarWriter extends TivooWriter {

    private List<TivooEvent> event;
    private List<Integer> date;
    private List<Integer> time;

    protected String getCSS() {
	return "weekly_calendar.css";
    }
    
    private void clearList() {
	event = new ArrayList<TivooEvent>();
	date = new ArrayList<Integer>();
	time = new ArrayList<Integer>();
    }

    public void writeEvents(HtmlCanvas summary, List<TivooEvent> eventlist, String summarypath) 
	    throws IOException {
	startTable(summary, "", "90%", "center", "1", "0", "0");
	DateTime current = TivooTimeHandler.createLocalTime(eventlist.get(0).getStart());
	clearList();
	boolean flag = true;
	int count = 0;
	for (TivooEvent e : eventlist) {
	    count++;
	    boolean addedThisTime = false;
	    DateTime localstart = TivooTimeHandler.createLocalTime(e.getStart());
	    if (localstart.getWeekOfWeekyear() != current.getWeekOfWeekyear()) {
		current = localstart;
		flag = false;
	    } 
	    else {
		date.add(localstart.getDayOfWeek() - 1);
		time.add(localstart.getHourOfDay());
		event.add(e);
		addedThisTime = true;
	    }
	    if (!event.isEmpty() && (!flag || count == eventlist.size())) {
		DateTime currentWeek = TivooTimeHandler.createLocalTime(event.get(0).getStart());
		DateTime weekStart = currentWeek.minusDays(currentWeek.getDayOfWeek() - 1);
		startRow(summary);
		String header = weekStart.toString("MMM dd, YYYY") + " ~ " 
			+ weekStart.plusDays(6).toString("MMM dd, YYYY");
		writeTableHead(summary, "", null, "1", "8", header, "");
		endRow(summary);
		startRow(summary);
		String[] firstrow = {" ", "Monday", "Tuesday", "Wednesday", 
			"Thursday", "Friday", "Saturday", "Sunday"};
		for (String s: firstrow)
		    writeTableHead(summary, "", "11.25%", "1", "1", s, "");
		endRow(summary);
		for (int i = 0; i < 24; i++) {
		    startRow(summary);
		    writeTableCellLiteral(summary, "", null, "1", "1", i + ":00");
		    for (int j = 0; j < 7; j++) {
			summary.td();
			for (int k = 0; k < date.size(); k++) {
			    if (date.get(k) == j && time.get(k) == i) {
				TivooEvent f = event.get(k);
				String detailpath = buildDetailPathRel(eventlist, f, summarypath);
				writeParagraph(summary, "", event.get(k).getTitle(), detailpath);
				new DetailPageWriter().doWriteDetail(f, buildDetailPathAbs(summarypath, detailpath));
			    }
			}
			summary._td();
		    }
		    endRow(summary);
		}
		clearList();
		flag = true;
	    }
	    if (!addedThisTime) {
		date.add(localstart.getDayOfWeek() - 1);
		time.add(localstart.getHourOfDay());
		event.add(e);
	    }
	}
    }

}