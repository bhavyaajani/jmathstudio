package org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters;

import org.JMathStudio.DataStructure.Cell.Cell;

/**
 * This class define a Gradient diffusion filter. This filter is based on the 
 * Perona and Malik formulation for anisotropic filters.
 * <p>
 * Such a filter is based on the diffusion process which allows for locally
 * adaptive diffusion strengths; edges are selectively smoothed or enhanced
 * based on the evaluation of the diffusion function.
 * <p>
 * The diffusion or flow process is dependent on the gradient magnitude for a
 * given pixel and thus has an anisotropic behaviour.
 * <p>
 * Function e^-(|x/k|^2), is modelled as diffusion control function for given gradient 
 * 'x' and flow constant 'k'.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * float flowConstant = 10;//Parameters for Gradient diffusion filter.
 * int iteration = 3;
 * float timestep = 0.15f;
 * 
 * GradientDiffusionFilter gdf = new GradientDiffusionFilter(flowConstant,
 * iteration,timestep);//Create an instance of GradientDiffusionFilter with given
 * parameters.
 * 
 * Cell result = gdf.filter(img);//Apply given diffusion filter on input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class GradientDiffusionFilter {

	// Default Flow constant. Should not be '0'.
	private float i0;
	private int i5;
	private float i3;

	/**
	 * This will create an gradient diffusion filter based
	 * on the parameters as provided.
	 * <p>
	 * The argument 'flowConstant' specify the flow constant or diffusion
	 * constant. The argument 'flowConstant' should be more than '0' else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'iteration' specify the number of recursive iterations of
	 * this filter. For each filtering operation this many times filtering will
	 * be recursively applied on the input image. The argument 'iteration'
	 * should be more than '0' else this method will throw an IllegalArgument
	 * Exception.
	 * <p>
	 * The argument 'timestep' specify the time interval or time step between
	 * two successive iterations. The maximum theoretical allowable value for
	 * the argument 'timestep' for a 2D image with 8 pixel neighbourhood is 1/7.
	 * The argument 'timestep' should be more than '0' else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * For each pixel location per iteration this filter consider the flow
	 * contribution from the 8 nearest pixels. It further scale the flow coming
	 * from the diagonal pixels.
	 * 
	 * @param float flowConstant
	 * @param int iteration
	 * @param float timestep
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public GradientDiffusionFilter(float flowConstant, int iteration,
			float timestep) throws IllegalArgumentException {
		// check filter parameters
		if (flowConstant <= 0 || iteration < 1 || timestep <= 0)
			throw new IllegalArgumentException();
		else {
			this.i0 = flowConstant;
			this.i5 = iteration;
			this.i3 = timestep;
		}

	}

	/**
	 * This method will apply the given gradient  diffusion filter on the discrete 
	 * image as represented by the {@link Cell} 'image' and return the resultant filtered image
	 * as a Cell.
	 * 
	 * @param Cell
	 *            image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell filter(Cell image) {
		// Current image produced for given iteration N. Initially new blank
		// image.
		Cell current = new Cell(image.getRowCount(), image.getColCount());
		// Image from iteration N-1. Initially original image.
		Cell initial = image;

		// Scale to scale the diagonal length for diagonal flow parameters.
		float scale = (float) Math.sqrt(2);

		int h = image.getRowCount();
		int w = image.getColCount();

		// For each iteration do....
		for (int k = 0; k < i5; k++) {
			// For all pixel locations in the image....
			for (int i = 0; i < current.getRowCount(); i++) {
				for (int j = 0; j < current.getColCount(); j++) {
					// Eight diffusion flow coming to the given pixel from 8
					// nearest pixels.
					// Note: Flow can be negative or positive based on the
					// gradient magnitude
					// sign. ie. outward or inward.
					float Fn = 0, Fs = 0, Fe = 0, Fw = 0, Fne = 0, Fnw = 0, Fse = 0, Fsw = 0;

					// Current pixel in question for which diffusion
					// contribution from nearest
					// neighbour is to be computed.
					float pixel = initial.getElement(i, j);

					// Compute 8 gradient with each 8 nearest pixel.
					// Critical: difference should be, Neighbour - pixel, only.
					int y1 = i - 1;
					int y2 = i + 1;
					int x1 = j - 1;
					int x2 = j + 1;

					if (y1 >= 0)
						Fs = initial.getElement(y1, j) - pixel;
					if (y2 < h)
						Fn = initial.getElement(y2, j) - pixel;
					if (x1 >= 0)
						Fw = initial.getElement(i, x1) - pixel;
					if (x2 < w)
						Fe = initial.getElement(i, x2) - pixel;

					if (y1 >= 0 && x1 >= 0)
						Fsw = initial.getElement(y1, x1) - pixel;
					if (y1 >= 0 && x2 < w)
						Fse = initial.getElement(y1, x2) - pixel;
					if (y2 < h && x1 >= 0)
						Fnw = initial.getElement(y2, x1) - pixel;
					if (y2 < h && x2 < w)
						Fne = initial.getElement(y2, x2) - pixel;

					// Calculate Flow = gradient * Flow or Diffusion control function.
					// Flow or Diffusion function in turn depends upon the absolute
					// gradient magnitude.
					Fs = Fs * f5(Fs);
					Fn = Fn * f5(Fn);
					Fw = Fw * f5(Fw);
					Fe = Fe * f5(Fe);

					// Flow is scaled down by root(2) for 4 diagonal components
					// as length is larger.
					Fsw = Fsw * f5(Fsw) / scale;
					Fse = Fse * f5(Fse) / scale;
					Fnw = Fnw * f5(Fnw) / scale;
					Fne = Fne * f5(Fne) / scale;

					// Add the flow contributed (negative and positive) for a
					// given time interval or
					// time step by multiplying the total Flow by time step.
					// Add the resultant total flow to the current pixel value.
					float res = pixel + i3
							* (Fn + Fs + Fe + Fw + Fnw + Fne + Fsw + Fse);

					// populate intermediate image, to be used by next
					// iteration.
					current.setElement(res, i, j);

				}
			}
			// This iteration to be N-1 iteration for next iteration.
			initial = current;
		}

		return current;
	}

	/**
	 * This method will return the Flow constant parameter of the given
	 * gradient  diffusion filter.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getFlowConstant() {
		return this.i0;
	}

	/**
	 * This method will return the Iteration parameter of the given gradient 
	 * diffusion filter.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getIteration() {
		return this.i5;
	}

	/**
	 * This method will return the Time Step parameter of the given gradient 
	 * diffusion filter.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getTimeStep() {
		return this.i3;
	}

	/**
	 * This method will reset the Flow Constant parameter of the given
	 * gradient  diffusion filter to the argument 'flowconstant' and
	 * reconfigure the given filter.
	 * <p>
	 * The argument 'flowconstant' should be greater than 0 else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param float flowconstant
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void reSetFlowConstant(float flowconstant)
			throws IllegalArgumentException {
		if (flowconstant <= 0)
			throw new IllegalArgumentException();
		else
			this.i0 = flowconstant;
	}

	/**
	 * This method will reset the Iteration parameter of the given gradient 
	 * diffusion filter to the argument 'iteration' and reconfigure the
	 * given filter.
	 * <p>
	 * The argument 'iteration' should be greater than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param int iteration
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void reSetIteration(int iteration) throws IllegalArgumentException {
		if (iteration < 1)
			throw new IllegalArgumentException();
		else
			this.i5 = iteration;
	}

	/**
	 * This method will reset the Time Step parameter of the given gradient 
	 * diffusion filter to the argument 'timestep' and reconfigure the
	 * given filter.
	 * <p>
	 * The argument 'timestep' should be greater than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param float timestep
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void reSetTimeStep(float timestep) throws IllegalArgumentException {
		if (timestep <= 0)
			throw new IllegalArgumentException();
		else
			this.i3 = timestep;
	}

	private float f5(float gradient) {
		// Function depends only upon the absolute of gradient.
		// As square is done, no absolute is applied on 'gradient' here.
		double param = gradient / i0;
		return (float) Math.exp(-(param * param));
	}

}
