package org.JMathStudio.ImageToolkit.ClusterTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;

/**
 * This class define a K Mean algorithm for clustering a discrete real image intensities.
 * <p>K Mean algorithm is an iterative algorithm for image pixel clustering with following
 * 2 steps per iteration:
 * <i>
 * <p>1. Cluster i.e. assign a pixel intensity of an image to a class (here centroid of the class)
 * with which it has minimum euclidian distance.
 * <p>2. Recompute the class centroid from the pixels assign to the given class at the end of
 * each iteration.
 * </i>
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * KMean kmean = new KMean();//Create an instance of KMean classifier.
 * float[] seed = new float[]{50,100,175,225};//Select number of classes with initial seed points.
 * 
 * int iteration = 3;//Select number of iterations.
 * 
 * Cell result = kmean.clusterAsClass(img, seed, iteration);//Classify pixels of input image into
 * one of the class, here assign class index as label to each resultant pixel.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class KMean {

	/**
	 * This method define the K Mean clustering operation for clustering the pixel
	 * intensities of a discrete real image as represented by a {@link Cell} 'cell'
	 * and return the clustered image as a Cell.
	 * <p>
	 * The 1d float array 'seed' specify initial set of seed/centroid points. The number
	 * of seed points in the array 'seed' also specify the number of class partition for
	 * clustering. The outcome of this operation is sensitive to these initial choice of 
	 * the seed points.
	 * <p>
	 * The argument 'N' specify number of iterations for clustering. The value of the argument
	 * 'N' should not be less than 1 else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The result of clustering is return as a Cell such that, <b><i>each pixel position has the centroid
	 * value of the class to which that pixel belongs </i></b>at the end of specified number of iteration
	 * of K Mean operation.
	 *   
	 * @param Cell
	 *            cell
	 * @param float[] seed
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell clusterAsIntensity(Cell cell, float[] seed, int N)throws IllegalArgumentException 
	{
		if (N < 1)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			try
			{
				Cell cluster = cell.clone();
				int height = cluster.getRowCount();
				int width = cluster.getColCount();
				
				for (int l = 0; l < N; l++) 
				{
					float[] distance = new float[seed.length];
					float[][] summation = new float[seed.length][2];

					//In the algorithm do not replace original image with the current
					//updated result. Always work with original image, as this is not
					//an adaptive algorithm.
					for (int i = 0; i < height; i++) 
					{
						for (int j = 0; j < width; j++)
						{
							for (int k = 0; k < distance.length; k++) 
							{
								distance[k] = Math.abs(cell.getElement(i, j) - seed[k]);
							}

							int index = f3(distance);
							cluster.setElement(seed[index], i, j);
							
							//Summation of all the pixels falling in this class.
							//Class is represented by index.
							summation[index][0]+= cell.getElement(i, j);
							//Count of pixels falling to this class.
							summation[index][1]++;
						}
					}

					for (int s = 0; s < seed.length; s++) 
					{
						if (summation[s][1] != 0) 
						{
							//New seed point is the average value of all the pixels
							//falling within the class.
							seed[s] = summation[s][0] / summation[s][1];
						}
					}

				}
				
				return cluster;

			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}

	}
	
	/**
	 * This method define the K Mean clustering operation for clustering the pixel
	 * intensities of a discrete real image as represented by a {@link Cell} 'cell'
	 * and return the clustered image as a Cell.
	 * <p>
	 * The 1d float array 'seed' specify initial set of seed/centroid points. The number
	 * of seed points in the array 'seed' also specify the number of class partition for
	 * clustering. The outcome of this operation is sensitive to these initial choice of 
	 * the seed points.
	 * <p>
	 * The argument 'N' specify number of iterations for clustering. The value of the argument
	 * 'N' should not be less than 1 else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The result of clustering is return as a Cell such that,<b><i> each pixel position has an integer
	 * value representing the class to which that pixel belongs </i></b>at the end of specified number of
	 * iteration of K Mean operation. Thus all pixel position with same integer value, belongs to
	 * one class. 
	 *   
	 * @param Cell
	 *            cell
	 * @param float[] seed
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell clusterAsClass(Cell cell, float[] seed, int N)throws IllegalArgumentException 
	{
		if (N < 1)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			try
			{
				Cell cluster = cell.clone();
				int height = cluster.getRowCount();
				int width = cluster.getColCount();
				
				for (int l = 0; l < N; l++) 
				{
					float[] distance = new float[seed.length];
					float[][] summation = new float[seed.length][2];

					//In the algorithm do not replace original image with the current
					//updated result. Always work with original image, as this is not
					//an adaptive algorithm.
					for (int i = 0; i < height; i++) 
					{
						for (int j = 0; j < width; j++)
						{
							for (int k = 0; k < distance.length; k++) 
							{
								distance[k] = Math.abs(cell.getElement(i, j) - seed[k]);
							}

							int index = f3(distance);
							cluster.setElement(index, i, j);
							
							//Summation of all the pixels falling in this class.
							//Class is represented by index.
							summation[index][0]+= cell.getElement(i, j);
							//Count of pixels falling to this class.
							summation[index][1]++;
						}
					}

					for (int s = 0; s < seed.length; s++) 
					{
						if (summation[s][1] != 0) 
						{
							//New seed point is the average value of all the pixels
							//falling within the class.
							seed[s] = summation[s][0] / summation[s][1];
						}
					}

				}
				
				return cluster;

			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}

	}


	private int f3(float[] array)
	{
		float min = Float.MAX_VALUE;
		int index = -1;

		for (int i = 0; i < array.length; i++) 
		{
			index = array[i] <= min ? i : index;
			min = array[i] <= min ? array[i] : min;
		}

		return index;
	}

}
