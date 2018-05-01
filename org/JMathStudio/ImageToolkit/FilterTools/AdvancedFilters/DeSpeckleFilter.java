package org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Structure.Neighbor;
import org.JMathStudio.DataStructure.Structure.Neighborhood;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a De-speckle filter to remove speckle noise from an image.
 * <p>This is an adaptive iterative filter. The filter estimate the mean (m) and standard
 * deviation (s) of the pixel intensities in the neighbourhood of a pixel per iteration. 
 * Now based upon some cutoff (c), filter limits the pixel intensity of the pixel within the range,
 * [m - c*s, m + c*s].
 * <p>The argument 'cutoff' (c) define an outlier pixel (corrupted with speckle noise) in the
 * units of standard deviation away from the mean intensity. Thus with cutoff '1', pixel with
 * intensity +/- 1 standard deviation away from the mean intensity (of the neighbourhood) is taken
 * as an outlier.
 * <p>If the pixel is identified as an outlier, its intensity value is limited to the either of
 * the nearest value, m - c*s or m + c*s.
 * <p>The filter preserves the edges in the image.
 * <p>If 'cutoff' is set to '0', this filter behaves as an iterative mean filter.
 * <p>The best estimate for cutoff 'c' is between 0.5 and 1.5 and iterations should be around 3.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * float cutoff = 1;//Parameters for required De-speckle filter.
 * int iteration = 3;
 * 
 * DeSpeckleFilter despeckle = new DeSpeckleFilter(cutoff,iteration);//Create an instance of
 * DeSpecfleFilter with given parameters.
 * 
 * Cell result = despeckle.filter(img);//Apply De-speckle filtering on the input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DeSpeckleFilter {

	private int iteration;
	private float cutoff;
	private Neighborhood nbr;

	/**
	 * This will create a De-speckle filter with cutoff and number of iterations as specified by
	 * the arguments 'cutoff' and 'iteration' respectively.
	 * <p>The argument 'cutoff' should be within the range [0,2] and argument 'iteration' should
	 * be more than '0' else this method will throw an IllegalArgument Exception.
	 * <p>The default {@link Neighborhood} for the filter is 8 connected neighborhood.
	 * @param float cutoff
	 * @param int iteration
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public  DeSpeckleFilter(float cutoff,int iteration) throws IllegalArgumentException{
		if(!f2(cutoff))
			throw new IllegalArgumentException();
		else
			this.cutoff = cutoff;

		if(!f6(iteration))
			throw new IllegalArgumentException();
		else
			this.iteration = iteration;

		this.nbr = Neighborhood.eightConnectedNeighborhood();		
	}

	/**
	 * This method will apply the given De-speckle filter on the discrete real image as represented
	 * by {@link Cell} 'img' and return the resultant de-speckled image as Cell.
	 * @param Cell img
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell filter(Cell img){

		int rc = img.getRowCount();
		int cc = img.getColCount();

		Cell res = img.clone();
		
		Cell[] stat;
		
		float pixel;
		float mean;
		float std;
		float deviation;
		float ut,lt;

		for(int k=0;k<iteration;k++)
		{
			stat = f4(res, nbr); 
			
			for(int i=0;i<rc;i++)
			{
				for(int j=0;j<cc;j++)
				{
					pixel = res.getElement(i, j);
					mean = stat[0].getElement(i, j);
					std = stat[1].getElement(i, j);
					deviation = cutoff*std;
					ut = mean + deviation;
					lt = mean - deviation;

					if(pixel > ut)
						res.setElement(ut, i, j);
					else if(pixel < lt)
						res.setElement(lt, i, j);
				}
			}
		}

		return res;
	}
	
	/**
	 * This method will return the number of iterations set for the given De-speckle filter.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getIterations(){
		return this.iteration;
	}
	
	/**
	 * This method will reset the number of iterations for this De-speckle filter as specified
	 * by the argument 'iteration'.
	 * <p>The argument 'iteration' should be more than '0' else this method will throw an 
	 * IllegalArgument Exception.
	 * @param int iteration
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setIterations(int iteration) throws IllegalArgumentException{
		if(!f6(iteration))
			throw new IllegalArgumentException();
		else
			this.iteration = iteration;
	}
	
	/**
	 * This method will return the cutoff set for the given De-speckle filter.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getCutoff(){
		return this.cutoff;
	}
	
	/**
	 * This method will reset the cutoff for this De-speckle filter as specified by the argument
	 * 'cutoff'.
	 * <p>The argument 'cutoff' should be in the range [0,2] else this method will throw an 
	 * IllegalArgument Exception.
	 * @param float cutoff
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setCutoff(float cutoff) throws IllegalArgumentException{
		if(!f2(cutoff))
			throw new IllegalArgumentException();
		else
			this.cutoff = cutoff;
	}
	
	/**
	 * This method will reset the {@link Neighborhood} define for this De-speckle filter with the
	 * Neighborhood as specified by the argument 'nbr'.
	 * @param Neighborhood nbr
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignNeighborhood(Neighborhood nbr){
		this.nbr = nbr;
	}
	
	private boolean f6(int iteration){
		return !(iteration<1);
	}
	
	private boolean f2(float cutoff){
		return !(cutoff <0 || cutoff >2);
	}
	
	private Cell[] f4(Cell cell, Neighborhood neighborhood) {

		Cell[] result = new Cell[2];
		int h = cell.getRowCount();
		int w = cell.getColCount();
		
		result[0] = new Cell(h,w);
		result[1] = new Cell(h,w);

		Neighbor[] kele = neighborhood.accessAllNeighbors();

		//If Neighborhood is empty i.e all the elements are false do nothing.
		if(kele == null)
			return result;

		//Number of Neighborhood elements in the Neighborhood.
		int kd = kele.length;

		int Y,X;
		float mean;
		for(int i = 0; i < h; i++)
		{
			for(int j = 0; j < w; j++) 
			{
				float[] elements = new float[kd];
				int index=0;
				mean=0;

				for(int k=0;k<kd;k++)
				{
					Y = i + kele[k].getY();
					X = j + kele[k].getX();

					if(Y >= 0 && Y < h && X >= 0 && X < w) {
						elements[index++] = cell.getElement(Y, X);
						mean+=cell.getElement(Y,X);
					}
				}

				if(index != 0){
					mean = mean/index;
					float std = 0;
					float tmp;

					for (int l = 0; l < index; l++) {
						tmp = (elements[l] - mean);
						std += tmp * tmp;
					}
					result[0].setElement(mean, i, j);
					result[1].setElement((float) Math.sqrt(std / index), i, j);
				}
			}
		}

		return result;

	}
}
