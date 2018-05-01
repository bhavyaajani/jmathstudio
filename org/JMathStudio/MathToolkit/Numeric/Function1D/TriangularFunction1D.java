package org.JMathStudio.MathToolkit.Numeric.Function1D;

import java.io.Serializable;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define the Triangular Function. A Triangular Function is parametrised by its
 * Height, Slope and Threshold.
 * <p>Height and Slope define the height and slope of the sides of Triangle respectively.
 * The output of this Function shape of a Triangle, thus Height define the maximum output
 * of this function and Slope define the rate of rise and fall of its edges.
 * <p>Threshold define the location of the Triangle i.e. input value where Triangular
 * Function has maximum output equal to its Height.
 * <p>Default value of Height, Slope and Threshold of Triangular Function is 1,1 and 0
 * respectively.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class TriangularFunction1D extends AbstractFunction1D implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5350577618838136566L;
	public final static String type = "Triangular";
	private float threshold;
	private float slope;
	private float height;
	
	/**
	 * This will create a Triangular Function with default threshold at 0 with slope and
	 * height equal to 1.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public TriangularFunction1D()
	{
		this.threshold = 0;
		this.slope = 1;
		this.height = 1;
	}
	
	/**
	 * This will create a Triangular Function with threshold, slope and height as specified
	 * by the arguments 'threshold', 'slope' and 'height' respectively.
	 * <p>The value of arguments 'slope' and 'height' should be more than 0 else this method
	 * will throw an IllegalArgument Exception.
	 * @param float threshold
	 * @param float slope
	 * @param float height
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public TriangularFunction1D(float threshold, float slope, float height) throws IllegalArgumentException
	{
		if(slope <=0 || height <=0)
			throw new IllegalArgumentException();
		else
		{
			this.threshold = threshold;
			this.slope = slope;
			this.height = height;
		}
	}
	
	/**
     * This method return the output value of this Triangular Function for the input given
     * by the argument 'x'.
     * @param float x
     * @return float
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */																			

	public float F(float x) 
	{
		float t = Math.abs(x-threshold);
		
		if(t>= height/slope)
			return 0;
		else
			return -slope*t + height;
				
	}
	
	/**
     * This method return the description for this Function. Returned String will
     * be equal to the static String variable 'type' for this class.
     * @return String
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
	public String getType() 
	{
		return type;
	}
	
	/**
	 * This method set the Threshold parameter of this Triangular Function to the
	 * argument 'threshold'. At this input, Triangular Function will have maximum
	 * ouput given by its Height parameter.
	 * @param float threshold
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setThreshold(float threshold)
	{
		this.threshold = threshold;
	}
	
	/**
	 * This method set the Height parameter of this Triangular Function to the argument
	 * 'height'. This parameter define the maximum output of this function.
	 * <p.If the argument 'height' is less than or equal to 0 this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param float height
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setHeight(float height) throws IllegalArgumentException
	{
		if(height<=0)
			throw new IllegalArgumentException();
		else
			this.height = height;
	}
	
	/**
	 * This method set the Slope parameter of this Triangular Function to the argument 'slope'.
	 * <p>If the argument 'slope' is less than or equal to 0 this method will throw an 
	 * IllegalArgument Exception.
	 * 
	 * @param float slope
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setSlope(float slope) throws IllegalArgumentException
	{
		if(slope<=0)
			throw new IllegalArgumentException();
		else
			this.slope = slope;
	}
	
	/**
	 * This method return the Threshold parameter of this Triangular Function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getThreshold()
	{
		return threshold;
	}
	
	/**
	 * This method return the Height parameter of this Triangular Function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getHeight()
	{
		return height;
	}
	
	/**
	 * This method return the Slope parameter of this Triangular Function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getSlope()
	{
		return slope;
	}
	
}
