package org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics;

import java.util.Arrays;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.MathToolkit.StatisticalTools.Histogram;
import org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics.VectorStatistics;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define statistical operations applicable over a {@link Cell} or
 * an UInt PixelImage.
 * <pre>Usage:
 * Let 'a' be a valid Cell object.
 * 
 * CellStatistics cs = new CellStatistics();//Create an instance of CellStatistics.
 * 
 * float mean = cs.mean(a);//Estimate mean of the input Cell data.
 * float std = cs.standardDeviation(a);//Estimate standard deviation of the input Cell data.
 * float energy = cs.energy(a);//Estimate energy content of the input Cell data.
 * float[] centroid = cs.centroid(a);//Estimate centroids of the input Cell data.
 * float normL1 = cs.normL1(a);//Estimate L1 norm of the input Cell data.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class CellStatistics {
	
	private Histogram histogram;
	private MatrixTools matrix;
	
	/**
	 * Constructor of the class.
	 */
	public CellStatistics(){
		histogram = new Histogram();
		matrix = new MatrixTools();
	}
	
	/**
	 * This method return the Energy content of the elements of Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float energy(Cell cell) {
		float energy = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				energy = energy + cell.getElement(i, j) * cell.getElement(i, j);
			}
		}

		return energy;
	}

	/**
	 * This method will return the Median value of the elements of the Cell
	 * 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float median(Cell cell) {
		try {
			Cell vector = new CellTools().reArrange(cell, 1, cell.getRowCount()
					* cell.getColCount());
			float[] tmp = vector.accessRow(0).accessVectorBuffer();
			Arrays.sort(tmp);

			if (tmp.length % 2 == 0) {
				return (tmp[(tmp.length - 1) / 2] + tmp[(tmp.length - 1) / 2 + 1]) / 2;
			} else {
				return tmp[tmp.length / 2];
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will return the Sum of absolute value of all the elements of
	 * the Cell 'Cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float absoluteSum(Cell cell) {
		float abssum = 0;
		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				abssum = abssum + Math.abs(cell.getElement(i, j));
			}
		}

		return abssum;
	}

	/**
	 * This method will return the Sum of all the elements of the Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float sum(Cell cell) {
		float sum = 0;
		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				sum = sum + cell.getElement(i, j);
			}
		}

		return sum;
	}

	/**
	 * This method will compute the Centroid (YC,XC) point for the Cell 'cell'.
	 * <p>
	 * The Centroid of the 'cell' will be returned as a 1D float array, where
	 * first element will be the Centroid 'XC' location along X axis or column
	 * index of the 'Cell' and second element will be the Centroid 'YC' location
	 * along Y axis or row index of the 'Cell'.
	 * <p>
	 * If Sum of all the elements of the 'Cell' is zero this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @return float[]
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float[] centroid(Cell cell)
			throws IllegalArgumentException {
		float xc = 0;
		float yc = 0;
		float sum = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				float value = cell.getElement(i, j);
				xc = xc + value * (j);
				yc = yc + value * (i);
				sum = sum + value;
			}

		}

		if (sum == 0) {
			throw new IllegalArgumentException();
		}

		return new float[] { xc / sum, yc / sum };

	}

	/**
	 * This method will return the Mean value of the elements of the Cell
	 * 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float mean(Cell cell) {
		return sum(cell) / (cell.getRowCount() * cell.getColCount());
	}

	/**
	 * This method will return the Standard Deviation value of the elements of
	 * the Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float standardDeviation(Cell cell) {
		Cell tmp;

		try {
			tmp = new CellTools().reArrange(cell, 1, cell.getRowCount()
					* cell.getColCount());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return new VectorStatistics().standardDeviation(tmp.accessRow(0));
	}

	/**
	 * This method will return the Root Mean Square value of the elements of the
	 * Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float RMS(Cell cell) {
		float rms = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				rms = rms + cell.getElement(i, j) * cell.getElement(i, j);
			}
		}

		rms = rms / (cell.getColCount() * cell.getRowCount());

		return (float) Math.sqrt(rms);
	}

	/**
	 * This method will compute the (p+q)th order Central Moment of the Cell
	 * 'cell'.
	 * <p>
	 * Here the argument 'p' is the order of the moment along X (column)
	 * direction and 'q' is the order of the moment along Y(row) direction. The
	 * argument 'p' and 'q' should be more than or equal to 0 else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * If the summation of all the elements 'cell' is 0 this method will throw
	 * an IllegalArgument Exception.
	 * <p>
	 * The Central Moment U(p,q) is calculated as :
	 * <p>
	 * U(p,q) = sum( (i-yc)^q * (j-xc)^p * Cell(i,j)),
	 * <p>
	 * where 'xc' and 'yc' is the centroid position for the given Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @param int p
	 * @param int q
	 * @return double
	 * @throws IllegalArgumentException
	 * @see #centroid(Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double centralMoment(Cell cell, int p, int q)
			throws IllegalArgumentException {
		if (p < 0 || q < 0) {
			throw new IllegalArgumentException();
		}

		float[] centroid = centroid(cell);
		float xc = centroid[0];
		float yc = centroid[1];

		double moment = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				moment = (moment + Math.pow(i - yc, q) * Math.pow(j - xc, p)
						* cell.getElement(i, j));
			}
		}

		return moment;
	}

	/**
	 * This method will return the Maximum valued element of the Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float maximum(Cell cell) {
		float maximum = -Float.MAX_VALUE;
		float ele;
		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				ele = cell.getElement(i, j);
				if (ele > maximum)
					maximum = ele;
			}
		}

		return maximum;
	}

	/**
	 * This method will return the Minimum valued element of the Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float minimum(Cell cell) {
		float minimum = Float.MAX_VALUE;
		float ele;
		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				ele = cell.getElement(i, j);
				if (ele < minimum)
					minimum = ele;
			}
		}

		return minimum;
	}

	/**
	 * This method will return the Manhattan or L1 norm of the elements of the
	 * Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float normL1(Cell cell) {		

		float sum = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for(int j=0;j<cell.getColCount();j++){
				sum = sum + Math.abs(cell.getElement(i, j));
			}
		}

		return sum;
	}
	
	/**
	 * This method will return the pth norm of the elements of the Cell
	 * 'cell'.
	 * <p>
	 * The argument 'p' which specify the dimension of the L^p space should be
	 * more than '0' else this method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param int p
	 * @return float
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float normP(Cell cell, int p) throws IllegalArgumentException {

		if (p < 1)
			throw new IllegalArgumentException();

		double sum = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for(int j=0;j< cell.getColCount();j++){
				sum = sum + Math.pow(Math.abs(cell.getElement(i, j)), p);
			}
		}

		return (float) Math.pow(sum, 1.0 / p);
	}

	/**
	 * This method will return the Euclidian or L2 norm of the elements of the
	 * Cell 'cell'.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float normL2(Cell cell) {
		float sum = 0;

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				sum = sum + cell.getElement(i, j) * cell.getElement(i, j);
			}
		}

		return (float) Math.sqrt(sum);
	}

	/**
	 * This method will remove the mean from the given Cell 'cell' and return
	 * the resultant zero mean Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell deMean(Cell cell) {
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		float mean = mean(cell);

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(cell.getElement(i, j) - mean, i, j);
			}
		}

		return result;
	}

	/**
	 * This method will return the norm1 of the given Cell 'cell'.
	 * <p>
	 * The norm1 of the Cell is the maximum of the column sum of the Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float norm1(Cell cell) {
		float f = 0;
		for (int j = 0; j < cell.getColCount(); j++) {
			float s = 0;
			for (int i = 0; i < cell.getRowCount(); i++) {
				s += Math.abs(cell.getElement(i, j));
			}
			f = Math.max(f, s);
		}
		return f;
	}

	/**
	 * This method will return the normInf of the given Cell 'cell'.
	 * <p>
	 * The normInf of the Cell is the maximum of the row sum of the Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float normInf(Cell cell) {
		float f = 0;
		for (int i = 0; i < cell.getRowCount(); i++) {
			float s = 0;
			for (int j = 0; j < cell.getColCount(); j++) {
				s += Math.abs(cell.getElement(i, j));
			}
			f = Math.max(f, s);
		}
		return f;
	}

	/**
	 * This method will return the Shannon Entropy of the {@link AbstractUIntPixelImage} 
	 * 'image' in the unit of bit.
	 * <p>
	 * Entropy is derived from the information content and probability mass
	 * function of the {@link AbstractUIntPixelImage} 'image'.
	 * 
	 * @param AbstractUIntPixelImage
	 *            image
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float entropy(AbstractUIntPixelImage image) {
		Vector pmf = histogram.PMF(image);

		// Used to calculate log of pmf to base 2, using change of base rule.
		double log2toe = Math.log(2);
		float entropy = 0;

		for (int i = 0; i < pmf.length(); i++) {
			float tmp = pmf.getElement(i);

			if (tmp > 0) {
				entropy += tmp * (Math.log(tmp) / log2toe);
			}
		}

		return -entropy;
	}
	
	/**
	 * This method will compute the coefficient of Inner product between the two
	 * {@link Cell}'s 'cell1' and 'cell2' and return the same.
	 * <p>
	 * The dimension of both the Cell's should be the same else this method will
	 * throw an DimensionMismatch Exception.
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return float
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float innerProduct(Cell cell1, Cell cell2)
			throws DimensionMismatchException {
		return matrix.innerProduct(cell1, cell2);
	}
	
	// /**
	// * This method will compute the Histogram of the elements of the Cell
	// 'cell' and
	// * return the same as a VectorStack.
	// * <p>
	// * The argument 'bins' specify the number of bins for computing the
	// histogram. This many
	// * partitions of the full range of elements of the 'cell' will be taken in
	// to consideration for
	// * computing histogram. The argument 'bins' should be more than 0 else
	// this method will throw an IllegalArgument Exception.
	// * <p>
	// * The 0th and the 1st Vector of the return VectorStack will contain
	// * respectively the histogram counts and the corresponding bin
	// range/partitions
	// * for the given histogram.
	// *
	// * @param Cell
	// * cell
	// * @param int bins
	// * @return VectorStack
	// * @throws IllegalArgumentException
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public VectorStack histogram(Cell cell, int bins)
	// throws org.JILab.Exceptions.IllegalArgumentException {
	// if (bins < 1) {
	// throw new org.JILab.Exceptions.IllegalArgumentException();
	// }
	//
	// float[][] hist = new float[2][bins];
	//
	// float min = Float.MAX_VALUE;
	// float max = -Float.MAX_VALUE;
	//
	// for (int i = 0; i < cell.getRowCount(); i++) {
	// for (int j = 0; j < cell.getColCount(); j++) {
	// float ele = cell.getElement(i, j);
	// min = ele < min ? ele : min;
	// max = ele > max ? ele : max;
	// }
	// }
	//
	// if (bins == 1)
	// {
	// hist[0][0] = max;
	// hist[1][0] = cell.getRowCount() * cell.getColCount();
	// }
	// float base = (max - min) / (bins);
	//
	// hist[1][0] = min;
	//
	// for (int i = 1; i < hist[1].length - 1; i++)
	// {
	// hist[1][i] = hist[1][i - 1] + base;
	// }
	//
	// hist[1][hist[1].length - 1] = max;
	//
	// for (int i = 0; i < cell.getRowCount(); i++)
	// {
	// for (int j = 0; j < cell.getColCount(); j++)
	// {
	// for (int h = 0; h < hist[1].length - 1; h++)
	// {
	// float value = cell.getElement(i, j);
	//
	// if (value >= hist[1][h] && value < hist[1][h + 1]) {
	// hist[0][h]++;
	// break;
	// }
	// if (value == hist[1][hist[1].length - 1]) {
	// hist[0][hist[1].length - 1]++;
	// break;
	// }
	// }
	//
	// }
	// }
	//
	// VectorStack result = new VectorStack(hist.length);
	//
	// for (int i = 0; i < result.size(); i++)
	// result.setVector(new Vector(hist[i]), i);
	//
	// return result;
	// }

}
