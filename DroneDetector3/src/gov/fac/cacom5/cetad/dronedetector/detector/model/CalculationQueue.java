package gov.fac.cacom5.cetad.dronedetector.detector.model;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class CalculationQueue extends Observable
{    	
	private final Semaphore semaphore;
	Hashtable<String, double[]> dronesArrayH;
	private Hashtable<String, Vector<Double>> correlationsH;
	private int offset = 0;
	//private int offset = 5;
	Covariance covariance = new Covariance();
	
	public CalculationQueue(Hashtable<String, double[]> dronesArray, int pOffset) {
		this.semaphore = new Semaphore(1); 
		this.dronesArrayH = dronesArray;
		correlationsH = new Hashtable<String, Vector<Double>>();
		this.offset = pOffset;
	}
	
	public void process(double[] data)
	{
		try {
			semaphore.acquire();
			calculateCorrelationVectors(data);			
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
	
	private void calculateCorrelationVectors(double[] rtDataComplete)
	{
		double x = 0;
		double y = 0;
		double result = 0;
		double x_pow = 0;
		double y_pow = 0;
		double xdoty = 0;
		//double[] rtData = Arrays.copyOfRange(rtDataComplete, offset, rtDataComplete.length);
		double[] rtData = Arrays.copyOfRange(rtDataComplete, 0, (rtDataComplete.length >= offset) ? offset : rtDataComplete.length);

		//for (int j = 0; j < dronesArrayH.values().size(); j++)
		for (String key : dronesArrayH.keySet())
		{
			//double[] sampleStored = Arrays.copyOfRange(dronesArrayH.get(key), offset, dronesArrayH.get(key).length);
			double[] sampleStored = Arrays.copyOfRange(dronesArrayH.get(key), 0, (dronesArrayH.get(key).length >= offset) ? offset : dronesArrayH.get(key).length);
			
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
				
				result = covariance.covariance(sampleStored, rtData);
				StandardDeviation standardDeviation = new StandardDeviation();
				double st1 = standardDeviation.evaluate(sampleStored);
				double st2 = standardDeviation.evaluate(rtData);
				result = result / (st1 * st2);
				
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
			
			//if(correlations.get(j) == null)
			if(correlationsH.get(key) == null)
			{
				Vector<Double> vector = new Vector<>();
				vector.addElement(result);
				//correlations.put(j, vector);
				correlationsH.put(key, vector);
			}
			else
			{
				//correlations.get(j).add(result);
				correlationsH.get(key).add(result);
			}
			
			x = 0;
			y = 0;
			x_pow = 0;
			y_pow = 0;
			xdoty = 0;
		}
		

		/*
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
	    String strDate = sdf.format(now);
		System.out.print(strDate + "\n");
		*/
	}
	
	public void detect(double threshold)
	{
		System.out.print("\n");
		try {
			semaphore.acquire();
			//for (int i = 0; i < correlations.values().size(); i++)
			for (String key : correlationsH.keySet())
			{
				double sum = 0;
				double aux = 0;
				//Vector<Double> vector = correlations.get(i);
				Vector<Double> vector = correlationsH.get(key);
				for (Double item : vector)
				{
					aux =  Math.abs(item.doubleValue());
					//aux =  item.doubleValue();
					if(!Double.isNaN(aux))
					{
						sum += aux;
					}
				}

				sum = sum / vector.size();

				if(sum > threshold)
				{
					//int response = i;
					String response = key;
					setChanged();
		            notifyObservers(response);
				}
				
				System.out.print(key + " : " + String.format("%.2f", sum) + "\n");
				
				//vector.clear();
				correlationsH.get(key).clear();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		
	}
}
