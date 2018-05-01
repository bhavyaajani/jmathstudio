package org.JMathStudio.DataStructure.Cell;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.DivideByZeroException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define basic mathematical operations on a {@link CCell}.
 * 
 * <pre>Usage:
 * Let 'a' & 'b' be CCells with similar dimension.
 * 
 * CCell c = CCellMath.add(a, b);//Add corresponding elements of input CCells.
 * 
 * CCell d = CCellMath.dotProduct(a, r);//Multiply each element of input CCell by
 * scalar 'r'. 
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class CCellMath {

	//Ensure no instances are made for utility classes.
	private CCellMath(){}
	
	/**
	 * This method will perform an element by element real product or scale
	 * operation between the complex elements of a CCell 'cell' and the real
	 * value given by the argument 'value'. The result is returned as a CCell.
	 * 
	 * @param CCell
	 *            cell
	 * @param float value
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell scale(CCell cell, float value) {
		
		int rc = cell.getRowCount();
		int cc = cell.getColCount();
		
		CCell result;
		try {
			result = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.ii[i][j] = cell.ii[i][j]*value;
				result.t2[i][j] = cell.t2[i][j]*value;
			}
		}

		return result;
	}

	/**
	 * This method will perform an element by element complex product operation
	 * between the corresponding complex elements of the CCells 'cell1' and
	 * 'cell2' and return the result as a CCell.
	 * <p>
	 * The dimension of both the CCells should be similar or this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param CCell
	 *            cell1
	 * @param CCell
	 *            cell2
	 * @return CCell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell dotProduct(CCell cell1, CCell cell2)
			throws DimensionMismatchException {
		if (cell1.getRowCount() != cell2.getRowCount()) {
			throw new DimensionMismatchException();
		}

		if (cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}
		
		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		CCell result;
		try {
			result = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				Complex tmp = cell1.getElement(i, j).product(
						cell2.getElement(i, j));
				result.setElement(tmp, i, j);
			}
		}

		return result;
	}
	
	/**
	 * This method will perform an element by element real product or scale operation
	 * on the complex elements of the CCell 'cell1' with corresponding real scalar
	 * elements of Cell 'cell2' and return the result as a CCell.
	 * <p>
	 * The dimension of both the CCell & Cell should be similar or this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param CCell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return CCell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell dotProduct(CCell cell1, Cell cell2)
			throws DimensionMismatchException {
		if (cell1.getRowCount() != cell2.getRowCount()) {
			throw new DimensionMismatchException();
		}

		if (cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}
		
		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		CCell result;
		try {
			result = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		float scalar;
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				scalar = cell2.i4[i][j];
				result.ii[i][j] = cell1.ii[i][j]*scalar;
				result.t2[i][j] = cell1.t2[i][j]*scalar;
			}
		}

		return result;
	}

	/**
	 * This method will perform an element by element complex division operation
	 * between the corresponding complex elements of the CCells 'cell1' and
	 * 'cell2'. The elements of 'cell1' will be divided by the corresponding
	 * elements of the 'cell2' and result will be return as a CCell.
	 * <p>
	 * If any complex element of the 'cell2' is found to have magnitude of zero,
	 * this method will throw an DivideByZero Exception.
	 * <p>
	 * The dimension of both the CCells should be similar or this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param CCell
	 *            cell1
	 * @param CCell
	 *            cell2
	 * @return CCell
	 * @throws DimensionMismatchException
	 *             DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public final static CCell dotDivision(CCell cell1, CCell cell2)
			throws DimensionMismatchException, DivideByZeroException {
		if (cell1.getRowCount() != cell2.getRowCount()) {
			throw new DimensionMismatchException();
		}

		if (cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}
		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		CCell result;
		try {
			result = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (cell2.getElement(i, j).getMagnitude() == 0) {
					throw new DivideByZeroException();
				} else {
					Complex tmp = cell1.getElement(i, j).divide(
							cell2.getElement(i, j));
					result.setElement(tmp, i, j);
				}
			}
		}

		return result;
	}

	/**
	 * This method will perform an element by element complex addition operation
	 * between the corresponding complex elements of the CCells 'cell1' and
	 * 'cell2' and return the result as a CCell.
	 * <p>
	 * The dimension of both the CCells should be similar or this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param CCell
	 *            cell1
	 * @param CCell
	 *            cell2
	 * @return CCell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell add(CCell cell1, CCell cell2)
			throws DimensionMismatchException {
		if (cell1.getRowCount() != cell2.getRowCount()) {
			throw new DimensionMismatchException();
		}

		if (cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}

		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		CCell res;
		try {
			res = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				res.ii[i][j] = cell1.ii[i][j] + cell2.ii[i][j];
				res.t2[i][j] = cell1.t2[i][j] + cell2.t2[i][j];
			}
		}

		return res;
	}

	/**
	 * This method will perform an element by element complex subtraction
	 * operation between the corresponding complex elements of the CCells
	 * 'cell1' and 'cell2' and return the result as a CCell.
	 * <p>
	 * Here complex elements from 'cell2' are subtracted from the corresponding
	 * complex elements of 'cell1'.
	 * <p>
	 * The dimension of both the CCells should be similar or this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param CCell
	 *            cell1
	 * @param CCell
	 *            cell2
	 * @return CCell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell subtract(CCell cell1, CCell cell2)
			throws DimensionMismatchException {
		if (cell1.getRowCount() != cell2.getRowCount()) {
			throw new DimensionMismatchException();
		}

		if (cell1.getColCount() != cell2.getColCount()) {
			throw new DimensionMismatchException();
		}

		int rc = cell1.getRowCount();
		int cc = cell2.getColCount();
		
		CCell res;
		try {
			res = new CCell(rc,cc);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		
		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				res.ii[i][j] = cell1.ii[i][j] - cell2.ii[i][j];
				res.t2[i][j] = cell1.t2[i][j] - cell2.t2[i][j];
			}
		}

		return res;
	}

}
