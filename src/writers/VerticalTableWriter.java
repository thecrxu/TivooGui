package writers;
import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.rendersnake.*;
import static org.rendersnake.HtmlAttributesFactory.*;
import model.*;

public class VerticalTableWriter extends TivooWriter {

    public void write(List<TivooEvent> eventlist, String outputsummary, String outputdetails) 
		    throws IOException {
	if (!new File(outputdetails).isDirectory()) throw new TivooException("Not a directory!");
	TivooUtils.clearDirectory(outputdetails);
	FileWriter summarywriter = new FileWriter(outputsummary);
	Collections.sort(eventlist, TivooEvent.EventTimeComparator);
	HtmlCanvas summary = new HtmlCanvas(summarywriter);
	HashSet<Integer> writtenstartdate = new HashSet<Integer>();
	summary
	.html();
	  writeHeadWithCSS(summary, "styles/vert_table_style.css");
	  summary.body().write("\n")
	    .table(width("70%").align("center"));
	    for (TivooEvent e: eventlist) {
		if (e.isLongEvent()) continue;
		DateTime localstart = TivooTimeHandler.createLocalTime(e.getStart());
		DateTime localend = TivooTimeHandler.createLocalTime(e.getEnd());
		if (!writtenstartdate.contains(localstart.getDayOfYear())) {
		    summary.tr().write("\n")
		      .th(colspan("2").align("left").class_("day")).write(localstart.toString("EEE, MMM dd"))
		      ._th()
		    ._tr();
		    writtenstartdate.add(localstart.getDayOfYear());
		}
		summary.tr().write("\n")
		  .th(class_("time")).write(localstart.toString("HH:mm") + "-" + localend.toString("HH:mm"))
		  ._th().write("\n")
		  .td()
		    .a(href(outputdetails.substring(outputdetails.indexOf("/") + 1) 
			    + buildDetailURL(e))).write(e.getTitle())
		    ._a()
		  ._td().write("\n")
		._tr().write("\n");
		ArrayList<TivooEvent> oneevent = new ArrayList<TivooEvent>();
		oneevent.add(e);
		new DetailPageWriter().write(oneevent, outputsummary, 
			buildDetailURL(e));
	    }
	    summary._table().write("\n")
	  ._body()
       ._html();
    summarywriter.close();
    }
    
}