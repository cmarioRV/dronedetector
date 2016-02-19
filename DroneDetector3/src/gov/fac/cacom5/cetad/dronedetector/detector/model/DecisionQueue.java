package gov.fac.cacom5.cetad.dronedetector.detector.model;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.concurrent.Semaphore;

public class DecisionQueue extends Observable {

	private final Semaphore semaphore;
	private int matchNumberThreshold;
	private Hashtable<String, Integer> count;
	
	public DecisionQueue(int pMatchNumberThreshold) {
		this.semaphore = new Semaphore(1); 
		this.matchNumberThreshold = pMatchNumberThreshold;
		count = new Hashtable<>();
	}
	
	public void addMatch(String droneName)
	{
		try 
		{
			this.semaphore.acquire();

			Integer previousValue = count.get(droneName);
			count.put(droneName, previousValue == null ? 1 : previousValue + 1);
			
			//this.matchCounter++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			this.semaphore.release();
		}
	}
	
	public void takeDecision()
	{
		try {
			this.semaphore.acquire();
			
			for (Entry<String, Integer> entry : count.entrySet()) {
				if(entry.getValue() >= this.matchNumberThreshold)
				{
					setChanged();
		            notifyObservers(entry.getKey());
		            entry.setValue(0);
		            break;
				}
				entry.setValue(0);
			}
			/*
			if(this.matchCounter >= this.matchNumberThreshold)
			{
				setChanged();
	            notifyObservers();
			}
			this.matchCounter = 0;
			*/
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			this.semaphore.release();
		}
	}
}
