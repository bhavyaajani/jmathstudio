package org.JMathStudio.ImageToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.DHT1D;

/**
 * This class define a 2D fast discrete hartley transform (DHT) and its inverse
 * on a discrete real image.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * DHT2D tr = new DHT2D();//Create an instance of DHT2D.
 * 
 * Cell dht = tr.dht2D(img);//Apply 2D DHT on input image and compute DHT coefficients.
 * Cell res = tr.idht2D(dht);//Recover original image by applying inverse 2D DHT on the
 * DHT coefficients.
 * </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DHT2D 
{
	private MatrixTools matrix = null;
	
	public DHT2D()
	{
		matrix = new MatrixTools();
	};
	
	/**
	 * This method will apply a fast discrete Hartley Transform (DHT)
	 * on the discrete real image as represented by the Cell 'cell' and
	 * return the result as a Cell.
	 * <p>2D DHT is computed by taking 1D DHT along rows and columns of
	 * the image.
	 *  
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dht2D(Cell cell)
	{
		Cell result = new Cell(cell.getRowCount(),cell.getColCount());
		
		DHT1D dht = new DHT1D();
		
		for(int i=0;i<result.getRowCount();i++)
		{
			try {
				result.assignRow(dht.dht1D(cell.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}
		
		result = matrix.transpose(result);
		
		for(int i=0;i<result.getRowCount();i++)
		{
			try {
				result.assignRow(dht.dht1D(result.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}
		
		return matrix.transpose(result);
	}
	
	/**
	 * This method will apply an inverse fast discrete Hartley transform (IDHT)
	 * on the Cell 'cell' and return the resultant image as a Cell.
	 * <p>2D IDHT is computed by taking 1D IDHT along rows and columns of
	 * the Cell 'cell'.
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell idht2D(Cell cell) 
	{
		Cell result = matrix.transpose(cell);
		DHT1D idht = new DHT1D();
		
		for(int i=0;i<result.getRowCount();i++)
		{
			try {
				result.assignRow(idht.idht1D(result.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}
	
		
		result = matrix.transpose(result);
		
		for(int i=0;i<result.getRowCount();i++)
		{
			try {
				result.assignRow(idht.idht1D(result.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}
		
		return result;
	}

}
