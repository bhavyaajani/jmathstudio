package org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics.VectorStatistics;
import org.JMathStudio.SignalToolkit.GeneralTools.Conv1DTools;
import org.JMathStudio.SignalToolkit.Utilities.WindowFactory;

/**
 * This class defines an Isotropic Gaussian filter. An Isotropic gaussian filter is a
 * spatial filter characterised by a gaussian, here a square, normalised filter
 * mask. An isotropic gaussian kernel has same standard deviation along its two principle axis.
 * <p>
 * This class implements a fast version of a 2D gaussian filter. Two dimensional
 * filtering is implemented independently as a 1D filtering along x and y direction i.e rows and
 * columns of the image.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * int N = 5;
 * float stddev = 2;
 * 
 * GaussianIsotropicFilter gif = new GaussianIsotropicFilter(N,stddev);//Create an instance of
 * GaussianIsotropicFilter with given parameters.
 * 
 * Cell result = gif.filter(img);//Apply Gaussian isotropic filtering on input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class GaussianIsotropicFilter {

	private int i1;
	private float i9;
	private Vector _1D;

	/**
	 * This will create an Isotropic Gaussian filter based on the parameter as
	 * provided. This filter is a spatial filter characterised by a square
	 * normalised gaussian filter mask.
	 * <p>
	 * The argument 'N' specify the dimension of the square normalised gaussian
	 * filter mask. The argument 'N' should be a positive odd integer greater
	 * than or equal to '3' else this method will throw an IllegalArgument
	 * Exception.
	 * <p>
	 * The argument 'stddev' specify the standard deviation of the gaussian bell
	 * in the filter mask. The argument 'stddev' should be more than zero else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The gaussian filter mask will be normalised so that the sum of all the
	 * filter mask coefficients is '1'.
	 * 
	 * @param int N
	 * @param float stddev
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public GaussianIsotropicFilter(int N, float stddev)
			throws IllegalArgumentException {
		if (N < 3 | N % 2 == 0 | stddev <= 0)
			throw new IllegalArgumentException();
		else {
			this.i1 = N;
			this.i9 = stddev;

			f0();
		}
	}

	/**
	 * This method will apply the given Isotropic Gaussian filter on the
	 * discrete image as represented by the {@link Cell} 'image' and return the
	 * resultant filtered image as a Cell.
	 * 
	 * @param Cell
	 *            image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell filter(Cell image) {
		try {
			Cell result = new MatrixTools().transpose(image);

			Conv1DTools conv = new Conv1DTools();

			for (int i = 0; i < result.getRowCount(); i++) {
				Vector tmp = conv.linearConvSame(result.accessRow(i), _1D);
				result.assignRow(tmp, i);
			}

			result = new MatrixTools().transpose(result);

			for (int i = 0; i < result.getRowCount(); i++) {
				Vector tmp = conv.linearConvSame(result.accessRow(i), _1D);
				result.assignRow(tmp, i);
			}

			return result;
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will return the dimension of the square gaussian filter mask
	 * associated with this filter.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMaskDimension() {
		return this.i1;
	}

	/**
	 * This method will return the standard deviation of the gaussian bell in
	 * the filter mask associated with this filter.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getMaskStdDev() {
		return this.i9;
	}

	/**
	 * This method will reset the dimension of the square gaussian filter mask
	 * associated with this filter to the argument 'N' and reconfigure the given
	 * filter as per the new specification.
	 * <p>
	 * The argument 'N' specifying the new dimension of the filter mask should
	 * be a positive odd integer greater than or equal to '3' else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int N
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetMaskDimension(int N) throws IllegalArgumentException {
		if (N < 3 | N % 2 == 0)
			throw new IllegalArgumentException();
		else {
			this.i1 = N;
			f0();
		}
	}

	/**
	 * This method will reset the standard deviation of the gaussian bell in the
	 * filter mask associated with this filter to the argument 'stddev' and
	 * reconfigure the given filter as per the new specification.
	 * <p>
	 * The argument 'stddev' specifying the new standard deviation of the
	 * gaussian bell should be greater than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param float stddev
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetMaskStdDev(float stddev) throws IllegalArgumentException {
		if (stddev <= 0)
			throw new IllegalArgumentException();
		else {
			this.i9 = stddev;
			f0();
		}
	}

	private void f0() {
		try {
			// Divide by 2.0 is important as it keeps gaussian bell at the
			// centre of the
			// kernel, will be required when we want to upgrade the kernel
			// length to
			// even size also.
			this._1D = WindowFactory.gaussian(i1, i9, (i1 - 1) / 2.0f);

			float sum = new VectorStatistics().sum(_1D);

			if (sum == 0)
				throw new BugEncounterException();

			for (int i = 0; i < _1D.length(); i++) {
				_1D.setElement(_1D.getElement(i) / sum, i);
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
}
