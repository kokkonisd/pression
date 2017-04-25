package gui;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import Calculation.Chaise;
import Calculation.Pied;

public class TestMainWindow {

	public static void main(String[] args) throws InterruptedException, HeadlessException, ClassNotFoundException, IOException {

		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				MainWindow win;
				try {
					win = new MainWindow();
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

				Chaise chaise=new Chaise();

				Pied p1=new Pied(0.0, 0.0, 1);
				Pied p2=new Pied(1.0, 0.0, 2);
				Pied p3=new Pied(0.0, 1.0, 3);
				Pied p4=new Pied(1.0, 1.0, 4);

				p1.setValue(5);
				p2.setValue(5);
				p3.setValue(5);
				p4.setValue(5);

				chaise.addPied(p1);
				chaise.addPied(p2);
				chaise.addPied(p3);
				chaise.addPied(p4);

				chaise.calculateGpos();
				System.out.println(chaise.getGposX());
				System.out.println(chaise.getGposY());

				System.out.println(chaise.getMaxPosX());
				System.out.println(chaise.getMaxPosY());

				// initialize deadzone
				chaise.setDeadzoneRatio(0.5);

				chaise.setDeadzoneX(chaise.getGposX());
				chaise.setDeadzoneY(chaise.getGposY());

				/*
		System.out.println(chaise.isGinDeadzone());


		chaise.setGposX(1);
		chaise.setGposY(1);
		System.out.println(chaise.isGinDeadzone());
				 */

				win.setChaise(chaise);
			}
		});


	}
}
