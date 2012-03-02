package writers;

import java.io.*;
import java.util.*;

import org.joda.time.*;
import org.rendersnake.*;

import model.*;

public class DetailPageWriter extends TivooWriter {

    protected String getCSS() {
	return "detail_page.css";
    }
    
    protected void writeEvents(HtmlCanvas detail, List<TivooEvent> eventlist, 
	    String summarypath) throws IOException {
	TivooEvent e = eventlist.get(0);
	startTable(detail, "", "70%", "center", "0", "0", "0");
	startRow(detail);
	writeTableHead(detail, "title", null,"1", "2", e.getTitle(), "");
	endRow(detail);
	startRow(detail);
	writeTableCellLiteral(detail, "time", null, "1", "2", 
		formatStartEnd(e.getStart(), e.getEnd()));
	endRow(detail);
	startRow(detail);
	writeTableCellLiteral(detail, "", null, "1", "1", e.getDescription());
	endRow(detail);
	Map<String, Object> specialAttributes = e.getSpecialAttributes();
	for (String attr: specialAttributes.keySet()) {
	    startRow(detail);
	    String towrite = attr.toString() + ": " +
		    specialAttributes.get(attr).toString().replaceAll("\\]", "")
	    		.replaceAll("\\[", "");
	    writeTableCellLiteral(detail, "", null, "1", "1", towrite);
	    endRow(detail);
	}
	startRow(detail);
	writeTableCellLink(detail, "back", null, "1", "1", "Back to summary", 
		"../summary.html");
	endRow(detail);
    }
    
    private String formatStartEnd(DateTime start, DateTime end) {
	String date = "Date: " + start.toString("MMM dd ");
	String startend = "Start: " + start.toString("HH:mm") + " End: " 
		+ end.toString("HH:mm");
	return date + startend;
    }

}