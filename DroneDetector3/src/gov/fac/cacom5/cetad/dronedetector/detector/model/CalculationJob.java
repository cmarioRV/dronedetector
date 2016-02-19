package gov.fac.cacom5.cetad.dronedetector.detector.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CalculationJob implements Callable<double[]>
{	
	private CalculationQueue calculationQueue;
	private byte[] data;
	private double[] coefficientsAverage;
    private ArrayList<double[]> coefficientsArray;
	
	private String input;
	private File file;
    private int p;
    private double[] e;         
    private double[][] alpha;
    private double[] r;         // Auto-correlation values
    private double[] k;         // PARCOR coefficients
    private double[] c;         // Cepstral coefficients
    private Vector<Double> s;
    //private Vector in;
    
    private double[] x;
    private double[] lpc;       // LP coefficients
    private int N;              // The length of a frame
    private int M;
    
    //private Vector rawInput;
    private Vector<Double> rawInput;
    private double normalizationFactor = 15000;
    
    private LPCParameters parameters;
	
	public CalculationJob(byte[] data, CalculationQueue queue, LPCParameters parameters) {
		this.calculationQueue = queue;
		this.data  = data;
		this.parameters = parameters;
		rawInput = new Vector<Double>();
	}

	@Override
	public double[] call() throws Exception {
		
		if(this.data != null)
		{		
			//Formating the input data for best results
			//start();
			printData();
			
			
			
			double[] sfad = BandpassFilter.filter((Double[])rawInput.toArray(new Double[rawInput.size()]));
			
			for (int i = 0; i < sfad.length; i++) {
				rawInput.set(i, sfad[i]);
			}
			
			preProcess();
			
			//Start LPC Calculation
			initialize(parameters);
			//readSamples();
			process(((s.size()-N)/M)+1);
			calculateAverage();
			
			//Drone detection		
			/*
			System.out.format(Locale.FRANCE, "%f %f %f %f %f %f %f %f %f %f %f %f %f %f %f %n", 
			coefficientsAverage[1],
			coefficientsAverage[2],
			coefficientsAverage[3],
			coefficientsAverage[4],
			coefficientsAverage[5],
			coefficientsAverage[6],
			coefficientsAverage[7],
			coefficientsAverage[8],
			coefficientsAverage[9],
			coefficientsAverage[10],
			coefficientsAverage[11],
			coefficientsAverage[12],
			coefficientsAverage[13],
			coefficientsAverage[14],
			coefficientsAverage[15]);
			*/
			calculationQueue.process(Arrays.copyOfRange(coefficientsAverage, 1, coefficientsAverage.length));
			//calculationQueue.process(coefficientsAverage);
		}	
		
		return null;
	}

	/*
	private void start()
	{	
		int times = Double.SIZE / Byte.SIZE;
	    double[] doubles = new double[data.length / times];
	    for(int i=0;i<doubles.length;i++){
	        rawInput.addElement(ByteBuffer.wrap(data, i*times, times).getDouble());
	    }
	}
	*/
	/**
     * Method to record the data section of a WAV file. In a WAV format, the higher byte 
     * comes second. So, the higher byte is read second into <code>buffer[1]</code>. The lower byte
     * which is read first is then masked with <code>0x000000FF</code> for the lower
     * eight bits(removing the sign bit). The 16-bit data value is constructed by 
     * shifting the higher byte by 8 bits and then ORing with the lower byte.
     */
    private void printData(){
        try {
        	byte[] buffer = new byte[2];
        	int temp = 0;
        	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.data);
        	BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);
            while (bufferedInputStream.read(buffer) >= 0) {
                temp = 0;
                temp = buffer[1];
                temp <<= 8;
                temp |= (0x000000FF & buffer[0]);
                rawInput.addElement(new Double(temp));
            }
            
            buffer = null;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
	
	 /**
     * Performs DC-shift and normalization on the speech sample.
     */
    private void preProcess(){
        double dc = 0;

        // Perform DC Shift
        for(int i=0;i<rawInput.size();i++)
            dc += ((Double) rawInput.elementAt(i)).doubleValue();
        dc = dc/rawInput.size();
        
        if(dc != 0){
            for(int i=0;i<rawInput.size();i++)
            	rawInput.set(i,((Double) rawInput.elementAt(i)) - dc);
        }
        
        // Normalize
        normalize();
    }
	
    /**
     * Method to normalize the input sample values before processing for LPC
     */
    private void normalize(){
        double max = Math.abs(((Double) rawInput.firstElement()).doubleValue());
        double tmp;
        for(int i=1;i<rawInput.size();i++){
            tmp = Math.abs(((Double)rawInput.elementAt(i)).doubleValue());
            if(max < tmp)
                max = tmp;
        }
        for(int i=0;i<rawInput.size();i++){
            tmp = ((Double) rawInput.elementAt(i)).doubleValue();
            tmp = (tmp/max) * normalizationFactor;
            rawInput.setElementAt(tmp, i);
        }
    }
	
	
	/**
     * Initialization method for LPCAnalyzer
     * @param p         The oreder in LPC
     * @param M         The shift between successive frames
     * @param N         The length of a frame
     * @param output    The name of the file where cepstral coefficients are to be recorded
     * @author CETAD - CACOM5 - FAC
     */
    public void initialize(LPCParameters parameters){
        this.p = parameters.getP(); // order
        this.M = parameters.getM(); // shift
        this.N = parameters.getN(); // frame length
        
        // Variable used in Durbin's algorithm
        e = new double[p+1];
        alpha = new double[p+1][p+1];
        r = new double[p+1];
        k = new double[p+1];
        c = new double[p+1];
        //s = new Vector();
        s = rawInput;
        //in = new Vector();
        
        x = new double[N];
        lpc = new double[p];
        
        coefficientsArray = new ArrayList<>();
        coefficientsAverage = new double[p+1];
    }
	
	/**
     * Reads the values stored in <code>data</code> variable
     * @author FAC - CACOM5 - CETAD
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     */
    public void readSamples() throws UnsupportedAudioFileException, IOException
    {
    	if(data != null)
    	{
	    	for (double element : data) {
	    		s.addElement(element);
			}
    	}
    	else if(file != null)
    	{
    		AudioInputStream ais = null;
    		ais = AudioSystem.getAudioInputStream(file);
            data = new byte[ais.available()];
            ais.read(data);

        	for (double b : data) {
    			s.addElement(b);
    		}
    	}
    	else if(input != null)
    	{
    		BufferedReader bfr = new BufferedReader(new FileReader(input));
            String temp;
            while(bfr.ready()){
                temp = bfr.readLine();
                s.addElement(Double.parseDouble(temp));
            }
            
            bfr.close();
    	}
    	else throw new IOException("No ha establecido los datos a procesar");
    }
    
    /**
     * The Linear Predictive Analysis of a speech signal with <code>frame</code> frames.
     * @param frames    The number of frames to be used in LPC
     */
    private void process(int frames){
        for(int l=0;l<frames;l++){
            //initialize();
            applyWindow(l);
            autoCorrelate();
            if(r[0] == 0){
                System.err.println("A unique solution does not exist");
                System.exit(-1);
            }
            else{
                e[0] = r[0];
            }
            LPCAnalysis();
            extractSolution();
            calculateCepstralCoefficients();
            
            coefficientsArray.add(c);
            //coefficientsArray.add(lpc);
            /*
            if(l % 8 == 0)
            {
            	calculateAverage();
            }*/
            //writeResult();
            //writeResultLPCCoeffs();
            /*
            System.out.format(Locale.FRANCE, "%f %f %f %f %f %f %f %f %f %f %f %f %f %f %f %n", 
        			c[1],
        			c[2],
        			c[3],
        			c[4],
        			c[5],
        			c[6],
        			c[7],
        			c[8],
        			c[9],
        			c[10],
        			c[11],
        			c[12],
        			c[13],
        			c[14],
        			c[15]);
            */
            /*
            System.out.format(Locale.FRANCE, "%f %f %f %f %f %f %f %f %f %f %f %f %f %f %f %n", 
            		lpc[0],
        			lpc[1],
        			lpc[2],
        			lpc[3],
        			lpc[4],
        			lpc[5],
        			lpc[6],
        			lpc[7],
        			lpc[8],
        			lpc[9],
        			lpc[10],
        			lpc[11],
        			lpc[12],
        			lpc[13],
        			lpc[14]);
        	*/
        }
    }
    
    /**
     * Applies Hamming window to the frame numbered <code>L</code>
     * @param L The frame number 0, 1, 2...
     */
    private void applyWindow(int L)
    {
        int n;
        for(int i = 0;i < N;i++)
        {
            n = M*L + i;
            x[i] = ((Double ) s.elementAt(n)).doubleValue() * hammingWindow((double) i);
        }
    }
    
    /**
     * Performs calculations of the Hamming function for a given <code>n</code>
     * @param n The value for which the Hamming function has to be calculated
     * @return  The value of the evaluated Hamming function for the given value of n
     */
    private double hammingWindow(double n){
        return 0.54 - 0.46*Math.cos(2*Math.PI*n/(N-1));
    }
    
    /**
     * Auto-correlation method
     */
    private void autoCorrelate(){
        int i,j;
        for(i=0;i<p+1;i++){
            r[i] = 0;
            for(j=0;j<N-i;j++){
                r[i] += (x[j]*x[j+i]);
            }
        }
    }
    
    /**
     * LPC Analysis using Durbin's algorithm
     */
    private void LPCAnalysis(){
        int i,j;
        double sum;
        for(i = 1;i <= p;i++){
            sum = 0;
            for(j = 1;j <= i-1;j++){
                sum += (alpha[j][i-1]*r[i-j]);
            }
            k[i] = (r[i] - sum)/e[i-1];
            alpha[i][i] = k[i];
            for(j=1;j<=i-1;j++){
                alpha[j][i] = alpha[j][i-1] - k[i]*alpha[i-j][i-1];
            }
            e[i] = (1 - k[i]*k[i]) * e[i-1];
        }
    }
    
    /**
     * Records the solutions in a separate array to be used for calculating
     * cepstral coefficients
     */
    private void extractSolution()
    {
        for(int i=0;i<p;i++){
            lpc[i] = alpha[i+1][p];
        }
    }
    
    /**
     * Calculating cepstral coefficients from the linear predicitve coefficients
     */
    private void calculateCepstralCoefficients(){
        int i,j;
	double sum;
	for(i=0;i<c.length;i++){
            c[i] = 0;
	}
	for(i=1;i<c.length;i++){
            sum = 0;
            for(j=1;j<=i-1;j++){
                sum += ((j/(double) i)*c[j]*lpc[i-j-1]);
            }
            c[i] = lpc[i-1] + sum;
		}
    }
    
    private void calculateAverage()
    {
    	for (int j = 0; j < coefficientsArray.size(); j++) {
    		
    		double[] coeffs = coefficientsArray.get(j);
    		
			for(int i = 0; i < coeffs.length; i++)
			{
				coefficientsAverage[i] += coeffs[i];
			}
		}
    	
    	for (int i = 0; i < coefficientsAverage.length; i++) {
    		coefficientsAverage[i] = coefficientsAverage[i] / (coefficientsArray.size() + 1);
		}
    	
    	coefficientsArray.clear();
    }
}