package gov.fac.cacom5.cetad.dronedetector;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gov.fac.cacom5.cetad.dronedetector.addsample.controller.AddSampleController;
import gov.fac.cacom5.cetad.dronedetector.configuration.controller.ConfigurationController;
import gov.fac.cacom5.cetad.dronedetector.configuration.model.ConfigurationParameters;
import gov.fac.cacom5.cetad.dronedetector.db.DatabaseManager;
import gov.fac.cacom5.cetad.dronedetector.deletesample.controller.DeleteSampleController;
import gov.fac.cacom5.cetad.dronedetector.detector.controller.DetectorController;
import gov.fac.cacom5.cetad.dronedetector.detector.model.LPCParameters;
import gov.fac.cacom5.cetad.dronedetector.model.FACJMenuBar;
import gov.fac.cacom5.cetad.dronedetector.model.FileInfo;
import gov.fac.cacom5.cetad.dronedetector.view.MainFrame;

public class MainController {
	
	//Controller declarations
	AddSampleController addSampleController;
	DeleteSampleController deleteSampleController;
	ConfigurationController configurationController;
	DetectorController detectorController;
	
	//Declare Database Manager
	DatabaseManager databaseManager;
	
	//View declarations
	MainFrame mainFrame;
	JLayeredPane layeredPane;
	CardLayout cardLayout;
	
	//Actions declarations
	AddDroneSampleAction addDroneSampleAction;
	DeleteDroneSampleAction deleteDroneSampleAction;
	OpenConfigurationAction openConfigurationAction;
	AboutUsAction aboutUsAction;
	
	//Menus and SubMenus declarations
	FACJMenuBar menuBar;
	JMenu menuDB, menuOptions, menuAboutUs;
	JMenuItem menuItemCreateNewDBItem, menuItemOpenConfiguration, menuItemAboutUs, menuItemDeleteDBItem;
	
	public MainController() {
		
		//Instance Database Manager
		databaseManager = new DatabaseManager();
		
		//Instance controllers
		configurationController = new ConfigurationController(this);
		detectorController 		= new DetectorController(this, databaseManager);
		addSampleController 	= new AddSampleController(this, databaseManager);
		deleteSampleController  = new DeleteSampleController(this, databaseManager);
		
		//Instance main view components
		mainFrame = new MainFrame();
		layeredPane = new JLayeredPane();
		cardLayout = new CardLayout();
		layeredPane.setLayout(cardLayout);
		
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/drone_icon64.gif")));
		
		layeredPane.add(detectorController.getWindow(), "1");
		layeredPane.add(configurationController.getWindow(), "2");
		layeredPane.add(addSampleController.getWindow(), "3");
		layeredPane.add(deleteSampleController.getWindow(), "4");
		cardLayout.show(layeredPane, "1");
		mainFrame.getContentPane().add(layeredPane);
		
		//Instance actions
		addDroneSampleAction = new AddDroneSampleAction("Agregar muestra", null, "", null);
		deleteDroneSampleAction = new DeleteDroneSampleAction("Eliminar muestra", null, "", null);
		openConfigurationAction = new OpenConfigurationAction("Configuraci\u00f3n...", null, "Abrir configuraci\u00f3n", null);
		aboutUsAction = new AboutUsAction("Acerca de", null, "Información del software", null);
				
		//Set MenuBar
		menuBar = new FACJMenuBar();
		menuBar.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.setBorderPainted(false);
		menuBar.setColor(Color.GRAY);
		menuBar.setBackground(SystemColor.activeCaptionBorder);
		
		//Set Menus
		menuOptions = new JMenu("Opciones");
		menuOptions.setForeground(Color.WHITE);
		menuOptions.setMnemonic(KeyEvent.VK_O);
		
		menuAboutUs = new JMenu("Ayuda");
		menuAboutUs.setForeground(Color.WHITE);
		menuAboutUs.setMnemonic(KeyEvent.VK_A);
		
		menuDB = new JMenu("Administrar muestras");
		menuDB.setBackground(Color.DARK_GRAY);
		menuDB.setForeground(Color.BLACK);
		menuDB.setMnemonic(KeyEvent.VK_B);
		
		//Set MenuItems
		menuItemCreateNewDBItem = new JMenuItem("Agregar nuevo Drone...", new ImageIcon("images/ic_add_circle_outline_black_18dp_1x.png"));
		menuItemCreateNewDBItem.setForeground(Color.WHITE);
		menuItemCreateNewDBItem.setBackground(Color.GRAY);
		menuItemCreateNewDBItem.setAction(addDroneSampleAction);
		
		menuItemDeleteDBItem = new JMenuItem("Eliminar muestra...", null);
		menuItemDeleteDBItem.setForeground(Color.WHITE);
		menuItemDeleteDBItem.setBackground(Color.GRAY);
		menuItemDeleteDBItem.setAction(deleteDroneSampleAction);
		
		menuItemOpenConfiguration = new JMenuItem("Configuración...", new ImageIcon("images/ic_settings_black_18dp_1x.png"));
		menuItemOpenConfiguration.setForeground(Color.WHITE);
		menuItemOpenConfiguration.setBackground(Color.GRAY);
		menuItemOpenConfiguration.setAction(openConfigurationAction);
		
		menuItemAboutUs = new JMenuItem("Acerca de Drone Detector...");
		menuItemAboutUs.setForeground(Color.WHITE);
		menuItemAboutUs.setBackground(Color.GRAY);
		menuItemAboutUs.setAction(aboutUsAction);
		
		//Add MenuItems to Menus
		menuDB.add(menuItemCreateNewDBItem);
		menuDB.add(menuItemDeleteDBItem);
		menuOptions.add(menuDB);
		menuOptions.add(menuItemOpenConfiguration);
		
		menuAboutUs.add(menuItemAboutUs);
		
		//Add Menus to MenuBar
		//menuBar.add(menuDB);
		menuBar.add(menuOptions);
		menuBar.add(menuAboutUs);
			
		//Add MenuBar to Frame
		mainFrame.setJMenuBar(menuBar);
		
		mainFrame.setVisible(true);
	}

