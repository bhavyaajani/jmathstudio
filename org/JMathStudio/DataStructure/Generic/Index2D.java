package org.JMathStudio.DataStructure.Generic;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.AbstractPixelImage;

/**
 * This class define a 2D index element to represent absolute pixel location within a 2D data structures 
 * like {@link Cell}, {@link AbstractPixelImage}, {@link CCell} etc. 
 * <p>A 2D index fully describe the location of the pixel by its absolute 'y' and 'x' index position
 * within the index space of the 2D data structure. 
 * <p>Note: Depending upon the type of 2D data structure, the 'y' and 'x' index position of Index2D corresponds
 * to row index along the height and column index along the width of the 2D structure/image respectively.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Index2D {

	private int x;
	private int y;
	
	/**
	 * This will create an {@link Index2D} object representing a pixel location within a 2D data structures 
	 * with pixels absolute 'y' and 'x' index position as given by the arguments 'y' and 'x' respectively.
	 * <p>The 'y' and 'x' index position should define a valid location within a 2D index space with origin
	 * at (0,0) i.e index space starts from (0,0) index. Thus arguments 'y' and 'x' should not be negative. 
	 * <p>If any of the argument 'y' and 'x' is less than 0 this will throw an IllegalArgument Exception.
	 * @param int y
	 * @param int x
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2D(int y,int x) throws IllegalArgumentException{
		if(y < 0 || x < 0)
			throw new IllegalArgumentException();
		else
		{
			this.y = y;
			this.x = x;
		}
	}
	
	/**
	 * This will return the absolute 'x' index location for the given 2D index.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * This will return the absolute 'y' index location for the given 2D index.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getY(){
		return this.y;
	}
}
