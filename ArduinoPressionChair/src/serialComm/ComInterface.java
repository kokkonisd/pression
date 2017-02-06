package serialComm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.text.BadLocationException;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import Calculation.Chaise;
import gui.PostureVisualizationJPanel;


/**
 * This class is designed to manage receiving and sending data from a specified SerialPort Object from JSerialComm library
 * @author J�r�my
 *
 */
public class ComInterface {

	//---------------------------Variables-----------------------------//
	/**
	 * Le port � utiliser
	 */
	private SerialPort serial;
	/**
	 * Flux de r�ception de donn�es
	 */
	private InputStream serialInputStream;
	/**
	 * Flux d'�mission de donn�es
	 */
	private OutputStream serialOutputStream;
	/**
	 * Vitesse de communication
	 */
	private int BaudRate=9600; //Par d�faut 9600
	/**
	 * Nombre de bits de donn�es
	 */
	private int DataBits=8; //Par d�faut 8
	/**
	 * Nombre de bits de Stop
	 */
	private int StopBits=1; //Par d�faut 1
	/**
	 * Parit� du message
	 */
	private int Parity=SerialPort.NO_PARITY; //Par d�faut pair
	/**
	 * Nom syst�me port COM
	 */
	private String SystemName;
	/**
	 * Nom driver port COM
	 */
	private String DescriptivePortName;


	/**
	 * Booleen de verification pour savoir si le port est ouvert
	 */
	private boolean isOpen=false;

	Chaise chaise;

	PostureVisualizationJPanel panel;



	//------------------------------Setter/Getters----------------------------

	public SerialPort getSerial() {
		return serial;
	}

	public void setSerial(SerialPort serial) {
		this.serial = serial;
		SystemName=serial.getSystemPortName();
		DescriptivePortName=serial.getDescriptivePortName();
		configurePort();
	}

	public InputStream getSerialInputStream() {
		return serialInputStream;
	}

	public void setSerialInputStream(InputStream serialInputStream) {
		this.serialInputStream = serialInputStream;
	}

	public OutputStream getSerialOutputStream() {
		return serialOutputStream;
	}

	public void setSerialOutputStream(OutputStream serialOutputStream) {
		this.serialOutputStream = serialOutputStream;
	}

	public int getBaudRate() {
		return BaudRate;
	}

	public void setBaudRate(int baudRate) {
		BaudRate = baudRate;
		configurePort();
	}

	public int getDataBits() {
		return DataBits;
	}

	public void setDataBits(int dataBits) {
		DataBits = dataBits;
	}

	public int getStopBit() {
		return StopBits;
	}

	public void setStopBit(int stopBit) {
		StopBits = stopBit;
	}

	public int getParity() {
		return Parity;
	}

	public void setParity(int parity) {
		Parity = parity;
	}

	public String getSystemPortName() {
		return SystemName;
	}

	public void setSystemName(String systemName) {
		SystemName = systemName;
	}

	public String getDescriptivePortName() {
		return DescriptivePortName;
	}

	public void setDescriptivePortName(String descriptivePortName) {
		DescriptivePortName = descriptivePortName;
	}


	public boolean isOpen() {
		return isOpen;
	}

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


	//---------------------------M�thodes------------------------

	/**
	 * new ComInterface(SerialPort serial) cr�e une interface de communication sur la port serial.
	 * @param <Chaise>
	 * @param serial
	 */
	public ComInterface(SerialPort serial,PostureVisualizationJPanel panel){
		this.serial=serial;
		this.chaise=panel.getChaise();
		this.panel=panel;
		SystemName=serial.getSystemPortName();
		DescriptivePortName=serial.getDescriptivePortName();
		configurePort(); //Applique les param�tres par d�faut au port.
	}

	/**
	 * Applique les param�tres au Port COM
	 */
	public void configurePort(){
		this.serial.setComPortParameters(BaudRate, DataBits, StopBits, Parity);
		this.serial.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 100,0);

	}

	/**
	 * Ouvrir le port COM et cr�er les Flux
	 */
	public boolean open(){
		if(serial.openPort()){
			isOpen=true;
			return true;
		}
		isOpen=false;
		return false;
	}

	public void start(){
		if(this.open()){
			serial.addDataListener(new mSerialPortDataListener());
			isOpen=true;
		}else{
			isOpen=false;
		}
	}


	public boolean stop(){

		serial.removeDataListener();
		if(serial.closePort()){
			isOpen=false;
			return true;
		}
		serial.addDataListener(new mSerialPortDataListener());
		return false;
	}



	public void write(String message) throws IOException, BadLocationException{
		if(isOpen){
			String dataMessage=message;
			char[] data=dataMessage.toCharArray(); //Convertion de la chaine de caract�re en Tableau de characteres
			for(int i=0;i<data.length;i++){
				write((int)data[i]); //Envoi du message
			}
		}
	}

	public void write(int data) throws IOException{
		if(isOpen){
			serial.getOutputStream().write(data);
		}
	}

	public void write(byte data) throws IOException{
		if(isOpen){
			serial.getOutputStream().write(data);
		}
	}

	public void write(byte[] data,int offset,int len) throws IOException{
		if(isOpen){
			serial.getOutputStream().write(data, offset, len);
		}
	}
	public void write(char data) throws IOException{
		write((int)data);
	}



	private class mSerialPortDataListener implements SerialPortDataListener{

		boolean recordData=false;
		StringBuilder Data=new StringBuilder();

		@Override
		public int getListeningEvents(){
			return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
		}


		@Override
		public void serialEvent(SerialPortEvent event){


			//Si on n'est pas en train d'attendre des donn�es, on passe
			if(event.getEventType()!=SerialPort.LISTENING_EVENT_DATA_AVAILABLE){
				return;
			}

			byte[] dataBuffer=new byte[serial.bytesAvailable()]; //On cr�e un buffer des octets disponibles sur le Port
			int numRead=serial.readBytes(dataBuffer, dataBuffer.length); //On lit les donn�es du port s�rie en les pla�ant dans le Buffer

			int inputData;

			try{
				for(int i=0;i<dataBuffer.length;i++){
					inputData=(int)dataBuffer[i];

					//System.out.println(inputData);
					//Si on a un STX
					if(inputData==02){
						recordData=true;
					}
					//Si on re�ois un CR caractere de fin
					else if(inputData==13){

						recordData=false;
						String rawData=Data.toString();
						System.out.println(rawData);
						updatePanelWithData(rawData);
						Data.delete(0, Data.length());
					}
					//Si on enregistre les donn�es
					else if(recordData){
						Data.append((char)inputData);
					}


				}
			}catch(Exception e){
				e.printStackTrace();
			}

		};


	}

	public void updatePanelWithData(String rawData){

		try{
			int[] sensorValues=Chaise.parseArduinoData(rawData);
			panel.getChaise().setPiedsValuesFromIntArray(sensorValues);
			panel.getChaise().calculateGpos();
			//panel.removeAll();
			panel.repaint();
		}catch(Exception e){
			//If parsing failed
		}




	}


}
