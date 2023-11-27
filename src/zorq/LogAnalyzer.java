package zorq;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public final class LogAnalyzer extends JPanel {
	
	private String logName;
	private Map<String,Map<String,ArrayList<Object>>> data;
	
  public LogAnalyzer(String logName) {
  	this.logName = logName;
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		makeData();
		Iterator keys = data.keySet().iterator();
		
		while(keys.hasNext()){
			String next  = (String)keys.next();
			if(data.get(next).get("kills").size() != 0)
				pieDataset.setValue(next, data.get(next).get("kills").size());
		}

		JFreeChart chart = ChartFactory.createPieChart3D
		                     ("Kills",   // Title
		                      pieDataset,           // Dataset
		                      true,                  // Show legend  
		                      true,
		                      true
		                     );
		
		chart.getPlot().setForegroundAlpha(.7f);
                    
		BufferedImage image = chart.createBufferedImage(500,300);

		JLabel lblChart = new JLabel();
		lblChart.setIcon(new ImageIcon(image));
		
		add(lblChart);
		
		//==========================================================================
		/*
		pieDataset = new DefaultPieDataset();
		
		
		keys = data.keySet().iterator();
		
		while(keys.hasNext()){
			String next  = (String)keys.next();
			
			ArrayList times = data.get(next).get("calculationTimes");
			
			double average = 0;
			
			for(int i = 0; i < times.size(); i++){
				average+= Double.parseDouble((String)times.get(i));
			}
			
			average/=times.size();

			pieDataset.setValue(next, average);
		}
		
		chart = ChartFactory.createPieChart
		    ("Average Decision Time",   // Title
		     pieDataset,           // Dataset
		     false,                  // Show legend  
		     false,
		     false
		    );
		image = chart.createBufferedImage(500,300);
		
		lblChart = new JLabel();
		lblChart.setIcon(new ImageIcon(image));
		
		
		add(lblChart);
		*/
		//=========================================================================
		
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		
		keys = data.keySet().iterator();
		
		while(keys.hasNext()){
			String next  = (String)keys.next();
			
			ArrayList times = data.get(next).get("calculationTimes");
			
			double average = 0;
			
			for(int i = 0; i < times.size(); i++){
				average+= Double.parseDouble((String)times.get(i));
			}
			
			average/=times.size();

			categoryDataset.setValue(average,next,"");
		}
		
		chart = ChartFactory.createBarChart(
				"Decision Time",
				"Ship",
				"Average Time (ns)",
				categoryDataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				true);
		
		image = chart.createBufferedImage(500,300);
		
		lblChart = new JLabel();
		lblChart.setIcon(new ImageIcon(image));
		
		add(lblChart);
}



/** Returns an ImageIcon, or null if the path was invalid. */
protected static ImageIcon createImageIcon(String path,
                                           String description) {
    java.net.URL imgURL = LogAnalyzer.class.getResource(path);
    if (imgURL != null) {
        return new ImageIcon(imgURL, description);
    } else {
        System.err.println("Couldn't find file: " + path);
        return null;
    }
}


public static void createAndShowGUI(String logName) {

    JFrame.setDefaultLookAndFeelDecorated(true);


    JFrame frame = new JFrame("LabelDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    LogAnalyzer newContentPane = new LogAnalyzer(logName);
    newContentPane.setOpaque(true); 
    frame.setContentPane(newContentPane);

    frame.pack();
    frame.setVisible(true);
}

private void makeData() {

		try{
			FileReader log = new FileReader(logName);
			
			BufferedReader logReader = new BufferedReader(log);
			
			 data = new HashMap<String,Map<String,ArrayList<Object>>>();
			
			String line;
			while((line = logReader.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line,"|");
				st.nextToken();
				String action = st.nextToken();
				if(action.equalsIgnoreCase("ShipCreate")){
					Map<String,ArrayList<Object>> shipData = new HashMap<String,ArrayList<Object>>();
					shipData.put("kills", new ArrayList<Object>());
					shipData.put("calculationTimes", new ArrayList<Object>());
					data.put(st.nextToken(),shipData);
				}
				else if(action.equalsIgnoreCase("ShipAction")){
					Map<String,ArrayList<Object>> shipData = data.get(st.nextToken());
					
					st.nextToken();
	  			shipData.get("calculationTimes").add(st.nextToken());
	
				}
				else if(action.equalsIgnoreCase("ShipDeath")){
					st.nextToken();
					String ship = st.nextToken();

					if(!ship.equalsIgnoreCase("null") && ship.indexOf("zorq") == -1 ){
						Map<String,ArrayList<Object>> shipData = data.get(ship);
						shipData.get("kills").add(new Object());
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          LogAnalyzer.createAndShowGUI("log/1143608239703.log");
      }
    });
	}

}
