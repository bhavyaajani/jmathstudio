package org.JMathStudio.ImageToolkit.IntensityTools;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.PixelImageStack;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class provide interfaces to manipulate an UInt PixelImage at its bit level. Class define
 * many useful interfaces to access & modify desire bit's of an integer pixel intensity. 
 * <p>
 * An UInt PixelImage will be represented by an {@link AbstractUIntPixelImage}.
 * <pre>Usage:
 * AbstractUIntPixelImage img = UInt8PixelImage.importImage("path");//Import external image
 * as UInt PixelImage type.
 * 
 * int N = img.getDepth();//Get depth of the input UInt PixelImage.
 * 
 * BitsTransform bt = new BitsTransform(img);//Create an instance of BitsTransform with input
 * image as argument.
 * 
 * bt.toggleBit(k, i, j);//Toggle specific kth bit of pixel (i,j) of input image.
 * bt.setBit(k, i, j);//Set specific kth bit of pixel (i,j) of input image.
 * 
 * AbstractUIntPixelImage result = BitsTransform.setBitPlane(img, N-1);//Set last bit of all
 * the pixels of input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class BitsTransform {
	
	private AbstractUIntPixelImage image;
	private int[] i0;
	
	/**
	 * This will create an object of the class with reference UInt PixelImage as specified by the
	 * argument {@link AbstractUIntPixelImage} 'img'. The class define a wrapper class to provide
	 * access and manipulation at the bit level for the specified reference image.
	 * <p>The class shall maintain an internal clone of the reference image on which subsequent 
	 * operation and manipulation would be performed.
	 * @param AbstractUIntPixelImage img
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BitsTransform(AbstractUIntPixelImage img){
		this.image = (AbstractUIntPixelImage) img.clone();
		int level = image.getDepth();
		
		i0 = new int[level];
				
		for(int i=0;i<level;i++){
			i0[i] = (int) Math.pow(2, i);
		}
	}
	
	/**
	 * This method gives access to the manipulated UInt PixelImage.
	 * @return AbstractUIntPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage accessImage(){
		return this.image;
	}
	
	/**
	 * This method will set the 'k'th bit of the integer pixel value to '1'. The argument 'k' should
	 * identify a valid bit position between [0 N-1] where 'N' is the depth of the reference UInt PixelImage
	 * else this method will throw an IllegalArgument Exception.
	 * <p>Here '0'th and 'N-1'th bits are the Least significant bit and the Most significant bit respectively. 
	 * <p>The argument 'i' & 'j' identify the row and column index of the pixel within the image. 
	 * The argument 'i' & 'j' should be within the bound of [0 height of image-1] and [0 width of image -1] 
	 * respectively else this method will throw an IllegalArgument Exception. 
	 * @param int k
	 * @param int i
	 * @param int j
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setBit(int k,int i,int j) throws IllegalArgumentException{
		try{
			int p = image.getPixel(i, j)|i0[k];
			image.setPixel(p, i, j);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * This method will set the 'k'th bit of the integer pixel value to '0'. The argument 'k' should
	 * identify a valid bit position between [0 N-1] where 'N' is the depth of the reference UInt PixelImage
	 * else this method will throw an IllegalArgument Exception.
	 * <p>Here '0'th and 'N-1'th bits are the Least significant bit and the Most significant bit respectively. 
	 * <p>The argument 'i' & 'j' identify the row and column index of the pixel within the image. 
	 * The argument 'i' & 'j' should be within the bound of [0 height of image-1] and [0 width of image -1] 
	 * respectively else this method will throw an IllegalArgument Exception. 
	 * @param int k
	 * @param int i
	 * @param int j
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void unSetBit(int k,int i,int j) throws IllegalArgumentException{
		try{
			int p = image.getPixel(i, j)&(~i0[k]);
			image.setPixel(p, i, j);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * This method will toggle the 'k'th bit of the integer pixel value. The argument 'k' should
	 * identify a valid bit position between [0 N-1] where 'N' is the depth of the reference UInt
	 * PixelImage else this method will throw an IllegalArgument Exception.
	 * <p>Here '0'th and 'N-1'th bits are the Least significant bit and the Most significant bit respectively. 
	 * <p>The argument 'i' & 'j' identify the row and column index of the pixel within the image. 
	 * The argument 'i' & 'j' should be within the bound of [0 height of image-1] and [0 width of image -1] 
	 * respectively else this method will throw an IllegalArgument Exception. 
	 * @param int k
	 * @param int i
	 * @param int j
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void toggleBit(int k,int i,int j) throws IllegalArgumentException{
		try{
			int p = image.getPixel(i, j)^i0[k];
			image.setPixel(p, i, j);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * This method will toggle 'k'th bit of the integer pixel value for all the pixels in the
	 * UInt PixelImage as represented by {@link AbstractUIntPixelImage} 'img' and return the
	 * resultant image.
	 * <p>The argument 'k' should identify a valid bit position between [0 N-1] where 'N' is the 
	 * depth of the UInt PixelImage 'img' else this method will throw an IllegalArgument Exception.
	 * <p>Here '0'th and 'N-1'th bits are the Least significant bit and the Most significant bit 
	 * respectively. 
	 * @param AbstractUIntPixelImage img
	 * @param int k
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static AbstractUIntPixelImage toggleBitPlane(AbstractUIntPixelImage img,int k) throws IllegalArgumentException{
		if(k <0 || k >=img.getDepth())
			throw new IllegalArgumentException();
		else
		{
			try{
				BitsTransform bt = new BitsTransform(img);
				int h = img.getHeight();
				int w = img.getWidth();
				
				for(int i=0;i<h;i++)
				{
					for(int j=0;j<w;j++)
					{
						bt.toggleBit(k, i, j);
					}
				}
				
				return bt.accessImage();
			}catch(IllegalArgumentException e){
				throw new BugEncounterException();
			}
		}
	}
	/**
	 * This method will set the 'k'th bit of the integer pixel value to '1' for all the pixels in the
	 * UInt PixelImage as represented by {@link AbstractUIntPixelImage} 'img' and return the
	 * resultant image.
	 * <p>The argument 'k' should identify a valid bit position between [0 N-1] where 'N' is the 
	 * depth of the UInt PixelImage 'img' else this method will throw an IllegalArgument Exception.
	 * <p>Here '0'th and 'N-1'th bits are the Least significant bit and the Most significant bit 
	 * respectively. 
	 * @param AbstractUIntPixelImage img
	 * @param int k
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static AbstractUIntPixelImage setBitPlane(AbstractUIntPixelImage img,int k) throws IllegalArgumentException{
		if(k <0 || k >=img.getDepth())
			throw new IllegalArgumentException();
		else
		{
			try{
				BitsTransform bt = new BitsTransform(img);
				int h = img.getHeight();
				int w = img.getWidth();
				
				for(int i=0;i<h;i++)
				{
					for(int j=0;j<w;j++)
					{
						bt.setBit(k, i, j);
					}
				}
				
				return bt.accessImage();
			}catch(IllegalArgumentException e){
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method will set the 'k'th bit of the integer pixel value to '0' for all the pixels in the
	 * UInt PixelImage as represented by {@link AbstractUIntPixelImage} 'img' and return the
	 * resultant image.
	 * <p>The argument 'k' should identify a valid bit position between [0 N-1] where 'N' is the 
	 * depth of the UInt PixelImage 'img' else this method will throw an IllegalArgument Exception.
	 * <p>Here '0'th and 'N-1'th bits are the Least significant bit and the Most significant bit 
	 * respectively. 
	 * @param AbstractUIntPixelImage img
	 * @param int k
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static AbstractUIntPixelImage unSetBitPlane(AbstractUIntPixelImage img,int k) throws IllegalArgumentException{
		if(k <0 || k >=img.getDepth())
			throw new IllegalArgumentException();
		else
		{
			try{
				BitsTransform bt = new BitsTransform(img);
				int h = img.getHeight();
				int w = img.getWidth();
				
				for(int i=0;i<h;i++)
				{
					for(int j=0;j<w;j++)
					{
						bt.unSetBit(k, i, j);
					}
				}
				
				return bt.accessImage();
			}catch(IllegalArgumentException e){
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method will slice the UInt PixelImage as represented by an
	 * {@link AbstractUIntPixelImage} 'img' along its bit planes and return the
	 * set of {@link BinaryPixelImage}s representing the bit plane map for the
	 * corresponding bit depth or level, as a {@link PixelImageStack}.
	 * <p>
	 * This operation will decompose the pixel intensities of the given UInt
	 * PixelImage in to a binary representation, with number of bits for each
	 * pixel intensity equal to the depth of the given UInt PixelImage and
	 * subsequently slice/assign the individual bits in to corresponding bit
	 * plane, thus generating a set of {@link BinaryPixelImage}, with each
	 * representing the bit plane of the pixel intensities for that bit depth or
	 * level.
	 * <p>
	 * Explanation: <i>For an UInt PixelImage with depth 4, valid pixel range
	 * will be [0 15] with each pixel intensity represented by a 4 bit
	 * representation, from 0000 to 1111. Thus bit slicing operation will
	 * extract bit for each bit level (here 0 to 3) for each pixel value and
	 * generate corresponding bit plane binary images.</i>
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return PixelImageStack
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static PixelImageStack bitsSlicing(AbstractUIntPixelImage img) {
		BinaryPixelImage[] result = new BinaryPixelImage[img.getDepth()];

		try {
			for (int k = 0; k < result.length; k++) {
				result[k] = new BinaryPixelImage(img.getHeight(), img
						.getWidth());
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				int pix = img.getPixel(i, j);
				char[] bitArray = Integer.toBinaryString(pix).toCharArray();

				for (int k = 0; k < bitArray.length; k++) {
					if (bitArray[k] == '1')
						result[bitArray.length - 1 - k].setPixel(true, i, j);
					}
			}
		}

		return new PixelImageStack(result);
	}

	//This Operation is same as bits Slicing.
	
//	/**
//	 * This method will decompose the given UInt PixelImage as represented by an
//	 * {@link AbstractUIntPixelImage} 'img' along its bit planes and return the
//	 * equivalent gray scale map for subsequent bit planes as a
//	 * {@link CellStack}.
//	 * <p>
//	 * The Bit slicing decomposition, decomposes the given UInt PixelImage
//	 * intensities in to different depth or bit planes and generate a set of
//	 * Gray scale maps associated with each depth or a bit plane.
//	 * <p>
//	 * The level of decomposition or number of bit planes generated is equal to
//	 * the <i>Depth</i> of the given UInt PixelImage. For UInt PixelImage with
//	 * depth 'n'. i.e intensity in the range of [0 2^n - 1] encoded by a 'n'
//	 * bits, this method will decompose each pixel intensity in to 'n' different
//	 * bit planes [0 -> 2^n - 1] and generate gray scale maps containing the
//	 * subsequent gray scale value for that bit plane for that pixel location.
//	 * <p>
//	 * <i>For a bit plane 'i', gray scale map for that bit plane will take a
//	 * value of 2^i if contribution from that bit plane for the given pixel
//	 * location exist else it will be 0.</i>
//	 * <p>
//	 * The operation for the gray scale bit plane slicing can be demonstrated
//	 * as,
//	 * <p>
//	 * <i>Gray scale = pixelvalue & 2^i, for ith Gray scale bit plane map.</i>
//	 * <p>
//	 * All such gray scale maps will be return as a {@link CellStack} such that
//	 * the 0th index {@link Cell} represent the the gray scale map for the LSB
//	 * bit plane and subsequent indexed {@link Cell}'s representing the gray
//	 * scale map of the bit plane in increasing order till last indexed Cell
//	 * represent the gray scale map for the MSB bit plane.
//	 * <p>
//	 * The number of {@link Cell}'s in the return {@link CellStack} will be
//	 * equal to the depth or number of bits encoding of the given UInt
//	 * PixelImage.
//	 * 
//	 * @param AbstractUIntPixelImage
//	 *            img
//	 * @return CellStack
//	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
//	 */
//	public CellStack grayScaleBitsPlaneSlicing(AbstractUIntPixelImage img) {
//		Cell[] result = new Cell[img.getDepth()];
//
//		int[] bits = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024,
//				2048, 4096, 8192, 16384, 32768 };
//
//		for (int k = 0; k < result.length; k++) {
//			result[k] = new Cell(img.getHeight(), img.getWidth());
//		}
//
//		for (int i = 0; i < img.getHeight(); i++) {
//			for (int j = 0; j < img.getWidth(); j++) {
//				int pix = img.getPixel(i, j);
//
//				for (int k = 0; k < result.length; k++) {
//					result[k].setElement(pix & bits[k], i, j);
//				}
//			}
//		}
//
//		return new CellStack(result);
//	}

}
