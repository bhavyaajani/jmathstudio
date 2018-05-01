package org.JMathStudio.MathToolkit.MatrixTools.JAMA;

/**
 * Adapter class over {@link QRDecomposition} JAMA class.
 * 
 * <p>This class is part of cross communication bridge between JAMA and current framework.
 * The class has only design utility and should not be used for any purpose outside the
 * package.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class QRDecompositionAdapter extends QRDecomposition{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6713435626765689086L;

	/** QR Decomposition.
	<P>
	   For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n
	   orthogonal matrix Q and an n-by-n upper triangular matrix R so that
	   A = Q*R.
	<P>
	   The QR decompostion always exists, even if the matrix does not have
	   full rank, so the constructor will never fail.  The primary use of the
	   QR decomposition is in the least squares solution of nonsquare systems
	   of simultaneous linear equations.  This will fail if isFullRank()
	   returns false.
	*/
	public QRDecompositionAdapter(MatrixAdapter matrix)
	{
		super(matrix);
	}
}
