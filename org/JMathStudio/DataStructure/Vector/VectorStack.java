package org.JMathStudio.DataStructure.Vector;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.VisualToolkit.Graph.VectorGraph;

/**
 * This class define a mutable container for storing a set of {@link Vector}'s
 * as a stack. New Vector's can be appended to the existing stack dynamically.
 * <p>
 * Internally this class represent Vector Stack as a {@link java.util.Vector} of
 * Vector objects.
 * <p>
 * Size - This indicates the number of {@link Vector}'s contained in the stack.
 * <p>
 * Each Vector in the stack can be accessed by its index position which start
 * from 0 to one less than the size of this stack.
 * <pre>Usage:
 * Let 'a', 'b' & 'c' be Vector objects.
 * 
 * VectorStack stack = new VectorStack();//Create an instance of an empty VectorStack.
 * 
 * stack.addVector(a);//Add Vector's to stack in order.
 * stack.addVector(b);
 * stack.addVector(c);
 * 
 * Vector d = stack.accessVector(i);//Access Vector at valid index within VectorStack.
 * 
 * stack.plot("Title", "xLegend", "yLegend");//Plot all the Vector's as multi-stack line graph.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class VectorStack {

	private java.util.Vector<Vector> stack;

	// private Vector[] stack;

	/**
	 * This will initiate a new empty Vector Stack object to which
	 * {@link Vector}'s can be appended later on.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack() {
		stack = new java.util.Vector<Vector>();
	}

	/**
	 * This will initiate a new Vector stack with specified initial capacity 'n'. 
	 * <p>If argument 'n' is less than '1' this method will throw an IllegalArgument
	 * Exception.
	 * <p>Argument 'n' here specify the initial capacity for the Vector stack, which increases
	 * as more {@link Vector}'s are added to the stack later on.
	 * @param int n
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack(int n) throws IllegalArgumentException
	{
		if(n <=0)
			throw new IllegalArgumentException();
		else
			stack = new java.util.Vector<Vector>(n);
	}
	
	/**
	 * This will initiate a new Vector Stack object and populate the same with
	 * the non null {@link Vector}'s from the given 1D Vector array
	 * 'vectorArray' in the same order.
	 * <p>
	 * If the Vector array 'vectorArray' contain any null vector's the size of
	 * this Vector Stack will not be same as that of the Vector array.
	 * <p>The argument 'vectorArray' is passed as reference and no deep copy of the array
	 * is made.
	 * 
	 * @param Vector
	 *            [] vectorArray
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack(Vector[] vectorArray) {
		stack = new java.util.Vector<Vector>();

		if (vectorArray != null) {
			for (int i = 0; i < vectorArray.length; i++) {
				if (vectorArray[i] != null)
					stack.addElement(vectorArray[i]);
			}
		}

	}

	/**
	 * This method will append the {@link Vector} 'vector' at the end of the given Vector
	 * Stack.
	 * <p>
	 * This increases the size of the given Vector Stack by 1.
	 * <p>
	 * If the Vector 'vector' is null this method will throw a NullPointer Exception.
	 * <p>The argument 'vector' is added by reference and no deep copy of the same is made.
	 * 
	 * @param Vector
	 *            vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void addVector(Vector vector) {
		if (vector == null)
			throw new NullPointerException();
		else
			stack.addElement(vector);
	}

	/**
	 * This method will convert the given Vector Stack to a 1D {@link Vector}
	 * array and return the same.
	 * <p>The order of the Vectors in the return array will be same as that in the given stack.
	 * <p>If the given Vector Stack is empty; a null shall be returned.
	 * 
	 * @return Vector[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector[] accessVectorArray() {

		if (stack.size() == 0)
			return null;
		else {
			Vector[] array = new Vector[stack.size()];

			for (int i = 0; i < array.length; i++) {
				array[i] = accessVector(i);
			}

			return array;
		}
	}

	/**
	 * This method will return the current size of the given Vector Stack.
	 * <p>
	 * This indicate the number of {@link Vector}'s contained within the given
	 * Vector Stack.
	 * <p>
	 * If the given Vector Stack has no Vector elements this method will return
	 * size as '0'.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public int size() {

		if (stack.isEmpty())
			return 0;
		else
			return stack.size();
	}

	/**
	 * This method will return the {@link Vector} located at the index position
	 * as given by the argument 'index' within the given Vector Stack.
	 * <p>
	 * The value of argument 'index' should be in the range of 0 to one less
	 * than the size of this Vector Stack else this method will throw an
	 * ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Vector accessVector(int index) {
		return stack.get(index);
	}

	/**
	 * This method will replace the {@link Vector} element located at the index
	 * position given by the argument 'index' in the given Vector Stack with the
	 * Vector as specified by the argument 'vector'.
	 * <p>
	 * The value of the argument 'index' should be in the range of 0 to one less
	 * than the size of this Vector Stack else this method will throw an
	 * ArrayIndexOutOfBound Exception.
	 * <p>
	 * If the {@link Vector} 'vector' is null this method will throw a
	 * NullPointer Exception.
	 * <p>The Vector 'vector' is replaced by reference and no deep copy of the Vector is
	 * made by the method.
	 * 
	 * @param Vector
	 *            vector
	 * @param int index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public void replace(Vector vector, int index) {

		if (vector == null)
			throw new NullPointerException();
		else {
			stack.remove(index);
			stack.insertElementAt(vector, index);
		}
	}

	/**
	 * This method will convert the VectorStack object to a {@link Cell} object.
	 * <p>
	 * Each Vector of the given stack becomes the corresponding row element of
	 * the return Cell.
	 * <p>
	 * All the Vectors of the given stack should be of same length else this
	 * method cannot form a Cell object out of them and will throw an
	 * DimensionMismatch Exception.
	 * <p>
	 * If the given VectorStack object is empty this method will throw a
	 * NullPointer Exception.
	 * 
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell toCell() throws DimensionMismatchException {
		if (stack.isEmpty())
			throw new NullPointerException();

		int height = stack.size();
		int width = stack.get(0).length();

		float[][] cell = new float[height][width];

		for (int i = 0; i < cell.length; i++) {
			if (stack.get(i).length() != width)
				throw new DimensionMismatchException();

			for (int j = 0; j < cell[i].length; j++)
				cell[i][j] = stack.get(i).getElement(j);
		}

		try {
			return new Cell(cell);
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will plot all the Vectors of the given VectorStack as
	 * multiple stack line graph on a {@link VectorGraph}.
	 * <p>
	 * The argument 'title', 'xLegend' and 'yLegend' respectively specify the
	 * Title, X axis legend and Y axis legend for the graph.
	 * <p>
	 * See {@link VectorGraph} class for further information on the graph utility.
	 * 
	 * @param String
	 *            title
	 * @param Strring
	 *            xLegend
	 * @param String
	 *            yLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(String title, String xLegend, String yLegend) {

		VectorGraph graph = new VectorGraph();

		graph.setGraphTitle(title);
		graph.setGraphXLegend(xLegend);
		graph.setGraphYLegend(yLegend);

		graph.plot(this);

		graph.setVisible(true);
	}

	/**
	 * This method will remove all the {@link Vector}s from the current stack and empty the stack.
	 * <p>The size of the stack will be '0' after this call.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void clear(){
		this.stack.clear();
	}
}
