package org.JMathStudio.MathToolkit.StatisticalTools.Regression;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class defines a Linear Regression to estimate coefficients of linear regression for a given set of
 * points in R2 space.
 * <p>Coefficients of linear regression 'a' & 'b' for a set of points in R2 space specify a straight line 
 * y = a*x + b; with best fit in least square error sense within the given points.   
 * 
 * <p>Linear Regression is a special case of {@link PolynomialRegression} with degree 1.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class LinearRegression { 

	private float a = -1;
	private float b = -1;
	private float R2 = -1;
	
	/**
	 * Creates an instance of {@link LinearRegression} and computes the coefficients of linear regression from a set of 
	 * points in R2 space as specified by their 2D (y,x) coordinates as given by {@link Vector}s y & x respectively.
	 * <p>Input Vector 'y' & 'x' should be of equal length and specify 'y' & 'x' coordinates for individual points under 
	 * consideration. Hence number of points under linear regression is equal to length of Vector 'y' or 'x' and should
	 * be more than 1.
	 * <p>If length of input Vectors 'y' & 'x' is not same this method shall throw an {@link IllegalArgumentException}.
	 * <p>If length of input Vectors 'y' & 'x' is 1 this method shall throw an {@link IllegalArgumentException}.
	 * @param Vector y
	 * @param Vector x
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public LinearRegression(Vector y, Vector x) throws IllegalArgumentException
	{
		if(x.length() != y.length())
			throw new IllegalArgumentException();
		
		if(x.length() == 1)
			throw new IllegalArgumentException();
		
		try{
			
			float[] _x = x.accessVectorBuffer();
			float[] _y = y.accessVectorBuffer();

			double sumx = 0.0, sumy = 0.0;

			final int n = _x.length;

			for(int i=0;i<n;i++)
			{
				sumx  += _x[i];
				sumy  += _y[i];
			}

			double xbar = sumx / n;
			double ybar = sumy / n;

			double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
			for (int i = 0; i < n; i++) {
				xxbar += (_x[i] - xbar) * (_x[i] - xbar);
				yybar += (_y[i] - ybar) * (_y[i] - ybar);
				xybar += (_x[i] - xbar) * (_y[i] - ybar);
			}
			a = (float) (xybar / xxbar);
			b = (float) (ybar - a * xbar);

			double ssr = 0.0;      
			for (int i = 0; i < n; i++) {
				double fit = a*_x[i] + b;
				ssr += (fit - ybar) * (fit - ybar);
			}

			R2    = (float) (ssr / yybar);
			
		}catch(Exception e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method returns 'y' coordinate for a hypothetical point on the best fit line with 'x' coordinate
	 * as given by argument 'x'. 
	 * <p> Based on the linear regression coefficients 'a' & 'b' this method returns 'y' such that;
	 * <p><i> y = a*x + b;</i> 
	 * @param x
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float evaluate(float x)
	{		
		return (float) (a*x + b);
	}
	
	/**
	 * This method returns coefficient 'a' of the given Linear Regression model.
	 * <p>Coefficient 'a' is slope of the best fit line, y= a*x + b.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getCoefficientA()
	{
		return this.a;
	}

	/**
	 * This method returns coefficient 'b' of the given Linear Regression model.
	 * <p>Coefficient 'b' is y intercept of the best fit line, y= a*x + b.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getCoefficientB()
	{
		return this.b;
	}

	/**
	 * This method returns residue R2 error of the given Linear Regression model. 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getR2Error()
	{
		return this.R2;
	}
}