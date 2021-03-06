package gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fazecast.jSerialComm.SerialPort;

import Calculation.Chaise;
import Calculation.Pied;
import serialComm.ComInterface;

public class MainWindow extends JFrame {

	// window size in pixels
	private final int WIDTH=700;
	private final int HEIGHT=400;

	// window title
	private final String AppTitle="Détecteur de posture (DI6)";

	// Chaise object
	private Chaise chaise;

	// custom made panel containing the visualization
	PostureVisualizationJPanel panel;

	// vars to hold the port we use and the list of available ports
	SerialPort serialPort;
	SerialPort[] serialPorts=SerialPort.getCommPorts();

	// custom made ComInterface
	ComInterface comInterface;

	/* J objects to be used in saving/loading the chaise object */
	// file chooser to load/save chaise
    final JFileChooser chaiseChooser = new JFileChooser(new File(System.getProperty("user.dir")));
    // slider to control the radius of the deadzone
 	final JSlider deadzoneRadiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	public MainWindow() throws HeadlessException, ClassNotFoundException, IOException {
		super();

		// initialize the Chaise and visualization panel objects
		chaise=new Chaise();
		panel=new PostureVisualizationJPanel(chaise);

		// MENU
		JMenuBar menuBar;
		JMenu menuFile, menuHelp;
		JMenuItem menuItem;

		// Create the menu bar
		menuBar = new JMenuBar();

		// Build the menus
		menuFile = new JMenu("Fichier");
		menuHelp = new JMenu("Aide");
		menuFile.getAccessibleContext().setAccessibleDescription("Menu Fichier");
		menuHelp.getAccessibleContext().setAccessibleDescription("Menu Aide");
		menuBar.add(menuFile);
		menuBar.add(menuHelp);

		// save chaise menu item
		menuItem = new JMenuItem("Enregistrer la chaise");
		menuItem.getAccessibleContext().setAccessibleDescription("Enregistrer la chaise");
		menuFile.add(menuItem);

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnSaveChaiseHandler(chaiseChooser);
			}
		});

		// load chaise menu item
		menuItem = new JMenuItem("Ouvrir la chaise");
		menuItem.getAccessibleContext().setAccessibleDescription("Ouvrir la chaise");
		menuFile.add(menuItem);

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnLoadChaiseHandler(chaiseChooser, deadzoneRadiusSlider);
			}
		});

		// documentation (en) menu item
		menuItem = new JMenuItem("Documentation (français)");
		menuItem.getAccessibleContext().setAccessibleDescription("Documentation FR");
		menuHelp.add(menuItem);

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					showDoc("fr");
				} catch (IOException e1) {
					System.out.println("ERROR: Could not show documentation.");
				}
			}
		});

		// documentation (en) menu item
		menuItem = new JMenuItem("Documentation (english)");
		menuItem.getAccessibleContext().setAccessibleDescription("Documentation EN");
		menuHelp.add(menuItem);

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					showDoc("en");
				} catch (IOException e1) {
					System.out.println("ERROR: Could not show documentation.");
				}
			}
		});

		this.setJMenuBar(menuBar);

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
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
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


			JPanel connectPanel=new JPanel();
			connectPanel.setLayout(new GridLayout(-1,1));

			JPanel btButPanel=new JPanel();
			btButPanel.setLayout(new GridLayout(1, 2));


			JPanel chaisePanel=new JPanel();
			chaisePanel.setLayout(new GridLayout(-1,1,0,10));

			JPanel chaiseButPanel=new JPanel();
			chaiseButPanel.setLayout(new GridLayout(-1,1,0,10));
			chaisePanel.add(chaiseButPanel);

			// get the names of the available ports on the machine
			String[] comListNames=new String[serialPorts.length];
			for(int i=0;i<serialPorts.length;i++){
				comListNames[i]=serialPorts[i].getSystemPortName()+ "|"+ serialPorts[i].getDescriptivePortName();
			}

			// make a combobox containing the port names, then add it to the container (main window)
			final JComboBox<String> comCombobox=new JComboBox<>(comListNames);
			connectPanel.add(comCombobox);

			connectPanel.add(btButPanel);

			// connection button
			final JButton btnConnexion = new JButton("Connexion");
			// de-connection button
			final JButton btnDeconnexion=new JButton("Deconnexion");

			// test button to show that we can send data to the Arduino
			final JButton btnSendTextToArduino=new JButton("Tare");

			// if no ports are available, the connection button should be turned off
			if(serialPorts.length==0){
				btnConnexion.setEnabled(false);
			}

			// if no connection is made, the de-connection button should be off (by default)
			btnDeconnexion.setEnabled(false);


			btnConnexion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					btnConnectionHandler(comCombobox, btnConnexion, btnDeconnexion);
				}
			});
			// add the connection button to the container (main window)
			btButPanel.add(btnConnexion);


			btnDeconnexion.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					btnDeconnectionHandler(comCombobox, btnConnexion, btnDeconnexion);
				}
			});
			// add the connection button to the container (main window)
			btButPanel.add(btnDeconnexion);


			btnSendTextToArduino.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					btnSendTextToArduinoHandler();
				}
			});
			// add the Arduino command button to the container (main window)
			chaiseButPanel.add(btnSendTextToArduino);


			// setting the chaise configuration
			final JButton btnConfigChaise = new JButton("Config Chaise");

			btnConfigChaise.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					btnConfigChaiseHandler();
				}
			});
			// add the button
			chaiseButPanel.add(btnConfigChaise);

			// controls for calibrating the deadzone
			final JButton btnCalibrateDeadzone = new JButton("Calibrer la zone");

			btnCalibrateDeadzone.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					btnCalibrateDeadzoneHandler();
				}
			});
			// add the button
			chaiseButPanel.add(btnCalibrateDeadzone);


			deadzoneRadiusSlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					chaise.setDeadzoneRatio(deadzoneRadiusSlider.getValue() / 100.0);
					panel.repaint();
				}
			});
			// add the slider
			chaisePanel.add(deadzoneRadiusSlider);


			//Add the panels
			add(connectPanel);
	        add(chaisePanel);



			// set labels on the slider
			deadzoneRadiusSlider.setMajorTickSpacing(20);
			deadzoneRadiusSlider.setMinorTickSpacing(5);
			deadzoneRadiusSlider.setPaintTicks(true);
			deadzoneRadiusSlider.setPaintLabels(true);
			deadzoneRadiusSlider.setBorder(
	                BorderFactory.createEmptyBorder(0,0,10,0));
	        Font font = new Font("Serif", Font.ITALIC, 15);
	        deadzoneRadiusSlider.setFont(font);


		}
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

	/**
	 * Method to handle the connection to the Arduino
	 * @param comCombobox
	 * @param btnConnexion
	 * @param btnDeconnexion
	 */
	public void btnConnectionHandler(JComboBox comCombobox, JButton btnConnexion, JButton btnDeconnexion) {
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
				btnConnexion.setText("Connecté");
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

	/**
	 * Method to handle the deconnection from the Arduino
	 * @param comCombobox
	 * @param btnConnexion
	 * @param btnDeconnexion
	 */
	public void btnDeconnectionHandler(JComboBox comCombobox, JButton btnConnexion, JButton btnDeconnexion) {
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

	/**
	 * Method to send a command to the Arduino
	 * (in this case, it's the "tare" command)
	 */
	public void btnSendTextToArduinoHandler() {
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

	/**
	 * Method to launch the chaise configuration procedure
	 */
	public void btnConfigChaiseHandler() {
		Chaise c = ChaiseConfigDialog();
		if (c != null) {
			setChaise(c);
			panel.repaint();
		}
	}

	/**
	 * Method to calibrate the position of the deadzone
	 */
	public void btnCalibrateDeadzoneHandler() {
		chaise.setDeadzoneX(chaise.getGposX());
		chaise.setDeadzoneY(chaise.getGposY());
		panel.repaint();
	}

	/**
	 * Method to handle chaise saving
	 * @param saveChaise
	 */
	public void btnSaveChaiseHandler(JFileChooser saveChaise) {
		int returnVal = saveChaise.showSaveDialog(MainWindow.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	System.out.println("Saving chaise...");
        	// get the filename chosen by the user
        	String path = saveChaise.getSelectedFile().getAbsolutePath();
        	// take off the file ending that the user may have put
        	path = path.split(Pattern.quote("."))[0];

        	// make sure we're not overriding a file
        	File f = new File(path + ".txt");
        	int result = 0;
        	if(f.exists() && !f.isDirectory()) {
        		result = JOptionPane.showConfirmDialog(MainWindow.this, "File already exists. Overwrite file?");
        	}
        	if (result == 0) {
        		try {
        			chaise.saveChaise(f);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        }
	}

	/**
	 * Method to handle chaise loading
	 * @param loadChaise
	 * @param deadzoneRadiusSlider
	 */
	public void btnLoadChaiseHandler(JFileChooser loadChaise, JSlider deadzoneRadiusSlider) {
		int returnVal = loadChaise.showOpenDialog(MainWindow.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	System.out.println("Loading chaise...");
        	String path = loadChaise.getSelectedFile().getAbsolutePath();
        	File fin = new File(path);
			try {
				chaise.loadChaise(fin);
	    		panel.repaint();
	    		// reset the radius slider to the value of the chaise we just loaded
	    		deadzoneRadiusSlider.setValue((int) (chaise.getDeadzoneRatio() * 100));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}

	/**
	 * Method to show the documentation
	 * @throws IOException
	 */
	private void showDoc(String lang) throws IOException {
		// get the path to the documentation pdf
		String path = System.getProperty("user.dir");
		Path pathToUrl = Paths.get(path + "/doc_pdf/doc_" + lang + ".pdf");

		// open the pdf
		File doc = new File(pathToUrl.toString());
		Desktop.getDesktop().open(doc);
	}
}