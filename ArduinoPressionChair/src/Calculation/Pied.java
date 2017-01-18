package Calculation;

public class Pied {


	private double posX,posY; //Vecteur position du pied
	private double value=0;	//Valeur associée sur le pied
	private int sensorID; //ID du sensor associé a ce pied


	public Pied(double posx,double posy,int sensorID){
		this.posX=posx;
		this.posY=posy;
		this.sensorID=sensorID;
	}

	public Pied(int sensorID){
		this.sensorID=sensorID;
		this.posX=0;
		this.posY=0;
	}


	public double getPosX() {
		return posX;
	}


	public void setPosX(double posX) {
		this.posX = posX;
	}


	public double getPosY() {
		return posY;
	}


	public void setPosY(double posY) {
		this.posY = posY;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}


	public int getSensorID() {
		return sensorID;
	}


	public void setSensorID(int sensorID) {
		this.sensorID = sensorID;
	}



}