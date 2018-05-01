package org.JMathStudio.MathToolkit.Numeric.Function1D;

import java.io.Serializable;

/**
 * This class define the Linear Step Function. A Linear Step Function, is define
 * as y = L(Thr,x), where 'y' is the output of this Function for given input 'x' 
 * with Threshold 'Thr'. 'Thr' is the value at which Linear Step Function makes
 * transition from '0' to '1' at output.
 * @see AbstractFunction1D
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class LinearStepFunction1D extends AbstractFunction1D implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3658427903960258158L;
	private float threshold;
    public final static String type ="LinearStep";
    
    /**
	 * This will create a Linear Step Function with default threshold at 0.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
    public LinearStepFunction1D()
    {
    	this.threshold = 0;
    }
    
    /**
     * This will create a Linear Step Function with threshold as specified by the
     * argument 'threshold'.
     * @param float threshold
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public LinearStepFunction1D(float threshold)
    {
    	this.threshold = threshold;
    }
    
    /**
     * This method set the threshold for this Linear Step Function.
     * Threshold define the value where Linear Step Function makes transition
     * from '0' to '1' at output.
     * @param float threshold
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public void setThreshold(float threshold)
    {
        this.threshold = threshold;
    }
    
    /**
     * This method will return the output of this Linear Step Function for the given input
     * value as given by the argument 'x' with current Threshold.
     * @param float x
     * @return float
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public float F(float x)
    {
        if(x >= threshold)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * This method return the current Threshold set for this LinearStep Function.
     * @return float
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public float getThreshold()
    {
        return this.threshold;
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

    
}
