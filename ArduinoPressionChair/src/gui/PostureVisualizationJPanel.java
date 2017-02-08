package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import Calculation.Chaise;
import Calculation.Pied;

public class PostureVisualizationJPanel extends JPanel {

	// the global Chaise object; we'll only have one of these at a time
	private Chaise chaise;

	/* setting the scale of the drawing by changing the diameter of 
	 * the circles drawn for the sensors & barycenter
	 */
	public static final int circleDiameter=30;
	
	// diameter of the green area around the barycenter
	public static final int areaDiameter = circleDiameter * 4;

	/**
	 * Constructor method for the visualization panel
	 */
	public PostureVisualizationJPanel() {
		this.chaise=new Chaise();
	}

	// overloading the constructor with a pre-existing Chaise object
	public PostureVisualizationJPanel(Chaise chaise){
		this.chaise=chaise;
	}
	/*

	public PostureVisulatizationJPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PostureVisulatizationJPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PostureVisulatizationJPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	*/

	// --- setters - getters ---
	public void setChaise(Chaise chaise){
		this.chaise=chaise;
	}

	public Chaise getChaise(){
		return this.chaise;
	}

	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// graphics object to draw our stuff
		Graphics2D g2d=(Graphics2D)g;

		// font metrics object
		// we use this to calculate the center of a string of text
		FontMetrics fm=g2d.getFontMetrics();

		// draw the axes
		g2d.setColor(Color.RED);
		g2d.drawLine(0, (getHeight() - 1)/2, getWidth() - 1, (getHeight() - 1)/2);
		g2d.drawLine((getWidth() - 1)/2, 0, (getWidth() - 1)/2, getHeight() - 1);
		
		g2d.setColor(Color.BLACK);

		// draw all the Pieds children of the Chaise object
		ArrayList<Pied> Pieds=chaise.getPieds();
		for(int i=0;i<Pieds.size();i++){
			Pied pied=Pieds.get(i);
			double posX=pied.getPosX();
			double posY=pied.getPosY();
			// set the positions to scale
			int x=scaleX(posX);
			int y=scaleY(posY);
			
			// draw the circles
			/* calculate the center of the text showing the 
			 * ID of the sensor - we need this to draw it inside the circle
			 */
			
			//char[] chars=(pied.getSensorID()+"").toCharArray();
			
			String sensorIDStr=pied.getSensorID()+"";
			int strwidth=fm.stringWidth(sensorIDStr);
			int strHeight=fm.getHeight();
			int stringStartX=x-strwidth/2;
			int stringStartY=y+strHeight/2-fm.getDescent();

			// actually draw the circle and text inside it
			g2d.drawString(sensorIDStr, stringStartX, stringStartY);
			drawCenteredCircle(g2d, x, y, circleDiameter, false);
		}

		// draw the area around the G point (barycenter)
		g2d.setColor(new Color(0.0f, 0.7f, 0.0f, 0.7f));
		drawCenteredCircle(g2d, scaleX(chaise.getAreaX()), scaleY(chaise.getAreaY()), areaDiameter, true);
		
		// set the drawing color back to black
		g2d.setColor(Color.BLACK);
		// draw the G point (barycenter)
		drawCenteredCircle(g2d, scaleX(chaise.getGposX()), scaleY(chaise.getGposY()), circleDiameter, true);

	}
	
	/**
	 * Method that draws a circle from a given center point
	 * (instead of drawing it from the top left)
	 * 
	 * @param g : Graphics2D object
	 * @param x : x coordinate of the center
	 * @param y : y coordinate of the center
	 * @param d : the diameter of the circle
	 * @param fill : true if we want to fill the circle, false if we just want the outline
	 */
	private void drawCenteredCircle(Graphics2D g, int x, int y, int d, boolean fill) {
		if (fill) {
			g.fillOval((x - d / 2), (y - d / 2), d, d);
		} else {
			g.drawOval((x - d / 2), (y - d / 2), d, d);
		}
	}
	
	/**
	 * Method to scale a position on the X axis
	 * 
	 * @param gX : given X coordinate
	 * @return scaledX : the scaled position
	 */
	public int scaleX(double gX) {
		// Actual width is 1 pixel shorter, so we have to cut it out
		// We also cut out 2 radiuses (= a diameter) of a circle
		int width = getWidth() - 1 - circleDiameter;
		
		// the coordinate is scaled down, then moved by a radius
		int scaledX = (int) (gX * width / chaise.getMaxPosX() + circleDiameter/2);
		
		return scaledX;
	}
	
	/**
	 * Method to scale a position on the Y axis
	 * 
	 * @param gY : given Y coordinate
	 * @return scaledY : the scaled position
	 */
	public int scaleY(double gY) {
		int height = getHeight() - 1 - circleDiameter;
		
		int scaledY = (int) (gY * height / chaise.getMaxPosY() + circleDiameter/2);
		
		return scaledY;
	}
}
