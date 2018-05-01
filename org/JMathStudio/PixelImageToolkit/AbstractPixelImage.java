package org.JMathStudio.PixelImageToolkit;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.io.IOException;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Generic.Index2D;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.UnSupportedImageFormatException;
import org.JMathStudio.PixelImageToolkit.PixelImage.PixelImageType;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt16PixelImage;
import org.JMathStudio.VisualToolkit.Viewer.ImageViewer;

/**
 * This is an abstract class which define the template for a {@link PixelImage}.
 * <h3>Image space to Display space transform</h3>
 * <p>
 * When the given PixelImage is displayed on a suitable Viewer, the
 * coordinate transform from Image space to Viewer/display space shall map the
 * point (0,0) of the PixelImage to the upper left corner of the Viewer
 * geometry.
 * <p>
 * These transform results in the flip of the 'Y' axis on the Viewer geometry
 * such that the positive 'Y' Image axis now points downward on the Viewer
 * geometry.
 * <p>
 * The 'X' Image axis is not affected by the coordinate transform and positive
 * 'X' Image axis continue to point towards right direction on the Viewer
 * geometry.
 * @see PixelImage
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractPixelImage {
	
	/**
	 * This abstract method return the Height of the PixelImage
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getHeight();

	/**
	 * This abstract method return the Width of the PixelImage
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract int getWidth();

	/**
	 * This abstract method will convert the given PixelImage to an appropriate
	 * BufferedImage object and return the same.
	 * <p>
	 * BufferedImage is a standard Image type for Java.
	 * 
	 * @return BufferedImage
	 * @see BufferedImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract BufferedImage toBufferedImage();

	/**
	 * This abstract method will convert the given PixelImage to a {@link Cell} and return
	 * the same.
	 * 
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract Cell toCell();

	/**
	 * This abstract method return a compatible blank PixelImage of the type and with
	 * dimension as that of the given PixelImage.
	 * 
	 * @return AbstractPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract AbstractPixelImage getEquivalentBlankImage();

	/**
	 * This abstract method return the Image Type for this PixelImage.
	 * <p>
	 * The return type will be one of the PixelImage type as define in the Enum {@link PixelImageType}.
	 * 
	 * @return {@link PixelImageType}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract PixelImageType getImageType();

	/**
	 * This abstract method return the Clone of the given PixelImage.
	 * 
	 * @return {@link AbstractPixelImage}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract AbstractPixelImage clone();

	/**
	 * This method will export the given PixelImage as an Image to an external
	 * directory path.
	 * <p>The method will convert the given PixelImage to an equivalent {@link BufferedImage}
	 * and export the same to an external image. Refer {@link #toBufferedImage()} method of a given
	 * PixelImage class for strategy on conversion of given PixelImage to BufferedImage. 
	 * <p>
	 * The argument 'path' specify the external directory path where PixelImage
	 * is to be exported. Note argument 'path' should not end with '/' or '\\'.
	 * <p>
	 * The argument 'fileName' specify the file name for exporting the given
	 * PixelImage as an external image. The argument 'fileName' should have a
	 * file name and an extension separated by a '.'. (Note : argument
	 * 'fileName' should have only one '.' to separate extension from the file
	 * name).
	 * <p>
	 * The extension of the argument 'fileName' like '.png','.gif','.jpg' will
	 * define the Image Format for exporting the PixelImage as an external Image
	 * file. Use supported Image Format which depends upon the platform. See
	 * {@link PixelImage#getExportFormatsSupported()} method for the list of
	 * Image Format supported on the current platform.
	 * <p>
	 * If the Image Format for exporting the given PixelImage is not supported
	 * by the platform this method will throw an UnSupportedImageFormat
	 * Exception.
	 * <p>
	 * If the external path as specified by the argument 'path' does not exist
	 * or if any IO related issues comes up this method will throw an
	 * IOException.
	 * 
	 * @param String
	 *            path
	 * @param String
	 *            fileName
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @see {@link PixelImage#getExportFormatsSupported()}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	//Note this method is overridden in some base class.
	public void exportImage(String path, String fileName)throws IOException, UnSupportedImageFormatException
	{
		BufferedImage image = toBufferedImage();
		PixelImage.exportBufferedImage(image, path, fileName);		
	}
	
	/**
	 * This method display the given PixelImage as an Image on a JFrame.
	 * <p>
	 * The argument 'title' specify the Title of the display JFrame.
	 * <p>The method will convert the given PixelImage to an equivalent {@link BufferedImage}
	 * and display the same on the suitable viewer. Refer {@link #toBufferedImage()} method of 
	 * a given PixelImage class for strategy on conversion of given PixelImage to BufferedImage. 
	 * 
	 * @param String
	 *            title
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	//Note this method is overridden in some base class.
	public void display (String title)
	{
		BufferedImage img = toBufferedImage();
		try{
			ImageViewer frame = new ImageViewer();
			frame.setTitle(title);
			frame.display(img, null);
			}catch(DimensionMismatchException e)
			{
				throw new BugEncounterException();
			}
	}

	/**
	 * This method will check if the row and column indexes as specified by the
	 * arguments 'row' and 'column' respectively falls within the valid bounds
	 * of this PixelImage.
	 * 
	 * @param int row
	 * @param int column
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isWithinBounds(int row, int column)	
	{
		if (row >= 0 && row < getHeight() && column >= 0 && column < getWidth())
			return true;
		else
			return false;
	}

	/**
	 * This method will check if the 2D index as specified by {@link Index2D} 'index' falls within the 
	 * valid bounds of this PixelImage.
	 * 
	 * @param Index2D index
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isWithinBounds(Index2D index)	
	{
		final int row = index.getY();
		final int column = index.getX();
		
		if (row >= 0 && row < getHeight() && column >= 0 && column < getWidth())
			return true;
		else
			return false;
	}
	
	/**
	 * This method check whether the AbstractPixelImage 'image' has dimensions similar to that
	 * of this AbstractPixelImage.
	 * @param AbstractPixelImage image
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean hasSameDimensions(AbstractPixelImage image){

		return (image.getHeight() == this.getHeight()) && (image.getWidth() == this.getWidth());
	}
	
	// Engines to parse various BufferedImage Pixels:

	protected final static short[][] f2(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		short[][] pixelData = new short[height][width];

		DataBuffer buff = img.getData().getDataBuffer();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixelData[y][x] = ((short) buff.getElem(y * width + x));
			}
		}

		return pixelData;
	}

	protected final static int[][] f1(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		int[][] pixelData = new int[height][width];

		DataBufferUShort buff = (DataBufferUShort) img.getData()
				.getDataBuffer();

		int correction = UInt16PixelImage.MAX + 1;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int tmp = buff.getElem(y * width + x);
				// Add correction to wrap up data in range of [-32768 32767] to
				// UInt16 range of [0 65535]

				if (tmp < 0)
					tmp += correction;
				pixelData[y][x] = tmp;

			}
		}

		return pixelData;
	}

	protected final static short[][][] f0(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		short[][][] pixelData = new short[4][height][width];
		short alpha,red,green,blue;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int argb = img.getRGB(x, y);

				alpha = (short) ((argb >> 24) & 0xff);
				red = (short) ((argb >> 16) & 0xff);
				green = (short) ((argb >> 8) & 0xff);
				blue = (short) (argb & 0xff);

				pixelData[0][y][x] = alpha;
				pixelData[1][y][x] = red;
				pixelData[2][y][x] = green;
				pixelData[3][y][x] = blue;
			}
		}

		return pixelData;
	}

	//Return ARGB data with Alpha 255.
	protected final static short[][][] f3(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		short[][][] pixelData = new short[4][height][width];
		short alpha,red,green,blue;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int argb = img.getRGB(x, y);

				alpha = 255;
				red = (short) ((argb >> 16) & 0xff);
				green = (short) ((argb >> 8) & 0xff);
				blue = (short) (argb & 0xff);

				pixelData[0][y][x] = alpha;
				pixelData[1][y][x] = red;
				pixelData[2][y][x] = green;
				pixelData[3][y][x] = blue;
			}
		}

		return pixelData;
	}
}
