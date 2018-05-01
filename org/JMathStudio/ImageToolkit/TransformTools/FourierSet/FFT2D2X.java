package org.JMathStudio.ImageToolkit.TransformTools.FourierSet;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class defines transforms to compute simultaneously two discrete Fast Fourier Transform (FFT) and/or its inverse for 2 
 * discrete real image of equal dimensions by employing a single discrete Fast Fourier Transform and/or its inverse over a 
 * discrete complex image by utilizing redundancy in FFT computation for real images.
 * <p>This transform computes FFT of 2 discrete real images by taking a single FFT over a complex hybrid image made up of 
 * input real images and later on splitting the FFT coefficients for individual real images from FFT coefficients of complex
 * hybrid image.
 * <p>Similarly the above transform is applied for inverse FFT by first merging the individual FFT coefficients or spectra and
 * applying a single inverse FFT over merged spectra and later on splitting the individual real images from the estimated 
 * inverse FFT.
 * <p>Internally this class uses FFT2D to compute FFT and hence refer {@link FFT2D} for further information on the FFT computation.
 * <p>This class help to improve performance in scenario where multiple FFTs or inverse FFTs are to be computed for real images with
 * same dimensions. 
 * 
 * <pre>Usage:
 * Let 'a' & 'b' be a valid Cell object representing two real images of same dimensions.
 *		
 * FFT2D2X fft2x = new FFT2D2X();
 * CCell[] ffts = fft2x.fft2X(a, b);//Here ffts[0] & ffts[1] are FFT for Cell 'a' & 'b' respectively.
 *  
 * Cell[] recover = fft2x.ifft2X(ffts[0], ffts[1]);//Recover original real images by taking a single inverse FFT. 
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class FFT2D2X {

	private FFT2D op = new FFT2D();

	/**
	 * This method computes simultaneously FFT of two real images represented by {@link Cell}s cell1 & cell2 having same dimension,
	 * by using a single FFT over a complex hybrid image made up of input real images. 
	 * <p>FFT coefficients of individual input real images are extracted from the FFT coefficients of the complex hybrid image.
	 * <p>The computed FFTs for real input images are return as a {@link CCell} array of size 2 where first & second CCell 
	 * represents FFTs for input real images 'cell1' and 'cell2' respectively.
	 * <p>If dimensions of Cell 'cell1' and 'cell2' are not same this method shall throw an {@link IllegalArgumentException}. 
	 *  
	 * <P>Refer {@link FFT2D#fft2D(Cell)} for understanding CCellr representing FFT coefficients.
	 * @param Cell cell1
	 * @param Cell cell2
	 * @return CCell[]
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCell[] fft2X(Cell cell1,Cell cell2) throws IllegalArgumentException
	{
		if(cell1 == null || cell2 == null)
			throw new NullPointerException();
		if(!cell1.hasSameDimensions(cell2))
			throw new IllegalArgumentException();

		try{

			CCell ccell = new CCell(cell1, cell2);
			CCell fft = op.fft2D(ccell);

			return f5(fft);

		}catch(Exception e){
			throw new BugEncounterException();
		}

	}

	/**
	 * This method estimates simultaneously two real images from their individual FFT coefficients as represented by 
	 * {@link CCell}s 'fft1' and 'fft2' having same dimensions, by using a single inverse FFT over a complex hybrid image
	 * obtained by merging the individual FFT coefficients or spectra. 
	 * <p>Real images are obtained as real and imaginary part of the computed inverse FFT of the complex hybrid image.
	 * <p>The estimated real images are return as a {@link Cell} array of size 2 where first & second Cell 
	 * represents real images for input FFT coefficients 'fft1' and 'fft2' respectively.
	 * <p>If dimensions of input CCell 'fft1' and 'fft2' are not same this method shall throw an {@link IllegalArgumentException}. 
	 *  
	 * <P>Refer {@link FFT2D#ifft2D(CCell)} for understanding CCell representing FFT coefficients.
	 * @param CCell fft1
	 * @param CCell fft2
	 * @return Cell[]
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell[] ifft2X(CCell fft1,CCell fft2) throws IllegalArgumentException
	{
		if(fft1 == null || fft2 == null)
			throw new NullPointerException();
		if(!fft1.hasSameDimensions(fft2))
			throw new IllegalArgumentException();

		try{

			CCell mfft = f1(fft1, fft2);
			CCell cimg = op.ifft2DComplex(mfft);

			Cell [] res = {cimg.accessRealPart(),cimg.accessImaginaryPart()};

			return res;
		}catch(Exception e){
			throw new BugEncounterException();
		}

	}

	private CCell[] f5(CCell fft) throws DimensionMismatchException, IllegalArgumentException
	{
		if(fft == null)
			throw new NullPointerException();

		try{

			float[][] real = fft.accessRealPart().accessCellBuffer();
			float[][] im = fft.accessImaginaryPart().accessCellBuffer();

			final int rc = fft.getRowCount();
			final int cc = fft.getColCount();

			CCell fft1 = new CCell(rc, cc);
			CCell fft2 = new CCell(rc, cc);

			float[][] real1 = fft1.accessRealPart().accessCellBuffer();
			float[][] im1 = fft1.accessImaginaryPart().accessCellBuffer();

			float[][] real2 = fft2.accessRealPart().accessCellBuffer();
			float[][] im2 = fft2.accessImaginaryPart().accessCellBuffer();

			final int N = rc;
			final int M = cc;
			final int Nh = N/2;
			final int Mh = M/2;

			real1[0][0] = real[0][0];
			real2[0][0] = im[0][0];
			im1[0][0] = 0;
			im2[0][0] = 0;                  

			for(int n=Nh; n > 0; n--) 
			{
				for(int m =Mh; m > 0;m--)
				{
					final int dn = N-n;
					final int dm = M-m;

					final float realnm = real[n][m];
					final float imnm = im[n][m];
					final float realdndm = real[dn][dm];
					final float imdndm = im[dn][dm];
					final float realndm = real[n][dm];
					final float realdnm = real[dn][m];
					final float imndm = im[n][dm];
					final float imdnm = im[dn][m];

					real1[n][m] = (realnm + realdndm)/2.0f;
					im1[n][m] = (imnm - imdndm)/2.0f;

					real1[dn][dm] = real1[n][m];
					im1[dn][dm] = - im1[n][m];

					real1[n][dm] = (realndm + realdnm)/2.0f;
					im1[n][dm] = (imndm - imdnm)/2.0f;

					real1[dn][m] = real1[n][dm];
					im1[dn][m] = - im1[n][dm];


					real2[n][m] = (imnm + imdndm)/2.0f;
					im2[n][m] = (realdndm - realnm)/2.0f;

					real2[dn][dm] = real2[n][m];
					im2[dn][dm] = - im2[n][m];

					real2[n][dm] = (imndm + imdnm)/2.0f;
					im2[n][dm] = (realdnm - realndm)/2.0f;

					real2[dn][m] = real2[n][dm];
					im2[dn][m] = - im2[n][dm];
				}
			}

			for(int n=Nh; n > 0; n--) 
			{
				final int dn = N-n;

				final float realn0 = real[n][0];
				final float realdn0 = real[dn][0];

				final float imn0 = im[n][0];
				final float imdn0 = im[dn][0];

				real1[n][0] = (realn0 + realdn0)/2.0f;
				im1[n][0] = (imn0 - imdn0)/2.0f;

				real2[n][0] = (imn0 + imdn0)/2.0f;
				im2[n][0] = (realdn0 - realn0)/2.0f;

				real1[dn][0] = real1[n][0];
				im1[dn][0] = -im1[n][0];

				real2[dn][0] = real2[n][0];
				im2[dn][0] = -im2[n][0];
			}

			for(int m=Mh; m > 0; m--) 
			{
				final int dm = M-m;

				final float real0m = real[0][m];
				final float real0dm = real[0][dm];

				final float im0m = im[0][m];
				final float im0dm = im[0][dm];

				real1[0][m] = (real0m + real0dm)/2.0f;
				im1[0][m] = (im0m - im0dm)/2.0f;

				real2[0][m] = (im0m + im0dm)/2.0f;
				im2[0][m] = (real0dm - real0m)/2.0f;

				real1[0][dm] = real1[0][m];
				im1[0][dm] = -im1[0][m];

				real2[0][dm] = real2[0][m];
				im2[0][dm] = -im2[0][m];
			}

			CCell[] res = {fft1,fft2};

			return res;
		}catch(Exception e){
			throw new BugEncounterException();
		}

	}

	private CCell f1(CCell fft1, CCell fft2) throws IllegalArgumentException {

		if(fft1 == null || fft2 == null)
			throw new NullPointerException();

		if(!fft1.hasSameDimensions(fft2))
			throw new IllegalArgumentException();

		try {
			final int N = fft1.getRowCount();
			final int Nh = N/2;
			final int M = fft2.getColCount();
			final int Mh = M/2;

			CCell fft = new CCell(N,M);

			float[][] real = fft.accessRealPart().accessCellBuffer();
			float[][] im = fft.accessImaginaryPart().accessCellBuffer();

			float[][] real1 = fft1.accessRealPart().accessCellBuffer();
			float[][] im1 = fft1.accessImaginaryPart().accessCellBuffer();

			float[][] real2 = fft2.accessRealPart().accessCellBuffer();
			float[][] im2 = fft2.accessImaginaryPart().accessCellBuffer();

			real[0][0] = real1[0][0];
			im[0][0] = real2[0][0];

			for (int n = Nh; n > 0; n--) 
			{
				for(int m = Mh; m > 0; m--)
				{
					final int dn = N-n;
					final int dm = M-m;

					real[n][m] = real1[n][m] + im2[dn][dm];
					real[dn][dm] = real1[dn][dm] + im2[n][m];
					real[dn][m] = real1[dn][m] + im2[n][dm];
					real[n][dm] = real1[n][dm] + im2[dn][m];

					im[n][m] = real2[n][m] + im1[n][m];
					im[dn][dm] = im1[dn][dm] + real2[dn][dm];
					im[dn][m] = im1[dn][m] + real2[dn][m];
					im[n][dm] = im1[n][dm] + real2[n][dm];
				}
			}

			for(int n=Nh; n > 0; n--)                    
			{
				final int dn = N-n;
				
				real[dn][0] = real1[dn][0] + im2[n][0];
				im[dn][0] = im1[dn][0] + real2[dn][0];

				real[n][0] = real1[n][0] + im2[dn][0];
				im[n][0] = im1[n][0] + real2[n][0];
			}

			for(int m=Mh; m > 0; m--)                    
			{
				final int dm = M-m;
				
				real[0][m] = real1[0][m] + im2[0][dm];
				im[0][m] = im1[0][m] + real2[0][m];

				real[0][dm] = real1[0][dm] + im2[0][m];
				im[0][dm] = im1[0][dm] + real2[0][dm];

			}


			return fft;

		} catch (Exception e) {
			throw new BugEncounterException();
		}
	}
}
