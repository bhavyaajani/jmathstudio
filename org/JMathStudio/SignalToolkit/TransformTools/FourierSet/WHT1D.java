package org.JMathStudio.SignalToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellMath;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;

/**
 * This class define a 1D discrete Walsh Hadamard Transform (WHT) and its
 * inverse on a discrete real signal.
 * <p>
 * A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be valid Vector object.
 * 
 * WHT1D wht = new WHT1D();//Create an instance of WHT1D.
 * 
 * Vector coeff = wht.wht1D(a);//Compute real WHT coefficients of signal as
 * represented by input Vector 'a'.
 * 
 * Vector recover = wht.iwht1D(coeff);//Recover original signal from its WHT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class WHT1D {
	
	/**
	 * This method will apply a discrete Walsh Hadamard Transform (WHT) on the
	 * discrete real signal as represented by the {@link Vector} 'vector' and
	 * return the result as a {@link Vector}.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector wht1D(Vector vector) {
		float[] result = null;
		int length = vector.length();

		if (length == 1) {
			return vector;
		}

		result = new VectorTools().makeDyadic(vector).accessVectorBuffer();

		float[][] decimate = new float[1][];
		decimate[0] = result;

		for (int i = result.length; i >= 2; i = i / 2) {
			float[][] tmp = new float[decimate.length * 2][];
			int index = 0;
			for (int j = 0; j < decimate.length; j++) {
				float[][] deci = f3(decimate[j]);
				tmp[index++] = deci[0];
				tmp[index++] = deci[1];
			}

			decimate = tmp;

		}

		float[] output = new float[decimate.length];

		for (int i = 0; i < decimate.length; i++) {
			output[i] = decimate[i][0];
		}

		return new Vector(output);

	}

	/**
	 * This method will apply an inverse discrete Walsh Hadamard Transform
	 * (IWHT) on the {@link Vector} 'vector' and return the resultant real
	 * signal as a {@link Vector}.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector iwht1D(Vector vector) {
		return VectorMath.linear(1.0f / vector.length(), 0, wht1D(vector));
	}

	private float[][] f3(float[] input) {
		float[][] output = new float[2][input.length / 2];

		for (int i = 0; i < input.length / 2; i++) {
			float[] tmp = f0(input[i], input[i + input.length / 2]);

			output[0][i] = tmp[0];
			output[1][i] = tmp[1];

		}

		return output;
	}

	private float[] f0(float a, float b) {
		float[] output = new float[2];

		output[0] = a + b;

		output[1] = a - b;

		return output;

	}

	/**
	 * This method will generate a square Hadamard Matrix of dimension as given
	 * by the argument 'N' and return the same as a {@link Cell}.
	 * <p>
	 * The value of the argument 'N' should be a power of 2 and more than 1 else
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell hadamardMatrix(int N)
			throws IllegalArgumentException {

		if (N < 2) {
			throw new IllegalArgumentException();
		}

		int index = 1;
		for (int i = 0;; i++) {
			if (N == index) {
				break;
			}

			if (N < index) {
				throw new IllegalArgumentException();
			}

			index = 2 * index;

		}

		return f2(N);

	}

	private Cell f2(int N) {
		if (N == 2) {
			try {
				return new Cell(new float[][] { { 1, 1 }, { 1, -1 } });
			} catch (IllegalCellFormatException e) {
				throw new BugEncounterException();
			}
		}

		Cell result;

		CellTools cell = new CellTools();

		Cell block = f2(N / 2);
		Cell tmp1 = null;
		Cell tmp2 = null;

		try {
			tmp1 = cell.concateLeft(block, block);

			tmp2 = cell.concateLeft(block, CellMath.linear(-1, 0, block));

			result = cell.concateBelow(tmp1, tmp2);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

		return result;

	}

}
