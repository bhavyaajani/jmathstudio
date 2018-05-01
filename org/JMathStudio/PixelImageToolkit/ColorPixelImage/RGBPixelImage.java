package org.JMathStudio.PixelImageToolkit.ColorPixelImage;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.PixelImageFormatException;
import org.JMathStudio.Exceptions.UnSupportedImageFormatException;
import org.JMathStudio.PixelImageToolkit.AbstractPixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage.PixelImageType;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt16PixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt8PixelImage;

/**
 * This class represents a RGB PixelImage and extends {@link AbstractColourPixelImage}. 
 * See {@link PixelImage} for further information on RGB PixelImage type.
 * <p>
 * A RGBPixelImage is a true colour image with RGB colour model. Colour at each
 * pixel location is represented by 3 distinct 8 bits Red, Green and Blue colour
 * components for that pixel location.
 * <p>
 * Thus RGB PixelImage supports 3 distinct Red, Green and Blue colour bands.
 * <p>Each band is represented by an {@link UInt8PixelImage} internally.
 * <p>
 * Indexing: Row index -> [0 height of image -1], Column index -> [0 width of
 * image -1]
 * <pre>Usage:
 * RGBPixelImage img = RGBPixelImage.importImage("image_path");//Import external image as an
 * equivalent RGBPixelImage.
 * 
 * UInt8PixelImage red = img.accessRedBand();//Access red color band for given RGBPixelImage.
 * 
 * Cell cell = img.toCell();//Convert given RGBPixelImage to a Cell object.
 * 
 * img.exportImage("path", "fileName");//Export given RGBPixelImage to an external image file.
 * </pre>
 * @see ARGBPixelImage
 * @see PixelImage
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class RGBPixelImage extends AbstractColourPixelImage {

	//private short[][][] RGB;
	private UInt8PixelImage i2;
	private UInt8PixelImage i4;
	private UInt8PixelImage i0;
	
	private final short i1 = 3;

	/**
	 * This will create a RGBPixelImage of height and width as given by the
	 * argument 'height' and 'width' respectively.
	 * <p>
	 * The argument 'height' and 'width' should be more than '0' else this
	 * method will throw an IllegalArgument Exception.
	 * <p>RGBPixelImage will be initialised to a default blank image with all colour bands
	 * having 0 value for all pixel positions. 
	 *  
	 * @param int height
	 * @param int width
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public RGBPixelImage(int height, int width) throws IllegalArgumentException {
		if (height <= 0 || width <= 0)
			throw new IllegalArgumentException();
		else {
						
			i2 = new UInt8PixelImage(height,width);
			i4 = new UInt8PixelImage(height,width);
			i0 = new UInt8PixelImage(height,width);
		}
	}

	/**
	 * This will create a RGB PixelImage with red, green and blue bands as represented by the 
	 * {@link UInt8PixelImage} 'red', 'green' and 'blue' respectively.
	 * <p>The arguments are taken by reference and no internally copy of the same is made.
	 * <p>Ensure that the dimensions of all the input {@link UInt8PixelImage} are same else this method
	 * will throw a DimensionMismatch Exception. The dimension of the RGB PixelImage shall be similar 
	 * to that of the input UInt8 PixelImages.
	 * @param UInt8PixelImage red
	 * @param UInt8PixelImage green
	 * @param UInt8PixelImage blue
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public RGBPixelImage(UInt8PixelImage red, UInt8PixelImage green, UInt8PixelImage blue) throws DimensionMismatchException
	{
		if(red == null || green == null || blue == null)
			throw new NullPointerException();
		
		int rh = red.getHeight();
		int rw = red.getWidth();
		int gh = green.getHeight();
		int gw = green.getWidth();
		int bh = blue.getHeight();
		int bw = blue.getWidth();
		
		if(rh != gh || rh != bh)
			throw new DimensionMismatchException();
		if(rw != gw || rw != bw)
			throw new DimensionMismatchException();
		
		this.i2 = red;
		this.i4 = green;
		this.i0 = blue;
	}

	/**
	 * This method will convert the given RGBPixelImage object to an equivalent {@link BufferedImage}
	 * object of type "TYPE_INT_RGB" and return the same. 
	 * <p>BufferedImage is a standard Image type for Java.
	 * @return BufferedImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BufferedImage toBufferedImage() {
		int h = getHeight();
		int w = getWidth();
		
		BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
			
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				image.setRGB(x, y, f7(255, f3(y,x),f5(y,x), f0(y,x)));
			}
		}

		return image;
	}

	/**
	 * This method return the height of the given RGBPixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getHeight() {
		return this.i2.getHeight();
	}

	/**
	 * This method return the width of the given RGBPixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getWidth() {
		return this.i0.getWidth();
	}

	/**
	 * This method will obtain the single band 8 bits gray scale representation of this multiple band RGBPixelImage 
	 * and return the same as a {@link Cell} object. 
	 * <p>The return Cell object will be of the same dimension as that of this image
	 * and will contain the pixel data for the gray scale representation of this RGB PixelImage.
	 * <p>A gray scale representation or conversion of a RGB image is based on following rule,
	 * <p><i> gray = 0.3 *Red + 0.59*Green + 0.11*Blue.</i>
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() {
		int h = getHeight();
		int w = getWidth();
		
		Cell cell = new Cell(h,w);
		
		float value;
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				value = Math.round(0.3f*f3(i,j) + 0.59f*f5(i,j) + 0.11f*f0(i,j));
				cell.setElement(value, i, j);
			}
		}

		return cell;
	}

	/**
	 * This method will obtain the single band 8 bits gray scale representation of this multiple band RGB PixelImage 
	 * and return the same as UInt8 PixelImage.
	 * <p>A gray scale conversion of this RGB PixelImage to an UInt8 PixelImage is based
	 * on following conversion rule,
	 * <p><i>gray = 0.3 *Red + 0.59*Green + 0.11*Blue </i>
	 * <p>The return {@link UInt8PixelImage} will have the similar dimensions as that of this image.
	 * 
	 * @return UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage toUInt8PixelImage() {

		int h = getHeight();
		int w = getWidth();
		
		UInt8PixelImage img;

		try {
			img = new UInt8PixelImage(h, w);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		
		int value;

		try {
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					value = Math.round(0.3f * f3(i,j) + 0.59f * f5(i,j) + 0.11f * f0(i,j));
					img.setPixel(value, i, j);
				}
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return img;
	}

	/**
	 * This method will obtain the single band 16 bits gray scale representation of this multiple band RGB PixelImage 
	 * and return the same as UInt16 PixelImage.
	 * <p>A gray scale conversion of this RGB PixelImage to an UInt16 PixelImage is based
	 * on following conversion rule,
	 * <p><i>gray = slope*(0.3 *Red + 0.59*Green + 0.11*Blue)
	 * <p>where slope = (UInt16 MAX/UInt8 MAX). </i>
	 * <p>The return {@link UInt16PixelImage} will have the same dimensions.
	 * 
	 * @return UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt16PixelImage toUInt16PixelImage() {

		UInt16PixelImage img;
		int h = getHeight();
		int w = getWidth();
		
		try {
			img = new UInt16PixelImage(h, w);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		
		int value;
		
		float slope = ((float) UInt16PixelImage.MAX) / ((float)UInt8PixelImage.MAX);
		
		try {
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					value = Math.round(slope*(0.3f*f3(i,j) + 0.59f*f5(i,j) + 0.11f*f0(i,j)));
					img.setPixel(value, i, j);
				}
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return img;

	}

	/**
	 * This method return the clone of the given RGBPixelImage object.
	 * 
	 * @return RGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public RGBPixelImage clone() {
		
		UInt8PixelImage r = i2.clone();
		UInt8PixelImage g = i4.clone();
		UInt8PixelImage b = i0.clone();
		
		try {
			return new RGBPixelImage(r,g,b);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method return the Image Type for this RGBPixelImage.
	 * <p>
	 * The return type will be one of the PixelImage type as define in the Enum {@link PixelImageType}.
	 * 
	 * @return {@link PixelImageType}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelImageType getImageType() {
		return PixelImageType.RGB;
	}

	/**
	 * This method return a compatible blank RGB PixelImage which has similar dimension as that of the given
	 * image. Further, the blank image contain 0's for all bands for all pixels. 
	 * @return AbstractPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractPixelImage getEquivalentBlankImage() {
		try {
			return new RGBPixelImage(getHeight(), getWidth());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will import an external image as specified by the argument
	 * 'path', as a {@link RGBPixelImage} and return the same.
	 * <p>
	 * This method uses the ImageIO framework to load the external image first
	 * as {@link BufferedImage} and subsequently transform the same to an
	 * {@link RGBPixelImage}.
	 * <p>
	 * Based upon the type of BufferedImage so imported, a suitable transform is
	 * applied to convert the same to an equivalent RGB PixelImage.
	 * <p>
	 * The external image file to be imported should have a valid platform
	 * supported Image Format (like 'PNG', 'JPEG','GIFF' etc) else this method
	 * will throw an UnSupportedImageFormat Exception. Supported Image Format
	 * depends upon the platform.
	 * <p>
	 * See {@link PixelImage#getImportFormatsSupported()} for the list of
	 * platform supported Image Formats.
	 * <p>
	 * If this method encounters any IO error an IO Exception will be thrown.
	 * 
	 * @param String
	 *            path
	 * @return RGBPixelImage
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static RGBPixelImage importImage(String path) throws IOException,
			UnSupportedImageFormatException {
		
		BufferedImage image = PixelImage.importAsBufferedImage(path);
		return RGBPixelImage.toRGBPixelImage(image);
	}

	/**
	 * This method will transform the {@link BufferedImage} 'image' to a
	 * {@link RGBPixelImage} and return the same.
	 * <p>
	 * Based upon the type of {@link BufferedImage} one of the following
	 * conversion rule is applied :
	 * <p>
	 * <i>BufferedImage.TYPE_BYTE_GRAY</i> : Such a BufferedImage is comparable
	 * to an UInt8 PixelImage and thus directly transformed first into an UInt8
	 * PixelImage which is subsequently converted to an equivalent RGB
	 * PixelImage.
	 * <p>
	 * <i>BufferedImage.TYPE_USHORT_GRAY</i> : Such a BufferedImage is
	 * comparable to an UInt16 PixelImage and thus directly transformed first to
	 * an UInt16 PixelImage which is subsequently converted to an equivalent RGB
	 * PixelImage.
	 * <p>
	 * <i>BufferedImage.TYPE_INT_RGB</i> : Such a BufferedImage is comparable to
	 * a RGB PixelImage and thus directly transformed into a RGB PixelImage.
	 * <p>
	 * <i>Default Any other type</i> : Such a BufferedImage is changed first to
	 * an BufferedImage of type "TYPE_INT_ARGB", which is comparable to an ARGB
	 * PixelImage and thus directly transformed to a RGB PixelImage by excluding the
	 * alpha band.
	 * 
	 * @param BufferedImage
	 *            image
	 * @return RGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static RGBPixelImage toRGBPixelImage(BufferedImage image) {
		int imageType = image.getType();

		try {
			if (imageType == BufferedImage.TYPE_BYTE_GRAY) {
				short data[][] = f2(image);
				return new UInt8PixelImage(data).toRGBPixelImage();

			} else if (imageType == BufferedImage.TYPE_USHORT_GRAY) {
				int data[][] = f1(image);
				return new UInt16PixelImage(data).toRGBPixelImage();

			} else {
				short data[][][] = f3(image);
				
				UInt8PixelImage r = new UInt8PixelImage(data[1]);
				UInt8PixelImage g = new UInt8PixelImage(data[2]);
				UInt8PixelImage b = new UInt8PixelImage(data[3]);
				
				return new RGBPixelImage(r,g,b);
			}
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will return the number of independent bands associated with the given
	 * RGB PixelImage.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getNumberOfBands() {
		return i1;
	}

	/**
	 * This method provides access to the Red band of the given RGB PixelImage which is
	 * internally represented by an {@link UInt8PixelImage}. 
	 * @return {@link UInt8PixelImage}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage accessRedBand()
	{
		return this.i2;
	}
	
	/**
	 * This method provides access to the Green band of the given RGB PixelImage which is
	 * internally represented by an {@link UInt8PixelImage}. 
	 * @return {@link UInt8PixelImage}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage accessGreenBand()
	{
		return this.i4;
	}
	
	/**
	 * This method provides access to the Blue band of the given RGB PixelImage which is
	 * internally represented by an {@link UInt8PixelImage}. 
	 * @return {@link UInt8PixelImage}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage accessBlueBand()
	{
		return this.i0;
	}
	
	/**
	 * This method replaces the Red band of the given RGB PixelImage with the new band as
	 * represented by the argument 'red'.
	 * <p>If the dimensions of the {@link UInt8PixelImage} 'red' does not match the dimensions
	 * of the given RGB PixelImage an DimensionMismatch Exception will be thrown.
	 * @param UInt8PixelImage red
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignRedBand(UInt8PixelImage red) throws DimensionMismatchException
	{
		if(red.getHeight() != getHeight() || red.getWidth() != getWidth())
			throw new DimensionMismatchException();
		else
			this.i2 = red;
	}
	
	/**
	 * This method replaces the Green band of the given RGB PixelImage with the new band as
	 * represented by the argument 'green'.
	 * <p>If the dimensions of the {@link UInt8PixelImage} 'green' does not match the dimensions
	 * of the given RGB PixelImage an DimensionMismatch Exception will be thrown.
	 * @param UInt8PixelImage green
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignGreenBand(UInt8PixelImage green) throws DimensionMismatchException
	{
		if(green.getHeight() != getHeight() || green.getWidth() != getWidth())
			throw new DimensionMismatchException();
		else
			this.i4 = green;
	}
	
	/**
	 * This method replaces the Blue band of the given RGB PixelImage with the new band as
	 * represented by the argument 'blue'.
	 * <p>If the dimensions of the {@link UInt8PixelImage} 'blue' does not match the dimensions
	 * of the given RGB PixelImage an DimensionMismatch Exception will be thrown.
	 * @param UInt8PixelImage blue
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignBlueBand(UInt8PixelImage blue) throws DimensionMismatchException
	{
		if(blue.getHeight() != getHeight() || blue.getWidth() != getWidth())
			throw new DimensionMismatchException();
		else
			this.i0 = blue;
	}
	
	private short f3(int i,int j){
		return (short) this.i2.getPixel(i, j);
	}
	
	private short f5(int i,int j){
		return (short) this.i4.getPixel(i, j);
	}
	
	private short f0(int i,int j){
		return (short) this.i0.getPixel(i, j);
	}
	
	private int f7(int a, int r, int g, int b) {
		return ((a << 24) | (r << 16) | (g << 8) | b);
	}
}
