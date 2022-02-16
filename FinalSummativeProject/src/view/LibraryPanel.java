package view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.MainController;
import customcomponents.CustomButton;
import model.Choreographer;
import model.Crew;
import model.Dancer;
import model.Routine;

import wraplayout.WrapLayout;

@SuppressWarnings("serial")
/*
 * This panel is used to display the choreographers "library" of routines and crews that they've set up
 */
public class LibraryPanel extends JPanel {
	// Instance Variables
	
	// Frames for Crew & Routine Creations
	private CrewInputFrame crewInputFrame;
	private RoutineInputFrame routineInputFrame;
	
	// Background Panels & Components
	private JPanel backgroundPanel;
	private MenuPanel menuPanel;
	private CustomButton logoutButton;
	
	// ScrollPanes for routines and crews
	private JLabel routinesHeader;
	public JPanel routinesPanel;
	private JScrollPane routinesScrollBar;
	
	private JLabel crewsHeader;
	public JPanel crewsPanel;
	private JScrollPane crewsScrollBar;
	
	// The Buttons to Launch Crew and Routine Frames
	private ArrayList<JButton> routineButtons = new ArrayList<JButton>();
	private CustomButton newRoutineButton;
	private ArrayList<JButton> crewButtons = new ArrayList<JButton>();
	private CustomButton newCrewButton;
	
	// Attributes
	public static ArrayList<Routine> routines = new ArrayList<Routine>();
	public static ArrayList<Crew> crews = new ArrayList<Crew>();
	public static int selectedRoutineIndex;
	
	// Colors
	public static final Color PRIMARYBLUE = new Color(61, 90, 241);
	public static final Color SHADEDBLUE = new Color(53, 78, 204);
	public static final Color TINTEDGREEN = new Color(226, 243, 245);
	public static final Color TEAL = new Color(34, 209, 238);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color GREY = new Color(195, 195, 195);
	
	/*
	 * Constructor Method
	 */
	public LibraryPanel() {
		
		setLayout(null);
		setBounds(0, 0, 1920, 1080);
		
		setupFont();                      // Setup Font
		
		setupTemplate();                  // Setup Background Panels
		
		setupHeaders();                   // Setup Header Labels
		
		setupRoutinesScrollPanel();       // Setup Routines Scroll Panel
		
		setupCrewsScrollPanel();          // Setup Crews Scroll Panel
	}

