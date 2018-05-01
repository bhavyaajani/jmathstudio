package org.JMathStudio.SignalToolkit.ProcessingTools.Cepstrum;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.GeneralTools.SignalUtilities;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;

/**
 * This class define a Complex Cepstrum and its inverse for Cepstral Analysis of a 
 * discrete real signal.
 * <p>A discrete real signal will be represented by a {@link Vector} object.
 * <p>This class uses FFT in the computation of the Complex cepstrum and its inverse.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * ComplexCepstrum cc = new ComplexCepstrum();//Create an instance of ComplexCepstrum.
 * 
 * CCepstrum cepstrum = cc.complexCepstrum(a);//Estimate complex cepstrum for signal as
 * represented by input Vector.
 * 
 * Vector b = cc.iComplexCepstrum(cepstrum);//Get inverse for input complex cepstrum.
 * </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
 */
public final class ComplexCepstrum {
	
	private int i7=0;
	private SignalUtilities util = new SignalUtilities();
	
	/**
	 * This method compute the Complex Cepstrum of the discrete real signal as represented by the 
	 * {@link Vector} 'vector' and return the resultant Complex Cepstrum as a {@link CCepstrum}.
	 * <p>The method shall add a linear phase term to the input signal (ie. circularly shift the signal
	 * after zero padding) before computing its complex cepstrum to have no phase discontinuity at
	 * +/- PI radians.
	 * <p>The amount of linear phase term added to the signal during computation of the complex
	 * cepstrum will be defined within the returned {@link CCepstrum} so that the inverse can be
	 * computed with minimum phase distortion.
	 * <p>For some of the discrete real signals complex cepstrum is not define. For such
	 * cases this method shall return a 'null'.
	 * <p>Ensure that the return cepstrum is not 'null' before using the same.
	 * @param Vector vector
	 * @return CCepstrum
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCepstrum complexCepstrum(Vector vector) {
			
			CVector fft = null;
			CVector result = null;
			try {
				FFT1D fft_ = new FFT1D(vector.length());
				fft = fft_.fft1D(vector);
				
				Vector mag = fft.getMagnitude();

				//It may happen that for some kind of input signals
				//magnitude value is zero and so 'log' will throw
				//an exception.
				//Such signals do not have complex cepstrum so return
				//null.
				try{
					mag = VectorMath.logE(mag);
				}catch(IllegalArgumentException e)
				{
					return null;
				}
				Vector phase = f0(fft.getAngle());
				result = new CVector(mag, phase);

			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}

			int nd = this.i7;
			
			//To have phase term to 0 each time this method is called again.
			this.i7 = 0;
			return new CCepstrum(FFT1D.ifft(result), nd);
			
	}
	
		/**
		 * This method computes the inverse of the complex cepstrum as represented by the 
		 * {@link CCepstrum} 'cepstrum' and return the inverse as a Vector.
		 * <p>The method make use of the linear phase term added to the original signal during
		 * the computation of the complex cepstrum, in order to minimise any phase distortion 
		 * in the resultant inverse.
		 * <p>However in some of the cases the resultant inverse shall have some amount of
		 * phase distortion.
		 * @param CCepstrum cepstrum
		 * @return Vector
		 * @author Ajani Bhavya - (jmathstudio@gmail.com)
		 */
	public Vector iComplexCepstrum(CCepstrum cepstrum)
	{
		try{
		
		Vector cCepstrum = cepstrum.accessComplexCepstrum();
		FFT1D fft_ = new FFT1D(cCepstrum.length());
		CVector fft = fft_.fft1D(cCepstrum);
		
		Vector real = VectorMath.exponential(fft.accessRealPart());
		Vector img = f3(fft.accessImaginaryPart(), cepstrum.getLinearPhaseTerm());
		
		Vector r = new Vector(real.length());
		Vector i = new Vector(img.length());
		
		float rtmp,itmp;
		
		for(int j=0;j<r.length();j++)
		{
			rtmp = (float) (real.getElement(j)*Math.cos(img.getElement(j)));
			itmp = (float) (real.getElement(j)*Math.sin(img.getElement(j)));
			
			r.setElement(rtmp, j);
			i.setElement(itmp, j);
		}
		
		return FFT1D.ifft(new CVector(r,i));
		
		}catch(DimensionMismatchException e)
		{
			throw new BugEncounterException();
			
		} catch (IllegalArgumentException e) 
		{
			throw new BugEncounterException();
		}		
		
	}
	
	private Vector f0(Vector phases)
	{
		Vector y = util.unwrap(phases);

		int nh = (int) Math.floor((y.length()+1.0)/2.0);

		int nd = (int) Math.round(y.getElement(nh+1)/Math.PI);

		this.i7 = nd;
		
		float tmp;
		double multiplier = Math.PI*nd/nh;
		
		for(int i=0;i<y.length();i++)
		{
			tmp = (float) (y.getElement(i) - i*multiplier);
			y.setElement(tmp, i);

		}

		return y;
	}
	
	private Vector f3(Vector phases,int nd)
	{
		int n = phases.length();
		int nh = (int) Math.floor((n+1.0f)/2.0f);
		
		Vector res = new Vector(n);
		
		double multiplier = Math.PI*nd/nh;
		float tmp;
		
		for(int i=0;i<res.length();i++)
		{
			tmp = (float) (phases.getElement(i) + i*multiplier);
			res.setElement(tmp, i);

		}
		return res;
	}
}
