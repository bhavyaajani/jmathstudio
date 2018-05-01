package org.JMathStudio.ImageToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.DCT1D;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a 2D fast discrete cosine transform (DCT) and its inverse
 * on a discrete real image.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * DCT2D tr = new DCT2D();//Create an instance of DCT2D.
 * 
 * Cell dct = tr.dct2D(img);//Apply 2D DCT on input image and compute DCT coefficients.
 * Cell res = tr.idct2D(dct);//Recover original image by applying inverse 2D DCT on the
 * DCT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DCT2D {
	private MatrixTools matrix = null;

	public DCT2D() {
		matrix = new MatrixTools();
	};

	/**
	 * This method will apply a fast discrete cosine transform (DCT) on the
	 * discrete real image as represented by the Cell 'cell' and return the
	 * result as a Cell.
	 * <p>
	 * 2D DCT is computed by taking 1D DCT along rows and columns of the image.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dct2D(Cell cell) {
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		DCT1D dct = new DCT1D();

		for (int i = 0; i < cell.getRowCount(); i++) {
			try {
				Vector vector = dct.dct1D(cell.accessRow(i), cell.accessRow(i)
						.length());
				result.assignRow(vector, i);
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		result = matrix.transpose(result);

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				Vector vector = dct.dct1D(result.accessRow(i), result.accessRow(i)
						.length());
				result.assignRow(vector, i);
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		return matrix.transpose(result);
	}

	/**
	 * This method will apply an inverse fast discrete cosine transform (IDCT)
	 * on the Cell 'cell' and return the resultant image as a Cell.
	 * <p>
	 * 2D IDCT is computed by taking 1D IDCT along rows and columns of the Cell
	 * 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell idct2D(Cell cell) {
		Cell result = matrix.transpose(cell);

		DCT1D idct = new DCT1D();

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				result.assignRow(idct.idct1D(result.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		result = matrix.transpose(result);

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				result.assignRow(idct.idct1D(result.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		return result;
	}

	/**
	 * This method will generate a square DCT Transform matrix and return the
	 * same as a Cell.
	 * <p>
	 * The argument 'N' specify the dimension of the square DCT Transform matrix
	 * and should be more than zero, else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dctTransformMatrix(int N)
			throws IllegalArgumentException {
		if (N < 1)
			throw new IllegalArgumentException();

		Cell result = new Cell(N, N);
		float k = (float) Math.sqrt(2.0f / N);
		float l = 2.0f * N;

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {

				if (i == 0) {
					result.setElement((float) Math.sqrt(1.0f / N), i, j);
				} else {
					float tmp = ((2.0f * j + 1) * i) / (l);
					float value = (float) Math.cos(Math.PI * tmp);

					value = (float) (value * k);

					result.setElement(value, i, j);
				}
			}
		}

		return result;
	}

}
