package org.JMathStudio.MathToolkit.StatisticalTools.Regression;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;

/**
 * This class defines a Polynomial Regression model which estimates coefficients
 * of polynomial regression for a given set of points in R2 space.
 * <p>
 * Polynomial Regression with degree 'n' for a set of 'm' points in R2 space
 * identify a nth degree polynomial fit in least square error sense such that;
 * ym ~= An*xm^(n) + An-1*xm^(n-1) + ... + A2*xm^(2) + A1*xm + A0; for 'm'
 * points with coordinates (ym,xm). Where; A0, A1, ... ,An-1, An; are
 * coefficients of Polynomial Regression.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class PolynomialRegression {

	private Vector coefficients = null;
	private double R2 = -1;
	private int degree = -1;

	/**
	 * Creates an instance of {@link PolynomialRegression} and computes the
	 * coefficients of polynomial regression model from a set of points in R2
	 * space as specified by their 2D (y,x) coordinates as given by
	 * {@link Vector}s y & x respectively.
	 * <p>
	 * Input Vector 'y' & 'x' should be of equal length and specify 'y' & 'x'
	 * coordinates of individual points under consideration. Hence number of
	 * points 'n' under polynomial regression is equal to length of Vector 'y'
	 * or 'x'.
	 * <p>
	 * Argument 'degree' specify the degree for polynomial regression. If degree
	 * is 1, polynomial regression is equal to Linear regression. If degree is
	 * 2, polynomial regression is equal to Quadratic regression and so on.
	 * <p>
	 * Argument 'degree' should be in the range of [0,n) where 'n' is number of
	 * points under polynomial regression else this method shall throw
	 * {@link IllegalArgumentException}.
	 * <p>
	 * Also if length of input Vectors 'y' & 'x' is not same this method shall
	 * throw an {@link IllegalArgumentException}.
	 * 
	 * @param Vector
	 *            y
	 * @param Vector
	 *            x
	 * @param int degree
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public PolynomialRegression(Vector y, Vector x, int degree)
			throws IllegalArgumentException {
		if (x.length() != y.length())
			throw new IllegalArgumentException();

		if (degree < 0 || degree >= x.length())
			throw new IllegalArgumentException();

		try {
			this.degree = degree;
			int N = x.length();

			float _x[] = x.accessVectorBuffer();
			float _y[] = y.accessVectorBuffer();

			float[][] vm = new float[N][degree + 1];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j <= degree; j++) {
					vm[i][j] = (float) Math.pow(_x[i], j);
				}
			}

			Cell X = new Cell(vm);

			Cell Y = new Cell(_y.length, 1);
			for (int i = 0; i < _y.length; i++)
				Y.setElement(_y[i], i, 0);

			MatrixTools mt = new MatrixTools();

			Cell solve = mt.solve(X, Y);
			if (solve.getColCount() != 1)
				throw new BugEncounterException();

			float res[] = new float[solve.getRowCount()];
			for (int i = 0; i < res.length; i++)
				res[i] = solve.getElement(i, 0);

			coefficients = new Vector(res);

			double sum = 0.0;
			for (int i = 0; i < N; i++)
				sum += _y[i];
			double mean = sum / N;

			double SST = 0;

			for (int i = 0; i < N; i++) {
				double dev = _y[i] - mean;
				SST += dev * dev;
			}

			Cell tmp = mt.crossProduct(X, solve);
			Cell residuals = mt.subtract(tmp, Y);

			float norm = mt.SVD(residuals).accessCell(0).getElement(0, 0);
			float SSE = norm * norm;

			R2 = 1.0 - SSE / SST;

		} catch (Exception e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method returns 'y' coordinate for a hypothetical point on the best
	 * fit polynomial regression model with 'x' coordinate as given by argument
	 * 'x'.
	 * <p>
	 * Based on the polynomial regression model coefficients this method returns
	 * 'y' such that;
	 * <p>
	 * <i> y = An*x^(n) + An-1*x^(n-1) + ... + A2*x^(2) + A1*x + A0;</i> where
	 * 'n' is degree of polynomial regression.
	 * 
	 * @param x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float evaluate(float x) {
		try {
			float y = 0.0f;
			for (int j = degree; j >= 0; j--)
				y = coefficients.getElement(j) + (x * y);
			return y;
		} catch (Exception e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method returns all Polynomial Regression coefficients as a Vector.
	 * <p>
	 * Return Vector has polynomial coefficients sorted in ascending order of
	 * degree.Thus for polynomial regression model of degree 'n'; y=An*x^(n) +
	 * An-1*x^(n-1) + ... + A2*x^(2) + A1*x + A0; with polynomial regression
	 * coefficients A0 to An, the return Vector stores coefficients in ascending
	 * order of degree i.e. A0,A1,....,An-1,An.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector getCoefficients() {
		return coefficients;
	}

	/**
	 * This method returns residue R2 error of the given Polynomial Regression
	 * model.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double getR2Error() {
		return R2;
	}

	/**
	 * This method returns degree of given Polynomial Regression model.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getDegree() {
		return this.degree;
	}
}
