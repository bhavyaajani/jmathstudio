package org.JMathStudio.MathToolkit.Numeric.Function1D;

import java.io.Serializable;

/**
 * This class define the Unitary Function. An Unitary Function is the function
 * whose output is equal to its input.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class UnitaryFunction1D extends AbstractFunction1D implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3880173528838515476L;
	public final static String type ="Unitary";
     
    /**
     * This method return the output of this Unitary Function for the input value
     * given by the argument 'x'.
     * <p>The output of Unitary Function is equal to its input.
     * @param float x
     * @return float
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
    public float F(float x)
    {
        return x;
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

 }
