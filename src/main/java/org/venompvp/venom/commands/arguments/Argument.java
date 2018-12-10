package org.venompvp.venom.commands.arguments;

public abstract class Argument<T> {

    public String check;

    private T value;

    public Argument(String check) {
        this.check = check;
    }

    public abstract boolean isArgumentType();

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
