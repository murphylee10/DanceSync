package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * This class represenets the main frame that stores all of the GroupAssist panels
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	// Instance Variables
	public static JPanel currentPanel;
	
	/*
	 * Constructor Method
	 */
	public MainFrame() {
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
		
		add(currentPanel);
	}
}
