package org.JMathStudio.SignalToolkit.FilterTools;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.GeneralTools.Conv1DTools;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;
import org.JMathStudio.SignalToolkit.Utilities.SignalGenerator;

/**
 * This class define a Digital Impulse Filter which is characterised by an impulse response.
 * <pre>Usage:
 * Let 'impulse' be a valid Vector representing the characteristic impulse response for
 * desired ImpFilter.
 * 
 * ImpFilter imp_filt = new ImpFilter(impulse,0);//Create an instance of ImpFilter with given
 * impulse response as represented by Vector and with origin at 0th index location.
 * 
 * Let 'signal' be a valid Vector object.
 * Vector result = imp_filt.filter(signal);//Filter input signal as represented by Vector.
 * 
 * imp_filt.showMagnitudeResponse(128);//Display 128 points Magnitude response of given
 * ImpFilter.
 * </pre>	
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImpFilter 
{
	private Vector impulse;
	private int i3;
	private Conv1DTools ctools = new Conv1DTools();
	private VectorTools vtools = new VectorTools();
	
	/**
	 * This will create a Digital Impulse Filter with the impulse response as represented
	 * by the Vector 'impulse'.
	 * <p>The argument 'origin' states the index location of the origin in the Impulse
	 * Vector 'impulse'. The argument 'origin' should point to a valid index location
	 * within the Vector 'impulse' else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'impulse' is passed by reference and no deep copy of the same is made.
	 * 
	 * @param Vector impulse
	 * @param int origin
	 * @throws IllegalArgumentException
	 * @see {@link Vector}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ImpFilter(Vector impulse,int origin) throws IllegalArgumentException
	{
		if(origin<0 || origin >=impulse.length())
			throw new IllegalArgumentException();
		else
		{
			this.i3 = origin;
			this.impulse = impulse;
		}
	}
	
	/**
	 * This method will return the characteristic Impulse response of this filter
	 * as a Vector.
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessImpulse()
	{
		return impulse;
	}
	
	/**
	 * This method will return the index position of the Origin in the impulse
	 * response of this filter.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getOriginInImpulse()
	{
		return i3;
	}
	/**
     * This method will filter the discrete real signal as represented by the Vector 'vector' 
     * and return the filtered signal as a Vector.
     * <p>The length of the return signal/Vector will be similar to that of the input signal/Vector.
     * <p>The filtering operation is carried out by linear convolution between the signal and the
     * impulse response of this filter.
     * <p>The resultant filtered signal is return as a Vector. This contain only that central portion
     * of the resultant convolution with the length equal to the original signal.
	 * @param Vector vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector filter(Vector vector)
	{
		Vector result = ctools.linearConv(vector, impulse);
		
		try 
		{
			return vtools.subVector(result,i3, vector.length());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	 /**
     * <p>This method will plot the 'N' point Magnitude response of this Digital Impulse filter.
     * <p>The argument 'N' specify the 'N' equi-distant frequency components in the range of
     * [-pi pi], for plotting the magnitude response.
     * <p>The argument 'N' should be a positive integer greater than or equal to the length of the 
     * impulse response of this filter else this method will throw an IllegalArgument Exception.
     * <p>This method will take into account the location/shift of the origin in the impulse response. 
     * @param int N
     * @throws IllegalArgumentException
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
	public void showMagnitudeResponse(int N) throws IllegalArgumentException
	{
		if(N < impulse.length())
			throw new IllegalArgumentException();
		try{
		FFT1D fft_ = new FFT1D(N);
		CVector fft;
		
		if(impulse.length() == N)
			fft = fft_.fft1D(impulse);
		else
			fft = fft_.fft1D(vtools.resize(impulse, N));
		
		//correction to account for phase change due to shift of origin
		//to 'origin' location.
		
		if(i3 != 0)
		{
			double wK = (2*Math.PI*i3)/impulse.length();
			Complex shift = new Complex((float)Math.cos(wK), (float)Math.sin(wK));
			
			for(int i=0;i<fft.length();i++)
				fft.setElement(fft.getElement(i).product(shift), i);
		}
		fft = FFT1D.centre(fft);
		
		Vector freq = SignalGenerator.rampUp(0, 1, fft.length());
		
		if(fft.length() != 1)
		{
			freq = VectorMath.linear((float) (Math.PI*2.0f/(fft.length()-1)), (float) -Math.PI, freq);
		}
							
		
		Vector magnitude = fft.getMagnitude();
		magnitude.plot(freq, "Magnitude Response", "Frequency - Radian","Amplitude");
			
		} catch (Exception e) 
		{
			throw new BugEncounterException();
		}
	}
	
	/**
     * <p>This method will plot the 'N' point Phase response of this Digital Impulse filter.
     * <p>The argument 'N' specify the 'N' equi-distant frequency components in the range of
     * [-pi pi], for plotting the phase response.
     * <p>The argument 'N' should be a positive integer greater than or equal to the length of the 
     * impulse response of this filter else this method will throw an IllegalArgument Exception.
     * <p>This method will take into account the location/shift of the origin in the impulse response.
     * <p>The argument 'wrapPhase' if 'True', this method will plot the phase response
     * with wrapped phases such that discontinuity between phases will not be more than
     * 2*PI.
     * @param int N
     * @param boolean wrapPhase
     * @throws IllegalArgumentException
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
	public void showPhaseResponse(int N,boolean wrapPhase) throws IllegalArgumentException
	{
		if(N < impulse.length())
			throw new IllegalArgumentException();
		try{
		FFT1D fft_ = new FFT1D(N);
		CVector fft;
		
		if(impulse.length() == N)
			fft = fft_.fft1D(impulse);
		else
			fft = fft_.fft1D(vtools.resize(impulse, N));
	
		//correction to account for phase change due to shift of origin
		//to 'origin' location.
		
		if(i3 != 0)
		{
			double wK = (2*Math.PI*i3)/impulse.length();
			Complex shift = new Complex((float)Math.cos(wK), (float)Math.sin(wK));
			
			for(int i=0;i<fft.length();i++)
				fft.setElement(fft.getElement(i).product(shift), i);
		}
		
		fft = FFT1D.centre(fft);
		
		Vector freq = SignalGenerator.rampUp(0, 1, fft.length());
		
		if(fft.length() != 1)
		{
			freq = VectorMath.linear((float) (Math.PI*2.0f/(fft.length()-1)), (float) -Math.PI, freq);
		}
		
                Vector phases = fft.getAngle();
                
                if(wrapPhase)
                    phases = this.f2(phases);
                
		phases.plot(freq, "Phase Response","Frequency - Radian","Phase - Radian");
			
		} catch (Exception e) 
		{
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will return the Frequency response of the given Digital Impulse filter 
	 * in Fourier space as a CVector.
	 * <p>The Frequency response of the filter is the 'N' point FFT of its impulse
	 * response with phase shift appropriate to the shift in origin in the impulse response.
	 * <p>Thus complex coefficients of the FFT representing the frequency response of this
	 * filter in Fourier space is return as a CVector.
	 * <p>The argument 'N' here specify the N point frequency response for the given filter.
	 * Thus the return CVector containing 'N' complex coefficients, represents frequency response
	 * for 'N' equi-spaced digital frequencies in the range of [0, 2*PI].
	 * <p>The argument 'N' should be a positive integer greater than or equal to the length of the 
     * impulse response of this filter else this method will throw an IllegalArgument Exception.
     * <p>The return CVector will have non centred FFT coefficients or frequency response i.e.
	 * DC response is not in the centre of the CVector. See {@link FFT1D#fft1D(Vector, int)} method
	 * for further clarification on this issue.
     * @param int N
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector frequencyResponse(int N) throws IllegalArgumentException
	{
		if(N < impulse.length())
			throw new IllegalArgumentException();
		try{
		FFT1D fft_ = new FFT1D(N);
		CVector fft;
		
		if(impulse.length() == N)
			fft = fft_.fft1D(impulse);
		else
			fft = fft_.fft1D(vtools.resize(impulse, N));
	
		//correction to account for phase change due to shift of origin
		//to 'origin' location.
		
		if(i3 != 0)
		{
			double wK = (2*Math.PI*i3)/impulse.length();
			Complex shift = new Complex((float)Math.cos(wK), (float)Math.sin(wK));
			
			for(int i=0;i<fft.length();i++)
				fft.setElement(fft.getElement(i).product(shift), i);
		}
		
		return fft;
		}catch (Exception e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method Wraps the radian phase angles as given by the Vector 'phases' by adding 
	 * multiples of +- 2*PI, when absolute jumps between consecutive phases
	 * are greater than or equal to the default jump tolerance of PI radians.
	 * <p>All such Wrapped phases are return as a Vector where discontinuity or jump
	 * between two consecutive phases will not be more than 2*PI. 
	 * <p>All the phases as given by the Vector 'phases' should be in radians.
	 * @param Vector phases
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	private Vector f2(Vector phases)
	{
		Vector result = phases.clone();
		
		while(!f4(result.accessVectorBuffer()))
		{
			f8(result.accessVectorBuffer());
		}
		
		return result;
	}
	
	private void f8(float[] phase) 
	{
		for(int i=1;i<phase.length;i++)
		{
			if(Math.abs(phase[i]-phase[i-1]) >= Math.PI)
			{
				float diff = phase[i]-phase[i-1];
				
				if(Math.signum(diff) == 1)
				{
					phase[i] = (float) (phase[i] - 2*Math.PI);
				}
				else if(Math.signum(diff) == -1)
				{
					phase[i] = (float) (phase[i] + 2*Math.PI);
				}
			}
		}
	}
	
	private boolean f4(float[] phase)
	{
		for(int i=1;i<phase.length;i++)
		{
			if(Math.abs(phase[i]-phase[i-1]) >= Math.PI)
			{
				return false;
			}
		}
		
		return true;
		
	}
}
