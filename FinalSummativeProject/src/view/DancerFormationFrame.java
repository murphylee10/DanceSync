package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import customcomponents.CustomPanel;
import model.Dancer;
import model.Routine;
import wraplayout.WrapLayout;

/*
 * This class displays the formations of a routine to the dancer (as images)
 */
@SuppressWarnings("serial")
public class DancerFormationFrame extends JFrame {
	// Instance Variables
	private JPanel backgroundPanel;
	private JPanel formationsPanel;
	private JScrollPane formationsScrollPane;
	private JLabel infoLabel;
	
	// Attributes
	private ArrayList<GridPanel> grids = new ArrayList<GridPanel>();
	private ArrayList<JPanel> holdingPanels = new ArrayList<JPanel>();
	private ArrayList<JLabel> formationLabels = new ArrayList<JLabel>();
	private Routine routine;
	private Dancer dancer;
	private ArrayList<ArrayList<CustomPanel>> dancerPanels = new ArrayList<ArrayList<CustomPanel>>();
	private ArrayList<ArrayList<JLabel>> dancerLabels = new ArrayList<ArrayList<JLabel>>();
	
	// Colors
	public static final Color BLUE = new Color(105, 127, 242);
	public static final Color SHADEDBLUE = new Color(53, 78, 204);
	public static final Color PINK = new Color(239, 61, 136);
	public static final Color TEAL = new Color(34, 209, 238);
	public static final Color GRIDFILL = new Color(235, 217, 217);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color GRIDGREY = new Color(122, 122, 122);
	public static final Color BLACK = new Color(0, 0, 0);
	
	
	/*
	 * Constructor method
	 */
	public DancerFormationFrame(Routine routine, Dancer dancer) {
		setLayout(null);
		setSize(1270, 800);
		
		this.routine = routine;
		this.dancer = dancer;
		
		setupFont();
		
		setupTemplate();                // Background Panels
		
		setupIDLabel();
		
		setupFormationScrollpane();     // ScrollPane displaying images
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
	 * This method sets up the label that shows the ID of the dancer
	 */
	private void setupIDLabel() {
		infoLabel = new JLabel();
		infoLabel.setText(String.format("Name: %s , ID: %d", dancer.getName(), dancer.getId()));
		infoLabel.setFont(new Font("Nunito", Font.PLAIN, 40));
		infoLabel.setForeground(WHITE);
		infoLabel.setBackground(SHADEDBLUE);
		infoLabel.setOpaque(true);
		infoLabel.setBounds(0, 20, 1270, 45);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		backgroundPanel.add(infoLabel);
	}
	
	/*
	 * This method sets up the scrollpane that contains the formation images
	 */
	private void setupFormationScrollpane() {
		// Panel
		formationsPanel = new JPanel();
		formationsPanel.setLayout(new WrapLayout(FlowLayout.CENTER, 10, 45));
		formationsPanel.setBackground(SHADEDBLUE);
		
		// Add grids to the panel
		addGrids();
		
		// Scroll Bar
		formationsScrollPane = new JScrollPane(formationsPanel);
		formationsScrollPane.setBorder(new EmptyBorder(getInsets()));
		formationsScrollPane.setBounds(40, 80, 1190, 740);
		backgroundPanel.add(formationsScrollPane);
	}
	
	/*
	 * This method adds the formations to the panel
	 */
	private void addGrids() {
		// Loop through formations in the routine
		for (int index = 0; index < routine.formationsList.size(); index++) {
			System.out.println(routine.formationsList.get(index));
			// Create a new grid
			GridPanel tempGrid = new GridPanel();
			tempGrid.setLayout(null);
			tempGrid.setBackground(GRIDFILL);
			tempGrid.setBorder(new LineBorder(TEAL, 6));
			tempGrid.setPreferredSize(new Dimension(1106, 553));
			
			// Load dancers to the grid
			dancerPanels.add(index, new ArrayList<CustomPanel>());
			dancerLabels.add(index, new ArrayList<JLabel>());
			
			// Loop through the dancers in the crew
			for (int index2 = 0; index2 < routine.getCrew().dancersList.size(); index2++) {
				int x = routine.formationsList.get(index).xPosList.get(index2);
				int y = routine.formationsList.get(index).yPosList.get(index2);
				
				JLabel tempDancerLabel = new JLabel();
				tempDancerLabel.setText(String.valueOf(routine.getCrew().dancersList.get(index2).getId()));
				tempDancerLabel.setFont(new Font("Nunito", Font.PLAIN, 16));
				tempDancerLabel.setForeground(WHITE);
				dancerLabels.get(index).add(index2, tempDancerLabel);
				
				CustomPanel tempDancerPanel = new CustomPanel(35);
				
				// Set the background to a different colour, according to if the id matches the dancer who logged in
				if (dancer.getId() == routine.getCrew().dancersList.get(index2).getId()) {
					tempDancerPanel.setBackground(TEAL);					
				}
				else {
					tempDancerPanel.setBackground(PINK);					
				}
				tempDancerPanel.setLayout(new GridBagLayout());
				tempDancerPanel.setBounds(x, y, 35, 35);
				tempDancerPanel.add(dancerLabels.get(index).get(index2));
				
				dancerPanels.get(index).add(index2, tempDancerPanel);
				
				tempGrid.add(dancerPanels.get(index).get(index2));
			}
			grids.add(index, tempGrid);
			
			// Label for formation
			formationLabels.add(index, new JLabel());
			formationLabels.get(index).setText(String.format("Formation %d", (index + 1)));
			formationLabels.get(index).setFont(new Font("Nunito", Font.PLAIN, 30));
			formationLabels.get(index).setForeground(WHITE);
			formationLabels.get(index).setHorizontalAlignment(SwingConstants.CENTER);
			formationLabels.get(index).setPreferredSize(new Dimension(1170, 35));
			
			// Add label and grid to the holding panel
			holdingPanels.add(new JPanel());
			holdingPanels.get(index).setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
			holdingPanels.get(index).setBackground(SHADEDBLUE);
			holdingPanels.get(index).setPreferredSize(new Dimension(1170, 590));
			
			holdingPanels.get(index).add(formationLabels.get(index));
			holdingPanels.get(index).add(grids.get(index));
			
			// Add grid to the formations panel
			formationsPanel.add(holdingPanels.get(index));
		}
		
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
