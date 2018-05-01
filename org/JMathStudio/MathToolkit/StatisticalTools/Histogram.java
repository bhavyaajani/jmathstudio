package org.JMathStudio.MathToolkit.StatisticalTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define methods to compute Histogram and related operations.
 * <pre>Usage:
 * AbstractUIntPixelImage img = UInt8PixelImage.importImage("path");//Import input image
 * as UInt PixelImage.
 * 
 * Histogram hg = new Histogram();//Create an instance of Histogram.
 * 
 * Vector hist = hg.histogram(img);//Compute histogram of input image.
 * Vector chist = hg.cumulativeHistogram(img);//Compute cumulative histogram
 * of input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Histogram {

	/**
	 * This method will compute the Histogram of the pixel intensities of the
	 * given UInt PixelImage as represented by an {@link AbstractUIntPixelImage}
	 * 'img' and return the same as a {@link Vector}.
	 * <p>
	 * As an UInt PixelImage with depth 'd' has 2^d different pixel intensities
	 * the return {@link Vector} will be of length 2^d with each index element
	 * of the vector representing the count for that pixel intensity value
	 * respectively.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector histogram(AbstractUIntPixelImage img) {

		float[] hist = new float[img.getMaxValidPixel() + 1];
		int h = img.getHeight();
		int w = img.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				hist[(int) img.getPixel(i, j)]++;
			}
		}

		return new Vector(hist);
	}
	
	/**
	 * This method will compute the Cumulative histogram of the pixel intensities of the
	 * given UInt PixelImage as represented by an {@link AbstractUIntPixelImage}
	 * 'img' and return the same as a {@link Vector}.
	 * <p>
	 * As an UInt PixelImage with depth 'd' has 2^d different pixel intensities
	 * the return {@link Vector} will be of length 2^d with each index element
	 * of the vector giving the count of number of pixels with intensity equal to or less
	 * than that pixel intensity within the image respectively.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector cumulativeHistogram(AbstractUIntPixelImage img){
		
		float[] hist = histogram(img).accessVectorBuffer();
		
		float[] chist = new float[hist.length];
		chist[0] = hist[0];
		
		for(int i=1;i<chist.length;i++){
			chist[i] = chist[i-1]+hist[i];
		}
		
		return new Vector(chist);
	}
	
	/**
	 * This method will compute the Probability Mass Function (PMF) of the pixel
	 * intensities of the given UInt PixelImage as represented by an
	 * {@link AbstractUIntPixelImage} 'img' and return the same as a
	 * {@link Vector}.
	 * <p>
	 * As an UInt PixelImage with depth 'd' has 2^d different pixel intensities
	 * the return {@link Vector} will be of length 2^d with each index element
	 * of the vector giving the probability for a pixel taking that pixel
	 * intensity value respectively.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector PMF(AbstractUIntPixelImage img) {
		int totalPixels = img.getHeight() * img.getWidth();

		Vector hist = histogram(img);
		Vector pmf = new Vector(hist.length());

		for (int i = 0; i < hist.length(); i++) {
			pmf.setElement(hist.getElement(i) / totalPixels, i);
		}

		return pmf;
	}
	
	/**
	 * This method will compute the Cumulative Probability Mass Function (CPMF) of the pixel
	 * intensities of the given UInt PixelImage as represented by an
	 * {@link AbstractUIntPixelImage} 'img' and return the same as a
	 * {@link Vector}.
	 * <p>
	 * As an UInt PixelImage with depth 'd' has 2^d different pixel intensities
	 * the return {@link Vector} will be of length 2^d with each index element
	 * of the vector giving the cumulative probability for the pixels with intensity equal to or
	 * less than that intensity value respectively.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector CPMF(AbstractUIntPixelImage img){
		//Cumulative histogram.
		Vector chist = cumulativeHistogram(img);
		
		//Cumulative Probability mass function.
		Vector cpmf = new Vector(chist.length());
		
		//Last elements equals total pixels in the image.
		//This is to ensure that last element in cpmf is exactly 1.0.
		float totalPixels = chist.getElement(chist.length()-1);
		
		for(int i=0;i<cpmf.length();i++){
			cpmf.setElement(chist.getElement(i)/totalPixels, i);
		}
		
		return cpmf;
	}
	
	/**
	 * This method will compute the Histogram for the elements of the Cell
	 * 'cell' and return the same as a VectorStack.
	 * <p>
	 * The argument 'bins' specify the number of bins for computing the
	 * histogram. This divide the full range of the elements of 'cell' in to
	 * this many equi-spaced partitions and compute histogram by counting number
	 * of elements of the 'cell' falling within a given bin/partition. The
	 * argument 'bins' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * Computed histogram will be return as a VectorStack containing two Vectors
	 * of length equal to 'bins' such that, the 0th and the 1st Vector of the
	 * VectorStack contains respectively the histogram counts for a given bin
	 * (in increasing order of bin range) and the corresponding centre for that
	 * bin/partition.
	 * <p>
	 * The coverage of an ith bin is,
	 * <p>
	 * <i>CB(i) - BS/2 upto but excluding CB(i) + BS/2 : if 'i' is not final
	 * bin.
	 * <p>
	 * CB(i) - BS/2 upto CB(i) + BS/2 : if 'i' is final bin.</i>
	 * <p>
	 * where, 'CB(i)' is the centre of ith bin and,
	 * <p>
	 * 'BS' is the bin size given by following equation,
	 * <p>
	 * <i>BS = (maximum element of cell - minimum element of cell)/(number of
	 * bins).</i>
	 * <p>
	 * However, if the minimum and the maximum element of the Cell 'cell' is
	 * same, bins cannot be computed and thus histogram will be computed only
	 * for a single bin covering all the elements of the 'cell' with centre of
	 * bin as maximum element of the 'cell' (centre of bin has no meaning in
	 * this scenario).
	 * 
	 * @param Cell
	 *            cell
	 * @param int bins
	 * @return VectorStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack histogram(Cell cell, int bins)
			throws IllegalArgumentException {

		// Number of bins cannot be negative or zero.
		if (bins < 1)
			throw new IllegalArgumentException();

		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;

		// Get Minimum and Maximum element of the Cell.
		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				float ele = cell.getElement(i, j);

				if (ele < min)
					min = ele;
				if (ele > max)
					max = ele;
			}
		}

		// If Minimum and Maximum are same, only one bin is possible and that
		// contains
		// all the elements of the Cell (here same value).
		// So Count has all elements of the Cell and Bins centre will be Maximum
		// (or Minimum).
		if (min == max) {
			// hist[0] has count and hist[1] has corresponding bins centre.
			float[][] hist = new float[2][1];

			hist[0][0] = cell.getRowCount() * cell.getColCount();
			hist[1][0] = max;

			VectorStack result = new VectorStack();

			for (int i = 0; i < hist.length; i++)
				result.addVector(new Vector(hist[i]));

			return result;

		} else {
			// hist[0] has count and hist[1] has corresponding bins centre.
			float[][] hist = new float[2][bins];
			// Size of each bin.
			float binsSize = (max - min) / (bins);
			// Centre of the first bin.
			float firstBinsCentre = min + binsSize / 2;
			hist[1][0] = firstBinsCentre;

			// HERE EACH ith BIN RANGE IS [MIN + i*BINSIZE, MIN+(i+1)*BINSIZE)
			// FOR FINAL BIN THE MAXIMUM VALUE OF CELL IS INCLUDED IN IT.

			// Centre of the subsequent bins are obtained by adding the bins
			// size
			// to the preceeding bins centre.
			for (int i = 1; i < bins; i++) {
				hist[1][i] = hist[1][i - 1] + binsSize;
			}

			for (int i = 0; i < cell.getRowCount(); i++) {
				for (int j = 0; j < cell.getColCount(); j++) {
					float value = cell.getElement(i, j);

					// If the element is Maximum, it goes to final bins.
					// This condition is not accounted by the preceeding
					// algorithm
					// to assign bin to an element.
					if (value == max) {
						hist[0][bins - 1]++;
					} else {
						int binsIndex = (int) Math.floor((value - min)
								/ binsSize);
						hist[0][binsIndex]++;
					}

				}
			}

			VectorStack result = new VectorStack();

			for (int i = 0; i < hist.length; i++)
				result.addVector(new Vector(hist[i]));

			return result;
		}
	}
	
	/**
	 * This method will compute the Histogram for the elements of the Vector
	 * 'vector' and return the same as a VectorStack.
	 * <p>
	 * The argument 'bins' specify the number of bins for computing the
	 * histogram. This divide the full range of the elements of 'vector' in to
	 * this many equi-spaced partitions and compute histogram by counting number
	 * of elements of the 'vector' falling within a given bin/partition. The
	 * argument 'bins' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * Computed histogram will be return as a VectorStack containing two Vectors
	 * of length equal to 'bins' such that, the 0th and the 1st Vector of the
	 * VectorStack contains respectively the histogram counts for a given bin
	 * (in increasing order of bin range) and the corresponding centre for that
	 * bin/partition.
	 * <p>
	 * The coverage of an ith bin is,
	 * <p>
	 * <i>CB(i) - BS/2 upto but excluding CB(i) + BS/2 : if 'i' is not final
	 * bin.
	 * <p>
	 * CB(i) - BS/2 upto CB(i) + BS/2 : if 'i' is final bin.</i>
	 * <p>
	 * where, 'CB(i)' is the centre of ith bin and,
	 * <p>
	 * 'BS' is the bin size given by following equation,
	 * <p>
	 * <i>BS = (maximum element of vector - minimum element of vector)/(number
	 * of bins).</i>
	 * <p>
	 * However, if the minimum and the maximum element of the Vector 'vector' is
	 * same, bins cannot be computed and thus histogram will be computed only
	 * for a single bin covering all the elements of the 'vector' with centre of
	 * bin as maximum element of the 'vector' (centre of bin has no meaning in
	 * this scenario).
	 * 
	 * @param Vector
	 *            vector
	 * @param int bins
	 * @return VectorStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack histogram(Vector vector, int bins)
			throws IllegalArgumentException {

		// Number of bins cannot be negative or zero.
		if (bins < 1)
			throw new IllegalArgumentException();

		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		float ele;

		// Get Minimum and Maximum element of the Vector.
		for (int i = 0; i < vector.length(); i++) {
			ele = vector.getElement(i);

			if (ele < min)
				min = ele;
			if (ele > max)
				max = ele;

		}

		// If Minimum and Maximum are same, only one bin is possible and that
		// contains
		// all the elements of the Vector (here same value).
		// So Count has all elements of the Vector and Bins centre will be
		// Maximum (or Minimum).
		if (min == max) {
			// hist[0] has count and hist[1] has corresponding bins centre.
			float[][] hist = new float[2][1];

			hist[0][0] = vector.length();
			hist[1][0] = max;

			VectorStack result = new VectorStack();

			for (int i = 0; i < hist.length; i++)
				result.addVector(new Vector(hist[i]));

			return result;

		} else {
			// hist[0] has count and hist[1] has corresponding bins centre.
			float[][] hist = new float[2][bins];
			// Size of each bin.
			float binsSize = (max - min) / (bins);
			// Centre of the first bin.
			float firstBinsCentre = min + binsSize / 2;
			hist[1][0] = firstBinsCentre;

			// HERE EACH ith BIN RANGE IS [MIN + i*BINSIZE, MIN+(i+1)*BINSIZE)
			// FOR FINAL BIN THE MAXIMUM VALUE OF VECTOR IS INCLUDED IN IT.

			// Centre of the subsequent bins are obtained by adding the bins
			// size
			// to the preceeding bins centre.
			for (int i = 1; i < bins; i++) {
				hist[1][i] = hist[1][i - 1] + binsSize;
			}

			for (int i = 0; i < vector.length(); i++) {
				ele = vector.getElement(i);

				// If the element is Maximum, it goes to final bins.
				// This condition is not accounted by the preceeding algorithm
				// to assign bin to an element.
				if (ele == max) {
					hist[0][bins - 1]++;
				} else {
					int binsIndex = (int) Math.floor((ele - min) / binsSize);
					hist[0][binsIndex]++;
				}

			}

			VectorStack result = new VectorStack();

			for (int i = 0; i < hist.length; i++)
				result.addVector(new Vector(hist[i]));

			return result;
		}
	}
}
