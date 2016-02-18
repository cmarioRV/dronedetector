package gov.fac.cacom5.cetad.dronedetector.addsample.controller;

import java.util.concurrent.ThreadPoolExecutor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import gov.fac.cacom5.cetad.dronedetector.addsample.view.CoefficientsChartPanel;

public class AudioPlotController {
	
	CoefficientsChartPanel audioPanel;
	DefaultCategoryDataset dataset;
	
	//Declare executor
	ThreadPoolExecutor executor;
	
	public AudioPlotController() {
		JFreeChart audioChart = ChartFactory.createLineChart("Streaming", "Coeficientes", "Valor", 
				createDataset(), PlotOrientation.HORIZONTAL, true, true, false);
		
		audioPanel = new CoefficientsChartPanel(audioChart);
		audioPanel.setPreferredSize( new java.awt.Dimension( 200 , 200));
		audioPanel.setVisible(true);
	}
	
	private DefaultCategoryDataset createDataset( )
   {
      dataset = new DefaultCategoryDataset( );
      
      dataset.addValue( 15 , "Valor" , "0" );
      dataset.addValue( 30 , "Valor" , "1" );
      dataset.addValue( 60 , "Valor" ,  "2" );
      dataset.addValue( 120 , "Valor" , "3" );
      dataset.addValue( 240 , "Valor" , "4" );
      dataset.addValue( 300 , "Valor" , "5" );
      dataset.addValue( 300 , "Valor" , "6" );
      dataset.addValue( 300 , "Valor" , "7" );
      dataset.addValue( 300 , "Valor" , "8" );
      dataset.addValue( 300 , "Valor" , "9" );
      return dataset;
   }
	
	public CoefficientsChartPanel getWindow() {
		return this.audioPanel;
	}
	
	public void setData(byte[] pData)
	{
		dataset.setValue(pData[0], "values", "0");
		dataset.setValue(pData[1], "values", "1");
		dataset.setValue(pData[2], "values", "2");
		dataset.setValue(pData[3], "values", "3");
		dataset.setValue(pData[4], "values", "4");
		dataset.setValue(pData[5], "values", "5");
		dataset.setValue(pData[6], "values", "6");
		dataset.setValue(pData[7], "values", "7");
		dataset.setValue(pData[8], "values", "8");
		dataset.setValue(pData[9], "values", "9");
	}
}
