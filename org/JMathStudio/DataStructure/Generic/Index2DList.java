package org.JMathStudio.DataStructure.Generic;

import java.util.Vector;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a mutable container for storing a set of element indexes within a 2D data structure
 * as represented by {@link Index2D}. New indexes can be appended to the existing list dynamically.
 * <p>
 * Internally this class represent Index2D list as a {@link java.util.Vector} of Index2D objects.
 * <p>
 * Size - This indicates the number of {@link Index2D}'s contained in the list.
 * <p>
 * Each Index2D object within the list can be accessed by its index position which start
 * from 0 to one less than the size of this list.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Index2DList {

	private Vector<Index2D> list;
	
	/**
	 * This will initiate a new empty Index2D list to which {@link Index2D}'s can be appended later
	 * on.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2DList() {
		list = new java.util.Vector<Index2D>();
	}
	
	/**
	 * This will initiate a new Index2D list with specified initial capacity 'n'. 
	 * <p>If argument 'n' is less than '0' this method will throw an IllegalArgument
	 * Exception.
	 * <p>Argument 'n' here specify the initial capacity for the list, which increases
	 * as more {@link Index2D}'s are added to the list later on.
	 * @param int n
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2DList(int n) throws IllegalArgumentException {
		if(n < 0)
			throw new IllegalArgumentException();
		else
			list = new java.util.Vector<Index2D>(n);
	}

	/**
	 * This will initiate a new Index2D list and populate the same with the non null {@link Index2D}'s
	 * from the given 1D Index2D array 'indexes' in the same order.
	 * <p>If any, null Index2D within the array 'indexes' shall not be added to the list and thus the size of
	 * this list may not be same as that of the Index2D array.
	 * <p>The argument 'indexes' is passed by reference and no deep copy of the same is made.
	 * 
	 * @param Index2D[] indexes
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2DList(Index2D[] indexes) {
		list = new java.util.Vector<Index2D>();

		if (indexes != null) {
			list.ensureCapacity(indexes.length);
			
			for (int i = 0; i < indexes.length; i++) {
				if (indexes[i] != null)
					list.addElement(indexes[i]);
			}
		}

	}

	/**
	 * This method will append the {@link Index2D} 'index' at the end of the given Index2D list.
	 * <p>
	 * This increases the size of the given list by 1.
	 * <p>
	 * If the Index2D 'index' is null this method will throw a NullPointer Exception.
	 * <p>The argument 'index' is added by reference and no deep copy of the same is made.
	 * 
	 * @param Index2D index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void add(Index2D index) {
		if (index == null)
			throw new NullPointerException();
		else
			list.addElement(index);
	}

	/**
	 * This method will convert the given Index2D list to a 1D {@link Index2D} array and return the
	 * same.
	 * <p>The order of the indexes in the return array will be same as that in the given list.
	 * <p>If the given Index2D list is empty; a null shall be returned.
	 * 
	 * @return Index2D[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index2D[] accessIndexArray() {

		if (list.size() == 0)
			return null;
		else {
			Index2D[] array = new Index2D[list.size()];

			for (int i = 0; i < array.length; i++) {
				array[i] = access(i);
			}

			return array;
		}
	}

	/**
	 * This method will return the current size of the given Index2D list.
	 * <p>
	 * This indicate the number of {@link Index2D}'s contained within the given
	 * list.
	 * <p>
	 * If the given Index2D list has no index elements this method will return
	 * size as '0'.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public int size() {

		if (list.isEmpty())
			return 0;
		else
			return list.size();
	}

	/**
	 * This method will return the {@link Index2D} object located at the index position
	 * as given by the argument 'index' within the given list.
	 * <p>
	 * The value of argument 'index' should be in the range of 0 to one less
	 * than the size of this list else this method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return Index2D
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Index2D access(int index) {
		return list.get(index);
	}

	/**
	 * This method will replace the {@link Index2D} object located at the index
	 * position given by the argument 'i' in the given list with the Index2D object as 
	 * specified by the argument 'index'.
	 * <p>
	 * The value of the argument 'i' should be in the range of 0 to one less
	 * than the size of this list else this method will throw an ArrayIndexOutOfBound Exception.
	 * <p>
	 * If the {@link Index2D} 'index' is null this method will throw a NullPointer Exception.
	 * <p>The Index2D 'index' is replaced by reference and no deep copy of the same is
	 * made by the method.
	 * 
	 * @param Index2D index
	 * @param int i
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public void replace(Index2D index, int i) {

		if (index == null)
			throw new NullPointerException();
		else {
			list.remove(i);
			list.insertElementAt(index, i);
		}
	}
	
	/**
	 * This method will remove all the {@link Index2D}s from the current list and empty the list.
	 * <p>The size of the list will be '0' after this call.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void clear(){
		this.list.clear();
	}
}
