package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fazecast.jSerialComm.SerialPort;

import Calculation.Chaise;
import Calculation.Pied;
import serialComm.ComInterface;

public class MainWindow extends JFrame {

	// window size in pixels
	private final int WIDTH=500;
	private final int HEIGHT=400;

	// window title
	private final String AppTitle="Amazing Posture Detector";

	// Chaise object
	private Chaise chaise;

	// custom made panel containing the visualization
	PostureVisualizationJPanel panel;

	// vars to hold the port we use and the list of available ports
	SerialPort serialPort;
	SerialPort[] serialPorts=SerialPort.getCommPorts();

	// custom made ComInterface
	ComInterface comInterface;

	public MainWindow() throws HeadlessException, ClassNotFoundException, IOException {
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
		public ConfigPanel(final MainWindow mainwindow) throws IOException, ClassNotFoundException {
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
			
			
			// testing chaise configuration
			
			final JButton btnConfigChaise = new JButton("Config Chaise");
			
			btnConfigChaise.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Chaise c = ChaiseConfigDialog();
					if (c != null) {
						setChaise(c);
						panel.repaint();
					}
					
				}
			});
			
			add(btnConfigChaise);
		
			// controls for calibrating the deadzone
			final JButton btnCalibrateDeadzone = new JButton("Calibrate Deadzone");
			
			btnCalibrateDeadzone.addActionListener(new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					chaise.setDeadzoneX(chaise.getGposX());
					chaise.setDeadzoneY(chaise.getGposY());
					panel.repaint();
				}
			});
			
			add(btnCalibrateDeadzone);
			
			final JLabel deadzoneStatusLabel = new JLabel("");
			
			add(deadzoneStatusLabel);
			
			// slider to control the radius of the deadzone
			final JSlider deadzoneRadiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
			
			deadzoneRadiusSlider.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent arg0) {
					chaise.setDeadzoneRadius(deadzoneRadiusSlider.getValue() / 100.0);
					panel.repaint();
				}
			});
			add(deadzoneRadiusSlider);
			
			// set labels on the slider
			deadzoneRadiusSlider.setMajorTickSpacing(20);
			deadzoneRadiusSlider.setMinorTickSpacing(5);
			deadzoneRadiusSlider.setPaintTicks(true);
			deadzoneRadiusSlider.setPaintLabels(true);
			deadzoneRadiusSlider.setBorder(
	                BorderFactory.createEmptyBorder(0,0,10,0));
	        Font font = new Font("Serif", Font.ITALIC, 15);
	        deadzoneRadiusSlider.setFont(font);
	        
	        // Saving the Chaise Object
	        
	        // File chooser to save tha chaise object, starts in current directory
	        final JFileChooser saveChaise = new JFileChooser(new File(System.getProperty("user.dir")));
	        // Button to launch file chooser
	        final JButton saveChaiseBtn = new JButton("Save Chaise");
	        
	        // Action handler for the button
	        saveChaiseBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int returnVal = saveChaise.showSaveDialog(MainWindow.this);
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			        	System.out.println("Saving chaise...");
			        	// get the filename chosen by the user
			        	String file = saveChaise.getSelectedFile().getName();
			        	String path = saveChaise.getSelectedFile().getAbsolutePath();
			        	// take off the file ending that the user may have put
			        	path = path.split(Pattern.quote("."))[0];
			        	
			        	// make sure we're not overriding a file
			        	File f = new File(path + ".ser");
			        	int result = 0;
			        	if(f.exists() && !f.isDirectory()) { 
			        		result = JOptionPane.showConfirmDialog(MainWindow.this, "File already exists. Overwrite file?");
			        	}
			        	if (result == 0) {
			        		try {
			        			// apply .ser extension
			        			FileOutputStream fout = new FileOutputStream(f);
			        			ObjectOutputStream oos = new ObjectOutputStream(fout);
			        			oos.writeObject(chaise);
			        			oos.close();
			        		} catch (Exception e) {
			        			e.printStackTrace();
			        		}
			        	}
			        }
				}
			});
	        
	        add(saveChaiseBtn);
	        
	        // file chooser used to load the chaise object
	        // file chooser will open in current directory
	        final JFileChooser loadChaise = new JFileChooser(new File(System.getProperty("user.dir")));
	        loadChaise.setFileFilter(new FileNameExtensionFilter("Serialized Objects", "ser"));
	        
	        // button that triggers the file chooser
	        final JButton loadChaiseBtn = new JButton("Load Chaise");
	        loadChaiseBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int returnVal = loadChaise.showOpenDialog(MainWindow.this);
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			        	System.out.println("Loading chaise...");
			        	String filename = loadChaise.getSelectedFile().getAbsolutePath();
			        	FileInputStream fin;
						try {
							fin = new FileInputStream(filename);
							ObjectInputStream ois = new ObjectInputStream(fin);
				    		setChaise((Chaise) ois.readObject());
				    		ois.close();
				    		panel.repaint();
				    		// reset the radius slider to the value of the chaise we just loaded
				    		deadzoneRadiusSlider.setValue((int) (chaise.getDeadzoneRadius() * 200));
						} catch (Exception e) {
							e.printStackTrace();
						}
			        }
				}
			});
	        
	        add(loadChaiseBtn);
		}
		
		/**
		 * Method to handle the configuration of the Chaise object
		 * @return Chaise object to be used
		 */
		public Chaise ChaiseConfigDialog() {
			// textfield for the number of pieds
			JTextField piedsNum = new JTextField();
			piedsNum.setColumns(5);
			
			JLabel piedsNumLabel = new JLabel("Nombre des pieds de la chaise");
			
			// arraylist of textfields for the positions of the pieds
			ArrayList<JTextField> piedsPos = new ArrayList<>();
			
			// chaise object to return (null by default)
			Chaise newChaise = null;
			
			// panel for the pieds num dialog
			JPanel piedsNumDialog = new JPanel();
			piedsNumDialog.add(piedsNumLabel);
			piedsNumDialog.add(piedsNum);
			
			// panel for the pieds positions dialog
			JPanel piedsPosDialog;
			
			// open a dialog window to get the number of pieds
			int piedsNumResult = JOptionPane.showConfirmDialog(null, piedsNumDialog, "Nombre des pieds de la chaise",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION);
			
			// if the user clicked OK
			if (piedsNumResult == JOptionPane.OK_OPTION) {
				// get the number of pieds
				int pieds = Integer.parseInt(piedsNum.getText());
				
				// initialize the pieds positions panel
				piedsPosDialog = new JPanel(new GridLayout(1, pieds));
				
				for (int i = 0; i < pieds; i++) {
					// make a temporary panel to store the elements of one pied
					JPanel tempPanel = new JPanel(new GridLayout(4, 1));
					
					// two textfields, one for the X pos and one for the Y pos
					JTextField tempTextFieldX = new JTextField();
					tempTextFieldX.setColumns(5);
					JTextField tempTextFieldY = new JTextField();
					tempTextFieldY.setColumns(5);
					
					// two labels, one for the X pos and one for the Y pos
					JLabel tempLabelX = new JLabel("Pied " + (i + 1) + " X");
					JLabel tempLabelY = new JLabel("Pied " + (i + 1) + " Y");
					
					// add the elements to the temp panel (in order)
					tempPanel.add(tempLabelX);
					tempPanel.add(tempTextFieldX);
					tempPanel.add(tempLabelY);
					tempPanel.add(tempTextFieldY);
					
					// add the textfields to the arraylist so we can get their values later
					piedsPos.add(tempTextFieldX);
					piedsPos.add(tempTextFieldY);
					
					// add the temp panel to the main pieds pos dialog
					piedsPosDialog.add(tempPanel);
				}
				
				// launch a dialog window to get the positions of the pieds
				int piedsValuesResult = JOptionPane.showConfirmDialog(null, piedsPosDialog, "Positions des pieds",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION);
				
				// if the user clicked OK
				if (piedsValuesResult == JOptionPane.OK_OPTION) {
					// make a new Chaise object
					newChaise = new Chaise();
					for (int i = 0; i < piedsPos.size(); i += 2) {
						// get the positions of the pieds from the arraylist
						double tempValX = Double.parseDouble(piedsPos.get(i).getText());
						double tempValY = Double.parseDouble(piedsPos.get(i+1).getText());
						
						// make a new pied and add it to the Chaise object
						Pied p = new Pied(tempValX, tempValY, i/2 + 1);
						newChaise.addPied(p);
					}
				}
			}
			
			// finally, return the chaise object (or null if the user cancelled out)
			return newChaise;
		}
	}
}