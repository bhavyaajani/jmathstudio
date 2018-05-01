package org.JMathStudio.PixelImageToolkit.BinaryPixelImage;

import org.JMathStudio.DataStructure.Generic.Index2D;
import org.JMathStudio.DataStructure.Generic.Index2DList;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class defines some of the useful operations on a binary image. A Binary image shall be
 * represented by a {@link BinaryPixelImage} object.
 * <pre>Usage:
 * Let 'mask' be a BinaryPixelImage representing the mask for masking an external image represented
 * by Cell 'image'. Both 'mask' and 'image' are of same dimension.
 * 
 * Index2DList list = BinaryImageUtilities.buildIndexList(mask);//Get indexes for all foreground pixels
 * in the mask.	
 * 
 * image.setAllElements(0, list);//Set '0' all the elements of Cell 'image' at indexes marked for 
 * masking. 
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class BinaryImageUtilities {

	//Ensure no instances are made for utility classes.
	private BinaryImageUtilities(){}
	
	/**
	 * This method will prepare a list of {@link Index2D} of all the foreground pixels (true's) within
	 * the {@link BinaryPixelImage} 'image' and return the list as {@link Index2DList}.
	 * <p>Each {@link Index2D} within the Index2DList gives the image index for a single unique foreground 
	 * pixel within the 'image'. The number of indexes within the return list also equals number of
	 * foreground pixels within the 'image'. 
	 * @param BinaryPixelImage image
	 * @return {@link IndexList}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Index2DList buildIndexList(BinaryPixelImage image){
		
		try{
			int count = countForeGroundPixels(image);

			Index2DList list = new Index2DList(count);

			final int h = image.getHeight();
			final int w = image.getWidth();
			boolean[][] buffer = image.accessPixelDataBuffer();

			for(int i=0;i<h;i++)
			{
				for(int j=0;j<w;j++)
				{
					if(buffer[i][j]){
						list.add(new Index2D(i,j));
					}
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will count the number of foreground pixels (true's) within the {@link BinaryPixelImage}
	 * 'image' and return the count.
	 * @param BinaryPixelImage image
	 * @return int 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static int countForeGroundPixels(BinaryPixelImage image){
	
		final int h = image.getHeight();
		final int w = image.getWidth();
		
		int count = 0;
		boolean[][] buffer = image.accessPixelDataBuffer();
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				if(buffer[i][j]){
					count++;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * This method will merge {@link BinaryPixelImage}s 'img1' and 'img2' into a single {@link BinaryPixelImage}
	 * and return the same.
	 * <p>The BinaryPixelImages are merged by taking OR operation between the corresponding pixels. If the input 
	 * BinaryPixelImages are of different dimensions, this method will first resize the images to their maximum 
	 * common dimension by padding background pixels 'false' before applying the merge operation.
	 * <p>The dimensions of the resultant merged BinaryPixelImage shall be largest of the dimensions of the input
	 * BinaryPixelImages.  
	 * <p>This method is useful in merging two different binary masks.
	 * @param BinaryPixelImage img1
	 * @param BinaryPixelImage img2
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public static final BinaryPixelImage mergeBinaryImages(BinaryPixelImage img1, BinaryPixelImage img2)
	{
		try{
			final int h1 = img1.getHeight();
			final int h2 = img2.getHeight();
			final int w1 = img1.getWidth();
			final int w2 = img2.getWidth();

			final int h = (h1 >= h2) ? h1 : h2;
			final int w = (w1 >= w2) ? w1 : w2;

			BinaryPixelImage res = new BinaryPixelImage(h, w);
			boolean[][] buffer = res.accessPixelDataBuffer();
			boolean[][] buf1 = img1.accessPixelDataBuffer();
			boolean[][] buf2 = img2.accessPixelDataBuffer();

			for(int i=0;i<h1;i++)
			{
				for(int j=0;j<w1;j++)
				{
					buffer[i][j] |= buf1[i][j];
				}
			}

			for(int i=0;i<h2;i++)
			{
				for(int j=0;j<w2;j++)
				{
					buffer[i][j] |= buf2[i][j];
				}
			}

			return res;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
}
