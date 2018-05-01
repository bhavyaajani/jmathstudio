package org.JMathStudio.PlugIn;

import org.JMathStudio.DataStructure.Vector.Vector;

/**
 * This class define an abstract Point operator which operate independently on each element of the {@link Vector}.
 * <p>The class define an abstract method 'operator' which state the definition for the required
 * point operator. 
 * <p>The point operator define here shall be a real function which takes in a real scalar and gives out a
 * real scalar.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractVectorOperator {
	
	/**
	 * This method will evaluate the given Vector operator over the elements of the {@link Vector}
	 * 'vector' and return the result as a {@link Vector}.
	 * <p>The definition of the operator is as stated by the method {@link #operator(float)}.
	 * <p>The returned Vector contain the output for the given operator evaluated independently over the
	 * corresponding elements of the Vector 'vector'.
	 * @param Vector vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final Vector evaluateOverVector(Vector vector)
	{
		int length = vector.length();
		Vector result = new Vector(length);
		
		float op=0;
		
		for(int i=0;i<length;i++)
		{
			op = operator(vector.getElement(i));
			result.setElement(op, i);
		}
		
		return result;
	}
	
	/**
	 * This abstract method state the definition for the given Vector operator.
	 * <p>The operator defined here shall be a real function defined over the
	 * real scalar 'x' and return a real scalar 'y' of the form, y = f(x). 
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float operator(float x);

}

