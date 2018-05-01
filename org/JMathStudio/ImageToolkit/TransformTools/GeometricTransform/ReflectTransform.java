package org.JMathStudio.ImageToolkit.TransformTools.GeometricTransform;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various Reflection operation on a discrete real image.
 * <p>Reflection is a type of geometric transform in which a discrete real image
 * is symmetrically reflected along a specified reflection axis or a point.
 * <p>All the reflection operation define in this class follow the geometry where origin
 * is at the centre of the image and 'X' and 'Y' axis align with the image width and
 * height respectively.
 * <p>All the reflected images will be return as a {@link Cell}.
 * <p>A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ReflectTransform rt = new ReflectTransform();//Create an instance of ReflectTransform.
 * 
 * Cell reflect = rt.reflectHorizontally(img, y);//Reflect image along horizontal axis
 * with Y coordinate at 'y'.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ReflectTransform 
{
	/**
	 * This method will reflect the discrete real image as represented by the {@link Cell} 'cell' along
	 * the vertical axis passing through 'X' position as specified by the argument 'x'. Thus the specified
	 * reflection axis will have a line equation of, X=x.
	 * <p>The specified reflection axis, here a vertical axis, should be passing through the image with image
	 * centred on the origin. Thus the argument 'x' should be within the centred 'X' bound of this image that
	 * is [-Lx Lx], where Lx = (Width of the image -1)/2.
	 * <p>As the discrete real image is represented as a {@link Cell} and as per the default notation the columns
	 * of the Cell define the width of the image, thus the argument 'x' here should be within the bound of
	 * [-Lx Lx], where Lx = (column count of cell -1)/2. Else this method will throw an IllegalArgument Exception.
	 * <p>Reflection axis located at x=0, will give a reflection of the image around it central vertical axis
	 * thus this special reflection will be a vertical flip of the image.
	 * <p>The reflected image will be return as a {@link Cell} covering the entire reflected image support.
	 * 
	 * @param Cell cell
	 * @param float x
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell reflectVertically(Cell cell,float x) throws IllegalArgumentException
	{
		int L = cell.getColCount()-1;
		float cX = L/2.0f;
		
		if(x<-cX || x>cX)
			throw new IllegalArgumentException();
		
		float rMaxX = 2*x+cX;
		float rMinX = 2*x - cX;
		
		float minX = (rMinX <-cX)?rMinX:-cX;
		float maxX = (rMaxX >cX)?rMaxX:cX;
		
		int XkernelLength = (int) (maxX - minX +1);
		
		Cell result = new Cell(cell.getRowCount(),XkernelLength);
		
		for(int j=0;j<cell.getColCount();j++)
		{
			float jR = rMaxX - j -minX;//2*x - (j-cX);coz 2*x +cX = rMaxX
									   //jR = jR - minX; combined this step.

			for(int i=0;i<cell.getRowCount();i++)
			{
				result.setElement(cell.getElement(i,j), i, (int)jR);
			}
		}
		
		return result;
	}
	
	/**
	 * This method will reflect the discrete real image as represented by the {@link Cell} 'cell' along
	 * the horizontal axis passing through 'Y' position as specified by the argument 'y'. Thus the specified
	 * reflection axis will have a line equation of, Y=y.
	 * <p>The specified reflection axis, here a horizontal axis, should be passing through the image with image
	 * centred on the origin. Thus the argument 'y' should be within the centred 'Y' bound of this image that
	 * is [-Ly Ly], where Ly = (Height of the image -1)/2.
	 * <p>As the discrete real image is represented as a {@link Cell} and as per the default notation the rows
	 * of the Cell define the height of the image, thus the argument 'y' here should be within the bound of
	 * [-Ly Ly], where Ly = (row count of cell -1)/2. Else this method will throw an IllegalArgument Exception.
	 * <p>Reflection axis located at y=0, will give a reflection of the image around it central horizontal axis
	 * thus this special reflection will be a horizontal flip of the image.
	 * <p>The reflected image will be return as a {@link Cell} covering the entire reflected image support.
	 * 
	 * @param Cell cell
	 * @param float y
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell reflectHorizontally(Cell cell,float y) throws IllegalArgumentException
	{
		int Ly = cell.getRowCount()-1;
		float cY = Ly/2.0f;
		
		if(y<-cY || y>cY)
			throw new IllegalArgumentException();
		
		y=-y;
		
		float rMaxY = 2*y+cY;
		float rMinY = 2*y - cY;
		
		float minY = (rMinY <-cY)?rMinY:-cY;
		float maxY = (rMaxY >cY)?rMaxY:cY;
		
		int YkernelLength = (int) (maxY - minY +1);
		
		Cell result = new Cell(YkernelLength,cell.getColCount());
		
		for(int i=0;i<cell.getRowCount();i++)
		{
			float iR = rMaxY - i - minY; //2*y - (i-cY);coz 2*y + cY = rMaxY
			//iR = iR - minY;combined this step.


			for(int j=0;j<cell.getColCount();j++)
			{
				result.setElement(cell.getElement(i,j), (int)iR, j);
			}
		}
		
		return result;
	}
	
	/**
	 * This method will reflect the discrete real image as represented by the {@link Cell} 'cell' along
	 * a point with 'Y' and 'X' location as specified by the argument 'y' and 'x' respectively. Thus the specified
	 * reflection will be a reflection along Vertical axis followed by a reflection along the Horizontal axis whose
	 * line equations will be X=x and Y = y respectively.
	 * <p>The specified point defining the vertical and horizontal axis of interest for reflection, should be within
	 * the image with image centred on the origin. Thus the point(y,x) should be within the centred 'Y' and 'X'
	 * bound of this image that is, y -> [-Ly Ly] and x -> [-Lx Lx], where Ly = (Height of image -1)/2 and Lx = 
	 * (Width of image -1)/2 respectively.
	 *<p>As the discrete real image is represented as a {@link Cell} and as per the default notation the rows and columns
	 * of the Cell define the height and width of the image respectively, thus the argument 'y' and 'x' here should be 
	 * within the bound of [-Ly Ly] and [-Lx Lx] respectively, where Ly = (row count of cell -1)/2 and Lx = (column count of cell -1)/s.
	 * Else this method will throw an IllegalArgument Exception.
	 * <p>Reflection around the origin i.e point (y,x) = (0,0) will give a reflection of the image around its centre
	 * along both the axis thus this special reflection will be a flip of the image along both the axis.
	 * <p>The reflected image will be return as a {@link Cell} covering the entire reflected image support.
	 *  
	 * @param Cell cell
	 * @param float y
	 * @param float x
	 * @return Cell
	 * @throws IllegalArgumentException
	 */
	public Cell reflectAroundAPoint(Cell cell,float y,float x) throws IllegalArgumentException
	{
		//Max 'X' and 'Y' dimension of image in image co-ordinate.
		int Lx = cell.getColCount()-1;
		int Ly = cell.getRowCount()-1;
		
		//obtain location of origin, use to transform image co-ordinate to geometric
		//co-ordinate. Shift all points of image by (-cY,-cX).
		float cX = Lx/2.0f;
		float cY = Ly/2.0f;
		
		//'y' and 'x' should be in the geometric co-ordinate.
		if(y<-cY || y>cY || x<-cX || x>cX)
			throw new IllegalArgumentException();
		
		//Because image 'y' co-ordinate is flip to geometric 'y' co-ordinates.
		//Cell indexing start from 0 to highest in top to bottom rows.
		y=-y;
		
		//Here 'x' takes value from [-cX to cX] and so for 'y'.
		//For a vertical axis at X=x, reflection around it will get new 'x' co-ordinate as,
		//new x = 2*x - old x;
		
		//Maximum 'x' value for reflected points.
		float rMaxX = 2*x+cX;
		//Minimum 'x' value for reflected points.
		float rMinX = 2*x - cX;
		//Maximum 'y' value for reflected points.
		float rMaxY = 2*y+cY;
		//Minimum 'y' value for reflected points.
		float rMinY = 2*y - cY;
		
		//If image is reflected towards right, -cX will be minimum else rMinx will be minimum
		//along x direction.
		float minX = (rMinX <-cX)?rMinX:-cX;
		//If image is reflected towards right, rMaxX will be maximum else cX will be maximum along 
		//x direction.
		float maxX = (rMaxX >cX)?rMaxX:cX;
    
		float minY = (rMinY <-cY)?rMinY:-cY;
		float maxY = (rMaxY >cY)?rMaxY:cY;
		
		//Use this minX,maxX,minY and maxY values to create final Cell support to populate the
		//full reflected pixels in it.
		int XkernelLength = (int) (maxX - minX +1);
		int YkernelLength = (int) (maxY - minY +1);
		
		Cell result = new Cell(YkernelLength,XkernelLength);
		
		for(int i=0;i<cell.getRowCount();i++)
		{
			float iR = rMaxY - i - minY; //2*y - (i-cY);coz 2*y + cY = rMaxY
										//iR = iR - minY;combined this step.
			
			for(int j=0;j<cell.getColCount();j++)
			{
				//find reflected 'x' position for this pixel. First
				//shift this image co-ordinate position by -cX to bring it to centred geometry co-ordinates.
				
				//convert [-minX maxX] range to [0 maxX+minX] range as array only takes 0 or
				//positive indexes.
					
				float jR = rMaxX - j -minX;//2*x - (j-cX);coz 2*x +cX = rMaxX
										   //jR = jR - minX; combined this step.
				
				result.setElement(cell.getElement(i,j), (int)iR, (int)jR);
			}
		}
		
		return result;
	}
}
