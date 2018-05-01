package org.JMathStudio.PlugIn;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.Exceptions.DimensionMismatchException;

/**
 * This class define an abstract multivariate point operator which operate over a list of {@link Cell}s as
 * represented by a {@link CellStack}.
 * <p>The class define an abstract method 'operator' which state the definition for the required
 * point operator. 
 * <p>The point operator define here shall be a real function which operate on a multiple real scalars 
 * and gives out a real scalar.
 * <i>
 * <p>Y(i,j) = f( X1(i,j), X2(i,j), ..., Xn(i,j) ).
 * <p>here, 'Y' is the resultant Cell.
 * <p> 'X1 to Xn' are input Cells with same dimensions.
 * <p>'f' is the multivariate point operator operating on the corresponding real elements (i,j) of input
 * Cells 'X1 to Xn' to produce output Y(i,j).
 * </i>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractMultiCellOperator {

	/**
	 * This method will evaluate the given multivariate point operator over the corresponding elements of the 
	 * input {@link Cell}s as represented by {@link CellStack} 'stack' and return the result as a {@link Cell}.
	 * <p>All the Cells within CellStack 'stack' should have similar dimensions else this method will throw a
	 * DimensionMismatch Exception.
	 * <p>The definition of the point operator is as stated by the method {@link #operator(float[]).
	 * <p>The returned Cell contain the output for the given operator evaluated independently over the
	 * corresponding elements of the input Cells.
	 * <p>Caution: 
	 * <p>1. <i>The number of input variables (dimensionality) of the given multivariate point operator is set to number
	 * of {@link Cell}s in the given CellStack. Ensure that the number of Cells in the input CellStack 'stack' 
	 * satisfy the dimensionality requirement of the defined operator {@link #operator(float[])} else this method
	 * may throw an {@link ArrayIndexOutOfBoundsException}.</i>
	 * <p>2. <i> Ensure correct ordering of the Cells in the input CellStack 'stack' as the operator {@link #operator(float[])}
	 * receives input variables from the given Cells in given order.</i>
	 *  
	 * @param CellStack stack
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final Cell evaluateOverMultiCells(CellStack stack) throws DimensionMismatchException{
		
		Cell[] buffer = stack.accessCellArray();
		
		if(buffer == null)
			throw new NullPointerException();
		
		int N = buffer.length;
		
		int h = buffer[0].getRowCount();
		int w = buffer[0].getColCount();
		
		for(int i=1;i<N;i++){
			if(!buffer[i].hasSameDimensions(buffer[0]))
				throw new DimensionMismatchException();
		}
		
		Cell res = new Cell(h,w);
				
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				float[] list = new float[N];
				for(int k=0;k<N;k++){
					list[k] = buffer[k].getElement(i, j);
				}
				res.setElement(operator(list), i, j);
			}
		}
		
		return res;
	}
	
	/**
	 * This abstract method state the definition for the given multivariate point operator.
	 * <p>The operator defined here shall be a real function defined over multiple real scalars
	 * as given by array 'list' and return a real scalar 'y' of the form, y = f(x1,x2,...xn).
	 * <p>The input array 'list' provide order set of elements from the corresponding location within
	 * all the input Cells as represented by the CellStack (see {@link #evaluateOverMultiCells(CellStack)}).
	 * The order of input variables within the array 'list' is similar to the order of Cells in the input
	 * CellStack. 
	 * @param float[] list
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float operator(float[] list);
}
