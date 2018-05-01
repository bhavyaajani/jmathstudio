package org.JMathStudio.PixelImageToolkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.JMathStudio.Exceptions.UnSupportedImageFormatException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.ARGBPixelImage;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.AbstractColourPixelImage;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.CustomUIntPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt16PixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt8PixelImage;

/**
 * <p>
 * A PixelImage is a standard Image class for this package. PixelImage is
 * defined by an {@link AbstractPixelImage} class which define the core attributes of a
 * PixelImage and different derived PixelImages (each representing a family of
 * Image Type) extends from it.
 * <p>
 * PixelImage has following derived Image Types defined, classified in 3 broad
 * family as,
 * <p>
 * <h3>Colour PixelImages</h3>
 * <p>
 * This PixelImages represents True Colour images with different Colour Model.
 * This PixelImages extends {@link AbstractColourPixelImage} which define the
 * common attributes for this family of PixelImages. AbstractColourPixelImage in
 * turn extends {@link AbstractPixelImage}, thus it define a PixelImage.
 * <p>
 * Following are the True Colour PixelImages available in this family:
 * <p>
 * ARGB Image Type
 * <p>
 * <i>ARGB Image is a true colour image with ARGB colour model. This colour
 * image has Alpha, Red, Green and Blue colour components for each pixel
 * position. Thus a true colour is represented as per the ARGB colour model.
 * Here each colour component has a depth of 8 bits represented by an integer in
 * the range of [0 255]. {@link ARGBPixelImage} object represents this type of
 * PixelImage. </i>
 * <p>
 * RGB Image Type
 * <p>
 * <i>RGB Image is a true colour image with RGB colour model. This colour images
 * has Red, Green and Blue colour components for each pixel location. Thus a
 * true colour is represented as per the RGB colour model. Here each colour
 * component has a depth of 8 bits represented by an integer in the range of [0
 * 255]. {@link RGBPixelImage} object represents this type of PixelImage. </i>
 * <p>
 * <h3>Gray Scale UInt PixelImages</h3>
 * <p>
 * This PixelImage family represents Gray Scale Intensity images with different
 * depth. This PixelImages extends {@link AbstractUIntPixelImage} which define
 * the common attributes for this family of PixelImages. AbstractUIntPixelImage
 * in turn extends {@link AbstractPixelImage}, thus it define a PixelImage.
 * <p>
 * Note UInt16 and UInt8 PixelImages are the standard UInt PixelImages for this
 * package. Custom UInt PixelImages are not the standard type and their use is
 * to be limited.
 * <p>
 * UInt16 Image Type
 * <p>
 * <i>UInt16 Image is a monochrome image with 16 bits of depth i.e. supporting
 * 65536 level of shades of Gray. Thus each pixel value of this image specify a
 * unique shade of Gray. The pixel values of this image should be in the range
 * of [0 65535], where '0' encodes Black and '65535' encodes White and
 * intermittent values encoding different shades of Gray in increasing order of
 * whiteness. {@link UInt16PixelImage} object represents this type of
 * PixelImage. </i>
 * <p>
 * UInt8 Image Type
 * <p>
 * <i>UInt8 Image is a monochrome image with 8 bits of depth i.e. supporting 256
 * level of shades of Gray. Thus each pixel value of this image specify a unique
 * shade of Gray. The pixel values of this image should be in the range of [0
 * 255], where '0' encodes Black and '255' encodes White and intermittent values
 * encoding different shades of Gray in increasing order of whiteness.
 * {@link UInt8PixelImage} object represents this type of PixelImage. </i>
 * <p>
 * Custom UInt Image Type
 * <p>
 * <i>Custom UInt Image is a monochrome image with customisable bits of depth in
 * the range of [0 16]. Thus this image support 2^N level of shades of Gray with
 * 'N' depth. Thus each pixel value of this image specify a unique shade of Gray
 * in the range of [0 2^N -15], where '0' encodes Black and '2^N -1' encodes
 * White and intermittent values encoding different shades of Gray in increasing
 * order of whiteness. {@link CustomUIntPixelImage} object represents this type
 * of PixelImage. </i>
 * <p>
 * <h3>Binary Image Type</h3>
 * <p>
 * <i>Binary Image is a boolean image with 1 bits of depth i.e. supporting only
 * two shades of gray level, 1 - White and 0 - Black. Thus White pixels with
 * value '1' denote foreground and Black pixels with value '0' denote background
 * of the BinaryPixelImage. {@link BinaryPixelImage} object represents this type
 * of PixelImage. </i>
 * 
 * @see ARGBPixelImage
 * @see RGBPixelImage
 * @see UInt16PixelImage
 * @see UInt8PixelImage
 * @see CustomUIntPixelImage
 * @see BinaryPixelImage
 * @see AbstractPixelImage
 * @see AbstractUIntPixelImage
 * @see AbstractColourPixelImage
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class PixelImage {

	//Ensure no instances are made for utility classes.
	private PixelImage(){}
	
	/**
	 * This enumeration list out all the defined PixelImage type.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public static enum PixelImageType {UInt8,UInt16,CustomUInt,Binary,RGB,ARGB};
	
//	public final static String UInt8 = "UInt8PixelImage";
//	public final static String UInt16 = "UInt16PixelImage";
//	public final static String CustomUInt = "CustomUInt";
//
//	public final static String Binary = "BinaryPixelImage";
//	public final static String RGB = "RGBPixelImage";
//	public final static String ARGB = "ARGBPixelImage";

	/**
	 * This method return the Image Format supported for Export operation on the
	 * PixelImage for the given platform.
	 * <p>
	 * All the supported Image Format for export operation will be return as a
	 * String array.
	 * 
	 * @return String[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static String[] getExportFormatsSupported() {
		return ImageIO.getWriterFormatNames();
	}

	/**
	 * This method return the Image Format supported for importing an external
	 * image as PixelImage for the given platform.
	 * <p>
	 * All the supported Image Format for import operation will be return in a
	 * String array.
	 * 
	 * @return String[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static String[] getImportFormatsSupported() {
		return ImageIO.getReaderFormatNames();
	}

	/**
	 * This method will export the given BufferedImage 'image' as an Image to an
	 * external directory path.
	 * <p>
	 * The argument 'path' specify the external directory path where
	 * BufferedImage is to be exported. Note argument 'path' should not end with
	 * '/' or '\\'.
	 * <p>
	 * The argument 'fileName' specify the file name for exporting the given
	 * BufferedImage as an external image. The argument 'fileName' should have a
	 * file name and an extension separated by a '.'. (Note : argument
	 * 'fileName' should have only one '.' to separate extension from the file
	 * name).
	 * <p>
	 * The extension of the argument 'fileName' like '.png','.gif','.jpg' will
	 * define the Image Format for exporting the BufferedImage as an external
	 * Image file. Use supported Image Format which depends upon the platform.
	 * See {@link #getExportFormatsSupported()} method for the list of Image
	 * Format supported on the current platform.
	 * <p>
	 * If the Image Format for exporting the given BufferedImage is not
	 * supported by the platform this method will throw an
	 * UnSupportedImageFormat Exception.
	 * <p>
	 * If the external path as specified by the argument 'path' does not exist
	 * or if any IO related issues comes up this method will throw an
	 * IOException.
	 * 
	 * @param BufferedImage
	 *            image
	 * @param String
	 *            path
	 * @param String
	 *            fileName
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @see BufferedImage
	 * @see {@link #getExportFormatsSupported()}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static void exportBufferedImage(BufferedImage image,
			String path, String fileName) throws IOException,
			UnSupportedImageFormatException {

		// Improvement required to catch null pointer exception, illegal path
		// condition,
		// and if proper filename format is not followed.
		String format = fileName.substring(fileName.indexOf('.') + 1);
		path = path + "\\" + fileName;
		File outputFile = new File(path);

		try{
		boolean check = ImageIO.write(image, format, outputFile);
		if (!check) {
			throw new UnSupportedImageFormatException();
		}
		}catch(IllegalArgumentException e)
		{
			throw new NullPointerException();
		}
		
	}

	/**
	 * This method will import an external image as specified by the argument
	 * 'path' and return the same as a BufferedImage.
	 * <p>
	 * The external image file to be imported as BufferedImage should have a
	 * valid supported Image Format (like 'PNG', 'JPEG','GIFF' etc) else this
	 * method will throw an UnSupportedImageFormat Exception. Supported Image
	 * Formats depends upon the current platform.
	 * <p>
	 * See {@link #getImportFormatsSupported()} for the list of supported Image
	 * Formats for the current platform.
	 * <p>
	 * This method will throw an IO Exception if it encounters any IO Error.
	 * 
	 * @param String
	 *            path
	 * @return BufferedImage
	 * @see BufferedImage
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BufferedImage importAsBufferedImage(String path)
			throws IOException, UnSupportedImageFormatException {
		File file = new File(path);
		try {
			BufferedImage img = ImageIO.read(file);

			if (img == null)
				throw new UnSupportedImageFormatException();
			else
				return img;
		} catch (IIOException e) {
			throw new IOException();
		}
	}

}