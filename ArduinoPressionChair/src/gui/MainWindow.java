package gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

	//window size in pixels
	private final int width=400;
	private final int height=200;
	private final String AppTitle="Amazing Posture Detector";

	public MainWindow() throws HeadlessException {
		super();
		this.setSize(width,height);
		this.setTitle(AppTitle);
		this.setVisible(true);


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

}
