package org.venompvp.venom.commands.arguments;

import org.venompvp.venom.Venom;
import org.venompvp.venom.module.Module;

public class ModuleArgument extends Argument<Module> {


    public ModuleArgument(String check) {
        super(check);
    }

    @Override
    public boolean isArgumentType() {
        return Venom.getInstance().getModules().stream().anyMatch(module -> module.getName().equalsIgnoreCase(check));
    }

    @Override
    public Module getValue() {
        return Venom.getInstance().getModules().stream().filter(module -> module.getName().equalsIgnoreCase(check)).findFirst().orElse(null);
    }

    @Override
    public String unableToParse() {
        return "unable to parse " + check + " as a module";
    }
}
