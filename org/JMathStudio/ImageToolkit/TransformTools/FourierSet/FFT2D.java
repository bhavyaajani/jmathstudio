package org.JMathStudio.ImageToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.CCellMath;
import org.JMathStudio.DataStructure.Cell.CCellTools;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.SignalToolkit.TransformTools.FourierSet.FFT1D;

/**
 * This class define a 2D discrete Fast Fourier Transform (FFT) and its inverse
 * on a discrete real and complex image.
 * <p>
 * A discrete real and complex image will be represented by a {@link Cell} and a {@link CCell}
 * object respectively.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * FFT2D tr = new FFT2D();//Create an instance of FFT2D.
 * 
 * CCell fft = tr.fft2D(img);//Apply 2D FFT on input image and compute FFT coefficients.
 * Cell res = tr.ifft2D(fft);//Recover original image by applying inverse 2D FFT on the
 * FFT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FFT2D {

	/**
	 * This method will apply a discrete Fast Fourier Transform (FFT) on the
	 * discrete complex image as represented by the CCell 'cell' and return the
	 * result as a CCell.
	 * <p>
	 * 2D FFT is computed by taking 1D FFT along rows and columns of the image.
	 * <p>
	 * The return CCell containing the fourier coefficients is not centred i.e.
	 * DC coefficient is not located at the centre of the CCell.
	 * @param CCell
	 *            cell
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public CCell fft2D(CCell cell) {
		try {


			int rc = cell.getRowCount();
			int cc = cell.getColCount();

			CCell result = new CCell(rc, cc);

			FFT1D fftrow = new FFT1D(cc);

			for (int i = 0; i < rc; i++) {
				result.assignRow(fftrow.fft1D(cell.accessRow(i)), i);
			}

			//After this for result, row count is cc and vice versa.
			result = result.getTransposed();
			FFT1D fftcol;

			if(rc == cc)
				fftcol = fftrow;
			else
				fftcol = new FFT1D(rc);

			for (int i = 0; i < cc; i++) {
				result.assignRow(fftcol.fft1D(result.accessRow(i)), i);
			}

			return result.getTransposed();

		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 ** This method will apply a discrete Fast Fourier Transform (FFT) on the
	 * discrete real image as represented by the Cell 'cell' and return the
	 * result as a CCell.
	 * <p>
	 * 2D FFT is computed by taking 1D FFT along rows and columns of the image.
	 * <p>
	 * The return CCell containing the fourier coefficients is not centred i.e.
	 * DC coefficient is not located at the centre of the CCell.
	 * @param Cell
	 *            cell
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell fft2D(Cell cell) {
		try {

			int rc = cell.getRowCount();
			int cc = cell.getColCount();

			CCell result = new CCell(rc, cc);

			FFT1D fftrow = new FFT1D(cc);

			for (int i = 0; i < rc; i++) {
				result.assignRow(fftrow.fft1D(cell.accessRow(i)), i);
			}

			//After this for result, row count is cc and vice versa.
			result = result.getTransposed();
			FFT1D fftcol;

			if(rc == cc)
				fftcol = fftrow;
			else
				fftcol = new FFT1D(rc);

			for (int i = 0; i < cc; i++) {
				result.assignRow(fftcol.fft1D(result.accessRow(i)), i);
			}

			return result.getTransposed();

		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will apply an inverse discrete Fast Fourier Transform (IFFT)
	 * on the CCell 'fft' representing the non centred FFT coefficients and
	 * return the resultant complex image as a CCell.
	 * <p>
	 * This method should be used in conjection with FFT only when the original
	 * image is a complex image.
	 * <p>
	 * 2D IFFT is computed by taking 1D IFFT along rows and columns of the CCell
	 * 'fft'.
	 * @param CCell
	 *            fft
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell ifft2DComplex(CCell fft) 
	{

		CCell result = fft.getConjugate();

		result = fft2D(result);
		result = result.getConjugate();

		float N = (float) result.getRowCount() * result.getColCount();

		return CCellMath.scale(result, 1.0f / N);

	}

	/**
	 * This method will apply an inverse discrete Fast Fourier Transform (IFFT)
	 * on the CCell 'fft' representing the non centred FFT coefficients and
	 * return the real part of the resultant complex image as a Cell.
	 * <p>
	 * This method should be used in conjection with FFT only when the original
	 * image is a real image thus imaginary part of resultant complex image will
	 * be zero and thus can be discarded.
	 * <p>
	 * 2D IFFT is computed by taking 1D IFFT along rows and columns of the CCell
	 * 'fft'.
	 * @param CCell
	 *            fft
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell ifft2D(CCell fft) {

		CCell result = fft.getConjugate();

		result = fft2D(result);
		//Do not take conjugate as we are going to return only real part as result.
		//result = result.getConjugate();

		float N = (float) result.getRowCount() * result.getColCount();

		result = CCellMath.scale(result, 1.0f / N);

		return result.accessRealPart();

	}

	/**
	 * This method will shuffle the 2D FFT coefficients as represented by the
	 * CCell 'fft' such that the return CCell has DC fourier coefficient located
	 * at the centre with left-right and top-bottom symmetry.
	 * <p>
	 * This method should be used for proper visualisation of 2D FFT
	 * coefficients but is not the standard representation of 2D FFT
	 * coefficients.
	 * <p>Do not use this coefficients to compute inverse 2D FFT.
	 * 
	 * @param CCell
	 *            fft
	 * @return CCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CCell centre(CCell fft) {

		return new CCellTools().wrapCCell(fft);
	}
}
