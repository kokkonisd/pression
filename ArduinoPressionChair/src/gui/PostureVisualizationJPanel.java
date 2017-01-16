package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JPanel;

import Calculation.Chaise;
import Calculation.Pied;

public class PostureVisualizationJPanel extends JPanel {

	private Chaise chaise;

	public static final int circleDiameter=10;
	public static final int margin=50;

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

	public void paint(Graphics g){

		Graphics2D g2d=(Graphics2D)g;

		g2d.setColor(Color.BLACK);

		ArrayList<Pied> Pieds=chaise.getPieds();
		for(int i=0;i<Pieds.size();i++){
			Pied pied=Pieds.get(i);
			System.out.println(pied.getPosX());

			g2d.drawOval(scaleX(pied.getPosX()), scaleY(pied.getPosY()), circleDiameter, circleDiameter);
		}

		g2d.fillOval(scaleX(chaise.getGposX()), scaleY(chaise.getGposY()), circleDiameter, circleDiameter);

	}

	public void paintG(double x,double y){
		int xscreen=scaleX(x);
		int yscreen=scaleY(y);
		Graphics2D g2d=(Graphics2D)getGraphics();
		g2d.setColor(Color.RED);
		g2d.fillOval(xscreen, yscreen, circleDiameter, circleDiameter);
	}

	public int scaleX(double posX){
		int width=getWidth()-circleDiameter;
		int Xvalue=(int)(width*(posX/chaise.getMaxPosX()));
		return Xvalue;
	}

	public int scaleY(double posY){
		int height=getHeight()-circleDiameter;
		int Yvalue=(int)(height*(posY/chaise.getMaxPosX()));
		return Yvalue;
	}




}
