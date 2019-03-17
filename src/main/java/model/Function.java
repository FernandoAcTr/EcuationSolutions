package model;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

public class Function {
    private String function;
    private Expression expression;


    public Function(String function) {
        this.function = function;
        expression = new ExpressionBuilder(function)
                    .implicitMultiplication(true)
                    .variable("x")
                    .build();
    }

    /**
     * Evalua la funcion dada para un cierto valor
     * @param value El valor para el cual se requiere elvaluar la función
     * @return double - El valor de la función para ese punto
     * @throws Exception
     */
    public double evaluateFrom(double value) throws Exception {
        double result = Double.NaN;
        expression.setVariable("x", value);

        ValidationResult validation = expression.validate();
        if(!validation.isValid()){
            throw new Exception("La expresión es inválida");
        }

        result = expression.evaluate();
        return result;
    }

    /**
     * Evalua la funcion dada para un conjunto de valores
     * @param values conjunto de valores a evaluar
     * @return conjunto de resultados, para el conjunto de valores
     * @throws Exception
     */
    public double[] evaluateFrom(double[] values) throws Exception{
        double results[] = new double[values.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = evaluateFrom(values[i]);
        }
        return results;
    }

    /**
     * Genera un rango de valores, desde un valor a otro, con los incrementos especificados
     * @param from valor inicial para el incremento
     * @param to valor final para el incremento
     * @param increment incremento entre valores
     * @return conjunto de valores que conforman el rango
     */
    public double[] generateRange(double from, double to, double increment){
        int numValues = (int)(Math.abs(to-from)/increment);
        double range[] = new double[numValues];
        for (int i = 0; i < numValues; i++) {
            range[i] = from;
            from += increment;
        }
        return range;
    }

    public String getFunction() {
        return function;
    }
}
