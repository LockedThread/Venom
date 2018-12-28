package org.venompvp.venom.commands.arguments;

public abstract class OptionalArgument<T> extends Argument<T> {

    private boolean present;

    public OptionalArgument() {
        super("");
        this.present = false;
    }

    public OptionalArgument(String check) {
        super(check);
        this.present = true;
    }

    public boolean isPresent() {
        return present;
    }
}
