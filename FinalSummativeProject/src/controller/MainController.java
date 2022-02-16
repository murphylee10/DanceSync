package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

import model.Choreographer;
import model.Crew;
import model.Music;
import model.Routine;
import view.DancerLibraryPanel;
import view.DancerLoginPanel;
import view.LibraryPanel;
import view.LoginPanel;
import view.MainFrame;
import view.RegisterPanel;
import view.RoutineInputFrame;
import view.WorkspacePanel;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.ofai.music.beatroot.BeatRoot;

/*
 * This class is the controller for interconnecting the GUI components and models
 */
public class MainController {
	// Instance Variables
	public static String frameState;
	public static boolean newFrame = false;
	
	// Frames & Panels
	public static MainFrame mainFrame;
	public static LoginPanel loginPanel;
	public static RegisterPanel registerPanel;
	public static LibraryPanel libraryPanel;
	public static WorkspacePanel workspacePanel;
	public static DancerLoginPanel dancerLoginPanel;
	public static DancerLibraryPanel dancerLibraryPanel;
	
	// Models
	public static Choreographer loggedChoreographer = new Choreographer();
	public static Routine newRoutine = new Routine();
	public static String usedPanel;
	
	/*
	 *  Constructor Method
	 */
	public MainController() {
		// Add login frame to MainFrame by default
		frameState = "Login";
		loginPanel = new LoginPanel();
		MainFrame.currentPanel = loginPanel;
		mainFrame = new MainFrame();
		mainFrame.setVisible(true);
		
		// Infinite loop for switching between panels effectively
		while (true) {
			System.out.print("");
			if (newFrame == true) {
				// Login Page
				if (frameState.equals("Login")) {
					// Instantiate Panel, reloading the frame containing the panels
					loginPanel = new LoginPanel();
					mainFrame.remove(MainFrame.currentPanel);
					MainFrame.currentPanel = loginPanel;
					mainFrame.add(MainFrame.currentPanel);
					mainFrame.repaint();
					mainFrame.revalidate();
					newFrame = false;
				}
				
				// Register Page
				if (frameState.equals("Register")) {
					// Instantiate Panel, reloading the frame containing the panels
					registerPanel = new RegisterPanel();
					mainFrame.remove(MainFrame.currentPanel);
					MainFrame.currentPanel = registerPanel;
					mainFrame.add(MainFrame.currentPanel);
					mainFrame.repaint();
					mainFrame.revalidate();
					newFrame = false;
				}
				
				// Library Page
				if (frameState.equals("Library")) {
					// Instantiate Panel, reloading the frame containing the panels
					libraryPanel = new LibraryPanel();
					mainFrame.remove(MainFrame.currentPanel);
					MainFrame.currentPanel = libraryPanel;
					mainFrame.add(MainFrame.currentPanel);
					mainFrame.repaint();
					mainFrame.revalidate();
					newFrame = false;
				}
				
				// Workspace for a New Routine
				if (frameState.equals("New Workspace")) {
					// Setup a new routine, adding it to the routine list
//					newRoutine.setName(RoutineInputFrame.routineName);
//					newRoutine.setCrew(loggedChoreographer.crewList.get(RoutineInputFrame.selectedCrewIndex));
//					loggedChoreographer.routinesList.add(newRoutine);
					
					// Create a new panel, using the newly constructed routine
					int tempIndex = loggedChoreographer.routinesList.size() - 1;
					workspacePanel = new WorkspacePanel(loggedChoreographer.routinesList.get(tempIndex), true, tempIndex);
					mainFrame.remove(MainFrame.currentPanel);
					MainFrame.currentPanel = workspacePanel;
					mainFrame.add(MainFrame.currentPanel);
					mainFrame.repaint();
					mainFrame.revalidate();
					newFrame = false;
				}
				
				// Workspace for an Existing Routine
				if (frameState.equals("Load Workspace")) {
					// Obtain the selected routine from the index of buttons that was selected
					int tempIndex = LibraryPanel.selectedRoutineIndex;
					
					// Get the latest version of the choreographer from the JSON file
					instantiateChoreographerFromLogin();
					
					// Create a new panel
					workspacePanel = new WorkspacePanel(loggedChoreographer.routinesList.get(tempIndex), false, tempIndex);
					mainFrame.remove(MainFrame.currentPanel);
					MainFrame.currentPanel = workspacePanel;
					mainFrame.add(MainFrame.currentPanel);
					mainFrame.repaint();
					mainFrame.revalidate();
					newFrame = false;
					
				}
				
				// Login Page for a Dancer
				if (frameState.equals("Dancer Login")) {
					dancerLoginPanel = new DancerLoginPanel();
					mainFrame.remove(MainFrame.currentPanel);
					MainFrame.currentPanel = dancerLoginPanel;
					mainFrame.add(MainFrame.currentPanel);
					mainFrame.repaint();
					mainFrame.revalidate();
					newFrame = false;
				}
				
				// Library Page for a Dancer
				if (frameState.equals("Dancer Library")) {
					// Obtain the list of routines that the dancer is in
					ArrayList<Routine> tempRoutines = getRoutinesFromCrew(DancerLoginPanel.loggedCrew);
					
					dancerLibraryPanel = new DancerLibraryPanel(tempRoutines, DancerLoginPanel.loggedDancer);
					mainFrame.remove(MainFrame.currentPanel);
					MainFrame.currentPanel = dancerLibraryPanel;
					mainFrame.add(MainFrame.currentPanel);
					mainFrame.repaint();
					mainFrame.revalidate();
					newFrame = false;
				}
			}
			
			
		}
	}
	
