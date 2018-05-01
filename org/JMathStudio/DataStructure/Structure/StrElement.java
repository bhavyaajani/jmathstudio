package org.JMathStudio.DataStructure.Structure;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.PixelImageFormatException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class represents a Structuring Element used in Morphological Image
 * Operations.
 * <p>
 * A structuring element define a morphological structure or shape. Internally this class
 * represent a morphological structuring element as a odd dimensional 2D boolean array.
 * <p>
 * The arrangement of 'True's' in the given 2D boolean array define the shape for structuring element.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class StrElement {

	private boolean[][] f1;

	/**
	 * This will create a Structuring Element object with the structure as
	 * defined by the arrangement of True's in the given 2D boolean array 'str'.
	 * 'True' identify the foreground and 'False' identity the background of the
	 * structuring element.
	 * <p>
	 * Length of each of the row of the 2D boolean array 'str' should be same
	 * i.e. str[x].length should be the same for each row 'x' else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * Also the 2D boolean array 'str' specifying the structure should have odd
	 * dimensions i.e. str.length and str[x].length should be odd number, so as
	 * to have the centre of the structuring element coincide with the pixel
	 * position. Else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'str' is passed by reference and no deep copy of the same is made.
	 * 
	 * @param boolean[][] str
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public StrElement(boolean[][] str)
			throws IllegalArgumentException {
		for (int i = 1; i < str.length; i++) {
			if (str[i].length != str[0].length)
				throw new IllegalArgumentException();
		}

		if (str.length % 2 == 0 || str[0].length % 2 == 0)
			throw new IllegalArgumentException();

		this.f1 = str;
	}

	/**
	 * This will return the Height of the given Structuring Element. Height is the vertical dimension
	 * (number of pixel positions) of the structuring element buffer. 
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getHeight() {
		return this.f1.length;
	}

	/**
	 * This will return the Width of the given Structuring Element. Width is the horizontal dimension
	 * (number of pixel positions) of the structuring element buffer.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getWidth() {
		return this.f1[0].length;
	}

	/**
	 * This will check whether the index position within the given Structuring
	 * Element as specified by its row and column index as given by the argument
	 * 'row' and 'column' respectively fall on a defined structure.
	 * <p>
	 * If the position in question define a structure this method will return
	 * True else it will return False.
	 * <p>
	 * A structure is define by the distribution of 'True's' within this
	 * structuring element. 'True' identify the fore ground while 'False' define
	 * the background of the given structuring element.
	 * <p>
	 * The argument 'row' and 'column' specifying the row and column index of
	 * the position in question should be within the bound [0 height) and [0
	 * width) respectively.
	 * 
	 * @param int row
	 * @param int column
	 * @return boolean
	 * @see #StrElement(boolean[][])
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isStructure(int row, int column) {
		return this.f1[row][column];
	}

	/**
	 * This will return the 2D boolean array buffer representing the given
	 * Structuring Element with in this class.
	 * 
	 * @return boolean[][]
	 * @see #StrElement(boolean[][])
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean[][] accessStrElementBuffer() {
		return this.f1;
	}

	/**
	 * This will create the complimentary structuring element for the given
	 * structuring element and return the same as {@link StrElement}.
	 * <p>
	 * A complimentary structuring element will have a structure complimentary
	 * of that define by this structuring element.
	 * <p>
	 * The 2D boolean array representing such complimentary structuring element
	 * will have 'True' in place of 'False' and vice versa.
	 * 
	 * @return StrElement
	 * @see #StrElement(boolean[][])
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public StrElement getComplimentStrElement() {
		boolean[][] str = new boolean[getHeight()][getWidth()];

		for (int i = 0; i < str.length; i++) {
			for (int j = 0; j < str[0].length; j++) {
				str[i][j] = !this.f1[i][j];
			}
		}

		try {
			return new StrElement(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will return a {@link StrElement} defining a Square structure.
	 * <p>
	 * The argument 'n' here specify the dimension of the square structure and
	 * should be a positive odd integer else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int n
	 * @return {@link StrElement}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public final static StrElement squareStrElement(int n)
			throws IllegalArgumentException {
		if (n < 1 || n % 2 == 0)
			throw new IllegalArgumentException();

		boolean[][] str = new boolean[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				str[i][j] = true;
			}
		}

		return new StrElement(str);
	}

	/**
	 * This will return a {@link StrElement} defining a Horizontal structure.
	 * <p>
	 * The argument 'n' here specify the horizontal dimension of the given
	 * structure and it should be a positive odd integer else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * <p>
	 * A horizontal structure is a horizontal structure with a unit height.
	 * 
	 * @param int n
	 * @return {@link StrElement}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static StrElement horizontalStrElement(int n)
			throws IllegalArgumentException {
		if (n < 1 || n % 2 == 0)
			throw new IllegalArgumentException();

		boolean[][] str = new boolean[1][n];

		for (int i = 0; i < n; i++)
			str[0][i] = true;

		return new StrElement(str);

	}

	/**
	 * This will return a {@link StrElement} defining a Vertical structure.
	 * <p>
	 * The argument 'n' here specify the vertical dimension of the given
	 * structure and it should be a positive odd integer else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * <p>
	 * A vertical structuring element is a vertical structure with a unit
	 * height.
	 * 
	 * @param int n
	 * @return {@link StrElement}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static StrElement verticalStrElement(int n)
			throws IllegalArgumentException {
		if (n < 1 || n % 2 == 0)
			throw new IllegalArgumentException();

		boolean[][] str = new boolean[n][1];

		for (int i = 0; i < n; i++)
			str[i][0] = true;

		return new StrElement(str);
	}

	/**
	 * This will return a {@link StrElement} defining a Saltire structure.
	 * <p>
	 * A Saltire is a diagonal cross structure with identical length along both the 
	 * principal diagonals. 
	 * <p>
	 * The argument 'n' here specify the length of the diagonal and it should be a 
	 * positive odd integer else this method will throw an IllegalArgument Exception.
	 * 
	 * @param int n
	 * @return {@link StrElement}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static StrElement saltireStrElement(int n) throws IllegalArgumentException
	{
		if (n < 1 || n%2 == 0)
			throw new IllegalArgumentException();

		int c = (n-1)/2;
				
		boolean[][] str = new boolean[n][n];

		for (int i = 0; i < n; i++)
		{
			for(int j = 0; j< n; j++)
			{
				if(Math.abs(i-c) == Math.abs(j-c)){
					str[i][j] = true;
				}
			}
		}

		try {
			return new StrElement(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will return a {@link StrElement} defining a Cross structure.
	 * <p>
	 * A Cross structure has a vertical and a horizontal axis crossing at the center. 
	 * <p>
	 * The argument 'n' and 'm' here specify respectively the length of the vertical and
	 * horizontal axis. Both the arguments should be a positive odd integer else this method 
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int n
	 * @param int m
	 * @return {@link StrElement}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static StrElement crossStrElement(int n,int m) throws IllegalArgumentException
	{
		if(n < 1 || m < 1 || n%2 == 0 || m%2 == 0)
			throw new IllegalArgumentException();
		else
		{
			boolean[][] buf = new boolean[n][m];
			final int cy = (n-1)/2;
			final int cx = (m-1)/2;
			
			for(int i=0;i<n;i++)
			{
				buf[i][cx] = true;
			}
			
			for(int j=0;j<m;j++)
			{
				buf[cy][j] = true;
			}
			
			try{
				return new StrElement(buf);
			}catch(IllegalArgumentException e){
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This will return a circular {@link StrElement} including the central pixel.
	 * <p>The argument 'diameter' gives the diameter of the circular structure in 
	 * pixels. The argument 'diameter' should be an odd positive integer greater than 0 else
	 * this method will throw an IllegalArgument Exception.
	 * @param int diameter
	 * @return StrElement
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static StrElement circularStrElement(int diameter) throws IllegalArgumentException
	{
		if(diameter < 1 || diameter%2 == 0)
			throw new IllegalArgumentException();
		else
		{
			int C = (diameter-1)/2;
			
			boolean[][] str = new boolean[diameter][diameter];
			float y,x;
			float check;
			
			for(int i=0;i<diameter;i++)
			{
				y = i-C;
				y*=y;
				for(int j=0;j<diameter;j++)
				{
					x = j-C;
					x*=x;
					check = (float) (Math.sqrt(y+x));
					if(check<=C)
						str[i][j]=true;
				}
			}
			
			try{
				return new StrElement(str);
			}catch(IllegalArgumentException e)
			{
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method will convert the given Structuring Element to a {@link Cell}
	 * and return the same.
	 * <p>
	 * The return {@link Cell} will be of same dimensions as that of the given
	 * structuring element and will consist of 1's and 0's, with arrangement of
	 * 1's defining the structure as defined by the given structuring element.
	 * 
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() {
		Cell cell = new Cell(getHeight(), getWidth());

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				if (this.f1[i][j] == true)
					cell.setElement(1, i, j);
				else
					cell.setElement(0, i, j);
			}
		}

		return cell;
	}

	/**
	 * This will convert the given Structuring Element to a
	 * {@link BinaryPixelImage} with similar dimensions and return the same.
	 * <p>
	 * The structure as defined by the given structuring element will be
	 * captured as the fore ground image consisting of white pixels in the
	 * return {@link BinaryPixelImage}.
	 * <p>
	 * Use this {@link BinaryPixelImage} representation of this structuring
	 * element to visualise the defined structure as foreground image.
	 * 
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage toBinaryPixelImage() {
		boolean[][] result = new boolean[this.getHeight()][this.getWidth()];

		for (int i = 0; i < result.length; i++) {
			System.arraycopy(this.f1[i], 0, result[i], 0, result[i].length);
		}

		try {
			return new BinaryPixelImage(result);
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will merge {@link StrElement}s 'str1' and 'str2' into a single larger StrElement and return
	 * the same.
	 * <p>The shape of the merged StrElement shall be a union of the shapes for StrElement 'str1' and 'str2'.
	 *   
	 * @param StrElement str1
	 * @param StrElement str2
	 * @return StrElement
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public static final StrElement mergeStrElements(StrElement str1, StrElement str2) 
	{
		final int h1 = str1.getHeight();
		final int h2 = str2.getHeight();
		final int w1 = str1.getWidth();
		final int w2 = str2.getWidth();

		final int h = (h1 >= h2) ? h1 : h2;
		final int w = (w1 >= w2) ? w1 : w2;

		boolean[][] buffer = new boolean[h][w];
		boolean[][] buf1 = str1.accessStrElementBuffer();
		boolean[][] buf2 = str2.accessStrElementBuffer();

		int yOffset = (h - h1)/2;
		int xOffset = (w - w1)/2;

		for(int i=0;i<h1;i++)
		{
			for(int j=0;j<w1;j++)
			{
				if(buf1[i][j]){
					buffer[i+yOffset][j+xOffset] = true;
				}
			}
		}

		yOffset = (h - h2)/2;
		xOffset = (w - w2)/2;

		for(int i=0;i<h2;i++)
		{
			for(int j=0;j<w2;j++)
			{
				if(buf2[i][j]){
					buffer[i+yOffset][j+xOffset] = true;
				}
			}
		}

		try {
			return new StrElement(buffer);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
}
