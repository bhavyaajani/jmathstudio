package org.JMathStudio.MathToolkit.StatisticalTools.Distribution;

import java.util.Random;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a Random Variable following a Poisson Distribution.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class PoissonRV extends AbstractRV {
	
	private float lambda;
	private double explambda;
	private Random random = new Random();

	/**
	 * This will create a Random Variable following a Poisson Distribution as
	 * characterised by the parameter 'lambda', which is the expected number of
	 * occurrences in an interval or mean of the Poisson distribution.
	 * <p>
	 * The argument 'lambda' should be a positive real number greater than 0
	 * else this method will throw an IllegalArgument Exception.
	 * 
	 * @param float lambda
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PoissonRV(float lambda)
			throws IllegalArgumentException {
		if (lambda <= 0)
			throw new IllegalArgumentException();
		else {
			this.lambda = lambda;
			this.explambda = Math.exp(-lambda);
		}
	}

	/**
	 * This method will return a random value taken by the given random variable
	 * as integer in the range of [0 inf) with the given poisson distribution.
	 * <p>
	 * The frequency with which the given random variable take a definite value
	 * will follow the given poisson distribution.
	 * <p>
	 * This method is based on the Knuth Algorithm for generating random
	 * variables with poisson distribution.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float nextRandomValue() {
		double p = 1.0;
		int k = 0;

		do {
			k++;
			p *= random.nextFloat();
		} while (p > explambda);

		return k - 1;

	}

	/**
	 * This method define the probability mass function for this random
	 * variable.
	 * <p>
	 * This will return the probability associated with the support value 'x'
	 * taken by this random variable.
	 * <p>
	 * The support for 'x' is the integer in the range [0 inf). If the argument
	 * 'x' is not within the valid support this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int x
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public float pmf(int x)
			throws IllegalArgumentException {

		if (x < 0)
			throw new IllegalArgumentException();
		else
			return (float) (explambda * Math.pow(lambda, x) / f3(x));
	}

	private double f3(int value)
			throws IllegalArgumentException {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		if (value == 0) {
			return 1;
		}
		double fact = 1;

		for (int i = value; i > 0; i--) {
			fact = fact * i;
		}

		return fact;
	}
}
