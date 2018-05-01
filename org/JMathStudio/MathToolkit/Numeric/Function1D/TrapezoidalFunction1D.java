package org.JMathStudio.MathToolkit.Numeric.Function1D;

import java.io.Serializable;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define the Trapezoidal Function. A Trapezoidal Function is
 * parametrised by its Lower and Upper Threshold along with Slope.
 * <p>
 * The parameter Lower Threshold define the input value where the rising edge of
 * this Function has value 0.5.
 * <p>
 * The parameter Upper Threshold define the input value where the decreasing
 * edge of this Function has value 0.5.
 * <p>
 * The Slope define the rate of rise and fall of its edges.
 * <p>
 * The default value of Lower and Upper Threshold is -10 and 10 with default
 * Slope of 1.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class TrapezoidalFunction1D extends AbstractFunction1D implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4589384756389999402L;
	public final static String type = "Trapezoid";
	private float lowerThreshold;
	private float upperThreshold;
	private float slope;

	/**
	 * This will create a Trapezoidal Function with default lower and upper
	 * threshold at -10 and 10 respectively with slope 1.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public TrapezoidalFunction1D() 
	{
		this.upperThreshold = 10;
		this.lowerThreshold = -10;
		this.slope = 1;
	}

	/**
	 * This will create a Trapezoidal Function with Lower and Upper Threshold as
	 * given by the argument 'lowerThreshold' and 'upperThreshold' respectively
	 * with Slope as given by the argument 'slope'.
	 * <p>
	 * The argument 'lowerThreshold' should be less than argument
	 * 'upperThreshold' and argument 'slope' should be positive else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param float lowerThreshold
	 * @param float upperThreshold
	 * @param float slope
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public TrapezoidalFunction1D(float lowerThreshold, float upperThreshold,
			float slope) throws IllegalArgumentException {
		if (lowerThreshold >= upperThreshold || slope < 0) {
			throw new IllegalArgumentException();
		}
		this.lowerThreshold = lowerThreshold;
		this.upperThreshold = upperThreshold;
		this.slope = slope;
	}

	/**
	 * This method will return the output of this Trapezoidal Function for the
	 * input as given by the argument 'x'.
	 * 
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float F(float x) {
		float centre = (upperThreshold + lowerThreshold) / 2;

		if (x <= centre)
			return (float) (1.0 / (1 + Math.exp(-slope
					* (x - lowerThreshold))));
		else
			return (float) (1.0 / (1 + Math.exp(+slope
					* (x - upperThreshold))));

	}

	/**
	 * This method return the Upper Threshold parameter set for this Trapezoidal
	 * Function.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getUpperThreshold() {
		return upperThreshold;
	}

	/**
	 * This method return the Lower Threshold parameter set for this Trapezoidal
	 * Function.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public float getLowerThreshold() {
		return lowerThreshold;
	}

	/**
	 * This method return the Slope parameter set for this Trapezoidal Function.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public float getSlope() {
		return slope;
	}

	/**
	 * This method will set the Lower and Upper Threshold parameter of this
	 * Trapezoidal Function to the argument 'lowerThreshold' and
	 * 'upperThreshold' respectively.
	 * <p>
	 * The argument 'upperThreshold' should be more than argument
	 * 'lowerThreshold' else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param float upperThreshold
	 * @param float lowerThreshold
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setThreshold(float upperThreshold, float lowerThreshold)
			throws IllegalArgumentException {
		if (lowerThreshold >= upperThreshold) {
			throw new IllegalArgumentException();
		}

		this.upperThreshold = upperThreshold;
		this.lowerThreshold = lowerThreshold;
	}

	/**
	 * This method will set the Slope parameter of this Trapezoidal Function to
	 * the argument 'slope'.
	 * <p>
	 * The argument 'slope' should be positive else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param float slope
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setSlope(float slope)
			throws IllegalArgumentException {
		if (slope < 0)
			throw new IllegalArgumentException();

		this.slope = slope;
	}

	/**
	 * This method return the description of this Function. Returned String will
	 * be equal to the static String variable 'type' for this class.
	 * 
	 * @return String
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public String getType() {
		return type;
	}

}
