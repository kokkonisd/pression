package Calculation;
import java.util.ArrayList;


public class Chaise {


	ArrayList<Pied> Pieds;

	double gposX,gposY;	//coordonnées du point G barycentre des masses de la chaise en fonction de ses pieds. relatif à 0.

	public Chaise(){
		Pieds=new ArrayList<>();
		gposX=0;
		gposY=0;
	}

	/**
	 * Crée une chaise avec autant n=piedsNumber pieds.
	 * Les id de pieds vont de 0 à piedsNumber-1 dans l'arrayList Pieds.
	 * @param piedsNumber
	 */
	public Chaise(int piedsNumber){
		Pieds=new ArrayList<>();
		for(int i=0;i<piedsNumber;i++){
			addPied(new Pied(i));
		}
		gposX=0;
		gposY=0;
	}

	void addPied(Pied p){
		Pieds.add(p);
	}

	Pied getPied(int piedIndex){
		return Pieds.get(piedIndex);
	}

	void calculateGpos(){

		//Init of the total value to ponderate the final result
		double totalValue=0;
		double tmpPosX=0;
		double tmpPosY=0;


		for(int i=0;i<Pieds.size();i++){
			Pied currentPied=Pieds.get(i);

			double posX=currentPied.getPosX();
			double posY=currentPied.getPosY();
			double value=currentPied.getValue();

			totalValue+=value;

			tmpPosX+=(posX*value);	//x*masse
			tmpPosY+=(posY*value);	//y*masse
		}

		gposX=tmpPosX/totalValue;
		gposY=tmpPosY/totalValue;

	}

	public ArrayList<Pied> getPieds() {
		return Pieds;
	}

	public void setPieds(ArrayList<Pied> pieds) {
		Pieds = pieds;
	}

	public double getGposX() {
		return gposX;
	}

	public void setGposX(double gposX) {
		this.gposX = gposX;
	}

	public double getGposY() {
		return gposY;
	}

	public void setGposY(double gposY) {
		this.gposY = gposY;
	}

	/**
	 * Prends tout les Pieds de la liste Pieds de la chaise et attribut les valeurs
	 * des capteurs aux pieds
	 * @param piedsValues
	 */
	public void setPiedsValuesFromIntArray(int[] piedsValues){
		for(int piedIndex=0;piedIndex<Pieds.size();piedIndex++){
			int value=piedsValues[piedIndex];
			Pieds.get(piedIndex).setValue(value);
		}
	}

	public double getMaxPosX(){
		double max=0;
		for(int i=0;i<Pieds.size();i++){
			double posX=Pieds.get(i).getPosX();
			if(posX>max){
				max=posX;
			}
		}
		return max;
	}
	public double getMaxPosY(){
		double max=0;
		for(int i=0;i<Pieds.size();i++){
			double posY=Pieds.get(i).getPosY();
			if(posY>max){
				max=posY;
			}
		}
		return max;
	}








}
