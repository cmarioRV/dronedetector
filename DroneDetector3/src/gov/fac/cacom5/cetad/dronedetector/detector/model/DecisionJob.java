package gov.fac.cacom5.cetad.dronedetector.detector.model;

public class DecisionJob implements Runnable {

	DecisionQueue decisionQueue;
	
	public DecisionJob(DecisionQueue pDecisionQueue) {
		this.decisionQueue = pDecisionQueue;
	}

	@Override
	public void run() {
		decisionQueue.takeDecision();
	}
}
