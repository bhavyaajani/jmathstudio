package org.JMathStudio.DataStructure.Structure;

/**
 * This class define a spatial structure which represent a Neighbor to a pixel. 
 * <p>A Neighbor is a simple structure which specify a neighbor to a pixel
 * along with its relative position from the pixel.
 * <p>A set (array) of all such Neighbors together define the whole neighborhood 
 * around the pixel.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Neighbor {

	private int x;
	private int y;
	
	//Protected constructor. Do not make public.
	/**
	 * Create a new Neighbor with relative 'Y' and 'X' index position from the pixel
	 * as given by the arguments 'y' and 'x' respectively.
	 * @param int y
	 * @param int x
	 */
	Neighbor(int y,int x){
		this.x = x;
		this.y = y;
	}
		
	/**
	 * This method return the relative 'X' index position of the Neighbor from the
	 * pixel.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getX(){
		return this.x;
	}
		
	/**
	 * This method return the relative 'Y' index position of the Neighbor from the
	 * pixel.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getY(){
		return this.y;
	}	
}
