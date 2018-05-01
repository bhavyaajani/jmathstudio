package org.JMathStudio.PixelImageToolkit.BinaryPixelImage;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.Exceptions.PixelImageFormatException;
import org.JMathStudio.Exceptions.UnSupportedImageFormatException;
import org.JMathStudio.ImageToolkit.IntensityTools.ImageBinarization;
import org.JMathStudio.PixelImageToolkit.AbstractPixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage.PixelImageType;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt8PixelImage;

/**
 * This class represents a Binary PixelImage and extends
 * {@link AbstractPixelImage}. See {@link PixelImage} for further information on
 * Binary PixelImage type.
 * <p>
 * Pixel intensities of a BinaryPixelImage is represented by a 2D boolean pixel
 * array, such that all True's represents 1's i.e foreground and False's
 * represents 0's i.e background.
 * <p>
 * Indexing: Row index -> [0 height of image -1], Column index -> [0 width of
 * image -1]
 * <pre>Usage:
 * BinaryPixelImage img1 = BinaryPixelImage.importImage("img_path");//Import external image
 * as an equivalent BinaryPixelImage.
 * 
 * img1.display("Title");//Display BinaryPixelImage on a suitable viewer.
 * 
 * BinaryPixelImage img2 = img1.getComplementaryImage();//Get a complement BinaryPixelImage.
 * 
 * Let 'cell' be a valid Cell object.
 * 
 * BinaryPixelImage binary = BinaryPixelImage.toBinaryPixelImage(cell, t);//Convert a Cell to
 * a binary image using threshold 't'.
 * </pre> 
 * @see PixelImage
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class BinaryPixelImage extends AbstractPixelImage {

	private boolean bolData[][];
	
	/**
	 * This will create a BinaryPixelImage of Height and Width as given by the
	 * argument 'height' and 'width' respectively.
	 * <p>
	 * The argument 'height' and 'width' should be more than '0' else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * BinaryPixelImage will be initialised to a background image with all pixel
	 * values having the default value of 0, that is the internal boolean array
	 * representing this PixelImage will be initialised with all False's.
	 * 
	 * @param int height
	 * @param intwidth
	 * @see PixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage(int height, int width)
			throws IllegalArgumentException {
		if (height <= 0 | width <= 0)
			throw new IllegalArgumentException();
		else {
			bolData = new boolean[height][width];
		}
	}

	/**
	 * This will create a BinaryPixelImage and populate the same with the given
	 * Binary pixels as given by the boolean 2D array 'pixelData'.
	 * <p>
	 * The boolean 2D array 'pixelData' specify the pixel values for the given
	 * BinaryPixelImage, which can take only two Binary values 1's and 0's.
	 * True's and False's of the array 'pixelData' thus denote respectively the
	 * 1's and 0's value for that pixel position.
	 * <p>
	 * Each row of the 2D boolean array 'pixelData' should be of same length
	 * else this method will throw an PixelImageFormat Exception. The number of
	 * rows and columns of the array will specify the Height and Width of the
	 * given PixelImage.
	 * <p>The argument 'pixelData' is passed by reference and no deep copy of the same
	 * is made.
	 * 
	 * @param boolean[][] pixelData
	 * @throws PixelImageFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage(boolean[][] pixelData)
			throws PixelImageFormatException {
		for (int i = 1; i < pixelData.length; i++) {
			if (pixelData[i].length != pixelData[0].length)
				throw new PixelImageFormatException();
		}

		this.bolData = pixelData;
	}

	/**
	 * This method will return the Pixel value located at the pixel as
	 * identified by its Row and Column index which is given by the arguments
	 * 'row' and 'column' respectively.
	 * <p>
	 * The argument 'row' and 'column' should be within the bound of [0 height
	 * of image-1] and [0 width of image -1] respectively else this method will
	 * throw an ArrayIndexOutOfBounds Exception.
	 * <p>
	 * The BinaryPixelImage can hold only '1' or '0' for each pixel location.
	 * Thus if the pixel value at the specified location is '1' than return
	 * boolean will be 'True' else for pixel value '0' it will be 'False'.
	 * 
	 * @param int row
	 * @param int column
	 * @return boolean
	 * @see #BinaryPixelImage(boolean[][])
	 * @throws ArrayIndexOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean getPixel(int row, int column) {
		return this.bolData[row][column];
	}

	/**
	 * This method will set the Pixel value as given by the argument 'value' for
	 * the pixel location as identified by its Row and Column index which is
	 * given by the arguments 'row' and 'column' respectively.
	 * <p>
	 * The argument 'row' and 'column' should be within the bound of [0 height
	 * of image-1] and [0 width of image -1] respectively else this method will
	 * throw an ArrayIndexOutOfBounds Exception.
	 * <p>
	 * As BinaryPixelImage can hold only two valid values for a pixel i.e '1' or
	 * '0', the argument 'value' if 'True' will represent '1' for that pixel
	 * position else if 'False' will represent '0' for that pixel position.
	 * 
	 * @param boolean value
	 * @param int row
	 * @param int column
	 * @see #BinaryPixelImage(boolean[][])
	 * @throws ArrayIndexOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setPixel(boolean value, int row, int column) {
		this.bolData[row][column] = value;
	}

	/**
	 * This method will return the pixel data buffer of this Binary PixelImage
	 * as a 2D boolean array where True's and False's of the array represents
	 * the 1's and 0's of the Binary PixelImage. The length and width of the
	 * return array will be similar to that of this PixelImage.
	 * 
	 * @return boolean[][]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean[][] accessPixelDataBuffer() {
		return this.bolData;
	}

	/**
	 * This method return the Height of the given BinaryPixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getHeight() {
		return this.bolData.length;
	}

	/**
	 * This method return the Width of the given BinaryPixelImage.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getWidth() {
		return this.bolData[0].length;
	}

	/**
	 * This method will convert the given BinaryPixelImage to a Cell object and
	 * return the same.
	 * <p>
	 * The return Cell object will be of the same dimension as that of this
	 * image and will contain the pixel data as either '1' or '0' for all the
	 * pixel positions.
	 * 
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() {
		float[][] result = new float[bolData.length][bolData[0].length];

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				if (bolData[i][j] == true)
					result[i][j] = 1;
				else
					result[i][j] = 0;
			}
		}

		try {
			return new Cell(result);
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method return the Clone of the given BinaryPixelImage object.
	 * 
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage clone() {
		boolean[][] result = new boolean[this.getHeight()][this.getWidth()];

		for (int i = 0; i < result.length; i++) {
			System.arraycopy(bolData[i], 0, result[i], 0, result[i].length);
		}

		try {
			return new BinaryPixelImage(result);
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method checks equality of the BinaryPixelImage as given by the
	 * argument 'image' with this BinaryPixelImage.
	 * <p>
	 * Equality condition checks whether the dimension of the both the
	 * BinaryPixelImages are same and if all the corresponding pixel elements of
	 * the images are same.
	 * <p>
	 * If both the BinaryPixelImages are found to be equal this method will
	 * return 'true' else it will return 'false'.
	 * 
	 * @param BinaryPixelImage
	 *            image
	 * @return boolean
	 * @see PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isEqual(BinaryPixelImage image)
			throws IllegalArgumentException {
		if (this.getHeight() != image.getHeight()) {
			return false;
		}

		if (this.getWidth() != image.getWidth()) {
			return false;
		}

		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (image.getPixel(i, j) != bolData[i][j])
					return false;
			}
		}

		return true;
	}

	/**
	 * This method will return the complementary BinaryPixelImage for the given
	 * BinaryPixelImage.
	 * <p>
	 * A complementary BinaryPixelImage has 1's in place of 0's and 0's in place
	 * of 1's of the target BinaryPixelImage.
	 * 
	 * @return BinaryPixelImage
	 * @see PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage getComplementaryImage() {
		boolean[][] result = new boolean[getHeight()][getWidth()];

		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				result[i][j] = !bolData[i][j];
			}
		}

		try {
			return new BinaryPixelImage(result);
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will transform the Cell 'cell' to a BinaryPixelImage by
	 * thresholding the elements of the 'cell' and return the same as a
	 * BinaryPixelImage.
	 * <p>
	 * The argument 'threshold' specify the threshold for thresholding the
	 * elements of the 'cell'. All the elements of the 'cell' with value more
	 * than 'threshold' is mapped to '1' and rest of the elements with value
	 * less than or equal to the 'threshold' is mapped to '0' in the resultant
	 * BinaryPixelImage.
	 * <p>
	 * The dimensions of the resultant Binary PixelImage will be similar to that
	 * of the input Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @param float threshold
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage toBinaryPixelImage(Cell Cell, float threshold) {
		boolean[][] result = new boolean[Cell.getRowCount()][Cell.getColCount()];

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				if (Cell.getElement(i, j) > threshold) {
					result[i][j] = true;
				} else {
					result[i][j] = false;
				}
			}
		}

		try {
			return new BinaryPixelImage(result);
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will transform the UInt PixelImage as represented by
	 * {@link AbstractUIntPixelImage} 'image' to a Binary PixelImage by
	 * thresholding the pixel intensities of the 'image' and return the
	 * resultant binary image as a BinaryPixelImage.
	 * <p>
	 * The argument 'threshold' specify the threshold for thresholding the pixel
	 * intensities of the UInt PixelImage 'image'. All the pixels with intensity
	 * more than 'threshold' is mapped to '1' and rest of the pixels with
	 * intensity less than or equal to the 'threshold' is mapped to '0' in the
	 * resultant binary image.
	 * <p>
	 * The argument 'threshold' should specify an intensity value within the
	 * valid intensity range of the given {@link AbstractUIntPixelImage} 'image'
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The dimensions of the resultant Binary PixelImage will be similar to that
	 * of the input image.
	 * 
	 * @param AbstractUIntPixelImage
	 *            image
	 * @param int threshold
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage toBinaryPixelImage(
			AbstractUIntPixelImage image, int threshold)
			throws IllegalArgumentException {
		if (threshold < image.getMinValidPixel() || threshold > image.getMaxValidPixel())
			throw new IllegalArgumentException();
		else {
			try {
				// Initialized by 'false' by default.
				boolean[][] result = new boolean[image.getHeight()][image
						.getWidth()];

				for (int i = 0; i < result.length; i++) {
					for (int j = 0; j < result[0].length; j++) {
						if (image.getPixel(i, j) > threshold) {
							result[i][j] = true;
						}
					}
				}

				return new BinaryPixelImage(result);
			} catch (PixelImageFormatException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will transform the UInt PixelImage as represented by
	 * {@link AbstractUIntPixelImage} 'image' to a Binary PixelImage by
	 * thresholding the pixel intensities of the 'image' and return the
	 * resultant binary image as a BinaryPixelImage.
	 * <p>
	 * The global image threshold intensity is computed using iterative isodata
	 * method. The same is than used to threshold the given UInt PixelImage. All
	 * the pixels with intensity more than threshold is mapped to '1' and rest
	 * of the pixels with intensity less than or equal to the threshold is
	 * mapped to '0' in the resultant binary image.
	 * <p>
	 * The dimensions of the resultant Binary PixelImage will be similar to that
	 * of the input image.
	 * 
	 * @param AbstractUIntPixelImage
	 *            image
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage toBinaryPixelImage(
			AbstractUIntPixelImage image) {
		try{
			int threshold = new ImageBinarization().isoData(image);
			return toBinaryPixelImage(image,threshold);
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method return the Image Type for this BinaryPixelImage.
	 *<p>
	 * The return type will be one of the PixelImage type as define in the enum {@link PixelImageType}.
	 * 
	 * @return {@link PixelImageType}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelImageType getImageType() {
		return PixelImageType.Binary;
	}

	/**
	 * This method will convert the given BinaryPixelImage object to an
	 * appropriate BufferedImage object and return the same.
	 * <p>
	 * The BinaryPixelImage will be converted to an UInt8PixelImage type with
	 * White for 1's and Black for 0's before converting the same to a
	 * BufferedImage.
	 * <p>
	 * BufferedImage is a standard Image type for Java.
	 * 
	 * @return BufferedImage
	 * @see BufferedImage
	 * @see UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BufferedImage toBufferedImage() {

		BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);

		byte[] byteArray = f0(bolData);
		image.getRaster().setDataElements(0, 0, getWidth(), getHeight(),
				byteArray);

		return image;
	}

	private byte[] f0(boolean[][] data) {
		int width = data[0].length;
		int height = data.length;
		byte result[] = new byte[width * height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (data[y][x] == true)
					result[y * width + x] = (byte) ((short) 255 & 0xFF);
				else
					result[y * width + x] = (byte) ((short) 0 & 0xFF);
			}
		}
		return result;

	}

	/**
	 * This method return a compatible blank Binary PixelImage with all 0's or
	 * false and dimension as that of the given Binary PixelImage.
	 * 
	 * @return AbstractPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractPixelImage getEquivalentBlankImage() {
		try {
			return new BinaryPixelImage(getHeight(), getWidth());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will import an external image as specified by the argument
	 * 'path', as a {@link BinaryPixelImage} and return the same.
	 * <p>
	 * This method uses the ImageIO framework to load the external image first
	 * as {@link BufferedImage} and subsequently transform the same to a
	 * {@link BinaryPixelImage}.
	 * <p>
	 * The BufferedImage so imported is first coverted to an
	 * {@link UInt8PixelImage}, which is subsequently thresholded to get an
	 * equivalent Binary PixelImage.
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
	 * @return BinaryPixelImage
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage importImage(String path) throws IOException,
			UnSupportedImageFormatException {
		return BinaryPixelImage.toBinaryPixelImage(UInt8PixelImage
				.importImage(path));
	}

	/**
	 * This method will transform the {@link BufferedImage} 'image' to a
	 * {@link BinaryPixelImage} and return the same.
	 * <p>
	 * BufferedImage is first transformed into an equivalent
	 * {@link UInt8PixelImage} which is subsequently thresholded to get a Binary
	 * PixelImage.
	 * 
	 * @param BufferedImage
	 *            image
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage toBinaryPixelImage(BufferedImage image) {
		return BinaryPixelImage.toBinaryPixelImage(UInt8PixelImage
				.toUInt8PixelImage(image));
	}
}
