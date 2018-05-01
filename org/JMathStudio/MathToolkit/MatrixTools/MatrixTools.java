package org.JMathStudio.MathToolkit.MatrixTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.DivideByZeroException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.JAMA.CholeskyDecompositionAdapter;
import org.JMathStudio.MathToolkit.MatrixTools.JAMA.EigenvalueDecompositionAdapter;
import org.JMathStudio.MathToolkit.MatrixTools.JAMA.LUDecompositionAdapter;
import org.JMathStudio.MathToolkit.MatrixTools.JAMA.MatrixAdapter;
import org.JMathStudio.MathToolkit.MatrixTools.JAMA.SingularValueDecompositionAdapter;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;
import org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics.VectorStatistics;

/**
 * This class provide Matrix utilities and operations define on a real scalar matrix.
 * <p>
 * A real scalar matrix is a 2D array of real numbers and thus will be represented here 
 * by a {@link Cell} object.
 * <p>
 * Within this class a Matrix and a Cell will be used inter-changeably with;
 * <p><i>
 * i)Row count of the Cell will give the number of rows in the matrix.
 * <p>
 * ii)Column count of the Cell will give the number of columns in the matrix.
 * <p>
 * iii)Row and column indexing will start from 0 to one less than row count
 * and column count respectively.
 * <p>
 * iv)Each element of the matrix will be identified uniquely by its row and column
 * index value.
 * </i>
 * <p>
 * Some of the Matrix decomposition utilities define here are based upon the JAMA SDK API. 
 * <pre>Usage:
 *  Let 'a' & 'b' be Cell's with same dimension.
 *  A real Matrix of order 2 is represented by a Cell object.
 *  
 *  MatrixTools mt = new MatrixTools();//Create an instance of MatrixTools.
 *  
 *  float rank = mt.rank(a);//Compute rank of Cell 'a'.
 *  float norm = mt.norm(b);//Compute norm of Cell 'b'.
 *  Cell product = mt.dotProduct(a, b);//Compute dot product between Cell 'a' and 'b'.
 *  CellStack eigen = mt.eigenDecomposition(a);//Get eigen decomposition of square Cell 'a'.
 *  </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class MatrixTools {
	
	/**
	 * This method will compute and return the determinant of the real matrix as
	 * represented by the Cell 'matrix'.
	 * <p>The 'matrix' has to be a square matrix with equal number of rows and columns
	 * else this method will throw an IllegalArgument Exception.
	 * @param Cell
	 *            matrix
	 * @return double
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public double determinant(Cell matrix) throws IllegalArgumentException {
		if(!matrix.isSquareCell())
			throw new IllegalArgumentException();
		else
			return new MatrixAdapter(matrix).det();
	}

	/**
	 * This method will compute and return the trace of the real matrix as
	 * represented by the Cell 'matrix'.
	 * 
	 * @param Cell
	 *            matrix
	 * @return double
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double trace(Cell matrix) {
		return new MatrixAdapter(matrix).trace();
	}

	/**
	 * This method will compute and return the rank of the real matrix as
	 * represented by the Cell 'matrix'.
	 * 
	 * @param Cell
	 *            matrix
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float rank(Cell matrix) {
		return (float) new MatrixAdapter(matrix).rank();
	}

	/**
	 * This method will perform Eigen Decomposition of the real square matrix
	 * represented by the Cell 'matrix' and will return the result as CellStack.
	 * <p>
	 * The return CellStack will have following Cells or Matrix in it,
	 * <p>
	 * Cell - 0 ->Eigen Vector Matrix 'V'.
	 * <p>
	 * Cell - 1 ->Diagonal Eigen Value Matrix 'D'.
	 * <p>
	 * The matrix represented by Cell 'matrix' should be a square matrix i.e.
	 * row count and column count of 'matrix' should be same else this method
	 * will throw an IllegalArgument Exception.
	 * <h4>JAMA description for Eigen value decomposition:</h4>
	 * <p><i>
	 * Eigenvalues and eigenvectors of a real matrix.
	 * <P>
	 * If A is symmetric, then A = V*D*V' where the eigenvalue matrix D is diagonal
	 * and the eigenvector matrix V is orthogonal. I.e. A =
	 * V.times(D.times(V.transpose())) and V.times(V.transpose()) equals the
	 * identity matrix.
	 * <P>
	 * If A is not symmetric, then the eigenvalue matrix D is block diagonal with
	 * the real eigenvalues in 1-by-1 blocks and any complex eigenvalues, lambda +
	 * i*mu, in 2-by-2 blocks, [lambda, mu; -mu, lambda]. The columns of V represent
	 * the eigenvectors in the sense that A*V = V*D, i.e. A.times(V) equals
	 * V.times(D). The matrix V may be badly conditioned, or even singular, so the
	 * validity of the equation A = V*D*inverse(V) depends upon conditions.
	 * </i>
	 * @param Cell
	 *            matrix
	 * @return CellStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack eigenDecomposition(Cell matrix)
			throws IllegalArgumentException {
		
		int rc = matrix.getRowCount();
		int cc = matrix.getColCount();
		
		if(rc != cc)
			throw new IllegalArgumentException();
		
		try{
		CellStack res = new CellStack(2);

		EigenvalueDecompositionAdapter eig = new EigenvalueDecompositionAdapter
		(new MatrixAdapter(matrix));
		
		Cell cell1 = eig.getDAdapter();
		Cell cell0 = eig.getVAdapter();

		res.addCell(cell0);
		res.addCell(cell1);

		return res;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will perform a Singular Value Decomposition on the real
	 * matrix represented by the Cell 'matrix' and return the result as a
	 * CellStack.
	 * <p>
	 * The return CellStack will have following Cells or Matrix in it,
	 * <p>
	 * Cell - 0 ->Diagonal Matrix of Singular Values arrange in descending order
	 * of values 'S'.
	 * <p>
	 * Cell - 1 ->Left Singular Vector Matrix 'U'.
	 * <p>
	 * Cell - 2 ->Right Singular Vector Matrix 'V'.
	 * <p>
	 * The Cell 'matrix' row count should be more than or equal to its column count
	 * else this method will throw an IllegalArgument Exception.
	 * <h4>JAMA description for Singular value decomposition:</h4>
	 * <p><i>
	 * Singular Value Decomposition.
	 * <P>
	 * For an m-by-n matrix A with m >= n, the singular value decomposition is an
	 * m-by-n orthogonal matrix U, an n-by-n diagonal matrix S, and an n-by-n
	 * orthogonal matrix V so that A = U*S*V'.
	 * <P>
	 * The singular values, sigma[k] = S[k][k], are ordered so that sigma[0] >=
	 * sigma[1] >= ... >= sigma[n-1].
	 * <P>
	 * The singular value decompostion always exists. The matrix condition number and the 
	 * effective numerical rank can be computed from this decomposition.
	 * </i>
 	 * @param Cell
	 *            matrix
	 * @return CellStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack SVD(Cell matrix)
			throws IllegalArgumentException {
		
		int rc = matrix.getRowCount();
		int cc = matrix.getColCount();
		
		if(rc < cc)
			throw new IllegalArgumentException();
		
		try{
		CellStack res = new CellStack(3);

		SingularValueDecompositionAdapter svd = new SingularValueDecompositionAdapter
		(new MatrixAdapter(matrix));
		
		Cell S = svd.getSAdapter();
		Cell U = svd.getUAdapter();
		Cell V = svd.getVAdapter();
		
		res.addCell(S);
		res.addCell(U);
		res.addCell(V);

		return res;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}	
	}

	/**
	 * This method will perform a LU Decomposition on the real matrix
	 * represented by the Cell 'matrix' and return the result as a CellStack.
	 * <p>
	 * The return CellStack will have following Cells or Matrix in it,
	 * <p>
	 * Cell - 0 ->Lower Triangular Factor Matrix 'L'.
	 * <p>
	 * Cell - 1 ->Upper Triangular Factor Matrix 'U'.
	 * <h4>JAMA description for LU decomposition: </h4>
	 * <p><i>LU Decomposition.
     * <P>
     * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n
     * unit lower triangular matrix L, an n-by-n upper triangular matrix U,
     * and a permutation vector piv of length m so that A(piv,:) = L*U.
     * If m < n, then L is m-by-m and U is m-by-n.
     * <P>
     * The LU decompostion with pivoting always exists, even if the matrix is
     * singular, so the constructor will never fail.  The primary use of the
     * LU decomposition is in the solution of square systems of simultaneous
     * linear equations.  This will fail if matrix A is Non singular.
     * </i>    
	 * @param Cell
	 *            matrix
	 * @return CellStack
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack LUDecomposition(Cell matrix) {
		try{
		CellStack res = new CellStack(2);

		LUDecompositionAdapter lud = new LUDecompositionAdapter(new MatrixAdapter(matrix));
		Cell L = lud.getLAdapter();
		Cell U = lud.getUAdapter();

		res.addCell(L);
		res.addCell(U);

		return res;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will perform the Cholesky Decomposition on the real symmetric
	 * and positive definite matrix as represented by the Cell 'matrix' and will
	 * return the Triangular Factor Matrix as a Cell.
	 * <p>
	 * If the matrix is not a Symmetric and Positive definite, a partial
	 * decomposition will be returned.
	 * <p>
	 * Use method 'isSymmetricAndPositiveDefiniteMatrix' to check for this
	 * condition before using this method.
	 * <h4>JAMA description for Cholesky Decomposition:</h4>
	 *  <p><i>
	 *  For a symmetric, positive definite matrix A, the Cholesky decomposition
	 *  is an lower triangular matrix L so that A = L*L'.
	 *  <P>
	 *  If the matrix is not symmetric or positive definite, the constructor
	 *  returns a partial decomposition.
	 *  </i>
	 * @param Cell
	 *            matrix
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell choleskyDecomposition(Cell matrix) {
		return new CholeskyDecompositionAdapter(new MatrixAdapter(matrix)).getLAdapter();
	}

	/**
	 * This method will check whether the real matrix represented by the Cell
	 * 'matrix' is Symmetric and Positive Definite matrix.
	 * 
	 * @param Cell
	 *            matrix
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isSymmetricAndPositiveDefiniteMatrix(Cell matrix) {
		return new CholeskyDecompositionAdapter(new MatrixAdapter(matrix)).isSPD();		
	}

	/**
	 * This method will check whether the real matrix represented by the Cell
	 * 'matrix' is a NonSingular matrix.
	 * 
	 * @param Cell
	 *            matrix
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isNonSingularMatrix(Cell matrix) {
		return new LUDecompositionAdapter(new MatrixAdapter(matrix)).isNonsingular();		
	}

	/**
	 * This method will return the Inverse matrix of the real matrix represented
	 * by the Cell 'matrix' as a Cell.
	 * <p>
	 * If the inverse is not define for this matrix than this method will return
	 * a Psuedo Inverse of the given matrix.
	 * 
	 * @param Cell
	 *            matrix
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell inverse(Cell matrix) {
		return new MatrixAdapter(matrix).inverseAdapter();
	}

	/**
	 * This method will perform the element by element product between the two
	 * real matrices as represented by the Cell 'matrix1' and 'matrix2' and
	 * return the resultant matrix as a Cell.
	 * <p>
	 * The dimension of both the Cell 'matrix1' and 'matrix2' should be the same
	 * or else this method will throw a DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            matrix1
	 * @param Cell
	 *            matrix2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dotProduct(Cell matrix1, Cell matrix2)
			throws DimensionMismatchException {

		if(!matrix1.hasSameDimensions(matrix2))
			throw new DimensionMismatchException();
		
		int rc = matrix1.getRowCount();
		int cc = matrix2.getColCount();
		
		Cell result = new Cell(rc, cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.setElement(matrix1.getElement(i, j)
						* matrix2.getElement(i, j), i, j);
			}
		}

		return result;
	}

	/**
	 * This method will perform the dot division between the two real matrices
	 * as represented by the Cell 'matrix1' and 'matrix2' and return the
	 * resultant matrix as a Cell.
	 * <p>
	 * This method will divide each element of the 'matrix1' by the
	 * corresponding element of the 'matrix2'. If any of the element of
	 * 'matrix2' is found to be zero, this method will throw an DivideByZero
	 * Exception.
	 * <p>
	 * The dimension of both the Cell 'matrix1' and 'matrix2' should be the same
	 * or else this method will throw a DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            matrix1
	 * @param Cell
	 *            matrix2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dotDivision(Cell matrix1, Cell matrix2)
			throws DimensionMismatchException, DivideByZeroException {
		if(!matrix1.hasSameDimensions(matrix2))
			throw new DimensionMismatchException();
		
		int rc = matrix1.getRowCount();
		int cc = matrix2.getColCount();
		
		Cell result = new Cell(rc, cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (matrix2.getElement(i, j) == 0)
					throw new DivideByZeroException();
				result.setElement(matrix1.getElement(i, j)
						/ matrix2.getElement(i, j), i, j);
			}
		}

		return result;
	}

	/**
	 * This method will perform the element by element addition between the two
	 * real matrices as represented by the Cell 'matrix1' and 'matrix2' and
	 * return the resultant matrix as a Cell.
	 * <p>
	 * The dimension of both the Cell 'matrix1' and 'matrix2' should be the same
	 * or else this method will throw a DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            matrix1
	 * @param Cell
	 *            matrix2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell add(Cell matrix1, Cell matrix2)
			throws DimensionMismatchException {
		if(!matrix1.hasSameDimensions(matrix2))
			throw new DimensionMismatchException();
		
		int rc = matrix1.getRowCount();
		int cc = matrix2.getColCount();
		
		Cell result = new Cell(rc, cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.setElement(matrix1.getElement(i, j)
						+ matrix2.getElement(i, j), i, j);
			}
		}

		return result;
	}

	/**
	 * This method will perform the element by element subtraction between the
	 * two real matrices as represented by the Cell 'matrix1' and 'matrix2' and
	 * return the resultant matrix as a Cell.
	 * <p>
	 * This method will subtract the elements of 'matrix2' from the
	 * corresponding elements of the 'matrix1'.
	 * <p>
	 * The dimension of both the Cell 'matrix1' and 'matrix2' should be the same
	 * or else this method will throw a DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            matrix1
	 * @param Cell
	 *            matrix2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell subtract(Cell matrix1, Cell matrix2)
			throws DimensionMismatchException {
		if(!matrix1.hasSameDimensions(matrix2))
			throw new DimensionMismatchException();
		
		int rc = matrix1.getRowCount();
		int cc = matrix2.getColCount();
		
		Cell result = new Cell(rc, cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.setElement(matrix1.getElement(i, j)
						- matrix2.getElement(i, j), i, j);
			}
		}

		return result;
	}

	/**
	 * This method will perform the element by element inverse operation on the
	 * real matrix as represented by the Cell 'matrix' and return the resultant
	 * matrix as a Cell.
	 * <p>
	 * Each element of the resultant matrix will be the inverse of the
	 * corresponding element of the matrix 'matrix'.
	 * <p>
	 * If any of the element of matrix 'matrix' is found to be zero this method
	 * will throw an DivideByZero Exception.
	 * 
	 * @param Cell
	 *            matrix
	 * @return Cell
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell dotInverse(Cell matrix) throws DivideByZeroException {

		int rc = matrix.getRowCount();
		int cc = matrix.getColCount();
		
		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (matrix.getElement(i, j) == 0) {
					throw new DivideByZeroException();
				}
				result.setElement(1.0f / matrix.getElement(i, j), i, j);
			}
		}

		return result;

	}

	/**
	 * This method will perform the cross product between the two real matrices
	 * as represented by the Cells 'matrix1' and 'matrix2' and return the
	 * resultant matrix as Cell.
	 * <p>
	 * The column count of the Cell 'matrix1' should be equal to the row count
	 * of the Cell 'matrix2' else this method will throw a DimensionMismatch
	 * Exception.
	 * 
	 * @param Cell
	 *            matrix1
	 * @param Cell
	 *            matrix2
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell crossProduct(Cell matrix1, Cell matrix2)
			throws DimensionMismatchException {

		int rc = matrix2.getRowCount();
		int cc = matrix1.getColCount();
		
		if (cc != rc) {
			throw new DimensionMismatchException();
		}

		Cell result = new Cell(matrix1.getRowCount(), matrix2.getColCount());

		Cell flip = transpose(matrix2);
		VectorStatistics stat = new VectorStatistics();
				
		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				float tmp = stat.sum(VectorMath.dotProduct(matrix1.accessRow(i), flip.accessRow(j)));
				result.setElement(tmp, i, j);
			}
		}

		return result;

	}

	/**
	 * This method will return the coefficient of Inner product between the two
	 * real matrices as represented by the {@link Cell}'s 'matrix1' and 'matrix2'
	 * respectively.
	 * <p>
	 * The dimension of both the Cell's should be the same else this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            matrix1
	 * @param Cell
	 *            matrix2
	 * @return float
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float innerProduct(Cell matrix1, Cell matrix2)
			throws DimensionMismatchException {
		Cell product = dotProduct(matrix1, matrix2);

		return new CellStatistics().sum(product);
	}
	
	/**
	 * This method will return the Transpose of the real matrix as represented
	 * by the Cell 'matrix' as a Cell.
	 * 
	 * @param Cell
	 *            matrix
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell transpose(Cell matrix) {
		Cell result = new Cell(matrix.getColCount(), matrix.getRowCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(matrix.getElement(j, i), i, j);
			}
		}

		return result;
	}

	/**
	 * This method will flip the order of the rows of the real matrix as
	 * represented by the Cell 'matrix' and return the resultant matrix as a
	 * Cell.
	 * <p>
	 * Flip operation will reverse or flip the index or arrangement of the rows
	 * in the matrix such that the row at index position 'i' will be relocated
	 * to the index position 'N-1-i', where 'N' is the row count of the matrix.
	 * 
	 * @param Cell
	 *            matrix
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 **/
	public Cell flipRows(Cell matrix) {

		int rc = matrix.getRowCount();
		int cc = matrix.getColCount();
		
		Cell result = new Cell(rc, cc);
		int shift = rc-1;

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(matrix.getElement(shift-i, j), i, j);
			}
		}

		return result;

	}

	/**
	 * This method will swap the column elements of the matrix represented by
	 * the Cell 'matrix' located at the index position as given by the argument
	 * 'TargetColumn' and 'DestinationColumn'. The resultant matrix will be
	 * return as a Cell.
	 * <p>
	 * The index positions given by the argument 'TargerColumn' and
	 * 'DestinationColumn' should be in the range of 0 and one less than the
	 * column count of Cell 'matrix', else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            matrix
	 * @param int TargetColumn
	 * @param int DestinationColumn
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell swapColumn(Cell matrix, int TargetColumn, int DestinationColumn)
	throws IllegalArgumentException {

		int rc = matrix.getRowCount();
		int cc = matrix.getColCount();

		if (TargetColumn < 0 || TargetColumn >= cc) {
			throw new IllegalArgumentException();
		}

		if (DestinationColumn < 0 || DestinationColumn >= cc) {
			throw new IllegalArgumentException();
		}

		Cell result = new Cell(rc, cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (j == TargetColumn) {
					result.setElement(matrix.getElement(i, DestinationColumn),
							i, j);
				} else if (j == DestinationColumn) {
					result.setElement(matrix.getElement(i, TargetColumn), i, j);
				} else {
					result.setElement(matrix.getElement(i, j), i, j);
				}
			}
		}
		return result;
	}
	
	/**
	 * This method will swap the row elements of the matrix represented by Cell
	 * 'matrix' located at the index position as given by the argument
	 * 'TargetRow' and 'DestinationRow'. The resultant matrix will be return as
	 * a Cell.
	 * <p>
	 * The index positions given by the argument 'TargerRow' and
	 * 'DestinationRow' should be in the range of 0 and one less than the row
	 * count of Cell 'matrix', else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param Cell
	 *            matrix
	 * @param int TargetRow
	 * @param int DestinationRow
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell swapRow(Cell matrix, int TargetRow, int DestinationRow)
	throws IllegalArgumentException {

		int rc = matrix.getRowCount();
		int cc = matrix.getColCount();

		if (TargetRow < 0 || TargetRow >= rc) {
			throw new IllegalArgumentException();
		}

		if (DestinationRow < 0 || DestinationRow >= rc) {
			throw new IllegalArgumentException();
		}

		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (i == TargetRow) {
					result.setElement(matrix.getElement(DestinationRow, j), i,
							j);
				} else if (i == DestinationRow) {
					result.setElement(matrix.getElement(TargetRow, j), i, j);
				} else {
					result.setElement(matrix.getElement(i, j), i, j);
				}
			}
		}
		return result;
	}

	/**
	 * This method will flip the order of the columns of the matrix as
	 * represented by the Cell 'matrix' and return the resultant matrix as a
	 * Cell.
	 * <p>
	 * Flip operation will reverse or flip the index or arrangement of the
	 * columns in the given matrix such that the column at index position 'i'
	 * will be relocated to the index position 'N-1-i', where 'N' is the column
	 * count of the matrix.
	 * 
	 * @param Cell
	 *            matrix
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 **/
	public Cell flipColumns(Cell matrix) {

		int rc = matrix.getRowCount();
		int cc = matrix.getColCount();
		
		Cell result = new Cell(rc, cc);
		int shift = cc-1;

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				result.setElement(matrix.getElement(i, shift - j), i, j);
			}
		}

		return result;

	}

	/**
	 * This method will check whether the two real matrices as represented by
	 * the Cells 'matrix1' and 'matrix2' are equal.
	 * <p>
	 * Two matrix are said to be equal if,
	 * <p>
	 * 1. Dimensions of both the matrices are same and,
	 * <p>
	 * 2. Corresponding elements of the matrices are same.
	 * 
	 * @param Cell
	 *            matrix1
	 * @param Cell
	 *            matrix2
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isEqual(Cell matrix1, Cell matrix2) {
		if (matrix1.getRowCount() != matrix2.getRowCount())
			return false;

		if (matrix1.getColCount() != matrix2.getColCount())
			return false;

		for (int i = 0; i < matrix1.getRowCount(); i++) {
			for (int j = 0; j < matrix1.getColCount(); j++) {
				if (matrix1.getElement(i, j) != matrix2.getElement(i, j))
					return false;

			}
		}

		return true;
	}

	/**
	 * This method will return the norm or Euclidian distance of the real matrix
	 * as represented by the Cell 'matrix'.
	 * <p>
	 * The norm of the matrix is the Square root of the sum of squares of all
	 * the elements in the matrix.
	 * 
	 * @param Cell
	 *            matrix
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float norm(Cell matrix) {
		float sum = 0;

		for (int i = 0; i < matrix.getRowCount(); i++) {
			for (int j = 0; j < matrix.getColCount(); j++) {
				sum = sum + matrix.getElement(i, j) * matrix.getElement(i, j);
			}
		}

		return (float) Math.sqrt(sum);
	}

	/**
	 * This method solves for A*X = B, where square matrix 'A' is represented by
	 * the Cell 'matrixA' and matrix 'B' is represented by the Cell 'matrixB'.
	 * <p>
	 * The solution of this ie. matrix 'X' is return as a Cell.
	 * <p>
	 * If matrix 'A' is not a square matrix i.e. if row count of 'matrixA' is
	 * not equal to its column count than a least squares solution is return.
	 * 
	 * @param Cell
	 *            matrixA
	 * @param Cell
	 *            matrixB
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell solve(Cell matrixA, Cell matrixB) {
		MatrixAdapter B = new MatrixAdapter(matrixB);
		MatrixAdapter A = new MatrixAdapter(matrixA);

		return A.solveAdapter(B);
	}

	/**
	 * This method solves for X*A = B, where square matrix 'A' is represented by
	 * the Cell 'matrixA' and matrix 'B' is represented by the Cell 'matrixB'.
	 * The solution of this ie. matrix 'X' is return as a Cell.
	 * <p>
	 * If matrix 'A' is not a square matrix i.e. if row count of 'matrixA' is
	 * not equal to its column count than a least squares solution is return.
	 * 
	 * @param Cell
	 *            matrixA
	 * @param Cell
	 *            matrixB
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell solveTranspose(Cell matrixA, Cell matrixB) {
		MatrixAdapter B = new MatrixAdapter(matrixB);
		MatrixAdapter A = new MatrixAdapter(matrixA);

		return A.solveTransposeAdapter(B);
	}

	/**
	 * This method will create a random real matrix with element values in the
	 * range of [0 1] and return the same as a Cell. The argument 'row' and
	 * 'column' here specify row count and column count for the matrix
	 * respectively.
	 * <p>
	 * The argument 'row' and 'column' should be more than 0 else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int row
	 * @param int column
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell randomMatrix(int row, int column)
			throws IllegalArgumentException {
		if (row < 1 || column < 1) {
			throw new IllegalArgumentException();
		}
		Cell result = new Cell(row, column);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				result.setElement((float) Math.random(), i, j);
			}
		}

		return result;

	}

	/**
	 * This method will create an uniform real matrix with each element of the
	 * matrix taking a common value as specified by the argument 'value' and
	 * return the same as a Cell.
	 * 
	 * <p>
	 * The argument 'row' and 'column' here specify row count and column count
	 * for the matrix respectively.
	 * <p>
	 * The argument 'row' and 'column' should be more than 0 else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int row
	 * @param int column
	 * @param float value
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell uniformMatrix(int row, int column, float value)
			throws IllegalArgumentException {
		if (row < 1 || column < 1) {
			throw new IllegalArgumentException();
		}
		Cell result = new Cell(row, column);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				result.setElement(value, i, j);
			}
		}

		return result;
	}
	
}
