package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.ModuleArgument;
import org.venompvp.venom.module.Module;

import java.util.Collections;
import java.util.List;

public class VenomReloadCommand extends Command {

    public VenomReloadCommand(Module module) {
        super(module, module.getCommandHandler().getCommand(VenomRootCommand.class), "reload", "Reloads configs", Collections.singletonList(ModuleArgument.class), "venom.reload");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        Module module = (Module) args.get(0).getValue();
        module.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "You have reloaded " + module.getName());
    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label + " [module]";
    }
}
