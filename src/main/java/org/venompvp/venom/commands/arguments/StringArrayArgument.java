package org.venompvp.venom.commands.arguments;

public class StringArrayArgument extends Argument<String[]> {

    public StringArrayArgument(String check) {
        super(check);
    }

    @Override
    public String[] getValue() {
        return check.split(" ");
    }

    @Override
    public boolean isArgumentType() {
        return true;
    }
}
