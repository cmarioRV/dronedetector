package gov.fac.cacom5.cetad.dronedetector.detector.model;

//import gov.fac.cacom5.cetad.dronedetector.model.MathematicalParameters;

public abstract class LPCParameters/* extends MathematicalParameters*/ {

	protected int P, N, M, offset, matchNumber;
	protected double threshold;
	protected String output;
	protected String input;
	
	public LPCParameters(int p, int offset, int n, int m, double threshold, int pMatchNumber, String input, String output) {
		//super(p, n, m, threshold, input, output);
		this.P = p;
		this.offset = offset;
		this.N = n;
		this.M = m;
		this.input = input;
		this.output = output;
		this.threshold = threshold;
		this.matchNumber = pMatchNumber;
	}

	public void setP(int pP)
	{
		this.P = pP;
	}
	
	public void setOffset(int pOffset)
	{
		this.offset = pOffset;
	}
	
	public void setN(int pN)
	{
		this.N = pN;
	}
	
	public void setM(int pM)
	{
		this.M = pM;
	}
	
	public void setThreshold(double threshold)
	{
		this.threshold = threshold;
	}
	
	public void setInput(String pIntput)
	{
		this.input = pIntput;
	}
	
	public void setOutput(String pOutput)
	{
		this.output = pOutput;
	}
	
	public void setMatchNumber(int pMatchNumber)
	{
		this.matchNumber = pMatchNumber;
	}
	
	public int getP()
	{
		return P;
	}
	
	public int getOffset()
	{
		return offset;
	}
	
	public int getN()
	{
		return N;
	}
	
	public int getM()
	{
		return M;
	}
	
	public double getThreshold()
	{
		return threshold;
	}
	
	public int getMatchNumber()
	{
		return matchNumber;
	}
	
	public String getInput()
	{
		return input;
	}
	
	public String getOutput()
	{
		return output;
	}
}
