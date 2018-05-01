package org.JMathStudio.MathToolkit.Utilities;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define many of the useful mathematical utilities and conversions.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class MathUtils {
	
	//Ensure no instances are made for utility classes.
	private MathUtils(){}
	
	/**
	 * Constant defining Square root of 2.
	 */
	public final static double SQRT_OF_TWO = 1.4142135623730950488;
	/**
	 * Constant defining residue error for difference to be taken as 0.
	 */
	public final static float ERR_MIN = 1.0E-10f;
	
	/**
	 * This method check if the given delta is significantly small to be considered
	 * as zero.
	 * <p>The decision is based on the residue error for difference as defined by
	 * {@link #ERR_MIN}.
	 * @param float delta
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static boolean isZero(float delta)
	{
		return (Math.abs(delta) < ERR_MIN);
	}
	
	/**
	 * This method check if the scalar 'a' and 'b' are sufficiently close to be considered
	 * as equal.
	 * <p>The decision of equality is based on the absolute difference between the scalars 
	 * 'a' and 'b' falling below the residue error for difference as defined by
	 * {@link #ERR_MIN}.
	 * @param float a
	 * @param float b
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static boolean isEqual(float a,float b)
	{
		return (Math.abs(a-b) < ERR_MIN);
	}
	
	/**
	 * This method will return the Logarithm of the argument 'x' to the base 'b'.
	 * <p>The value of argument 'x' should be more than '0' and value of the argument
	 * 'b' should be more than 1, else this method will throw an IllegalArgument Exception.
	 * @param float x
	 * @param short b
	 * @return double
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static double log(float x,short b) throws IllegalArgumentException
	{
		if(x<=0 || b <=1)
			throw new IllegalArgumentException();
		
		return (Math.log(x)/Math.log(b));
	}

	/**
	 * This method will return the output of the Legendre function with
	 * Polynomial order 'n' for the input as given by the argument 'x'.
	 * <p>
	 * The value of argument 'n' should be more than or equal to 0 else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * The ideal range of argument 'x' is [-1,1].
	 * 
	 * @param int n
	 * @param float x
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static float legPoly(int n, float x) throws IllegalArgumentException 
	{
		if (n < 0) 
		{
			throw new IllegalArgumentException();
		}

		if (n == 0) 
		{
			return 1.0f;
		} else if (n == 1) 
		{
			return x;
		} else 
		{
			return ((2 * n - 1) * x * legPoly(n - 1, x) - (n - 1)
					* legPoly(n - 2, x))
					/ n;
		}
	}
	
	/**
	 * This method will return the corresponding magnitude value for the given decibel (dB)
	 * value 'db'.
	 * @param float db
	 * @return double
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static double dbToMag(float db)
	{
		return Math.pow(10, db/20);
	}
	
	/**
	 * This method will return the corresponding decibel (dB) value for the given magnitude
	 * value 'mag'.
	 * @param float mag
	 * @return double
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static double magToDb(float mag)
	{
		return 20*Math.log10(mag);
	}
	
	/**
	 * This method will return the corresponding power value for the given decibel (dB)
	 * value 'db'.
	 * @param float db
	 * @return double
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static double dbToPow(float db)
	{
		return Math.pow(10, db/10);
	}
	
	/**
	 * This method will return the corresponding decibel (dB) value for the given power
	 * value 'pow'.
	 * <p>The power value 'pow' should not be negative else this method will throw an IllegalArgument
	 * Exception.
	 * @param float pow
	 * @return double
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static double powToDb(float pow) throws IllegalArgumentException
	{
		if(pow < 0)
			throw new IllegalArgumentException();
		else
			return 10*Math.log10(pow);
	}
	
	/**
	 * The method will round off the floating point value as specified by the argument
	 * 'value' to the nearest decimal precision as specified by the argument 'precision'
	 * and return the same.
	 * <p>The argument 'precision' should not be less than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * @param float value
	 * @param int precision
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static float roundOff(float value,int precision) throws IllegalArgumentException
	{
		if(precision < 0)
			throw new IllegalArgumentException();
		
		
		int scale = (int) Math.pow(10, precision);
		
		return ((float) Math.round(scale * value))/scale;
	}
	
	/**
	 * The method will check whether the integer 'value' is a prime. If so this method will
	 * return true else false.
	 * <p>The argument 'value' should be more than '1' else this method will throw an 
	 * IllegalArgument Exception.
	 * @param int value
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static boolean isPrime(int value) throws IllegalArgumentException {
		if (value < 2)
			throw new IllegalArgumentException();

		if (value == 2 || value == 3)
			return true;

		for (int i = 2; i <= value / 2; i++) {
			if (value % i == 0)
				return false;
		}

		return true;
	}

}