	/*
	 * This class uses an ObjectMapper to load all object states from a JSON file (when an existing user logs in)
	 */
	public static void instantiateChoreographerFromLogin() {
		String filePath;
		try {
			// Get the username from the login panel or register panel depending on which was used
			if (usedPanel.equals("Login Panel")) {
				filePath = "files/" + LoginPanel.username + ".json";
			}
			
			else if (usedPanel.equals("Register Panel")) {
				filePath = "files/" + RegisterPanel.username + ".json";
			}
			
			else {
				filePath = "files/" + DancerLoginPanel.choreoUsername + ".json";
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			File file = new File(filePath);
			
			// Use the object mapper to read the JSON data to the object
			loggedChoreographer = objectMapper.readValue(file, Choreographer.class);
			
		} 
		
		catch (Exception e) {
			e.printStackTrace();		
		}
	}
	
	/*
	 * This class uses an ObjectMapper to save the choreographers data to a JSON file (the file name corresponds to the choreographers username)
	 */
	public static void saveChoreographerData() {
		String filePath;
		try {
			// Get the username from the login panel or register panel depending on which was used
			if (usedPanel.equals("Login Panel")) {
				filePath = "files/" + LoginPanel.username + ".json";
			}
			
			else {
				filePath = "files/" + RegisterPanel.username + ".json";
			}
			
			// Use a FileOutputStream to write to the assigned file path
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectMapper mapper = new ObjectMapper();
			
			// Use the ObjectMapper to write to the JSON file (huan readable)
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggedChoreographer);
			fos.write(jsonStr.getBytes());
			fos.close();
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method registers a new account by writing their data to the "Registered Accounts" file
	 */
	public static void registerChoreographer() {
		try {
			// Write the username, password, and filename to the Registered Accounts txt file
			Formatter formatter = new Formatter(new BufferedWriter(new FileWriter("files/RegisteredAccounts.txt", true)));
			formatter.format("%s %s %s%n", RegisterPanel.username, RegisterPanel.password, RegisterPanel.username + ".json");
			formatter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method takes a music file path and analyzes it, and then adds the routine to the choreographer object
	 */
	public static void addRoutine() {
		// Create new music and routine objects
		Music music = new Music();
		newRoutine = new Routine();
		
		// Analyze the clip, create a music object, and add the routine to the choreographer's list
		newRoutine.setName(RoutineInputFrame.routineName);
		newRoutine.setCrew(loggedChoreographer.crewList.get(RoutineInputFrame.selectedCrewIndex));
		music.setMusicPath(RoutineInputFrame.musicFilePath);
		double bps = BeatRoot.getBPM(RoutineInputFrame.musicFilePath) / 60;
		music.setBps(bps);
		newRoutine.setMusic(music);
		loggedChoreographer.routinesList.add(newRoutine);
	}
	
	/*
	 * This method accepts a crew, and obtains the routines that the crew is in
	 */
	private ArrayList<Routine> getRoutinesFromCrew(Crew crew) {
		ArrayList<Routine> tempRoutines = new ArrayList<Routine>();
		
		// Loop through the choreographers routines, adding to the routines if the crew objects are the same
		for (int index = 0; index < loggedChoreographer.routinesList.size(); index++) {
			String s1 = loggedChoreographer.routinesList.get(index).getCrew().toString();
			String s2 = crew.toString();
			
			// If the 2 objects match, add the routine to the list
			if (s1.equals(s2)) {
				tempRoutines.add(loggedChoreographer.routinesList.get(index));
			}
		}
		
		return tempRoutines;
	}
}
