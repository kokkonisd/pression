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

import javax.swing.JButton;
import javax.swing.JComboBox;
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
	SerialPort[] serialPorts=SerialPort.getCommPorts();
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


	public class ComConfigPanel extends JPanel {


		public ComConfigPanel(final MainWindow mainwindow) {
			super();

			String[] comListNames=new String[serialPorts.length];
			for(int i=0;i<serialPorts.length;i++){
				comListNames[i]=serialPorts[i].getSystemPortName()+ "|"+ serialPorts[i].getDescriptivePortName();
			}

			final JComboBox<String> comCombobox=new JComboBox<>(comListNames);
			add(comCombobox);


			final JButton btnConnexion = new JButton("Connexion");
			final JButton btnDeconnexion=new JButton("Deconnexion");

			if(serialPorts.length==0){
				btnConnexion.setEnabled(false);
			}

			btnDeconnexion.setEnabled(false);


			btnConnexion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					comCombobox.setEnabled(false);

					if(comInterface!=null){
						comInterface.stop();
						comInterface.setSerial(serialPorts[comCombobox.getSelectedIndex()]);
					}
					else{
						comInterface=new ComInterface(serialPorts[comCombobox.getSelectedIndex()], panel);
					}

					if(!comInterface.isOpen()){
						comInterface.start();
						if(comInterface.isOpen()){
							btnConnexion.setText("Connecté");
							btnDeconnexion.setEnabled(true);
							btnConnexion.setEnabled(false);
						}else{
							btnConnexion.setText("Echec ouverture");
							comCombobox.setEnabled(true);
						}
					}


				}
			});
			add(btnConnexion);


			btnDeconnexion.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(comInterface.stop()){
						//openButton.setText(comInterface.getSystemPortName()+" Fermé");
						//openButton.setIcon(statusOffline);
						//openButton.setToolTipText("Réouvrir le port COM et réactiver l'écoute des données");
						comCombobox.setEnabled(true);
						btnConnexion.setEnabled(true);
						btnConnexion.setText("Connexion");
						btnDeconnexion.setEnabled(false);
					}
					else{
						btnDeconnexion.setText("ECHEC FERMETURE");
					}



				}
			});
			add(btnDeconnexion);

		}


	}



}
