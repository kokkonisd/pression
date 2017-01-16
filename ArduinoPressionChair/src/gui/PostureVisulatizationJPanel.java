package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JPanel;

import Calculation.Chaise;
import Calculation.Pied;

public class PostureVisulatizationJPanel extends JPanel {

	private Chaise chaise;

	public static final int circleDiameter=5;

	public PostureVisulatizationJPanel() {
		this.chaise=new Chaise();
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
		int width=getWidth();
		int height=getHeight();

		g.setColor(Color.BLACK);

		ArrayList<Pied> Pieds=chaise.getPieds();
		for(int i=0;i<Pieds.size();i++){
			Pied pied=Pieds.get(i);
			g.drawOval(scaleX(pied.getPosX()), scaleY(pied.getPosY()), circleDiameter, circleDiameter);
		}

	}

	public int scaleX(double posX){
		int width=getWidth();
		int Xvalue=(int)(width*(posX/(chaise.getMaxPosX())));
		return Xvalue;
	}

	public int scaleY(double posY){
		int width=getHeight();
		int Yvalue=(int)(width*(posY/(chaise.getMaxPosX())));
		return Yvalue;
	}




}
