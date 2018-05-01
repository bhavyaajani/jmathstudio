package org.JMathStudio.ImageToolkit.ProcessingTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellMath;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.ImageToolkit.IntensityTools.ImageThreshold;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;
import org.JMathStudio.ToolBoxes.WaveletToolBox.DWT2D;
import org.JMathStudio.ToolBoxes.WaveletToolBox.DWT2DCoeff;
import org.JMathStudio.ToolBoxes.WaveletToolBox.Wavelet;

/**
 * This class define various Image denoising operations.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */

public final class ImageDeNoiser {
	/**
	 * This method define an Adaptive de-noising operation for denoising the
	 * discrete real image as represented by the {@link Cell} 'cell' and return
	 * the resultant denoised image as a Cell.
	 * <p>
	 * The dimensions i.e. row and column count of the Cell 'cell' should be
	 * more than '1' else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The denoising algorithm employed here is <i>Normal Shrink adaptive
	 * denoising</i> which thresholds the 2D DWT coefficients of the given input
	 * image with soft thresholding.
	 * <p>
	 * The argument 'wavelet' here specify the Analysing and Synthesising
	 * wavelet to be employed for this operation
	 * <p>
	 * The argument 'level' specify the level of Wavelet decomposition to be
	 * carried out for given denoising operation and it should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param Wavelet
	 *            wavelet
	 * @param int level
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell normalShrinkAdaptive(Cell cell, Wavelet wavelet, int level)
			throws IllegalArgumentException {
		CellStatistics statistics = new CellStatistics();

		ImageThreshold it = new ImageThreshold();
		DWT2DCoeff coeff = new DWT2D().dwt(cell, level, wavelet);

		Cell hh1 = coeff.accessDiagonal(1);
		hh1 = CellMath.absolute(hh1);
		float noiseVariance = statistics.median(hh1) / 0.6745f;
		noiseVariance = noiseVariance * noiseVariance;

		for (int i = 1; i <= level; i++) {
			Cell hh = coeff.accessDiagonal(i);
			Cell hl = coeff.accessHorizontal(i);
			Cell lh = coeff.accessVertical(i);

			float hhSigma = statistics.standardDeviation(hh);
			float hlSigma = statistics.standardDeviation(hl);
			float lhSigma = statistics.standardDeviation(lh);

			float B = (float) Math.sqrt(Math.log((hh.getRowCount() * hh
					.getColCount())
					/ level));

			float hhThres = B * noiseVariance / hhSigma;
			float hlThres = B * noiseVariance / hlSigma;
			float lhThres = B * noiseVariance / lhSigma;

			hh = it.softThreshold(hh, hhThres);
			hl = it.softThreshold(hl, hlThres);
			lh = it.softThreshold(lh, lhThres);

			try {
				coeff.assignDiagonal(hh, i);
				coeff.assignHorizontal(hl, i);
				coeff.assignVertical(lh, i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}

		}

		return new DWT2D().idwt(coeff, coeff.accessAssociatedWavelet());

	}
}
