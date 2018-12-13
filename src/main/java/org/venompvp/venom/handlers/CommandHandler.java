package org.venompvp.venom.handlers;

import com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.venompvp.venom.Venom;
import org.venompvp.venom.commands.Command;
import org.venompvp.venom.commands.ParentCommand;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.StringArrayArgument;
import org.venompvp.venom.module.Module;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandHandler implements CommandExecutor {

    private Venom instance;
    private ArrayList<Command> commands = new ArrayList<>();
    private HashMap<Module, ArrayList<Command>> moduleCommands = new HashMap<>();

    public CommandHandler(Venom instance) {
        this.instance = instance;
    }

    public Command getCommand(Class<? extends Command> commandClass) {
        return moduleCommands.entrySet().stream().flatMap(entry -> entry.getValue().stream()).filter(command -> command.getClass().getName().equals(commandClass.getName())).findFirst().orElse(null);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        for (Map.Entry<Module, ArrayList<Command>> entry : moduleCommands.entrySet()) {
            List<Command> commands = entry.getValue();
            for (Command command : commands) {
                if (bukkitCommand.getName().equalsIgnoreCase(command.getName()) || command.getAliases().contains(bukkitCommand.getName().toLowerCase())) {
                    if (args.length > 0) {
                        if (!command.getSubCommands().isEmpty()) {
                            for (Command subCommand : command.getSubCommands()) {
                                if (subCommand.getName().equalsIgnoreCase(args[0]) || subCommand.getAliases().contains(args[0].toLowerCase())) {
                                    if (!commandSender.hasPermission(subCommand.getPermission())) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + "You don't have permission to access " + subCommand.getUsage(label));
                                        return true;
                                    }
                                    List<Argument> arguments = new ArrayList<>();
                                    args = Arrays.copyOfRange(args, 1, args.length);
                                    for (int i = 0; i < subCommand.getPresetArguments().size(); i++) {
                                        Class<? extends Argument> argumentClass = subCommand.getPresetArguments().get(i);
                                        if (argumentClass.getName().equals(StringArrayArgument.class.getName()) && args.length > 1) {
                                            try {
                                                subCommand.execute(commandSender, new ArrayList<>(Collections.<Argument>singletonList(StringArrayArgument.class.getConstructor(String.class).newInstance(Joiner.on(" ").skipNulls().join(args)))), label);
                                            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                                e.printStackTrace();
                                            }
                                            return true;
                                        }
                                        if (args.length <= i) {
                                            commandSender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + command.getUsage(label));
                                            return true;
                                        }
                                        try {
                                            Argument argument = argumentClass.getConstructor(String.class).newInstance(args[i]);
                                            if (!argument.isArgumentType()) {
                                                commandSender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + command.getUsage(label));
                                                return true;
                                            }
                                            arguments.add(argument);
                                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    subCommand.execute(commandSender, arguments, label);
                                    return true;
                                }
                            }
                        } else {
                            if (!commandSender.hasPermission(command.getPermission())) {
                                commandSender.sendMessage(ChatColor.DARK_RED + "You don't have permission to access " + command.getUsage(label));
                                return true;
                            }
                            if (!command.getPresetArguments().isEmpty()) {
                                List<Argument> arguments = new ArrayList<>();
                                for (int i = 0; i < command.getPresetArguments().size(); i++) {
                                    Class<? extends Argument> argumentClass = command.getPresetArguments().get(i);
                                    if (argumentClass.getName().equals(StringArrayArgument.class.getName()) && args.length > 1) {
                                        try {
                                            command.execute(commandSender, new ArrayList<>(Collections.<Argument>singletonList(StringArrayArgument.class.getConstructor(String.class).newInstance(Joiner.on(" ").skipNulls().join(args)))), label);
                                        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                        return true;
                                    }
                                    if (args.length <= i) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + command.getUsage(label));
                                        return true;
                                    }
                                    try {
                                        Argument argument = argumentClass.getConstructor(String.class).newInstance(args[i]);
                                        if (!argument.isArgumentType()) {
                                            commandSender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + command.getUsage(label));
                                            return true;
                                        }
                                        arguments.add(argument);
                                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                                command.execute(commandSender, arguments, label);
                                return true;
                            } else {
                                if (command.getPresetArguments().isEmpty()) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + command.getUsage(label));
                                    return true;
                                }
                            }
                        }
                    }
                    if (!commandSender.hasPermission(command.getPermission())) {
                        commandSender.sendMessage(ChatColor.DARK_RED + "You don't have permission to access " + command.getUsage(label));
                        return true;
                    }
                    if (args.length == 0 && !command.getPresetArguments().isEmpty()) {
                        commandSender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + command.getUsage(label));
                        return true;
                    }
                    command.execute(commandSender, Collections.emptyList(), label);
                }
            }
        }
        return true;
    }

    public void register(Module module, Command command) {
        CommandMap commandMap = getCommandMap();
        PluginCommand pluginCommand = getPluginCommand(command);
        if (command.getParentCommand() != null) {
            throw new RuntimeException("You can't register a sub command!");
        } else if (commandMap != null && pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setAliases(command.getAliases());
            commandMap.register(instance.getDescription().getName(), pluginCommand);
            if (getModuleCommands().containsKey(module)) {
                ArrayList<Command> commands = getModuleCommands().get(module);
                commands.add(command);
                getModuleCommands().put(module, commands);
            }
            getModuleCommands().putIfAbsent(module, new ArrayList<>(Collections.singleton(command)));
            ((ParentCommand) command).setupSubCommands();
            instance.getLogger().info("Registered command: " + command.getName());
        } else {
            throw new RuntimeException("Unable to register command \n" + command.getName() + "\n. " + instance.ERROR_CONTACT_AUTHOR);
        }
    }


    public void unregister(Command command) {
        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            Object object = field.get(instance.getServer().getPluginManager());
            if (object instanceof Map) {
                Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) field.get(instance.getServer().getPluginManager());
                knownCommands.remove(command.getName());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            instance.getLogger().info("Unable to unregister " + command.getName() + ". " + instance.ERROR_CONTACT_AUTHOR);
            e.printStackTrace();
        }
    }

    private CommandMap getCommandMap() {
        try {
            if (instance.getServer().getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                return (CommandMap) field.get(instance.getServer().getPluginManager());
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            instance.getLogger().info("Unable to get the Bukkit CommandMap. " + instance.ERROR_CONTACT_AUTHOR);
            e.printStackTrace();
        }
        return null;
    }

    private PluginCommand getPluginCommand(Command command) {
        try {
            Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            commandConstructor.setAccessible(true);
            return commandConstructor.newInstance(command.getName(), instance);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Module, ArrayList<Command>> getModuleCommands() {
        return moduleCommands;
    }
}
