package org.JMathStudio.SignalToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.DataStructure.Vector.VectorTools;

/**
 * This class define basic Signal Processing operations on a discrete signal.
 * <p>
 * A discrete signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * SignalUtilities su = new SignalUtilities();//Create an instance of SignalUtilities.
 * 
 * Vector auto_corr = su.autoCorrelation(a);//Compute auto correlation for signal as
 * represented by input Vector.
 * 
 * Vector res1 = su.downSample(a, l);//Down sample input Vector to new length 'l'.
 * 
 * Complex coeff = su.goertzelFrequencyAnalysis(a, W);//Get goertzel frequency analysis for
 * discrete frequency 'W' within signal as represented by input Vector.
 * </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SignalUtilities {

	private Conv1DTools conv = new Conv1DTools();
	private VectorTools tools = new VectorTools();
	
	/**
	 * This method will return the Auto Correlation of the given discrete real
	 * signal as represented by the {@link Vector} 'vector'.
	 * <p>
	 * The auto correlation of the signal will be return as a {@link Vector} in
	 * which the origin will be located at the centre.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @see #crossCorrelation(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector autoCorrelation(Vector vector) {
		return crossCorrelation(vector, vector);
	}

	/**
	 * This method will perform the Cross Correlation operation between the two
	 * discrete real signals as represented by {@link Vector}'s 'vector1' and
	 * 'vector2' and return the same as a {@link Vector}.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @see #autoCorrelation(Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector crossCorrelation(Vector vector1, Vector vector2) {
		return conv.linearConv(vector1, tools.flip(vector2));
	}

	/**
	 * This method will up sample the discrete real signal as represented by the
	 * {@link Vector} 'vector' to a new length as specified by the arguments 'L'
	 * and return the resultant up sampled Signal as a {@link Vector}.
	 * <p>
	 * The argument 'L' should be more than the length of the Vector 'vector'
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The resultant up-sampled signal will be of length as given by arguments
	 * 'L'. Thus this method gives an effective up sampling by the factor of L/l
	 * of the original signal, where 'l' is the length of the original signal.
	 * <p>
	 * All new positions are filled up by linear interpolation from original
	 * signal positions.
	 * 
	 * @param Vector
	 *            vector
	 * @param int L
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see #downSample(Vector, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector upSample(Vector vector, int L)
			throws IllegalArgumentException {
		int l = vector.length();

		if (L <= l)
			throw new IllegalArgumentException();

		Vector res = new Vector(L);

		double mapper = (l - 1.0) / (L - 1.0);

		float mapX;

		for (int i = 0; i < L; i++) {
			mapX = (float) (i * mapper);

			if (mapX > l - 1)
				mapX = l - 1;

			int lx = (int) Math.floor(mapX);
			int hx = (int) Math.ceil(mapX);
			float dx = mapX - lx;

			float value = vector.getElement(lx) * (1 - dx)
					+ vector.getElement(hx) * dx;
			res.setElement(value, i);
		}

		return res;

	}

	/**
	 * This method will down sample the discrete real signal as represented by
	 * the {@link Vector} 'vector' to a new length as specified by the arguments
	 * 'L' and return the resultant down sampled signal as a {@link Vector}.
	 * <p>
	 * The argument 'L' should be less than the length of the Vector 'vector'
	 * else this method will throw an IllegalArgument Exception. Also the
	 * arguments 'L' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The resultant down-sampled signal will be of length as given by arguments
	 * 'L'. Thus this method gives an effective down sampling by the factor of
	 * l/L of the original signal, where 'l' is the length of the original
	 * signal.
	 * <p>
	 * All new positions are filled up by linear interpolation from original
	 * signal positions.
	 * 
	 * @param Vector
	 *            vector
	 * @param int L
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see #upSample(Vector, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector downSample(Vector vector, int L)
			throws IllegalArgumentException {
		int l = vector.length();

		if (L <= 0 | L >= l)
			throw new IllegalArgumentException();

		Vector res = new Vector(L);

		double mapper = (l - 1.0) / (L - 1.0);

		float mapX;

		for (int i = 0; i < L; i++) {
			mapX = (float) (i * mapper);

			if (mapX > l - 1)
				mapX = l - 1;

			int lx = (int) Math.floor(mapX);
			int hx = (int) Math.ceil(mapX);
			float dx = mapX - lx;

			float value = vector.getElement(lx) * (1 - dx)
					+ vector.getElement(hx) * dx;
			res.setElement(value, i);
		}

		return res;

	}

	/**
	 * This method will return a discrete periodic signal as a {@link Vector}
	 * which contain the the given discrete real signal as represented by the
	 * {@link Vector} 'vector' as one period.
	 * <p>
	 * The argument 'size' specify the length of the return Vector or define the
	 * cycles of given signal in its periodic version.
	 * <p>
	 * The value of the argument 'size' should not be less than the length of
	 * the given signal or (at-least one cycle) as represented by the Vector
	 * 'vector' else this method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param int size
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector makePeriodic(Vector vector, int size)
			throws IllegalArgumentException {
		if (size < vector.length()) {
			throw new IllegalArgumentException();
		} else {
			float[] signal = vector.accessVectorBuffer();
			float[] result = new float[size];

			for (int i = 0; i < size; i++) {
				result[i] = signal[i % signal.length];
			}

			return new Vector(result);
		}
	}

	// /**
	// * This method return the circular shift version of the input signal. The
	// amount and direction
	// * of shift depend upon the argument 'space'. If space is negative shift
	// will be from
	// * right to left, with left shifted part of signal padded on the right
	// side. If space is positive shift
	// * will be from left to right, with right shifted part of signal padded on
	// the left side.
	// * @param float[] signal
	// * @param int space
	// * @return float[]
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public float[] circularShift(float[] signal,int space)
	// {
	// if(space>= 0)
	// {
	// for(int i=0;i<space;i++)
	// {
	// float tmp = signal[signal.length-1];
	// signal = shiftRight(signal, 1);
	// signal[0] = tmp;
	//				
	// }
	// return signal;
	// }
	// else
	// {
	// for(int i=0;i<-space;i++)
	// {
	// float tmp = signal[0];
	// signal = shiftLeft(signal,1);
	// signal[signal.length-1] = tmp;
	//				
	// }
	// return signal;
	// }
	// }

	/**
	 * This method return the discrete frequency 'W' content in the discrete
	 * real signal as represented by the {@link Vector} 'vector' as a
	 * {@link Complex} coefficient.
	 * <p>
	 * The argument 'W' should specify a normalized frequency in the range of
	 * [0,1) else this method will throw an IllegalArgument Exception.
	 * <p>
	 * This operation is similar to FFT, but unlike FFT which analyse
	 * equi-distant frequencies, goertzel analysis only analyse for the specific
	 * discrete frequency component in the signal.
	 * <p>
	 * This method does not compute FFT of the signal, but uses goertzel
	 * algorithm to determine the the required frequency component of the
	 * signal.
	 * <p>
	 * The Absolute and Angle of the return complex coefficient will give the
	 * Magnitude and Phase of the required discrete frequency component in the
	 * given signal.
	 * 
	 * @param Vector
	 *            vector
	 * @param float W
	 * @return Complex
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex goertzelFrequencyAnalysis(Vector vector, float W)
			throws IllegalArgumentException {
		if (W < 0 || W >= 1) {
			throw new IllegalArgumentException();
		}

		float realW = (float) (2.0 * Math.cos(2.0 * Math.PI * W));
		float imagW = (float) Math.sin(2.0 * Math.PI * W);

		float d1 = 0.0f;
		float d2 = 0.0f;
		float y = 0;
		float[] signal = vector.accessVectorBuffer();

		for (int n = 0; n < signal.length; ++n) {
			y = signal[n] + realW * d1 - d2;
			d2 = d1;
			d1 = y;
		}

		return new Complex(0.5f * realW * d1 - d2, imagW * d1);

	}

	/**
	 * This method will perform an Even/Odd decomposition of the discrete real
	 * signal as represented by the {@link Vector} 'vector' and return the Even
	 * and Odd components as a {@link VectorStack}.
	 * <p>
	 * The 0th and 1st index Vector in the VectorStack will be the even and odd
	 * component respectively.
	 * <p>
	 * This operation decomposes a signal in to an even symmetric signal and an
	 * odd symmentric signal such that the addition of the two will give the
	 * original signal.
	 * <p>
	 * The length of both the even and odd symmetric signal will be the same and
	 * equal to the length of the original signal.
	 * 
	 * @param Vector
	 *            vector
	 * @return VectorStack
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack evenOddDecomposition(Vector vector) {
		float[] signal = vector.accessVectorBuffer();

		float[][] result = new float[2][signal.length];

		for (int i = 0; i < signal.length; i++) {
			result[0][i] = (signal[i] + signal[signal.length - 1 - i]) / 2;
			result[1][i] = (signal[i] - signal[signal.length - 1 - i]) / 2;
		}

		VectorStack stack = new VectorStack();
		for (int i = 0; i < result.length; i++)
			stack.addVector(new Vector(result[i]));

		return stack;
	}

	/**
	 * This method will perform an Interlaced decomposition of the discrete real
	 * signal as represented by the {@link Vector} 'vector' and return the Even
	 * and Odd part as a {@link VectorStack}.
	 * <p>
	 * The 0th and 1st index Vector in the VectorStack will be the even and odd
	 * part respectively.
	 * <p>
	 * This operation divides the signal in to its even and odd part of length
	 * as that of the signal such that the even part has all the even index
	 * elements and zeros for all odd index position of the signal and odd part
	 * has all the odd index elements and zeros for all the even index position
	 * of the signal .
	 * 
	 * @param Vector
	 *            vector
	 * @return {@link VectorStack}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack interlacedDecomposition(Vector vector) {
		float[] signal = vector.accessVectorBuffer();

		float[][] result = new float[2][signal.length];

		for (int i = 0; i < signal.length; i++) {
			if (i % 2 == 0)
				result[0][i] = signal[i];
			else
				result[1][i] = signal[i];
		}
		VectorStack stack = new VectorStack();
		for (int i = 0; i < result.length; i++)
			stack.addVector(new Vector(result[i]));

		return stack;

	}

	/**
	 * This method corrects the radian phase angles as given by the
	 * {@link Vector} vector by adding multiples of (+/- 2 PI) when absolute
	 * jumps between consecutive elements of the 'vector' are greater than or
	 * equal to the default jump tolerance of PI radians.
	 * <p>
	 * The corrected phase angles are returned as a Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector unwrap(Vector vector) {
		Vector result = vector.clone();

		while (!f3(result.accessVectorBuffer())) {
			f7(result.accessVectorBuffer());
		}

		return result;
	}

	private void f7(float[] phase) {
		for (int i = 1; i < phase.length; i++) {
			if (Math.abs(phase[i] - phase[i - 1]) >= Math.PI) {
				float diff = phase[i] - phase[i - 1];

				if (Math.signum(diff) == 1) {
					phase[i] = (float) (phase[i] - 2 * Math.PI);
				} else if (Math.signum(diff) == -1) {
					phase[i] = (float) (phase[i] + 2 * Math.PI);
				}
			}
		}
	}

	private boolean f3(float[] phase) {
		for (int i = 1; i < phase.length; i++) {
			if (Math.abs(phase[i] - phase[i - 1]) >= Math.PI) {
				return false;
			}
		}

		return true;

	}
}
