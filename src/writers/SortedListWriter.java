package writers;

import java.io.*;
import java.util.*;
import org.rendersnake.*;
import model.*;

public class SortedListWriter extends TivooWriter {

    protected String getCSS() {
	return "list_view.css";
    }
    
    public void writeEvents(HtmlCanvas summary, List<TivooEvent> eventlist, String summarypath)
	    throws IOException {
	startTable(summary, "", "80%", "center", "0", "0", "0");
	for (TivooEvent e : eventlist) {
	    startRow(summary);
	    String detailpath = buildDetailPathRel(eventlist, e, summarypath);
	    writeTableCellLink(summary, "", null, "1", "1", e.getTitle(), detailpath);
	    endRow(summary);
	    new DetailPageWriter().doWriteDetail(e, buildDetailPathAbs(summarypath, detailpath));
	}
    }

}