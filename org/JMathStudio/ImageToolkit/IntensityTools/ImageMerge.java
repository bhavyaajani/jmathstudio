package org.JMathStudio.ImageToolkit.IntensityTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define methods to merge discrete real images together.
 * <p>A discrete real image will be represented by a {@link Cell} object.
 * <p>Two images are merged by combining their corresponding pixel intensities.
 * <pre>Usage:
 * Cell img1 = Cell.importImageAsCell("path1");
 * Cell img2 = Cell.importImageAsCell("path2");//Import input images with same dimension as
 * Cells.
 * 
 * ImageMerge im = new ImageMerge();//Create an instance of ImageMerge.
 * 
 * Cell merge1 = im.maxEveryPixel(img1, img2);//Merge two input images by taking maximum of
 * the corresponding pixels.
 * 
 * Cell merge2 = im.combine(img1, 0.2f, img2, 0.8f);//Merge two input images by linear fusion
 * with given weights.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageMerge {
	
	/**
	 * This method will merge together images as represented by Cells 'cell1' and 'cell2' and 
	 * return the same as a Cell.
	 * <p>
	 * Merge operation here combine two distinct images by taking the weighted sum of their
	 * corresponding pixel intensities.
	 * <p>
	 * The argument 'alpha1' and 'alpha2' specify the weightage for the images as represented
	 * by the Cells 'cell1' and 'cell2' respectively.
	 * <p>
	 * The argument 'alpha1' and 'alpha2' should be in the range of [0 1] and
	 * should satisfy following condition,
	 * <p>
	 * <b>alpha1 + alpha2 = 1,</b> else this method will throw an
	 * IllegalArgument Exception.
	 * </p>
	 * <p>
	 * Also the dimensions of both the Cells should match else this method will
	 * throw a DimensionMismatch Exception.
	 * <p>
	 * For the corresponding pixel intensities 'X1' and 'X2' of the original
	 * images, corresponding pixel intensity in the resultant merged image 'Y' is
	 * obtained as follows,
	 * <p>
	 * <b>Y = alpha1*X1 + alpha2*X2.</b>
	 * </p>
	 * 
	 * @param Cell
	 *            cell1
	 * @param float alpha1
	 * @param Cell
	 *            cell2
	 * @param float alpha2
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell combine(Cell cell1, float alpha1, Cell cell2, float alpha2)
			throws IllegalArgumentException,DimensionMismatchException {
		if (alpha1 < 0 || alpha1 > 1 || alpha2 < 0 || alpha2 > 1
				|| alpha1 + alpha2 != 1) {
			throw new IllegalArgumentException();
		}

		if (cell1.getRowCount() != cell2.getRowCount()
				|| cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}

		Cell result = new Cell(cell1.getRowCount(), cell2.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				float element = alpha1 * cell1.getElement(i, j) + alpha2
						* cell2.getElement(i, j);
				result.setElement(element, i, j);
			}
		}

		return result;
	}
	
	/**
	 * This method will merge together images as represented by Cells 'cell1' and 'cell2' and 
	 * return the same as a Cell.
	 * <p>
	 * Merge operation here combine two distinct images by taking the minimum of their corresponding
	 * pixel intensities.
	 * <p>The dimensions of both the Cells should match else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>For the corresponding pixel intensities 'X1' and 'X2' of the original
	 * images, corresponding pixel intensity in the resultant merged image 'Y' is
	 * obtained as follows,
	 * <p>
	 * <b>Y = minimum (X1,X2).</b>
	 * </p>
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell minEveryPixel(Cell cell1, Cell cell2) throws DimensionMismatchException{
		
		if(!cell1.hasSameDimensions(cell2))
			throw new DimensionMismatchException();
		else
		{
			int rows = cell1.getRowCount();
			int cols = cell2.getColCount();
			
			Cell result = new Cell(rows,cols);
					
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					
					if(cell1.getElement(i, j) < cell2.getElement(i, j))
						result.setElement(cell1.getElement(i, j), i, j);
					else
						result.setElement(cell2.getElement(i, j), i, j);
				}
			}
			
			return result;
		}
	}
	
	/**
	 * This method will merge together images as represented by Cells 'cell1' and 'cell2' and 
	 * return the same as a Cell.
	 * <p>
	 * Merge operation here combine two distinct images by taking the maximum of their corresponding
	 * pixel intensities.
	 * <p>The dimensions of both the Cells should match else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>For the corresponding pixel intensities 'X1' and 'X2' of the original
	 * images, corresponding pixel intensity in the resultant merged image 'Y' is
	 * obtained as follows,
	 * <p>
	 * <b>Y = maximum (X1,X2).</b>
	 * </p>
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell maxEveryPixel(Cell cell1, Cell cell2) throws DimensionMismatchException{
		
		if(!cell1.hasSameDimensions(cell2))
			throw new DimensionMismatchException();
		else
		{
			int rows = cell1.getRowCount();
			int cols = cell2.getColCount();
			
			Cell result = new Cell(rows,cols);
					
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					
					if(cell1.getElement(i, j) > cell2.getElement(i, j))
						result.setElement(cell1.getElement(i, j), i, j);
					else
						result.setElement(cell2.getElement(i, j), i, j);
				}
			}
			
			return result;
		}
	}
	
	/**
	 * This method will merge together images as represented by Cells 'cell1' and 'cell2' and 
	 * return the same as a Cell.
	 * <p>
	 * Merge operation here combine two distinct images by taking the average of their corresponding
	 * pixel intensities.
	 * <p>The dimensions of both the Cells should match else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>For the corresponding pixel intensities 'X1' and 'X2' of the original
	 * images, corresponding pixel intensity in the resultant merged image 'Y' is
	 * obtained as follows,
	 * <p>
	 * <b>Y = (X1+X2)/2 .</b>
	 * </p>
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell averageEveryPixel(Cell cell1, Cell cell2) throws DimensionMismatchException{
		
		if(!cell1.hasSameDimensions(cell2))
			throw new DimensionMismatchException();
		else
		{
			int rows = cell1.getRowCount();
			int cols = cell2.getColCount();
			
			Cell result = new Cell(rows,cols);
			float i1,i2;
			
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					i1 = cell1.getElement(i, j);
					i2 = cell2.getElement(i, j);
					result.setElement((i1+i2)/2.0f, i, j);
				}
			}
			
			return result;
		}
	}

}
