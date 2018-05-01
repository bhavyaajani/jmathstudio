package org.JMathStudio.SignalToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.CVectorMath;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;

/**
 * This class define various methods for estimating Spectral density/Spectrum of a discrete real signal.
 * <p> A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' and 'b' be valid Vector objects.
 * 
 * SignalSpectrum ss = new SignalSpectrum();//Create an instance of SignalSpectrum.
 * 
 * Vector esd = ss.ESD(a);//Estimate energy spectral density (ESD) for signal as represented by
 * input Vector.
 * 
 * Vector cesd = ss.CESD(a, b);//Estimate cross energy spectral density (CESD) for signals as 
 * represented by input Vectors.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SignalSpectrum {
	private VectorTools vtools = new VectorTools();
	
	/**
	 * This method will compute the two sided Energy Spectral Density of the discrete real
	 * energy signal as represented by {@link Vector} 'vector' using correlogram approach and
	 * return the same as a {@link Vector}.
	 * <p>The ESD is computed from the 1D FFT of the linear autocorrelation function of the given
	 * vector. The return Vector representing the ESD has DC energy component located
	 * at the centre with elements on the either side represent the energy for the corresponding 
	 * evenly spread normalised frequencies in the range of [-0.5 0.5] respectively. Further the
	 * length of the return Vector will be 2N-1 corresponding to 2N-1 discrete frequencies. Here
	 * 'N' being the length of the original Vector.
	 * @param Vector vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector ESD(Vector vector)
	{
		try{
			//Essentially, ESD = FFT(R(x)), where R(x) is linear auto correlation of x(t).
			//This is equal to following,
			//ESD = F(w)* F'(w), where F(w) is FFT(x(t)) and F'(w) is conjugate of F(w).
			//This is equal to, |abs(F(w))|^2.
			
			int size = 2*vector.length()-1;
			FFT1D fft_ = new FFT1D(size);
			CVector fft;
			
			if(vector.length() == size)
				fft =  fft_.fft1D(vector);
			else
				fft = fft_.fft1D(vtools.resize(vector, size));
			
			Vector esd = fft.getMagnitude();

			esd = VectorMath.power(esd, 2);
						
			return vtools.wrapVector(esd);
		}
		catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will compute the two sided Cross Energy Spectral Density for the discrete real
	 * energy signals as represented by {@link Vector}'s 'vector1' and 'vector2' using cross correlogram
	 * approach and return the same as a {@link Vector}.
	 * <p>The CESD is computed from the 1D FFT of the linear cross correlation function for the given
	 * vectors. The return Vector representing the CESD has DC energy component located
	 * at the centre with elements on the either side represent the energy for the corresponding evenly spread 
	 * normalised frequencies in the range of [-0.5 0.5] respectively. Further the
	 * length of the return Vector will be N+M-1 corresponding to N+M-1 discrete frequencies. Here
	 * 'N' and 'M' being the length of the original Vector's respectively.
	 * @param Vector vector1
	 * @param Vector vector2
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector CESD(Vector vector1, Vector vector2)
	{
		try{
			
			int size = vector1.length()+ vector2.length() - 1;
			FFT1D fft_ = new FFT1D(size);
			CVector fft_y,fft_x;
			
			if(vector1.length() == size)
				fft_y =  fft_.fft1D(vector1);
			else
				fft_y = fft_.fft1D(vtools.resize(vector1, size));
		
			if(vector2.length() == size)
				fft_x =  fft_.fft1D(vector2);
			else
				fft_x = fft_.fft1D(vtools.resize(vector2, size));
		
			fft_y = CVectorMath.dotProduct(fft_y, fft_x.getConjugate());
			
			Vector esd = fft_y.getMagnitude();

			return vtools.wrapVector(esd);
		}
		catch(IllegalArgumentException e)
		{   throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will compute the two sided Power Spectral Density of the discrete real
	 * periodic signal, whose one period is as represented by {@link Vector} 'vector' using 
	 * correlogram approach and return the same as a {@link Vector}.
	 * <p>The PSD is computed from the 1D FFT of the circular autocorrelation function of the given
	 * vector. The return Vector representing the PSD has DC power component located
	 * at the centre with elements on the either side represent the power for the corresponding 
	 * evenly spread normalised frequencies in the range of [-0.5 0.5] respectively. Further the
	 * length of the return Vector will be N corresponding to N discrete frequencies. Here
	 * 'N' being the length of the original Vector.
	 * @param Vector vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector PSD(Vector vector)
	{
		try{
			//Essentially, PSD = FFT(R(x))/N, where R(x) is circular auto correlation of x(t).
			//This is equal to following,
			//PSD = F(w)* F'(w)/N, where F(w) is FFT(x(t)) and F'(w) is conjugate of F(w).
			//This is equal to, (|abs(F(w))|^2)/N.
			
			int N = vector.length();
			FFT1D fft_ = new FFT1D(N);
			
			CVector fft;
			
			if(vector.length() == N)
				fft =  fft_.fft1D(vector);
			else
				fft = fft_.fft1D(vtools.resize(vector, N));
		
			Vector psd = fft.getMagnitude();

			psd = VectorMath.power(psd, 2);
			
			psd = VectorMath.linear(1.0f/N, 0, psd);
						
			return vtools.wrapVector(psd);
		}
		catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}

	}
	
//	public Vector spectral_coherence(Vector vector1, Vector vector2)
//	{
//		int L = (vector1.length() >= vector2.length()) ? vector1.length():vector2.length();
//		
//		float norm = (vector1.length()+vector2.length())/2.0f;
//		
//		VectorTools tools = new VectorTools();
//		
//		try{
//		Vector esd1 = ESD(tools.resize(vector1,L), false);
//		Vector esd2 = ESD(tools.resize(vector2,L), false);
//		Vector csd = CESD(vector1, vector2, false);
//		
//		Vector coherence = new Vector(L);
//		float p1,p2,c;
//		
//		for(int i=0;i<L;i++)
//		{
//			p1 = esd1.getElement(i);
//			p2 = esd2.getElement(i);
//			c = csd.getElement(i);
//			
//			coherence.setElement(c*c/(norm*p1*p2), i);
//		}
//		
//		return coherence;
//		}catch(IllegalArgumentException e){
//			throw new BugEncounterException();
//		}
//	}
	
}
