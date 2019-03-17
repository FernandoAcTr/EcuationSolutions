package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileFunction;
import model.Function;
import model.GraphicData;
import model.ResolveMethod;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private TextField txtFunction;

    @FXML
    private Button btnShowGraphic, btnResolve;

    @FXML
    private TextField txtFrom;

    @FXML
    private TextField txtTo;

    @FXML
    private Label lblMethod;

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

    @FXML
    LineChart<Number, Number> lineChart;

    @FXML
    private VBox paneMethod;

    @FXML
    private ComboBox<String> cmbMethod;

    @FXML
    private TabPane tabPane;

    private TextField txtPointA, txtPointB, txtError;
    private TextField txtPointAOpen, txtDerived, txtGFunction;

    private HBox paneCloseMethod, paneNewtonMethod, paneFixedPointMethod, paneSecantMethod;

    ResolveMethod resolveMethod;
    FileFunction fileFunction;
    FileChooser fileChooser;

    public void initialize(URL location, ResourceBundle resources) {


        initData();

        btnShowGraphic.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showGraphic();
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

                mnuSaveAction((Stage) btnShowGraphic.getScene().getWindow(), false);
            }
        });

        mnuSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                mnuSaveAction((Stage) btnShowGraphic.getScene().getWindow(), true);
            }
        });

        mnuOpen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                mnuOpenAction((Stage) btnShowGraphic.getScene().getWindow());
            }
        });

        btnResolve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                btnResolveAction();
            }
        });

        buildClosedPane();
        cmbMethod.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("Bisección") || newValue.equals("Regla Falsa"))
                    buildClosedPane();
                else if (newValue.equals("Punto Fijo"))
                    buildFixedPointPane();
                else if (newValue.equals("Newton-Raphson"))
                    buildNewtonPane();
                else if(newValue.equals("Método de la secante"))
                    buildSecantPane();

                txtAreaProcedure.clear();
            }
        });

    }

    private void initData() {
        resolveMethod = new ResolveMethod();
        fileFunction = new FileFunction();
        fileChooser = new FileChooser();
        fileChooser.setInitialFileName("*.func");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Function File", "*.func"));

        cmbMethod.getItems().add("Bisección");
        cmbMethod.getItems().add("Regla Falsa");
        cmbMethod.getItems().add("Punto Fijo");
        cmbMethod.getItems().add("Newton-Raphson");
        cmbMethod.getItems().add("Método de la secante");

        try {
            paneCloseMethod = FXMLLoader.load(getClass().getResource("/fxml/layout_closed_method.fxml"));
            paneNewtonMethod = FXMLLoader.load(getClass().getResource("/fxml/layout_newton_method.fxml"));
            paneFixedPointMethod = FXMLLoader.load(getClass().getResource("/fxml/layout_fixedpoint_method.fxml"));
            paneSecantMethod = FXMLLoader.load(getClass().getResource("/fxml/layout_secant_method.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        cmbMethod.getSelectionModel().selectFirst();

    }

    private void showGraphic() {
        double from, to;
        String def = txtFunction.getText();
        Function function = new Function(def);
        double increment;

        try {
            from = Double.parseDouble(txtFrom.getText());
            to = Double.parseDouble(txtTo.getText());
            increment = Math.abs(from - to) / 800;
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
            GraphicData graphicData = new GraphicData();
            ObservableList ob = graphicData.getChartData(def, xValues, yValues);

            lineChart.setData(ob);

            final DecimalFormat formatter = new DecimalFormat("#.###");

            for (int i = 0; i < lineChart.getData().size(); i++)
                for (final XYChart.Data data : lineChart.getData().get(i).getData())
                    Tooltip.install(data.getNode(), new Tooltip("X: " + formatter.format(data.getXValue()) + " Y: " + formatter.format(data.getYValue())));

        } catch (Exception ex) {
            showMessage("",
                    "Error", "Por favor revisa la ayuda acerca de como ingresar la funcion", Alert.AlertType.ERROR);
        }
    }

    private void biseccionAction() {

        try {
            String def = txtFunction.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.initCloseProcedure();
            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByBiseccion(pointA, pointB);
            txtAreaProcedure.setText(resolveMethod.getProcedure());
            txtAreaProcedure.appendText("\nRaíz: " + resolveMethod.toStringRoot(resolveMethod.getRoot()));
            resolveMethod.restartProcedure();
        } catch (NumberFormatException e) {
            showMessage("Asegurate de ingresar: punto A, punto B, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void falseRuleAction() {
        try {
            String def = txtFunction.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.initCloseProcedure();
            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByFalseRule(pointA, pointB);
            txtAreaProcedure.setText(resolveMethod.getProcedure());
            txtAreaProcedure.appendText("\nRaíz: " + resolveMethod.toStringRoot(resolveMethod.getRoot()));
            resolveMethod.restartProcedure();
        } catch (NumberFormatException e) {
            showMessage("Asegurate de ingresar: punto A, punto B, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void fixedPointAction() {
        try {
            String def = txtFunction.getText().trim();
            String gFunc = txtGFunction.getText().trim();
            double pointX = Double.parseDouble(txtPointAOpen.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);
            Function gFunction = new Function(gFunc);

            resolveMethod.initFixedPointProcedure();
            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByFixedPoint(gFunction, pointX);
            txtAreaProcedure.setText(resolveMethod.getProcedure());
            txtAreaProcedure.appendText("\nRaíz: " + resolveMethod.toStringRoot(resolveMethod.getRoot()));
            resolveMethod.restartProcedure();

        } catch (NumberFormatException e) {
            showMessage("Asegurate de ingresar: Funcion g(x), punto A, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void newtonAction() {
        try {
            String def = txtFunction.getText().trim();
            String dFunc = txtDerived.getText().trim();
            double pointX = Double.parseDouble(txtPointAOpen.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);
            Function dFunction = new Function(dFunc);

            resolveMethod.initNewtonProcedure();
            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByNewtonRaphson(dFunction, pointX);
            txtAreaProcedure.setText(resolveMethod.getProcedure());
            txtAreaProcedure.appendText("\nRaíz: " + resolveMethod.toStringRoot(resolveMethod.getRoot()));
            resolveMethod.restartProcedure();

        } catch (NumberFormatException e) {
            showMessage("Asegurate de ingresar: Funcion g(x), punto A, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void secantAction(){
        try {
            String def = txtFunction.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.initSecantProcedure();
            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveBySecant(pointA, pointB);
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
                + "\n Los signos de agrupacion aceptados son: (), {}, []";




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

    private void btnResolveAction() {
        if (cmbMethod.getSelectionModel().getSelectedIndex() == 0)
            biseccionAction();
        else if (cmbMethod.getSelectionModel().getSelectedIndex() == 1)
            falseRuleAction();
        else if (cmbMethod.getSelectionModel().getSelectedIndex() == 2)
            fixedPointAction();
        else if(cmbMethod.getSelectionModel().getSelectedIndex() == 3)
            newtonAction();
        else if (cmbMethod.getSelectionModel().getSelectedIndex() == 4)
            secantAction();
    }

    private void cleanAll() {
        txtAreaProcedure.setText("");
        txtError.setText("");
        txtFrom.setText("");
        txtFunction.setText("");
        txtPointA.setText("");
        txtPointB.setText("");
        txtTo.setText("");
        if (txtPointAOpen != null) txtPointAOpen.setText("");
        if (txtDerived != null) txtDerived.setText("");
        if (txtGFunction != null) txtGFunction.setText("");
        lineChart.getData().clear();
        tabPane.getSelectionModel().selectFirst();
        fileFunction.restartFile();
        ((Stage)txtFunction.getParent().getScene().getWindow()).setTitle("Nuevo Documento");
    }

    /**
     * Guarda una funcion
     * @param stage
     * @param typeSave True para Guardar. False para Guardar como...
     */
    private void mnuSaveAction(Stage stage, boolean typeSave) {
        boolean save = false;
        byte typeMethod = (byte) cmbMethod.getSelectionModel().getSelectedIndex();
        FileFunction.BeanFunction beanFunction = null;


        if (typeMethod == FileFunction.BeanFunction.BISECCION || typeMethod == FileFunction.BeanFunction.FALSE_RULE
                || typeMethod == FileFunction.BeanFunction.SECANT) {

            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointA.getText() != null ? txtPointA.getText() : "";
            String b = txtPointB.getText() != null ? txtPointB.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            String p = txtAreaProcedure.getText() != null ? txtAreaProcedure.getText() : "";
            beanFunction = new FileFunction.BeanFunction(typeMethod, f, from, to, a, b, e, p);

        } else if (typeMethod == FileFunction.BeanFunction.PUNTO_FIJO) {

            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointAOpen.getText() != null ? txtPointAOpen.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            String p = txtAreaProcedure.getText() != null ? txtAreaProcedure.getText() : "";
            String gFun = txtGFunction.getText() != null ? txtGFunction.getText() : "";
            beanFunction = new FileFunction.BeanFunction(typeMethod, f, from, to, a, e, p);
            beanFunction.setExtraFunction(gFun);

        } else if (typeMethod == FileFunction.BeanFunction.NEWTON) {
            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointAOpen.getText() != null ? txtPointAOpen.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            String p = txtAreaProcedure.getText() != null ? txtAreaProcedure.getText() : "";
            String dFun = txtDerived.getText() != null ? txtDerived.getText() : "";
            beanFunction = new FileFunction.BeanFunction(typeMethod, f, from, to, a, e, p);
            beanFunction.setExtraFunction(dFun);
        }

        if (fileFunction.getFunctionFile() != null && typeSave) {
            fileFunction.openFile(fileFunction.getFunctionFile());
            save = true;
        } else {
            fileChooser.setTitle("Save As...");
            File file = refactorFileName(fileChooser.showSaveDialog(stage));
            if (file != null) {
                fileFunction.openFile(file);
                save = true;
            }
        }

        if (save) {
            fileFunction.saveFunction(beanFunction);
            fileFunction.closeFile();
            ((Stage)txtFunction.getParent().getScene().getWindow()).setTitle(fileFunction.getFunctionFile().getName());
        }
    }


    private void mnuOpenAction(Stage stage) {
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            fileFunction.openFile(file);
            FileFunction.BeanFunction bean = fileFunction.readFunction();
            byte type = bean.getTypeMethod();

            txtFunction.setText(bean.getFunction());
            txtFrom.setText(bean.getFrom());
            txtTo.setText(bean.getTo());

            if (type == FileFunction.BeanFunction.BISECCION || type == FileFunction.BeanFunction.FALSE_RULE) {
                buildClosedPane();
                txtPointA.setText(bean.getPointA());
                txtPointB.setText(bean.getPointB());
                txtError.setText(bean.getError());
                txtAreaProcedure.setText(bean.getProcedure());

            } else if (type == FileFunction.BeanFunction.PUNTO_FIJO) {
                buildFixedPointPane();
                txtPointAOpen.setText(bean.getPointA());
                txtError.setText(bean.getError());
                txtAreaProcedure.setText(bean.getProcedure());
                txtGFunction.setText(bean.getExtraFunction());

            } else if (type == FileFunction.BeanFunction.NEWTON) {
                buildNewtonPane();
                txtPointAOpen.setText(bean.getPointA());
                txtError.setText(bean.getError());
                txtAreaProcedure.setText(bean.getProcedure());
                txtDerived.setText(bean.getExtraFunction());

            }else if (type == FileFunction.BeanFunction.SECANT){
                buildSecantPane();
                txtPointA.setText(bean.getPointA());
                txtPointB.setText(bean.getPointB());
                txtError.setText(bean.getError());
                txtAreaProcedure.setText(bean.getProcedure());
            }

            cmbMethod.getSelectionModel().select(type);
            fileFunction.closeFile();
            ((Stage)txtFunction.getParent().getScene().getWindow()).setTitle(file.getName());
        }
    }

    private File refactorFileName(File file) {
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

    private void buildClosedPane() {
        txtPointA = (TextField) paneCloseMethod.getChildren().get(1);
        txtPointB = (TextField) paneCloseMethod.getChildren().get(3);
        txtError = (TextField) paneCloseMethod.getChildren().get(5);
        paneMethod.getChildren().set(1, paneCloseMethod);
        lblMethod.setText("Métodos Cerrados");
        txtPointA.requestFocus();
    }

    private void buildFixedPointPane() {
        txtGFunction = (TextField) paneFixedPointMethod.getChildren().get(1);
        txtPointAOpen = (TextField) paneFixedPointMethod.getChildren().get(3);
        txtError = (TextField) paneFixedPointMethod.getChildren().get(5);
        paneMethod.getChildren().set(1, paneFixedPointMethod);
        lblMethod.setText("Métodos Abiertos");
        txtGFunction.requestFocus();
    }

    private void buildNewtonPane() {
        txtDerived = (TextField) paneNewtonMethod.getChildren().get(1);
        txtPointAOpen = (TextField) paneNewtonMethod.getChildren().get(3);
        txtError = (TextField) paneNewtonMethod.getChildren().get(5);
        paneMethod.getChildren().set(1, paneNewtonMethod);
        lblMethod.setText("Métodos Abiertos");
        txtDerived.requestFocus();
    }

    private void buildSecantPane() {
        txtPointA = (TextField) paneSecantMethod.getChildren().get(1);
        txtPointB = (TextField) paneSecantMethod.getChildren().get(3);
        txtError = (TextField) paneSecantMethod.getChildren().get(5);
        paneMethod.getChildren().set(1, paneSecantMethod);
        lblMethod.setText("Métodos Abiertos");
        txtPointA.requestFocus();
    }

}
