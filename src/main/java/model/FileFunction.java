package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileFunction {
    private RandomAccessFile randomFile;
    private File functionFile;

    public FileFunction(){
        randomFile = null;
        functionFile = null;
    }

    /**
     * Abre un archivo de acceso directo con la ruta especificada
     * @param file
     */
    public void openFile(File file){
        try {
            functionFile = file;
            randomFile = new RandomAccessFile(file,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierrra el archivo
     */
    public void closeFile(){
        try {
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe una funcion y los datos necesarios para resolverla en un archivo binario,
     * @param beanFunction objeto de tipo FileFunction.BeanFucntion
     */
    public void saveFunction(FileFunction.BeanFunction beanFunction){
        try {
            randomFile.seek(0);
            randomFile.writeUTF(beanFunction.function);
            randomFile.writeUTF(beanFunction.from);
            randomFile.writeUTF(beanFunction.to);
            randomFile.writeUTF(beanFunction.pointA);
            randomFile.writeUTF(beanFunction.pointB);
            randomFile.writeUTF(beanFunction.error);
            randomFile.writeUTF(beanFunction.procedure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna una instancia de FileFunction.BeanFunction
     */
    public FileFunction.BeanFunction readFunction(){
        BeanFunction bean = null;
        try {
            String function = randomFile.readUTF();
            String from = randomFile.readUTF();
            String to = randomFile.readUTF();
            String pointA = randomFile.readUTF();
            String pointB = randomFile.readUTF();
            String error = randomFile.readUTF();
            String procedure = randomFile.readUTF();
            bean = new BeanFunction(function,from,to,pointA,pointB,error,procedure);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * Pierde la referencia al archivo que se estaba apuntando
     */
    public void restartFile(){
        functionFile = null;
    }

    public File getFunctionFile() {
        return functionFile;
    }

    /**
     * Clase que sirve de contenedor de los valores de la interfaz grafica
     */
    public static class BeanFunction{
        private String function;
        private String from;
        private String to;
        private String pointA;
        private String pointB;
        private String procedure;
        private String error;

        public BeanFunction(String function, String from, String to, String pointA, String pointB, String procedure, String error) {
            this.function = function;
            this.from = from;
            this.to = to;
            this.pointA = pointA;
            this.pointB = pointB;
            this.procedure = procedure;
            this.error = error;
        }

        public String getFunction() {
            return function;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getPointA() {
            return pointA;
        }

        public String getPointB() {
            return pointB;
        }

        public String getProcedure() {
            return procedure;
        }

        public String getError() {
            return error;
        }
    }
}
