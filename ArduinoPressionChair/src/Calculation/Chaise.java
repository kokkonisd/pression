package Calculation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class Chaise implements Serializable {


	ArrayList<Pied> Pieds;

	double gposX=Double.NaN;
	double gposY=Double.NaN;	//coordonn�es du point G barycentre des masses de la chaise en fonction de ses pieds. relatif � 0.

	double deadzoneX = Double.NaN;
	double deadzoneY = Double.NaN;
	double deadzoneR = Double.NaN;

	double maxPosX = Double.NaN;
	double maxPosY = Double.NaN;

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
	public static int[] parseArduinoData(String s) throws RuntimeException,NumberFormatException{

		// bug testing, optional
		if (s.equals("") || s.charAt(s.length() - 1) == ';') {
			System.out.println("ERROR: parseArduinoData: bad input (empty string or missing data)");
			throw new RuntimeException("bad input (empty string or missing data)");

		}

		String[] data_chunks = s.split(";");
		int[] parsed_data = new int[data_chunks.length];
		for (int i = 0; i < data_chunks.length; i++) {
			parsed_data[i] = Integer.parseInt(data_chunks[i]);
		}

		// verify that we read all 4 sensors
		if (parsed_data.length != 4) {

			System.out.println("ERROR: parseArduinoData: incomplete data");
			throw new RuntimeException("ERROR: parseArduinoData: incomplete data");


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
		calculateMaxPosX();
		calculateMaxPosY();
	}

	public void removePied(Pied p){
		Pieds.remove(p);
		calculateMaxPosX();
		calculateMaxPosY();
	}

	public void removePied(int index){
		Pieds.remove(index);
		calculateMaxPosX();
		calculateMaxPosY();
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
		calculateMaxPosX();
		calculateMaxPosY();
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


	//Update to improve performance (not recalculate maxposx or maxposy until a pied has changed

	private void calculateMaxPosX() {
		double max=0;
		for(int i=0;i<Pieds.size();i++){
			double posX=Pieds.get(i).getPosX();
			if(posX>max){
				max=posX;
			}
		}

		maxPosX = max;
	}

	private void calculateMaxPosY() {
		double max=0;
		for(int i=0;i<Pieds.size();i++){
			double posY=Pieds.get(i).getPosY();
			if(posY>max){
				max=posY;
			}
		}

		maxPosY = max;
	}

	public double getMaxPosX(){
		return maxPosX;
	}

	public double getMaxPosY(){
		return maxPosY;
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

	public double getDeadzoneX() {
		return deadzoneX;
	}

	public double getDeadzoneY() {
		return deadzoneY;
	}

	public void setDeadzoneX(double x) {
		deadzoneX = x;
	}

	public void setDeadzoneY(double y) {
		deadzoneY = y;
	}

	public void setDeadzoneRadius(double ratio) {
		deadzoneR = ratio / 2 * Math.min(getMaxPosX(), getMaxPosY());
	}

	public double getDeadzoneRadius() {
		return deadzoneR;
	}

	public boolean isGinDeadzone(){
		if(gposX==Double.NaN
				|| gposY==Double.NaN
				|| deadzoneX == Double.NaN
				|| deadzoneY == Double.NaN
				|| deadzoneR == Double.NaN){

			throw new RuntimeException("One of the values is NaN");
		}

		double deltaX=gposX-deadzoneX;
		double deltaY=gposY-deadzoneY;
		if((deltaX*deltaX + deltaY*deltaY)<=(deadzoneR*deadzoneR)){
			return true;
		}
		return false;

	}
	
	// === METHODS TO SAVE & LOAD CHAISE ===
	public void saveChaise(OutputStream out) throws IOException {
		StringBuilder chaiseBuilder = new StringBuilder();
		String separator = "|";
		
		chaiseBuilder.append("{").append(gposX).append(separator)
			.append(gposY).append(separator).append(deadzoneX)
			.append(separator).append(deadzoneY).append(separator)
			.append(deadzoneR).append("}").append("\n");
		
		for (int i = 0; i < Pieds.size(); i++) {
			Pied pied = Pieds.get(i);
			chaiseBuilder.append("{").append(pied.getPosX()).append(separator)
				.append(pied.getPosY()).append(separator).append(pied.getValue())
				.append(separator).append(pied.getSensorID()).append("}").append("\n");
		}
		
		char[] outChars = chaiseBuilder.toString().toCharArray();
		byte[] outBytes = new byte[outChars.length];
		for (int i = 0; i < outChars.length; i++) {
			outBytes[i] = (byte) outChars[i];
		}
		out.write(outBytes);
	}
}

