package org.JMathStudio.MathToolkit.MatrixTools.JAMA;

/**
 * Adapter class over {@link CholeskyDecomposition} JAMA class.
 * 
 * <p>This class is part of cross communication bridge between JAMA and current framework.
 * The class has only design utility and should not be used for any purpose outside the
 * package.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CholeskyDecompositionAdapter extends CholeskyDecomposition{

	/**
	 * 
	 */
	private static final long serialVersionUID = 472298813882806926L;

	/** Cholesky Decomposition.
	   <P>
	   For a symmetric, positive definite matrix A, the Cholesky decomposition
	   is an lower triangular matrix L so that A = L*L'.
	   <P>
	   If the matrix is not symmetric or positive definite, the constructor
	   returns a partial decomposition and sets an internal flag that may
	   be queried by the isSPD() method.
	   */
	public CholeskyDecompositionAdapter(MatrixAdapter matrix)
	{
		super(matrix);
	}
}
