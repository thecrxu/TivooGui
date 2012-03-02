package writers;

import java.io.*;
import java.util.*;
import org.rendersnake.*;
import model.*;

public class SortedListWriter extends TivooWriter {

    public void write(List<TivooEvent> eventlist, String outputsummary,
	    String outputdetails) throws IOException {
	FileWriter fw = getSummaryFileWriter(outputsummary, outputdetails);
	HtmlCanvas summary = super.startPage(fw, eventlist, "styles/list_view.css");
	startTable(summary, "", "80%", "center", "0", "0", "0");
	for (TivooEvent e : eventlist) {
	    startRow(summary);
	    writeTableCellLink(summary, "", null, "1", "1", e.getTitle(), 
		    formatDetailURL(eventlist, e, outputdetails));
	    endRow(summary);
	    doWriteDetailPage(eventlist, e, outputsummary, outputdetails);
	}
	endPage(summary, fw);
    }

}