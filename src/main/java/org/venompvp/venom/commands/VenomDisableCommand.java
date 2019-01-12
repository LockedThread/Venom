package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.ModuleArgument;
import org.venompvp.venom.module.Module;

import java.util.Collections;
import java.util.List;

public class VenomDisableCommand extends Command {

    public VenomDisableCommand(Module module) {
        super(module, module.getCommandHandler().getCommand(VenomRootCommand.class), "disable", "Disables a module", Collections.singletonList(ModuleArgument.class), "venom.reload");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        Module target = (Module) args.get(0).getValue();
        HandlerList.unregisterAll(target);
        module.getServer().getPluginManager().disablePlugin(target);
        sender.sendMessage(ChatColor.GREEN + "Disabeles " + target.getName());

    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label + " [module]";
    }
}
