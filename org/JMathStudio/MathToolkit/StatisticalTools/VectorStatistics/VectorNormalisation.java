package org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.DivideByZeroException;

/**
 * This class define various Normalisation operations on the elements of {@link Vector}.
 * 
 * <pre>Usage:
 * 
 * Let 'a' be a valid Vector object.
 * 
 * VectorNormalisation vn = new VectorNormalisation();//Create an instance of VectorNormalisation.
 * 
 * Vector norm_amp = vn.normaliseAmplitude(a);//Normalise amplitude of the input Vector so
 * as to have amplitude in the range of [-1 1].
 * 
 * Vector norm_energy = vn.normaliseEnergy(a);//Normalise input Vector so as to have unit energy.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
 */

public final class VectorNormalisation {

	private VectorStatistics stat;

	/**
	 * Constructor of the class.
	 */
	public VectorNormalisation() {
		stat = new VectorStatistics();
	}

	/**
	 * This method will normalise the statistics, i.e. Mean =0 and Standard
	 * Deviation =1, of the elements of the {@link Vector} 'vector' and return
	 * the statistically normalised Vector.
	 * <p>
	 * Elements of such a statistically normalised Vector will have a zero mean
	 * and an unit standard deviation.
	 * <p>
	 * If the given Vector 'vector' has zero standard deviation than statistical
	 * normalisation is not possible. In such a scenario this method will throw
	 * a DivideByZero Exception.
	 * <p>
	 * Statistical normalisation is carried out as follows,
	 * <p>
	 * <i> NX = (X - mean)/std.
	 * <p>
	 * where, NX is the statistical normalised element.
	 * <p>
	 * X is the pre normalised element.
	 * <p>'mean' and 'std' are the mean and standard deviation respectively of
	 * the Vector to be statistically normalised. </i>
	 * <p>
	 * A statistically normalised Vector elements will have a zero mean and an
	 * unit standard deviation.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector normaliseStatistics(Vector vector)
			throws DivideByZeroException {
		float mean = stat.mean(vector);
		float std = stat.standardDeviation(vector);

		if (std == 0)
			throw new DivideByZeroException();
		else {
			Vector result = new Vector(vector.length());

			for (int i = 0; i < result.length(); i++) {
				result.setElement((vector.getElement(i) - mean) / std, i);
			}

			return result;
		}

	}

	/**
	 * This method will normalise the Amplitude range of the elements of the
	 * {@link Vector} 'vector' to the range of [-1 1].
	 * <p>
	 * The resultant {@link Vector} with normalised amplitude range will be
	 * return by this method.
	 * <p>
	 * Amplitude range normalisation linearly maps the elements of the Vector
	 * 'vector' to a range of [-1 1], by dividing all the elements with the
	 * maximum absolute valued element of the Vector.
	 * <p>
	 * Thus the resultant return Vector will have all the elements within the
	 * range of [-1 1].
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector normaliseAmplitude(Vector vector) {

		Vector result = new Vector(vector.length());

		float max = Math.abs(vector.getElement(0));

		for (int i = 1; i < vector.length(); i++) {
			float tmp = Math.abs(vector.getElement(i));
			max = tmp > max ? tmp : max;
		}

		if (max == 0)
			return result;
		else {
			for (int i = 0; i < result.length(); i++)
				result.setElement(vector.getElement(i) / max, i);

		}

		return result;

	}

	/**
	 * This method will normalise the Energy content of the elements of
	 * {@link Vector} 'vector' and return the resultant Vector with an unit
	 * energy.
	 * <p>
	 * A normalise energy Vector has energy of 1 unit.
	 * <p>
	 * If the {@link Vector} 'vector' has zero energy, this method will return
	 * the clone of the original zero energy Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector normaliseEnergy(Vector vector) {
		float factor = (float) Math.sqrt(stat.energy(vector));

		Vector result = new Vector(vector.length());

		if (factor == 0) {
			return result;
		} else {
			for (int i = 0; i < result.length(); i++) {
				result.setElement(vector.getElement(i) / factor, i);
			}

			return result;
		}

	}

	/**
	 * This method will normalise the Absolute sum of the elements of the
	 * {@link Vector} 'vector' and return the normalised absolute sum Vector.
	 * <p>
	 * A normalised absolute sum Vector has the sum of absolute value of all its
	 * elements '1'.
	 * <p>
	 * A normalised absolute sum Vector is obtained by dividing all the elements
	 * of the Vector by its absolute sum.
	 * <p>
	 * If the absolute sum of the Vector 'vector' is '0', this method will
	 * return the clone of the original Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector normaliseAbsoluteSum(Vector vector) {
		float absSum = stat.absoluteSum(vector);

		Vector result = new Vector(vector.length());

		// No need to clone the original Cell 'cell' as absolute sum is zero
		// means all
		// the elements are '0', thus create a new same dimension zero Cell and
		// return it.
		// which will be same as the original Cell.

		if (absSum == 0) {
			return result;
		} else {
			for (int i = 0; i < result.length(); i++) {
				result.setElement(vector.getElement(i) / absSum, i);
			}

			return result;
		}
	}

}
