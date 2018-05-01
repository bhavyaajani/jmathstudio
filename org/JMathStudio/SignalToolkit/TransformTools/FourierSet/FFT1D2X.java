package org.JMathStudio.SignalToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class defines transforms to compute simultaneously two discrete Fast Fourier Transform (FFT) and/or its inverse for 2 1D
 * real signals of equal length by employing a single discrete Fast Fourier Transform and/or its inverse over a complex signal 
 * by utilizing redundancy in FFT computation for real signals.
 * <p>This transform computes FFT of 2 real input signals by taking single FFT over a complex hybrid signal made up of 
 * input real signals and later on splitting the FFT coefficients for individual real signals from FFT coefficients of complex
 * hybrid signal.
 * <p>Similarly the above transform is applied for inverse FFT by first merging the individual FFT coefficients or spectra and
 * applying a single inverse FFT over merged spectra and later on splitting the individual real signals from the estimated 
 * inverse FFT.
 * <p>Internally this class uses FFT1D to compute FFT and hence refer {@link FFT1D} for further information on the FFT computation.
 * <p>This class help to improve performance in scenario where multiple FFTs or inverse FFTs are to be computed for real signals with
 * same length.
 *   
 * <pre>Usage:
 * Let 'a' & 'b' be a valid Vector object representing two real signals of equal length.
 *		
 * FFT1D2X fft2x = new FFT1D2X();
 * CVector[] ffts = fft2x.fft2X(a, b);//Here ffts[0] & ffts[1] are FFT for signals 'a' & 'b' respectively.
 *  
 * Vector[] recover = fft2x.ifft2X(ffts[0], ffts[1]);//Recover original real signals by taking a single inverse FFT. 
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FFT1D2X {

	/**
	 * This method computes simultaneously FFT of two real signals represented by {@link Vector}s sig1 & sig2 having same length,
	 * by using a single FFT over a complex hybrid signal made up of input real signals. 
	 * <p>FFT coefficients of individual input real signals are extracted from the FFT coefficients of the complex hybrid signal.
	 * <p>The computed FFTs for real input signals are return as a {@link CVector} array of size 2 where first & second CVector 
	 * represents FFTs for input real signals 'sig1' and 'sig2' respectively.
	 * <p>If length of Vector 'sig1' and 'sig2' are not same this method shall throw an {@link IllegalArgumentException}. 
	 *  
	 * <P>Refer {@link FFT1D#fft1D(Vector)} for understanding CVector representing FFT coefficients.
	 * @param Vector sig1
	 * @param Vector sig2
	 * @return CVector[]
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector[] fft2X(Vector sig1,Vector sig2) throws IllegalArgumentException
	{
		if(sig1 == null || sig2 == null)
			throw new NullPointerException();

		if(sig1.length() != sig2.length())
			throw new IllegalArgumentException();

		try{

			CVector csig = new CVector(sig1,sig2);
			CVector fft = FFT1D.fft(csig, csig.length());

			return f1(fft);

		}catch(Exception e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method estimates simultaneously two real signals from their individual FFT coefficients as represented by 
	 * {@link CVector}s 'fft1' and 'fft2' having same length, by using a single inverse FFT over a complex hybrid signal
	 * obtained by merging the individual FFT coefficients or spectra. 
	 * <p>Real signals are obtained as real and imaginary part of the computed inverse FFT of the complex hybrid signal.
	 * <p>The estimated real signals are return as a {@link Vector} array of size 2 where first & second Vector 
	 * represents real signal for input FFT coefficients 'fft1' and 'fft2' respectively.
	 * <p>If length of input CVector 'fft1' and 'fft2' are not same this method shall throw an {@link IllegalArgumentException}. 
	 *  
	 * <P>Refer {@link FFT1D#ifft1D(CVector)} for understanding CVector representing FFT coefficients.
	 * @param CVector fft1
	 * @param CVector fft2
	 * @return Vector[]
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector[] ifft2X(CVector fft1, CVector fft2) throws IllegalArgumentException
	{
		if(fft1 == null || fft2 == null)
			throw new NullPointerException();

		if(fft1.length() != fft2.length())
			throw new IllegalArgumentException();

		try{
			CVector mfft = f3(fft1, fft2);
			CVector csig = FFT1D.ifftComplex(mfft);

			Vector[] res = {csig.accessRealPart(),csig.accessImaginaryPart()};

			return res;

		}catch(Exception e){
			throw new BugEncounterException();
		}
	}

	private CVector[] f1(CVector fft)
	{
		if(fft == null)
			throw new NullPointerException();

		try{

			final int N = fft.length();
			final int Nh = N/2;

			float real[] = fft.accessRealPart().accessVectorBuffer();
			float im[] = fft.accessImaginaryPart().accessVectorBuffer();

			CVector fft1 = new CVector(N);
			CVector fft2 = new CVector(N);

			float[] real1 = fft1.accessRealPart().accessVectorBuffer();
			float[] im1 = fft1.accessImaginaryPart().accessVectorBuffer();

			float[] real2 = fft2.accessRealPart().accessVectorBuffer();
			float im2[] = fft2.accessImaginaryPart().accessVectorBuffer();

			real1[0] = real[0];
			real2[0] = im[0];
			im1[0] = 0;
			im2[0] = 0;                  

			for(int n=Nh; n > 0; n--)
			{
				final int dn = N-n;
				real1[n] = (real[n] + real[dn])/2.0f;
				im1[n] = (im[n] - im[dn])/2.0f;

				real2[n] = (im[n] + im[dn])/2.0f;
				im2[n] = (real[dn] - real[n])/2.0f;
			}

			for(int n=N-1,m=1;n>Nh;n--,m++)
			{
				real1[n] = real1[m];
				im1[n] = - im1[m];

				real2[n] = real2[m];
				im2[n] = - im2[m];	    	
			}

			CVector[] ffts = {fft1,fft2};

			return ffts;

		}catch(Exception e){
			throw new BugEncounterException();
		}
	}


	private CVector f3(CVector fft1, CVector fft2) throws IllegalArgumentException {
		if(fft1 == null || fft2 == null)
			throw new NullPointerException();

		if (fft1.length() != fft2.length())
			throw new IllegalArgumentException();

		try {
			final int N = fft1.length();
			final int Nh = N/2;

			CVector fft = new CVector(N);

			float[] real = fft.accessRealPart().accessVectorBuffer();
			float[] im = fft.accessImaginaryPart().accessVectorBuffer();

			float[] real1 = fft1.accessRealPart().accessVectorBuffer();
			float[] im1 = fft1.accessImaginaryPart().accessVectorBuffer();

			float[] real2 = fft2.accessRealPart().accessVectorBuffer();
			float[] im2 = fft2.accessImaginaryPart().accessVectorBuffer();

			real[0] = real1[0];
			im[0] = real2[0];

			for (int n = Nh; n > 0; n--)
			{
				final int dn = N-n;
				real[n] = real1[n] + im2[dn];
				im[n] = real2[n] + im1[n];

				real[dn] = real1[dn] + im2[n];
				im[dn] = im1[dn] + real2[dn];
			}

			return fft;

		} catch (Exception e) {
			throw new BugEncounterException();
		}
	}
}

