package org.JMathStudio.ImageToolkit.IntensityTools;

import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.CustomUIntPixelImage;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a Dither transform for dithering a gray scale image to an
 * lower depth gray scale image. The Dither transform is based on the
 * Floyd-Steinberg algorithm which is a kind of Error diffusion technique.
 * <p>
 * The error diffusion is the technique for distributing the quantisation error
 * generated at the current pixel position to the neighbouring pixels so as to
 * keep the average quantisation error close to zero. The error diffusion is
 * weighted by an error matrix.
 * <p>
 * Dithering is the process by which a gray scale image of higher depth (more
 * shades of gray) is is represented by a gray scale image of lower depth (less
 * shades of gray) by exploiting the spatial integration by human eye.
 * <p>
 * A gray scale images will be represented by an {@link AbstractUIntPixelImage}
 * with a given depth.
 * <pre>Usage:
 * AbstractUIntPixelImage img = UInt8PixelImage.importImage("path");//Import external image
 * as UInt PixelImage type.
 * 
 * DitherTransform dt = new DitherTransform();//Create an instance of DitherTransform.
 * 
 * AbstractUIntPixelImage result = dt.dither(img, k);//Apply dithering on input image for
 * low depth/gray-scale 'k'.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DitherTransform {

	/**
	 * This method will dither the given gray scale image as represented by the
	 * {@link AbstractUIntPixelImage} 'img' to a gray scale image with lower
	 * depth as defined by the argument 'depth' and return the dithered image as
	 * {@link AbstractUIntPixelImage}.
	 * <p>
	 * The argument 'depth' here specify the depth or bits per pixel for the
	 * resultant dithered image. The argument 'depth' should be less than the
	 * depth of the 'img' and should be more than '0' else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * For 'depth' of '1' this operation reduces to halftoning.
	 * <p>
	 * The resultant dithered image will be returned as a
	 * {@link AbstractUIntPixelImage}.
	 * 
	 * @param AbstractUIntPixel
	 *            img
	 * @param int depth
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage dither(AbstractUIntPixelImage img, int depth)
			throws IllegalArgumentException {
		if (depth < 1 || depth >= img.getDepth())
			throw new IllegalArgumentException();
		else {
			int height = img.getHeight();
			int width = img.getWidth();

			AbstractUIntPixelImage res = new CustomUIntPixelImage(depth,
					height, width);

			float oldMax = img.getMaxValidPixel();
			float newMax = res.getMaxValidPixel();

			// Buffer to store Error diffused to each pixel.
			float[][] errorBuffer = new float[height][width];// img.toCell().
																// getCellBuffer
																// ();

			// Scale to map image intensities to lower depth intensities.
			float fwd_scale = newMax / oldMax;
			// Scale to map lower depth intensities back to original image
			// depth.
			float bk_scale = oldMax / newMax;
			int temp1,temp2;
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					// Add diffused error for that pixel to its intensity.
					float pixel = img.getPixel(i, j) + errorBuffer[i][j];

					// Maintain the range for intensity.
					// Prevent over and under shoot of intensities by the
					// addition
					// of cumulative error.
					if (pixel > oldMax)
						pixel = oldMax;
					else if (pixel < 0)
						pixel = 0;

					// Map current intensity to the equivalent lower depth
					// intensity.
					int newPixel = Math.round(pixel * fwd_scale);

					// set the mapped intensity for that pixel.
					res.setPixel(newPixel, i, j);

					// Re scale the equivalent lower depth intensity to original
					// depth/range. The resultant intensity will not be equal to
					// the original due to quantisation loss introduced when
					// mapping
					// the same to lower depth. Measure this quantisation error
					// by
					// subtraction of re scaled intensity from original
					// intensity.
					float error = pixel - newPixel * bk_scale;

					// Propagate the error generated at current pixel to the
					// neighbouring pixels with weight
					// as set by the Floyd-Steinberg error diffusion matrix.
					temp1 = j+1;
					if (temp1 < width)
						errorBuffer[i][temp1] += error * 0.4375f;
					temp1 = i+1;
					temp2 = j-1;
					if (temp1< height && temp2 > 0)
						errorBuffer[temp1][temp2] += error * 0.1875f;
					if (temp1 < height)
						errorBuffer[temp1][j] += error * 0.3125f;
					temp2 = j+1;
					if (temp1 < height && temp2 < width)
						errorBuffer[temp1][temp2] += error * 0.0625;
				}
			}

			return res;
		}
	}

	/**
	 * This method will halftone the given gray scale image as represented by
	 * the {@link AbstractUIntPixelImage} 'img' to a binary image and return the
	 * same as {@link BinaryPixelImage}.
	 * <p>
	 * Halftone image is the special case of dithering with depth 1.
	 * <p>
	 * The argument 'T' here specify the normalised threshold for halftoning and
	 * should be in the range of [0 1] else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * Generally 'T' is set to 0.5. The normalised range of 'T' correspond to
	 * the full gray scale intensity range of the given UInt PixelImage.
	 * <p>
	 * The resultant binary image will be returned as a {@link BinaryPixelImage}.
	 * 
	 * @param AbstractUIntPixel
	 *            img
	 * @param float T
	 * @return BinaryPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage halftone(AbstractUIntPixelImage img, float T)
			throws IllegalArgumentException {
		// 'T' is the threshold for half toning should be within the range [0 1]
		// corresponding to the minimum and maximum gray scale range of the
		// given
		// UInt image.

		if (T < 0 || T > 1)
			throw new IllegalArgumentException();
		else {
			int height = img.getHeight();
			int width = img.getWidth();

			float maxPixel = img.getMaxValidPixel();

			float[][] buffer = new float[height][width];
			BinaryPixelImage res = new BinaryPixelImage(height, width);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) { // Initialised by normalise
													// pixel values in the range
													// of [0 1].
					buffer[i][j] = img.getPixel(i, j) / maxPixel;
				}
			}
			
			int temp1,temp2;
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					// Pixel value is equal to original pixel and sum of all the
					// diffused error to that pixel due to prior assignments.
					float pix = buffer[i][j];
					// Error for current pixel location.
					float error;

					if (pix >= T) {
						// Set to '1'
						res.setPixel(true, i, j);
						error = pix - 1;
					} else {
						// Set to '0'
						res.setPixel(false, i, j);
						error = pix;
					}

					// Propagate the error generated at current pixel to the
					// neighbouring pixels with weight
					// as set by the Floyd-Steinberg error diffusion matrix.
					temp1 = j+1;
					if (temp1 < width)
						buffer[i][temp1] += error * 0.4375f;
					temp1 = i+1;
					temp2 = j-1;
					if (temp1 < height && temp2 > 0)
						buffer[temp1][temp2] += error * 0.1875f;
					if (temp1 < height)
						buffer[temp1][j] += error * 0.3125f;
					temp2 = j+1;
					if (temp1 < height && temp2 < width)
						buffer[temp1][temp2] += error * 0.0625;

				}
			}

			return res;
		}
	}

}