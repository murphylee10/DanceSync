package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.LineBorder;

import customcomponents.CustomPanel;
import customcomponents.CustomSliderUI;
import model.Crew;
import model.Routine;

/*
 * This class is a frame used to present the animated dance from the workspace
 */
@SuppressWarnings("serial")
public class PresentDanceFrame extends JFrame {
	// Instance Variables
	
	// GUI Components
	private JPanel fillerPanel;
	private JPanel backgroundPanel;
	private GridPanel gridPanel;
	private JLabel routineLabel;
	private JSlider musicProgressSlider;
	private JButton playDanceButton;
	private JLabel timestampLabel;
	
	private ArrayList<CustomPanel> dancerPanels;
	private ArrayList<JLabel> dancerLabels;
	
	// Timers and Threads
	private Timer sliderTimer;                           // Controlling music slider
	private Timer colorTimer;                            // Changing dancer colors
	private MoveDancerListener moveDancerListener;       // Initiating the movement of dancers
	private Timer moveDancerTimer;                      
	private MoveDancerListener2 moveDancerListener2;     // Actual movement of dancers
	private Timer moveDancerTimer2;                 
	private CheckMusicListener checkMusicListener;       // Start moveDancerListener according to time of song
	private Timer checkMusicTimer;
	private DisposeFrameListener disposeFrameListener;   // Dispose the frame when the clip stops
	private Timer disposeFrameTimer;
	
	// Attributes
	private Routine routine;
	private Clip clip;
	private int currentFormationIndex;
	private int colorIndex;
	
	
	// Color
	public static final Color BLUE = new Color(105, 127, 242);
	public static final Color PINK = new Color(239, 61, 136);
	public static final Color TEAL = new Color(34, 209, 238);
	public static final Color GRIDFILL = new Color(235, 217, 217);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color SHADEDBLUE = new Color(53, 78, 204);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color YELLOW = new Color(255, 248, 13);
	public static final Color ORANGE = new Color(255, 128, 0);
	public static final Color GREEN = new Color(102, 255, 102);
	public static final Color PURPLE = new Color(127, 0, 255);

