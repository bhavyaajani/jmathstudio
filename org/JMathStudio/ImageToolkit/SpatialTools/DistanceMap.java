package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class define various Distance map operations on a discrete binary image.
 * This transform computes distance maps which gives minimum separation of a
 * foreground pixel from a background pixel.
 * <p>
 * A discrete binary image will be represented by a {@link BinaryPixelImage}
 * object.
 * <pre>Usage:
 * BinaryPixelImage img = BinaryPixelImage.importImage("path");//Import input image as BinaryPixelImage.
 * 
 * DistanceMap dm = new DistanceMap();//Create an instance of DistanceMap.
 * 
 * Cell cityBlock = dm.cityBlock(img);//Compute city block distance for foreground pixels in 
 * input binary image.
 * 
 * Cell euclidian = dm.euclidian(img);//Compute euclidian distance for foreground pixels in input
 * binary image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class DistanceMap {

	/**
	 * This enumeration list supported distance type.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	private static enum DistanceType {Euclidian,CityBlock,ChessBoard,QuasiEuclidian};
	
//	private String Euclidian = "Euclidian";
//	private String CityBlock = "CityBlock";
//	private String ChessBoard = "ChessBoard";
//	private String QuasiEuclidian = "QuasiEuclidian";

	/**
	 * This method computes the Quasi Euclidian distance map of the discrete
	 * binary image as represented by a BinaryPixelImage 'img' and return the
	 * same as a Cell.
	 * <p>
	 * Quasi Euclidian distance is the measure of the total Euclidean distance
	 * along a set of horizontal, vertical, and diagonal line segments of an
	 * foreground pixel from the nearest background pixel position.
	 * <p>
	 * The Quasi Euclidian distance map is thus return as a Cell with dimension
	 * similar to the original BinaryPixelImage such that each non zero element
	 * of the return Cell gives the minimum Quasi Euclidian distance for that
	 * foreground pixel.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @return Cell
	 * @see {@link #euclidian(BinaryPixelImage)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell quasiEuclidian(BinaryPixelImage img) {
		return this.f3(img, DistanceType.QuasiEuclidian);
	}

	/**
	 * This method computes the Euclidian distance map of the discrete binary
	 * image as represented by a BinaryPixelImage 'img' and return the same as a
	 * Cell.
	 * <p>
	 * Euclidian distance is the minimum straight line distance of an foreground
	 * pixel from the nearest background pixel position.
	 * <p>
	 * The Euclidian distance map is thus return as a Cell with dimension
	 * similar to the original BinaryPixelImage such that each non zero element
	 * of the return Cell gives the Euclidian distance for that foreground
	 * pixel.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @return Cell
	 * @see {@link #quasiEuclidian(BinaryPixelImage)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell euclidian(BinaryPixelImage img) {
		return this.f3(img, DistanceType.Euclidian);
	}

	/**
	 * This method computes the Chess Board distance map of the discrete binary
	 * image as represented by a BinaryPixelImage 'img' and return the same as a
	 * Cell.
	 * <p>
	 * Chess Board distance is the minimum distance of an foreground pixel from
	 * the nearest background pixel position based on a 8 connected
	 * neighbourhood.
	 * <p>
	 * The Chess Board distance map is thus return as a Cell with dimension
	 * similar to the original BinaryPixelImage such that each non zero element
	 * of the return Cell gives the Chess Board distance for that foreground
	 * pixel.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @return Cell
	 * @see {@link #cityBlock(BinaryPixelImage)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell chessBoard(BinaryPixelImage img) {
		return f3(img, DistanceType.ChessBoard);
	}

	/**
	 * This method computes the City Block distance map of the discrete binary
	 * image as represented by a BinaryPixelImage 'img' and return the same as a
	 * Cell.
	 * <p>
	 * City Block Distance is the minimum distance of an foreground pixel from
	 * the nearest background pixel position based on a 4 connected
	 * neighbourhood.
	 * <p>
	 * The City Block distance map is thus return as a Cell with dimension
	 * similar to the original BinaryPixelImage such that each non zero element
	 * of the return Cell gives the City Block distance for that foreground
	 * pixel.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @return Cell
	 * @see {@link #chessBoard(BinaryPixelImage)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell cityBlock(BinaryPixelImage img) {
		return f3(img, DistanceType.CityBlock);
	}

	private Cell f3(BinaryPixelImage img, DistanceType choice) {
		int height = img.getHeight();
		int width = img.getWidth();

		Cell result = new Cell(height, width);

		// minDistance will be number of steps away from current pixel
		// we will search in square periphery for distances.
		int maxDimension = height > width ? height : width;

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				// To compute distances for foreground pixels only.
				if (img.getPixel(i, j) == true) {
					// start from square periphery pixels at distance
					// 1 to maxDimension.
					// min distances are located for lower square periphery
					// elements.
					for (int iter = 1; iter < maxDimension; iter++) {
						int k, l;
						float minDist = Float.MAX_VALUE;

						// to check if min distance is found in current square
						// only if '0' i.e background pixel is
						// present in square periphery located at this distance.
						// if so no need to look for higher squares periphery
						boolean isDone = false;

						// bottom square edge at distance iter.
						k = -iter;

						if (i + k >= 0 && i + k < height) {
							for (l = -iter; l <= iter; l++) {
								if (j + l >= 0 && j + l < width) {
									if (img.getPixel(i + k, j + l) == false) {
										isDone = true;
										float dist = f1(choice, k, l);
										minDist = dist < minDist ? dist
												: minDist;
									}
								}
							}
						}
						// top square edge at distance iter.
						k = iter;
						if (i + k >= 0 && i + k < height) {
							for (l = -iter; l <= iter; l++) {
								if (j + l >= 0 && j + l < width) {
									if (img.getPixel(i + k, j + l) == false) {
										isDone = true;
										float dist = f1(choice, k, l);
										minDist = dist < minDist ? dist
												: minDist;
									}
								}
							}
						}

						// left square edge at distance iter.
						l = -iter;
						if (j + l >= 0 && j + l < width) {
							for (k = -iter; k <= iter; k++) {
								if (i + k >= 0 && i + k < height) {
									if (img.getPixel(i + k, j + l) == false) {
										isDone = true;
										float dist = f1(choice, k, l);
										minDist = dist < minDist ? dist
												: minDist;
									}
								}
							}
						}
						// right square edge at distance iter.
						l = iter;
						if (j + l >= 0 && j + l < width) {
							for (k = -iter; k <= iter; k++) {
								if (i + k >= 0 && i + k < height) {
									if (img.getPixel(i + k, j + l) == false) {
										isDone = true;
										float dist = f1(choice, k, l);
										minDist = dist < minDist ? dist
												: minDist;
									}
								}
							}
						}

						if (isDone) {
							// set result distance by taking square root.
							// exit for current pixel location.

							if (choice == DistanceType.Euclidian)
								result.setElement((float) Math.sqrt(minDist),
										i, j);
							else
								result.setElement(minDist, i, j);
							break;
						}

					}
				}
			}
		}

		return result;
	}

//	private float f1(DistanceType type, int k, int l) {
//		if (type == DistanceType.Euclidian)
//			return k * k + l * l;
//		else if (type == DistanceType.CityBlock)
//			return abs(k) + abs(l);
//		else if (type == DistanceType.ChessBoard) {
//			float absK = abs(k);
//			float absL = abs(l);
//
//			return absK > absL ? absK : absL;
//		} else if (type == DistanceType.QuasiEuclidian) {
//			float absK = abs(k);
//			float absL = abs(l);
//			float sqrt2less1 = 0.4142135f;
//
//			if (absL > absK)
//				return absL + (sqrt2less1) * absK;
//			else
//				return absK + (sqrt2less1) * absL;
//		} else
//			throw new BugEncounterException();
//	}

	private float f1(DistanceType type, int k, int l) {
		
		switch (type) {
		case Euclidian:
			return k * k + l * l;
		case CityBlock:
			return abs(k) + abs(l);
		case ChessBoard:
			float absK = abs(k);
			float absL = abs(l);

			return absK > absL ? absK : absL;
		case QuasiEuclidian:
			float absoluteK = abs(k);
			float absoluteL = abs(l);
			float sqrt2less1 = 0.4142135f;

			if (absoluteL > absoluteK)
				return absoluteL + (sqrt2less1) * absoluteK;
			else
				return absoluteK + (sqrt2less1) * absoluteL;
		default:
			throw new BugEncounterException();
		}
	}

	private float abs(float value) {
		if (value < 0)
			return -value;
		else
			return value;
	}

	// public Cell cityBlock(BinaryPixelImage img)
	// {
	// int height = img.getHeight();
	// int width = img.getWidth();
	//		
	// Cell result = new Cell(height, width);
	//		
	// //To store x and y positions of all pixels with 1's
	// //for computing distances later on.
	// ArrayList<Integer> x = new ArrayList<Integer>();
	// ArrayList<Integer> y = new ArrayList<Integer>();
	//		
	// for(int i=0;i<result.getRowCount();i++)
	// {
	// for(int j=0;j<result.getColCount();j++)
	// {
	// //set distance 0 for pixel with 1's.
	// //To differentiate between default 0
	// //values in array with the position with
	// //0 distance, put -1 instead of 0 temporarily.
	// if(img.getPixel(i, j) == true)
	// {
	// result.setElement(-1,i,j);
	// //Store x y positions for all positions with 1's.
	// //to compute distance with.
	// x.add(j);
	// y.add(i);
	//					
	// //Look for top/bottom/right/left positions around the current pixel
	// position
	// //with 1's if found any 0's, distance for that position to
	// //nearest 1's will be '1' so put that.
	// if(j-1>=0)
	// {
	// if(img.getPixel(i, j-1) == false)
	// result.setElement(1,i,j-1);
	// }
	// if(j+1<width)
	// {
	// if(img.getPixel(i, j+1) == false)
	// result.setElement(1,i,j+1);
	// }
	// if(i-1>=0)
	// {
	// if(img.getPixel(i-1, j) == false)
	// result.setElement(1,i-1,j);
	// }
	// if(i+1<height)
	// {
	// if(img.getPixel(i+1, j) == false)
	// result.setElement(1,i+1,j);
	// }
	// }
	// }
	// }
	// //Possible is no pixel position has 1's, in that case
	// //all are 0's and all have distance 0 as no 1's exist.
	// if(x.isEmpty())
	// return new Cell(height,width);
	//		
	// int size = x.size();
	// int[] X = new int[size];
	// int[] Y = new int[size];
	//		
	// for(int i=0;i<size;i++)
	// {
	// X[i] = x.get(i).intValue();
	// Y[i] = y.get(i).intValue();
	// }
	//		
	// for(int i=0;i<result.getRowCount();i++)
	// {
	// for(int j=0;j<result.getColCount();j++)
	// {
	// //Do only for pixels which is not 1's with distance 0
	// //or 0's top/bottom/left/right to 1's with distance 1.
	// //This two are identified in earlier loop so skip them.
	// if(result.getElement(i,j) == 0)
	// {
	// float minDistance = Float.MAX_VALUE;
	//					
	// for(int k=0;k<X.length;k++)
	// {
	// //find minimum of city block distance from all 1's in the image.
	// float distance = abs(j-X[k]) + abs(i-Y[k]);
	// minDistance = (distance <minDistance)?distance:minDistance;
	// }
	//					
	// result.setElement(minDistance, i,j);
	// }
	// else if(result.getElement(i,j) == -1)
	// {
	// //replaces all -1(temporarily kept in place of 0 to avoid
	// //confusion with position with default 0 in array) with 0.
	// result.setElement(0,i,j);
	// }
	// }
	// }
	//		
	// return result;
	//		
	// }

	// public Cell euclidian(BinaryPixelImage img)
	// {
	// int height = img.getHeight();
	// int width = img.getWidth();
	//		
	// Cell result = new Cell(height, width);
	//		
	// //To store x and y positions of all pixels with 1's
	// //for computing distances later on.
	// ArrayList<Integer> x = new ArrayList<Integer>();
	// ArrayList<Integer> y = new ArrayList<Integer>();
	//		
	// for(int i=0;i<result.getRowCount();i++)
	// {
	// for(int j=0;j<result.getColCount();j++)
	// {
	// //set distance 0 for pixel with 1's.
	// //To differentiate between default 0
	// //values in array with the position with
	// //0 distance, put -1 instead of 0 temporarily.
	// if(img.getPixel(i, j) == true)
	// {
	// result.setElement(-1,i,j);
	// //Store x y positions for all positions with 1's.
	// //to compute distance with.
	// x.add(j);
	// y.add(i);
	//					
	// //Look for top/bottom/right/left positions around the current pixel
	// position
	// //with 1's if found any 0's, distance for that position to
	// //nearest 1's will be '1' so put that.
	// if(j-1>=0)
	// {
	// if(img.getPixel(i, j-1) == false)
	// result.setElement(1,i,j-1);
	// }
	// if(j+1<width)
	// {
	// if(img.getPixel(i, j+1) == false)
	// result.setElement(1,i,j+1);
	// }
	// if(i-1>=0)
	// {
	// if(img.getPixel(i-1, j) == false)
	// result.setElement(1,i-1,j);
	// }
	// if(i+1<height)
	// {
	// if(img.getPixel(i+1, j) == false)
	// result.setElement(1,i+1,j);
	// }
	// }
	// }
	// }
	//		
	// //Possible is no pixel position has 1's, in that case
	// //all are 0's and all have distance 0 as no 1's exist.
	// if(x.isEmpty())
	// return new Cell(height,width);
	//		
	// int size = x.size();
	// int[] X = new int[size];
	// int[] Y = new int[size];
	//		
	// for(int i=0;i<size;i++)
	// {
	// X[i] = x.get(i).intValue();
	// Y[i] = y.get(i).intValue();
	// }
	//		
	// for(int i=0;i<result.getRowCount();i++)
	// {
	// for(int j=0;j<result.getColCount();j++)
	// {
	// //Do only for pixels which is not 1's with distance 0
	// //or 0's top/bottom/left/right to 1's with distance 1.
	// //This two are identified in earlier loop so skip them.
	// if(result.getElement(i,j) == 0)
	// {
	// float minDistance = Float.MAX_VALUE;
	//					
	// for(int k=0;k<X.length;k++)
	// {
	// //find minimum of distance from all 1's in the image.
	// //distance square is taken instead of distance to
	// //speed up processing.
	// float distance = (j-X[k])*(j-X[k]) + (i-Y[k])*(i-Y[k]);
	// minDistance = (distance <minDistance)?distance:minDistance;
	// }
	//					
	// //put square root of minimum distance square to get distance.
	// result.setElement((float) Math.sqrt(minDistance), i,j);
	// }
	// else if(result.getElement(i,j) == -1)
	// {
	// //replaces all -1(temporarily kept in place of 0 to avoid
	// //confusion with position with default 0 in array) with 0.
	// result.setElement(0,i,j);
	// }
	// }
	// }
	//		
	// return result;
	//		
	// }
}
