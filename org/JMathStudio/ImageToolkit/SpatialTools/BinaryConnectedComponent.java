package org.JMathStudio.ImageToolkit.SpatialTools;

import java.util.Vector;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Structure.Neighbor;
import org.JMathStudio.DataStructure.Structure.Neighborhood;
import org.JMathStudio.Exceptions.EmptyNeighborhoodException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class defines operations for finding connected components (set of foreground pixels) within a Binary image.
 * A binary image shall be represented by an {@link BinaryPixelImage} object.
 * 
 * <pre>Usage:
 * BinaryPixelImage img = BinaryPixelImage.importImage("path");//Import external image as binary image.
 *		
 * BinaryConnectedComponent bcc = new BinaryConnectedComponent();//Create an instance of BinaryConnectedComponent.
 *		
 * Cell labels = bcc.fastEightConnected(img);//Mark all eight connected components.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class BinaryConnectedComponent {

	/**
	 * This method will find all the connected components (foreground pixels) within the {@link BinaryPixelImage} 
	 * 'image' using the {@link Neighborhood} 'nbr' which defines the criteria for connectedness. 
	 * <p>Each unique set/group of connected foreground pixels within the 'image' are assign a unique common label (positive
	 * integer). The return Cell contains an integer value (label) indicating the set/group to which corresponding foreground
	 * pixel in the 'image' belongs to. All background pixels shall be assign a label 0.
	 * <p>With 4 and 8 connectedness use {@link #fastFourConnected(BinaryPixelImage)} and {@link #fastEightConnected(BinaryPixelImage)}
	 * methods respectively for improved performance.
	 * <p>If {@link Neighborhood} 'nbr' is empty i.e if connectedness is not defined this method will throw
	 * an EmptyNeighborhood Exception.
	 * @param BinaryPixelImage image
	 * @param Neighborhood nbr
	 * @return Cell
	 * @throws EmptyNeighborhoodException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Cell markConnectedComponent(BinaryPixelImage image, Neighborhood nbr) throws EmptyNeighborhoodException
	{
		int h = image.getHeight();
		int w = image.getWidth();
		
		boolean[][] ipbfr = image.accessPixelDataBuffer();
		Cell res = new Cell(h,w);
		float[][] opbfr = res.accessCellBuffer();
		
		long label = 1;
		float L;
		
		Neighbor[] nbrs = nbr.accessAllNeighbors();
		if(nbrs == null)
			throw new EmptyNeighborhoodException();
		
		int x,y;
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				if(ipbfr[i][j]){
					L = 0;
					for(int k=0;k<nbrs.length;k++)
					{
						x = j + nbrs[k].getX();
						y = i + nbrs[k].getY();
						
						if(x >=0 && x< w && y >= 0 && y < h){
							
							if(ipbfr[y][x] && opbfr[y][x] != 0){
								L = f0(opbfr[y][x], L);
							}
						}
					}
					
					if( L == 0){
						opbfr[i][j] = label++;
					}
					else
						opbfr[i][j] = L;
				}
			}
		}
		
		boolean loop = false;
		
		do{
			loop = false;
			
			for(int i=0;i<h;i++)
			{
				for(int j=0;j<w;j++)
				{
					if(ipbfr[i][j]){
						L = opbfr[i][j];
						for(int k=0;k<nbrs.length;k++)
						{
							x = j + nbrs[k].getX();
							y = i + nbrs[k].getY();
							
							if(x >=0 && x< w && y >= 0 && y < h){
								
								if(ipbfr[y][x]){
									L = f0(opbfr[y][x], L);
								}
							}
						}
						
						if(opbfr[i][j] != L){
							for(int k=0;k<nbrs.length;k++)
							{
								x = j + nbrs[k].getX();
								y = i + nbrs[k].getY();
								
								if(x >=0 && x< w && y >= 0 && y < h){
									
									if(ipbfr[y][x]){
										opbfr[y][x] = L;
									}
								}
							}
							loop = true;
						}
					}
				}
			}
		}while(loop);
		
		f4(res);
		return res;
	}
	
	/**
	 * This method will find all the connected components (foreground pixels) within the {@link BinaryPixelImage} 'image' which satisfy 4 connectedness. 
	 * In 4 connectedness, a pixel 'A' is said to be connected to pixel 'B' if location of 'A' is either to the immediate left, right,
	 * top or bottom of location of 'B'.
	 * <p>Each unique set/group of connected foreground pixels within the 'image' are assign a unique common label (positive
	 * integer). The return Cell contains an integer value (label) indicating the set/group to which corresponding foreground
	 * pixel in the 'image' belongs to. All background pixels shall be assign a label 0.
	 * @param BinaryPixelImage image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell fastFourConnected(BinaryPixelImage image)
	{
		int h = image.getHeight();
		int w = image.getWidth();
		
		boolean[][] ipbfr = image.accessPixelDataBuffer();
		Cell res = new Cell(h,w);
		float[][] opbfr = res.accessCellBuffer();
		
		long label = 1;
		int tmp = 0;
		
		//Label connected components along horizontal x direction for first initial pass.
		for(int i=0;i<h;i++)
		{
			if(ipbfr[i][0])
				opbfr[i][0] = label++;
			
			for(int j=1;j<w;j++)
			{
				//If current pixel is foreground.
				if(ipbfr[i][j]){
					
					tmp = j-1;
					//and if the left pixel is also foreground i.e connected
					if(ipbfr[i][tmp]){
						//Connected so label as that of left most (which is minimum).
						opbfr[i][j] = opbfr[i][tmp];
					}
					else//Not connected give new label.
						opbfr[i][j] = label++;
				}
			}
		}
		
		boolean loop = false;
	
		//In loop now no new labels are assigned but labels are consolidated as per the connected
		//components as identified. Stop loop when no more change possible i.e solution is reached.
		do{
			loop = false;
						
			//For each iteration set minimum of self and top (if connected pixels) labels.
			for(int j=0;j<w;j++)
			{
				for(int i=1;i<h;i++)
				{
					tmp = i-1;
					if(ipbfr[i][j] && ipbfr[tmp][j]){
						
						if(opbfr[i][j] != opbfr[tmp][j]){
							float min = f0(opbfr[i][j] , opbfr[tmp][j]);
							opbfr[i][j] = min;
							opbfr[tmp][j] = min;
							loop = true;
						}
					}
				}
			}
			
			//For each iteration set minimum of self and left (if connected pixels) labels.
			for(int i=0;i<h;i++)
			{
				for(int j=1;j<w;j++)
				{
					tmp = j-1;
					if(ipbfr[i][j] && ipbfr[i][tmp]){
						
						if(opbfr[i][j] != opbfr[i][tmp]){
							float min = f0(opbfr[i][j] , opbfr[i][tmp]);
							opbfr[i][j] = min;
							opbfr[i][tmp] = min;
							loop = true;
						}
					}
				}
			}
			
		}while(loop);
		
		f4(res);
		return res;
		
	}
	
	/**
	 * This method will find all the connected components (foreground pixels) within the {@link BinaryPixelImage} 'image' which satisfy 8
	 * connectedness. In 8 connectedness, a pixel 'A' is said to be connected to pixel 'B' if location of 'A' is either to the immediate 
	 * left, right, top, bottom, top-left, top-right, bottom-left or bottom-right of location of 'B'.
	 * <p>Each unique set/group of connected foreground pixels within the 'image' are assign a unique common label (positive
	 * integer). The return Cell contains an integer value (label) indicating the set/group to which corresponding foreground
	 * pixel in the 'image' belongs to. All background pixels shall be assign a label 0.
	 * @param BinaryPixelImage image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell fastEightConnected(BinaryPixelImage image) 
	{
		int h = image.getHeight();
		int w = image.getWidth();
		
		boolean[][] ipbfr = image.accessPixelDataBuffer();
		Cell res = new Cell(h,w);
		float[][] opbfr = res.accessCellBuffer();
		
		long label = 1;
		int tmp = 0;
		int _x,_y;
		//Label connected components along horizontal x direction for first initial pass.
		for(int i=0;i<h;i++)
		{
			if(ipbfr[i][0])
				opbfr[i][0] = label++;
			
			for(int j=1;j<w;j++)
			{
				//If current pixel is foreground.
				if(ipbfr[i][j]){
					
					tmp = j-1;
					//and if the left pixel is also foreground i.e connected
					if(ipbfr[i][tmp]){
						//Connected so label as that of left most (which is minimum).
						opbfr[i][j] = opbfr[i][tmp];
					}
					else//Not connected give new label.
						opbfr[i][j] = label++;
				}
			}
		}
		
		boolean loop = false;
		
		//In loop now no new labels are assigned but labels are consolidated as per the connected
		//components as identified. Stop loop when no more change possible i.e solution is reached.
		do{
			loop = false;
						
			//For each iteration set minimum of self and top (if connected pixels) labels.
			for(int j=0;j<w;j++)
			{
				for(int i=1;i<h;i++)
				{
					tmp = i-1;
					if(ipbfr[i][j] && ipbfr[tmp][j]){
						
						if(opbfr[i][j] != opbfr[tmp][j]){
							float min = f0(opbfr[i][j] , opbfr[tmp][j]);
							opbfr[i][j] = min;
							opbfr[tmp][j] = min;
							loop = true;
						}
					}
				}
			}
			
			//For each iteration set minimum of self and top-left(if connected pixels) labels.
			for(int j=1;j<w;j++)
			{
				for(int i=1;i<h;i++)
				{
					_y = i-1;
					_x = j-1;
					if(ipbfr[i][j] && ipbfr[_y][_x]){
						
						if(opbfr[i][j] != opbfr[_y][_x]){
							float min = f0(opbfr[i][j] , opbfr[_y][_x]);
							opbfr[i][j] = min;
							opbfr[_y][_x] = min;
							loop = true;
						}
					}
				}
			}
			
			//For each iteration set minimum of self and top-right (if connected pixels) labels.
			for(int j=0;j<w-1;j++)
			{
				for(int i=1;i<h;i++)
				{
					_y = i-1;
					_x = j+1;
					if(ipbfr[i][j] && ipbfr[_y][_x]){
						
						if(opbfr[i][j] != opbfr[_y][_x]){
							float min = f0(opbfr[i][j] , opbfr[_y][_x]);
							opbfr[i][j] = min;
							opbfr[_y][_x] = min;
							loop = true;
						}
					}
				}
			}
			
			//For each iteration set minimum of self and left (if connected pixels) labels.
			for(int i=0;i<h;i++)
			{
				for(int j=1;j<w;j++)
				{
					tmp = j-1;
					if(ipbfr[i][j] && ipbfr[i][tmp]){
						
						if(opbfr[i][j] != opbfr[i][tmp]){
							float min = f0(opbfr[i][j] , opbfr[i][tmp]);
							opbfr[i][j] = min;
							opbfr[i][tmp] = min;
							loop = true;
						}
					}
				}
			}
			
		}while(loop);
		
		f4(res);
		return res;		
	}
	
	//Act on the input Cell.
	private void f4(Cell res)
	{
		Vector<Long> map = new Vector<Long>();
		
		int h = res.getRowCount();
		int w = res.getColCount();
		
		float[][] labels = res.accessCellBuffer();
		//Add label 0 first, thus '0th' label will always has indexOf '0'.
		//So 0 in input Cell will map 0 in output Cell.
		map.add(new Long(0));
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				Long label = new Long((long)labels[i][j]);
				if(map.contains(label)){
					labels[i][j] = map.indexOf(label);
				}
				else{
					map.add(label);	
					labels[i][j] = map.indexOf(label);
				}
			}
		}
		
	}
	
	private float f0(float l1, float l2)
	{
		if(l1 <= l2)
			return l1;
		else
			return l2;
	}	
}
