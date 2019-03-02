package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;


public class GraphicData {

    public GraphicData() {

    }

    public ObservableList< XYChart.Series<Double, Double> > getChartData(String serieName, double xValues[], double yValues[]){
        ObservableList< XYChart.Series<Double, Double> > ol = FXCollections.observableArrayList();
        XYChart.Series serie = getSerie(serieName, xValues, yValues);
        serie.setName(serieName);
        ol.add(serie);

        return ol;
    }

    private XYChart.Series getSerie(String serieName, double xValues[], double yValues[]){
        int numValues = xValues.length;
        XYChart.Series<Double, Double> serie = new XYChart.Series<Double, Double>();
        serie.setName(serieName);

        for (int i = 0; i < numValues; i++)
            serie.getData().add(new XYChart.Data<Double, Double>(xValues[i], yValues[i]));

        return serie;
    }

}
