package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;

import controller.*;

@SuppressWarnings("serial")
public class TivooBrowser extends JFrame {

	public static final String BLANK = " ";
	public static final String TITLE = "TivooBrowser";
	public static final Dimension SIZE = new Dimension(800, 600);
	public static String DEFAULT_START_PAGE = "http://cs.duke.edu/~rcd/index_hacked.html";

	private TivooBrowserModel myModel;
	private TivooGui myView;
	private static TivooBrowser myInstance;

	private JEditorPane myPage;
	private JLabel myStatus;
	private JTextField myURLDisplay;
	private JButton myBackButton;
	private JButton myNextButton;
	private JButton myHomeButton;
	private JButton myAddButton;
	private JButton myRemoveButton;

	public static TivooBrowser getInstance(TivooBrowserModel model,
			TivooGui view, TivooController controller) {
		if (myInstance == null)
			myInstance = new TivooBrowser(model, view, controller);
		myInstance.setVisible(true);
		return myInstance;
	}

	private TivooBrowser(TivooBrowserModel model, TivooGui view,
			TivooController controller) {
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(new JPanel());
		myModel = model;
		myView = view;
		setLayout(new BorderLayout());
		add(makePageDisplay(), BorderLayout.CENTER);
		add(makeInputPanel(), BorderLayout.NORTH);
		add(makeInformationPanel(), BorderLayout.SOUTH);
		enableButtons();
		setLocationRelativeTo(myView);
		setSize(SIZE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		showPage(myModel.getHome() == null ? DEFAULT_START_PAGE : myModel
				.getHome());
	}

	/**
	 * Display given URL.
	 */
	public void showPage(String url) {
		try {
			if (!url.startsWith("http") && !url.startsWith("file")) {
				if (url.startsWith("www"))
					url = "http://" + url;
				else
					url = "file:///" + url;
			}
			new URL(url);
			myModel.go(url);
			update(url);
		} catch (MalformedURLException e) {
			showError("Could not load " + url);
		}
	}

	/**
	 * Display given message as an error in the GUI.
	 */
	public void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Browser Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Display given message as information in the GUI.
	 */
	public void showStatus(String message) {
		myStatus.setText(message);
	}

	// move to the next URL in the history
	private void next() {
		String url = myModel.next();
		if (url != null) {
			update(url);
		}
	}

	// move to the previous URL in the history
	private void back() {
		String url = myModel.back();
		if (url != null) {
			update(url);
		}
	}

	// change current URL to the home page, if set
	private void home() {
		String url = myModel.getHome();
		if (url != null) {
			showPage(url);
		}
	}

	public void refresh() {
		update(myPage.getPage().toString());
	}

	// update just the view to display given URL
	private void update(String url) {
		try {

			myPage.setPage(url);
			myURLDisplay.setText(url);
			enableButtons();
		} catch (IOException e) {
			// should never happen since only checked URLs make it this far ...
			System.out.println("hey o");
			showError("Could not load " + url);
		}
	}

	private void setHome(String url) {
		myModel.setHome(url);
	}

	// make user-entered URL/text field and back/next buttons
	private JComponent makeNavigationPanel() {
		JPanel panel = new JPanel();
		myBackButton = new JButton("Back");
		panel.add(myBackButton);

		myNextButton = new JButton("Next");
		panel.add(myNextButton);

		myHomeButton = new JButton("Home");
		panel.add(myHomeButton);

		// if user presses return, load/show the URL
		myURLDisplay = new JTextField(35);
		myURLDisplay.addActionListener(new ShowPageAction());
		panel.add(myURLDisplay);

		JButton goButton = new JButton("Go");
		goButton.addActionListener(new ShowPageAction());
		myBackButton.addActionListener(new BackAction());
		myNextButton.addActionListener(new NextAction());
		myHomeButton.addActionListener(new HomeAction());
		panel.add(goButton);

		return panel;
	}

	/**
	 * Inner class to factor out showing page associated with the entered URL
	 */
	private class ShowPageAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			showPage(myURLDisplay.getText());
		}
	}

	private class BackAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			back();
		}
	}

	private class NextAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			next();
		}
	}

	private class HomeAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			home();
		}
	}

	private class SetHomeAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setHome(myURLDisplay.getText());
		}
	}

	/**
	 * Inner class to deal with link-clicks and mouse-overs
	 */
	private class LinkFollower implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent evt) {
			// user clicked a link, load it and show it
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				showPage(evt.getURL().toString());
			}
			// user moused-into a link, show what would load
			else if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
				showStatus(evt.getURL().toString());
			}
			// user moused-out of a link, erase what was shown
			else if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
				showStatus(BLANK);
			}
		}
	}

	// only enable buttons when useful to user
	private void enableButtons() {
		myBackButton.setEnabled(myModel.hasPrevious());
		myNextButton.setEnabled(myModel.hasNext());
		myHomeButton.setEnabled(myModel.getHome() != null);
	}

	// convenience method to create HTML page display
	private JComponent makePageDisplay() {
		// displays the web page
		myPage = new JEditorPane();
		myPage.setPreferredSize(SIZE);
		// allow editor to respond to link-clicks/mouse-overs
		myPage.setEditable(false);
		myPage.addHyperlinkListener(new LinkFollower());
		return new JScrollPane(myPage);
	}

	private JComponent makeInputPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(makeNavigationPanel(), BorderLayout.NORTH);
		return panel;
	}

	// make the panel where "would-be" clicked URL is displayed
	private JComponent makeInformationPanel() {
		// BLANK must be non-empty or status label will not be displayed in GUI
		myStatus = new JLabel(BLANK);
		return myStatus;
	}
}