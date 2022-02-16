package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.databind.ObjectMapper;

import componentmover.ComponentMover;
import controller.MainController;
import customcomponents.CustomButton;
import customcomponents.CustomPanel;
import customcomponents.CustomSliderUI;
import model.Choreographer;
import model.Crew;
import model.Formation;
import model.Routine;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dthcr.DefaultTableHeaderCellRenderer;

@SuppressWarnings("serial")
/*
 * 
 */
public class WorkspacePanel extends JPanel {
	// Instance Variables
	
	// GUI Components
	
	// Background Panels
	private JPanel backgroundPanel;
	private GridPanel gridPanel;
	
	// Labels
	private JLabel routineNameLabel;
	private JLabel crewNameLabel;
	private JLabel transitionsLabel;
	private JLabel audienceLabel;
	private JLabel timestampLabel;
	private JLabel musicInstructionsLabel;
	
	// Formation Combo Box
	private JComboBox<String> formationDropdown;
	
	private CustomButton libraryButton;
	private JButton newFormationButton;
	private JButton deleteFormationButton;
	private JButton saveDraftButton;
		
	// Music Control Components
	private JButton setTransitionButton;
	private JButton playMusicButton;
	private JButton pauseMusicButton;
	private JButton viewDanceButton;
	private JButton instructionsButton;
	private JSlider musicProgressSlider;
	
	// Transition Information Table
	private JScrollPane scrollpane1;
	private JTable transitionTable;
	private String[] transitionTableColumnNames;
	private Object[][] transitionTableData;
	
	
	// Crew Information Table
	private JScrollPane scrollpane2;
	private JTable crewTable;
	private String[] crewTableColumnNames;
	private Object[][] crewTableData;
	
	// Dancer Model Components
	private List<CustomPanel> dancerPanels;
	private List<JLabel> dancerLabels;
	
	// Attributes
	
	private Routine tempRoutine;
	private boolean newRoutine;
	private int routineIndex;
	
	private int currentFormationIndex;
	
	private Clip clip;
	private int songPositionMs;
	
	// Colors 
	public static final Color BLUE = new Color(105, 127, 242);
	public static final Color PINK = new Color(239, 61, 136);
	public static final Color TEAL = new Color(34, 209, 238);
	public static final Color GRIDFILL = new Color(235, 217, 217);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color GRIDGREY = new Color(122, 122, 122);
	public static final Color BLACK = new Color(0, 0, 0);
	
	/*
	 * Constructor Method
	 */
	public WorkspacePanel(Routine routine, boolean newRoutine, int routineIndex) {
		setLayout(null);
		setBounds(0, 0, 1920, 1080);
		
		this.tempRoutine = routine;
		this.newRoutine = newRoutine;
		this.routineIndex = routineIndex;
		
		// Load a default 1st formation if it's a new dance
		if (this.newRoutine == true) {
			addNewFormation(0);
		}
		// Save new formation to JSON
		MainController.saveChoreographerData();	

		currentFormationIndex = 0;
		
		setupFont();                    // Font setup
		
		setupTemplate();                // Background panel setup
		
		setupLabels();                  // Label setup
		
		setupTransitionTable();         // Table showing the transition times
		
		setupCrewTable();               // Crew table setup
		
		setupFormationDropdown(0);      // Dropdown to toggle formations
		
		setupGridButtons();             // Buttons to control grid
				
		setupMusicButtons();            // Buttons to control music
		
		setupMusicPanel();              // Slider, transition, and view dance buttons
		
		loadDancers();                  // Display dancer models
	}
	
	/*
	 * This method sets up the background panels of the current panel
	 */

	private void setupTemplate() {
		// Content Pane
		backgroundPanel = new JPanel();
		backgroundPanel.setLayout(null);
		backgroundPanel.setBackground(BLUE);
		backgroundPanel.setBounds(0, 0, 1920, 1080);
		add(backgroundPanel);
		
		
		// Grid Panel
		gridPanel = new GridPanel();
		gridPanel.setLayout(null);
		gridPanel.setBackground(GRIDFILL);
		gridPanel.setBorder(new LineBorder(TEAL, 6)); 
		gridPanel.setBounds(407, 110, 1106, 553);
		backgroundPanel.add(gridPanel);
				
	}
	
