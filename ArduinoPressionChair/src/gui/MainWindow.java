package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import com.fazecast.jSerialComm.SerialPort;

import Calculation.Chaise;
import serialComm.ComInterface;

public class MainWindow extends JFrame {

	// window size in pixels
	private final int WIDTH=500;
	private final int HEIGHT=400;
	
	// window title
	private final String AppTitle="Amazing Posture Detector";
	
	// Chaise object
	private Chaise chaise;
	
	// custom made panel containing the visualisation
	PostureVisualizationJPanel panel;
	
	// vars to hold the port we use and the list of available ports
	SerialPort serialPort;
	SerialPort[] serialPorts=SerialPort.getCommPorts();
	
	// custom made ComInterface
	ComInterface comInterface;

	public MainWindow() throws HeadlessException {
		super();

		// initialize the Chaise and visualization panel objects
		chaise=new Chaise();
		panel=new PostureVisualizationJPanel(chaise);

		// make a new panel that has 1 row and 2 columns
		// the first column contains the visualization
		// the second column contains the configuration elements
		JPanel mainPanel=new JPanel(new GridLayout(1, 2));

		// make a new config panel
		ConfigPanel configPanel=new ConfigPanel(this);

		// add visualisation & config panels to main panel
		mainPanel.add(panel);
		mainPanel.add(configPanel);

		this.setContentPane(mainPanel);
		//add(panel);
		
		// minimum screen size
		this.setMinimumSize(new Dimension(300, 300));
		// set the normal size
		this.setSize(WIDTH,HEIGHT);
		// set the window title
		this.setTitle(AppTitle);
		// make the window visible
		this.setVisible(true);
		// set close operation to close the window when we quit
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// --- setters-getters ---
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


	// Comms configuration panel class
	public class ConfigPanel extends JPanel {
		
		// class constructor, takes a MainWindow object as a parameter
		public ConfigPanel(final MainWindow mainwindow) {
			super();
			
			// get the names of the available ports on the machine
			String[] comListNames=new String[serialPorts.length];
			for(int i=0;i<serialPorts.length;i++){
				comListNames[i]=serialPorts[i].getSystemPortName()+ "|"+ serialPorts[i].getDescriptivePortName();
			}

			// make a combobox containing the port names, then add it to the container (main window)
			final JComboBox<String> comCombobox=new JComboBox<>(comListNames);
			add(comCombobox);

			// connection button
			final JButton btnConnexion = new JButton("Connexion");
			// de-connection button
			final JButton btnDeconnexion=new JButton("Deconnexion");
			
			// test button to show that we can send data to the Arduino
			final JButton btnSendTextToArduino=new JButton("Send Arduino Command");

			// if no ports are available, the connection button should be turned off
			if(serialPorts.length==0){
				btnConnexion.setEnabled(false);
			}

			// if no connection is made, the de-connection button should be off (by default)
			btnDeconnexion.setEnabled(false);

			
			btnConnexion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// turn off the combobox when handling an action
					comCombobox.setEnabled(false);
					
					if(comInterface!=null){
						// stop the interface and start a new connection based on the selected port
						comInterface.stop();
						comInterface.setSerial(serialPorts[comCombobox.getSelectedIndex()]);
					}else{
						// if there's nothing to stop, just start the connection
						comInterface=new ComInterface(serialPorts[comCombobox.getSelectedIndex()], panel);
					}

					
					if(!comInterface.isOpen()){
						// start the connection
						comInterface.start();
						if(comInterface.isOpen()){
							// set the button text to "connected"
							btnConnexion.setText("Connect�");
							// de-connection should be available
							btnDeconnexion.setEnabled(true);
							// connection should not be available
							btnConnexion.setEnabled(false);
						}else{
							// the port cannot open
							btnConnexion.setText("Echec ouverture");
							comCombobox.setEnabled(true);
						}
					}


				}
			});
			// add the connection button to the container (main window)
			add(btnConnexion);


			btnDeconnexion.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(comInterface.stop()){
						//openButton.setText(comInterface.getSystemPortName()+" Ferm�");
						//openButton.setIcon(statusOffline);
						//openButton.setToolTipText("R�ouvrir le port COM et r�activer l'�coute des donn�es");
						
						// if the connection is stopped, turn stuff on and off appropriately
						comCombobox.setEnabled(true);
						btnConnexion.setEnabled(true);
						btnConnexion.setText("Connexion");
						btnDeconnexion.setEnabled(false);
					}else{
						// there's a problem when closing the connection
						btnDeconnexion.setText("ECHEC FERMETURE");
					}
				}
			});
			// add the connection button to the container (main window)
			add(btnDeconnexion);

			btnSendTextToArduino.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(comInterface==null){
						return;
					}
					else if(!comInterface.isOpen()){
						return;
					}
					else{
						try {
							comInterface.write('A');
						} catch (IOException e) {
							// there's a problem when sending the command
							e.printStackTrace();
						}
					}

				}
			});
			// add the Arduino command button to the container (main window)
			add(btnSendTextToArduino);
		}
	}
}
