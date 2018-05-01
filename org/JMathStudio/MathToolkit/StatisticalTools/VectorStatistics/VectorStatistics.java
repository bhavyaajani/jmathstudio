package org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics;

import java.util.Arrays;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.GeneralTools.SignalUtilities;

/**
 * This class define statistical operations applicable over a {@link Vector}.
 * <pre>Usage:
 * Let 'a' & 'b' be valid Vector's with same length.
 * 
 * VectorStatistics vs = new VectorStatistics();//Create an instance of VectorStatistics.
 * 
 * double corr_coef = vs.correlation(a, b);//Compute correlation coefficient between input Vectors.
 * 
 * double energy = vs.energy(a);//Compute energy of input Vector.
 * 
 * double mean = vs.mean(a);//Compute mean value of input Vector.
 * 
 * Vector cross_corr_func = vs.crossCorrelationFunction(a, b);//Compute cross correlation function
 * for input Vector's.
 *
 * double percentile = vs.percentile(a, p);//Compute pth percentile value of the elements of input Vector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class VectorStatistics {

	private SignalUtilities util = new SignalUtilities();
	
	/**
	 * This method will return the Sum of absolute value of all the elements of
	 * the Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float absoluteSum(Vector vector) {
		float abssum = 0;
		for (int i = 0; i < vector.length(); i++) {
			abssum = abssum + Math.abs(vector.getElement(i));
		}

		return abssum;
	}

	/**
	 * This method will return the Energy content of the elements of the Vector
	 * 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float energy(Vector vector) {
		float energy = 0;
		float[] signal = vector.accessVectorBuffer();
		for (int i = 0; i < signal.length; i++) {
			energy = energy + signal[i] * signal[i];
		}

		return energy;
	}

	/**
	 * This method will return the Median value of the elements of the Vector
	 * 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float median(Vector vector) {
		float[] tmp = vector.clone().accessVectorBuffer();
		Arrays.sort(tmp);

		if (tmp.length % 2 == 0) {
			return (tmp[(tmp.length - 1) / 2] + tmp[(tmp.length - 1) / 2 + 1]) / 2;
		} else {
			return tmp[tmp.length / 2];
		}
	}

	/**
	 * This method will return the Mean value of the elements of the Vector
	 * 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float mean(Vector vector) {
		return sum(vector) / vector.length();
	}

	/**
	 * This method will return the Sum of all the elements of the Vector
	 * 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float sum(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float sum = 0;

		for (int i = 0; i < buffer.length; i++) {
			sum = sum + buffer[i];
		}
		return sum;
	}

	/**
	 * This method will return the Harmonic Mean of the positive elements of the
	 * Vector 'vector'.
	 * <p>
	 * If any element of the 'vector' is negative or zero this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float harmonicMean(Vector vector)
			throws IllegalArgumentException {

		double sum = 0;

		for (int i = 0; i < vector.length(); i++) {
			if (vector.getElement(i) <= 0)
				throw new IllegalArgumentException();
			else
				sum += (1.0 / vector.getElement(i));
		}

		return (float) (vector.length() / sum);
	}

	/**
	 * This method will return the Geometric Mean of the positive elements of
	 * the Vector 'vector'.
	 * <p>
	 * If any element of the 'vector' is negative or zero this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float geometricMean(Vector vector)
			throws IllegalArgumentException {
		double product = 1;

		for (int i = 0; i < vector.length(); i++) {
			if (vector.getElement(i) <= 0)
				throw new IllegalArgumentException();
			else
				product *= vector.getElement(i);
		}

		return (float) Math.pow(product, 1.0f / vector.length());

	}

	/**
	 * This method will return the Standard deviation value of the elements of
	 * the Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float standardDeviation(Vector vector) {
		float mean = mean(vector);
		double stddev = 0;

		float[] buffer = vector.accessVectorBuffer();

		for (int i = 0; i < buffer.length; i++) {
			float tmp = (buffer[i] - mean);
			stddev = stddev + (tmp) * (tmp);
		}

		return (float) Math.sqrt(stddev / buffer.length);
	}

	/**
	 * This method will return the Variance of the elements of the Vector
	 * 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float variance(Vector vector) {
		float mean = mean(vector);
		double var = 0;

		float[] buffer = vector.accessVectorBuffer();

		for (int i = 0; i < buffer.length; i++) {
			float tmp = (buffer[i] - mean);
			var = var + (tmp) * (tmp);
		}

		return (float) (var / buffer.length);
	}

	/**
	 * This method will return the k'th order Central Moment of the elements of
	 * the Vector 'vector'.
	 * <p>
	 * The order of the moment is given by the argument 'k' which should not be
	 * less than 1. For k=1, the central moment will be zero.
	 * <p>
	 * If value of argument 'k' is less than 1, this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param int k
	 * @return double
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double centralMoment(Vector vector, int k)
			throws IllegalArgumentException {
		if (k < 1) {
			throw new IllegalArgumentException();
		}
		float mean = mean(vector);
		double moment = 0;

		float[] buffer = vector.accessVectorBuffer();

		for (int i = 0; i < buffer.length; i++) {
			moment = moment + Math.pow((buffer[i] - mean), k);
		}

		return moment / buffer.length;
	}

	/**
	 * This method will return the Cumulative Mass Function of the Probability
	 * Mass Function as given by the Vector 'vector', as Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector cdf(Vector vector) {
		float[] pdf = vector.accessVectorBuffer();

		float[] result = new float[pdf.length];
		result[0] = pdf[0];

		for (int i = 1; i < result.length; i++) {
			result[i] = result[i - 1] + pdf[i];
		}

		return new Vector(result);
	}

	/**
	 * This method will return the Root Mean Square value of the elements of the
	 * Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float RMS(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float rms = 0;

		for (int i = 0; i < buffer.length; i++) {
			rms = rms + buffer[i] * buffer[i];
		}

		rms = rms / buffer.length;

		return (float) Math.sqrt(rms);
	}

	/**
	 * This method will compute the Covariance coefficient between the two
	 * Vectors given by the argument 'vector1' and 'vector2'.
	 * <p>
	 * If length of both the Vector is not same Vector with smaller length is
	 * padded with appropriate numbers of zero so as to make the length of both
	 * the Vector equal.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return float
	 * @see #correlation(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float covariance(Vector vector1, Vector vector2) {
		float[] buff1 = vector1.accessVectorBuffer();
		float[] buff2 = vector2.accessVectorBuffer();

		float[] tmp1 = null;
		float[] tmp2 = null;
		VectorTools tools = new VectorTools();

		if (buff1.length != buff2.length) {
			int max = buff1.length >= buff2.length ? buff1.length
					: buff2.length;

			try {
				tmp1 = tools.resize(vector1, max).accessVectorBuffer();
				tmp2 = tools.resize(vector2, max).accessVectorBuffer();
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		} else {
			tmp1 = vector1.clone().accessVectorBuffer();
			tmp2 = vector2.clone().accessVectorBuffer();
		}

		float mean1 = mean(vector1);
		float mean2 = mean(vector2);

		float ipSum = 0;

		for (int i = 0; i < tmp1.length; i++) {
			ipSum = ipSum + (tmp1[i] - mean1) * (tmp2[i] - mean2);
		}

		return ipSum / (tmp1.length);

	}

	/**
	 * This method will compute the Correlation coefficient between the Vectors
	 * given by the argument 'vector1' and 'vector2'.
	 * <p>
	 * The correlation coefficient is obtain by the following formula,
	 * <p>
	 * P = G/(S1*S2),
	 * <p>
	 * where 'P' is the correlation coefficient, 'G' is the Covariance
	 * coefficient, and 'S1' and 'S2' are the standard deviation of the
	 * 'vector1' and 'vector2' respectively.
	 * <p>
	 * The Correlation coefficient is always in the range of -1 to 1.
	 * <p>
	 * The length of both the input vector should be more than 1 else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * If length of both the Vector is not same Vector with smaller length is
	 * padded with appropriate numbers of zero so as to make the length of both
	 * the Vector equal.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return float
	 * @throws IllegalArgumentException
	 * @see #covariance(Vector, Vector)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float correlation(Vector vector1, Vector vector2)
			throws IllegalArgumentException {

		if (vector1.length() < 2 || vector2.length() < 2) {
			throw new IllegalArgumentException();
		}
		float std1 = standardDeviation(vector1);
		float std2 = standardDeviation(vector2);

		return covariance(vector1, vector2) / (std1 * std2);
	}

	/**
	 * This method will compute the Covariance Matrix for the 'N' number of
	 * Vectors as given by the VectorStack 'vectors' and return the same as a
	 * Cell.
	 * <p>
	 * Here 'N' will be equal to number of Vectors in the VectorStack 'vectors'.
	 * <p>
	 * The resultant Covariance Cell will be of dimension 'N' by 'N' where each
	 * element at ith row and jth column index position will be the Covariance
	 * between the ith and jth index Vector of the VectorStack 'vectors'.
	 * <p>
	 * All the Vectors of VectorStack 'vectors' should be of same length else
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param VectorStack
	 *            vectors
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell covarianceMatrix(VectorStack vectors)
			throws IllegalArgumentException {
		int length = vectors.accessVector(0).length();

		float sqrtN = (float) Math.sqrt(length);

		Cell tmp = new Cell(vectors.size(), length);

		try {
			for (int i = 0; i < vectors.size(); i++) {
				if (vectors.accessVector(i).length() != length) {
					throw new IllegalArgumentException();
				}
				float mean = mean(vectors.accessVector(i));

				Vector nomean = VectorMath.linear(1.0f / sqrtN, -mean / sqrtN,
						vectors.accessVector(i));
				tmp.assignRow(nomean, i);
			}

			return new MatrixTools().crossProduct(tmp, new MatrixTools()
					.transpose(tmp));

		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will return the Maximum valued element of the Vector
	 * 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float maximum(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float max = -Float.MAX_VALUE;
		float ele;
		for (int i = 0; i < buffer.length; i++) {
			ele = buffer[i];
			if (ele > max)
				max = ele;
		}
		return max;
	}

	/**
	 * This method will return the Minimum valued element of the Vector
	 * 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float minimum(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float min = Float.MAX_VALUE;
		float ele;
		for (int i = 0; i < buffer.length; i++) {
			ele = buffer[i];
			if (ele < min)
				min = ele;
		}
		return min;
	}

	/**
	 * This method will return the Euclidian or L2 norm of the elements of the
	 * Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float normL2(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float sum = 0;

		for (int i = 0; i < buffer.length; i++) {
			sum = sum + buffer[i] * buffer[i];

		}

		return (float) Math.sqrt(sum);
	}

	/**
	 * This method will return the Manhattan or L1 norm of the elements of the
	 * Vector 'vector'.
	 * 
	 * @param Vector
	 *            vector
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float normL1(Vector vector) {
		float[] buffer = vector.accessVectorBuffer();

		float sum = 0;

		for (int i = 0; i < buffer.length; i++) {
			sum = sum + Math.abs(buffer[i]);
		}

		return sum;
	}

	/**
	 * This method will return the pth norm of the elements of the Vector
	 * 'vector'.
	 * <p>
	 * The argument 'p' which specify the dimension of the L^p space should be
	 * more than '0' else this method will throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param int p
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float normP(Vector vector, int p) throws IllegalArgumentException {

		if (p < 1)
			throw new IllegalArgumentException();

		float[] buffer = vector.accessVectorBuffer();

		double sum = 0;

		for (int i = 0; i < buffer.length; i++) {
			sum = sum + Math.pow(Math.abs(buffer[i]), p);
		}

		return (float) Math.pow(sum, 1.0 / p);
	}

	/**
	 * This method will remove the mean from the given Vector 'vector' and
	 * return the resultant zero mean Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector deMean(Vector vector) {
		Vector result = new Vector(vector.length());

		float mean = mean(vector);

		for (int i = 0; i < result.length(); i++) {
			result.setElement(vector.getElement(i) - mean, i);
		}

		return result;
	}

	/**
	 * This method computes the Auto Covariance function for the data sequence
	 * as represented by the {@link Vector} 'vector' and return the same as a
	 * Vector.
	 * <p>
	 * The auto covariance function measures the covariance of the data sequence
	 * with itself for different time lag. The time lag index representing '0'
	 * lag is located at the centre of the return Vector.
	 * <p>
	 * The auto covariance function is similar to auto correleation function
	 * except for the normalisation by the variance of the data sequence in case
	 * of the auto correlation function. Thus,
	 * <p>
	 * <i> auto covariance function = variance * auto correlation function.</i>
	 * 
	 * @see #autoCorrelationFunction(Vector)
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector autoCovarianceFunction(Vector vector) {
		Vector tmp = deMean(vector);

		int N = vector.length();

		Vector auto_cor = util.autoCorrelation(tmp);
		Vector auto_cov_stat = new Vector(auto_cor.length());
		float ele;

		for (int i = 0; i < auto_cov_stat.length(); i++) {
			ele = (auto_cor.getElement(i)) / N;
			auto_cov_stat.setElement(ele, i);
		}

		return auto_cov_stat;
	}

	/**
	 * This method computes the Auto Correlation function also called Auto
	 * Correlation coefficient for the data sequence as represented by the
	 * {@link Vector} 'vector' and return the same as a Vector.
	 * <p>
	 * The auto correlation function or coefficient measures the correlation of
	 * the data sequence with itself for different time lag. The auto
	 * correlation function so estimated has range of [-1 1], with '1'
	 * indicating perfect correlation and '-1' indicating perfect anti
	 * correlation for the given time lag. The time lag index representing '0'
	 * lag is located at the centre of the return Vector.
	 * <p>
	 * The auto correlation function defined here is similar to auto correlation
	 * function defined for signal processing but with no bias/dc component and
	 * appropriate normalisation.
	 * 
	 * @see #autoCovarianceFunction(Vector)
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector autoCorrelationFunction(Vector vector) {
		Vector tmp = deMean(vector);

		float variance = variance(tmp);
		int N = vector.length();
		float norm = N * variance;

		Vector auto_cor = util.autoCorrelation(tmp);
		Vector auto_cor_stat = new Vector(auto_cor.length());
		float ele;

		for (int i = 0; i < auto_cor_stat.length(); i++) {
			ele = (auto_cor.getElement(i)) / norm;
			auto_cor_stat.setElement(ele, i);
		}

		return auto_cor_stat;
	}

	/**
	 * This method computes the Cross Covariance function between the data
	 * sequences as represented by the {@link Vector}s 'vector1', 'vector2' and
	 * return the same as a Vector.
	 * <p>
	 * The cross covariance function measures the covariance between the input
	 * data sequences for different time lag. The time lag index representing
	 * '0' lag is located at the centre of the returned Vector.
	 * <p>
	 * The cross covariance function is similar to cross correleation function
	 * except for the normalization term. Thus,
	 * <p>
	 * <i> cross covariance function = normalization * cross correlation
	 * function.</i>
	 * 
	 * @see #crossCorrelationFunction(Vector)
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector crossCovarianceFunction(Vector vector1, Vector vector2) {
		Vector vec1 = deMean(vector1);
		Vector vec2 = deMean(vector2);

		int n1 = vec1.length();
		int n2 = vec2.length();
		float n = (n1 + n2) / 2.0f;

		Vector cross_cor = util.crossCorrelation(vec1, vec2);

		Vector cross_cov_stat = new Vector(cross_cor.length());
		float ele;

		for (int i = 0; i < cross_cov_stat.length(); i++) {
			ele = (cross_cor.getElement(i)) / n;
			cross_cov_stat.setElement(ele, i);
		}

		return cross_cov_stat;
	}

	/**
	 * This method computes the Cross Correlation function also called Cross
	 * Correlation coefficients between the data sequences as represented by the
	 * {@link Vector}s 'vector1', 'vector2' and return the same as a Vector.
	 * <p>
	 * The cross correlation function or coefficients measure the correlation
	 * between the input data sequences for different time lag. The cross
	 * correlation function so estimated has range of [-1 1], with '1'
	 * indicating perfect correlation and '-1' indicating perfect anti
	 * correlation for the given time lag. The time lag index representing '0'
	 * lag is located at the centre of the return Vector.
	 * <p>
	 * The cross correlation function defined here is similar to cross
	 * correlation function defined for signal processing but with no bias/dc
	 * component and appropriate normalisation.
	 * 
	 * @see #crossCovarianceFunction(Vector)
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector crossCorrelationFunction(Vector vector1, Vector vector2) {
		int n1 = vector1.length();
		int n2 = vector2.length();
		float n = (n1 + n2) / 2.0f;

		Vector vec1 = deMean(vector1);
		Vector vec2 = deMean(vector2);

		float var1 = variance(vec1);
		float var2 = variance(vec2);

		Vector cross_cor = util.crossCorrelation(vec1, vec2);
		float norm = (float) (n * Math.sqrt(var1 * var2));

		Vector cross_cor_stat = new Vector(cross_cor.length());
		float ele;

		for (int i = 0; i < cross_cor_stat.length(); i++) {
			ele = (cross_cor.getElement(i)) / norm;
			cross_cor_stat.setElement(ele, i);
		}

		return cross_cor_stat;
	}

	/**
	 * This method will return the pth percentile value for the elements of the Vector
	 * 'vector'. 
	 * <p>The argument 'p' which specify the percentile should be in the range of
	 * [0 100] else this method will throw an IllegalArgument Exception.
	 * <p>Method make use of the linear interpolation of the two nearest ranks to estimate
	 * the percentile value.
	 * @param Vector
	 *            vector
	 * @param float p
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float percentile(Vector vector, float p) throws IllegalArgumentException
	{
		if(p <0 || p>100)
			throw new IllegalArgumentException();
		else
		{
			int N = vector.length();
			float[] ele = vector.clone().accessVectorBuffer();
			Arrays.sort(ele);
			
			float n = (p*(N-1)/100.0f) + 1;
			int k = (int) Math.floor(n);
			float d = n-k;
			
			//Indexing in array is 1 less than rank 'k'.
			//Thus if rank is 1, it point to 1st element i.e 0th index in array.
			if(k==1)
				return ele[0];
			else if(k==N)
				return ele[N-1];
			else{
				return ele[k-1] + d*(ele[k]-ele[k-1]);
			}
		}
	}
	
	/**
	 * This method will compute the coefficient of Inner product between two {@link Vector}'s
	 * 'vector1' and 'vector2' and return the same.
	 * <p>
	 * The length of both the Vectors should be the same else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float innerProduct(Vector vector1, Vector vector2)throws IllegalArgumentException {
		if (vector1.length() != vector2.length())
			throw new IllegalArgumentException();

		float sum = 0;
		float[] signal = vector1.accessVectorBuffer();
		float[] basis = vector2.accessVectorBuffer();

		for (int i = 0; i < signal.length; i++)
			sum = sum + signal[i] * basis[i];

		return sum;
	}
}
