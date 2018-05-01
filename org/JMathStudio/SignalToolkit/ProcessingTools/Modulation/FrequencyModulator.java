package org.JMathStudio.SignalToolkit.ProcessingTools.Modulation;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.CVectorMath;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.Utilities.Calculus;
import org.JMathStudio.SignalToolkit.GeneralTools.SignalUtilities;
import org.JMathStudio.SignalToolkit.TransformTools.Hilbert;
import org.JMathStudio.SignalToolkit.Utilities.SignalGenerator;

/**
 * This class define a Frequency Modulator which,
 * <p><i>1. Modulates a carrier frequency based on the message signal using the frequency modulation.
 * <p>2. Demodulates message signal from the frequency modulated carrier signal.
 * </i>
 * <p>The message signal and modulated carrier signal shall be represented by a {@link Vector}
 * object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * FrequencyModulator fm = new FrequencyModulator();//Create an instance of FrequencyModulator.
 * 
 * Vector fre_mod = fm.modulate(a, fc, fs, freqDev, initPhase);//Frequency modulate the carrier
 * signal with specified parameters with message signal as given by input Vector 'a'.
 * 
 * Vector recover = fm.demodulate(fre_mod, fc, fs, freqDev, initPhase);//Recover message signal
 * from modulated signal.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FrequencyModulator {

	/**
	 * This method will frequency modulate the carrier frequency based on the message signal as represented by the
	 * {@link Vector} 'signal' and return the modulated carrier signal as a Vector.
	 * <p>The argument 'fc' specify the carrier frequency in hertz and should be more than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>The argument 'fs' specify the sampling frequency in hertz for the message signal and carrier signal.
	 * The argument 'fs' should be more than 0 else this method will throw an IllegalArgument Exception.
	 * <p>Further the argument 'fs' and 'fc' should satisfy following condition,
	 * <p><i> fs >= 2*fc </i>
	 * <p>else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'freqDev' specify the frequency deviation of the modulated signal in hertz.
	 * The argument 'freqDev' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>The argument 'initPhase' specify the initial phase in radians of the message signal.
	 * The argument 'initPhase' should not be negative else this method will throw an IllegalArgument
	 * Exception. 
	 * @param Vector signal
	 * @param float fc
	 * @param float fs
	 * @param float freqDev
	 * @param float initPhase
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector modulate(Vector signal,float fc,float fs,float freqDev,float initPhase) throws IllegalArgumentException
	{
		//Based on matlab fmmod m-file.
		if(fc <=0 || fs <=0 || initPhase <0 || fs < 2*fc || freqDev <=0)
			throw new IllegalArgumentException();
		else
		{
			try{
				int length = signal.length();
				float timeSpacing = 1.0f/fs;
				
				Vector index = SignalGenerator.rampUp(0, timeSpacing, length);
				float[] cumsum = new float[length];
				cumsum[0] = signal.getElement(0);
				
				for(int i=1;i<length;i++)
					cumsum[i] = cumsum[i-1] + signal.getElement(i);
				
				Vector result = new Vector(length);
				float tmp;
				double wc = 2*Math.PI*fc;
				double wfd = 2*Math.PI*freqDev/fs;
				
				for(int i=0;i<length;i++)
				{
					tmp = (float) Math.cos(wc*index.getElement(i) + wfd*cumsum[i] + initPhase);
					result.setElement(tmp, i);
				}
				
				return result;
				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			
			} catch (IllegalArgumentException e) {
				
				throw new BugEncounterException();
			}
		}
	}
	/**
	 * This method will demodulate the message signal from the frequency modulated carrier signal as represented
	 * by {@link Vector} 'signal' and return the same as a {@link Vector}.
	 * <p>The argument 'fc' here specify the carrier frequency in hertz and should be more than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>The argument 'fs' specify the sampling frequency in hertz for the message signal and carrier signal.
	 * The argument 'fs' should be more than 0 else this method will throw an IllegalArgument Exception.
	 * <p>Further the argument 'fs' and 'fc' should satisfy following condition,
	 * <p><i> fs >= 2*fc </i>
	 * <p>else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'freqDev' specify the frequency deviation of the modulated signal in hertz.
	 * The argument 'freqDev' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>The argument 'initPhase' specify the initial phase in radians of the original message signal.
	 * The argument 'initPhase' should not be negative else this method will throw an IllegalArgument
	 * Exception. 
	 * @param Vector signal
	 * @param float fc
	 * @param float fs
	 * @param float freqDev
	 * @param float initPhase
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector demodulate(Vector signal,float fc,float fs,float freqDev,float initPhase) throws IllegalArgumentException
	{
		//Based on matlab fmdemod m-file.
		if(fc <=0 || fs <=0 || initPhase <0 || fs < 2*fc || freqDev <=0)
			throw new IllegalArgumentException();
		else
		{
			try{
				
				int length = signal.length();
				float timeSpacing = 1.0f/fs;
				
				Vector index = SignalGenerator.rampUp(0, timeSpacing, length);
				CVector hilbert = new Hilbert().transform(signal);
				
				float zReal[] = new float[length];
				float zImg[] = new float[length];
				double wc = 2*fc*Math.PI;
				double theta;
				
				for(int i=0;i<length;i++)
				{
					theta = wc*index.getElement(i) - initPhase;
					zReal[i] = (float) Math.cos(theta);
					zImg[i] = (float) -Math.sin(theta);
				}
				
				//The following code is approximated to the code steps in the
				//matlab code file.
				CVector temp = new CVector(zReal,zImg);
				
				temp = CVectorMath.dotProduct(hilbert, temp);
				
				Vector angle = new SignalUtilities().unwrap(temp.getAngle());
								
				angle = new Calculus().difference(angle);
				
				float normalizer = (float) (fs/(2*Math.PI*freqDev));
				
				return VectorMath.multiply(normalizer, angle);
				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			} catch (IllegalArgumentException e) {
				
				throw new BugEncounterException();
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}
	}
}
