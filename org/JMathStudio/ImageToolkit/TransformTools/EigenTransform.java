package org.JMathStudio.ImageToolkit.TransformTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellMath;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;

/**
 * This class define an operation for the computation of Eigen Faces for the set
 * of finite discrete real images.
 * <p>
 * This operation is also called<i> Karhunen-Lo�ve transform or Principal
 * Component Analysis</i>.
 * <p>
 * The set of discrete real images will be represented by a CellStack object where each
 * Cell of the CellStack will represent a discrete real image.
 * <pre>Usage:
 * CellStack images = Cell.importMultipleImagesAsCell("path");//Import equi-dimensional 
 * input images as CellStack.
 * 
 * EigenTransform et = new EigenTransform();//Create an instance of EigenTransform.
 * CellStack eigenfaces = et.decompose(images);//Compute eigen faces for input images.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class EigenTransform {
	
	/**
	 * This method computes a set of Eigen faces or images from the set of
	 * discrete real images as represented by the CellStack 'stack' and return
	 * the resultant Eigen faces/images as a CellStack.
	 * <p>
	 * The dimensions of all the Cells in the CellStack 'stack' should be
	 * similar else this method will throw an DimensionMismatch Exception.
	 * <p>
	 * <i>Eigen Faces/Images are the Eigen vectors of the covariance matrix of
	 * the given image Vectors. Such a set of Eigen faces/images forms an
	 * Orthogonal Basis set for the given Image Vectors.</i>
	 * <p>
	 * The return CellStack will contain all the computed Eigen faces/images
	 * represented as a Cell. The number of Eigen faces/images computed will be
	 * similar to the number of original Images.
	 * 
	 * @param CellStack
	 *            stack
	 * @return CellStack
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack decompose(CellStack stack)
			throws DimensionMismatchException {
		int rows = stack.accessCell(0).getRowCount();
		int cols = stack.accessCell(0).getColCount();

		for (int i = 0; i < stack.size(); i++) {
			if (stack.accessCell(i).getRowCount() != rows
					|| stack.accessCell(i).getColCount() != cols) {
				throw new DimensionMismatchException();
			}
		}

		MatrixTools matrix = new MatrixTools();
		
		CellStatistics stat = new CellStatistics();
		CellTools tools = new CellTools();

		Cell[] result = new Cell[stack.size()];

		for (int i = 0; i < stack.size(); i++) {
			float mean = stat.mean(stack.accessCell(i));
			Cell cell = CellMath.linear(1, -mean, stack.accessCell(i));
			result[i]= cell;
		}

		try {

			VectorStack vStack = new VectorStack();

			for (int i = 0; i < result.length; i++) {
				Cell cell = tools.reArrange(result[i], 1, rows * cols);
				vStack.addVector(cell.accessRow(0));
			}

			Cell vector = new Cell(vStack.size(), vStack.accessVector(0).length());
			for (int i = 0; i < vStack.size(); i++)
				vector.assignRow(vStack.accessVector(i), i);

			Cell buffer = null;

			buffer = matrix.crossProduct(vector, matrix.transpose(vector));
			buffer = matrix.eigenDecomposition(buffer).accessCell(0);

			buffer = matrix.crossProduct(matrix.transpose(vector), buffer);
			buffer = matrix.transpose(buffer);

			for (int i = 0; i < buffer.getRowCount(); i++) {
				float[][] tmp = new float[1][];
				tmp[0] = buffer.accessRow(i).accessVectorBuffer();
				Cell buff = new Cell(tmp);

				buff = tools.reArrange(buff, rows, cols);
				float norm = matrix.norm(buff);
				buff = CellMath.linear(1.0f / norm, 0, buff);

				result[i]= buff;
			}

		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}

		return new CellStack(result);
	}

}
