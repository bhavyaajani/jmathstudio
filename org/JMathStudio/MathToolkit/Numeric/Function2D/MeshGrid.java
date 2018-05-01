package org.JMathStudio.MathToolkit.Numeric.Function2D;

import org.JMathStudio.DataStructure.Vector.Vector;

/**
 * This class define a Mesh Grid. A mesh grid is a 2D linear discrete cartesian grid
 * represented by a set of X and Y axis grid point vectors.
 * <p>
 * The grid point vectors 'Y' and 'X' respectively store the 'y' and 'x' axis
 * coordinates for the given mesh grid in the increasing order.
 * <p>
 * Thus if the dimension of the Vectors Y and X is 'n' and 'm', the dimension of
 * the mesh grid represented by these vectors shall be [n,m].
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class MeshGrid {

	private float[] x;
	private float[] y;

	private float xinc;
	private float yinc;

	/**
	 * This will create a Mesh Grid with following parameters:
	 * <p>
	 * 'minX' - This will be the minimum 'X' axis coordinate of the mesh grid.
	 * <p>
	 * 'incX' - This argument specify the linear spacing for 'X' axis
	 * coordinates.
	 * <p>'maxX' - This set the maximum bound for the 'X' axis coordinate of the
	 * mesh grid. The number of X axis coordinate points in the given mesh grid
	 * or dimension along X will be,
	 * <p>
	 * <i>Nx = floor((maxX-minX)/incX) + 1</i>
	 * <p>
	 * Thus the maximum X axis coordinate of the mesh grid will be,
	 * <p>
	 * <i>(Nx * incX + minX) <= maxX.</i>
	 * <p>
	 * 'minY' - This will be the minimum 'Y' axis coordinate of the mesh grid.
	 * <p>
	 * 'incY' - This argument specify the linear spacing for 'Y' axis
	 * coordinates.
	 * <p>'maxY' - This set the maximum bound for the 'Y' axis coordinate of the
	 * mesh grid. The number of Y axis coordinate points in the given mesh grid
	 * or dimension along Y will be,
	 * <p>
	 * <i>Ny = floor((maxY-minY)/incY) + 1</i>
	 * <p>
	 * Thus the maximum Y axis coordinate of the mesh grid will be,
	 * <p>
	 * <i>(Ny * incY + minY) <= maxY.</i>
	 * <p>
	 * The arguments 'minX' and 'minY' respectively should be less than or equal
	 * to the arguments 'maxX' and 'maxY' else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The arguments 'incX' and 'incY' should be more than '0' else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param float minX
	 * @param float incX
	 * @param float maxX
	 * @param float minY
	 * @param float incY
	 * @param float maxY
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public MeshGrid(float minX, float incX, float maxX, float minY, float incY,
			float maxY) throws IllegalArgumentException {
		if (minX > maxX | minY > maxY)
			throw new IllegalArgumentException();
		else if (incX <= 0 | incY <= 0)
			throw new IllegalArgumentException();
		else {
			int xSize = (int) (Math.floor((maxX - minX) / incX) + 1);
			int ySize = (int) (Math.floor((maxY - minY) / incY) + 1);

			x = new float[xSize];
			y = new float[ySize];

			this.xinc = incX;
			this.yinc = incY;

			for (int i = 0; i < x.length; i++)
				x[i] = minX + i * incX;

			for (int i = 0; i < y.length; i++)
				y[i] = minY + i * incY;

		}

	}

	/**
	 * This will return the minimum X axis coordinate for the given mesh grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getGridMinX() {
		return x[0];
	}

	/**
	 * This will return the maximum X axis coordinate for the given mesh grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getGridMaxX() {
		return x[x.length - 1];
	}

	/**
	 * This will return the minimum Y axis coordinate for the given mesh grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getGridMinY() {
		return y[0];
	}

	/**
	 * This will return the maximum Y axis coordinate for the given mesh grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getGridMaxY() {
		return y[y.length - 1];
	}

	/**
	 * This will return the dimension or number of grid points along X axis in
	 * the given mesh grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getNumberOfXGridPoints() {
		return x.length;
	}

	/**
	 * This will return the dimension or number of grid points along Y axis in
	 * the given mesh grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getNumberOfYGridPoints() {
		return y.length;
	}

	/**
	 * This will return the linear spacing along the X axis for the given mesh
	 * grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getXGridIncrement() {
		return this.xinc;
	}

	/**
	 * This will return the linear spacing along the Y axis for the given mesh
	 * grid.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getYGridIncrement() {
		return this.yinc;
	}

	/**
	 * This will return the X axis grid point vector containing the X axis
	 * coordinates in the increasing order for the given mesh grid as a
	 * {@link Vector}.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessGridXVector() {
		return new Vector(x);
	}

	/**
	 * This will return the Y axis grid point vector containing the X axis
	 * coordinates in the increasing order for the given mesh grid as a
	 * {@link Vector}.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessGridYVector() {
		return new Vector(y);
	}

	// public void flush()
	// {
	// for(int i=0;i<y.length;i++)
	// {
	// for(int j=0;j<x.length;j++)
	// {
	// System.out.print(" ("+y[i]+" , "+x[j]+") ");
	// }
	// System.out.println();
	// }
	// }
}
