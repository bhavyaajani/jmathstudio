package org.JMathStudio.SignalToolkit.Utilities;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various standard discrete Signals.
 * <p>
 * A discrete signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Vector random = SignalGenerator.random(128);//Create a random signal of given length.
 * 
 * Vector sine = SignalGenerator.sine(10, 100, 0, 128);//Create a Sine signal with given
 * specifications.
 * 
 * Vector saw = SignalGenerator.sawTooth(0.2f, 64);//Create a Saw tooth signal with given
 * specifications.
 * 
 * Vector unitStep = SignalGenerator.unitStep(64, 32);//Create a Unit step signal with
 * given specifications.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SignalGenerator {
	
	//Ensure no instances are made of Utility class.
	private SignalGenerator(){}
	
	/**
	 * This method will create a discrete Sinc signal with cutoff frequency as
	 * given by the argument 'fc' and of length 'M'+1 and return the same as a
	 * {@link Vector}.
	 * <p>
	 * Here the argument 'fc' should be a normalised frequency and should be in
	 * the range of [0,0.5] else this method will throw an IllegalArgument
	 * Exception.
	 * <p>
	 * The argument 'M' should be an even number greater than 1 else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * The resultant Sinc signal has normalised amplitude with central lobe
	 * located at the centre.
	 * 
	 * @param int M
	 * @param float fc
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector sinc(int M, float fc)
			throws IllegalArgumentException {
		if (M < 2 || M % 2 != 0)
			throw new IllegalArgumentException();

		if (fc < 0 || fc > 0.5f)
			throw new IllegalArgumentException();

		float PI = (float) Math.PI;
		float[] result = new float[M + 1];// 0 to M
		float K = 2 * PI * fc;

		for (int i = 0; i < result.length; i++) {
			if (i != M / 2)
				result[i] = (float) (Math.sin(2 * PI * fc * (i - M / 2.0f)) / (i - M / 2.0f));
			else
				result[i] = K;

			result[i] = result[i] / K;// To make Max Amplitude 1.
		}

		return new Vector(result);
	}

	/**
	 * This method will create a discrete Linear Chirp Signal with given
	 * specifications and return the same as a {@link Vector}.
	 * <p>
	 * The argument 'b' specify the initial normalised frequency and argument
	 * 'a' specify the slope for linear increase in the instantaneous frequency
	 * and argument 'N'specify the length of the resultant Chirp Signal.
	 * <p>
	 * The argument 'b' 'a' and 'N' should satisfy following constraint on the
	 * range of normalised frequency,
	 * <p>
	 * <b>(N-1)*a + b <= 0.5</b>.
	 * <p>
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * Also value of the argument 'b' should be in the range of [0,0.5] else
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param float b
	 * @param float a
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector chirp(float b, float a, int N)
			throws IllegalArgumentException {
		if (N < 1) {
			throw new IllegalArgumentException();
		}

		if (b < 0 || b > 0.5) {
			throw new IllegalArgumentException();
		}

		float maxFreq = (N - 1) * a + b;

		if (maxFreq < 0 || maxFreq > 0.5) {
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.sin(Math.PI * (a * i + b) * i);
		}

		return new Vector(result);
	}

	/**
	 * This method will create a discrete Sine signal with normalised frequency
	 * as given by the argument 'F' of length as given by the argument 'N' with
	 * Phase shift as given by the argument 'P' and return the same as a
	 * {@link Vector}.
	 * <p>
	 * The value of the argument 'N' should be more than 0 and argument 'F'
	 * should be a normalised frequency in the range of [0,0.5] else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * Argument 'P' specify phase shift in radians.
	 * 
	 * @param float F
	 * @param int N
	 * @param float P
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector sine(float F, int N, float P)
			throws IllegalArgumentException {
		if (F < 0 || F > 0.5 || N < 1)
			throw new IllegalArgumentException();

		float[] result = new float[N];
		float PI = (float) Math.PI;

		for (int i = 0; i < result.length; i++)
			result[i] = (float) Math.sin(2 * PI * F * i + P);

		return new Vector(result);

	}

	/**
	 * This method will create a discrete Cosine signal with normalised
	 * frequency as given by the argument 'F' of length as given by the argument
	 * 'N' with Phase shift as given by the argument 'P' and return the same as
	 * a {@link Vector}.
	 * <p>
	 * The value of the argument 'N' should be more than 0 and argument 'F'
	 * should be a normalised frequency in the range of [0,0.5] else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * Argument 'P' specify phase shift in radians.
	 * 
	 * @param float F
	 * @param int N
	 * @param float P
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector cosine(float F, int N, float P)
			throws IllegalArgumentException {
		if (F < 0 || F > 0.5 || N < 1)
			throw new IllegalArgumentException();

		float[] result = new float[N];
		float PI = (float) Math.PI;

		for (int i = 0; i < result.length; i++)
			result[i] = (float) Math.cos(2 * PI * F * i + P);

		return new Vector(result);

	}

	/**
	 * This method will create a discrete Cosine signal of length 'N' with
	 * frequency and sampling frequency, both in hertz, as specified by the
	 * argument 'f' and 'fs' respectively. The generated signal will be return
	 * as a Vector.
	 * <p>
	 * The argument 'f' should not be negative and argument 'fs' should be more
	 * than '0' else this method will throw an IllegalArgument Exception.
	 * Further frequencies 'f' and 'fs' should satisfy the nyquist criteria, fs
	 * >= 2*f else this method will throw an IllegalArgument Exception.
	 * <p>
	 * Argument 'P' specify phase shift in radians.
	 * <p>
	 * The argument 'N' specify the length of the signal and it should be more
	 * than '0' else this method will throw an IllegalArgument Exception.
	 * 
	 * @param float f
	 * @param float fs
	 * @param float P
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector cosine(float f, float fs, float P, int N)
			throws IllegalArgumentException {
		if (f < 0 || fs <= 0 || N < 1)
			throw new IllegalArgumentException();
		if (fs < 2*f)
			throw new IllegalArgumentException();

		float[] result = new float[N];
		float PI = (float) Math.PI;
		double fn = 2 * PI * f / fs;

		for (int i = 0; i < result.length; i++)
			result[i] = (float) Math.cos(fn * i + P);

		return new Vector(result);

	}

	/**
	 * This method will create a discrete Sine signal of length 'N' with
	 * frequency and sampling frequency, both in hertz, as specified by the
	 * argument 'f' and 'fs' respectively. The generated signal will be return
	 * as a Vector.
	 * <p>
	 * The argument 'f' should not be negative and argument 'fs' should be more
	 * than '0' else this method will throw an IllegalArgument Exception.
	 * Further frequencies 'f' and 'fs' should satisfy the nyquist criteria, fs
	 * >= 2*f else this method will throw an IllegalArgument Exception.
	 * <p>
	 * Argument 'P' specify phase shift in radians.
	 * <p>
	 * The argument 'N' specify the length of the signal and it should be more
	 * than '0' else this method will throw an IllegalArgument Exception.
	 * 
	 * @param float f
	 * @param float fs
	 * @param float P
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector sine(float f, float fs, float P, int N)
			throws IllegalArgumentException {
		if (f < 0 || fs <= 0 || N < 1)
			throw new IllegalArgumentException();
		if (fs < 2*f)
			throw new IllegalArgumentException();

		float[] result = new float[N];
		float PI = (float) Math.PI;
		double fn = 2 * PI * f / fs;

		for (int i = 0; i < result.length; i++)
			result[i] = (float) Math.sin(fn * i + P);

		return new Vector(result);

	}

	/**
	 * This method will create a discrete Impulse Train signal of length as
	 * given by the argument 'N' with inter-impulse spacing given by the
	 * argument 'offset' and return the same as a {@link Vector}.
	 * <p>
	 * The argument 'N' and 'offset' should not be less than 1 else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int N
	 * @param int offset
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector impulseTrain(int N, int offset)
			throws IllegalArgumentException {
		if (N < 1 || offset < 1) {
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];

		for (int i = 0; offset * i < result.length; i++) {
			result[offset * i] = 1.0f;

		}

		return new Vector(result);
	}

	/**
	 * This method will create a discrete Random signal of length as given by
	 * the argument 'N' and return the same as a {@link Vector}.
	 * <p>
	 * The Random signal will have an Uniform distribution in the range of
	 * [0,1).
	 * <p>
	 * The argument 'N' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector random(int N)
			throws IllegalArgumentException {
		if (N < 1) {
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.random();
		}

		return new Vector(result);
	}

	/**
	 * This method will create a discrete Ram Up signal of length as given by
	 * the argument 'N' with given specifications and return as a {@link Vector}
	 * .
	 * <p>
	 * The argument 'start' specify the starting element value and argument
	 * 'diff' specify the uniform increment in the corresponding values.
	 * <p>
	 * The argument 'N' should be more than 0 and argument 'diff' should not be
	 * a negative number else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param float start
	 * @param float diff
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector rampUp(float start, float diff, int N)
			throws IllegalArgumentException {
		if (N < 1 || diff < 0) {
			throw new IllegalArgumentException();
		}
		float[] result = new float[N];

		for (int i = 0; i < result.length; i++) {
			result[i] = start + i * diff;
		}
		return new Vector(result);
	}

	/**
	 * This method will create a discrete Ram Down signal of length as given by
	 * the argument 'N' with given specifications and return as a {@link Vector}
	 * .
	 * <p>
	 * The argument 'start' specify the starting element value and argument
	 * 'diff' specify the uniform decrement in the corresponding values.
	 * <p>
	 * The argument 'N' should be more than 0 and argument 'diff' should not be
	 * a negative number else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param float start
	 * @param float diff
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector rampDown(float start, float diff, int N)
			throws IllegalArgumentException {
		if (N < 1 || diff < 0) {
			throw new IllegalArgumentException();
		}
		float[] result = new float[N];

		for (int i = 0; i < result.length; i++) {
			result[i] = start - i * diff;
		}
		return new Vector(result);
	}

	/**
	 * This method will create a discrete DC or Uniform signal with DC value as
	 * specified by the argument 'value' and of length as given by the argument
	 * 'N' and return the same as a {@link Vector}.
	 * <p>
	 * The argument 'N' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int N
	 * @param float value
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector uniform(int N, float value)
			throws IllegalArgumentException {
		if (N < 1) {
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];

		for (int i = 0; i < N; i++) {
			result[i] = value;
		}
		return new Vector(result);
	}

	/**
	 * This method will create a discrete Unit Impulse signal of length as given
	 * by the argument 'N' with shift as given by the argument 'S' and return
	 * the same as a {@link Vector}.
	 * <p>
	 * The value of the argument 'N' should be more than 0 and argument 'S'
	 * should be a valid shift position in the range of [0,N-1] else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int N
	 * @param int S
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector unitImpulse(int N, int S)
			throws IllegalArgumentException {
		if (N < 1 || S < 0 || S > N - 1)
			throw new IllegalArgumentException();

		float[] result = new float[N];
		result[S] = 1;

		return new Vector(result);

	}

	/**
	 * This method will create a discrete Unit Step signal of length as given by
	 * the argument 'N' and with shift as given by the argument 'S' and return
	 * the same as a {@link Vector}.
	 * <p>
	 * The value of the argument 'N' should be more than 0 and argument 'S'
	 * should be a valid shift position in the range of [0,N-1] else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'S' specify the shift from origin or here index location
	 * within the return Vector where step transition takes place.
	 * 
	 * @param int N
	 * @param int S
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector unitStep(int N, int S)
			throws IllegalArgumentException {
		if (N < 1 || S < 0 || S > N - 1)
			throw new IllegalArgumentException();

		float[] result = new float[N];

		for (int i = S; i < result.length; i++)
			result[i] = 1;

		return new Vector(result);

	}

	/**
	 * This method will create a discrete Saw Tooth signal of length as given by
	 * the argument 'N' with normalised periodic frequency as given by the
	 * argument 'F' and return the same as a {@link Vector}.
	 * <p>
	 * The value of the argument 'N' should be more than 0 and the argument 'F'
	 * should be a normalised frequency in the range of [0,0.5] else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param float F
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector sawTooth(float F, int N)
			throws IllegalArgumentException {
		if (F < 0 || F > 0.5f || N < 1)
			throw new IllegalArgumentException();

		float[] result = new float[N];

		for (int i = 0; i < result.length; i++) {
			float q = i * F;
			result[i] = 2.0f * (q - Math.round(q));
		}

		return new Vector(result);

	}

	/**
	 * This method will create a discrete periodic Triangular Pulse signal of
	 * length as given by the argument 'N' with normalised periodic frequency as
	 * given by the argument 'F' and return the same as a {@link Vector}.
	 * <p>
	 * The value of the argument 'N' should be more than 0 and the argument 'F'
	 * should be a normalised frequency in the range of [0,0.5] else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param float F
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector triangularPulses(float F, int N)
			throws IllegalArgumentException {
		if (F < 0 || F > 0.5f || N < 1)
			throw new IllegalArgumentException();

		float p = 2.0f * F;
		float[] result = new float[N];

		for (int i = 0; i < result.length; i++) {
			int ip = (int) Math.round(i * p);
			result[i] = 2.0f * (1 - 2 * (ip % 2)) * (i * p - ip);
		}

		return new Vector(result);
	}

	/**
	 * This method will create a discrete periodic Square pulse signal of length
	 * as given by the argument 'N' with normalised periodic frequency as given
	 * by the argument 'F' and return the same as a {@link Vector}.
	 * <p>
	 * The value of the argument 'N' should be more than 0 and the argument 'F'
	 * should be a normalised frequency in the range of [0,0.5] else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param float F
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public final static Vector squarePulses(float F, int N)
			throws IllegalArgumentException {
		if (F < 0 || F > 0.5f || N < 1)
			throw new IllegalArgumentException();

		float p = 2.0f * F;
		float[] result = new float[N];

		for (int i = 0; i < result.length; i++)
			result[i] = Math.floor(i * p) % 2 == 0 ? 1 : -1;

		return new Vector(result);

	}
	
	/**
	 * This method will create a discrete linear Signal containing 'N' linearly spaced elements 
	 * between the range of [a,b] and return the same as a {@link Vector}.
	 * <p>
	 * The argument 'a' specify the starting element value whereas argument 'b' specify the value for
	 * last element of the generated signal.
	 * <p>If argument 'a' is lesser/greater than 'b' than generated signal has linearly uniformly 
	 * increasing/decreasing values in range of [a,b].  
	 * <p>
	 * The argument 'N' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>If argument 'a' and 'b' are same than this method shall return a Uniform Signal of length 'N'
	 * containing same value; equal to 'a' = 'b'.
	 * 
	 * @param float a
	 * @param float b
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public static Vector linespace(float a, float b, int N)throws IllegalArgumentException {
		if (N <= 0)
			throw new IllegalArgumentException();

		if (N == 1)
			return new Vector(new float[] { b });

		Vector result = new Vector(N);

		float res[] = result.accessVectorBuffer();

		final double diff = (b - a) / (N - 1);

		res[0] = a;

		for (int i = 1; i < N-1; i++)
			res[i] = (float) (a + i*diff);

		res[N-1] = b;
		
		return result;
	}
}
