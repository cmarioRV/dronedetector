package gov.fac.cacom5.cetad.dronedetector.view;

import javax.swing.JFrame;

import java.awt.Color;

public class MainFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame() {
		setSize(600, 339);
		setTitle("Drone Detector");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setForeground(Color.WHITE);	
		setLocationRelativeTo(null);
	}
}
