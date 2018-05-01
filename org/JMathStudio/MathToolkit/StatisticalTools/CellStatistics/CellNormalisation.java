package org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.DivideByZeroException;

/**
 * This class define various Normalisation operations on the elements of a {@link Cell}.
 * <pre>Usage:
 * Let 'a' be a valid Cell object.
 * 
 * CellNormalisation cn = new CellNormalisation();//Create an instance of CellNormalisation.
 * 
 * Cell norm_energy = cn.normaliseEnergy(a);//Normalise input Cell so as to have unit energy.
 * Cell norm_amp = cn.normaliseAmplitudeRange(a);//Normalise input Cell so as to have amplitude
 * in the range of [-1 1].
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class CellNormalisation {
	
	private CellStatistics stat;
	
	/**
	 * Constructor of the class.
	 */
	public CellNormalisation(){
		stat = new CellStatistics();
	}
	
	/**
	 * This method will normalise the statistics, i.e. Mean =0 and Standard Deviation =1, of the elements of the
	 * {@link Cell} 'cell' and return the statistically normalised Cell.
	 * <p>Elements of such a statistically normalised Cell will have a zero mean and an unit standard deviation.
	 * <p>If the given Cell 'cell' has zero standard deviation than statistical normalisation is not possible. 
	 * In such a scenario this method will throw a DivideByZero Exception.
	 * <p> Statistical normalisation is carried out as follows,
	 * <p><i> NX = (X - mean)/std.
	 * <p>where, NX is the statistical normalised element.
	 * <p>X is the pre normalised element.
	 * <p>'mean' and 'std' are the mean and standard deviation respectively of the Cell to be statistically normalised. 
	 * </i>
	 * <p>A statistically normalised Cell elements will have a zero mean and an unit standard deviation.
	 * @param Cell cell
	 * @return Cell
	 * @throws DivideByZeroException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell normaliseStatistics(Cell cell) throws DivideByZeroException
	{
		float mean = stat.mean(cell);
		float std = stat.standardDeviation(cell);
		
		if(std == 0)
			throw new DivideByZeroException();
		else
		{
			Cell result = new Cell(cell.getRowCount(),cell.getColCount());
			
			for(int i=0;i<result.getRowCount();i++)
			{
				for(int j=0;j<result.getColCount();j++)
				{
					result.setElement((cell.getElement(i,j)-mean)/std,i,j);
				}
			}
			
			return result;
		}
	}

	
	/**
	 * This method will normalise the Amplitude range of the elements of the {@link Cell} 'cell' to the range 
	 * of [-1 1].
	 * <p>The resultant {@link Cell} with normalised amplitude range will be return by this method.
	 * <p>Amplitude range normalisation linearly maps the elements of the Cell 'cell' to
	 * a range of [-1 1], by dividing all the elements with the maximum absolute valued  
	 * element of the Cell. 
	 * <p>Thus the resultant return Cell will have all the elements within the range of [-1 1].
	 * @param Cell cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell normaliseAmplitudeRange(Cell cell)
	{	
		float max = Math.abs(cell.getElement(0,0));

		for (int i = 0; i < cell.getRowCount(); i++) 
		{
			for(int j=0;j< cell.getColCount();j++)
			{
				float tmp = Math.abs(cell.getElement(i,j));
				max = tmp > max ? tmp : max;
			}
		}

		Cell result = new Cell(cell.getRowCount(),cell.getColCount());
		
		if(max == 0)
		{
			return result;
		}
		else
		{						
			for(int i=0;i<result.getRowCount();i++)
			{
				for(int j=0;j<result.getColCount();j++)
				{
					float tmp = cell.getElement(i,j)/max;
					result.setElement(tmp, i, j);
				}
			}
			
			return result;
		}
	}

	/**
	 * This method will normalise the Energy content of the elements of the {@link Cell} 'cell' and return the
	 * resultant normalised energy Cell with a unit energy content.
	 * <p>
	 * A normalise energy Cell has an energy of 1 unit.
	 * <p>If the {@link Cell} 'cell' has zero energy, this method will return the clone of the original
	 * zero energy Cell.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public Cell normaliseEnergy(Cell cell) 
	{
		float factor = (float) Math.sqrt(stat.energy(cell));
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		if (factor == 0) 
		{
			return result;
		} else 
		{			
			for (int i = 0; i < result.getRowCount(); i++) {
				for (int j = 0; j < result.getColCount(); j++) {
					result.setElement(cell.getElement(i, j) / factor, i, j);
				}
			}

			return result;
		}
	}

	/**
	 * This method will normalise the Absolute sum of the elements of the {@link Cell} 'cell' and
	 * return the normalised absolute sum Cell.
	 * <p>A normalised absolute sum Cell has the sum of absolute value of all its elements '1'.
	 * <p>A normalised absolute sum Cell is obtained by dividing all the elements of the Cell by
	 * its absolute sum.
	 * <p>If the absolute sum of the Cell 'cell' is '0', this method will return the clone of the original Cell.
	 * @param Cell cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell normaliseAbsoluteSum(Cell cell)
	{
		float absSum = stat.absoluteSum(cell);
		
		Cell result = new Cell(cell.getRowCount(),cell.getColCount());
		
		//No need to clone the original Cell 'cell' as absolute sum is zero means all
		//the elements are '0', thus create a new same dimension zero Cell and return it.
		//which will be same as the original Cell.
		if(absSum == 0)
			return result;
		else
		{
		for(int i=0;i<result.getRowCount();i++)
		{
			for(int j=0;j<result.getColCount();j++)
			{
				result.setElement(cell.getElement(i,j)/absSum,i,j);
			}
		}
		
			return result;
		}
	}
}
