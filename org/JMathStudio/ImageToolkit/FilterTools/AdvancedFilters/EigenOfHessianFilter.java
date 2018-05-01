package org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.JAMA.EigenvalueDecompositionAdapter;
import org.JMathStudio.MathToolkit.MatrixTools.JAMA.MatrixAdapter;

/**
 * This class define an Eigen of Hessian filter. This filter estimates 2nd order hessian matrix at each pixel
 * position within the image (considered as 2D real function f(y,x)) and computes principal eigen values of 
 * this hessian matrix.
 * <p>This filter is useful in estimating the vessel like or blob like structures within the image.
 * <p>The discrete real image will be represented by {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("image_path");//Import external image as Cell.
 * 
 * EigenOfHessianFilter ehf = new EigenOfHessianFilter(1,1);//Create an instance of EigenOfHessianFilter
 * with unit spacing along both direction.
 * 
 * CellStack eigen_maps = ehf.filter(img);//Apply filter on the input image.
 * 
 * eigen_maps.accessCell(0).showAsUInt8Image("Eigen 0 Map");
 * eigen_maps.accessCell(1).showAsUInt8Image("Eigen 1 Map");//Display computed eigen of hessian
 * maps.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public class EigenOfHessianFilter {

	private float i4;
	private float i1;
	private float i3;
	private float i0;
	private float i8;
	
	/**
	 * This will create an EigenOfHessianFilter with 'y' & 'x' image spacing as given
	 * by the arguments 'ys' & 'xs' respectively.
	 * <p>Both the arguments 'ys' & 'xs' should be more than 0 else this constructor will
	 * thrown an IllegalArgument Exception.
	 * <p>The arguments 'ys' & 'xs' are used in estimating the 2nd order derivatives for 
	 * hessian matrix.  
	 * @param float ys
	 * @param float xs
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public EigenOfHessianFilter(float ys, float xs) throws IllegalArgumentException{
		if(ys <=0 || xs <=0)
			throw new IllegalArgumentException();
		else{		
			this.i4 = xs;
			this.i1 = ys;
			f0();
		}
	}
	
	/**
	 * This will apply the Eigen of Hessian filter on the discrete real image as represented by {@link Cell}
	 * 'image' and return the map of computed eigen values of the 2nd order hessian matrix computed at each 
	 * pixel position within the image, as a {@link CellStack}.
	 * <p>The size of return CellStack will be 2, where 0th and 1st Cell respectively gives the
	 * eigen maps for the 2 principal eigen values. The dimensionality of both the eigen maps will be same as that
	 * of the input image. The corresponding elements in the 0th and 1st eigen maps gives 2 principal eigen
	 * values of the 2nd order hessian matrix computed at the corresponding pixel location within the input
	 * image.
	 * <p>The filter output is sensitive to the 'y' & 'x' image spacing. The 'y' & 'x' spacing could be reset 
	 * by {@link #resetYSpacing(float)} & {@link #resetXSpacing(float)} methods respectively. 
	 * @param Cell image
	 * @return CellStack
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack filter(Cell image){

		try{
			int height = image.getRowCount();
			int width = image.getColCount();

			Cell eigen1 = new Cell(height,width);
			Cell eigen2 = new Cell(height,width);

			for(int i=0;i<height;i++)
			{
				for(int j=0;j<width;j++)
				{
					float c = image.getElement(i, j);
					float n,s,e,w,ne,nw,se,sw;

					int y1 = i - 1;
					int y2 = i + 1;
					int x1 = j - 1;
					int x2 = j + 1;

					if (y1 >= 0){
						n = image.getElement(y1, j);
					}
					else{
						n = image.getElementWithPadding(y1, j);
					}
					if (y2 < height){
						s = image.getElement(y2, j);
					}
					else{
						s = image.getElementWithPadding(y2, j);
					}
					if (x1 >= 0){
						w = image.getElement(i, x1);
					}
					else{
						w = image.getElementWithPadding(i, x1);
					}
					if (x2 < width){
						e = image.getElement(i, x2);
					}
					else{
						e = image.getElementWithPadding(i, x2);
					}

					if (y1 >= 0 && x1 >= 0)
						nw = image.getElement(y1, x1);
					else
						nw = image.getElementWithPadding(y1, x1);

					if (y1 >= 0 && x2 < width)
						ne = image.getElement(y1, x2);
					else
						ne = image.getElementWithPadding(y1, x2);

					if (y2 < height && x1 >= 0)
						sw = image.getElement(y2, x1);
					else
						sw = image.getElementWithPadding(y2, x1);

					if (y2 < height && x2 < width)
						se = image.getElement(y2, x2);
					else
						se = image.getElementWithPadding(y2, x2);

					double[][] hessian = f1(nw, n, ne, w, c, e, sw, s, se);
					float[][] D = f3(hessian).accessCellBuffer();
					
					eigen1.setElement(D[0][0], i, j);
					eigen2.setElement(D[1][1], i, j);
				}
			}
			CellStack eigen = new CellStack(2);
			eigen.addCell(eigen1);
			eigen.addCell(eigen2);

			return eigen;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will reset the horizontal 'x' spacing for computation to value as given by 
	 * the argument 's'.
	 * <p>The argument 's' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * @param float s
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetXSpacing(float s) throws IllegalArgumentException{
		if(s <=0)
			throw new IllegalArgumentException();
		else{
			this.i4 = s;
			f0();
		}
	}
	
	/**
	 * This method will reset the vertical 'y' spacing for computation to value as given by 
	 * the argument 's'.
	 * <p>The argument 's' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * @param float s
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetYSpacing(float s) throws IllegalArgumentException{
		if(s <=0)
			throw new IllegalArgumentException();
		else{
			this.i1 = s;
			f0();
		}
	}

	private Cell f3(double[][] matrix){
		MatrixAdapter adapter = new MatrixAdapter(matrix);
		EigenvalueDecompositionAdapter eigen = new EigenvalueDecompositionAdapter(adapter);

		return eigen.getDAdapter();
	}

	private double[][] f1(float nw,float n,float ne,float w,float c,float e,float sw,float s,float se){
		double[][] hessian = new double[2][2];
		
		//TBD: Should remove 4 from denominator ?.
		//Redo numerical partial derivative research.
		//FXX
		hessian[0][0] = (e-2*c+w)/i3;
		//FYX
		hessian[0][1] = (ne-nw-se+sw)/i8;
		//FXY
		hessian[1][0] = hessian[0][1];//d2f/dxdy = d2f/dydx		
		//FYY
		hessian[1][1] = (n-2*c+s)/i0;

		return hessian;
	}

	private void f0(){
		this.i3 = i4*i4;
		this.i0 = i1*i1;
		this.i8 = 4*i4*i1;
	}
}
