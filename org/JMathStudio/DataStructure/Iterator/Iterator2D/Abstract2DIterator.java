package org.JMathStudio.DataStructure.Iterator.Iterator2D;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.IteratorOutOfBoundsException;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define an Abstract 2D Iterator, helpful in iterating over elements defined in a 2D index space.
 * <p>Data structures representing an image like {@link Cell} or {@link AbstractUIntPixelImage}
 * are 2D structures with elements indexed over 2D index space. Each element in the 2D index space
 * is refer by its unique 'x' and 'y' integer index location where 'x' takes values from [0 to width) 
 * and 'y' takes values from [0 to height) of the 2D structure respectively. Thus 2D Index space is an ordered
 * set of elements located over a 2D grid where each location is uniquely identified by its 'y' and 'x' integer
 * index location.
 * <p>The Iterator defined here provides an easy mechanism to iterate over the elements located within a
 * well defined sub index space or region within a 2D data structure with read & write access. The iterator sub index space or region over 
 * shall be represented by an {@link Iterator2DBound} object.
 * <p>An Iterator2DBound has arbitrary origin in the 2D index space with height and width. Iterator2DBound
 * defining the sub index space shall be completely within the largest index space for the associated data structure. 
 * <p>Each state of the iterator is defined by its current 'y' and 'x' location within the iterator bounds
 * over the 2D index space and provides read & write access to element present at that index location 
 * within the associated data structure. By default, iterator initialises at the iterator bounds origin within the 
 * index space. 
 * <h3>Iterator Movement</h3>
 * <p>Each call to 'next' moves the iterator to next valid index within the iterator bounds by incrementing 
 * the 'x' index position. If 'x' index position has reach the end of line within the iterator bounds, the next 
 * valid index is set by incrementing the 'y' index and thus iterator moves to the first element of next line.
 * If iterator has reach the end of bounded region, subsequent calls to 'next' shall move and restrict the iterator
 * location to one index beyond the end of bounded region.
 * <p>Each call to 'previous' moves the iterator to previous valid index within the iterator bounds by decrementing 
 * the 'x' index position. If 'x' index position has reach the start of line within the iterator bounds, the previous 
 * valid index is set by decrementing the 'y' index and thus iterator moves to the last element of the previous line.
 * If iterator has reach the start/origin of bounded region, subsequent calls to 'previous' shall move and restrict the iterator
 * location to one index beyond the start of bounded region.
 * <p>Iterator provides read & write access to the elements of associated data structure for current iterator location. 
 * If read & write access to the element is made when iterator is beyond the valid set bounds, see {@link #isBound()}, 
 * an IteratorOutOfBounds Exception shall be thrown. This way, iterator limits the operations within the set Iterator2DBound.     
 * <h3>Understanding Index Space </h3>
 * <p>Consider a 2D data structure with height 20 and width 20. The index space for this data structure shall be an ordered set 
 * of indexes (y,x) starting from location(0,0) to (19,19). The origin (0,0) shall be located at the top-left corner. Each
 * element of the data structure shall be identified by an unique location over the index space by its 'y' and 'x' index
 * location. 
 * <p>Iterator2DBound shall define a sub index space within the index space of data structure. Thus index sub space
 * with origin at (5,5) and height of 10 indexes and width of 5 indexes respectively, span the index space from (5,5) 
 * till (14,9), which is a valid sub index space within the index space of parent data structure. However, a index sub space
 * with origin at (10,10) and height of 15 indexes and width of 11 indexes respectively, span the index space from
 * (10,10) till (24,20), which is not a valid sub index space as it overshoot the index space of parent data structure. 
 *<p>Once an Iterator2DBound is set, 2D Iterator shall limit itself over the index space as defined by the Iterator2DBound and 
 * provide access to element at the desired index location within the bound.
 * <p>Let the Iterator2DBound define an index sub space from (5,5) till (9,9) [Origin at (5,5) with height and width of 5 indexes]. 
 * Let the iterator initialise at the origin/start of the iterator bounds, that is at location (5,5). The subsequent next calls 
 * shall move the iterator along the horizontal direction by incrementing the 'x' index location from (5,5) till (5,9). Once the iterator
 * has reach the end of line at (5,9), the next call shall increment the 'y' index location and reset the 'x' index location at the
 * first value, that is (6,5). Now subsequent calls to next increment the 'x' index location till end of line that is (6,9) is reach
 * and so on. Once the iterator reaches the end of the iterator bounds that is (9,9) subsequent calls to the next shall bound the 
 * iterator location at (9,10) which is just outside the bounds of the set iterator.
 * <p>Similarly for calls to previous shall decrement the 'x' index location till the start of line is reach. Call to previous at
 * this point shall decrement the 'y' index position and reset the 'x' index location to the last index location.  Once the iterator 
 * reaches the start of the iterator bounds that is (5,5) subsequent calls to the previous shall bound the iterator location at (5,4) 
 * which is just outside the bounds of the set iterator. 
 * <h3>Note:</h3>
 * <p>Though Iterator mechanism provides an easy way to iterate over a desired region within the data structures,
 * it is slow as compare to standard for loop implementation.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class Abstract2DIterator {

	private Iterator2DBound bound = null;
	protected int y = 0;
	protected int x = 0;
	protected boolean i3 = false;
	
	/**
	 * This method reset the {@link Iterator2DBound} of the given 2D Iterator with {@link Iterator2DBound}
	 * 'bound'.
	 * <p>If the Iterator2DBound 'bound' is not completely within the index space of the data structure 
	 * associated with the 2D Iterator, this method will throw an IteratorOutOfBounds Exception.
	 * @param Iterator2DBound bound
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract void assignBounds(Iterator2DBound bound) throws IteratorOutOfBoundsException;
		
	/**
	 * This method provide access to the current Iterator2DBound set for the given 2D Iterator.
	 * 
	 * @return Iterator2DBound
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final Iterator2DBound accessBounds(){
		return this.bound;
	}
	
	/**
	 * This method check if the given 2D Iterator is at the start of the set Iterator2DBound. 
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final boolean isAtStart(){
		return bound.isAtStart(y, x);
	}

	/**
	 * This method check if the given 2D Iterator is at the end of the set Iterator2DBound. 
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final boolean isAtEnd(){
		return bound.isAtEnd(y, x);
	}
	
	/**
	 * This method moves the given 2D Iterator to the start of the set Iterator2DBound. 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void goToStart(){
		this.y = bound.oy;
		this.x = bound.ox;
		i3 = true;
	}
	
	/**
	 * This method moves the given 2D Iterator to the end of the set Iterator2DBound.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void goToEnd(){
		this.y = bound.ey;
		this.x = bound.ex;
		i3 = true;
	}
	
	/**
	 * This method moves the given 2D Iterator to the arbitrary location within the index
	 * space as specified by the arguments 'y' and 'x'. Here 'y' and 'x' specify the index locations
	 * along the height and width of the data structure respectively.
	 * <p>If the index location specified by the arguments 'y' and 'x' does not fall within the
	 * set Iterator2DBound, this method shall throw an IteratorOutOfBounds Exception.  
	 * @param int y
	 * @param int x
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void goToIndex(int y, int x) throws IteratorOutOfBoundsException{
		if(!bound.isWithinBounds(y, x))
			throw new IteratorOutOfBoundsException();
		else{
			this.y = y;
			this.x = x;
			this.i3 = true;
		}
	}
	
	protected final void f1(Iterator2DBound bound){
		this.y = bound.oy;
		this.x = bound.ox;
		this.i3 = true;
		this.bound = bound;
	}
	/**
	 * This method moves the given 2D Iterator to next valid location within the set Iterator2DBound.
	 * <p>Check if the iterator is within the bounds of set Iterator2DBound before accessing the element.
	 * See {@link #isBound()}.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void next(){
		x++;
		if(x > bound.ex){
			x = bound.ox;
			y++;
			if(y > bound.ey){
				//Bound the last out of bound index.
				x = bound.ex+1;
				y = bound.ey;
				i3 = false;
			}
			else
				i3 = true;
		}
	}
	
	/**
	 * This method moves the given 2D Iterator to previous valid location within the set Iterator2DBound.
	 * <p>Check if the iterator is within the bounds of set Iterator2DBound before accessing the element.
	 * See {@link #isBound()}.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void previous(){
		x--;
		if(x < bound.ox){
			x = bound.ex;
			y--;
			if(y < bound.oy){
				//Bound the last out of bound index.
				x = bound.ox-1;
				y = bound.oy;
				i3 = false;
			}
			else
				i3 = true;
		}
	}
	
	/**
	 * This method checks if the given 2D Iterator is within the bounds of the set Iterator2DBound.
	 * <p>If the iterator is within the bounds of the set Iterator2DBound the element at current iterator
	 * location may be access and manipulated otherwise the operation shall throw an IteratorOutOfBounds
	 * Exception.
	 * @return boolean 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final boolean isBound(){
		return this.i3;
	}
	
	/**
	 * This method return the 'y' index for current iterator location.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final int getCurrentYIndex(){
		return this.y;
	}
	
	/**
	 * This method return the 'x' index for current iterator location.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final int getCurrentXIndex(){
		return this.x;
	}
}
