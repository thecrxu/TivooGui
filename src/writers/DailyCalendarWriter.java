package writers;

import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.rendersnake.*;

import model.*;

public class DailyCalendarWriter extends TivooWriter {
    
    protected String getCSS() {
	return "daily_calendar.css";
    }

    protected void writeEvents(HtmlCanvas summary, List<TivooEvent> eventlist, String summarypath)
	    throws IOException {
	Set<Integer> writtenstartdate = new HashSet<Integer>();
	startTable(summary, "", "80%", "center", "0", "0", "0");
	for (TivooEvent e: eventlist) {
	    if (e.isLongEvent()) continue;
	    DateTime localstart = TivooTimeUtils.createLocalTime(e.getStart());
	    DateTime localend = TivooTimeUtils.createLocalTime(e.getEnd());
	    checkDuplicateStartDate(summary, localstart, writtenstartdate);
	    startRow(summary);
	    writeTableHead(summary, "time", null, "1", "1", formatStartEnd(localstart, localend), "");
	    String detailpath = buildDetailPathRel(eventlist, e, summarypath);
	    writeTableCellLink(summary, "", null, "1", "1", e.getTitle(), detailpath);
	    endRow(summary);
	    new DetailPageWriter().doWriteDetail(e, buildDetailPathAbs(summarypath, detailpath));
	}
    }
    
    private String formatStartEnd(DateTime start, DateTime end) {
	return start.toString("HH:mm") + "-" + end.toString("HH:mm");
    }
    
    private void checkDuplicateStartDate(HtmlCanvas target, DateTime startdate
	    , Set<Integer> writtenstartdate) throws IOException {
	if (!writtenstartdate.contains(startdate.getDayOfYear())) {
	    startRow(target);
	    writeTableHead(target, "day", null, "1", "2", startdate.toString("EEE, MMM dd"), "");
	    endRow(target);
	    writtenstartdate.add(startdate.getDayOfYear());
	}
    }
    
}