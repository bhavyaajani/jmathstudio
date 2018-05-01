package org.JMathStudio.ToolBoxes.WaveletToolBox;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class hold the DWT coefficients for a 2D DWT Decomposition of a real Image and
 * gives access to the same. 
 * <p>This class holds the Approximate and various Detail coefficients (Horizontal, Vertical
 * and Diagonal) for all the level of decomposition.
 * <p>Because 2D DWT reconstruction is sensitive to this coefficients, an object of this
 * class can only be obtained using 2D DWT decomposition operation.
 * @See DWT2D
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */

public final class DWT2DCoeff {
	
	private CellStack[] i6;
	private boolean[][] b3 = null;
	private Wavelet wavelet;
	
	protected DWT2DCoeff(CellStack[] decomposition,boolean[][] EO){
		this.b3 = EO;
		this.i6 = decomposition;
	}
	
	/**
	 * This method will return the Approximate coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' as a {@link Cell}.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param int level
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessApproximate(int level) throws IllegalArgumentException {

		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		return i6[level - 1].accessCell(0);
	}

	/**
	 * This method will return the Horizontal Detail coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' as a {@link Cell}.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param int level
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell accessHorizontal(int level) throws IllegalArgumentException {

		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		return i6[level - 1].accessCell(1);
	}
	/**
	 * This method will return the Vertical Detail coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' as a {@link Cell}.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param int level
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell accessVertical(int level) throws IllegalArgumentException {

		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		return i6[level - 1].accessCell(2);
	}
	/**
	 * This method will return the Diagonal Detail coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' as a {@link Cell}.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param int level
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell accessDiagonal(int level) throws IllegalArgumentException {

		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		return i6[level - 1].accessCell(3);
	}

	/**
	 * This method will replace the Approximate coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' with the Approximate coefficients 
	 * as given by the argument {@link Cell} 'coeff'.
	 * <p>The dimensions of the Cell 'coeff' should be the same as that of the original Cell representing the
	 * Approximate coefficients for that level else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param Cell coeff
	 * @param int level
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignApproximate(Cell coeff, int level)
			throws IllegalArgumentException, DimensionMismatchException {
		
		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		if (!f7(coeff, i6[level - 1].accessCell(0))) {
			throw new DimensionMismatchException();
		} else
			i6[level - 1].replace(coeff,0);
	}

	/**
	 * This method will replace the Horizontal Detail coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' with the Horizontal Detail coefficients 
	 * as given by the argument {@link Cell} 'coeff'.
	 * <p>The dimensions of the Cell 'coeff' should be the same as that of the original Cell representing the
	 * Horizontal Detail coefficients for that level else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param Cell coeff
	 * @param int level
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignHorizontal(Cell coeff, int level)
			throws IllegalArgumentException, DimensionMismatchException {
	
		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		if (!f7(coeff, i6[level - 1].accessCell(1))) {
			throw new DimensionMismatchException();
		} else
			i6[level - 1].replace(coeff,1);
	}
	/**
	 * This method will replace the Vertical Detail coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' with the Vertical Detail coefficients 
	 * as given by the argument {@link Cell} 'coeff'.
	 * <p>The dimensions of the Cell 'coeff' should be the same as that of the original Cell representing the
	 * Vertical Detail coefficients for that level else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param Cell coeff
	 * @param int level
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignVertical(Cell coeff, int level)
			throws IllegalArgumentException, DimensionMismatchException {
	
		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		if (!f7(coeff, i6[level - 1].accessCell(2))) {
			throw new DimensionMismatchException();
		} else
			i6[level - 1].replace(coeff,2);
	}
	/**
	 * This method will replace the Diagonal Detail coefficients of the given 2D DWT decomposition
	 * for the level as specified by the argument 'level' with the Diagonal Detail coefficients 
	 * as given by the argument {@link Cell} 'coeff'.
	 * <p>The dimensions of the Cell 'coeff' should be the same as that of the original Cell representing the
	 * Diagonal Detail coefficients for that level else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>The argument 'level' should be in the range of [1 Max Level of decomposition] else this
	 * method will throw an IllegalArgument Exception.
	 * @param Cell coeff
	 * @param int level
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @see DWT2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignDiagonal(Cell coeff, int level)
			throws IllegalArgumentException, DimensionMismatchException {
		
		if (level < 1 || level > i6.length) {
			throw new IllegalArgumentException();
		}

		if (!f7(coeff, i6[level - 1].accessCell(3))) {
			throw new DimensionMismatchException();
		} else
			i6[level - 1].replace(coeff,3);
	}

	/**
	 * This method return the 4D float array which contain all the decomposition
	 * coefficient for all the level. See the constructor of the class for more
	 * information on the nature of the return float 4D array.
	 * 
	 * @return float[][][][]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	protected CellStack[] f5() {
		return this.i6;
	}

	protected boolean[][] f3() {
		return this.b3;
	}

	private boolean f7(Cell img1, Cell img2) {
		if (img1.getRowCount() != img2.getRowCount()) {
			return false;
		} else if (img1.getColCount() != img2.getColCount()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method will associate the given Wavelet with the decomposition
	 * operation. Associated Wavelet implies that current DWT coefficients are
	 * obtained using given Wavelet.
	 * 
	 * @param Wavelet
	 *            wavelet
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	protected void assignAssociatedWavelet(Wavelet wavelet) {
		this.wavelet = wavelet;
	}

	/**
	 * This method will return the Analysing Wavelet associated with this 2D DWT decomposition.
	 * <p>Analysing Wavelet is the Wavelet used in generating current 2D DWT decomposition
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
	 * This method return the Maximum level of Decomposition associated with this 2D DWT decomposition.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 * 
	 */
	public int getDecompositionLevel() {
		return this.i6.length;
	}
}
