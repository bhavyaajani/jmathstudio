package org.JMathStudio.MathToolkit.MatrixTools.JAMA;

import org.JMathStudio.DataStructure.Cell.Cell;

/**
 * Adapter class over {@link Matrix} JAMA class.
 * 
 * <p>This class is part of cross communication bridge between JAMA and current framework.
 * The class has only design utility and should not be used for any purpose outside the
 * package.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class MatrixAdapter extends Matrix{

	/**
	 * 
	 */
	private static final long serialVersionUID = 651968871669883727L;

	/**
	 * Adapter between {@link Cell} and {@link Matrix} objects.
	 * @see Matrix.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public MatrixAdapter(Cell cell)
	{
		super(cell.getRowCount(),cell.getColCount());
			
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				this.A[i][j] = cell.getElement(i, j);
			}
		}
	}	
	
	public MatrixAdapter(double[][] matrix)
	{
		super(matrix);		
	}	
}
