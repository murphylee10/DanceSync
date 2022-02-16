package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import wraplayout.WrapLayout;

/*
 * This class displays the formations of a routine to the dancer (as images)
 */
@SuppressWarnings("serial")
public class InstructionsFrame extends JFrame {
	// Instance Variables
	private JPanel backgroundPanel;
	private JPanel instructionsPanel;
	private JScrollPane formationsScrollPane;
	private JLabel informationLabel;
	
	// Attributes
	
	// Colors
	public static final Color BLUE = new Color(105, 127, 242);
	public static final Color SHADEDBLUE = new Color(53, 78, 204);
	public static final Color PINK = new Color(239, 61, 136);
	public static final Color TEAL = new Color(34, 209, 238);
	
	
	/*
	 * Constructor method
	 */
	public InstructionsFrame() {
		setLayout(null);
		setSize(1270, 800);

		setupFont();
		
		setupTemplate();                // Background Panels
		
		setupInstructionsScrollpane();     // ScrollPane displaying images
	}
	
	/*
	 * This method sets up the background panels of the current panel
	 */
	private void setupTemplate() {
		// Content Pane
		backgroundPanel = new JPanel();
		backgroundPanel.setLayout(null);
		backgroundPanel.setBackground(BLUE);
		backgroundPanel.setBounds(0, 0, 1270, 800);
		add(backgroundPanel);
	}
	
	
	/*
	 * This method sets up the scrollpane that contains the instructions image
	 */
	private void setupInstructionsScrollpane() {
		// Panel
		instructionsPanel = new JPanel();
		instructionsPanel.setLayout(new WrapLayout(FlowLayout.CENTER, 10, 45));
		instructionsPanel.setBackground(SHADEDBLUE);
		
		// Add label to the panel
		informationLabel = new JLabel();
		informationLabel.setIcon(new ImageIcon("images/Instructions.png"));
		informationLabel.setPreferredSize(new Dimension(1020, 774));
		instructionsPanel.add(informationLabel);
		
		// Scroll Bar
		formationsScrollPane = new JScrollPane(instructionsPanel);
		formationsScrollPane.setBorder(new EmptyBorder(getInsets()));
		formationsScrollPane.setBounds(40, 80, 1190, 740);
		backgroundPanel.add(formationsScrollPane);
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

