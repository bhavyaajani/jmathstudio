package org.JMathStudio.PixelImageToolkit.ColorPixelImage;

import org.JMathStudio.PixelImageToolkit.AbstractPixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage;

/**
 * This is an abstract class which define the template for a Colour PixelImages.
 * <p>
 * This abstract class extends {@link AbstractPixelImage}, thus it define a
 * valid PixelImage.
 * <p>
 * All the Colour PixelImages like {@link RGBPixelImage} and
 * {@link ARGBPixelImage} extends this abstract base class.
 * 
 * @see PixelImage
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public abstract class AbstractColourPixelImage extends AbstractPixelImage {
	
	/**
	 * This abstract method return the number of independent bands/channels associated with given
	 * Colour PixelImage.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getNumberOfBands();	
		
}
