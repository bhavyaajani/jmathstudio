package org.JMathStudio.ImageToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.DST1D;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a 2D fast discrete sine transform (DST) and its inverse on
 * a discrete real image.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * DST2D tr = new DST2D();//Create an instance of DST2D.
 * 
 * Cell dst = tr.dst2D(img);//Apply 2D DST on input image and compute DST coefficients.
 * Cell res = tr.idst2D(dst);//Recover original image by applying inverse 2D DST on the
 * DST coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DST2D {
	private MatrixTools matrix = null;

	public DST2D() {
		matrix = new MatrixTools();
	};

	/**
	 * This method will apply a fast discrete sine transform (DST) on the
	 * discrete real image as represented by the Cell 'cell' and return the
	 * result as a Cell.
	 * <p>
	 * 2D DST is computed by taking 1D DST along rows and columns of the image.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dst2D(Cell cell) {
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		DST1D dst = new DST1D();

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				Vector vector = dst.dst1D(cell.accessRow(i), cell.accessRow(i)
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
				Vector vector = dst.dst1D(result.accessRow(i), result.accessRow(i)
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
	 * This method will apply an inverse fast discrete sine transform (IDST) on
	 * the Cell 'cell' and return the resultant image as a Cell.
	 * <p>
	 * 2D IDST is computed by taking 1D IDST along rows and columns of the Cell
	 * 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell idst2D(Cell cell) {
		Cell result = matrix.transpose(cell);
		DST1D idst = new DST1D();

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				result.assignRow(idst.idst1D(result.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		result = matrix.transpose(result);

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				result.assignRow(idst.idst1D(result.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		return result;
	}

}
