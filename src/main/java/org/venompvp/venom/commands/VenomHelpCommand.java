package org.venompvp.venom.commands;

import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.module.Module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VenomHelpCommand extends Command {

    public VenomHelpCommand(Module module) {
        super(module, module.getCommandHandler().getCommand(VenomRootCommand.class), "help", "shows VenomPVP administration related tools", Collections.emptyList(), "venom.help", "commands");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8---------------------[&cVenomHelp&8]---------------------"));
        module.getCommandHandler().getModuleCommands().forEach((key, commands) -> {
            ArrayList<String> lines = new ArrayList<>();
            commands.forEach(command -> {
                if (command.getSubCommands().isEmpty()) {
                    lines.add(ChatColor.YELLOW + command.getUsage(command.getName()));
                } else {
                    command.getSubCommands().stream().map(subCommand -> ChatColor.YELLOW + subCommand.getUsage(subCommand.getName())).forEach(lines::add);
                }
            });
            new FancyMessage(key.getName()).color(ChatColor.RED)
                    .tooltip(lines)
                    .send(sender);
        });
    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label;
    }
}
