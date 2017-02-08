package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
			int ovalCenterX=x+circleDiameter/2;
			int ovalCenterY=y+circleDiameter/2;

			/* calculate the center of the text showing the 
			 * ID of the sensor - we need this to draw it inside the circle
			 */
			char[] chars=(pied.getSensorID()+"").toCharArray();
			String sensorIDStr=pied.getSensorID()+"";
			int strwidth=fm.stringWidth(sensorIDStr);
			int strHeight=fm.getHeight();
			int stringStartX=ovalCenterX-strwidth/2;
			int stringStartY=ovalCenterY+strHeight/2-fm.getDescent();

			// actually draw the circle and text inside it
			g2d.drawString(sensorIDStr, stringStartX, stringStartY);
			g2d.drawOval(x, y, circleDiameter, circleDiameter);
		}

		// draw the G point (barycenter)
		g2d.fillOval(scaleX(chaise.getGposX()), scaleY(chaise.getGposY()), circleDiameter, circleDiameter);

	}

	// --- methods to set the scale of the drawing ---
	public int scaleX(double posX){
		/* set the width to be the width of the screen minus one
		 * (this is because of JPanel, so it must be hard-coded)
		 * 
		 * then, also subtract one diameter of a circle since we
		 * want to "move the end of the screen" a diameter back
		 * so that we don't draw circles outside of the screen
		 */
		int width=getWidth()-circleDiameter-1;
		
		// simply multiply the ratio of posX/maxPosX by the width
		int Xvalue=(int)(width*(posX/chaise.getMaxPosX()));
		return Xvalue;
	}

	public int scaleY(double posY){
		// this is exactly like scaleX
		int height=getHeight()-circleDiameter-1;
		int Yvalue=(int)(height*(posY/chaise.getMaxPosY()));
		return Yvalue;
	}
}
