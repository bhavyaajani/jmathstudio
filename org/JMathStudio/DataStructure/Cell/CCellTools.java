package org.JMathStudio.DataStructure.Cell;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define several useful operations and manipulations applicable over
 * a {@link CCell}.
 * <pre>Usage:
 * Let 'a' be CCell object.
 * 
 * CCellTools tool = new CCellTools();//Create an instance of CCellTools.
 * CCell flip = tool.flipRows(a);//Flip order of rows of input CCell.
 * CCell b = tool.swapRows(a, i, j);//Swap selected rows of input CCell.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CCellTools {

	/**
	 * This method will flip the order of the columns of the CCell 'cell' and
	 * the resultant flip version of 'cell' will be return as the CCell.
	 * <p>
	 * Flip operation will reverse or flip the index or arrangement of the
	 * columns in the CCell 'cell', such that column at index position i will be
	 * relocated to the index position N-1-i, where 'N' is the column count of
	 * the 'cell'.
	 * 
	 * @param CCell
	 *            cell
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell flipColumns(CCell cell) {

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		CCell result;
		try {
			result = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		int L = cc-1;
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.ii[i][j] = cell.ii[i][L-j];
				result.t2[i][j] = cell.t2[i][L-j];
			}
		}

		return result;

	}

	/**
	 * This method will swap the column elements of the CCell 'cell' located at
	 * the index position given by the argument 'TargetColumn' and
	 * 'DestinationColumn'. The resultant swap version of 'cell' is returned as
	 * CCell.
	 * <p>
	 * The index positions given by the argument 'TargerColumn' and
	 * 'DestinationColumn' should be in the range of 0 and one less than the
	 * column count of 'cell', else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param CCell
	 *            cell
	 * @param int TargetColumn
	 * @param int DestinationColumn
	 * @return CCell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell swapColumn(CCell cell, int TargetColumn, int DestinationColumn)
			throws IllegalArgumentException {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		if (TargetColumn < 0 || TargetColumn >= cc) {
			throw new IllegalArgumentException();
		}

		if (DestinationColumn < 0 || DestinationColumn >= cc) {
			throw new IllegalArgumentException();
		}
		
		CCell result = new CCell(rc,cc);
		
		for (int i = 0; i < rc; i++) 
		{
			for (int j = 0; j < cc; j++) 
			{
				if (j == TargetColumn) 
				{
					result.ii[i][j] = cell.ii[i][DestinationColumn];
					result.t2[i][j] = cell.t2[i][DestinationColumn];										
				} else if (j == DestinationColumn) 
				{
					result.ii[i][j] = cell.ii[i][TargetColumn];
					result.t2[i][j] = cell.t2[i][TargetColumn];
				} else 
				{
					result.ii[i][j] = cell.ii[i][j];
					result.t2[i][j] = cell.t2[i][j];
				}

			}
		}

		return result;
	}

	/**
	 * This method will swap the row elements of the CCell 'cell' located at the
	 * index position given by the argument 'TargetRow' and 'DestinationRow'.
	 * The resultant swap version of 'cell' is returned as CCell.
	 * <p>
	 * The index positions given by the argument 'TargerRow' and
	 * 'DestinationRow' should be in the range of 0 and one less than the row
	 * count of 'cell', else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param CCell
	 *            cell
	 * @param int TargetRow
	 * @param int DestinationRow
	 * @return CCell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public CCell swapRows(CCell cell, int TargetRow, int DestinationRow)
			throws DimensionMismatchException {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		if (TargetRow < 0 || TargetRow >= rc) {
			throw new DimensionMismatchException();
		}

		if (DestinationRow < 0 || DestinationRow >= rc) {
			throw new DimensionMismatchException();
		}
		
		CCell result;
		try {
			result = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				if (i == TargetRow) {
					result.ii[i][j] = cell.ii[DestinationRow][j];
					result.t2[i][j] = cell.t2[DestinationRow][j];
				} else if (i == DestinationRow) {
					result.ii[i][j] = cell.ii[TargetRow][j];
					result.t2[i][j] = cell.t2[TargetRow][j];
				} else {
					result.ii[i][j] = cell.ii[i][j];
					result.t2[i][j] = cell.t2[i][j];
				}
			}
		}
		return result;
	}

	/**
	 * This method will flip the order of the rows of the CCell 'cell' and the
	 * resultant flip version of 'cell' will be return as CCell.
	 * <p>
	 * Flip operation will reverse or flip the index or arrangement of the rows
	 * in the CCell 'cell', such that row at index position i will be relocated
	 * to the index position N-1-i, where 'N' is the row count of the 'cell'.
	 * 
	 * @param CCell
	 *            cell
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell flipRows(CCell cell) {

		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		CCell result;
		try {
			result = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		int L = rc-1;
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.ii[i][j] = cell.ii[L-i][j];
				result.t2[i][j] = cell.t2[L-i][j];
			}
		}

		return result;
	}

	/**
	 * This method will rearrange the elements of the CCell 'cell' so as to
	 * configure the dimensionality of the CCell 'cell' as given by the
	 * arguments 'M' and 'N'.
	 * <p>
	 * Here argument 'M' specify the row count and 'N' specify the column count
	 * of the reconfigured CCell respectively, which is returned as a CCell.
	 * <p>
	 * This re arrange operation requires a prerequisite that number of elements
	 * in the resultant reconfigured CCell with row count and column count given
	 * by argument 'M' and 'N' should be same as that of the CCell 'cell', this
	 * requires satisfaction of the following condition,
	 * <p>
	 * M*N = Cell_RowCount * Cell_ColumnCount.
	 * <p>
	 * If the above required condition is not meet this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * This method works properly if all the column elements of the CCell 'cell'
	 * has same length or size.
	 * 
	 * @param CCell
	 *            cell
	 * @param int M
	 * @param int N
	 * @return CCell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell reArrange(CCell cell, int M, int N)
			throws IllegalArgumentException {
		
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		if (M * N != rc * cc) {
			throw new IllegalArgumentException();
		}

		CCell result;
		try{
			result = new CCell(M, N);
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}

		int indexI = 0;
		int indexJ = 0;

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if(indexJ == N)
				{
					indexJ =0;
					indexI++;
				}
				
				result.ii[indexI][indexJ] = cell.ii[i][j];
				result.t2[indexI][indexJ] = cell.t2[i][j];
				
				indexJ++;
				
			}
		}

		return result;
	}

	/**
	 * This method will Wrap the CCell 'cell' and return the same as a CCell.
	 * <p>
	 * Wrap operation will make the CCell to wrap on itself so that the outer
	 * edge elements of the CCell moves towards the centre of the CCell and vice
	 * versa.
	 * <p>
	 * The dimensions of the return CCell will be similar to the original CCell.
	 * 
	 * @param CCell
	 *            cell
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell wrapCCell(CCell cell) {
		try {
			int height = cell.getRowCount();
			int width = cell.getColCount();

			int yShift = (height - 1) / 2;
			int xShift = (width - 1) / 2;

			CCell result = new CCell(height, width);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int y = (i + yShift) % height;
					int x = (j + xShift) % width;
					
					result.ii[y][x] = cell.ii[i][j];
					result.t2[y][x] = cell.t2[i][j];
				}
			}

			return result;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

}
