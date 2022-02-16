package customcomponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

/**
 * This class represents a custom oval button
 * https://stackoverflow.com/questions/423950/rounded-swing-jbutton-using-java
 */
@SuppressWarnings("serial")
public class CustomButton extends JButton implements MouseListener, MouseMotionListener {

    /** Specify that the button has oval shape (default). */
    public static final int SHAPE_OVAL = 0;
    /** Specify that the button has capsule-like shape. */
    public static final int SHAPE_CAPSULE = 1;
    /** Specify that the capsule shaped button is oriented vertically. */
    public static final int VERTICAL = 2;
    /** Specify that the capsule shaped button is oriented horizontally. */
    public static final int HORIZONTAL = 4;

    private Color colorNormal;
    private Color colorHighlighted;
    private Color colorBorderNormal;
    private Color colorBorderHighlighted;
    private int borderThickness;
    private boolean borderHighlighted;
    private Color currentBackground;

    /** Shape of this button. */
    protected final int shape;
    /** Orientation of this button. */
    protected final int orientation;
    /** Radius of this button. */
    protected double radius;

    /*
     * Construct a default oval button.
     */
    public CustomButton() {
        this(SHAPE_OVAL, VERTICAL);
    }

    /*
     * Construct an oval button with the specified shape and orientation.
     */
    public CustomButton(int shape, int orientation) {
        this(shape, orientation, 0.5, Color.WHITE, Color.LIGHT_GRAY, Color.BLACK, Color.RED);
    }

