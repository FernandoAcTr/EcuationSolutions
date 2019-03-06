package model;

import java.text.DecimalFormat;

public class ResolveMethod {

    private double errorPermited;
    private double Xr;
    private String procedure;
    private Function function;
    DecimalFormat formatter;

    public ResolveMethod() {
        formatter = new DecimalFormat("0.000000");
    }

    public ResolveMethod(Function function) {
        this.function = function;
        formatter = new DecimalFormat("0.000000");
    }

    /**
     * Resuelve la funcion por medio del metodo de biseccion
     */
    public void resolveByBiseccion(double pointA, double pointB){
        double error = Double.POSITIVE_INFINITY;
        int i = 1;
        double funA, funB, funXr;
        double previousXr = 0;

        try {
            funA = function.evaluateFrom(pointA);
            funB = function.evaluateFrom(pointB);
            Xr = (pointA + pointB)/2;
            funXr = function.evaluateFrom(Xr);
            concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            do {

                if(funA * funXr == 0)
                    break;
                else if(funA*funXr > 0)
                    pointA = Xr;
                else if(funA * funXr < 0)
                    pointB = Xr;

                previousXr = Xr;

                funA = function.evaluateFrom(pointA);
                funB = function.evaluateFrom(pointB);
                Xr = (pointA + pointB)/2;
                funXr = function.evaluateFrom(Xr);

                error = Math.abs((Xr-previousXr)/Xr) * 100.0;
                i++;

                concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            } while (error > errorPermited);

        }catch (Exception ex){};
    }

    /**
     * Resuelve la funcion cpor el metodo de falsa regla
     */
    public void resolveByFalseRule(double pointA, double pointB){
        double error = Double.POSITIVE_INFINITY;
        int i = 1;
        double funA, funB, funXr;
        double previousXr = 0;

        try {
            funA = function.evaluateFrom(pointA);
            funB = function.evaluateFrom(pointB);
            Xr = pointB - (funB*(pointA-pointB)) / (funA - funB);
            funXr = function.evaluateFrom(Xr);
            concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            do {

                if(funA * funXr == 0)
                    break;
                else if(funA*funXr > 0)
                    pointA = Xr;
                else if(funA * funXr < 0)
                    pointB = Xr;

                previousXr = Xr;

                funA = function.evaluateFrom(pointA);
                funB = function.evaluateFrom(pointB);
                Xr = pointB - (funB*(pointA-pointB)) / (funA - funB);
                funXr = function.evaluateFrom(Xr);

                error = Math.abs((Xr-previousXr)/Xr) * 100.0;
                i++;

                concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            } while (error > errorPermited);

        }catch (Exception ex){};
    }

    /**
     * Resuelve la funcion por el metodo de Punto Fijo
     * @param gFunction funcion g(x)
     * @param pointC punto X arbitrario inicial para comenzar a evaluar
     */
    public void resolveByFixedPoint(Function gFunction, double pointC){

        double error;
        int i = 1;

        try {

            Xr = gFunction.evaluateFrom(pointC);
            error = Math.abs((Xr-pointC)/Xr) * 100.0;

            concatFixedPointProcedure(i,pointC,Xr,error);

            do {

                pointC = Xr;
                Xr = gFunction.evaluateFrom(pointC);
                error = Math.abs((Xr-pointC)/Xr) * 100.0;
                i++;

                concatFixedPointProcedure(i,pointC,Xr,error);

            } while (error > errorPermited);

        }catch (Exception ex){};
    }

    /**
     * Resuelve la funcion por el metodo de Newton-Raphson
     * @param dFunction la derivada de la funcion
     * @param pointC punto X arbitrario para comenzae a evaluar
     */
    public void resolveByNewtonRaphson(Function dFunction, double pointC){

        double error;
        int i = 1;
        double funC, dfunC;

        try {

            funC = function.evaluateFrom(pointC);
            dfunC = dFunction.evaluateFrom(pointC);
            Xr = pointC-(funC/dfunC);
            error = Math.abs((Xr-pointC)/Xr) * 100.0;

            concatNewtonProcedure(i,pointC,funC,dfunC,Xr,error);

            do {

                pointC = Xr;
                funC = function.evaluateFrom(pointC);
                dfunC = dFunction.evaluateFrom(pointC);
                Xr = pointC-(funC/dfunC);
                error = Math.abs((Xr-pointC)/Xr) * 100.0;
                i++;
                concatNewtonProcedure(i,pointC,funC,dfunC,Xr,error);
            } while (error > errorPermited);

        }catch (Exception ex){};
    }


    /**
     * Concatena al procedimiento un renglon más del procedimiento, para metodos cerrados
     */
    private void concatCloseProcedure(int i,double pointA, double pointB, double funA, double funB, double Xr, double funXr, double error){
        String a = formatter.format(pointA);
        String b = formatter.format(pointB);
        String fa = formatter.format(funA);
        String fb = formatter.format(funB);
        String xr = formatter.format(Xr);
        String fxr = formatter.format(funXr);
        String err = (i>1) ? formatter.format(error) : "------";

        String lineProcedure = String.format("%-3s\t%10s\t%10s\t%10s\t%10s\t%10s\t%10s\t%10s",i, a, b, fa,
                fb, xr, fxr, err);
        procedure += "\n"+lineProcedure;
    }

    /**
     * Concatena al procedimiento un renglon más del procedimiento, para el metodo de punto fijo
     */
    private void concatFixedPointProcedure(int i, double pointC, double Xr, double error){
        String c = formatter.format(pointC);
        String xr = formatter.format(Xr);
        String err = formatter.format(error);

        String lineProcedure = String.format("%-8s\t%-16s\t%-16s\t%-16s",i, c, xr, err);
        procedure += "\n"+lineProcedure;
    }

    private void concatNewtonProcedure(int i, double pointC, double valueF,double valueFPrime, double Xr, double error){
        String c = formatter.format(pointC);
        String f = formatter.format(valueF);
        String df = formatter.format(valueF);
        String xr = formatter.format(Xr);
        String err = formatter.format(error);

        String lineProcedure = String.format("%-8s\t%-19s\t%-19s\t%-19s\t%-19s\t%-19s",i, c, f, df, xr,err);
        procedure += "\n"+lineProcedure;
    }

    public double getRoot(){
        return Xr;
    }

    public String toStringRoot(double root){
        return formatter.format(root);
    }

    public String getProcedure(){
        return procedure;
    }

    public void setErrorPermited(double errorPermited) {
        this.errorPermited = errorPermited;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void initCloseProcedure(){
        procedure = String.format("%-6s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s","No", "a", "b", "f(a)",
                "f(b)", "Xr", "f(Xr)", "error");
    }

    public void initFixedPointProcedure(){
        procedure = String.format("%-6s\t%-16s\t%-16s\t%-16s","No", "Xo", "Xi", "error");
    }

    public void initNewtonProcedure(){
        procedure = String.format("%-6s\t%-25s\t%-25s\t%-25s\t%-25s\t%-25s","No", "Xi", "f(Xi)", "f'(Xi)", "Xr", "error");
    }

    public void restartProcedure() {
        procedure = String.format("%-6s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s","No", "a", "b", "f(a)",
                "f(b)", "Xr", "f(Xr)", "error");
    }
}
