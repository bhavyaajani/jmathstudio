package org.JMathStudio.DataStructure.Cell;

import org.JMathStudio.DataStructure.Generic.Index2D;
import org.JMathStudio.DataStructure.Generic.Index2DList;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;

/**
 * This class define some of the useful operations for finding the elements within a {@link Cell} with specific
 * traits or values.
 * <p>The location of all the matching elements within the Cell shall be return as an {@link Index2DList}.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import external image as Cell.
 *		
 * CellProbe cp = new  CellProbe();//Create an instance of CellProbe object.
 * Index2DList indexes = cp.findAllMaxElementIndexes(img);//Find index location of all maximum elements. 
 * 		
 * img.setAllElements(0, indexes);//Replace all maximum elements with 0.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CellProbe {

	/**
	 * This method will find index location for all the elements of {@link Cell} 'cell' which has global maximum value
	 * within the Cell and return the index list as {@link Index2DList}.
	 * <p>Thus all the {@link Index2D} within the return list specify location of all those elements 
	 * within the Cell 'cell' having the global maximum value within the Cell.
	 * @param Cell cell
	 * @return Index2DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2DList findAllMaxElementIndexes(Cell cell)
	{
		try{
			float max = new CellStatistics().maximum(cell);

			final int rc = cell.getRowCount();
			final int cc = cell.getColCount();

			float[][] buffer = cell.accessCellBuffer();
			Index2DList list = new Index2DList();

			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					if(buffer[i][j] == max){
						list.add(new Index2D(i, j));
					}
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Cell} 'cell' which has global minimum value
	 * within the Cell and return the index list as {@link Index2DList}.
	 * <p>Thus all the {@link Index2D} within the return list specify location of all those elements 
	 * within the Cell 'cell' having the global minimum value within the Cell.
	 * @param Cell cell
	 * @return Index2DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2DList findAllMinElementIndexes(Cell cell)
	{
		try{
			float min = new CellStatistics().minimum(cell);

			final int rc = cell.getRowCount();
			final int cc = cell.getColCount();

			float[][] buffer = cell.accessCellBuffer();
			Index2DList list = new Index2DList();

			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					if(buffer[i][j] == min){
						list.add(new Index2D(i, j));
					}
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Cell} 'cell' which has value equal to 
	 * the argument 'element' and return the index list as {@link Index2DList}.
	 * <p>Thus all the {@link Index2D} within the return list specify location of all those elements 
	 * within the Cell 'cell' having value equal to the argument 'element'.
	 * @param Cell cell
	 * @param float element
	 * @return Index2DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2DList findAllElementIndexes(Cell cell,float element)
	{
		try{
			final int rc = cell.getRowCount();
			final int cc = cell.getColCount();

			float[][] buffer = cell.accessCellBuffer();
			Index2DList list = new Index2DList();

			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					if(buffer[i][j] == element){
						list.add(new Index2D(i, j));
					}
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Cell} 'cell' which has value 
	 * more than or equal to argument 'min' but less than or equal to argument 'max' respectively and return 
	 * the index list as {@link Index2DList}.
	 * <p>Thus all the {@link Index2D} within the return list specify location of all those elements 
	 * within the Cell 'cell' having value in the range of [min max].
	 * <p>If argument 'min' is more than argument 'max' this method will throw an {@link IllegalArgumentException}.
	 * 
	 * @param Cell cell
	 * @param float min
	 * @param float max
	 * @return Index2DList
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Index2DList findAllElementIndexesInRange(Cell cell,float min,float max) throws IllegalArgumentException
	{
		if(min > max)
			throw new IllegalArgumentException();
		
		try{
					
			final int rc = cell.getRowCount();
			final int cc = cell.getColCount();

			float[][] buffer = cell.accessCellBuffer();
			Index2DList list = new Index2DList();

			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					float check = buffer[i][j];
					if(check >= min && check <= max){
						list.add(new Index2D(i, j));
					}
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Cell} 'cell' which has value 
	 * greater than or equal to argument 'threshold' and return the index list as {@link Index2DList}.
	 * <p>Thus all the {@link Index2D} within the return list specify location of all those elements 
	 * within the Cell 'cell' having value greater than or equal to 'threshold'.
	 * 
	 * @param Cell cell
	 * @param float threshold
	 * @return Index2DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Index2DList findAllLargerElementIndexes(Cell cell,float threshold) 
	{
		try{					
			
			final int rc = cell.getRowCount();
			final int cc = cell.getColCount();

			float[][] buffer = cell.accessCellBuffer();
			Index2DList list = new Index2DList();

			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					float check = buffer[i][j];
					if(check >= threshold){
						list.add(new Index2D(i, j));
					}
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Cell} 'cell' which has value 
	 * less than or equal to argument 'threshold' and return the index list as {@link Index2DList}.
	 * <p>Thus all the {@link Index2D} within the return list specify location of all those elements 
	 * within the Cell 'cell' having value less than or equal to 'threshold'.
	 * 
	 * @param Cell cell
	 * @param float threshold
	 * @return Index2DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Index2DList findAllSmallerElementIndexes(Cell cell,float threshold) 
	{
		try{
					
			final int rc = cell.getRowCount();
			final int cc = cell.getColCount();

			float[][] buffer = cell.accessCellBuffer();
			Index2DList list = new Index2DList();

			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					float check = buffer[i][j];
					if(check <= threshold){
						list.add(new Index2D(i, j));
					}
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
}