	public void hidePanel(int panelNumber)
	{
		//if(panelNumber == 2)
		cardLayout.show(layeredPane, "1");
	}
	
	public int getBufferSize()
	{
		return configurationController.getConfigurationParameters().getN();
	}
	
	public LPCParameters getLPCParameters()
	{
		return (LPCParameters)configurationController.getConfigurationParameters();
	}	
	
	public ConfigurationParameters getConfigurationParameters()
	{
		return configurationController.getConfigurationParameters();
	}	
	
	public FileInfo getFileInfo(File file) throws UnsupportedAudioFileException, IOException
	{
		double[] coefficients = detectorController.getFileCoefficients(file, configurationController.getConfigurationParameters());
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		
		FileInfo fileInfo = new FileInfo(coefficients, audioStream.getFormat(), file.getName().substring(0, file.getName().indexOf(".")));
		
		return fileInfo;
	}
	
	public void repaint()
	{
		mainFrame.repaint();
	}
	
	public void detectionStarted()
	{
		addDroneSampleAction.setEnabled(false);
		openConfigurationAction.setEnabled(false);
		deleteDroneSampleAction.setEnabled(false);
	}
	
	public void detectionStopping()
	{
		
	}
	
	public void detectionStopped()
	{
		addDroneSampleAction.setEnabled(true);
		openConfigurationAction.setEnabled(true);
		deleteDroneSampleAction.setEnabled(true);
	}
	
	class OpenConfigurationAction extends AbstractAction 
	{
		private static final long serialVersionUID = 1L;

		public OpenConfigurationAction(String text, ImageIcon icon, String desc, Integer mnemonic) 
		{
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(layeredPane, "2");
		}
	}
	
	class AddDroneSampleAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public AddDroneSampleAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(layeredPane, "3");
		}
	}
	
	class DeleteDroneSampleAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public DeleteDroneSampleAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(layeredPane, "4");
		}
	}
	
	class AboutUsAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public AboutUsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(mainFrame, "Drone Detector 2016. FAC - CACOM5 - CETAD");
		}
	}
	
	public void droneDetected(int pName)
	{
		
	}
}