	/*
	 * This method sets up the labels displaying user information
	 */
	private void setupLabels() {
		// Routine name
		routineNameLabel = new JLabel();
		routineNameLabel.setText(tempRoutine.getName());
		routineNameLabel.setFont(new Font("Nunito", Font.PLAIN, 40));
		routineNameLabel.setForeground(WHITE);
		routineNameLabel.setBounds(407, 35, 1106, 45);
		routineNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(routineNameLabel);
		
		// Showing the "audience" - tells us that the down direction represents the front of the stage
		audienceLabel = new JLabel();
		audienceLabel.setText("Audience");
		audienceLabel.setFont(new Font("Nunito", Font.PLAIN, 30));
		audienceLabel.setForeground(WHITE);
		audienceLabel.setBounds(700, 700, 520, 35);
		audienceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(audienceLabel);
		
		// Display crew name
		crewNameLabel = new JLabel();
		crewNameLabel.setText(tempRoutine.getCrew().getCrewName());
		crewNameLabel.setFont(new Font("Nunito", Font.PLAIN, 32));
		crewNameLabel.setForeground(WHITE);
		crewNameLabel.setBounds(1513, 125, 407, 40);
		crewNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(crewNameLabel);
		
		// Transition header
		transitionsLabel = new JLabel();
		transitionsLabel.setText("Transitions");
		transitionsLabel.setFont(new Font("Nunito", Font.PLAIN, 32));
		transitionsLabel.setForeground(WHITE);
		transitionsLabel.setBounds(0, 125, 407, 40);
		transitionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(transitionsLabel);
		
		// Music instructions
		musicInstructionsLabel = new JLabel();
		musicInstructionsLabel.setText("Navigate to the Desired Music Timestamp Before Setting the Transition");
		musicInstructionsLabel.setFont(new Font("Nunito", Font.PLAIN, 20));
		musicInstructionsLabel.setForeground(WHITE);
		musicInstructionsLabel.setBounds(507, 830, 906, 25);
		musicInstructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(musicInstructionsLabel);
	}
	
	/*
	 * This method sets up the Jtable showing transiton data
	 */
	private void setupTransitionTable() {
		// ScrollPane
		scrollpane1 = new JScrollPane();
		scrollpane1.setBounds(24, 180, 359, 220);
		backgroundPanel.add(scrollpane1);

		// Table
		transitionTable = new JTable();
		transitionTable.setBackground(Color.LIGHT_GRAY);
		transitionTable.setRowHeight(20);
		transitionTable.getTableHeader().setPreferredSize(
			     new Dimension(scrollpane1.getWidth(), 20)
			);
		
		scrollpane1.setViewportView(transitionTable);
		transitionTableColumnNames = new String[] {"Transiiton:", "Start Time:", "End Time:"};
		
		loadTableData();
	}
	
	/*
	 * This method sets up the data aspect of the transitions table (for dynamic adding)
	 */
	private void loadTableData() {
		List<Formation> formations = tempRoutine.formationsList;
		transitionTableData = new Object[formations.size() - 1][3];
		
		System.out.println("Hi");
		
		// Loop through formations, adding transition information
		for (int index = 0; index < transitionTableData.length; index++) {
			transitionTableData[index][0] = String.format("%d - %d", index + 1, index + 2);
			
			long startMS = tempRoutine.formationsList.get(index).transitionStarts;
			long endMS = tempRoutine.formationsList.get(index).transitionEnds;
			
			System.out.println(startMS);
			System.out.println(endMS);
			
			if (startMS != -1) {
				int s = (int) (startMS / 1000000);
				int mins = s / 60;
				int secs = s % 60;
				transitionTableData[index][1] = String.format("%02d:%02d", mins, secs);
			}
			
			if (endMS != -1) {
				int s = (int) (endMS / 1000000);
				int mins = s / 60;
				int secs = s % 60;
				transitionTableData[index][2] = String.format("%02d:%02d", mins, secs);
			}
		}
		
		// Set the crewTable model to display the crew data
		transitionTable.setModel(new DefaultTableModel(transitionTableData, transitionTableColumnNames));

		// Centering text in the cells
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		DefaultTableHeaderCellRenderer dthcr = new DefaultTableHeaderCellRenderer();
		dthcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		transitionTable.getColumnModel().getColumn(0).setCellRenderer(dtcr);
		transitionTable.getColumnModel().getColumn(1).setCellRenderer(dtcr);
		transitionTable.getTableHeader().setDefaultRenderer(dthcr);
	}
	
