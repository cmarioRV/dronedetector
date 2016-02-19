package gov.fac.cacom5.cetad.dronedetector.addsample.view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;

import javax.sound.sampled.AudioFormat;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.Box;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;

public class AddSamplePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton btnAccept;
	JButton btnCancel;
	JButton btnOpenFile;
	
	private JPanel panelCenter;
	private JPanel panelGraph;
	private JPanel panelData;
	
	private JPanel splitLeftPanel;
	private JPanel splitRightPanel;
	
	CoefficientsChartPanel coefficientsChartPanel;
	JFreeChart audioChart;
	DefaultCategoryDataset dataset;
	private Box leftVerticalBox;
	private Component verticalStrut;
	private Component verticalStrut_1;
	private Component verticalStrut_2;
	private Box rightVerticalBox;
	private JLabel encoding;
	private JLabel sample_rate;
	private JLabel sample_bits;
	private JLabel channels;
	private Component verticalStrut_3;
	private Component verticalStrut_4;
	private Component verticalStrut_5;
	private JPanel panelDataCenter;
	private JPanel panelDataSouth;
	private JLabel lblSampleName;
	private JTextField txtSampleName;

	public AddSamplePanel() {
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout(0, 0));
		
		this.dataset = new DefaultCategoryDataset();
		
		JPanel panelNorth = new JPanel();
		panelNorth.setBackground(Color.DARK_GRAY);
		add(panelNorth, BorderLayout.NORTH);
		
		JLabel lblOpenFile = new JLabel("Seleccione el archivo");
		lblOpenFile.setForeground(Color.WHITE);
		panelNorth.add(lblOpenFile);
		
		btnOpenFile = new JButton("Archivo...");
		panelNorth.add(btnOpenFile);
		
		JPanel panelSouth = new JPanel();
		panelSouth.setBackground(Color.DARK_GRAY);
		add(panelSouth, BorderLayout.SOUTH);
		
		btnAccept = new JButton("Aceptar");
		panelSouth.add(btnAccept);
		
		btnCancel = new JButton("Cancelar");
		panelSouth.add(btnCancel);
		
		panelCenter = new JPanel();
		panelCenter.setBackground(Color.DARK_GRAY);
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.X_AXIS));
		
		panelGraph = new JPanel();
		panelGraph.setBackground(Color.DARK_GRAY);
		panelGraph.setVisible(false);
		
		audioChart = ChartFactory.createLineChart(null, null, null, this.dataset, PlotOrientation.VERTICAL, false, false, false);
		coefficientsChartPanel = new CoefficientsChartPanel(audioChart);
		coefficientsChartPanel.setBackground(Color.DARK_GRAY);
		coefficientsChartPanel.setPreferredSize( new java.awt.Dimension( 200 , 200));
		coefficientsChartPanel.setVisible(true);
		panelGraph.add(coefficientsChartPanel);
		panelCenter.add(panelGraph);
		
		panelData = new JPanel();
		panelData.setBackground(Color.DARK_GRAY);
		panelCenter.add(panelData);
		panelData.setLayout(new BorderLayout(0, 0));
		
		panelDataCenter = new JPanel();
		panelDataCenter.setBackground(Color.DARK_GRAY);
		panelData.add(panelDataCenter, BorderLayout.CENTER);
		
		splitLeftPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) splitLeftPanel.getLayout();
		flowLayout_1.setVgap(30);
		panelDataCenter.add(splitLeftPanel);
		splitLeftPanel.setBackground(Color.DARK_GRAY);
		
		leftVerticalBox = Box.createVerticalBox();
		splitLeftPanel.add(leftVerticalBox);
		
		JLabel lblEncoding = new JLabel("Encoding");
		lblEncoding.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
		leftVerticalBox.add(lblEncoding);
		lblEncoding.setForeground(Color.WHITE);
		
		verticalStrut = Box.createVerticalStrut(5);
		leftVerticalBox.add(verticalStrut);
		JLabel lblSampleRate = new JLabel("Sample Rate");
		lblSampleRate.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
		leftVerticalBox.add(lblSampleRate);
		lblSampleRate.setForeground(Color.WHITE);
		
		verticalStrut_1 = Box.createVerticalStrut(5);
		leftVerticalBox.add(verticalStrut_1);
		JLabel lblSampleBits = new JLabel("Sample Bits");
		lblSampleBits.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
		leftVerticalBox.add(lblSampleBits);
		lblSampleBits.setForeground(Color.WHITE);
		
		verticalStrut_2 = Box.createVerticalStrut(5);
		leftVerticalBox.add(verticalStrut_2);
		JLabel lblChannels = new JLabel("Channels");
		lblChannels.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
		leftVerticalBox.add(lblChannels);
		lblChannels.setForeground(Color.WHITE);
		splitRightPanel = new JPanel();
		panelDataCenter.add(splitRightPanel);
		splitRightPanel.setBackground(Color.DARK_GRAY);
		
		rightVerticalBox = Box.createVerticalBox();
		splitRightPanel.add(rightVerticalBox);
		
		encoding = new JLabel("");
		encoding.setForeground(Color.WHITE);
		rightVerticalBox.add(encoding);
		
		verticalStrut_3 = Box.createVerticalStrut(5);
		rightVerticalBox.add(verticalStrut_3);
		
		sample_rate = new JLabel("");
		sample_rate.setForeground(Color.WHITE);
		rightVerticalBox.add(sample_rate);
		
		verticalStrut_4 = Box.createVerticalStrut(5);
		rightVerticalBox.add(verticalStrut_4);
		
		sample_bits = new JLabel("");
		sample_bits.setForeground(Color.WHITE);
		rightVerticalBox.add(sample_bits);
		
		verticalStrut_5 = Box.createVerticalStrut(5);
		rightVerticalBox.add(verticalStrut_5);
		
		channels = new JLabel("");
		channels.setForeground(Color.WHITE);
		rightVerticalBox.add(channels);
		
		panelDataSouth = new JPanel();
		panelDataSouth.setBackground(SystemColor.inactiveCaptionText);
		panelData.add(panelDataSouth, BorderLayout.SOUTH);
		
		lblSampleName = new JLabel("Nombre muestra");
		lblSampleName.setForeground(Color.WHITE);
		panelDataSouth.add(lblSampleName);
		
		txtSampleName = new JTextField();
		panelDataSouth.add(txtSampleName);
		txtSampleName.setColumns(15);
		panelData.setVisible(false);

		//panelCenter.setVisible(true);
	}
	
	public void suscribeAcceptActionButton(Action action)
	{
		btnAccept.setAction(action);
	}
	
	public void suscribeCalceActionButton(Action action)
	{
		btnCancel.setAction(action);
	}
	
	public void suscribeOpenFileActionButton(Action action)
	{
		btnOpenFile.setAction(action);
	}
	
	public void setData(AudioFormat pFormat, double[] pCoefficients, String pFileName)
	{
		dataset.addValue(pCoefficients[0], "Valor", "0");
		dataset.addValue(pCoefficients[1], "Valor", "1");
		dataset.addValue(pCoefficients[2], "Valor",  "2");
		dataset.addValue(pCoefficients[3], "Valor", "3");
		dataset.addValue(pCoefficients[4], "Valor", "4");
		dataset.addValue(pCoefficients[5], "Valor", "5");
		dataset.addValue(pCoefficients[6], "Valor", "6");
		dataset.addValue(pCoefficients[7], "Valor", "7");
		dataset.addValue(pCoefficients[8], "Valor", "8");
		dataset.addValue(pCoefficients[9], "Valor", "9");
		
		encoding.setText(pFormat.getEncoding().toString());
		sample_rate.setText(String.valueOf(pFormat.getSampleRate()));
		sample_bits.setText(String.valueOf(pFormat.getSampleSizeInBits()));
		channels.setText(String.valueOf(pFormat.getChannels()));
		txtSampleName.setText(pFileName);
		panelData.setVisible(true);
		panelGraph.setVisible(true);
	}
	
	public void disableData()
	{
		panelData.setVisible(false);
		panelGraph.setVisible(false);
	}
	
	public String getNewSampleName()
	{
		return txtSampleName.getText();
	}
}
