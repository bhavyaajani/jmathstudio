package org.JMathStudio.DataStructure.Iterator.Iterator1D;

import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.IteratorOutOfBoundsException;

/**
 * This class define an Abstract 1D Iterator, helpful in iterating over elements defined on a 1D index space.
 * <p>Data structures like {@link Vector} or {@link CVector} are 1D structures with elements indexed over 1D index 
 * space. Each element in the 1D index space is refer by its unique 'x' integer index location where 'x' takes values
 * from [0 to length) of the 1D structure. Thus 1D Index space is an ordered set of elements located over a 1D line 
 * where each location is uniquely identified by its 'x' integer index location.
 * <p>The Iterator defined here provides an easy mechanism to iterate over the elements located within a
 * well defined sub index space or segment within a 1D data structure with read & write access. The iterator sub 
 * index space or segment shall be represented by an {@link Iterator1DBound} object.
 * <p>An Iterator1DBound has arbitrary origin in the 1D index space and finite length. Iterator1DBound
 * defining the sub index segment shall be completely within the largest index space for the associated data structure. 
 * <p>Each state of the iterator is defined by its current 'x' location within the iterator bounds
 * over the 1D index space and provides read & write access to element present at that index location 
 * within the associated data structure. By default, iterator initialises at the iterator bounds origin within the 
 * index space. 
 * <h3>Iterator Movement</h3>
 * <p>Each call to 'next' moves the iterator to next valid index within the iterator bounds by incrementing 
 * the current 'x' index position. If 'x' index position has reach the end of segment within the iterator bounds, the subsequent
 * calls to 'next' moves and restrict the 'x' location to one index beyond the end of iterator segment.
 * <p>Each call to 'previous' moves the iterator to previous valid index within the iterator bounds by decrementing 
 * the current 'x' index position. If 'x' index position has reach the start of segment within the iterator bounds, the subsequent
 * calls to 'previous' moves and restrict the 'x' location to one index beyond the start of iterator segment.
 * <p>Iterator provides read & write access to the elements of associated data structure for current iterator location. 
 * If read & write access to the element is made when iterator is beyond the valid set bounds, see {@link #isBound()}, 
 * an IteratorOutOfBounds Exception shall be thrown. This way, iterator limits the operations within the set Iterator1DBound.     
 * <h3>Understanding Index Space </h3>
 * <p>Consider a 1D data structure with length 20. The index space for this data structure shall be an ordered set 
 * of indexes (x) starting from location(0) to (19). The origin (0) shall be located at the left most point. Each
 * element of the data structure shall be identified by an unique location over the index space by its 'x' index
 * location. 
 * <p>Iterator1DBound shall define a sub index space within the index space of data structure. Thus index sub space
 * with origin at (5) and length of 10 indexes, span the index space from (5) till (14), which is a valid sub index space within 
 * the index space of parent data structure. However, a index sub space with origin at (10) and length 15 indexes span the index 
 * space from (10) till (24), which is not a valid sub index space as it overshoot the index space of parent data structure. 
 * <p>Once an Iterator1DBound is set, 1D Iterator shall limit itself over the index space as defined by the Iterator1DBound and 
 * provide access to element at the desired index location within the bound.
 * <p>Let the Iterator1DBound define an index sub space from (5) till (9) [Origin at (5) with length of 5 indexes]. 
 * Let the iterator initialise at the origin/start of the iterator bounds, that is at location (5). The subsequent next calls 
 * shall move the iterator towards right by incrementing the 'x' index location from (5) till (9). Once the iterator
 * has reach the end of iterator bounds that is (9) subsequent calls to the next shall bound the iterator location at (10) 
 * which is just outside the valid bounds of the set iterator.
 * <p>Similarly for calls to previous shall move the iterator towards left by decrementing the 'x' index location till the start 
 * of bound is reach. Call to previous beyond this point shall bound the iterator location at (4) which is just beyond the valid
 * bounds of the set iterator. 
 * <h3>Note:</h3>
 * <p>Though Iterator mechanism provides an easy way to iterate over a desired region within the data structures,
 * it is slow as compare to standard for loop implementation.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class Abstract1DIterator {

	private Iterator1DBound bound = null;
	protected int i4 = 0;
	protected boolean i2 = false;
	
	/**
	 * This method reset the {@link Iterator1DBound} of the given 1D Iterator with {@link Iterator1DBound}
	 * 'bound'.
	 * <p>If the Iterator1DBound 'bound' is not completely within the index space of the data structure 
	 * associated with the 1D Iterator, this method will throw an IteratorOutOfBounds Exception.
	 * @param Iterator1DBound bound
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract void assignBounds(Iterator1DBound bound) throws IteratorOutOfBoundsException;
		
	/**
	 * This method provide access to the current Iterator1DBound set for the given 1D Iterator.
	 * 
	 * @return Iterator1DBound
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final Iterator1DBound accessBounds(){
		return this.bound;
	}
	
	/**
	 * This method check if the given 1D Iterator is at the start of the set Iterator1DBound. 
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final boolean isAtStart(){
		return bound.isAtStart(i4);
	}

	/**
	 * This method check if the given 1D Iterator is at the end of the set Iterator1DBound. 
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final boolean isAtEnd(){
		return bound.isAtEnd(i4);
	}
	
	/**
	 * This method moves the given 1D Iterator to the start of the set Iterator1DBound. 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void goToStart(){
		this.i4 = bound.ox;
		i2 = true;
	}
	
	/**
	 * This method moves the given 1D Iterator to the end of the set Iterator1DBound.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void goToEnd(){
		this.i4 = bound.ex;
		i2 = true;
	}
	
	/**
	 * This method moves the given 1D Iterator to the arbitrary index location within the index
	 * space as specified by the arguments 'x'.
	 * <p>If the index location specified by the argument 'x' does not fall within the valid index bounds 
	 * for set Iterator1DBound, this method shall throw an IteratorOutOfBounds Exception.  
	 * @param int x
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void goToIndex(int x) throws IteratorOutOfBoundsException{
		if(!bound.isWithinBounds(x))
			throw new IteratorOutOfBoundsException();
		else{
			this.i4 = x;
			this.i2 = true;
		}
	}
	
	protected final void f1(Iterator1DBound bound){
		this.i4 = bound.ox;
		this.i2 = true;
		this.bound = bound;
	}
	/**
	 * This method moves the given 1D Iterator to next valid location within the set Iterator1DBound.
	 * <p>Check if the iterator is within the bounds of set Iterator1DBound before accessing the element.
	 * See {@link #isBound()}.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void next(){
		i4++;
		if(i4 > bound.ex){
			i4 = bound.ex+1;
			i2 = false;
		}
		else
			i2 = true;
	}
	
	/**
	 * This method moves the given 1D Iterator to previous valid location within the set Iterator1DBound.
	 * <p>Check if the iterator is within the bounds of set Iterator1DBound before accessing the element.
	 * See {@link #isBound()}.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final void previous(){
		i4--;
		if(i4 < bound.ox){
			i4 = bound.ox-1;
			i2 = false;
		}
		else
			i2 = true;
	}
	
	/**
	 * This method checks if the given 1D Iterator is within the bounds of the set Iterator1DBound.
	 * <p>If the iterator is within the bounds of the set Iterator1DBound the element at current iterator
	 * location may be access and manipulated otherwise the operation shall throw an IteratorOutOfBounds
	 * Exception.
	 * @return boolean 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final boolean isBound(){
		return this.i2;
	}
	
		
	/**
	 * This method return the 'x' index for current iterator location.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final int getCurrentIndex(){
		return this.i4;
	}
}
