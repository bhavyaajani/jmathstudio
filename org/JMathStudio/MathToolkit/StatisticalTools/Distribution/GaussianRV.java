package org.JMathStudio.MathToolkit.StatisticalTools.Distribution;

import java.util.Random;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a Random Variable following a Gaussian Distribution.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class GaussianRV extends AbstractRV {
	
	private float mean;
	private float std;
	private float norm;
	private float variance;

	private Random random = new Random();

	/**
	 * This will create a Random Variable following the Gaussian Distribution.
	 * The gaussian distribution is characterised by its Mean and Standard
	 * deviation as given by the argument 'mean' and 'std' respectively.
	 * <p>
	 * The argument 'std' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param float mean
	 * @param float std
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public GaussianRV(float mean, float std)
			throws IllegalArgumentException {
		if (std <= 0)
			throw new IllegalArgumentException();
		else {
			this.std = std;
			this.mean = mean;
			this.norm = (float) (1.0f / (Math.sqrt(2 * Math.PI) * std));
			this.variance = std * std;
		}
	}

	/**
	 * This method define the probability distribution function for this random
	 * variable.
	 * <p>
	 * This will return the probability associated with the support value 'x'
	 * taken by this random variable.
	 * 
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float pdf(float x) {
		double param = ((x - mean) * (x - mean)) / (2 * variance);
		return (float) (norm * (Math.exp(-param)));
	}

	/**
	 * This method will return a random value taken by the given random variable
	 * in the range of (-inf inf).
	 * <p>
	 * The frequency with which the given random variable take a definite value
	 * will follow the specified gaussian distribution.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float nextRandomValue() {
		return (float) (random.nextGaussian() * std + mean);
	}

}
