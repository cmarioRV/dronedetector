package gov.fac.cacom5.cetad.dronedetector.detector.view;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import javax.swing.Action;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.border.EtchedBorder;

public class DetectorPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton btnIniciar;
	public JPanel panelCenter;
	private JPanel panel;
	private JLabel lblState;
	private Component horizontalStrut;
	private JPanel panelDroneDetected;
	private JLabel lblDroneDetected;
	private JLabel lblNewLabel;
	private Box horizontalBox;
	private JLabel lblDroneType;
	private Component horizontalStrut_1;
	private JButton btnClearAlert;
	private Box verticalBox;
	private JPanel panel_1;
	private Component verticalStrut;
	private Component verticalStrut_1;
	private Component horizontalStrut_2;
	private ImageIcon cetadIcon;
	private ImageIcon facIcon;
	private ImageIcon cacomIcon;
	
	public DetectorPanel() {
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelSouth = new JPanel();
		panelSouth.setBackground(Color.DARK_GRAY);
		add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.setHorizontalAlignment(SwingConstants.RIGHT);
		panelSouth.add(btnIniciar);
		
		panelCenter = new JPanel();
		panelCenter.setBackground(Color.DARK_GRAY);
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		panelDroneDetected = new JPanel();
		panelDroneDetected.setBackground(Color.DARK_GRAY);
		panelCenter.add(panelDroneDetected, BorderLayout.CENTER);
		panelDroneDetected.setLayout(new BoxLayout(panelDroneDetected, BoxLayout.Y_AXIS));
		
		verticalBox = Box.createVerticalBox();
		verticalBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelDroneDetected.add(verticalBox);
		
		lblDroneDetected = new JLabel("Drone Detectado");
		verticalBox.add(lblDroneDetected);
		lblDroneDetected.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblDroneDetected.setFont(new Font("Arial Unicode MS", Font.PLAIN, 30));
		lblDroneDetected.setHorizontalAlignment(SwingConstants.CENTER);
		lblDroneDetected.setForeground(Color.ORANGE);
		lblDroneDetected.setBackground(Color.DARK_GRAY);
		
		horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		horizontalBox.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		lblNewLabel = new JLabel("Posible tipo:");
		horizontalBox.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
		lblNewLabel.setForeground(Color.WHITE);
		
		horizontalStrut_1 = Box.createHorizontalStrut(10);
		horizontalBox.add(horizontalStrut_1);
		
		lblDroneType = new JLabel("\"\"");
		lblDroneType.setForeground(Color.WHITE);
		lblDroneType.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
		horizontalBox.add(lblDroneType);
		
		btnClearAlert = new JButton("OK");
		verticalBox.add(btnClearAlert);
		btnClearAlert.setBackground(Color.GRAY);
		btnClearAlert.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panelCenter.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		horizontalStrut = Box.createHorizontalStrut(3);
		panel.add(horizontalStrut);
		
		verticalStrut_1 = Box.createVerticalStrut(50);
		panel.add(verticalStrut_1);
		
		lblState = new JLabel("Iniciado");
		lblState.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblState.setHorizontalAlignment(SwingConstants.CENTER);
		lblState.setForeground(Color.GREEN);
		lblState.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
		panel.add(lblState);
		
		horizontalStrut_2 = Box.createHorizontalStrut(50);
		panel.add(horizontalStrut_2);
		
		panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);
		panel_1.setBackground(Color.DARK_GRAY);
		
		verticalStrut = Box.createVerticalStrut(50);
		panel_1.add(verticalStrut);
		
		//cetadIcon = new ImageIcon("images/cetad.png");
		//cacomIcon = new ImageIcon("images/cacom5.png");
		//facIcon = new ImageIcon("images/fac.png");
		cetadIcon = new ImageIcon(getClass().getResource("/images/cetad.png"));
		cacomIcon = new ImageIcon(getClass().getResource("/images/cacom5.png"));
		facIcon = new ImageIcon(getClass().getResource("/images/fac.png"));
		JLabel im1 = new JLabel(cetadIcon);
		JLabel im2 = new JLabel(cacomIcon);
		JLabel im3 = new JLabel(facIcon);

		panel_1.add(im1);
		panel_1.add(im2);
		panel_1.add(im3);
		
		panelDroneDetected.setVisible(false);
		
		setVisible(true);
	}
	
	public void suscribeActionToStartStopButton(Action action)
	{
		btnIniciar.setAction(action);
	}
	
	public void suscribeActionToClearAlertButton(Action action)
	{
		btnClearAlert.setAction(action);
	}
	
	public void setPlot(ChartPanel pChartPanel)
	{
		panelCenter.add(pChartPanel);
	}
	
	public void setState(String pState, Color pColor)
	{
		lblState.setText(pState);
		lblState.setForeground(pColor);
	}
	
	public void showAlert(String droneType)
	{
		lblDroneType.setText(droneType);
		panelDroneDetected.setVisible(true);
	}
	
	public void hideAlert()
	{
		panelDroneDetected.setVisible(false);
	}
}
