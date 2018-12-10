package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.module.Module;

import java.util.Collections;
import java.util.List;

public class VenomModulesCommand extends Command {

    public VenomModulesCommand(Module module) {
        super(module,
                module.getCommandHandler().getCommand(VenomRootCommand.class),
                "modules",
                "modules subcommand",
                Collections.emptyList(),
                "venom.modules",
                "mods");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------[&cModules&8]----------------------"));
        for (Module mod : module.getVenom().getModules()) {
            sender.sendMessage(ChatColor.GREEN + mod.getModuleInfo().name() + " V" + mod.getModuleInfo().version() + " by " + mod.getModuleInfo().author());
        }
        sender.sendMessage("");
    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label;
    }

}
