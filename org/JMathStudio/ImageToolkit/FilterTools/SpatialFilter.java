package org.JMathStudio.ImageToolkit.FilterTools;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellMath;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.ImageToolkit.GeneralTools.Conv2DTools;
import org.JMathStudio.ImageToolkit.TransformTools.FourierSet.FFT2D;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;

/**
 * This class define a Spatial Filter which is characterised by its Filter Mask.
 * <p>Filtering is done by linear convolution of the discrete real image with the
 * flipped filter mask which becomes the convolution kernel.
 * <p>Both filter mask and discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * Cell mask = KernelFactory.gaussianKernel(7, 7, 2);//Generate appropriate 2D filter mask.
 * SpatialFilter sf = new SpatialFilter(mask);//Create an instance of SpatialFilter with
 * selected 2D filter mask.
 * 
 * Cell result = sf.filter(img);//Apply spatial filtering on input image. 
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class SpatialFilter {
	
	private Cell f2;
	private Cell f5;
	
	private Conv2DTools conv;
	/**
	 * This will create a Spatial Filter with filter mask as specified by the argument
	 * {@link Cell} 'mask'.
	 * <p>The filter mask here should represent the required filter kernel and should not
	 * be a flipped version which is the convolution kernel for the given filter.
	 * <p>The argument 'mask' is passed by reference and no deep copy of the same is made.
	 * 
	 * @param Cell mask
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public SpatialFilter(Cell mask)
	{
		this.conv = new Conv2DTools();
		this.f2 = mask;
		
		MatrixTools tool = new MatrixTools();
		//Flip the Filter Mask as convolution mask is flipped of filter mask.
		this.f5 = tool.flipRows(tool.flipColumns(mask));
	}
	
	/**
	 * This method will filter the discrete real image as represented by the {@link Cell} 'cell'
	 * and return the filtered image with similar dimensions as a {@link Cell}.
	 * 
	 * <p>The spatial filtering is carried out by a linear 2D convolution on the discrete real image
	 * with the flipped filter mask or convolution kernel associated with this spatial filter.
	 *  
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell filter(Cell cell) 
	{
		return conv.linearConvSame(cell, f5);
	}
	
	/**
	 * This will return the filter mask associated with this spatial filter as {@link Cell}.
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessFilterMask()
	{
		return f2;
	}
	
	/**
	 * This will return the convolution kernel associated with this spatial filter as {@link Cell}.
	 * <p>A convolution kernel is the flip version of filter mask.
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessConvolutionKernel()
	{
		return f5;
	}
	
	
	
	/**
	 * This method will compute the Magnitude response of this spatial filter as characterised by its
	 * filter mask and return the same as a {@link Cell}.
	 * <p>
	 * The argument 'M' and 'N' respectively specify the M X N points magnitude response to be computed.
	 * Thus 'M' and 'N' specify the number of discrete normalised vertical and horizontal
	 * frequencies in the range of [-PI PI] for which magnitude response is to be computed. The return Cell
	 * will have the similar dimensions.
	 * <p>
	 * The magnitude response is derived by taking magnitude of the 2D FFT of the appropriately
	 * zero padded filter mask with M X N dimensions.
	 * <p>
	 * The argument 'M' and 'N' should not be less than the filter mask height and width respectively
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The computed magnitude response will be symmetric centred, i.e. DC component will be in the centre 
	 * of the return {@link Cell} with lateral symmetry. The row and column of the return Cell will
	 * specify the equi-spaced vertical and the horizontal discrete frequencies in the range of [-PI PI]. 
	 * @param int M
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see {@link FFT2D}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getMagnitudeResponse(int M, int N)
	throws IllegalArgumentException {
		if (M < f2.getRowCount() || N < f2.getColCount()) 
			throw new IllegalArgumentException();
		

		Cell tmp = new CellTools().resize(f2, M, N);
		CCell fft = FFT2D.centre(new FFT2D().fft2D(tmp));

		Cell magnitude = fft.getMagnitude();
		magnitude = CellMath.roundOff(magnitude, 4);
		return magnitude;
	}

	/**
	 * This method will compute the Phase response of this spatial filter as characterised by its
	 * filter mask and return the same as a {@link Cell}.
	 * <p>
	 * The argument 'M' and 'N' respectively specify the M X N points phase response to be computed.
	 * Thus 'M' and 'N' specify the number of discrete normalised vertical and horizontal
	 * frequencies in the range of [-PI PI] for which phase response is to be computed. The return Cell
	 * will have the similar dimensions. The computed phases will be in radians.
	 * <p>
	 * The phase response is derived by taking phase of the 2D FFT of the appropriately
	 * zero padded filter mask with M X N dimensions.
	 * <p>
	 * The argument 'M' and 'N' should not be less than the filter mask height and width respectively
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The computed phase response will be symmetric centred, i.e. DC component will be in the centre 
	 * of the return {@link Cell} with lateral symmetry. The row and column of the return Cell will
	 * specify the equi-spaced vertical and the horizontal discrete frequencies in the range of [-PI PI]. 
	 * @param int M
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see {@link FFT2D}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getPhaseResponse(int M, int N)
	throws IllegalArgumentException {
		if (M < f2.getRowCount() || N < f2.getColCount())
			throw new IllegalArgumentException();
		
		Cell tmp = new CellTools().resize(f2, M, N);
		CCell fft = FFT2D.centre(new FFT2D().fft2D(tmp));

		Cell phase = fft.getAngle();
		phase = CellMath.roundOff(phase, 4);
		return phase;

	}



}
