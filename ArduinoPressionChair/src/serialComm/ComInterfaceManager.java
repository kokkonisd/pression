package serialComm;

import com.fazecast.jSerialComm.SerialPort;

public class ComInterfaceManager {

	public SerialPort[] getListPorts() {
		return ListPorts;
	}

	public void setListPorts(SerialPort[] listPorts) {
		ListPorts = listPorts;
	}

	public String[] getListPortsSystemName() {
		return ListPortsSystemName;
	}

	public void setListPortsSystemName(String[] listPortsSystemName) {
		ListPortsSystemName = listPortsSystemName;
	}

	public String[] getListPortsDescriptiveName() {
		return ListPortsDescriptiveName;
	}

	public void setListPortsDescriptiveName(String[] listPortsDescriptiveName) {
		ListPortsDescriptiveName = listPortsDescriptiveName;
	}

	public ComInterface[] getListComInterfaces() {
		return ListComInterfaces;
	}

	public void setListComInterfaces(ComInterface[] listComInterfaces) {
		ListComInterfaces = listComInterfaces;
	}

	/**
	 * Liste des ports Com du Systeme
	 */
	SerialPort[] ListPorts;

	/**
	 * Liste des noms des Ports (COM*)
	 */
	String[] ListPortsSystemName;

	/**
	 * Liste des noms des Ports (Virtual Port, Serial USB etc...
	 */
	String[] ListPortsDescriptiveName;

	/**
	 * Liste des ComInterface
	 */
	public ComInterface[] ListComInterfaces;

	/**
	 * Nombre d'interfaces Com gérés par le manager
	 */
	public int length;

	/**
	 *
	 */

	public ComInterfaceManager() {
		this.ListPorts=SerialPort.getCommPorts();
		//this.ListPortsSystemName=new String[ListPorts.length]; //peut être utile mais là non, on desactive
		//this.ListPortsDescriptiveName=new String[ListPorts.length];
		this.ListComInterfaces=new ComInterface[ListPorts.length];
		for(int i=0;i<ListPorts.length;i++){
			//ListPortsSystemName[i]=ListPorts[i].getSystemPortName();
			//ListPortsDescriptiveName[i]=ListPorts[i].getDescriptivePortName();
			ListComInterfaces[i]=new ComInterface(ListPorts[i]);
		}
		length=ListPorts.length;
	}

	public void reloadComInterfaceManager(){
		this.ListPorts=SerialPort.getCommPorts();
		//this.ListPortsSystemName=new String[ListPorts.length]; //peut être utile mais là non, on desactive
		//this.ListPortsDescriptiveName=new String[ListPorts.length];
		this.ListComInterfaces=new ComInterface[ListPorts.length];
		for(int i=0;i<ListPorts.length;i++){
			//ListPortsSystemName[i]=ListPorts[i].getSystemPortName();
			//ListPortsDescriptiveName[i]=ListPorts[i].getDescriptivePortName();
			ListComInterfaces[i]=new ComInterface(ListPorts[i]);
		}
		length=ListPorts.length;
	}

	/**
	 * Renvoi le port Com à partir de son nom, renvoi null si non trouvé
	 * @param SystemName
	 * @return
	 */
	public SerialPort getSerialPort(String SystemName){
		for(int i=0;i<ListPorts.length;i++){
			if((ListPorts[i].getSystemPortName()).equals(SystemName)){
				return ListPorts[i];
			}
		}
		return null;
	}

	/**
	 * Renvoi la ComInterface du Port de nom systeme SystemName, getComInterface("COM7") renvoi la ComInterface du COM7
	 * renvoi Null si l'interface n'est pas trouvé
	 * @param SystemName
	 * @return
	 */
	public ComInterface getComInterface(String SystemName){
		for(int i=0;i<ListComInterfaces.length;i++){
			if((ListComInterfaces[i].getSystemPortName().equals(SystemName))){
				return ListComInterfaces[i];
			}
		}
		return null;
	}



}
