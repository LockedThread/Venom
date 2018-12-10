package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.module.Module;

import java.util.Collections;
import java.util.List;

public class VenomRootCommand extends Command implements ParentCommand {

    public VenomRootCommand(Module module) {
        super(module,
                "venom",
                "root venom command",
                Collections.emptyList(),
                "venom.root",
                "venompvp");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        sender.sendMessage(ChatColor.RED + "For correct usage of this command please execute /venom help");
    }

    @Override
    public String getUsage(String label) {
        return "/" + label;
    }

    @Override
    public void setupSubCommands() {
        addSubCommands(new VenomModulesCommand(module), new VenomInfoCommand(module), new VenomHelpCommand(module));
    }
}
