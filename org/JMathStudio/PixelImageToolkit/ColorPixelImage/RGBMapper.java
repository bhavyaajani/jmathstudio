package org.JMathStudio.PixelImageToolkit.ColorPixelImage;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.Numeric.Function1D.AbstractFunction1D;
import org.JMathStudio.MathToolkit.Numeric.Function1D.GaussianFunction1D;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt16PixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt8PixelImage;

/**
 * This class provide the service for mapping a Gray Scale intensity value as
 * represented by an integer to an unique pseudo 8 bits RGB colour. A RGB colour
 * will be specified by 3 Red,Green and Blue components, with each having a
 * depth of 8 bits taking value in the range of [0 255].
 * <p>
 * This class make use of 3 {@link AbstractFunction1D} mapping function respectively for
 * generating the pseudo Red, Green and Blue colour components from the given
 * Gray Scale intensity as represented by an integer value.
 * <p>
 * The integer value representing the Gray Scale intensity should be more than
 * 0. This class will not check for this condition, but still to be followed as
 * intensities cannot be negative.
 * <p>
 * This class is useful in assigning a Pseudo/False RGB colour to a Gray Scale
 * Pixel value.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class RGBMapper {

	private AbstractFunction1D redFunc;
	private AbstractFunction1D greenFunc;
	private AbstractFunction1D blueFunc;

	// Set depth for each color component, i.e. 8 bits depth implies maximum
	// value for each color
	// component will be 255 and minimum will be 0.
	// AbstractFunction may return lower or higher op value, so need to
	// truncate the op value within bound of [0 depth]
	private short depth = 255;

	/**
	 * This will create a RGBMapper with the Colour mapping functions as
	 * specified by the {@link AbstractFunction1D} 'redTrFunc', 'greenTrFunc' and
	 * 'blueTrFunc' respectively for mapping Red, Green and Blue components from
	 * a given Gray Scale intensity.
	 * <p>
	 * The Function as specified should give a normalised output in the range of
	 * [0 1] else the mapped colour components will not fall in the range of 8
	 * bits [0 255] bound and shall be thresholded leading to improper colour
	 * mapping.
	 * <p>
	 * The support or kernel of the Functions should be appropriate to the range
	 * of Gray Scale intensities for the Gray Scale image whose pixel
	 * intensities are to be mapped. i.e for 8 bits Gray Scale image, the
	 * Functions should cover the interval of [0 255].
	 * <p>All arguments are passed by reference and no deep copy of the same is made.
	 * 
	 * @param AbstractFunction1D
	 *            redTrFunc
	 * @param AbstractFunction1D
	 *            greenTrFunc
	 * @param AbstractFunction1D
	 *            blueTrFunc
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public RGBMapper(AbstractFunction1D redTrFunc, AbstractFunction1D greenTrFunc,
			AbstractFunction1D blueTrFunc) {
		this.redFunc = redTrFunc;
		this.greenFunc = greenTrFunc;
		this.blueFunc = blueTrFunc;
	}

	// /**
	// * Based on the set Mapping curves for Red,Green and Blue color
	// components,
	// * this method will return an integer representing the unique mapped color
	// * for the argument 'value'.
	// * <p>
	// * The associated color components are encoded in a 32 bits represented
	// and
	// * return as an Integer value.
	// * <p>
	// * The 32 bits of the returned Integer can be decoded as follows,
	// * <p>
	// * Last 8 (0-7) bits gives Blue color component in 0-255 range.
	// * <p>
	// * Next 8 (8-15) bits gives Green color component in 0-255 range.
	// * <p>
	// * Next 8 (16-23) bits gives Red color component in 0-255 range.
	// * <p>
	// * First 8 (24-31) bits gives Alpha color components which will be all
	// ones
	// * or 255 value.
	// *
	// * @param short value
	// * @return int
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// protected int getRGB(short value) {
	// int r = 0, g = 0, b = 0;
	//
	// r = gaussian(value, RedMean, window) & 0xff;
	// g = gaussian(value, GreenMean, window) & 0xff;
	// b = gaussian(value, BlueMean, window) & 0xff;
	//
	// return (255 << 24) | (r << 16) | (g << 8) | b;
	// }

	/**
	 * This method will return the pseudo Red component for the Gray Scale
	 * intensity as given by the argument 'value'.
	 * <p>
	 * The pseudo Red component will be obtained from the corresponding mapping
	 * function and will be scaled up to the 8 bits colour depth i.e. 255.
	 * <p>
	 * If the final pseudo colour value falls outside the range of 8 bits colour
	 * depth i.e [0 255], it shall be truncated appropriately.
	 * <p>
	 * The pseudo colour component will be return as an integer value with in
	 * the bound of 8 bits depth of [0 255].
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public short getRedComponent(int value) {
		int red = Math.round(depth * redFunc.F(value));
		return f0(red);
	}

	/**
	 * This method will return the pseudo Green component for the Gray Scale
	 * intensity as given by the argument 'value'.
	 * <p>
	 * The pseudo Green component will be obtained from the corresponding
	 * mapping function and will be scaled up to the 8 bits colour depth i.e.
	 * 255.
	 * <p>
	 * If the final pseudo colour value falls outside the range of 8 bits colour
	 * depth i.e [0 255], it shall be truncated appropriately.
	 * <p>
	 * The pseudo colour component will be return as an integer value with in
	 * the bound of 8 bits depth of [0 255].
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public short getGreenComponent(int value) {
		int green = Math.round(depth * greenFunc.F(value));
		return f0(green);
	}

	/**
	 * This method will return the pseudo Blue component for the Gray Scale
	 * intensity as given by the argument 'value'.
	 * <p>
	 * The pseudo Blue component will be obtained from the corresponding mapping
	 * function and will be scaled up to the 8 bits colour depth i.e. 255.
	 * <p>
	 * If the final pseudo colour value falls outside the range of 8 bits colour
	 * depth i.e [0 255], it shall be truncated appropriately.
	 * <p>
	 * The pseudo colour component will be return as an integer value with in
	 * the bound of 8 bits depth of [0 255].
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public short getBlueComponent(int value) {
		int blue = Math.round(depth * blueFunc.F(value));
		return f0(blue);
	}

	private short f0(int value) {
		if (value > depth)
			return depth;
		else if (value < 0)
			return 0;
		else
			return (short) value;
	}

	/**
	 * This will return a default RGBMapper for converting an UInt8 Gray Scale
	 * PixelImage to Pseudo RGBPixelImage.
	 * 
	 * @return RGBMapper
	 * @see UInt8PixelImage#mapToRGBPixelImage(RGBMapper)
	 */
	public final static RGBMapper UInt8Mapper() {
		float mean = UInt8PixelImage.MAX;
		float window = UInt8PixelImage.MAX / 3.0f;
		try {
			AbstractFunction1D redFunc = new GaussianFunction1D(mean, window);
			AbstractFunction1D greenFunc = new GaussianFunction1D(mean / 2, window);
			AbstractFunction1D blueFunc = new GaussianFunction1D(0.0f, window);

			return new RGBMapper(redFunc, greenFunc, blueFunc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will return a default RGBMapper for converting an UInt16 Gray Scale
	 * PixelImage to Pseudo RGBPixelImage.
	 * 
	 * @return RGBMapper
	 * @see UInt16PixelImage#mapToRGBPixelImage(RGBMapper)
	 */
	public final static RGBMapper UInt16Mapper() {
		float mean = UInt16PixelImage.MAX;
		float window = UInt16PixelImage.MAX / 3.0f;
		try {
			AbstractFunction1D redFunc = new GaussianFunction1D(mean, window);
			AbstractFunction1D greenFunc = new GaussianFunction1D(mean / 2, window);
			AbstractFunction1D blueFunc = new GaussianFunction1D(0.0f, window);

			return new RGBMapper(redFunc, greenFunc, blueFunc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

}
