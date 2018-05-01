package org.JMathStudio.MathToolkit.MatrixTools.JAMA;

/**
 * Adapter class over {@link LUDecomposition} JAMA class.
 * 
 * <p>This class is part of cross communication bridge between JAMA and current framework.
 * The class has only design utility and should not be used for any purpose outside the
 * package.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class LUDecompositionAdapter extends LUDecomposition{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5704245382403793077L;

	 /** LU Decomposition.
	   <P>
	   For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n
	   unit lower triangular matrix L, an n-by-n upper triangular matrix U,
	   and a permutation vector piv of length m so that A(piv,:) = L*U.
	   If m < n, then L is m-by-m and U is m-by-n.
	   <P>
	   The LU decompostion with pivoting always exists, even if the matrix is
	   singular, so the constructor will never fail.  The primary use of the
	   LU decomposition is in the solution of square systems of simultaneous
	   linear equations.  This will fail if isNonsingular() returns false.
	   */
	public LUDecompositionAdapter(MatrixAdapter matrix)
	{
		super(matrix);
	}
}
