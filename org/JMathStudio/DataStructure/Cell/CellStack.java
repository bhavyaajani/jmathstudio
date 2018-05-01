package org.JMathStudio.DataStructure.Cell;

import java.awt.image.BufferedImage;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBMapper;
import org.JMathStudio.PixelImageToolkit.ColorPixelImage.RGBPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt16PixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.UInt8PixelImage;
import org.JMathStudio.VisualToolkit.Viewer.SeriesViewer;

/**
 * This class define a mutable container for storing a set of {@link Cell}'s
 * as a stack. New Cell's can be appended to the existing stack dynamically.
 * <p>Internally this class represent the Cell Stack as a {@link java.util.Vector}
 * of Cell objects.
 * <p>
 * Size - This indicates the number of {@link Cell}'s contained in the stack.
 * <p>
 * Each Cell in the stack can be accessed by its index position which start
 * from 0 to one less than the size of this stack.
 * <pre>Usage:
 * Let 'a', 'b' & 'c' be Cell objects.
 * 
 * CellStack stack = new CellStack();//Create an instance of empty CellStack.
 * 
 * stack.addCell(a);//Add Cell's to the stack in order.
 * stack.addCell(b);
 * stack.addCell(c);
 * 
 * Cell a = stack.accessCell(0);//Access first Cell from stack.
 * 
 * stack.showAsUInt8ImageSeries("Title");//Display Cell's within stack.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CellStack {
	
	private java.util.Vector<Cell> stack;

	/**
	 * This will initiate a new empty Cell Stack object to which {@link Cell}'s can be
	 * appended later on.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack()
	{
		stack = new java.util.Vector<Cell>();
	}
	
	/**
	 * This will initiate a new Cell Stack with specified initial capacity 'n'. 
	 * <p>If argument 'n' is less than '1' this method will throw an IllegalArgument
	 * Exception.
	 * <p>Argument 'n' here specify the initial capacity for the Cell Stack, which increases
	 * as more {@link Cell}'s are added to the stack later on.
	 * @param int n
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack(int n) throws IllegalArgumentException
	{
		if(n <=0)
			throw new IllegalArgumentException();
		else
			stack = new java.util.Vector<Cell>(n);
	}
	/**
	 * This will initiate a new Cell Stack object and populate the same with the non
	 * null {@link Cell}'s from the given 1D Cell array 'cellArray' in the same order.
	 * <p>If the Cell array 'cellArray' contain any null Cell's the size of this
	 * Cell Stack will not be same as that of the Cell array.
	 * <p>The argument 'cellArray' is passed as reference and no deep copy of the array
	 * is made.
	 * @param Cell[] cellArray
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack(Cell[] cellArray) 
	{
		stack = new java.util.Vector<Cell>();
		
		if(cellArray != null)
		{
			for(int i=0;i<cellArray.length;i++)
			{
				if(cellArray[i] != null)
					stack.addElement(cellArray[i]);
			}
		}
		
	}

	/**
	 * This method will append the {@link Cell} 'cell' at the end of the given Cell Stack.
	 * <p>This increases the size of the given Cell Stack by 1.
	 * <p>If the Cell 'cell' is null this method will throw a NullPointer Exception.
	 * <p>The argument 'cell' is added by reference and no deep copy of the same is made.
	 * @param Cell cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void addCell(Cell cell)
	{
		if(cell == null)
			throw new NullPointerException();
		else
			stack.addElement(cell);
	}

	/**
	 * This method will convert the given Cell Stack to a 1D {@link Cell} array
	 * and return the same.
	 * <p>The order of the Cells in the return array will be same as that in the given stack.
	 * <p>If the given Cell Stack is empty; a null shall be returned. 
	 * @return Cell[]
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell[] accessCellArray() {
		
		if(stack.size()==0)
			return null;
		else
		{
			Cell[] array = new Cell[stack.size()];
			
			for(int i=0;i<array.length;i++){
				array[i] = accessCell(i);
			}
			
			return array;
		}
	}

	/**
	 * This method will return the current size of the given Cell Stack.
	 * <p>This indicate the number of {@link Cell}'s contained within the
	 * given Cell Stack.
	 * <p>If the given Cell Stack has no Cell elements this method will
	 * return size as '0'.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public int size() {
		
		if(stack.isEmpty())
			return 0;
		else
			return stack.size();
	}

	/**
	 * This method will return the {@link Cell} located at the index
	 * position as given by the argument 'index' within the given Cell Stack.
	 * <p>
	 * The value of argument 'index' should be in the range of 0 to one less
	 * than the size of this Cell Stack else this method will throw an
	 * ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell accessCell(int index) {
		return stack.get(index);
	}

	/**
	 * This method will replace the {@link Cell} element located at the
	 * index position given by the argument 'index' in the given Cell Stack with the
	 * Cell as specified by the argument 'cell'.
	 * <p>
	 * The value of the argument 'index' should be in the range of 0 to one less
	 * than the size of this Cell Stack else this method will throw an
	 * ArrayIndexOutOfBound Exception.
	 * <p>If the {@link Cell} 'cell' is null this method will throw a NullPointer
	 * Exception.
	 * <p>The Cell 'cell' is replaced by reference and no deep copy of the Cell is
	 * made by the method.
	 * 
	 * @param Cell
	 *            cell
	 * @param int index
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public void replace(Cell cell, int index) {
		
		if(cell == null)
			throw new NullPointerException();
		else{
			stack.remove(index);
			stack.insertElementAt(cell, index);
		}
	}
	
	/**
	 * This method will convert the all the Cell's of this {@link CellStack} to an {@link UInt16PixelImage}'s
	 * and display the same as a series on a {@link SeriesViewer}.
	 * <p>Each {@link Cell} of the given CellStack will be converted to an UInt16 pixel image 
	 * by mapping linearly the elements of the Cell to an UInt16 pixel range of [0 65535].
	 * <p>The argument 'title' specify the Title for the SeriesViewer.
	 * 
	 * @param String
	 *            title
	 * @see UInt16PixelImage#toUInt16PixelImage(Cell)
	 * @see UInt16PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showAsUInt16ImageSeries(String title)
	{
		BufferedImage[] imgs = new BufferedImage[size()];
		
		for(int i=0;i<imgs.length;i++)
		{
			imgs[i] = UInt16PixelImage.toUInt16PixelImage(accessCell(i)).toBufferedImage();
		}
		
		SeriesViewer viewer = new SeriesViewer();
		viewer.setTitle(title);
		
		try{
			viewer.displaySeries(imgs, accessCellArray());
		}catch(DimensionMismatchException e)
		{
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will convert the all the Cell's of this {@link CellStack} to an {@link UInt8PixelImage}'s
	 * and display the same as a series on a {@link SeriesViewer}.
	 * <p>Each {@link Cell} of the given CellStack will be converted to an UInt8 pixel image 
	 * by mapping linearly the elements of the Cell to an UInt8 pixel range of [0 255].
	 * <p>The argument 'title' specify the Title for the SeriesViewer.
	 * 
	 * @param String
	 *            title
	 * @see UInt8PixelImage#toUInt8PixelImage(Cell)
	 * @see UInt8PixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showAsUInt8ImageSeries(String title)
	{
		BufferedImage[] imgs = new BufferedImage[size()];
		
		for(int i=0;i<imgs.length;i++)
		{
			imgs[i] = UInt8PixelImage.toUInt8PixelImage(accessCell(i)).toBufferedImage();
		}
		
		SeriesViewer viewer = new SeriesViewer();
		viewer.setTitle(title);
		
		try{
			viewer.displaySeries(imgs, accessCellArray());
		}catch(DimensionMismatchException e)
		{
			throw new BugEncounterException();
		}

	}

	
	/**
	 * This method will convert all the {@link Cell}'s of this {@link CellStack} to a False/Pseudo {@link RGBPixelImage}
	 * and display the image series on a {@link SeriesViewer}.
	 * <p>The elements of the Cell will be linearly mapped to an UInt8 Pixel image range 
	 * of [0 255] and subsequently each mapped value will be assign a colour as define by the {@link RGBMapper}
	 * 'mapper'.
	 * <p>The resultant false color {@link RGBPixelImage}'s will be displayed as a series on the SeriesViewer.
	 * <p>
	 * The argument 'title' specify the Title for the SeriesViewer.
	 * 
	 * @param RGBMapper
	 *            mapper
	 * @param String
	 *            title
	 * @see RGBMapper
	 * @see UInt8PixelImage#toUInt8PixelImage(Cell)
	 * @see UInt8PixelImage#mapToRGBPixelImage(RGBMapper)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void showAsPseudoRGBImageSeries(RGBMapper mapper,String title)
	{
		BufferedImage[] imgs = new BufferedImage[size()];
		
		for(int i=0;i<imgs.length;i++)
		{
			imgs[i] = UInt8PixelImage.toUInt8PixelImage(accessCell(i)).mapToRGBPixelImage(mapper).toBufferedImage();
		}
		
		SeriesViewer viewer = new SeriesViewer();
		viewer.setTitle(title);
		
		try{
			viewer.displaySeries(imgs, accessCellArray());
		}catch(DimensionMismatchException e)
		{
			throw new BugEncounterException();
		}

	}
	
	/**
	 * This method will remove all the {@link Cell}s from the current stack and empty the stack.
	 * <p>The size of the stack will be '0' after this call.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void clear(){
		this.stack.clear();
	}
}
