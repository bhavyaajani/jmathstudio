package org.JMathStudio.PixelImageToolkit;

import java.awt.image.BufferedImage;
import java.util.EmptyStackException;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.VisualToolkit.Viewer.SeriesViewer;


/**
 * This class define a mutable container for storing a set of {@link AbstractPixelImage}'s
 * as a stack. New PixelImages can be appended to the existing stack dynamically.
 * <p>
 * Internally this class represent PixelImage Stack as a {@link java.util.Vector} of
 * AbstractPixelImage objects.
 * <p>
 * Size - This indicates the number of PixelImages contained in the stack.
 * <p>
 * Each PixelImage in the stack can be accessed by its index position which start
 * from 0 to one less than the size of this stack.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class PixelImageStack {

	private java.util.Vector<AbstractPixelImage> stack;

	// private Vector[] stack;

	/**
	 * This will initiate a new empty PixelImage Stack object to which
	 * {@link AbstractPixelImage}'s can be appended later on.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelImageStack() {
		stack = new java.util.Vector<AbstractPixelImage>();
	}

	/**
	 * This will initiate a new PixelImage Stack object and populate the same with
	 * the non null {@link AbstractPixelImage}s from the given 1D array of PixelImages
	 * 'pixelImageArray' in the same order.
	 * <p>
	 * If the PixelImage array 'pixelImageArray' contain any null PixelImages the size of
	 * this PixelImage Stack will not be same as that of the PixelImage array.
	 * <p>The argument 'pixelImageArray' is passed as reference and no deep copy of the same
	 * is made.
	 * 
	 * @param AbstractPixelImage
	 *            [] pixelImageArray
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelImageStack(AbstractPixelImage[] pixelImageArray) {
		stack = new java.util.Vector<AbstractPixelImage>();

		if (pixelImageArray != null) {
			for (int i = 0; i < pixelImageArray.length; i++) {
				if (pixelImageArray[i] != null)
					stack.addElement(pixelImageArray[i]);
			}
		}

	}

	/**
	 * This method will append the {@link AbstractPixelImage} 'image' at the end of the given PixelImage
	 * stack.
	 * <p>
	 * This increases the size of the given PixelImage stack by 1.
	 * <p>
	 * If the AbstractPixelImage 'image' is null this method will throw a NullPointer
	 * Exception.
	 * <p>The argument 'image' is added by reference and no deep copy of the same is made.
	 * 
	 * @param AbstractPixelImage image
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void addPixelImage(AbstractPixelImage image) {
		if (image == null)
			throw new NullPointerException();
		else
			stack.addElement(image);
	}

	/**
	 * This method will convert the given PixelImage stack to a 1D {@link AbstractPixelImage}
	 * array and return the same.
	 * <p>The order of the Pixel Images in the return array will be same as that in the given 
	 * stack.
	 * <p>If the given PixelImage stack is empty; a null shall be returned.
	 * 
	 * @return AbstractPixelImage[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractPixelImage[] accessPixelImageArray() {

		if (stack.size() == 0)
			return null;
		else {
			AbstractPixelImage[] array = new AbstractPixelImage[stack.size()];

			for (int i = 0; i < array.length; i++) {
				array[i] = accessPixelImage(i);
			}

			return array;
		}
	}

	/**
	 * This method will return the current size of the given PixelImage stack.
	 * <p>
	 * This indicate the number of {@link AbstractPixelImage}'s contained within the given
	 * PixelImage stack.
	 * <p>
	 * If the given PixelImage stack has no elements this method will return size '0'.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public int size() {

		if (stack.isEmpty())
			return 0;
		else
			return stack.size();
	}

	/**
	 * This method will return the {@link AbstractPixelImage} located at the index position
	 * as given by the argument 'index' within the given PixelImage stack.
	 * <p>
	 * The value of argument 'index' should be in the range of 0 to one less
	 * than the size of this PixelImage stack else this method will throw an
	 * ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return AbstractPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public AbstractPixelImage accessPixelImage(int index) {
		return stack.get(index);
	}

	/**
	 * This method will replace the PixelImage located at the index
	 * position given by the argument 'index' in the given PixelImage stack with the
	 * PixelImage as specified by the argument 'image'.
	 * <p>
	 * The value of the argument 'index' should be in the range of 0 to one less
	 * than the size of this PixelImage stack else this method will throw an
	 * ArrayIndexOutOfBound Exception.
	 * <p>
	 * If the {@link AbstractPixelImage} 'image' is null this method will throw a
	 * NullPointer Exception.
	 * <p>The argument 'image' is passed by reference and no deep copy of the same is made.
	 * 
	 * @param AbstractPixelImage
	 *            image
	 * @param int index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public void replace(AbstractPixelImage image, int index) {

		if (image == null)
			throw new NullPointerException();
		else {
			stack.remove(index);
			stack.insertElementAt(image, index);
		}
	}

	/**
	 * This method will display all the {@link PixelImage}'s available within this {@link PixelImageStack}
	 * as a series on a {@link SeriesViewer}.
	 * <p>Each PixelImage shall be rendered/displayed according to their type.
	 * <p>If the current PixelImage stack is empty this method will throw an EmptyStack Exception.
	 * <p>The argument 'title' specify the Title for the SeriesViewer.
	 * 
	 * @param String
	 *            title
	 * @throws EmptyStackException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void displayImageSeries(String title)
	{
		if(size()==0){
			throw new EmptyStackException();
		}
		else{
			BufferedImage[] imgs = new BufferedImage[size()];

			for(int i=0;i<imgs.length;i++)
			{
				imgs[i] = accessPixelImage(i).toBufferedImage();
			}

			SeriesViewer viewer = new SeriesViewer();
			viewer.setTitle(title);

			try{
				viewer.displaySeries(imgs, null);
			}catch(DimensionMismatchException e)
			{
				throw new BugEncounterException();
			}
		}

	}

	/**
	 * This method will remove all the Pixel Images from the current stack and empty the stack.
	 * <p>The size of the stack will be '0' after this call.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void clear(){
		this.stack.clear();
	}
}
