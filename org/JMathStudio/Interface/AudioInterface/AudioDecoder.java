package org.JMathStudio.Interface.AudioInterface;

import java.io.File;
import java.io.IOException;

import javax.naming.SizeLimitExceededException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.UnSupportedAudioFormatException;

/**
 * AudioDecoder class provides interface to load an external Audio file and extract multi-channel raw audio data
 * from the same.
 * <p>
 * This class uses Java Sound API to load an external audio file. The audio file types supported by
 * this class depends upon the platform support. Normally following audio file types are supported:
 * <p>
 * -WAVE -AU -SND -AIFF -AIFC
 * <p>This class supports loading of multiple channel audio file with either 8 or 16 bits per sample format.
 * 8 bits per sample format supports both PCM SIGN and UNSIGN format. 16 bits per sample format supports only
 * PCM SIGN format with both Big Endian and Little Endian encoding.
 * <p>The extracted raw audio data for all channels shall be normalised to the range [-1 1).
 * <pre>Usage:
 * 
 * AudioDecoder ad = new AudioDecoder();//Create an instance of AudioDecoder.
 * 
 * AudioBuffer data = ad.decodeAudioData("D:\\in.wav");//Read an external audio file with
 * supported format and decode raw audio data as an {@link AudioBuffer} object.
 * 
 * VectorStack allChannels = data.accessAudioBuffer();//Access multi-channel normalised raw
 * audio data as VectorStack from AudioBuffer object.
 *  
 * Vector channel1 = allChannels.accessVector(0);//Access raw audio data for 1st channel.
 * 
 * AudioEncoder ae = new AudioEncoder(AudioFileFormat.Type.WAVE, data.accessAudioFormat());
 * Create an instance of AudioEncoder with specified audio file type and audio format from last
 * read audio file.
 * 
 * ae.encodeAudioData("D:\\out.wav", allChannels);//Write multi-channel normalised raw audio 
 * data as represented by input VectorStack 'allChannels" to an external audio file with specified 
 * format.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class AudioDecoder {

	/**
	 * This method will load and extract raw audio data from an external audio file whose path is as given by the 
	 * argument 'path' and return the extracted audio data as an {@link AudioBuffer} object.
	 * <p>The decoded raw audio data for any audio format shall be suitably normalised to the range of [-1 1). 
	 * <p>The argument 'path' should provide an absolute full path to an external valid audio file. 
	 * <p>If the platform does not support specified audio file type or the audio file does not follow supported format
	 * or if audio format is PCM UNSIGN for 16 bits sample, this method will throw an UnSupportedAudioFormat Exception. 
	 * <p>If audio file does not exist or in case of any IO problem while parsing the file, method will 
	 * throw an IO Exception.
	 * <p>Further, if the size of file exceeds certain limit, such that the resultant data array cannot hold
	 * all the samples due to limit set by the size of Integer variable, this method will throw an
	 * SizeLimitExceeded Exception.
	 * 
	 * @param String path
	 * @return {@link AudioBuffer}
	 * @throws UnSupportedAudioFormatException
	 * @throws IOException
	 * @throws SizeLimitExceededException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public AudioBuffer decodeAudioData(String path) throws IOException, SizeLimitExceededException, UnSupportedAudioFormatException{
		
		File soundFile = new File(path);
		if(!soundFile.isFile())
			throw new IOException();
		
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e1) {
			throw new UnSupportedAudioFormatException();
		}
	
		AudioFormat audioFormat = audioInputStream.getFormat();
		boolean isPCMUnSign = f7(audioFormat);
		
		if(isPCMUnSign && audioFormat.getSampleSizeInBits() == 16)
			throw new UnSupportedAudioFormatException();
		
		long _frameLength = audioInputStream.getFrameLength();
		int frameSize = (int) audioFormat.getFrameSize();
		
		if(_frameLength < 1 || frameSize < 1)
			throw new UnSupportedAudioFormatException();
		
		if(_frameLength*frameSize >= Integer.MAX_VALUE)
			throw new SizeLimitExceededException();
		
		
		int frameLength = (int) _frameLength;
				
		byte[] buffer = new byte[frameLength * frameSize];

		try {
			audioInputStream.read(buffer);

		} catch (Exception e) {
			throw new IOException();
		}

		VectorStack data = null;

		if (audioFormat.getSampleSizeInBits() == 8) {
			data = f5(buffer, frameLength, audioFormat);

		} else if (audioFormat.getSampleSizeInBits() == 16) {
			data = f0(buffer, frameLength, audioFormat);
		}else{
			throw new UnSupportedAudioFormatException();
		}

		audioInputStream.close();

		return new AudioBuffer(data,audioFormat);
	}

	private VectorStack f5(byte[] buffer,int frameLength, AudioFormat format){

		int numChannels = format.getChannels();
		Vector[] channels = new Vector[numChannels];

		for(int i=0;i<channels.length;i++)
			channels[i] = new Vector(frameLength);

		int sampleIndex = 0;
		final float norm = 128;
		
		boolean isPCMUnSign = f7(format);
		byte sample;
		if(!isPCMUnSign){
			//Data is in range of [-128 127] with centre at 0.
			for (int t = 0; t < buffer.length;) {
				for (int channel = 0; channel < numChannels; channel++) {
					sample = buffer[t++];
					//-128 to 127 as it is.
					channels[channel].setElement(sample/norm,sampleIndex);
				}
				sampleIndex++;
			}
		}else{
			//Data is actually in range of [0 255] with centre at 128.
			//However, for byte the range 128 to 255 gets mapped to -128 -1.
			for (int t = 0; t < buffer.length;) {
				for (int channel = 0; channel < numChannels; channel++) {
					sample = buffer[t++];
					//0 127 128 255 which is mapped as 0 127 -128 -1 should be shifted to
					//proper range in -128 127.
					channels[channel].setElement(f9(sample)/norm,sampleIndex);
				}
				sampleIndex++;
			}
		}

		VectorStack data = new VectorStack();

		for(int i=0;i<channels.length;i++)
			data.addVector(channels[i]);

		return data;
	}

	private VectorStack f0(byte[] buffer,int frameLength, AudioFormat format){

		int numChannels = format.getChannels();
		Vector[] channels = new Vector[numChannels];

		for(int i=0;i<channels.length;i++)
			channels[i] = new Vector(frameLength);

		int sampleIndex = 0;

		final float norm = 32768;
		int sample;

		if(format.isBigEndian()){
			for (int t = 0; t < buffer.length;) {
				for (int channel = 0; channel < numChannels; channel++) {
					int first = (int) buffer[t++];
					int second = (int) buffer[t++];
					sample = f2(first, second);
					channels[channel].setElement(sample/norm,sampleIndex);
				}
				sampleIndex++;
			}
		}else{
			for (int t = 0; t < buffer.length;) {
				for (int channel = 0; channel < numChannels; channel++) {
					int first = (int) buffer[t++];
					int second = (int) buffer[t++];
					sample = f2(second, first);
					channels[channel].setElement(sample/norm,sampleIndex);
				}
				sampleIndex++;
			}
		}

		VectorStack data = new VectorStack();

		for(int i=0;i<channels.length;i++)
			data.addVector(channels[i]);

		return data;
	}

	//Return [-128 127] for [0 127 128 255] encoded as [0 127 -128 -1].
	private int f9(byte b){
		if(b >= 0)
			return b - 128;//Convert [0 127] -> [-128 -1]
		else
			return b + 128;//Convert [-128 -1] -> [0 127]
	}

	private int f2(int high, int low) {
		return (high << 8) + (low & 0x00ff);
	}	
	
	private boolean f7(AudioFormat format){
		return format.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED;
	}
}
