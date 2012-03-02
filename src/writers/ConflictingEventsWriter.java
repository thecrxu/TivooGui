package writers;

import java.io.*;
import java.util.*;
import model.*;
import org.rendersnake.HtmlCanvas;

public class ConflictingEventsWriter extends TivooWriter {

    protected String getCSS() {
	return "conflict_view.css";
    }
    
    protected void writeEvents(HtmlCanvas summary, List<TivooEvent> eventlist, String summarypath)
	    throws IOException {
	startTable(summary, "", "80%", "center", "0", "0", "0");
	for (TivooEvent e : eventlist) {
	    List<TivooEvent> conflicts = getConflicts(e, eventlist);
	    if (conflicts.isEmpty()) continue;
	    startRow(summary);
	    String detailpath = buildDetailPathRel(eventlist, e, summarypath);
	    writeTableCellLink(summary, "conflict", null, "1", "1", e.getTitle(), detailpath);
	    endRow(summary);
	    for (TivooEvent o: conflicts) {
		startRow(summary);
		writeTableCellLiteral(summary, "", null, "1", "1", o.toString());
		endRow(summary);
	    }
	    new DetailPageWriter().doWriteDetail(e, buildDetailPathAbs(summarypath, detailpath));
	}
    }
    
    private List<TivooEvent> getConflicts(TivooEvent e, List<TivooEvent> eventlist) {
	List<TivooEvent> conflicts = new ArrayList<TivooEvent>();
	for (TivooEvent o: eventlist) 
	    if (e.hasConflict(o) && !o.isLongEvent())
		conflicts.add(o);
	return conflicts;
    }

}