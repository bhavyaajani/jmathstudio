package org.JMathStudio.MathToolkit.StatisticalTools.Distribution;

import java.util.Random;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a Random Variable following an Exponential Distribution.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ExponentialRV extends AbstractRV {
	
	private Random random = new Random();
	private float lambda;

	/**
	 * This will create a Random Variable with an Exponential Distribution as
	 * characterised by the rate parameter 'lambda'.
	 * <p>
	 * The argument 'lambda' should be a positive real number else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param float lambda
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ExponentialRV(float lambda)
			throws IllegalArgumentException {
		if (lambda <= 0)
			throw new IllegalArgumentException();
		else
			this.lambda = lambda;

	}

	/**
	 * This method will return a random value taken by the given random variable
	 * in the range of [0 inf) with the given exponential distribution.
	 * <p>
	 * The frequency with which the given random variable take a definite value
	 * will follow the given exponential distribution.
	 * <p>
	 * This method is based on the Ziggurat Algorithm for generating random
	 * variables with exponential distribution.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float nextRandomValue() {
		float u = random.nextFloat();
		return (float) (-Math.log(1 - u) / lambda);
	}

	/**
	 * This method define the probability distribution function for this random
	 * variable.
	 * <p>
	 * This will return the probability associated with the support value 'x'
	 * taken by the given random variable.
	 * 
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float pdf(float x) {

		if (x < 0)
			return 0;
		else
			return (float) (lambda * (Math.exp(-lambda * x)));

	}

	/**
	 * This method define the cumulative distribution function for this random
	 * variable.
	 * <p>
	 * This will return the cumulative probability associated with the support
	 * value 'x' taken by this random variable.
	 * 
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float cdf(float x) {
		if (x < 0)
			return 0;
		else
			return (float) (1.0f - Math.exp(-lambda * x));
	}

}
