package model;

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
    private XYSeriesCollection collectionData;

    public Graphic(String title, String lableX, String lableY) {
        this.title = title;
        this.lableX = lableX;
        this.lableY = lableY;
        collectionData = new XYSeriesCollection();
        graphic = ChartFactory.createXYLineChart(title,lableX,lableY,collectionData,
                PlotOrientation.VERTICAL,true,true,true);
    }

    public Graphic(){
        this("", "X", "Y");
    }

    /**
     * Agrega una grafica de una funcion, a la coleccion de graficas
     * @param id - id de la funcion
     * @param xValues - conjunto de valores de x a graficar
     * @param yValues - conjunto de valores de soluciones para los valores de x
     */
    public void addGraphic(String id, double xValues[], double yValues[]){
        XYSeries serie = new XYSeries(id);
        int numValues = xValues.length;
        for (int i = 0; i < numValues; i++)
            serie.add(xValues[i], yValues[i]);
        collectionData.addSeries(serie);
    }

    /**
     * Crea una grafica de una funcion, a la coleccion de graficas
     * @param id - id de la funcion
     * @param xValues - conjunto de valores de x a graficar
     * @param yValues - conjunto de valores de soluciones para los valores de x
     */
    public void createGraphic(String id, double xValues[], double yValues[]){
        collectionData.removeAllSeries();
        addGraphic(id,xValues,yValues);
    }

    /**
     * Regresa Un panel que contiene la gráfica
     * @return gráfica como instancia de JPanel
     */
    public JPanel getGraphic(){
        return new ChartPanel(graphic);
    }
}
