package modelo;

import javafx.embed.swing.SwingNode;
import javafx.scene.Node;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class Graphic {
    private JFreeChart graphic;
    private String title;
    private String lableX;
    private String lableY;
    XYSeriesCollection collectionData= new XYSeriesCollection();

    public Graphic(String title, String lableX, String lableY) {
        this.title = title;
        this.lableX = lableX;
        this.lableY = lableY;
        graphic = ChartFactory.createXYLineChart(title,lableX,lableY,collectionData,
                PlotOrientation.VERTICAL,true,true,true);
    }

    public Graphic(){
        this("Sin titulo...", "X", "Y");
    }

    public void addGraphic(String id, double xValues[], double yValues[]){
        XYSeries serie = new XYSeries(id);
        int numValues = xValues.length;
        for (int i = 0; i < numValues; i++)
            serie.add(xValues[i], yValues[i]);
        collectionData.addSeries(serie);
    }

    public void createGraphic(String id, double xValues[], double yValues[]){
        collectionData.removeAllSeries();
        addGraphic(id,xValues,yValues);
    }

    public JPanel getGraphic(){
        return new ChartPanel(graphic);
    }
}
