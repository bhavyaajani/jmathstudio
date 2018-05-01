package org.JMathStudio.SignalToolkit.FilterTools;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class creates various Generic FIR Filters.
 * <p>
 * The FIR Filter will be represented by a FIRFilter object.
 * 
 * @see FIRFilter
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FIRFilterMaker {
	
	//Ensure no instances are made of Utility class.
	private FIRFilterMaker(){}
	
	/**
	 * This method will build a Moving Average (MA) smoothing FIR Filter and
	 * return the same as a FIRFilter.
	 * <p>
	 * The argument 'order' specify the order of the given filter and will have
	 * order +1 taps. The argument 'order' should be more than 0 else this
	 * method will throw an IllegalArgument Exception.
	 * 
	 * @param int order
	 * @return FIRFilter
	 * @throws IllegalArgumentException
	 * @see {@link FIRFilter}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static FIRFilter MAFilter(int order)
			throws IllegalArgumentException {
		if (order < 1)
			throw new IllegalArgumentException();

		int N = order + 1;

		float[] coeff = new float[N];

		for (int i = 0; i < coeff.length; i++)
			coeff[i] = 1.0f / N;

		return new FIRFilter(coeff);
	}

	/**
	 * This method will build an Exponential Moving Average (MA) smoothing FIR
	 * Filter and return the same as a FIRFilter.
	 * <p>
	 * The argument 'constant' specify the smoothing constant for the filter and
	 * it should be in the range of (0,1] else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The argument 'order' specify the order of the given filter and will have
	 * order number of taps. The argument 'order' should be more than 2 else
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param int order
	 * @return FIRFilter
	 * @throws IllegalArgumentException
	 * @see {@link FIRFilter}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static FIRFilter expMAFilter(float constant, int order)
			throws IllegalArgumentException {
		if (constant <= 0 || constant > 1 || order < 3)
			throw new IllegalArgumentException();

		float[] coeff = new float[order];
		coeff[0] = 1;
		float sum = 1;

		float tmp = 1 - constant;

		for (int i = 1; i < coeff.length; i++) {
			coeff[i] = constant * tmp;
			tmp *= (1 - constant);
			sum += coeff[i];
		}

		for (int i = 0; i < coeff.length; i++)
			coeff[i] = coeff[i] / sum;

		return new FIRFilter(coeff);
	}

}
