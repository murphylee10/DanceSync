package view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.*;


import controller.MainController;
import customcomponents.CustomButton;
import customcomponents.CustomPanel;
import customcomponents.CustomPasswordField;
import customcomponents.CustomTextField;
import model.Choreographer;
import model.Crew;
import model.Dancer;

/*
 * This class is used to validate the dancers' login attempts
 */
@SuppressWarnings("serial")
public class DancerLoginPanel extends JPanel {
	// Instance Variables
	private MenuPanel menuPanel;
	private CustomButton choreoLoginButton;
	private JPanel borderPanel;
	private JPanel backgroundPanel;
	private CustomPanel loginInputPanel;
	private JLabel sloganLabel;
	private JLabel headerLabel;
	private CustomTextField choreoUsernameField;
	private CustomPasswordField crewCodeField;
	private JButton loginButton;
	private JLabel errorLabel;
	
	// Attributes
	public static String choreoUsername;
	public static String crewCode;
	public static Crew loggedCrew;
	public static Dancer loggedDancer;
	
	// Colors 
	
	public static final Color PRIMARYBLUE = new Color(61, 90, 241);
	public static final Color LIGHTBLUE = new Color(98, 123, 248);
	public static final Color DARKBLUE = new Color(39, 51, 114);
	public static final Color CREAM = new Color(226, 243, 245);
	public static final Color TEAL = new Color(34, 209, 238);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color PINK = new Color(239, 61, 136);
	
	/*
	 * Constructor Method
	 */
	public DancerLoginPanel() {
		setLayout(null);
		setBounds(0, 0, 1920, 1080);
		
		// For determining which panel to obtain the username or password from
		MainController.usedPanel = "Dancer Login Panel";
		
		// Adding GUI components to the panel
		
		setupFont();             // Font
		
		setupTemplate();         // Background Panels
		
		setupSlogan();           // Slogan Label
		
		setupLoginPanel();       // Panel Containing Login Components
		
		setupHeader();           // Header Labels
		
		setupTextFields();       // Text Fields for Username & Password Inputs
		
		setupButtons();          // Buttons For Submission
	} 
	
