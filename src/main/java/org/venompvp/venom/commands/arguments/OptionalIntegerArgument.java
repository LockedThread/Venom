package org.venompvp.venom.commands.arguments;

public class OptionalIntegerArgument extends OptionalArgument<Integer> {

    public OptionalIntegerArgument(String check) {
        super(check);
    }

    public OptionalIntegerArgument() {
        super();
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

    @Override
    public String unableToParse() {
        return check + " is unable to parse as an Integer";
    }
}
