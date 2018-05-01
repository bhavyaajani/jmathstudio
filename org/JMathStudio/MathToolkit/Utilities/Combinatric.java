package org.JMathStudio.MathToolkit.Utilities;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class provide various Combinatric operations.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Combinatric 
{
	/**
	 * This method computes the factorial of the integer number as given
	 * by the argument 'value'. The argument 'value' should not be less 
	 * than 0 else this method will throw an IllegalArgument Exception.
	 * @param int value
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float factorial(int value) throws IllegalArgumentException
	{
		if(value <0)
		{
			throw new IllegalArgumentException();
		}
		if(value ==0)
		{
			return 1;
		}
		float fact =1;
		
		for(int i= value;i>0;i--)
		{
			fact = fact*i;
		}
		
		return fact;
	}
	
	/**
	 * This method computes the nCk combinations possible for the given argument 'n' and
	 * 'k'.
	 * <p>The argument 'n' and 'k' should not be less than 0 and argument 'n'
	 * should not be less than 'k' else this method will throw an IllegalArgument
	 * Exception.
	 * @param int n
	 * @param int k
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float combination(int n,int k) throws IllegalArgumentException
	{
		if(n <0 || k< 0)
		{
			throw new IllegalArgumentException();
		}
		if(n < k)
		{
			throw new IllegalArgumentException();
		}
		return factorial(n)/(factorial(k)*factorial(n-k));
	}
	
	/**
	 * This method computes the nPk permutation possible for the given argument 'n' and
	 * 'k'.
	 * <p>The argument 'n' and 'k' should not be less than 0 and argument 'n'
	 * should not be less than 'k' else this method will throw an IllegalArgument
	 * Exception.
	 * @param int n
	 * @param int k
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float permutation(int n,int k) throws IllegalArgumentException
	{
		if(n <0 || k< 0)
		{
			throw new IllegalArgumentException();
		}
		if(n < k)
		{
			throw new IllegalArgumentException();
		}
				
		return factorial(n)/(factorial(n-k));
	}
	
	/**
	 * This method will compute the 2nd order Sterling number for the variable as given by
	 * the argument 'n' and 'k'.
	 * <p>The argument 'n' and 'k' should not be less than 0 and argument 'n' should not be
	 * less than 'k' else this method will throw an IllegalArgument Exception.
	 * @param int n
	 * @param int k
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float sterlingNumber2(int n,int k) throws IllegalArgumentException
	{
		if(n <0 || k< 0)
		{
			throw new IllegalArgumentException();
		}
		if(k > n)
		{
			throw new IllegalArgumentException();
		}
		
		float sterling =0;
		for(int j=0;j<=k;j++)
		{
			float tmp = (float) (Math.pow(-1, k-j)*combination(k,j)*Math.pow(j, n));
			sterling = sterling + tmp;
		}
		
		return sterling/factorial(k);
	}

}
