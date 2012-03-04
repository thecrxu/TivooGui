package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;

import org.dom4j.DocumentException;
import org.joda.time.DateTime;
import com.toedter.*;
import com.toedter.calendar.JDateChooser;

import writers.ConflictingEventsWriter;
import writers.DailyCalendarWriter;
import writers.MonthlyCalendarWriter;
import writers.SortedListWriter;
import writers.TivooWriter;
import writers.WeeklyCalendarWriter;

import model.*;
import controller.*;

public class TivooGui extends JFrame
{
	
	private Border blackline = BorderFactory.createLineBorder(Color.black);
	
	protected TivooModel myModel;
	protected TivooBrowserModel myBrowserModel;
	protected TivooGui myView;
	protected TivooBrowser myBrowser;
	protected TivooController myController;
	
	private JRadioButton mySortedListButton = new JRadioButton("Sorted List");
	private JRadioButton myConflictButton = new JRadioButton("Conflicting Events");
	private JRadioButton myDailyCalendarButton = new JRadioButton("Daily Calendar");
	private JRadioButton myWeeklyCalendarButton = new JRadioButton("Weekly Calendar");
	private JRadioButton myMonthlyCalendarButton = new JRadioButton("Monthly Calendar");
	private JRadioButton myStartTimeButton = new JRadioButton("Start Time");
	private JRadioButton myEndTimeButton = new JRadioButton("End Time");
	private JRadioButton myTitleButton = new JRadioButton("Title");
	
	private ButtonGroup writerGroup;
	private ButtonGroup sorterGroup;
	private JCheckBox myDateBox;
	private JCheckBox myKeywordBox;
	private JCheckBox myReverseBox;
	private JDateChooser myStartDate;
	private JDateChooser myEndDate;
	
	private JTextArea myCommonKeywordsArea;
    private JCheckBox myCommonNegateBox;

	private JButton myViewButton;
	
	private Map<JRadioButton, TivooWriter> writerMap = new HashMap<JRadioButton, TivooWriter>() 
	{
		{
			put(mySortedListButton, new SortedListWriter());
			put(myConflictButton, new ConflictingEventsWriter());
			put(myDailyCalendarButton, new DailyCalendarWriter());
			put(myWeeklyCalendarButton, new WeeklyCalendarWriter());
			put(myMonthlyCalendarButton, new MonthlyCalendarWriter());
		}
	};
	
	private Map<JRadioButton, Comparator<TivooEvent>> sortMap = new HashMap<JRadioButton, Comparator<TivooEvent>>() {
		{
			put(myStartTimeButton, TivooEvent.EventStartComparator);
			put(myEndTimeButton, TivooEvent.EventEndComparator);
			put(myTitleButton, TivooEvent.EventTitleComparator);
		}
	};
	
