package org.JMathStudio.PlugIn;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define an abstract block operator which operate over an individual blocks/segments of a {@link Cell}.
 * <p>The class define an abstract method 'operator' which should state the required block operation. 
 * <p>The block operator works as follows:
 * <i>
 * <p>1. Divide input Cell into different blocks of specified dimensions (apply suitable zero padding if necessary).
 * <p>2. Apply block operation over each block. 
 * <p>3. Recombine operated blocks in correct order to get final resultant Cell.
 * </i>
 * <p>The dimensions of the resultant Cell may or may not be similar to that of the input Cell.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractCellBlockOperator {

	/**
	 * This method will apply the given block operator on the input {@link Cell} 'cell' and return the
	 * resultant Cell.
	 * <p>This method will segment the input 'cell' into blocks of [m n] dimensions where 'm' is the 
	 * row count and 'n' is the column count for each block. If the dimensions of the input 'cell' is not a
	 * multiple of the dimensions of blocks, appropriate padding with zero shall be applied prior to block estimation. 
	 * <p>The value of argument 'm' and 'n' should be more than '0' else this method will throw an IllegalArgument
	 * Exception. Further, argument 'm' and 'n' respectively should not be more than the row count and column
	 * count of 'cell' else this method will throw an IllegalArgument Exception.
	 * <p>Once blocks are computed, this method will apply the block operator as defined by method {@link #operator(Cell)}
	 * on each individual block and re-composed these operated blocks in correct order to produce the final resultant
	 * Cell, which shall be returned.
	 * <p>If the dimensions of operated block as return by {@link #operator(Cell)} is not similar to dimensions
	 * of input block i.e [m n], this method will throw an DimensionMismatch Exception.
	 * <p>The dimensions of the return Cell may or may not be similar to that of input 'cell'. If 'rc' and 'cc' is
	 * the row count and column count of the input 'cell' than dimensions of the return Cell will be;
	 * <i>
	 * <p>if (rc%m !=0) [rc + m -(rc%m)] else rc
	 * <p>if (cc%n !=0) [cc + n -(cc%n)] else cc
	 * <p>Note: If 'rc' and 'cc' are multiple of 'm' and 'n' i.e (rc%m = 0) & (cc%n = 0), the resultant Cell will have 
	 * the same dimension as that of input 'cell'.
	 * </i>
	 * @param Cell cell
	 * @param int m
	 * @param int n
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Cell evaluateOverCell(Cell cell,int m,int n)throws IllegalArgumentException, DimensionMismatchException
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

		CellTools ct = new CellTools();

		try{
			//This also create a buffer on which blocks as segmented shall be replaced by 
			//corresponding resultant blocks. Thus input Cell is not touched.
			Cell resize = ct.resize(cell, yPadd, xPadd);

			int row_blocks = yPadd/m;
			int col_blocks = xPadd/n;

			Cell block;

			for(int i=0;i<row_blocks;i++)
			{
				for(int j=0;j<col_blocks;j++)
				{
					block = ct.subCell(resize, i*m, m, j*n, n);
					block = operator(block);
					if(block.getRowCount() != m || block.getColCount() != n)
						throw new DimensionMismatchException();
					
					insert(resize, block, i*m, j*n);
				}

			}

			return resize;
		}catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This abstract method state the definition for the given block operator.
	 * <p>The block operator defined here shall operate on an input block as represented by Cell 'block'
	 * and return the resultant operated block as a Cell.
	 * <p>Ensure that the dimensions of return block is similar to that of the input block. 
	 * @param Cell block
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract Cell operator(Cell block);

	protected final void insert(Cell cell,Cell insert,int r,int c) throws IllegalArgumentException
	{
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
			for(int i=r,p=0;i<R;i++,p++)
			{
				for(int j=c,q=0;j<C;j++,q++)
				{
					cell.setElement(insert.getElement(p, q), i, j);
				}
			}			
		}
	}

}
