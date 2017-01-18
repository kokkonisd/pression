package gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

public class ComConfigPanel extends JPanel {

	SerialPort[] serialPorts=SerialPort.getCommPorts();
	SerialPort selectedSerial;

	public ComConfigPanel(final MainWindow mainwindow) {
		super();

		String[] comListNames=new String[serialPorts.length];
		for(int i=0;i<serialPorts.length;i++){
			comListNames[i]=serialPorts[i].getSystemPortName()+ "|"+ serialPorts[i].getDescriptivePortName();
		}

		final JComboBox<String> comCombobox=new JComboBox<>(comListNames);
		add(comCombobox);

		selectedSerial=serialPorts[comCombobox.getSelectedIndex()];

		final JButton btnConnexion = new JButton("Connexion");
		final JButton btnDeconnexion=new JButton("Deconnexion");

		btnConnexion.setEnabled(true);
		btnDeconnexion.setEnabled(false);


		btnConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comCombobox.setEnabled(false);

				selectedSerial=serialPorts[comCombobox.getSelectedIndex()];

				mainwindow.setSerialPort(selectedSerial);

				btnDeconnexion.setEnabled(true);
				btnConnexion.setEnabled(false);
			}
		});
		add(btnConnexion);


		btnDeconnexion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comCombobox.setEnabled(true);
				btnConnexion.setEnabled(true);
				btnDeconnexion.setEnabled(false);

			}
		});
		add(btnDeconnexion);

	}

	public ComConfigPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ComConfigPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ComConfigPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

}
