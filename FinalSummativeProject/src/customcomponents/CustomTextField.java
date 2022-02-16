package customcomponents;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextField;

/*
 * This class represents a custom textfield used to obtain user login information
 * https://stackoverflow.com/questions/8515601/java-swing-rounded-border-for-jtextfield
 */
@SuppressWarnings("serial")
public class CustomTextField extends JTextField {
    private Shape shape;
    public CustomTextField() {
        super();
        setOpaque(false); // As suggested by @AVD in comment.
    }
    protected void paintComponent(Graphics g) {
         g.setColor(getBackground());
         g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 17, 17);
         super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
         g.setColor(getForeground());
    }
    public boolean contains(int x, int y) {
         if (shape == null || !shape.getBounds().equals(getBounds())) {
             shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 17, 17);
         }
         return shape.contains(x, y);
    }
}