    /*
     * Construct a button and specify its colors.
     */
    public CustomButton(int shape, int orientation, double radius, Color colorNormal, Color colorHighlighted, Color colorBorderNormal, Color colorBorderHighlighted) {
        super();
        if (shape != SHAPE_CAPSULE && shape != SHAPE_OVAL) {
            throw new IllegalArgumentException("Invalid shape chosen for OvalButton: " + shape);
        }
        if (orientation != VERTICAL && orientation != HORIZONTAL) {
            throw new IllegalArgumentException("Invalid orientation chosen for OvalButton: " + orientation);
        }
        this.shape = shape;
        this.orientation = orientation;
        this.radius = radius;
        this.colorNormal = colorNormal;
        this.currentBackground = colorNormal;
        this.colorHighlighted = colorHighlighted;
        this.colorBorderNormal = colorBorderNormal;
        this.colorBorderHighlighted = colorBorderHighlighted;
        borderThickness = 5;
        borderHighlighted = false;
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Getters and Setters

    public void setColorNormal(Color colorNormal) {
        this.colorNormal = colorNormal;
    }

    public void setColorHighlighted(Color colorHighlighted) {
        this.colorHighlighted = colorHighlighted;
    }

    public void setColorBorderNormal(Color colorBorderNormal) {
        this.colorBorderNormal = colorBorderNormal;
    }

    public void setColorBorderHighlighted(Color colorBorderHighlighted) {
        this.colorBorderHighlighted = colorBorderHighlighted;
    }

    public void setBorderThickness(int borderThickness) {
        if (borderThickness < 0) {
            this.borderThickness = 0;
        }
        else {
            this.borderThickness = borderThickness;
        }
    }
    
    public void setRadius(double newRadius) {
        if (newRadius < 0 || newRadius > 1) {
            throw new IllegalArgumentException("Invalid radius: " + newRadius);
        }
        radius = newRadius;
    }


    public void setHighlightedBorder(boolean isHighlighted) {
        borderHighlighted = isHighlighted;
    }

    @Override
    public void addActionListener(ActionListener l) {
        super.addActionListener(e -> {
            if (isValidClickPosition(MouseInfo.getPointerInfo().getLocation())) {
                l.actionPerformed(e);
            }
        });
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public boolean isBorderHighlighted() {
        return borderHighlighted;
    }

 boolean isValidClickPosition(Point screenPosition) {
        if (shape == SHAPE_OVAL) {
            return isInOval(screenPosition);
        }
        else {
            return isInCapsule(screenPosition);
        }
    }

    protected BufferedImage getBackgroundImage() {
        return null;
    }

    private boolean isInOval(Point p) {
        double x = p.x;
        double y = p.y;

        // Calculate centre of the ellipse.
        double s1 = getLocationOnScreen().x + getSize().width / 2;
        double s2 = getLocationOnScreen().y + getSize().height / 2;

        // Calculate semi-major and semi-minor axis
        double a = getSize().width / 2;
        double b = getSize().height / 2;

        // Check if the given point is withing the specified ellipse:
        return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
    }

    private boolean isInCapsule(Point p) {
        double x = p.x;
        double y = p.y;

        if (orientation == VERTICAL) {
            Double r = 0.5 * radius * getSize().height;
            double buttonX = getLocationOnScreen().x;
            double buttonY = getLocationOnScreen().y;
            if (y < buttonY + r) {
                // check if in upper ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + getSize().width / 2;
                double s2 = buttonY + r;
                double a = getSize().width / 2;
                double b = r;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else if (y > buttonY + getSize().height - r) {
                // check if in lower ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + getSize().width / 2;
                double s2 = buttonY + getSize().height - r;
                double a = getSize().width / 2;
                double b = r;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else {
                return true;
            }
        }
        else {
            Double r = 0.5 * radius * getSize().width;
            double buttonX = getLocationOnScreen().x;
            double buttonY = getLocationOnScreen().y;
            if (x < buttonX + r) {
                // check if in upper ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + r;
                double s2 = buttonY + getSize().height / 2;
                double a = r;
                double b = getSize().height / 2;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else if (x > buttonX + getSize().width - r) {
                // check if in lower ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + getSize().width - r;
                double s2 = buttonY + getSize().height / 2;
                double a = r;
                double b = getSize().height / 2;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else {
                return true;
            }
        }
    }

    // Methods for correct rendering of the button:

    /*
     * This method takes care of rendering the oval button correctly.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        if (shape == SHAPE_OVAL) {
            paintOval(gr);
        }
        else if (shape == SHAPE_CAPSULE) {
            paintCapsule(gr);
        }
        super.paintComponent(g);
    }

    /*
     * This method sets the main background color of the button according to its state.
     */
    private void setMainColor(Graphics g) {
        if (isEnabled()){
            g.setColor(currentBackground);
        }
        else {
            g.setColor(colorNormal);
        }
    }

    /*
     * This method paints oval with border on the button.
     */
    private void paintOval(Graphics2D g) {
        Dimension d = getSize();

        BufferedImage img = getBackgroundImage();
        if (img == null) {
            setMainColor(g);
            g.fillRoundRect(0, 0, d.width, d.height, 50, 50);
        }
        else {
            g.setClip(new Ellipse2D.Double(0,0,d.width,d.height));
            g.drawImage(img, 0,0,getWidth(), getHeight(),this);
        }

        Shape border = createOvalBorder();

        if (borderHighlighted) {
            g.setColor(colorBorderHighlighted);
        }
        else {
            g.setColor(colorBorderNormal);
        }
        g.fill(border);
        g.setClip(0,0,getWidth(),getHeight());
    }

    /*
     * This method creates an oval border shape (like a ring).
     */
    private Shape createOvalBorder() {
        double width = getSize().width;
        double height = getSize().height;
        Ellipse2D outer = new Ellipse2D.Double(0, 0, width, height);
        double inX = (width/2) - (width/2 - borderThickness);
        double inY = (height/2) - (height/2 - borderThickness);
        double inW = width - 2*borderThickness;
        double inH = height - 2*borderThickness;
        Ellipse2D inner = new Ellipse2D.Double(inX, inY, inW, inH);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

    /*
     * This method paints a capsule shape with border to the button.
     */
    private void paintCapsule(Graphics2D g) {
        Shape mainCapsule = createCapsule(0, 0, getWidth(), getHeight());
        BufferedImage img = getBackgroundImage();
        if (img == null) {
            setMainColor(g);
            g.fill(mainCapsule);
        }
        else {
            g.setClip(mainCapsule);
            g.drawImage(img, 0,0,getWidth(), getHeight(),this);
        }

        Shape border = createCapsuleBorder();
        if (borderHighlighted) {
            g.setColor(colorBorderHighlighted);
        }
        else {
            g.setColor(colorBorderNormal);
        }
        g.fill(border);
        g.setClip(0,0,getWidth(),getHeight());
    }

    /*
     * This method creates the capsule shape that corresponds to the current button state (vertical/horizontal).
     */
    private Shape createCapsule(double x, double y, double width, double height) {
        double r;
        Ellipse2D upper;
        Rectangle2D middle;
        Ellipse2D lower;
        if (orientation == VERTICAL) {
            r = 0.5 * radius * height;
            upper = new Ellipse2D.Double(x, y, width, 2 * r);
            middle = new Rectangle2D.Double(x, y + r, width, height - 2 * r);
            lower = new Ellipse2D.Double(x, y + (height - 2 * r), width, 2 * r);
        }
        else {
            r = 0.5 * radius * width;
            upper = new Ellipse2D.Double(x, y, 2 * r, height);
            middle = new Rectangle2D.Double(x + r, y, width - 2 * r, height);
            lower = new Ellipse2D.Double(x + (width - 2 * r), y, 2 * r, height);
        }

        Area capsule = new Area(upper);
        capsule.add(new Area(middle));
        capsule.add(new Area(lower));

        return capsule;
    }

    /**
     * This method creates a capsule border shape.
     */
    private Shape createCapsuleBorder() {
        double width = getSize().width;
        double height = getSize().height;
        Shape outer = createCapsule(0, 0, width, height);
        double inX = (width/2) - (width/2 - borderThickness);
        double inY = (height/2) - (height/2 - borderThickness);
        double inW = width - 2*borderThickness;
        double inH = height - 2*borderThickness;
        Shape inner = createCapsule(inX, inY, inW, inH);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

    // Methods needed for highlighting the button correctly.

    @Override
    public void mousePressed(MouseEvent e) {
        if (isValidClickPosition(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted.darker();
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isValidClickPosition(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted;
        }
        else {
            currentBackground = colorNormal;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isValidClickPosition(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted;
        }
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentBackground = colorNormal;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isValidClickPosition(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted;
        }
        else {
            currentBackground = colorNormal;
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
