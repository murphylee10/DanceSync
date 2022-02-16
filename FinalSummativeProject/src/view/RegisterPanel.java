package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.*;

import controller.MainController;
import customcomponents.CustomButton;
import customcomponents.CustomPanel;
import customcomponents.CustomPasswordField;
import customcomponents.CustomTextField;

/*
 * This class represents the panel for obtaining user registration input
 */
@SuppressWarnings("serial")
public class RegisterPanel extends JPanel {
	// Instance Variables
	
	// Swing Components
	private MenuPanel menuPanel;
	private CustomButton studentLoginButton;
	private JPanel borderPanel;
	private JPanel backgroundPanel;
	private CustomPanel registerInputPanel;
	private JLabel sloganLabel;
	private JLabel headerLabel;
	private CustomTextField usernameField;
	private CustomPasswordField passwordField;
	private JButton registerButton;
	private JLabel loginHyperlink;
	private JLabel errorLabel;
	
	// Attributes
	public static String username;
	public static String password;
	
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
	public RegisterPanel() {
		setLayout(null);
		setBounds(0, 0, 1920, 1080);
		
		// For determining which panel to obtain the username or password from
		MainController.usedPanel = "Register Panel";
		
		setupFont();         // Setup Font
		
		setupTemplate();     // Setup Background Panels
		
		setupSlogan();       // Setup Slogan Label
		
		setupRegisterPanel();   // Setup Login Panel
		
		setupHeader();       // Setup Header Labels
		
		setupTextFields();   // Text Fields

		setupButtons();      // Setup Buttons
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
		
		// Button to go to dancer login page
		studentLoginButton = new CustomButton(1, 4, 0.25, PINK, PINK, PINK, PINK);
		studentLoginButton.setText("Student Login");
		studentLoginButton.setFont(new Font("Nunito", Font.PLAIN, 16));
		studentLoginButton.setForeground(WHITE);
		studentLoginButton.setBackground(PINK);
		studentLoginButton.setHorizontalTextPosition(SwingConstants.CENTER);
		studentLoginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		studentLoginButton.setBounds(1710, 40, 150, 40);
		menuPanel.add(studentLoginButton);
		
		// ActionListener - Clear current choreographer reference and go back to login page
		studentLoginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainController.frameState = "Dancer Login";
				MainController.newFrame = true;
			}
		});
	}
	
	/*
	 * This method sets up the label that displays the company slogan
	 */
	private void setupSlogan() {
		// Slogan Label
		sloganLabel = new JLabel("\"Create your own original formations with just a few clicks\"");
		sloganLabel.setFont(new Font("Nunito", Font.PLAIN, 30));
		sloganLabel.setForeground(WHITE);
		sloganLabel.setBounds(460, 100, 1000, 30);
		sloganLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(sloganLabel);
	}
	
	/*
	 * This method sets up the panel for storing user-input components
	 */
	private void setupRegisterPanel() {
		// Create and set properties of the panel
		registerInputPanel = new CustomPanel(20);
		registerInputPanel.setLayout(null);
		registerInputPanel.setBackground(LIGHTBLUE);
		registerInputPanel.setBounds(460, 160, 1000, 600);
		backgroundPanel.add(registerInputPanel);
	}
	
	/*
	 * This method sets up the header labels for prompting user input
	 */
	private void setupHeader() {
		// Header label
		headerLabel = new JLabel("Register");
		headerLabel.setFont(new Font("Nunito", Font.PLAIN, 50));
		headerLabel.setForeground(WHITE);
		headerLabel.setBounds(0, 50, 1000, 55);
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		registerInputPanel.add(headerLabel);
		
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
		usernameField = new CustomTextField();
		usernameField.setText("Username");
		usernameField.setFont(new Font("Nunito", Font.PLAIN, 30));
		usernameField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(), BorderFactory.createEmptyBorder(0,  25,  0, 0)));
		usernameField.setBounds(150, 150, 700, 100);
		registerInputPanel.add(usernameField);
		
		// FocusListener for username
		usernameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (usernameField.getText().equals("Username")) {
					usernameField.setText("");	
				}
			}
		});
		
		// TextField for obtaining password
		passwordField = new CustomPasswordField();
		passwordField.setText("Password");
		passwordField.setFont(new Font("Nunito", Font.PLAIN, 30));
		passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(), BorderFactory.createEmptyBorder(0,  25,  0, 0)));
		passwordField.setBounds(150, 280, 700, 100);
		passwordField.setEchoChar((char)0);
		registerInputPanel.add(passwordField);
		
		// FocusListener for password
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (String.valueOf(passwordField.getPassword()).equals("Password")) {
					passwordField.setText("");	
				}
				passwordField.setEchoChar('*');
			}
		});
		
	}
	
	/*
	 * This method sets up both the "go to register" link and the "login" button
	 */
	private void setupButtons() {
		// Login Hyperlink
		loginHyperlink = new JLabel("Back to Login");
		loginHyperlink.setFont(new Font("Nunito", Font.PLAIN, 20));
		loginHyperlink.setForeground(WHITE);
		loginHyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginHyperlink.setBounds(155, 430, 135, 20);
		registerInputPanel.add(loginHyperlink);
		
		// Underline Hyperlink
		Font font = loginHyperlink.getFont();
		Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		loginHyperlink.setFont(font.deriveFont(attributes));
		
		// ActionListener for Hyperlink
		loginHyperlink.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				MainController.frameState = "Login";
				MainController.newFrame = true;
			}
		});
		
		
		// Register Button
		registerButton = new CustomButton(1, 4, 0.25, TEAL, TEAL, TEAL, TEAL);
		registerButton.setText("Register");
		registerButton.setFont(new Font("Nunito", Font.PLAIN, 25));
		registerButton.setForeground(WHITE);
		registerButton.setBackground(TEAL);
		registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		registerButton.setBounds(645, 420, 200, 60);
		registerInputPanel.add(registerButton);
		
		// ActionListener for Register Button
		registerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validateInput() == true) {
					username = usernameField.getText();
					password = String.valueOf(passwordField.getPassword());
					MainController.registerChoreographer();
					MainController.frameState = "Library";
					MainController.newFrame = true;
					
				}
			}
		});
	}
	
	/*
	 * This method validates the input given by the user
	 */
	private boolean validateInput() {
		boolean valid = true;
		// The fields must be alphanumeric
		for (char character : usernameField.getText().toCharArray()) {
			if (!(Character.isLetterOrDigit(character))) {
				errorLabel.setText("AlphaNumeric Characters Only (Username)");
				valid = false;
			}
		}
		
		for (char character : String.valueOf(passwordField.getPassword()).toCharArray()) {
			if (!(Character.isLetterOrDigit(character))) {
				errorLabel.setText("AlphaNumeric Characters Only (Password)");
				valid = false;
			}
		}
		
		// The username and password must be longer than 4 characters
		if (usernameField.getText().length() <= 4) {
			errorLabel.setText("Username must be at least 5 characters");
			valid = false;
		}
		
		else if (String.valueOf(passwordField.getPassword()).length() <= 4) {
			errorLabel.setText("Password must be at least 5 characters");
			valid = false;
		}
		
		// Check if the username is already taken
		Scanner scanner;
		try {
			scanner = new Scanner(new File("files/RegisteredAccounts.txt"));
			while (scanner.hasNextLine()) {
				StringTokenizer st = new StringTokenizer(scanner.nextLine());
				String atmpUser = st.nextToken();
				if (usernameField.getText().equals(atmpUser)) {
					errorLabel.setText("This username is already in use. Choose another one");
					valid = false;
				}
			}
			
			scanner.close();
			
		} 
		catch (FileNotFoundException e) {
			valid = true;
		}
		
		
		registerInputPanel.add(errorLabel);
		revalidate();
		repaint();
		return valid;
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

