package model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Choreographer implements Serializable {
	// Instance Variables
	public ArrayList<Routine> routinesList = new ArrayList<Routine>();
	public ArrayList<Crew> crewList = new ArrayList<Crew>();
		
	// toString method
	
	@Override
	public String toString() {
		return "Choreographer [routinesList=" + routinesList + ", crewList=" + crewList + "]";
	}
}
