package org.JMathStudio.DataStructure.Generic;

import java.util.Vector;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a mutable container for storing a set of {@link Pixel}'s. 
 * New Pixel's can be appended to the existing list dynamically.
 * <p>
 * Internally this class represent Pixel list as a {@link java.util.Vector} of
 * Pixel objects.
 * <p>
 * Size - This indicates the number of {@link Pixel}'s contained in the list.
 * <p>
 * Each Pixel in the list can be accessed by its index position which start
 * from 0 to one less than the size of this list.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class PixelList {

	private Vector<Pixel> list;
	
	/**
	 * This will initiate a new empty Pixel list to which {@link Pixel}'s can be appended later
	 * on.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelList() {
		list = new java.util.Vector<Pixel>();
	}
	
	/**
	 * This will initiate a new Pixel list with specified initial capacity 'n'. 
	 * <p>If argument 'n' is less than '0' this method will throw an IllegalArgument
	 * Exception.
	 * <p>Argument 'n' here specify the initial capacity for the list, which increases
	 * as more {@link Pixel}'s are added to the list later on.
	 * @param int n
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelList(int n) throws IllegalArgumentException {
		if(n < 0)
			throw new IllegalArgumentException();
		else
			list = new java.util.Vector<Pixel>(n);
	}

	/**
	 * This will initiate a new Pixel list and populate the same with the non null {@link Pixel}'s
	 * from the given 1D Pixel array 'pixels' in the same order.
	 * <p>
	 * If the Pixel array 'pixels' contain any null Pixel the size of this list will not be same as 
	 * that of the Pixel array.
	 * <p>The argument 'pixels' is passed by reference and no deep copy of the same is made.
	 * 
	 * @param Pixel[] pixels
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelList(Pixel[] pixels) {
		list = new java.util.Vector<Pixel>();

		if (pixels != null) {
			list.ensureCapacity(pixels.length);
			
			for (int i = 0; i < pixels.length; i++) {
				if (pixels[i] != null)
					list.addElement(pixels[i]);
			}
		}

	}

	/**
	 * This method will append the {@link Pixel} 'pixel' at the end of the given Pixel list.
	 * <p>
	 * This increases the size of the given list by 1.
	 * <p>
	 * If the Pixel 'pixel' is null this method will throw a NullPointer Exception.
	 * <p>The argument 'pixel' is added by reference and no deep copy of the same is made.
	 * 
	 * @param Pixel pixel
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void add(Pixel pixel) {
		if (pixel == null)
			throw new NullPointerException();
		else
			list.addElement(pixel);
	}

	/**
	 * This method will convert the given Pixel list to a 1D {@link Pixel} array and return the
	 * same.
	 * <p>The order of the Pixels in the return array will be same as that in the given list.
	 * <p>If the given Pixel list is empty; a null shall be returned.
	 * 
	 * @return Pixel[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Pixel[] accessPixelArray() {

		if (list.size() == 0)
			return null;
		else {
			Pixel[] array = new Pixel[list.size()];

			for (int i = 0; i < array.length; i++) {
				array[i] = access(i);
			}

			return array;
		}
	}

	/**
	 * This method will return the current size of the given Pixel list.
	 * <p>
	 * This indicate the number of {@link Pixel}'s contained within the given
	 * list.
	 * <p>
	 * If the given Pixel list has no Pixel elements this method will return
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
	 * This method will return the {@link Pixel} located at the index position
	 * as given by the argument 'index' within the given list.
	 * <p>
	 * The value of argument 'index' should be in the range of 0 to one less
	 * than the size of this list else this method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return Pixel
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Pixel access(int index) {
		return list.get(index);
	}

	/**
	 * This method will replace the {@link Pixel} element located at the index
	 * position given by the argument 'index' in the given list with the
	 * Pixel as specified by the argument 'pixel'.
	 * <p>
	 * The value of the argument 'index' should be in the range of 0 to one less
	 * than the size of this list else this method will throw an ArrayIndexOutOfBound Exception.
	 * <p>
	 * If the {@link Pixel} 'pixel' is null this method will throw a NullPointer Exception.
	 * <p>The Pixel 'pixel' is replaced by reference and no deep copy of the same is
	 * made by the method.
	 * 
	 * @param Pixel pixel
	 * @param int index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public void replace(Pixel pixel, int index) {

		if (pixel == null)
			throw new NullPointerException();
		else {
			list.remove(index);
			list.insertElementAt(pixel, index);
		}
	}
	
	/**
	 * This method will remove all the {@link Pixel}s from the current list and empty the list.
	 * <p>The size of the list will be '0' after this call.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void clear(){
		this.list.clear();
	}
}
