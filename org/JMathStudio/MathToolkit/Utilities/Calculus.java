package org.JMathStudio.MathToolkit.Utilities;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class provides Calculus (discrete) operations on a 1D real
 * discrete function or signal represented by a {@link Vector} object.
 * <p>Note: Differentiation and Integration operation define here might not work well at
 * boundary elements.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * Calculus calculus = new Calculus();//Create an instance of Calculus.
 * 
 * Vector d = calculus.differentiation(a, dt);//Differentiate input Vector with given 
 * sampling rate/time difference 'dt'.
 * 
 * Vector i = calculus.integration(d, dt);//Integrate the differentiation output with
 * given sampling rate/time difference 'dt' to recover original Vector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class Calculus 
{
	
	/**
	 * This method will perform discrete differentiation operation on the 1D real discrete
	 * signal represented by Vector 'vector' and will return the resultant Vector.
	 * <p>The argument 'dt' specify the sampling rate or reciprocal of sampling frequency.
	 * The argument 'dt' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * @param Vector Vector
	 * @param float dt
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector differentiation(Vector Vector, float dt) throws IllegalArgumentException
	{
		float[] vector = Vector.accessVectorBuffer();
		
		if(dt<=0)
			throw new IllegalArgumentException();
		
		float[] result = new float[vector.length];
		
		result[0] = vector[0]/dt;
		
		for(int i=1;i<vector.length;i++)
			result[i] = (vector[i] - vector[i-1])/dt;
		
		return new Vector(result);
	}
	
	/**
	 * This method will perform difference operation on the 1D real discrete signal
	 * as represented by the Vector 'Vector' and will return the resultant Vector.
	 * <p>For each element of the Vector 'Vector' this operation takes its difference
	 * with the immediate prior element. For first element difference is taken with
	 * padded zero.
	 * <p>This operation is reciprocal of summation operation.
	 * @param Vector Vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector difference(Vector Vector)
	{
		float[] vector = Vector.accessVectorBuffer();
		
		float[] result = new float[vector.length];
		
		result[0] = vector[0];
		
		for(int i=1;i<vector.length;i++)
			result[i] = (vector[i] - vector[i-1]);
		
		return new Vector(result);
	}
	
	/**
	 * This method will perform discrete integration operation on the 1D real discrete
	 * signal represented by Vector 'Vector' and will return the resultant Vector.
	 * <p>The argument 'dt' specify the sampling rate or reciprocal of sampling frequency.
	 * The argument 'dt' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * @param Vector Vector
	 * @param float dt
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector integration(Vector Vector, float dt) throws IllegalArgumentException
	{
		float[] vector = Vector.accessVectorBuffer();
		
		if(dt<=0)
			throw new IllegalArgumentException();
		
		float[] result = new float[vector.length];
		
		result[0] = vector[0]*dt;
		
		for(int i=1;i<vector.length;i++)
			result[i] = (vector[i]*dt + result[i-1]);
		
		return new Vector(result);
	}
	
	/**
	 * /**
	 * This method will perform summation operation on the 1D real discrete signal
	 * as represented by the Vector 'Vector' and will return the resultant Vector.
	 * <p>For each element of the Vector 'Vector' this method take summation of all 
	 * the prior elements.
	 * <p>This operation is reciprocal of difference operation.
	 * @param Vector Vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector summation(Vector Vector)
	{
		float[] vector = Vector.accessVectorBuffer();
		
		float[] result = new float[vector.length];
		
		result[0] = vector[0];
		
		for(int i=1;i<vector.length;i++)
			result[i] = (vector[i] + result[i-1]);
		
		return new Vector(result);

	}
}
