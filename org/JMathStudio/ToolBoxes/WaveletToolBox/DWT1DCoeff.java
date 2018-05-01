package org.JMathStudio.ToolBoxes.WaveletToolBox;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class hold the DWT coefficients for a 1D DWT Decomposition of a real Signal and
 * gives access to the same. 
 * <p>This class holds the Approximate and Detail coefficients for all the level of decomposition.
 * <p>Because 1D DWT reconstruction is sensitive to this coefficients, an object of this
 * class can only be obtained using 1D DWT decomposition operation.
 * @See DWT1D
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class DWT1DCoeff {
	
	private VectorStack[] decomposition;
	private boolean[] b2;
	private Wavelet wavelet;
	
	protected DWT1DCoeff(VectorStack[] decomp,boolean[] EO){
		this.decomposition = decomp;
		this.b2 = EO;
	}
	
	/**
	 * This method will return the Approximate coefficients of the given 1D DWT decomposition
	 * for the level as specified by the argument 'level' as a Vector.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param int level
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see DWT1D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessApproximate(int level) throws IllegalArgumentException {
		if (level < 1 || level > decomposition.length) {
			throw new IllegalArgumentException();
		}

		return decomposition[level - 1].accessVector(0);

	}
	/**
	 * This method will return the Detail coefficients of the given 1D DWT decomposition
	 * for the level as specified by the argument 'level' as a Vector.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param int level
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see DWT1D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessDetail(int level) throws IllegalArgumentException {
		if (level < 1 || level > decomposition.length) {
			throw new IllegalArgumentException();
		}

		return decomposition[level - 1].accessVector(1);

	}
	/**
	 * This method will replace the Approximate coefficients of the given 1D DWT decomposition
	 * for the level as specified by the argument 'level' with the Approximate coefficients 
	 * as given by the argument Vector 'coefficient'.
	 * <p>The length of the Vector 'coefficient' should be the same as the length of original
	 * Approximate coefficients for that level else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param Vector coefficients
	 * @param int level
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @see DWT1D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignApproximate(Vector coefficient, int level)
			throws IllegalArgumentException, DimensionMismatchException {

		if (level < 1 || level > decomposition.length) {
			throw new IllegalArgumentException();
		}

		if (coefficient.length() != decomposition[level - 1].accessVector(0).length()) {
			throw new DimensionMismatchException();
		} else
			decomposition[level - 1].replace(coefficient,0);

	}

	/**
	 * This method will replace the Detail coefficients of the given 1D DWT decomposition
	 * for the level as specified by the argument 'level' with the Detail coefficients 
	 * as given by the argument Vector 'coefficient'.
	 * <p>The length of the Vector 'coefficient' should be the same as the length of original
	 * Detail coefficients for that level else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param Vector coefficients
	 * @param int level
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @see DWT1D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignDetail(Vector coefficient, int level)
			throws IllegalArgumentException, DimensionMismatchException {

		if (level < 1 || level > decomposition.length) {
			throw new IllegalArgumentException();
		}

		if (coefficient.length() != decomposition[level - 1].accessVector(1).length()) {
			throw new DimensionMismatchException();
		} else
			decomposition[level - 1].replace(coefficient,1);
	}

	protected boolean[] f0() {
		return b2;
	}

	/**
	 * This method return an array of {@link VectorStack} which contain all the decomposition
	 * coefficient for all the level. 
	 * @return {@link VectorStack}[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	protected VectorStack[] f2() {
		return decomposition;
	}

	/**
	 * This method will associate the given Wavelet with the decomposition
	 * operation. Associated Wavelet implies that current DWT coefficients ar
	 * obtained using given Wavelet.
	 * 
	 * @param Wavelet
	 *            wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	protected void assignAssociatedWavelet(Wavelet wavelet) {
		this.wavelet = wavelet;
	}

	/**
	 * This method will return the Analysing Wavelet associated with this 1D DWT decomposition.
	 * <p>Analysing Wavelet is the Wavelet used in generating current 1D DWT decomposition
	 * coefficients. Reconstruction using the analysing Wavelet only gives the correct
	 * result.

	 * @return Wavelet
	 * @see Wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Wavelet accessAssociatedWavelet() {
		return this.wavelet;
	}

	/**
	 * This method return the Maximum level of Decomposition for this 1D DWT decomposition.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 * 
	 */
	public int getDecompositionLevel() {
		return this.decomposition.length;
	}
}
