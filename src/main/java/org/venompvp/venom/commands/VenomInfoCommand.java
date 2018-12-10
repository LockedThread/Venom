package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.module.Module;

import java.util.Collections;
import java.util.List;

public class VenomInfoCommand extends Command {

    public VenomInfoCommand(Module module) {
        super(module,
                module.getCommandHandler().getCommand(VenomRootCommand.class),
                "info",
                "shows info about VenomPVP",
                Collections.emptyList(),
                "venom.info");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8---------------------[&cVenomInfo&8]---------------------"));
        sender.sendMessage(ChatColor.YELLOW + "Venom is a modular plugin library for VenomPVP.org, the library allows plugins to be created easier and are more centralized. Do /venom help to view all of the commands throughout all of the modules.");
        sender.sendMessage("");
    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label;
    }
}
