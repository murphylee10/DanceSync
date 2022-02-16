package model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Crew implements Serializable {
	// Instance Variables
	private String crewCode;
	private String crewName;
	public ArrayList<Dancer> dancersList = new ArrayList<Dancer>();
	
	// Constructor Methods
	
	// Empty constructor is necessary for ObjectMapper deserialization
	public Crew() {
		
	}
	
	public Crew(String crewName, String crewCode) {
		super();
		this.crewCode = crewCode;
		this.crewName = crewName;
	}
	
	// Getters and Setters
	
	public String getCrewCode() {
		return crewCode;
	}
	public void setCrewCode(String crewCode) {
		this.crewCode = crewCode;
	}
	public String getCrewName() {
		return crewName;
	}
	public void setCrewName(String crewName) {
		this.crewName = crewName;
	}
	
	// toString method
	
	@Override
	public String toString() {
		return "Crew [crewCode=" + crewCode + ", crewName=" + crewName + ", dancersList=" + dancersList + "]";
	}
	
	
}
