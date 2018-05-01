package org.JMathStudio.VisualToolkit.Graph;

import javax.swing.JFrame;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.VisualToolkit.Graph.PtPlot.PtPlot;

/**
 * This class define a Graph frame for plotting 1D data or signal as represented by a {@link Vector}
 * as a graph with different supported graph style. 
 *<p>
 * This class support graph display for a single {@link Vector} or a multiple {@link Vector}s with
 * different graph style. 
 * <p>Following Graph style are supported by this class:
 *<p><i>
 * LineGraph - The line graphs are continuous graph with consecutive points connected by a 
 * line in a graph.
 *<p>
 * ImpulseGraph - The impulse graphs are stem graph with each point plotted as an impulse on
 * a graph.
 *<p>
 * BarGraph - The bar graphs has rectangular bars for each point plotted on the graph.
 *<p></i>
 *This class make use of Ptolemy or PtPlot SDK API. 
 *@author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class VectorGraph extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2934449429545915494L;

	/**
	 * Default height of the Graph frame.
	 */
	public final static int DEFAULT_HEIGHT = 300;
	/**
	 * Default width of the Graph frame.
	 */
	public final static int DEFAULT_WIDTH = 500;
	
	final static String DEFAULT_FRAME_TITLE="JMathStudio";
	
	final static String DEFAULT_GRAPH_TITLE="Graph";
	final static String DEFAULT_X_LEGEND="X";
	final static String DEFAULT_Y_LEGEND="Y";
	
	final private PtPlot plot;
	
	/**
	 * This will create a Vector Graph frame with default format.
	 * <p>The default format specify the default dimensions of the graph and set the graph
	 * style to LineGraph.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorGraph()
	{
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * This will create a Vector Graph frame with width and height as specified by the arguments
	 * 'width' and 'height' respectively.
	 * <p>The default graph style is set to LineGraph.
	 * @param int width
	 * @param int height
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorGraph(int width,int height)
	{
		super(DEFAULT_FRAME_TITLE);
		
		setSize(width, height);
		
		plot = new PtPlot();
		plot.setTitle(DEFAULT_GRAPH_TITLE);
	    plot.setXLabel(DEFAULT_X_LEGEND);
		plot.setYLabel(DEFAULT_Y_LEGEND);
		
		plot.setDoubleBuffered(true);
		plot.setButtons(true);

		//Default graph style. Do not change without updating doc or API doc.
		setToLineGraph();
		
		getContentPane().add(plot);
	
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		
		setVisible(true);
	}

	/**
	 * This method will plot all the Vectors given by the VectorStack 'YVectorStack' as a mixed stack graph.
	 * <p>Each Vector of the 'YVectorStack' will be plotted as an independent graph (with a unique colour),
	 * where each element of the Vector is plotted against its index position in that Vector.
	 * <p>X axis will represent the index positions starting from '0'. 
	 * <p>This call will clear and overwrite the existing graph.
	 * @param VectorStack
	 *            YVectorStack
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(VectorStack YVectorStack) {
		
		// Clears currently plotted plot only if false.
		plot.clear(false);

		for (int series = 0; series < YVectorStack.size(); series++) {
			for (int index = 0; index < YVectorStack.accessVector(series).length(); index++) {
				plot.addPoint(series, index, YVectorStack.accessVector(series).getElement(index), plot
						.getConnected());
			}
		}
		plot.fillPlot();
	}

	/**
	 * This method will plot the Vector 'YVector' on the graph frame.
	 * <p>
	 * Each element of the 'YVector' will be plotted against its index position
	 * in the Vector.
	 * <p>This call will clear and overwrite the existing graph.
	 * @param Vector
	 *            YVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(Vector YVector) {
		
		this.plot(new VectorStack(new Vector[] { YVector }));
	}

	/**
	 * This method will plot the elements of Vector 'YVector' against the corresponding elements
	 * of the Vector 'XVector' on the graph frame.
	 * <p>
	 * The length of both the Vectors should be the same else this method will
	 * throw an DimensionMismatch Exception.
	 * <p>This call will clear and overwrite the existing graph.
	 * @param Vector
	 *            YVector
	 * @param Vector
	 *            XVector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(Vector YVector, Vector XVector)
			throws DimensionMismatchException {
		VectorStack YVectorStack = new VectorStack(new Vector[] { YVector });
		VectorStack XVectorStack = new VectorStack(new Vector[] { XVector });

		this.plot(YVectorStack, XVectorStack);

	}

	/**
	 * This method will plot all the Vectors given by the VectorStack 'YVectorStack'
	 * against the reference Vector 'XVector' as a Mixed Stack graph.
	 * <p>
	 * Each Vector of the 'YVectorStack' will be plotted independently (with unique colour) against 
	 * the Vector 'XVector' on the graph frame.
	 * <p>
	 * The length of each Vector of the VectorStack 'YVectorStack' should be the
	 * same as that of the Vector 'XVector' else this method will throw an
	 * DimensionMismatch Exception.
	 * <p>This call will clear and overwrite the existing graph.
	 * @param VectorStack
	 *            YVectorStack
	 * @param Vector
	 *            XVector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(VectorStack YVectorStack, Vector XVector)
			throws DimensionMismatchException {
		float[] XSeries = XVector.accessVectorBuffer();

		for (int yIndex = 0; yIndex < YVectorStack.size(); yIndex++) {
			if (XSeries.length != YVectorStack.accessVector(0).length()
					|| YVectorStack.accessVector(yIndex).length() != YVectorStack.accessVector(0).length())
				throw new DimensionMismatchException();
		}
		// Clears currently plotted plot only if false.
		plot.clear(false);

		for (int series = 0; series < YVectorStack.size(); series++) {
			for (int index = 0; index < YVectorStack.accessVector(series).length(); index++) {
				plot.addPoint(series, XSeries[index], YVectorStack.accessVector(series).getElement(index),
						plot.getConnected());
			}
		}
		plot.fillPlot();

	}

	/**
	 * This method will plot all the Vectors given by the VectorStack 'YVectorStack'
	 * against the corresponding Vectors given by the VectorStack 'XVectorStack'
	 * as a Mixed Stack graph.
	 * <p>
	 * The size of both the VectorStack should be the same else this method will
	 * throw an DimensionMismatch Exception.
	 * <p>
	 * Each Vector of the 'YVectorStack' will be plotted independently against the
	 * corresponding reference Vector from the 'XVectorStack' (with unique colour)
	 * on the graph frame.
	 * <p>
	 * The length of each Vector of the VectorStack 'YVectorStack' should be
	 * same as that of the corresponding Vector in VectorStack 'XVectorStack'
	 * else this method will throw an DimensionMismatch Exception.
	 * <p>This call will clear and overwrite the existing graph.
	 * @param VectorStack
	 *            YVectorStack
	 * @param VectorStack
	 *            XVectorStack
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot(VectorStack YVectorStack, VectorStack XVectorStack)
			throws DimensionMismatchException {
		
		if (YVectorStack.size() != XVectorStack.size())
			throw new DimensionMismatchException();

		for (int index = 0; index < YVectorStack.size(); index++) {
			if (XVectorStack.accessVector(index).length() != YVectorStack.accessVector(index).length())
				throw new DimensionMismatchException();
		}
		// Clears currently plotted plot only if false.
		plot.clear(false);

		for (int series = 0; series < YVectorStack.size(); series++) {
			for (int index = 0; index < YVectorStack.accessVector(series).length(); index++) {
				plot.addPoint(series, XVectorStack.accessVector(series).getElement(index),
						YVectorStack.accessVector(series).getElement(index), plot.getConnected());
			}
		}
		plot.fillPlot();

	}

	/**
	 * This method will set the Graph style to LineGraph.
	 * <p>
	 * Current and all the subsequent graphs will be of this style.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setToLineGraph() {
		plot.setConnected(true);
		plot.setBars(false);
		plot.setImpulses(false);

		plot.fillPlot();
	}

	/**
	 * This method will set the Graph style to ImpulseGraph.
	 * <p>
	 * Current and all the subsequent graphs will be of this style.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setToImpulseGraph() {
		plot.setConnected(false);
		plot.setBars(false);
		plot.setImpulses(true);

		plot.fillPlot();
	}

	/**
	 * This method will set the Graph style to BarGraph.
	 * <p>
	 * Current and all the subsequent graphs will be of this style.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setToBarGraph() {
		plot.setConnected(false);
		plot.setBars(true);
		plot.setImpulses(false);

		plot.fillPlot();
	}
	
	/**
	 * This will reset the title of the graph to as specified by the argument 'title'.
	 * @param String title
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphTitle(String title)
	{
		plot.setTitle(title);
	}
	/**
	 * This will reset the X axis legend for the graph to as specified by the
	 * argument 'xLegend'.
	 * @param String xLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphXLegend(String xLegend)
	{
		plot.setXLabel(xLegend);
	}
	/**
	 * This will reset the Y axis legend for the graph to as specified by the
	 * argument 'yLegend'.
	 * @param String yLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphYLegend(String yLegend)
	{
		plot.setYLabel(yLegend);
	}
	
	/**
	 * This method will clear all current graphs on the graph frame.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void clearGraph()
	{
		plot.clear(false);
		plot.repaint();
	}
}
