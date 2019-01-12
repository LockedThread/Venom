package org.venompvp.venom.commands.arguments;

public class DoubleArgument extends Argument<Double> {

    public DoubleArgument(String check) {
        super(check);
    }

    @Override
    public boolean isArgumentType() {
        try {
            Double.valueOf(check);
            setValue(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }

    }

    @Override
    public Double getValue() {
        return null;
    }

    @Override
    public String unableToParse() {
        return check + " is unable to parse as a Double";
    }
}
