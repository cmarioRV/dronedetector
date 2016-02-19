package gov.fac.cacom5.cetad.dronedetector.deletesample.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Box;
import java.awt.Component;

public class DeleteSamplePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//DefaultCategoryDataset dataset;
	JComboBox<String> comboBox;
	JButton btnDelete;
	JButton btnCancel;
	
	public DeleteSamplePanel() {
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		panelNorth.setBackground(Color.DARK_GRAY);
		add(panelNorth, BorderLayout.NORTH);
		
		JLabel lblTitle = new JLabel("Eliminar muestra");
		lblTitle.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
		lblTitle.setForeground(Color.WHITE);
		panelNorth.add(lblTitle);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Color.DARK_GRAY);
		add(panelCenter, BorderLayout.CENTER);
		
		Box verticalBox = Box.createVerticalBox();
		panelCenter.add(verticalBox);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);
		
		JLabel lblSelectSample = new JLabel("Seleccione la muestra a eliminar");
		lblSelectSample.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblSelectSample.setAlignmentY(0.0f);
		verticalBox.add(lblSelectSample);
		lblSelectSample.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectSample.setForeground(Color.WHITE);
		
		Component verticalStrut_1 = Box.createVerticalStrut(5);
		verticalBox.add(verticalStrut_1);
		
		comboBox = new JComboBox<String>();
		comboBox.setForeground(Color.BLACK);
		verticalBox.add(comboBox);
		comboBox.setBackground(Color.LIGHT_GRAY);
		
		JPanel panelSouth = new JPanel();
		panelSouth.setBackground(Color.DARK_GRAY);
		add(panelSouth, BorderLayout.SOUTH);
		
		btnDelete = new JButton("Eliminar");
		panelSouth.add(btnDelete);
		
		btnCancel = new JButton("Cancelar");
		panelSouth.add(btnCancel);
	}
	
	public void suscribeDeleteActionButton(Action action)
	{
		btnDelete.setAction(action);
	}
	
	public void suscribeCalceActionButton(Action action)
	{
		btnCancel.setAction(action);
	}
	
	public void setData(String[] pFileNames)
	{
		comboBox.removeAllItems();
		for (String filename : pFileNames) comboBox.addItem(filename);
	}
	
	public void addData(String pFilename)
	{
		comboBox.addItem(pFilename);
	}
	
	public String getItemSelected()
	{
		return (String) comboBox.getSelectedItem();
	}
}
