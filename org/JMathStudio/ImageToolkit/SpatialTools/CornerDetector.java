package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.ImageToolkit.Utilities.KernelFactory;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various Corner detection operations on a discrete real
 * image.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * CornerDetector cd = new CornerDetector();//Create an instance of  CornerDetector.
 * 
 * Cell result = cd.plesseyOperator(img, N, K);//Apply plessey operator to detect corner
 * points in input image with suitable parameters.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class CornerDetector {

	/**
	 * This method define the Plessey Operator for detection and estimation of
	 * the corner points in a discrete real image as represented by the Cell
	 * 'cell'.
	 * <p>
	 * The returned Cell contents the corresponding cornerness values for each
	 * pixel position in the original image which indicates the objective
	 * evidence of the present of a corner at that location in the original
	 * image.
	 * <p>
	 * The argument 'N' specify the width of the gaussian window in sample
	 * points employed for defining the neighbourhood around a point (Ideal
	 * value is 3). The argument 'N' should be positive odd number else this
	 * method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'K' specify a constant for Plessey Operator. Ideally it should
	 * be in the range of [0 1].
	 * 
	 * @param Cell
	 *            cell
	 * @param int N
	 * @param float K
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see #moravecOperator(Cell, int)
	 * @see #trajkovicOperator(Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell plesseyOperator(Cell cell, int N, float K)throws IllegalArgumentException
	{
		if (N <= 0 || (N % 2) == 0) {
			throw new IllegalArgumentException();
		}
		float[][] window = KernelFactory.gaussianKernel(N, N, N / 3f).accessCellBuffer();
		int shift = (N - 1) / 2;

		int height = cell.getRowCount();
		int width = cell.getColCount();

		Cell result = new Cell(height, width);
		int X,Y,x,y;
		float A,B,C;
		int xp,yl,xl,yp;
		float dx,dy;
		boolean isdx,isdy;
		float cornerness;
		
		for (int i = 0; i < height; i++) 
		{
			Y = i-shift;
			for (int j = 0; j < width; j++) 
			{
				A = 0;
				B = 0;
				C = 0;
				X = j-shift;
				
				for (int k = 0; k < N; k++) 
				{
					for (int l = 0; l < N; l++) 
					{
						x = X + l;
						y = Y + k;

						xp = x+1;
						xl = x-1;
						yp = y+1;
						yl = y-1;
						dx=dy=0;
						isdx=isdy=false;
						
						if (x >= 0 && x < width && y >= 0 && y < height) {
														
							if (xp >= 0 && xp < width && xl >=0 && xl <width) {
								isdx=true;
								dx = (cell.getElement(y, xp) - cell.getElement(y, xl));
								A += window[k][l] * dx * dx;
							}

							if (yp >=0 && yp < height && yl >= 0 && yl < height) {
								isdy=true;
								dy = (cell.getElement(yp, x) - cell.getElement(yl, x));
								B += window[k][l] * dy * dy;
							}
							
							if (isdx && isdy) {
								C += window[k][l] * dx * dy;
							}
						}

					}
				}

				//Use variable dx instead of a new variable. Save memory.
				dx = A+B;
				cornerness = Math.abs(A * B - C * C) - K * (dx)*(dx);
    			result.setElement(Math.abs(cornerness), i, j);
			}
		}

		return result;

	}

	/**
	 * This method define the Moravec Operator for detection and estimation of
	 * the corner points in a discrete real image as represented by the Cell
	 * 'cell'.
	 * <p>
	 * The returned Cell contents the corresponding cornerness values for each
	 * pixel position in the original image which indicates the objective
	 * evidence of the present of a corner at that location in the original
	 * image.
	 * <p>
	 * The argument 'N' specify the width of the gaussian window in sample
	 * points employed for defining the neighbourhood around a point (Ideal
	 * value is 3). The argument 'N' should be positive odd number else this
	 * method will throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param int N
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @see #plesseyOperator(Cell, int, float)
	 * @see #trajkovicOperator(Cell)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell moravecOperator(Cell cell, int N)throws IllegalArgumentException {
		
		if (N <= 0 || (N % 2) == 0) {
			throw new IllegalArgumentException();
		}

		float[][] window = KernelFactory.gaussianKernel(N, N, N / 3f).accessCellBuffer();
		int shift = (N - 1) / 2;

		int height = cell.getRowCount();
		int width = cell.getColCount();

		Cell result = new Cell(height, width);
		int X,x,Y,y;
		int xp,xl,yp,yl;
		float ele;
		float n,e,s,w,ne,nw,se,sw;
		boolean left,right,top,bottom;
		float diff=0;
		
		for (int i = 0; i < height; i++) 
		{
			Y = i - shift;
			for (int j = 0; j < width; j++) 
			{
				e = 0;
				w = 0;
				s = 0;
				n = 0;
				ne = 0;
				nw = 0;
				se = 0;
				sw = 0;

				X = j-shift;

				for (int k = 0; k < N; k++) 
				{
					for (int l = 0; l < N; l++) 
					{
						x=X+l;
						y=Y+k;
						if (x >= 0 && x < width && y >= 0 && y < height)
						{
							xp = x+1;
							yp = y+1;
							xl = x-1;
							yl = y-1;
							ele = cell.getElement(y,x);
							
							left=right=top=bottom=false;

							if(xp>=0 && xp <width)
								right=true;
							if(xl>=0 && xl <width)
								left=true;
							if(yp>=0 && yp<height)
								top=true;
							if(yl>=0 && yp<height)
								bottom=true;
							
							diff=0;
							if(right){
								diff = (cell.getElement(y, xp) - ele);
								e += window[k][l] * diff * diff;
							}
							if(left){
								diff = (cell.getElement(y, xl) - ele);
								w += window[k][l] * diff * diff;
							}
							if(bottom){
								diff = (cell.getElement(yl, x) - ele);
								n += window[k][l] * diff * diff;
							}
							if(top){
								diff = (cell.getElement(yp, x) - ele);
								s += window[k][l] * diff * diff;
							}
							if(right && bottom){
								diff = (cell.getElement(yl, xp) - ele);
								ne += window[k][l] * diff * diff;
							}
							if(right && top){
								diff = (cell.getElement(yp, xp) - ele);
								se += window[k][l] * diff * diff;
							}
							if(left && bottom){
								diff = (cell.getElement(yl, xl) - ele);
								nw += window[k][l] * diff * diff;
							}
							if(left && top){
								diff = (cell.getElement(yp, xl) - ele);
								sw += window[k][l] * diff * diff;
							}
						}

					}
				}

				float min1,min2;
				float T1,T2;
				float cornerness;

				if(n<s)
					min1 = n;
				else 
					min1 = s;

				if(e<w)
					min2 = e;
				else
					min2 = w;

				if(min1 < min2)
					T1 = min1;
				else
					T1 = min2;

				if(ne<nw)
					min1 = ne;
				else
					min1 = nw;
				if(se<sw)
					min2 = se;
				else
					min2 = sw;

				if(min1 < min2)
					T2 = min1;
				else
					T2 = min2;

				if(T1 < T2)
					cornerness = T1;
				else
					cornerness = T2;

				result.setElement(cornerness, i, j);

			}
		}

		return result;
	}


	/**
	 * This method define the Trajkovic 4-neighbourhood Operator for detection
	 * and estimation of the corner points in a discrete real image as
	 * represented by the Cell 'cell'.
	 * <p>
	 * The returned Cell contents the corresponding cornerness values for each
	 * pixel position in the original image which indicates the objective
	 * evidence of the present of a corner at that location in the original
	 * image.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @see #moravecOperator(Cell, int)
	 * @see #plesseyOperator(Cell, int, float)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell trajkovicOperator(Cell cell) {

		int height = cell.getRowCount();
		int width = cell.getColCount();

		Cell result = new Cell(height, width);
		int xl,xp,yl,yp;
		float IC,IR,IL,IT,IB;
		float B1,B2,rA,rB;
		float A,B,C;
		float cornerness;
		
		float IT_IR,IT_IL,IT_IC,IB_IR,IB_IC,IB_IL,IR_IC,IL_IC;
		
		for (int y = 0; y < height; y++) 
		{
			for (int x = 0; x < width; x++) 
			{
				xl=x-1;
				xp=x+1;
				yl=y-1;
				yp=y+1;
				
				IC = cell.getElement(y, x);
				IR = IC;
				IL = IC;
				IT = IC;
				IB = IC;

				if (xp >= 0 && xp < width)
					IR = cell.getElement(y, xp);
				if (xl >= 0 && xl < width)
					IL = cell.getElement(y, xl);
				if (yp >= 0 && yp < height)
					IB = cell.getElement(yp, x);
				if (yl >= 0 && yl < height)
					IT = cell.getElement(yl, x);

				IT_IR = IT-IR;
				IT_IL = IT-IL;
				IB_IR = IB-IR;
				IB_IC = IB-IC;
				IB_IL = IB-IL;
				IR_IC = IR-IC;
				IL_IC = IL-IC;
				IT_IC = IT-IC;
				
				B1 = (IT_IR) * (IR_IC) + (IB_IL) * (IL_IC);
				B2 = (IT_IL) * (IL_IC) + (IB_IR) * (IR_IC);
				rA = (IR_IC) * (IR_IC) + (IL_IC) * (IL_IC);
				rB = (IT_IC) * (IT_IC) + (IB_IC) * (IB_IC);

				C = rA;
				B = B1 < B2 ? B1 : B2;
				A = rB - rA - 2 * B;

				if (B < 0 && A + B > 0) {
					cornerness = C - (B * B / A);
					result.setElement(Math.abs(cornerness), y, x);
				} else {
					cornerness = rA < rB ? rA : rB;
					result.setElement(Math.abs(cornerness), y, x);
				}

			}
		}

		return result;
	}

}
