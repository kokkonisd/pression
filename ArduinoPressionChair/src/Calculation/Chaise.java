package Calculation;
import java.util.ArrayList;


public class Chaise {


	ArrayList<Pied> Pieds;

	double gposX=Double.NaN;
	double gposY=Double.NaN;	//coordonn�es du point G barycentre des masses de la chaise en fonction de ses pieds. relatif � 0.


	// --------- STATICS ---------
	/**
	 * Method to parse string data received from the Arduino by
	 * serial communication. We capture the data in order, so
	 * the values are stored in an one-dimensional integer array.
	 *
	 * The sensor IDs corresponding to these values are simply the indexes.
	 * (eg: parsed_data[0] will contain the data coming from the sensor with ID = 0)
	 *
	 * The format of the string will be:
	 * dataFromSensorID0;dataFromSensorID1;dataFromSensorID2 etc.
	 *
	 * @param s : string of data communicated by the Arduino
	 * @return : an one-dimensional integer array containing the indexed data.
	 */
	public static int[] parseArduinoData(String s) {

		// bug testing, optional
		if (s.equals("") || s.charAt(s.length() - 1) == ';') {
			System.out.println("ERROR: parseArduinoData: bad input (empty string or missing data)");
			// return empty integer array
			return new int[] {};
		}

		String[] data_chunks = s.split(";");
		int[] parsed_data = new int[data_chunks.length];
		for (int i = 0; i < data_chunks.length; i++) {
			parsed_data[i] = Integer.parseInt(data_chunks[i]);
		}

		// verify that we read all 4 sensors
		if (parsed_data.length != 4) {
			System.out.println("ERROR: parseArduinoData: incomplete data");
			// return empty integer array
			return new int[] {};
		}

		return parsed_data;
	}

	public Chaise(){
		Pieds=new ArrayList<>();
	}

	/**
	 * Cr�e une chaise avec autant n=piedsNumber pieds.
	 * Les id de pieds vont de 0 � piedsNumber-1 dans l'arrayList Pieds.
	 * @param piedsNumber
	 */
	public Chaise(int piedsNumber){
		this();
		for(int i=0;i<piedsNumber;i++){
			addPied(new Pied(i));
		}
	}

	public void addPied(Pied p){
		Pieds.add(p);
	}

	public void removePied(Pied p){
		Pieds.remove(p);
	}

	public void removePied(int index){
		Pieds.remove(index);
	}

	Pied getPied(int piedIndex){
		return Pieds.get(piedIndex);
	}

	public void calculateGpos(){

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
		//return maxPosX;
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
		//return maxPosY;

	}

	/*
	public void calculateMaxPos(){
		double maxX=0;
		double maxY=0;
		for(int i=0;i<Pieds.size();i++){
			double posX=Pieds.get(i).getPosX();
			double posY=Pieds.get(i).getPosX();
			if(posX>maxX){
				maxX=posX;
			}
			if(posY>maxY){
				maxY=posY;
			}
		}
		maxPosX=maxX;
		maxPosY=maxY;
	}
	 */








}
