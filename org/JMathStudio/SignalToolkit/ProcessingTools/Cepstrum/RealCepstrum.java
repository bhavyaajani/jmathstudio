package org.JMathStudio.SignalToolkit.ProcessingTools.Cepstrum;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;

/**
 * This class define Real and Power Cepstrum for Cepstral Analysis of a discrete
 * real signal.
 * <p>A discrete real signal will be represented by a {@link Vector} object.
 * <p>This class uses FFT in the computation of the Real and Power cepstrum.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * RealCepstrum rc = new RealCepstrum();//Create an instance of RealCepstrum.
 * 
 * Vector real_ceps = rc.realCepstrum(a);//Estimate real cepstrum for signal as represented
 * by input Vector.
 * 
 * Vector power_ceps = rc.powerCepstrum(a);//Estimate power cepstrum for signal as represented
 * by input Vector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class RealCepstrum {

	/**
	 * This method compute the Power Cepstrum of the discrete real signal as represented by the 
	 * {@link Vector} 'vector' and return the resultant Power Cepstrum as a {@link Vector}.
	 * <p>For some of the discrete real signals power cepstrum is not define. For such
	 * cases this method shall return a 'null'.
	 * <p>Ensure that the return cepstrum is not 'null' before using the same.
	 * @param Vector vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector powerCepstrum(Vector vector) {
		
		CVector fft = null;
		Vector result = null;

		try {
			FFT1D fft_ = new FFT1D(vector.length());
			fft = fft_.fft1D(vector);
			Vector tmp = fft.getMagnitude();

			tmp = VectorMath.power(tmp,2);
			
			//It may happen that for some kind of input signals
			//magnitude value is zero and so 'log' will throw
			//an exception.
			//Such signals do not have power cepstrum so return
			//null.
			try{
				tmp = VectorMath.logE(tmp);
			}catch(IllegalArgumentException e)
			{
				return null;
			}
			//tmp is same length as vector so using same FFT1D.
			result = fft_.fft1D(tmp).getMagnitude();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return VectorMath.power(result,2);

	}

	/**
	 * This method compute the Real Cepstrum of the discrete real signal as represented by the 
	 * {@link Vector} 'vector' and return the resultant Real Cepstrum as a {@link Vector}.
	 * <p>For some of the discrete real signals real cepstrum is not define. For such
	 * cases this method shall return a 'null'.
	 * <p>Ensure that the return cepstrum is not 'null' before using the same.
	 * @param Vector vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector realCepstrum(Vector vector) {
		CVector fft = null;

		try {
			FFT1D fft_ = new FFT1D(vector.length());
			fft = fft_.fft1D(vector);
			
			Vector tmp = fft.getMagnitude();

			//It may happen that for some kind of input signals
			//magnitude value is zero and so 'log' will throw
			//an exception.
			//Such signals do not have real cepstrum so return
			//null.
			try{
				tmp = VectorMath.logE(tmp);
			}catch(IllegalArgumentException e)
			{
				return null;
			}
			
			CVector result = new CVector(tmp, new Vector(tmp.length()));

			return FFT1D.ifft(result);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}
}
