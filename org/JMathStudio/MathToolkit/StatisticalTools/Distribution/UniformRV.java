package org.JMathStudio.MathToolkit.StatisticalTools.Distribution;

import java.util.Random;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a Random Variable following an Uniform Distribution.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class UniformRV extends AbstractRV
{
	//correction required in this class, kernel support is actually [a b) and not [a b] as
	//specified. This is because "nextFloat()" method in random generator gives op in [0 1).
	//So as far as random number generation is considered, we will get uniform random numbers
	//in range [a b) and not [a b] as stated. Rest of all statistics are fine with [a b].
	
	private float a;
	private float b;
	private Random engine = new Random();
	
	/**
	 * This will create a Random Variable following an Uniform Distribution with support interval [a b] as given
	 * by the arguments 'a' and 'b'.
	 * <p>The argument 'a' should be less than 'b' else this method will throw an IllegalArgument
	 * Exception.
	 * @param float a
	 * @param float b
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UniformRV(float a, float b) throws IllegalArgumentException
	{
		if(a >= b)
			throw new IllegalArgumentException();
		else
		{
			this.a = a;
			this.b = b;
		}
	}

	/**
	 * This method will return a random value taken by the given random variable in the range [a b) 
	 * with the given uniform distribution.
	 * <p>The frequency with which the given random variable takes a definite value will follow
	 * the given uniform distribution.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float nextRandomValue() 
	{
		return (b-a)*engine.nextFloat() + a;
	}

	/**
	 * This method define the probability distribution function for this random variable.
	 * <p>This will return the probability associated with the support value 'x' taken by this
	 * random variable.
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float pdf(float x) {

		if(x>=a & x <=b)
			return 1.0f/(b-a);
		else
			return 0;
	}
	
	/**
	 * This method define the cumulative distribution function for this random variable.
	 * <p>This will return the cumulative probability associated with the support value 'x' taken by this
	 * random variable.
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float cdf(float x)
	{
		if(x < a)
			return 0;
		else if(x >=a & x <=b)
			return (x-a)/(b-a);
		else
			return 1;
	}

}
