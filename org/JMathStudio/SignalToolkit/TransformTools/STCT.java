package org.JMathStudio.SignalToolkit.TransformTools;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FCT1D;
import org.JMathStudio.SignalToolkit.Utilities.WindowFactory;

/**
 * This class define a discrete Short Time Chirp Transform (STCT) on a discrete
 * real signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class STCT {
	
	/**
	 * This method will apply a Short Time Chirp Transform (STCT)on a discrete
	 * real signal as represented by the {@link Vector} 'vector' and return the result
	 * as a {@link VectorStack}.
	 * <p>
	 * This method computes magnitude of the Fast Chirp Transform of the
	 * successive sections of the signal obtain by windowing with a suitably
	 * shifted window. Each Vector in the return VectorStack will be a magnitude
	 * of the FCT of the one windowed section of the signal.
	 * <p>
	 * This method make use of Gaussian Window whose width is given by the
	 * argument 'windowWidth'. The value of the argument 'windowWidth' should be
	 * more than 0 else this method will throw an IllegalArgument Exception. The
	 * argument 'windowWidth' also describe the Time Resolution for the STCT.
	 * <p>
	 * The number of sections of the signal for which FCT will be computed will
	 * be equal to the value 'signal length/ windowWidth', with a shift of
	 * 'windowWidth' for each consecutive section.
	 * 
	 * @param Vector
	 *            vector
	 * @param int windowWidth
	 * @return VectorStack
	 * @throws IllegalArgumentException
	 * @see {@link FCT1D}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack analyse(Vector vector, int windowWidth)
			throws IllegalArgumentException {
		if (windowWidth <= 0)
			throw new IllegalArgumentException();

		int shift = vector.length() / windowWidth;
		float stddev = windowWidth / 2.0f;
		float[][] result = new float[shift][vector.length()];

		FCT1D fct = new FCT1D();

		for (int i = 0; i < result.length; i++) {
			try {
				Vector gauss = WindowFactory.gaussian(vector.length(), stddev, i
						* windowWidth);
				Vector tmp = VectorMath.dotProduct(gauss, vector);
				tmp = fct.fct1D(tmp, tmp.length()).getMagnitude();
				result[i] = tmp.accessVectorBuffer();

			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}

		}
		VectorStack stack = new VectorStack();
		for (int i = 0; i < result.length; i++)
			stack.addVector(new Vector(result[i]));

		return stack;

	}

}
