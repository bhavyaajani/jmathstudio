package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Generic.Index2D;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class define various Geodesic Distance map operations on a discrete binary image as represented by {@link BinaryPixelImage}.
 * <p>Geodesic distance maps gives geodesic separation for each connected foreground pixel in binary image from a 
 * given reference pixel location represented by {@link Index2D}. Geodesic separation is estimated only for all those foreground pixels
 * in binary image which are connected to the reference pixel. Ofcourse, reference pixel has to be a foreground pixel within input
 * binary image. 
 * <p>
 * <pre>Usage:
 * BinaryPixelImage img = BinaryPixelImage.importImage("path");//Import input image as BinaryPixelImage.
 * GeodesicDistanceMap gdm = new GeodesicDistanceMap();//Create an instance of GeodesicDistanceMap.
 * Index2D start = new Index2D(11, 11);//Specify reference foreground pixel within input image for computing geodesic distance maps.

 * Cell cityBlockMap = gdm.cityBlock(img, start);//Compute geodesic City block distance map.
 * Cell euclidianMap = gdm.euclidian(img, start);//Compute geodesic Euclidian distance map.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class GeodesicDistanceMap {

	/**
	 * This method computes a Geodesic Chess Board distance map for a discrete binary image as 
	 * represented by the argument {@link BinaryPixelImage} 'img' with reference pixel position as 
	 * given by the argument {@link Index2D} 'start'and return the computed distance map as Cell.
	 * <p>The pixel position as identified by argument 'start' should fall within the valid bounds of BinaryPixelImage 'img'
	 * and should be a foreground pixel within the 'img' else this method will throw an IllegalArgument Exception.
	 * <p>Geodesic Chess Board distance operation computes chess board distance/separation based on 8 connectedness 
	 * for each foreground pixel in binary image 'img' from the reference pixel position as given by 'start'. 
	 * Geodesic chess board distance/separation is computed only for those foreground pixels in 'img' which are 
	 * spatially connected to the pixel position as identified by 'start' based on 8 connectedness.
	 * <p>The return Cell is of similar dimension as that of the 'img' and contains geodesic chess board distance
	 * for the corresponding pixels in the 'img'. Background pixels in 'img' are assigned a geodesic chess board distance
	 * of -2 while all those foreground pixels in 'img' not connected spatially to the reference pixel position 'start'
	 * based on 8 connectedness are assigned a distance of -1.
	 * <p>For all spatially connected foreground pixels in 'img' corresponding element in return Cell contains a value
	 * greater than or equal to 0 indicating a geodesic distance/separation for that pixel from the reference pixel position
	 * based on chess board 8 connectedness.
	 * 
	 * @param BinaryPixelImage img
	 * @param Index2D start
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell chessBoard(BinaryPixelImage img,Index2D start) throws IllegalArgumentException
	{
		if(!img.isWithinBounds(start) || !img.getPixel(start.getY(), start.getX()))
			throw new IllegalArgumentException();
		else
		{
			try{
				final int H = img.getHeight();
				final int W = img.getWidth();

				Cell res = new Cell(H,W);
				final float max = Float.MAX_VALUE;
				float [][] obuff = res.accessCellBuffer();

				for(int i=0;i<H;i++)
				{
					for(int j=0;j<W;j++)
					{
						if(img.getPixel(i, j))
							obuff[i][j] = max;
						else
							obuff[i][j] = -2;						
					}
				}

				obuff[start.getY()][start.getX()]=0;
				java.util.Vector<Index2D> front = new java.util.Vector<Index2D>();
				front.add(new Index2D(start.getY(), start.getX()));

				int y = 0;
				int x = 0;
				int yu = 0;
				int yl = 0;
				int xu = 0;
				int xl = 0;

				final float sqrtOfTwo = (float) Math.sqrt(2);

				while(front.size() > 0){
					java.util.Vector<Index2D> live = new java.util.Vector<Index2D>();

					for(int i=0;i<front.size();i++)
					{
						y = front.get(i).getY();
						x = front.get(i).getX();

						final float D = obuff[y][x];

						yu = y+1;
						yl = y-1;
						xu = x+1;
						xl = x-1;

						if(xl >=0 && yl>=0 && img.getPixel(yl,xl))
						{
							if(obuff[yl][xl] == max)
								live.add(new Index2D(yl, xl));

							float d = (float) (D + sqrtOfTwo);
							if(obuff[yl][xl] > d)
								obuff[yl][xl] = d;
						}
						if(yl>=0 && img.getPixel(yl,x))
						{
							if(obuff[yl][x] == max)
								live.add(new Index2D(yl, x));

							float d = (float) (D + 1);
							if(obuff[yl][x] > d)
								obuff[yl][x] = d;
						}
						if(xu < W && yl>=0 && img.getPixel(yl,xu))
						{
							if(obuff[yl][xu] == max)
								live.add(new Index2D(yl, xu));

							float d = (float) (D + sqrtOfTwo);
							if(obuff[yl][xu] > d)
								obuff[yl][xu] = d;
						}
						if(xl >=0 && img.getPixel(y,xl))
						{
							if(obuff[y][xl] == max)
								live.add(new Index2D(y, xl));

							float d = (float) (D + 1);
							if(obuff[y][xl] > d)
								obuff[y][xl] = d;
						}
						if(xu < W && img.getPixel(y,xu))
						{
							if(obuff[y][xu] == max)
								live.add(new Index2D(y, xu));

							float d = (float) (D + 1);
							if(obuff[y][xu] > d)
								obuff[y][xu] = d;
						}
						if(xl >=0 && yu < H && img.getPixel(yu, xl))
						{
							if(obuff[yu][xl] == max)
								live.add(new Index2D(yu, xl));

							float d = (float) (D + sqrtOfTwo);
							if(obuff[yu][xl] > d)
								obuff[yu][xl] = d;
						}
						if(yu < H && img.getPixel(yu,x))
						{
							if(obuff[yu][x] == max)
								live.add(new Index2D(yu, x));

							float d = (float) (D + 1);
							if(obuff[yu][x] > d)
								obuff[yu][x] = d;
						}
						if(xu < W && yu < H && img.getPixel(yu,xu))
						{
							if(obuff[yu][xu] == max)
								live.add(new Index2D(yu, xu));

							float d = (float) (D + sqrtOfTwo);
							if(obuff[yu][xu] > d)
								obuff[yu][xu] = d;
						}						
					}

					front.clear();
					front = live;

				}

				for(int i=0;i<H;i++)
				{
					for(int j=0;j<W;j++)
					{
						if(obuff[i][j] == max)
							obuff[i][j] = -1;
					}
				}

				return res;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes a Geodesic City Block distance map for a discrete binary image as 
	 * represented by the argument {@link BinaryPixelImage} 'img' with reference pixel position as 
	 * given by the argument {@link Index2D} 'start'and return the computed distance map as Cell.
	 * <p>The pixel position as identified by argument 'start' should fall within the valid bounds of BinaryPixelImage 'img'
	 * and should be a foreground pixel within the 'img' else this method will throw an IllegalArgument Exception.
	 * <p>Geodesic City Block distance operation computes city block distance/separation based on 4 connectedness 
	 * for each foreground pixel in binary image 'img' from the reference pixel position as given by 'start'. 
	 * Geodesic city block distance/separation is computed only for those foreground pixels in 'img' which are 
	 * spatially connected to the pixel position as identified by 'start' based on 4 connectedness.
	 * <p>The return Cell is of similar dimension as that of the 'img' and contains geodesic city block distance
	 * for the corresponding pixels in the 'img'. Background pixels in 'img' are assigned a geodesic city block distance
	 * of -2 while all those foreground pixels in 'img' not connected spatially to the reference pixel position 'start'
	 * based on 4 connectedness are assigned a distance of -1.
	 * <p>For all spatially connected foreground pixels in 'img' corresponding element in return Cell contains a value
	 * greater than or equal to 0 indicating a geodesic distance/separation for that pixel from the reference pixel position
	 * based on city block 4 connectedness.
	 * 
	 * @param BinaryPixelImage img
	 * @param Index2D start
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell cityBlock(BinaryPixelImage img,Index2D start) throws IllegalArgumentException
	{
		if(!img.isWithinBounds(start) || !img.getPixel(start.getY(), start.getX()))
			throw new IllegalArgumentException();
		else
		{
			try{
				final int H = img.getHeight();
				final int W = img.getWidth();

				Cell res = new Cell(H,W);
				final float max = Float.MAX_VALUE;
				float [][] obuff = res.accessCellBuffer();

				for(int i=0;i<H;i++)
				{
					for(int j=0;j<W;j++)
					{
						if(img.getPixel(i, j))
							obuff[i][j] = max;
						else
							obuff[i][j] = -2;						
					}
				}

				obuff[start.getY()][start.getX()]=0;
				java.util.Vector<Index2D> front = new java.util.Vector<Index2D>();
				front.add(new Index2D(start.getY(), start.getX()));

				int y = 0;
				int x = 0;
				int yu = 0;
				int yl = 0;
				int xu = 0;
				int xl = 0;

				while(front.size() > 0){
					java.util.Vector<Index2D> live = new java.util.Vector<Index2D>();

					for(int i=0;i<front.size();i++)
					{
						y = front.get(i).getY();
						x = front.get(i).getX();

						final float D = obuff[y][x];

						yu = y+1;
						yl = y-1;
						xu = x+1;
						xl = x-1;

						if(yl>=0 && img.getPixel(yl,x))
						{
							if(obuff[yl][x] == max)
								live.add(new Index2D(yl, x));

							float d = (float) (D + 1);
							if(obuff[yl][x] > d)
								obuff[yl][x] = d;
						}
						if(xl >=0 && img.getPixel(y,xl))
						{
							if(obuff[y][xl] == max)
								live.add(new Index2D(y, xl));

							float d = (float) (D + 1);
							if(obuff[y][xl] > d)
								obuff[y][xl] = d;
						}
						if(xu < W && img.getPixel(y,xu))
						{
							if(obuff[y][xu] == max)
								live.add(new Index2D(y, xu));

							float d = (float) (D + 1);
							if(obuff[y][xu] > d)
								obuff[y][xu] = d;
						}
						if(yu < H && img.getPixel(yu,x))
						{
							if(obuff[yu][x] == max)
								live.add(new Index2D(yu, x));

							float d = (float) (D + 1);
							if(obuff[yu][x] > d)
								obuff[yu][x] = d;
						}											
					}

					front.clear();
					front = live;

				}

				for(int i=0;i<H;i++)
				{
					for(int j=0;j<W;j++)
					{
						if(obuff[i][j] == max)
							obuff[i][j] = -1;
					}
				}

				return res;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes a Geodesic Euclidian distance map for a discrete binary image as 
	 * represented by the argument {@link BinaryPixelImage} 'img' with reference pixel position as 
	 * given by the argument {@link Index2D} 'start'and return the computed distance map as Cell.
	 * <p>The pixel position as identified by argument 'start' should fall within the valid bounds of BinaryPixelImage 'img'
	 * and should be a foreground pixel within the 'img' else this method will throw an IllegalArgument Exception.
	 * <p>Geodesic Euclidian distance operation computes direct euclidian distance/separation for each foreground 
	 * pixel in binary image 'img' from the reference pixel position as given by 'start'. Geodesic euclidian distance/separation
	 * is computed only for those foreground pixels in 'img' which are spatially connected to the pixel position as identified by 
	 * 'start' based on 8 connectedness.
	 * <p>The return Cell is of similar dimension as that of the 'img' and contains geodesic euclidian distance
	 * for the corresponding pixels in the 'img'. Background pixels in 'img' are assigned a distance
	 * of -2 while all those foreground pixels in 'img' not connected spatially to the reference pixel position 'start'
	 * based on 8 connectedness are assigned a distance of -1.
	 * <p>For all spatially connected foreground pixels in 'img' corresponding element in return Cell contains a value
	 * greater than or equal to 0 indicating a geodesic euclidian distance/separation for that pixel from the reference pixel 
	 * position.
	 * 
	 * @param BinaryPixelImage img
	 * @param Index2D start
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell euclidian(BinaryPixelImage img,Index2D start) throws IllegalArgumentException
	{
		if(!img.isWithinBounds(start) || !img.getPixel(start.getY(), start.getX()))
			throw new IllegalArgumentException();
		else
		{
			try{
				final int H = img.getHeight();
				final int W = img.getWidth();

				Cell res = new Cell(H,W);
				float[][] obuff = res.accessCellBuffer();

				for(int i=0;i<H;i++)
				{
					for(int j=0;j<W;j++)
					{
						if(img.getPixel(i, j))
							obuff[i][j] = -1;
						else
							obuff[i][j] = -2;						
					}
				}

				obuff[start.getY()][start.getX()] = 0;
				java.util.Vector<Index2D> front = new java.util.Vector<Index2D>();
				front.add(new Index2D(start.getY(), start.getX()));

				int y = 0;
				int x = 0;
				int yu = 0;
				int yl = 0;
				int xu = 0;
				int xl = 0;

				final int Y = start.getY();
				final int X = start.getX();

				while(front.size() > 0){
					java.util.Vector<Index2D> live = new java.util.Vector<Index2D>();

					for(int i=0;i<front.size();i++)
					{
						y = front.get(i).getY();
						x = front.get(i).getX();

						yu = y+1;
						yl = y-1;
						xu = x+1;
						xl = x-1;

						if(xl >=0 && yl>=0 && img.getPixel(yl,xl))
						{
							if(obuff[yl][xl] == -1){
								live.add(new Index2D(yl, xl));
								float d = f2(Y, X, yl, xl);
								obuff[yl][xl] = d;
							}
						}
						if(yl>=0 && img.getPixel(yl,x))
						{
							if(obuff[yl][x] == -1){
								live.add(new Index2D(yl, x));
								float d = f2(Y, X, yl, x);
								obuff[yl][x] = d;
							}
						}
						if(xu < W && yl>=0 && img.getPixel(yl,xu))
						{
							if(obuff[yl][xu] == -1){
								live.add(new Index2D(yl, xu));
								float d = f2(Y, X, yl, xu);
								obuff[yl][xu] = d;
							}
						}
						if(xl >=0 && img.getPixel(y,xl))
						{
							if(obuff[y][xl] == -1){
								live.add(new Index2D(y, xl));
								float d = f2(Y, X, y, xl);
								obuff[y][xl] = d;
							}
						}
						if(xu < W && img.getPixel(y,xu))
						{
							if(obuff[y][xu] == -1){
								live.add(new Index2D(y, xu));
								float d = f2(Y, X, y, xu);
								obuff[y][xu] = d;
							}
						}
						if(xl >=0 && yu < H && img.getPixel(yu, xl))
						{
							if(obuff[yu][xl] == -1){
								live.add(new Index2D(yu, xl));
								float d = f2(Y, X, yu, xl);
								obuff[yu][xl] = d;
							}
						}
						if(yu < H && img.getPixel(yu,x))
						{
							if(obuff[yu][x] == -1){
								live.add(new Index2D(yu, x));
								float d = f2(Y, X, yu, x);
								obuff[yu][x] = d;
							}
						}
						if(xu < W && yu < H && img.getPixel(yu,xu))
						{
							if(obuff[yu][xu] == -1){
								live.add(new Index2D(yu, xu));
								float d = f2(Y, X, yu, xu);
								obuff[yu][xu] = d;
							}
						}						
					}

					front.clear();
					front = live;

				}

				return res;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	private float f2(int y1,int x1,int y2,int x2)
	{
		float yd = y2-y1;
		float xd = x2-x1;
		return (float) Math.sqrt(yd*yd+xd*xd);
	}
}
