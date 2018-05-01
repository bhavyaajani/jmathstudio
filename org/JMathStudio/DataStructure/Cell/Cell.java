package org.JMathStudio.DataStructure.Cell;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.JMathStudio.DataStructure.Generic.Index2D;
import org.JMathStudio.DataStructure.Generic.Index2DList;
import org.JMathStudio.DataStructure.Iterator.Iterator2D.CellIterator;
import org.JMathStudio.DataStructure.Iterator.Iterator2D.Iterator2DBound;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.Exceptions.UnSupportedImageFormatException;
import org.JMathStudio.PixelImageToolkit.PixelImage;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBMapper;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt16PixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt8PixelImage;
import org.JMathStudio.VisualToolkit.Viewer.ImageViewer;

/**
 * This class define a 2D data structure 'Cell' which stores scalar (float) elements in 2D.
 * <p>
 * <h3>Notations</h3>
 * <p>
 * Internally, a Cell is represented by a two dimensional array of floating
 * point elements.
 * </p>
 * <p>
 * <h4>Row</h4> - An individual horizontal structure of the Cell. Indexing start
 * from 0 to one less than the number of row count i.e. numbers of row.
 * <p>
 * <h4>Column</h4> - An individual vertical structure of the Cell. Indexing
 * start from 0 to one less than the number of column count i.e. numbers of
 * column.
 * <p>
 * <h4>Row Count</h4> - Number of rows for the given Cell (also Height).
 * <p>
 * <h4>Column Count</h4> - Number of columns for the given Cell (also Width).
 * <p>
 * Each Row of Cell should have same number of Column elements or each Row
 * should be of same length.
 * <p>
 * Cell being a 2D data structure, each element of the Cell can be uniquely identified by its
 * index location within 2D index space by its row and the column index. Thus in row-column
 * space an element located at row index 'i' [0 to Row Count) and column index 'j' [0 to Column
 * Count) shall be represented as,
 * <h4>Cell(i,j)</h4>
 * <h3>Cell Representation</h3>
 * <p>
 * Within the toolkit, a Cell is used to represent the following :
 * <p>
 * 1 Real scalar matrix, where each element of the matrix is identified by its
 * row and column position in the Cell uniquely. Row and Column indexing will
 * start from 0.
 * <p>
 * 2 2D Discrete Random Variable.
 * <p>
 * 3 Discrete Real Image. Cell will represent the raw gray scale pixel
 * intensities of an image. With this representation, row and column count of
 * the scale is equal to the image height and width respectively.
 * <h3>Geometry</h3>
 * <h4>Spatial Geometry</h4>
 * <p>
 * A Cell object default 2D geometry should be interpreted as follows,
 * <p>
 * The Rows of Cell in increasing order starting from '0' points along positive
 * upward Y axis and Columns of Cell in increasing order starting from '0'
 * points along the positive X axis towards right direction.
 * <p>
 * Having said that, entire Cell object will reside in the 1st Quadrant with the
 * first element of the Cell i.e. Cell(0,0) located at the Origin.
 * <p>
 * <h4>Spatial Centred Geometry</h4>
 * <p>
 * The centred geometry for a Cell object should be interpreted as follows,
 * <p>
 * The origin will be at the centre of the Cell with 'X' and 'Y' axis align
 * along the columns (width) and rows (height) of the Cell respectively.
 * <p>
 * <b>By default "Default Geometry" will apply, otherwise if specified</b>.
 * <h3>Cell space to Display space transform</h3>
 * <p>
 * When the given Cell is displayed on a suitable Viewer as an image,
 * the coordinate transform from Cell space to Viewer/display space shall map
 * the point (0,0) of the Cell to the upper left corner of the Viewer geometry.
 * <p>
 * These transform results in the flip of the 'Y' row axis on the Viewer
 * geometry such that the Cell's positive 'Y' row axis now points downward on
 * the Viewer geometry.
 * <p>
 * The Cell's 'X' column axis is not affected by the coordinate transform and
 * positive 'X' column axis continue to point towards right direction on the
 * Viewer geometry.
 * <pre>Usage:
 * Cell image = Cell.importImageAsCell("path");//Import external image as Cell.
 * 
 * int row_count = image.getRowCount();//Get row count (height) of the Cell.
 * int col_count = image.getColCount();//Get column count (width) of the Cell.
 * 
 * image.showAsUInt8Image("Title");//Show image on viewer.
 * 
 * float[][] data = image.accessCellBuffer();//Access internal data buffer of the Cell.
 * 
 * image.exportAsUInt8Image("D:\\", "export.jpg");//Export Cell as an image.
 * 
 * Vector col = image.getColumn(i);//Get a specified column from the Cell.
 * 
 * float ele = image.getElementWithPadding(i, j);//Get virtual element by padding for indexes
 * falling outside Cell.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Cell implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1123824457249872610L;
	//As Cell is Serializable, all fields should be either transient or Serializable.
	float[][] i4;
	private transient CellIterator iterator = null;

	/**
	 * This will create a Cell with given number of Rows and Columns as
	 * specified by the argument 'rowCount' and 'colCount' respectively. Each
	 * element of Cell will gave default value of 0.
	 * <p>
	 * The argument 'rowCount' and 'colCount' should be more than 0.
	 * 
	 * @param int rowCount
	 * @param int colCount
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell(int rowCount, int colCount) {
		this.i4 = new float[rowCount][colCount];
	}

	/**
	 * This will create a Cell with given float cell elements as represented by
	 * the 2D float array 'cell'.
	 * <p>
	 * Each row of the array 'cell' should be of the same length else this
	 * method will throw an IllegalCellFormat Exception.
	 * <p>The argument 'cell' is passed as reference and no deep copy of the array is made.
	 * 
	 * @param float[][] cell
	 * @throws IllegalCellFormatException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell(float[][] cell) throws IllegalCellFormatException {
		for (int i = 1; i < cell.length; i++) {
			if (cell[i].length != cell[0].length)
				throw new IllegalCellFormatException();
		}
		this.i4 = cell;
	}

	/**
	 * This method will return the 2D float array buffer representing the given
	 * Cell internally.
	 * 
	 * @return float[][]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[][] accessCellBuffer() {
		return this.i4;
	}

	/**
	 * This method will return the number of rows for the given Cell.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getRowCount() {
		return this.i4.length;
	}

	/**
	 * This method will return the number of columns for the given Cell.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getColCount() {
		// Assumption that all column are of equal length.
		return this.i4[0].length;
	}

	/**
	 * This method will return the required row of the given Cell as specified
	 * by the argument 'index'. The required row is returned as a Vector object.
	 * <p>
	 * The indexing of the row starts from 0. If argument 'index' is not in the
	 * range of [0 RowCount-1], this method will throw an ArrayIndexOutOfBound
	 * Exception.
	 * 
	 * @param int index
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessRow(int index) {
		return new Vector(i4[index]);
	}

	/**
	 * This method will return the required column of the given Cell as
	 * specified by the argument 'index'. The required column is returned as a
	 * Vector object.
	 * <p>
	 * The indexing of the column starts from 0. If argument 'index' is not in
	 * the range of [0 ColumnCount - 1], this method will throw an
	 * ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector getColumn(int index) {
		Vector result = new Vector(getRowCount());

		for (int i = 0; i < result.length(); i++)
			result.setElement(i4[i][index], i);

		return result;
	}

	/**
	 * This method will replace the specified row element given by the argument
	 * 'index' of the Cell with the row element as given by the Vector argument
	 * 'row'.
	 * <p>
	 * The argument 'row' should be in the range of [0 RowCount -1] for this
	 * Cell else an ArrayIndexOutOfBound Exception will be thrown.
	 * <p>
	 * The length of the Vector should be same as that of the column count for
	 * this Cell else this method will throw an DimensionMismatch Exception.
	 * 
	 * @param Vector
	 *            row
	 * @param int index
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignRow(Vector vector, int row)
	throws DimensionMismatchException {
		if (vector.length() != getColCount())
			throw new DimensionMismatchException();

		this.i4[row] = vector.accessVectorBuffer();
	}

	/**
	 * This method will return the element present at the given row and column
	 * position as specified by the arguments 'row' and 'column' respectively.
	 * <p>
	 * If the arguments 'row' and 'column' over shoot valid range of indexing,
	 * an ArrayIndexOutOfBound Exception will be thrown.
	 * 
	 * @param int row
	 * @param int column
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getElement(int row, int column) {
		return i4[row][column];
	}
	
	/**
	 * This method will return the element present at the given row and column
	 * index position as specified by the {@link Index2D} 'index'.
	 * <p>If the index position as specified by Index2D 'index' is outside the valid
	 * index bounds of the Cell, an ArrayIndexOutOfBound Exception will be thrown.
	 * 
	 * @param Index2D index
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getElement(Index2D index) {
		return i4[index.getY()][index.getX()];
	}

	/**
	 * This method will replace the element present at the given row and column
	 * position as specified by the arguments 'row' and 'column' respectively by
	 * the element specified by argument 'element'.
	 * <p>
	 * If the arguments 'row' and 'column' over shoot valid range of indexing,
	 * an ArrayIndexOutOfBound Exception will be thrown.
	 * 
	 * @param float element
	 * @param int row
	 * @param int column
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(float element, int row, int column) {
		this.i4[row][column] = element;
	}
	
	/**
	 * This method will replace the element present at the given row and column
	 * index position as specified by the {@link Index2D} 'index' by the element 
	 * specified by argument 'element'.
	 * <p>If the index position as specified by Index2D 'index' is outside the valid
	 * index bounds of the Cell, an ArrayIndexOutOfBound Exception will be thrown.
	 *
	 * @param float element
	 * @param Index2D index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(float element, Index2D index) {
		this.i4[index.getY()][index.getX()] = element;
	}
	
	/**
	 * This method will replace all the elements of the Cell located at the index positions as
	 * specified by {@link Index2DList} 'indexes' with element specified by argument 'element'.
	 * <p>If the index position as specified by any of the Index2D within the Index2DList is 
	 * outside the valid index bounds of the Cell, an ArrayIndexOutOfBound Exception will be thrown.
	 *
	 * @param float element
	 * @param Index2DList indexes
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAllElements(float element, Index2DList indexes)
	{
		for(int i=0;i<indexes.size();i++)
		{
			this.setElement(element, indexes.access(i));
		}
	}
	
	/**
	 * This method will replace all the elements of the Cell equal to the argument 'element' with 
	 * element specified by the argument 'replace'.
	 * @param float element
	 * @param float replace
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void replaceAllElements(float element,float replace)
	{
		final int rc = getRowCount();
		final int cc = getColCount();
		
		for(int i=0;i<rc;i++)
		{
			for(int j=0;j<cc;j++)
			{
				if(i4[i][j] == element){
					i4[i][j] = replace;
				}
			}
		}
	}

	/**
	 * This method will return the clone of the given Cell object.
	 * <p>
	 * The clone is the exact replica of the original Cell with same dimensions.
	 * 
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell clone() {
		Cell result = new Cell(this.getRowCount(), this.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			System.arraycopy(this.accessRow(i).accessVectorBuffer(), 0, result.accessRow(i)
					.accessVectorBuffer(), 0, result.getColCount());
		}

		return result;
	}

	/**
	 * This method will convert the given Cell to an UInt16PixelImage and
	 * display the same on an {@link ImageViewer}.
	 * <p>
	 * Cell will be converted to an UInt16 pixel image by mapping linearly the
	 * elements of the Cell to an UInt16 pixel range of [0 65535].
	 * <p>
	 * The argument 'title' specify the Title for the ImageViewer.
	 * 
	 * @param String
	 *            title
	 * @see UInt16PixelImage#toUInt16PixelImage(Cell)
	 * @see UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showAsUInt16Image(String title) {

		BufferedImage img = UInt16PixelImage.toUInt16PixelImage(this)
		.toBufferedImage();

		try {
			ImageViewer frame = new ImageViewer();
			frame.setTitle(title);
			frame.display(img, this);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will convert the given Cell to an UInt8PixelImage and display
	 * the same on an {@link ImageViewer}.
	 * <p>
	 * Cell will be converted to an UInt8 pixel image by mapping linearly the
	 * elements of the Cell to an UInt8 pixel range of [0 255].
	 * <p>
	 * The argument 'title' specify the Title for the ImageViewer.
	 * 
	 * @param String
	 *            title
	 * @see UInt8PixelImage#toUInt8PixelImage(Cell)
	 * @see UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showAsUInt8Image(String title) {

		BufferedImage img = UInt8PixelImage.toUInt8PixelImage(this)
		.toBufferedImage();

		try {
			ImageViewer frame = new ImageViewer();
			frame.setTitle(title);
			frame.display(img, this);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will convert the given Cell to a False/Pseudo RGBPixelImage
	 * and display the same on an {@link ImageViewer}.
	 * <p>
	 * The elements of the Cell will be linearly mapped to an UInt8 Pixel image
	 * range of [0 255] and subsequently each mapped value will be assign a
	 * colour as define by the RGBMapper 'mapper'.
	 * <p>
	 * The resultant RGBPixelImage displayed on the ImageViewer will be the
	 * false colour representation of the given Cell.
	 * <p>
	 * The argument 'title' specify the Title for the ImageViewer.
	 * 
	 * @param RGBMapper
	 *            mapper
	 * @param String
	 *            title
	 * @see UInt8PixelImage#toUInt8PixelImage(Cell)
	 * @see UInt8PixelImage#mapToRGBPixelImage(RGBMapper)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showAsPseudoRGBImage(RGBMapper mapper, String title) {

		BufferedImage img = UInt8PixelImage.toUInt8PixelImage(this)
		.mapToRGBPixelImage(mapper).toBufferedImage();

		try {
			ImageViewer frame = new ImageViewer();
			frame.setTitle(title);
			frame.display(img, this);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will load an external image located at the path as given by
	 * the argument 'path' and return the equivalent gray scale pixel
	 * intensities of the image as Cell.
	 * <p>
	 * The row and column count of the return Cell gives the height and width of
	 * the loaded image.
	 * <p>
	 * If the external image is an ARGB or RGB colour image, this method will
	 * return the equivalent gray scale pixel intensities of the given colour
	 * image.
	 * <p>
	 * If the external image is a gray scale intensity image, this method will
	 * return the pixel intensities of the image as such.
	 * <p>
	 * This method uses the ImageIO framework to load the external image as
	 * {@link BufferedImage} and extracts (and maps to gray scale if colour
	 * image) pixel intensities.
	 * <p>
	 * The external image file to be imported as Cell should be a valid platform
	 * supported Image Format (like 'PNG', 'JPEG','GIFF' etc) else this method
	 * will throw an UnSupportedImageFormat Exception. Supported Image Format
	 * depends upon the platform.
	 * <p>
	 * See {@link PixelImage#getImportFormatsSupported()} for the list of
	 * supported Image Formats for the current platform.
	 * <p>
	 * If this method encounters any IO issues an IO Exception will be thrown.
	 * 
	 * @param String
	 *            path
	 * @return Cell
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @see {@link PixelImage#getImportFormatsSupported()}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell importImageAsCell(String path) throws IOException,
	UnSupportedImageFormatException {
		BufferedImage image = PixelImage.importAsBufferedImage(path);
		return toCell(image);
	}

	/**
	 * This method will extract the image pixel values from the {@link BufferedImage} 'image' and maps the
	 * same to an equivalent gray scale intensity (if image is multi-band colour image) and return the
	 * same as a {@link Cell}.
	 * <p>Based upon the type of {@link BufferedImage} one of the following conversion rule is 
	 * applied :
	 * <p><i>BufferedImage.TYPE_BYTE_GRAY</i> : Such a BufferedImage is comparable to data model for UInt8 PixelImage
	 * and thus return Cell has values in the range of [0 255].
	 * <p><i>BufferedImage.TYPE_USHORT_GRAY</i> : Such a BufferedImage is comparable to data model for UInt16 PixelImage
	 * and thus return Cell has values in the range of [0 65535].
	 * <p><i>BufferedImage.TYPE_INT_RGB</i> : Such a BufferedImage is comparable to data model for RGB PixelImage
	 * and has 3 colour bands; Red, Green and Blue per pixel. Each of the RGB pixel is map to its equivalent gray scale
	 * value in the range of [0 255].
	 * <p><i>Default Any other type</i> : Such a BufferedImage is converted first to an equivalent BufferedImage.TYPE_INT_ARGB
	 * which is comparable to data model for ARGB PixelImage and has 4 colour bands; Alpha, Red, Green and Blue per pixel. 
	 * Each of the ARGB pixel is map to its equivalent gray scale value in the range of [0 255].
	 * 
	 * @param BufferedImage image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell toCell(BufferedImage image)
	{
		int imageType = image.getType();

		int height = image.getHeight();
		int width = image.getWidth();
		Cell result = new Cell(height, width);

		if (imageType == BufferedImage.TYPE_BYTE_GRAY) {
			DataBuffer buff = image.getData().getDataBuffer();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					result.setElement(buff.getElem(y * width + x), y, x);
				}
			}

		} else if (imageType == BufferedImage.TYPE_USHORT_GRAY) {
			DataBufferUShort buff = (DataBufferUShort) image.getData()
			.getDataBuffer();
			int correction = UInt16PixelImage.MAX + 1;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int tmp = buff.getElem(y * width + x);
					// Add correction to wrap up data in range of [-32768 32767]
					// to UInt16 range of [0 65535]

					if (tmp < 0) {
						tmp += correction;
					}

					result.setElement(tmp, y, x);
				}
			}
		} else if (imageType == BufferedImage.TYPE_INT_RGB) {
			float value;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int argb = image.getRGB(x, y);

					@SuppressWarnings("unused")
					int alpha = (argb >> 24) & 0xff;
					int red = (argb >> 16) & 0xff;
					int green = (argb >> 8) & 0xff;
					int blue = argb & 0xff;

					value = (float) (0.3f * red + 0.59f * green + 0.11 * blue);
					result.setElement(value, y, x);
				}
			}
		} else {
			// By default create ARGB. getRGB() method does color model conversion
			//if default is not ARGB.
			float value;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int argb = image.getRGB(x, y);

					@SuppressWarnings("unused")
					int alpha = (argb >> 24) & 0xff;
					int red = (argb >> 16) & 0xff;
					int green = (argb >> 8) & 0xff;
					int blue = argb & 0xff;

					value = (float) (0.3f * red + 0.59f * green + 0.11 * blue);
					result.setElement(value, y, x);
				}
			}
		}

		return result;
	}
	/**
	 * This method will read multiple images located in the directory as
	 * specified by the argument 'directory' using the
	 * {@link #importImageAsCell(String)} method and will return the pixel data
	 * for all the read images as a CellStack.
	 * <p>
	 * Each Cell of the return CellStack will contain pixel data for one image
	 * read.
	 * <p>
	 * If the argument 'directory' does not point to a directory path this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * If this method encounter any IO error during image loading an IO
	 * Exception will be thrown.
	 * <p>
	 * Depending upon the platform if any image file format is not supported
	 * this method will throw an UnSupportedImageFormat Exception. See
	 * {@link PixelImage#getImportFormatsSupported()} for the list of Image
	 * Format supported by the current platform. Ensure that the directory in question
	 * contains only the image files with supported Image Format.
	 * 
	 * @param String
	 *            directory
	 * @return CellStack
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws UnSupportedImageFormatException
	 * @see #importImageAsCell(String)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CellStack importMultipleImagesAsCell(String directory)
	throws IOException, IllegalArgumentException,
	UnSupportedImageFormatException {
		File dir = new File(directory);
		Cell[] images = null;

		if (dir.isDirectory()) {
			String files[] = dir.list();

			int n = 0;

			for (int i = 0; i < files.length; i++) {
				String filePath = dir.getAbsolutePath().concat("\\" + files[i]);
				File test = new File(filePath);

				if (test.isFile() && !test.isHidden()) {
					n++;
				}

			}

			images = new Cell[n];

			n = 0;

			for (int i = 0; i < images.length; i++) {
				String filePath = dir.getAbsolutePath().concat(
						"\\" + files[n++]);
				File test = new File(filePath);

				if (test.isFile()) {
					images[i] = importImageAsCell(filePath);
				} else {
					i--;
				}
			}

			return new CellStack(images);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * This method will convert the given Cell to an UInt8PixelImage and export
	 * the same as an Image to external path as specified by the argument 'path'
	 * as an image file with file name as given by the argument 'fileName'. Note
	 * argument 'path' should not end with '/' or '\\'.
	 * <p>
	 * As this method first convert the given Cell to an UInt8PixelImage before
	 * exporting the same as an image, the exported image represents the
	 * rendered Gray Scale image for the given Cell and pixel data of such an
	 * image does not correlates to the elements of the given Cell.
	 * <p>
	 * The argument 'fileName' specify the file name for exporting the given
	 * Cell as an external image. The argument 'fileName' should have a file
	 * name and an extension separated by a '.'. (Note : argument 'fileName'
	 * should have only one '.' to separate extension from the file name).
	 * <p>
	 * The extension of the argument 'fileName' like '.png','.gif','.jpg' will
	 * define the Image Format for exporting the given Cell as an external Image
	 * file. Use supported Image Format which depends upon the platform. See
	 * {@link #getExportFormatsSupported()} method for the list of Image Format
	 * supported on the current platform.
	 * <p>
	 * If the Image Format for exporting the given Cell is not supported by the
	 * platform this method will throw an UnSupportedImageFormat Exception.
	 * <p>
	 * If this method encounters any IO error an IO Exception will be thrown.
	 * 
	 * @param String
	 *            path
	 * @param String
	 *            fileName
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @see {@link PixelImage#getExportFormatsSupported()}
	 * @see UInt8PixelImage#toUInt8PixelImage(Cell)
	 * @see UInt8PixelImage#exportImage(String, String)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void exportAsUInt8Image(String path, String fileName)
	throws IOException, UnSupportedImageFormatException {
		UInt8PixelImage img = UInt8PixelImage.toUInt8PixelImage(this);

		img.exportImage(path, fileName);
	}

	/**
	 * This method will convert the given Cell to an UInt16PixelImage and export
	 * the same as an Image to external path as specified by the argument 'path'
	 * as an image file with file name as given by the argument 'fileName'. Note
	 * argument 'path' should not end with '/' or '\\'.
	 * <p>
	 * As this method first convert the given Cell to an UInt16PixelImage before
	 * exporting the same as an image, the exported image represents the
	 * rendered Gray Scale image for the given Cell and pixel data of such an
	 * image does not correlates to the elements of the given Cell.
	 * <p>
	 * The argument 'fileName' specify the file name for exporting the given
	 * Cell as an external image. The argument 'fileName' should have a file
	 * name and an extension separated by a '.'. (Note : argument 'fileName'
	 * should have only one '.' to separate extension from the file name).
	 * <p>
	 * The extension of the argument 'fileName' like '.png','.gif','.jpg' will
	 * define the Image Format for exporting the given Cell as an external Image
	 * file. Use supported Image Format which depends upon the platform. See
	 * {@link #getExportFormatsSupported()} method for the list of Image Format
	 * supported on the current platform.
	 * <p>
	 * If the Image Format for exporting the given Cell is not supported by the
	 * platform this method will throw an UnSupportedImageFormat Exception.
	 * <p>
	 * If this method encounters any IO error an IO Exception will be thrown.
	 * 
	 * @param String
	 *            path
	 * @param String
	 *            fileName
	 * @throws IOException
	 * @throws UnSupportedImageFormatException
	 * @see {@link PixelImage#getExportFormatsSupported()}
	 * @see UInt16PixelImage#toUInt16PixelImage(Cell)
	 * @see UInt16PixelImage#exportImage(String, String)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void exportAsUInt16Image(String path, String fileName)
	throws IOException, UnSupportedImageFormatException {
		UInt16PixelImage img = UInt16PixelImage.toUInt16PixelImage(this);

		img.exportImage(path, fileName);
	}

	/**
	 * This method will put the elements of this Cell in an ordered structure to
	 * a Standard Output Stream.
	 * <p>
	 * This method is useful to display the elements of the given Cell on the
	 * console if the dimensions of the Cell are small enough for console to
	 * hold the displayed elements.
	 */
	public void dumpToStandardOPStream() {
		System.out.println();

		for (int i = 0; i < getRowCount(); i++) {
			for (int j = 0; j < getColCount(); j++) {
				System.out.print(getElement(i, j) + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * This method will save the current state of this {@link Cell} object to an
	 * external file with file name as specified by the argument 'filename' and
	 * path as specified by the argument 'dirPath'.
	 * <p>
	 * The argument 'filename' should not contain '.' charactor and any
	 * extension. Further an extension '.cell' will be added to the saved state
	 * file by this method.
	 * <p>
	 * The current state of this {@link Cell} object stored in the external file
	 * can be loaded into the framework at later stage using the
	 * {@link #retrieveState(String)} method.
	 * <p>
	 * If the argument 'dirPath' does not specify a valid directory path or if
	 * the method encounters any IO issues during the state save this method
	 * will throw an IO Exception.
	 * 
	 * @param String
	 *            dirPath
	 * @param String
	 *            filename
	 * @throws IOException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void saveState(String dirPath, String filename) throws IOException {
		String filePath = dirPath + filename + ".cell";

		FileOutputStream fs = new FileOutputStream(filePath);
		ObjectOutputStream os = new ObjectOutputStream(fs);

		os.writeObject(this);
		os.close();
		fs.close();

	}

	/**
	 * This method load the saved state of the {@link Cell} object saved in the
	 * external file as specified by the argument 'filePath' and return the same
	 * as a {@link Cell}.
	 * <p>
	 * The argument 'filePath' should provide the full path to an external file
	 * which contain the saved state of a {@link Cell} object. Further the
	 * extension of the file should be '.cell'.
	 * <p>
	 * If the extension of the external file containing the saved state of a
	 * {@link Cell} object is not '.cell' or is not a valid save state file of a
	 * {@link Cell} object this method will throw an Illegal Argument Exception.
	 * <p>
	 * If the argument 'filePath' does not point to a file or if this method
	 * encounters any IO issues an IO Exception will be thrown.
	 * 
	 * @param String
	 *            filePath
	 * @return Cell
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell retrieveState(String filePath) throws IOException,
	IllegalArgumentException {
		String format = filePath.substring(filePath.indexOf('.') + 1);

		if (!format.equalsIgnoreCase("cell"))
			throw new IllegalArgumentException();

		FileInputStream fs = new FileInputStream(filePath);
		ObjectInputStream os = new ObjectInputStream(fs);

		Cell cell = null;

		try {
			cell = (Cell) os.readObject();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException();
		}

		os.close();
		fs.close();

		return cell;
	}

	/**
	 * This method will check if the row and column indexes as specified by the
	 * arguments 'row' and 'column' respectively falls within the valid bounds
	 * of this Cell.
	 * 
	 * @param int row
	 * @param int column
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isWithinBounds(int row, int column) {
		if (row >= 0 && row < i4.length && column >= 0
				&& column < i4[0].length)
			return true;
		else
			return false;
	}
	
	/**
	 * This method will check if the 2D index position as specified by {@link Index2D} 'index'
	 * is within the valid index bounds for this Cell.
	 * @param Index2D index
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isWithinBounds(Index2D index) {
		if (index.getY() >= 0 && index.getY() < i4.length && index.getX() >= 0
				&& index.getX() < i4[0].length)
			return true;
		else
			return false;
	}

	/**
	 * This method will return the element present at the location as specified
	 * by the arguments 'row' and 'column'. The argument 'row' and 'column' can
	 * take any value.
	 * <p>
	 * If the location specified by the arguments 'row' and 'column' falls with
	 * in the valid bounds of this {@link Cell}, the element at that location
	 * with in the Cell will be returned.
	 * <p>
	 * However if the location specified by the arguments 'row' and 'column'
	 * falls outside the bounds of this Cell, the element for that location
	 * shall be interpreted based on the circular periodic extention of the Cell
	 * along both the axis and thus shall be returned.
	 * <p>
	 * This method is useful in the implementation of the functions/algorithm
	 * which extends beyond the Cell's bounds and need to pass the element using
	 * some padding scheme.
	 * 
	 * @param int row
	 * @param int column
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getElementWithPadding(int row, int column) {
		int height = getRowCount();
		int width = getColCount();
		int y, x;

		if (row >= height)
			y = row % height;
		else if (row < 0) {
			row = row % height;
			y = Math.abs(height + row) % height;
		} else
			y = row;

		if (column >= width)
			x = column % width;
		else if (column < 0) {
			column = column % width;
			x = Math.abs(width + column) % width;
		} else
			x = column;

		return getElement(y, x);
	}

	/**
	 * This method check whether the Cell 'cell' has dimensions similar to that
	 * of this Cell.
	 * @param Cell cell
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean hasSameDimensions(Cell cell){

		return (cell.getRowCount() == this.getRowCount()) && (cell.getColCount() == this.getColCount());
	}

	/**
	 * This method will flatten the given 2 dimensional {@link Cell} to a 1 dimensional {@link Vector} and return the same.
	 * <p>The number of elements in the return Vector, i.e length of the Vector, will be equal to the number
	 * of elements, i.e row count * column count, of this Cell.
	 * <p>The Cell is flatten to a Vector by sequentially concating rows of the Cell (in increasing row index)
	 * one after another back to back.
	 * <p>The Cell can be rebuilt from the flatten Vector if the original column count of the Cell is known using
	 * method {@link Vector#promoteToCell(int)}.   
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector flattenToVector()
	{
		try{
			int rc = getRowCount();
			int cc = getColCount();
			int size = rc*cc;

			Vector res = new Vector(size);

			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					res.setElement(i4[i][j], i*cc+j);
				}
			}

			return res;
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will check whether the given Cell is a square Cell.
	 * <p>A square Cell has equal number of rows and columns.
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isSquareCell()
	{
		return (this.getRowCount() == this.getColCount());
	}

	/**
	 * This method computes the bilinear interpolated value for the hypothetical element located at
	 * any index position (Y,X) with in the given Cell, where 'Y' specify the row position and 'X' specify
	 * the column position.
	 * <p>The 'Y' and 'X' can take any integer and non-integer row and column position within the valid index
	 * bounds of the given Cell. 
	 * <p>The value of argument 'Y' and 'X' should be in the range of [0 rowCount-1] and [0 colCount-1]
	 * respectively else this method will throw an IllegalArgument Exception.
	 * @param float Y
	 * @param float X
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float interpolate(float Y,float X) throws IllegalArgumentException
	{
		if(Y<0 || Y>this.getRowCount()-1 || X<0 || X>this.getColCount()-1)
		{
			throw new IllegalArgumentException();
		}
		
		int yl = (int) Math.floor(Y);
		int yh = (int) Math.ceil(Y);
		int xl = (int) Math.floor(X);
		int xh = (int) Math.ceil(X);
		
		if(yl == yh && xl==xh)
		{
			return this.getElement(yl,xl);
		}
		else if(yl == yh)
		{	//Y=yl=yh
			int y=yl;
			return this.getElement(y,xl)*(xh-X) + this.getElement(y,xh)*(X-xl);
		}
		else if(xl == xh)
		{	//X=xl=xh
			int x = xl;
			return this.getElement(yl,x)*(yh-Y) + this.getElement(yh,x)*(X-xl);
		}
		else
		{
			float Yl = this.getElement(yl,xh)*(X-xl) + this.getElement(yl,xl)*(xh-X);
			float Yu = this.getElement(yh,xh)*(X-xl) + this.getElement(yh,xl)*(xh-X);
			
			return Yl*(yh-Y) + Yu*(Y-yl);
		}
	}
	
	/**
	 * This will return an instance of {@link CellIterator} associated with the given Cell.
	 * <p>Each Cell object maintains a single internal instance of CellIterator object. Getting
	 * CellIterator through this method avoid creation of multiple instances of CellIterator associated
	 * with the given Cell.
	 * @return CellIterator
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellIterator getAssociatedIterator(){
		if(iterator == null)
			iterator = new CellIterator(this);
		return iterator;
	}
	
	/**
	 * This method return the largest possible iterable bounds for the given Cell as {@link Iterator2DBound}.
	 * @return Iterator2DBound
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Iterator2DBound getLargestIterableBounds(){
		try {
			return new Iterator2DBound(0,0,getRowCount(),getColCount());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
	
	// private Cell importCell() throws IOException,
	// UnSupportedImageFormatException
	// {
	// FileDialog dg = new FileDialog(new
	// JFrame("Choose the Image to load as Cell : "));
	// dg.setVisible(true);
	//		
	// String path = dg.getDirectory() + dg.getFile();
	//		
	// dg.dispose();
	// return importImageAsCell(path);
	//
	// }
}
