package org.JMathStudio.ToolBoxes.WaveletToolBox;

/**
 * This class provides various Orthogonal Wavelet as Wavelet object.
 * @see Wavelet
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class WaveletFactory 
{
	//Ensure no instances are made of Utility class.
	private WaveletFactory(){}
	
	/**
	 * This will create and return a Daubechies DB1 Wavelet. This Wavelet
	 * is also known as Haar Wavelet. 
	 * @return Wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Wavelet getDB1Wavelet()
	{
		String type = "DB1";
		float[] ld = new float[]{0.707f,0.707f};
		float[] hd = new float[]{-0.707f,0.707f};
		float[] lr = new float[]{0.707f,0.707f};
		float[] hr = new float[]{0.707f,-0.707f};
		
		return new Wavelet(type,ld,hd,lr,hr);
	}
	
	/**
	 * This will create and return a Daubechies DB2 Wavelet.  
	 * @return Wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Wavelet getDB2Wavelet()
	{
		String type = "DB2";
		float[] ld = new float[]{-0.1294f,0.2241f,0.8365f,0.4830f};
		float[] hd = new float[]{-0.4830f,0.8365f,-0.2241f,-0.1294f};
		float[] lr = new float[]{0.4830f,0.8365f,0.2241f,-0.1294f};
		float[] hr = new float[]{-0.1294f,-0.2241f,0.8365f,-0.4830f};
		
		return new Wavelet(type,ld,hd,lr,hr);
	}
	
	/**
	 * This will create and return a Daubechies DB3 Wavelet.  
	 * @return Wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Wavelet getDB3Wavelet()
	{
		String type = "DB3";
		float[] ld = new float[]{0.0352f,-0.0854f,-0.1350f,0.4599f,0.8069f,0.3327f};
		float[] hd = new float[]{-0.3327f,0.8069f,-0.4599f,-0.1350f,0.0854f,0.0352f};
		float[] lr = new float[]{0.3327f,0.8069f,0.4599f,-0.1350f,-0.0854f,0.0352f};
		float[] hr = new float[]{0.0352f,0.0854f,-0.1350f,-0.4599f,0.8069f,-0.3327f};
		
		return new Wavelet(type,ld,hd,lr,hr);
	}
	
	/**
	 * This will create and return a Daubechies DB4 Wavelet.  
	 * @return Wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Wavelet getDB4Wavelet()
	{
		String type = "DB4";
		float[] ld = new float[]{-0.0106f,0.0329f,0.0308f,-0.1870f,-0.0280f,0.6309f,0.7148f,0.2304f};
		float[] hd = new float[]{-0.2304f,0.7148f,-0.6309f,-0.0280f,0.1870f,0.0308f,-0.0329f,-0.0106f};
		float[] lr = new float[]{0.2304f,0.7148f,0.6309f,-0.0280f,-0.1870f,0.0308f,0.0329f,-0.0106f};
		float[] hr = new float[]{-0.0106f,-0.0329f,0.0308f,0.1870f,-0.0280f,-0.6309f,0.7148f,-0.2304f};
		
		return new Wavelet(type,ld,hd,lr,hr);
	}

	/**
	 * This will create and return a Daubechies DB5 Wavelet.  
	 * @return Wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Wavelet getDB5Wavelet()
	{
		String type = "DB5";
		float[] ld = new float[]{0.0033f,-0.0126f,-0.0062f,0.0776f,-0.0322f,-0.2423f,0.1384f,0.7243f,0.6038f,0.1601f};
		float[] hd = new float[]{-0.1601f,0.6038f,-0.7243f,0.1384f,0.2423f,-0.0322f,-0.0776f,-0.0062f,0.0126f,0.0033f};
		float[] lr = new float[]{0.1601f,0.6038f,0.7243f,0.1384f,-0.2423f,-0.0322f,0.0776f,-0.0062f,-0.0126f,0.0033f};
		float[] hr = new float[]{0.0033f,0.0126f,-0.0062f,-0.0776f,-0.0322f,0.2423f,0.1384f,-0.7243f,0.6038f,-0.1601f};
		
		return new Wavelet(type,ld,hd,lr,hr);
	}
	
	/**
	 * This will create and return a Daubechies DB6 Wavelet.  
	 * @return Wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Wavelet getDB6Wavelet()
	{
		String type = "DB6";
		float[] ld = new float[]{-0.0011f,0.0048f,0.0006f,-0.0316f,0.0275f,0.0975f,-0.1298f,-0.2263f,0.3153f,0.7511f,0.4946f,0.1115f};
		float[] hd = new float[]{-0.1115f,0.4946f,-0.7511f,0.3153f,0.2263f,-0.1298f,-0.0975f,0.0275f,0.0316f,0.0006f,-0.0048f,-0.0011f};
		float[] lr = new float[]{0.1115f,0.4946f,0.7511f,0.3153f,-0.2263f,-0.1298f,0.0975f,0.0275f,-0.0316f,0.0006f,0.0048f,-0.0011f};
		float[] hr = new float[]{-0.0011f,-0.0048f,0.0006f,0.0316f,0.0275f,-0.0975f,-0.1298f,0.2263f,0.3153f,-0.7511f,0.4946f,-0.1115f};
		
		return new Wavelet(type,ld,hd,lr,hr);
	}

}
