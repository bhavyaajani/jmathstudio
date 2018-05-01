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
import org.JMathStudio.Exceptions.UnSupportedImageFormatException;
import org.JMathStudio.PixelImageToolkit.AbstractPixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage.PixelImageType;
import org.JMathStudio.VisualToolkit.Viewer.ImageViewer;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class represents a Custom UInt PixelImage and extends
 * {@link AbstractUIntPixelImage}. See {@link PixelImage} for further
 * information on Custom UInt PixelImage type.
 * <p>
 * A CustomUIntPixelImage is an UInt PixelImage with require depth. Pixel
 * intensities of a Custom UInt PixelImage with depth 'N' is represented by a 2D
 * integer pixel array representing different shades of gray with 'N' bits depth
 * for each pixel position.
 * <p>
 * Thus pixel values of this UInt PixelImage takes value in the range of [0 2^N
 * -1] corresponding to 2^N different shades of gray.
 * <p>
 * This UInt PixelImage will be represented as an {@link AbstractUIntPixelImage}
 * which define generic characteristics of an UInt PixelImages, to all the
 * Transforms and Operations defined on UInt PixelImages family so as to make
 * those generic.
 * <p>
 * Indexing: Row index -> [0 height of image -1], Column index -> [0 width of
 * image -1]
 * <pre>Usage:
 * Let 'cell' be a valid Cell object.
 * CustomUIntPixelImage img1 = CustomUIntPixelImage.toCustomUIntPixelImage(4, cell);//Transform
 * scalar image as represented by input Cell to an UInt PixelImage with depth '4'.
 * 
 * Let 'uint_img' be an UInt PixelImage of any valid depth.
 * CustomUIntPixelImage img2 = CustomUIntPixelImage.toCustomUIntPixelImage(6, uint_img);//Map 
 * pixel intensities of input UInt PixelImage to UInt PixelImage with depth '6'.  
 * 
 * img2.exportImage("directory", "fileName");//Export given CustomUIntPixelImage to an external
 * image.
 * </pre>		
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class CustomUIntPixelImage extends AbstractUIntPixelImage {

	public final static int MAXDEPTH = 16;
	public final static int MINDEPTH = 1;

	private int i8;
	private int i2;
	private int i0;
	private int i7;
	
	private int[][] pixelData;

	/**
	 * This will create a Custom UInt PixelImage with depth as specified by the
	 * argument 'depth' and with height and width as specified by the arguments
	 * 'height' and 'width' respectively.
	 * <p>
	 * The argument 'height' and 'width' should be more than '0' else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * A Custom UInt PixelImage with depth 'N' has 2^N shades of gray. Thus
	 * pixel values of such an image takes value from 0 to 2^N-1, representing
	 * various shades of gray from black to white respectively.
	 * <p>
	 * The maximum and minimum depth permissible for a Custom UInt PixelImage is
	 * 16 and 1 respectively. Thus argument 'depth' should be within the bound
	 * of [1 16] else this method will throw an IllegalArgument Exception.
	 * 
	 * @param int depth
	 * @param int height
	 * @param int width
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CustomUIntPixelImage(int depth, int height, int width)
			throws IllegalArgumentException {
		if (depth < MINDEPTH || depth > MAXDEPTH)
			throw new IllegalArgumentException();
		else {
			this.i8 = depth;
			this.i0 = (int) (Math.pow(2, depth) - 1);
			this.i2 = 0;
			this.i7 = this.i0+1;
		}

		if (height <= 0 || width <= 0)
			throw new IllegalArgumentException();
		else {
			this.pixelData = new int[height][width];
		}
	}

	/**
	 * This method return the clone of the given Custom UIntPixelImage object.
	 * 
	 * @return CustomUIntPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CustomUIntPixelImage clone() {
		try {
			CustomUIntPixelImage clone = new CustomUIntPixelImage(this.i8,
					this.getHeight(), this.getWidth());

			for (int i = 0; i < clone.getHeight(); i++) {
				for (int j = 0; j < clone.getWidth(); j++) {
					clone.setPixel(this.getPixel(i, j), i, j);
				}
			}

			return clone;
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method display the given Custom UInt PixelImage as a gray scale
	 * intensity image on an {@link ImageViewer}.
	 * <p>
	 * The given Custom UIntPixelImage will be displayed as an UInt16 PixelImage
	 * but the discrete quantization levels shall be equal to that supported by
	 * the given Custom UInt PixelImage.
	 * <p>
	 * The argument 'title' specify the Title for the ImageViewer.
	 * 
	 * @param String
	 *            title
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void display(String title) {
		try {
			BufferedImage img;

			if (this.i8 != 16)
				img = CustomUIntPixelImage.toCustomUIntPixelImage(16, this)
						.toBufferedImage();
			else
				img = toBufferedImage();

			ImageViewer frame = new ImageViewer();
			frame.setTitle(title);
			frame.display(img, null);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will export the given Custom UInt PixelImage as an UInt16
	 * Image to an external directory path.
	 * <p>
	 * The argument 'path' specify the external directory path where Custom UInt
	 * PixelImage is to be exported. Note argument 'path' should not end with
	 * '/' or '\\'.
	 * <p>
	 * The argument 'fileName' specify the file name for exporting the given
	 * Custom UInt PixelImage as an external image. The argument 'fileName'
	 * should have a file name and an extension separated by a '.'. (Note :
	 * argument 'fileName' should have only one '.' to separate extension from
	 * the file name).
	 * <p>
	 * The extension of the argument 'fileName' like '.png','.gif','.jpg' will
	 * define the Image Format for exporting the Custom UInt PixelImage as an
	 * external Image file. Use supported Image Format which depends upon the
	 * platform. See {@link PixelImage#getExportFormatsSupported()} method for
	 * the list of Image Format supported on the current platform.
	 * <p>
	 * If the Image Format for exporting the given Custom UInt PixelImage is not
	 * supported by the platform this method will throw an
	 * UnSupportedImageFormat Exception.
	 * <p>
	 * If the external path as specified by the argument 'path' does not exist
	 * or if any IO related issues comes up this method will throw an
	 * IOException.
	 * <p>
	 * This method will export Custom UInt PixelImages as an UInt16 Image Type
	 * but will not modify the pixel data in the process but will scale up the
	 * depth of the image to 16. For Custom UInt PixelImage with low depth this
	 * may result in increasing the darkness in the resultant exported image.
	 * <p>
	 * On importing such exported image one will get it as an UInt16 Type image.
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
	public void exportImage(String path, String fileName) throws IOException,
			UnSupportedImageFormatException {
		BufferedImage image = toBufferedImage();
		PixelImage.exportBufferedImage(image, path, fileName);	
	}

	/**
	 * This method return the Height of the given Custom UInt PixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getHeight() {
		return this.pixelData.length;
	}

	/**
	 * This method return the Width of the given Custom UInt PixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getWidth() {

		return this.pixelData[0].length;
	}

	/**
	 * This method return the Image Type for this Custom UInt PixelImage.
	 * <p>
	 * The return type will be one of the PixelImage type as define in the Enum {@link PixelImageType}.
	 * 
	 * @return {@link PixelImageType}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelImageType getImageType() {
		return PixelImageType.CustomUInt;
	}

	/**
	 * This method will convert the given Custom UInt PixelImage object to an
	 * appropriate {@link BufferedImage} object and return the same.
	 * <p>
	 * This Custom UInt PixelImage will be converted to a BufferedImage of type
	 * "TYPE_USHORT_GRAY" which has an UInt16 representation. In the conversion
	 * pixel values will not be modified, only depth of the image will be scaled
	 * up to 16. For low depth Custom UInt PixelImage, this scaling of depth to
	 * 16 may result in increasing the darkness in the resultant
	 * {@link BufferedImage}.
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
				img[0][i * width + j] = (short) (pixelData[i][j]);
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
	 * This method will convert the given Custom UInt PixelImage to a Cell
	 * object and return the same.
	 * <p>
	 * The return Cell object will be of the same dimension as that of this
	 * image and will contain the corresponding pixel data of this PixelImage as
	 * a float elements.
	 * 
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() {
		Cell cell = new Cell(getHeight(), getWidth());

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				cell.setElement(pixelData[i][j], i, j);
			}
		}

		return cell;

	}

	/**
	 * This method will return the Depth of the given Custom UInt PixelImage.
	 * <p>
	 * Custom UInt PixelImage with depth 'd' has <i> 2^d </i> different Gray
	 * Scale levels.
	 * <p>
	 * The valid range of depth for a Custom UInt PixelImage is [1 16].
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getDepth() {

		return this.i8;
	}

	/**
	 * This method will return the Maximum valid Pixel Intensity for this Custom
	 * UInt PixelImage with given depth.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMaxValidPixel() {
		return this.i0;
	}

	/**
	 * This method will return the Minimum valid Pixel Intensity for this Custom
	 * UInt PixelImage.
	 * <p>
	 * This will always be '0' for all gray scale intensity images.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMinValidPixel() {

		return this.i2;
	}

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
	 * in the range of [0 2^N -1] where 'N' is the depth of the current Custom
	 * UInt PixelImage. Return pixel value thus represent different shades of
	 * gray from Black to White.
	 * 
	 * @param int row
	 * @param int column
	 * @return int
	 * @throws ArrayIndexOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getPixel(int row, int column) {
		return this.pixelData[row][column];
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
	 * A Custom UInt PixelImage with depth 'N' can represent 2^N different
	 * shades of gray's corresponding to the values 0 to 2^N -1 from black to
	 * white, thus the argument 'value' here should be within this range of [0
	 * 2^N -1] else this method will throw an IllegalArgument Exception.
	 * <p>
	 * See {@link #getMaxPixel()} and {@link #getMinPixel()} for the range of
	 * valid pixel intensities for this UInt PixelImage.
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
		if (!f0(value))
			throw new IllegalArgumentException();
		else
			this.pixelData[row][column] = value;

	}

//	/**
//	 * This method will return the integer 2D array representing the pixel data
//	 * buffer for this Custom UInt PixelImage. The height and width of the
//	 * return 2D array will be similar to the dimensions of this PixelImage.
//	 * <p>
//	 * The integer values within the return array will be within the valid pixel
//	 * range of this Custom UInt PixelImage.
//	 * <p>
//	 * See {@link #getMaxPixel()} and {@link #getMinPixel()} for the range of
//	 * valid pixel intensities for this UInt PixelImage.
//	 * 
//	 * @return int[][]
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	public int[][] accessPixelDataBuffer() {
//		return this.pixelData;
//	}

	/**
	 * This method will convert the given {@link Cell} 'cell' to a Custom UInt
	 * PixelImage with depth as specified by the argument 'depth' and return the
	 * same as a {@link CustomUIntPixelImage}.
	 * <p>
	 * The argument 'depth' specifying the depth of the required Custom UInt
	 * PixelImage should be within the permissible range of [0 16] else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * The dimension of the return {@link CustomUIntPixelImage} will be similar
	 * to that of the {@link Cell} 'cell'.
	 * <p>
	 * The Cell 'cell' is converted to the specified Custom UInt PixelImage by
	 * linearly mapping the elements of the Cell to the valid pixel range of the
	 * required Custom UInt PixelImage. Thus the lowest element of the Cell
	 * 'cell' will be mapped to '0' and highest element will be mapped to the
	 * maximum pixel value of the Custom UInt PixelImage, which depends upon the
	 * depth specified.
	 * <p>
	 * As this method linearly maps the elements of the Cell 'cell' to the pixel
	 * range of the required Custom UInt PixelImage, this the pixel data of the
	 * resultant {@link CustomUIntPixelImage} does not reflect the original
	 * pixel data as represented by the Cell 'cell'.
	 * 
	 * @param int depth
	 * @param Cell
	 *            cell
	 * @return {@link CustomUIntPixelImage}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CustomUIntPixelImage toCustomUIntPixelImage(int depth,
			Cell cell) throws IllegalArgumentException {

		CustomUIntPixelImage img = new CustomUIntPixelImage(depth, cell
				.getRowCount(), cell.getColCount());

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
						img.setPixel(img.i0, i, j);

					}
				}
			} else {
				float slope = img.i0 / (max - min);

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
	 * This method will convert the {@link AbstractUIntPixelImage} 'image' to a
	 * Custom UInt PixelImage with depth as specified by the argument 'depth'
	 * and return the same as a {@link CustomUIntPixelImage}.
	 * <p>
	 * The argument 'depth' specifying the depth of the required Custom UInt
	 * PixelImage should be within the permissible range of [1 16] else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * The dimension of the return {@link CustomUIntPixelImage} will be similar
	 * to that of the {@link AbstractUIntPixelImage} 'image'.
	 * <p>
	 * The UInt PixelImage 'image' is converted to the specified Custom UInt
	 * PixelImage by linearly scaling the pixel data of the UInt PixelImage
	 * 'image' to the valid pixel range of the required Custom UInt PixelImage.
	 * Thus the linearly mapping employed here will map the valid pixel
	 * intensity range of the given UInt PixelImage to that of the required
	 * Custom UInt PixelImage.
	 * 
	 * @param int depth
	 * @param AbstractUIntPixelImage
	 *            image
	 * @return {@link CustomUIntPixelImage}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CustomUIntPixelImage toCustomUIntPixelImage(int depth,
			AbstractUIntPixelImage image)
			throws IllegalArgumentException {
		CustomUIntPixelImage result = new CustomUIntPixelImage(depth, image
				.getHeight(), image.getWidth());

		float slope = result.i0 / (float) (image.getMaxValidPixel());

		for (int i = 0; i < result.getHeight(); i++) {
			for (int j = 0; j < result.getWidth(); j++) {
				int value = Math.round(image.getPixel(i, j) * slope);

				try {
					result.setPixel(value, i, j);
				} catch (IllegalArgumentException e) {
					throw new BugEncounterException();
				}
			}
		}

		return result;

	}

	/**
	 * This method will convert the given Custom UInt PixelImage to an UInt16
	 * PixelImage and return the same as an {@link UInt16PixelImage}.
	 * <p>
	 * As UInt16 PixelImage has the highest depth for all UInt PixelImages a
	 * Custom UInt PixelImage with any depth can be converted to the
	 * {@link UInt16PixelImage} with out the need to scale the pixel data. Thus
	 * the pixel data of this Custom UInt PixelImage are preserved in this
	 * process.
	 * 
	 * @return UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt16PixelImage toUInt16PixelImage() {
		UInt16PixelImage result;

		try {
			result = new UInt16PixelImage(this.getHeight(), this.getWidth());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		try {
			for (int i = 0; i < result.getHeight(); i++) {
				for (int j = 0; j < result.getWidth(); j++) {
					result.setPixel(this.getPixel(i, j), i, j);
				}
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return result;
	}

	private boolean f0(int pixel) {
		if (pixel < i2 || pixel > i0)
			return false;
		else
			return true;
	}

	/**
	 * This method return a compatible blank Custom UIntPixelImage with all 0's
	 * and dimension as that of the given Custom UIntPixelImage.
	 * 
	 * @return AbstractPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractPixelImage getEquivalentBlankImage() {
		try {
			return new CustomUIntPixelImage(getDepth(), getHeight(), getWidth());
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
		return this.i7;
	}

}
