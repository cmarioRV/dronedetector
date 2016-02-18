package gov.fac.cacom5.cetad.dronedetector.detector.model;

public class DetectionJob implements Runnable {
	
	private double threshold;
	CalculationQueue calculationQueue;
	
	public DetectionJob(CalculationQueue calculationQueue, double threshold) {
		this.threshold = threshold;
		this.calculationQueue = calculationQueue;
	}

	@Override
	public void run() {
		calculationQueue.detect(threshold);
	}
}
