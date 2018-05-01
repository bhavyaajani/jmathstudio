package org.JMathStudio.SignalToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.CVectorTools;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a 1D discrete Fast Fourier Transform (FFT) and its inverse
 * on a discrete real signal and a discrete complex signal.
 * <p>
 * Mix radix algorithm is employed for the fast computation of fourier
 * transform.
 * <p>
 * A discrete real and complex signal will be represented respectively by a
 * {@link Vector} and a {@link CVector} object. The computed FFT coefficients
 * shall be return as a {@link CVector}.
 * <p>
 * The returned CVector containing the 'N' points fourier coefficients will be
 * non centred with the DC fourier coefficient as the first coefficient followed
 * by fourier coefficients for the first (N-1)/2 positive and first (N-1)/2 or
 * ((N-1)/2)-1 (if N is odd/even) negative frequencies respectively.
 * <p>
 * Though this class supports arbitrary points FFT, the performance is not
 * uniform for all. Best performance is obtained for power of 2 points FFT and
 * worst for prime length FFT.
 * 
 * <pre>
 * Usage:
 * Let 'a' be a valid Vector object.
 * 
 * CVector fft = FFT1D.fft(a, a.length());//Compute complex FFT coefficients of signal as
 * represented by input Vector 'a'.
 * 
 * Vector recover = FFT1D.ifft(fft);//Recover original signal from its FFT coefficients.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FFT1D {

	// The number of DFT is factorized.
	//
	// Some short FFTs, such as length 2, 3, 4, 5, 8, 10, are used
	// to improve the speed.
	//
	//
	// A getOrder() function is used to make sure FFT can be calculated
	// in place.
	//
	// A twiddleFactor() function is used to perform the FFT.

	// Maximum numbers of factors allowed.
	// private static final int MaxFactorsNumber = 30;
	// private static final int MaxNumbersOfFactors = 37;

	// OnetoSqrt2 = 1/sqrt(2), used in fft8().

	// private static int finalRadix = 0;

	private int i0; // length of N point FFT.
	private int i1; // Number of factors of N.

	// This specify maximum biggest factor.
	// private static final int maxFactor=1000; // Maximum factor of N.
	// This specify maximum numbers of factors.
	// private static final int maxNosFactor = 37;

	private int i2[]; // Factors of N processed in the current stage.
	private int i3[]; // Finished factors before the current stage.
	private int i4[]; // Finished factors after the current stage.

	private float f1[], f2[]; // Input of FFT.
	private float f3[], f4[]; // Intermediate result of FFT.

	// Do not allow direct access or modifications on this outputs as for each
	// fft,
	// output arrays are passed as reference into the return result.
	// For next fft, output arrays are created new and thus does not impact the
	// last
	// result.
	private float f5[], f6[]; // Output of FFT.

	/**
	 * <p>
	 * This will create an instance of FFT1D class for computing 'N' point 1D
	 * fast fourier transform of either a discrete real signal or a discrete
	 * complex signal. An 'N' point FFT can only be applied over signals having
	 * length 'N'.
	 * <p>
	 * The same instance can be used to compute 'N' points 1D inverse fast
	 * fourier transform from the fourier coefficients.
	 * <p>
	 * The argument 'N' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The constructor will pre-compute the associated cache and entries
	 * required for computing 'N' point FFT which result in increase speed for
	 * computation.
	 * <p>
	 * A single instance of a 'N' point FFT1D class can be used to compute
	 * multiple 'N' points FFT's of signals and 'N' points inverse FFT's from
	 * coefficients; both having length 'N'. However, to compute multiple
	 * arbitrary points FFT's and/or its inverse, it is necessary to create
	 * different instance of this class for different arbitrary points.
	 * <p>
	 * Use helper methods {@link #fft(Vector, int)} and
	 * {@link #fft(CVector, int)} to compute arbitrary points FFT's of an
	 * arbitrary length signal without creating multiple instances of this
	 * class. Use helper methods {@link #ifft(CVector)} and
	 * {@link #ifftComplex(CVector)} to compute arbitrary points inverse FFT's
	 * of real and complex signals respectively without creating multiple
	 * instances of this class.
	 * 
	 * @param int N
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public FFT1D(int N) throws IllegalArgumentException {
		if (N < 1)
			throw new IllegalArgumentException();

		this.i0 = N;
		if (this.i0 > 1)
			f18();

		this.i9 = (int) (Math.log(N) / Math.log(2));

		// Only applying radix2 algo if signal is power of 2 length.
		// Check if N is a power of 2
		if (N == (1 << i9)) {
			f0();
			b0 = true;
		}
	}

	/**
	 * This method will return the number of points FFT supported by this FFT1D
	 * object.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getFFTPoints() {
		return this.i0;
	}

	/**
	 * <p>
	 * This method will apply a 'N' point 1D discrete Fast Fourier Transform
	 * (FFT) on the discrete real signal as represented by the Vector 'signal'
	 * and return the fourier coefficients as a CVector.
	 * <p>
	 * The value of the argument 'N' should be equal to or more than the length
	 * of the Vector 'signal' else this method will throw an IllegalArgument
	 * Exception.
	 * <p>
	 * The signal represented by the Vector 'signal' will be padded with
	 * appropriate number of trailing zeros so as to make its length equal to
	 * the argument 'N' before applying the FFT.
	 *  
	 * @param Vector
	 *            signal
	 * @param int N
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @see {@link #ifft(CVector)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector fft(Vector signal, int N)
			throws IllegalArgumentException {

		if (N < signal.length())
			throw new IllegalArgumentException();
		else {
			try {
				FFT1D fft = new FFT1D(N);
				if (signal.length() == N)
					return fft.fft1D(signal);
				else
					return fft.fft1D(new VectorTools().resize(signal, N));
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * <p>
	 * This method will apply a 'N' point 1D discrete Fast Fourier Transform
	 * (FFT) on the discrete complex signal as represented by the CVector
	 * 'signal' and return the fourier coefficients as a CVector.
	 * <p>
	 * The value of the argument 'N' should be equal to or more than the length
	 * of the CVector 'signal' else this method will throw an IllegalArgument
	 * Exception.
	 * <p>
	 * The signal represented by the CVector 'signal' will be padded with
	 * appropriate number of trailing zeros so as to make its length equal to
	 * the argument 'N' before applying the FFT.
	 *  
	 * @param CVector
	 *            signal
	 * @param int N
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @see {@link #ifftComplex(CVector)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector fft(CVector signal, int N)
			throws IllegalArgumentException {

		if (N < signal.length())
			throw new IllegalArgumentException();
		else {
			try {
				FFT1D fft = new FFT1D(N);
				if (signal.length() == N)
					return fft.fft1D(signal);
				else
					return fft.fft1D(new CVectorTools().resize(signal, N));
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * <p>
	 * This method will apply a 'N' point 1D discrete Fast Fourier Transform
	 * (FFT) on the discrete complex signal as represented by the CVector
	 * 'signal' and return the fourier coefficients as a CVector.
	 * <p>
	 * The number of points FFT 'N' is decided by the argument passed to the
	 * constructor.
	 * <p>
	 * For 'N' points FFT the length of the CVector 'signal' should be 'N' else
	 * this method will throw an IllegalArgument Exception.
	 *  
	 * @param CVector
	 *            signal
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector fft1D(CVector signal) throws IllegalArgumentException {
		if (signal.length() != i0)
			throw new IllegalArgumentException();
		else if (this.i0 == 1) {
			CVector result = new CVector(1);
			result.setElement(signal.getElement(0), 0);
			return result;
		}
		// else if(isRadix2)
		// return fft2(signal);
		else {
			this.f1 = signal.accessRealPart().accessVectorBuffer();
			this.f2 = signal.accessImaginaryPart().accessVectorBuffer();

			this.f4 = new float[i0];
			this.f3 = new float[i0];

			this.f5 = new float[i0];
			this.f6 = new float[i0];

			f11();

			for (int factorIndex = 0; factorIndex < i1; factorIndex++) {
				f20(factorIndex);
			}

			try {
				return new CVector(f5, f6);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}

		}
	}

	/**
	 * <p>
	 * This method will apply a 'N' point 1D discrete Fast Fourier Transform
	 * (FFT) on the discrete real signal as represented by the Vector 'signal'
	 * and return the fourier coefficients as a CVector.
	 * <p>
	 * The number of points FFT 'N' is decided by the argument passed to the
	 * constructor.
	 * <p>
	 * For 'N' points FFT the length of the Vector 'signal' should be 'N' else
	 * this method will throw an IllegalArgument Exception.
	 *  
	 * @param Vector
	 *            signal
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector fft1D(Vector signal) throws IllegalArgumentException {
		if (signal.length() != i0)
			throw new IllegalArgumentException();
		// Only applying radix2 algo if signal is power of 2 length and is real.
		else if (this.i0 == 1) {
			CVector result = new CVector(1);
			result.setElement(signal.getElement(0), 0, 0);
			return result;
		} else if (b0)
			return f13(signal);
		else {
			this.f1 = signal.accessVectorBuffer();
			this.f2 = new float[i0];

			this.f4 = new float[i0];
			this.f3 = new float[i0];

			this.f5 = new float[i0];
			this.f6 = new float[i0];

			f11();

			for (int factorIndex = 0; factorIndex < i1; factorIndex++) {
				f20(factorIndex);
			}

			try {
				return new CVector(f5, f6);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}

		}
	}

	/**
	 * This method will make the non centred fourier coefficients as represented
	 * by the CVector 'fft' to a centred fourier coefficients with DC component
	 * located at the centre and negative and positive frequency coefficients
	 * located on the left and right side of the DC coefficient respectively by
	 * applying an appropriate circular shift.
	 * <p>
	 * Do not use this centred fourier coefficients to compute inverse 1D FFT.
	 * 
	 * @param CVector
	 *            fft
	 * @return CVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector centre(CVector fft) {
		return new CVectorTools().wrapCVector(fft);
	}

	/**
	 * This method will plot the Magnitude and Phase spectrum for the fourier
	 * coefficients as represented by the CVector 'fft'.
	 * <p>
	 * Both the spectrums will be centred and plotted against the normalised
	 * frequencies.
	 * <p>
	 * Phase spectrum will show phases in radians[-PI,PI].
	 * 
	 * @param CVector
	 *            fft
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static void showSpectrum(CVector fft) {
		fft = centre(fft);

		Vector magnitude = fft.getMagnitude();
		Vector phase = fft.getAngle();

		float[] freq = new float[magnitude.length()];
		int shift = (fft.length() - 1) / 2;

		for (int i = 0; i < freq.length; i++) {
			freq[i] = (float) (i - shift) / fft.length();
		}

		try {
			magnitude.plot(new Vector(freq), "Magnitude Spectrum",
					"Normalised Frequency", "Magnitude");
			phase.plot(new Vector(freq), "Phase Spectrum",
					"Normalised Frequency", "Phases in radian");
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	// Description:
	// According to the definition of the inverse fourier transform,
	// We can use FFT to calculate the IFFT,
	// IFFT(x) = 1/N * conj(FFT(conj(x)).
	//
	// . Change the sign of the imaginary part of the FFT input.
	// . Calculate the FFT.
	// . Change the sign of the imaginary part of the FFT output.
	// . Scale the output by 1/N.
	//

	/**
	 * This method will apply a 1D discrete Inverse Fast Fourier Transform
	 * (IFFT) on the non centred fourier coefficients as represented by the
	 * CVector 'vector' and return the result as a CVector.
	 * <p>
	 * This method should only be used if one is working with the complex
	 * signals.
	 *  
	 * @param CVector
	 *            vector
	 * @return CVector
	 * @see #fft(CVector, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector ifftComplex(CVector vector) {
		try {
			return new FFT1D(vector.length()).ifft1DComplex(vector);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * <p>
	 * This method will apply a 'N' point 1D discrete Inverse Fast Fourier
	 * Transform (IFFT) on the non centred fourier coefficients as represented
	 * by the CVector 'vector' and return the resultant complex signal as
	 * CVector.
	 * <p>
	 * The number of points IFFT 'N' is decided by the argument passed to the
	 * constructor.
	 * <p>
	 * For 'N' points IFFT the length of the CVector 'vector' should be 'N' else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * This method should only be used if one is working with the complex
	 * signals.
	 *  
	 * @param CVector
	 *            vector
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector ifft1DComplex(CVector vector)
			throws IllegalArgumentException {
		if (vector.length() != i0)
			throw new IllegalArgumentException();

		else if (vector.length() == 1) {
			return vector.clone();
		} else {
			CVector conj = vector.getConjugate();
			CVector ifft = null;
			try {
				// if(isRadix2_available)
				// ifft = this.radix2_conj_fft(conj);
				// else
				ifft = this.fft1D(conj);
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}

			// Change the sign of the imaginary part of the FFT output.
			// Scale output by 1/N.

			float norm = 1.0f / conj.length();
			float[] real = ifft.accessRealPart().accessVectorBuffer();
			float[] img = ifft.accessImaginaryPart().accessVectorBuffer();

			for (int i = 0; i < ifft.length(); i++) {
				real[i] = real[i] * norm;
				img[i] = -img[i] * norm;
			}

			return ifft;
		}
	}

	/**
	 * This method will apply a 1D discrete Inverse Fast Fourier Transform
	 * (IFFT) on the non centred fourier coefficients as represented by the
	 * CVector 'vector' and return the real part of the resultant complex
	 * signal, which is the required real signal, as a Vector.
	 * <p>
	 * This method should only be employed when working with real signals.
	 *  
	 * @param CVector
	 *            vector
	 * @return Vector
	 * @see #fft(Vector, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector ifft(CVector vector) {
		try {
			return new FFT1D(vector.length()).ifft1D(vector);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * <p>
	 * This method will apply a 'N' point 1D discrete Inverse Fast Fourier
	 * Transform (IFFT) on the non centered fourier coefficients as represented
	 * by the CVector 'vector' and return the real part of the resultant complex
	 * signal, which is the required real signal, as Vector.
	 * <p>
	 * The number of points IFFT 'N' is decided by the argument passed to the
	 * constructor.
	 * <p>
	 * For 'N' points IFFT the length of the CVector 'vector' should be 'N' else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * This method should only be used if one is working with the real signals.
	 *  
	 * @param CVector
	 *            vector
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector ifft1D(CVector vector) throws IllegalArgumentException {
		if (vector.length() != i0)
			throw new IllegalArgumentException();

		else if (vector.length() == 1) {
			return vector.clone().accessRealPart();
		} else {
			CVector conj = vector.getConjugate();
			CVector ifft = null;
			try {
				// Only applying radix2 algo if signal is power of 2 length and
				// is real.
				if (b0)
					ifft = this.f19(conj);
				else
					ifft = this.fft1D(conj);
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}

			// Change the sign of the imaginary part of the FFT output.
			// Scale output by 1/N.

			// Do not take conjugate as you are only going to return the real
			// part of the
			// result.
			// ifft = ifft.getConjugate();
			float norm = 1.0f / conj.length();
			float[] real = ifft.accessRealPart().accessVectorBuffer();

			for (int i = 0; i < ifft.length(); i++) {
				real[i] = real[i] * norm;
			}

			return ifft.accessRealPart();
		}
	}

	private void f18() {
		int radices[] = { 2, 3, 4, 5, 8, 10 };
		// Maximum number of factors.
		int maxNosFactor;

		// added bhavya, to decide automatically maximum numbers of factors
		// for a given 'N'. Originally hard code to 37, may fail for large
		// 'N'.

		if (i0 == 1)
			maxNosFactor = 1;
		else
			maxNosFactor = (int) (Math.sqrt(i0) + 1);

		int temFactors[] = new int[maxNosFactor];

		// 1 - point FFT, no need to factorize N.
		if (i0 == 1) {
			temFactors[0] = 1;
			i1 = 1;
		}

		// N - point FFT, N is needed to be factorized.
		int n = i0;
		int index = 0; // index of temFactors.
		int i = radices.length - 1;

		while ((n > 1) && (i >= 0)) {
			if ((n % radices[i]) == 0) {
				n /= radices[i];
				temFactors[index++] = radices[i];
			} else
				i--;
		}

		// Substitute 2x8 with 4x4.
		// index>0, in the case only one prime factor, such as N=263.
		if ((index > 0) && (temFactors[index - 1] == 2))
			for (i = index - 2; i >= 0; i--)
				if (temFactors[i] == 8) {
					temFactors[index - 1] = temFactors[i] = 4;
					// break out of for loop, because only one '2' will exist in
					// temFactors, so only one substitutation is needed.
					break;
				}

		if (n > 1) {
			for (int k = 2; k < Math.sqrt(n) + 1; k++)
				while ((n % k) == 0) {
					n /= k;
					temFactors[index++] = k;
				}
			if (n > 1) {
				temFactors[index++] = n;
			}
		}
		i1 = index;
		// if(temFactors[NumofFactors-1] > 10)
		// maxFactor = n;
		// else
		// maxFactor = 10;

		// Inverse temFactors and store factors into factors[].
		i2 = new int[i1];
		for (i = 0; i < i1; i++) {
			i2[i] = temFactors[i1 - i - 1];
		}

		// Calculate sofar[], remain[].
		// sofar[] : finished factors before the current stage.
		// factors[]: factors of N processed in the current stage.
		// remain[] : finished factors after the current stage.
		i3 = new int[i1];
		i4 = new int[i1];

		i4[0] = i0 / i2[0];
		i3[0] = 1;
		for (i = 1; i < i1; i++) {
			i3[i] = i3[i - 1] * i2[i - 1];
			i4[i] = i4[i - 1] / i2[i];
		}
	} // End of function factorize().

	private void f11() {
		// Maximum number of factors.
		int count[] = new int[37];
		int j;
		int k = 0;

		for (int i = 0; i < i0 - 1; i++) {
			f5[i] = f1[k];
			f6[i] = f2[k];
			j = 0;
			k = k + i4[j];
			count[0] = count[0] + 1;
			while (count[j] >= i2[j]) {
				count[j] = 0;
				k = k - (j == 0 ? i0 : i4[j - 1]) + i4[j + 1];
				j++;
				count[j] = count[j] + 1;
			}
		}
		f5[i0 - 1] = f1[i0 - 1];
		f6[i0 - 1] = f2[i0 - 1];
	}

	private void f20(int factorIndex) {
		// Get factor data.
		int sofarRadix = i3[factorIndex];
		int radix = i2[factorIndex];
		int remainRadix = i4[factorIndex];

		float tem; // Temporary variable to do data exchange.

		float W = 2 * (float) Math.PI / (sofarRadix * radix);
		float cosW = (float) Math.cos(W);
		float sinW = -(float) Math.sin(W);

		float twiddleRe[] = new float[radix];
		float twiddleIm[] = new float[radix];
		float twRe = 1.0f, twIm = 0f;

		// Initialize twiddle addBk.address variables.
		int dataOffset = 0, groupOffset = 0, address = 0;

		for (int dataNo = 0; dataNo < sofarRadix; dataNo++) {
			// System.out.println("datano="+dataNo);
			if (sofarRadix > 1) {
				twiddleRe[0] = 1.0f;
				twiddleIm[0] = 0.0f;
				twiddleRe[1] = twRe;
				twiddleIm[1] = twIm;
				for (int i = 2; i < radix; i++) {

					twiddleRe[i] = twRe * twiddleRe[i - 1] - twIm
							* twiddleIm[i - 1];
					twiddleIm[i] = twIm * twiddleRe[i - 1] + twRe
							* twiddleIm[i - 1];
				}
				tem = cosW * twRe - sinW * twIm;
				twIm = sinW * twRe + cosW * twIm;
				twRe = tem;
			}
			for (int groupNo = 0; groupNo < remainRadix; groupNo++) {
				// System.out.println("groupNo="+groupNo);
				if ((sofarRadix > 1) && (dataNo > 0)) {
					f3[0] = f5[address];
					f4[0] = f6[address];
					int blockIndex = 1;
					do {
						address = address + sofarRadix;
						f3[blockIndex] = twiddleRe[blockIndex] * f5[address]
								- twiddleIm[blockIndex] * f6[address];
						f4[blockIndex] = twiddleRe[blockIndex] * f6[address]
								+ twiddleIm[blockIndex] * f5[address];
						blockIndex++;
					} while (blockIndex < radix);
				} else
					for (int i = 0; i < radix; i++) {
						// System.out.println("temRe.length="+temRe.length);
						// System.out.println("i = "+i);
						f3[i] = f5[address];
						f4[i] = f6[address];
						address += sofarRadix;
					}
				// System.out.println("radix="+radix);
				switch (radix) {

				case 2:
					tem = f3[0] + f3[1];
					f3[1] = f3[0] - f3[1];
					f3[0] = tem;
					tem = f4[0] + f4[1];
					f4[1] = f4[0] - f4[1];
					f4[0] = tem;
					break;
				case 3:
					float t1Re = f3[1] + f3[2];
					float t1Im = f4[1] + f4[2];
					f3[0] = f3[0] + t1Re;
					f4[0] = f4[0] + t1Im;

					// cos2to3PI = cos(2*pi/3), using for 3 point FFT.
					// cos(2*PI/3) is not -1.5
					final float twiddleReal2To3 = -1.5000f;
					// sin2to3PI = sin(2*pi/3), using for 3 point FFT.
					final float twiddleImag2To3 = 8.6602540378444E-01f;

					float m1Re = twiddleReal2To3 * t1Re;
					float m1Im = twiddleReal2To3 * t1Im;
					float m2Re = twiddleImag2To3 * (f4[1] - f4[2]);
					float m2Im = twiddleImag2To3 * (f3[2] - f3[1]);
					float s1Re = f3[0] + m1Re;
					float s1Im = f4[0] + m1Im;

					f3[1] = s1Re + m2Re;
					f4[1] = s1Im + m2Im;
					f3[2] = s1Re - m2Re;
					f4[2] = s1Im - m2Im;
					break;
				case 4:
					f14(f3, f4);
					break;
				case 5:
					f15(f3, f4);
					break;
				case 8:
					f16();
					break;
				case 10:
					f12();
					break;
				default:
					f17(radix);
					break;
				}
				address = groupOffset;
				for (int i = 0; i < radix; i++) {
					f5[address] = f3[i];
					f6[address] = f4[i];
					address += sofarRadix;
				}
				groupOffset += sofarRadix * radix;
				address = groupOffset;
			}
			groupOffset = ++dataOffset;
			address = groupOffset;
		}
	} // End of function twiddle().

	// The two arguments dataRe[], dataIm[] are mainly for using in fft8();
	private void f14(float dataRe[], float dataIm[]) {
		float t1Re, t1Im, t2Re, t2Im;
		float m2Re, m2Im, m3Re, m3Im;

		t1Re = dataRe[0] + dataRe[2];
		t1Im = dataIm[0] + dataIm[2];
		t2Re = dataRe[1] + dataRe[3];
		t2Im = dataIm[1] + dataIm[3];

		m2Re = dataRe[0] - dataRe[2];
		m2Im = dataIm[0] - dataIm[2];
		m3Re = dataIm[1] - dataIm[3];
		m3Im = dataRe[3] - dataRe[1];

		dataRe[0] = t1Re + t2Re;
		dataIm[0] = t1Im + t2Im;
		dataRe[2] = t1Re - t2Re;
		dataIm[2] = t1Im - t2Im;
		dataRe[1] = m2Re + m3Re;
		dataIm[1] = m2Im + m3Im;
		dataRe[3] = m2Re - m3Re;
		dataIm[3] = m2Im - m3Im;
	} // End of function fft4().

	// The two arguments dataRe[], dataIm[] are mainly for using in fft10();
	private void f15(float dataRe[], float dataIm[]) {
		float t1Re, t1Im, t2Re, t2Im, t3Re, t3Im, t4Re, t4Im, t5Re, t5Im;
		float m1Re, m1Im, m2Re, m2Im, m3Re, m3Im, m4Re, m4Im, m5Re, m5Im;
		float s1Re, s1Im, s2Re, s2Im, s3Re, s3Im, s4Re, s4Im, s5Re, s5Im;

		t1Re = dataRe[1] + dataRe[4];
		t1Im = dataIm[1] + dataIm[4];
		t2Re = dataRe[2] + dataRe[3];
		t2Im = dataIm[2] + dataIm[3];
		t3Re = dataRe[1] - dataRe[4];
		t3Im = dataIm[1] - dataIm[4];
		t4Re = dataRe[3] - dataRe[2];
		t4Im = dataIm[3] - dataIm[2];
		t5Re = t1Re + t2Re;
		t5Im = t1Im + t2Im;

		dataRe[0] = dataRe[0] + t5Re;
		dataIm[0] = dataIm[0] + t5Im;

		// TwotoFivePI = 2*pi/5.
		// c51, c52, c53, c54, c55 are used in fft5().
		// c51 =(cos(TwotoFivePI)+cos(2*TwotoFivePI))/2-1.
		final float c51 = -1.25f;
		// c52 =(cos(TwotoFivePI)-cos(2*TwotoFivePI))/2.
		final float c52 = 5.5901699437495E-01f;
		// c53 = -sin(TwotoFivePI).
		final float c53 = -9.5105651629515E-01f;
		// c54 =-(sin(TwotoFivePI)+sin(2*TwotoFivePI)).
		final float c54 = -1.5388417685876E+00f;
		// c55 =(sin(TwotoFivePI)-sin(2*TwotoFivePI)).
		final float c55 = 3.6327126400268E-01f;

		m1Re = c51 * t5Re;
		m1Im = c51 * t5Im;
		m2Re = c52 * (t1Re - t2Re);
		m2Im = c52 * (t1Im - t2Im);
		m3Re = -c53 * (t3Im + t4Im);
		m3Im = c53 * (t3Re + t4Re);
		m4Re = -c54 * t4Im;
		m4Im = c54 * t4Re;
		m5Re = -c55 * t3Im;
		m5Im = c55 * t3Re;

		s3Re = m3Re - m4Re;
		s3Im = m3Im - m4Im;
		s5Re = m3Re + m5Re;
		s5Im = m3Im + m5Im;
		s1Re = dataRe[0] + m1Re;
		s1Im = dataIm[0] + m1Im;
		s2Re = s1Re + m2Re;
		s2Im = s1Im + m2Im;
		s4Re = s1Re - m2Re;
		s4Im = s1Im - m2Im;

		dataRe[1] = s2Re + s3Re;
		dataIm[1] = s2Im + s3Im;
		dataRe[2] = s4Re + s5Re;
		dataIm[2] = s4Im + s5Im;
		dataRe[3] = s4Re - s5Re;
		dataIm[3] = s4Im - s5Im;
		dataRe[4] = s2Re - s3Re;
		dataIm[4] = s2Im - s3Im;
	} // End of function fft5().

	private void f16() {
		float data1Re[] = new float[4];
		float data1Im[] = new float[4];
		float data2Re[] = new float[4];
		float data2Im[] = new float[4];
		float tem;
		final float oneBySquareRoot = 7.0710678118655E-01f;

		// To improve the speed, use direct assaignment instead for loop here.
		data1Re[0] = f3[0];
		data2Re[0] = f3[1];
		data1Re[1] = f3[2];
		data2Re[1] = f3[3];
		data1Re[2] = f3[4];
		data2Re[2] = f3[5];
		data1Re[3] = f3[6];
		data2Re[3] = f3[7];

		data1Im[0] = f4[0];
		data2Im[0] = f4[1];
		data1Im[1] = f4[2];
		data2Im[1] = f4[3];
		data1Im[2] = f4[4];
		data2Im[2] = f4[5];
		data1Im[3] = f4[6];
		data2Im[3] = f4[7];

		f14(data1Re, data1Im);
		f14(data2Re, data2Im);

		tem = oneBySquareRoot * (data2Re[1] + data2Im[1]);
		data2Im[1] = oneBySquareRoot * (data2Im[1] - data2Re[1]);
		data2Re[1] = tem;
		tem = data2Im[2];
		data2Im[2] = -data2Re[2];
		data2Re[2] = tem;
		tem = oneBySquareRoot * (data2Im[3] - data2Re[3]);
		data2Im[3] = -oneBySquareRoot * (data2Re[3] + data2Im[3]);
		data2Re[3] = tem;

		f3[0] = data1Re[0] + data2Re[0];
		f3[4] = data1Re[0] - data2Re[0];
		f3[1] = data1Re[1] + data2Re[1];
		f3[5] = data1Re[1] - data2Re[1];
		f3[2] = data1Re[2] + data2Re[2];
		f3[6] = data1Re[2] - data2Re[2];
		f3[3] = data1Re[3] + data2Re[3];
		f3[7] = data1Re[3] - data2Re[3];

		f4[0] = data1Im[0] + data2Im[0];
		f4[4] = data1Im[0] - data2Im[0];
		f4[1] = data1Im[1] + data2Im[1];
		f4[5] = data1Im[1] - data2Im[1];
		f4[2] = data1Im[2] + data2Im[2];
		f4[6] = data1Im[2] - data2Im[2];
		f4[3] = data1Im[3] + data2Im[3];
		f4[7] = data1Im[3] - data2Im[3];
	} // End of function fft8().

	private void f12() {
		float data1Re[] = new float[5];
		float data1Im[] = new float[5];
		float data2Re[] = new float[5];
		float data2Im[] = new float[5];

		// To improve the speed, use direct assaignment instead for loop here.
		data1Re[0] = f3[0];
		data2Re[0] = f3[5];
		data1Re[1] = f3[2];
		data2Re[1] = f3[7];
		data1Re[2] = f3[4];
		data2Re[2] = f3[9];
		data1Re[3] = f3[6];
		data2Re[3] = f3[1];
		data1Re[4] = f3[8];
		data2Re[4] = f3[3];

		data1Im[0] = f4[0];
		data2Im[0] = f4[5];
		data1Im[1] = f4[2];
		data2Im[1] = f4[7];
		data1Im[2] = f4[4];
		data2Im[2] = f4[9];
		data1Im[3] = f4[6];
		data2Im[3] = f4[1];
		data1Im[4] = f4[8];
		data2Im[4] = f4[3];

		f15(data1Re, data1Im);
		f15(data2Re, data2Im);

		f3[0] = data1Re[0] + data2Re[0];
		f3[5] = data1Re[0] - data2Re[0];
		f3[6] = data1Re[1] + data2Re[1];
		f3[1] = data1Re[1] - data2Re[1];
		f3[2] = data1Re[2] + data2Re[2];
		f3[7] = data1Re[2] - data2Re[2];
		f3[8] = data1Re[3] + data2Re[3];
		f3[3] = data1Re[3] - data2Re[3];
		f3[4] = data1Re[4] + data2Re[4];
		f3[9] = data1Re[4] - data2Re[4];

		f4[0] = data1Im[0] + data2Im[0];
		f4[5] = data1Im[0] - data2Im[0];
		f4[6] = data1Im[1] + data2Im[1];
		f4[1] = data1Im[1] - data2Im[1];
		f4[2] = data1Im[2] + data2Im[2];
		f4[7] = data1Im[2] - data2Im[2];
		f4[8] = data1Im[3] + data2Im[3];
		f4[3] = data1Im[3] - data2Im[3];
		f4[4] = data1Im[4] + data2Im[4];
		f4[9] = data1Im[4] - data2Im[4];
	} // End of function fft10().

	private void f17(int radix) {
		// Initial WRe, WIm.
		float W = 2 * (float) Math.PI / radix;
		float cosW = (float) Math.cos(W);
		float sinW = -(float) Math.sin(W);
		float WRe[] = new float[radix];
		float WIm[] = new float[radix];

		WRe[0] = 1;
		WIm[0] = 0;
		WRe[1] = cosW;
		WIm[1] = sinW;

		for (int i = 2; i < radix; i++) {
			WRe[i] = cosW * WRe[i - 1] - sinW * WIm[i - 1];
			WIm[i] = sinW * WRe[i - 1] + cosW * WIm[i - 1];
		}

		// FFT of prime length data, using DFT, can be improved in the future.
		float rere, reim, imre, imim;
		int j, k;
		int max = (radix + 1) / 2;

		float tem1Re[] = new float[max];
		float tem1Im[] = new float[max];
		float tem2Re[] = new float[max];
		float tem2Im[] = new float[max];

		for (j = 1; j < max; j++) {
			tem1Re[j] = f3[j] + f3[radix - j];
			tem1Im[j] = f4[j] - f4[radix - j];
			tem2Re[j] = f3[j] - f3[radix - j];
			tem2Im[j] = f4[j] + f4[radix - j];
		}

		for (j = 1; j < max; j++) {
			f3[j] = f3[0];
			f4[j] = f4[0];
			f3[radix - j] = f3[0];
			f4[radix - j] = f4[0];
			k = j;
			for (int i = 1; i < max; i++) {
				rere = WRe[k] * tem1Re[i];
				imim = WIm[k] * tem1Im[i];
				reim = WRe[k] * tem2Im[i];
				imre = WIm[k] * tem2Re[i];

				f3[radix - j] += rere + imim;
				f4[radix - j] += reim - imre;
				f3[j] += rere - imim;
				f4[j] += reim + imre;

				k = k + j;
				if (k >= radix)
					k = k - radix;
			}
		}
		for (j = 1; j < max; j++) {
			f3[0] = f3[0] + tem1Re[j];
			f4[0] = f4[0] + tem2Im[j];
		}
	} // End of function fftPrime().

	// Disbanding Radix 2 FFT : See rationale below.

	// There is no consistency between the coefficients generated by this radix2
	// algo
	// and mixed radix algo above. The coefficients are misalign though they are
	// same.
	// Both real and imaginary part of coefficients are misalign (if signal is
	// complex) or only
	// imaginary part is misalign (if signal is real).
	// Thus implemented a correction to align coefficients in forward fft based
	// on radix2
	// and reverse this change during inverse fft.
	// However, it was observed that with real and complex signals, coefficients
	// are misalign
	// differently. Thus need to implement 2 separate correction/inversal for
	// both real & img
	// part of coefficients based on whether we are working with a real or
	// complex signal.
	// So far good, dilemma is from complex coefficients how do you figure out
	// that they were
	// derived for a real or complex signal, unless we put a flag in CVector.
	//
	// The above alignment is not necessary needed (if one do not bother about
	// the consistency
	// between coefficients as for power of 2 we are not going to use mix radix
	// algorithm)
	// as long as both forward and backward fft for power of 2 points is done
	// using this radix 2
	// algo.
	//
	// However, some issues were seen with DCT. To correct it this alignment was
	// carried out.
	// DCT was working fine but 2D FFT was broken as it uses fft first on real
	// row elements and
	// than in second state on complex column elements. As misalignment is
	// different for real
	// and complex signals, this was affecting FFT 2D.
	//
	// So to fix all we have to put in place an alignment/inversal for both real
	// and imaginary parts for
	// both real and complex signals. This is future improvement.
	//
	// If can be done, this will more than half the execution time of FFT for
	// power of 2 points.
	//
	// Note: Following are change in radix2 coefficients as compare to mix radix
	// for real and complex
	// signals in mangnitude and phase part.
	// For real signals:
	// Magnitude is same but phase is flipped and shifted by 1 index.
	// For complex signals:
	// Magnitude is flipped and shifted by 1 index, but phase is totally
	// different.

	// !!!!Workaround:
	// As radix2 is working fine after coefficients alignment atleast for real
	// signals, implement
	// radix2 for computing fft if we are dealing with real signals of power of
	// 2 length. For complex
	// signal of any length keep using the mix radix algorithm.

	// //-------------------Radix 2 FFT Algorithm for power of 2 points
	// --------------------//

	// Cosine and Sine cache for radix 2 FFT.
	private double[] d1;
	private double[] d2;
	private int i9;
	private boolean b0;

	private void f0() {
		// precompute cache
		d1 = new double[i0 / 2];
		d2 = new double[i0 / 2];
		double w = -2 * Math.PI / i0;

		for (int i = 0; i < i0 / 2; i++) {
			d1[i] = Math.cos(-w * i);
			d2[i] = Math.sin(-w * i);
		}
	}

	//
	// private CVector fft2(CVector signal)
	// {
	// CVector input = signal.clone();
	//
	// float[] x = input.accessRealPart().accessVectorBuffer();
	// float[] y = input.accessImaginaryPart().accessVectorBuffer();
	//
	// int n = N;
	// //int m = (int)(Math.log(n) / Math.log(2));
	// // Make sure n is a power of 2
	// // if(n != (1<<m))
	// // throw new IllegalArgumentException();
	//
	// int i,j,k,n1,n2,a;
	// double c,s,t1,t2;
	//
	//
	// // Bit-reverse
	// j = 0;
	// n2 = n/2;
	// for (i=1; i < n - 1; i++) {
	// n1 = n2;
	// while ( j >= n1 ) {
	// j = j - n1;
	// n1 = n1/2;
	// }
	// j = j + n1;
	//
	// if (i < j) {
	// t1 = x[i];
	// x[i] = x[j];
	// x[j] = (float) t1;
	// t1 = y[i];
	// y[i] = y[j];
	// y[j] = (float) t1;
	// }
	// }
	//
	// // FFT
	// n1 = 0;
	// n2 = 1;
	//
	// for (i=0; i < m; i++) {
	// n1 = n2;
	// n2 = n2 + n2;
	// a = 0;
	//
	// for (j=0; j < n1; j++) {
	// c = cos[a];
	// s = sin[a];
	// a += 1 << (m-i-1);
	//
	// for (k=j; k < n; k=k+n2) {
	// t1 = c*x[k+n1] - s*y[k+n1];
	// t2 = s*x[k+n1] + c*y[k+n1];
	// x[k+n1] = (float) (x[k] - t1);
	// y[k+n1] = (float) (y[k] - t2);
	// x[k] = (float) (x[k] + t1);
	// y[k] = (float) (y[k] + t2);
	// }
	// }
	// }
	// int length = y.length;
	// float[] y_flip = new float[length];
	//
	// //Flip and shift right by 1 to match imaginary part of radix2 fft
	// coefficients with
	// //that obtained from mixed radix algorithm for power of 2 points FFT.
	// //Real part matches so no change.
	// y_flip[length-1] = y[0];
	// for(int l=0;l<length-1;l++){
	// y_flip[l+1] = y[length-1-l];
	// }
	//
	// try {
	// return new CVector(x,y_flip);
	// } catch (DimensionMismatchException e1) {
	// throw new BugEncounterException();
	// }
	// }
	//
	private CVector f13(Vector signal) {
		Vector clone = signal.clone();

		float[] x = clone.accessVectorBuffer();
		float[] y = new float[x.length];

		int n = i0;
		// int m = (int)(Math.log(n) / Math.log(2));
		// Make sure n is a power of 2
		// if(n != (1<<m))
		// throw new IllegalArgumentException();

		int i, j, k, n1, n2, a;
		double c, s, t1, t2;

		// Bit-reverse
		j = 0;
		n2 = n / 2;
		for (i = 1; i < n - 1; i++) {
			n1 = n2;
			while (j >= n1) {
				j = j - n1;
				n1 = n1 / 2;
			}
			j = j + n1;

			if (i < j) {
				t1 = x[i];
				x[i] = x[j];
				x[j] = (float) t1;
				t1 = y[i];
				y[i] = y[j];
				y[j] = (float) t1;
			}
		}

		// FFT
		n1 = 0;
		n2 = 1;

		for (i = 0; i < i9; i++) {
			n1 = n2;
			n2 = n2 + n2;
			a = 0;

			for (j = 0; j < n1; j++) {
				c = d1[a];
				s = d2[a];
				a += 1 << (i9 - i - 1);

				for (k = j; k < n; k = k + n2) {
					t1 = c * x[k + n1] - s * y[k + n1];
					t2 = s * x[k + n1] + c * y[k + n1];
					x[k + n1] = (float) (x[k] - t1);
					y[k + n1] = (float) (y[k] - t2);
					x[k] = (float) (x[k] + t1);
					y[k] = (float) (y[k] + t2);
				}
			}
		}
		int length = y.length;
		float[] y_flip = new float[length];

		// Flip and shift right by 1 to match imaginary part of radix2 fft
		// coefficients with
		// that obtained from mixed radix algorithm for power of 2 points FFT.
		// Real part matches so no change.
		y_flip[length - 1] = y[0];
		for (int l = 0; l < length - 1; l++) {
			y_flip[l + 1] = y[length - 1 - l];
		}

		try {
			return new CVector(x, y_flip);
		} catch (DimensionMismatchException e1) {
			throw new BugEncounterException();
		}
	}

	//
	// //This is not an inverse fft, but forward fft to be applied to obtain
	// inverse fft
	// //for radix 2 algorithm.
	//
	private CVector f19(CVector fft2) {
		// CVector input = fft2.clone();
		float[] x = fft2.accessRealPart().clone().accessVectorBuffer();
		// Donot clone as we shall create a new array for the same.
		float[] _y = fft2.accessImaginaryPart().accessVectorBuffer();
		int length = x.length;

		float[] y = new float[length];

		// Undo changes to imaginary part by fft2.
		// Real part was not impacted so no change.
		y[0] = _y[length - 1];
		for (int l = 0; l < length - 1; l++) {
			y[length - 1 - l] = _y[l + 1];
		}

		int n = i0;
		// int m = (int)(Math.log(n) / Math.log(2));
		// Make sure n is a power of 2
		// if(n != (1<<m))
		// throw new IllegalArgumentException();

		int i, j, k, n1, n2, a;
		double c, s, t1, t2;

		// Bit-reverse
		j = 0;
		n2 = n / 2;
		for (i = 1; i < n - 1; i++) {
			n1 = n2;
			while (j >= n1) {
				j = j - n1;
				n1 = n1 / 2;
			}
			j = j + n1;

			if (i < j) {
				t1 = x[i];
				x[i] = x[j];
				x[j] = (float) t1;
				t1 = y[i];
				y[i] = y[j];
				y[j] = (float) t1;
			}
		}

		// FFT
		n1 = 0;
		n2 = 1;

		for (i = 0; i < i9; i++) {
			n1 = n2;
			n2 = n2 + n2;
			a = 0;

			for (j = 0; j < n1; j++) {
				c = d1[a];
				s = d2[a];
				a += 1 << (i9 - i - 1);

				for (k = j; k < n; k = k + n2) {
					t1 = c * x[k + n1] - s * y[k + n1];
					t2 = s * x[k + n1] + c * y[k + n1];
					x[k + n1] = (float) (x[k] - t1);
					y[k + n1] = (float) (y[k] - t2);
					x[k] = (float) (x[k] + t1);
					y[k] = (float) (y[k] + t2);
				}
			}
		}

		try {
			return new CVector(x, y);
		} catch (DimensionMismatchException e1) {
			throw new BugEncounterException();
		}
	}
}
