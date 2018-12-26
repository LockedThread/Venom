package org.venompvp.venom.commands.arguments;

public class IntegerArgument extends Argument<Integer> {

    public IntegerArgument(String check) {
        super(check);
    }

    @Override
    public boolean isArgumentType() {
        try {
            Integer.parseInt(check);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public Integer getValue() {
        return Integer.parseInt(check);
    }
}
