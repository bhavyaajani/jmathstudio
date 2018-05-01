package org.JMathStudio.MathToolkit.Numeric.Function1D;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This abstract class define a 1D Function [R -> R].
 * <p>
 * A Function is represented as,
 * <p>
 * y = F(x), where 'y' is the real output of the Function 'F' for real input 'x'.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractFunction1D {

	/**
	 * This abstract method gives definition for the given Function. The function should be defined
	 * for all values of float 'x' and should return a real float.
	 * 
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float F(float x);

	/**
	 * This method will estimate the numerical approximate for first order derivative of this 
	 * Function y = F(x) i.e F'(x) at point x as given by the argument 'x' with  spacing given
	 * by argument 'h'.
	 * <p>The numerical approximate is based upon the Central difference expression.
	 * <p>The argument 'h' giving the spacing should be more than '0' else this method will throw
	 * an IllegalArgument Exception.
	 * <p>Note: It is up to User to make sure that the first derivative exist at point 'x' for
	 * the given function.  
	 * @param float x
	 * @param float h
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float D1(float x,float h) throws IllegalArgumentException{
		if(h <= 0)
			throw new IllegalArgumentException();
		else
			return d1(x,h);
	}

	private float d1(float x,float h){
		return (F(x+h) - F(x-h))/2*h;
	}

	/**
	 * This method will estimate the numerical approximate for second order derivative of this 
	 * Function y = F(x) i.e F''(x) at point x as given by the argument 'x' with  spacing given
	 * by argument 'h'.
	 * <p>The numerical approximate is based upon the Central difference expression.
	 * <p>The argument 'h' giving the spacing should be more than '0' else this method will throw
	 * an IllegalArgument Exception.
	 * <p>Note: It is up to User to make sure that the second derivative exist at point 'x' for
	 * the given function.  
	 * @param float x
	 * @param float h
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float D2(float x,float h) throws IllegalArgumentException{
		if(h <= 0)
			throw new IllegalArgumentException();
		else
			return d2(x,h);
	}

	private float d2(float x,float h){
		return (F(x+h) - 2*F(x) + F(x-h))/h*h;
	}

	/**
	 * This method will evaluate the given function F(x) over an ordered set of points as defined 
	 * by the argument {@link MeshLine} 'line' and return the output of the function as a {@link Vector}.
	 * <p>The definition of the function is as stated by the method {@link #F(float)}.
	 * <p>The returned Vector contain the output of the given function over the corresponding points 
	 * of {@link MeshLine} 'line';
	 * <p><i> y[i] = F(x[i]), x[i] is the ith element from MeshLine.</i> 
	 * @param MeshLine line
	 * @return {@link Vector}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector F(MeshLine line)
	{
		Vector x = line.accessLineVector();		

		int xSize = x.length();

		Vector y = new Vector(xSize);
		float op=0;

		for(int i=0;i<xSize;i++)
		{
			op = F(x.getElement(i));
			y.setElement(op,i);			
		}

		return y;
	}

	/**
	 * This method will estimate the numerical approximation for first order derivative of given function i.e F'(x) 
	 * over an ordered set of points as defined by the argument {@link MeshLine} 'line' and return the resultant derivatives
	 * as a {@link Vector}.
	 * <p>The definition of the function is as stated by the method {@link #F(float)} & first order derivative is as
	 * defined by method {@link #D1(float, float)}.
	 * <p>The argument 'h' specify the spacing and should be more than 0 else this method will throw an IllegalArgument
	 * Exception. 
	 * <p>The returned Vector contain the numerical approximation for first order derivative of given function at the 
	 * corresponding points of {@link MeshLine} 'line';
	 * <p><i> y[i] = F'(x[i]), x[i] is the ith element from MeshLine.</i> 
	 * @param MeshLine line
	 * @param float h
	 * @return {@link Vector}
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Vector D1(MeshLine line,float h) throws IllegalArgumentException
	{
		if(h <= 0)
			throw new IllegalArgumentException();
		else{
			Vector x = line.accessLineVector();		

			int xSize = x.length();

			Vector y = new Vector(xSize);
			float op=0;

			for(int i=0;i<xSize;i++)
			{
				op = d1(x.getElement(i),h);
				y.setElement(op,i);			
			}

			return y;
		}
	}

	/**
	 * This method will estimate the numerical approximation for second order derivative of given function i.e F''(x) 
	 * over an ordered set of points as defined by the argument {@link MeshLine} 'line' and return the resultant derivatives
	 * as a {@link Vector}.
	 * <p>The definition of the function is as stated by the method {@link #F(float)} & second order derivative is as
	 * defined by method {@link #D2(float, float)}.
	 * <p>The argument 'h' specify the spacing and should be more than 0 else this method will throw an IllegalArgument
	 * Exception. 
	 * <p>The returned Vector contain the numerical approximation for second order derivative of given function at the 
	 * corresponding points of {@link MeshLine} 'line';
	 * <p><i> y[i] = F''(x[i]), x[i] is the ith element from MeshLine.</i> 
	 * @param MeshLine line
	 * @param float h
	 * @return {@link Vector}
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Vector D2(MeshLine line,float h) throws IllegalArgumentException
	{
		if(h <= 0)
			throw new IllegalArgumentException();
		else{
			Vector x = line.accessLineVector();		

			int xSize = x.length();

			Vector y = new Vector(xSize);
			float op=0;

			for(int i=0;i<xSize;i++)
			{
				op = d2(x.getElement(i),h);
				y.setElement(op,i);			
			}

			return y;
		}
	}

	/**
	 * This method plot the given Function, F(x) for range of real input 'x'.
	 * <p>
	 * The argument 'start' and 'end' specify the lower and upper range of 'x' for 
	 * plotting the given function F(x). The argument 'start' should be less than 
	 * argument 'end' else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'samples' specify the number of equi-placed values of 'x' in
	 * the given range for which F(x) is to be computed and plotted. The
	 * argument 'samples' should be more than 1 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param float start
	 * @param float end
	 * @param int samples
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(float start, float end, int samples)
	throws IllegalArgumentException{
		if (start >= end || samples < 2)
			throw new IllegalArgumentException();

		float step = (end - start) / (float) (samples - 1);

		float[] X = new float[samples];
		float[] Y = new float[samples];

		for (int i = 0; i < X.length; i++) {
			X[i] = start + i * step;
			Y[i] = this.F(X[i]);
		}

		try {
			new Vector(Y).plot(new Vector(X), "Function Curve", "X", "F(X)");

		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}
}
