package modelo;

import com.bestcode.mathparser.IMathParser;
import com.bestcode.mathparser.MathParserFactory;
import javafx.scene.control.Alert;


public class Function {
    private String function;
    IMathParser parser;

    public Function(String function) {
        this.function = function;
        parser = MathParserFactory.create();
        parser.setExpression(function);
    }

    public double evaluateFrom(double value) throws Exception {
        double result = Double.NaN;
        parser.setX(value);
        result = parser.getValue();
        return result;
    }

    public double[] evaluateFrom(double[] values) throws Exception{
        double results[] = new double[values.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = evaluateFrom(values[i]);
        }
        return results;
    }

    public double[] generateRange(double from, double to, double increment){
        int numData = (int)(Math.abs(to-from)/increment);
        double range[] = new double[numData];
        for (int i = 0; i < numData; i++) {
            range[i] = from;
            from += increment;
        }
        return range;
    }

    public String getFunction(){
        return function;
    }
}
