package org.JMathStudio.SignalToolkit.FilterTools;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;
import org.JMathStudio.SignalToolkit.Utilities.SignalGenerator;

/**
 * This class define a causal FIR Digital Filter characterised by its Numerator
 * coefficients.
 * <p>
 * This filter will be a direct form II transposed implementation of the
 * standard difference equation.
 * <pre>Usage:
 * float[] coeff = new float[]{0.3f,0.3f,0.3f};//Numerator coefficients for FIRFilter.
 * 
 * FIRFilter fir = new FIRFilter(coeff);//Create an instance of FIRFilter.
 * 
 * Let 'signal' be a valid Vector object.
 * Vector result = fir.filter(signal);//Filter input signal as represented by Vector.
 * 
 * Vector imp = fir.getImpulseResponse(128);//Generate 128 points impulse response
 * of given FIRFilter.
 * 
 * fir.showMagnitudeResponse(128);//Display 128 points Magnitude response of given
 * FIRFilter.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FIRFilter {
	private float[] numCoeff;

	/**
	 * This will build a causal FIR Digital filter with the Numerator
	 * coefficients as given by the float 1D array 'numCoeff'.
	 * <p>
	 * The FIR Digital Filter so created will be a causal filter with direct
	 * form II transposed implementation of the standard difference equation.
	 * <p>
	 * The Numerator coefficients for the given filter as represented by the 1D
	 * float array 'numCoeff' will be the coefficients for the corresponding
	 * past inputs as given by its index position.
	 * <p>The argument 'numCoeff' is passed by reference and no deep copy of the same
	 * is made.
	 * 
	 * @param float[] numCoeff
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public FIRFilter(float[] numCoeff) {
		this.numCoeff = numCoeff;
	}

	/**
	 * This method will return the Numerator coefficients of the given FIR
	 * Digital Filter as a Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessNumeratorCoefficients() {
		return new Vector(this.numCoeff);
	}

	/**
	 * This method will filter the discrete real signal as represented by the
	 * Vector 'vector' and return the filtered signal as a Vector.
	 * <p>
	 * The length of the return signal/Vector will be similar to that of the
	 * input signal/Vector.
	 * <p>
	 * The filter operation can be summarised as follows,
	 * <p>
	 * <i>Y[n] = (E(X(n-i)*numCoeff(i)))
	 * <p>
	 * where, i ->[0 numCoeff.length-1].
	 * <p>
	 * numCoeff is the numerator coefficients of this filter.
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

		}

		return new Vector(result);
	}

	/**
	 * <p>
	 * This method will plot the 'N' point Magnitude response of this Digital
	 * FIR filter.
	 * <p>
	 * The argument 'N' specify the 'N' equi-distant frequency components in the
	 * range of [-pi pi], for plotting the magnitude response.
	 * <p>
	 * The argument 'N' should be a positive integer greater than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * 
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
	 * This method will plot the 'N' point Phase response of this Digital FIR
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
			phases = f3(phases);

		phases.plot(freq, "Phase Response", "Frequency - Radian",
					"Phase - Radian");

		} catch (Exception e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will compute the Impulse response of the given Digital FIR
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
	 * This method will return the Frequency response of the given Digital FIR
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
	 * <p>The argument 'N' should be a positive integer greater than 0 else this method will throw
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

	private void f2(float[] phase) {
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

	private boolean f0(float[] phase) {
		for (int i = 1; i < phase.length; i++) {
			if (Math.abs(phase[i] - phase[i - 1]) >= Math.PI) {
				return false;
			}
		}

		return true;

	}

	private Vector f3(Vector vector) {
		Vector result = vector.clone();

		while (!f0(result.accessVectorBuffer())) {
			f2(result.accessVectorBuffer());
		}

		return result;
	}
}
