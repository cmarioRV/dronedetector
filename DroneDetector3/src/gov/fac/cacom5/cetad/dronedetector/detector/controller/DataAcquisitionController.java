package gov.fac.cacom5.cetad.dronedetector.detector.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class DataAcquisitionController {
	
	DetectorController parentController;
	
	//Variable for stopping Capture process
	public boolean enableCapture = true;
	
	//Declare executor
	ThreadPoolExecutor executor;
	
	//Declare Listener for line events
	MicrophoneListener microphoneListener;
	
	AudioCapture audioCapture;
	
	public DataAcquisitionController(DetectorController pParentController) {
		parentController = pParentController;
		executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
		microphoneListener = new MicrophoneListener();
	}
	
	public void startCapture(int pBufferSize) throws LineUnavailableException, IllegalArgumentException
	{
		audioCapture = new AudioCapture(pBufferSize * 5);
		executor.execute(audioCapture);
	}
	
	public void stopCapture()
	{
		enableCapture = false;
		System.out.print("Stopping...\n");
		executor.shutdown();
		try {
			executor.awaitTermination(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(audioCapture != null)
			{
				audioCapture.closeLine();
			}
		}
	}

	private class MicrophoneListener implements LineListener
	{

		@Override
		public void update(LineEvent e) {
			if(e.getType().equals(LineEvent.Type.CLOSE))
			{
				parentController.lineIsClosed();
				System.out.print("Line is closed\n");
			}
		}
		
	}
	/*
	encoding - the audio encoding technique 
	sampleRate - the number of samples per second 
	sampleSizeInBits - the number of bits in each sample 
	channels - the number of channels (1 for mono, 2 for stereo, and so on) 
	frameSize - the number of bytes in each frame 
	frameRate - the number of frames per second 
	bigEndian - indicates whether the data for a single sample is stored in big-endian byte order (false means little-endian)  
	 */
	public class AudioCapture implements Runnable
	{
		float sampleRate = 44100;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		//AudioFormat audioFormat = new AudioFormat(Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
		AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		DataLine.Info info;
		TargetDataLine microphone;
		int bufferSize;
		
		public AudioCapture(int pBufferSize) throws LineUnavailableException
		{
			bufferSize = pBufferSize;
			info = new DataLine.Info(TargetDataLine.class, audioFormat); // format is an AudioFormat object
			if (!AudioSystem.isLineSupported(info)) {
				throw new LineUnavailableException();
			}
			
			microphone = (TargetDataLine) AudioSystem.getLine(info);
			//microphone.open(audioFormat, bufferSize);
			microphone.open(audioFormat);
			microphone.addLineListener(microphoneListener);
		}

		@Override
		public void run() 
		{
			byte[] data = new byte[microphone.getBufferSize() / 5];
			//byte[] data = new byte[microphone.getBufferSize()];
			microphone.start();
			
			while (enableCapture) 
			{
			   // Read the next chunk of data from the TargetDataLine.
			   microphone.read(data, 0, data.length);
			   parentController.processDataBuffer(data.clone());
			} 
		}
		
		public void closeLine()
		{
			System.out.print("Closing line...\n");
			//microphone.drain();
			microphone.stop();
			microphone.close();
			microphone.flush();
			microphone = null;
		}
	}
}
