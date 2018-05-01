package org.JMathStudio.ImageToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.CCellMath;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.ImageToolkit.TransformTools.FourierSet.FFT2D;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;

/**
 * This class define basic Image Processing operations on a discrete real image.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * <pre>
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ImageUtilites iu = new ImageUtilites();//Create an instance of ImageUtilities.
 * 
 * Cell auto_corr = iu.autoCorrelation(img);//Estimate auto correlation of input image.
 * 
 * int h = img.getRowCount() + 20;//New height after up sampling.
 * int w = img.getColCount() + 10;//New width after up sampling.
 * Cell up_sample = iu.upSample2D(img, h, w);//Up sample input image to new dimension as
 * specified.
 * </pre>s
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageUtilites {
	
	private MatrixTools matrix = new MatrixTools();
	
	/**
	 * This method will compute the Auto correlation of the discrete real image as represented by 
	 * {@link Cell} 'cell' and return the same as a Cell.
	 * <p>
	 * The Auto correlation of the 'cell' will be return as a Cell, where origin will be 
	 * located at the centre.
	 * 
	 * @param Cell
	 * @return Cell
	 * @see #correlation(Cell, Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell autoCorrelation(Cell cell)
	{
		//N + M -1 resize to reflect linear correlation.
		int height = 2*cell.getRowCount() -1;
		int width = 2*cell.getColCount()-1;

		Cell img;
		try {
			FFT2D fft = new FFT2D();
			CellTools tools = new CellTools();
			
			//Do resize to reflect linear correlation and not
			//circular correlation.
			img = tools.resize(cell, height, width);
			CCell imgfft = fft.fft2D(img);
			CCell imgfftConj = imgfft.getConjugate();
			
			//Auto correlation = F(w)*F'(w) - F'-> conjugate.
			imgfft = CCellMath.dotProduct(imgfft, imgfftConj);

			//Need to do wrapping around to bring DC in centre.
			return tools.wrapCell(fft.ifft2D(imgfft));			

		} catch (IllegalArgumentException e1) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}
	
	//Slower version.
//	public Cell autoCorrelation(Cell cell)
//	{
//		return correlation(cell, cell);
//	}
	
	/**
	 * This method will compute Cross correlation between two discrete real images as represented by
	 * {@link Cell}s 'cell1' and 'cell2' respectively and return the same as a Cell.
	 *  
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @see #autoCorrelation(Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell correlation(Cell cell1, Cell cell2) {
		Cell flipimg = matrix.flipColumns(cell2);
		flipimg = matrix.flipRows(flipimg);

		return new Conv2DTools().linearConvFull(cell1, flipimg);

	}
	
	/**
	 * This method will up sample the discrete real image as represented by the
	 * Cell 'cell' to a new dimension as specified by the arguments, 'H' giving new
	 * Height and 'W' giving new Width, respectively and return the resultant up sampled Image a Cell.
	 * <p>
	 * The argument 'H' and 'W' respectively should be more than the height/row count and width/column
	 * count of the Cell 'cell' else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The resultant Image will be of Height and Width as given by arguments 'H' and 'W'
	 * respectively. Thus this method gives effective up sampling by factor H/h along
	 * Y axis (rows) and W/w along X axis (columns) of the original image where 'h' and 'w' are the
	 * height and width of the original image.  
	 * <p>All new pixel positions are filled up by bilinear interpolation from original image
	 * pixel positions.
	 * 
	 * @param Cell
	 *            cell
	 * @param int H
	 * @param int W
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see #downSample2D(Cell, int, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell upSample2D(Cell cell,int H,int W) throws IllegalArgumentException
	{
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		//Check if new dimensions not less than original dimensions
		//as this method is upsampling.
		if(H<=h || W<=w)
			throw new IllegalArgumentException();
		
		Cell res = new Cell(H,W);
		
		//Mapper to map new pixel location to point in orginal image for
		//interpolating value for all new pixel locations in new image.
		//N-1 not N as index ie point location start from 0 to N-1.
		double xMapper = ((w-1.0)/(W-1.0));
		double yMapper = ((h-1.0)/(H-1.0));
		
		float mapY;
		float mapX;
		

		for(int i=0;i<H;i++)
		{
			for(int j=0;j<W;j++)
			{
				//Mapped location in original image.
				mapY = (float) (i*yMapper);
				mapX = (float) (j*xMapper);
				
				//Not needed but still check if maps points lands outside
				//original image. Less than 0 check is not required.
				if(mapY >h-1)
					mapY=h-1;
				if(mapX >w-1)
					mapX=w-1;
				
				//Get surrounding integer locations.
				int lx = (int) Math.floor(mapX);
				int hx = (int) Math.ceil(mapX);
				int ly = (int) Math.floor(mapY);
				int hy = (int) Math.ceil(mapY);
				
				//Get pixel values from surrounding 4 pixel points in original
				//image.
				float _00 = cell.getElement(ly,lx);
				float _01 = cell.getElement(ly,hx);
				float _10 = cell.getElement(hy,lx);
				float _11 = cell.getElement(hy,hx);
								
				float dx = mapX - lx;
				float dy = mapY - ly;
				float less1dx = 1-dx;
				float less1dy = 1-dy;
				
				//Interpolation for getting value for new pixel in new image.
				float value = _00*less1dx*less1dy + _01*dx*less1dy + _10*less1dx*dy
				+ _11*dx*dy;
				
				res.setElement(value, i,j);
				
			}
		}
		
		return res;
	}
	
	/**
	 * This method will down sample the discrete real image as represented by the
	 * Cell 'cell' to a new dimension as specified by the arguments, 'H' giving new
	 * Height and 'W' giving new Width, respectively and return the resultant down sampled Image a Cell.
	 * <p>
	 * The argument 'H' and 'W' respectively should be less than the height/row count and width/column
	 * count of the Cell 'cell' else this method will throw an IllegalArgument Exception.
	 * Also the arguments 'H' and 'W' should be more than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * The resultant Image will be of Height and Width as given by arguments 'H' and 'W'
	 * respectively. Thus this method gives effective down sampling by factor h/H along
	 * Y axis (rows) and w/W along X axis (columns) of the original image where 'h' and 'w' are the
	 * height and width of the original image.  
	 * <p>All new pixel positions are filled up by bilinear interpolation from original image
	 * pixel positions.
	 * 
	 * @param Cell
	 *            cell
	 * @param int H
	 * @param int W
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see #upSample2D(Cell, int, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell downSample2D(Cell cell,int H,int W) throws IllegalArgumentException
	{
		int h = cell.getRowCount();
		int w = cell.getColCount();
		

		//Check if new dimensions are not more than original dimensions
		//as this method is downsampling. Also they should not be less
		//than or equal to 0.
		if(H <=0|| H>=h || W<=0 || W>=w)
			throw new IllegalArgumentException();
		
				
		Cell res = new Cell(H,W);
		
		//Mapper to map new pixel location to point in orginal image for
		//interpolating value for all new pixel locations in new image.
		//N-1 not N as index ie point location start from 0 to N-1.
		double xMapper = ((w-1.0)/(W-1.0));
		double yMapper = ((h-1.0)/(H-1.0));
		
		float mapY;
		float mapX;
		

		for(int i=0;i<H;i++)
		{
			for(int j=0;j<W;j++)
			{
				//Mapped location in original image.
				mapY = (float) (i*yMapper);
				mapX = (float) (j*xMapper);
				
				//Not needed but still check if maps points lands outside
				//original image. Less than 0 check is not required.
				if(mapY >h-1)
					mapY=h-1;
				if(mapX >w-1)
					mapX=w-1;
				
				//Get surrounding integer locations.
				int lx = (int) Math.floor(mapX);
				int hx = (int) Math.ceil(mapX);
				int ly = (int) Math.floor(mapY);
				int hy = (int) Math.ceil(mapY);
				
				//Get pixel values from surrounding 4 pixel points in original
				//image.
				float _00 = cell.getElement(ly,lx);
				float _01 = cell.getElement(ly,hx);
				float _10 = cell.getElement(hy,lx);
				float _11 = cell.getElement(hy,hx);
								
				float dx = mapX - lx;
				float dy = mapY - ly;
				float less1dx = 1-dx;
				float less1dy = 1-dy;
				
				//Interpolation for getting value for new pixel in new image.
				float value = _00*less1dx*less1dy + _01*dx*less1dy + _10*less1dx*dy
				+ _11*dx*dy;
				
				res.setElement(value, i,j);
				
			}
		}
		
		return res;
	}

	
	
		
		
	// public float[][] correlation(float[][] image1,float[][] image2)
	// {
	// float[][] result = new float[image1.length+
	// image2.length-1][image1[0].length+image2[0].length-1];
	//		
	// for(int i=0;i<result.length;i++)
	// {
	// for(int j=0;j<result[0].length;j++)
	// {
	// float tmp =0;
	// for(int k=0;k<image2.length;k++)
	// {
	// for(int l=0;l<image2[0].length;l++)
	// {
	// int X = i+k-(image2.length)+1;
	// int Y = j+l-(image2[0].length)+1;
	//						
	// if(X>=0 && X <image1.length && Y>=0 && Y<image1[0].length)
	// {
	// tmp = tmp + image2[k][l]*image1[X][Y];
	// }
	// }
	//					
	// }
	// result[i][j] = tmp;
	// }
	// }
	//		
	// return result;
	// }

}