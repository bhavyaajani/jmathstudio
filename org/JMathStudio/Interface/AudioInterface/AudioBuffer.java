package org.JMathStudio.Interface.AudioInterface;

import javax.sound.sampled.AudioFormat;

import org.JMathStudio.DataStructure.Vector.VectorStack;

/**
 * This class define a container class to hold raw audio data and the associated format.
 * <p>The multiple channel raw audio data is stored as a {@link VectorStack} while the audio
 * format is represented by an {@link AudioFormat} object.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class AudioBuffer {

	private VectorStack data;
	private final AudioFormat format;
	
	protected AudioBuffer(VectorStack data, AudioFormat format){
		this.data = data;
		this.format = format;
	}
	
	/**
	 * This method gives access to the associated Audio format. See {@link AudioFormat}.
	 * @return {@link AudioFormat}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AudioFormat accessAudioFormat(){
		return this.format;
	}

	/**
	 * This method gives access to multiple channel raw audio data stored internally as VectorStack.
	 * <p>Each Vector of the VectorStack will be a raw audio data for an independent channel
	 * and will be of same length i.e frame size.
	 * @return VectorStack
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack accessAudioBuffer(){
		return this.data;
	}
}
