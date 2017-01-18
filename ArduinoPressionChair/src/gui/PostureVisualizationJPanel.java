package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JPanel;

import Calculation.Chaise;
import Calculation.Pied;

public class PostureVisualizationJPanel extends JPanel {

	private Chaise chaise;

	public static final int circleDiameter=20;

	public PostureVisualizationJPanel() {
		this.chaise=new Chaise();
	}

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

	public void setChaise(Chaise chaise){
		this.chaise=chaise;
	}

	public Chaise getChaise(){
		return this.chaise;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);



		Graphics2D g2d=(Graphics2D)g;

		FontMetrics fm=g2d.getFontMetrics();

		g2d.setColor(Color.BLACK);

		ArrayList<Pied> Pieds=chaise.getPieds();
		for(int i=0;i<Pieds.size();i++){
			Pied pied=Pieds.get(i);
			double posX=pied.getPosX();
			double posY=pied.getPosY();
			int x=scaleX(posX);
			int y=scaleY(posY);
			int ovalCenterX=x+circleDiameter/2;
			int ovalCenterY=y+circleDiameter/2;

			char[] chars=(pied.getSensorID()+"").toCharArray();
			String sensorIDStr=pied.getSensorID()+"";
			int strwidth=fm.stringWidth(sensorIDStr);
			int strHeight=fm.getHeight();
			int stringStartX=ovalCenterX-strwidth/2;
			int stringStartY=ovalCenterY+strHeight/2-fm.getDescent();


			g2d.drawString(sensorIDStr, stringStartX, stringStartY);
			g2d.drawOval(x, y, circleDiameter, circleDiameter);
		}

		g2d.fillOval(scaleX(chaise.getGposX()), scaleY(chaise.getGposY()), circleDiameter, circleDiameter);

	}

	public int scaleX(double posX){
		int width=getWidth()-circleDiameter-1;
		int Xvalue=(int)(width*(posX/chaise.getMaxPosX()));
		return Xvalue;
	}

	public int scaleY(double posY){
		int height=getHeight()-circleDiameter-1;
		int Yvalue=(int)(height*(posY/chaise.getMaxPosX()));
		return Yvalue;
	}




}
