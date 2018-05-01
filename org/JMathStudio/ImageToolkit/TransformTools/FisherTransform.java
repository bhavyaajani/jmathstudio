package org.JMathStudio.ImageToolkit.TransformTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellMath;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics.VectorStatistics;

/**
 * This class define an operation for the computation of Fisher Faces for the
 * set of finite discrete real images.
 * <p>
 * This operation is also called<i> Linear Discriminant Analysis.</i>
 * <p>
 * The set of discrete real images will be represented by a CellStack object
 * where each Cell of the CellStack will represent a discrete real image.
 * <pre>Usage:
 * CellStack images = Cell.importMultipleImagesAsCell("path");//Import equi-dimensional 
 * input images as CellStack.
 * 
 * FisherTransform ft = new FisherTransform();//Create an instance of FisherTransform.
 * CellStack fisherfaces = ft.decompose(images);//Compute fisher faces for input images.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FisherTransform {
	
	/**
	 * This method computes a set of Fisher faces or images from the set of
	 * discrete real images as represented by the CellStack 'stack' and return
	 * the resultant Fisher faces/images as a CellStack.
	 * <p>
	 * The dimensions of all the Cells in the CellStack 'stack' should be
	 * similar else this method will throw an DimensionMismatch Exception.
	 * <p>
	 * <i>Fisher faces/images are the Eigen vectors of the ratio of within class
	 * scatter matrix and between class scatter matrix, where each image vector
	 * represent a class. Such Fisher faces/images forms an Orthogonal basis set
	 * for the given image vectors.</i>
	 * <p>
	 * The return CellStack will contain all the computed Fisher faces/images
	 * represented as a Cell. The number of Fisher faces/images computed will be
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

		VectorStack vector = new VectorStack();
		Vector meanVector = new Vector(stack.size());

		VectorStatistics stat = new VectorStatistics();
		CellTools tools = new CellTools();
		
		try {

			for (int i = 0; i < stack.size(); i++) {
				Vector tmp = tools.reArrange(stack.accessCell(i), 1, rows * cols)
						.accessRow(0);
				float mean = stat.mean(tmp);
				meanVector.setElement(mean, i);
				vector.addVector(VectorMath.linear(1, -mean, tmp));
			}

			float totalMean = stat.mean(meanVector);
			meanVector = VectorMath.linear(1.0f, -totalMean, meanVector);

			MatrixTools ops = new MatrixTools();
			Cell matrix = new Cell(vector.size(), vector.accessVector(0).length());
			for (int i = 0; i < vector.size(); i++)
				matrix.assignRow(vector.accessVector(i), i);

			Cell sW = ops.crossProduct(matrix, ops.transpose(matrix));
			sW = CellMath.linear(1.0f / rows * cols, 0, sW);

			Cell tmp = new Cell(new float[][] { meanVector.accessVectorBuffer() });

			Cell sB = ops.crossProduct(ops.transpose(tmp), tmp);
			sB = CellMath.linear(1.0f / stack.size(), 0, sB);

			tmp = ops.crossProduct(ops.inverse(sB), sW);

			tmp = ops.eigenDecomposition(tmp).accessCell(0);
			tmp = ops.crossProduct(ops.transpose(matrix), tmp);
			tmp = ops.transpose(tmp);

			Cell[] result = new Cell[tmp.getRowCount()];

			for (int i = 0; i < result.length; i++) {
				Cell image1D = new Cell(new float[][] { tmp.accessRow(i)
						.accessVectorBuffer() });

				image1D = tools.reArrange(image1D, rows, cols);
				float norm = ops.norm(image1D);
				image1D = CellMath.linear(1.0f / norm, 0, image1D);

				result[i] = image1D;
			}

			return new CellStack(result);

		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}

	}

}
