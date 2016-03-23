package gov.fac.cacom5.cetad.dronedetector.configuration.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import gov.fac.cacom5.cetad.dronedetector.MainController;
import gov.fac.cacom5.cetad.dronedetector.configuration.model.ConfigurationParameters;
import gov.fac.cacom5.cetad.dronedetector.configuration.view.ConfigurationPanel;

public class ConfigurationController {
	MainController mainController;
	ConfigurationPanel configurationPanel;
	
	//Actions declaration
	AcceptAction acceptAction;
	CancelAction cancelAction;
	
	//Declare ConfigurationParameters
	public ConfigurationParameters configurationParameters;
	
	public ConfigurationController(MainController pMainController) {
		this.mainController = pMainController;
		
		//Initialize panels
		configurationPanel = new ConfigurationPanel();
		
		//Initialize actions
		acceptAction = new AcceptAction("Aceptar", null, "Guardar los cambios", KeyEvent.VK_A);
		cancelAction = new CancelAction("Cancelar", null, "Descartar los cambios", KeyEvent.VK_C);
		
		//Add actions to panels
		configurationPanel.suscribeActionAcceptButton(acceptAction);
		configurationPanel.suscribeActionCancelButton(cancelAction);
		
		//Initialize ConfigurationParameters
		configurationParameters = new ConfigurationParameters(configurationPanel.getP(), configurationPanel.getOffset(), configurationPanel.getN(), 
				configurationPanel.getM(), configurationPanel.getThreshold(), configurationPanel.getMatchNumber(), configurationPanel.getInput(), configurationPanel.getOutput(), configurationPanel.getEmail());
	}
	
	public Component getWindow()
	{
		return configurationPanel;
	}
	/*
	public int getN()
	{
		return configurationPanel.getN();
	}
	*/
	
	public ConfigurationParameters getConfigurationParameters()
	{
		return configurationParameters;
	}
	class AcceptAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AcceptAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			configurationParameters.setP(configurationPanel.getP());
			configurationParameters.setN(configurationPanel.getN());
			configurationParameters.setM(configurationPanel.getM());
			configurationParameters.setOutput(configurationPanel.getOutput());
			configurationParameters.setEmail(configurationPanel.getEmail());
			configurationParameters.setThreshold(configurationPanel.getThreshold());
			configurationParameters.setOffset(configurationPanel.getOffset());
			configurationParameters.setMatchNumber(configurationPanel.getMatchNumber());
			mainController.hidePanel(2);
		}
	}
	
	class CancelAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CancelAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			mainController.hidePanel(2);
		}
	}
}
