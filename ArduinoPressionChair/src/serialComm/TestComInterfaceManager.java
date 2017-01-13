package serialComm;

import java.util.Scanner;

import javax.swing.JFrame;

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
			System.out.printf("Entrez un nombre de 0 à %d pour choisir le port com dans la liste:",CIM.length-1);
			selectedComPortIndex=Integer.parseInt(in.nextLine());
		}while(selectedComPortIndex<0 || selectedComPortIndex>=CIM.length);

		ComInterface SelectedCM=CIM.getListComInterfaces()[selectedComPortIndex];

		System.out.println("Vous avez choisi le port:"+SelectedCM.getSystemPortName() +"|"+SelectedCM.getDescriptivePortName());

		System.out.println("Tentative d'ouverture du port et de création des flux.");
		SelectedCM.start();
		System.out.println("Ouverture réussie écoute des données sur le port:");

	}

}
