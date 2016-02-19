package gov.fac.cacom5.cetad.dronedetector.detector.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import gov.fac.cacom5.cetad.dronedetector.MainController;
import gov.fac.cacom5.cetad.dronedetector.db.DatabaseManager;
import gov.fac.cacom5.cetad.dronedetector.detector.model.CalculationQueue;
import gov.fac.cacom5.cetad.dronedetector.detector.model.DecisionQueue;
import gov.fac.cacom5.cetad.dronedetector.detector.model.LPCParameters;
import gov.fac.cacom5.cetad.dronedetector.detector.view.DetectorPanel;
import gov.fac.cacom5.cetad.dronedetector.model.Estimator;

public class DetectorController implements Observer {
	
	private static final int STOPPED = 0;
	private static final int STARTING = 1;
	private static final int RUNNING = 2;
	private static final int STOPPING = 3;

	MainController mainController;
	DetectorPanel detectorPanel;
	DataAcquisitionController audioController;
	EmailController emailController;
	//LPCController lpcController;
	Estimator estimator;
	
	CalculationController calculationController;
	
	ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);
	
	DatabaseManager databaseManager;
	
	//Actions declaration
	StartDetectorAction startDetectorAction;
	StopDetectorAction stopDetectorAction;
	ClearAlertAction clearAlertAction;
	
	public DetectorController(MainController pMainController, DatabaseManager pDatabaseManager) {
		this.mainController = pMainController;
		this.calculationController = new CalculationController(pMainController.getLPCParameters());
		this.emailController = new EmailController();
		
		this.databaseManager = pDatabaseManager;
		
		//Initialize panels
		detectorPanel = new DetectorPanel();
		setStatus(STOPPED);
		
		//Initialize actions
		startDetectorAction = new StartDetectorAction("Iniciar", null, "Iniciar la detecci\u00f3n", KeyEvent.VK_I);
		stopDetectorAction	= new StopDetectorAction("Detener", null, "Detener la detecci\u00f3n", null);
		clearAlertAction    = new ClearAlertAction("OK", null, "Ocultar alerta", null);
		
		//Add actions to panels
		detectorPanel.suscribeActionToStartStopButton(startDetectorAction);
		detectorPanel.suscribeActionToClearAlertButton(clearAlertAction);
	}
	
	public Component getWindow()
	{
		return detectorPanel;
	}
	
	public void startDetection()
	{
		startDetectorAction.setEnabled(false);
		
		setStatus(STARTING);
		
		try {
			//Instantiate AudioController
			audioController = new DataAcquisitionController(this);

			databaseManager.init();	
			//calculationController = new CalculationController(this, mainController.getLPCParameters(), databaseManager.getCoefficients());
			calculationController = new CalculationController(this, mainController.getLPCParameters(), databaseManager.getDBData());
			databaseManager.close();
			
			audioController.startCapture(mainController.getBufferSize());
			detectorPanel.suscribeActionToStartStopButton(stopDetectorAction);

			startDetectorAction.setEnabled(true);
			
			//Inform to MainController that Detector is running
			mainController.detectionStarted();
			
			emailController.start();
			setStatus(RUNNING);
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(detectorPanel, "Dispositivo de entrada no detectado", "Error", JOptionPane.ERROR_MESSAGE);
			mainController.detectionStopped();
			startDetectorAction.setEnabled(true);
			calculationController.stop();
			setStatus(STOPPED);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(detectorPanel, "Error de base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			mainController.detectionStopped();
			startDetectorAction.setEnabled(true);
			calculationController.stop();
			setStatus(STOPPED);
		} catch (NullPointerException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(detectorPanel, "No existen muestras almacenadas", "Error", JOptionPane.ERROR_MESSAGE);
			mainController.detectionStopped();
			startDetectorAction.setEnabled(true);
			calculationController.stop();
			setStatus(STOPPED);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(detectorPanel, "Error de base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			mainController.detectionStopped();
			startDetectorAction.setEnabled(true);
			calculationController.stop();
			setStatus(STOPPED);
		} finally {
			startDetectorAction.setEnabled(true);
		}
	}
	
	public void stopDetection()
	{
		stopDetectorAction.setEnabled(false);
		setStatus(STOPPING);
		mainController.detectionStopping();
		calculationController.stop();
		audioController.stopCapture();
		emailController.stop();
	}
	
	public void lineIsClosed()
	{
		detectorPanel.suscribeActionToStartStopButton(startDetectorAction);
		stopDetectorAction.setEnabled(true);
		
		//Inform to MainController that Detector is stopped
		mainController.detectionStopped();
		setStatus(STOPPED);
	}
	
	public void processDataBuffer(byte[] pData)
	{
		calculationController.processData(pData);
	}
	
	class StartDetectorAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public StartDetectorAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			startDetection();
		}
	}
	
	class StopDetectorAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public StopDetectorAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			stopDetection();
		}
	}
	
	class ClearAlertAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public ClearAlertAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			detectorPanel.hideAlert();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o.getClass().equals(CalculationQueue.class))
		{
			calculationController.newMatch((String)arg);
		}
		else if(o.getClass().equals(DecisionQueue.class))
		{
			detectorPanel.showAlert((String)arg);
			emailController.newMatch((String)arg);
		}
	}
	
	public double[] getFileCoefficients(File file, LPCParameters parameters)
	{
		try {
			if(calculationController == null) calculationController = new CalculationController(parameters);
			double[] result = calculationController.getFileCoefficients(file, parameters);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void setStatus(int status)
	{
		switch (status) {
		case 0:
			detectorPanel.setState("Detenido", Color.RED);
			break;
		case 1:
			detectorPanel.setState("Iniciando...", Color.LIGHT_GRAY);
			break;
		case 2:
			detectorPanel.setState("Iniciado", Color.GREEN);
			break;
		case 3:
			detectorPanel.setState("Deteniendo...", Color.ORANGE);
			break;

		default:
			break;
		}
	}
}
