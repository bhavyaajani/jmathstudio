package org.JMathStudio.ImageToolkit.TransformTools.RadonToolKit;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;

/**This class define Finite Radon Transform (FRT) on a discrete real image.
 * <p>This class support both the Forward and Inverse Reconstruction
 * operation for FRT.
 * <p>A discrete real image will be represented by a Cell object.
 * <p>FRT is a pseudo Radon transform, which is define for square Image with
 * prime dimension.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input square image with prime dimension
 * as Cell.
 * 
 * FiniteRadonTransform frt = new FiniteRadonTransform();//Create an instance of FiniteRadonTransform.
 * 
 * VectorStack projections = frt.FRT(img);//Get FRT projections of the input image.
 * Cell result = frt.IFRT(projections);//Recover zero mean image from FRT projections.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class FiniteRadonTransform 
{
	/**
	 * This method computes the forward finite radon tranform (FRT) operation on a
	 * discrete real image as represented by a Cell 'cell' and return the FRT projections
	 * as a VectorStack.
	 * <p>
	 * The Cell 'cell' representing the image should be a square cell with
	 * dimension 'P' a prime number else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * This method will remove the mean of the image/Cell before computing the
	 * FRT, thus FRT will give projections for the zero mean image and not the
	 * original image.
	 * <p>
	 * This means that Inverse FRT will reconstruct this zero mean image from
	 * the FRT projections.
	 * <p>
	 * The FRT is computed by taking 'P+1' finite numbers of projection for each
	 * slope 'k' define over the interval [0,P]. Each projection vector will be
	 * of length 'P' corresponding to 'P'->[0,P-1] y-intercepts for each slope.
	 * <p>
	 * The projections of FRT will be return as a VectorStack containing P+1
	 * number of Vectors corresponding to P+1 slopes-.[0 P]. Each Vector of the
	 * return VectorStack will be of same length 'P'.
	 * 
	 * <p> <p>This transform will fail if given image/Cell has negative pixel values.
	 * @param Cell
	 *            cell
	 * @return VectorStack
	 * @throws IllegalArgumentException
	 * @see #IFRT(VectorStack)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack FRT(Cell cell) throws IllegalArgumentException {
		if (cell.getRowCount() != cell.getColCount())
			throw new IllegalArgumentException();

		if (!MathUtils.isPrime(cell.getRowCount()))
			throw new IllegalArgumentException();

		// Square image with prime length.
		Cell zeroMeanImage = new CellStatistics().deMean(cell);

		int p = zeroMeanImage.getRowCount();
		float[][] frt = new float[p + 1][p];
		float norm = (float) Math.sqrt(p);

		for (int k = 0; k < p; k++) {
			for (int x = 0; x < cell.getColCount(); x++) {
				for (int l = 0; l < p; l++) {
					int y = (k * x + l) % p;
					frt[k][l] = frt[k][l] + zeroMeanImage.getElement(y, x)
							/ norm;
				}
			}
		}

		for (int l = 0; l < p; l++) {
			for (int y = 0; y < p; y++) {
				frt[p][l] = frt[p][l] + zeroMeanImage.getElement(y, l) / norm;
			}
		}

		VectorStack stack = new VectorStack();

		for (int i = 0; i < frt.length; i++)
			stack.addVector(new Vector(frt[i]));

		return stack;

	}

	/**
	 * This method apply the inverse finite radon transform(IFRT) on the
	 * FRT projection vectors as represented by the VectorStack 'projections' and 
	 * return the reconstructed image as a Cell.
	 * <p>
	 * As FRT projection vectors are computed from a zero mean image, the
	 * reconstructed image will have a zero mean.
	 * <p>
	 * The length of all the Vectors in the VectorStack 'projections' should be
	 * same else this method will throw an IllegalArgument Exception.
	 * 
	 * @param VectorStack
	 *            projections
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see #FRT(Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell IFRT(VectorStack projections) throws IllegalArgumentException {
		for (int i = 1; i < projections.size(); i++) {
			if (projections.accessVector(i).length() != projections.accessVector(0)
					.length())
				throw new IllegalArgumentException();
		}

		int p = projections.size() - 1;

		Cell result = new Cell(p, p);
		float norm = (float) Math.sqrt(p);

		for (int k = 0; k < p; k++) {
			for (int x = 0; x < p; x++) {
				for (int l = 0; l < p; l++) {
					int y = (k * x + l) % p;
					float tmp = result.getElement(y, x)
							+ projections.accessVector(k).getElement(l) / norm;
					result.setElement(tmp, y, x);
				}
			}
		}

		for (int y = 0; y < p; y++) {
			for (int x = 0; x < p; x++) {
				float tmp = result.getElement(y, x)
						+ projections.accessVector(p).getElement(x) / norm;
				result.setElement(tmp, y, x);
			}
		}

		return result;

	}
	
}
