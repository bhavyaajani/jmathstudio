package org.JMathStudio.PixelImageToolkit.UIntPixelImage;

import org.JMathStudio.DataStructure.Iterator.Iterator2D.Iterator2DBound;
import org.JMathStudio.DataStructure.Iterator.Iterator2D.UIntPixelImageIterator;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.AbstractPixelImage;

/**
 * This is an abstract class which define the template for an UInt PixelImages.
 * <p>
 * This abstract class extends {@link AbstractPixelImage}, thus it define a
 * valid PixelImage.
 * <p>
 * All the UInt PixelImages like {@link UInt8PixelImage}, {@link UInt16PixelImage} and 
 * {@link CustomUIntPixelImage} extends this abstract base class.
 * <p>
 * All UInt PixelImages will be represented by this abstract class for all
 * related transforms and operations defined on UInt PixelImages.
 * <p>Note: For all UInt PixelImages, the pixel intensities should be represented by an
 * unsigned integer i.e pixel intensities should not be negative.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public abstract class AbstractUIntPixelImage extends AbstractPixelImage {

	//PixelImage classes are not Serializable right now but may be in future.
	protected transient UIntPixelImageIterator iterator = null;
	
	/**
	 * This abstract method will return the Depth of the given UInt PixelImage.
	 * <p>
	 * UInt PixelImage with depth 'd' has <i> 2^d </i> different Gray Scale
	 * levels.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getDepth();

	/**
	 * This abstract method will return the number of different gray scale intensity level for
	 * the given UInt PixelImage.
	 * <p>UInt PixelImage with depth 'd' has 2^d different gray scale intensity levels. 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getIntensityLevels();
	
	/**
	 * This abstract method return the Pixel Intensity for the location as specified by
	 * its Row and Column given by arguments 'row' and 'column' respectively for
	 * this UInt PixelImage.
	 * <p>
	 * The argument 'row' and 'column' should be within the bound of [0 height
	 * of image-1] and [0 width of image -1] respectively.
	 * <p>
	 * The Pixel Intensity return will be in the intensity range define for that
	 * UInt PixelImage.
	 * 
	 * @param int row
	 * @param int column
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getPixel(int row, int column);

	/**
	 * This abstract method replaces the Pixel Intensity for the location as specified by
	 * its Row and Column given by the arguments 'row' and 'column' respectively
	 * for this UInt PixelImage by Pixel intensity as given by argument 'value'.
	 * <p>
	 * The pixel intensity 'value' should be in the range of valid pixel
	 * intensity for this UInt PixelImage else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * See {@link #getMaxValidPixel()} and {@link #getMinValidPixel()} method to know the
	 * maximum and minimum valid pixel intensity for this UInt PixelImage.
	 * 
	 * @param int value
	 * @param int row
	 * @param int column
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract void setPixel(int value, int row, int column)
			throws IllegalArgumentException;

	/**
	 * This abstract method return the Maximum valid Pixel intensity for this UInt
	 * PixelImage. This depends upon the Gray Scale level or depth of the given
	 * UInt PixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getMaxValidPixel();

	/**
	 * This abstract method return the Minimum valid Pixel intensity for this UInt
	 * PixelImage.
	 * <p>
	 * This will always be '0' for all UInt PixelImages.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getMinValidPixel();

//	/**
//	 * This method will clone the given UInt PixelImage and return the exact
//	 * replica of it as an AbstractUIntPixelImage of the same type.
//	 * 
//	 * @return AbstractUIntPixelImage
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	public abstract AbstractUIntPixelImage clone();

	/**
	 * This method will check whether the pixel intensity as given by the argument 'pixel'
	 * is within the valid gray scale intensity range for the given UInt PixelImage.
	 * @param int pixel
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean validatePixel(int pixel)
	{
		return !(pixel<getMinValidPixel() || pixel>getMaxValidPixel());
	}	
	
	/**
	 * This will return an instance of {@link UIntPixelImageIterator} associated with the given AbstractUIntPixelImage.
	 * <p>Each AbstractUIntPixelImage object maintains a single internal instance of UIntPixelImageIterator object. Getting
	 * UIntPixelImageIterator through this method avoid creation of multiple instances of UIntPixelImageIterator associated
	 * with the given AbstractUIntPixelImage.
	 * @return UIntPixelImageIterator
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UIntPixelImageIterator getAssociatedIterator(){
		if(iterator == null)
			iterator = new UIntPixelImageIterator(this);
		return iterator;
	}
	
	/**
	 * This method return the largest possible iterable bounds for the given AbstractUIntPixelImage as {@link Iterator2DBound}.
	 * @return Iterator2DBound
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Iterator2DBound getLargestIterableBounds(){
		try {
			return new Iterator2DBound(0,0,getHeight(),getWidth());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
}
