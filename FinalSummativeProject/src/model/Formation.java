package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Formation implements Serializable {
	// Instance Variables
	public List<Integer> xPosList;
	public List<Integer> yPosList;
	
	public long transitionStarts;
	public long transitionEnds;
	
	// Constructor Method
	public Formation() {
		xPosList = new ArrayList<Integer>();
		yPosList = new ArrayList<Integer>();
		transitionStarts = -1;
		transitionEnds = -1;
	}

		
	// toString method
	
	@Override
	public String toString() {
		return "Formation [xPosList=" + xPosList + ", yPosList=" + yPosList + ", transitionStarts=" + transitionStarts
				+ ", transitionEnds=" + transitionEnds + "]";
	}
	
}
