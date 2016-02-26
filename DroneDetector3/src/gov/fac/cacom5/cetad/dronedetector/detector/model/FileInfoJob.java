package gov.fac.cacom5.cetad.dronedetector.detector.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.Callable;

import gov.fac.cacom5.cetad.dronedetector.utils.Wav2TextConverter;

public class FileInfoJob implements Callable<double[]> {

	private Wav2TextConverter wav2TextConverter;

	private double[] coefficientsAverage;
    private ArrayList<double[]> coefficientsArray;
	
    private int p;
    private double[] e;         
    private double[][] alpha;
    private double[] r;         // Auto-correlation values
    private double[] k;         // PARCOR coefficients
    private double[] c;         // Cepstral coefficients
    private Vector<Double> s;
    
    private double[] x;
    private double[] lpc;       // LP coefficients
    private int N;              // The length of a frame
    private int M;
    
    private BufferedWriter bfwr;
    
    private LPCParameters parameters;
	
	public FileInfoJob(File file, LPCParameters parameters) throws IOException {
		this.wav2TextConverter = new Wav2TextConverter(file.getAbsolutePath(), "resources/outputWav2Text.txt");
		this.parameters = parameters;
	}
	
	@Override
	public double[] call() throws Exception {
		this.wav2TextConverter.convert();
		initialize(parameters);
		readSamples("resources/outputWav2Text.txt");
		process(((s.size()-N)/M)+1);
		bfwr.close();
		calculateAverage();
		return Arrays.copyOfRange(coefficientsAverage, 1, coefficientsAverage.length);
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
        s = new Vector<Double>();
        
        x = new double[N];
        lpc = new double[p];
        
        coefficientsArray = new ArrayList<>();
        coefficientsAverage = new double[p+1];
        
        try {
            bfwr = new BufferedWriter(new FileWriter(parameters.getOutput()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Reads the sample values from the output of Wav2TextConverter.
     * @param filename  The name of the file containing the sample values
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    private void readSamples(String filename) throws FileNotFoundException, IOException{
        BufferedReader bfr = new BufferedReader(new FileReader(filename));
        String temp;
        while(bfr.ready()){
            temp = bfr.readLine();
            s.addElement(Double.parseDouble(temp));
        }
        bfr.close();
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
            }
            */
            //writeResult();
            //writeResultLPCCoeffs();
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
    
    /**
     * Writes or records the linear predicitve cepstral coefficients
     */
    @SuppressWarnings("unused")
	private void writeResult(){
        try {
            for (int i = 1; i < c.length; i++) {
                bfwr.write(String.format("%f ", c[i]));
            }
            bfwr.write("\n");
            bfwr.flush();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
