package org.JMathStudio.SignalToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.Utilities.SignalGenerator;

/**
 * This class define a 1D discrete Fast Chirp Transform and its inverse on a
 * discrete real signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be valid Vector object.
 * 
 * FCT1D fct = new FCT1D();//Create an instance of FCT1D.
 * 
 * CVector coeff = fct.fct1D(a, a.length());//Compute complex FCT coefficients of signal as
 * represented by input Vector 'a'.
 * 
 * Vector recover = fct.ifct1D(coeff);//Recover original signal from its FCT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FCT1D {
	
	private VectorTools tools = new VectorTools();

	
	/**
	 * This method will apply a 'N' point discrete Fast Chirp Transform (FCT) on
	 * the discrete real signal as represented by the {@link Vector} 'vector' and
	 * return the complex coefficients of FCT as a {@link CVector}.
	 * <p>
	 * A Fast Chirp Transform is computed by applying a 1D FFT on the signal
	 * after time warping operation.
	 * <p>
	 * The time-warping operation convert the linear time axis of the signal
	 * with uniform sampling to a new chirp time axis with chirp sampling by
	 * interpolating the values for the new time axis positions from the
	 * original signal using linear interpolation. The chirp used in the 1D FCT
	 * is a quadratic linear chirp.
	 * <p>
	 * Signal will be resize to length 'N' by padding appropriate zeros if
	 * signal as represented by the argument 'vector' has length less than the
	 * argument 'N' prior to applying the transform.
	 * <p>
	 * The value of the argument 'N' should not be less than the length of the
	 * signal represented by the argument 'vector' else this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param int N
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @see {@link FFT1D}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector fct1D(Vector vector, int N) throws IllegalArgumentException {
		if (vector.length() == 1) 
		{
			CVector result = new CVector(1);
			result.setElement(vector.getElement(0),0, 0);
			return result;
		}
		if (N < vector.length()) {
			throw new IllegalArgumentException();
		}

		Vector signal = tools.resize(vector, N);

		try {
			// Index positions for the signal which is to be wrap
			// to new index positions.
			float[] index = SignalGenerator.rampUp(0, 1, N).accessVectorBuffer();

			// Get wraped index positions for given index positions of the
			// signal.
			// wrap Function "isi" is used to get new 'n' from old 'n' time axis
			// position.
			float[] wrapIndex = f1(index, N - 1);

			// Original signal is converted to new Time Axis, by interpolating
			// the values for the new Time Axis positions from Original signal.
			float[] wrapSignal = new float[wrapIndex.length];

			for (int i = 0; i < wrapSignal.length; i++) {
				wrapSignal[i] = f0(signal, wrapIndex[i]);
			}
			FFT1D fft = new FFT1D(wrapSignal.length);
			return fft.fft1D(new Vector(wrapSignal));
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will apply discrete inverse Fast Chirp Transform (IFCT) on
	 * the FCT coefficient vector as represented by the {@link CVector} 'fct' and 
	 * return the resultant real signal as a {@link Vector}.
	 * <p>
	 * This inverse discrete FCT is computed by applying 1D inverse FFT followed by
	 * un-wrapping operation.
	 * <p>
	 * The un-warping operation convert the chirp time axis with chirp sampling
	 * to a new linear time axis with uniform sampling by interpolating the
	 * values for the new index position, using linear interpolation.
	 * <p>
	 * IFCT being a lossy orthogonal transform, the result of this inverse
	 * transformation may or may not be the same as the original signal.
	 * 
	 * @param CVector
	 *            fct
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector ifct1D(CVector fct) {
		Vector ifft = FFT1D.ifft(fct);
		Vector index;
		try {
			// Index positions for the computed 'ifft' which is to be unWrap
			// to new index positions.
			index = SignalGenerator.rampUp(0, 1, ifft.length());

			// Get unWrapIndex positions for the given index position using
			// unWrap function given by "si". The signal is converted to new
			// Time Axis by interpolating values for the new Time Axis.
			float[] unwrapIndex = f9(index.accessVectorBuffer(), ifft.length() - 1);

			float[] result = new float[unwrapIndex.length];

			for (int i = 0; i < result.length; i++) {
				result[i] = f0(ifft, unwrapIndex[i]);
			}
			return new Vector(result);

		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This function unWrap the time position values as given by the array
	 * index, all values in index should be in the range of [0,N].
	 * 
	 * @param index
	 * @param N
	 * @return
	 */
	private float[] f9(float[] index, int N) {
		float[] result = new float[index.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = f7(index[i], N);
		}

		return result;
	}

	/**
	 * This function wrap the time position values as given by the array index,
	 * all values in index should be in the range of [0,N].
	 * 
	 * @param index
	 * @param N
	 * @return
	 */
	private float[] f1(float[] index, int N) {
		float[] result = new float[index.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = f3(index[i], N);
		}

		return result;

	}

	/**
	 * This method provide time unWrap function, converting a given time
	 * position to a new time position. The value of 't' should be in the range
	 * of [0,N]. output is also in the range of [0,N].This method basically
	 * compress the time axis towards left.
	 * 
	 * @param t
	 * @param N
	 * @return
	 */
	private float f7(float t, int N) {
		return (1 + (t - N) / (float) N) * t;
	}

	/**
	 * This method provide time Wrap function, converting a given time position
	 * to a new time position. The value of 't' should be in the range of [0,N].
	 * output is also in the range of [0,N]. This method basically compress the
	 * time axis towards right.
	 * 
	 * @param t
	 * @param N
	 * @return
	 */
	private float f3(float t, int N) {
		return (float) (N * Math.sqrt(t / N));
	}
	
	private float f0(Vector Vector, float X)throws IllegalArgumentException 
	{
		if (X < 0 || X > Vector.length() - 1)
			throw new IllegalArgumentException();

		int xl = (int) Math.floor(X);
		int xh = (int) Math.ceil(X);

		if (xl == xh)
			return Vector.getElement(xl);
		else
			return Vector.getElement(xl) * (xh - X) + Vector.getElement(xh)
					* (X - xl);
	}


}
