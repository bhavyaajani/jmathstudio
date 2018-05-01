package org.JMathStudio.MathToolkit.Numeric.Function1D;

import java.io.Serializable;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define the Gaussian Function. A Gaussian Function is parametrised
 * by its Mean and Standard deviation.
 * <p>
 * The default mean value is 0 and standard deviation value is 1.
 * <p>
 * The mean define the location of the centre lobe of the Gaussian Function and
 * standard deviation define the width of the slope.
 * <p>
 * Standard deviation has to be a non zero positive value.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class GaussianFunction1D extends AbstractFunction1D implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6619078025461711988L;
	private float mean;
	private float std;
	public final static String type = "Gaussian";

	/**
	 * This will create a Gaussian Function with default mean of 0 and standard
	 * deviation 1.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public GaussianFunction1D() {
		this.mean = 0;
		this.std = 1;
	}

	/**
	 * This will create and instantiate a Gaussian Function with mean and
	 * stadard deviation as specified by the argument 'mean' and 'std'
	 * respectively.
	 * <p>
	 * The value of argument 'std' should be more than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param float mean
	 * @param float std
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public GaussianFunction1D(float mean, float std)
			throws IllegalArgumentException {
		if (std <= 0)
			throw new IllegalArgumentException();
		else {
			this.mean = mean;
			this.std = std;
		}
	}

	/**
	 * This method will return the mean of the given Gaussian Function.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getMean() {
		return this.mean;
	}

	/**
	 * This method will return the standard deviation of the given Gaussian
	 * Function.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getStandardDeviation() {
		return this.std;
	}

	/**
	 * This will set the mean of the given Gaussian Function as specified by the
	 * argument 'mean'.
	 * 
	 * @param float mean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setMean(float mean) {
		this.mean = mean;
	}

	/**
	 * This will set the standard deviation of the given Gaussian Function as
	 * specified by the argument 'std'.
	 * <p>
	 * The argument 'std' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param float std
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setStandardDeviation(float std)
			throws IllegalArgumentException {
		if (std <= 0)
			throw new IllegalArgumentException();
		else
			this.std = std;
	}

	/**
	 * This method return the output value of this Gaussian Function for the
	 * input value as given by the argument 'x'.
	 * 
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float F(float x) {
		float tmp = x - mean;
		
		float par = -((tmp) * (tmp)) / (2 * std * std);
		return (float) (Math.exp(par));
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
