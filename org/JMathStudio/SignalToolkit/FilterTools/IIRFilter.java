package org.JMathStudio.SignalToolkit.FilterTools;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;
import org.JMathStudio.SignalToolkit.Utilities.SignalGenerator;

/**
 * This class define a causal IIR Digital Filter characterised by a set of
 * Numerator and Denominator coefficients.
 * <p>
 * This filter will be a direct form II transposed implementation of the
 * standard difference equation.
 * <pre>Usage:
 * float[] a = new float[]{0.1f,0.8f,0.1f};//Numerator coefficients for IIRFilter.
 * float[] b = new float[]{1,-1};//Denominator coefficients for IIRFilter.
 * 
 * IIRFilter iir = new IIRFilter(a,b);//Create an instance of IIRFilter.
 * 
 * Let 'signal' be a valid Vector object.
 * Vector result = iir.filter(signal);//Filter input signal as represented by Vector.
 * 
 * Vector imp = iir.getImpulseResponse(256);//Generate 256 points impulse response
 * of given IIRFilter.
 *	
 * iir.showMagnitudeResponse(256);//Display 256 points Magnitude response of given
 * IIRFilter.	
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class IIRFilter {
	
	private float[] numCoeff;
	private float[] denCoeff;

	/**
	 * This will build a causal IIR Digital filter with Numerator and
	 * Denominator coefficients as given by the float 1D array 'numCoeff' and
	 * 'denomCoeff' respectively.
	 * <p>
	 * If the first element of the array 'denomCoeff' is zero this method will
	 * throw an IllegalArgumentException.
	 * <p>
	 * The IIR Digital Filter so created will be a causal filter with direct
	 * form II transposed implementation of the standard difference equation.
	 * <p>
	 * The Numerator and Denominator coefficients for the given filter as
	 * represented by the 1D float array 'numCoeff' and 'denomCoeff'
	 * respectively will be the coefficients for the corresponding past input
	 * and output as given by its index position.
	 * <p>
	 * The arguments 'numCoeff' and 'denomCoeff' are passed by reference and no deep
	 * copy of the same is made.
	 * 
	 * @param float[] numCoeff
	 * @param float[] denomCoeff
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public IIRFilter(float[] numCoeff, float[] denomCoeff)
			throws IllegalArgumentException {
		if (denomCoeff[0] == 0)
			throw new IllegalArgumentException();

		this.numCoeff = numCoeff;
		this.denCoeff = denomCoeff;
	}

	/**
	 * This method will return the Numerator coefficients of the given IIR
	 * Digital Filter as a Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessNumeratorCoefficients() {
		return new Vector(this.numCoeff);
	}

	/**
	 * This method will return the Denominator coefficients of the given IIR
	 * Digital Filter as a Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessDenominatorCoefficients() {
		return new Vector(this.denCoeff);
	}

	/**
	 * This method will filter the discrete real signal as represented by the
	 * Vector 'vector' and return the filtered signal as a Vector.
	 * <p>
	 * The length of the return signal will be similar to that of the input
	 * signal.
	 * <p>
	 * The filter operation can be summarised as follows,
	 * <p>
	 * <i>Y[n] = (E(X(n-i)*numCoeff(i)) - E(Y(n-j)*denCoeff(j)))
	 * <p>
	 * where, i ->[0 numCoeff.length-1] and j -> [1 denCoeff.length-1].
	 * <p>
	 * numCoeff and denCoeff respectively are the numerator and denominator
	 * coefficients of this filter.
	 * <p>
	 * Y[n] is the nth output,
	 * <p>
	 * X[n] is the nth input/signal as represented by Vector 'vector',
	 * <p>
	 * E is the summation operator.</i>
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector filter(Vector vector) {
		if (denCoeff[0] == 0)
			throw new BugEncounterException();

		float[] signal = vector.accessVectorBuffer();
		float[] result = new float[signal.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = 0;
			int index = i;

			for (int bi = 0; bi <= i; bi++) {
				if (bi < numCoeff.length && index >= 0) {
					result[i] = result[i] + numCoeff[bi] * signal[index--];
				}
			}

			index = i - 1;
			for (int ai = 1; ai <= i; ai++) {
				if (ai < denCoeff.length && index >= 0) {
					result[i] = result[i] - denCoeff[ai] * result[index--];
				}
			}
		}

		return new Vector(result);
	}

	/**
	 * <p>
	 * This method will plot the 'N' point Magnitude response of this Digital
	 * IIR filter.
	 * <p>
	 * The argument 'N' specify the 'N' equi-distant frequency components in the
	 * range of [-pi pi], for plotting the magnitude response.
	 * <p>
	 * The argument 'N' should be a positive integer greater than 0 else this method will throw
	 * an IllegalArgument Exception.
	  
	 * @param int N
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showMagnitudeResponse(int N)
			throws IllegalArgumentException {
		Vector impulseResponse = getImpulseResponse(N);
		try{
		FFT1D fft_ = new FFT1D(N);
		
		CVector fft = fft_.fft1D(impulseResponse);
		fft = FFT1D.centre(fft);

		Vector freq = SignalGenerator.rampUp(0, 1, fft.length());

		if (fft.length() != 1) {
			freq = VectorMath.linear((float) (Math.PI * 2.0f / (fft
					.length() - 1)), (float) -Math.PI, freq);
		}

		Vector magnitude = fft.getMagnitude();
		magnitude.plot(freq, "Magnitude Response", "Frequency - Radian",
					"Amplitude");

		} catch (Exception e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * <p>
	 * This method will plot the 'N' point Phase response of this Digital IIR
	 * filter.
	 * <p>
	 * The argument 'N' specify the 'N' equi-distant frequency components in the
	 * range of [-pi pi], for plotting the phase response.
	 * <p>
	 * The argument 'N' should be a positive integer greater than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * <p>
	 * The argument 'wrapPhase' if 'True', this method will plot the phase
	 * response with wrapped phases such that discontinuity between phases will
	 * not be more than 2*PI.
	 * 
	 * @param int N
	 * @param boolean wrapPhase
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showPhaseResponse(int N, boolean wrapPhase)
			throws IllegalArgumentException {
		Vector impulseResponse = getImpulseResponse(N);
		try{
		FFT1D fft_ = new FFT1D(N);
		
		CVector fft = fft_.fft1D(impulseResponse);
		fft = FFT1D.centre(fft);

		Vector freq = SignalGenerator.rampUp(0, 1, fft.length());

		if (fft.length() != 1) {
			freq = VectorMath.linear((float) (Math.PI * 2.0f / (fft
					.length() - 1)), (float) -Math.PI, freq);
		}

		Vector phases = fft.getAngle();

		if (wrapPhase)
			phases = f5(phases);

		phases.plot(freq, "Phase Response", "Frequency - Radian",
					"Phase - Radian");
		} catch (Exception e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will compute the Impulse response of the given Digital IIR
	 * Filter and return the same as a Vector.
	 * <p>
	 * The argument 'N' here specify the 'N' point impulse response thus the
	 * length of the return Vector will be 'N'. Argument 'N' should be more than
	 * 0 else this method will throw an IllegalArgument Exception.
	 * <p>
	 * This method computes a causal impulse response of a given causal filter.
	 * 
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector getImpulseResponse(int N)
			throws IllegalArgumentException {
		if (N < 1)
			throw new IllegalArgumentException();

		Vector impulse = new Vector(N);
		impulse.setElement(1, 0);

		return filter(impulse);
	}

	/**
	 * This method will return the Frequency response of the given Digital IIR
	 * filter in Fourier space as a CVector.
	 * <p>
	 * The Frequency response of the filter is the 'N' point FFT of its impulse
	 * response.
	 * <p>
	 * Thus complex coefficients of the FFT representing the frequency response
	 * of this filter in Fourier space is return as a CVector.
	 * <p>
	 * The argument 'N' here specify the N point frequency response for the
	 * given filter. Thus the return CVector containing 'N' complex
	 * coefficients, represents frequency response for 'N' equi-spaced digital
	 * frequencies in the range of [0, 2*PI]. 
	 * <p>
	 * The argument 'N' should be a positive integer greater than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * <p>
	 * The return CVector will have non centred FFT coefficients or frequency
	 * response i.e. DC response is not in the centre of the CVector. See
	 * {@link FFT1D#fft1D(Vector, int)} method for further clarification on this
	 * issue.
	 * 
	 * @param int N
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector frequencyResponse(int N)
			throws IllegalArgumentException {
		Vector h = getImpulseResponse(N);
		return new FFT1D(N).fft1D(h);
	}

	/**
	 * This method will Normalise the filter coefficients of this Digital IIR
	 * Filter.
	 * <p>
	 * Normalisation is achieved by dividing both numerator and denominator
	 * coefficients of this filter by the first denominator coefficient.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 **/
	public void normalise() {
		if (denCoeff[0] != 1) {
			float factor = denCoeff[0];

			for (int i = 0; i < numCoeff.length; i++) {
				numCoeff[i] = numCoeff[i] / factor;
			}
			for (int i = 0; i < denCoeff.length; i++) {
				denCoeff[i] = denCoeff[i] / factor;
			}
		}
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

	private Vector f5(Vector vector) {
		Vector result = vector.clone();

		while (!f3(result.accessVectorBuffer())) {
			f7(result.accessVectorBuffer());
		}

		return result;
	}
}
