package org.JMathStudio.DataStructure.Generic;

import java.util.Vector;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define an index element to represent absolute location of an element within a 1D data structures 
 * like {@link Vector}, {@link CVector} etc. 
 * <p>A 1D index fully describe the location of the element by its absolute 'x' position within the index
 * space of the 1D data structure. 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Index1D {

	private int x;
	
	/**
	 * This will create an {@link Index1D} object representing an elements location within a 1D data structure 
	 * with its absolute 'x' index position as given by the argument 'x'.
	 * <p>The 'x' index position should define a valid location within a 1D index space with origin
	 * at (0) i.e index space starts from (0) index. Thus argument 'x' should not be negative. 
	 * <p>If argument 'x' is less than 0 this will throw an IllegalArgument Exception.
	 * @param int x
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index1D(int x) throws IllegalArgumentException{
		if(x < 0)
			throw new IllegalArgumentException();
		else
		{
			this.x = x;
		}
	}
	
	/**
	 * This will return the absolute 'x' index location for the given 1D index.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getX(){
		return this.x;
	}	
}
