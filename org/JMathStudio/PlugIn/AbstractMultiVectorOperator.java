package org.JMathStudio.PlugIn;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.DimensionMismatchException;

/**
 * This class define an abstract multivariate point operator which operate over a list of {@link Vector}s as
 * represented by a {@link VectorStack}.
 * <p>The class define an abstract method 'operator' which state the definition for the required
 * point operator. 
 * <p>The point operator define here shall be a real function which operate on a multiple real scalars 
 * and gives out a real scalar.
 * <i>
 * <p>Y(i) = f( X1(i), X2(i), ..., Xn(i) ).
 * <p>here, 'Y' is the resultant Vector.
 * <p> 'X1 to Xn' are input Vectors with same length.
 * <p>'f' is the multivariate point operator operating on the corresponding ith real elements of input
 * Vectors 'X1 to Xn' to produce output Y(i).
 * </i>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractMultiVectorOperator {

	/**
	 * This method will evaluate the given multivariate point operator over the corresponding elements of the 
	 * input {@link Vector}s as represented by {@link VectorStack} 'stack' and return the result as a {@link Vector}.
	 * <p>All the Vectors within VectorStack 'stack' should have same length else this method will throw a
	 * DimensionMismatch Exception.
	 * <p>The definition of the point operator is as stated by the method {@link #operator(float[]).
	 * <p>The returned Vector contain the output for the given operator evaluated independently over the
	 * corresponding elements of the input Vectors.
	 * <p>Caution: 
	 * <p>1. <i>The number of input variables (dimensionality) of the given multivariate point operator is set to number
	 * of {@link Vector}s in the given VectorStack. Ensure that the number of Vectors in the input VectorStack 'stack' 
	 * satisfy the dimensionality requirement of the defined operator {@link #operator(float[])} else this method
	 * may throw an {@link ArrayIndexOutOfBoundsException}.</i>
	 * <p>2. <i> Ensure correct ordering of the Vectors in the input VectorStack 'stack' as the operator {@link #operator(float[])}
	 * receives input variables from the given Vectors in given order.</i>
	 *  
	 * @param VectorStack stack
	 * @return Vector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final Vector evaluateOverMultiVectors(VectorStack stack) throws DimensionMismatchException{

		Vector[] buffer = stack.accessVectorArray();

		if(buffer == null)
			throw new NullPointerException();

		int N = buffer.length;

		int l = buffer[0].length();

		for(int i=1;i<N;i++){
			if(!buffer[i].hasSameLength(buffer[0]))
				throw new DimensionMismatchException();
		}

		Vector res = new Vector(l);

		for(int i=0;i<l;i++){
			float[] list = new float[N];
			for(int k=0;k<N;k++){
				list[k] = buffer[k].getElement(i);
			}
			res.setElement(operator(list), i);
		}

		return res;
	}

	/**
	 * This abstract method state the definition for the given multivariate point operator.
	 * <p>The operator defined here shall be a real function defined over multiple real scalars
	 * as given by array 'list' and return a real scalar 'y' of the form, y = f(x1,x2,...xn).
	 * <p>The input array 'list' provide order set of elements from the corresponding location within
	 * all the input Vectors as represented by the VectorStack (see {@link #evaluateOverMultiVectors(VectorStack)}).
	 * The order of input variables within the array 'list' is similar to the order of Vectors in the input
	 * VectorStack. 
	 * @param float[] list
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float operator(float[] list);
}
