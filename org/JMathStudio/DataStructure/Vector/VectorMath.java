package org.JMathStudio.DataStructure.Vector;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.DivideByZeroException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;

/**
 * This class define basic mathematical operations on a {@link Vector}.
 *  
 * <pre>Usage:
 * Let 'a' & 'b' be Vector objects with same length.
 * 
 * Vector cos = VectorMath.cos(a);//Compute cosine value for each element of input
 * Vector.
 * 
 * Vector exp = VectorMath.exponential(a);//Compute exponential, e^x,for each element of 
 * input Vector.
 * 
 * Vector product = VectorMath.dotProduct(a, b);//Take product of corresponding elements of 
 * input Vectors.
 * </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class VectorMath {
	
	//Ensure no instances are made for utility classes.
	private VectorMath(){}
	
	/**
	 * This method will apply the base 10 Logarithm on each element of the {@link Vector} 
	 * 'vector' and return the result as a Vector.
	 * <p>
	 * The return Vector will contain Logarithm values for the corresponding elements of the
	 * Vector 'vector'.
	 * <p>
	 * If any of the element in the Vector 'vector' is negative or zero this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see Math#log10(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector log10(Vector vector) throws IllegalArgumentException {
		int L = vector.length();
		
		float[] buffer = vector.accessVectorBuffer();
		float[] result = new float[L];
		float ele;
		
		for (int i = 0; i < L; i++) {
			ele = buffer[i];
			if (ele <= 0) {
				throw new IllegalArgumentException();
			}
			result[i] = (float) Math.log10(ele);
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Natural Logarithm for each element of the {@link Vector}
	 * 'vector' and return the result as a Vector.
	 * <p>
	 * The return Vector will contain Natural Logarithm for the corresponding element of the 
	 * Vector 'vector'.
	 * <p>
	 * If any of the element in the Vector 'vector' is negative or zero this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see Math#log(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector logE(Vector vector) throws IllegalArgumentException {
		int L = vector.length();
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[L];
		float ele;

		for (int i = 0; i < L; i++) {
			ele = buffer[i];
			if (ele <= 0) {
				throw new IllegalArgumentException();
			}
			result[i] = (float) Math.log(ele);
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Square Root of each element of the {@link Vector} 'vector' 
	 * and return the result as a Vector.
	 * <p>
	 * The return Vector will contain Square Root value of the corresponding elements of the 
	 * Vector 'vector'.
	 * <p>
	 * If any of the element in the Vector 'vector' is negative this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see Math#sqrt(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector sqrt(Vector vector) throws IllegalArgumentException {
		int L = vector.length();
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[L];
		float ele;

		for (int i = 0; i < L; i++) {
			ele = buffer[i];
			if (ele < 0) {
				throw new IllegalArgumentException();
			}
			result[i] = (float) Math.sqrt(ele);
		}

		return new Vector(result);
	}

	/**
	 * This method will raise the elements of the {@link Vector} 'vector' to the power
	 * of, as given by the argument 'power', and return the result as a Vector.
	 * <p><i> y = x^a;</i>
	 * <p>The return Vector will contain value of corresponding elements of the
	 * Vector 'vector' raised to the given power.
	 * <p>
	 * If any of the element of 'vector' is negative and argument 'power' is
	 * such that a root is to be computed which is not computable this method will 
	 * throw an Exception. This scenario includes computing square root of a negative
	 * number.
	 * 
	 * @param Vector
	 *            vector
	 * @param float power
	 * @return Vector
	 * @see Math#pow(double, double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector power(Vector vector,float power) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.pow(buffer[i], power);
		}

		return new Vector(result);
	}

	/**
	 * This method will apply a linear operation on the elements of {@link Vector}
	 * 'vector' using the scale argument 'scale' and shift argument 'shift' and
	 * return the resultant Vector.
	 * <p>
	 * The linear operation is equivalent to multiplying the element by the
	 * argument 'scale' and adding the argument 'shift' to it.
	 * <p><i>y = scale*x + shift</i>
	 * <p>The return Vector contain the linear operated values for the
	 * corresponding elements of the Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector linear(float scale, float shift, Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = scale * buffer[i] + shift;
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Complex Sinusoid for each element of the {@link Vector} 
	 * 'vector' and return the resultant Complex Vector as {@link CVector}.
	 * <p>
	 * The Complex Sinusoid takes input in radian angle.
	 * <p><i> y = cos(x) + i sin(x)</i>
	 * <p>The resultant CVector contain the Complex sinusoid value for the corresponding element
	 * (in radian angle) of the Vector 'vector'.
	 * 
	 * @param Vector
	 *            Vector
	 * @return CVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector complexSinusoid(Vector vector) {
		int L = vector.length();
		
		float[] real = new float[L];
		float[] img = new float[L];
		
		float[] buffer = vector.accessVectorBuffer();
		float ele;
		
		for (int i = 0; i < L; i++) 
		{
			ele = buffer[i];
			real[i] = (float) Math.cos(ele);
			img[i] = (float) Math.sin(ele);
		}
		try {
			return new CVector(real,img);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will apply Exponential function on each element of the {@link Vector} 'vector' 
	 * and return the result as a Vector.
	 * <p>
	 * The return Vector will contain Exponential value for the corresponding elements of the 
	 * Vector 'vector'.
	 * <p><i>y = e^x</i>
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @see Math#exp(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector exponential(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.exp(buffer[i]);
		}

		return new Vector(result);

	}

	/**
	 * This method will compute the Cosine of each element of the {@link Vector} 'vector' 
	 * and return the result as a Vector.
	 * <p>
	 * The Cosine function takes angle in radian as input.
	 * <p><i>y = cos(x)</i>
	 * <p>The return Vector will contain Cosine value for the corresponding elements of the 
	 * Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @see Math#cos(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector cos(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.cos(buffer[i]);
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Sine of each element of the {@link Vector} 'vector' and
	 * return the result as a Vector.
	 * <p>
	 * The Sine function takes angle in radian as input.
	 * <p><i>y = sin(x)</i>
	 * <p>The return Vector will contain Sine value for the corresponding elements of the 
	 * Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @see Math#sin(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector sin(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.sin(buffer[i]);
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Tangent of each element of the {@link Vector} 'vector' 
	 * and return the result as a Vector.
	 * <p>
	 * The Tangent function takes angle in radian as input.
	 * <p><i>y = tan(x)</i>
	 * <p>The return Vector will contain Tangent value for the corresponding elements of the 
	 * Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @see Math#tan(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector tan(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.tan(buffer[i]);
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Inverse Sine for each element of the {@link Vector} 
	 * 'vector' and return the result as a Vector.
	 * <p>
	 * The Inverse Sine is defined for the interval of [-1 1].
	 * <p>The return Vector will contain Inverse Sine value in radian for the
	 * corresponding elements of the Vector 'vector'.
	 * <p>
	 * If any element of the Vector 'vector' has absolute value more than 1,
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see Math#asin(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector asin(Vector vector) throws IllegalArgumentException {
		float[] buffer = vector.accessVectorBuffer();
		int L = buffer.length;
		
		float[] result = new float[L];
		float ele;
		
		for (int i = 0; i < L; i++) {
			ele = buffer[i];
			if (Math.abs(ele) > 1)
				throw new IllegalArgumentException();

			result[i] = (float) Math.asin(ele);
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Inverse Cosine for each element of the {@link Vector} 
	 * 'vector' and return the result as a Vector.
	 * <p>
	 * The Inverse Cosine is defined for the interval of [-1 1].
	 * <p>
	 * The return Vector will contain Inverse Cosine value in radian for the corresponding 
	 * elements of the Vector 'vector'.
	 * <p>
	 * If any element of the Vector 'vector' has absolute value more than 1,
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see Math#acos(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector acos(Vector vector) throws IllegalArgumentException {
		float[] buffer = vector.accessVectorBuffer();
		int L = buffer.length;
		
		float[] result = new float[L];
		float ele;
		
		for (int i = 0; i < L; i++) {
			ele = buffer[i];
			if (Math.abs(ele) > 1)
				throw new IllegalArgumentException();

			result[i] = (float) Math.acos(ele);
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Inverse Tangent for each element of the {@link Vector} 
	 * 'vector' and return the result as a Vector.
	 * <p>
	 * The return Vector will contain Inverse Tangent value in radian for the
	 * corresponding elements of the Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @see Math#atan(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector atan(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.atan(buffer[i]);
		}

		return new Vector(result);
	}

	/**
	 * This method will Round Off the values of each element of the {@link Vector} 'vector'
	 * to a specified precision as given by the argument 'precision' and return the result 
	 * as a Vector.
	 * <p>
	 * The return Vector will contain round off value with given decimal precision for the 
	 * corresponding elements of the Vector 'vector'.
	 * <p>The 'precision' should not be negative else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param Vector vector
	 * @param int precision
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector roundOff(Vector vector, int precision)throws IllegalArgumentException {
		
		if (precision < 0) {
			throw new IllegalArgumentException();
		}
		
		float[] buffer = vector.accessVectorBuffer();

		int scale = (int) Math.pow(10, precision);
		int L = buffer.length;
		float[] result = new float[L];
		float tmp;
		
		for (int i = 0; i < L; i++) {
			tmp = Math.round(scale * buffer[i]);
			result[i] = tmp / scale;
		}

		return new Vector(result);
	}

	/**
	 * This method will compute the Absolute value for each element of the {@link Vector} 
	 * 'vector' and return the result as a Vector.
	 * <p>
	 * The return Vector will contain absolute values for the corresponding elements of the
	 * Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @see Math#abs(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector absolute(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float[] result = new float[buffer.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = Math.abs(buffer[i]);
		}

		return new Vector(result);
	}

	/**
	 * This method will convert value of each element of the {@link Vector} 'vector' 
	 * to its decibel value and return the resultant Vector containing the corresponding
	 * decibel values.
	 * <p>
	 * If any element of the Vector 'vector' has value less than or equal to 
	 * '0', this method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector vector
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector decibel(Vector vector) throws IllegalArgumentException
	{
		int l = vector.length();
		
		float[] buffer = vector.accessVectorBuffer();
		float[] result = new float[l];
		float ele;
		
		for(int i=0;i<l;i++)
		{
			ele = buffer[i];
			if(ele<=0)
				throw new IllegalArgumentException();
			else
			{
				result[i] = (float) MathUtils.magToDb(ele);
			}
		}
		
		return new Vector(result);
	}
	
	/**
	 * This method will multiply the elements of the {@link Vector} 'vector' by the
	 * argument 'value' and return the result as a Vector.
	 * <p>
	 * Each element of the return Vector will be the result of multiplication of
	 * the corresponding element of 'vector' with the argument 'value'.
	 * 
	 * @param float value
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector multiply(float value, Vector vector) {
		int l = vector.length();
		
		float[] result = new float[l];
		float[] buffer = vector.accessVectorBuffer();
		
		for (int i = 0; i < l; i++) {
			result[i] = buffer[i] * value;
		}

		return new Vector(result);
	}

	/**
	 * This method will perform an element by element product operation between
	 * the corresponding elements of a {@link Vector}s given by the argument 'vector1'
	 * and 'vector2' and return the result as a Vector.
	 * <p>
	 * The dimension or length of both the Vectors 'vector1' and 'vector2'
	 * should be the same or else this method will throw an DimensionMismatch
	 * Exception.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector dotProduct(Vector vector1, Vector vector2)
			throws DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}
		int l = vector1.length();
		
		float[] result = new float[l];
		float[] buffer1 = vector1.accessVectorBuffer();
		float[] buffer2 = vector2.accessVectorBuffer();

		for (int i = 0; i < l; i++) {
			result[i] = buffer1[i] * buffer2[i];
		}

		return new Vector(result);
	}

	/**
	 * This method will divide the elements of Vector 'vector1' by the
	 * corresponding element of Vector 'vector2' and return the result as a
	 * Vector.
	 * <p>
	 * The dimension or length of both the Vectors 'vector1' and 'vector2'
	 * should be the same or else this method will throw an DimensionMismatch
	 * Exception.
	 * <p>
	 * If any of the element of Vector 'vector2' is found to be zero this method
	 * will throw an DivideByZero Exception.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @throws DimensionMismatchException
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector dotDivision(Vector vector1, Vector vector2)
			throws DimensionMismatchException, DivideByZeroException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}
		int l = vector1.length();
		
		float[] result = new float[l];
		float[] buffer1 = vector1.accessVectorBuffer();
		float[] buffer2 = vector2.accessVectorBuffer();
		float ele;
		
		for (int i = 0; i < l; i++) {
			ele = buffer2[i];
			if (ele == 0) {
				throw new DivideByZeroException();
			} else {
				result[i] = buffer1[i] / ele;
			}
		}

		return new Vector(result);
	}

	/**
	 * This method will return the Vector containing the inverse (1/X) value of
	 * the corresponding element of the {@link Vector} 'vector'.
	 * <p>
	 * If any element of the Vector 'vector' is found to be zero this method
	 * will throw a DivideByZero Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector dotInverse(Vector vector) throws DivideByZeroException {
		int L = vector.length();
		float[] result = new float[L];
		float[] buffer = vector.accessVectorBuffer();
		float ele;
		
		for (int i = 0; i < L; i++) {
			ele = buffer[i];
			if (ele == 0) {
				throw new DivideByZeroException();
			} else {
				result[i] = 1.0f / ele;
			}
		}

		return new Vector(result);
	}

	/**
	 * This method will perform an element by element addition operation between
	 * the corresponding elements of the {@link Vector}s given by the argument 'vector1'
	 * and 'vector2' and return the result as a Vector.
	 * <p>
	 * The dimension of both the Vectors should be similar or this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector add(Vector vector1, Vector vector2)
			throws DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}
		int l = vector1.length();
		
		float[] result = new float[l];
		float[] buffer1 = vector1.accessVectorBuffer();
		float[] buffer2 = vector2.accessVectorBuffer();

		for (int i = 0; i < l; i++) {
			result[i] = buffer1[i] + buffer2[i];
		}

		return new Vector(result);
	}

	/**
	 * This method will subtract the elements of the Vector 'vector2' from the
	 * corresponding elements of the Vector 'vector1' and return the result as a
	 * Vector.
	 * <p>
	 * The dimension of both the Vectors should be similar or this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector subtract(Vector vector1, Vector vector2)
			throws DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}
		int l = vector1.length();
		
		float[] result = new float[l];
		float[] buffer1 = vector1.accessVectorBuffer();
		float[] buffer2 = vector2.accessVectorBuffer();

		for (int i = 0; i < l; i++) {
			result[i] = buffer1[i] - buffer2[i];
		}

		return new Vector(result);
	}

	/**
	 * This method will round off the value of the elements of the {@link Vector}
	 * 'vector' to closest integer and return the result as a Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector roundOf(Vector vector) {
		int L = vector.length();
		float[] result = new float[L];
		float[] buffer = vector.accessVectorBuffer();

		for (int i = 0; i < L; i++) {
			result[i] = Math.round(buffer[i]);
		}

		return new Vector(result);
	}
}
