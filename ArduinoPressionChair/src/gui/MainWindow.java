package gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Calculation.Chaise;

public class MainWindow extends JFrame {

	//window size in pixels
	private final int width=300;
	private final int height=300;
	private final String AppTitle="Amazing Posture Detector";
	private Chaise chaise;
	PostureVisualizationJPanel panel;

	public MainWindow() throws HeadlessException {
		super();

		chaise=new Chaise();
		panel=new PostureVisualizationJPanel(chaise);

		this.setContentPane(panel);
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



}
