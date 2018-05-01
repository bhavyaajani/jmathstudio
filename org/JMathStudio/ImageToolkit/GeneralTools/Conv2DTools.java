package org.JMathStudio.ImageToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.DataStructure.Cell.CCellMath;
import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.ImageToolkit.TransformTools.FourierSet.FFT2D;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;

/**
 * This class define various 2D Convolution operations on a discrete real image.
 * <p>A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * Cell kernel = KernelFactory.gaussianKernel(7, 7, 2);//Select appropriate convolution
 * kernel.
 * 
 * Conv2DTools conv = new Conv2DTools();//Create an instance of Conv2DTools.
 * 
 * Cell conv_same = conv.linearConvSame(img, kernel);//Apply linear 2D convolution on input
 * image with given kernel, preserving dimensions of the resultant image.
 * 
 * Cell conv_full = conv.linearConvFull(img, kernel);//Apply full linear 2D convolution on
 * input image with given kernel.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Conv2DTools {

	private FFT2D fft;
	private CellTools ctools;
	private MatrixTools mtools;

	public Conv2DTools()
	{
		fft = new FFT2D();
		ctools = new CellTools();
		mtools = new MatrixTools();
	}

	/**
	 * This method will compute the 2D Linear Convolution of the discrete real image as
	 * represented by the {@link Cell} 'image' with the convolution kernel as represented
	 * by the Cell 'kernel' and return that central portion of the convolution result
	 * as Cell which has dimension similar to that of the original image.
	 * <p>This method will choose either of the linear convolution algorithm, {@link #linearConvSameWithFFT(Cell, Cell)}
	 * or {@link #linearConvSameWithoutFFT(Cell, Cell)}, to compute the linear convolution of the
	 * given image with the given kernel. The choice will depend upon optimising the time taken to
	 * compute the convolution.
	 * <p>User can directly use the available algorithms if not satisfied with the optimisation 
	 * provided by this method.
	 * 
	 * @param Cell
	 *            image
	 * @param Cell
	 *            kernel
	 * @return Cell
	 * @see #linearConvSameWithFFT(Cell, Cell)
	 * @see #linearConvSameWithoutFFT(Cell, Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell linearConvSame(Cell image, Cell kernel) 
	{
		int kernelDimension = kernel.getRowCount()*kernel.getColCount();

		if(kernelDimension <15*15)
			return linearConvSameWithoutFFT(image, kernel);
		else	
			return linearConvSameWithFFT(image, kernel);

	}

	/**
	 * This method will compute the 2D Linear Convolution of the discrete real image as
	 * represented by the {@link Cell} 'image' with the convolution kernel as represented
	 * by the Cell 'kernel' and return the result as a Cell.
	 * <p>This method will choose either of the linear convolution algorithm, {@link #linearConvFullWithFFT(Cell, Cell)}
	 * or {@link #linearConvFullWithoutFFT(Cell, Cell)}, to compute the linear convolution of the
	 * given image with the given kernel. The choice will depend upon optimising the time taken to
	 * compute the convolution.
	 * <p>User can directly use the available algorithms if not satisfied with the optimisation 
	 * provided by this method.	 * 
	 * @param Cell
	 *            image
	 * @param Cell
	 *            kernel
	 * @return Cell
	 * @see #linearConvFullWithFFT(Cell, Cell)
	 * @see #linearConvFullWithoutFFT(Cell, Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell linearConvFull(Cell cell, Cell kernel) 
	{
		int kernelDimension = kernel.getRowCount()*kernel.getColCount();

		if(kernelDimension <15*15)
			return linearConvFullWithoutFFT(cell, kernel);
		else	
			return linearConvFullWithFFT(cell, kernel);

	}

	/**
	 * This method will perform a Linear Convolution of the discrete real image as
	 * represented by the {@link Cell} 'image' with the discrete convolution kernel as represented 
	 * by the Cell 'kernel' and return the convolution result as a {@link Cell}.
	 * <p>This method make use of 2D FFT to compute the linear convolution in frequency domain. Following are the steps
	 * involve,
	 * <p><i>Resizing both the image and kernel to appropriate dimension by padding the trailing zeroes. 
	 * <p>2D FFT of both the resized image and the kernel and subsequent multiplication of the both.
	 * <p>Inverse 2D FFT of the resultant multiplied FFT gives desired linear convolution result.
	 * </i>
	 * <p>This method for computing linear convolution using 2D FFT will be optimised when the kernel dimension
	 * is medium to large. 
	 * <p>However it is left to user to decide when to employ the given method for computing
	 * linear convolution.
	 * <p>By default make use of {@link #linearConvFull(Cell, Cell)} method which will select either
	 * of the appropriate method for calculating the linear convolution.
	 * @param Cell
	 *            image
	 * @param Cell
	 *            kernel
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell linearConvFullWithFFT(Cell image, Cell kernel) 
	{
		int height = image.getRowCount() + kernel.getRowCount() - 1;
		int width = image.getColCount() + kernel.getColCount() - 1;

		Cell img, imp;
		try {
			img = ctools.resize(image, height, width);
			imp = ctools.resize(kernel, height, width);

		} catch (IllegalArgumentException e1) {
			throw new BugEncounterException();
		}

		CCell imgfft = fft.fft2D(img);
		CCell impfft = fft.fft2D(imp);

		try {
			imgfft = CCellMath.dotProduct(imgfft, impfft);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

		return fft.ifft2D(imgfft);
	}

	/**
	 * This method will perform a Linear Convolution of the discrete real image as
	 * represented by the {@link Cell} 'image' with the discrete convolution kernel as represented 
	 * by the Cell 'kernel' and return the convolution result as a {@link Cell}.
	 * <p>This method compute the linear convolution in spatial domain by directly taking the inner product between
	 * the image and linearly shifted kernel.
	 * <p>This method does not make use of 2D FFT to compute linear convolution so will be optimised
	 * for the small dimension kernel or if both the image and the kernel are of small dimensions.
	 * <p>Do not use this method if 'kernel' is of large dimensions. 
	 * <p>However it is left to user to decide when to employ the given method for computing
	 * linear convolution.
	 * <p>By default make use of {@link #linearConvFull(Cell, Cell)} method which will select either
	 * of the appropriate method for calculating the linear convolution.
	 * @param Cell
	 *            image
	 * @param Cell
	 *            kernel
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell linearConvFullWithoutFFT(Cell image, Cell kernel)
	{
		Cell result = new Cell(image.getRowCount()+kernel.getRowCount()-1,image.getColCount()+kernel.getColCount()-1);

		Cell mask = mtools.flipRows(mtools.flipColumns(kernel));

		int krow_1 = mask.getRowCount()-1;
		int kcol_1 = mask.getColCount()-1;
		int x,y;
		int X,Y;

		for(int i=0;i<result.getRowCount();i++)
		{
			y = i - krow_1;
			for(int j=0;j<result.getColCount();j++)
			{
				float tmp =0;
				x = j - kcol_1;
				for(int k=0;k<mask.getRowCount();k++)
				{
					for(int l=0;l<mask.getColCount();l++)
					{
						Y = k+y;
						X = l+x;

						if(Y>=0 && Y <image.getRowCount() && X>=0 && X<image.getColCount())
						{
							float ktmp = mask.getElement(k,l);
							float itmp = image.getElement(Y,X);

							tmp = tmp + ktmp*itmp;
						}
					}

				}
				result.setElement(tmp,i,j);
			}
		}

		return result;
	}

	/**
	 * This method will perform a Linear Convolution of the discrete real image as
	 * represented by the {@link Cell} 'image' with the discrete convolution kernel as represented 
	 * by the Cell 'kernel' and return that central portion of the convolution result
	 * as {@link Cell} which has dimension similar to that of the original image. 
	 * <p>This method make use of 2D FFT to compute the linear convolution in frequency domain. Following are the steps
	 * involve,
	 * <p><i>Resizing both the image and kernel to appropriate dimension by padding the trailing zeroes. 
	 * <p>2D FFT of both the resized image and the kernel and subsequent multiplication of the both.
	 * <p>Inverse 2D FFT of the resultant multiplied FFT gives linear convolution result.
	 * <p>Selecting appropriate central portion of the convolution result.
	 * </i>
	 * <p>This method for computing linear convolution using 2D FFT will be optimised when the kernel dimension
	 * is medium to large. 
	 * <p>However it is left to user to decide when to employ the given method for computing
	 * linear convolution.
	 * <p>By default make use of {@link #linearConvSame(Cell, Cell)} method which will select either
	 * of the appropriate method for calculating the linear convolution.
	 * @param Cell
	 *            image
	 * @param Cell
	 *            kernel
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell linearConvSameWithFFT(Cell image, Cell kernel) 
	{
		Cell result = linearConvFullWithFFT(image, kernel);

		int height_shift = (kernel.getRowCount()-1) / 2;
		int width_shift = (kernel.getColCount()-1) / 2;

		try {
			return ctools.subCell(result, height_shift, image
					.getRowCount(), width_shift, image.getColCount());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}
	/**
	 * This method will perform a Linear Convolution of the discrete real image as
	 * represented by the {@link Cell} 'image' with the discrete convolution kernel as represented 
	 * by the Cell 'kernel' and return that central portion of the convolution result
	 * as {@link Cell} which has dimension similar to that of the original image. 
	 * <p>This method compute the linear convolution in spatial domain by directly taking the inner product between
	 * the image and linearly shifted kernel.
	 * <p>This method does not make use of 2D FFT to compute linear convolution so will be optimised
	 * for the small dimension kernel or if both the image and the kernel are of small dimensions.
	 * <p>Do not use this method if 'kernel' is of large dimensions. 
	 * <p>However it is left to user to decide when to employ the given method for computing
	 * linear convolution.
	 * <p>By default make use of {@link #linearConvSame(Cell, Cell)} method which will select either
	 * of the appropriate method for calculating the linear convolution.
	 * @param Cell
	 *            image
	 * @param Cell
	 *            kernel
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell linearConvSameWithoutFFT(Cell image,Cell kernel)
	{

		Cell result = new Cell(image.getRowCount(),image.getColCount());
		Cell mask = mtools.flipRows(mtools.flipColumns(kernel));

		int Y_shift = (mask.getRowCount()-1)/2;
		int X_shift = (mask.getColCount()-1)/2;

		int krow_1 = mask.getRowCount()-1;
		int kcol_1 = mask.getColCount()-1;
		int x,y,X,Y;

		for(int i=0;i<result.getRowCount();i++)
		{
			y = i-krow_1+Y_shift;
			for(int j=0;j<result.getColCount();j++)
			{
				float tmp =0;
				x = j-kcol_1+X_shift;
				for(int k=0;k<mask.getRowCount();k++)
				{
					for(int l=0;l<mask.getColCount();l++)
					{
						Y = k+y;
						X = l+x;

						if(Y>=0 && Y <image.getRowCount() && X>=0 && X<image.getColCount())
						{
							float ktmp = mask.getElement(k,l);
							float itmp = image.getElement(Y,X);

							tmp = tmp + ktmp*itmp;//mask[k][l]*image[X][Y];
						}
					}

				}
				result.setElement(tmp,i,j);
			}
		}

		return result;
	}
}

//	public Cell linearConv_Same_WithoutFFT_withPadding(Cell image,Cell kernel)
//	{
//			Cell result = new Cell(image.getRowCount(),image.getColCount());
//			Cell mask = mtools.flipRows(mtools.flipColumns(kernel));
//
//			int Y_shift = (mask.getRowCount()-1)/2;
//			int X_shift = (mask.getColCount()-1)/2;
//			
//			int krow_1 = mask.getRowCount()-1;
//			int kcol_1 = mask.getColCount()-1;
//			int x,y,X,Y;
//			
//			for(int i=0;i<result.getRowCount();i++)
//			{
//				y = i-krow_1+Y_shift;
//				for(int j=0;j<result.getColCount();j++)
//				{
//					float tmp =0;
//					x = j-kcol_1+X_shift;
//					for(int k=0;k<mask.getRowCount();k++)
//					{
//						for(int l=0;l<mask.getColCount();l++)
//						{
//							Y = k+y;
//							X = l+x;
//
//							float ktmp = mask.getElementWithPadding(k,l);
//							float itmp = image.getElementWithPadding(Y,X);
//								
//							tmp = tmp + ktmp*itmp;//mask[k][l]*image[X][Y];
//						}
//
//					}
//					result.setElement(tmp,i,j);
//				}
//			}
//
//			return result;
//		}
//	
//	public Cell linearConv_Same_WithFFT_withPadding(Cell image, Cell kernel) throws IllegalArgumentException 
//	{
//		int ih = image.getRowCount();
//		int kh = kernel.getRowCount();
//		int iw = image.getColCount();
//		int kw = kernel.getColCount();
//		
//		int h,w;
//		int height_shift=0;
//		int width_shift=0;
//		
//		if(ih < kh){
//			h = kh;
//			height_shift = (kh-ih)/2;
//		}
//		else{
//			h = ih;
//		}
//		
//		if(iw < kw){
//			w = kw;
//			width_shift = (kw-iw)/2;
//		}
//		else
//			w = iw;
//		
//		Cell img = ctools.resize(image, h, w);
//		Cell imp = ctools.resize(kernel, h, w);
//		
//		CCell imgfft = fft.fft2D(img);
//		CCell impfft = fft.fft2D(imp);
//		
//		try {
//			imgfft = cctools.dotProduct(imgfft, impfft);
//		} catch (DimensionMismatchException e1) {
//			throw new BugEncounterException();
//		}
//		
//		Cell result = fft.ifft2D(imgfft);
//
//		try {
//			return ctools.subCell(result, height_shift, image
//					.getRowCount(), width_shift, image.getColCount());
//		} catch (IllegalArgumentException e) {
//			throw new BugEncounterException();
//		}
//	}
//	
//	public Cell linearConv_Full_WithFFT_withPadding(Cell image, Cell kernel) 
//	{
//		int height = image.getRowCount() + kernel.getRowCount() - 1;
//		int width = image.getColCount() + kernel.getColCount() - 1;
//
//		Cell img, imp;
//		try {
//			img = ctools.resize(image, height, width);
//			imp = ctools.resize(kernel, height, width);
//
//		} catch (IllegalArgumentException e1) {
//			throw new BugEncounterException();
//		}
//
//		CCell imgfft = fft.fft2D(img);
//		CCell impfft = fft.fft2D(imp);
//
//		try {
//			imgfft = cctools.dotProduct(imgfft, impfft);
//		} catch (DimensionMismatchException e) {
//			throw new BugEncounterException();
//		}
//
//		return fft.ifft2D(imgfft);
//	}
//	
//	public Cell linearConv_Full_WithoutFFT_withPadding(Cell image, Cell kernel)
//	{
//			Cell result = new Cell(image.getRowCount()+kernel.getRowCount()-1,image.getColCount()+kernel.getColCount()-1);
//			
//			Cell mask = mtools.flipRows(mtools.flipColumns(kernel));
//
//			int krow_1 = mask.getRowCount()-1;
//			int kcol_1 = mask.getColCount()-1;
//			int x,y;
//			int X,Y;
//			
//			for(int i=0;i<result.getRowCount();i++)
//			{
//				y = i - krow_1;
//				for(int j=0;j<result.getColCount();j++)
//				{
//					float tmp =0;
//					x = j - kcol_1;
//					for(int k=0;k<mask.getRowCount();k++)
//					{
//						for(int l=0;l<mask.getColCount();l++)
//						{
//							Y = k+y;
//							X = l+x;
//
//							float ktmp = mask.getElementWithPadding(k,l);
//							float itmp = image.getElementWithPadding(Y,X);
//								
//							tmp = tmp + ktmp*itmp;
//						}
//
//					}
//					result.setElement(tmp,i,j);
//				}
//			}
//
//			return result;
//	}
