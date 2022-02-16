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
 * This class is used to validate the users' login attempts
 */
@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
	// Instance Variables
	private MenuPanel menuPanel;
	private CustomButton studentLoginButton;
	private JPanel borderPanel;
	private JPanel backgroundPanel;
	private CustomPanel loginInputPanel;
	private JLabel sloganLabel;
	private JLabel headerLabel;
	private CustomTextField usernameField;
	private CustomPasswordField passwordField;
	private JButton loginButton;
	private JLabel registerHyperlink;
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
	public static final Color GREY = new Color(195, 195, 195);
	public static final Color PINK = new Color(239, 61, 136);

	/*
	 * Constructor Method
	 */
	public LoginPanel() {
		setLayout(null);
		setBounds(0, 0, 1920, 1080);
		
		// For determining which panel to obtain the username or password from
		MainController.usedPanel = "Login Panel";
		
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
		sloganLabel = new JLabel("\"A dance formation tool for choreographers worldwide\"");
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
		headerLabel = new JLabel("Sign In");
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
		usernameField = new CustomTextField();
		usernameField.setText("Username");
		usernameField.setFont(new Font("Nunito", Font.PLAIN, 30));
		usernameField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(), BorderFactory.createEmptyBorder(0,  25,  0, 0)));
		usernameField.setBounds(150, 150, 700, 100);
		loginInputPanel.add(usernameField);
		
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
		loginInputPanel.add(passwordField);
		
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
		// Register Hyperlink
		registerHyperlink = new JLabel("Register Now");
		registerHyperlink.setFont(new Font("Nunito", Font.PLAIN, 20));
		registerHyperlink.setForeground(WHITE);
		registerHyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		registerHyperlink.setBounds(155, 430, 130, 20);
		loginInputPanel.add(registerHyperlink);
		
		// Underline Hyperlink
		Font font = registerHyperlink.getFont();
		Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		registerHyperlink.setFont(font.deriveFont(attributes));
		
		// ActionListener for Hyperlink
		registerHyperlink.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				MainController.frameState = "Register";
				MainController.newFrame = true;
			}
		});
		
		
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
				if (validateInput() == true) {
					username = usernameField.getText();
					password = String.valueOf(passwordField.getPassword());
					
					MainController.instantiateChoreographerFromLogin();
					MainController.frameState = "Library";
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
				String username = st.nextToken();
				String password = st.nextToken();
				
				// If the username and password match, the users input has been successfully entered
				if (usernameField.getText().equals(username) && String.valueOf(passwordField.getPassword()).equals(password)) {
					return true;
				}
			}
			
			scanner.close();
			
		} 
		catch (FileNotFoundException e) {
		}
		
		// If "true" hasn't been returned, the user didn't enter valid information
		errorLabel.setText("Invalid Username or Password");		
		
		// Add error label with text
		loginInputPanel.add(errorLabel);
		revalidate();
		repaint();
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

