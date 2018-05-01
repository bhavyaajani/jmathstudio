package org.JMathStudio.ToolBoxes.WaveletToolBox;

/**
 * This class define an orthogonal Wavelet. An orthogonal Wavelet is
 * characterised by its Low Pass and High Pass decomposition and reconstruction
 * filter coefficients.
 * <p>
 * The specified filter coefficients should form an Quadrature Mirror filter.
 * 
 * @see WaveletFactory
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Wavelet {
	
	private float[] ld;
	private float[] hd;
	private float[] lr;
	private float[] hr;
	private String type;

	/**
	 * This will create an orthogonal Wavelet of type as specified by the
	 * argument 'type'.
	 * <p>
	 * The given Wavelet will be characterised by following filter coefficients,
	 * <i>
	 * <p>
	 * 'LD' - Low Pass decomposition filter coefficients.
	 * <p>
	 * 'HD' - High Pass decomposition filter coefficients.
	 * <p>
	 * 'LR' - Low Pass reconstruction filter coefficients.
	 * <p>
	 *'HR' - High Pass reconstruction filter coefficients. </i>
	 * <p>
	 * When creating a new Wavelet object ensure that filter coefficients passed
	 * on are correct for the Wavelet type as specified.
	 * <p>All array arguments are passed by reference and no deep copy of the same is made.
	 *  
	 * @param String
	 *            type
	 * @param float[] LD
	 * @param float[] HD
	 * @param float[] LR
	 * @param float[] HR
	 * @see WaveletFactory
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Wavelet(String type, float[] LD, float[] HD, float[] LR, float[] HR) {
		this.type = type;
		this.ld = LD;
		this.hd = HD;
		this.lr = LR;
		this.hr = HR;
	}

	/**
	 * This method will return the Low Pass Decomposition filter coefficinets
	 * for the given Wavelet.
	 * 
	 * @return float[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[] accessLD() {
		return this.ld;
	}

	/**
	 * This method will return the High Pass Decomposition filter coefficinets
	 * for the given Wavelet.
	 * 
	 * @return float[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[] accessHD() {
		return this.hd;
	}

	/**
	 * This method will return the Low Pass Reconstruction filter coefficinets
	 * for the given Wavelet.
	 * 
	 * @return float[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[] accessLR() {
		return this.lr;
	}

	/**
	 * This method will return the High Pass Reconstruction filter coefficinets
	 * for the given Wavelet.
	 * 
	 * @return float[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[] accessHR() {
		return this.hr;
	}

	/**
	 * This will specify the type of this Wavelet object.
	 * 
	 * @return String
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public String getWaveletType() {
		return this.type;
	}

}