	/*
	 * This method sets up the background panels of the current panel
	 */
	private void setupTemplate() {
		// Content Pane
		borderPanel = new JPanel();
		borderPanel.setLayout(null);
		borderPanel.setBackground(DARKBLUE);
		borderPanel.setBounds(0, 120, 1920, 1080);
		add(borderPanel);
		
		backgroundPanel = new JPanel();
		backgroundPanel.setLayout(null);
		backgroundPanel.setBackground(PRIMARYBLUE);
		backgroundPanel.setBounds(30, 30, 1860, 870);
		borderPanel.add(backgroundPanel);
		
		// Menu Panel
		menuPanel = new MenuPanel();
		add(menuPanel);
		
		// Button to go back to choreographer login
		choreoLoginButton = new CustomButton(1, 4, 0.25, PINK, PINK, PINK, PINK);
		choreoLoginButton.setText("Choreographer Login");
		choreoLoginButton.setFont(new Font("Nunito", Font.PLAIN, 16));
		choreoLoginButton.setForeground(WHITE);
		choreoLoginButton.setBackground(PINK);
		choreoLoginButton.setHorizontalTextPosition(SwingConstants.CENTER);
		choreoLoginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		choreoLoginButton.setBounds(1650, 40, 210, 40);
		menuPanel.add(choreoLoginButton);
		
		// ActionListener - Clear current choreographer reference and go back to login page
		choreoLoginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainController.frameState = "Login";
				MainController.newFrame = true;
			}
		});
	}
	
	/*
	 * This method sets up the label that displays the company slogan
	 */
	private void setupSlogan() {
		// Slogan Label
		sloganLabel = new JLabel("\"View your placements in your choreographer's formations\"");
		sloganLabel.setFont(new Font("Nunito", Font.PLAIN, 30));
		sloganLabel.setForeground(WHITE);
		sloganLabel.setBounds(460, 100, 1000, 30);
		sloganLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(sloganLabel);
	}
	
	/*
	 * This method sets up the panel for storing user-input components
	 */
	private void setupLoginPanel() {
		// Create and set properties of the panel
		loginInputPanel = new CustomPanel(20);
		loginInputPanel.setLayout(null);
		loginInputPanel.setBackground(LIGHTBLUE);
		loginInputPanel.setBounds(460, 160, 1000, 600);
		backgroundPanel.add(loginInputPanel);
	}
	
	/*
	 * This method sets up the header labels for prompting user input
	 */
	private void setupHeader() {
		// Header label
		headerLabel = new JLabel("Dancer Login");
		headerLabel.setFont(new Font("Nunito", Font.PLAIN, 50));
		headerLabel.setForeground(WHITE);
		headerLabel.setBounds(0, 50, 1000, 55);
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginInputPanel.add(headerLabel);
		
		// Error Label
		errorLabel = new JLabel();
		errorLabel.setFont(new Font("Nunito", Font.PLAIN, 20));
		errorLabel.setForeground(Color.RED);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(0, 500, 1000, 30);
	}
	
	/*
	 * This method sets up the TextFields for obtaining user input
	 */
	private void setupTextFields() {
		// Placeholder field to be the default selected field
		JTextField placeHolder = new JTextField();
		placeHolder.setText("");
		backgroundPanel.add(placeHolder);
				
		// TextField for obtaining username
		choreoUsernameField = new CustomTextField();
		choreoUsernameField.setText("Choreographer Username");
		choreoUsernameField.setFont(new Font("Nunito", Font.PLAIN, 30));
		choreoUsernameField.setBorder(BorderFactory.createCompoundBorder(choreoUsernameField.getBorder(), BorderFactory.createEmptyBorder(0,  25,  0, 0)));
		choreoUsernameField.setBounds(150, 150, 700, 100);
		loginInputPanel.add(choreoUsernameField);
		
		// FocusListener for username
		choreoUsernameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (choreoUsernameField.getText().equals("Choreographer Username")) {
					choreoUsernameField.setText("");	
				}
			}
		});
		
		// TextField for obtaining password
		crewCodeField = new CustomPasswordField();
		crewCodeField.setText("Crew Code");
		crewCodeField.setFont(new Font("Nunito", Font.PLAIN, 30));
		crewCodeField.setBorder(BorderFactory.createCompoundBorder(crewCodeField.getBorder(), BorderFactory.createEmptyBorder(0,  25,  0, 0)));
		crewCodeField.setBounds(150, 280, 700, 100);
		crewCodeField.setEchoChar((char)0);
		loginInputPanel.add(crewCodeField);
		
		// FocusListener for password
		crewCodeField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (String.valueOf(crewCodeField.getPassword()).equals("Crew Code")) {
					crewCodeField.setText("");	
				}
				crewCodeField.setEchoChar('*');
			}
		});
		
	}
	
	/*
	 * This method sets up both the "go to register" link and the "login" button
	 */
	private void setupButtons() {		
		
		// Login Button
		loginButton = new CustomButton(1, 4, 0.25, TEAL, TEAL, TEAL, TEAL);
		loginButton.setText("Login");
		loginButton.setFont(new Font("Nunito", Font.PLAIN, 25));
		loginButton.setForeground(WHITE);
		loginButton.setBackground(TEAL);
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginButton.setBounds(645, 420, 200, 60);
		loginInputPanel.add(loginButton);
		
		// ActionListener for Login Button
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				choreoUsername = choreoUsernameField.getText();
				crewCode = String.valueOf(crewCodeField.getPassword());
				
				if (validateInput() == true) {
					MainController.frameState = "Dancer Library";
					MainController.newFrame = true;
				}
			}
			
		});
	}
	
	/*
	 * This method validates the login input given by the user
	 */
	private boolean validateInput() {
		Scanner scanner;
		try {
			// Open the "Registered Accounts" file, seeing if the user input matches any lines
			scanner = new Scanner(new File("files/RegisteredAccounts.txt"));
			while (scanner.hasNextLine()) {
				StringTokenizer st = new StringTokenizer(scanner.nextLine());
				String field1 = st.nextToken();      // Choreographer username
				
				// If the username cannot be found in the text file, the user has entered an invalid username
				if (!choreoUsernameField.getText().equals(field1)) {
					errorLabel.setText("Choreographer Username is Invalid");
					loginInputPanel.add(errorLabel);
					revalidate();
					repaint();
					return false;
				}
				
				// Check if th crew code represents a valid crew
				else if (crewExists(String.valueOf(crewCodeField.getPassword())) == null) {
					errorLabel.setText("The Crew Code Doesn't Represent an Existing Crew");
					loginInputPanel.add(errorLabel);
					revalidate();
					repaint();
					return false;
				}
				
				// Check if the dancer exists in the crew
				else {
					String fullName = JOptionPane.showInputDialog("Enter your full name:");
					
					if (nameFoundInCrew(fullName) == false) {
						errorLabel.setText("The Name You Entered Doesn't Exist in the Crew");
						loginInputPanel.add(errorLabel);
						revalidate();
						repaint();
						return false;						
					}
				}
			}
			
			scanner.close();
			
		} 
		catch (FileNotFoundException e) {
			errorLabel.setText("A Login Error Has Occured");
			loginInputPanel.add(errorLabel);
			revalidate();
			repaint();
			return false;
		}
		
		return true;
	}
	
	/*
	 * This method instantiates the proper choreographer object, seeing if there is a crew with the proper crew code
	 */
	private Crew crewExists(String crewCode) {
		// Instantiate choreographer
		MainController.instantiateChoreographerFromLogin();
		Choreographer tempChoreographer = MainController.loggedChoreographer;
		
		// Loop through choreographers crews
		for (int index = 0; index < tempChoreographer.crewList.size(); index++) {
			Crew crew = tempChoreographer.crewList.get(index);
			System.out.println(crew.getCrewCode());
			// If the crew code matches what the user inputted, 
			if (crew.getCrewCode().equals(crewCode)) {
				loggedCrew = crew;
				return crew;
			}
		}
		
		// Return null if no such crew exists
		return null;
	}
	
	private boolean nameFoundInCrew(String name) {
		// Loop through the dancers in the crew, seeing if any names match
		for (int index = 0; index < loggedCrew.dancersList.size(); index++) {
			if (loggedCrew.dancersList.get(index).getName().equalsIgnoreCase(name)) {
				loggedDancer = loggedCrew.dancersList.get(index);
				return true;
			}
		}
		
		return false;
	}
	

	/*
	 * This method sets up the font that is used within the panel
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
