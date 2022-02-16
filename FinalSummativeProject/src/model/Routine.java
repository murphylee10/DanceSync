 package model;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class Routine implements Serializable {
	// Instance Variables
	private String name;
	private Music music;
	private Crew crew;
	public List<Formation> formationsList = new ArrayList<Formation>();
	
	// Getters and Setters
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Music getMusic() {
		return music;
	}
	
	public void setMusic(Music music) {
		this.music = music;
	}
	
	public Crew getCrew() {
		return crew;
	}
	
	public void setCrew(Crew crew) {
		this.crew = crew;
	}
	
	
	// toString
	@Override
	public String toString() {
		return "Routine [name=" + name + ", music=" + music + ", crew=" + crew + ", formationsList=" + formationsList
				+ "]";
	}
}
