package org.JMathStudio.ImageToolkit.ClusterTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;

/**
 * This class define a Fuzzy C Mean algorithm for clustering a discrete real
 * image intensities.
 * <p>
 * Fuzzy C Mean algorithm is an iterative algorithm for image pixel clustering
 * with following 2 steps per iteration: <i>
 * <p>
 * 1. Fuzzy Clustering : Assign a pixel intensity of an image to a class (here
 * centroid of the class) based on some appropriate membership function defined
 * over the euclidian distance between the pixel intensity and corresponding
 * class centroid.
 * <p>
 * 2. Update class centroids : Recompute the class centroid from the pixels
 * assign to the given class with a certain membership at the end of each
 * iteration. </i>
 * <p>
 * At the end of specified number of iterations, the result is de-fuzzified by
 * assigning a pixel intensity to one of that class with which it has highest
 * membership value.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Input image.
 * 
 * FuzzyCMean fuzzy = new FuzzyCMean();//Create an instance of FuzzyCMean classifier.
 * float[] centroids = new float[]{50,150,250};//Select number of classes with initial 
 * centroids.
 *  
 * int iteration = 4;//Select number of iterations.
 * Cell result = fuzzy.clusterAsIntensity(img, centroids, iteration);//Classify input image
 * pixels into one of the class, here assign class centroids as label to the pixel.
 * </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FuzzyCMean {

	final private short sensitivity = 3;

	private float[][][] U;
	private float[] C;

	/**
	 * This method define the Fuzzy C Mean clustering operation for clustering
	 * the pixel intensities of a discrete real image as represented by a
	 * {@link Cell} 'cell' and return the clustered image as a Cell.
	 * <p>
	 * The 1d float array 'seed' specify initial set of seed/centroid points.
	 * The number of seed points in the array 'seed' also specify the number of
	 * class partition for clustering. The outcome of this operation is
	 * sensitive to these initial choice of the seed points.
	 * <p>
	 * The argument 'N' specify number of iterations for clustering. The value
	 * of the argument 'N' should not be less than 1 else this method will throw
	 * an IllegalArgument Exception.
	 * <p>
	 * The result of clustering is return as a Cell such that,<b><i> each pixel
	 * position has an integer value representing the class to which that pixel
	 * belongs </i></b>at the end of specified number of iteration of Fuzzy C
	 * Mean operation. Thus all pixel position with same integer value, belongs
	 * to one class.
	 * 
	 * @param Cell
	 *            cell
	 * @param float[] seed
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell clusterAsClass(Cell image, float[] centroids, int N)
			throws IllegalArgumentException {
		f1(image, centroids, N);
		return f2(image, true);
	}

	/**
	 * This method define the Fuzzy C Mean clustering operation for clustering
	 * the pixel intensities of a discrete real image as represented by a
	 * {@link Cell} 'cell' and return the clustered image as a Cell.
	 * <p>
	 * The 1d float array 'seed' specify initial set of seed/centroid points.
	 * The number of seed points in the array 'seed' also specify the number of
	 * class partition for clustering. The outcome of this operation is
	 * sensitive to these initial choice of the seed points.
	 * <p>
	 * The argument 'N' specify number of iterations for clustering. The value
	 * of the argument 'N' should not be less than 1 else this method will throw
	 * an IllegalArgument Exception.
	 * <p>
	 * The result of clustering is return as a Cell such that, <b><i>each pixel
	 * position has the centroid value of the class to which that pixel belongs
	 * </i></b>at the end of specified number of iteration of Fuzzy C Mean
	 * operation.
	 * 
	 * @param Cell
	 *            cell
	 * @param float[] seed
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell clusterAsIntensity(Cell image, float[] centroids, int N)
			throws IllegalArgumentException {
		f1(image, centroids, N);
		return f2(image, false);
	}

	/**
	 * This method will return a {@link Vector} of class centroids computed for
	 * the last Fuzzy C Mean clustering operation.
	 * <p>
	 * The Vector contain the centroid value for the corresponding class as
	 * identified by the index position.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessClassCentroids() {
		return new Vector(C);
	}

	private void f1(Cell image, float[] centroids, int N)
			throws IllegalArgumentException {
		if (N < 1)
			throw new IllegalArgumentException();

		// Number of class.
		int cL = centroids.length;
		// Create vector containing centroid for each class.
		C = new float[cL];
		// Initialise Centroids as specified by User.
		for (int i = 0; i < cL; i++)
			C[i] = centroids[i];

		int height = image.getRowCount();
		int width = image.getColCount();

		// Weight matrix, U[k][i][j] is weight for pixel[i][j] for
		// kth class. Weight here specify membership of a pixel[i][j] to class
		// k.
		// Weight should be in the range of [0 1]. Also for pixel[i][j] sum of
		// all
		// the weights corresponding to k classes should be 1.
		U = new float[cL][height][width];

		int iteration = 0;

		do {
			f5(image);
			f6(image);
		} while (iteration++ < N);

	}

	private Cell f2(Cell image, boolean asClass) {
		int height = image.getRowCount();
		int width = image.getColCount();
		int cL = U.length;

		Cell res = new Cell(height, width);

		int maxMembershipClass;
		float maxWeight;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				maxWeight = U[0][i][j];
				maxMembershipClass = 0;
				for (int k = 1; k < cL; k++) {
					if (maxWeight < U[k][i][j]) {
						maxWeight = U[k][i][j];
						maxMembershipClass = k;
					}
				}
				if (asClass)
					res.setElement(maxMembershipClass, i, j);
				else
					res.setElement(C[maxMembershipClass], i, j);
			}
		}

		return res;
	}

	private void f5(Cell image) {
		// Update weights:
		// For each weight U[k][i][j], corresponding to the pixel[i][j] for kth
		// class.
		// Step 1: Calculate distance of the pixel[i][j] from the kth class
		// centroid -> Dk.
		// Step 2: Estimate membership of the pixel[i][j] to a given class 'k'
		// i.e weight U[k][i][j]
		// using the distance 'Dk' and some appropriate membership function.
		// Here membership function
		// used is MF(Dk) = (1/Dk)^m, where 'm' is an integer greater than 0.
		// Step 3: Normalize all the weights U[k] for pixel [i][j] such that
		// their sum is 1.

		int height = image.getRowCount();
		int width = image.getColCount();
		int cL = U.length;

		float pixel;
		float sum = 0;
		float power = 0;
		float D = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				pixel = image.getElement(i, j);
				sum = 0;

				for (int k = 0; k < cL; k++) {
					D = Math.abs(pixel - C[k]);

					// Precision is set to 1.
					// Results are accurate till unit '1' intensity.
					if (D <= 1)
						U[k][i][j] = 1;
					else {
						// Donot use Math.pow() function it is expensive.
						// Use instead for loop to estimate (1/D)^m.
						power = 1 / D;
						for (int p = 1; p < sensitivity; p++) {
							power *= 1 / D;
						}
						U[k][i][j] = power;
					}

					sum += U[k][i][j];
				}

				for (int k = 0; k < cL; k++) {
					// Normalise weights.
					U[k][i][j] = U[k][i][j] / sum;
				}

			}
		}
	}

	private void f6(Cell image) {
		float UNorm;
		float sum;

		int height = image.getRowCount();
		int width = image.getColCount();

		float[][] img = image.accessCellBuffer();

		for (int k = 0; k < C.length; k++) {
			UNorm = 0;
			sum = 0;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					sum = sum + U[k][i][j] * img[i][j];
					UNorm = UNorm + U[k][i][j];
				}
			}
			C[k] = (float) (sum / UNorm);
		}

	}

}
