package gov.fac.cacom5.cetad.dronedetector.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gov.fac.cacom5.cetad.dronedetector.detector.controller.DetectorController;

public class Estimator{

	ThreadPoolExecutor executor;
	ScheduledThreadPoolExecutor scheduledExecutor;
	CalculationQueue calculationQueue;
	DetectDrone detectDrone;
	private Hashtable<Integer, Vector<Double>> correlations;
	private final Semaphore semaphore;
	
	public Estimator(DetectorController pParentController, Integer[] pIds, ArrayList<double[]> pListOfDroneData, double pThreshold) {
		semaphore = new Semaphore(1);
		executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
		correlations = new Hashtable<Integer, Vector<Double>>();
		calculationQueue = new CalculationQueue(pListOfDroneData);
		detectDrone = new DetectDrone(pThreshold);
		detectDrone.addObserver(pParentController);
		scheduledExecutor = new ScheduledThreadPoolExecutor(1);
		scheduledExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		scheduledExecutor.scheduleAtFixedRate(detectDrone, 1000, 1000, TimeUnit.MILLISECONDS);
		
	}
	
	public void addData(double[] pData)
	{
		if(!executor.isShutdown())
			try {
				executor.submit(new CoefficientsCalculator(calculationQueue, pData));
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
			}
	}
	
	public void stop()
	{
		executor.shutdown();
		scheduledExecutor.shutdown();
	}

	class CoefficientsCalculator implements Callable<double[]>
	{
		private CalculationQueue calculationQueue;
		
		public CoefficientsCalculator(CalculationQueue pCalculationQueue, double[] pData) {
			this.calculationQueue = pCalculationQueue;
			this.calculationQueue.addNewRealTimeData(pData);
		}

		@Override
		public double[] call() throws Exception {
			calculationQueue.process();
			return null;
		}
	}
	
	class CalculationQueue
	{
		//This Semaphore will keep track of no. of printers used at any point of time.
	    //private final Semaphore semaphore;
	     	    
	    
	    private double[] rtData;
	    private ArrayList<double[]> listOfDronesData;
	    
	    public CalculationQueue(ArrayList<double[]> pListOfDronesData) {
	    	this.listOfDronesData = pListOfDronesData;
	    	//correlations = new Hashtable<Integer, Vector<Double>>(this.listOfDronesData.size());
			//semaphore = new Semaphore(1);
		}
	    
	    public void addNewRealTimeData(double[] pData)
	    {
	    	try 
	    	{
				semaphore.acquire();
				rtData = pData.clone();
				
			} catch (InterruptedException e) 
	    	{
				e.printStackTrace();
			}
	    	finally 
	    	{
	    		semaphore.release();
			}
	    }
	    
	    public void process()
	    {
	    	try {
				semaphore.acquire();
				calculateCorrelationVectors();
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	finally {
				semaphore.release();
			}
	    }
	    
	    private void calculateCorrelationVectors()
		{
			double x = 0;
			double y = 0;
			double result = 0;
			double x_pow = 0;
			double y_pow = 0;
			double xdoty = 0;
			
			for (int j = 0; j < listOfDronesData.size(); j++)
			{
				double[] sampleStored = listOfDronesData.get(j);
				if(sampleStored.length <= rtData.length)
				{
					for(int i = 0; i < sampleStored.length; i++)
					{
						x += sampleStored[i];
						y += rtData[i];
						x_pow += Math.pow(sampleStored[i], 2);
						y_pow += Math.pow(rtData[i], 2);
						xdoty += sampleStored[i] * rtData[i];
					}
					
					int n = sampleStored.length;
	
					result = ((n * xdoty) - (x * y)) / (Math.sqrt((n * x_pow) - Math.pow(x, 2)) * Math.sqrt((n * y_pow) - Math.pow(y, 2)));
				}
				else
				{	
					for(int i = 0; i < rtData.length; i++)
					{
						x += sampleStored[i];
						y += rtData[i];
						x_pow += Math.pow(sampleStored[i], 2);
						y_pow += Math.pow(rtData[i], 2);
						xdoty += sampleStored[i] * rtData[i];
					}
					
					int n = rtData.length;
					result = ((n * xdoty) - (x * y)) / (Math.sqrt((n * x_pow) - Math.pow(x, 2)) * Math.sqrt((n * y_pow) - Math.pow(y, 2)));
				}
				
				if(correlations.get(j) == null)
				{
					Vector<Double> vector = new Vector<>();
					vector.addElement(result);
					correlations.put(j, vector);
				}
				else
				{
					correlations.get(j).add(result);
				}
				
				x = 0;
				y = 0;
				x_pow = 0;
				y_pow = 0;
				xdoty = 0;
			}
		}
	}
	
	class DetectDrone extends Observable implements Runnable
	{
		double threshold;
		public DetectDrone(double pThreshold) {
			this.threshold = pThreshold;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire();
				detect();
				//System.out.print("Detect drone Task" + "\n");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				semaphore.release();
			}
			
		}
		
		private void detect()
		{
			System.out.print("\n");
			
			for (int i = 0; i < correlations.values().size(); i++)
			{
				double sum = 0;
				double aux = 0;
				Vector<Double> vector = correlations.get(i);
				for (Double item : vector)
				{
					//aux =  Math.abs(item.doubleValue());
					aux =  item.doubleValue();
					if(!Double.isNaN(aux))
					{
						sum += aux;
					}
				}

				sum = sum / vector.size();

				if(sum > threshold)
				{
					//int response = i; 
					//setChanged();
		            //notifyObservers(response);
				}
				
				System.out.print("Correlation[" + String.valueOf(i) + "]: " + String.format("%.2f", sum) + "\n");
				
				vector.clear();
			}
		}
	}
}
