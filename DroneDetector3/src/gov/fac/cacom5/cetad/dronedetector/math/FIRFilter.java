package gov.fac.cacom5.cetad.dronedetector.math;

public abstract class FIRFilter {
	private static int length = 0;
	private static double[] coefficients;
	private static double[] delayLine;
	private static int count = 0;
	
	/**
	 * Initialize FIRFilter
	 * @param coeffs Impulse response filter coefficients
	 */
	public FIRFilter(double[] coeffs) {
		FIRFilter.length = coeffs.length;
		FIRFilter.coefficients = coeffs;
		FIRFilter.delayLine = new double[FIRFilter.length];
	}
	
	public static double getOutputSample(double inputSample)
	{
		delayLine[count] = inputSample;
		double result = 0.0;
		int index = count;
		for (int i=0; i<length; i++) {
			result += coefficients[i] * delayLine[index--];
			if (index < 0) index = length-1;
		}
		if (++count >= length) count = 0;
		return result;
	}
	
	public static double[] getOutputSamples(Double[] inputSamples)
	{
		double[] result = new double[inputSamples.length];
		
		for (int i = 0; i < inputSamples.length; i++) {
			result[i] = getOutputSample(inputSamples[i]);
		}
		
		return result;
	}
}
