package org.JMathStudio.ImageToolkit.TransformTools.RadonToolKit;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;
import org.JMathStudio.SignalToolkit.GeneralTools.Conv1DTools;

/**
 * This class define a two dimensional Radon transform and its inverse on a discrete real image.
 * A discrete real image will be represented as a {@link Cell}. 
 * <p>The forward radon transform takes parallel beam projections of a discrete real image at different angles. 
 * The projections are the integral of the image pixels over a set of equi-spaced discrete 
 * straight lines. 
 * <p>The inverse radon transform back projects the projections using Fourier back projection,
 * and estimate the original image.
 * <h4>Assumptions</h4>
 * <p>1. All projections are for parallel beam geometry with image centrally located
 * on the origin with Y and X direction coinciding with image (Cell) rows and
 * columns respectively. 
 * <p>2. The image kernel has unit spatial separation along Y and X axis.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * RadonTransform rt = new RadonTransform();//Create an instance of RadonTransform.
 * 
 * VectorStack projections = rt.project(img, 360);//Compute projections of input image for
 * given number of angles.
 * 
 * Cell result = rt.reconstruct(projections);//Reconstruct image from its projections.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 **/
public final class RadonTransform{
	
	private final float SCALE = 3F;
	
	/**
	 * This method will take the parallel beam projections of the discrete real image as
	 * represented by the Cell 'cell' at different angles and return the projections as a VectorStack. 
	 * <p>The dimension of the Cell 'cell' should be more than 3 else this method will throw
	 * an IllegalArgument Exception.
	 * <p>The argument 'angles' specify the numbers of equi-spaced angles in the range of [0 PI) 
	 * for which projections are to be computed. For example, if argument 'angles' is '4',projections will be 
	 * computed for angles: 0,PI/4,PI/2,3*PI/4. The argument 'angles' should be more than '0' else 
	 * this method will throw an IllegalArgument Exception.
	 * <p>The computed projections will be return as a VectorStack where each index
	 * Vector will represent the projection for corresponding angle in counter
	 * clockwise increasing order.
	 * <p>For each projection Vector, the index position will corresponds to the -Nr/2 to
	 * Nr/2 projection rays; where Nr is the total number of projection rays per projection.
	 * <p>Number of projection rays (Nr) per projection is taken to be the Ceil value of the root 2 times
	 * maximum dimension of the image. If calculated Nr is not an odd number Nr is taken as Nr+1. 
	 * Thus this odd number of projection rays will now span index position from -Nr/2 to Nr/2 with
	 * central projection ray passing through the origin for all angles. In short, each projection Vector 
	 * will be of length Nr.
	 * <p>The total number of Vectors in the return VectorStack will equal to the
	 * argument 'angles' and length of each Vector will be same and equal to the
	 * number of projection rays i.e. Nr.
	 * 
	 * @param Cell
	 *            cell
	 * @param int angles
	 * @return {@link VectorStack}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack project(Cell cell, int angles)throws IllegalArgumentException {
		if (angles < 1)
			throw new IllegalArgumentException();

		int height = cell.getRowCount();
		int width = cell.getColCount();

		int noOfRays = height > width ? height : width;

		noOfRays = (int) (Math.ceil(noOfRays * MathUtils.SQRT_OF_TWO));

		if (noOfRays % 2 == 0)// added to make number of rays odd.
			noOfRays = noOfRays + 1;

		// angle 0 to pi, -rays 0 to rays.
		float[][] result = new float[angles][noOfRays];
		//float[][] norm = new float[angles][noOfRays];
		float[][] buffer = cell.accessCellBuffer();
		
		// -N -> 0 -> N, N = (noRays-1)/2 for odd noRays
		int oneHalfRays = (noOfRays - 1) / 2; // Now rays <=
		// Modulus(oneHalfRays)

		float piByAngles = (float) (Math.PI / angles);
		float piby4 = (float) (Math.PI / 4);
		// Make PI/4 and 3PI/4 projection check false i.e -1 here
		// projections are poor at this angle so need to skip them.
		int _PIby4Proj = -1;
		int _3PIby4Proj = -1;

		float cY = (float) ((height - 1) / 2.0);
		float cX = (float) ((width - 1) / 2.0);

		//Pre-compute the (y,x) geometric location of each (i,j) index element in Cell.
		float[] yc = new float[height];
		float[] xc = new float[width];

		for(int i=0;i<yc.length;i++)
			yc[i] = i-cY;
		for(int j=0;j<xc.length;j++)
			xc[j] = j-cX;

		float part;
		int index;
		
		for (int a = 0; a < angles; a++) {
			float theta = piByAngles * a;

			if (theta == piby4) {
				// PI/4 projection is there make check true i.e angle
				// index here also skip taking projection for this angle.
				_PIby4Proj = a;
				continue;
			} else if (theta == 3 * piby4) {
				// 3PI/4 projection is there make check true i.e angle
				// index here also skip taking projection for this angle.
				_3PIby4Proj = a;
				continue;
			}

			float cosTheta = (float) Math.cos(theta);
			float sinTheta = (float) Math.sin(theta);

			for (int i = 0; i < height; i++) {
				float y = (yc[i]) * cosTheta;

				for (int j = 0; j < width; j++) {
					// Rotate Pixel position and get its new Y position.
					float Y = y + (xc[j] * sinTheta);

					// Find two nearest Rays index.
					int projLower = (int) Math.floor(Y);
					int projUpper = (int) Math.ceil(Y);

					// If rotated point falls on integer Y position so it falls
					// exactly on a Ray with that index.
					if (projLower == projUpper) {
						if (projLower >= -oneHalfRays
								& projLower <= oneHalfRays) {
							index = oneHalfRays + projLower;
							result[a][index] += buffer[i][j];
							//norm[a][index]+=1;//Part will be 1 i.e full pixel contribution.
						}
					} else// Interpolate the pixel value between the two nearest
						// Rays.
					{
						if (projLower >= -oneHalfRays
								& projLower <= oneHalfRays) {
							index = oneHalfRays + projLower;
							part = (projUpper - Y);
							result[a][index] += part*buffer[i][j];
							//norm[a][index]+=part;
						}
						if (projUpper >= -oneHalfRays
								& projUpper <= oneHalfRays) {
							index = oneHalfRays + projUpper;
							part = (Y - projLower);
							result[a][index] += part*buffer[i][j];
							//norm[a][index]+= part;
						}
					}

				}
			}
		}

		// If this projection was present so was skipped, get this projection by
		// taking
		// average of the nearest two angle projections. a-1 and a+1 index.
		if (_PIby4Proj != -1) {
			int preProj = _PIby4Proj - 1;
			int nxtProj = _PIby4Proj + 1;

			if (preProj > 0 & nxtProj < angles) {
				for (int i = 0; i < result[_PIby4Proj].length; i++) {
					result[_PIby4Proj][i] = (result[preProj][i] + result[nxtProj][i]) / 2;

				}
			} else if (preProj > 0 & nxtProj >= angles) {
				for (int i = 0; i < result[_PIby4Proj].length; i++) {
					result[_PIby4Proj][i] = result[preProj][i];
				}
			} else {
				for (int i = 0; i < result[_PIby4Proj].length; i++) {
					result[_PIby4Proj][i] = result[nxtProj][i];
				}
			}

		}

		// If this projection was present so was skipped, get this projection by
		// taking
		// average of the nearest two angle projections. a-1 and a+1 index.
		if (_3PIby4Proj != -1) {
			int preProj = _3PIby4Proj - 1;
			int nxtProj = _3PIby4Proj + 1;

			if (preProj > 0 & nxtProj < angles) {
				for (int i = 0; i < result[_3PIby4Proj].length; i++) {
					result[_3PIby4Proj][i] = (result[preProj][i] + result[nxtProj][i]) / 2;

				}
			} else if (preProj > 0 & nxtProj >= angles) {
				for (int i = 0; i < result[_3PIby4Proj].length; i++) {
					result[_3PIby4Proj][i] = result[preProj][i];
				}
			} else {
				for (int i = 0; i < result[_3PIby4Proj].length; i++) {
					result[_3PIby4Proj][i] = result[nxtProj][i];
				}
			}

		}

		VectorStack stack = new VectorStack();

		for (int i = 0; i < result.length; i++)
			stack.addVector(new Vector(result[i]));

		return stack;

	}

	/**
	 * This method will reconstruct the discrete real image from its projections
	 * as given by the VectorStack 'projections' and return the reconstructed
	 * image as Cell.
	 * <p>Each Vector of the VectorStack 'projections' in increasing index order represent 
	 * the projections of the original image taken at an equi-spaced angle in the range [0 PI)
	 * in counter clockwise increasing order. Thus number of projection vectors specify the 
	 * number of equi-spaced angles for which projections were taken.
	 * <p>The number of projection vectors in the VectorStack 'projection' should
	 * be at least one else this method will throw an IllegalArgument Exception.
	 * <p>All the projection Vectors within the VectorStack 'projections' should be
	 * of same length i.e. projection rays 'Nr' for projections at all angles should be the same
	 * else this method will throw an IllegalArgument Exception.
	 * <h4>Algorithm</h4>
	 * <p>
	 * <i>1. This method make use of the filtered back projection(FBP) algorithm for reconstructing the 
	 * image from its projections. A suitable ramlak filter is applied to the projections
	 * before back projecting the same.
	 * <p>
	 * 2. As projections were taken at dimensions larger than the original image so as to cover
	 * whole image for all angles; reconstruction will only reconstruct that central square
	 * portion of the filtered back projection buffer, whose dimension will be equal to Floor(Nr/root(2)).
	 * <p>
	 * 3. This method will apply a correction for preventing the values of the reconstructed image from 
	 * getting over shoot by computing the average back projected value per pixel position. </i>
	 * <p>
	 * The reconstructed image will be return as a Cell.
	 * 
	 * @param VectorStack
	 *            projections
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell reconstruct(VectorStack projections)throws IllegalArgumentException {

		int rays = projections.accessVector(0).length();
		int angles = projections.size();

		// Find Maximum dimension of the to be Original Image.
		// Nos of projections were approximately equal to Ceil of Maximum
		// Dimension times root 2.
		// So to be reconstructed image should be square with this Maximum
		// dimension of the original image.
		int ROI = (int) Math.floor(rays / MathUtils.SQRT_OF_TWO);

		if (angles < 1)
			throw new IllegalArgumentException();

		float[][] radon;
				
		// This will also check if all projections are of same length.
		try {
			// Apply Ramlak filter to all the projections.
			radon = RadonUtilities.f1(projections, 2*rays).toCell().accessCellBuffer();
		} catch (DimensionMismatchException e) {
			throw new IllegalArgumentException();
		}

		float[][] result = new float[ROI][ROI];
		//float[][] norm = new float[ROI][ROI];
		
		int noRaysby2 = (rays - 1) / 2;// Now Rays will be <= Modules(noRayby2)

		float PIperAngle = (float) (Math.PI / angles);
		int C = (int) ((ROI - 1) / 2.0);
		
		//Pre-compute the (yc,xc) geometric location of each (i,j) index element in Kernel.
		//(yc,xc) = (i-C,j-C) -> put origin at centre of kernel.
		
		//As Kernel is square, only compute centred 'yc' values. The same can be used
		//for 'xc'.
		
		float[] yOrxC = new float[ROI];
					
		for(int i=0;i<yOrxC.length;i++)
			yOrxC[i] = i-C;
				
		float Y;
		int projLower, projUpper;
		
		//For each angle, pre-computes calues for yc*costheta and xc*sintheta.
		float[][] ymap = new float[angles][ROI];
		float[][] xmap = new float[angles][ROI];
		
		for(int a=0;a<angles;a++)
		{
			float theta = (float) (PIperAngle * a);
			float cosTheta = (float) Math.cos(theta);
			float sinTheta = (float) Math.sin(theta);
			
			//As kernel is square with dimension equal to "ROI" only one loop
			//will do.
			for(int i=0;i<ROI;i++){
				ymap[a][i]=yOrxC[i]*cosTheta;
				xmap[a][i]=yOrxC[i]*sinTheta;
			}
				
		}
		
		float part;
		
		for (int a = 0; a < angles; a++) {
			for (int i = 0; i < ROI; i++) {
				for (int j = 0; j < ROI; j++) {
					// Rotate Pixel position and get its new Y position.
					//Y = yc*costheta + xc*sintheta.
					
					Y = (float) (ymap[a][i] + xmap[a][j]);

					// Find two nearest Rays index.
					projLower = (int) Math.floor(Y);
					projUpper = (int) Math.ceil(Y);

					// If rotated point falls on integer Y position so it falls
					// exactly on a Ray with that index so back project that ray
					// value to this pixel position.
					if (projLower == projUpper) {
						if (projLower >= -noRaysby2 && projLower <= noRaysby2) {
							result[i][j] += radon[a][projLower + noRaysby2];
							//norm[i][j]+=1;
						}
					} else// Interpolate back projection from two nearest Rays
							// to this pixel.
					{
						if (projLower >= -noRaysby2 && projLower <= noRaysby2) {
							part = (projUpper - Y);
							result[i][j] += part*radon[a][projLower+noRaysby2];
							//norm[i][j]+=part;
						}
						if (projUpper >= -noRaysby2 && projUpper <= noRaysby2) {
							part = (Y - projLower);
							result[i][j] += part*radon[a][projUpper+noRaysby2];
							//norm[i][j]+=part;
						}

					}

				}

			}
		}

		final float correction = SCALE/angles;
		
		for(int i=0;i<ROI;i++)
		{
			for(int j=0;j<ROI;j++)
			{
				result[i][j]*= correction;
			}
		}
		
		try {
			return new Cell(result);
		} catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}

}

/**
 * This class define utility functions which are required by the Radon
 * Transform.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
final class RadonUtilities {
	
	final static VectorStack f1(VectorStack projections,
			int ramlakPoints) throws IllegalArgumentException {
		int projLength = projections.accessVector(0).length();
		VectorStack res = new VectorStack();
		
		for (int i = 1; i < projections.size(); i++) {
			if (projections.accessVector(i).length() != projLength)
				throw new IllegalArgumentException();
		}
		
		Conv1DTools conv = new Conv1DTools();

		try {
			Vector ram = f0(ramlakPoints);
			
			for (int i = 0; i < projections.size(); i++) {
				Vector tmp = projections.accessVector(i);
				res.addVector(conv.linearConvSameWithoutFFT(tmp, ram));
			}
			
			return res;
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method creates a Ram-Lak kernel of length as given by the argument
	 * 'N' and return the same as a Vector.
	 * <p>
	 * The argument 'N' should not be less than 3 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	final static Vector f0(int N) throws IllegalArgumentException {
		int check = (N - 1) / 2;

		if (check < 1) {
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];
		float piSquare = (float) (Math.PI * Math.PI);

		for (int i = 0; i < result.length; i++) {
			int index = i - (N - 1) / 2;

			if (index == 0) {
				result[i] = 1.0f / 4.0f;
			} else if (Math.abs(index) % 2 == 0) {
				result[i] = 0;
			} else {
				result[i] = (float) (-1.0f / (index * index * piSquare));
			}

		}

		return new Vector(result);
	}

}

//class ProjEngine3 extends AbstractProjectionEngine
//{
//
//	public VectorStack project(Cell cell, int angles)throws IllegalArgumentException
//	{
//		if (angles < 1)
//			throw new IllegalArgumentException();
//
//		int height = cell.getRowCount();
//		int width = cell.getColCount();
//
//		int noOfRays = height > width ? height: width;
//
//		noOfRays = (int) (Math.ceil(noOfRays * MathUtils.SQRT_OF_TWO));
//
//		if(noOfRays%2 ==0)//added to make number of rays odd.
//			noOfRays = noOfRays+1;
//
//		// angle 0 to pi, -rays 0 to rays.
//		float[][] result = new float[angles][noOfRays];
//
//		//-N -> 0 -> N, N = (noRays-1)/2 for odd noRays
//		int oneHalfRays = (noOfRays-1)/2; //Now rays <= Modulus(oneHalfRays)
//
//
//		float cY = (height-1.0f)/2.0f;
//		float cX = (width-1.0f)/2.0f;
//
//		float deltaAngle = (float) (Math.PI/angles);
//		float piby4 = (float) (Math.PI/4);
//		float piBy2 = (float)(Math.PI/2);
//
//		//Make PI/4 and 3PI/4 projection check false i.e -1 here
//		//projections are poor at this angle so need to skip them.
//		int _PIby4Proj = -1;
//		int _3PIby4Proj = -1;
//		
//		float minX = -cX;
//		float maxX = cX;
//
//		float minY = -cY;
//		float maxY = cY;
//
//		for (int a = 0; a < angles; a++)
//		{
//			float theta = (float) (deltaAngle*a);
//			//This step needed to bring the radon output consistent with other algorithms.
//			//else the angle dimension is shifted by the factor of PI/2 as compare to the
//			//outcomes from other radon algorithms.
//			theta = piBy2 - theta;
//			
//			if(theta == piby4)
//			{
//				//PI/4 projection is there make check true i.e angle
//				//index here also skip taking projection for this angle.
//				_PIby4Proj = a;
//				
//			}
//			else if(theta == 3*piby4)
//			{
//				//3PI/4 projection is there make check true i.e angle
//				//index here also skip taking projection for this angle.
//				_3PIby4Proj = a;
//				
//			}
//			else if(theta == piBy2)
//			{
//				for(int y=0;y<cell.getRowCount();y++)
//				{
//					int N = (int) (y-cY);
//					for(int x=0;x<cell.getColCount();x++)
//					{
//						result[a][N+oneHalfRays]+=cell.getElement(y,x);
//					}
//				}				
//
//			}
//			else if(theta == 0)
//			{
//				for(int x=0;x<cell.getColCount();x++)
//				{
//					int N = (int) (x-cX);
//					for(int y=0;y<cell.getRowCount();y++)
//					{
//						result[a][N+oneHalfRays]+=cell.getElement(y,x);
//					}
//				}				
//
//			}
//			else
//			{
//				float cosTheta = (float) Math.cos(theta);
//				float sinTheta = (float) Math.sin(theta);
//				
//				float tanTheta = (float) Math.tan(theta);//sinTheta/cosTheta;
//				float cotTheta = (float) (1/Math.tan(theta));//cosTheta/sinTheta;
//
//				float x1=0,x2=0,y1=0,y2=0;
//				float swap;
//
//				for(int N = -oneHalfRays;N<=oneHalfRays;N++)
//				{
//					float C = N/sinTheta;
//					float D = N/cosTheta;
//
//					y1 = (C + cX*cotTheta);
//					y2 = (C - cX*cotTheta);
//					
//					if(y1 < minY)
//						y1 = minY;
//					else if(y1 > maxY)
//						y1 = maxY;
//
//					if(y2 < minY)
//						y2 = minY;
//					else if(y2 > maxY)
//						y2 = maxY;
//
//					x1 = (D - y1*tanTheta);
//					x2 = (D - y2*tanTheta);
//
//					if(x1 > x2)
//					{
//						swap = x1;
//						x1 = x2;
//						x2 = swap;
//					}
//										
//					x1 = MathUtils.roundOff(x1,2);
//					x2 = MathUtils.roundOff(x2,2);
//
//					if(x1 < minX || x2 > maxX)
//						continue;
//											
//					if(y2 == y1)
//						continue;
//					
//					//For situation where, x2 and x1 are very near ie for angle 0.
//					//But still we need to parse through all values 'y' for small
//					//x window. If we kept count=1 i.e x++ we will cover no pixels
//					//at angle near to 0 as x2 ~= x1.
//					//So set count proportional to distance between x2 & x1.
//					float slope = Math.abs(x2-x1)/Math.abs(y2-y1);
//					float count = Math.abs(slope*cosTheta);
//					
//					
//					for(float x=x1;x<=x2;x=x+count)
//					{
//						float y = (C -x*cotTheta);
//						int i = (int) Math.round(y+cY);
//
//						//int i = (int) (index+cY);
//						int j = Math.round(x+cX);
//						//System.out.println(index+" "+x+" "+cY+" "+cX);
//						result[a][N+oneHalfRays] += cell.getElement(i,j);
//
//					}
//				}
//			}
//		}
//		
//		//If this projection was present so was skipped, get this projection by
//		//taking average of the nearest two angle projections. a-1 and a+1 index.
//		if(_PIby4Proj != -1)
//		{
//			int preProj = _PIby4Proj-1;
//			int nxtProj = _PIby4Proj+1;
//
//			if(preProj>0 & nxtProj<angles)
//			{
//				for(int i=0;i<result[_PIby4Proj].length;i++)
//				{
//					result[_PIby4Proj][i] = (result[preProj][i] + result[nxtProj][i])/2;
//
//				}
//			}
//			else if(preProj >0 & nxtProj>=angles)
//			{
//				for(int i=0;i<result[_PIby4Proj].length;i++)
//				{
//					result[_PIby4Proj][i] = result[preProj][i];
//				}
//			}
//			else
//			{
//				for(int i=0;i<result[_PIby4Proj].length;i++)
//				{
//					result[_PIby4Proj][i] = result[nxtProj][i];
//				}
//			}
//
//		}
//		
//		//If this projection was present so was skipped, get this projection by
//		//taking average of the nearest two angle projections. a-1 and a+1 index.
//		if(_3PIby4Proj != -1)
//		{
//			int preProj = _3PIby4Proj-1;
//			int nxtProj = _3PIby4Proj+1;
//
//			if(preProj>0 & nxtProj<angles)
//			{
//				for(int i=0;i<result[_3PIby4Proj].length;i++)
//				{
//					result[_3PIby4Proj][i] = (result[preProj][i] + result[nxtProj][i])/2;
//
//				}
//			}
//			else if(preProj >0 & nxtProj>=angles)
//			{
//				for(int i=0;i<result[_3PIby4Proj].length;i++)
//				{
//					result[_3PIby4Proj][i] = result[preProj][i];
//				}
//			}
//			else
//			{
//				for(int i=0;i<result[_3PIby4Proj].length;i++)
//				{
//					result[_3PIby4Proj][i] = result[nxtProj][i];
//				}
//			}
//
//		}
//
//		VectorStack stack = new VectorStack();
//
//		for (int i = 0; i < result.length; i++)
//			stack.addVector(new Vector(result[i]));
//
//		return stack;
//	}
//}