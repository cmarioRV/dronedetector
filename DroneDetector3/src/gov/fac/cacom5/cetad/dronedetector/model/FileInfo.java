package gov.fac.cacom5.cetad.dronedetector.model;

import javax.sound.sampled.AudioFormat;

public class FileInfo {
	
	double[] coefficients;
	AudioFormat audioFormat;
	String name;
	
	public FileInfo(double[] coefficients, AudioFormat audioFormat, String name) {
		this.coefficients = coefficients;
		this.audioFormat = audioFormat;
		this.name = name;
		
	}

	public String getName() {
		return name;
	}
	
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}
	
	public double[] getCoefficients() {
		return coefficients;
	}
	
	public void setCoefficients(double[] coefficients) {
		this.coefficients = coefficients;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAudioFormat(AudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}
}
