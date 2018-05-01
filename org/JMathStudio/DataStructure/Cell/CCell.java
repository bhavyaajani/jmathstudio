/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.JMathStudio.DataStructure.Cell;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Generic.Index2D;
import org.JMathStudio.DataStructure.Iterator.Iterator2D.CCellIterator;
import org.JMathStudio.DataStructure.Iterator.Iterator2D.Iterator2DBound;
import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;

/**
 * This class define a 2D data structure 'CCell' which stores complex elements in 2D.
 * <p>
 * <h3>Notations</h3>
 * <p>
 * Internally, a CCell is represented by 2 separate 2D float arrays, one representing the
 * real part and another representing the imaginary part of the complex 2D array.
 * </p>
 * <p>
 * <h4>Row</h4> - An individual horizontal structure of the CCell. Indexing start
 * from 0 to one less than the number of row count i.e. numbers of row.
 * <p>
 * <h4>Column</h4> - An individual vertical structure of the CCell. Indexing
 * start from 0 to one less than the number of column count i.e. numbers of
 * column.
 * <p>
 * <h4>Row Count</h4> - Number of rows for the given CCell (also Height).
 * <p>
 * <h4>Column Count</h4> - Number of columns for the given CCell (also Width).
 * <p>
 * Each Row of CCell should have same number of Column elements or each Row
 * should be of same length.
 * <p>
 * CCell being a 2D data structure, each element of the CCell can be uniquely identified by its
 * index location within 2D index space by its row and the column index. Thus in row-column
 * space an element located at row index 'i' [0 to Row Count) and column index 'j' [0 to Column
 * Count) shall be represented as,
 * <h4>CCell(i,j)</h4>
 * <pre>Usage:
 * CCell ccell = new CCell(128,128);// Create an instance of CCell with given dimensions.
 * 
 * Cell magnitude = ccell.getMagnitude();// Get Magnitude of CCell elements.
 * CCell transpose = ccell.getTransposed();// Get Transposed CCell.
 * 
 * Complex ele = ccell.getElement(i, j);// Get complex element at given index.
 * CVector row = ccell.accessRow(i);// Access a valid row of the CCell.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CCell implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3951378441252224487L;
	//As CCell is serializable, all fields must be either serializable or transient.
	float[][] ii;
	float[][] t2;
	private transient CCellIterator iterator = null;

	/**
	 * This will create a CCell with 2D collection of complex elements with Cell
	 * 'real' and 'img' respectively representing the real and imaginary part of
	 * the corresponding complex elements.
	 * <p>
	 * The dimension of both the Cells should be same else this method will
	 * throw an DimensionMismatch Exception.
	 * <p>The arguments 'real' and 'img' are passed as reference and no deep copy of the
	 * object is made.
	 * @param Cell
	 *            real
	 * @param Cell
	 *            img
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell(Cell real, Cell img) throws DimensionMismatchException {
		if (real.getRowCount() != img.getRowCount())
			throw new DimensionMismatchException();
		if (real.getColCount() != img.getColCount())
			throw new DimensionMismatchException();

		this.ii = real.accessCellBuffer();
		this.t2 = img.accessCellBuffer();

	}

	/**
	 * This will create a CCell with given number of Rows and Columns as
	 * specified by the argument 'rowCount' and 'colCount' respectively. Each
	 * element of the CCell will have a default value of -> 0 +i0.
	 * <p>
	 * The argument 'rowCount' and 'colCount' should be more than 0 else this
	 * method will throw an IllegalArgument Exception.
	 * 
	 * @param int rowCount
	 * @param int colCount
	 * @throws IllegalArgumentException
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell(int rowCount, int colCount) throws IllegalArgumentException {
		if (rowCount < 1 || colCount < 1)
			throw new IllegalArgumentException();

		this.ii = new float[rowCount][colCount];
		this.t2 = new float[rowCount][colCount];
	}

	/**
	 * This method will return the number of rows for the given CCell.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getRowCount() {
		return this.ii.length;
	}

	/**
	 * This method will return the number of columns for the given CCell.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getColCount() {
		return this.ii[0].length;
	}

	/**
	 * This method will return the complex element present at the given row and
	 * column index as specified by the arguments 'row' and 'column'
	 * respectively.
	 * <p>
	 * If the arguments 'row' and 'column' over shoot valid range of indexing,
	 * an ArrayIndexOutOfBound Exception will be thrown.
	 * 
	 * @param int row
	 * @param int column
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getElement(int row, int column) {
		return new Complex(ii[row][column], t2[row][column]);
	}

	/**
	 * This method will return the complex element present at the given row and column
	 * index position as specified by the {@link Index2D} 'index'.
	 * <p>If the index position as specified by Index2D 'index' is outside the valid
	 * index bounds of the CCell, an ArrayIndexOutOfBound Exception will be thrown.
	 * 
	 * @param Index2D index
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getElement(Index2D index) {
		return new Complex(ii[index.getY()][index.getX()], t2[index.getY()][index.getX()]);
	}
	
	/**
	 * This method will replace the element present at the given row and column
	 * index as specified by the arguments 'row' and 'column' respectively by
	 * the complex element specified by argument 'element'.
	 * <p>
	 * If the arguments 'row' and 'column' over shoot valid range of indexing,
	 * an ArrayIndexOutOfBound Exception will be thrown.
	 * 
	 * @param Complex
	 *            element
	 * @param int row
	 * @param int column
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(Complex element, int row, int column) {
		this.ii[row][column] = element.getRealPart();
		this.t2[row][column] = element.getImaginaryPart();
	}

	/**
	 * This method will replace the complex element present at the given row and column
	 * index position as specified by the {@link Index2D} 'index' by the complex element 
	 * specified by argument 'element'.
	 * <p>If the index position as specified by Index2D 'index' is outside the valid
	 * index bounds of the CCell, an ArrayIndexOutOfBound Exception will be thrown.
	 *
	 * @param Complex element
	 * @param Index2D index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(Complex element, Index2D index) {
		this.ii[index.getY()][index.getX()] = element.getRealPart();
		this.t2[index.getY()][index.getX()] = element.getImaginaryPart();
	}
	
	/**
	 * This method will return the required row of the given CCell as specified
	 * by the argument 'index'. The required row is returned as a
	 * {@link CVector} object.
	 * <p>
	 * The indexing of the row starts from 0. If argument 'index' is not in the
	 * range of [0 row count -1], this method will throw an ArrayIndexOutOfBound
	 * Exception.
	 * 
	 * @param int index
	 * @return CVector
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector accessRow(int index) {
		try {
			return new CVector(ii[index], t2[index]);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will replace the specified row element given by the argument
	 * 'index' of the CCell with the row element as given by the CVector
	 * argument 'row'.
	 * <p>
	 * The length of the CVector should be same as that of the column count for
	 * this CCell else this method will throw a DimensionMismatch Exception.
	 * <p>
	 * The indexing of the row starts from 0. If argument 'index' is not in the
	 * range of [0 row count -1], this method will throw an ArrayIndexOutOfBound
	 * Exception.
	 * 
	 * @param CVector
	 *            row
	 * @param int index
	 * @throws DimensionMismatchException
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignRow(CVector row, int index)
			throws DimensionMismatchException {
		if (row.length() != getColCount())
			throw new DimensionMismatchException();

		this.ii[index] = row.accessRealPart().accessVectorBuffer();
		this.t2[index] = row.accessImaginaryPart().accessVectorBuffer();
	}

	/**
	 * This method will perform an element by element conjugate operation on the
	 * elements of the of the given CCell and return the resultant CCell
	 * containing corresponding conjugate complex elements.
	 * <p>
	 * The return CCell will have the similar dimension as that of this CCell
	 * and corresponding element of return CCell will be the conjugate of the
	 * element of this CCell.
	 * 
	 * @return CCell
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell getConjugate() {
		Cell cReal = new Cell(getRowCount(), getColCount());
		Cell cImag = new Cell(getRowCount(), getColCount());

		for (int i = 0; i < cReal.getRowCount(); i++) {
			for (int j = 0; j < cImag.getColCount(); j++) {
				cReal.setElement(ii[i][j], i, j);
				cImag.setElement(-t2[i][j], i, j);
			}
		}

		try {
			return new CCell(cReal, cImag);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will compute the magnitude value of all the elements of the
	 * given CCell and return the resultant Magnitudes as a {@link Cell}.
	 * <p>
	 * The dimension of the return Cell will be similar to that of this CCell
	 * and each element of the Cell will be the magnitude value for the
	 * corresponding complex element of this CCell.
	 * 
	 * @return Cell
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getMagnitude() {
		Cell result = new Cell(getRowCount(), getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				float mag = (float) Math.sqrt(ii[i][j] * ii[i][j]
						+ t2[i][j] * t2[i][j]);
				result.setElement(mag, i, j);
			}
		}

		return result;

	}

	/**
	 * This method will extract the real part of all the elements of the given
	 * CCell and return the result as a Cell
	 * <p>
	 * <p>
	 * The dimension of the return Cell will be similar to that of this CCell
	 * and each element of the Cell will be the real part for the corresponding
	 * complex element of this CCell.
	 * 
	 * @return Cell
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessRealPart() {
		try {
			return new Cell(ii);
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will extract the imaginary part of all the elements of the
	 * given CCell and return the result as a Cell
	 * <p>
	 * <p>
	 * The dimension of the return Cell will be similar to that of this CCell
	 * and each element of the Cell will be the imaginary part for the
	 * corresponding complex element of this CCell.
	 * 
	 * @return Cell
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessImaginaryPart() {
		try {
			return new Cell(t2);
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will compute the phase angles of all the elements of the
	 * given CCell and return the resultant phase angles as a Cell.
	 * <p>
	 * The dimension of the return Cell will be similar to that of this CCell
	 * and each element of the Cell will be the phase angle for the
	 * corresponding complex element of this CCell.
	 * 
	 * @return Cell
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getAngle() {
		Cell result = new Cell(getRowCount(), getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				float phase = (float) Math.atan2(t2[i][j], ii[i][j]);
				result.setElement(phase, i, j);
			}
		}

		return result;

	}

	/**
	 * This method will return the clone of the given CCell object.
	 * <p>
	 * The clone is the exact replica of the original CCell with same
	 * dimensions.
	 * 
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell clone() {
		try {
			CCell clone = new CCell(this.getRowCount(), this.getColCount());

			for (int i = 0; i < clone.getRowCount(); i++) {
				for (int j = 0; j < clone.getColCount(); j++) {
					clone.setElement(this.getElement(i, j), i, j);
				}
			}

			return clone;
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will return the transpose of the given CCell as a CCell.
	 * <p>
	 * Transposed operation interchanges the Rows of the given CCell with the
	 * corresponding Columns.
	 * <p>
	 * The dimensionality of the returned transposed CCell will be inverse of
	 * that of the given CCell i.e. row count will be replaced with column count
	 * and vice versa.
	 * 
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell getTransposed() {

		CCell result;
		try {
			result = new CCell(getColCount(), getRowCount());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		
		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.ii[i][j] = ii[j][i];
				result.t2[i][j] = t2[j][i];
			}
		}

		return result;
	}

	/**
	 * This method will save the current state of this {@link CCell} object to
	 * an external file with file name as specified by the argument 'filename'
	 * and path as specified by the argument 'dirPath'.
	 * <p>
	 * The argument 'filename' should not contain '.' charactor and any
	 * extension. Further an extension '.ccell' will be added to the saved state
	 * file by this method.
	 * <p>
	 * The current state of this {@link CCell} object stored in the external
	 * file can be loaded into the framework at later stage using the
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
		String filePath = dirPath + filename + ".ccell";

		FileOutputStream fs = new FileOutputStream(filePath);
		ObjectOutputStream os = new ObjectOutputStream(fs);

		os.writeObject(this);
		os.close();
		fs.close();

	}

	/**
	 * This method load the saved state of the {@link CCell} object saved in the
	 * external file as specified by the argument 'filePath' and return the same
	 * as a {@link CCell}.
	 * <p>
	 * The argument 'filePath' should provide the full path to an external file
	 * which contain the saved state of a {@link CCell} object. Further the
	 * extension of the file should be '.ccell'.
	 * <p>
	 * If the extension of the external file containing the saved state of a
	 * {@link CCell} object is not '.ccell' or is not a valid save state file of
	 * a {@link CCell} object this method will throw an Illegal Argument
	 * Exception.
	 * <p>
	 * If the argument 'filePath' does not point to a file or if this method
	 * encounters any IO issues an IO Exception will be thrown.
	 * 
	 * @param String
	 *            filePath
	 * @return CCell
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell retrieveState(String filePath) throws IOException,
			IllegalArgumentException {
		String format = filePath.substring(filePath.indexOf('.') + 1);

		if (!format.equalsIgnoreCase("ccell"))
			throw new IllegalArgumentException();

		FileInputStream fs = new FileInputStream(filePath);
		ObjectInputStream os = new ObjectInputStream(fs);

		CCell ccell = null;

		try {
			ccell = (CCell) os.readObject();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException();
		}

		os.close();
		fs.close();

		return ccell;
	}

	/**
	 * This method will check if the row and column indexes as specified by the
	 * arguments 'row' and 'column' respectively falls within the valid bounds
	 * of this CCell.
	 * 
	 * @param int row
	 * @param int column
	 * @return boolean
	 */
	public boolean isWithinBounds(int row, int column) {
		if (row >= 0 && row < ii.length && column >= 0
				&& column < t2[0].length)
			return true;
		else
			return false;
	}
	
	/**
	 * This method will check if the 2D index position as specified by {@link Index2D} 'index'
	 * is within the valid index bounds for this CCell.
	 * @param Index2D index
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isWithinBounds(Index2D index) {
		if (index.getY() >= 0 && index.getY() < ii.length && index.getX() >= 0
				&& index.getX() < t2[0].length)
			return true;
		else
			return false;
	}
	
	/**
	 * This method check whether the CCell 'cell' has dimensions similar to that
	 * of this CCell.
	 * @param CCell cell
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean hasSameDimensions(CCell cell){
		
		return (cell.getRowCount() == this.getRowCount()) && (cell.getColCount() == this.getColCount());
	}
	
	/**
	 * This will return an instance of {@link CCellIterator} associated with the given CCell.
	 * <p>Each CCell object maintains a single internal instance of CCellIterator object. Getting
	 * CCellIterator through this method avoid creation of multiple instances of CCellIterator associated
	 * with the given CCell.
	 * @return CCellIterator
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCellIterator getAssociatedIterator(){
		if(iterator == null)
			iterator = new CCellIterator(this);
		return iterator;
	}
	
	/**
	 * This method return the largest possible iterable bounds for the given CCell as {@link Iterator2DBound}.
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
}
