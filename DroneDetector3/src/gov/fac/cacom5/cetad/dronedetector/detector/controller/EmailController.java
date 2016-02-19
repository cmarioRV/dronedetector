package gov.fac.cacom5.cetad.dronedetector.detector.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import gov.fac.cacom5.cetad.dronedetector.model.EmailSender;

public class EmailController {
	Date offsetDate;
	Hashtable<String, Date> datesTable;
	String[] emailArray;
	static final long offsetSecondsInMillis = 60000; //60 seconds
	Calendar date;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ThreadPoolExecutor executor;
	
	public EmailController(String pEmail) {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		datesTable = new Hashtable<>();
		emailArray = pEmail.split(",");
	}
	
	public void newMatch(String drone)
	{
		if(datesTable.get(drone) != null)
		{
			date = Calendar.getInstance();
			long t = date.getTimeInMillis();
			offsetDate = new Date(t - (offsetSecondsInMillis));

			if(datesTable.get(drone).before(offsetDate))
			{
				datesTable.put(drone, new Date());
				sendEmail(drone);
			}
		}
		else
		{
			datesTable.put(drone, new Date());
			sendEmail(drone);
		}
	}
	
	private void sendEmail(String drone)
	{
		executor.execute(new SendEmail(drone));
	}
	
	public boolean start()
	{
		if(executor != null)
		{
			if(executor.isShutdown())
			{
				executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
				return true;
			}
		}
		
		return false;
	}
	
	public void stop()
	{
		if(executor != null)
		{
			executor.shutdown();
		}
	}
	
	private class SendEmail implements Runnable
	{
		String drone;
		public SendEmail(String pDrone) 
		{
			drone = pDrone;
		}
		
		@Override
		public void run() {
			//String[] to = {"mariomad18@gmail.com"};
			Date now = new Date();
			String message = "Un posible drone fue detectado el " + sdf.format(now) + "\n\n" + "Del tipo:" + "\n" + drone + 
					"\n\n\n\n\n" + "DroneDetector\n" + "Fuerza Aérea Colombiana\n" + "CACOM5\n" + "CETAD\n" + "cetad@cetad.co";
			EmailSender.send("crua@cetad.co", emailArray, "CETAd2012*", "Posible Drone detectado!", message);
		}
	}
}
