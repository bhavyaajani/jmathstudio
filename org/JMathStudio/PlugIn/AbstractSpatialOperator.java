package org.JMathStudio.PlugIn;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Generic.Pixel;
import org.JMathStudio.DataStructure.Generic.PixelList;
import org.JMathStudio.DataStructure.Structure.Neighbor;
import org.JMathStudio.DataStructure.Structure.Neighborhood;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.EmptyNeighborhoodException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define an abstract Spatial operator which operate on the neighbourhood of each individual 
 * pixel of the image as represented by {@link Cell}.
 * <p>The class define an abstract method 'operator' which state the definition for the required
 * spatial operator. 
 * <p>The spatial operator define here shall be a function which takes a set of neighbourhood pixels and return
 * a real scalar. The specified operator shall be applied for each pixel of the image.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public abstract class AbstractSpatialOperator {

	/**
	 * This method will evaluate the given Spatial operator over the pixels of the image as represented
	 * by {@link Cell} 'image' and return the result as a {@link Cell}.
	 * <p>The argument {@link Neighborhood} 'nbr' define the neighbourhood to be employed for the spatial
	 * operation. If the neighbourhood as define by argument 'nbr' is empty this method will throw an
	 * EmptyNeighborhood Exception.
	 * <p>The definition of the spatial operator is as stated by the method {@link #operator(PixelList, float)}.
	 * For each pixel in the image, the operator shall take in a list of neighbourhood image pixels ( {@link PixelList})
	 * based upon the neighbourhood define by the argument 'nbr' and compute the output value for the current image
	 * pixel.
	 * <p>If the neighbourhood pixel identified for an image pixel falls outside the image; it is discarded.   
	 * <p>The return Cell will contain the output of the spatial operator for the corresponding pixels in the
	 * image.
	 * @param Cell image
	 * @param Neighborhood nbr
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final Cell evaluateOverCell(Cell image, Neighborhood nbr) throws EmptyNeighborhoodException{
		
		final Neighbor[] nbrList = nbr.accessAllNeighbors();
		
		if(nbrList == null)
			throw new EmptyNeighborhoodException();
		
		int h = image.getRowCount();
		int w = image.getColCount();
		
		Cell result = new Cell(h,w);
		
		int l = nbrList.length;
		
		PixelList list;
		try {
			list = new PixelList(l);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
		
		int Y,X;
		int y,x;
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				for(int k=0;k<l;k++)
				{
					y = nbrList[k].getY();
					x = nbrList[k].getX();
					
					Y = i + y;
					X = j + x;

					if(Y >= 0 && Y < h && X >= 0 && X < w) {
						list.add(new Pixel(image.getElement(Y, X),y,x));
					}
				}
				
				result.setElement(operator(list,image.getElement(i, j)), i, j);
				list.clear();
			}
		}
		
		return result;
	}
	
	/**
	 * This abstract method gives definition for the spatial operator. For each pixel image, spatial operator takes in a
	 * set of neighbourhood image pixels as given by the {@link PixelList} 'list' and compute the output value.
	 * <p>The argument 'currentPixel' gives the value of the current image pixel for which operation is carried out. 
	 * The current pixel is also the central pixel within the neighbourhood and each {@link Pixel} in the PixelList
	 * 'list' has spatial coordinates relative to this central pixel.
	 * <p>PixelList 'list' may be empty if no valid neighbour pixels exist for an image pixel or if they fall outside
	 * the image. Ensure to account for the empty PixelList.
	 * @param PixelList list
	 * @param float currentPixel
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float operator(PixelList list, float currentPixel);
}
