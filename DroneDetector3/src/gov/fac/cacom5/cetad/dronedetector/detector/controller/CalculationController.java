package gov.fac.cacom5.cetad.dronedetector.detector.controller;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gov.fac.cacom5.cetad.dronedetector.detector.model.CalculationQueue;
import gov.fac.cacom5.cetad.dronedetector.detector.model.DecisionJob;
import gov.fac.cacom5.cetad.dronedetector.detector.model.DecisionQueue;
import gov.fac.cacom5.cetad.dronedetector.detector.model.DetectionJob;
import gov.fac.cacom5.cetad.dronedetector.detector.model.FileInfoJob;
import gov.fac.cacom5.cetad.dronedetector.detector.model.CalculationJob;
import gov.fac.cacom5.cetad.dronedetector.detector.model.LPCParameters;

public class CalculationController {

	CalculationQueue calculationQueue;
	DecisionQueue decisionQueue;
	LPCParameters parameters;
	ThreadPoolExecutor executor;
	ScheduledThreadPoolExecutor scheduledExecutor;
	
	public CalculationController(DetectorController pParentController, LPCParameters pParameters, Hashtable<String, double[]> dronesArray) {
		this.parameters = pParameters;
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		scheduledExecutor = new ScheduledThreadPoolExecutor(2);
		calculationQueue = new CalculationQueue(dronesArray, pParameters.getOffset());
		calculationQueue.addObserver(pParentController);
		decisionQueue = new DecisionQueue(pParameters.getMatchNumber());
		decisionQueue.addObserver(pParentController);
		scheduledExecutor.scheduleAtFixedRate(new DetectionJob(calculationQueue, this.parameters.getThreshold()), 1000, 250, TimeUnit.MILLISECONDS);
		scheduledExecutor.scheduleAtFixedRate(new DecisionJob(decisionQueue), 1500, 1000, TimeUnit.MILLISECONDS);
	}
	
	public CalculationController(LPCParameters pParameters)
	{
		if(executor == null) executor  = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		if(scheduledExecutor == null) scheduledExecutor = new ScheduledThreadPoolExecutor(1);
		this.parameters = pParameters;
	}
	/*
	public void start()
	{
		if(executor.isShutdown()) executor  = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		if(scheduledExecutor.isShutdown()) scheduledExecutor = new ScheduledThreadPoolExecutor(1);
	}
	*/
	public void processData(byte[] pData)
	{
		if(!executor.isShutdown()) executor.submit(new CalculationJob(pData, calculationQueue, parameters));
	}
	
	public double[] getFileCoefficients(File file, LPCParameters parameters) throws IOException, InterruptedException, ExecutionException
	{
		if(!executor.isShutdown()) 
		{
			Future<double[]> result = executor.submit(new FileInfoJob(file, parameters));
			
			//for (double iterable_element : result.get()) System.out.print(String.format("%f ", iterable_element));
			
			return result.get();
		}
		return null;
	}
	
	public void newMatch(String droneName)
	{
		decisionQueue.addMatch(droneName);
	}
	
	public void stop()
	{
		executor.shutdown();
		scheduledExecutor.shutdown();
		calculationQueue.deleteObservers();
		/*
		try {
			executor.awaitTermination(500, TimeUnit.MILLISECONDS);
			scheduledExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
}
