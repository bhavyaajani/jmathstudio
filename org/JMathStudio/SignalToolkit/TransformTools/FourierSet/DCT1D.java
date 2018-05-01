package org.JMathStudio.SignalToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.DivideByZeroException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a 1D fast discrete cosine transform (DCT) and its inverse
 * on a discrete real signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be valid Vector object.
 * 
 * DCT1D dct = new DCT1D();//Create an instance of DCT1D.
 * 
 * Vector coeff = dct.dct1D(a, a.length());//Compute real DCT coefficients of signal as
 * represented by input Vector 'a'.
 * 
 * Vector recover = dct.idct1D(coeff);//Recover original signal from its DCT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DCT1D {
	
	private VectorTools tools = new VectorTools();
	
	/**
	 * This method will apply a 'N' point fast discrete cosine transform (DCT)
	 * on the discrete real signal as represented by the Vector 'vector' and
	 * return the result as a Vector.
	 * <p>
	 * The value of the argument 'N' should not be less than the length of the
	 * Vector 'vector' else this method will throw an IllegalArgument Exception.
	 * The signal represented by the Vector 'vector' will be padded with
	 * appropriate number of zeros so as to make its length equal to the
	 * argument 'N' before applying the DCT.
	 * 
	 * @param Vector
	 *            vector
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector dct1D(Vector vector, int N) throws IllegalArgumentException {
		if (vector.length() == 1) {
			return vector.clone();
		}

		if (N < vector.length()) {
			throw new IllegalArgumentException();
		}

		Vector signal = tools.resize(vector, N);

		Vector result = tools.flip(signal);

		result = tools.concate(signal, result);
		
		FFT1D fft_ = new FFT1D(result.length());
		CVector fft = fft_.fft1D(result);

		float constant = (float) (0.5 * Math.sqrt(4.0 / fft.length()));
		float norm = (float) (-Math.PI/fft.length());
		
		for (int i = 0; i < fft.length(); i++) 
		{
			if (i == 0) 
			{
				float value = (float) (0.5 * Math.sqrt(2.0 / fft.length()));
				fft.setElement(fft.getElement(i).product(value),i);
				Complex twiddle = new Complex(1,0);
				fft.setElement(fft.getElement(i).product(twiddle),i);
			} else {
				fft.setElement(fft.getElement(i).product(constant),i);

				float angle = (float) (norm * i);
				Complex twiddle = new Complex((float) Math.cos(angle),
						(float) Math.sin(angle));
				fft.setElement(fft.getElement(i).product(twiddle),i);
			}
		}

		result = fft.accessRealPart();

		return tools.resize(result, result.length() / 2);
	}

	// /**
	// * This method compute the 'N' point conventional Forward Discrete Cosine
	// Transform of the
	// * input 1D signal 'input'. If signal length is not equal to 'N', signal
	// is resize to length
	// * 'N'. The value of 'N' should not be less than 'input' length.
	// * @param float[] input
	// * @param int N
	// * @return float[]
	// * @throws IllegalArgumentException
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public float[] DCT_slow(float[] input,int N) throws
	// IllegalArgumentException
	// {
	// if(N < input.length)
	// {
	// throw new IllegalArgumentException();
	// }
	// float[] signal = new Vector().resize(input,N,0);
	// float[] result = new float[signal.length];
	// N = result.length;
	//		
	// for(int k=0;k<N;k++)
	// {
	// float dct=0;
	//			
	// for(int n=0;n<N;n++)
	// {
	// double theta = Math.PI*((2.0*n +1)*k)/(2.0*N);
	// double cos = Math.cos(theta);
	// dct = (float) (dct + signal[n]*cos);
	// }
	//			
	// if(k == 0)
	// {
	// result[k]=(float) (dct * 1.0/Math.sqrt(N));
	// }
	// else
	// {
	// result[k] = (float)(dct * Math.sqrt(2.0/N));
	// }
	//			
	//			
	// }
	// return result;
	// }

	/**
	 * This method will apply an fast inverse discrete cosine transform (IDCT)
	 * on the Vector 'vector' and return the resultant signal as a Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector idct1D(Vector vector) {
		if (vector.length() == 1) {
			return vector.clone();
		}

		Vector tmp = tools.flip(vector);
		tmp = VectorMath.linear(-1, 0, tmp);
		tmp = tools.linearShift(tmp, 1);
		Vector dct_tmp = tools.concate(vector, tmp);

		CVector array;
		try {
			array = new CVector(dct_tmp,new Vector(dct_tmp.length()));
		} catch (DimensionMismatchException e1) {
			throw new BugEncounterException();
		}
		
		float constant = (float) (0.5 * Math.sqrt(4.0 / array.length()));
		constant = 1.0f/constant;
		
		double Wn = (-Math.PI/ array.length());
		
		for (int i = 0; i < array.length(); i++)
		{
			if (i == 0) 
			{
				Complex twiddle = new Complex(1,0);

				try {
					array.setElement(array.getElement(i).divide(twiddle),i);
				} catch (DivideByZeroException e) {
					throw new BugEncounterException();
				}
				float value = (float) (0.5 * Math.sqrt(2.0 / array.length()));
				array.setElement(array.getElement(i).product(1.0f / value),i);
			} else {
				float angle = (float) (Wn * i);
				Complex twiddle = new Complex((float) Math.cos(angle),
						(float) Math.sin(angle));

				try {
					array.setElement(array.getElement(i).divide(twiddle),i);
				} catch (DivideByZeroException e) {
					throw new BugEncounterException();
				}
				array.setElement(array.getElement(i).product(constant),i);
			}
		}

		dct_tmp = FFT1D.ifft(array);

		try {
			return tools.resize(dct_tmp, (dct_tmp.length() + 1) / 2);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}
	// /**
	// * This method compute the Conventional Inverse Discrete Cosine Transform
	// of the
	// * 1D DCT 'dct'.
	// * @param float[] dct
	// * @return float[]
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public float[] IDCT_slow(float[] dct)
	// {
	// float[] result = new float[dct.length];
	// float N = result.length;
	//		
	// for(int n=0;n<N;n++)
	// {
	// float value=0;
	//			
	// for(int k=0;k<N;k++)
	// {
	// double theta = Math.PI*((2.0*n +1)*k)/(2.0*N);
	// double cos = Math.cos(theta);
	// float w;
	// if(k == 0)
	// {
	// w=(float) (1.0/Math.sqrt(N));
	// }
	// else
	// {
	// w = (float)(Math.sqrt(2.0/N));
	// }
	// value = (float) (value + w*dct[k]*cos);
	// }
	// result[n] = value;
	//			
	// }
	// return result;
	//		
	// }

}