	/*
	 * This method sets up the background panels of the current panel
	 */
	private void setupTemplate() {
		// Content Pane
		backgroundPanel = new JPanel();
		backgroundPanel.setLayout(null);
		backgroundPanel.setBackground(PRIMARYBLUE);
		backgroundPanel.setBounds(0, 120, 1920, 960);
		add(backgroundPanel);
		
		// Menu Panel
		menuPanel = new MenuPanel();
		add(menuPanel);
		
		// Logout Button
		logoutButton = new CustomButton(1, 4, 0.25, GREY, GREY, GREY, GREY);
		logoutButton.setText("Logout");
		logoutButton.setFont(new Font("Nunito", Font.PLAIN, 16));
		logoutButton.setBackground(GREY);
		logoutButton.setHorizontalTextPosition(SwingConstants.CENTER);
		logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logoutButton.setBounds(1740, 40, 100, 40);
		menuPanel.add(logoutButton);
		
		// ActionListener - Clear current choreographer reference and go back to login page
		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainController.loggedChoreographer = new Choreographer();
				MainController.frameState = "Login";
				MainController.newFrame = true;
			}
			
		});
	}
	
	/*
	 * This method sets up the labels for prompting user input
	 */
	private void setupHeaders() {
		// "My Routines" label
		routinesHeader = new JLabel("My Routines");
		routinesHeader.setFont(new Font("Nunito", Font.PLAIN, 50));
		routinesHeader.setForeground(WHITE);
		routinesHeader.setBounds(0, 40, 1920, 55);
		routinesHeader.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(routinesHeader);
		
		// "My Crews" label
		crewsHeader = new JLabel("My Crews");
		crewsHeader.setFont(new Font("Nunito", Font.PLAIN, 50));
		crewsHeader.setForeground(WHITE);
		crewsHeader.setBounds(0, 480, 1920, 55);
		crewsHeader.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(crewsHeader);
	}
	
	/*
	 * This method sets up the scroll panel containing the routines list
	 */
	public void setupRoutinesScrollPanel() {
		// Panel
		routinesPanel = new JPanel();
		routinesPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 70));
		routinesPanel.setBackground(SHADEDBLUE);
		
		// Button to create a new routine
		newRoutineButton = new CustomButton(0, 4, 0.25, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN);
		newRoutineButton.setText("Add Routine");
		newRoutineButton.setFont(new Font("Nunito", Font.PLAIN, 20));
		newRoutineButton.setIcon(new ImageIcon("images/IconNewRoutine.png"));
		newRoutineButton.setBackground(TINTEDGREEN);
		newRoutineButton.setHorizontalTextPosition(SwingConstants.CENTER);
		newRoutineButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		newRoutineButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		newRoutineButton.setSize(290, 250);
		routinesPanel.add(newRoutineButton);
		
		// ActionListener - Create New Routine
		newRoutineButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Don't let the choreographer start a new routine unless they have at least 1 crew
				if (MainController.loggedChoreographer.crewList.size() < 1) {
					JOptionPane.showMessageDialog(MainController.mainFrame, "You must create at least 1 crew before creating a routine.");
				}
				else {
					routineInputFrame = new RoutineInputFrame();
					routineInputFrame.setVisible(true);
				}
			}
			
		});
		
		// Add buttons for the existing routines dynamically
		ArrayList<Routine> routinesList = MainController.loggedChoreographer.routinesList;
		for (int index = 0; index < routinesList.size(); index++) {
			Routine tempRoutine = routinesList.get(index);
			routineButtons.add(index, new CustomButton(0, 4, 0.25, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN));
			routineButtons.get(index).setText(tempRoutine.getName());
			routineButtons.get(index).setFont(new Font("Nunito", Font.PLAIN, 20));
			routineButtons.get(index).setIcon(new ImageIcon("images/IconOpenRoutine.png"));
			routineButtons.get(index).setBackground(TINTEDGREEN);
			routineButtons.get(index).setHorizontalTextPosition(SwingConstants.CENTER);
			routineButtons.get(index).setVerticalTextPosition(SwingConstants.BOTTOM);
			routineButtons.get(index).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			routineButtons.get(index).setActionCommand(String.valueOf(index));
			routineButtons.get(index).setSize(290, 250);
			
			// ActionListener - Load the routine data to the workspace for further editing
			routineButtons.get(index).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					selectedRoutineIndex = Integer.parseInt(e.getActionCommand());
					MainController.frameState = "Load Workspace";
					MainController.newFrame = true;
				}
				
			});
			routinesPanel.add(routineButtons.get(index));
		}
		
		// Scroll Bar
		routinesScrollBar = new JScrollPane(routinesPanel);
		routinesScrollBar.setBorder(new EmptyBorder(getInsets()));
		routinesScrollBar.setBounds(290, 110, 1340, 350);
		backgroundPanel.add(routinesScrollBar);
	}

	/*
	 * This method sets up the scroll panel containing the routines list
	 */
	public void setupCrewsScrollPanel() {
		// Panel
		crewsPanel = new JPanel();
		crewsPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 70));
		crewsPanel.setBackground(SHADEDBLUE);
		
		// Button to create a new crew
		newCrewButton = new CustomButton(0, 4, 0.25, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN);
		newCrewButton.setText("Add Crew");
		newCrewButton.setFont(new Font("Nunito", Font.PLAIN, 20));
		newCrewButton.setIcon(new ImageIcon("images/IconNewCrew.png"));
		newCrewButton.setBackground(TINTEDGREEN);
		newCrewButton.setHorizontalTextPosition(SwingConstants.CENTER);
		newCrewButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		newCrewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		newCrewButton.setSize(290, 250);
		
		// ActionListener - Create New Crew
		newCrewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				crewInputFrame = new CrewInputFrame(new ArrayList<Dancer>(), true, -1);
				crewInputFrame.setVisible(true);
				
			}
			
		});
		
		crewsPanel.add(newCrewButton);
		
		// Add buttons for the existing crews dynamically
		ArrayList<Crew> crewsList = MainController.loggedChoreographer.crewList;
		for (int index = 0; index < crewsList.size(); index++) {
			Crew tempCrew = crewsList.get(index);
			crewButtons.add(index, new CustomButton(0, 4, 0.25, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN));
			crewButtons.get(index).setText(tempCrew.getCrewName());
			crewButtons.get(index).setFont(new Font("Nunito", Font.PLAIN, 20));
			crewButtons.get(index).setIcon(new ImageIcon("images/IconOpenCrew.png"));
			crewButtons.get(index).setBackground(TINTEDGREEN);
			crewButtons.get(index).setHorizontalTextPosition(SwingConstants.CENTER);
			crewButtons.get(index).setVerticalTextPosition(SwingConstants.BOTTOM);
			crewButtons.get(index).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			crewButtons.get(index).setActionCommand(String.valueOf(index));
			crewButtons.get(index).setSize(290, 250);
			
			// ActionListener - Open existing crew for further editing
			crewButtons.get(index).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int index = Integer.parseInt(e.getActionCommand());
					crewInputFrame = new CrewInputFrame(MainController.loggedChoreographer.crewList.get(index).dancersList, false, index);
					crewInputFrame.setVisible(true);
				}
				
			});
			crewsPanel.add(crewButtons.get(index));
		}
		
		
		// Scroll Bar
		crewsScrollBar = new JScrollPane(crewsPanel);
		crewsScrollBar.setBorder(new EmptyBorder(getInsets()));
		crewsScrollBar.setBounds(290, 550, 1340, 350);
		backgroundPanel.add(crewsScrollBar);
	}
	
	/*
	 * This method adds an addition crew button to the scrollpanel when a new crew has been created
	 */
	public void addCrewButton() {
		ArrayList<Crew> crewsList = MainController.loggedChoreographer.crewList;      // Store the list of crews in a placeholder
		Crew newCrew = crewsList.get(crewsList.size() - 1);
		int index = crewButtons.size();
		
		// Add a new instance of the button to the current list of buttons
		crewButtons.add(new CustomButton(0, 4, 0.25, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN, TINTEDGREEN));
		crewButtons.get(index).setText(newCrew.getCrewName());
		crewButtons.get(index).setFont(new Font("Nunito", Font.PLAIN, 20));
		crewButtons.get(index).setIcon(new ImageIcon("images/IconOpenCrew.png"));
		crewButtons.get(index).setBackground(TINTEDGREEN);
		crewButtons.get(index).setHorizontalTextPosition(SwingConstants.CENTER);
		crewButtons.get(index).setVerticalTextPosition(SwingConstants.BOTTOM);
		crewButtons.get(index).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		crewButtons.get(index).setActionCommand(String.valueOf(index));
		crewButtons.get(index).setSize(290, 250);
		crewsPanel.add(crewButtons.get(index));

		
		// ActionListener - Editing a Crew
		crewButtons.get(index).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Obtain the index from the buttons' action command
				int index = Integer.parseInt(e.getActionCommand());
				crewInputFrame = new CrewInputFrame(MainController.loggedChoreographer.crewList.get(index).dancersList, false, index);
				crewInputFrame.setVisible(true);
			}
			
		});
	}
	
	/*
	 * This method sets up the font that is used for the panel
	 */
	private void setupFont() {
		Font nunitoFont = null;
		try {
			nunitoFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Nunito-Regular.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(nunitoFont);
		}
		catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
	}
}

