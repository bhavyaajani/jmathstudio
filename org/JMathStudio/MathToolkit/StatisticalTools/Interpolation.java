package org.JMathStudio.MathToolkit.StatisticalTools;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various Interpolation methods for 1D unit grid.
 * <p>
 * This essentially estimate the values of the curve define over 1D axis at any
 * points between two known points.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Interpolation {
	
	//Ensure no instances are made for utility classes.
	private Interpolation(){}
	
	/**
	 * This method return the Linear Interpolated value for the location 'p'
	 * falling between the two data points 'y1' and 'y2'.
	 * <p>
	 * The point y1 is located at 0 and point y2 is located at position 1 and
	 * the argument 'p' gives the location of the hypothetical point between
	 * 'y1' and 'y2'.
	 * <p>
	 * The value of argument 'p' should be in the range of [0 1] else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * This method assumes unit grid.
	 * 
	 * @param float y1
	 * @param float y2
	 * @param float p
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static float linearInterpolate(float y1, float y2, float p)
			throws IllegalArgumentException {
		if (p < 0 || p > 1)
			throw new IllegalArgumentException();
		else
			return (y1 * (1 - p) + p * y2);
	}

	/**
	 * This method return the Cosine Interpolated value for the location 'p'
	 * falling between the two data points 'y1' and 'y2'.
	 * <p>
	 * The point y1 is located at 0 and point y2 is located at position 1 and
	 * the argument 'p' gives the location of the hypothetical point between
	 * 'y1' and 'y2'.
	 * <p>
	 * The value of argument 'p' should be in the range of [0 1] else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * This method assumes unit grid.
	 * 
	 * @param float y1
	 * @param float y2
	 * @param float p
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static float cosineInterpolate(float y1, float y2, float p)
			throws IllegalArgumentException {
		if (p < 0 || p > 1)
			throw new IllegalArgumentException();
		else {
			float tmp = (float) ((1 - Math.cos(p * Math.PI)) / 2);
			return (y1 * (1 - tmp) + y2 * tmp);
		}
	}

	/**
	 * This method return the Cubic Interpolated value for the location 'p'
	 * falling between the data points 'y1' and 'y2' with data points 'y0' and
	 * 'y3' lying on either sides of data point 'y1' and 'y2' respectively.
	 * <p>
	 * The point 'y1' is located at 0 and point 'y2' is located at 1 and the
	 * argument 'p' gives the location of the hypothetical point between 'y1'
	 * and 'y2'.
	 * <p>
	 * The value of the argument 'p' should be in the range of [0 1] else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * This method assumes unit grid.
	 * 
	 * @param float y0
	 * @param float y1
	 * @param float y2
	 * @param float y3
	 * @param float p
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static float cubicInterpolate(float y0, float y1, float y2,
			float y3, float p)
			throws IllegalArgumentException {
		if (p < 0 || p > 1)
			throw new IllegalArgumentException();
		else {
			float a0, a1, a2, a3, tmp;

			tmp = p * p;
			a0 = y3 - y2 - y0 + y1;
			a1 = y0 - y1 - a0;
			a2 = y2 - y0;
			a3 = y1;

			return (a0 * p * tmp + a1 * tmp + a2 * p + a3);
		}
	}

	/**
	 * This method return the Hermite Interpolated value for the location 'p'
	 * falling between the data points 'y1' and 'y2' with data points 'y0' and
	 * 'y3' lying on either sides of data point 'y1' and 'y2' respectively.
	 * <p>
	 * The point 'y1' is located at 0 and point 'y2' is located at 1 and the
	 * argument 'p' gives the location of the hypothetical point between 'y1'
	 * and 'y2'.
	 * <p>
	 * The value of the argument 'p' should be in the range of [0 1] else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'tension' define the tension control which is used to
	 * tighten up the curvature at the known points. The argument 'tension'
	 * should be in the range of [-1 1] where, -1 indicate low tension, 0
	 * indicate normal tension and 1 indicate high tension. If the argument
	 * 'tension' is not within the range of [-1 1] this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The argument 'bias' is used to twist the curvature about the known
	 * points. The argument 'bias' should be in the range of [-1 1] with
	 * negative bias indicate towards first segment, positive bias indicate
	 * towards other segment and 0 indicate even. The argument 'bias' should be
	 * in the range of [-1 1] else this method will throw an IllegalArgument
	 * Exception.
	 * <p>
	 * This method assumes unit grid.
	 * 
	 * @param float y0
	 * @param float y1
	 * @param float y2
	 * @param float y3
	 * @param float p
	 * @param float tension
	 * @param float bias
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public final static float hermiteInterpolate(float y0, float y1, float y2,
			float y3, float p, float tension, float bias)
			throws IllegalArgumentException {
		if (p < 0 || p > 1)
			throw new IllegalArgumentException();
		else if (tension > 1 || tension < -1)
			throw new IllegalArgumentException();
		else if (bias > 1 || bias < -1)
			throw new IllegalArgumentException();
		else {
			float m0, m1, m2, m3;
			float a0, a1, a2, a3;

			m2 = p * p;
			m3 = m2 * p;

			m0 = (y1 - y0) * (1 + bias) * (1 - tension) / 2;
			m0 += (y2 - y1) * (1 - bias) * (1 - tension) / 2;

			m1 = (y2 - y1) * (1 + bias) * (1 - tension) / 2;
			m1 += (y3 - y2) * (1 - bias) * (1 - tension) / 2;

			a0 = 2 * m3 - 3 * m2 + 1;
			a1 = m3 - 2 * m2 + p;
			a2 = m3 - m2;
			a3 = -2 * m3 + 3 * m2;

			return (a0 * y1 + a1 * m0 + a2 * m1 + a3 * y2);
		}
	}

}
