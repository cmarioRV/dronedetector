package gov.fac.cacom5.cetad.dronedetector.deletesample.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import gov.fac.cacom5.cetad.dronedetector.MainController;
import gov.fac.cacom5.cetad.dronedetector.db.DatabaseManager;
import gov.fac.cacom5.cetad.dronedetector.deletesample.view.DeleteSamplePanel;

public class DeleteSampleController implements Observer {
	
	MainController mainController;
	DeleteSamplePanel deleteSamplePanel;
	
	DatabaseManager databaseManager;
	
	DeleteAction deleteAction;
	CancelAction cancelAction;
	
	public DeleteSampleController(MainController pMainController, DatabaseManager pDatabaseManager) {
		this.mainController = pMainController;
		
		deleteAction = new DeleteAction("Eliminar", null, "Elimina la muestra de la base de datos", null);
		cancelAction = new CancelAction("Cancelar", null, "Descarta los cambios", null);
		
		this.databaseManager = pDatabaseManager;
		
		databaseManager = pDatabaseManager;
		databaseManager.addObserver(this);
		
		deleteSamplePanel = new DeleteSamplePanel();
		deleteSamplePanel.setData(getData());
		if(getData().length == 0) deleteAction.setEnabled(false);
		
		deleteSamplePanel.suscribeDeleteActionButton(deleteAction);
		deleteSamplePanel.suscribeCalceActionButton(cancelAction);
	}
	
	public Component getWindow()
	{
		return deleteSamplePanel;
	}
	
	private String[] getData()
	{
		String[] results = null;
		try {
			//DatabaseManager databaseManager = new DatabaseManager();
			databaseManager.init();
			results = databaseManager.getNames();
			databaseManager.close();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return results;
	}
	
	class DeleteAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public DeleteAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			if(newSampleCoefficients != null)
			{
				DatabaseManager databaseManager = new DatabaseManager();
				databaseManager.setNewData(addSamplePanel.getNewSampleName(), newSampleCoefficients);
				addSamplePanel.disableData();
				mainController.hidePanel(3);
			}
			*/
			int dialogResult = JOptionPane.showConfirmDialog(deleteSamplePanel, "Borrar muestra?", "Confirmar", JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				
				
				try {
					//DatabaseManager databaseManager = new DatabaseManager();
					databaseManager.init();
					String[] results = databaseManager.deleteEntry(deleteSamplePanel.getItemSelected());
					databaseManager.close();
					deleteSamplePanel.setData(results);
					if(results.length == 0) deleteAction.setEnabled(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				
				//mainController.hidePanel(4);
			}
			
		}
	}
	
	class CancelAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public CancelAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mainController.hidePanel(4);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		deleteSamplePanel.addData((String) arg);
		deleteAction.setEnabled(true);
	}
}
