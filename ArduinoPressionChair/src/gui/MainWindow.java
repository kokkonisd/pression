package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

import Calculation.Chaise;
import serialComm.ComInterface;

public class MainWindow extends JFrame {

	//window size in pixels
	private final int width=500;
	private final int height=400;
	private final String AppTitle="Amazing Posture Detector";
	private Chaise chaise;
	PostureVisualizationJPanel panel;
	SerialPort serialPort;
	ComInterface comInterface;

	public MainWindow() throws HeadlessException {
		super();

		chaise=new Chaise();
		panel=new PostureVisualizationJPanel(chaise);

		JPanel mainPanel=new JPanel(new GridLayout(1, 2));

		ComConfigPanel configPanel=new ComConfigPanel(this);

		mainPanel.add(panel);
		mainPanel.add(configPanel);

		this.setContentPane(mainPanel);
		//add(panel);
		this.setMinimumSize(new Dimension(300, 300));
		this.setSize(width,height);
		this.setTitle(AppTitle);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}



	public MainWindow(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MainWindow(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MainWindow(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}



	public Chaise getChaise() {
		return chaise;
	}



	public void setChaise(Chaise chaise) {
		this.chaise = chaise;
		panel.setChaise(chaise);
	}

	public SerialPort getSerialPort(){
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort){
		this.serialPort=serialPort;
	}



	public ComInterface getComInterface() {
		return comInterface;
	}



	public void setComInterface(ComInterface comInterface) {
		this.comInterface = comInterface;
	}





}
