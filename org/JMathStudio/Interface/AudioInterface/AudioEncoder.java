package org.JMathStudio.Interface.AudioInterface;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat.Type;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.UnSupportedAudioFormatException;

/**
 * AudioEncoder class provides interface to write multi-channel normalised (range limited to [-1 1) ) 
 * raw audio data to an external Audio file with supported Audio format.
 * <p>
 * This class uses Java Sound API to write raw data to an external audio file. The audio file types supported 
 * by this class for write operation depends upon the platform support. Normally following audio file types 
 * are supported:
 * <p>
 * -WAVE -AU -SND -AIFF -AIFC
 * <p>However, use {@link #getAudioTypesSupported()} to get the list of Audio file types supported by the
 * platform for write operation.
 * <p>This class supports writing of multiple channel raw audio data with either 8 or 16 bits per sample format.
 * 8 bits per sample format supports both PCM SIGN and UNSIGN format. 16 bits per sample format supports only
 * PCM SIGN format with both Big Endian and Little Endian encoding.
 * <p><b>CAUTION: Ensure that specified Audio format is consistent with Audio file type. Do not pass Audio format
 * from one file type (say WAVE) for writing data to another file type (say AU). Results are unspecified.</b>
 * <pre>Usage:
 * See Usage for {@link AudioDecoder}.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public class AudioEncoder implements Runnable {
	
	private final AudioFileFormat.Type type;
	private final AudioFormat format;

	private PipedOutputStream pos = null;
	private PipedInputStream pis = null;
	private AudioInputStream ais = null;
	private File file = null;
	private Thread helper = null;


	/**
	 * This will create an AudioEncoder object for audio file type as specified by the argument {@link AudioFileFormat.Type} 
	 * 'type' and audio format as specified by argument {@link AudioFormat} 'format' respectively; to write multi-channel 
	 * normalised raw audio data to an external audio file with specified audio file type and audio format.
	 * <p>If the audio format does not supports either 8 or 16 bits per sample format or if audio format
	 * is 16 bits per sample with PCM UNSIGN, this method will throw an UnSupportedAudioFormat Exception. 
	 * @param AudioFileFormat.Type type
	 * @param AudioFormat format
	 * @throws UnSupportedAudioFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AudioEncoder(AudioFileFormat.Type type, AudioFormat format) throws UnSupportedAudioFormatException{

		this.type = type;

		if(format.getSampleSizeInBits() == 8 || format.getSampleSizeInBits() == 16){
			if(format.getSampleSizeInBits() == 16 && f2(format))
				throw new UnSupportedAudioFormatException();
			else
				this.format = format;
		}
		else
			throw new UnSupportedAudioFormatException();
	}

	@SuppressWarnings("deprecation")
	private void f3(File file) throws IOException{
		//Ensure we write data to the current file.
		this.file = file;
		// Write to the output stream
		pos = new PipedOutputStream();

		// It will then go to the file via the input streams
		pis = new PipedInputStream(pos);
		ais = new AudioInputStream(pis, format, AudioSystem.NOT_SPECIFIED);

		if(helper != null){
			helper.stop();
			helper = null;
		}

		helper = new Thread(this);
		helper.start();
		//new Thread(this).start();
	}

	private void f7(byte[] bytes) throws IOException{
		// write it
		if(pos != null)
			pos.write(bytes, 0, bytes.length);

		pos.flush();
	}

	@SuppressWarnings("deprecation")
	private void f0() throws IOException{
		if(pos != null) {
			ais.close();
			pis.close();
			pos.close();
		}

		if(helper != null){
			helper.stop();
			helper = null;
		}
	}

	public void run() {
		try {
			AudioSystem.write(ais, type, file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will write the multi-channel normalised raw audio data as represented by the {@link VectorStack}
	 * 'data' with specified audio format and file type (see {@link AudioEncoder#AudioEncoder(Type, AudioFormat)}) to 
	 * an external audio file whose full path is as given by the argument 'path'.
	 * <p>The argument 'path' should give full path to the external file along with the file name and extension. 
	 * <b>Further, the audio file extension should be consistent with the specified audio file type else the audio file
	 * will not be recognised as a valid audio file.</b>
	 * <p>If the external audio file is a directory or is not a valid path this method will throw a FileNotFound Exception. 	
	 * <p>Each {@link Vector} of {@link VectorStack} 'data' representing the raw audio data for an independent channel should
	 * be of same length else this method will throw an IllegalArgument Exception. Further, the raw audio data for each channel
	 * should be in the range of [-1 1) else this method will throw an IllegalArgument Exception.
	 * <p>If the method encounters any IO problem during write this method will throw an IO Exception.
	 * @param String path
	 * @param VectorStack data
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void encodeAudioData(String path, VectorStack data) throws IllegalArgumentException, IOException{
		if(!f5(data))
			throw new IllegalArgumentException();

		File op = new File(path);
				
		byte[] buffer = null;

		if(format.getSampleSizeInBits() == 8)
			buffer = f4(data);
		else if(format.getSampleSizeInBits() == 16)
			buffer = f1(data);
		else
			throw new BugEncounterException();

		f3(op);

		f7(buffer);

		try{
			f0();	
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	private byte[] f4(VectorStack data){
		int numChannels = data.size();
		int frameLength = data.accessVector(0).length();

		byte[] buffer = new byte[frameLength * format.getFrameSize()];
		Vector[] channels = data.accessVectorArray();
		int t = 0;
		final float norm = 128;
		
		boolean isPCMUnSign = f2(format);
		int bits;

		if(!isPCMUnSign){
			for (int sample = 0; sample < frameLength;sample++) {
				for (int channel = 0; channel < numChannels; channel++) {
					bits = Math.round(norm*channels[channel].getElement(sample));
					if(bits == norm)
						bits--;
					buffer[t++] = (byte) bits;					
				}
			}
		}else{
			for (int sample = 0; sample < frameLength;sample++) {
				for (int channel = 0; channel < numChannels; channel++) {
					bits = Math.round(norm*channels[channel].getElement(sample));
					if(bits == norm)
						bits--;
					buffer[t++] = f6(bits);						
				}
			}
		}

		return buffer;
	}

	private byte[] f1(VectorStack data){
		int numChannels = data.size();
		int frameLength = data.accessVector(0).length();

		byte[] buffer = new byte[frameLength * format.getFrameSize()];
		Vector[] channels = data.accessVectorArray();
		int t = 0;
		final float norm = 32768;
		
		int in;
		if (format.isBigEndian()) {
			for (int sample = 0; sample < frameLength;sample++) {
				for (int channel = 0; channel < numChannels; channel++) {
					in = (int) Math.round(norm*channels[channel].getElement(sample));
					if(in == norm)
						in--;
					/* First byte is MSB (high order) */
					buffer[t++] = (byte)(in >> 8);
					/* Second byte is LSB (low order) */
					buffer[t++] = (byte)(in & 255);					
				}
			}
		}else{
			for (int sample = 0; sample < frameLength;sample++) {
				for (int channel = 0; channel < numChannels; channel++) {
					in = (int)Math.round(norm*channels[channel].getElement(sample));
					if(in == norm)
						in--;
					/* First byte is LSB (low order) */
					buffer[t++] = (byte)(in & 255);
					/* Second byte is MSB (high order) */
					buffer[t++] = (byte)(in >> 8);					
				}
			}
		}

		return buffer;
	}

	private byte f6(int in){
		return (byte) (in+128);
	}
	
	//Range [-1 1) and all Vector's of same length.
	private boolean f5(VectorStack data){
		int L = data.accessVector(0).length();
		int C = data.size();

		for(int l=1;l<C;l++){
			if(data.accessVector(l).length() != L)
				return false;
		}

		float ele;
		for(int l=0;l<C;l++){
			for(int i=0;i<L;i++){
				ele = data.accessVector(l).getElement(i);
				if(ele >=1 || ele <-1)
					return false;
			}
		}

		return true;
	}
	
	/**
	 * This method return a list of audio file extensions for audio file types which are
	 * supported for write operation by the platform. 
	 * @return String[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static String[] getAudioTypesSupported(){
		Type[] types = AudioSystem.getAudioFileTypes();
		
		if(types == null || types.length == 0)
			return null;
		
		String[] ext = new String[types.length];
		
		for(int i=0;i<ext.length;i++)
			ext[i] = types[i].getExtension();
		
		return ext;
	}
	
	private boolean f2(AudioFormat format){
		return format.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED;
	}
}
