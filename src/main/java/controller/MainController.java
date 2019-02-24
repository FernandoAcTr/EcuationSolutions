package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileFunction;
import model.Function;
import model.Graphic;
import model.ResolveMethod;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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

    @FXML
    private MenuItem mnuClose;

    @FXML
    private MenuItem mnuHelp;

    @FXML
    private MenuItem mnuAbout;

    @FXML
    private MenuItem mnuNew;

    @FXML
    private MenuItem mnuOpen;

    @FXML
    private MenuItem mnuSave;

    @FXML
    private MenuItem mnuSaveAs;

    Graphic graphic;
    ResolveMethod resolveMethod;
    FileFunction fileFunction;
    FileChooser fileChooser;

    public void initialize(URL location, ResourceBundle resources) {
        graphic = new Graphic();
        resolveMethod = new ResolveMethod();
        fileFunction = new FileFunction();

        fileChooser = new FileChooser();
        fileChooser.setInitialFileName("*.func");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Function File", "*.func"));

        btnShowGraphic.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showGraphic();
            }
        });

        btnBisection.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                btnBiseccionAction();
            }
        });

        btnFalseRule.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                btnFalseRuleAction();
            }
        });

        mnuClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        mnuHelp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showHelpMessage();
            }
        });

        mnuAbout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/layout_about.fxml"));
                    Scene scene = new Scene(root, 420, 360);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mnuNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                cleanAll();
            }
        });

        mnuSaveAs.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                mnuSaveAsAction((Stage) btnShowGraphic.getScene().getWindow());
            }
        });

        mnuSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                mnuSaveAction((Stage) btnShowGraphic.getScene().getWindow());
            }
        });

        mnuOpen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                mnuOpenAction((Stage) btnShowGraphic.getScene().getWindow());
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
        } catch (NumberFormatException ex) {
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
            showMessage("",
                    "Error", "Por favor revisa la ayuda acerca de como ingresar la funcion", Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    private void btnBiseccionAction() {
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
            txtAreaProcedure.appendText("\nRaíz: " + resolveMethod.toStringRoot(resolveMethod.getRoot()));
            resolveMethod.restartProcedure();
        } catch (NumberFormatException e) {
            showMessage("Asegurate de ingresar: punto A, punto B, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void btnFalseRuleAction() {
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
            txtAreaProcedure.appendText("\nRaíz: " + resolveMethod.toStringRoot(resolveMethod.getRoot()));
            resolveMethod.restartProcedure();
        } catch (NumberFormatException e) {
            showMessage("Asegurate de ingresar: punto A, punto B, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void showHelpMessage() {
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
                + "\n Nota: Los productos no se aceptan sin signo * (ej. 2x --> 2*x)"
                + "\n Ejemplo: 3*x^3 + 5*x^2 - 2*x + sin(x)";


        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.getStyleClass().setAll("alert", "alert-info");

        Label lblInfo = new Label(information);
        final Button btnAcept = new Button("Aceptar");
        btnAcept.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ((Stage) btnAcept.getScene().getWindow()).close();
            }
        });

        root.getChildren().addAll(lblInfo, btnAcept);
        Scene scene = new Scene(root, 500, 300);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void cleanAll() {
        txtAreaProcedure.setText("");
        txtError.setText("");
        txtFrom.setText("");
        txtFunction.setText("");
        txtPointA.setText("");
        txtPointB.setText("");
        txtTo.setText("");
        fileFunction.restartFile();
    }

    private void mnuSaveAction(Stage stage) {
        if(fileFunction.getFunctionFile() != null){
            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointA.getText() != null ? txtPointA.getText() : "";
            String b = txtPointB.getText() != null ? txtPointB.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            String p = txtAreaProcedure.getText() != null ? txtAreaProcedure.getText() : "";
            fileFunction.openFile(fileFunction.getFunctionFile());
            fileFunction.saveFunction(new FileFunction.BeanFunction(f, from, to, a, b, e, p));
            fileFunction.closeFile();
        }else
            mnuSaveAsAction(stage);
    }

    private void mnuSaveAsAction(Stage stage) {
        fileChooser.setTitle("Save As...");
        File file = refactorFilaName(fileChooser.showSaveDialog(stage));
        if (file != null) {
            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointA.getText() != null ? txtPointA.getText() : "";
            String b = txtPointB.getText() != null ? txtPointB.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            String p = txtAreaProcedure.getText() != null ? txtAreaProcedure.getText() : "";
            fileFunction.openFile(file);
            fileFunction.saveFunction(new FileFunction.BeanFunction(f, from, to, a, b, e, p));
            fileFunction.closeFile();
        }
    }

    private void mnuOpenAction(Stage stage){
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            fileFunction.openFile(file);
            FileFunction.BeanFunction bean = fileFunction.readFunction();
            txtFunction.setText(bean.getFunction());
            txtFrom.setText(bean.getFrom());
            txtTo.setText(bean.getTo());
            txtPointA.setText(bean.getPointA());
            txtPointB.setText(bean.getPointB());
            txtError.setText(bean.getError());
            txtAreaProcedure.setText(bean.getProcedure());
            fileFunction.closeFile();
        }
    }

    private File refactorFilaName(File file) {
        File refactorFile = file;
        if (file != null)
            if (!file.getName().endsWith(".func"))
                refactorFile = new File(file.getPath() + ".fucn");

        return refactorFile;
    }

    private void showMessage(String message, String title, String header, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

}
