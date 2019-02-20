package modelo;

public class Test {
    public static void main(String[] args){
        Function function = new Function("x^4 - 2*x^3 - 12*x^2 + 16*x - 40");
        ResolveMethod resolveMethod = new ResolveMethod(function);

        try {
            resolveMethod.setPointA(4.2);
            resolveMethod.setPointB(4.4);
            resolveMethod.setErrorPermited(0.01);
            resolveMethod.resolveByBiseccion();
            System.out.println(resolveMethod.getProcedure());
            System.out.println("Raiz: "+ resolveMethod.getRaiz());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
