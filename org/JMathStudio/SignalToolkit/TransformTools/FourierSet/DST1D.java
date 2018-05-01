package org.JMathStudio.SignalToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a 1D fast discrete sine transform (DST) and its inverse on
 * a discrete real signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be valid Vector object.
 * 
 * DST1D dst = new DST1D();//Create an instance of DST1D.
 * 
 * Vector coeff = dst.dst1D(a, a.length());//Compute real DST coefficients of signal as
 * represented by input Vector 'a'.
 * 
 * Vector recover = dst.idst1D(coeff);//Recover original signal from its DST coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DST1D {
	
	private VectorTools tools = new VectorTools();
	
	/**
	 * This method will apply a 'N' point fast discrete sine transform (DST) on
	 * the discrete real signal as represented by the Vector 'vector' and return
	 * the result as a Vector.
	 * <p>
	 * The value of the argument 'N' should not be less than the length of the
	 * Vector 'vector' else this method will throw an IllegalArgument Exception.
	 * The signal represented by the Vector 'vector' will be padded with
	 * appropriate number of zeros so as to make its length equal to the
	 * argument 'N' before applying the DST.
	 * 
	 * @param Vector
	 *            vector
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector dst1D(Vector vector, int N)
			throws IllegalArgumentException {
		if (vector.length() == 1) {
			return vector.clone();
		}

		if (N < vector.length()) {
			throw new IllegalArgumentException();
		}
		Vector signal = tools.resize(vector, N);

		Vector result = tools.flip(signal);
		result = VectorMath.linear(-1, 0, result);
		signal = f5(signal, signal.length() + 2, 1);
		result = tools.concate(signal, result);
		
		FFT1D fft_ = new FFT1D(result.length());
		CVector fft = fft_.fft1D(result);

		float[] output = new float[N];

		for (int i = 0; i < output.length; i++) {
			output[i] = fft.getElement(i + 1).getImaginaryPart() * (-0.5f);
		}
		return new Vector(output);

	}

	/**
	 * This method will apply an fast inverse discrete sine transform (IDST) on
	 * the Vector 'vector' and return the resultant signal as a Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector idst1D(Vector vector) {
		Vector tmp = VectorMath.linear(-2, 0, vector);
		try {
			tmp = f5(tmp, tmp.length() + 2, 1);

			Vector mirror = tools.flip(VectorMath.linear(2, 0, vector));
			tmp = tools.concate(tmp, mirror);

			CVector fft = new CVector(tmp.length());

			for (int i = 0; i < fft.length(); i++) {
				fft.setElement(new Complex(0, tmp.accessVectorBuffer()[i]), i);
			}
			Vector result = FFT1D.ifft(fft);

			float[] output = new float[vector.length()];

			for (int i = 0; i < output.length; i++) {
				output[i] = result.accessVectorBuffer()[i + 1];
			}

			return new Vector(output);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	private Vector f5(Vector Vector, int size, int phase)
			throws IllegalArgumentException {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		if (phase < 0) {
			throw new IllegalArgumentException();
		}

		float[] result = new float[size];

		for (int i = 0; i < size - phase; i++) {
			if (i < Vector.length()) {
				result[i + phase] = Vector.getElement(i);
			} else {
				break;
			}
		}

		return new Vector(result);
	}

}
