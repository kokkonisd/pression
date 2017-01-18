package gui;

import java.sql.Time;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import com.fazecast.jSerialComm.SerialPort;

import Calculation.Chaise;
import Calculation.Pied;
import serialComm.ComInterface;

public class TestMainWindow {

	public static void main(String[] args) throws InterruptedException {
		MainWindow win=new MainWindow();

		Chaise chaise=new Chaise();

		Pied p1=new Pied(0.0, 0.0, 1);
		Pied p2=new Pied(1.0, 0.0, 2);
		Pied p3=new Pied(0.0, 1.0, 3);
		Pied p4=new Pied(1.0, 1.0, 4);

		p1.setValue(5);
		p2.setValue(5);
		p3.setValue(2);
		p4.setValue(2);

		chaise.addPied(p1);
		chaise.addPied(p2);
		chaise.addPied(p3);
		chaise.addPied(p4);


		chaise.calculateGpos();
		System.out.println(chaise.getGposX());
		System.out.println(chaise.getGposY());

		System.out.println(chaise.getMaxPosX());
		System.out.println(chaise.getMaxPosY());

		win.setChaise(chaise);


		ComInterface cm=new ComInterface(SerialPort.getCommPort("COM1"), win.panel);
		cm.start();


		/*
		Random r=new Random();
		for(int i=0;i<200;i++){

			String s=r.nextInt(1025)+";"+r.nextInt(1025)+";"+r.nextInt(1025)+";"+r.nextInt(1025);
			cm.updatePanelWithData(s);

			Thread.sleep(150);

		}
		*/

	}

}
