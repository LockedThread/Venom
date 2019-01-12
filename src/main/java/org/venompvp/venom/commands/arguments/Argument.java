package org.venompvp.venom.commands.arguments;

public abstract class Argument<T> {

    public String check;

    public T value;

    public Argument(String check) {
        this.check = check;
    }

    public abstract boolean isArgumentType();


    public abstract T getValue();

    public void setValue(T value) {
        this.value = value;
    }

    public abstract String unableToParse();
}
