package org.JMathStudio.MathToolkit.Numeric.Function1D;

import org.JMathStudio.DataStructure.Vector.Vector;

/**
 * This class define a Mesh Line. A mesh line is a set of discrete points in increasing order 
 * along X axis.
 * <p>
 * The line point vector 'X' store the 'x' axis points for the given mesh line
 * in the increasing order.
 *  
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class MeshLine {

	private float[] x;

	private float xinc;

	/**
	 * This will create a Mesh Line with following parameters:
	 * <p>
	 * 'minX' - This will be the minimum or starting 'X' axis coordinate of the mesh line.
	 * <p>
	 * 'incX' - This argument specify the linear spacing between consecutive  'X' axis points.
	 * <p>'maxX' - This set the maximum bound for the 'X' axis coordinate of the
	 * mesh line. The number of points in the given mesh line will be,
	 * <p>
	 * <i>Nx = floor((maxX-minX)/incX) + 1</i>
	 * <p>
	 * Thus the maximum X axis coordinate of the mesh line will be,
	 * <p>
	 * <i>(Nx * incX + minX) <= maxX.</i>
	 * <p>
	 * The argument 'minX' should be less than or equal to the argument 'maxX' else this 
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'incX' should be more than '0' else this method will throw an 
	 * IllegalArgument Exception.
	 * 
	 * @param float minX
	 * @param float incX
	 * @param float maxX
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public MeshLine(float minX, float incX, float maxX) throws IllegalArgumentException {
		if (minX > maxX)
			throw new IllegalArgumentException();
		else if (incX <= 0)
			throw new IllegalArgumentException();
		else {
			int xSize = (int) (Math.floor((maxX - minX) / incX) + 1);
			
			x = new float[xSize];
			
			this.xinc = incX;
			
			for (int i = 0; i < x.length; i++)
				x[i] = minX + i * incX;
		}

	}

	/**
	 * This will return the coordinate for the minimum X axis point for the given mesh line.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getLineMinX() {
		return x[0];
	}

	/**
	 * This will return the coordinate for the maximum X axis point for the given mesh line.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getLineMaxX() {
		return x[x.length - 1];
	}

	/**
	 * This will return the dimension or number of points on the given mesh line.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getNumberOfLinePoints() {
		return x.length;
	}

	/**
	 * This will return the linear spacing between consecutive points for the given mesh
	 * line.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getIncrement() {
		return this.xinc;
	}

	/**
	 * This will return a Vector containing the X axis coordinates of the points in the
	 * given mesh line in the increasing order.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessLineVector() {
		return new Vector(x);
	}

}