	/*
	 * This method sets up the JTable that displays crew data
	 */
	private void setupCrewTable() {
		// ScrollPane
		scrollpane2 = new JScrollPane();
		scrollpane2.setBounds(1537, 180, 359, 220);
		backgroundPanel.add(scrollpane2);

		// Table
		crewTable = new JTable();
		crewTable.setBackground(Color.LIGHT_GRAY);
		crewTable.setRowHeight(20);
		crewTable.getTableHeader().setPreferredSize(
			     new Dimension(scrollpane2.getWidth(), 20)
			);
		
		scrollpane2.setViewportView(crewTable);
		crewTableColumnNames = new String[] {"ID:", "Name:"};
		Crew crew = tempRoutine.getCrew();
		crewTableData = new Object[crew.dancersList.size()][2];
		// Loop through crew, adding name and ID's
		for (int index = 0; index < crewTableData.length; index++) {
			String name = crew.dancersList.get(index).getName();
			crewTableData[index][0] = crew.dancersList.get(index).getId();
			crewTableData[index][1] = name;
		}
		
		// Set the crewTable model to display the crew data
		crewTable.setModel(new DefaultTableModel(crewTableData, crewTableColumnNames));

		// Centering text in the cells
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		DefaultTableHeaderCellRenderer dthcr = new DefaultTableHeaderCellRenderer();
		dthcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		crewTable.getColumnModel().getColumn(0).setCellRenderer(dtcr);
		crewTable.getColumnModel().getColumn(1).setCellRenderer(dtcr);
		crewTable.getTableHeader().setDefaultRenderer(dthcr);
	}
	
