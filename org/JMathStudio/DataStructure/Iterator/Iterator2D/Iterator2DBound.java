package org.JMathStudio.DataStructure.Iterator.Iterator2D;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define Iterator2DBound which specify the sub index space bounds for a 2D Iterator.
 * <p>See {@link Abstract2DIterator} for more information on 2D iterators.
 * <p>2D Iterators operate within the bounds as set by the Iterator2DBound.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public class Iterator2DBound {

	//ImageRegion should be immutable class.
	int ox;
	int oy;
	int ex;
	int ey;
	int h;
	int w;
	//Geometry: Origin at top left corner.
	/**
	 * This will create an Iterator2DBound with origin in index space at location (oy,ox) and with height and
	 * width as given by the arguments 'height' and 'width' respectively.
	 * <p>Thus the Iterator2DBound shall span the valid index space from location (oy,ox) till (oy+height-1,ox+width-1).
	 * <p>If the value of arguments 'oy' and 'ox' is less than 0 this method will throw an IllegalArgument Exception.
	 * <p>Further, if the value of arguments 'height' and 'width' is less than 1 this method will throw an IllegalArgument
	 * Exception.
	 * @param int oy
	 * @param int ox
	 * @param int height
	 * @param int width
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Iterator2DBound(int oy,int ox,int height,int width) throws IllegalArgumentException{
		if(oy < 0 || ox < 0 || height <=0 || width <= 0)
			throw new IllegalArgumentException();
		else{
			this.ox = ox;
			this.oy = oy;
			this.h = height;
			this.w = width;
			this.ex = ox + width -1;
			this.ey = oy + height -1;
		}
	}
	
	/**
	 * This method checks if the index space as define by the Iterator2DBound 'bound' is within or sub index space
	 * of the index space as defined by the given Iterator2DBound.
	 * @param Iterator2DBound bound
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isSubBounds(Iterator2DBound bound){
		return (bound.ox >= ox && bound.oy >= oy && bound.ex <= ex && bound.ey <= ey);
	}
	
	/**
	 * This method checks if the index location as specified by the arguments 'y' and 'x' falls within the valid
	 * bounds for given Iterator2DBound.
	 * <p>If the origin of the Iterator2DBound is set at (oy,ox) with height 'h' and width 'w'; this method checks
	 * for condition,
	 * <p><i>(oy <= y && y < oy+h) && (ox <= x && x < ox+w)</i>
	 * @param int y
	 * @param int x
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isWithinBounds(int y,int x){
		return (y >= oy && y <= ey && x >= ox && x <= ex);
	}
	
	/**
	 * This method checks if the index location as specified by the arguments 'y' and 'x' defines the origin/start
	 * of the given Iterator2DBound.
	 * @param int y
	 * @param int x
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isAtStart(int y,int x){
		return (y==oy && x==ox);
	}
	
	/**
	 * This method checks if the index location as specified by the arguments 'y' and 'x' defines the end
	 * of the given Iterator2DBound.
	 * @param int y
	 * @param int x
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isAtEnd(int y,int x){
		return (y==ey && x==ex);
	}
}
