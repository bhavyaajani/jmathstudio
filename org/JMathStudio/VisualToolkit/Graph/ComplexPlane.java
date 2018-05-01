package org.JMathStudio.VisualToolkit.Graph;

import javax.swing.JFrame;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.VisualToolkit.Graph.PtPlot.PtPlot;

/**
 * This class define a Complex Plane graph for plotting {@link Complex} numbers on a
 * complex plane.
 * <p>A Complex plane is a 2 dimensional plane with 'X' axis representing the real axis and 
 * 'Y' axis representing the imaginary axis.
 * <p>Thus a complex number (a + ib) is plotted on the plane at a point (a,b) with 'X' 
 * (real) value 'a' and 'Y' (imaginary) value 'b'.  
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ComplexPlane extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1212534320748318692L;
	/**
	 * Default height for Complex plane graph.
	 */
	public final static int DEFAULT_HEIGHT = 300;
	/**
	 * Default width for Complex plane graph.
	 */
	public final static int DEFAULT_WIDTH = 300;
	
	final static String DEFAULT_FRAME_TITLE="JMathStudio";
	
	final static String DEFAULT_PLOT_TITLE="Complex Plane";
	final static String DEFAULT_X_LEGEND="Real Axis";
	final static String DEFAULT_Y_LEGEND="Imaginary Axis";
	
	private PtPlot cplot;
	
	private float realMin = Float.MAX_VALUE;
	private float realMax = -Float.MAX_VALUE;
	private float imgMin = Float.MAX_VALUE;
	private float imgMax = -Float.MAX_VALUE;
	
	/**
	 * This will create a Complex Plane graph frame with default dimensions.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ComplexPlane()
	{
		this(DEFAULT_WIDTH,DEFAULT_HEIGHT);
	}
	
	/**
	 * This will create a Complex Plane graph frame with width and height as specified 
	 * by the arguments 'width' and 'height' respectively.
	 * @param int width
	 * @param int height
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ComplexPlane(int width,int height) {
		super(DEFAULT_FRAME_TITLE);
		
		setSize(width, height);
		
		cplot = new PtPlot();
		
		cplot.setTitle(DEFAULT_PLOT_TITLE);
		cplot.setXLabel(DEFAULT_X_LEGEND);
		cplot.setYLabel(DEFAULT_Y_LEGEND);
		
		cplot.setDoubleBuffered(true);
		
		//Keep button disabled as we do not want user to change the type of plot.
		cplot.setButtons(false);

		//Default for Complex Plane graph. Do not change with out impact on OM and
		//API DOC.
		forceToPointPlot();
		
		getContentPane().add(cplot);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		setVisible(true);
	}

	/**
	 * This method will plot all the {@link Complex} numbers within the {@link CVector} 'vector'
	 * on the given complex plane graph. 
	 * 
	 * <p>The 'X' and 'Y' axis bounds for the plane will be computed automatically so as to
	 * accomodate all Complex numbers on the graph.
	 * 
	 * @param CVector vector
	 * 			
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(CVector vector) {
		
		// Clears currently plotted plot only if false.
		this.cplot.clear(false);

		for(int i=0;i<vector.length();i++)
		{
			Complex value = vector.getElement(i);
			float real = value.getRealPart();
			float img = value.getImaginaryPart();
			
			if(real > realMax)
				realMax = real;
			else if(real < realMin)
				realMin = real;
			
			if(img > imgMax)
				imgMax = img;
			else if(img < imgMin)
				imgMin = img;
			
			this.cplot.addPoint(0, real,img, false);
		}
		
		this.cplot.setXRange(realMin, realMax);
		this.cplot.setYRange(imgMin, imgMax);
		
		this.cplot.fillPlot();
	}

	private void forceToPointPlot() {
		
		this.cplot.setConnected(false);
		this.cplot.setBars(false);
		this.cplot.setImpulses(false);
		this.cplot.setMarksStyle("points");

	}
	
	/**
	 * This will reset the title of the Complex plane graph to as specified by the argument 'title'.
	 * @param String title
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphTitle(String title)
	{
		this.cplot.setTitle(title);
	}
	/**
	 * This will reset the X axis legend for the Complex plane graph to as specified by the
	 * argument 'xLegend'.
	 * @param String xLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphXLegend(String xLegend)
	{
		this.cplot.setXLabel(xLegend);
	}
	/**
	 * This will reset the Y axis legend for the Complex plane graph to as specified by the
	 * argument 'yLegend'.
	 * @param String yLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphYLegend(String yLegend)
	{
		this.cplot.setYLabel(yLegend);
	}
	
	/**
	 * This method will clear all current points on the Complex plane graph.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void clearGraph()
	{
		this.cplot.clear(false);
		this.cplot.repaint();
	}
}
