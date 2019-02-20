package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modelo.ResolveMethod;
import modelo.Function;
import modelo.Graphic;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private TextField txtFunction;

    @FXML
    private Button btnShowGraphic;

    @FXML
    private TextField txtFrom;

    @FXML
    private TextField txtTo;

    @FXML
    private TextField txtPointA;

    @FXML
    private TextField txtPointB;

    @FXML
    private TextField txtError;

    @FXML
    private Button btnBisection;

    @FXML
    private Button btnFalseRule;

    @FXML
    private TextArea txtAreaProcedure;

    Graphic graphic;
    ResolveMethod resolveMethod;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Instituto Tecnológico de Celaya\nProyecto Primer Parcial\nMétodos Numéricos");
        alert.setContentText("Luis Fernando Acosta Tovar\nGabriela Martínez Tort");
        alert.show();

        graphic = new Graphic();
        resolveMethod = new ResolveMethod();

        btnShowGraphic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showGraphic();
            }
        });

        btnBisection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resolveByBiseccion();
            }
        });

        btnFalseRule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resolveByFalseRule();
            }
        });
    }

    private void showGraphic() {
        double from = 0, to = 0;
        String def = txtFunction.getText();
        double increment = 0.01;
        Function function = new Function(def);

        try {
            from = Double.parseDouble(txtFrom.getText());
            to = Double.parseDouble(txtTo.getText());
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ingresa correctamente el intervalo");
            alert.show();
            return;
        }

        try {
            double xValues[] = function.generateRange(from, to, increment);
            double yValues[] = function.evaluateFrom(xValues);
            graphic.createGraphic(def, xValues, yValues);

            JFrame graphicFrame = new JFrame();
            graphicFrame.setLayout(new FlowLayout());
            graphicFrame.setLocation(0, 0);
            graphicFrame.setSize(700, 500);
            graphicFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            graphicFrame.add(graphic.getGraphic());
            graphicFrame.setResizable(false);
            graphicFrame.setVisible(true);

        } catch (Exception ex) {
            String information = "Usar los siguientes simbolos: "
                    + "\n Potencias: ^"
                    + "\n Seno: SenH: sin(), sinh()"
                    + "\n Cosen: CosH: cos(), cosh()"
                    + "\n Tangente: tan()"
                    + "\n Cotangente: cotan()"
                    + "\n Absoluto: abs()"
                    + "\n logaritmo: log(), ln()"
                    + "\n logaritmo base N: logN(n,x)"
                    + "\n Raiz: sqrt()"
                    + "\n Los signos de agrupacion aceptados son: (), {}, []"
                    + "\n Nota: Los productos no se aceptan sin signo * (ej. 2x --> 2*x)";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Por favor checa la entrada de la función");
            alert.setContentText(information);
            alert.show();
            ex.printStackTrace();
        }
    }

    private void resolveByBiseccion(){
        try {
            String def = txtFunction.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.setPointA(pointA);
            resolveMethod.setPointB(pointB);
            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByBiseccion();
            txtAreaProcedure.setText(resolveMethod.getProcedure());
            resolveMethod.restartProcedure();
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Asegurate de ingresar: punto A, punto B, Error");
            alert.show();
        }
    }

    private void resolveByFalseRule(){
        try {
            String def = txtFunction.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.setPointA(pointA);
            resolveMethod.setPointB(pointB);
            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByFalseRule();
            txtAreaProcedure.setText(resolveMethod.getProcedure());
            resolveMethod.restartProcedure();
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Asegurate de ingresar: punto A, punto B, Error");
            alert.show();
        }
    }

}
