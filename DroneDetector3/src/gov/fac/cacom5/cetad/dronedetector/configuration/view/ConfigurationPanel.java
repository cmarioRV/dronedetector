package gov.fac.cacom5.cetad.dronedetector.configuration.view;

import javax.swing.JPanel;
import java.awt.Color;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.border.EtchedBorder;

public class ConfigurationPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtP;
	private JTextField txtN;
	private JTextField txtM;
	
	JButton btnAccept;
	JButton btnCancel;
	private JTextField txtEmail;
	private JTextField txtThreshold;
	private JTextField txtSkipUntilCoeff;
	private JTextField txtMatchNumber;
	
	public ConfigurationPanel() {
		setSize(550, 339);
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		panelNorth.setBackground(Color.DARK_GRAY);
		add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));
		
		JLabel lblTitle = new JLabel("Par\u00E1metros del detector");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelNorth.add(lblTitle);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblSubtitle = new JLabel("No modifique a menos que conozca su funcionamiento");
		lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelNorth.add(lblSubtitle);
		lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblSubtitle.setForeground(Color.RED);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Color.DARK_GRAY);
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 30));
		
		JPanel panelCenterWest = new JPanel();
		panelCenterWest.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelCenterWest.setBackground(Color.DARK_GRAY);
		panelCenter.add(panelCenterWest);
		panelCenterWest.setLayout(new BorderLayout(0, 5));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.GRAY);
		panelCenterWest.add(panel_2, BorderLayout.NORTH);
		FlowLayout fl_panel_2 = new FlowLayout(FlowLayout.CENTER, 5, 0);
		panel_2.setLayout(fl_panel_2);
		
		JLabel lblNewLabel = new JLabel("Par\u00E1metros de detecci\u00F3n");
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial Unicode MS", Font.BOLD, 11));
		panel_2.add(lblNewLabel);
		lblNewLabel.setForeground(Color.WHITE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panelCenterWest.add(panel, BorderLayout.WEST);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Box verticalBox = Box.createVerticalBox();
		panel.add(verticalBox);
		
		JLabel lblCoeffNumber = new JLabel("N\u00FAmero coeficientes");
		verticalBox.add(lblCoeffNumber);
		lblCoeffNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCoeffNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblCoeffNumber.setForeground(Color.WHITE);
		
		Component verticalStrut = Box.createVerticalStrut(9);
		verticalBox.add(verticalStrut);
		
		JLabel lblNewLabel_1 = new JLabel("Descartar primeros Coefficientes");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(lblNewLabel_1);
		
		Component verticalStrut_3 = Box.createVerticalStrut(9);
		verticalBox.add(verticalStrut_3);
		
		JLabel lblHammingSize = new JLabel("Tama\u00F1o ventana Hamming");
		verticalBox.add(lblHammingSize);
		lblHammingSize.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblHammingSize.setHorizontalAlignment(SwingConstants.CENTER);
		lblHammingSize.setForeground(Color.WHITE);
		
		Component verticalStrut_1 = Box.createVerticalStrut(9);
		verticalBox.add(verticalStrut_1);
		
		JLabel lblM = new JLabel("M");
		verticalBox.add(lblM);
		lblM.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblM.setHorizontalAlignment(SwingConstants.CENTER);
		lblM.setForeground(Color.WHITE);
		
		Component verticalStrut_2 = Box.createVerticalStrut(9);
		verticalBox.add(verticalStrut_2);
		
		JLabel lblThreshold = new JLabel("Threshold");
		lblThreshold.setHorizontalAlignment(SwingConstants.CENTER);
		lblThreshold.setForeground(Color.WHITE);
		lblThreshold.setAlignmentX(0.5f);
		verticalBox.add(lblThreshold);
		
		Component verticalStrut_8 = Box.createVerticalStrut(9);
		verticalBox.add(verticalStrut_8);
		
		JLabel lblmatchNumber = new JLabel("N\u00FAmero de positivos");
		lblmatchNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblmatchNumber.setForeground(Color.WHITE);
		lblmatchNumber.setAlignmentX(0.5f);
		verticalBox.add(lblmatchNumber);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		panelCenterWest.add(panel_1, BorderLayout.EAST);
		
		Box verticalBox_1 = Box.createVerticalBox();
		panel_1.add(verticalBox_1);
		
		txtP = new JTextField();
		txtP.setHorizontalAlignment(SwingConstants.CENTER);
		verticalBox_1.add(txtP);
		txtP.setText("15");
		txtP.setColumns(10);
		
		Component verticalStrut_4 = Box.createVerticalStrut(1);
		verticalBox_1.add(verticalStrut_4);
		
		txtSkipUntilCoeff = new JTextField();
		txtSkipUntilCoeff.setHorizontalAlignment(SwingConstants.CENTER);
		txtSkipUntilCoeff.setText("5");
		verticalBox_1.add(txtSkipUntilCoeff);
		txtSkipUntilCoeff.setColumns(10);
		
		Component verticalStrut_5 = Box.createVerticalStrut(1);
		verticalBox_1.add(verticalStrut_5);
		
		txtN = new JTextField();
		txtN.setHorizontalAlignment(SwingConstants.CENTER);
		verticalBox_1.add(txtN);
		txtN.setText("3000");
		txtN.setColumns(10);
				
				Component verticalStrut_6 = Box.createVerticalStrut(1);
				verticalBox_1.add(verticalStrut_6);
		
				txtM = new JTextField();
				txtM.setHorizontalAlignment(SwingConstants.CENTER);
				verticalBox_1.add(txtM);
				txtM.setText("1000");
				txtM.setColumns(10);
				
				Component verticalStrut_7 = Box.createVerticalStrut(1);
				verticalBox_1.add(verticalStrut_7);
				
				txtThreshold = new JTextField();
				txtThreshold.setHorizontalAlignment(SwingConstants.CENTER);
				txtThreshold.setText("0.62");
				verticalBox_1.add(txtThreshold);
				txtThreshold.setColumns(10);
				
				Component verticalStrut_9 = Box.createVerticalStrut(1);
				verticalBox_1.add(verticalStrut_9);
				
				txtMatchNumber = new JTextField();
				txtMatchNumber.setText("4");
				txtMatchNumber.setHorizontalAlignment(SwingConstants.CENTER);
				txtMatchNumber.setColumns(10);
				verticalBox_1.add(txtMatchNumber);
		
		JPanel panelCenterEast = new JPanel();
		panelCenterEast.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelCenterEast.setBackground(Color.DARK_GRAY);
		panelCenter.add(panelCenterEast);
		panelCenterEast.setLayout(new BorderLayout(0, 5));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.GRAY);
		panelCenterEast.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		
		JLabel lblParmetrosDeAlerta = new JLabel("Par\u00E1metros de alerta");
		panel_3.add(lblParmetrosDeAlerta);
		lblParmetrosDeAlerta.setHorizontalAlignment(SwingConstants.CENTER);
		lblParmetrosDeAlerta.setForeground(Color.WHITE);
		lblParmetrosDeAlerta.setFont(new Font("Arial Unicode MS", Font.BOLD, 11));
		lblParmetrosDeAlerta.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.DARK_GRAY);
		panelCenterEast.add(panel_4, BorderLayout.CENTER);
		
		Box verticalBox_2 = Box.createVerticalBox();
		panel_4.add(verticalBox_2);
		
		JLabel lblEmail = new JLabel("Correo electr\u00F3nico");
		lblEmail.setForeground(Color.WHITE);
		verticalBox_2.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setText("mariomad18@gmail.com");
		txtEmail.setHorizontalAlignment(SwingConstants.CENTER);
		verticalBox_2.add(txtEmail);
		txtEmail.setColumns(15);
		
		JPanel panelSouth = new JPanel();
		panelSouth.setBackground(Color.DARK_GRAY);
		add(panelSouth, BorderLayout.SOUTH);
		
		btnAccept = new JButton("Aceptar");
		panelSouth.add(btnAccept);
		
		btnCancel = new JButton("Cancelar");
		panelSouth.add(btnCancel);
		
		setVisible(true);
	}
	
	public void suscribeActionAcceptButton(Action action)
	{
		btnAccept.setAction(action);
	}
	
	public void suscribeActionCancelButton(Action action)
	{
		btnCancel.setAction(action);
	}
	
	public int getP()
	{
		return Integer.parseInt(txtP.getText());
	}
	
	public int getOffset()
	{
		return Integer.parseInt(txtSkipUntilCoeff.getText());
	}
	
	public int getN()
	{
		return Integer.parseInt(txtN.getText());
	}
	
	public int getM()
	{
		return Integer.parseInt(txtM.getText());
	}
	
	public double getThreshold()
	{
		return Double.parseDouble(txtThreshold.getText());
	}
	
	public int getMatchNumber()
	{
		return Integer.parseInt(txtMatchNumber.getText());
	}

	public String getInput()
	{
		return "resources/input.txt";
	}
	
	public String getOutput()
	{
		return "resources/output.txt";
	}	
	
	public String getEmail()
	{
		return txtEmail.getText();
	}	
}
