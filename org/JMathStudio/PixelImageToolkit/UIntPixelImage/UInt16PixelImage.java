package org.JMathStudio.PixelImageToolkit.UIntPixelImage;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.PixelImageFormatException;
import org.JMathStudio.Exceptions.UnSupportedImageFormatException;
import org.JMathStudio.PixelImageToolkit.AbstractPixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.ARGBPixelImage;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBMapper;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBPixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage.PixelImageType;

/**
 * This class represents an UInt16 PixelImage and extends {@link AbstractUIntPixelImage}. 
 * See {@link PixelImage} for further information on UInt16 PixelImage type.
 * <p>
 * An UInt16 PixelImage belong to the UInt PixelImage family. Pixel intensities
 * of an UInt16 PixelImage is represented by a 2D integer pixel array
 * representing different shades of gray with 16 bits depth for each pixel
 * position.
 * <p>
 * Thus pixel values of this UInt PixelImage takes value in the range of [0
 * 65535] corresponding to 65536 different shades of gray.
 * <p>
 * This UInt PixelImage will be represented as an {@link AbstractUIntPixelImage}
 * which define generic characteristics of an UInt PixelImages, to all the
 * Transforms and Operations defined on UInt PixelImages family so as to make
 * those generic.
 * <p>
 * Indexing: Row index -> [0 height of image -1], Column index -> [0 width of
 * image -1]
 * 
 * <pre>Usage:
 * UInt16PixelImage img = UInt16PixelImage.importImage("image_path");//Import external image
 * as an equivalent UInt16PixelImage.
 * 
 * img.display("Title");//Display given UInt16PixelImage on the viewer.
 * 
 * Let 'map' be a valid {@link RGBMapper}.
 * RGBPixelImage rgb_img = img.mapToRGBPixelImage(map);//Map given UInt16PixelImage to a
 * RGBPixelImage using specified RGBMapper or LUT.
 * 
 * int pixel = img.getPixel(i, j);//Get pixel intensity at specified image location within
 * given UInt16PixelImage.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class UInt16PixelImage extends AbstractUIntPixelImage {

	public final static int MAX = 65535;
	public final static int MIN = 0;

	private final int i4 = 16;
	private final int i1 = 65536;

	private int[][] i0;

	/**
	 * This will create an UInt16PixelImage of Height and Width as given by the
	 * argument 'height' and 'width' respectively.
	 * <p>
	 * The argument 'height' and 'width' should be more than '0' else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * UInt16PixelImage will be initialised to a black image with all the pixel
	 * values having the default value of 0 representing the black.
	 * 
	 * @param int height
	 * @param intwidth
	 * @see PixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt16PixelImage(int height, int width)
	throws IllegalArgumentException {
		if (height <= 0 || width <= 0)
			throw new IllegalArgumentException();
		else {
			i0 = new int[height][width];
		}
	}

	/**
	 * This will create an UInt16PixelImage and populate the same with the pixel
	 * values as given by the integer 2D array 'pixelData'.
	 * <p>
	 * The integer 2D array 'pixelData' should have a valid UInt16 integer array
	 * format else this method will throw a PixelImageFormat Exception. See
	 * {@link #isUInt16Data(int[][])} method for further clarification on UInt16
	 * integer array requirements.
	 * <p>The argument 'pixelData' is passed by reference and no deep copy of the same
	 * is made.
	 * 
	 * @param int[][] pixelData
	 * @throws PixelImageFormatException
	 * @see {@link #isUInt16Data(int[][])}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt16PixelImage(int[][] pixelData) throws PixelImageFormatException {
		if (!UInt16PixelImage.isUInt16Data(pixelData))
			throw new PixelImageFormatException();
		else {
			this.i0 = pixelData;
		}
	}

	/**
	 * This method will convert the given UInt16PixelImage object to an
	 * appropriate BufferedImage object and return the same.
	 * <p>
	 * The UInt16PixelImage will be converted to a BufferedImage of type
	 * "TYPE_USHORT_GRAY".
	 * <p>
	 * BufferedImage is a standard Image type for Java.
	 * 
	 * @return BufferedImage
	 * @see BufferedImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BufferedImage toBufferedImage() {

		int width = getWidth();
		int height = getHeight();

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_USHORT_GRAY);

		short[][] img = new short[1][width * height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Cast to short to convert [0 65535] data to range of [-32768
				// 32767].
				// BufferedImage type USHORT_GRAY take this range of short data
				// for 16 bits.
				// Donot round off this. Cast to short is must.
				img[0][i * width + j] = (short) (i0[i][j]);
			}
		}
		DataBuffer buff = new DataBufferUShort(img, 1);
		SampleModel model = image.getSampleModel();
		WritableRaster raster = Raster.createWritableRaster(model, buff,
				new Point(0, 0));
		image.setData(raster);

		return image;
	}

	/**
	 * This method return the Height of the given UInt16PixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getHeight() {
		return this.i0.length;
	}

	/**
	 * This method return the Width of the given UInt16PixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getWidth() {
		return this.i0[0].length;
	}

	/**
	 * This method will convert the given UInt16PixelImage to a Cell object and
	 * return the same.
	 * <p>
	 * The return Cell object will be of the same dimension as that of this
	 * image and will contain the corresponding pixel data of this
	 * UInt16PixelImage as a float elements.
	 * 
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() {

		Cell cell = new Cell(getHeight(), getWidth());

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				cell.setElement(i0[i][j], i, j);
			}
		}

		return cell;
	}

	//	/**
	//	 * This method will export the given UInt16PixelImage as an Image to an
	//	 * external directory path.
	//	 * <p>
	//	 * The argument 'path' specify the external directory path where
	//	 * UInt16PixelImage is to be exported. Note argument 'path' should not end
	//	 * with '/' or '\\'.
	//	 * <p>
	//	 * The argument 'fileName' specify the file name for exporting the given
	//	 * UInt16PixelImage as an external image. The argument 'fileName' should
	//	 * have a file name and an extension separated by a '.'. (Note : argument
	//	 * 'fileName' should have only one '.' to separate extension from the file
	//	 * name).
	//	 * <p>
	//	 * The extension of the argument 'fileName' like '.png','.gif','.jpg' will
	//	 * define the Image Format for exporting the UInt16PixelImage as an external
	//	 * Image file. Use supported Image Format which depends upon the platform.
	//	 * See {@link PixelImage#getExportFormatsSupported()} method for the list of
	//	 * Image Format supported on the current platform.
	//	 * <p>
	//	 * If the Image Format for exporting the given UInt16PixelImage is not
	//	 * supported by the platform this method will throw an
	//	 * UnSupportedImageFormat Exception.
	//	 * <p>
	//	 * If the external path as specified by the argument 'path' does not exist
	//	 * or if any IO related issues comes up this method will throw an
	//	 * IOException.
	//	 * 
	//	 * @param String
	//	 *            path
	//	 * @param String
	//	 *            fileName
	//	 * @throws IOException
	//	 * @throws UnSupportedImageFormatException
	//	 * @see {@link PixelImage#getExportFormatsSupported()}
	//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	//	 */
	//	public void exportImage(String path, String fileName) throws IOException,
	//			UnSupportedImageFormatException {
	//		BufferedImage image = toBufferedImage();
	//		PixelImage.exportBufferedImage(image, path, fileName);	
	//	}

	/**
	 * This method will return the Pixel value located at the pixel position as
	 * identified by its row and column index which is given by the arguments
	 * 'row' and 'column' respectively.
	 * <p>
	 * The argument 'row' and 'column' should be within the bound of [0 height
	 * of image-1] and [0 width of image -1] respectively else this method will
	 * throw an ArrayIndexOutOfBounds Exception.
	 * <p>
	 * The pixel value at that pixel location will be return as an integer value
	 * in the range of [0 65535] representing different shades of gray from
	 * Black to White.
	 * 
	 * @param int row
	 * @param int column
	 * @return int
	 * @see #isUInt16Data(int[][])
	 * @throws IllegalArgumentException
	 * @throws {@link ArrayIndexOutOfBoundsException}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getPixel(int row, int column) {
		return this.i0[row][column];
	}

	/**
	 * This method will set the Pixel value as given by the argument 'value' for
	 * the pixel location as identified by its row and column index which is
	 * given by the arguments 'row' and 'column' respectively.
	 * <p>
	 * The argument 'row' and 'column' should be within the bound of [0 height
	 * of image-1] and [0 width of image -1] respectively else this method will
	 * throw an ArrayIndexOutOfBounds Exception.
	 * <p>
	 * As UInt16PixelImage pixels can represents 65536 different shades of
	 * gray's corresponding to the values 0 to 65535 from black to white, the
	 * argument 'value' should be within this range of [0 65535] else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * See {@link #getMaxPixel()} and {@link #getMinPixel()} for the range of
	 * valid pixel intensities for this UInt16 PixelImage.
	 * 
	 * @param int value
	 * @param int row
	 * @param int column
	 * @throws IllegalArgumentException
	 * @throws {@link ArrayIndexOutOfBoundsException}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setPixel(int value, int row, int column)
	throws IllegalArgumentException {
		if (!f4(value))
			throw new IllegalArgumentException();
		else
			this.i0[row][column] = value;
	}

	//	/**
	//	 * This method will return the integer 2D array representing the pixel data
	//	 * buffer for this UInt16PixelImage. The height and width of the return 2D
	//	 * array will be similar to the dimensions of this UInt16 PixelImage.
	//	 * <p>
	//	 * The return array will follow UInt16 integer array format.
	//	 * 
	//	 * @return int[][]
	//	 * @see #isUInt16Data(int[][])
	//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	//	 */
	//	public int[][] accessPixelDataBuffer() {
	//		return this.pixelData;
	//	}

	/**
	 * This method will check whether the 2D integer array 'uint16' follows a
	 * valid UInt16 integer array format.
	 * <p>
	 * <h4>UInt16 integer array format definition</h4>
	 * <p>
	 * A 2D integer array 'uint16[][]' should satisfy following conditions to be
	 * a valid UInt16 integer array format.
	 * <p>
	 * (a) All the rows of the 2D integer array i.e. uint16[x] should be of
	 * equal length.
	 * <p>
	 * (b) All the elements of the 'uint16' array should be an integer in the
	 * range of [0 65535].
	 * <p>
	 * (c) uint16.length and uint16[x].length, gives respectively the height and
	 * the width of the the uint16 Pixel Image, where 'x' is any row of that 2D
	 * array.
	 * 
	 * @param int[][] uint16
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static boolean isUInt16Data(int[][] uint16) {
		for (int i = 0; i < uint16.length; i++) {
			if (uint16[i].length != uint16[0].length)
				return false;

			for (int j = 0; j < uint16[i].length; j++) {
				if (uint16[i][j] < MIN | uint16[i][j] > MAX)
					return false;
			}
		}

		return true;
	}

	/**
	 * This method will convert the Cell 'cell' object to an UInt16PixelImage
	 * and return the same.
	 * <p>
	 * This row count and column count of the 'cell' will be the Height and
	 * Width of the resultant image.
	 * <p>
	 * This method will linearly map the corresponding elements of the 'cell' to
	 * an integer in the range of [0 65535] which will be the pixel value for
	 * that pixel location in the resultant UInt16PixelImage. The minimum
	 * element of the 'cell' will be mapped to black i.e. '0' pixel value and
	 * maximum element of the 'cell' will be mapped to white i.e. '65535' pixel
	 * value in the resultant UInt16PixelImage.
	 * <p>
	 * As this method maps the element of the 'cell' to an integer in the range
	 * of [0 65535] representing a unique shade of gray for rendering the image,
	 * the pixel data of the resultant UInt16PixelImage will not reflect the
	 * original pixel data as represented by the Cell 'cell'.
	 * <p>
	 * This method should be employed to store or render the given 'cell' as an
	 * UInt16PixelImage.
	 * 
	 * @param Cell
	 *            cell
	 * @return UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static UInt16PixelImage toUInt16PixelImage(Cell cell) {

		UInt16PixelImage img;

		try {
			img = new UInt16PixelImage(cell.getRowCount(), cell.getColCount());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				float value = cell.getElement(i, j);

				if (value > max)
					max = value;
				if (value < min)
					min = value;
			}
		}

		try {
			if (max == min) {
				for (int i = 0; i < img.getHeight(); i++) {
					for (int j = 0; j < img.getWidth(); j++) {
						img.setPixel(MAX, i, j);

					}
				}
			} else {
				float slope = MAX / (max - min);

				for (int i = 0; i < img.getHeight(); i++) {
					for (int j = 0; j < img.getWidth(); j++) {
						int value = Math.round((cell.getElement(i, j) - min)
								* slope);
						img.setPixel(value, i, j);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		return img;
	}

	/**
	 * This method return the Clone of the given UInt16PixelImage object.
	 * 
	 * @return UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt16PixelImage clone() {
		try {
			int[][] clone = new int[getHeight()][getWidth()];

			for (int i = 0; i < clone.length; i++) {
				System.arraycopy(i0[i], 0, clone[i], 0, clone[i].length);
			}
			return new UInt16PixelImage(clone);
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method return the Image Type for this UInt16PixelImage.
	 * <p>
	 * The return type will be one of the PixelImage type as define in the Enum {@link PixelImageType}.
	 * 
	 * @return {@link PixelImageType}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelImageType getImageType() {
		return PixelImageType.UInt16;
	}

	//	/**
	//	 * This method display the given UInt16PixelImage as a gray scale intensity
	//	 * image on an {@link ImageViewer}.
	//	 * <p>
	//	 * The argument 'title' specify the Title for the ImageViewer.
	//	 * 
	//	 * @param String
	//	 *            title
	//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	//	 */
	//	public void display(String title) {
	//		BufferedImage img = toBufferedImage();
	//		try {
	//			ImageViewer frame = new ImageViewer();
	//			frame.setTitle(title);
	//			frame.display(img, null);
	//		} catch (DimensionMismatchException e) {
	//			throw new BugEncounterException();
	//		}
	//
	//	}

	/**
	 * This method will convert the given UInt16 PixelImage to an UInt8
	 * PixelImage and return the same.
	 * <p>
	 * The return UInt8 PixelImage with 8 bits of depth, is the low
	 * contrast/depth representation of the given UInt16 PixelImage with 16 bits
	 * of depth.
	 * <p>
	 * The UInt8 PixelImage is generated by linearly mapping the pixel
	 * intensities of this UInt16 PixelImage to an 8 bits range of [0 255].
	 * 
	 * @return UInt8PixelImage
	 * @see UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage toUInt8PixelImage() {
		UInt8PixelImage img;

		try {
			img = new UInt8PixelImage(getHeight(), getWidth());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		float slope = (1.0f / MAX);
		float _8Max = UInt8PixelImage.MAX;

		try {
			for (int i = 0; i < img.getHeight(); i++) {
				for (int j = 0; j < img.getWidth(); j++) {
					short pix = (short) Math.round(_8Max
							* (this.getPixel(i, j) * slope));
					img.setPixel(pix, i, j);
				}
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return img;
	}

	/**
	 * This method will convert the given 16 bits monochrome UInt16 PixelImage
	 * to a Pseudo RGB PixelImage and return the same as a RGBPixelImage.
	 * <p>
	 * Each pixel value of the given UInt16 PixelImage representing a shade of
	 * gray in the range of [0 65535] will be uniquely mapped to a RGB colour
	 * using the RGBMapper as given by the argument 'map'.
	 * <p>
	 * The dimensions of the return RGBPixelImage will be similar to that of the
	 * given UInt16 PixelImage.
	 * 
	 * @param RGBMapper
	 *            map
	 * @return RGBPixelImage
	 * @see RGBMapper
	 * @see RGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public RGBPixelImage mapToRGBPixelImage(RGBMapper map) {
		int h = getHeight();
		int w = getWidth();

		short[][] red = new short[h][w];
		short[][] green = new short[h][w];
		short[][] blue = new short[h][w];

		for(int i=0;i< h;i++)
		{
			for(int j=0;j< w;j++)
			{
				red[i][j] = map.getRedComponent(i0[i][j]);
				green[i][j] = map.getGreenComponent(i0[i][j]);
				blue[i][j] = map.getBlueComponent(i0[i][j]);
			}
		}

		try{
			UInt8PixelImage ru = new UInt8PixelImage(red);
			UInt8PixelImage gu = new UInt8PixelImage(green);
			UInt8PixelImage bu = new UInt8PixelImage(blue);

			return new RGBPixelImage(ru,gu,bu);
		}catch(DimensionMismatchException e){
			throw new BugEncounterException();
		}catch(PixelImageFormatException e){
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will convert the given 16 bits monochrome UInt16 PixelImage
	 * to a Pseudo ARGB PixelImage and return the same as an ARGBPixelImage.
	 * <p>
	 * Each pixel value of the given UInt16 PixelImage representing a shade of
	 * gray in the range of [0 65535] will be uniquely mapped to a RGB colour
	 * using the RGBMapper as given by the argument 'map'. The alpha or A
	 * component will be assign a value of 255 for all pixel values.
	 * <p>
	 * The dimensions of the return ARGBPixelImage will be similar to that of
	 * the given UInt16 PixelImage.
	 * 
	 * @param RGBMapper
	 *            map
	 * @return ARGBPixelImage
	 * @see RGBMapper
	 * @see ARGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public ARGBPixelImage mapToARGBPixelImage(RGBMapper map) {
		int h = getHeight();
		int w = getWidth();

		short[][] alpha = new short[h][w];
		short[][] red = new short[h][w];
		short[][] green = new short[h][w];
		short[][] blue = new short[h][w];

		for(int i=0;i< h;i++)
		{
			for(int j=0;j< w;j++)
			{
				alpha[i][j] = 255;
				red[i][j] = map.getRedComponent(i0[i][j]);
				green[i][j] = map.getGreenComponent(i0[i][j]);
				blue[i][j] = map.getBlueComponent(i0[i][j]);
			}
		}

		try{
			UInt8PixelImage au = new UInt8PixelImage(alpha);
			UInt8PixelImage ru = new UInt8PixelImage(red);
			UInt8PixelImage gu = new UInt8PixelImage(green);
			UInt8PixelImage bu = new UInt8PixelImage(blue);

			return new ARGBPixelImage(au,ru,gu,bu);
		}catch(DimensionMismatchException e){
			throw new BugEncounterException();
		}catch(PixelImageFormatException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will return the Maximum valid Pixel Intensity for this UInt16
	 * PixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMaxValidPixel() {
		return MAX;
	}

	/**
	 * This method will return the Minimum valid Pixel Intensity for this UInt16
	 * PixelImage.
	 * <p>
	 * This will always be '0' for all UInt PixelImages.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMinValidPixel() {
		return MIN;
	}

	/**
	 * This method will return the Depth of the given UInt16 PixelImage.
	 * <p>
	 * UInt PixelImage with depth 'd' has <i> 2^d </i> different Gray Scale
	 * levels.
	 * <p>
	 * UInt16 PixelImage with depth of '16' has 2^16 or 65536 different Gray
	 * Scale levels.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getDepth() {
		return i4;
	}

	private boolean f4(int pixel) {
		if (pixel < MIN || pixel > MAX)
			return false;
		else
			return true;
	}

	/**
	 * This method return a compatible blank UInt16 PixelImage with all 0's and
	 * dimension as that of the given UInt16 PixelImage.
	 * 
	 * @return AbstractPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractPixelImage getEquivalentBlankImage() {
		try {
			return new UInt16PixelImage(getHeight(), getWidth());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will promote the given UInt16 PixelImage to a
	 * {@link RGBPixelImage} and return the same.
	 * <p>
	 * The UInt16 image is first converted into an equivalent UInt8 PixelImage
	 * which is subsequently promoted to a RGB PixelImage.
	 * 
	 * @return RGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public RGBPixelImage toRGBPixelImage() {
		return toUInt8PixelImage().toRGBPixelImage();
	}

	/**
	 * This method will promote the given UInt16 PixelImage to an
	 * {@link ARGBPixelImage} and return the same.
	 * <p>
	 * The UInt16 image is first converted into an equivalent UInt8 PixelImage
	 * which is subsequently promoted to an ARGB PixelImage.
	 * 
	 * @return ARGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ARGBPixelImage toARGBPixelImage() {
		return toUInt8PixelImage().toARGBPixelImage();
	}

	/**
	 * This method will import an external image as specified by the argument
	 * 'path', as an {@link UInt16PixelImage} and return the same.
	 * <p>
	 * This method uses the ImageIO framework to load the external image first
	 * as {@link BufferedImage} and subsequently transform the same to an
	 * {@link UInt16PixelImage}.
	 * <p>
	 * Based upon the type of BufferedImage so imported, a suitable transform is
	 * applied to convert the same to an equivalent UInt16 PixelImage.
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
	 * @return UInt16PixelImage
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static UInt16PixelImage importImage(String path) throws IOException,
	UnSupportedImageFormatException {
		BufferedImage image = PixelImage.importAsBufferedImage(path);
		return UInt16PixelImage.toUInt16PixelImage(image);
	}

	/**
	 * This method will transform the {@link BufferedImage} 'image' to an
	 * {@link UInt16PixelImage} and return the same.
	 * <p>
	 * Based upon the type of {@link BufferedImage} one of the following
	 * conversion rule is applied :
	 * <p>
	 * <i>BufferedImage.TYPE_BYTE_GRAY</i> : Such a BufferedImage is comparable
	 * to an UInt8 PixelImage and thus directly transformed first into an UInt8
	 * PixelImage which is subequently converted to an equivalent UInt16
	 * PixelImage.
	 * <p>
	 * <i>BufferedImage.TYPE_USHORT_GRAY</i> : Such a BufferedImage is
	 * comparable to an UInt16 PixelImage and thus directly transformed to an
	 * UInt16 PixelImage.
	 * <p>
	 * <i>BufferedImage.TYPE_INT_RGB</i> : Such a BufferedImage is comparable to
	 * a RGB PixelImage and thus directly transformed first to a RGB PixelImage
	 * which is subsequently converted to an equivalent UInt16 PixelImage.
	 * <p>
	 * <i>Default Any other type</i> : Such a BufferedImage is changed first to
	 * an BufferedImage of type "TYPE_INT_ARGB", which is comparable to an ARGB
	 * PixelImage and thus directly transformed to an ARGB PixelImage, which is
	 * subsequently converted to an equivalent UInt16 PixelImage.
	 * 
	 * @param BufferedImage
	 *            image
	 * @return UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static UInt16PixelImage toUInt16PixelImage(BufferedImage image) {
		int imageType = image.getType();

		try {
			if (imageType == BufferedImage.TYPE_BYTE_GRAY) {
				short data[][] = f2(image);
				return new UInt8PixelImage(data).toUInt16PixelImage();

			} else if (imageType == BufferedImage.TYPE_USHORT_GRAY) {
				int data[][] = f1(image);
				return new UInt16PixelImage(data);

			} else if (imageType == BufferedImage.TYPE_INT_RGB) {
				short data[][][] = f3(image);
				int h = data[0].length;
				int w = data[0][0].length;

				UInt16PixelImage img = new UInt16PixelImage(h, w);

				int value;

				float slope = ((float) UInt16PixelImage.MAX) / ((float)UInt8PixelImage.MAX);

				for (int i = 0; i < h; i++) {
					for (int j = 0; j < w; j++) {
						value = Math.round(slope*(0.3f*data[1][i][j] + 0.59f*data[2][i][j] + 0.11f*data[3][i][j]));
						img.setPixel(value, i, j);
					}
				}

				return img;
			} else {
				short data[][][] = f0(image);
				int h = data[0].length;
				int w = data[0][0].length;

				UInt16PixelImage img = new UInt16PixelImage(h, w);

				int value;

				float slope = ((float) UInt16PixelImage.MAX) / ((float)UInt8PixelImage.MAX);

				for (int i = 0; i < h; i++) {
					for (int j = 0; j < w; j++) {
						value = Math.round(slope*(0.3f*data[1][i][j] + 0.59f*data[2][i][j] + 0.11f*data[3][i][j]));
						img.setPixel(value, i, j);
					}
				}

				return img;

			}
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will return the number of different gray scale intensity level for
	 * the given UInt PixelImage.
	 * <p>UInt PixelImage with depth 'd' has 2^d different gray scale intensity levels. 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getIntensityLevels() {
		return i1;
	}
}
