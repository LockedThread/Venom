package org.venompvp.venom.commands;


import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    public final Module module;

    private final String name;
    private final String description;
    private final List<String> aliases;
    private final List<Class<? extends Argument>> presetArguments;

    // Sub Commands
    private final Command parentCommand;
    private final List<Command> subCommands;
    private final String permission;
    private final boolean requirePlayer;

    public Command(Module module, Command parentCommand, String name, String description, List<Class<? extends Argument>> presetArguments, String permission, boolean requirePlayer, String... aliases) {
        this.module = module;
        this.permission = permission;
        this.subCommands = new ArrayList<>();
        this.parentCommand = parentCommand;
        this.name = name;
        this.description = description;
        this.presetArguments = presetArguments;
        this.requirePlayer = requirePlayer;
        this.aliases = Arrays.asList(aliases);
    }

    public Command(Module module, Command parentCommand, String name, String description, List<Class<? extends Argument>> presetArguments, String permission, String... aliases) {
        this(module, parentCommand, name, description, presetArguments, permission, false, aliases);
    }

    public Command(Module module, String name, String description, List<Class<? extends Argument>> presetArguments, String permission, boolean requirePlayer, String... aliases) {
        this(module, null, name, description, presetArguments, permission, requirePlayer, aliases);
    }

    public Command(Module module, String name, String description, List<Class<? extends Argument>> presetArguments, String permission, String... aliases) {
        this(module, null, name, description, presetArguments, permission, false, aliases);
    }


    public boolean isRequirePlayer() {
        return requirePlayer;
    }

    public boolean isSubCommand() {
        return parentCommand != null;
    }

    public abstract void execute(CommandSender sender, List<Argument> args, String label);

    public abstract String getUsage(String label);

    public void addSubCommands(Command... commands) {
        subCommands.addAll(Arrays.asList(commands));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<Class<? extends Argument>> getPresetArguments() {
        return presetArguments;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }

    public Command getParentCommand() {
        return parentCommand;
    }

    public Module getModule() {
        return module;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
