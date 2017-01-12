import java.util.ArrayList;

public class Chaise {

	ArrayList<Pied> Pieds;

	double gposX,gposY;	//coordonnées du point G barycentre des masses de la chaise en fonction de ses pieds. relatif à 0.

	public Chaise(){
		Pieds=new ArrayList<>();
	}

	void addPied(Pied p){
		Pieds.add(p);
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








}
