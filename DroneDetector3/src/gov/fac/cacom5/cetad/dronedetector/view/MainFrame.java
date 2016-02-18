package gov.fac.cacom5.cetad.dronedetector.view;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTabbedPane;

import gov.fac.cacom5.cetad.dronedetector.detector.view.DetectorPanel;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		setSize(550, 339);
		setTitle("Drone Detector");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setForeground(Color.WHITE);	
		setLocationRelativeTo(null);
	}
}
