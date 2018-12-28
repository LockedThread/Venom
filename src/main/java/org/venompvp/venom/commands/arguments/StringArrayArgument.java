package org.venompvp.venom.commands.arguments;

import com.google.common.base.Joiner;

public class StringArrayArgument extends Argument<String[]> {

    public StringArrayArgument(String check) {
        super(check);
    }

    public StringArrayArgument(String[] check) {
        super(Joiner.on(" ").skipNulls().join(check));
    }

    @Override
    public String[] getValue() {
        return check.split(" ");
    }

    @Override
    public boolean isArgumentType() {
        return true;
    }

    @Override
    public String unableToParse() {
        return check + " is unable to parse as a String[]";
    }
}
