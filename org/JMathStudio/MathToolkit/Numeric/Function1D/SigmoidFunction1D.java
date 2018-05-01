package org.JMathStudio.MathToolkit.Numeric.Function1D;

import java.io.Serializable;

/**
 * This class define the Sigmoid Function. A Sigmoid Function is parametrised
 * by its Threshold and Slope.
 * <p>The output of Sigmoid Function will be 0.5 for input equal to Threshold value.
 * Default Threshold value is 0.
 * <p>The Slope define the curvature of the Sigmoid Function. Negative Slope will
 * flip the Sigmoid Function. The default value of Slope is 1.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SigmoidFunction1D extends AbstractFunction1D implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -85789196634647390L;
	private float threshold;
    public final static String type ="Sigmoid";
    private float slope;
    
    /**
	 * This will create a Sigmoid Function with default threshold and slope at 0 and
	 * 1 respectively.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
    public SigmoidFunction1D()
    {
    	this.threshold = 0;
    	this.slope = 1;
    }
    
    /**
     * This will create a Sigmoid Function with threshold and slope as specified by the
     * argument 'threshold' and 'slope' respectively.
     * @param float threshold
     * @param float slope
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public SigmoidFunction1D(float threshold, float slope)
    {
    	this.threshold = threshold;
    	this.slope = slope;
    }
    
    /**
     * This method set the threshold parameter of this Sigmoid Function to the
     * argument 'threshold'. Output of Sigmoid Function is 0.5 for this input.
     * @param float threshold
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public void setThreshold(float threshold)
    {
        this.threshold = threshold;
    }
    
    /**
     * This method return the threshold parameter of this Sigmoid Function.
     * @return float
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public float getThreshold()
    {
        return this.threshold;
    }
    
    /**
     * This method return the output value of this Sigmoid Function for the input
     * value as given by the argument 'x'.
     * @param float x
     * @return float
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public float F(float x)
    {
        return (float)(1.0/(1 + Math.exp(-slope*(x - threshold))));
    }
    
     /**
     * This method return the description of this Function. Returned String will
     * be equal to the static String variable 'type' for this class.
     * @return String
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public String getType()
    {
        return type;
    }

    
    /**
	 * This method return the slope parameter of this Sigmoid Function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getSlope()
	{
		return this.slope;
	}
	
	/**
	 * This method set the slope parameter of this Sigmoid Function to the 
	 * argument 'slope'.
	 * <p>If 'slope' is negative Sigmoid Function will be fliped.
	 * @param float slope
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setSlope(float slope)
	{
		this.slope = slope;
	}

}
