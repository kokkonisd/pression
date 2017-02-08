package serialComm;

import java.util.Scanner;

import serialComm.ComInterface;

import UnitTesting.UnitTestModule;

public class TestComInterfaceManager {

	static Scanner in=new Scanner(System.in);

	public static void main(String[] args) {

		ComInterfaceManager CIM=new ComInterfaceManager();

		if(CIM.length==0){
			System.out.println("Aucun Port COM, Arret du programme");
			return;
		}

		for(int i=0;i<CIM.length;i++){

			ComInterface CM=CIM.getListComInterfaces()[i];

			System.out.println(i+"->" + CM.getSystemPortName() +"|"+ CM.getDescriptivePortName());
		}

		int selectedComPortIndex;

		do{
			System.out.printf("Entrez un nombre de 0 � %d pour choisir le port com dans la liste:",CIM.length-1);
			selectedComPortIndex=Integer.parseInt(in.nextLine());
		}while(selectedComPortIndex<0 || selectedComPortIndex>=CIM.length);

		ComInterface SelectedCM=CIM.getListComInterfaces()[selectedComPortIndex];

		System.out.println("Vous avez choisi le port:"+SelectedCM.getSystemPortName() +"|"+SelectedCM.getDescriptivePortName());

		System.out.println("Tentative d'ouverture du port et de cr�ation des flux.");
		SelectedCM.start();
		System.out.println("Ouverture r�ussie �coute des donn�es sur le port:");
		
		
		// testing the arduino data parser
		System.out.println("\n===\n");
		
		UnitTestModule parserTest = new UnitTestModule("Arduino Parser");
		String parseString = "172;1827;8493;99";
		parserTest.setTests(new int[][] {{ComInterface.parseArduinoData(parseString)[0], 172}, 
			{ComInterface.parseArduinoData(parseString)[1], 1827}, 
			{ComInterface.parseArduinoData(parseString)[2], 8493},
			{ComInterface.parseArduinoData(parseString)[3], 99}});
		parserTest.runTests();
		
	}

}
