/*
 * Name: Murphy Lee
 * Date of Submission: January 18, 2022
 * Course: ICS4U1-01
 * Teacher: Mr.Fernandes 
 * Title: GroupAssist - A dance formation application
 * Description: GroupAssist is a dance formation tool that will help choreographers with the management of their dancers. There are many different ways to 
 * 			arrange a set of dancers, which makes the formation process tedious and time consuming for teachers. With the help of GroupAssist, teachers 
 * 			can preset their formations for any given routine, by dragging and dropping dancer models. Through the dancer login feature, the dancers can also learn their 
 * 			spots within each formation before entering the studio.

 * Major Skills: Java Swing, File Reading & Writing, Object Serialization, Object Oriented Programming, Music Formatting, Music Playing, 
 * 			Data Structures, Access Modifiers, Conditional Loops, For & While Loops, ImageIcons, EventListeners, Math X & Y Calculations, Swing Timers, Layout Managers,
 * 			Input Validation, Animation, CRUD Operations
 *
 * Features: 
 * - *** SYNCING OF TRANSITIONS & MUSIC: The choreographer sets a STARTING POINT and DURATION for each transition between formations. When they press "view dance", they
 * 			can view exactly how the dance would look, and the smooth transitions between formations.
 * - Save and Reload: When the choreographer creates a routine, all of its formations and data are stored to the choreographers account, which they can access at any time.
 * - Crews: The choreographer can add, delete, and edit CREWS, which represent the individual teams within a dance studio. These crews are used to create routines
 * - Dancer Login: The individual dancers within a crew can log in to their specific account, and see where they are in all of their formations.
 * 			(login information: choreographer's username, crew passcode, their full name)
 * 
 * Area of concerns:
 *  - When "viewing the dance", the only function supported is "play". You cannot pause, rewind, or fast forward the dance. Such a feature would require advanced
 *  		 multithreading with the movement animations, rather than the simple use of Swing Timers (which can't be "paused").
 *  - A monitor that supports 1920x1080p is recommended to support the frame size
 *  
 *  Imported APIs:
 *  - TrackAnalyzer: Imported to calculate the BPM of any WAV file (helped with syncing transitions with the music) (also requires commons-math, jaudiotagger, java, jcommander, jtransforms)
 *  - jackson-databind: Imported for Choreographer Object Serialization (also requires jackson-annotations and jackson-core)
 *  - component-mover: Imported for making dancer models "movable", while colliding with the bounds of their parent panel
 *  - wrap-layout: Imported as a custom layout manager (like Flow Layout, but elements will wrap to a new row if necessary)
 *  - table-header-renderer: Imported to center JTable headers
 * 
 * External Resources:
 *  - Customizing a TextField to have round corners: https://stackoverflow.com/questions/8515601/java-swing-rounded-border-for-jtextfield
 *  - Customizing a JPanel to have round corners: https://stackoverflow.com/questions/15025092/border-with-rounded-corners-transparency
 *  - Customizing a JButton to have round corners: https://stackoverflow.com/questions/423950/rounded-swing-jbutton-using-java
 *  - Customizing a JPanel to display a grid: https://www.youtube.com/watch?v=NxUllV998UQ
 *  - Customizing the UI of a JSlider to appear as a music progress bar: https://stackoverflow.com/questions/62609789/change-thumb-and-color-of-jslider
 *  - WrapLayout, a subclass of FlowLayout that does dynamic size allocation: https://tips4java.wordpress.com/2008/11/06/wrap-layout/ 
 */

package application;

import controller.MainController;

public class GroupAssistApplication {
	public static void main(String[] args) {
		new MainController();
	}
}
