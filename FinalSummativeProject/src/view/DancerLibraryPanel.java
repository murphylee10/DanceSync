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
import model.Dancer;
import model.Routine;

import wraplayout.WrapLayout;

@SuppressWarnings("serial")
/*
 * This panel is used to display the choreographers "library" of routines and crews that they've set up
 */
public class DancerLibraryPanel extends JPanel {
	// Instance Variables
	
	// Frames for Crew & Routine Creations
	
	// Background Panels & Components
	private JPanel backgroundPanel;
	private MenuPanel menuPanel;
	private CustomButton logoutButton;
	
	// ScrollPanes for routines and crews
	private JLabel routinesHeader;
	private JPanel routinesPanel;
	private JScrollPane routinesScrollBar;
	
	// The Buttons to Launch Crew and Routine Frames
	private ArrayList<JButton> routineButtons = new ArrayList<JButton>();
	
	// Attributes
	public static ArrayList<Routine> routines = new ArrayList<Routine>();
	public static Dancer loggedDancer;
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
	public DancerLibraryPanel(ArrayList<Routine> routinesList, Dancer dancer) {
		
		setLayout(null);
		setBounds(0, 0, 1920, 1080);
		
		routines = routinesList;
		loggedDancer = dancer;
		
		setupFont();                      // Setup Font
		
		setupTemplate();                  // Setup Background Panels
		
		setupHeader();                   // Setup Header Labels
		
		setupRoutinesScrollPanel();       // Setup Routines Scroll Panel
		
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
	private void setupHeader() {
		// "My Routines" label
		routinesHeader = new JLabel("My Routines");
		routinesHeader.setFont(new Font("Nunito", Font.PLAIN, 50));
		routinesHeader.setForeground(WHITE);
		routinesHeader.setBounds(0, 40, 1920, 55);
		routinesHeader.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(routinesHeader);
	}
	
	/*
	 * This method sets up the scroll panel containing the routines list
	 */
	private void setupRoutinesScrollPanel() {
		// Panel
		routinesPanel = new JPanel();
		routinesPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 70));
		routinesPanel.setBackground(SHADEDBLUE);
				
		// Add buttons for the existing routines dynamically
		for (int index = 0; index < routines.size(); index++) {
			Routine tempRoutine = routines.get(index);
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
					
					DancerFormationFrame dancerFormationFrame = new DancerFormationFrame(routines.get(selectedRoutineIndex), loggedDancer);
					dancerFormationFrame.setVisible(true);
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


