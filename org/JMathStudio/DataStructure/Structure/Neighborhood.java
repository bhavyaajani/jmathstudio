package org.JMathStudio.DataStructure.Structure;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.EmptyNeighborhoodException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.PixelImageFormatException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class define a Neighborhood structure which specify a neighborhood around a pixel position. 
 * Many of the spatial operations operate on a neighborhood around a central pixel position. Such a 
 * neighborhood can be defined with a Neighborhood structure.
 * <p>Neighborhood is structured as a 2D boolean array (neighborhood buffer) with odd dimensions. 
 * Further the centre of the Neighborhood coincide with the pixel position in question during 
 * the spatial operations.
 * <p>A neighborhood around a pixel is defined by the patterns of 'true' in the neighborhood buffer with
 * the centre of the buffer coinciding with that pixel. A neighborhood may or may not contain the central pixel. 
 * <p>All the elements of the neighborhood buffer with 'true' is a valid neighbor to the pixel.
 *  
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Neighborhood {

	private boolean[][] f2;
	private Neighbor[] neighbors;
	
	/**
	 * This will create a Neighborhood structure defining the neighborhood around a pixel.
	 * <p>A neighborhood is specified by a 2D boolean array 'neighborhood' (also neighborhood buffer). 
	 * The dimensions of the array 'neighborhood' should be odd else this method will throw an 
	 * IllegalArgument Exception. 
	 * <p>The patterns of 'true' within the array 'neighborhood' define the neighborhood around the
	 * pixel with centre of the array coinciding with that pixel.
	 * <p>Further, length of each of the row of the array 'neighborhood' should be of
	 * same length else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'neighborhood' is passed by reference and no deep copy of the array is made. 
	 *  
	 * @param boolean[][] neighborhood
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Neighborhood(boolean[][] neighborhood)throws IllegalArgumentException 
	{
		for (int i = 1; i < neighborhood.length; i++) {
			if (neighborhood[i].length != neighborhood[0].length)
				throw new IllegalArgumentException();
			}

		//neighborhood Dimensions should be odd.
		if (neighborhood.length % 2 == 0 || neighborhood[0].length % 2 == 0)
			throw new IllegalArgumentException();
		else{
			this.f2 = neighborhood;
			f0();
		}		
	}

	//Should be called only from constructor.
	private void f0() 
	{
		int elements = 0;
		
		for(int i=0;i<f2.length;i++)
		{
			for(int j=0;j<f2[0].length;j++)
			{
				if(f2[i][j])
					elements++;
			}
		}
		
		if(elements > 0){
			neighbors = new Neighbor[elements];
			int cy = (f2.length-1)/2; 
			int cx = (f2[0].length-1)/2;
			int index=0;
			
			for(int i=0;i<f2.length;i++)
			{
				for(int j=0;j<f2[0].length;j++)
				{
					if(f2[i][j]){
						neighbors[index++] = new Neighbor(i-cy,j-cx);
					}
				}
			}

		}
		else
		{
			//No elements i.e all elements are false in the neighborhood buffer.
			neighbors = null;
		}
			
	}
	
	/**
	 * This method will return a set of {@link Neighbor}s specifying all the neighbor
	 * elements within the given Neighborhood. 
	 * <p>A Neighbor element is that element of the {@link Neighborhood} which is neighbor to the
	 * pixel; defined as 'true' in the neighborhood buffer.
	 * <p>If Neighborhood has no neighbor elements this method will return a null.
	 * <p>A set of {@link Neighbor} fully define a neighborhood for a spatial operation.
	 * @return Neighbor[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Neighbor[] accessAllNeighbors()
	{
		return this.neighbors;
	}
	
	/**
	 * This will return the Height of the given Neighborhood. Height is the vertical dimension
	 * (number of pixel positions) of the neighborhood buffer.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	private int getBufferHeight() {
		return this.f2.length;
	}

	/**
	 * This will return the Width of the given Neighborhood. Width is the horizontal dimension
	 * (number of pixel positions) of the neighborhood buffer.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	private int getBufferWidth() {
		return this.f2[0].length;
	}

	/**
	 * This method will check whether the element within the Neighborhood with row and column index
	 * position as given by the arguments 'row' and 'column' respectively is a Neighbor to the
	 * pixel.
	 * <p>
	 * If the neighborhood element in question is define as a neighbor this method will
	 * return True else it will return False.
	 * <p>
	 * The argument 'row' and 'column' specifying the row and column index position of
	 * the element in question should be within the bound [0 height of Neighborhood)
	 * and [0 width of Neighborhood) respectively else this method will throw an 
	 * {@link ArrayIndexOutOfBoundsException}.
	 * 
	 * @param int row
	 * @param int column
	 * @return boolean
	 * @see #Neighborhood(boolean[][])
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isNeighbor(int row, int column) {
		return this.f2[row][column];
	}
	
	/**
	 * This method will return the neighborhood buffer (2D boolean array) representing the given
	 * Neighborhood.
	 * 
	 * @return boolean[][]
	 * @see #Neighborhood(boolean[][])
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean[][] accessNeighborhoodBuffer() {
		return this.f2;
	}

	/**
	 * This will return a complimentary {@link Neighborhood} to the given Neighborhood.
	 * <p>The complimentary Neighborhood will toggle the Neighbor status for all the elements
	 * within the given Neighborhood.
	 * <p>Thus a complimentary neighborhood buffer will have toggled 'true'/'false' flag for 
	 * each element within the neighborhood buffer for this Neighborhood.
	 * 
	 * @return Neighborhood
	 * @see #Neighborhood(boolean[][])
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Neighborhood getComplimentaryNeighborhood() {
		boolean[][] cnhbr = new boolean[getBufferHeight()][getBufferWidth()];

		for (int i = 0; i < cnhbr.length; i++) {
			for (int j = 0; j < cnhbr[0].length; j++) {
				cnhbr[i][j] = !this.f2[i][j];
			}
		}

		try {
			return new Neighborhood(cnhbr);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will return a square {@link Neighborhood} including the central pixel.
	 * <p>
	 * The argument 'n' here specify the dimension of the square neighborhood
	 * and should be a positive odd integer else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int n
	 * @return Neighborhood
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood squareNeighborhood(int n)
			throws IllegalArgumentException {
		if (n < 1 || n % 2 == 0)
			throw new IllegalArgumentException();

		boolean[][] str = new boolean[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				str[i][j] = true;
			}
		}

		try {
			return new Neighborhood(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will return a Horizontal {@link Neighborhood} including the central pixel.
	 * <p>
	 * The argument 'n' here specify the horizontal dimension of the given
	 * neighborhood and it should be a positive odd integer else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * <p>
	 * A horizontal neighborhood is a horizontal strip with unit height centred
	 * around the central pixel.
	 * 
	 * @param int n
	 * @return Neighborhood
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood horizontalNeighborhood(int n)
			throws IllegalArgumentException {
		if (n < 1 || n % 2 == 0)
			throw new IllegalArgumentException();

		boolean[][] str = new boolean[1][n];

		for (int i = 0; i < n; i++)
			str[0][i] = true;

		try {
			return new Neighborhood(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This will return a Vertical {@link Neighborhood} including the central pixel.
	 * <p>
	 * The argument 'n' here specify the vertical dimension of the given
	 * neighborhood and it should be a positive odd integer else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * <p>
	 * A vertical neighborhood is a vertical strip with unit width centred
	 * around the central pixel.
	 * 
	 * @param int n
	 * @return Neighborhood
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood verticalNeighborhood(int n)
			throws IllegalArgumentException {
		if (n < 1 || n % 2 == 0)
			throw new IllegalArgumentException();

		boolean[][] str = new boolean[n][1];

		for (int i = 0; i < n; i++)
			str[i][0] = true;

		try {
			return new Neighborhood(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will return a Saltire {@link Neighborhood}.
	 * <p>
	 * A Saltire is a diagonaly cross neighborhood with identical length along both the 
	 * principal diagonals. 
	 * <p>
	 * The argument 'n' here specify the length of the diagonal and it should be a 
	 * positive odd integer else this method will throw an IllegalArgument Exception.
	 * 
	 * @param int n
	 * @return {@link Neighborhood}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood saltireNeighborhood(int n) throws IllegalArgumentException
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
			return new Neighborhood(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will return a Cross {@link Neighborhood}.
	 * <p>
	 * A Cross neighborhood consist of a vertical and a horizontal strip crossing at the center. 
	 * <p>
	 * The argument 'n' and 'm' here specify respectively the length of the vertical and
	 * horizontal strip. Both the arguments should be a positive odd integer else this method 
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int n
	 * @param int m
	 * @return {@link Neighborhood}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood crossNeighborhood(int n,int m) throws IllegalArgumentException
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
				return new Neighborhood(buf);
			}catch(IllegalArgumentException e){
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This will return a four connected neighborhood including the central pixel.
	 * 
	 * @return Neighborhood
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood fourConnectedNeighborhood() {
		boolean[][] str = new boolean[3][3];

		for (int i = 0; i < 3; i++) {
			str[i][1] = true;
			str[1][i] = true;
		}
		try {
			return new Neighborhood(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will return an eight connected {@link Neighborhood} including the central pixel.
	 * 
	 * @return Neighborhood
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood eightConnectedNeighborhood() {
		boolean[][] str = new boolean[3][3];

		for (int i = 0; i < 3; i++) 
		{
			for (int j = 0; j < 3; j++) 
			{
				str[i][j] = true;
			}
		}
		try {
			return new Neighborhood(str);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will return a circular {@link Neighborhood} including the central pixel.
	 * <p>The argument 'diameter' gives the diameter of the circular neighborhood in 
	 * pixels. The argument 'diameter' should be an odd positive integer greater than 0 else
	 * this method will throw an IllegalArgument Exception.
	 * @param int diameter
	 * @return Neighborhood
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Neighborhood circularNeighborhood(int diameter) throws IllegalArgumentException
	{
		if(diameter < 1 || diameter%2 == 0)
			throw new IllegalArgumentException();
		else
		{
			int C = (diameter-1)/2;
			
			boolean[][] neighborhood = new boolean[diameter][diameter];
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
						neighborhood[i][j]=true;
				}
			}
			
			try{
				return new Neighborhood(neighborhood);
			}catch(IllegalArgumentException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will convert the given {@link Neighborhood} structure to an equivalent
	 * {@link Cell} and return the same.
	 * <p>The return Cell will be of the same dimensions as that of the given
	 * Neighborhood and will consist only of 1's and 0's.
	 * <p>The arrangement of 1's shall be as per the neighborhood defined within the given
	 * {@link Neighborhood} structure.
	 * 
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() {
		Cell cell = new Cell(getBufferHeight(), getBufferWidth());

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				if (this.f2[i][j] == true)
					cell.setElement(1, i, j);
				else
					cell.setElement(0, i, j);
			}
		}

		return cell;
	}

	/**
	 * This will convert the given {@link Neighborhood} structure to an equivalent {@link BinaryPixelImage}
	 * and return the same.
	 * <p>The dimensions of BinaryPixelImage will be similar to that of the given Neighborhood.
	 * <p>The foreground within the return BinaryPixelImage shall represent the neighborhood defined within
	 * the given Neighborhood.
	 * <p>
	 * Use this {@link BinaryPixelImage} representation to visualise the neighborhood as defined within the
	 * given Neighborhood structure. 
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage toBinaryPixelImage() {
		boolean[][] result = new boolean[this.getBufferHeight()][this.getBufferWidth()];

		for (int i = 0; i < result.length; i++) {
			System.arraycopy(this.f2[i], 0, result[i], 0,
					result[i].length);
		}

		try {
			return new BinaryPixelImage(result);
		} catch (PixelImageFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will merge {@link Neighborhood}s 'nbr1' and 'nbr2' into a single larger Neighborhood and return
	 * the same.
	 * <p>The merged Neighborhood shall contain union of all the Neighbors defined within the input Neighborhood
	 * 'nbr1' and 'nbr2'.
	 * <p>If either of the input Neighborhood is empty (has no Neighbor defined) this method will throw an
	 * EmptyNeighborhood Exception.  
	 * @param Neighborhood nbr1
	 * @param Neighborhood nbr2
	 * @return Neighborhood
	 * @throws EmptyNeighborhoodException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public static final Neighborhood mergeNeighborhoods(Neighborhood nbr1, Neighborhood nbr2) throws EmptyNeighborhoodException
	{
		if(nbr1.accessAllNeighbors() == null || nbr2.accessAllNeighbors() == null){
			throw new EmptyNeighborhoodException();
		}
		else{
			
			final int h1 = nbr1.getBufferHeight();
			final int h2 = nbr2.getBufferHeight();
			final int w1 = nbr1.getBufferWidth();
			final int w2 = nbr2.getBufferWidth();
			
			final int h = (h1 >= h2) ? h1 : h2;
			final int w = (w1 >= w2) ? w1 : w2;
			
			boolean[][] buffer = new boolean[h][w];
			boolean[][] buf1 = nbr1.accessNeighborhoodBuffer();
			boolean[][] buf2 = nbr2.accessNeighborhoodBuffer();
			
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
				return new Neighborhood(buffer);
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}
}