	public TivooGui(TivooModel model, TivooController controller)
	{
		myModel = model;
		myView = this;
		myBrowserModel = new TivooBrowserModel();
		myController = controller;
		
		setSize(800, 280);
		
		setTitle("Tivoo GUI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		createMenu();
		add(createOutputPanel(), BorderLayout.NORTH);
		add(createViewPanel(), BorderLayout.PAGE_END);
		add(createSortPanel(), BorderLayout.WEST);
		add(createFilterPanel(), BorderLayout.CENTER);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void createMenu()
	{
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open File");
		open.addActionListener(new LoadFileAction());
		
		file.add(open);
		menubar.add(file);
		setJMenuBar(menubar);     
	}
	
	private JPanel createViewPanel() 
	{
		JPanel viewPanel = new JPanel(new BorderLayout());
		myViewButton = new JButton("View Output");
		myViewButton.setEnabled(false);
		myViewButton.addActionListener(new CreateBrowserAction());
		viewPanel.add(myViewButton);
		return viewPanel;
	}
	
	private JPanel createOutputPanel()
	{
		JPanel outputPanel = new JPanel(new BorderLayout());

		Border title = BorderFactory.createTitledBorder(blackline, "Output");
		outputPanel.setBorder(title);
		
		writerGroup = new ButtonGroup();
		writerGroup.add(mySortedListButton);
		writerGroup.add(myConflictButton);
		writerGroup.add(myDailyCalendarButton);
		writerGroup.add(myWeeklyCalendarButton);
		writerGroup.add(myMonthlyCalendarButton);
		
		outputPanel.setLayout(new FlowLayout());
		
		outputPanel.add(mySortedListButton);
		outputPanel.add(myConflictButton);
		outputPanel.add(myDailyCalendarButton);
		outputPanel.add(myWeeklyCalendarButton);
		outputPanel.add(myMonthlyCalendarButton);
		return outputPanel;
	}
	
	private JComponent createFilterPanel() 
	{
		JPanel filterPanel = new JPanel(new BorderLayout(10, 10));

		Border title = BorderFactory.createTitledBorder(blackline, "Filter by");
		
		filterPanel.setBorder(title);
		JPanel timeFramePanel = new JPanel(new BorderLayout(10, 10));
		
		timeFramePanel.setLayout(new GridLayout(1, 2));
		
		myDateBox = new JCheckBox("Date");
		
		myStartDate = new JDateChooser(DateTime.now().toDate());
		myEndDate = new JDateChooser(DateTime.now().toDate());
		
		timeFramePanel.add(myDateBox);
		timeFramePanel.add(myStartDate);
		timeFramePanel.add(myEndDate);
		
		myKeywordBox = new JCheckBox("Keyword");

		JPanel keywordPanel = new JPanel(new BorderLayout(10, 10));
		
		keywordPanel.setLayout(new GridLayout(1, 1));

		keywordPanel.add(myKeywordBox);
		
		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.setLayout(new GridLayout(1, 3));
		JLabel commonlabel = new JLabel("<html>Keywords<br>(separate using \",\")</html>");
		myCommonKeywordsArea = new JTextArea(1, 1);
		myCommonKeywordsArea.setBorder(BorderFactory.createEtchedBorder());
		
		JScrollPane commoninputscrollpane = new JScrollPane(myCommonKeywordsArea);
		
		myCommonNegateBox = new JCheckBox("Negate Filter");
		inputPanel.add(commonlabel);
		inputPanel.add(commoninputscrollpane);
		inputPanel.add(myCommonNegateBox);
		
		filterPanel.add(timeFramePanel);
		
		filterPanel.setLayout(new GridLayout(2, 3));
		
		keywordPanel.add(inputPanel);
		
		filterPanel.add(keywordPanel);
		
		return filterPanel;
	}
	
	private JComponent createSortPanel() 
	{
		JPanel sortPanel = new JPanel(new BorderLayout());
		
		Border title = BorderFactory.createTitledBorder(blackline, "Sort by");
		
		sortPanel.setBorder(title);
		
		sorterGroup = new ButtonGroup();
		sorterGroup.add(myStartTimeButton);
		sorterGroup.add(myEndTimeButton);
		sorterGroup.add(myTitleButton);
		sorterGroup.setSelected(myStartTimeButton.getModel(), true);
		myReverseBox = new JCheckBox("Reverse Order");
		
		sortPanel.setLayout(new GridLayout(4, 1));
		
		sortPanel.add(myStartTimeButton);
		sortPanel.add(myEndTimeButton);
		sortPanel.add(myTitleButton);
		sortPanel.add(myReverseBox);
		return sortPanel;
	}
	
	private TivooWriter getWriter()
	{
		for (JRadioButton b: writerMap.keySet())
		{
			if (b.getModel() == writerGroup.getSelection())
				return writerMap.get(b);
		}
		return null;
	}
	
	private class CreateBrowserAction implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{		
			TivooWriter writer = getWriter();
			
			if (writer == null) {
				displayError("Please select an output type");
				return;
			}
			
			File f = getOutputFile();
			if (f == null)
				return;
			String outputdir;
			try {
				outputdir = f.getCanonicalPath();
				filter();
				sort();
				
				myController.doWrite(writer, outputdir);
				myBrowserModel.setHome(outputdir + "/summary.html");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
		
			myBrowser = TivooBrowser.getInstance(myBrowserModel, myView, myController);
			myBrowser.refresh();
		}
	}
	
	public Comparator<TivooEvent> getComparator()
	{
		for (JRadioButton b: sortMap.keySet())
		{
			if (b.getModel() == sorterGroup.getSelection())
				return sortMap.get(b);
		}
		return null;
	}
	
	public void sort()
	{
		boolean reverse = myReverseBox.isSelected();
		myController.doSort(TivooEvent.EventTimeComparator, false);
		if (mySortedListButton.isSelected() || myConflictButton.isSelected()) 
		{
			Comparator<TivooEvent> comp = getComparator();
			if (comp == null)
				comp = TivooEvent.EventTimeComparator;
			myController.doSort(comp, reverse);
		}
	}
	
	public void filter()
	{
		if (myDateBox.isSelected()) 
		{
			DateTime startdate = new DateTime(myStartDate.getDate());
			DateTime enddate = new DateTime(myEndDate.getDate());
			myController.doFilterByTime(startdate, enddate);
		}
		
		if (myKeywordBox.isSelected())
		{
			String[] searchedWords = myCommonKeywordsArea.getText().split(",");
			
			Set<String> words = new HashSet<String>();
			for (String s: searchedWords)
				words.add(s);
			
			myController.doFilterByKeywordsCommon(words, myCommonNegateBox.isSelected());			
		}
	}
	
	private void displayError(String errorType) 
	{
		JOptionPane.showMessageDialog(this, errorType);
	}
	
	private File getOutputFile()
	{
		JFileChooser fc = new JFileChooser(".");
		fc.setDialogTitle("Save To");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		myViewButton.setEnabled(true);
		
		int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) 
            return fc.getSelectedFile();
        else
        	return null;
	}
	
	private class LoadFileAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			File f = loadFile();
			if (f == null)
				return;
			myController.doRead(f);
		}
	}
	
	private File loadFile()
	{
		JFileChooser fc = new JFileChooser(".");
        
        int returnVal = fc.showOpenDialog(this);
        
        myViewButton.setEnabled(true);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) 
            return fc.getSelectedFile();
        else
        	return null;
	}
}
