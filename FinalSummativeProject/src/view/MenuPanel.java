package view;

import javax.swing.*;
import java.awt.*;

// This class extends JPanel, representing a menu bar with the logo, profile picture, and logout button
@SuppressWarnings("serial")
public class MenuPanel extends JPanel {
	// Instance Variables - Swing Components
	private JLabel logoLabel;
	
	// Colors
	public static final Color CREAM = new Color(226, 243, 245);
	
	/*
	 * Constructor Method
	 */
	public MenuPanel() {
		setLayout(null);
		setBounds(0, 0, 1920, 120);
		setupDisplay();
		setBackground(CREAM);
	}
	
	// Utility Methods
	
	// This method adds the logo, profile button, and logout button to the panel
	private void setupDisplay() {
		// Add Logo to the center
		logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon("images/Logo.png"));
		logoLabel.setBounds(800, 15, 602, 90);
		add(logoLabel);
	}
}
