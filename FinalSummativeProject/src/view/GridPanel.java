package view;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")

/*
 * This class represents a custom JPanel that displays a grid
 * A few lines were from https://www.youtube.com/watch?v=NxUllV998UQ
 */
public class GridPanel extends JPanel {
	// Instance Variables
	public static int columns = 14;
	public static int rows = 7;
	public static int cellSize = 79;
	public static int startX;
	public static int startY;
	
	// Colors
	public static final Color GRIDGREY = new Color(122, 122, 122);
	
	/*
	 * Overriding the paint method to display a grid
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(GRIDGREY);
		
		// Formula for DRAWLINE: x1, y1, x2, y2
		// Draw a sequence of horizontal lines, incrementing the y position according to the index
		for (int index = 0; index < rows + 1; index++) {
			g.drawLine(startX, startY + (index * cellSize), startX + (columns * cellSize), startY + (index * cellSize));
		}
		
		// Draw a sequence of vertical lines, incrementing the x position according to the index
		for (int index = 0; index < columns + 1; index++) {
			g.drawLine(startX + (index * cellSize), startY, startX + (index * cellSize), startY + (rows * cellSize));
		}
	}
}