	public static final Color[] colorList = {PINK, RED, ORANGE, GREEN, TEAL, BLUE, PURPLE};
	/*
	 * Constructor Method
	 */
	public PresentDanceFrame(Routine routine) {
		setLayout(null);
		setSize(1270, 900);
			
		// Stop all timers if the window closes
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (sliderTimer != null) {
					sliderTimer.stop();					
				}
				if (colorTimer != null) {
					colorTimer.stop();	
					
				}
				if (moveDancerTimer != null) {
					moveDancerTimer.stop();					
				}
				
				if (moveDancerTimer2 != null) {
					moveDancerTimer2.stop();					
					
				}
				
				if (checkMusicTimer != null) {
					checkMusicTimer.stop();
				}
				clip.close();
			}
		});
		
		this.routine = routine;
		currentFormationIndex = 0;
		colorIndex = 0;
		
		setupTemplate();         // Background panels
		
		setupGridAndDancers();   // Grid and dancers
		
		setupPlayControls();     // Music controls
		
		setupRoutineLabel();     // Routine label
		
		setupTimers();           // Animation and background timers
	}
	
	/*
	 * This method sets up the background panels of the current panel
	 */
	private void setupTemplate() {
		// Content Pane
		fillerPanel = new JPanel();
		fillerPanel.setLayout(null);
		fillerPanel.setBackground(BLUE);
		fillerPanel.setBounds(0, 0, 1270, 900);
		add(fillerPanel);
		
		backgroundPanel = new JPanel();
		backgroundPanel.setLayout(null);
		backgroundPanel.setBackground(SHADEDBLUE);
		backgroundPanel.setBounds(39, 90, 1190, 600);
		fillerPanel.add(backgroundPanel);
	}
	
	/*
	 * This method instantiates timers for different tasks
	 */
	private void setupTimers() {
		// Controlling the slider
		SliderTimeListener timeListener = new SliderTimeListener();
		sliderTimer = new Timer(1000, timeListener);
		
		// Color changing
		// Calculate the milliseconds that each beat consumes
		double clipLengthMS = (double) (clip.getMicrosecondLength() / 1000);
		int spb = (int) (clipLengthMS / ((routine.getMusic().getBps() / 1000) * clipLengthMS));
		
		// Set a TimeListener to animate the color change with each beat
		ColorTimeListener colorTimeListener = new ColorTimeListener();
		colorTimer = new Timer(spb, colorTimeListener);
		
		// Set a listener for moving dancers
		moveDancerListener = new MoveDancerListener();
//		moveDancerTimer = new Timer(100, moveDancerListener);
		
		
	}
	
	/*
	 * This method sets up the grid with the initial formation of the routine
	 */
	private void setupGridAndDancers() {
		// Grid Panel
		gridPanel = new GridPanel();
		gridPanel.setLayout(null);
		gridPanel.setBackground(GRIDFILL);
		gridPanel.setBorder(new LineBorder(TEAL, 6)); 
		gridPanel.setBounds(42, 23, 1106, 553);
		backgroundPanel.add(gridPanel);
		
		// Loading dancers
		Crew crew = routine.getCrew();
		
		dancerPanels = new ArrayList<CustomPanel>();
		dancerLabels = new ArrayList<JLabel>();
		
		// Loop through each dancer, setting the x and y location values
		for (int index = 0; index < crew.dancersList.size(); index++) {
			int x = routine.formationsList.get(currentFormationIndex).xPosList.get(index);
			int y = routine.formationsList.get(currentFormationIndex).yPosList.get(index);
			
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
						
			dancerPanels.add(index, tempDancerPanel);
			
			// Add the dancers to the grid
			gridPanel.add(dancerPanels.get(index));
		}
	}
	
	/*
	 * This method sets up the progress bar and the play button for the dance
	 */
	private void setupPlayControls() {
		File file = new File(routine.getMusic().getMusicPath());
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
					
			musicProgressSlider = new JSlider(JSlider.HORIZONTAL, 0, (int) (clip.getMicrosecondLength() / 1000), 0) {
				@Override
                public void updateUI() {
                    setUI(new CustomSliderUI(this));
                }
			};
			musicProgressSlider.setOpaque(false);
			musicProgressSlider.setEnabled(false);
			musicProgressSlider.setBounds(160, 730, 950, 40);
			fillerPanel.add(musicProgressSlider);
			
			// Play button for music
			playDanceButton = new JButton();
			playDanceButton.setIcon(new ImageIcon("images/IconPlayButton.png"));
			playDanceButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			playDanceButton.setBounds(602, 770, 65, 65);
			fillerPanel.add(playDanceButton);
			
			// Action Listener - start music, and all timers
			playDanceButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO
					clip.start();
					
					colorTimer.start();
					
					// Updating music slider
					sliderTimer.start();
					
					// Disable play button
					playDanceButton.setEnabled(false);
					
					// Set transition
					checkMusicListener = new CheckMusicListener();
					checkMusicTimer = new Timer(5, checkMusicListener);
					checkMusicTimer.start();
					
					// Dispose when clip playback finishes
					disposeFrameListener = new DisposeFrameListener();
					disposeFrameTimer = new Timer(100, disposeFrameListener);
					disposeFrameTimer.start();
					
				}
			});
			
			// Showing music timestamp
			timestampLabel = new JLabel();
			timestampLabel.setText(getTimestamp());
			timestampLabel.setFont(new Font("Nunito", Font.PLAIN, 20));
			timestampLabel.setForeground(WHITE);
			timestampLabel.setBounds(1118, 735, 200, 32);
			fillerPanel.add(timestampLabel);
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	/*
	 * This method sets up the label that shows the ID of the dancer
	 */
	private void setupRoutineLabel() {
		routineLabel = new JLabel();
		routineLabel.setText(routine.getName());
		routineLabel.setFont(new Font("Nunito", Font.PLAIN, 40));
		routineLabel.setForeground(WHITE);
		routineLabel.setBackground(SHADEDBLUE);
		routineLabel.setOpaque(true);
		routineLabel.setBounds(0, 20, 1270, 45);
		routineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fillerPanel.add(routineLabel);
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
	 * Methods that control that music slider
	 */
	class SliderTimeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			updateSlider();
			updateLabel();
		}
		
		/*
		 * This method sets the value of the slider according to the current position of the song
		 */
		private void updateSlider() {
			musicProgressSlider.setValue((int) (clip.getMicrosecondPosition() / 1000));
		}
		
		/*
		 * This method updates the label according to the current position of the song
		 */
		private void updateLabel() {
			timestampLabel.setText(getTimestamp());
		}
	}
	
	/*
	 * Methods that change the colour of the circle
	 */
	class ColorTimeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Increment color counter
			colorIndex = (colorIndex + 1) % 7;
			// Loop through each dancer panel
			for (int index = 0; index < dancerPanels.size(); index++) {
				dancerPanels.get(index).setBackground(colorList[colorIndex]);
			}
		}
	}
	
	// Instance variables for data that is used only within the next few class listeners
	
	private final int DIVISOR = 20;
	private ArrayList<Integer> currentxList;
	private ArrayList<Integer> currentyList;
	private ArrayList<Integer> nextxList;
	private ArrayList<Integer> nextyList;
	private ArrayList<Integer> dxList;
	private ArrayList<Integer> dyList;
	private ArrayList<Integer> vxList;
	private ArrayList<Integer> vyList;
	private int animationRate;
	
	/*
	 *  Class for initiating the movement of dancers
	 */
	class MoveDancerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// Current coordinates of each dancer panel
			currentxList = new ArrayList<Integer>();
			currentyList = new ArrayList<Integer>(); 
			for (int index1 = 0; index1 < dancerPanels.size(); index1++) {
				currentxList.add(index1, dancerPanels.get(index1).getLocation().x);
				currentyList.add(index1, dancerPanels.get(index1).getLocation().y);
			}
			
			// Next x and y values
			nextxList = new ArrayList<Integer>();
			nextyList = new ArrayList<Integer>();
			
			// The distance that each dancer panel needs to travel
			dxList = new ArrayList<Integer>();
			dyList = new ArrayList<Integer>(); 
			
			// The velocity of each dancer panel
			vxList = new ArrayList<Integer>();
			vyList = new ArrayList<Integer>(); 
			
			
			
			// Get the velocity of x and y required for each dancer
			for (int index = 0; index < routine.getCrew().dancersList.size(); index++) {
				nextxList.add(index, routine.formationsList.get(currentFormationIndex + 1).xPosList.get(index));
				nextyList.add(index, routine.formationsList.get(currentFormationIndex + 1).yPosList.get(index));
				
				dxList.add(index, routine.formationsList.get(currentFormationIndex + 1).xPosList.get(index) 
						- routine.formationsList.get(currentFormationIndex).xPosList.get(index));
				dyList.add(index, routine.formationsList.get(currentFormationIndex + 1).yPosList.get(index) 
						- routine.formationsList.get(currentFormationIndex).yPosList.get(index));
				
				
				vxList.add(index, dxList.get(index) / DIVISOR);
				vyList.add(index, dyList.get(index) / DIVISOR);
			}
			
			// Get total transition time
			double msStartTime = (double)(routine.formationsList.get(currentFormationIndex).transitionStarts) / 1000;
			double msEndTime = (double)(routine.formationsList.get(currentFormationIndex).transitionEnds) / 1000;
			int transitionTimeMS = (int) (msEndTime - msStartTime);
			
			// Get the rate of animation
			animationRate = transitionTimeMS / DIVISOR;
			
			moveDancerListener2 = new MoveDancerListener2();
			moveDancerTimer2 = new Timer(animationRate, moveDancerListener2);
			moveDancerTimer2.start();
						
			moveDancerTimer.stop();
			currentFormationIndex++;
		}
		
	} 
	
	/*
	 * Class for moving each dancer
	 */
	class MoveDancerListener2 implements ActionListener {
		private int count = 0;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// Stop music listener when the its the last formation
			if (count == DIVISOR) {
				moveDancerTimer2.stop();
			}
			
			// Move dancers
			else {
				// Loop through panels
				System.out.println("Hi");
				System.out.println(count);
				for (int index = 0; index < dancerPanels.size(); index++) {
					// Set to next formation bounds if it's on the last iteration
					if (count == DIVISOR - 1) {
						dancerPanels.get(index).setLocation(nextxList.get(index), nextyList.get(index));
						
					}
					else {
						dancerPanels.get(index).setLocation(currentxList.get(index) + vxList.get(index), currentyList.get(index) + vyList.get(index));
						currentxList.set(index, dancerPanels.get(index).getLocation().x);
						currentyList.set(index, dancerPanels.get(index).getLocation().y);						
					}
				}
				repaint();
				count++;
			}
		}
		
	}
	
	/*
	 * Class for triggering a formation change according to the music position
	 */
	class CheckMusicListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Stop music listener when the its the last formation
			if (currentFormationIndex == routine.formationsList.size() - 1) {
				checkMusicTimer.stop();
			}
			
			// Start animation timer according to the music position
			else if (routine.formationsList.get(currentFormationIndex).transitionStarts <= clip.getMicrosecondPosition() - 20000) {
				moveDancerTimer = new Timer(0, moveDancerListener);
				moveDancerTimer.start();
			}
			
		}
	}
	
	class DisposeFrameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (clip.isRunning() == false) {
				dispose();
			}
		}
		
	}
}
