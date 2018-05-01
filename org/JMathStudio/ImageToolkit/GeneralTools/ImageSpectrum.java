package org.JMathStudio.ImageToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.CCellMath;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellMath;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.ImageToolkit.TransformTools.FourierSet.FFT2D;

/**
 * This class define various methods for estimating Spectral density/Spectrum of a
 * discrete real image.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ImageSpectrum is = new ImageSpectrum();//Create an instance of ImageSpectrum.
 * 
 * Cell esd = is.ESD(img);//Estimate coefficients of energy spectral density for input image.
 * 
 * Cell psd = is.PSD(img);//Estimate coefficients of power spectral density for input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageSpectrum {

	/**
	 * This method compute the two sided Energy Spectral Density for the
	 * discrete real image as represented by the {@link Cell} 'cell' and return
	 * the same as a Cell.
	 * <p>
	 * The ESD is computed from the 2D FFT of the linear auto correlation
	 * function of the given image. The return Cell representing the ESD has DC
	 * energy component located at the centre with elements on the either side
	 * represent the energy for the corresponding evenly spread normalised
	 * frequencies in the range of [-0.5 0.5] respectively. Further the
	 * dimension of the return Cell will be [2N-1, 2M-1] corresponding to [2N-1,
	 * 2M-1] discrete frequencies. Here 'N' and 'M' being the height and width
	 * of the original Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell ESD(Cell cell) {
		try {
			// N + N -1 resize to reflect linear auto correlation.
			int size_y = 2 * cell.getRowCount() - 1;
			int size_x = 2 * cell.getColCount() - 1;

			FFT2D fft = new FFT2D();
			CellTools tools = new CellTools();
			
			Cell resize_img = tools.resize(cell, size_y, size_x);
			CCell imgfft = fft.fft2D(resize_img);
			// ESD is FFT(linear auto correlation)
			Cell esd = imgfft.getMagnitude();
			esd = CellMath.power(esd, 2);

			// Need to do wrapping around to bring DC in centre.
			return tools.wrapCell(esd);

		} catch (IllegalArgumentException e1) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method compute the two sided Cross Energy Spectral Density between
	 * the discrete real images as represented by the {@link Cell}'s 'cell1' and
	 * 'cell2' and return the same as a Cell.
	 * <p>
	 * The CESD is computed from the 2D FFT of the linear cross correlation
	 * function for the given images. The return Cell representing the CESD has
	 * DC energy component located at the centre with elements on the either
	 * side represent the energy for the corresponding evenly spread normalised
	 * frequencies in the range of [-0.5 0.5] respectively. Further the
	 * dimension of the return Cell will be [N + N^ -1, M + M^ -1] corresponding
	 * to [N + N^ -1, M + M^ -1] discrete frequencies. Here [N M] and [N^ M^]
	 * being the dimensions of the original Cell's.
	 * 
	 * @param Cell
	 *            cell1
	 * @param Cell
	 *            cell2
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell CESD(Cell cell1, Cell cell2) {
		try {
			// N + M -1 resize to reflect linear correlation.
			int size_y = cell1.getRowCount() + cell2.getRowCount() - 1;
			int size_x = cell1.getColCount() + cell2.getColCount() - 1;

			FFT2D fft = new FFT2D();
			CellTools tools = new CellTools();

			Cell resize_cell1 = tools.resize(cell1, size_y, size_x);
			Cell resize_cell2 = tools.resize(cell2, size_y, size_x);

			CCell fft1 = fft.fft2D(resize_cell1);
			CCell fft2 = fft.fft2D(resize_cell2);

			fft1 = CCellMath.dotProduct(fft1, fft2.getConjugate());

			// ESD is FFT(linear auto correlation)
			Cell esd = fft1.getMagnitude();

			// Need to do wrapping around to bring DC in centre.
			return tools.wrapCell(esd);

		} catch (IllegalArgumentException e1) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method compute the two sided Power Spectral Density for the discrete
	 * real periodic image whose one period is as represented by the
	 * {@link Cell} 'cell' and return the same as a Cell.
	 * <p>
	 * The PSD is computed from the 2D FFT of the circular auto correlation
	 * function of the given image. The return Cell representing the PSD has DC
	 * power component located at the centre with elements on the either side
	 * represent the power for the corresponding evenly spread normalised
	 * frequencies in the range of [-0.5 0.5] respectively. Further the
	 * dimension of the return Cell will be [N, M] corresponding to [N, M]
	 * discrete frequencies. Here [N M] being the dimension of the original
	 * Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell PSD(Cell cell) {
		int N = cell.getRowCount();
		int M = cell.getColCount();

		FFT2D fft = new FFT2D();
		CellTools tools = new CellTools();
		
		CCell imgfft = fft.fft2D(cell);
		// ESD is FFT(circular auto correlation)
		Cell psd = imgfft.getMagnitude();
		psd = CellMath.power(psd, 2);

		psd = CellMath.linear(1.0f / (N * M), 0, psd);

		// Need to do wrapping around to bring DC in centre.
		return tools.wrapCell(psd);
	}

}
