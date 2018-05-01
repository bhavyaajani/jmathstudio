package org.JMathStudio.ImageToolkit.FilterTools;

import java.util.Arrays;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Structure.Neighbor;
import org.JMathStudio.DataStructure.Structure.Neighborhood;

/**
 * This class define various Statistical spatial filters.
 * <p>
 * Statistical filters are a spatial operators defined over a discrete real image which
 * shall be represented by a {@link Cell} object. The statistical filters operate
 * within the neighbourhood define around a pixel position.
 * <p>
 * The neighbourhood define around a pixel will be specified by a {@link Neighborhood} structure.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * StatisticalFilter sf = new StatisticalFilter();//Create an instance of StatisticalFilter.
 * 
 * Neighborhood nbr = Neighborhood.eightConnectedNeighborhood();//Define required 
 * neighborhood for spatial operation.
 * 
 * Cell a = sf.medianFilter(img, nbr);//Apply Median filtering on input image.
 * Cell b = sf.peakAndValleyFilter(img, nbr);//Apply Peak & Valley filtering on input image.
 * </pre> 
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class StatisticalFilter {

	/**
	 * This method define a Geometric Filter which is a type of Statistical
	 * filter, for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * geometric mean of the neighborhood pixel intensities.
	 * <p>
	 * As this filter operation involves computation of the geometric mean, it
	 * is recommended that the given image does not contain any negative or zero
	 * values, as this may lead to a scenario where geometric mean is not
	 * possible and will cause this method to throw a Runtime Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell geometricFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++) 
			{
				double product = 1;
				int count = 0;

				for(int k=0;k<kd;k++) 
				{
					int Y = i + kele[k].getY();
					int X = j + kele[k].getX();

					if (Y >= 0 && Y < cellHeight && X >= 0 && X < cellWidth) {
						if (cell.getElement(Y, X) <= 0)
							throw new RuntimeException();
						else {
							product *= cell.getElement(Y, X);
							count++;
						}
					}
				}

				if (count == 0) {
					result.setElement(0, i, j);
				} else {

					float gMean = (float) Math.pow(product, 1.0f / count);
					result.setElement(gMean, i, j);
				}
			}
		}

		return result;

	}

	/**
	 * This method define a Harmonic Filter which is a type of Statistical
	 * filter, for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * harmonic mean of the neighborhood pixel intensities.
	 * <p>
	 * As this filter operation involves computation of the harmonic mean, it is
	 * recommended that the given image does not contain any negative or zero
	 * values, as this may lead to a scenario where harmonic mean is not
	 * possible and will cause this method to throw a Runtime Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell harmonicFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++) 
			{
				double sum = 0;
				int count = 0;

				for(int k=0;k<kd;k++)
				{
					int Y = i + kele[k].getY();
					int X = j + kele[k].getX();
					if (Y >= 0 && Y < cellHeight && X >= 0 && X < cellWidth) {
						if (cell.getElement(Y, X) <= 0)
							throw new RuntimeException();
						else {
							sum += (1.0 / cell.getElement(Y, X));
							count++;
						}
					}
				}

				// if count == 0 => sum == 0 so we have 0/0 condition.
				if (count == 0) {
					result.setElement(0, i, j);
				} else {
					float hMean = (float) (count / sum);
					result.setElement(hMean, i, j);
				}
			}
		}

		return result;

	}

	/**
	 * This method define a Mean Filter which is a type of Statistical filter,
	 * for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * arithmetic mean of the neighborhood pixel intensities.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell meanFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++)
			{
				double sum = 0;
				int count = 0;

				for(int k=0;k<kd;k++)
				{
					int Y = i + kele[k].getY();
					int X = j + kele[k].getX();
					if (Y >= 0 && Y < cellHeight && X >= 0 && X < cellWidth) {
						sum += cell.getElement(Y, X);
						count++;
					}
				}

				if (count == 0) {
					result.setElement(0, i, j);
				} else {
					result.setElement((float) (sum / count), i, j);

				}
			}
		}

		return result;

	}

	/**
	 * This method define a Median Filter which is a type of Statistical filter,
	 * for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * median of the neighborhood pixel intensities.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell medianFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		int Y,X;

		for(int i = 0; i < cellHeight; i++)
		{
			for(int j = 0; j < cellWidth; j++) 
			{
				float[] elements = new float[kd];
				int index=0;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if(Y >= 0 && Y < cellHeight && X >= 0 && X < cellWidth) {
						elements[index++] = cell.getElement(Y, X);
					}
				}

				if(index <1){
					result.setElement(0,i,j);
				}
				else if(index==1){
					result.setElement(elements[0],i,j);
				}
				else if(index==2){
					result.setElement((elements[0]+elements[1])/2.0f,i,j);
				}
				else {
					float median;
					Arrays.sort(elements,0,index);

					if (index % 2 == 0) {
						int midIndex = (index - 1) / 2;
						median = (elements[midIndex] + elements[midIndex + 1]) / 2;
					} else {
						median = elements[index / 2];
					}

					result.setElement(median, i, j);
				}
			}
		}

		return result;

	}

	/**
	 * This method define a Standard deviation Filter which is a type of
	 * Statistical filter, for spatial filtering on a discrete real image as
	 * represented by {@link Cell} 'cell' and return the resultant filtered
	 * image as a {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * standard deviation of the neighborhood pixel intensities.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell standardDeviationFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		int Y,X;
		float mean;
		for(int i = 0; i < cellHeight; i++)
		{
			for(int j = 0; j < cellWidth; j++) 
			{
				float[] elements = new float[kd];
				int index=0;
				mean=0;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if(Y >= 0 && Y < cellHeight && X >= 0 && X < cellWidth) {
						elements[index++] = cell.getElement(Y, X);
						mean+=cell.getElement(Y,X);
					}
				}

				if(index <1){
					result.setElement(0,i,j);
				}
				else {
					mean = mean/index;
					float std = 0;
					float tmp;

					for (int l = 0; l < index; l++) {
						tmp = (elements[l] - mean);
						std += tmp * tmp;
					}
					result.setElement((float) Math.sqrt(std / index),i,j);
				}
			}
		}

		return result;

	}
	/**
	 * This method define a Maximum Filter which is a type of Statistical
	 * filter, for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * maximum of the neighborhood pixel intensities.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell maximumFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		float ele;
		int Y,X;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++) 
			{
				float max = -Float.MAX_VALUE;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if (Y >= 0 && Y < cellHeight && X >= 0 && X < cellWidth) {

						ele = cell.getElement(Y, X);
						if (ele > max) {
							max = ele;
						}

					}
				}

				result.setElement(max, i, j);

			}
		}

		return result;

	}

	/**
	 * This method define a Minimum Filter which is a type of Statistical
	 * filter, for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * minimum of the neighborhood pixel intensities.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell minimumFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		float ele;
		int Y,X;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++) 
			{
				float min = Float.MAX_VALUE;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if (Y >= 0 && Y < cellHeight && X >= 0 && X < cellWidth) {
						ele = cell.getElement(Y, X);
						if (ele < min) {
							min = ele;
						}
					}
				}

				result.setElement(min, i, j);

			}
		}

		return result;

	}

	/**
	 * This method define a Mid Point Filter which is a type of Statistical
	 * filter, for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the
	 * average of the maximum and minimum of the neighborhood pixel
	 * intensities.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell midPointFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		float ele;
		int Y,X;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++) 
			{
				float min = Float.MAX_VALUE;
				float max = -Float.MAX_VALUE;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if (Y >= 0 && Y < cellHeight && X >= 0	&& X < cellWidth) {
						ele = cell.getElement(Y, X);

						if (ele > max)
							max = ele;
						if (ele < min)
							min = ele;
					}
				}

				result.setElement((min + max) / 2, i, j);

			}
		}

		return result;

	}

	/**
	 * This method define a Range Filter which is a type of Statistical filter,
	 * for spatial filtering on a discrete real image as represented by
	 * {@link Cell} 'cell' and return the resultant filtered image as a
	 * {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * This statistical filter replaces the pixel intensity of the image with the range
	 * or difference in the maximum and minimum of the neighborhood pixel
	 * intensities.
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell rangeFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		float ele;
		int Y,X;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++) 
			{
				float min = Float.MAX_VALUE;
				float max = -Float.MAX_VALUE;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if (Y >= 0 && Y < cellHeight && X >= 0	&& X < cellWidth) {
						ele = cell.getElement(Y, X);

						if (ele > max)
							max = ele;
						if (ele < min)
							min = ele;
					}
				}

				result.setElement((max - min), i, j);
			}
		}

		return result;

	}

	/**
	 * This method define a Peak and Valley Filter which is a type of
	 * Statistical filter, for spatial filtering on a discrete real image as
	 * represented by {@link Cell} 'cell' and return the resultant filtered
	 * image as a {@link Cell}.
	 * <p>
	 * The argument {@link Neighborhood} 'neighborhood' define the neighbourhood 
	 * around the pixel for filtering. The result of filtering operation will depend
	 * upon the selection of neighbourhood as specified by the argument 'neighborhood'.
	 * <p>
	 * 
	 * @param Cell
	 *            cell
	 * @param Neighborhood
	 *            neighborhood
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell peakAndValleyFilter(Cell cell, Neighborhood neighborhood) {

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		int cellHeight = cell.getRowCount();
		int cellWidth = cell.getColCount();

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		float ele;
		int Y,X;

		for (int i = 0; i < cellHeight; i++) 
		{
			for (int j = 0; j < cellWidth; j++) 
			{
				float min = Float.MAX_VALUE;
				float max = -Float.MAX_VALUE;
				int count = 0;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if (Y >= 0 && Y < cellHeight && X >= 0	&& X < cellWidth) {
						// Current pixel value is not to be considered
						if (Y == i && X == j) {

						} else {
							// minimum and maximum of neighbourhood
							// not including current pixel.
							ele = cell.getElement(Y, X);
							if (ele > max)
								max = ele;
							if (ele < min)
								min = ele;
						}
						count++;
					}
				}

				// if all are false in structuring element.
				if (count == 0)
					result.setElement(0, i, j);
				// Only true at centre in structuring element corresponding
				// to current pixel. No neighbourhood value in calculation.
				// More like impulse Neighborhood operation.
				else if (max == -Float.MAX_VALUE && min == Float.MAX_VALUE) {
					result.setElement(cell.getElement(i, j), i, j);
				}
				// if current pixel more than neighbourhood max replace
				// by neighbourhood max.
				else if (cell.getElement(i, j) > max) {
					result.setElement(max, i, j);
				}
				// if current pixel less than neighbourhood min replace
				// by neighbourhood min.
				else if (cell.getElement(i, j) < min) {
					result.setElement(min, i, j);
				}
				// If neither max nor min, result is same pixel.
				else {
					result.setElement(cell.getElement(i, j), i, j);
				}

			}
		}

		return result;

	}

}