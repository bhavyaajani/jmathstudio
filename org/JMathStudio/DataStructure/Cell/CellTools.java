package org.JMathStudio.DataStructure.Cell;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;

/**
 * This class define several useful operations and manipulations applicable 
 * over a {@link Cell}.
 * <pre>Usage:
 *  Let 'a' & 'b' be Cell objects.
 *  CellTools tool = new CellTools();//Create an instance of CellTools.
 * 
 *  Cell c = tool.cirShiftRows(a, shift);//Shift circularly rows of the Cell.
 *  
 *  Cell d = tool.resize(b, height, width);//Resize dimensionality of the Cell.
 *  
 *  Cell e = tool.concateLeft(a, b);//Concate input Cells.
 *  </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CellTools 
{
	/**
	 * This method will Circularly shift the Columns of the Cell 'cell' 
	 * and return the resultant circularly shifted Cell.
	 * <p>
	 * The argument 'space' specify the amount of circular shift to be applied
	 * and its sign will specify the direction of the shift, i.e.,negative shift
	 * will circularly shift the column elements towards left and positive shift 
	 * will circularly shift the column elements towards right respectively.
	 * 
	 * @param Cell
	 *            cell
	 * @param int space
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell cirShiftColumns(Cell cell, int space) {
		Cell res = cell.clone();

		if (space == 0)
			return res;

		MatrixTools matrix = new MatrixTools();
		
		if (space < 0) 
		{
			res = matrix.transpose(res);

			for (int i = 0; i < -space; i++) {
				Vector tmp = res.accessRow(0);
				try {
					for (int j = 1; j < res.getRowCount(); j++) {

						res.assignRow(res.accessRow(j), j - 1);

					}
					res.assignRow(tmp, res.getRowCount() - 1);
				} catch (DimensionMismatchException e) {
					throw new BugEncounterException();
				}
			}

			return matrix.transpose(res);
		} else {
			res = matrix.transpose(res);

			for (int i = 0; i < space; i++) {
				Vector tmp = res.accessRow(res.getRowCount() - 1);

				try {
					for (int j = res.getRowCount() - 1; j > 0; j--) {
						res.assignRow(res.accessRow(j - 1), j);
					}
					res.assignRow(tmp, 0);
				} catch (DimensionMismatchException e) {
					throw new BugEncounterException();
				}

			}

			return matrix.transpose(res);
		}
	}

	/**
	 * This method will Circularly shift the Rows of the Cell 'cell' and return the
	 * resultant circularly shifted Cell.
	 * <p>
	 * The argument 'space' specify the amount of circular shift to be applied
	 * and its sign will specify the direction of the shift, i.e.,negative shift
	 * will circularly shift the row elements upward and positive shift
	 * will circularly shift the row elements downward respectively.
	 * 
	 * @param Cell
	 *            cell
	 * @param int space
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell cirShiftRows(Cell cell, int space) {
		Cell res = cell.clone();

		if (space == 0)
			return res;

		if (space < 0) {
			for (int i = 0; i < -space; i++) {
				Vector tmp = res.accessRow(0);

				try {
					for (int j = 1; j < res.getRowCount(); j++) {
						res.assignRow(res.accessRow(j), j - 1);
					}
					res.assignRow(tmp, res.getRowCount() - 1);
				} catch (DimensionMismatchException e) {
					throw new BugEncounterException();
				}

			}

			return res;
		} else {
			for (int i = 0; i < space; i++) {
				Vector tmp = res.accessRow(res.getRowCount() - 1);

				try {
					for (int j = res.getRowCount() - 1; j > 0; j--) {
						res.assignRow(res.accessRow(j - 1), j);
					}

					res.assignRow(tmp, 0);
				} catch (DimensionMismatchException e) {
					throw new BugEncounterException();
				}

			}

			return res;
		}
	}

	/**
	 * This method will resize the given Cell 'cell' to a new dimension with row and column count as 
	 * specified by the arguments 'row' and 'col' respectively and return the same. 
	 * <p>The argument 'row' and 'col' should not be less than 1 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>If the dimension as specified by argument 'row' and 'col' is less than that of the Cell 'cell',
	 * the elements of Cell 'cell' falling outside the new dimension shall be discarded. On other hand, if the 
	 * dimension is larger than the dimension of Cell 'cell', the Cell shall be expanded to the larger dimension by
	 * inserting 0's along the extended dimension.    
	 * @param Cell cell
	 * @param int row
	 * @param int col
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell resize(Cell cell,int row,int col) throws IllegalArgumentException
	{
		if(row <1 || col <1)
		{
			throw new IllegalArgumentException();
		}
		Cell result = new Cell(row,col);
		
		for (int i = 0; i < row; i++) {
			if (i < cell.getRowCount()) {
				for (int j = 0; j < col; j++) {
					if (j < cell.getColCount())
						result.setElement(cell.getElement(i,j),i,j);
					else
						break;

				}
			} else
				break;
		}
		
		return result;
	}

	/**
	 * This method will shift the elements of the Cell 'cell' linearly along row and column dimension by amount
	 * as specified by the argument 'rowShift' and 'colShift' respectively and return the same. The
	 * dimensions of the return Cell is similar to that of the original Cell.
	 * <p>The sign of the arguments 'rowShift' and 'colShift' will decide the direction of shift.
	 * <p>Each element of the Cell 'cell' with row and column index 'r' and 'c' respectively shall be 
	 * relocated or shifted to the new row and column index 'r + rowShift' and 'c + colShift'.
	 * <p>During relocation if element falls outside the dimensions of the Cell 'cell' that element 
	 * will be discarded. On other hand the vacant position created due to shift in the Cell 'cell' shall
	 * be filled with 0's.
	 * <p>This operation is equivalent to translation of the Cell 'cell' along both the dimension.
	 * @param Cell cell
	 * @param int rowShift
	 * @param int colShift
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell shift(Cell cell,int rowShift,int colShift)
	{
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);
		int y,x;
		
		for(int i=0;i<rc;i++)
		{
			for(int j=0;j<cc;j++)
			{
				y = i-rowShift;
				x = j-colShift;
				
				if(x >=0 && x<cc && y>=0 && y<rc)
					result.setElement(cell.getElement(y,x),i,j);
				
			}
			
		}
		
		return result;	
	}

	/**
	 * This method will shift the the Cell 'cell' cyclicly along its row and column dimension by amount
	 * as specified by the argument 'rowShift' and 'colShift' respectively and return the same. The
	 * dimensions of the return Cell is similar to that of the original Cell.
	 * <p>The sign of the argument 'rowShift' and 'colShift' will decide the direction for shift.
	 * <p>The sub part of the Cell which goes outside the bounds of the Cell after shift is wrapped around and
	 * enter the Cell from the opposite side. Thus unlike linear shift where elements of the shifted Cell which
	 * falls outside the Cell are discarded, in cyclic Cell no elements are lost.
	 * <p>This operation is equivalent to simultaneous cyclic rotation/shift of the rows and columns of the Cell 
	 * with specified amount.
	 * 
	 * @param Cell cell
	 * @param int rowShift
	 * @param int colShift
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell shiftCyclic(Cell cell,int rowShift,int colShift)
	{
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		if(rowShift%rc == 0 && colShift%cc == 0){
			return cell.clone();		
		}
		
		Cell result = new Cell(rc,cc);
		int y,x;
		
		for(int i=0;i<rc;i++)
		{
			for(int j=0;j<cc;j++)
			{
				y = i-rowShift;
				x = j-colShift;
				
				if(x >=0 && x<cc && y>=0 && y<rc)
					result.setElement(cell.getElement(y,x),i,j);
				else
					result.setElement(cell.getElementWithPadding(y, x), i, j);
				
			}
			
		}
		
		return result;	
	}
	
	/**
     * This method will rearrange the elements of the Cell 'cell' so as to configure the dimensionality
     * of the Cell 'cell' as given by the arguments 'M' and 'N'.
     * <p>Here argument 'M' specify the row count and 'N' specify the column count of the reconfigured Cell
     * respectively, which is returned as a Cell.
     * <p>
     * This re arrange operation requires a prerequisite that number of elements in the resultant reconfigured Cell
     * with row count and column count given by argument 'M' and 'N' should be same as that of the Cell
     * 'cell', this requires satisfaction of the following condition,
     * <p> M*N = Cell_RowCount * Cell_ColumnCount.
     * <p> If the above required condition is not meet this method will throw an IllegalArgument Exception.
     * 
     * @param Cell cell
     * @param int M
     * @param int N
     * @return Cell
     * @throws IllegalArgumentException
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */	
	public Cell reArrange(Cell cell, int M,int N) throws IllegalArgumentException 
	{
		if(M*N != cell.getRowCount()*cell.getColCount())
		{
			throw new IllegalArgumentException();
		}
		
		Cell result = new Cell(M,N);
				
		int indexI=0;
		int indexJ=0;
		
		for(int i=0;i<cell.getRowCount();i++)
		{
			for(int j=0;j<cell.getColCount();j++)
			{
				if(indexJ == result.getColCount())
				{
					indexJ =0;
					indexI++;
				}
				result.setElement(cell.getElement(i,j),indexI,indexJ++);
			}
		}
		
		return result;
	}
	
    /**
	 * This method will concate the Cells given by arguments 'cell1' and 'cell2' along the rows.
	 * The corresponding rows of Cell 'cell2' will be concated with the corresponding rows of
	 * Cell 'cell1'. This operation will be like placing the 'cell2' on left of the 'cell1'.
	 * <p>Because concate operation will be along the rows, the row count for both the Cells should
	 * be same or else this method will throw an DimensionMisMatch Exception.	 
	 * <p>The resulting concated Cell will be returned by this method.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell concateLeft(Cell cell1,Cell cell2) throws DimensionMismatchException
	{
			if(cell1.getRowCount() != cell2.getRowCount())
			{
				throw new DimensionMismatchException();
			}
			
			Cell result = new Cell(cell1.getRowCount(),cell1.getColCount() + cell2.getColCount());
					
			for(int i=0;i<cell1.getRowCount();i++)
			{
				for(int j=0;j<cell1.getColCount();j++)
				{
					result.setElement(cell1.getElement(i,j), i,j);
				}
			}
			
			for(int i=0;i<cell2.getRowCount();i++)
			{
				for(int j=0;j<cell2.getColCount();j++)
				{
					result.setElement(cell2.getElement(i,j),i,j + cell1.getColCount());
				}
			}
			
			return result;
	}

	/**
	 * This method will concate the Cells given by arguments 'cell1' and 'cell2' along the columns.
	 * The corresponding columns of Cell 'cell2' will be concated with the corresponding columns of
	 * Cell 'cell1'. This operation will be like placing the 'cell2' below the 'cell1'.
	 * <p>Because concate operation will be along the columns, the column count for both the Cells should
	 * be same or else this method will throw an DimensionMisMatch Exception.	 
	 * <p>The resulting concated Cell will be returned by this method.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell concateBelow(Cell cell1,Cell cell2) throws DimensionMismatchException
	{
			if(cell1.getColCount() != cell2.getColCount())
			{
				throw new DimensionMismatchException();
			}
			
			Cell result = new Cell(cell1.getRowCount()+cell2.getRowCount(),cell1.getColCount());
			
			for(int i=0;i<cell1.getRowCount();i++)
			{
				result.assignRow(cell1.accessRow(i), i);
			}
			
			for(int j=0;j<cell2.getRowCount();j++)
			{
				result.assignRow(cell2.accessRow(j), j + cell1.getRowCount());
			}
			
			return result;
		
	}

	/**
	 * This method will return a Cell which is a sub Cell or part of the Cell 'cell'. The returned
	 * Cell will have its extreme top-left position starting from the row and column element given
	 * by arguments 'row' and 'col' respectively of the mother Cell 'cell'. The subCell will extend 'rowCount' 
	 * rows and 'colCount' columns from the starting position of the mother 'cell'.
	 * <p>If any of the argument 'row','col','rowCount' and 'colCount' is such that the subCell extends beyond
	 * the present dimension of the mother Cell 'cell',this method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell cell
	 * @param int row
	 * @param int rowCount
	 * @param int col
	 * @param int colCount
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell subCell(Cell cell,int row,int rowCount,int col,int colCount) throws IllegalArgumentException
	{
		if(col < 0 || row < 0 || rowCount <1 || colCount <1)
		{
			throw new IllegalArgumentException();
		}
		
		if(row + rowCount > cell.getRowCount() || col + colCount > cell.getColCount())
		{
			throw new IllegalArgumentException();
		}
		
		Cell result = new Cell(rowCount,colCount);
		
		for(int i=0;i<rowCount;i++)
		{
			for(int j=0;j<colCount;j++)
			{
				float tmp = cell.getElement(row + i,col + j);
				result.setElement(tmp,i,j);
			}
		}
		
		return result;			
	}
	
	/**
	 * This method segment the Cell 'cell' into the blocks with 'm' rows and 'n' columns and will return all
	 * such blocks or sub Cells as a CellStack.
	 * <p>The CellStack will have blocks starting from the top-left to bottom-right block in increasing order.
	 * <p>If the row and column count of 'cell' is not a multiple of 'm' and 'n' respectively, Cell 'cell' will
	 * be appropriately padded with row and column elements with all zeros, before blocking.
	 * <p>The arguments 'm' and 'n' should be more than 0 else this method will throw an IllegalArgument
	 * Exception. Further, arguments 'm' and 'n' respectively should not be more than the row count and column count 
	 * of 'cell' else this method will throw an IllegalArgument Exception.
	 * @param Cell cell
	 * @param int m
	 * @param int n
	 * @return CellStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack block(Cell cell,int m,int n) throws IllegalArgumentException
	{
		if(m <=0 || n<=0)
			throw new IllegalArgumentException();
				
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		if(m > rc || n > cc)
			throw new IllegalArgumentException();
		
		int yPadd = rc;
		int xPadd = cc;
		
		if( rc%m !=0)
		{
			yPadd = yPadd + m - (rc%m);
		}
		if( cc%n !=0)
		{
			xPadd = xPadd + n - (cc%n);
		}
		
		Cell resize = resize(cell, yPadd, xPadd);
		
		int nosImages = (yPadd/m)*(xPadd/n);
		
		Cell[] result = new Cell[nosImages];
		
		int index=0;
		
		for(int i=0;i<resize.getRowCount()/m;i++)
		{
			for(int j=0;j<resize.getColCount()/n;j++)
			{
				result[index++]=subCell(resize, i*m, m, j*n, n);
			}
			
		}

		return new CellStack(result);
		
	}
	
	/**
	 * This method will shrink the Cell 'cell' by shrinking its Rows and Columns
	 * by the factor as specified by the argument 'm' and 'n' respectively by removing
	 * intermediate elements of the Cell. The shrink Cell will be return.
	 * <p>
	 * The argument 'm' and 'n' should be more than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * This method will shrink the given Cell by removing intermediate elements. Thus the
	 * return Cell will have Row count of floor((original Row count)/m) and floor(Column count of 
	 * (original Column count)/n) respectively.
	 * <p>This method first shrinks the Cell along its columns and than shrinks the column
	 * shrinked cell along its rows.
	 * 
	 * @param Cell
	 *            cell
	 * @param int m
	 * @param int n
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see #expand(Cell, int, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell shrink(Cell cell, int m, int n)
			throws IllegalArgumentException {
		if (m < 1 || n < 1) {
			throw new IllegalArgumentException();
		}
		float[][] result = new float[cell.getRowCount()][cell.getColCount()];

		VectorTools tools = new VectorTools();
		MatrixTools matrix = new MatrixTools();

		for (int i = 0; i < result.length; i++) {
			Vector vector = tools.shrink(cell.accessRow(i), n);
			result[i] = vector.accessVectorBuffer();
		}
		try {
			result = matrix.transpose(new Cell(result)).accessCellBuffer();

			for (int i = 0; i < result.length; i++) {
				Vector vector = tools.shrink(new Vector(result[i]), m);
				result[i] = vector.accessVectorBuffer();
			}

			return matrix.transpose(new Cell(result));
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will expand the Cell 'cell' by expansion of its Rows and Columns
	 * by the factor as specified by the argument 'm' and 'n' respectively by inserting
	 * appropriate number of intermediate zeros. The expanded Cell will be return.
	 * <p>
	 * The argument 'm' and 'n' should be more than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * This method will expand or stretch the given Cell by inserting zeros in between. Thus the
	 * return Cell will have Row count of m*(original Row count) and Column count of 
	 * n*(original Column count) respectively.
	 * <p>This method first expands the Cell along its columns and than expand the column
	 * expanded cell along its rows.
	 * 
	 * @param Cell
	 *            cell
	 * @param int m
	 * @param int n
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see #shrink(Cell, int, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell expand(Cell cell, int m, int n)
			throws IllegalArgumentException {
		if (m < 1 || n < 1) {
			throw new IllegalArgumentException();
		}

		float[][] result = new float[cell.getRowCount()][cell.getColCount()];

		VectorTools tools = new VectorTools();
		MatrixTools matrix = new MatrixTools();
		
		for (int i = 0; i < result.length; i++) {
			Vector vector = tools.expand(cell.accessRow(i), n);
			result[i] = vector.accessVectorBuffer();
		}
		try {
			result = matrix.transpose(new Cell(result)).accessCellBuffer();

			for (int i = 0; i < result.length; i++) {
				Vector vector = tools.expand(new Vector(result[i]), m);
				result[i] = vector.accessVectorBuffer();
			}

			return matrix.transpose(new Cell(result));
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
     * This method will Wrap the Cell 'cell' and return the same as a Cell.
     * <p>Wrap operation will make the Cell to wrap on itself so that the outer
     * edge elements of the Cell moves towards the centre of the Cell and vice
     * versa.
     * <p>The dimensions of the return Cell will be similar to the original Cell.
     *  
     * @param Cell cell
     * @return Cell
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
	public Cell wrapCell(Cell cell)
	{
		int height = cell.getRowCount();
		int width = cell.getColCount();
		 
		int yShift = (height-1)/2;
		int xShift = (width-1)/2;
		  
		Cell result = new Cell(height,width);
		 		  
		  for(int i=0;i<height;i++)
		  {
			  for(int j=0;j<width;j++)
			  {
				  int y = (i+yShift)%height;
				  int x = (j+xShift)%width;
			  	  result.setElement(cell.getElement(i,j),y,x);
			  }  
		  }
		  
		  return result;
	}
	
	/**
	 * This method will flip the order of the columns of the Cell 'cell' and return the 
	 * resultant Cell.
	 * <p>
	 * Flip operation will reverse or flip the index or arrangement of the
	 * columns in the given Cell such that the column at index position 'i'
	 * will be relocated to the index position 'N-1-i', where 'N' is the column
	 * count of the Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 **/
	public Cell flipColumns(Cell cell) {

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc, cc);
		int shift = cc-1;

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.setElement(cell.getElement(i, shift - j), i, j);
			}
		}

		return result;

	}

	/**
	 * This method will flip the order of the rows of the Cell 'cell' and return the 
	 * resultant Cell.
	 * <p>
	 * Flip operation will reverse or flip the index or arrangement of the rows
	 * in the Cell such that the row at index position 'i' will be relocated
	 * to the index position 'N-1-i', where 'N' is the row count of the Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 **/
	public Cell flipRows(Cell cell) {

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc, cc);
		int shift = rc-1;

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(cell.getElement(shift-i, j), i, j);
			}
		}

		return result;

	}
	
	/**
	 * This method will swap the columns of the Cell 'cell' as identified by their index 
	 * position given by the arguments 'TargetColumn' and 'DestinationColumn' respectively
	 * and return the resultant Cell.
	 * <p>
	 * The index positions given by the argument 'TargerColumn' and 'DestinationColumn' should
	 * be in the range of 0 and one less than the column count of Cell 'cell', else this 
	 * method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param int TargetColumn
	 * @param int DestinationColumn
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell swapColumn(Cell cell, int TargetColumn, int DestinationColumn)
			throws IllegalArgumentException {

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		if (TargetColumn < 0 || TargetColumn >= cc) {
			throw new IllegalArgumentException();
		}

		if (DestinationColumn < 0 || DestinationColumn >= cc) {
			throw new IllegalArgumentException();
		}

		Cell result = new Cell(rc, cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (j == TargetColumn) {
					result.setElement(cell.getElement(i, DestinationColumn),
							i, j);
				} else if (j == DestinationColumn) {
					result.setElement(cell.getElement(i, TargetColumn), i, j);
				} else {
					result.setElement(cell.getElement(i, j), i, j);
				}
			}
		}
		return result;
	}
	
	/**
	 * This method will swap the rows of the Cell 'cell' as identified by their index 
	 * position given by the arguments 'TargetRow' and 'DestinationRow' respectively
	 * and return the resultant Cell.
	 * <p>
	 * The index positions given by the argument 'TargerRow' and 'DestinationRow' should
	 * be in the range of 0 and one less than the row count of Cell 'cell', else this 
	 * method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param int TargetRow
	 * @param int DestinationRow
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell swapRow(Cell cell, int TargetRow, int DestinationRow)
			throws IllegalArgumentException {

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		if (TargetRow < 0 || TargetRow >= rc) {
			throw new IllegalArgumentException();
		}

		if (DestinationRow < 0 || DestinationRow >= rc) {
			throw new IllegalArgumentException();
		}

		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (i == TargetRow) {
					result.setElement(cell.getElement(DestinationRow, j), i,
							j);
				} else if (i == DestinationRow) {
					result.setElement(cell.getElement(TargetRow, j), i, j);
				} else {
					result.setElement(cell.getElement(i, j), i, j);
				}
			}
		}
		return result;
	}
	
	/**
	 * This method will resize the given Cell 'cell' to a new dimension with row and column count as 
	 * specified by the arguments 'row' and 'col' respectively and return the same. 
	 * <p>The argument 'row' and 'col' should not be less than 1 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>If the new dimension as specified by argument 'row' and 'col' is less than that of the Cell 'cell',
	 * the elements of Cell 'cell' falling outside the dimension shall be discarded. On other hand, if the dimension
	 * is larger than the dimension of Cell 'cell', the Cell shall be expanded to the larger dimension by
	 * cyclic extention along respective dimension i.e new elements are filled in along the extended dimension
	 * by cyclic extention of the original cell.    
	 * @param Cell cell
	 * @param int row
	 * @param int col
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell resizeCyclic(Cell cell,int row,int col)throws IllegalArgumentException{
		
		if(row<=0 || col<=0)
			throw new IllegalArgumentException();
		
		int height = cell.getRowCount();
		int width = cell.getColCount();
		
		if(row <= height || col <= width)
		{
			Cell res = new Cell(row,col);
			
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<col;j++)
				{
					res.setElement(cell.getElement(i, j), i, j);
				}
			}
			
			return res;
		}
		else
		{
			Cell res = new Cell(row,col);
			
			for(int i=0;i<height;i++)
			{
				for(int j=0;j<width;j++)
				{
					res.setElement(cell.getElement(i, j), i, j);
				}
			}
			float ele;
			for(int i=0;i<height;i++)
			{
				for(int j=width;j<col;j++)
				{
					ele = cell.getElement(i%height, j%width);
					res.setElement(ele, i, j);
				}
			}
			for(int i=height;i<row;i++)
			{
				for(int j=0;j<width;j++)
				{
					ele = cell.getElement(i%height, j%width);
					res.setElement(ele, i, j);
				}
			}
			for(int i=height;i<row;i++)
			{
				for(int j=width;j<col;j++)
				{
					ele = cell.getElement(i%height, j%width);
					res.setElement(ele, i, j);
				}
			}
			return res;
		}
	}
	
	/**
	 * This method will insert the Cell 'insert' within the Cell 'cell' at the offset position as specified by the 
	 * row and column position given by the arguments 'r' and 'c' respectively.
	 * <p>The argument 'r' and 'c' specifying the row and column offset position for inserting the Cell 'insert' should not
	 * be negative else this method will throw an IllegalArgument Exception.
	 * <p>The insert operation will overwrite the portion of the Cell 'cell' with the Cell 'insert'.
	 * <p>The dimensions of the Cell 'insert' and the insert offset 'r' and 'c' should be such that the Cell 'insert'
	 * remains completely within the Cell 'cell' else this method can not insert the Cell 'insert' completely and
	 * thus will throw an IllegalArgument Exception.
	 * <p>If the dimensions of the Cell 'cell' are (R,C) and that of Cell 'insert' are (R',C'), ensure that following
	 * condition is meet,
	 * <p><i> R' + r <= R & C' + c <= C.</i> 
	 * 
	 * @param Cell cell
	 * @param Cell insert
	 * @param int r
	 * @param int c 
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell insert(Cell cell,Cell insert,int r,int c) throws IllegalArgumentException
	{
		if(r < 0 || c < 0)
			throw new IllegalArgumentException();
		
		int Rc = cell.getRowCount();
		int Cc = cell.getColCount();
		
		int rc = insert.getRowCount();
		int cc = insert.getColCount();
		
		int R = rc + r;
		int C = cc + c;
		
		if(R > Rc || C > Cc)
			throw new IllegalArgumentException();
		else
		{
			try{
			Cell res = new Cell(Rc,Cc);
			
			for(int i=0;i<r;i++)
			{
				for(int j=0;j<Cc;j++)
				{
					res.setElement(cell.getElement(i, j), i, j);
				}
			}
			
			for(int i=r;i<Rc;i++)
			{
				for(int j=0;j<c;j++)
				{
					res.setElement(cell.getElement(i, j), i, j);
				}
			}
			
			for(int i=r,p=0;i<R;i++,p++)
			{
				for(int j=c,q=0;j<C;j++,q++)
				{
					res.setElement(insert.getElement(p, q), i, j);
				}
			}
			
			for(int i=R;i<Rc;i++)
			{
				for(int j=c;j<Cc;j++)
				{
					res.setElement(cell.getElement(i, j), i, j);
				}
			}
			
			for(int i=r;i<R;i++)
			{
				for(int j=C;j<Cc;j++)
				{
					res.setElement(cell.getElement(i, j), i, j);
				}
			}
			
			return res;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}
}
