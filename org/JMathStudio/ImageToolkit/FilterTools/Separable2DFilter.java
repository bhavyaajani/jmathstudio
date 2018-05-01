package org.JMathStudio.ImageToolkit.FilterTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.GeneralTools.Conv1DTools;

/**
 * This class define a 2D spatial separable filter which is characterised by its
 * horizontal and vertical 1D convolution mask.
 * <p>
 * Such a filter are faster to implement as compare to classical 2D spatial convolution
 * based filters.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * Vector v = SignalGenerator.random(5);//Select appropriate 1D vertical mask.
 * Vector h = SignalGenerator.random(7);//Select appropriate 1D horizontal mask.
 * 
 * Separable2DFilter filter = new Separable2DFilter(h,v);//Create an instance of 
 * Separable2DFilter with selected 1D masks.
 * 
 * Cell result = filter.filter(img);//Apply separable 2D filtering on the input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Separable2DFilter {

	private Vector i0;
	private Vector i3;

	private Conv1DTools conv;
	private MatrixTools matrix;
	/**
	 * This will create a 2D spatial separable filter as characterised by its 1D
	 * horizontal and vertical convolution mask as represented by the
	 * {@link Vector}s 'h' and 'v' respectively.
	 * <p>
	 * Vector 'h' is taken as a row vector where as Vector 'v' is taken as a
	 * column vector respectively.
	 * <p>
	 * Both 'h' and 'v' should be such that there outer product gives the 2D
	 * convolution mask for the desired 2D spatial filter.
	 * <p>
	 * <i>i.e. (v'*h) = C, where 'C' represent the 2D convolution mask of the
	 * desired 2D spatial filter.</i>.
	 * <p>The arguments 'h' and 'v' are passed as reference and no deep copy of the same
	 * is made.
	 * 
	 * @param Vector
	 *            h
	 * @param Vector
	 *            v
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Separable2DFilter(Vector h, Vector v) {
		this.conv = new Conv1DTools();
		this.matrix = new MatrixTools();
		
		this.i0 = h;
		this.i3 = v;
	}

	/**
	 * This will filter the discrete real image as represented by the
	 * {@link Cell} 'image' and return the resultant filtered image as a Cell.
	 * <p>
	 * The 2D filtering operation for a separable 2D filter is carried out by
	 * convolving first the columns of the 'image' with the vertical convolution
	 * mask followed by convolving the rows of the 'image' with the horizontal
	 * convolution mask.
	 * 
	 * @param Cell
	 *            image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell filter(Cell image) {
		try {
			Cell result = matrix.transpose(image);

			for (int i = 0; i < result.getRowCount(); i++) {
				Vector tmp = conv.linearConvSame(result.accessRow(i), i3);
				result.assignRow(tmp, i);
			}

			result = matrix.transpose(result);

			for (int i = 0; i < result.getRowCount(); i++) {
				Vector tmp = conv.linearConvSame(result.accessRow(i), i0);
				result.assignRow(tmp, i);
			}

			return result;
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This will return the horizontal convolution mask associated with the
	 * given 2D separable filter as a Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessHorizontalMask() {
		return this.i0;
	}

	/**
	 * This will return the vertical convolution mask associated with the given
	 * 2D separable filter as a Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessVerticalMask() {
		return this.i3;
	}

	/**
	 * This will create an equivalent 2D Separable spatial filter for the 2D
	 * convolution mask as represented by the Cell 'mask' and return the same as
	 * a {@link Separable2DFilter} object.
	 * <p>
	 * The 2D Separable filter can only be created if the 2D convolution mask is
	 * separable in to its horizontal and vertical 1D convolution mask. This
	 * condition is satisfied if the rank of the 'mask' is '1' and the row count
	 * of the 'mask' is greater than or equal to its column count.
	 * <p>
	 * If the 2D convolution mask 'mask' is separable than its horizontal and
	 * vertical 1D convolution mask will be derived based on the SVD
	 * decomposition and a Separable 2D filter will be return as a
	 * {@link Separable2DFilter} object.
	 * <p>
	 * On other hand if the 2D convolution mask 'mask' is not separable, a null
	 * will be return.
	 * <p>
	 * Also if the row count of the 'mask' is less than its column count an
	 * IllegalArgument Exception will be thrown by this method.
	 * 
	 * @param Cell
	 *            mask
	 * @return {@link Separable2DFilter}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Separable2DFilter createSeparable2DFilter(Cell mask)
			throws IllegalArgumentException {
		// Condition to be satisfied for computing SUV decomposition.
		if (mask.getRowCount() < mask.getColCount())
			throw new IllegalArgumentException();
		else {
			try {

				MatrixTools matrix = new MatrixTools();
				float rank = matrix.rank(mask);

				// If rank is not '1', given convolution 'mask' is not separable
				// return a null.
				if (rank != 1)
					return null;
				else {
					// Computes SUV decomposition.
					CellStack svd = matrix.SVD(mask);

					// Get first column vector of both U and V matrix.
					Vector U = svd.accessCell(1).getColumn(0);
					Vector V = svd.accessCell(2).getColumn(0);

					// Take root of the only non zero 1st diagonal element of S
					// matrix.
					// This is because as rank is '1', only 1 non zero element
					// in diagonal of S matrix.

					float rootOfS = (float) Math.sqrt(svd.accessCell(0)
							.getElement(0, 0));

					Vector v = new Vector(U.length());
					Vector h = new Vector(V.length());

					// Compute vertical mask from U vector.
					for (int i = 0; i < v.length(); i++) {
						v.setElement(U.getElement(i) * rootOfS, i);
					}

					// Compute horizontal mask from V vector.
					for (int i = 0; i < h.length(); i++) {
						h.setElement(V.getElement(i) * rootOfS, i);
					}

					return new Separable2DFilter(h, v);
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			}

		}

	}
}
