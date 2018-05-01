package org.JMathStudio.SignalToolkit.ProcessingTools.Modulation;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.CVectorMath;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.Hilbert;
import org.JMathStudio.SignalToolkit.Utilities.SignalGenerator;

/**
 * This class define a Phase Modulator which,
 * <p>
 * <i>1. Modulates a carrier frequency based on the message signal using the
 * phase modulation.
 * <p>
 * 2. Demodulates message signal from the phase modulated carrier signal. </i>
 * <p>
 * The message signal and modulated carrier signal shall be represented by a
 * {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * PhaseModulator pm = new PhaseModulator();//Create an instance of PhaseModulator.
 * 
 * Vector phase_mod = pm.modulate(a, fc, fs, phaseDev, initPhase);//Phase modulate the carrier
 * signal with specified parameters with message signal as given by input Vector 'a'.
 * 
 * Vector recover = pm.demodulate(phase_mod, fc, fs, phaseDev, initPhase);//Recover message signal
 * from modulated signal.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class PhaseModulator {

	/**
	 * This method will phase modulate the carrier frequency based on the
	 * message signal as represented by the {@link Vector} 'signal' and return
	 * the modulated carrier signal as a Vector.
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
	 * The argument 'phaseDev' specify the phase deviation of the modulated
	 * signal in radians. The argument 'phaseDev' should be more than 0 else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'initPhase' specify the initial phase in radians of the
	 * message signal. The argument 'initPhase' should not be negative else this
	 * method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            signal
	 * @param float fc
	 * @param float fs
	 * @param float phaseDev
	 * @param float initPhase
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector modulate(Vector signal, float fc, float fs, float phaseDev,
			float initPhase) throws IllegalArgumentException {
		// Based on matlab pmmod m-file.
		if (fc <= 0 || fs <= 0 || phaseDev <= 0 || initPhase < 0 || fs < 2 * fc)
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
					tmp = (float) Math.cos(wc * index.getElement(i)
							+ signal.getElement(i) * phaseDev + initPhase);
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
	 * This method will demodulate the message signal from the phase modulated
	 * carrier signal as represented by {@link Vector} 'signal' and return the
	 * same as a {@link Vector}.
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
	 * The argument 'phaseDev' specify the phase deviation of the modulated
	 * signal in radians. The argument 'phaseDev' should be more than 0 else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'initPhase' specify the initial phase in radians of the
	 * original message signal. The argument 'initPhase' should not be negative
	 * else this method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            signal
	 * @param float fc
	 * @param float fs
	 * @param float phaseDev
	 * @param float initPhase
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector demodulate(Vector signal, float fc, float fs, float phaseDev,
			float initPhase) throws IllegalArgumentException {
		// Based on matlab pmdemod m-file.
		if (fc <= 0 || fs <= 0 || initPhase < 0 || fs < 2 * fc || phaseDev <= 0)
			throw new IllegalArgumentException();
		else {
			try {

				int length = signal.length();
				float timeSpacing = 1.0f / fs;

				Vector index = SignalGenerator.rampUp(0, timeSpacing, length);
				CVector hilbert = new Hilbert().transform(signal);

				float zReal[] = new float[length];
				float zImg[] = new float[length];
				double wc = 2 * fc * Math.PI;
				double wp = 2 * Math.PI * initPhase;

				double theta;

				for (int i = 0; i < length; i++) {
					theta = wc * index.getElement(i) + wp;
					zReal[i] = (float) Math.cos(theta);
					zImg[i] = (float) -Math.sin(theta);
				}

				CVector temp = new CVector(zReal, zImg);

				temp = CVectorMath.dotProduct(hilbert, temp);

				Vector angle = temp.getAngle();

				float normalizer = 1.0f / phaseDev;

				return VectorMath.multiply(normalizer, angle);

			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			} catch (IllegalArgumentException e) {

				throw new BugEncounterException();
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}
	}

}
