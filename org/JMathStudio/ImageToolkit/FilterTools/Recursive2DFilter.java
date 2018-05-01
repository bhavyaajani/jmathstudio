package org.JMathStudio.ImageToolkit.FilterTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.GeneralTools.Conv1DTools;

/**
 * This class define a 2D Recursive separable filter which is characterised by its
 * horizontal and vertical 1D convolution mask/kernel.
 * <p>A Recursive filter is essentially a 2D Separable filter with both horizontal and
 * vertical convolution applied recursively on an image.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * Vector v = SignalGenerator.random(5);//Select vertical 1D mask.
 * Vector h = SignalGenerator.random(3);//Select horizontal 1D mask.
 * 
 * Recursive2DFilter rf = new Recursive2DFilter(h,v);//Create an instance of 
 * Recursive2DFilter with selected 1D masks.
 * 
 * int iteration = 2;//Select number of iterations. 
 * Cell result = rf.filter(img, iteration);//Apply filtering on the input image recursively
 * for selected number of iterations.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Recursive2DFilter {

	private Vector i2;
	private Vector i8;

	private Conv1DTools conv;
	private MatrixTools matrix;
	
	/**
	 * This will create a 2D Recursive separable filter as characterised by its 1D
	 * horizontal and vertical convolution mask as represented by the {@link Vector}s 'h' 
	 * and 'v' respectively.
	 * <p>The rows and columns of the image are convolved recursively with the Vector 'h' 
	 * and Vector 'v' respectively.
	 * <p>The arguments 'h' and 'v' are passed as reference and no deep copy of the same
	 * is made.
	 * 
	 * @param Vector
	 *            h
	 * @param Vector
	 *            v
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Recursive2DFilter(Vector h, Vector v) {
		this.conv = new Conv1DTools();
		this.matrix = new MatrixTools();
		
		this.i2 = h;
		this.i8 = v;
	}

	/**
	 * This will filter the discrete real image as represented by the {@link Cell} 'image' 
	 * and return the resultant filtered image as a Cell.
	 * <p>The 2D recursive separable filtering operation is applied by convolving rows and columns of 
	 * the 'image' recursively with the associated horizontal and vertical convolution masks. 
	 * <p>The argument 'iteration' specify the number of recursive convolutions along each 
	 * direction. The argument 'iteration' should be more than 0 else this method will throw 
	 * an IllegalArgument Exception.
	 * <p>As no padding is employed for convolution, with large number of iterations, distortion
	 * along image boundary will occur. Appropriately pad image, if applying recursive filtering
	 * with either large convolution mask or greater numbers of iteration so as to avoid any 
	 * distortion along image boundary. 
	 * 
	 * @param Cell
	 *            image
	 * @param int 
	 * 			 iteration
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)  
	 */
	public Cell filter(Cell image,int iteration) throws IllegalArgumentException {
		if(iteration < 1)
			throw new IllegalArgumentException();
		try {
			Cell result = matrix.transpose(image);

			for (int i = 0; i < result.getRowCount(); i++) {
				for(int k=0;k<iteration;k++){
					Vector tmp = conv.linearConvSame(result.accessRow(i), i8);
					result.assignRow(tmp, i);
				}
			}

			result = matrix.transpose(result);

			for (int i = 0; i < result.getRowCount(); i++) {
				for(int k=0;k<iteration;k++){
					Vector tmp = conv.linearConvSame(result.accessRow(i), i2);
					result.assignRow(tmp, i);
				}
			}

			return result;
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This will return the horizontal convolution mask associated with the
	 * given 2D recursive separable filter as a Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessHorizontalMask() {
		return this.i2;
	}

	/**
	 * This will return the vertical convolution mask associated with the given
	 * 2D recursive separable filter as a Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessVerticalMask() {
		return this.i8;
	}
}
