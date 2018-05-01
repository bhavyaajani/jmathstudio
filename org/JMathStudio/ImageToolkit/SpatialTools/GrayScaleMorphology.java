package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Structure.StrElement;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;


/**
 * This class define various Gray scale morphological operations on a discrete real image.
 * <p>A discrete real image will be represented by a Cell object.
 * <p>For all the morphological operations defined here, if condition arise to consider the
 * image pixels out side the image bounds, all such outlier pixels are ignored with in the
 * operation.  
 * <p>All Gray scale morphological operations are defined with a flat structuring element.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * GrayScaleMorphology gsm = new GrayScaleMorphology();//Create an instance of GrayScaleMorphology.
 * StrElement ele = StrElement.crossStrElement(7);//Select a suitable structuring element.
 * 
 * Cell res1 = gsm.closing(img, ele);//Apply gray scale morphological closing operation on input
 * image.
 * Cell res2 = gsm.blackTopHat(img, ele);//Apply gray scale morphological black top hat transform
 * on input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class GrayScaleMorphology {
	
	/**
	 * This method apply the gray scale morphological Dilation operation on the
	 * discrete image as represented by the Cell 'cell' and return the resultant
	 * dilated image as a Cell.
	 * <p>The argument {@link StrElement} 'strEle' specify the flat structuring element
	 * to be employed for the given morphological operation.
	 *
	 * @param Cell
	 *            cell
	 * @param StrElement
	 *            strEle
	 * @return Cell
	 * @see #erasion(Cell, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dilation(Cell cell, StrElement strEle){
			
		int H = cell.getRowCount();
		int W = cell.getColCount();
		int h = strEle.getHeight();
		int w = strEle.getWidth();
		
		Cell result = new Cell(H, W);
		
		//Ensure the structuring element dimensions are odd.
		int cY = (h- 1) / 2;
		int cX = (w- 1) / 2;
		
		float ele;
		int Y,X,y,x;
		
		for (int i = 0; i < H; i++) 
		{
			y = i-cY;
			for (int j = 0; j < W; j++) 
			{
				float max = -Float.MAX_VALUE;
				x = j-cX;
				for (int k = 0; k < h; k++) 
				{
					for (int l = 0; l < w; l++) 
					{
						if(strEle.isStructure(k,l)) 
						{
							Y = k+y;
							X = l+x;
														
							if(Y >= 0 && Y < H && X >= 0 && X < W) 
							{
								ele = cell.getElement(Y, X); 
								
								if(ele>max)
								{
									max = ele;
								}
							}
						}

					}
				}

				result.setElement(max, i, j);
			}
		}

		return result;

	}

	/**
	 * This method apply the gray scale morphological Erasion operation on the
	 * discrete image as represented by the Cell 'cell' and return the resultant
	 * erased image as a Cell.
	 * <p>The argument {@link StrElement} 'strEle' specify the flat structuring element
	 * to be employed for the given morphological operation.
	 *
	 * @param Cell
	 *            cell
	 * @param StrElement
	 *            strEle
	 * @return Cell
	 * @see #dilation(Cell, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell erasion(Cell cell, StrElement strEle){
		
		int H = cell.getRowCount();
		int W = cell.getColCount();
		int h = strEle.getHeight();
		int w = strEle.getWidth();
		
		Cell result = new Cell(H, W);
		
		//Ensure the structuring element dimensions are odd.
		int cY = (h- 1) / 2;
		int cX = (w- 1) / 2;
		
		float ele;
		int X,Y,x,y;
		
		for (int i = 0; i < H; i++) 
		{
			y = i-cY;
			for (int j = 0; j < W; j++) 
			{
				float min = Float.MAX_VALUE;
				x = j-cX;
				for (int k = 0; k < h; k++) 
				{
					for (int l = 0; l < w; l++) 
					{
						if(strEle.isStructure(k,l)) 
						{
							Y = k+y;
							X = l+x;

							if (Y >= 0 && Y < H && X >= 0 && X < W) 
							{
								ele = cell.getElement(Y,X);
								if(ele<min)
								{
									min = ele;
								}
							}
						}

					}
				}

				result.setElement(min, i, j);
			}
		}

		return result;
	}

	/**
	 * This method apply the gray scale morphological Opening operation on the
	 * discrete image as represented by the Cell 'cell' and return the resultant
	 * opened image as a Cell.
	 * <p>The argument {@link StrElement} 'strEle' specify the flat structuring element
	 * to be employed for the given morphological operation.
	 * <p><i>Opening operation is the Erasion of the image followed by a Dilation.</i>
	 * 
	 * @param Cell
	 *            cell
	 * @param StrElement
	 *            strEle
	 * @return Cell
	 * @see #closing(Cell, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell opening(Cell cell, StrElement strEle){
		return dilation(erasion(cell, strEle), strEle);
	}

	/**
	 * This method apply the gray scale morphological Closing operation on the
	 * discrete image as represented by the Cell 'cell' and return the resultant
	 * closed image as a Cell.
	 * <p>The argument {@link StrElement} 'strEle' specify the flat structuring element
	 * to be employed for the given morphological operation.
	 * <p><i>Closing operation is the Dilation of the image followed by an Erasion.</i>
	 * 
	 * @param Cell
	 *            cell
	 * @param StrElement
	 *            strEle
	 * @return Cell
	 * @see #opening(Cell, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell closing(Cell cell, StrElement strEle){
		return erasion(dilation(cell, strEle), strEle);
	}

	/**
	 * This method apply the gray scale morphological White Top Hat operation on the
	 * discrete image as represented by the Cell 'cell' and return the resultant
	 * image as a Cell.
	 * <p>The argument {@link StrElement} 'strEle' specify the flat structuring element
	 * to be employed for the given morphological operation.
	 * 
	 * @param Cell
	 *            cell
	 * @param StrElement
	 *            strEle
	 * @return Cell
	 * @see #blackTopHat(Cell, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell whiteTopHat(Cell cell, StrElement strEle){
		try {
			return new MatrixTools().subtract(cell, opening(cell, strEle));
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the gray scale morphological Black Top Hat operation on the
	 * discrete image as represented by the Cell 'cell' and return the resultant
	 * image as a Cell.
	 * <p>The argument {@link StrElement} 'strEle' specify the flat structuring element
	 * to be employed for the given morphological operation.
	 * 
	 * @param Cell
	 *            cell
	 * @param StrElement
	 *            strEle
	 * @return Cell
     * @see #whiteTopHat(Cell, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell blackTopHat(Cell cell, StrElement strEle){
		try {
			return new MatrixTools().subtract(closing(cell, strEle), cell);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

}
