package org.JMathStudio.SignalToolkit.TransformTools;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;

/**
 * This class define a 1D discrete Hilbert Transform on a discrete real
 * signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * Hilbert h = new Hilbert();//Create an instance of Hilbert.
 * 
 * CVector hilbert = h.transform(a);//Compute complex Hilbert transform of the signal as
 * represented by input Vector 'a'.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Hilbert {
	
	/**
	 * This method will apply a Hilbert Transform on the discrete real signal as
	 * represented by the {@link Vector} 'vector' and return the result as a {@link CVector}.
	 * <p>
	 * The return {@link CVector} is the Analytical signal, where real part is the
	 * original signal and imaginary part is the hilbert pair of the original
	 * signal.
	 * 
	 * @param Vector
	 *            vector
	 * @return CVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector transform(Vector vector) {
		CVector fft = null;
		try {
			FFT1D fft_ = new FFT1D(vector.length());
			fft = fft_.fft1D(vector);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		float[] h = null;
		try {
			h = f4(fft.length()).accessVectorBuffer();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		for (int i = 0; i < fft.length(); i++) 
		{
			fft.setElement(fft.getElement(i).product(h[i]),i);
		}
		return FFT1D.ifftComplex(fft);

	}
	
	private Vector f4(int length) throws IllegalArgumentException {
		if (length < 1) {
			throw new IllegalArgumentException();
		}

		float[] vector = new float[length];

		vector[0] = 1;

		if (length % 2 == 0) {
			vector[vector.length / 2] = 1;
			for (int i = 1; i < vector.length / 2; i++) {
				vector[i] = 2;
			}

			return new Vector(vector);
		} else {
			for (int i = 1; i <= vector.length / 2; i++) {
				vector[i] = 2;
			}

			return new Vector(vector);
		}
	}

}
