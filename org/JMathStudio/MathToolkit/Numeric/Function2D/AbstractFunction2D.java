package org.JMathStudio.MathToolkit.Numeric.Function2D;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;

/**
 * This abstract class define a 2D Function [R^2 -> R].
 * <p>
 * A Function is represented as,
 * <p>
 * z = F(y,x), where 'z' is the real output of the Function 'F' for real inputs 'y' & 'x'.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractFunction2D {
	
	/**
	 * This method will apply the given function F(y,x) over the 2D discrete grid as defined by the
	 * argument {@link MeshGrid} 'grid' and return the output of the function as a {@link Cell}.
	 * <p>The definition of the function is as stated by the method {@link #F(float, float)}.
	 * <p>The returned Cell contain the output of the given function over the {@link MeshGrid} 'grid'.
	 * Each element (i,j) of the Cell with ith row and jth column shall be the output of the function
	 * corresponding to that unique grid point (y,x) in the MeshGrid 'grid' such that;
	 * <p><i> z(i,j) = F(y,x), where (y,x) is the point from MestGrid at (i,j) location.</i> 
	 * @param MeshGrid grid
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell F(MeshGrid grid)
	{
		Vector x = grid.accessGridXVector();
		Vector y = grid.accessGridYVector();
		
		int ySize = y.length();
		int xSize = x.length();
		
		Cell z = new Cell(ySize,xSize);
		float op=0;
		
		for(int i=0;i<ySize;i++)
		{
			for(int j=0;j<xSize;j++)
			{
				op = F(y.getElement(i),x.getElement(j));
				z.setElement(op,i,j);
			}
		}
		
		return z;
	}
	
	/**
	 * This abstract method gives definition for the given Function. The function should be defined
	 * for all values of float 'y' & 'x' and should return a real float.
	 * @param float y
	 * @param float x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float F(float y,float x);

}
