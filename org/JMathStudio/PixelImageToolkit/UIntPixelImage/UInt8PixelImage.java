package org.JMathStudio.PixelImageToolkit.UIntPixelImage;

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
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.ARGBPixelImage;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBMapper;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBPixelImage;
import org.JMathStudio.PixelImageToolkit.PixelImage.PixelImageType;

/**
 * This class represents an UInt8 PixelImage and extends {@link AbstractUIntPixelImage}.
 * See {@link PixelImage} for further information on UInt8 PixelImage type.
 * <p>An UInt8PixelImage belong to an UInt PixelImage family. Pixel intensities of an UInt8 PixelImage is represented by a
 * 2D short pixel array representing different shades of gray with 8 bits depth for each 
 * pixel position.
 * <p>Thus pixel values of this UInt8 PixelImage takes value in the range of [0 255] corresponding to 256 different
 * shades of gray. 
 * <p>This UInt PixelImage will be represented as an {@link AbstractUIntPixelImage} which define generic characteristics 
 * of an UInt PixelImages, to all the Transforms and Operations defined on UInt PixelImages family so as to make those generic. 
 * <p>Indexing: Row index -> [0 height of image -1], Column index -> [0 width of image -1]
 * <pre>Usage: Refer to UInt16PixelImage.</pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class UInt8PixelImage extends AbstractUIntPixelImage{

	public final static int MAX = 255;
	public final static int MIN = 0;

	private final int i3 = 8;
	private final int i7 = 256;

	private short[][] i1;

	/**
	 * This will create an UInt8PixelImage of Height and Width as given by the argument
	 * 'height' and 'width' respectively.
	 * <p>The argument 'height' and 'width' should be more than '0' else this method will throw
	 * an IllegalArgument Exception.
	 * <p>UInt8PixelImage will be initialised to a black image with all the pixel values having
	 * the default value of 0 representing the black.
	 * @param int height
	 * @param intwidth
	 * @see PixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage(int height,int width) throws IllegalArgumentException
	{
		if(height <=0 || width <=0)
			throw new IllegalArgumentException();
		else
		{
			i1 = new short[height][width];
		}

	}

	/**
	 * This will create an UInt8PixelImage and populate the same with the pixel values as given
	 * by the short 2D array 'pixelData'.
	 * <p>The short 2D array 'pixelData' should have a valid UInt8 short array format else this method will throw
	 * a PixelImageFormat Exception. See {@link #isUInt8Data(short[][])} method for further clarification
	 * on UInt8 short array requirements.
	 * <p>The argument 'pixelData' is passed by reference and no deep copy of the same is made.
	 * 
	 * @param short[][] pixelData
	 * @throws PixelImageFormatException
	 * @see {@link #isUInt8Data(short[][])}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage(short[][] pixelData) throws PixelImageFormatException 
	{
		if(!UInt8PixelImage.isUInt8Data(pixelData))
			throw new PixelImageFormatException();
		else
		{
			this.i1 = pixelData;
		}
	}

	/**
	 * This method will convert the given UInt8PixelImage object to an appropriate BufferedImage
	 * object and return the same. 
	 * <p>The UInt8PixelImage will be converted to a BufferedImage of type "TYPE_BYTE_GRAY".
	 * <p>BufferedImage is a standard Image type for Java.
	 * @return BufferedImage
	 * @see BufferedImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BufferedImage toBufferedImage() {

		BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);

		byte[] byteArray = f4(i1);
		image.getRaster().setDataElements(0, 0, getWidth(), getHeight(), byteArray);

		return image;
	}

	/**
	 * This method return the Height of the given UInt8PixelImage.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getHeight() {
		return this.i1.length;
	}

	/**
	 * This method return the Width of the given UInt8PixelImage.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getWidth() {
		return this.i1[0].length;
	}

	/**
	 * This method will convert the given UInt8PixelImage to a Cell object and 
	 * return the same. 
	 * <p>The return Cell object will be of the same dimension as that of this image
	 * and will contain the corresponding pixel data of this UInt8 PixelImage as a float elements.
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() {

		Cell cell = new Cell(getHeight(),getWidth());

		for(int i=0;i<cell.getRowCount();i++)
		{
			for(int j=0;j<cell.getColCount();j++)
			{
				cell.setElement(i1[i][j], i, j);
			}
		}

		return cell;
	}

	//	/**
	//	 * This method will export the given UInt8PixelImage as an Image to an external directory path.
	//	 * <p>The argument 'path' specify the external directory path where UInt8PixelImage is to be
	//	 * exported. Note argument 'path' should not end with '/' or '\\'.
	//	 * <p>The argument 'fileName' specify the file name for exporting the given UInt8PixelImage as
	//	 * an external image. The argument 'fileName' should have a file name and an extension separated by a '.'.
	//	 * (Note : argument 'fileName' should have only one '.' to separate extension from the file name).
	//	 * <p>The extension of the argument 'fileName' like '.png','.gif','.jpg' will define the Image Format for 
	//	 * exporting the UInt8PixelImage as an external Image file. Use supported Image Format which depends upon the
	//	 * platform. See {@link PixelImage#getExportFormatsSupported()} method for the list of Image Format supported on the current platform.
	//	 * <p>If the Image Format for exporting the given UInt8PixelImage is not supported by the platform this method
	//	 * will throw an UnSupportedImageFormat Exception.
	//	 * <p>If the external path as specified by the argument 'path' does not exist or if
	//	 * any IO related issues comes up this method will throw an IOException.
	//	 * @param String path
	//	 * @param String fileName
	//	 * @throws IOException
	//	 * @throws UnSupportedImageFormatException
	//	 * @see {@link PixelImage#getExportFormatsSupported()}
	//	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	//	 */
	//	public void exportImage(String path,String fileName) throws IOException, UnSupportedImageFormatException {
	//		BufferedImage image = toBufferedImage();
	//		PixelImage.exportBufferedImage(image, path, fileName);	
	//	}

	/**
	 * This method will return the Pixel value located at the pixel position as identified by its row and column
	 * index which is given by the arguments 'row' and 'column' respectively.
	 * <p>The argument 'row' and 'column' should be within the bound of [0 height of image-1] and [0 width of image -1]
	 * respectively else this method will throw an ArrayIndexOutOfBounds Exception.
	 * <p>The pixel value at that pixel location will be return as an integer value in the range
	 * of [0 255] representing different shades of gray from Black to White.
	 * @param int row
	 * @param int column
	 * @return int
	 * @see #isUInt8ShortArray(short[][])
	 * @throws ArrayIndexOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getPixel(int row,int column) 
	{
		return this.i1[row][column];
	}

	/**
	 * This method will set the Pixel value as given by the argument 'value' for the pixel location
	 * as identified by its row and column index which is given by the arguments 'row' and 'column' respectively.
	 * <p>The argument 'row' and 'column' should be within the bound of [0 height of image-1] and [0 width of image -1]
	 * respectively else this method will throw an ArrayIndexOutOfBounds Exception.
	 * <p>As UInt8PixelImage pixels can represents 256 different shades of gray's corresponding
	 * to the values 0 to 255 from black to white, the argument 'value' should be within this range of
	 * [0 255] else this method will throw an IllegalArgument Exception.
	 * <p>See {@link #getMaxPixel()} and {@link #getMinPixel()} for the range of valid pixel intensities
	 * for this UInt8 PixelImage.
	 * @param int value
	 * @param int row
	 * @param int column
	 * @throws IllegalArgumentException
	 * @throws {@link ArrayIndexOutOfBoundsException}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setPixel(int value,int row,int column) throws IllegalArgumentException
	{
		if(!f3(value))
			throw new IllegalArgumentException();
		else
			this.i1[row][column] = (short) value;
	}

	//	/**
	//	 * This method will return the short 2D array representing the pixel data buffer for this UInt8PixelImage.
	//	 * The height and width of the return 2D array will be similar to the dimensions of this UInt8 PixelImage.
	//	 * <p>The return array will follow UInt8 short array format.
	//	 * 
	//	 * @return short[][]
	//	 * @see #isUInt8ShortArray(short[][])
	//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	//	 */
	//	public short[][] accessPixelDataBuffer()
	//	{
	//		return this.pixelData;
	//	}

	/**
	 * This method will check whether the 2D short array 'uint8' follows a valid
	 * UInt8 short array format.
	 * <p><h4>UInt8 short array format definition</h4>
	 * <p>A 2D short array 'uint8[][]' should satisfy following conditions to
	 * be a valid UInt8 short array format.
	 * <p>(a) All the rows of the 2D short array i.e. uint8[x] should be of equal length.
	 * <p>(b) All the elements of the 'uint8' array should be an integer in the range of [0 255].
	 * <p>(c) uint8.length and uint8[x].length, gives respectively the height and the width of the
	 * UInt8 Pixel Image, where 'x' is any row of that 2D array.
	 * @param short[][] uint8
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static boolean isUInt8Data(short[][] uint8)
	{
		for(int i=0;i<uint8.length;i++)
		{
			if(uint8[i].length != uint8[0].length)
				return false;

			for(int j=0;j<uint8[i].length;j++)
			{
				if(uint8[i][j] <MIN || uint8[i][j]>MAX)
					return false;
			}
		}

		return true;
	}

	/**
	 * This method will convert the Cell 'cell' object to an UInt8PixelImage and return the same.
	 * <p>This row count and column count of the 'cell' will be the Height and Width of the resultant
	 * image.
	 * <p>This method will linearly map the corresponding elements of the 'cell' to an integer in the range of [0 255]
	 * which will be the pixel value for that pixel location in the resultant UInt8PixelImage. The minimum element
	 * of the 'cell' will be mapped to black i.e. '0' pixel value and maximum element of the 'cell' will be mapped to
	 * white i.e. '255' pixel value in the resultant UInt8PixelImage.
	 * <p>As this method maps the element of the 'cell' to an integer in the range of [0 255] representing
	 * a unique shade of gray for rendering the image, the pixel data of the resultant UInt8PixelImage will not
	 * reflect the original pixel data as represented by the Cell 'cell'.
	 * <p>This method should be employed to store or render the given 'cell' as an UInt8PixelImage.
	 * @param Cell cell
	 * @return UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static UInt8PixelImage toUInt8PixelImage(Cell cell) {

		UInt8PixelImage img;

		try{
			img = new UInt8PixelImage(cell.getRowCount(), cell.getColCount());
		}catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}

		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;

		for (int i = 0; i < cell.getRowCount(); i++) 
		{
			for (int j = 0; j < cell.getColCount(); j++) 
			{
				float value = cell.getElement(i,j);

				if(value>max)
					max=value;
				if(value<min)
					min=value;
			}
		}

		try
		{
			if(max == min)
			{
				for (int i = 0; i < img.getHeight(); i++) 
				{
					for (int j = 0; j < img.getWidth(); j++)
					{
						img.setPixel(MAX,i,j);

					}
				}
			}
			else
			{
				float slope = MAX/(max-min);

				for (int i = 0; i < img.getHeight(); i++) 
				{
					for (int j = 0; j < img.getWidth(); j++) 
					{
						int value = Math.round((cell.getElement(i,j) - min) * slope);
						img.setPixel(value, i,j);
					}
				}
			}
		}
		catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}
		return img;
	}
	/**
	 * This method return the Clone of the given UInt8PixelImage object.
	 * @return UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt8PixelImage clone()
	{
		try {
			short[][] clone = new short[getHeight()][getWidth()];

			for (int i = 0; i < clone.length; i++) {
				System.arraycopy(i1[i], 0, clone[i], 0, clone[i].length);
			}
			return new UInt8PixelImage(clone);
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}			
	}

	private byte[] f4(short[][] data) {
		int width = data[0].length;
		int height = data.length;
		byte result[] = new byte[width * height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				result[y * width + x] = (byte) (data[y][x] & 0xFF);
			}
		}
		return result;

	}

	/**
	 * This method return the Image Type for this UInt8PixelImage.
	 * <p>
	 * The return type will be one of the PixelImage type as define in the Enum {@link PixelImageType}.
	 * 
	 * @return {@link PixelImageType}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PixelImageType getImageType() {
		return PixelImageType.UInt8;
	}

	/**
	 * This method will convert the given 8 bits monochrome UInt8 PixelImage
	 * to a Pseudo RGB PixelImage and return the same as a RGBPixelImage.
	 * <p>Each pixel value of the given UInt8 PixelImage representing a shade
	 * of gray in the range of [0 255] will be uniquely mapped to a RGB
	 * colour using the RGBMapper as given by the argument 'map'.
	 * <p>The dimensions of the return RGBPixelImage will be similar to
	 * that of the given UInt8 PixelImage.
	 * 
	 * @param RGBMapper map
	 * @return RGBPixelImage
	 * @see RGBMapper
	 * @see RGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public RGBPixelImage mapToRGBPixelImage(RGBMapper map)
	{
		int h = getHeight();
		int w = getWidth();

		short[][] red = new short[h][w];
		short[][] green = new short[h][w];
		short[][] blue = new short[h][w];

		for(int i=0;i< h;i++)
		{
			for(int j=0;j< w;j++)
			{
				red[i][j] = map.getRedComponent(i1[i][j]);
				green[i][j] = map.getGreenComponent(i1[i][j]);
				blue[i][j] = map.getBlueComponent(i1[i][j]);
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
	 * This method will convert the given 8 bits monochrome UInt8 PixelImage
	 * to a Pseudo ARGB PixelImage and return the same as an ARGBPixelImage.
	 * <p>Each pixel value of the given UInt8 PixelImage representing a shade
	 * of gray in the range of [0 255] will be uniquely mapped to a RGB
	 * colour using the RGBMapper as given by the argument 'map'. The alpha
	 * or A component will be assign a value of 255 for all pixel values.
	 * <p>The dimensions of the return ARGBPixelImage will be similar to
	 * that of the given UInt8 PixelImage.
	 * 
	 * @param RGBMapper map
	 * @return ARGBPixelImage
	 * @see RGBMapper
	 * @see ARGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ARGBPixelImage mapToARGBPixelImage(RGBMapper map)
	{
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
				red[i][j] = map.getRedComponent(i1[i][j]);
				green[i][j] = map.getGreenComponent(i1[i][j]);
				blue[i][j] = map.getBlueComponent(i1[i][j]);
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
	 * This method will convert the given UInt8 PixelImage to an UInt16 PixelImage
	 * and return the same.
	 * <p>The return UInt16 PixelImage with 16 bits of depth, is the high contrast/depth 
	 * representation of the given UInt8 PixelImage with 8 bits of depth.
	 * <p>The UInt16 PixelImage is generated by linearly mapping the pixel intensities of this
	 * UInt8 PixelImage to a 16 bits range of [0 65535].
	 * @return UInt16PixelImage
	 * @see UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UInt16PixelImage toUInt16PixelImage()
	{
		UInt16PixelImage img;

		try{
			img = new UInt16PixelImage(getHeight(),getWidth());
		}catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}

		float slope = (1.0f/MAX);
		float _16Max = UInt16PixelImage.MAX;

		try{
			for(int i=0;i<img.getHeight();i++)
			{
				for(int j=0;j<img.getWidth();j++)
				{
					int pix = Math.round(_16Max*(this.getPixel(i,j)*slope));
					img.setPixel(pix, i,j);
				}
			}
		}catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}

		return img;
	}

	//	/**
	//	 * This method display the given UInt8PixelImage as a gray scale intensity image on an {@link ImageViewer}.
	//	 * <p>The argument 'title' specify the Title for the ImageViewer.
	//	 * @param String title
	//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	//	 */
	//	public void display(String title) 
	//	{
	//		BufferedImage img = toBufferedImage();
	//		try{
	//			ImageViewer frame = new ImageViewer();
	//			frame.setTitle(title);
	//			frame.display(img, null);
	//		}catch(DimensionMismatchException e)
	//		{
	//			throw new BugEncounterException();
	//		}
	//
	//	}

	/**
	 * This method will return the Maximum valid Pixel Intensity for this UInt8 PixelImage.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMaxValidPixel() 
	{
		return MAX;
	}

	/**
	 * This method will return the Minimum valid Pixel Intensity for this UInt8 PixelImage.
	 * <p>This will always be '0' for all UInt PixelImages.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMinValidPixel() 
	{
		return MIN;
	}

	/**
	 * This method will return the Depth of the given UInt8 PixelImage. 
	 * <p>UInt PixelImage with depth 'd' has <i> 2^d </i> different Gray Scale levels.
	 * <p>UInt8 PixelImage with depth of '8' has 2^8 or 256 different Gray Scale
	 * levels.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getDepth() {
		return i3;
	}

	private boolean f3(int pixel)
	{
		if(pixel<MIN || pixel >MAX)
			return false;
		else 
			return true;
	}

	/**
	 * This method return a compatible blank UInt8 PixelImage with all 0's and dimension as that 
	 * of the given UInt8 PixelImage. 
	 * @return AbstractPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractPixelImage getEquivalentBlankImage()
	{
		try {
			return new UInt8PixelImage(getHeight(),getWidth());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will promote the given UInt8 PixelImage to a {@link RGBPixelImage} and
	 * return the same.
	 * <p>The 8 bits Gray band for this UInt8 PixelImage would be copied into the 8 bits Red,
	 * Green and Blue band of the RGB PixelImage so return.
	 * @return RGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public RGBPixelImage toRGBPixelImage()
	{
		UInt8PixelImage r = this.clone();
		UInt8PixelImage g = this.clone();
		UInt8PixelImage b = this.clone();

		try {
			return new RGBPixelImage(r,g,b);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will promote the given UInt8 PixelImage to an {@link ARGBPixelImage} and
	 * return the same.
	 * <p>The 8 bits Gray band for this UInt8 PixelImage would be copied into the 8 bits Red,
	 * Green and Blue band of the ARGB PixelImage so return. Further, Alpha band would
	 * have full luminance (255) for all pixels.
	 * @return ARGBPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ARGBPixelImage toARGBPixelImage()
	{
		int h = getHeight();
		int w = getWidth();

		short[][] alpha = new short[h][w];

		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				alpha[i][j] = 255;
			}
		}

		try{
			UInt8PixelImage a = new UInt8PixelImage(alpha);
			UInt8PixelImage r = this.clone();
			UInt8PixelImage g = this.clone();
			UInt8PixelImage b = this.clone();

			return new ARGBPixelImage(a,r,g,b);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will import an external image as specified by the argument 'path', as an
	 * {@link UInt8PixelImage} and return the same.
	 * <p>This method uses the ImageIO framework to load the external image first as {@link BufferedImage}
	 * and subsequently transform the same to an {@link UInt8PixelImage}.
	 * <p>Based upon the type of BufferedImage so imported, a suitable transform is applied to convert the
	 * same to an equivalent UInt8 PixelImage.
	 * <p>The external image file to be imported should have a valid platform supported Image Format 
	 * (like 'PNG', 'JPEG','GIFF' etc) else this method will throw an UnSupportedImageFormat Exception.
	 * Supported Image Format depends upon the platform.
	 * <p>See {@link PixelImage#getImportFormatsSupported()} for the list of platform supported Image Formats.
	 * <p>If this method encounters any IO error an IO Exception will be thrown.
	 * 
	 * @param String
	 *            path
	 * @return UInt8PixelImage
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static UInt8PixelImage importImage(String path) throws IOException,UnSupportedImageFormatException 
	{
		BufferedImage image = PixelImage.importAsBufferedImage(path);
		return UInt8PixelImage.toUInt8PixelImage(image);
	}

	/**
	 * This method will transform the {@link BufferedImage} 'image' to an {@link UInt8PixelImage} and
	 * return the same.
	 * <p>Based upon the type of {@link BufferedImage} one of the following conversion rule is 
	 * applied :
	 * <p><i>BufferedImage.TYPE_BYTE_GRAY</i> : Such a BufferedImage is comparable to an UInt8 PixelImage and
	 * thus directly transformed into an UInt8 PixelImage.
	 * <p><i>BufferedImage.TYPE_USHORT_GRAY</i> : Such a BufferedImage is comparable to an UInt16 PixelImage and
	 * thus directly transformed first to an UInt16 PixelImage which is subsequently converted to an equivalent 
	 * UInt8 PixelImage.
	 * <p><i>BufferedImage.TYPE_INT_RGB</i> : Such a BufferedImage is comparable to a RGB PixelImage and
	 * thus directly transformed first to a RGB PixelImage which is subsequently converted to an equivalent 
	 * UInt8 PixelImage.
	 * <p><i>Default Any other type</i> : Such a BufferedImage is changed first to an BufferedImage of type
	 * "TYPE_INT_ARGB", which is comparable to an ARGB PixelImage and thus directly transformed to an ARGB 
	 * PixelImage, which is subsequently converted to an equivalent UInt8 PixelImage.
	 * 
	 * @param BufferedImage image
	 * @return UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static UInt8PixelImage toUInt8PixelImage(BufferedImage image)
	{
		int imageType = image.getType();

		try
		{
			if (imageType == BufferedImage.TYPE_BYTE_GRAY) 
			{
				short data[][] = f2(image);
				return new UInt8PixelImage(data);

			} else if (imageType == BufferedImage.TYPE_USHORT_GRAY)
			{
				int data[][] = f1(image);
				return new UInt16PixelImage(data).toUInt8PixelImage();

			} else if (imageType == BufferedImage.TYPE_INT_RGB)
			{
				short data[][][] = f3(image);
				int h = data[0].length;
				int w = data[0][0].length;

				UInt8PixelImage img = new UInt8PixelImage(h, w);

				int value;

				for (int i = 0; i < h; i++) {
					for (int j = 0; j < w; j++) {
						value = Math.round(0.3f * data[1][i][j] + 0.59f * data[2][i][j] + 0.11f * data[3][i][j]);
						img.setPixel(value, i, j);
					}
				}
				return img;
			} else 
			{
				short data[][][] = f0(image);
				int h = data[0].length;
				int w = data[0][0].length;

				UInt8PixelImage img = new UInt8PixelImage(h, w);

				int value;

				for (int i = 0; i < h; i++) {
					for (int j = 0; j < w; j++) {
						value = Math.round(0.3f * data[1][i][j] + 0.59f * data[2][i][j] + 0.11f * data[3][i][j]);
						img.setPixel(value, i, j);
					}
				}

				return img;
			}
		}
		catch(PixelImageFormatException e)
		{	throw new BugEncounterException();
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
		return i7;
	}
}
