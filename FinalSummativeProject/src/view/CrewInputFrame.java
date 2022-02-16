package view;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.MainController;
import model.Choreographer;
import model.Crew;
import model.Dancer;
import model.Routine;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class CrewInputFrame extends JFrame {
	// Instance Variables
	
	// Crew Member Components
	private JScrollPane scrollpane;
	private JTable crewTable;
	private JLabel addMemberLabel1;
	private JLabel addMemberLabel2;	
	private JTextField addMemberField;
	
	// Crew Name and Code Components
	private JLabel crewNameLabel;
	private JTextField crewNameField;
	private JLabel crewCodeLabel;
	private JPasswordField crewCodeField;
	
	// Submission Components
	private JButton saveCrewButton;
	private JButton deleteCrewButton;
	private JButton viewCrewCodeButton;
	private JLabel errorLabel;
	
	// Attributes
	private Object[][] tableData;
	private String[] tableColumnNames;
	
	private ArrayList<Dancer> crew;
	private int currentID;
	private ArrayList<Dancer> tempCrew = new ArrayList<Dancer>();
	private boolean newCrew;
	private int crewIndex;
	
	/*
	 * Constructor Method
	 */
	public CrewInputFrame(ArrayList<Dancer> crew, boolean newCrew, int crewIndex) {
		setSize(640, 400);
		getContentPane().setLayout(null);
		
		this.crew = crew;
		this.currentID = crew.size() + 1;
		this.newCrew = newCrew;
		this.crewIndex = crewIndex;
		
		// Copy contents of the crew to a tempCrew placeholder
		tempCrew = new ArrayList<Dancer>();
		for (int index = 0; index < crew.size(); index++) {
			tempCrew.add(crew.get(index));
		}
		
		setupFont();        // Font setup
		
		setupLabels();      // Label setup
		
		setupTextFields();  // Text Input setup
		
		setupTable();       // Table setup
		
		setupButtons();     // Buttons setup
	}
	
	/*
	 * This method sets up the informational labels and displays them to the screen
	 */
	private void setupLabels() {
		// Prompting for the crew name
		crewNameLabel = new JLabel("Crew Name:");
		crewNameLabel.setBounds(189, 22, 79, 16);
		getContentPane().add(crewNameLabel);
		
		// Prompting for a member
		addMemberLabel1 = new JLabel("Adding a Member:");
		addMemberLabel1.setBounds(250, 271, 117, 16);
		getContentPane().add(addMemberLabel1);
		
		addMemberLabel2 = new JLabel("Format: \"FirstName LastName\" [press \"Enter\"]");
		addMemberLabel2.setBounds(176, 338, 291, 16);
		getContentPane().add(addMemberLabel2);
		
		// Prompting for a crew code
		crewCodeLabel = new JLabel("Crew Passcode:");
		crewCodeLabel.setBounds(361, 22, 98, 16);
		getContentPane().add(crewCodeLabel);
		
		// Displays different error messages according to a validation method
		errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(Color.RED);
		errorLabel.setBounds(491, 48, 149, 16);
	}
	
	/*
	 * This method sets up the TextFields for user input and displays them to the screen
	 */
	private void setupTextFields() {
		// Obtaining crew name
		crewNameField = new JTextField();
		crewNameField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		crewNameField.setHorizontalAlignment(JTextField.CENTER);
		crewNameField.setBounds(157, 43, 142, 26);
		getContentPane().add(crewNameField);
		crewNameField.setColumns(10);
		
		// Obtaining crew code
		crewCodeField = new JPasswordField();
		crewCodeField.setHorizontalAlignment(SwingConstants.CENTER);
		crewCodeField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		crewCodeField.setColumns(10);
		crewCodeField.setBounds(338, 42, 142, 26);
		getContentPane().add(crewCodeField);
		
		// Obtaining crew member names
		addMemberField = new JTextField();
		addMemberField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		addMemberField.setHorizontalAlignment(JTextField.CENTER);
		addMemberField.setBounds(172, 299, 291, 26);
		getContentPane().add(addMemberField);
		addMemberField.setColumns(10);
		
		// KeyListener for TextField - Assign a numerical ID and add data to the table
		addMemberField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// If this crew is new, add the data
					if (newCrew == true) {
						// Add first and last name to JTable
						String nameInput = addMemberField.getText();
						tempCrew.add(new Dancer(currentID, nameInput));
						addMember(currentID, nameInput);
						addMemberField.setText("");
						currentID++;						
					}
					
					else if (JOptionPane.showConfirmDialog(null, "Adding a crew member to an existing crew may affect it's routines. Proceed?", 
						"UNSAVED CHANGES", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						// Add first and last name to JTable
						String nameInput = addMemberField.getText();
						tempCrew.add(new Dancer(currentID, nameInput));
						addMember(currentID, nameInput);
						addMemberField.setText("");
						
						// Loop through all routines within this crew
						for (int index = 0; index < MainController.loggedChoreographer.routinesList.size(); index++) {
							// If the routine involves the current crew
							if (MainController.loggedChoreographer.routinesList.get(index).getCrew()
									.toString().equals(MainController.loggedChoreographer.crewList.get(crewIndex).toString())) {
								// Add crew member
								MainController.loggedChoreographer.routinesList.get(index).getCrew().dancersList.add(new Dancer(currentID, nameInput));
								// Add x and y positions for each formation
								for (int index2 = 0; index2 < MainController.loggedChoreographer.routinesList.get(index).formationsList.size(); index2++) {
									MainController.loggedChoreographer.routinesList.get(index).formationsList.get(index2).xPosList.add(1046);
									MainController.loggedChoreographer.routinesList.get(index).formationsList.get(index2).yPosList.add(493);
								}
							}
						}
						currentID++;
					}
				}
			}
		});
		
		// Set text in fields if a crew is being edited
		if (newCrew == false) {
			crewNameField.setText(MainController.loggedChoreographer.crewList.get(crewIndex).getCrewName());
			crewCodeField.setText(MainController.loggedChoreographer.crewList.get(crewIndex).getCrewCode());
		}
	}
	
	/*
	 * This method sets up the crew data table and displays it to the screen
	 */
	private void setupTable() {
		// ScrollPane
		scrollpane = new JScrollPane();
		scrollpane.setBounds(137, 87, 359, 172);
		getContentPane().add(scrollpane);

		// Table
		crewTable = new JTable();
		crewTable.setBackground(Color.LIGHT_GRAY);
		scrollpane.setViewportView(crewTable);
		
		// Table column and data
		tableColumnNames = new String[] {"ID:", "Full Name:"};
		tableData = new Object[crew.size()][2];
		
		// Loop through crew, adding name and ID's
		for (int index = 0; index < tableData.length; index++) {
			String name = crew.get(index).getName();
			tableData[index][0] = crew.get(index).getId();
			tableData[index][1] = name;
		}
	
		crewTable.setModel(new DefaultTableModel(tableData, tableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}
	
	/*
	 * This method sets up the buttons for crew functions
	 */
	private void setupButtons() {	
		// Button to save crew to logged data
		saveCrewButton = new JButton("Save Crew");
		saveCrewButton.setBounds(507, 18, 117, 29);
		getContentPane().add(saveCrewButton);
		
		// ActionListener - Save Data to choreographer object
		saveCrewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure all fields are filled
				if (validateInput() == true) {
					// Update choreographer crew
					crew = tempCrew;
					
					// Get text from fields
					String tempCrewName = crewNameField.getText();
					String tempCrewCode = String.valueOf(crewCodeField.getPassword());
					
					// Create temp crew and add it to the choreographer object
					Crew saveCrew = new Crew(tempCrewName, tempCrewCode);
					saveCrew.dancersList = crew;
					
					// Save information according to whether the crew is new or edited
					if (newCrew == true) {
						MainController.loggedChoreographer.crewList.add(saveCrew);
						MainController.libraryPanel.addCrewButton();
					}
					
					else {
						MainController.loggedChoreographer.crewList.set(crewIndex, saveCrew);
					}
					
					// Reload the library panel to show updated data
					MainController.libraryPanel.repaint();
					MainController.libraryPanel.revalidate();
					
					MainController.saveChoreographerData();
					dispose();
				}
			}
		});	
		
		// Button for decrypting crew code (visible)
		viewCrewCodeButton = new JButton("Show Crew Code");
		viewCrewCodeButton.setBounds(502, 97, 132, 32);
		getContentPane().add(viewCrewCodeButton);
		
		deleteCrewButton = new JButton("Delete Crew");
		if (newCrew == true) {
			deleteCrewButton.setEnabled(false);
		}
		deleteCrewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Deleting a crew will also delete its routines. Are you sure you want to delete?", 
						"UNSAVED CHANGES", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					// Delete routines
					ArrayList<Routine> toRemove = new ArrayList<Routine>();
					
					for (Routine routine : MainController.loggedChoreographer.routinesList) {
						if (routine.getCrew().toString().equals(MainController.loggedChoreographer.crewList.get(crewIndex).toString())) {
							toRemove.add(routine);
						}
					}
					MainController.loggedChoreographer.routinesList.removeAll(toRemove);
					
					// Delete crew
					MainController.loggedChoreographer.crewList.remove(crewIndex);
					
					// Refresh the panels
