package org.JMathStudio.PlugIn;

import org.JMathStudio.DataStructure.Cell.Cell;


/**
 * This class define an abstract Point operator which operate independently on each element of the {@link Cell}.
 * <p>The class define an abstract method 'operator' which state the definition for the required
 * point operator. 
 * <p>The point operator define here shall be a real function which takes in a real scalar and gives out a
 * real scalar.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractCellOperator {
	
	/**
	 * This method will evaluate the given Point operator over the elements of the {@link Cell}
	 * 'cell' and return the result as a {@link Cell}.
	 * <p>The definition of the point operator is as stated by the method {@link #operator(float)}.
	 * <p>The returned Cell contain the output for the given operator evaluated independently over the
	 * corresponding elements of the Cell 'cell'.
	 * @param Cell cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final Cell evaluateOverCell(Cell cell)
	{
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		Cell result = new Cell(rc,cc);
		
		float op=0;
		
		for(int i=0;i<rc;i++)
		{
			for(int j=0;j<cc;j++)
			{
				op = operator(cell.getElement(i,j));
			    result.setElement(op,i,j);
			}
		}
		
		return result;
	}
	
	/**
	 * This abstract method state the definition for the given Point operator.
	 * <p>The operator defined here shall be a real function defined over the
	 * real scalar 'x' and return a real scalar 'y' of the form, y = f(x).
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float operator(float x);

}

