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
            randomFile.writeByte(beanFunction.typeMethod);
            randomFile.writeUTF(beanFunction.function);
            randomFile.writeUTF(beanFunction.from);
            randomFile.writeUTF(beanFunction.to);
            randomFile.writeUTF(beanFunction.pointA);

            if(beanFunction.typeMethod == BeanFunction.BISECCION || beanFunction.typeMethod == BeanFunction.FALSE_RULE){
                randomFile.writeUTF(beanFunction.pointB);
                randomFile.writeUTF(beanFunction.error);
                randomFile.writeUTF(beanFunction.procedure);
            }else if (beanFunction.typeMethod == BeanFunction.PUNTO_FIJO || beanFunction.typeMethod == BeanFunction.NEWTON){
                randomFile.writeUTF(beanFunction.error);
                randomFile.writeUTF(beanFunction.extraFunction);
                randomFile.writeUTF(beanFunction.procedure);
            }

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
            byte type = randomFile.readByte();
            String function = randomFile.readUTF();
            String from = randomFile.readUTF();
            String to = randomFile.readUTF();
            String pointA = randomFile.readUTF();

            if(type == BeanFunction.BISECCION || type == BeanFunction.FALSE_RULE){
                String pointB = randomFile.readUTF();
                String error = randomFile.readUTF();
                String procedure = randomFile.readUTF();
                bean = new BeanFunction(type, function,from,to,pointA,pointB,error,procedure);
            }else if(type == BeanFunction.PUNTO_FIJO || type == BeanFunction.NEWTON){
                String error = randomFile.readUTF();
                String extraFun = randomFile.readUTF();
                String procedure = randomFile.readUTF();
                bean = new BeanFunction(type, function,from,to,pointA,error,procedure);
                bean.setExtraFunction(extraFun);
            }

           return bean;

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
        private byte typeMethod;
        private String function;
        private String from;
        private String to;
        private String pointA;
        private String pointB;
        private String procedure;
        private String error;
        private String extraFunction;

        //estas constantes deben coincidir con los indices del ComboBox de metodos de solucion
        public static final byte BISECCION = 0;
        public static final byte FALSE_RULE = 1;
        public static final byte PUNTO_FIJO = 2;
        public static final byte NEWTON = 3;

        public BeanFunction(byte typeMethod, String function, String from, String to, String pointA,
                            String pointB, String procedure, String error) {
            this.typeMethod = typeMethod;
            this.function = function;
            this.from = from;
            this.to = to;
            this.pointA = pointA;
            this.pointB = pointB;
            this.procedure = procedure;
            this.error = error;
        }

        public BeanFunction(byte typeMethod, String function, String from, String to, String pointA, String error, String procedure) {
            this.typeMethod = typeMethod;
            this.function = function;
            this.from = from;
            this.to = to;
            this.pointA = pointA;
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

        public byte getTypeMethod() {
            return typeMethod;
        }

        public String getExtraFunction() {
            return extraFunction;
        }

        public void setExtraFunction(String extraFunction) {
            this.extraFunction = extraFunction;
        }
    }
}