//					MainController.libraryPanel.routinesPanel.removeAll();
//					MainController.libraryPanel.setupRoutinesScrollPanel();
//					MainController.libraryPanel.crewsPanel.removeAll();
//					MainController.libraryPanel.setupCrewsScrollPanel();
					
//					MainController.libraryPanel.revalidate();
//					MainController.libraryPanel.repaint();
					
					MainController.saveChoreographerData();
					
					JOptionPane.showMessageDialog(null, "You must log out to save changes");
					
					MainController.loggedChoreographer = new Choreographer();
					MainController.frameState = "Login";
					MainController.newFrame = true;
					
					dispose();
				}
			}
		});
		deleteCrewButton.setBounds(10, 18, 117, 29);
		getContentPane().add(deleteCrewButton);
		
		// ActionListener - Show Crew Code
		viewCrewCodeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (viewCrewCodeButton.getText().equals("Show Crew Code")) {
					crewCodeField.setEchoChar((char) 0);
					viewCrewCodeButton.setText("Hide Crew Code");
				}
				
				else {
					crewCodeField.setEchoChar('*');
					viewCrewCodeButton.setText("Show Crew Code");
				}
			}
			
		});
	}
	
	// Utility Methods
	
	/*
	 * This method adds an additional row to the crew table (when a member is added)
	 */
	private void addMember(int id, String name) {
		Object[][] temp = new Object[tableData.length + 1][tableColumnNames.length];
		for (int i = 0; i < tableData.length; i++) {
			for (int j = 0; j < tableColumnNames.length; j++) {
				temp[i][j] = tableData[i][j];
			}
		}
		
		temp[tableData.length][0] = id;
		temp[tableData.length][1] = name;
		tableData = temp;
		
		crewTable.setModel(new DefaultTableModel(tableData, tableColumnNames));
		
	}
	
	/*
	 * This method checks if the user input meets the proper conditions to create the crew
	 */
	private boolean validateInput() {
		boolean valid = true;
		
		// If the crew name is empty, the input is not valid
		if (crewNameField.getText().equals("")) {
			errorLabel.setText("Enter Crew Name");
			valid = false;
		}
		
		// If the crew code is empty, the input is not valid
		else if (String.valueOf(crewCodeField.getPassword()).equals("")) {
			errorLabel.setText("Enter Crew Code");
			valid = false;
		}
		
		// If there are no members in the crew, the input is not valid
		else if (tempCrew.size() == 0) {
			errorLabel.setText("Add Crew Members");
			valid = false;
		}
		
		// If the crew name or code aren't distinct 
		else if (isDistinctInput() == false && newCrew == true) {
			valid = false;
		}
		
		
		// Add the error label, and refresh the screen
		getContentPane().add(errorLabel);
		revalidate();
		repaint();
		return valid;
	}
	
	/*
	 * This method checks for distinct input (crew name and crew passcode)
	 */
	private boolean isDistinctInput() {
		// Loop through choreographer crews
		for (int index = 0; index < MainController.loggedChoreographer.crewList.size(); index++) {
			// If the crew names are the same
			if (crewNameField.getText().equals(MainController.loggedChoreographer.crewList.get(index).getCrewName())) {
				errorLabel.setText("Crew Name Taken");
				return false;
			}
			
			else if (String.valueOf(crewCodeField.getPassword()).equals(MainController.loggedChoreographer.crewList.get(index).getCrewCode())) {
				errorLabel.setText("Crew Code Taken");
				return false;
			}
		}
		return true;
	}
	
	/*
	 * This method loads the font used in the panel
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
