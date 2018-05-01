package org.JMathStudio.DataStructure.Cell;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.DivideByZeroException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;

/**
 * This class define basic mathematical operations on a {@link Cell}.
 * 
 * <pre>Usage:
 * Let 'a' & 'b' be Cells with similar dimension.
 * 
 * Cell abs = CellMath.absolute(a);//Compute absolute value for each element of input
 * Cell.
 * 
 * Cell sqrt = CellMath.sqrt(a);//Compute square root of each element of input Cell.
 * 
 * Cell sub = CellMath.subtract(a, b);//Subtract eqi-dimensional input Cells.
 * 
 * 
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class CellMath {
	
	//Ensure no instances are made for utility classes.
	private CellMath(){}
	
	/**
	 * This method will apply a linear operation on the elements of {@link Cell}
	 * 'cell' using the scale argument 'scale' and shift argument 'shift' and
	 * return the resultant Cell.
	 * <p>
	 * The linear operation is equivalent to multiplying the element by the
	 * argument 'scale' and adding the argument 'shift' to it.
	 * <p><i>y = scale*x + shift</i>
	 * <p>The return Cell contain the linear operated values for the
	 * corresponding elements of the Cell 'cell'.
	 * 
	 * @param Cell cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell linear(float scale, float shift, Cell cell) {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.i4[i][j] = scale * cell.i4[i][j] + shift;
			}
		}

		return result;
	}

	/**
	 * This method will apply the base 10 Logarithm on each element of the {@link Cell} 
	 * 'cell' and return the result as a Cell.
	 * <p>
	 * The return Cell will contain Logarithm values for the corresponding elements of the
	 * Cell 'cell'.
	 * <p>
	 * If any of the element in the Cell 'cell' is negative or zero this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see Math#log10(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell log10(Cell cell)
			throws IllegalArgumentException {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (cell.i4[i][j] <= 0) {
					throw new IllegalArgumentException();
				}
				result.i4[i][j] = (float) Math.log10(cell.i4[i][j]);
			}
		}

		return result;
	}

	/**
	 * This method will compute the Natural Logarithm for each element of the {@link Cell}
	 * 'cell' and return the result as a Cell.
	 * <p>
	 * The return Cell will contain Natural Logarithm for the corresponding element of the 
	 * Cell 'cell'.
	 * <p>
	 * If any of the element in the Cell 'cell' is negative or zero this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see Math#log(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell logE(Cell cell)
			throws IllegalArgumentException {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (cell.i4[i][j] <= 0) {
					throw new IllegalArgumentException();
				}
				result.i4[i][j] = (float) Math.log(cell.i4[i][j]);
			}
		}

		return result;
	}

	/**
	 * This method will raise the elements of the {@link Cell} 'cell' to the power
	 * of, as given by the argument 'power', and return the result as a Cell.
	 * <p><i> y = x^a;</i>
	 * <p>The return Cell will contain value of corresponding elements of the
	 * Cell 'cell' raised to the given power.
	 * <p>
	 * If any of the element of 'cell' is negative and argument 'power' is
	 * such that a root is to be computed which is not computable this method will 
	 * throw an Exception. This scenario includes computing square root of a negative
	 * number.
	 * 
	 * @param Cell
	 *            cell
	 * @param float power
	 * @return Cell
	 * @see Math#pow(double, double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell power(Cell cell, float power) {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.i4[i][j] = (float) Math.pow(cell.i4[i][j], power);
			}
		}

		return result;
	}

	/**
	 * This method will compute the Square Root of each element of the {@link Cell} 'cell' 
	 * and return the result as a Cell.
	 * <p>
	 * The return Cell will contain Square Root value of the corresponding elements of the 
	 * Cell 'cell'.
	 * <p>
	 * If any of the element in the Cell 'cell' is negative this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see Math#sqrt(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell sqrt(Cell cell)
			throws IllegalArgumentException {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (cell.i4[i][j] < 0) {
					throw new IllegalArgumentException();
				}
				result.i4[i][j] = (float) Math.sqrt(cell.i4[i][j]);
			}
		}

		return result;
	}

	/**
	 * This method will Round Off the values of each element of the {@link Cell} 'cell'
	 * to a specified precision as given by the argument 'precision' and return the result 
	 * as a Cell.
	 * <p>
	 * The return Cell will contain round off value with given decimal precision for the 
	 * corresponding elements of the Cell 'cell'.
	 * <p>The 'precision' should not be negative else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param Cell cell
	 * @param int precision
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell roundOff(Cell cell, int precision)throws IllegalArgumentException {
		if (precision < 0) {
			throw new IllegalArgumentException();
		}

		int scale = (int) Math.pow(10, precision);

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				float tmp = Math.round(scale * cell.i4[i][j]);
				result.i4[i][j] =(tmp / scale);
			}
		}

		return result;

	}

	/**
	 * This method will compute the Absolute value for each element of the {@link Cell} 
	 * 'cell' and return the result as a Cell.
	 * <p>
	 * The return Cell will contain absolute values for the corresponding elements of the
	 * Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @see Math#abs(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell absolute(Cell cell) {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.i4[i][j] = Math.abs(cell.i4[i][j]);
			}
		}
		return result;
	}

	/**
	 * This method will apply Exponential function on each element of the {@link Cell} 'cell' 
	 * and return the result as a Cell.
	 * <p>
	 * The return Cell will contain Exponential value for the corresponding elements of the 
	 * Cell 'cell'.
	 * <p><i>y = e^x</i>
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @see Math#exp(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell exponential(Cell cell) {
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
				
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				res.i4[i][j] = (float) Math.exp(cell.i4[i][j]);
			}
		}
		return res;
	}

	/**
	 * This method will compute the Cosine of each element of the {@link Cell} 'cell' 
	 * and return the result as a Cell.
	 * <p>
	 * The Cosine function takes angle in radian as input.
	 * <p><i>y = cos(x)</i>
	 * <p>The return Cell will contain Cosine value for the corresponding elements of the 
	 * Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @see Math#cos(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell cos(Cell cell) {
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
				
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				res.i4[i][j] = (float) Math.cos(cell.i4[i][j]);
			}
		}
		return res;
	}

	/**
	 * This method will compute the Sine of each element of the {@link Cell} 'cell' and
	 * return the result as a Cell.
	 * <p>
	 * The Sine function takes angle in radian as input.
	 * <p><i>y = sin(x)</i>
	 * <p>The return Cell will contain Sine value for the corresponding elements of the 
	 * Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @see Math#sin(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell sin(Cell cell) {
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
				
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				res.i4[i][j] = (float) Math.sin(cell.i4[i][j]);
			}
		}
		return res;
	}

	/**
	 * This method will compute the Tangent of each element of the {@link Cell} 'cell' 
	 * and return the result as a Cell.
	 * <p>
	 * The Tangent function takes angle in radian as input.
	 * <p><i>y = tan(x)</i>
	 * <p>The return Cell will contain Tangent value for the corresponding elements of the 
	 * Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @see Math#tan(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell tan(Cell cell) {
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
				
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				res.i4[i][j] = (float) Math.tan(cell.i4[i][j]);
			}
		}
		return res;
	}

	/**
	 * This method will compute the Inverse Sine for each element of the {@link Cell} 
	 * 'cell' and return the result as a Cell.
	 * <p>
	 * The Inverse Sine is defined for the interval of [-1 1].
	 * <p>The return Cell will contain Inverse Sine value in radian for the
	 * corresponding elements of the Cell 'cell'.
	 * <p>
	 * If any element of the Cell 'cell' has absolute value more than 1,
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see Math#asin(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell asin(Cell cell) throws IllegalArgumentException {
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
		float ele;
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				ele = cell.i4[i][j];
				if (Math.abs(ele) > 1)
					throw new IllegalArgumentException();
				
				res.i4[i][j] = (float) Math.asin(ele);				
			}
		}
		return res;
	}

	/**
	 * This method will compute the Inverse Cosine for each element of the {@link Cell} 
	 * 'cell' and return the result as a Cell.
	 * <p>
	 * The Inverse Cosine is defined for the interval of [-1 1].
	 * <p>
	 * The return Cell will contain Inverse Cosine value in radian for the corresponding 
	 * elements of the Cell 'cell'.
	 * <p>
	 * If any element of the Cell 'cell' has absolute value more than 1,
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see Math#acos(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell acos(Cell cell) throws IllegalArgumentException {
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
		float ele;
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				ele = cell.i4[i][j];
				if (Math.abs(ele) > 1)
					throw new IllegalArgumentException();
				
				res.i4[i][j] = (float) Math.acos(ele);
			}
		}
		return res;
	}

	/**
	 * This method will compute the Inverse Tangent for each element of the {@link Cell} 
	 * 'cell' and return the result as a Cell.
	 * <p>
	 * The return Cell will contain Inverse Tangent value in radian for the
	 * corresponding elements of the Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @see Math#atan(double)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell atan(Cell cell) {
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
				
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				res.i4[i][j] = (float) Math.atan(cell.i4[i][j]);
			}
		}
		return res;
	}
	
	/**
	 * This method will compute the Complex Sinusoid for each element of the {@link Cell} 
	 * 'cell' and return the resultant Complex Cell as {@link CCell}.
	 * <p>
	 * The Complex Sinusoid takes input in radian angle.
	 * <p><i> y = cos(x) + i sin(x)</i>
	 * <p>The resultant CCell contain the Complex sinusoid value for the corresponding element
	 * (in radian angle) of the Cell 'cell'.
	 * 
	 * @param Cell cell
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell complexSinusoid(Cell cell) {

		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell real = new Cell(h,w);
		Cell img = new Cell(h,w);
		
		float ele;
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				ele = cell.i4[i][j];
				
				real.i4[i][j] = (float)Math.cos(ele);
				img.i4[i][j] = (float)Math.sin(ele);
			}
		}
		try{
			return new CCell(real,img);
		}catch(DimensionMismatchException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will convert value of each element of the {@link Cell} 'cell' 
	 * to its decibel value and return the resultant Cell containing the corresponding
	 * decibel values.
	 * <p>
	 * If any element of the Cell 'cell' has value less than or equal to 
	 * '0', this method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell cell
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell decibel(Cell cell) throws IllegalArgumentException
	{
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		Cell res = new Cell(h,w);
		float ele;
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				ele = cell.i4[i][j];
				if(ele<=0)
					throw new IllegalArgumentException();
				
				res.i4[i][j] = (float)MathUtils.magToDb(ele);
			}
		}
		return res;		
	}
	
	/**
	 * This method will perform the element by element multiplication between the elements of two
	 * {@link Cell}'s 'cell1' and 'cell2' and return the result as a Cell.
	 * <p>
	 * This method will multiply each element of the 'cell1' by the corresponding element of 
	 * the 'cell2'. 
	 * <p>
	 * The dimension of both the Cell's should be the same else this method will throw a 
	 * DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell dotProduct(Cell cell1, Cell cell2)
			throws DimensionMismatchException {
		if(!cell1.hasSameDimensions(cell2))
			throw new DimensionMismatchException();
		
		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		Cell result = new Cell(rc, cc);
		float ele1,ele2;
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				ele2 = cell2.i4[i][j];
				ele1 = cell1.i4[i][j];
				
				result.i4[i][j] = (ele1*ele2);
			}
		}

		return result;
	}
	
	/**
	 * This method will perform the element by element division between the elements of two
	 * {@link Cell}'s 'cell1' and 'cell2' and return the result as a Cell.
	 * <p>
	 * This method will divide each element of the 'cell1' by the corresponding element of 
	 * the 'cell2'. If any of the element of 'cell2' is found to be zero, this method will 
	 * throw an DivideByZero Exception.
	 * <p>
	 * The dimension of both the Cell's should be the same else this method will throw a 
	 * DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell dotDivision(Cell cell1, Cell cell2)
			throws DimensionMismatchException, DivideByZeroException {
		if(!cell1.hasSameDimensions(cell2))
			throw new DimensionMismatchException();
		
		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		Cell result = new Cell(rc, cc);
		float ele1,ele2;
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				ele2 = cell2.i4[i][j];
				ele1 = cell1.i4[i][j];
				
				if (ele2 == 0)
					throw new DivideByZeroException();
				else
					result.i4[i][j] = (ele1/ele2);
			}
		}

		return result;
	}
	/**
	 * This method will perform the element by element inverse operation on the
	 * elements of the Cell 'cell' and return the result as a Cell.
	 * <p>
	 * Each element of the resultant Cell will be the inverse of the
	 * corresponding element of the Cell 'cell'.
	 * <p>
	 * If any of the element of Cell 'cell' is found to be zero this method
	 * will throw an DivideByZero Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell dotInverse(Cell cell) throws DivideByZeroException {

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);
		float ele;
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				ele = cell.i4[i][j];
				
				if (ele == 0) {
					throw new DivideByZeroException();
				}
				result.i4[i][j] = (1.0f / ele);
			}
		}

		return result;

	}
	/**
	 * This method will add the corresponding elements of {@link Cell}s 'cell1' and
	 * 'cell2' and return the resultant Cell. 
	 * <p>
	 * The dimension of both the Cells 'cell1' and 'cell2' should be the same
	 * or else this method will throw a DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell add(Cell cell1, Cell cell2)
			throws DimensionMismatchException {
		if (cell1.getRowCount() != cell2.getRowCount()) {
			throw new DimensionMismatchException();
		}

		if (cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}

		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		Cell result = new Cell(rc,cc);
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.i4[i][j] = cell1.i4[i][j] + cell2.i4[i][j];
			}
		}

		return result;
	}

	/**
	 * This method will compute the difference between the corresponding elements of 
	 * {@link Cell}s 'cell1' and 'cell2' and return the resultant Cell. 
	 * <p>
	 * This method will subtract elements of 'cell2' from the corresponding elements
	 * of 'cell1'.
	 * <p>
	 * The dimension of both the Cells 'cell1' and 'cell2' should be the same
	 * or else this method will throw a DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell subtract(Cell cell1, Cell cell2)
			throws DimensionMismatchException {
		if (cell1.getRowCount() != cell2.getRowCount()) {
			throw new DimensionMismatchException();
		}

		if (cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}

		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		Cell result = new Cell(rc,cc);
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.i4[i][j] = cell1.i4[i][j] - cell2.i4[i][j];
			}
		}

		return result;
	}
}
