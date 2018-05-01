package org.JMathStudio.MathToolkit.MatrixTools.JAMA;

/**
 * Adapter class over {@link EigenvalueDecomposition} JAMA class.
 * 
 * <p>This class is part of cross communication bridge between JAMA and current framework.
 * The class has only design utility and should not be used for any purpose outside the
 * package.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class EigenvalueDecompositionAdapter extends EigenvalueDecomposition{

	/**
	 * 
	 */
	private static final long serialVersionUID = -886728207935739053L;
	
	/**
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
	 * validity of the equation A = V*D*inverse(V) depends upon V.cond().
	 **/
	public EigenvalueDecompositionAdapter(MatrixAdapter matrix)
	{
		super(matrix);
	}
}
