package org.JMathStudio.VisualToolkit.Graph;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.VisualToolkit.Graph.PtPlot.PtPlot;

/**
 * This class define a Running Graph for plotting a stream of data in real time. The running
 * graphs are dynamic graphs which updates itself with every new data and are continuous in time.
 * <p>The data stream for the running graph will be captured from a class implementing {@link VectorDataStream}
 * interface. 
 * <p>This class make use of Ptolemy or PtPlot SDK API. 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class VectorRunningGraph implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3551835238960376695L;
	
	private final static int DEFAULT_HEIGHT = 300;
	private final static int DEFAULT_WIDTH = 700;
	
	/*Finite latency in refreshing the plot with given frame size.
	 *If framelength is changed the latency will need to be updated.
	 */
	private final int MINIMUM_DELAY_FOR_LATENCY = 100;
	private final static String DEFAULT_FRAME_TITLE = "JMathStudio";
	private final static String DEFAULT_PLOT_TITLE = "Running Graph";
	private final static String DEFAULT_Y_LEGEND = "Data";
	private final static String DEFAULT_X_LEGEND = "Index";
	
	/*The default running rate for the running plot is set to minimum
	 *latency interval.
	 */
	private int delay = MINIMUM_DELAY_FOR_LATENCY;
	/*
	 * Frame length define the number of data points plotted on the point at
	 * any instance of time.
	 */
	private int frameLength = 20;
	
	private PtPlot plot;
	private JFrame frame;
	
	/*
	 * FIFO buffer of the current data points plotted. Size equal to framelength.
	 */
	private float[] ybuffer;
	/*
	 * Variable to keep track of total data points plotted so far.
	 */
	private int count;
	/*
	 * Flag to point to the starting index in the FIFO buffer for next plot.
	 * The FIFO buffer is not created every time for new data but the last
	 * data is replaced by the new data and the starting offset is updated.
	 * The buffer is cyclicly shifted for each new data.
	 */
	private int index;
	/*
	 * The flag to specify the starting value for the 'x' variable in the plot
	 * for each new plot refresh. Equals count-1 as 'x' start from '0'.
	 */
	private int xStart;
	/*
	 * Flag to indicate the stop flag for the running graph. Until this flag is false
	 * the while loop for data polling will continue. 
	 */
	private boolean isStop=false;
	/*
	 * Thread to run the running graph.
	 */
    private Thread driver = null;
	
    private VectorDataStream stream;
    
    /**
     * This will create a new Running graph which will plot the data as available from the
     * {@link VectorDataStream} 'stream' in real time.
     * <p>The class maintain a FIFO data buffer of 20 data points which get updated at regular interval 
     * with the next data as captured from the VectorDataStream 'stream' and refresh the 
     * plot accordingly. If no next data is available from the 'stream' the graph shall update plot
     * with '0' by default. 
     * <p>The regular interval for update shall define the running rate or temporal rate of the
     * graph. The default running rate is set to 0.1 second. 
     * <p>The running graph will automatically update the 'y' axis range and fit the available data.
     * <p>The running graph will start only when the method {@link #start()} is called.
     * <p>The argument 'stream' is passed by reference and no deep copy of the same is made.
     * 
     * @param VectorDataStream stream
     * @author Ajani Bhavya - (jmathstudio@gmail.com)
     */
	public VectorRunningGraph(VectorDataStream stream){
		
		this.stream = stream;
		
		initGraph();		
	}
	
	private void initGraph(){
		
		/*
		 * FIFO buffer of length frame length.
		 */
		ybuffer = new float[frameLength];
		//Count of elements initially is zero.
		count=0;
		
		plot = new PtPlot();
		plot.setDoubleBuffered(true);
		plot.setButtons(true);
		plot.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		plot.setTitle(DEFAULT_PLOT_TITLE);
		plot.setXLabel(DEFAULT_X_LEGEND);
		plot.setYLabel(DEFAULT_Y_LEGEND);
		plot.disableZoomAndDragListeners();
		
		frame = new JFrame(DEFAULT_FRAME_TITLE);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		frame.getContentPane().add(plot);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		/*
		 * When the JFrame is closed, the running thread need to be disposed else
		 * thread will keep running in the background.
		 */
		frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        pause();		        	
		      }
		    });

	    frame.pack();
	    frame.setVisible(true);
	}
	
	/**
	 * This method will start the running graph. If the graph was paused this method will
	 * resume the graph from the last state.
	 * <p>When the running graph is started it will continuously pole the {@link VectorDataStream},
	 * at a rate as defined, for a new data and plot the same dynamically.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void start(){
		isStop = false;
		
		if(driver==null){
			driver = new Thread(this);
			driver.start();
		}		
	}
	
	/**
	 * This will pause the running graph which can be resumed by {@link #start()} method.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	@SuppressWarnings("deprecation")
	public void pause(){
		
		if(driver !=null){
			driver.stop();
			driver=null;
		}
		isStop=true;
	}
	
	/**
	 * This method will set the running rate in seconds for the running graph to
	 * 'rate'. Due to finite latency in refreshing the plot, the running rate should
	 * not be less than 0.1 seconds else this method will throw an IllegalArgument
	 * Exception.
	 * <p>The running rate can be updated dynamically.  
	 * @param float rate
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setRunningRate(float rate) throws IllegalArgumentException{
		/*
		 * Running rate in milli-seconds.
		 */		
		int _delay = Math.round(rate*1000);
		
		/*
		 * If running rate is less than Minimum latency throw exception and stop
		 * the graph.
		 */
		if(_delay < MINIMUM_DELAY_FOR_LATENCY){
			this.pause();
			throw new IllegalArgumentException();
		}
		else
			this.delay = _delay;
	}
	
	/**
	 * This will reset the title of the running graph to as specified by the argument 'title'.
	 * @param String title
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphTitle(String title)
	{
		plot.setTitle(title);
	}
	/**
	 * This will reset the X axis legend for the running graph to as specified by the
	 * argument 'xLegend'.
	 * @param String xLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphXLegend(String xLegend)
	{
		plot.setXLabel(xLegend);
	}
	/**
	 * This will reset the Y axis legend for the running graph to as specified by the
	 * argument 'yLegend'.
	 * @param String yLegend
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setGraphYLegend(String yLegend)
	{
		plot.setYLabel(yLegend);
	}
	
	private synchronized void updateForNewData(float y){
		updateDataBuffer(y);	
		refreshPlot();
	}
	
	/*
	 * Smart FIFO strategy for updating data buffer for every new data.
	 */
	private void updateDataBuffer(float y){
		
		/*
		 * If number of data are less than full frame length i.e. buffer is not filled
		 * fully as initially. No need to remove any data from FIFO.
		 */
		// 0 1 0 0 - index at 0; plot 0 1 0 0
		// 0 1 2 0 - index at 0; plot 0 1 2 0
		// 0 1 2 3 - index at 0; plot 0 1 2 3
		if(count < frameLength){
			index=0;
			//Put data in the FIFO.
			ybuffer[count] = y;
			//Update data count.
			count++;
			//xStart will always be '0'.
			
			xStart=0;
		}
		/*
		 * If number of data are more than the full frame length i.e the first data has
		 * to be removed from the FIFO.
		 */
		// 0 1 2 3 - index at 0; plot 0 1 2 3
		// 4 1 2 3 - index at 1; plot 1 2 3 4
		// 4 5 2 3 - index at 2; plot 2 3 4 5
		else{
			/*
			 * As data is more than buffer size, cyclicly move it around and super impose
			 * on the first data of the buffer i.e do FIFO.
			 * Note the first data does not mean at '0'th index but the oldest data in the
			 * buffer which will increasingly be located at the higher index in the array as
			 * more new data are cyclicly pushed in buffer.
			 * Thus count%frameLength gives the oldest data index in buffer. Replace it with
			 * new data.
			 */
			index = count%frameLength;
			ybuffer[index] = y;
			//Increment total data count till now.
			count++;
			//Starting 'x' position for plot will be count-1.
			xStart = count-1;
			index++;
		}
	}
	
	private void refreshPlot(){
		
		plot.clear(false);
		
		int x = xStart;
		/*
		 * For plot 4 5 2 3 with index at '2' plot from 2 to 3.
		 */
		for(int i=index;i<frameLength;i++){
			plot.addPoint(0, x++, ybuffer[i], true);
		}
		/*
		 * For plot 4 5 2 3 with index at '2' plot from 4 to 5.
		 */
		for(int i=0;i<index;i++){
			plot.addPoint(0, x++, ybuffer[i], true);
		}
				
		plot.fillPlot();
	}

	public void run() {
			
		while(!isStop){
			try {
				//Define running rate.
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(stream.hasNextData())
				updateForNewData(stream.getNextData());
			else//No data ? plot '0' so graph liveliness is maintain.
				updateForNewData(0);
			
		}
		
	}

}