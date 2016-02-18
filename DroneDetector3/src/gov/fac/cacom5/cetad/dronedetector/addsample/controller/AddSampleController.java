package gov.fac.cacom5.cetad.dronedetector.addsample.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.xml.crypto.Data;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import gov.fac.cacom5.cetad.dronedetector.Main;
import gov.fac.cacom5.cetad.dronedetector.MainController;
import gov.fac.cacom5.cetad.dronedetector.addsample.model.AudioFilter;
import gov.fac.cacom5.cetad.dronedetector.addsample.view.AddSamplePanel;
import gov.fac.cacom5.cetad.dronedetector.addsample.view.CoefficientsChartPanel;
import gov.fac.cacom5.cetad.dronedetector.db.DatabaseManager;
import gov.fac.cacom5.cetad.dronedetector.model.FileInfo;
import gov.fac.cacom5.cetad.dronedetector.utils.Wav2TextConverter;

public class AddSampleController extends Observable implements Observer {
	
	MainController mainController;
	
	DatabaseManager databaseManager;
	
	//Declare panels
	AddSamplePanel addSamplePanel;
	DefaultCategoryDataset dataset;
	
	//Actions declaration
	AcceptAction acceptAction;
	CancelAction cancelAction;
	OpenFileAction openFileAction;
	
	//File chooser
	JFileChooser fileChooser;
	
	//Coefficients from the new sample data
	double[] newSampleCoefficients;
	
	public AddSampleController(MainController pMainController, DatabaseManager pDatabaseManager) {
		this.mainController = pMainController;
		this.databaseManager = pDatabaseManager;
		
		//Initiallize panels
		addSamplePanel = new AddSamplePanel();

		//Initialize actions
		acceptAction = new AcceptAction("Aceptar", null, "Guardar muestra", null);
		cancelAction = new CancelAction("Cancelar", null, "Descartar cambios", null);
		openFileAction = new OpenFileAction("Archivo...", null, "Seleccione el archivo de audio", null);
				
		//Add actions to panels
		addSamplePanel.suscribeAcceptActionButton(acceptAction);
		addSamplePanel.suscribeCalceActionButton(cancelAction);
		addSamplePanel.suscribeOpenFileActionButton(openFileAction);
		
		//Create filter for audio files
		AudioFilter audioFilter = new AudioFilter();
		fileChooser = new JFileChooser(System.getProperty("user.home") + 
				System.getProperty("file.separator") + "Ingenieria Cetad" +
				System.getProperty("file.separator") + "Mario Rua" +
				System.getProperty("file.separator") + "Proyectos" +
				System.getProperty("file.separator") + "Drone" +
				System.getProperty("file.separator") + "DESARROLLO" +
				System.getProperty("file.separator") + "Matlab" +
				System.getProperty("file.separator") + "Extractor de coeficientes");
		fileChooser.setFileFilter(audioFilter);
	}
	
	
	class AcceptAction extends AbstractAction
	{
		public AcceptAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(newSampleCoefficients != null)
			{
				
				try {
					//DatabaseManager databaseManager = new DatabaseManager();
					databaseManager.init();
					databaseManager.addEntry(addSamplePanel.getNewSampleName(), newSampleCoefficients);
					databaseManager.close();
					addSamplePanel.disableData();
					mainController.hidePanel(3);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
			
			
		}
	}
	
	class CancelAction extends AbstractAction
	{
		public CancelAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			addSamplePanel.disableData();
			mainController.hidePanel(3);
		}
	}
	
	class OpenFileAction extends AbstractAction
	{
		public OpenFileAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showOpenDialog(addSamplePanel);
		    if (returnVal == JFileChooser.APPROVE_OPTION)
		    {
				try 
				{
					FileInfo fileInfo = mainController.getFileInfo(fileChooser.getSelectedFile());
					newSampleCoefficients = fileInfo.getCoefficients();
					addSamplePanel.setData(fileInfo.getAudioFormat(), fileInfo.getCoefficients(), fileInfo.getName());
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
		    } 
		    else 
		    {
		        System.out.println("File access cancelled by user.");
		    }
		}
	}
	
	public Component getWindow()
	{
		return addSamplePanel;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
