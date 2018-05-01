package org.JMathStudio.SignalToolkit.TransformTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics.VectorStatistics;

/**
 * This class define Principal Component Decomposition (PCD) operation on a set
 * of discrete real signals. The set of discrete real signals will be
 * represented by a {@link VectorStack} object.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class PCD {

	/**
	 * This method will perform the Principal Component Decomposition on the set
	 * of discrete real signals as represented by the {@link VectorStack} 'vectorStack'
	 * where each {@link Vector} of the VectorStack represents a discrete real signal.
	 * <p>
	 * The result of PCD is return as a VectorStack where each Vector represents
	 * a unique independent Principal Component.
	 * <p>
	 * The length of all the Vectors representing the set of signal as given by
	 * the argument 'vectorStack' should be the same else this method will throw
	 * a DimensionMismatch Exception.
	 * <p>
	 * PCD computes the Eigen Vectors of the cross correlation matrix of the
	 * given set of signal and such Principal components forms an Orthogonal
	 * Basis for the given set of signal.
	 * 
	 * @param VectorStack
	 *            vectorStack
	 * @return VectorStack
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack decompose(VectorStack vectorStack)
			throws DimensionMismatchException {
		float[][] signals = new float[vectorStack.size()][];
		for (int i = 0; i < signals.length; i++)
			signals[i] = vectorStack.accessVector(i).accessVectorBuffer();

		int length = signals[0].length;

		for (int i = 0; i < signals.length; i++) {
			if (signals[i].length != length) {
				throw new DimensionMismatchException();
			}
		}

		MatrixTools matrix = new MatrixTools();
		
		VectorStatistics stat = new VectorStatistics();

		float[][] result = new float[signals.length][];

		for (int i = 0; i < signals.length; i++) {
			Vector buff = new Vector(signals[i]);
			float mean = stat.mean(buff);
			result[i] = VectorMath.linear(1, -mean, buff).accessVectorBuffer();
		}

		try {

			Cell buffer = null;
			Cell tmp = new Cell(result);

			buffer = matrix.crossProduct(tmp, matrix.transpose(tmp));
			buffer = matrix.eigenDecomposition(buffer).accessCell(0);

			buffer = matrix.crossProduct(matrix.transpose(tmp), buffer);
			buffer = matrix.transpose(buffer);

			for (int i = 0; i < buffer.getRowCount(); i++) {
				float norm = stat.normL2(buffer.accessRow(i));
				result[i] = VectorMath.linear(1.0f / norm, 0, buffer.accessRow(i))
						.accessVectorBuffer();
			}

		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}

		VectorStack stack = new VectorStack();
		for (int i = 0; i < result.length; i++)
			stack.addVector(new Vector(result[i]));

		return stack;
	}

}