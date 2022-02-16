package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Dancer implements Serializable {
	// Instance Variables
	private int id;
	private String name;
	
	// Constructor Methods
	
	// Empty constructor is necessary for ObjectMapper deserialization
	public Dancer() {
		
	}
	public Dancer(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	// Getters and Setters
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	
	// toString method
	public String toString() {
		return "Dancer [id=" + id + ", name=" + name + "]";
	}
}
