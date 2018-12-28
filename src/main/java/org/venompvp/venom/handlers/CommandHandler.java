package org.venompvp.venom.handlers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.venompvp.venom.Venom;
import org.venompvp.venom.commands.Command;
import org.venompvp.venom.commands.ParentCommand;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.OptionalArgument;
import org.venompvp.venom.commands.arguments.StringArrayArgument;
import org.venompvp.venom.module.Module;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandHandler implements CommandExecutor {

    private Venom instance;
    private HashMap<Module, ArrayList<Command>> moduleCommands = new HashMap<>();

    public CommandHandler(Venom instance) {
        this.instance = instance;
    }

    public Command getCommand(Class<? extends Command> commandClass) {
        return moduleCommands.entrySet().stream().flatMap(entry -> entry.getValue().stream()).filter(command -> command.getClass().getName().equals(commandClass.getName())).findFirst().orElse(null);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        moduleCommands.forEach((key, value) ->
                value.stream()
                        .filter(command -> bukkitCommand.getName().equalsIgnoreCase(command.getName()) || command.getAliases().contains(bukkitCommand.getName().toLowerCase()))
                        .forEach(command -> {
                            try {
                                runCommand(commandSender, command, args, label);
                            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }));
        return true;
    }

    private void runCommand(CommandSender sender, Command command, String[] args, String label) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command.getPresetArguments().isEmpty() && args.length == 0) {
            command.execute(sender, Collections.emptyList(), label);
        } else {
            if (!command.getSubCommands().isEmpty()) {
                if (args.length > 0) {
                    for (Command subCommand : command.getSubCommands()) {
                        if (subCommand.getName().equalsIgnoreCase(args[0]) || subCommand.getAliases().contains(args[0].toLowerCase())) {
                            if (subCommand.getPresetArguments().size() == 0) {
                                subCommand.execute(sender, Collections.emptyList(), label);
                                return;
                            } else {
                                final String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
                                ArrayList<Argument> arguments = new ArrayList<>();
                                for (int i = 0; i < subCommand.getPresetArguments().size(); i++) {
                                    Class<? extends Argument> argumentClass = subCommand.getPresetArguments().get(i);
                                    if (argumentClass.getSuperclass().getName().equalsIgnoreCase(OptionalArgument.class.getName())) {
                                        if (subCommandArgs.length - 1 >= i) {
                                            Argument argument = argumentClass.getConstructor(String.class).newInstance(subCommandArgs[i]);
                                            if (!argument.isArgumentType()) {
                                                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + argument.unableToParse());
                                                return;
                                            } else {
                                                arguments.add(argument);
                                            }
                                        } else {
                                            arguments.add(argumentClass.getConstructor().newInstance());
                                        }
                                    } else if (argumentClass.getName().equals(StringArrayArgument.class.getName())) {
                                        StringArrayArgument stringArrayArgument = (StringArrayArgument) argumentClass.getConstructor(String[].class).newInstance((Object) Arrays.copyOfRange(args, i, args.length));
                                        command.execute(sender, Collections.singletonList(stringArrayArgument), label);
                                        return;
                                    } else {
                                        Argument argument = argumentClass.getConstructor(String.class).newInstance(subCommandArgs[i]);
                                        if (!argument.isArgumentType()) {
                                            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + argument.unableToParse());
                                            return;
                                        }
                                        arguments.add(argument);
                                    }
                                }
                                subCommand.execute(sender, arguments, args[0]);
                            }
                        }
                    }
                }
            } else {
                if (command.getPresetArguments().size() == 0) {
                    command.execute(sender, Collections.emptyList(), label);
                } else {
                    ArrayList<Argument> arguments = new ArrayList<>();
                    for (int i = 0; i < command.getPresetArguments().size(); i++) {
                        Class<? extends Argument> argumentClass = command.getPresetArguments().get(i);
                        if (argumentClass.getSuperclass().getName().equalsIgnoreCase(OptionalArgument.class.getName())) {
                            if (args.length - 1 >= i) {
                                Argument argument = argumentClass.getConstructor(String.class).newInstance(args[i]);
                                if (!argument.isArgumentType()) {
                                    sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + argument.unableToParse());
                                    return;
                                } else {
                                    arguments.add(argument);
                                }
                            } else {
                                arguments.add(argumentClass.getConstructor().newInstance());
                            }
                            return;
                        } else if (argumentClass.getName().equals(StringArrayArgument.class.getName())) {
                            StringArrayArgument stringArrayArgument = (StringArrayArgument) argumentClass.getConstructor(String[].class).newInstance((Object) Arrays.copyOfRange(args, i, args.length));
                            command.execute(sender, Collections.singletonList(stringArrayArgument), label);
                            return;
                        } else {
                            Argument argument = argumentClass.getConstructor(String.class).newInstance(args[i]);
                            if (!argument.isArgumentType()) {
                                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + argument.unableToParse());
                                return;
                            }
                            arguments.add(argument);
                        }
                    }
                    command.execute(sender, arguments, label);
                }
            }
        }
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
            SimplePluginManager pluginManager = (SimplePluginManager) instance.getServer().getPluginManager();
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            Object object = field.get(pluginManager);
            if (object instanceof Map) {
                Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) field.get(pluginManager);
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
