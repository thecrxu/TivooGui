package writers;

import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.rendersnake.*;
import model.*;

public class MonthlyCalendarWriter extends TivooWriter {

    private List<TivooEvent> event;
    private List<Integer> date;
    private List<Integer> week;

    protected String getCSS() {
	return "monthly_calendar.css";
    }

    private void clearList() {
	event = new ArrayList<TivooEvent>();
	date = new ArrayList<Integer>();
	week = new ArrayList<Integer>();
    }

    public void writeEvents(HtmlCanvas summary, List<TivooEvent> eventlist, String summarypath)
	    throws IOException {
	startTable(summary, "", "90%", "center", "1", "0", "0");
	DateTime current = TivooTimeUtils.createLocalTime(eventlist.get(0).getStart());
	clearList();
	boolean flag = true;
	int count = 0;
	for (TivooEvent e: eventlist) {
	    count++;
	    boolean addedThisTime = false;
	    DateTime localstart = TivooTimeUtils.createLocalTime(e.getStart());
	    if (localstart.getMonthOfYear() != current.getMonthOfYear()) {
		current = localstart;
		flag = false;
	    } 
	    else {
		date.add(localstart.getDayOfWeek() - 1);
		week.add(localstart.getWeekOfWeekyear());
		event.add(e);
		addedThisTime = true;
	    }
	    if (!event.isEmpty() && (!flag || count == eventlist.size())) {
		startRow(summary);
		String header = TivooTimeUtils.createLocalTime(event.get(0).getStart())
			.toString("MMMM YYYY");
		writeTableHead(summary, "", null, "1", "8", header, "");
		endRow(summary);
		startRow(summary);
		String[] firstrow = {" ", "Monday", "Tuesday", "Wednesday", 
			"Thursday", "Friday", "Saturday", "Sunday"};
		for (String s: firstrow)
		    writeTableHead(summary, "", "11.25%", "1", "1", s, "");
		endRow(summary);
		DateTime currentMonth = TivooTimeUtils.createLocalTime(event.get(0).getStart());
		DateTime monthStart = currentMonth.minusDays(currentMonth.getDayOfMonth() - 1);
		int startWeek = monthStart.getWeekOfWeekyear();
		DateTime weekStart = monthStart.minusDays(monthStart.getDayOfWeek() - 1);
		for (int i = 0; i < 5; i++) {
		    startRow(summary);
		    String rowhead = weekStart.plusDays(i*7).toString("MM.dd")+ 
			    "-" + weekStart.plusDays(i*7+6).toString("MM.dd");
		    writeTableCellLiteral(summary, "", null, "1", "1", rowhead);
		    for (int j = 0; j < 7; j++) {
			summary.td();
			for (int k = 0; k < date.size(); k++) {
			    if (date.get(k) == j && week.get(k) == i + startWeek) {
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
		week.add(localstart.getWeekOfWeekyear());
		event.add(e);
	    }
	}
    }

}