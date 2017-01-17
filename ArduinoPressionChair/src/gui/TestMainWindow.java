package gui;

import com.fazecast.jSerialComm.SerialPort;

import Calculation.Chaise;
import Calculation.Pied;
import serialComm.ComInterface;

public class TestMainWindow {

	public static void main(String[] args) {
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

		ComInterface cm=new ComInterface(SerialPort.getCommPort("COM10"), win.panel);
		cm.start();


		//win.panel.paintG(chaise.getGposX(), chaise.getGposY());

		//win.panel.invalidate();
		//win.panel.repaint();

	}

}
