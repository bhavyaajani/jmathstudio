package org.JMathStudio.SignalToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a 1D fast discrete Hartley transform (DHT) and its inverse
 * on a discrete real signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be valid Vector object.
 * 
 * DHT1D dht = new DHT1D();//Create an instance of DHT1D.
 * 
 * Vector coeff = dht.dht1D(a);//Compute real DHT coefficients of signal as
 * represented by input Vector 'a'.
 * 
 * Vector recover = dht.idht1D(coeff);//Recover original signal from its DHT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DHT1D {
	
	/**
	 * This method will apply a fast discrete hartley transform (DHT) on the
	 * discrete real signal as represented by the Vector 'vector' and return the
	 * result as a Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector dht1D(Vector vector) 
	{
		CVector fft = null;
		
		try {
			FFT1D fft_ = new FFT1D(vector.length());
			fft = fft_.fft1D(vector);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();

		}
		float[] result = new float[fft.length()];

		for (int i = 0; i < result.length; i++) 
		{
			Complex ele = fft.getElement(i);
			
			result[i] = ele.getRealPart() + ele.getImaginaryPart();
		}

		return new Vector(result);
	}

	/**
	 * This method will apply an fast inverse discrete hartley transform (IDHT)
	 * on the Vector 'vector' and return the resultant signal as a Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Vector idht1D(Vector vector) {
		Vector result = dht1D(vector);
		result = VectorMath.linear(1.0f / result.length(), 0, result);
		return result;
	}

}
