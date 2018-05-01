package org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.Utilities.WindowFactory;

/**
 * This class defines an AnIsotropic Gaussian filter. AnIsotropic gaussian filter involve a 2D gaussian kernel
 * with different standard deviation along its principle axis and oriented at some angle theta, giving anisotropic
 * behaviour.
 * <p>This class implements fast algorithm where an 2D anisotropic gaussian filtering is implemented in two steps:
 * <p>a. 1D gaussian filtering along x-axis with some standard deviation followed by, 
 * <p>b. 1D gaussian filtering along some nonorthogonal direction with some standard deviation.  
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * float sigmaX = 3;//Parameters for Gaussian anisotropic kernel.
 * float sigmaY = 2;
 * float theta = 45;
 * 
 * GaussianAnIsotropicFilter gaf = new GaussianAnIsotropicFilter(sigmaX,sigmaY,theta);//Create
 * an instance of GaussianAnIsotropicFilter with given parameters.
 * 
 * Cell result = gaf.filter(img);//Apply filter on the input image.
 * </pre>
 *@author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public class GaussianAnIsotropicFilter {

	private float i2;
	private float i1;
	private float i7;

	private float i0;

	private float[] gaussX;
	private float[] gaussT;

	/**
	 * This will create an AnIsotropic gaussian filter with 2D gaussian kernel whose standard deviation along its
	 * x and y axis is as given by the arguments 'sigmaX' & 'sigmaY' respectively with gaussian kernel 'x' axis 
	 * oriented at an angle as given by the argument 'theta' with the image 'x' axis.
	 * <p>The arguments 'sigmaX' and 'sigmaY' should be more than or equal to 1 else this method will throw an
	 * IllegalArgument Exception.
	 * <p>The argument 'theta' should be in the range of [0 180)degrees else this method will thrown an IllegalArgument
	 * Exception.
	 * <p>The dimensions of the gaussian kernel is calculated automatically based upon the standard deviation along its axis.
	 * @param float sigmaX
	 * @param float sigmaY
	 * @param float theta
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public GaussianAnIsotropicFilter(float sigmaX,float sigmaY,float theta) throws IllegalArgumentException{

		//Limit theta to [0 180) as full 360 degree is not required as gaussian blob is
		//symmetrical. 
		if(sigmaX <1 || sigmaY <1 || theta <0 || theta >=180)
			throw new IllegalArgumentException();
		else
		{
			this.i2 = sigmaX;
			this.i1 = sigmaY;
			this.i7 = theta;

			f9();
		}
	}

	/**
	 * This method will apply the given AnIsotropic gaussian filter on the discrete real image as
	 * represented by {@link Cell} 'image' and return the resultant image as Cell.
	 * @param Cell image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell filter(Cell image){

		int rc = image.getRowCount();
		int cc = image.getColCount();

		Cell result = new Cell(rc,cc);
		MatrixTools mt = new MatrixTools();

		try{
			//Convolve along rows: X direction.
			for(int i=0;i<rc;i++){
				Vector tmp = f1(image.accessRow(i), gaussX);
				result.assignRow(tmp, i);
			}
		}catch(DimensionMismatchException e){
			e.printStackTrace();
			throw new BugEncounterException();
		}

		Cell X = result.clone();
		result = mt.transpose(result);
		//As image is transposed prior to this loop, convolution on row with slope
		//is equal to convolution along columns at slope in original image.
		for(int j=0;j<cc;j++){
			//Convolve along T direction.
			f0(j, X, result, gaussT, i0);
		}

		return mt.transpose(result);
	}

	private void f9(){
		float angle = (float) ((this.i7 / 180.0f) * Math.PI);
		float cosTheta = (float) Math.cos(angle);
		float sinTheta = (float) Math.sin(angle);

		float norm = (float) Math.sqrt(i1*i1*cosTheta*cosTheta+i2*i2*sinTheta*sinTheta);
		//if sx and sy cannot be zero, norm cannot be zero.
		if(norm == 0)
			throw new BugEncounterException();

		//As sx & sy are >0 and so is norm, ux > 0.
		float ux = (i2*i1)/(norm);

		i0 = (norm*norm)/((i2*i2 - i1*i1)*cosTheta*sinTheta);
		double shi = Math.atan(i0);

		//as, -1 >=Math.sin()<=1, ut can be negative.
		//However, as ut is standard deviation i.e width of gaussian blob
		//take absolute of the same.
		//Sign indicate orientation/direction and comes due to equation.
		float ut = (float) Math.abs(norm/Math.sin(shi));

		//if ux and ut are very small or 0, length of kernel will be 0.
		//prevent this case.
		//Further, for any ux or ut, length of kernel is kept at 4 times.
		int Nx = (ux <= 1)?5: f5(4*ux);
		int Nt = (ut <= 1)?5: f5(4*ut);

		try{
			gaussX = WindowFactory.gaussian(Nx, ux, (Nx-1)/2).accessVectorBuffer();
			gaussT = WindowFactory.gaussian(Nt, ut, (Nt-1)/2).accessVectorBuffer();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			throw new BugEncounterException();
		}	

		float sumX=0,sumT=0;

		//Normalise gaussian kernels.
		for(int i=0;i<gaussX.length;i++)
			sumX+=gaussX[i];
		for(int i=0;i<gaussT.length;i++)
			sumT+=gaussT[i];

		for(int i=0;i<gaussX.length;i++)
			gaussX[i]/=sumX;
		for(int i=0;i<gaussT.length;i++)
			gaussT[i]/=sumT;
	}

	//Convolve Yth row of an image with impulse at a slope and store result in result. 
	private void f0(int Y, Cell image, Cell result, float[] impulse,float u){
		int rc = image.getRowCount();
		int _cc = image.getColCount()-1;
		int l2 = impulse.length;
		float[][] data = image.accessCellBuffer();

		int shift = (l2 - 1) / 2;

		try{
			for (int x = shift; x < rc + shift; x++) {
				float tmp = 0;
				int i1 = x;
				int i2 = 0;
				//-shift >= j <= shift -- along columns.
				float j = -shift;
				float y;

				while (i1 >= 0 & i2 < l2) {
					//As convolution is done at a slope for a raw, the y does not remain
					//fix for Yth row but changes as per the slope equation.
					y = Y + j/u;
					if (i1 < rc && y >=0 && y<_cc)
						tmp += f8(data,i1,y) * impulse[i2];
					i1--;
					i2++;
					j++;
				}

				result.setElement(tmp, Y,x - shift);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			throw new BugEncounterException();
		}
	}

	private float f8(float[][] data,int Y, float X)
	{
		int xl = (int) Math.floor(X);
		int xh = (int) Math.ceil(X);

		if(xl==xh)
			return data[Y][xl];
		else 
			return data[Y][xl]*(xh-X) + data[Y][xh]*(X-xl);
	}

	private Vector f1(Vector signal, float[] impulse) {
		int l1 = signal.length();
		int l2 = impulse.length;

		Vector result = new Vector(l1);
		int shift = (l2 - 1) / 2;

		for (int x = shift; x < result.length() + shift; x++) {
			float tmp = 0;
			int i1 = x;
			int i2 = 0;

			while (i1 >= 0 & i2 < l2) {
				if (i1 < l1)
					tmp += signal.getElement(i1) * impulse[i2];
				i1--;
				i2++;
			}

			result.setElement(tmp, x - shift);

		}

		return result;

	}

	private int f5(float value){
		int n = (int) Math.round(value);

		return (n%2==0)?n+1:n;
	}
}
