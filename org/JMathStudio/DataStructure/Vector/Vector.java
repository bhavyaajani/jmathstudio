package org.JMathStudio.DataStructure.Vector;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Generic.Index1D;
import org.JMathStudio.DataStructure.Generic.Index1DList;
import org.JMathStudio.DataStructure.Iterator.Iterator1D.Iterator1DBound;
import org.JMathStudio.DataStructure.Iterator.Iterator1D.VectorIterator;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.VisualToolkit.Graph.VectorGraph;

/**
 * This class define a 1D data structure 'Vector' which stores 1D scalar (float) elements.
 * <p><h3>Notations</h3>
 * Internally, a Vector is represented by a 1D array of floating point elements.
 * <p><h4>Length</h4>The number of elements present in this Vector. The indexing of the
 * elements starts from 0 till one less than the length of this Vector.
 * <p>With in the toolkit a Vector is used to represent the following :
 * <p>1 1D Discrete Real Signal. A discrete real signal is the ordered sequence of scalar (float)
 * elements as given by the Vector. 
 * <p><i>By default, elements of the Vector (here discrete real signal) are equi-sampled and ordered in
 * the increasing time position with first element located at the origin. </i>
 * <p>2 1D ordered sequence of scalar (float) data. 
 * <pre>Usage:
 * float[] data = new float[1025];
 * Vector a = new Vector(data);//Create an instance of Vector from data array.
 * 
 * float ele = a.getElement(j);//Access given element within Vector.
 * 
 * a.plot("Title", "yLegend");//Plot elements of the Vector as line graph.
 * 
 * int length = a.length();//Get length i.e element count for the Vector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Vector implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9054202561939090690L;
	//As Vector is serializable, all fields must be either serializable or transient.
	private float[] i0;
	private transient VectorIterator iterator = null;

	/**
	 * This will create a Vector with elements as given by the 1D Float array
	 * argument 'vector'.
	 * <p>The argument 'vector' is passed as reference and no deep copy of the array
	 * is made.
	 * @param float [] vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector(float[] vector) {
		this.i0 = vector;
	}

	/**
	 * This will create a Vector with the capacity to store the number of Float
	 * elements as given by the argument 'length'. The argument 'length' should
	 * be more than 0.
	 * 
	 * @param int length
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Vector(int length) {
		i0 = new float[length];
	}

	/**
	 * This method will return the 1D float array or elements associated with
	 * this Vector.
	 * 
	 * @return float[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[] accessVectorBuffer() {
		return this.i0;
	}

	/**
	 * This method will return the length or size of the given Vector.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public int length() {
		return this.i0.length;
	}

	/**
	 * This method will return the Float element present at the index location
	 * as given by the argument 'index' from this Vector.
	 * <p>
	 * The argument 'index' should be in the range of [0 length-1], else this
	 * method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getElement(int index) {
		return i0[index];
	}
	
	/**
	 * This method will return the Float element present at the index location
	 * as given by the argument {@link Index1D} 'index' from this Vector.
	 * <p>
	 * The argument 'index' should give a valid index position within the Vector in the range of 
	 * [0 length-1] else this method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param Index1D index
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getElement(Index1D index) {
		return i0[index.getX()];
	}

	/**
	 * This method will replace the Float element present at the index location
	 * as given by the argument 'index' in this Vector with the float number as
	 * given by the argument 'element'.
	 * <p>
	 * The argument 'index' should be in the range of [0 length-1], else this
	 * method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param float element
	 * @param int index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(float element, int index) {
		this.i0[index] = element;
	}
	
	/**
	 * This method will replace the Float element present at the index position
	 * as given by the argument {@link Index1D} 'index' in this Vector with the float number as
	 * given by the argument 'element'.
	 * <p>
	 * The argument 'index' should give a valid index position within the Vector in the range 
	 * of [0 length-1] else this method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param float element
	 * @param Index1D index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(float element, Index1D index) {
		this.i0[index.getX()] = element;
	}

	/**
	 * This method will replace all the elements of the Vector located at the index positions as
	 * specified by {@link Index1DList} 'indexes' with element specified by argument 'element'.
	 * <p>If the index position as specified by any of the Index1D within the Index1DList is 
	 * outside the valid index bounds of the Vector, an ArrayIndexOutOfBound Exception will be thrown.
	 *
	 * @param float element
	 * @param Index1DList indexes
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAllElements(float element,Index1DList indexes)
	{
		for(int i=0;i<indexes.size();i++)
		{
			setElement(element, indexes.access(i));
		}
	}
	
	/**
	 * This method will replace all the elements of the Vector equal to the argument 'element' with 
	 * element specified by the argument 'replace'.
	 * @param float element
	 * @param float replace
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void replaceAllElements(float element,float replace)
	{
		final int L = length();
		
		for(int i=0;i<L;i++)
		{
			if(i0[i] == element){
				i0[i] = replace;
			}
		}
	}
	
	/**
	 * This method will plot the elements of the given Vector against the corresponding elements of the
	 * Vector 'xVector' as a line graph on a {@link VectorGraph}. 
	 * <p>The length of 'xVector' should be same as that of given Vector else this method will throw a 
	 * DimentionMismatch Exception.
	 * <p>
	 * The argument 'title', 'xLegend' and 'yLegend' respectively specify the
	 * Title, X axis legend and Y axis legend for the graph.
	 * <p>
	 * See {@link VectorGraph} class for further information on the graph utility.
	 * 
	 * @param Vector
	 *            xVector
	 * @param String
	 *            title
	 * @param String
	 *            xLegend
	 * @param String
	 *            yLegend
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(Vector xVector,String title,
			String xLegend, String yLegend) throws DimensionMismatchException {

		VectorGraph graph = new VectorGraph();
		
		graph.setGraphTitle(title);
		graph.setGraphXLegend(xLegend);
		graph.setGraphYLegend(yLegend);

		graph.plot(this, xVector);

		graph.setVisible(true);
	}

	/**
	 * This method will plot the elements of the given Vector against its index positions
	 * as a line graph on a {@link VectorGraph}.
	 * <p>
	 * The argument 'title, and 'yLegend' respectively specify the Title and Y axis legend for the graph.
	 * <p>
	 * See {@link VectorGraph} class for further information on the graph utility.
	 * 
	 * @param String
	 *            title
	 * @param String
	 *            yLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(String title, String yLegend) {

		VectorGraph graph = new VectorGraph();
		
		graph.setGraphTitle(title);
		graph.setGraphXLegend("Index");
		graph.setGraphYLegend(yLegend);

		graph.plot(this);

		graph.setVisible(true);
	}

	/**
	 * This method will return the clone copy of the given Vector.
	 * <p>
	 * A clone of the Vector is the Vector with similar length and same corresponding
	 * complex elements as that of original Vector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Vector clone() {
		float[] vector = accessVectorBuffer();

		float[] result = new float[vector.length];

		System.arraycopy(vector, 0, result, 0, result.length);

		return new Vector(result);
	}

	/**
	 * This method will put the elements of this Vector in an ordered structure to a Standard Output Stream.
	 * <p>This method is useful to display the elements of the given Vector on the console if the length
	 * of the Vector is small enough for console to hold the displayed elements.
	 */
	public void dumpToStandardOPStream()
	{
		System.out.println();

		for(int i=0;i<length();i++)
		{
			System.out.print(getElement(i)+"\t");
		}
	}

	/**
	 * This method will save the current state of this {@link Vector} object to an external file with
	 * file name as specified by the argument 'filename' and path as specified by the
	 * argument 'dirPath'.
	 * <p>The argument 'filename' should not contain '.' charactor and any extension.
	 * Further an extension '.vector' will be added to the saved state file by this method.
	 * <p>The current state of this {@link Vector} object stored in the external
	 * file can be loaded into the framework at later stage using the {@link #retrieveState(String)}
	 * method.
	 * <p>If the argument 'dirPath' does not specify a valid directory path or if the
	 * method encounters any IO issues during the state save this method will throw
	 * an IO Exception.
	 * 
	 * @param String dirPath
	 * @param String filename
	 * @throws IOException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void saveState(String dirPath, String filename) throws IOException
	{
		String filePath = dirPath+filename+".vector";

		FileOutputStream fs = new FileOutputStream(filePath);
		ObjectOutputStream os = new ObjectOutputStream(fs);

		os.writeObject(this);
		os.close();
		fs.close();

	}

	/**
	 * This method load the saved state of the {@link Vector} object saved in the external file
	 * as specified by the argument 'filePath' and return the same as a {@link Vector}.
	 * <p>The argument 'filePath' should provide the full path to an external file which
	 * contain the saved state of a {@link Vector} object. Further the extension of the file
	 * should be '.vector'.
	 * <p>If the extension of the external file containing the saved state of a {@link Vector} object
	 * is not '.vector' or is not a valid save state file of a {@link Vector} object this method 
	 * will throw an Illegal Argument Exception.
	 * <p>If the argument 'filePath' does not point to a file or if this method encounters any
	 * IO issues an IO Exception will be thrown.
	 * @param String filePath
	 * @return Vector
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector retrieveState(String filePath) throws IOException, IllegalArgumentException
	{
		String format = filePath.substring(filePath.indexOf('.') + 1);

		if(!format.equalsIgnoreCase("vector"))
			throw new IllegalArgumentException();

		FileInputStream fs = new FileInputStream(filePath);
		ObjectInputStream os = new ObjectInputStream(fs);

		Vector vec = null;

		try{
			vec = (Vector) os.readObject();
		}catch(ClassCastException e){
			throw new IllegalArgumentException();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException();
		}

		os.close();
		fs.close();

		return vec;
	}

	/**
	 * This method will check if the index as specified by the argument
	 * 'index' falls within the valid bounds of this Vector.
	 * @param int index
	 * @return boolean
	 */
	public boolean isWithinBounds(int index)
	{
		if(index>=0 && index<i0.length)
			return true;
		else
			return false;
	}

	/**
	 * This method will return the element present at the index position (with context to this Vector)
	 * as specified by the argument 'index'. The argument 'index' can take any value.
	 * <p>If the position specified by the arguments 'index' falls within the valid bounds of this
	 * {@link Vector}, the element at that position within the Vector will be returned.
	 * <p>However if the position specified by the argument 'index' falls outside the bounds of 
	 * this Vector, the element for that position shall be interpreted based on the
	 * circular periodic extension of the Vector and shall be returned.
	 * <p>This method is useful in the implementation of the functions/algorithm which extends
	 * beyond the Vector's bounds and need to pass the element using some padding scheme. 
	 * @param int index
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getElementWithPadding(int index)
	{
		int L = length();
		int i;

		if(index >= L)
			i = index%L;
		else if(index < 0)
		{
			index = index%L;
			i = Math.abs(L+index)%L;
		}
		else
			i = index;

		return getElement(i);
	}

	/**
	 * This method check whether the Vector 'vector' has length similar to that
	 * of this Vector.
	 * @param Vector vector
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean hasSameLength(Vector vector){

		return (vector.length() == this.length());
	}

	/**
	 * This method will promote the given 1 dimensional {@link Vector} to a 2 dimensional Cell and return the same.
	 * <p>The Vector is promoted to a Cell by dividing a Vector into independent blocks of equal length and re-arranging
	 * the blocks as a stack forming a Cell. The number of independent blocks become row count and length of the block 
	 * become column count of the resultant Cell. 
	 * <p>The argument 'block' here specify the length of each block (i.e column count of the resultant Cell).
	 * <p>The argument 'block' should be in the range of (0 l], where 'l' is the length of the given Vector, else
	 * this method will throw an IllegalArgument Exception.
	 * <p>Further argument 'block' should be such that it divides Vector in to some 'N' independent blocks of equal
	 * length i.e l%block should equal 0 else this method will throw a DimensionMismatch Exception.
	 * <p>If the above conditions are satisfied, this method shall convert the given Vector to a Cell with row count
	 * equal to 'N' & column count equal to 'block'.
	 * <p><i>For example let Vector 'a' has 12 elements. Let argument 'block' equal to 3. Thus first condition '3' is 
	 * more than 0 but less than or equal to length of the Vector '12' is satisfied. Further, second condition 12 modulo
	 * 3 is equal to 0 i.e Vector 'a' with length '12' can be divided equally into '4' independent blocks of 3 elements.
	 * <p>Thus, Vector 'a' with elements [1 2 3 4 5 6 7 8 9 10 11 12] shall be rearranged as,
	 * <p> 1  2  3<p> 4  5  6<p> 7  8  9<p>10 11 12<p>forming a Cell with row count equal to '4' and column count equal to '3'.  
	 * </i>
	 * @param int block
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 */
	public Cell promoteToCell(int block) throws IllegalArgumentException, DimensionMismatchException{

		int l = length();
		if(block <= 0 || block >l)
			throw new IllegalArgumentException();
		else if(l%block !=0)
			throw new DimensionMismatchException();
		else
		{
			try{
				int rc = l/block;
				int cc = block;

				Cell res = new Cell(rc,cc);

				for(int i=0;i<rc;i++)
				{
					for(int j=0;j<cc;j++)
					{
						res.setElement(i0[i*cc+j], i,j);
					}
				}

				return res;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method computes the linear interpolated value for the hypothetical
	 * element located at any index position 'X' with in the given Vector, where
	 * 'X' specify the index position.
	 * <p>
	 * The 'X' can take any integer and non-integer index position within the
	 * valid index bounds of the given Vector.
	 * <p>
	 * The value of the argument 'X' should be in the range of [0 vector_length -1] else 
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param float X
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float interpolate(float X)throws IllegalArgumentException {
		if (X < 0 || X > this.length() - 1)
			throw new IllegalArgumentException();

		int xl = (int) Math.floor(X);
		int xh = (int) Math.ceil(X);

		if (xl == xh)
			return this.getElement(xl);
		else
			return this.getElement(xl) * (xh - X) + this.getElement(xh)
					* (X - xl);

	}
	
	/**
	 * This will return an instance of {@link VectorIterator } associated with the given Vector.
	 * <p>Each Vector object maintains a single internal instance of VectorIterator object. Getting
	 * VectorIterator through this method avoid creation of multiple instances of VectorIterator associated
	 * with the given Vector.
	 * @return VectorIterator
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorIterator getAssociatedIterator(){
		if(iterator == null)
			iterator = new VectorIterator(this);
		return iterator;
	}
	
	/**
	 * This method return the largest possible iterable bounds for the given Vector as {@link Iterator1DBound}.
	 * @return Iterator1DBound
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Iterator1DBound getLargestIterableBounds(){
		try {
			return new Iterator1DBound(0,length());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will get all the elements of the Vector located at the index positions as
	 * specified by {@link Index1DList} 'indexes' and return the same as {@link Vector}.
	 * <p>Thus each element in return Vector shall be the element present in this Vector at the
	 * index location as given by the corresponding {@link Index1D} of the {@link Index1DList} 
	 * argument 'indexes' respectively.
	 * <p>If the index position as specified by any of the Index1D within the Index1DList is 
	 * outside the valid index bounds of this Vector, an ArrayIndexOutOfBound Exception will be thrown.
	 * <p>If size of {@link Index1DList} 'indexes' is zero than this method will return null.
	 * 
	 * @param Index1DList indexes
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector getAllElements(Index1DList indexes)
	{
		if(indexes.size() <= 0)
			return null;
		
		float res[] = new float[indexes.size()];
		
		for(int i=0;i<indexes.size();i++)
		{
			res[i] = this.i0[indexes.access(i).getX()];
		}
		
		return new Vector(res);
	}
}
