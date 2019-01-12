package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.StringArrayArgument;
import org.venompvp.venom.module.Module;

import java.util.Collections;
import java.util.List;

public class VenomEnableCommand extends Command {

    public VenomEnableCommand(Module module) {
        super(module,
                module.getCommandHandler().getCommand(VenomRootCommand.class),
                "enable",
                "Disables a module",
                Collections.singletonList(StringArrayArgument.class),
                "venom.reload");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        String[] strings = (String[]) args.get(0).getValue();
        module.getServer().getPluginManager().enablePlugin(module.getServer().getPluginManager().getPlugin(strings[0]));
        sender.sendMessage(ChatColor.GREEN + "Enables " + strings[0]);

    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label + " [module]";
    }
}
