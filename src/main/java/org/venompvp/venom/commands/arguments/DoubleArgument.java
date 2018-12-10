package org.venompvp.venom.commands.arguments;

public class DoubleArgument extends Argument<Double> {

    public DoubleArgument(String check) {
        super(check);
    }

    @Override
    public boolean isArgumentType() {
        try {
            Double.valueOf(check);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
