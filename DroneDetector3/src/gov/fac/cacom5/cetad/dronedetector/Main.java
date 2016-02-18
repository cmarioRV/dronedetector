package gov.fac.cacom5.cetad.dronedetector;

import java.text.SimpleDateFormat;
import java.util.Date;

import gov.fac.cacom5.cetad.dronedetector.model.EmailSender;

public class Main {
	
	static MainController mainController;
	
	public static void main(String[] args) {
		mainController = new MainController();
	}
}
