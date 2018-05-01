package org.JMathStudio.MathToolkit.StatisticalTools.Distribution;

import java.util.Random;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a Random Variable following a Binomial Distribution.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class BinomialRV extends AbstractRV
{
	private Random random = new Random();
	
	private int N;
	private float p;
	private double NFact;
	
	/**
	 * This will create a Random Variable with a binomial distribution as characterised by the arguments 'N' and 'p'.
	 * <p>The argument 'N' specify the number of trials of binomial distribution and should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'p' specify the probability of success for a single trial in the 
	 * binomial distribution. The argument 'p' should not be negative else this method will throw an IllegalArgument
	 * Exception.
	 * @param int N
	 * @param float p
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinomialRV(int N,float p) throws IllegalArgumentException
	{
		if(N <1 || p<0)
			throw new IllegalArgumentException();
		else
		{
			this.N = N;
			this.p = p;
			this.NFact = f0(N);
		}
	}

	/**
	 * This method will return a random value taken by the given random variable as integer value in the 
	 * range of [0 N] with the given binomial distribution.
	 * <p>The frequency with which the given random variable take a definite value will follow
	 * the given binomial distribution.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float nextRandomValue() 
	{
		int x = 0;
		for(int i = 0; i < N; i++) 
		{
		    if(random.nextFloat() < p)
		      x++;
		}
		  return x;
	}

	/**
	 * This method define the probability mass function for this random variable.
	 * <p>This will return the probability associated with the support value 'x' taken by the given
	 * random variable. 
	 * <p>The support for 'x' is the integer in the range [0 N]. If the argument
	 * 'x' is not within the valid support this method will throw an IllegalArgument Exception.
	 * @param int x
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public float pmf(int x) throws IllegalArgumentException
	{
		if(x<0 || x >N)
			throw new IllegalArgumentException();
		else
		{
			int n_x = N-x;
			double xFact = f0(x);
			double n_xFact = f0(n_x);
			double ptox = Math.pow(p, x);
			double qton_x = Math.pow(1-p, n_x);
			
			return (float) ((NFact/(n_xFact*xFact))*(ptox*qton_x));
		}
	}
			
	private double f0(int value) throws IllegalArgumentException
	{
		if(value <0)
		{
			throw new IllegalArgumentException();
		}
		if(value ==0)
		{
			return 1;
		}
		double fact =1;
		
		for(int i= value;i>0;i--)
		{
			fact = fact*i;
		}
		
		return fact;
	}

}
