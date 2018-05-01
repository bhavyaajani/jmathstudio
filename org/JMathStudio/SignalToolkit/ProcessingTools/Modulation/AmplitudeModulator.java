package org.JMathStudio.SignalToolkit.ProcessingTools.Modulation;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.FilterTools.IIRFilter;
import org.JMathStudio.SignalToolkit.FilterTools.IIRFilterMaker;
import org.JMathStudio.SignalToolkit.Utilities.SignalGenerator;

/**
 * This class define an Amplitude Modulator which,
 * <p>
 * <i>1 . Modulates a carrier frequency based on the message signal using the
 * amplitude modulation.
 * <p>
 * The amplitude modulation define here is a suppressed carrier modulation.
 * <p>
 * 2. Demodulates message signal from the suppressed carrier amplitude modulated
 * carrier signal. </i>
 * <p>
 * The message signal and modulated carrier signal shall be represented by a
 * {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * AmplitudeModulator am = new AmplitudeModulator();//Create an instance of AmplitudeModulator.
 * 
 * Vector amp_mod = am.modulate(a, fc, fs, initPhase);//Amplitude modulate the carrier signal with
 * frequency 'fc' and sampling rate of 'fs' with message signal as given by input Vector 'a'.
 * 
 * Vector recover = am.demodulate(amp_mod, fc, fs, initPhase);//Recover message signal from 
 * modulated signal.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class AmplitudeModulator {

	/**
	 * This method will amplitude modulate the carrier frequency based on the
	 * message signal as represented by the {@link Vector} 'signal' and return
	 * the modulated carrier signal as a Vector.
	 * <p>
	 * The amplitude modulation used here is a suppressed carrier amplitude
	 * modulation with amplitude of the carrier frequency '0'.
	 * <p>
	 * The argument 'fc' specify the carrier frequency in hertz and should be
	 * more than 0 else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'fs' specify the sampling frequency in hertz for the message
	 * signal and carrier signal. The argument 'fs' should be more than 0 else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * Further the argument 'fs' and 'fc' should satisfy following condition,
	 * <p>
	 * <i> fs >= 2*fc </i>
	 * <p>
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * Note: The following condition should be satisfied for satisfactory
	 * results,
	 * <p>
	 * <i>fs > 2(fc + BW),
	 * <p>
	 * where 'BW' is the band width of the message signal.</i>
	 * <p>
	 * The argument 'initPhase' specify the initial phase in radians of the
	 * message signal. The argument 'initPhase' should not be negative else this
	 * method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            signal
	 * @param float fc
	 * @param float fs
	 * @param float initPhase
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector modulate(Vector signal, float fc, float fs, float initPhase)
			throws IllegalArgumentException {
		// Based on matlab ammod m-file.
		if (fc <= 0 || fs <= 0 || initPhase < 0 || fs < 2 * fc)
			throw new IllegalArgumentException();
		else {
			try {
				int length = signal.length();
				float timeSpacing = 1.0f / fs;

				Vector index = SignalGenerator.rampUp(0, timeSpacing, length);

				Vector result = new Vector(length);

				double wc = 2 * Math.PI * fc;

				float tmp = 0;
				for (int i = 0; i < length; i++) {
					tmp = (float) (signal.getElement(i) * (Math.cos(wc
							* index.getElement(i) + initPhase)));
					result.setElement(tmp, i);
				}

				return result;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will demodulate the message signal from the suppressed
	 * carrier amplitude modulated carrier signal as represented by
	 * {@link Vector} 'signal' and return the same as a {@link Vector}.
	 * <p>
	 * The argument 'fc' here specify the carrier frequency in hertz and should
	 * be more than 0 else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'fs' specify the sampling frequency in hertz for the message
	 * signal and carrier signal. The argument 'fs' should be more than 0 else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * Further the argument 'fs' and 'fc' should satisfy following condition,
	 * <p>
	 * <i> fs >= 2*fc </i>
	 * <p>
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * Note: The following condition should be satisfied for satisfactory
	 * results,
	 * <p>
	 * <i>fs > 2(fc + BW),
	 * <p>
	 * where 'BW' is the band width of the original message signal.</i>
	 * <p>
	 * The argument 'initPhase' specify the initial phase in radians of the
	 * original message signal. The argument 'initPhase' should not be negative
	 * else this method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            signal
	 * @param float fc
	 * @param float fs
	 * @param float initPhase
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector demodulate(Vector signal, float fc, float fs, float initPhase)
			throws IllegalArgumentException {
		// Based on matlab amdemod m-file.
		if (fc <= 0 || fs <= 0 || fs < 2 * fc || initPhase < 0)
			throw new IllegalArgumentException();
		else {
			try {

				int length = signal.length();
				float timeSpacing = 1.0f / fs;

				Vector index = SignalGenerator.rampUp(0, timeSpacing, length);

				double wc = 2 * Math.PI * fc;
				Vector z = new Vector(length);
				float tmp;

				for (int i = 0; i < length; i++) {
					tmp = (float) (signal.getElement(i) * Math.cos(wc
							* index.getElement(i) + initPhase));
					z.setElement(tmp, i);
				}

				// The matlab filtfilt filter is approximated here with
				// Butterworth
				// IIR filter with one forward and one backward filtering.
				IIRFilter filter = IIRFilterMaker.butterWorthLowPass(5,	fc / fs);
				VectorTools tools = new VectorTools();

				z = filter.filter(z);
				z = tools.flip(z);

				z = filter.filter(z);
				z = tools.flip(z);

				z = VectorMath.multiply(2, z);

				return z;

			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();

			}
		}
	}

}
