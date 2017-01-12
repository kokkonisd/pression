package serialComm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.text.BadLocationException;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * This class is designed to manage receiving and sending data from a specified SerialPort Object from JSerialComm library
 * @author Jérémy
 *
 */
public class ComInterface {

	//---------------------------Variables-----------------------------//
		/**
		 * Le port à utiliser
		 */
		private SerialPort serial;
		/**
		 * Flux de réception de données
		 */
		private InputStream serialInputStream;
		/**
		 * Flux d'émission de données
		 */
		private OutputStream serialOutputStream;
		/**
		 * Vitesse de communication
		 */
		private int BaudRate=9600; //Par défaut 9600
		/**
		 * Nombre de bits de données
		 */
		private int DataBits=8; //Par défaut 8
		/**
		 * Nombre de bits de Stop
		 */
		private int StopBits=1; //Par défaut 1
		/**
		 * Parité du message
		 */
		private int Parity=SerialPort.NO_PARITY; //Par défaut pair
		/**
		 * Nom système port COM
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



		//------------------------------Setter/Getters----------------------------

		SerialPort getSerial() {
			return serial;
		}

		void setSerial(SerialPort serial) {
			this.serial = serial;
		}

		InputStream getSerialInputStream() {
			return serialInputStream;
		}

		void setSerialInputStream(InputStream serialInputStream) {
			this.serialInputStream = serialInputStream;
		}

		OutputStream getSerialOutputStream() {
			return serialOutputStream;
		}

		void setSerialOutputStream(OutputStream serialOutputStream) {
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


		//---------------------------Méthodes------------------------

		/**
		 * new ComInterface(SerialPort serial) crée une interface de communication sur la port serial.
		 * @param serial
		 */
		public ComInterface(SerialPort serial ){
			this.serial=serial;
			SystemName=serial.getSystemPortName();
			DescriptivePortName=serial.getDescriptivePortName();
			configurePort(); //Applique les paramètres par défaut au port.
		}

		/**
		 * Applique les paramètres au Port COM
		 */
		public void configurePort(){
			this.serial.setComPortParameters(BaudRate, DataBits, StopBits, Parity);
			this.serial.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 100,0);

		}

		/**
		 * Ouvrir le port COM et créer les Flux
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
				OutputStream serialOutputStream=serial.getOutputStream();
				String dataMessage=message;
				char[] data=dataMessage.toCharArray(); //Convertion de la chaine de caractère en Tableau de characteres
				for(int i=0;i<data.length;i++){
					serialOutputStream.write((int)data[i]); //Envoi du message
				}
			}
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

				//Si on n'est pas en train d'attendre des données, on passe
				if(event.getEventType()!=SerialPort.LISTENING_EVENT_DATA_AVAILABLE){
					return;
				}

				byte[] dataBuffer=new byte[serial.bytesAvailable()]; //On crée un buffer des octets disponibles sur le Port
				int numRead=serial.readBytes(dataBuffer, dataBuffer.length); //On lit les données du port série en les plaçant dans le Buffer

				int inputData;



				try{
					for(int i=0;i<dataBuffer.length;i++){
						inputData=(int)dataBuffer[i];
						System.out.print(inputData +"!");
					}
					System.out.println();
				}catch(Exception e){

				}
				//System.out.println("FIN FOR");
			};


		}


}
