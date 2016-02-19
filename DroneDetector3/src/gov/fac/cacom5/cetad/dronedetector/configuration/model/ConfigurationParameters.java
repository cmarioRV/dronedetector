package gov.fac.cacom5.cetad.dronedetector.configuration.model;

import gov.fac.cacom5.cetad.dronedetector.detector.model.LPCParameters;

public class ConfigurationParameters extends LPCParameters {
	
	String email;
	
	public ConfigurationParameters(int p, int offset, int n, int m, double threshold, int matchNumber, String input, String output, String email) {
		super(p, offset, n, m, threshold, matchNumber, input, output);
		this.email = email;
	}
	
	public void setEmail(String pEmail)
	{
		this.email = pEmail;
	}
	
	public String getEmail()
	{
		return email;
	}
}
