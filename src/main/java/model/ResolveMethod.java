package model;

import java.text.DecimalFormat;

public class ResolveMethod {

    private double pointA;
    private double pointB;
    private double errorPermited;
    private double Xr;
    private String procedure;
    private Function function;
    DecimalFormat formatter;

    public ResolveMethod() {
        procedure = String.format("%-6s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s","No", "a", "b", "f(a)",
                "f(b)", "Xr", "f(Xr)", "error");
        formatter = new DecimalFormat("0.000000");
    }

    public ResolveMethod(Function function) {
        this.function = function;
        procedure = String.format("%-6s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s","No", "a", "b", "f(a)",
                "f(b)", "Xr", "f(Xr)", "error");
        formatter = new DecimalFormat("0.000000");
    }

    public void resolveByBiseccion(){
        double error = Double.POSITIVE_INFINITY;
        int i = 1;
        double funA, funB, funXr;
        double previousXr = 0;

        try {
            funA = function.evaluateFrom(pointA);
            funB = function.evaluateFrom(pointB);
            Xr = (pointA + pointB)/2;
            funXr = function.evaluateFrom(Xr);
            concatProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

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

                concatProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            } while (error > errorPermited);

        }catch (Exception ex){};
    }

    public void resolveByFalseRule(){
        double error = Double.POSITIVE_INFINITY;
        int i = 1;
        double funA, funB, funXr;
        double previousXr = 0;

        try {
            funA = function.evaluateFrom(pointA);
            funB = function.evaluateFrom(pointB);
            Xr = pointB - (funB*(pointA-pointB)) / (funA - funB);
            funXr = function.evaluateFrom(Xr);
            concatProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

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

                concatProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            } while (error > errorPermited);

        }catch (Exception ex){};
    }

    private void concatProcedure(int i,double pointA, double pointB, double funA, double funB, double Xr, double funXr, double error){
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

    public double getRoot(){
        return Xr;
    }

    public String toStringRoot(double root){
        return formatter.format(root);
    }

    public String getProcedure(){
        return procedure;
    }

    public void setPointA(double pointA) {
        this.pointA = pointA;
    }

    public void setPointB(double pointB) {
        this.pointB = pointB;
    }

    public void setErrorPermited(double errorPermited) {
        this.errorPermited = errorPermited;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void restartProcedure() {
        procedure = String.format("%-6s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s\t%-16s","No", "a", "b", "f(a)",
                "f(b)", "Xr", "f(Xr)", "error");
    }
}
