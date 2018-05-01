package org.JMathStudio.DataStructure.Generic;

/**
 * This class define an individual spatial real image pixel. 
 * <p>A pixel has a set of spatial coordinate/offset relative to the reference point and stores
 * a real value.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class Pixel {

	private float value;
	private int x;
	private int y;
	
	/**
	 * This will initialise an image pixel with value as given by the argument 'value' and relative spatial 
	 * coordinates (y,x) as specified by the arguments 'y' and 'x'.
	 * @param float value
	 * @param int y
	 * @param int x
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Pixel(float value,int y,int x){
		this.value = value;
		this.y = y;
		this.x = x;
	}
	
	/**
	 * This will return the pixel value.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getValue(){
		return this.value;
	}
	
	/**
	 * This will return the relative 'y' coordinate/offset for the pixel.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getYOffset(){
		return this.y;
	}
	
	/**
	 * This will return the relative 'x' coordinate/offset for the pixel.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getXOffset(){
		return this.x;
	}
}
