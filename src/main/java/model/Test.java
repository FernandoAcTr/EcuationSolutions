package model;

public class Test {
    public static void main(String[] args){
        Function function = new Function("x^4 - 2*x^3 - 12*x^2 + 16*x - 40");
        ResolveMethod resolveMethod = new ResolveMethod(function);

        try {
           /* resolveMethod.setErrorPermited(0.01);
            resolveMethod.resolveByBiseccion(4.2,4.4);
            System.out.println(resolveMethod.getProcedure());
            System.out.println("Raiz: "+ resolveMethod.getRoot());*/
           double b = function.evaluateFrom(5);
            System.out.println(b);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
