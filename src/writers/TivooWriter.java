package writers;

import static org.rendersnake.HtmlAttributesFactory.*;
import java.io.*;
import model.*;
import org.rendersnake.*;
import java.util.*;

public abstract class TivooWriter {

    protected abstract void writeEvents(HtmlCanvas target, List<TivooEvent> eventlist, 
	    String summarypath) throws IOException;
    
    protected abstract String getCSS();
    
    public void doWriteSummary(List<TivooEvent> eventlist, String outputpath)
    	throws IOException {
	TivooFileUtils.makeDirectory(outputpath + "/details");
	String summarypath = outputpath + "/summary.html";
	doWrite(eventlist, summarypath);
    }
    
    protected void doWriteDetail(TivooEvent e, String detailpath)
	    throws IOException {
	List<TivooEvent> oneevent = new ArrayList<TivooEvent>();
	oneevent.add(e);
	doWrite(oneevent, detailpath);
    }
    
    private void doWrite(List<TivooEvent> eventlist, String path) throws IOException {
	FileWriter fw = new FileWriter(path);
	HtmlCanvas summary = startPage(fw, eventlist, 
		TivooFileUtils.getWorkingDirectory() + "/styles/" + getCSS());
	writeEvents(summary, eventlist, path);
	endPage(summary, fw);
    }
    
    protected String buildDetailPathRel(List<TivooEvent> eventlist, TivooEvent e, 
	    String summarypath) {
	//System.out.println("Event is: " + e);
	 return "details/".concat(e.getTitle()
	        	.replaceAll("[^A-z0-9]", "").replaceAll("\\s+", "_").trim()
	        	.concat(Integer.toString(eventlist.indexOf(e))).concat(".html"));
    }
    
    protected String buildDetailPathAbs(String summarypath, String detailpathrel) {
	 return summarypath.replace("summary.html", "") + detailpathrel;
    }
    
    protected HtmlCanvas startPage(FileWriter fw, List<TivooEvent> eventlist, String css) throws IOException {
	HtmlCanvas summary = new HtmlCanvas(fw);
	startHtml(summary);
	writeHeadWithCSS(summary, css);
	startBody(summary);
	return summary;
    }
    
    protected void endPage(HtmlCanvas target, FileWriter fw) throws IOException {
	endTable(target);
	endBody(target);
	endHtml(target);
	fw.close();
    }
    
    protected void writeHeadWithCSS(HtmlCanvas target, String stylefile) throws IOException {
	target.head()
	  .link(href(stylefile).type("text/css").rel("stylesheet").media("screen"))
	._head().write("\n");
    }
    
    protected void startHtml(HtmlCanvas target) throws IOException {
	target.html().write("\n");
    }
    
    protected void endHtml(HtmlCanvas target) throws IOException {
	target._html().write("\n");
    }
    
    protected void startBody(HtmlCanvas target) throws IOException {
	target.body().write("\n");
    }
    
    protected void endBody(HtmlCanvas target) throws IOException {
	target._body().write("\n");
    }
    
    protected void startTable(HtmlCanvas target, String cssclass, 
	    String width, String align, String border, 
	    String cellpadding, String cellspacing) throws IOException {
	target.table(class_(cssclass).width(width).align(align).border(border)
		.cellpadding(cellpadding).cellspacing(cellspacing)).write("\n");
    }
    
    protected void endTable(HtmlCanvas target) throws IOException {
	target._table().write("\n");
    }
    
    protected void startRow(HtmlCanvas target) throws IOException {
	target.tr().write("\n");
    }
    
    protected void endRow(HtmlCanvas target) throws IOException {
	target._tr().write("\n");
    }
    
    protected void writeTableHead(HtmlCanvas target, String cssclass, String width,
	    String rowspan, String colspan, String content, String link) 
	    throws IOException {
	target
	.th(class_(cssclass).width(width).rowspan(rowspan).colspan(colspan)).write("\n");
	  if (!link.equals("")) target.a(href(link));
	  else target.a();
	  target.write(content)._a()
	._th().write("\n");
    }
    
    protected void writeTableCellLink(HtmlCanvas target, String cssclass, String width, String rowspan, String colspan, 
	    String content, String link) 
	    throws IOException {
	target
	.td(class_(cssclass).width(width).rowspan(rowspan).colspan(colspan)).write("\n")
	.a(href(link)).write(content)._a()
	._td().write("\n");
    }
    
    protected void writeTableCellLiteral(HtmlCanvas target, String cssclass, String width, String rowspan, String colspan, 
	    String content) 
	    throws IOException {
	target.td(class_(cssclass).width(width).rowspan(rowspan).colspan(colspan)).write("\n")
	   .write(content)._td().write("\n");
    }
    
    protected void writeParagraph(HtmlCanvas target, String cssclass, String content, String link) 
	    throws IOException {
	target
	.p(class_(cssclass)).write("\n");
	  if (!link.equals("")) target.a(href(link));
	  else target.a();
	  target.write(content)._a()
	._p().write("\n");
    }
    
}