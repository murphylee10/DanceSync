package view;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.MainController;
import model.Crew;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class RoutineInputFrame extends JFrame {
	// Instance Variables
	private JScrollPane scrollpane;
	private JTable crewTable;
	private JLabel routineNameLabel;
	private JTextField routineNameField;
	private JLabel setMusicLabel2;
	private JLabel setMusicLabel1;
	private JButton startRoutineButton;
	private JButton setMusicButton;
	private JLabel errorLabel;
	private JLabel selectCrewLabel;
	private JButton selectCrewButton;
	private JPanel musicPanel;
	
	// Attributes
	public static String[] tableColumnNames;
	public static Object[][] tableData;
	public static String routineName;
	public static int selectedCrewIndex = -1;
	public static String musicFilePath;
	
	/*
	 * Constructor Method
	 */
	public RoutineInputFrame() {
		setSize(600, 400);
		getContentPane().setLayout(null);
		
		setupFont();      // Font
		
		setupTemplate();  // Background Panels
		
		setupLabels();    // Labels
		
		setupTextFields();// Text Fields
		
		setupTable();     // Table
		
		setupButtons();   // Buttons
	}
	
	/*
	 * This method sets up the background panels of the current panel
	 */
	private void setupTemplate() {
		// Panel for holding music-related input components
		musicPanel = new JPanel();
		musicPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		musicPanel.setBounds(1, 281, 599, 90);
		getContentPane().add(musicPanel);
		musicPanel.setLayout(null);
	}
	
	/*
	 * This method sets up the lables for prompting user inptu
	 */
	private void setupLabels() {
		// Routine name
		routineNameLabel = new JLabel("Routine Name:");
		routineNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		routineNameLabel.setBounds(120, 20, 352, 16);
		getContentPane().add(routineNameLabel);
		
		// Select crew from tabe
		selectCrewLabel = new JLabel("Select a Crew in the Table & Press \"Select Crew\"");
		selectCrewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		selectCrewLabel.setBounds(118, 74, 357, 16);
		getContentPane().add(selectCrewLabel);
		
		// Select music file
		setMusicLabel1 = new JLabel("Set Routine Music (WAV Files Only)");
		setMusicLabel1.setBounds(116, 11, 359, 16);
		musicPanel.add(setMusicLabel1);
		setMusicLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Shows the path of the file selected
		setMusicLabel2 = new JLabel("File Path Will Show Here...");
		setMusicLabel2.setBounds(4, 65, 591, 16);
		musicPanel.add(setMusicLabel2);
		setMusicLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Error label
		errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(Color.RED);
		errorLabel.setBounds(458, 43, 145, 16);
	}
	
	/*
	 * This method sets up the text fields for obtaining user input
	 */
	private void setupTextFields() {
		// Routine name
		routineNameField = new JTextField();
		routineNameField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		routineNameField.setHorizontalAlignment(JTextField.CENTER);
		routineNameField.setBounds(179, 39, 227, 26);
		getContentPane().add(routineNameField);
		routineNameField.setColumns(10);
	}
	
	/*
	 * This method sets up the table that displays the crew data
	 */
	private void setupTable() {
		// ScrollPane
		scrollpane = new JScrollPane();
		scrollpane.setBounds(117, 98, 359, 172);
		getContentPane().add(scrollpane);
		
		// Table
		crewTable = new JTable();
		crewTable.setBackground(Color.LIGHT_GRAY);
		
		scrollpane.setViewportView(crewTable);
		tableColumnNames = new String[] {"Crew Name:", "Number of Dancers:"};
		
		// Table Data
		ArrayList<Crew> crews = MainController.loggedChoreographer.crewList;
		tableData = new Object[crews.size()][2];
		
		// Loop through each crew member, displaying data
		for (int index = 0; index < tableData.length; index++) {
			tableData[index][0] = crews.get(index).getCrewName();
			tableData[index][1] = crews.get(index).dancersList.size();
			
		}
		
//		tableData = new Object[crew.size()][3];
		crewTable.setModel(new DefaultTableModel(tableData, tableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}
	
	/*
	 * This method sets up the buttons for data submission
	 */
	private void setupButtons() {	
		// Button to create the routine and proceed to workspace
		startRoutineButton = new JButton("Start Routine");
		startRoutineButton.setBounds(473, 10, 117, 29);
		getContentPane().add(startRoutineButton);
		
		// ActionListener - Save Data
		startRoutineButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure all fields are filled
				if (validateInput() == true) {
					// Move to the creations page
					routineName = routineNameField.getText();
					MainController.addRoutine();
					dispose();
					MainController.frameState = "New Workspace";
					MainController.newFrame = true;
				}
			}
		});	
		
		// Select crew for routine
		selectCrewButton = new JButton("Select Crew");
		selectCrewButton.setBounds(479, 242, 117, 29);
		getContentPane().add(selectCrewButton);
		
		// ActionListener - Find selected row, set crew label accordingly
		selectCrewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Find index of selected row
				int[] indices = crewTable.getSelectedRows();
				
				// If there is more than one selected index, display error message
				if (indices.length == 0) {
					selectCrewLabel.setForeground(Color.RED);
					selectedCrewIndex = -1;
					selectCrewLabel.setText("You must select at least 1 crew");
				}
				else if (indices.length > 1) {
					selectCrewLabel.setForeground(Color.RED);
					selectedCrewIndex = -1;
					selectCrewLabel.setText("You must select only 1 crew");
				}
				
				// Otherwise, display the selected crew
				else {
					selectCrewLabel.setForeground(Color.BLACK);
					selectedCrewIndex = crewTable.getSelectedRow();
					selectCrewLabel.setText("Selected Crew: " + MainController.loggedChoreographer.crewList.get(selectedCrewIndex).getCrewName());
				}
			}
			
		});
		
		// Set the file path of the music for the routine
		setMusicButton = new JButton("Set Music");
		setMusicButton.setBounds(242, 32, 104, 29);
		musicPanel.add(setMusicButton);
		
		// ActionListener - Show Crew Code
		setMusicButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Launch JFileChooser to select file
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));
				fileChooser.setAcceptAllFileFilterUsed(false);
				int returnValue = fileChooser.showOpenDialog(null);
				
				// If the user selected a file, change the string path accordingly
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					musicFilePath = selectedFile.getPath();
					setMusicLabel2.setText(musicFilePath);
				}
			}
			
		});
	}
	
	// Utility Methods
	

	/*
	 * This method validates the routine name, crew selected, and music selected
	 */
	private boolean validateInput() {
		boolean valid = true;
		
		// The username and password must be longer than 4 characters
		if (routineNameField.getText().equals("")) {
			errorLabel.setText("Enter Routine Name");
			valid = false;
		}
		
		// Make sure the user selected a crew 
		else if (selectedCrewIndex == -1) {
			errorLabel.setText("Select Crew");
			valid = false;
		}
		
		// Make sure the user selected a music file
		else if (musicFilePath == null) {
			errorLabel.setText("Select Music File");
			valid = false;
		}
		
		// Make sure the routine name is distinct
		else if (isDistinctInput() == false) {
			valid = false;
		}
		
		// Add the error label
		getContentPane().add(errorLabel);
		revalidate();
		repaint();
		return valid;
	}
	
	/*
	 * This method checks for distinct input (routine name)
	 */
	private boolean isDistinctInput() {
		// Loop through choreographer crews
		for (int index = 0; index < MainController.loggedChoreographer.routinesList.size(); index++) {
			// If the crew names are the same
			if (routineNameField.getText().equals(MainController.loggedChoreographer.routinesList.get(index).getName())) {
				errorLabel.setText("Routine Name Taken");
				return false;
			}
		}
		return true;
	}
	
	/*
	 * This method sets up the font that is used throughout the frame
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

