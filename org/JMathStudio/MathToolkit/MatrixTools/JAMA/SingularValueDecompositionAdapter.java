package org.JMathStudio.MathToolkit.MatrixTools.JAMA;

/**
 * Adapter class over {@link SingularValueDecomposition} JAMA class.
 * 
 * <p>This class is part of cross communication bridge between JAMA and current framework.
 * The class has only design utility and should not be used for any purpose outside the
 * package.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SingularValueDecompositionAdapter extends SingularValueDecomposition{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7319598656593327085L;

	/**
	 * Singular Value Decomposition.
	 * <P>
	 * For an m-by-n matrix A with m >= n, the singular value decomposition is an
	 * m-by-n orthogonal matrix U, an n-by-n diagonal matrix S, and an n-by-n
	 * orthogonal matrix V so that A = U*S*V'.
	 * <P>
	 * The singular values, sigma[k] = S[k][k], are ordered so that sigma[0] >=
	 * sigma[1] >= ... >= sigma[n-1].
	 * <P>
	 * The singular value decompostion always exists, so the constructor will never
	 * fail. The matrix condition number and the effective numerical rank can be
	 * computed from this decomposition.
	 */
	public SingularValueDecompositionAdapter(MatrixAdapter matrix)
	{
		super(matrix);
	}
}
