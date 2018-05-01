package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.ImageToolkit.FilterTools.SpatialFilter;
import org.JMathStudio.ImageToolkit.FilterTools.SpatialFilterMaker;
import org.JMathStudio.ImageToolkit.GeneralTools.Conv2DTools;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;

/**
 * This class provide various Edge detection operations on a discrete real image.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * 
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * EdgeDetector ed = new EdgeDetector();//Create an instance of EdgeDetector.
 * 
 * Cell edge1 = ed.laplacian(img);//Apply laplacian operator on input image to estimate
 * edge points. 
 * 
 * Cell edge2 = ed.homogeneityOperator(img);//Apply homogeneity operator on input image to
 * estimate edge points.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class EdgeDetector {
	
	/**
	 * This method define the Marr-Hildreth operator, for the detection of Edge
	 * points, on the discrete real image as represented by {@link Cell} 'cell'.
	 * <p>
	 * This operator is also known as Laplacian of Gaussian (LOG) operator.
	 * <p>
	 * The argument 'M' and 'std' respectively specify the dimension and
	 * the standard deviation of the square Gaussian kernel employed by this method.
	 * The argument 'M' should be a positive odd integer greater than 0 and 'std' should be
	 * more than zero else this method will throw an IllegalArgument Exception.
	 * <p>
	 * If the argument 'std' is less than 0.5, this operation will become
	 * equivalent to the Laplacian operation.
	 * <p>
	 * The return Cell contains the Edgeness values indicating the objective
	 * presence of an Edge at the corresponding pixel position in the original
	 * Image.
	 * 
	 * @param Cell
	 *            cell
	 * @param int M
	 * @param float std
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell marrHildreth(Cell cell, int M, float std)throws IllegalArgumentException {
		
		if(M <1 || M%2==0)
			throw new IllegalArgumentException();
		
		SpatialFilter gauss = SpatialFilterMaker.gaussianFilter(M, std);
		SpatialFilter lap = SpatialFilterMaker.laplacianFilter();
		
		Conv2DTools conv = new Conv2DTools();
		Cell LOGkernel = conv.linearConvFull(gauss.accessConvolutionKernel(), lap.accessConvolutionKernel());
		return conv.linearConvSame(cell, LOGkernel);
	}

	/**
	 * This method define the Difference of Gaussian (DOG) operator for finding the edges in the
	 * discrete real image as represented by the {@link Cell} 'cell' and return the resultant edge image
	 * as a {@link Cell}.
	 * <p>The difference of gaussian operator as name suggest estimate the edges in the image by first 
	 * filtering the original image with two separate gaussian filters and subsequently taking the 
	 * difference of this filtered images.
	 * <p>The argument 'M' specify the dimension of the square gaussian filter mask. The argument
	 * 'M' should be an odd integer greater than '0' else this metod will throw an IllegalArgument
	 * Exception.
	 * <p>The argument 'std1' and 'std2' respectively specify the standard deviation of the two
	 * gaussian filter mask for filtering the image prior to taking there difference. Both the 
	 * argument 'std1' and 'std2' should be more than '0' else this method will throw an IllegalArgument
	 * Exception.
	 * @param Cell cell
	 * @param int M
	 * @param float std1
	 * @param float std2
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell diffOfGaussian(Cell cell,int M,float std1,float std2) throws IllegalArgumentException
	{
		if(M <1 || M%2==0)
			throw new IllegalArgumentException();
		if(std1<=0 || std2<=0)
			throw new IllegalArgumentException();
		
		SpatialFilter gauss1 = SpatialFilterMaker.gaussianFilter(M, std1);
		SpatialFilter gauss2 = SpatialFilterMaker.gaussianFilter(M, std2);
		
		Cell filt1 = gauss1.filter(cell);
		Cell filt2 = gauss2.filter(cell);
		
		try {
			return new MatrixTools().subtract(filt1, filt2);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
		
	}
	/**
	 * This method define the Laplacian operator, for the detection of Edge
	 * points, on the discrete real image as represented by {@link Cell} 'cell'.
	 * <p>
	 * The return Cell contains the Edgeness values indicating the objective
	 * presence of an Edge at the corresponding pixel position in the original
	 * Image.
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell laplacian(Cell cell) {
		
		//This is laplacian operator and not laplacian filter mask.
		float[][] mask = new float[][]{{ 1, 1, 1 }, { 1, -8, 1},{ 1, 1, 1} };
		
		try{
		return new Conv2DTools().linearConvSame(cell, new Cell(mask));
		}catch(IllegalCellFormatException e)
		{
			throw new BugEncounterException();
		}		
	}

	
	/**
	 * This method define the Canny Operator, for the detection of Edge points,
	 * on a discrete real image as represented by the {@link Cell} 'cell'.
	 * <p>
	 * The argument 'threshold' specify the cut-off for the Edge Magnitude which
	 * is defined in the units of the RMS value of the Edge Magnitudes. The
	 * argument 'threshold' should be more than or equal to 0 else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * The result of the Canny Edge Operation is returned as a Cell containing
	 * 1's and 0's, with 1's indicating the presence of an Edge at the
	 * corresponding pixel position in the original Image.
	 * 
	 * @param Cell
	 *            cell
	 * @param float threshold
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell canny(Cell cell, float threshold)throws IllegalArgumentException {
		if (threshold < 0) {
			throw new IllegalArgumentException();
		}
		try {
			
			float PI = (float) Math.PI;
			float _piBy4 = PI/4.0f;
			float _piBy2 = PI/2.0f;
			float _3piBy4 =  3*_piBy4;
			
			//Smoothen the image to reduce spurious edges.
			SpatialFilter filt = SpatialFilterMaker.gaussianFilter(5, 1);
			Cell tmp = filt.filter(cell);

			Conv2DTools conv = new Conv2DTools();
			//Get magnitude and angles of edge at each pixel using the sobel operation.
			//Any operator would do, sobel is the most used.
			ImageGradient sobel = ImageGradient.sobel();
			
			//Compute both magnitude and direction simultaneously to improve performance.
			//donot use the sobel operator to get the magnitude and direction directly as
			//it will recompute convolution twice for both.
			Cell gx = conv.linearConvSame(tmp, sobel.accessHorizontalGradientMask());
			Cell gy = conv.linearConvSame(tmp, sobel.accessVerticalGradientMask());

			Cell mag = new Cell(tmp.getRowCount(),tmp.getColCount());
			Cell dir = new Cell(tmp.getRowCount(),tmp.getColCount());
			
			float constant = (float) (Math.PI/2);
			float x,y;
			float abs;
			
			for (int i = 0; i < mag.getRowCount(); i++) 
			{
				for (int j = 0; j < dir.getColCount(); j++)
				{
					x = gx.getElement(i, j);
					y = gy.getElement(i, j);
					
					if (x == 0) {
							dir.setElement(constant, i, j);
					} 
					else {
						dir.setElement((float) Math.atan(y/x), i, j);
					}
					
					abs = (float) Math.sqrt((x * x + y * y));
					mag.setElement(abs, i, j);
				}
			}
			
			//Round of the angles to the 4 different angles, 0,45,90 & 135.
			dir = f3(dir);

			//Threshold for selecting the edge points.
			//Edge point if edge magnitude more than or equal  to this threshold.
			float thres = new CellStatistics().RMS(mag) * threshold;

			Cell result = new Cell(cell.getRowCount(), cell.getColCount());

			int row = cell.getRowCount();
			int col = cell.getColCount();

			//Also, Edge point if difference in edge magnitude value of neighbouring pixels
			//based on the direction of normal i.e angle here.
			for (int i = 0; i < result.getRowCount(); i++) 
			{
				for (int j = 0; j < result.getColCount(); j++) 
				{
					if (mag.getElement(i, j) >= thres) 
					{
						if (dir.getElement(i, j) == 0) 
						{
							if (j >= 1 && j < col - 1) {
								if (mag.getElement(i, j + 1) <= mag.getElement(
										i, j)
										&& mag.getElement(i, j - 1) <= mag
												.getElement(i, j)) {
									result.setElement(1, i, j);
								}
							}
						} else if (dir.getElement(i, j) == _piBy4) {
							if (i >= 1 && i < row - 1 && j >= 1 && j < col - 1) {
								if (mag.getElement(i - 1, j + 1) <= mag
										.getElement(i, j)
										&& mag.getElement(i + 1, j - 1) <= mag
												.getElement(i, j)) {
									result.setElement(1, i, j);
								}
							}
						} else if (dir.getElement(i, j) == _piBy2) {
							if (i >= 1 && i < row - 1) {
								if (mag.getElement(i + 1, j) <= mag.getElement(
										i, j)
										&& mag.getElement(i - 1, j) <= mag
												.getElement(i, j)) {
									result.setElement(1, i, j);
								}
							}
						} else if (dir.getElement(i, j) == _3piBy4) {
							if (i >= 1 && i < row - 1 && j >= 1 && j < col - 1) {
								if (mag.getElement(i - 1, j - 1) <= mag
										.getElement(i, j)
										&& mag.getElement(i + 1, j + 1) <= mag
												.getElement(i, j)) {
									result.setElement(1, i, j);
								}
							}
						} else {
							throw new BugEncounterException();
						}
					}
				}
			}

			return result;

		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method define a Homogeneity Operator for estimating the edges in the discrete
	 * real image as represented by {@link Cell} 'cell' and return the edge image as a Cell.
	 * <p>Homogeneity operator defined here uses subtraction to find the edge. The operator define
	 * edge value at a pixel location as the maximum of the absolute difference of the pixel value
	 * with its immediate neighbouring pixels. 
	 * @param Cell cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell homogeneityOperator(Cell cell)
	{
		try{
		int height = cell.getRowCount();
		int width = cell.getColCount();
		
		Cell result = new Cell(height,width);
		
		float tmp;
		float pixel;
		float res;
		int xp,xl,yp,yl;
		
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				tmp=0;
				res=0;
				pixel = cell.getElement(y,x);
				xp = x+1;
				xl = x-1;
				yp = y+1;
				yl = y-1;
				
				boolean lessI = (yl>=0);
				boolean lessJ = (xl>=0);
				boolean moreI = (yp<height);
				boolean moreJ = (xp<width);
				
				if(lessI)
				{
					tmp = Math.abs(pixel - cell.getElement(yl,x));
					if(tmp > res)
						res=tmp;
				}
				if(lessJ)
				{
					tmp = Math.abs(pixel - cell.getElement(y,xl));
					if(tmp > res)
						res=tmp;
				}
				if(lessI && lessJ)
				{
					tmp = Math.abs(pixel - cell.getElement(yl,xl));
					if(tmp > res)
						res=tmp;
				}
				if(moreI)
				{
					tmp = Math.abs(pixel - cell.getElement(yp,x));
					if(tmp > res)
						res=tmp;
				}
				if(moreJ)
				{
					tmp = Math.abs(pixel - cell.getElement(y,xp));
					if(tmp > res)
						res=tmp;
				}
				if(moreI && moreJ)
				{
					tmp = Math.abs(pixel - cell.getElement(yp,xp));
					if(tmp > res)
						res=tmp;
				}
				if(moreI && lessJ)
				{
					tmp = Math.abs(pixel - cell.getElement(yp,xl));
					if(tmp > res)
						res=tmp;
				}
				if(lessI && moreJ)
				{
					tmp = Math.abs(pixel - cell.getElement(yl,xp));
					if(tmp > res)
						res=tmp;
				}
				
				result.setElement(res,y,x);
			}
		}
		
		return result;
		}catch(ArrayIndexOutOfBoundsException e)
		{
			throw new BugEncounterException();
		}
		
	}
	private Cell f3(Cell cell) {
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		float PI = (float) Math.PI;
		float _piBy4 = PI/4.0f;
		float _piBy2 = PI/2.0f;
		float _3piBy4 =  3*_piBy4;
		
		float _piBy6 = PI/6.0f;
		float _piBy3 = PI/3.0f;
		float _2piBy3 = 2*_piBy3;
		float _5piBy6 = 5*_piBy6;
		
		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				float angle = cell.getElement(i, j);
				if (angle < 0) {
					result.setElement(PI + angle, i, j);
				}

				if (angle >= 0 && angle < _piBy6) {
					result.setElement(0, i, j);
				} else if (angle >= _piBy6 && angle < _piBy3) {
					result.setElement(_piBy4, i, j);
				} else if (angle >= _piBy3 && angle < _2piBy3) {
					result.setElement(_piBy2, i, j);
				} else if (angle >= _2piBy3 && angle < _5piBy6) {
					result.setElement(_3piBy4, i, j);
				} else {
					result.setElement(0, i, j);
				}
			}
		}

		return result;
	}

//	/**
//	 * This method define the Scharr operator, for the detection of the Edge
//	 * points, on the Discrete Real Image as represented by the Cell 'cell'.
//	 * <p>
//	 * This method will compute an Edge Vector for each pixel position in the
//	 * Image, Magnitude and Direction of which forms objective evidence for the
//	 * presence of an Edge at that position.
//	 * <p>
//	 * The argument 'mode' specify either the Magnitude or the Direction of the
//	 * Edge vectors to be computed. See Static class identifier for the modes
//	 * supported. Any other argument value will cause this method to throw an
//	 * IllegalArgument Exception.
//	 * <p>
//	 * The return Cell will contain either the Magnitude or the Direction of the
//	 * estimated Edge Vectors located at the corresponding pixel positions in
//	 * the original Image, based on the argument 'mode' specification.
//	 * 
//	 * @param Cell
//	 *            cell
//	 * @param String
//	 *            mode
//	 * @return Cell
//	 * @throws IllegalArgumentException
//	 * @see Cell
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	public Cell scharr(Cell cell, String mode) throws IllegalArgumentException {
//
//		return edgeEngine(cell, EdgeTools.scharr, mode);
//	}
//
//	/**
//	 * This method define the Prewitt operator, for the detection of the Edge
//	 * points, on the Discrete Real Image as represented by the Cell 'cell'.
//	 * <p>
//	 * This method will compute an Edge Vector for each pixel position in the
//	 * Image, Magnitude and Direction of which forms objective evidence for the
//	 * presence of an Edge at that position.
//	 * <p>
//	 * The argument 'mode' specify either the Magnitude or the Direction of the
//	 * Edge vectors to be computed. See Static class identifier for the modes
//	 * supported. Any other argument value will cause this method to throw an
//	 * IllegalArgument Exception.
//	 * <p>
//	 * The return Cell will contain either the Magnitude or the Direction of the
//	 * estimated Edge Vectors located at the corresponding pixel positions in
//	 * the original Image, based on the argument 'mode' specification.
//	 * 
//	 * @param Cell
//	 *            cell
//	 * @param String
//	 *            mode
//	 * @return Cell
//	 * @throws IllegalArgumentException
//	 * @see Cell
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	public Cell prewitt(Cell cell, String mode) throws IllegalArgumentException {
//
//		return edgeEngine(cell, EdgeTools.prewitt, mode);	
//		}
//
//	/**
//	 * This method define the Roberts operator, for the detection of the Edge
//	 * points, on the Discrete Real Image as represented by the Cell 'cell'.
//	 * <p>
//	 * This method will compute an Edge Vector for each pixel position in the
//	 * Image, Magnitude and Direction of which forms objective evidence for the
//	 * presence of an Edge at that position.
//	 * <p>
//	 * The argument 'mode' specify either the Magnitude or the Direction of the
//	 * Edge vectors to be computed. See Static class identifier for the modes
//	 * supported. Any other argument value will cause this method to throw an
//	 * IllegalArgument Exception.
//	 * <p>
//	 * The return Cell will contain either the Magnitude or the Direction of the
//	 * estimated Edge Vectors located at the corresponding pixel positions in
//	 * the original Image, based on the argument 'mode' specification.
//	 * 
//	 * @param Cell
//	 *            cell
//	 * @param String
//	 *            mode
//	 * @return Cell
//	 * @throws IllegalArgumentException
//	 * @see Cell
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	public Cell roberts(Cell cell, String mode) throws IllegalArgumentException {
//
//		return edgeEngine(cell, EdgeTools.roberts, mode);	}
//
//	/**
//	 * This method define the Sobel operator, for the detection of the Edge
//	 * points, on the Discrete Real Image as represented by the Cell 'cell'.
//	 * <p>
//	 * This method will compute an Edge Vector for each pixel position in the
//	 * Image, Magnitude and Direction of which forms objective evidence for the
//	 * presence of an Edge at that position.
//	 * <p>
//	 * The argument 'mode' specify either the Magnitude or the Direction of the
//	 * Edge vectors to be computed. See Static class identifier for the modes
//	 * supported. Any other argument value will cause this method to throw an
//	 * IllegalArgument Exception.
//	 * <p>
//	 * The return Cell will contain either the Magnitude or the Direction of the
//	 * estimated Edge Vectors located at the corresponding pixel positions in
//	 * the original Image, based on the argument 'mode' specification.
//	 * 
//	 * @param Cell
//	 *            cell
//	 * @param String
//	 *            mode
//	 * @return Cell
//	 * @throws IllegalArgumentException
//	 * @see Cell
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	public Cell sobel(Cell cell, String mode) throws IllegalArgumentException {
//
//		return edgeEngine(cell, EdgeTools.sobel, mode);
//	}
//
//	private Cell edgeEngine(Cell cell,String operator,String mode) throws IllegalArgumentException
//	{
//		Cell horizontal = null;
//		Cell vertical = null;
//		
//		try{
//			horizontal = getHorizontalMatrix(operator);
//			vertical = getVerticalMatrix(operator);
//		}catch(IllegalArgumentException e)
//		{
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//		
//		Basic2DTools ops = new Basic2DTools();
//
//		Cell gx = ops.convolveSame(cell, horizontal);
//		Cell gy = ops.convolveSame(cell, vertical);
//
//		if (mode.equalsIgnoreCase(EdgeTools.magnitude)) 
//		{
//
//			Cell result = new Cell(gx.getRowCount(), gx.getColCount());
//
//			for (int i = 0; i < result.getRowCount(); i++) {
//				for (int j = 0; j < result.getColCount(); j++) {
//					float X = gx.getElement(i, j);
//					float Y = gy.getElement(i, j);
//					float tmp = (float) Math.sqrt((X * X + Y * Y));
//
//					result.setElement(tmp, i, j);
//				}
//			}
//
//			return result;
//		} else if (mode.equalsIgnoreCase(EdgeTools.direction)) {
//			Cell result = new Cell(gx.getRowCount(), gx.getColCount());
//
//			for (int i = 0; i < result.getRowCount(); i++) {
//				for (int j = 0; j < result.getColCount(); j++) {
//					if (gx.getElement(i, j) == 0) {
//						result.setElement((float) (Math.PI / 2), i, j);
//					} else {
//						result.setElement((float) Math.atan(gy.getElement(i, j)
//								/ gx.getElement(i, j)), i, j);
//					}
//				}
//			}
//
//			return result;
//		} else {
//			throw new IllegalArgumentException();
//		}
//
//	}
//	/**
//	 * This method return the Horizontal Edge Operator Matrix associated with
//	 * the Edge operator as specified by the argument 'type', as a Cell.
//	 * <p>
//	 * See static string identifier of this class for the types of Edge
//	 * Operators supported. If the argument 'type' do not specify any one of the
//	 * supported Edge operators this method will throw an IllegalArgument
//	 * Exception.
//	 * 
//	 * @param String
//	 *            type
//	 * @return Cell
//	 * @throws IllegalArgumentException
//	 * @see Cell
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	private Cell getHorizontalMatrix(String type)
//			throws IllegalArgumentException {
//		try {
//			if (type.equalsIgnoreCase(EdgeTools.sobel)) {
//				return new Cell(new float[][] { { 1, 0, -1 }, { 2, 0, -2 },
//						{ 1, 0, -1 } });
//			} else if (type.equalsIgnoreCase(EdgeTools.scharr)) {
//				return new Cell(new float[][] { { 3, 0, -3 }, { 10, 0, -10 },
//						{ 3, 0, -3 } });
//			} else if (type.equalsIgnoreCase(EdgeTools.prewitt)) {
//				return new Cell(new float[][] { { -1, 0, 1 }, { -1, 0, 1 },
//						{ -1, 0, 1 } });
//			} else if (type.equalsIgnoreCase(EdgeTools.roberts)) {
//				return new Cell(new float[][] { { 1, 0 }, { 0, -1 } });
//			} else {
//				throw new IllegalArgumentException();
//			}
//		} catch (IllegalCellFormatException e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//
//	}
//	
//	/**
//	 * This method return the Vertical Edge Operator Matrix associated with the
//	 * Edge operator as specified by the argument 'type', as a Cell.
//	 * <p>
//	 * See static string identifier of this class for the types of Edge
//	 * Operators supported. If the argument 'type' do not specify any one of the
//	 * supported Edge operators this method will throw an IllegalArgument
//	 * Exception.
//	 * 
//	 * @param String
//	 *            type
//	 * @return Cell
//	 * @throws IllegalArgumentException
//	 * @see Cell
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//
//	private Cell getVerticalMatrix(String type) throws IllegalArgumentException {
//		try {
//			if (type.equalsIgnoreCase(EdgeTools.sobel)) {
//				return new Cell(new float[][] { { 1, 2, 1 }, { 0, 0, 0 },
//						{ -1, -2, -1 } });
//			} else if (type.equalsIgnoreCase(EdgeTools.scharr)) {
//				return new Cell(new float[][] { { 3, 10, 3 }, { 0, 0, 0 },
//						{ -3, -10, -3 } });
//			} else if (type.equalsIgnoreCase(EdgeTools.prewitt)) {
//				return new Cell(new float[][] { { -1, -1, -1 }, { 0, 0, 0 },
//						{ 1, 1, 1 } });
//			} else if (type.equalsIgnoreCase(EdgeTools.roberts)) {
//				return new Cell(new float[][] { { 0, 1 }, { -1, 0 } });
//			} else {
//				throw new IllegalArgumentException();
//			}
//		} catch (IllegalCellFormatException e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//
//	}

}
