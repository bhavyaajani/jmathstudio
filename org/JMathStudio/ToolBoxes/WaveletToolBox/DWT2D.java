package org.JMathStudio.ToolBoxes.WaveletToolBox;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define 2D discrete Wavelet transform (DWT) operation on a
 * discrete real image.
 * <p>A discrete real image will be represented by a {@link Cell} object.
 * <p>
 * 2D DWT consists of following two operations,
 * <p>
 * <i>Analytical or Decomposition</i>
 * <p>
 * This operation decomposes a discrete real image in to a set of wavelet
 * coefficients.
 * <p>
 * <i>Synthesis or Reconstruction</i>
 * <p>
 * This operation synthesis or reconstructs a discrete real image from its
 * wavelet coefficients.
 * <p>
 * 2D {@link Wavelet} Coefficients will be represented by a {@link DWT2DCoeff}
 * object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("image_path");//Import external image as Cell.
 * 
 * DWT2D dwt = new DWT2D();//Create an instance of DWT2D.
 * Wavelet wavelet = WaveletFactory.getDB3Wavelet();//Create required Wavelet for decomposition.
 * 
 * DWT2DCoeff coeff = dwt.dwt(img, 2, wavelet);//Compute DWT coefficients for image as
 * represented by input Cell 'img' for given levels.
 * 
 * Cell recover = dwt.idwt(coeff, coeff.accessAssociatedWavelet());//Recover image from its
 * DWT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class DWT2D {

	/**
	 * This method will perform a 2D DWT Analytical or Decomposition operation
	 * on a discrete real image as represented by the {@link Cell} 'cell' with the
	 * analysing {@link Wavelet} as specified by the argument 'wavelet' and
	 * level of decomposition as given by the argument 'level'.
	 * <p>
	 * The set of wavelet decomposition coefficient for all levels will be
	 * return as a {@link DWT2DCoeff}.
	 * <p>
	 * The argument 'level' should be more than 0 and row and column count of
	 * 'cell' should be more than '1' else this method will throw an
	 * IllegalArgument Exception.
	 * <p>The argument 'wavelet' is retain as reference within the return {@link DWT2DCoeff}.
	 * 
	 * @param Cell
	 *            cell
	 * @param int level
	 * @param Wavelet
	 *            wavelet
	 * @return DWT2DCoeff
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public DWT2DCoeff dwt(Cell cell, int level, Wavelet wavelet)
			throws IllegalArgumentException {
		if (level < 1) {
			throw new IllegalArgumentException();
		}

		if (cell.getRowCount() < 2 || cell.getColCount() < 2)
			throw new IllegalArgumentException();

		boolean[][] evenOdd = new boolean[level][2];
		CellStack[] decomposition = new CellStack[level];

		Cell currentApproximate = cell;

		for (int i = 0; i < level; i++) {
			if (currentApproximate.getRowCount() % 2 == 0) {
				evenOdd[i][0] = true;
			} else {
				evenOdd[i][0] = false;
			}

			if (currentApproximate.getColCount() % 2 == 0) {
				evenOdd[i][1] = true;
			} else {
				evenOdd[i][1] = false;
			}
			decomposition[i] = f3(currentApproximate, wavelet);
			currentApproximate = decomposition[i].accessCell(0);
		}

		DWT2DCoeff coeff = new DWT2DCoeff(decomposition,evenOdd);
		coeff.assignAssociatedWavelet(wavelet);

		return coeff;

	}

	/**
	 * This method will perform a 2D DWT Synthesis or Reconstruction operation
	 * using a set of DWT coefficients as represented by the argument
	 * {@link DWT2DCoeff} 'coeff' with the synthesising {@link Wavelet} as
	 * specified by the argument 'wavelet'.
	 * <p>
	 * The reconstructed image will be return as a {@link Cell}.
	 * 
	 * @param DWT2DCoeff
	 *            coeff
	 * @param Wavelet
	 *            wavelet
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell idwt(DWT2DCoeff coeff, Wavelet wavelet) {
		CellStack[] decomposition = coeff.f5();
		boolean[][] evenOdd = coeff.f3();
		int level = decomposition.length;

		Cell currentApproximate = decomposition[level - 1].accessCell(0);

		try {
			for (int i = level - 1; i >= 0; i--) {
				decomposition[i].replace(currentApproximate, 0);
				currentApproximate = f5(decomposition[i], wavelet,
						evenOdd[i]);
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return currentApproximate;
	}

	private CellStack f3(Cell image, Wavelet wave) {
		Vector low = new Vector(wave.accessLD());
		Vector high = new Vector(wave.accessHD());

		MatrixTools tool = new MatrixTools();

		Cell ll = null;
		Cell lh = null;
		Cell hl = null;
		Cell hh = null;

		ll = f4(f1(image, low));
		lh = f4(f1(tool.transpose(ll), high));
		lh = tool.transpose(lh);
		ll = f4(f1(tool.transpose(ll), low));
		ll = tool.transpose(ll);

		hl = f4(f1(image, high));
		hh = f4(f1(tool.transpose(hl), high));
		hh = tool.transpose(hh);
		hl = f4(f1(tool.transpose(hl), low));
		hl = tool.transpose(hl);

		Cell[] decomposition = new Cell[4];
		decomposition[0] = ll;// approximate
		decomposition[1] = hl;// horizontal
		decomposition[2] = lh;// vertical
		decomposition[3] = hh;// diagonal

		return new CellStack(decomposition);

	}

	private Cell f5(CellStack decomposition, Wavelet wave,
			boolean[] evenOdd)
			throws IllegalArgumentException {
		if (decomposition.size() != 4) {
			throw new IllegalArgumentException();
		}

		Vector low = new Vector(wave.accessLR());
		Vector high = new Vector(wave.accessHR());

		MatrixTools tool = new MatrixTools();

		Cell ll = decomposition.accessCell(0);
		Cell hl = decomposition.accessCell(1);
		Cell lh = decomposition.accessCell(2);
		Cell hh = decomposition.accessCell(3);

		try {
			ll = tool.transpose(f1(f6(tool
					.transpose(ll)), low));
			hl = tool.transpose(f1(f6(tool
					.transpose(hl)), low));
			lh = tool.transpose(f1(f6(tool
					.transpose(lh)), high));
			hh = tool.transpose(f1(f6(tool
					.transpose(hh)), high));

			ll = tool.add(ll, lh);
			hh = tool.add(hl, hh);

			ll = f1(f6(ll), low);
			hh = f1(f6(hh), high);

			Cell tmp = tool.add(ll, hh);

			int rowLength, colLength;

			if (evenOdd[0])
				rowLength = tmp.getRowCount() - (2 * low.length()) + 3;
			else
				rowLength = tmp.getRowCount() - (2 * low.length()) + 2;

			if (evenOdd[1])
				colLength = tmp.getColCount() - (2 * low.length()) + 3;
			else
				colLength = tmp.getColCount() - (2 * low.length()) + 2;

			return new CellTools().subCell(tmp, low.length() -1, rowLength,
					low.length() -1, colLength);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	private Cell f6(Cell image) {
		Cell result = new Cell(image.getRowCount(), image.getColCount() * 2);

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				result.assignRow(f2(image.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		return result;
	}

	private Vector f2(Vector signal) {
		float[] result = new float[signal.length() * 2];

		for (int i = 0; i < signal.length(); i++) {
			result[2 * i+1] = signal.getElement(i);
		}

		return new Vector(result);
	}

	private Cell f4(Cell image) {
		Cell result = new Cell(image.getRowCount(), image.getColCount() / 2);
		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				result.assignRow(f8(image.accessRow(i)), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		return result;
	}

	private Vector f8(Vector signal) {
		if (signal.length() <= 2) {
			return new Vector(new float[] { signal.getElement(0) });
		} else {
			float result[] = new float[signal.length() / 2];

			for (int i = 0; i < result.length; i++) {
				result[i] = signal.getElement(2*i+1);
			}

			return new Vector(result);
		}
	}

	private Cell f1(Cell image, Vector impulse) {
		Cell result = new Cell(image.getRowCount(), image.getColCount()
				+ impulse.length() - 1);

		for (int i = 0; i < result.getRowCount(); i++) {
			try {
				result.assignRow(f7(image.accessRow(i), impulse), i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}
		}

		return result;
	}

	private Vector f7(Vector signal, Vector impulse) {
		int l1 = signal.length();
		int l2 = impulse.length();

		//Linear convolution of signal with padding result.
		Vector result = new Vector(l1 + l2 - 1);
		
		int twiceL_1 = 2*(l1-1);
		int l;
		
		for (int x = 0; x < result.length(); x++) {
			float tmp = 0;
			int i1 = x;
			int i2 = 0;

			while (i2 < l2){
				l = i1;
				//Getting index (l) within given signal bounds for indexes (i1)which are outside
				//signal bound to get symmetric padded values.
				//This is employed here directly rather than using any method to obtain
				//padding values to speed up calculation.
				if(i1>=l1 || i1<0){
				l = Math.abs(i1%twiceL_1);
				
				if(l >= l1)
					l = twiceL_1 - l;
				}
				tmp += signal.getElement(l)*impulse.getElement(i2);
				i1--;
				i2++;
			}

			result.setElement(tmp, x);

		}

		return result;

	}
}
