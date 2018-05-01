package org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;

/**
 * This class define various operations to measure difference between the two {@link Cell}s.
 * <p>Difference between two Cells is a function of the difference in their corresponding element value.
 * <pre>Usage:
 * Let 'a' & 'b' be Cells with similar dimension.
 * 
 * CellDifference cd = new CellDifference();//Create an instance of CellDifference.
 * 
 * double abs_diff = cd.absDiff(a, b);//Compute absolute difference between the input Cells.
 * double mean_diff = cd.meanDiff(a, b);//Compute mean difference between the input Cells.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CellDifference {

	/**
	 * This method computes the sum of difference between the corresponding elements of the two {@link Cell}s 
	 * 'cell1' and 'cell2' and return the same.
	 * <p>The dimensions of both the Cells 'cell1' and 'cell2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double diff(Cell cell1, Cell cell2) throws DimensionMismatchException{

		if(cell1.getRowCount() != cell2.getRowCount() || cell1.getColCount() != cell2.getColCount())
			throw new DimensionMismatchException();
		else
		{
			try{
				double diff = 0;
				int rows = cell1.getRowCount();
				int cols = cell2.getColCount();

				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<cols;j++)
					{
						diff+= cell1.getElement(i,j) - cell2.getElement(i, j);
					}
				}

				return diff;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method computes the sum of absolute difference between the corresponding elements of the two {@link Cell}s 
	 * 'cell1' and 'cell2' and return the same.
	 * <p>This quantity is also called the L1 norm of difference between the two {@link Cell}s.
	 * <p>The dimensions of both the Cells 'cell1' and 'cell2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double absDiff(Cell cell1, Cell cell2) throws DimensionMismatchException{

		if(cell1.getRowCount() != cell2.getRowCount() || cell1.getColCount() != cell2.getColCount())
			throw new DimensionMismatchException();
		else
		{
			try{
				double diff = 0;
				int rows = cell1.getRowCount();
				int cols = cell2.getColCount();

				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<cols;j++)
					{
						diff+= Math.abs(cell1.getElement(i,j) - cell2.getElement(i, j));
					}
				}

				return diff;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the mean of difference between the corresponding elements of the two {@link Cell}s 
	 * 'cell1' and 'cell2' and return the same.
	 * <p>The dimensions of both the Cells 'cell1' and 'cell2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double meanDiff(Cell cell1, Cell cell2) throws DimensionMismatchException{

		if(cell1.getRowCount() != cell2.getRowCount() || cell1.getColCount() != cell2.getColCount())
			throw new DimensionMismatchException();
		else
		{
			try{
				double diff = 0;
				int rows = cell1.getRowCount();
				int cols = cell2.getColCount();
				int elements = rows*cols;

				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<cols;j++)
					{
						diff+= cell1.getElement(i,j) - cell2.getElement(i, j);
					}
				}

				return diff/elements;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the mean of absolute difference between the corresponding elements of the two {@link Cell}s 
	 * 'cell1' and 'cell2' and return the same.
	 * <p>The dimensions of both the Cells 'cell1' and 'cell2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double meanAbsDiff(Cell cell1, Cell cell2) throws DimensionMismatchException{

		if(cell1.getRowCount() != cell2.getRowCount() || cell1.getColCount() != cell2.getColCount())
			throw new DimensionMismatchException();
		else
		{
			try{
				double diff = 0;
				int rows = cell1.getRowCount();
				int cols = cell2.getColCount();
				int elements = rows*cols;

				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<cols;j++)
					{
						diff+= Math.abs(cell1.getElement(i,j) - cell2.getElement(i, j));
					}
				}

				return diff/elements;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the euclidian difference or L2 norm between the two {@link Cell}s 'cell1' and 'cell2'
	 * and return the same.
	 * <p>Euclidian difference between the two Cells is the square root of the sum of square of difference between
	 * their corresponding elements. 
	 * <p>The dimensions of both the Cells 'cell1' and 'cell2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double euclidianDiff(Cell cell1, Cell cell2) throws DimensionMismatchException{

		if(cell1.getRowCount() != cell2.getRowCount() || cell1.getColCount() != cell2.getColCount())
			throw new DimensionMismatchException();
		else
		{
			try{
				double euclidian = 0;
				int rows = cell1.getRowCount();
				int cols = cell2.getColCount();
				float diff=0;

				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<cols;j++)
					{
						diff = cell1.getElement(i,j) - cell2.getElement(i,j);
						euclidian+=diff*diff;
					}
				}

				return Math.sqrt(euclidian);
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method computes the Root Mean Square (RMS) difference between the two {@link Cell}s 'cell1' and 'cell2'
	 * and return the same.
	 * <p>RMS difference between the two Cells is the square root of the average of square of difference between
	 * their corresponding elements. 
	 * <p>The dimensions of both the Cells 'cell1' and 'cell2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double RMSDiff(Cell cell1, Cell cell2) throws DimensionMismatchException{

		if(cell1.getRowCount() != cell2.getRowCount() || cell1.getColCount() != cell2.getColCount())
			throw new DimensionMismatchException();
		else
		{
			try{
				double rms = 0;
				int rows = cell1.getRowCount();
				int cols = cell2.getColCount();
				int elements = rows*cols;
				float diff=0;

				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<cols;j++)
					{
						diff = cell1.getElement(i,j) - cell2.getElement(i,j);
						rms+=diff*diff;
					}
				}

				return Math.sqrt(rms/elements);
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}
}
