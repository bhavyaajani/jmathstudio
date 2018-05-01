package org.JMathStudio.ImageToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;

/**
 * This class support methods for calculating various higher order real moments and 
 * the related operations defined over a discrete real image.
 * <p>A discrete real image will be represented by a {@link Cell} object.
 * 
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ImageMoment im = new ImageMoment();//Create an instance of ImageMoment.
 * 
 * double[] hu_moments = im.HUMomentSet(img);//Generate HU set of invariant moments for
 * input image.
 * 
 * double moment =  im.centralMoment(img, p, q);//Generate central moment of given order
 * 'p & 'q' for input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class ImageMoment {
	
	/**
	 * This method will compute the (p+q)th order central moment of the discrete real image
	 * as represented by {@link Cell} 'cell' and return the same.
	 * <p>Here the argument 'p' is the order of the moment along 'X' (column) axis
	 * and 'q' is the order of the moment along 'Y' (row) axis. The argument 'p' and
	 * 'q' should be more than or equal to 0 else this method will throw an IllegalArgument
	 * Exception.
	 * <p> If the sum of all the pixel intensities of the image is 0 this method will throw an 
	 * IllegalArgument Exception.
	 * <p>The central moment U(p,q) is calculated as:
	 * <p>
	 * U(p,q) = sum( (i-yc)^q * (j-xc)^p * cell(i,j)),
	 * <p> where 'xc' and 'yc' is the centroid position for the given Cell.
	 * @param Cell cell
	 * @param int p
	 * @param int q
	 * @return double
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double centralMoment(Cell cell,int p,int q) throws IllegalArgumentException
	{
		if(p <0 || q<0)
		{
			throw new IllegalArgumentException();
		}
		
		float[] centroid = centroid(cell);
		float xc = centroid[0];
		float yc = centroid[1];
		
		double moment =0;
		
		for(int i=0;i<cell.getRowCount();i++)
		{
			for(int j=0;j<cell.getColCount();j++)
			{
				moment = (moment + Math.pow(i-yc,q)*Math.pow(j-xc,p)*cell.getElement(i,j));
			}
		}
		
		return moment;
	}
	
	/**
	 * This method will compute the centroid (YC,XC) point for the discrete real image
	 * as represented by {@link Cell} 'cell'.
	 * <p>The centroid of the image will be returned as a 1D float array, where
	 * first element will be the centroid X-axis coordinate location along X axis (along the
	 * column) and the second element will be the centroid Y-axis coordinate location along 
	 * Y axis (along the row).
	 * <p>If sum of all the pixel intensities of the image is zero this method will throw 
	 * an IllegalArgument Exception. 
	 * @param Cell cell
	 * @return float[] 
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[] centroid(Cell cell) throws IllegalArgumentException 
	{
		float xc=0;
		float yc=0;
		float sum=0;
		
		for(int i=0;i<cell.getRowCount();i++)
		{
			for(int j=0;j<cell.getColCount();j++)
			{
				float value = cell.getElement(i,j);
				xc = xc + value*(j);
				yc = yc + value*(i);
				sum = sum + value;
			}
			
		}
		
		if(sum ==0)
		{
			throw new IllegalArgumentException();
		}
		
		return new float[]{xc/sum,yc/sum};
			
	}
	/**
	 * This method return a unique set of 7 different central moments which are
	 * invariant under translation, scaling and rotation of the discrete real
	 * image as represented by the Cell cell.
	 * <p>
	 * <i>Such moment set is called Hu set of invariant moments.</i>
	 * <p>
	 * The discrete real image as represented by Cell 'cell' should not have a zero
	 * mean i.e. summation of all the elements of the Cell 'cell' should not be
	 * zero else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The Hu set of moments associated with the given Image will be return as
	 * an array of doubles containing 7 different Hu moments (I1 to I7) located
	 * at the corresponding increasing index positions (0 to 6).
	 * 
	 * @param Cell
	 *            cell
	 * @return double[]
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double[] HUMomentSet(Cell cell) throws IllegalArgumentException {
		CellStatistics stat = new CellStatistics();
		float sum = stat.sum(cell);

		if (sum == 0) {
			throw new IllegalArgumentException();
		}
		try {

			double n20 = stat.centralMoment(cell, 2, 0)
					/ Math.pow(sum, 1 + (2 + 0) / 2);
			double n02 = stat.centralMoment(cell, 0, 2)
					/ Math.pow(sum, 1 + (0 + 2) / 2);
			double n11 = stat.centralMoment(cell, 1, 1)
					/ Math.pow(sum, 1 + (1 + 1) / 2);
			double n30 = stat.centralMoment(cell, 3, 0)
					/ Math.pow(sum, 1 + (3 + 0) / 2);
			double n12 = stat.centralMoment(cell, 1, 2)
					/ Math.pow(sum, 1 + (1 + 2) / 2);
			double n21 = stat.centralMoment(cell, 2, 1)
					/ Math.pow(sum, 1 + (2 + 1) / 2);
			double n03 = stat.centralMoment(cell, 0, 3)
					/ Math.pow(sum, 1 + (0 + 3) / 2);

			double[] result = new double[7];

			result[0] = n20 + n02;
			result[1] = (n20 - n02) * (n20 - n02) + 4 * n11 * n11;
			result[2] = (n30 - 3 * n12) * (n30 - 3 * n12) + (3 * n21 - n03)
					* (3 * n21 - n03);
			result[3] = (n30 + n12) * (n30 + n12) + (n21 + n03) * (n21 + n03);
			result[4] = (n30 - 3 * n12)
					* (n30 + n12)
					* ((n30 + n12) * (n30 + n12) - 3 * (n21 + n03)
							* (n21 + n03))
					+ (3 * n21 - n03)
					* (n21 + n03)
					* (3 * (n30 + n12) * (n30 + n12) - (n21 + n03)
							* (n21 + n03));
			result[5] = (n20 - n02)
					* ((n30 + n12) * (n30 + n12) - (n21 + n03) * (n21 + n03))
					+ 4 * n11 * (n30 + n12) * (n21 + n03);
			result[6] = (3 * n21 - n03)
					* (n30 + n12)
					* ((n30 + n12) * (n30 + n12) - 3 * (n21 + n03)
							* (n21 + n03))
					- (n30 - 3 * n12)
					* (n21 + n03)
					* (3 * (n30 + n12) * (n30 + n12) - (n21 + n03)
							* (n21 + n03));

			return result;

		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method compute the Legendre Moment for the given discrete real image
	 * as represented by the Cell 'cell'.
	 * <p>
	 * The arguments 'm' and 'n' specify respectively the Lengendre polynomial
	 * order for the (Horizontal) X axis and (Vertical) Y axis respectively.
	 * Thus order of the given Legendre Moment will be (m + n).
	 * <p>
	 * The arguments 'm' and 'n' should not be negative else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * The dimensions of the Cell 'cell' should be more than 1 else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param int m
	 * @param int n
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float legendreMoment(Cell cell, int m, int n)
			throws IllegalArgumentException {
		if (m < 0 || n < 0) {
			throw new IllegalArgumentException();
		}

		if (cell.getRowCount() == 1 || cell.getColCount() == 1) {
			throw new IllegalArgumentException();
		}

		float[] legPolyY = new float[cell.getRowCount()];
		float[] legPolyX = new float[cell.getColCount()];

		float slopeY = (float) (2.0 / (cell.getRowCount() - 1));
		for (int i = 0; i < cell.getRowCount(); i++) {
			//y from [-1 1]
			float y = i * slopeY - 1;
			legPolyY[i] = MathUtils.legPoly(n, y);
		}

		float slopeX = (float) (2.0 / (cell.getColCount() - 1));
		for (int j = 0; j < cell.getColCount(); j++) {
			//x from [-1 1] 
			float x = j * slopeX - 1;
			legPolyX[j] = MathUtils.legPoly(m, x);
		}

		float moment = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				moment = moment + legPolyX[j] * legPolyY[i]
						* cell.getElement(i, j);

			}
		}

		return moment * (2 * m + 1) * (2 * n + 1) / 4.0f;
	}

}
