package org.JMathStudio.SignalToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.CVectorMath;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.DivideByZeroException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;

/**
 * This class define various 1D Convolution operations on a discrete real signal.
 * <p>A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' & 'b' be a valid Vector objects.
 * 
 * Conv1DTools ct = new Conv1DTools();//Create an instance of Conv1DTools.
 * 
 * Vector linear_conv = ct.linearConv(a, b);//Compute full linear convolution between
 * signals as represented by input Vectors.
 * 
 * Vector cirr_conv = ct.circularConv(a, b, N);//Compute 'N' point circular convolution
 * between signals as represented by input Vectors.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Conv1DTools {

	private VectorTools tools = new VectorTools();

	/**
	 * This method will perform a Linear Convolution of the discrete real signal
	 * as represented by the {@link Vector} 'signal' with the discrete real
	 * impulse signal as represented by the Vector 'impulse' and return the
	 * convolution result as a {@link Vector}.
	 * <p>
	 * This method will choose either of the linear convolution algorithm,
	 * {@link #linearConvWithFFT(Vector, Vector)} or
	 * {@link #linearConvWithoutFFT(Vector, Vector)} to compute the linear
	 * convolution of the two discrete real signal. This choice will depend upon
	 * optimising the time taken to compute the convolution.
	 * <p>
	 * User can directly use the either available method if not satisfied with
	 * the optimisation.
	 * 
	 * @param Vector
	 *            signal
	 * @param Vectr
	 *            impulse
	 * @return Vector
	 * @see #linearConvWithFFT(Vector, Vector)
	 * @see #linearConvWithoutFFT(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector linearConv(Vector signal, Vector impulse) {
		if (signal.length() <= 3000 & impulse.length() <= 3000)
			return linearConvWithoutFFT(signal, impulse);
		else if (impulse.length() <= 1500)
			return linearConvWithoutFFT(signal, impulse);
		else
			return linearConvWithFFT(signal, impulse);

	}

	/**
	 * This method will perform a Linear Convolution of the discrete real signal
	 * as represented by the {@link Vector} 'signal' with the discrete real
	 * impulse signal as represented by the Vector 'impulse' and return that
	 * central portion of the convolution result of length equal to 'signal'
	 * length as a {@link Vector}.
	 * <p>
	 * This method will choose either of the linear convolution algorithm,
	 * {@link #linearConvSameWithFFT(Vector, Vector)} or
	 * {@link #linearConvSameWithoutFFT(Vector, Vector)} to compute the linear
	 * convolution of the two discrete real signal. This choice will depend upon
	 * optimising the time taken to compute the convolution.
	 * <p>
	 * User can directly use the either available method if not satisfied with
	 * the optimisation.
	 * 
	 * @param Vector
	 *            signal
	 * @param Vectr
	 *            impulse
	 * @return Vector
	 * @see #linearConvSameWithFFT(Vector, Vector)
	 * @see #linearConvSameWithoutFFT(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Vector linearConvSame(Vector signal, Vector impulse) {
		if (signal.length() <= 5000 & impulse.length() <= 5000)
			return linearConvSameWithoutFFT(signal, impulse);
		else if (impulse.length() <= 3000)
			return linearConvSameWithoutFFT(signal, impulse);
		else
			return linearConvSameWithFFT(signal, impulse);
	}

	/**
	 * This method will perform a Linear Convolution of the discrete real signal
	 * as represented by the {@link Vector} 'signal' with the discrete real
	 * impulse signal as represented by the Vector 'impulse' and return the
	 * convolution result as a {@link Vector}.
	 * <p>
	 * This method compute the linear convolution in spatial domain by directly
	 * taking the inner product between the Vector 'signal' and linearly shifted
	 * Vector 'impulse'.
	 * <p>
	 * This method does not make use of 1D FFT to compute linear convolution so
	 * will be optimised for the small to medium length signals or if either of
	 * the signal is of small length.
	 * <p>
	 * Do not use this method if 'impulse' is of very large length.
	 * <p>
	 * However it is left to user to decide when to employ the given method for
	 * computing linear convolution.
	 * <p>
	 * By default make use of {@link #linearConv(Vector, Vector)} method which
	 * will select either of the appropriate method for calculating the linear
	 * convolution.
	 * @param Vector
	 *            signal
	 * @param Vectr
	 *            impulse
	 * @return Vector
	 * @see #linearConv(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector linearConvWithoutFFT(Vector signal, Vector impulse) {

		int l1 = signal.length();
		int l2 = impulse.length();

		Vector result = new Vector(l1 + l2 - 1);

		for (int x = 0; x < result.length(); x++) {
			float tmp = 0;
			int i1 = x;
			int i2 = 0;

			while (i1 >= 0 & i2 < l2) {
				if (i1 < l1)
					tmp += signal.getElement(i1) * impulse.getElement(i2);
				i1--;
				i2++;
			}

			result.setElement(tmp, x);

		}

		return result;
	}


	/**
	 * This method will perform a Linear Convolution of the discrete real signal
	 * as represented by the {@link Vector} 'signal' with the discrete real
	 * impulse signal as represented by the Vector 'impulse' and return the
	 * convolution result as a {@link Vector}.
	 * <p>
	 * This method make use of 1D FFT to compute the linear convolution in
	 * frequency domain. Following are the steps involve,
	 * <p>
	 * <i>Resizing both the signal and impulse to appropriate length by padding
	 * the trailing zeroes.
	 * <p>
	 * 1D FFT of both the resized signal and impulse and its multiplication.
	 * <p>
	 * Inverse 1D FFT of the resultant multiplied FFT gives linear convolution
	 * result. </i>
	 * <p>
	 * This method for computing linear convolution using 1D FFT will be
	 * optimised for the medium length signals. But will not be optimised for
	 * small length signal or impulse or very large length signals as it
	 * involves calculating 1D FFT.
	 * <p>
	 * However it is left to user to decide when to employ the given method for
	 * computing linear convolution.
	 * <p>
	 * By default make use of {@link #linearConv(Vector, Vector)} method which
	 * will select either of the appropriate method for calculating the linear
	 * convolution.
	 * 
	 * @param Vector
	 *            signal
	 * @param Vectr
	 *            impulse
	 * @return Vector
	 * @see #linearConv(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector linearConvWithFFT(Vector signal, Vector impulse) {
		int N = signal.length() + impulse.length() - 1;

		try {
			FFT1D fft = new FFT1D(N);

			CVector fft1;
			if(signal.length() == N)
				fft1 = fft.fft1D(signal);
			else
				fft1 = fft.fft1D(tools.resize(signal, N));

			CVector fft2;
			if(impulse.length() == N)
				fft2 = fft.fft1D(impulse);
			else
				fft2 = fft.fft1D(tools.resize(impulse, N));

			fft1 = CVectorMath.dotProduct(fft1, fft2);

			return FFT1D.ifft(fft1);

		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will perform a Linear Convolution of the discrete real signal
	 * as represented by the {@link Vector} 'signal' with the discrete real
	 * impulse signal as represented by the Vector 'impulse' and return that
	 * central portion of the convolution result of length equal to the 'signal'
	 * length as a {@link Vector}.
	 * <p>
	 * This method compute the linear convolution in spatial domain by directly
	 * taking the inner product between the Vector 'signal' and linearly shifted
	 * Vector 'impulse'.
	 * <p>
	 * This method does not make use of 1D FFT to compute linear convolution so
	 * will be optimised for the small to medium length signals or if either of
	 * the signal is of small length.
	 * <p>
	 * Do not use this method if 'impulse' is of very large length.
	 * <p>
	 * However it is left to user to decide when to employ the given method for
	 * computing linear convolution.
	 * <p>
	 * By default make use of {@link #linearConvSame(Vector, Vector)} method
	 * which will select either of the appropriate method for calculating the
	 * linear convolution.
	 * @param Vector
	 *            signal
	 * @param Vectr
	 *            impulse
	 * @return Vector
	 * @see #linearConvSame(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector linearConvSameWithoutFFT(Vector signal, Vector impulse) {

		int l1 = signal.length();
		int l2 = impulse.length();

		Vector result = new Vector(l1);

		int shift = (impulse.length() - 1) / 2;

		for (int x = shift; x < result.length() + shift; x++) {
			float tmp = 0;
			int i1 = x;
			int i2 = 0;

			while (i1 >= 0 & i2 < l2) {
				if (i1 < l1)
					tmp += signal.getElement(i1) * impulse.getElement(i2);
				i1--;
				i2++;
			}

			result.setElement(tmp, x - shift);

		}

		return result;
	}

	/**
	 * This method will perform a Linear Convolution of the discrete real signal
	 * as represented by the {@link Vector} 'signal' with the discrete real
	 * impulse signal as represented by the Vector 'impulse' and return that
	 * central portion of the convolution result of length equal to the 'signal'
	 * length as a {@link Vector}.
	 * <p>
	 * This method make use of 1D FFT to compute the linear convolution in
	 * frequency domain. Following are the steps involve,
	 * <p>
	 * <i>Resizing both the signal and impulse to appropriate length by padding
	 * the trailing zeroes.
	 * <p>
	 * 1D FFT of both the resized signal and impulse and its multiplication.
	 * <p>
	 * Inverse 1D FFT of the resultant multiplied FFT gives linear convolution
	 * result.
	 * <p>
	 * Selecting appropriate central portion of the convolution result. </i>
	 * <p>
	 * This method for computing linear convolution using 1D FFT will be
	 * optimised for the medium length signals. But will not be optimised for
	 * small length signal or impulse or very large length signals as it
	 * involves calculating 1D FFT.
	 * <p>
	 * However it is left to user to decide when to employ the given method for
	 * computing linear convolution.
	 * <p>
	 * By default make use of {@link #linearConvSame(Vector, Vector)} method
	 * which will select either of the appropriate method for calculating the
	 * linear convolution.
	 * 
	 * @param Vector
	 *            signal
	 * @param Vectr
	 *            impulse
	 * @return Vector
	 * @see #linearConvSame(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector linearConvSameWithFFT(Vector signal, Vector impulse) {
		Vector result = linearConvWithFFT(signal, impulse);
		int shift = (impulse.length() - 1) / 2;

		try {
			return tools.subVector(result, shift, signal.length());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will perform a N point (as given by the argument 'N')
	 * Circular Convolution between the discrete real signals as represented by
	 * the {@link Vector}'s 'vector1' and 'vector2'. The result of Circular
	 * Convolution is return as an {@link Vector}.
	 * <p>
	 * If the Vectors 'vector1' and 'vector2' are less than 'N' length,
	 * appropriate zeroes will be padded to make them 'N' length vectors before
	 * circular convolution takes place.
	 * <p>
	 * The value of argument 'N' should not be less than the length of either of
	 * the Vector 'vector1' or 'vector2' else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see {@link #linearConv(Vector, Vector)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Vector circularConv(Vector vector1, Vector vector2, int N)
			throws IllegalArgumentException {
		if (N < vector1.length() || N < vector2.length()) {
			throw new IllegalArgumentException();
		}
		try {
			FFT1D fft = new FFT1D(N);

			CVector fft1;
			if(vector1.length() == N)
				fft1 = fft.fft1D(vector1);
			else
				fft1 = fft.fft1D(tools.resize(vector1, N));

			CVector fft2;
			if(vector2.length() == N)
				fft2 = fft.fft1D(vector2);
			else
				fft2 = fft.fft1D(tools.resize(vector2, N));

			fft1 = CVectorMath.dotProduct(fft1, fft2);
			return FFT1D.ifft(fft1);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will perform the de-convolution operation on the linear
	 * convolved output as represented by the {@link Vector} 'vector', when the
	 * causal impulse for convolution was as given by the Vector 'impulse'. The
	 * result of de-convolution is return as a {@link Vector}.
	 * <p>
	 * This method extract the original unknown causal signal which when
	 * convolved with the causal impulse as represented by the Vector 'impulse'
	 * gave the convoluted output as represented by the Vector 'vector'.
	 * <p>
	 * The length of the Vector 'impulse' should not be more than the length of
	 * convoluted output Vector 'vector' else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * As this method make use of 1D FFT to obtain deconvolved signal, in rare
	 * case when the 1D FFT of the 'impulse' signal has any zero values, this
	 * method will throw a DivideByZero Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param Vector
	 *            impulse
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector deconvolution(Vector vector, Vector impulse)
			throws IllegalArgumentException,DivideByZeroException {
		if (impulse.length() > vector.length()) {
			throw new IllegalArgumentException();
		}

		int N = vector.length();
		try{
			FFT1D fft = new FFT1D(N);

			CVector cfft;
			if(vector.length() == N)
				cfft = fft.fft1D(vector);
			else
				cfft = fft.fft1D(tools.resize(vector, N));

			CVector impfft;
			if(impulse.length() == N)
				impfft = fft.fft1D(impulse);
			else
				impfft = fft.fft1D(tools.resize(impulse, N));

			cfft = CVectorMath.dotDivision(cfft, impfft);

			Vector result = FFT1D.ifft(cfft);

			return tools.resize(result, N + 1 - impulse.length());

		} catch (Exception e) {
			throw new BugEncounterException();
		}		
	}

	// /**
	// * This method return the result of Circular convolution of the signal
	// with the
	// * given impulse. Argument 'N' specify the N point circular convolution,
	// where signal
	// * is padded with zeros to make it N point signal before convolution. The
	// value of 'N'
	// * should not be less than the length of the input signal.
	// * @param float[] signal
	// * @param float[] impulse
	// * @param int N
	// * @return float[]
	// * @throws IllegalArgumentException
	// */
	// public float[] circularConvolve(float[] signal,float[] impulse,int N)
	// throws IllegalArgumentException
	// {
	// if(N<signal.length)
	// {
	// throw new org.JIP.Exceptions.IllegalArgumentException();
	// }
	//		
	// if(signal.length!=N)
	// {
	// signal = new Vector().Concate(signal, new float[N-signal.length]);
	// }
	//		
	//		
	// float[] result = new float[signal.length];
	//		
	// for(int i=0;i<result.length;i++)
	// {
	// float tmp=0;
	//			
	// for(int j=0;j<impulse.length;j++)
	// {
	// tmp = tmp + impulse[j]*circularShift(signal,j)[0];
	// }
	// result[i] = tmp;
	// signal = circularShift(signal, -1);
	// }
	//		
	// return result;
	//		
	// }

}
