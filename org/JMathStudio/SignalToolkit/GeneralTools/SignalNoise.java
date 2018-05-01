package org.JMathStudio.SignalToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.ExponentialRV;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.GaussianRV;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.PoissonRV;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.UniformRV;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various independent additive Noise model for adding Noise to a discrete
 * real signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * SignalNoise sn = new SignalNoise();//Create an instance of SignalNoise.
 * 
 * Vector gauss_noise = sn.gaussianNoise(a, 0, 5);//Add gaussian noise with specified parameters
 * to signal as represented by input Vector.
 * 
 * Vector poisson_noise = sn.poissonNoise(a, 1);//Add poission noise with given specification to
 * signal as represented by input Vector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SignalNoise {

	/**
	 * This will add an Uniform Noise in the interval [a b) to the discrete real
	 * signal as represented by the {@link Vector} 'vector' and return the noisy
	 * signal as a {@link Vector} with same length.
	 * <p>
	 * The argument 'b' should be more than 'a' else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param float a
	 * @param float b
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector uniformNoise(Vector vector, float a, float b)
			throws IllegalArgumentException {
		UniformRV ud = new UniformRV(a, b);
		Vector result = new Vector(vector.length());

		for (int i = 0; i < result.length(); i++)
			result.setElement(vector.getElement(i) + ud.nextRandomValue(), i);

		return result;
	}

	/**
	 * This will add a Gaussian Noise with Mean and Standard deviation as given
	 * by the arguments 'mean' and 'std' respectively to the discrete real
	 * signal as represented by the {@link Vector} 'vector' and return the noisy
	 * signal as a {@link Vector} with same length.
	 * <p>
	 * The argument 'std' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param float mean
	 * @param float std
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector gaussianNoise(Vector vector, float mean, float std)
			throws IllegalArgumentException {
		GaussianRV gd = new GaussianRV(mean, std);
		Vector result = new Vector(vector.length());

		for (int i = 0; i < result.length(); i++)
			result.setElement(vector.getElement(i) + gd.nextRandomValue(), i);

		return result;

	}

	/**
	 * This will add a Poisson Noise with mean as given by the argument
	 * 'lambda', which gives the expected number of occurrences in an interval,
	 * to the discrete real signal as represented by the {@link Vector} 'vector'
	 * and return the noisy signal as a {@link Vector} with same length.
	 * <p>
	 * The argument 'lambda' should be more than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param float lambda
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector poissonNoise(Vector vector, float lambda)
			throws IllegalArgumentException {
		PoissonRV pd = new PoissonRV(lambda);
		Vector result = new Vector(vector.length());

		for (int i = 0; i < result.length(); i++)
			result.setElement(vector.getElement(i) + pd.nextRandomValue(), i);

		return result;

	}

	/**
	 * This will add an Exponential Noise with rate parameter 'lambda' to the
	 * discrete real signal as represented by the {@link Vector} 'vector' and
	 * return the noisy signal as a {@link Vector} with same length.
	 * <p>
	 * The argument 'lambda' should be more than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param float lambda
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector exponentialNoise(Vector vector, float lambda)
			throws IllegalArgumentException {
		ExponentialRV ed = new ExponentialRV(lambda);
		Vector result = new Vector(vector.length());

		for (int i = 0; i < result.length(); i++)
			result.setElement(vector.getElement(i) + ed.nextRandomValue(), i);

		return result;

	}

}