	/*
	 * This method sets up the dropdown to toggle between formations
	 */
	private void setupFormationDropdown(int formationIndex) {
		DefaultComboBoxModel<String> dm = new DefaultComboBoxModel<String>();
		// Loop through existing formation indexes, adding an option to the combobox
		for (int index = 0; index < tempRoutine.formationsList.size(); index++) {
			String name = "Formation " + String.valueOf(index + 1);
			dm.addElement(name);
		}
		
		// Formation dropdown
		formationDropdown = new JComboBox<String>(dm);
		formationDropdown.setFont(new Font("Nunito", Font.PLAIN, 16));
		formationDropdown.setSelectedIndex(formationIndex);
		formationDropdown.setBounds(865, 780, 190, 20);
		backgroundPanel.add(formationDropdown);
		
		// Centering text in JCombobox
		DefaultListCellRenderer dtcr = new DefaultListCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		formationDropdown.setRenderer(dtcr);
		
		// ActionListener - Reset the formmation index, display the new locations of the dancers
		formationDropdown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				saveFormationLocally();
				currentFormationIndex = formationDropdown.getSelectedIndex();
				reloadDancers();
			}
		});
	}
	
	/*
	 * This method sets up the buttons that control the grid
	 */
	private void setupGridButtons() {
		// This button switches back to the library panel
		libraryButton = new CustomButton(1, 4, 0.3, PINK, PINK, PINK, PINK);
		libraryButton.setText("Library");
		libraryButton.setFont(new Font("Nunito", Font.PLAIN, 25));
		libraryButton.setForeground(WHITE);
		libraryButton.setBackground(PINK);
		libraryButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		libraryButton.setBounds(50, 40, 175, 50);
		backgroundPanel.add(libraryButton);
		
		// ActionListener - Return to library
		libraryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Stop the clip if playing
				clip.close();
				
				// If the user hasn't saved their latest draft, prompt them to save
				if (isEdited() == true) {
					// If the user decides to save their draft, save the latest data before returning to the library panel
					if (JOptionPane.showConfirmDialog(null, "You have unsaved changes. Would you like to save before proceeding?", 
							"UNSAVED CHANGES", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						MainController.loggedChoreographer.routinesList.set(routineIndex, tempRoutine);
						MainController.saveChoreographerData();
						MainController.frameState = "Library";
						MainController.newFrame = true;	
					}
					
					// Otherwise, don't save changes
					else {
						MainController.frameState = "Library";
						MainController.newFrame = true;	
					}
				}
				
				// Otherwise, bring back the user to the library panel
				else {
					MainController.frameState = "Library";
					MainController.newFrame = true;					
				}
			}
			
		});
		
		// This button switches back to the library panel
		saveDraftButton = new CustomButton(1, 4, 0.3, PINK, PINK, PINK, PINK);
		saveDraftButton.setText("Save Draft");
		saveDraftButton.setFont(new Font("Nunito", Font.PLAIN, 25));
		saveDraftButton.setForeground(WHITE);
		saveDraftButton.setBackground(PINK);
		saveDraftButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveDraftButton.setBounds(1700, 40, 175, 50);
		backgroundPanel.add(saveDraftButton);
		
		// ActionListener - Return to library
		saveDraftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainController.loggedChoreographer.routinesList.set(routineIndex, tempRoutine);
				MainController.saveChoreographerData();
			}
			
		});
		
		// Button to create a new formation
		newFormationButton = new CustomButton(1, 4, 0.3, PINK, PINK, PINK, PINK);
		newFormationButton.setText("Add Formation");
		newFormationButton.setFont(new Font("Nunito", Font.PLAIN, 20));
		newFormationButton.setForeground(WHITE);
		newFormationButton.setBackground(PINK);
		newFormationButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		newFormationButton.setBounds(1330, 694, 179, 46);
		backgroundPanel.add(newFormationButton);
		
		// ActionListener - Add a formation to the list
		newFormationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int formationNumber = -1;
				try {
					int min = 1;
					int max = tempRoutine.formationsList.size() + 1;
					
					formationNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter the Formation Number "
							+ "(" + String.valueOf(min) + " - " + String.valueOf(max) + ")"));
					
					if (formationNumber >= min && formationNumber <= max) {
//						saveFormationLocally();
						currentFormationIndex = formationNumber - 1;
						addNewFormation(currentFormationIndex);
						
						// Reload affected components
						reloadDancers();
						
						backgroundPanel.remove(formationDropdown);
						setupFormationDropdown(currentFormationIndex);
					}
					
					else {
						JOptionPane.showMessageDialog(null, "Number Given Was Out of Bounds.");	
					}
					
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Invalid Input - Must Enter an Integer.");						
				}
			}
		});
		
		// Button to delete a formation
		deleteFormationButton = new CustomButton(1, 4, 0.3, PINK, PINK, PINK, PINK);
		deleteFormationButton.setText("Delete Formation");
		deleteFormationButton.setFont(new Font("Nunito", Font.PLAIN, 20));
		deleteFormationButton.setForeground(WHITE);
		deleteFormationButton.setBackground(PINK);
		deleteFormationButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		deleteFormationButton.setBounds(407, 694, 209, 46);
		backgroundPanel.add(deleteFormationButton);
		
		// ActionListener - Add a formation to the list
		deleteFormationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// If there is only one formation, show error message
				if (tempRoutine.formationsList.size() == 1) {
					JOptionPane.showMessageDialog(null, "You only have one formation. Deleting is not allowed.", "NO DELETING", JOptionPane.ERROR_MESSAGE);
				}
				
				// If the user confirms that they want to delete the formation, proceed
				else if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this formation?", 
						"DELETE FORMATION", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					tempRoutine.formationsList.remove(currentFormationIndex);
					
					// If the latest formation was deleted, decrement to the previous formation
					if (currentFormationIndex == tempRoutine.formationsList.size()) {
						currentFormationIndex -= 1;
					}
					
					// Reload affected components
					
					reloadDancers();
					
					backgroundPanel.remove(transitionTable);
					loadTableData();
					
					backgroundPanel.remove(formationDropdown);
					setupFormationDropdown(currentFormationIndex);
				}
			}
		});
	}
	
	/*
	 * This method sets up the buttons that control the music
	 */
	private void setupMusicButtons() {
		// This button switches back to the library panel
		setTransitionButton = new CustomButton(1, 4, 0.3, TEAL, TEAL, TEAL, TEAL);
		setTransitionButton.setText("Place Transition");
		setTransitionButton.setFont(new Font("Nunito", Font.PLAIN, 20));
		setTransitionButton.setForeground(WHITE);
		setTransitionButton.setBackground(TEAL);
		setTransitionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setTransitionButton.setBounds(507, 925, 195, 50);
		backgroundPanel.add(setTransitionButton);
		
		// ActionListener - get duration of transition
		setTransitionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clip.stop();    // Stop the clip if it was already playing
				
				// Don't allow a transition to be set if it is the final formation
				if (currentFormationIndex == (tempRoutine.formationsList.size() - 1)) {
					JOptionPane.showMessageDialog(null, "You are not allow to set a transition on the final formation.", "TRANSITION WARNING", JOptionPane.ERROR_MESSAGE);
				}
				
				// Don't allow a transition to be set if there are previous formations without transitions
				else if (prevTransitionsSet() == false) {
					JOptionPane.showMessageDialog(null, "You must set all of your previous transitions first", "SET TRANSITIONS", JOptionPane.ERROR_MESSAGE);
				}
				
				// Don't allow a transition to be set if it starts before the previous transition ends
				else if (validStartingPosition(clip.getMicrosecondPosition()) == false) {
					JOptionPane.showMessageDialog(null, "You cannot start this transition in the middle of a previous one. Navigate further.", "SET TRANSITIONS", JOptionPane.ERROR_MESSAGE);
				}
				
				// Otherwise, they can set the transition
				else {
					// Obtain the current position within the song
					long transitionStarts = clip.getMicrosecondPosition();
					tempRoutine.formationsList.get(currentFormationIndex).transitionStarts = transitionStarts;
					
					int numBeats = -1;
					
					// Prompt the user for the length of the transition (in beats)
					int minBeats = 1;
					double clipLength = (double) (clip.getMicrosecondLength() / 1000000);
					double clipPos = (double) (clip.getMicrosecondPosition() / 1000000);
					double clipRemaining = clipLength - clipPos;
					double bps = tempRoutine.getMusic().getBps();
					int maxBeats = (int) (bps * clipRemaining);
					String prompt = String.format("Enter the length of the transition in beats (%d - %d):", minBeats, maxBeats);
					
					try {
						numBeats = Integer.parseInt(JOptionPane.showInputDialog(prompt));
						
						// Validate the length given
						if (numBeats >= minBeats && numBeats <= maxBeats) {
							// Calculate the end time of the transition - convert beats to microseconds, add to start time
							double spb = clipLength / (bps * clipLength);
							long transitionLength = (long) ((spb * (double) numBeats) * 1000000);
							long transitionEnds = transitionStarts + transitionLength;
							tempRoutine.formationsList.get(currentFormationIndex).transitionEnds = transitionEnds;
							
							// Reset the transition table
							backgroundPanel.remove(transitionTable);
							loadTableData();
//						transitionTable.revalidate();
//						transitionTable.repaint();
						}
						
						else if (numBeats == -1) {
							JOptionPane.showMessageDialog(null, "Invalid Input. Enter an integer.");	
						}
						
						else {
							JOptionPane.showMessageDialog(null, "Number Given Was Out of Bounds.");	
						}
						
					}
					catch (NumberFormatException e2) {
						
					}
				}
			}
			
		});
		
		// This button creates a new window to watch the dance
		viewDanceButton = new CustomButton(1, 4, 0.3, TEAL, TEAL, TEAL, TEAL);
		viewDanceButton.setText("View Dance");
		viewDanceButton.setFont(new Font("Nunito", Font.PLAIN, 20));
		viewDanceButton.setForeground(WHITE);
		viewDanceButton.setBackground(TEAL);
		viewDanceButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		viewDanceButton.setBounds(1700, 975, 185, 50);
		backgroundPanel.add(viewDanceButton);
		
		viewDanceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clip.stop();     // Stop clip if it was currently playing
				
				// Only allow the user to move on if all the transitions have been set
				if (allTransitionsSet() == false) {
					JOptionPane.showMessageDialog(null, "You must set all of your transitions before viewing the dance.", "SET TRANSITIONS", JOptionPane.ERROR_MESSAGE);
				}
				
				else {
					// TODO
					PresentDanceFrame presentDanceFrame = new PresentDanceFrame(tempRoutine);
					presentDanceFrame.setVisible(true);
				}
			}
			
		});
		
		// This button creates a new window, showing information about how to make formation transitions
		instructionsButton = new CustomButton(1, 4, 0.3, TEAL, TEAL, TEAL, TEAL);
		instructionsButton.setText("Instructions");
		instructionsButton.setFont(new Font("Nunito", Font.PLAIN, 20));
		instructionsButton.setForeground(WHITE);
		instructionsButton.setBackground(TEAL);
		instructionsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		instructionsButton.setBounds(35, 975, 185, 50);
		backgroundPanel.add(instructionsButton);
		
		instructionsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				InstructionsFrame instructionsFrame = new InstructionsFrame();			
				instructionsFrame.setVisible(true);
			}
			
		});
	}
	
	/*
	 * This method sets up the panel for the music conponents
	 */
	private void setupMusicPanel() {
		// Load music file in file path
		File file = new File(tempRoutine.getMusic().getMusicPath());
		try {
			// Open clip
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
					
			// JSlider showing progress within music
			musicProgressSlider = new JSlider(JSlider.HORIZONTAL, 0, (int) (clip.getMicrosecondLength() / 1000), songPositionMs) {
				@Override
                public void updateUI() {
                    setUI(new CustomSliderUI(this));
                }
			};
			musicProgressSlider.setOpaque(false);
//			System.out.println(clip.getMicrosecondLength());
//			System.out.println((int)(clip.getMicrosecondLength() / 1000));
			musicProgressSlider.setBounds(507, 865, 906, 40);
			backgroundPanel.add(musicProgressSlider);
			
			// ChangeListener - skip to specific points of the song
			musicProgressSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent event) {
					clip.setMicrosecondPosition((int)(musicProgressSlider.getValue() * 1000));
				}
			});
			
			// Plays music
			playMusicButton = new JButton();
			playMusicButton.setIcon(new ImageIcon("images/IconPlayButton.png"));
			playMusicButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			playMusicButton.setBounds(885, 915, 65, 65);
			backgroundPanel.add(playMusicButton);
			
			playMusicButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					clip.start();
				}
			});
			
			// Pauses music
			pauseMusicButton = new JButton();
			pauseMusicButton.setIcon(new ImageIcon("images/IconPauseButton.png"));
			pauseMusicButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			pauseMusicButton.setBounds(965, 915, 65, 65);
			backgroundPanel.add(pauseMusicButton);
			
			pauseMusicButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					clip.stop();
				}
			});
			
			// Shows music timestamp
			timestampLabel = new JLabel();
			timestampLabel.setText(getTimestamp());
			timestampLabel.setFont(new Font("Nunito", Font.PLAIN, 24));
			timestampLabel.setForeground(WHITE);
			timestampLabel.setBounds(1417, 864, 200, 32);
			backgroundPanel.add(timestampLabel);
			
			// Updating music slider
			TimeListener timeListener = new TimeListener();
			Timer timer = new Timer(1000, timeListener);
			timer.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	/*
	 * This method laods the dancers to the grid
	 */
	private void loadDancers() {
		// Component move makes JPanels movable
		ComponentMover cm = new ComponentMover();
		Crew crew = tempRoutine.getCrew();
		
		// Dancer models
		dancerPanels = new ArrayList<CustomPanel>();
		dancerLabels = new ArrayList<JLabel>();
		
		// Loop through crew members, creating and placing a new dancer panel
		for (int index = 0; index < crew.dancersList.size(); index++) {
			int x = tempRoutine.formationsList.get(currentFormationIndex).xPosList.get(index);
			int y = tempRoutine.formationsList.get(currentFormationIndex).yPosList.get(index);
			
			System.out.println(x);
			System.out.println(y);
			
			JLabel tempDancerLabel = new JLabel();
			tempDancerLabel.setText(String.valueOf(crew.dancersList.get(index).getId()));
			tempDancerLabel.setFont(new Font("Nunito", Font.PLAIN, 16));
			tempDancerLabel.setForeground(WHITE);
			dancerLabels.add(tempDancerLabel);
			
			CustomPanel tempDancerPanel = new CustomPanel(35);
			tempDancerPanel.setBackground(PINK);
			tempDancerPanel.setLayout(new GridBagLayout());
			tempDancerPanel.add(tempDancerLabel);
			tempDancerPanel.setBounds(x, y, 35, 35);
			tempDancerPanel.add(dancerLabels.get(index));
			
			// Mouse listener, when the location changes, update the position within the object
			tempDancerPanel.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					saveFormationLocally();
				}
			});
			
			// Register the component to the component mover
			cm.registerComponent(tempDancerPanel);
			dancerPanels.add(index, tempDancerPanel);
			
			// Add dancer panel to grid
			gridPanel.add(dancerPanels.get(index));
		}
	}
	
	/*
	 * This method reloads the dancers on the screen when a formation changes
	 */
	private void reloadDancers() {
		gridPanel.removeAll();
		loadDancers();
		gridPanel.revalidate();
		gridPanel.repaint(); 
	}
	
	/*
	 * This method saves the dancer locations locally (in the formations list)
	 */
	private void saveFormationLocally() {
		Crew crew = tempRoutine.getCrew();
		Formation tempFormation = new Formation();
		
		// Loop through each crew member to obtain the position of their panel
		for (int index = 0; index < crew.dancersList.size(); index++) {
			Point point = dancerPanels.get(index).getLocation();
			int x = point.x;
			int y = point.y;
			
			// Add x and y values to the formation placeholder
			tempFormation.xPosList.add(x);
			tempFormation.yPosList.add(y);
		}
		
		// Replace the formation object's x and y values with the placeholder
		tempRoutine.formationsList.set(currentFormationIndex, tempFormation);
	}
	
	/*
	 * This method sets the bounds for a new formation
	 */
	private void addNewFormation(int formationIndex) {
		Formation tempFormation = new Formation();
		
		// If its the first formation
		if (formationIndex == 0) {
			// Loop through each dancer, setting them to their default position
			for (int index = 0; index < tempRoutine.getCrew().dancersList.size(); index++) {
				int x;
				int y;
				
				int id = index + 1;
				// Odd number arrangement
				if ((tempRoutine.getCrew().dancersList.size()) % 2 == 1) {
					int row = id / 14;
					
					
					if (id % 13 == 1) {
						x = 536;
					}
					else if (id % 13 == 2) {
						x = 615;
					}
					else if (id % 13 == 3) {
						x = 457;
					}
					
					else if (((id % 2 == 0) & (row % 2 == 0)) || ((id % 2 == 1) & (row % 2 == 1))) {
						x = tempFormation.xPosList.get(index - 2) + 79;
					}
					
					else {
						x = tempFormation.xPosList.get(index - 2) - 79;
					}
					
					y = 222 + (79 * row);
					
					tempFormation.xPosList.add(x);
					tempFormation.yPosList.add(y);
				}
				
				// Even number arrangement
				else {
					int row = id / 13;
					
					if (id % 12 == 1) {
						x = 496;
					}
					else if (id % 12 == 2) {
						x = 575;
					}
					else if (id % 12 == 3) {
						x = 417;
					}
					
					else if (((id % 2 == 0) & (row % 2 == 0)) || ((id % 2 == 1) & (row % 2 == 1))) {
						x = tempFormation.xPosList.get(index - 2) + 79;
					}
					
					else {
						x = tempFormation.xPosList.get(index - 2) - 79;
					}
					
					y = 222 + (79 * row);
					
					tempFormation.xPosList.add(x);
					tempFormation.yPosList.add(y);
				}
				
			}
		}
		
		// Otherwise, set to the postiion of the previous formation
		else {
			for (int index = 0; index < tempRoutine.getCrew().dancersList.size(); index++) {
				// Set to the same positions as last formation
				tempFormation.xPosList.add(tempRoutine.formationsList.get(formationIndex - 1).xPosList.get(index));
				tempFormation.yPosList.add(tempRoutine.formationsList.get(formationIndex - 1).yPosList.get(index));
			}
		}
		// Add the formation lists
		tempRoutine.formationsList.add(formationIndex, tempFormation);
	}
	
	
	/*
	 * This method checks if edits have been made in the workspace
	 */
	private boolean isEdited() {
		String filePath;
		
		// Load the choreographer data into a placeholder object
		try {
			if (MainController.usedPanel.equals("Login Panel")) {
				filePath = "files/" + LoginPanel.username + ".json";
			}
			
			else {
				filePath = "files/" + RegisterPanel.username + ".json";
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			File file = new File(filePath);
			
			Choreographer tempChoreographer = objectMapper.readValue(file, Choreographer.class);
			
			System.out.println(tempChoreographer.routinesList.get(routineIndex));
			System.out.println(tempRoutine);
			
			// Compare the 2 objects, returning true if the objects contain the same data
			String choreoString1 = tempChoreographer.routinesList.get(routineIndex).toString();
			String choreoString2 = tempRoutine.toString();
			if (choreoString1.equals(choreoString2)) {
				return false;
			}
		} 
		
		catch (Exception e) {
			e.printStackTrace();		
		}
		
		return true;
	}
	
	/*
	 * This method forms the string of timestamps of the music that is playing
	 */
	private String getTimestamp() {
		int songLength = (int) (clip.getMicrosecondLength() / 1000000);
		int songPosition = (int) (clip.getMicrosecondPosition() / 1000000);
		
		int positionMins = songPosition / 60;
		int positionSecs = songPosition % 60;
		
		int lengthMins = songLength / 60;
		int lengthSecs = songLength % 60;
		
		return String.format("%02d:%02d / %02d:%02d", positionMins, positionSecs, lengthMins, lengthSecs);
	}
	
	/*
	 * This method checks if the transitions in the formations before the current one have been set
	 */
	private boolean prevTransitionsSet() {
		for (int index = currentFormationIndex - 1; index >= 0; index--) {
			// If the formation transition is null, return false
			if ((tempRoutine.formationsList.get(index).transitionStarts == -1) || (tempRoutine.formationsList.get(index).transitionEnds == -1)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * This method checks if all the transitions have been set
	 */
	private boolean allTransitionsSet() {
		for (int index = 0; index < tempRoutine.formationsList.size() - 1; index++) {
			if ((tempRoutine.formationsList.get(index).transitionStarts == -1) || (tempRoutine.formationsList.get(index).transitionEnds == -1)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * This method checks if the transition breakpoint conflicts with other transitions
	 */
	private boolean validStartingPosition(long currentPosition) {
		for (int index = 0; index < currentFormationIndex; index++) {
			if (currentPosition < tempRoutine.formationsList.get(index).transitionEnds) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * This method sets up the font that is used in the panel
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
	
	class TimeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			updateSlider();
			updateLabel();
		}
		
		private void updateSlider() {
			musicProgressSlider.setValue((int) (clip.getMicrosecondPosition() / 1000));
		}
		
		private void updateLabel() {
			timestampLabel.setText(getTimestamp());
		}
	}
}