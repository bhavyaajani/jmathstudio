package org.JMathStudio.MathToolkit.Numeric.Function1D;

import java.io.Serializable;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define the S curve Function. An S Curve Function is parametrised
 * by two values called left and right break points. The default left and right
 * break points for this Function is -1 and 1 respectively.
 * <p>Function has output 1 for all input larger and equal to right break point.
 * <p>Function has output 0 for all input smaller and equal to left break point.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SCurveFunction1D extends AbstractFunction1D implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -581261098357153569L;
	public final static String type = "SCurve";
	private float xl;
	private float xr;
	private double PI = Math.PI;

	/**
	 * This will create a SCurve Function with default left and right break points at
	 * -1 and 1 respectively.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public SCurveFunction1D()
	{
		this.xl = -1;
		this.xr = 1;
	}
	
	/**
	 * This will create a SCurve Function with left and right break points as specified by
	 * the arguments 'xl' and 'xr' respectively.
	 * <p>The value of argument 'xl' should be less than the value of argument 'xr' else this method
	 * will throw an IllegalArgument Exception.
	 * @param float xl
	 * @param float xr
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public SCurveFunction1D(float xl,float xr) throws IllegalArgumentException
	{
		if(xl>= xr)
			throw new IllegalArgumentException();
		else
			this.xl = xl;
			this.xr = xr;
	}
	/**
	 * This method will return the output value of this SCurve Function for the given input
	 * value as given by the argument 'x'.
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public float F(float x) 
	{
		if(x<xl)
			return 0;
		else if(x>xr)
			return 1;
		else
			return (float) (0.5*(1 + Math.cos( PI*(x- xr)/(xr-xl) ))); 
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
	 * This method will set the Left and Right Break Points parameter of this SCurve Function
	 * to argument 'leftPoint' and 'rightPoint' respectively.
	 * <p>The argument 'leftPoint' should be less than the argument 'rightPoint' else this method
	 * will throw an IllegalArgument Exception.
	 * @param float leftPoint
	 * @param float rightPoint
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setBreakPoint(float leftPoint, float rightPoint) throws IllegalArgumentException
	{
		if(leftPoint>= rightPoint)
			throw new IllegalArgumentException();
		else
			xl = leftPoint;
			xr = rightPoint;
	}

		
	/**
	 * This method return the Left Break Point parameter of this SCurve Function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getLeftBreakPoint()
	{
		return xl;
	}
	
	/**
	 * This method return the Right Break Point parameter of this SCurve Function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getRightBreakPoint()
	{
		return xr;
	}
	
}
