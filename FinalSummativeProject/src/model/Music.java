package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Music implements Serializable {
	// Instance Variables
	private String musicPath;
	private double bps;
	
	// Getters and Setters
	
	public String getMusicPath() {
		return musicPath;
	}
	
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
	
	public double getBps() {
		return bps;
	}
	
	public void setBps(double bps) {
		this.bps = bps;
	}

	// toString
	@Override
	public String toString() {
		return "Music [musicPath=" + musicPath + ", bps=" + bps + "]";
	}
}
