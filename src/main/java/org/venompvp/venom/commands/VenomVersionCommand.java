package org.venompvp.venom.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.StringArrayArgument;
import org.venompvp.venom.module.Module;
import org.venompvp.venom.utils.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class VenomVersionCommand extends Command {

    public VenomVersionCommand(Module module) {
        super(module,
                module.getCommandHandler().getCommand(VenomRootCommand.class),
                "version",
                "checks the version",
                Collections.singletonList(StringArrayArgument.class),
                "venom.version", false);
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        String[] stringArgs = (String[]) args.get(0).getValue();
        if (stringArgs.length == 1) {
            if (stringArgs[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------[&cVersions&8]----------------------"));
                HashMap<Integer, Integer> map = new HashMap<>();
                Bukkit.getOnlinePlayers().stream().mapToInt(player -> getModule().getVenom().getProtocolManager().getProtocolVersion(player)).forEach(protocolVersion -> {
                    map.computeIfPresent(protocolVersion, (k, v) -> v++);
                    map.putIfAbsent(protocolVersion, 1);
                });
                map.forEach((key, value) -> sender.sendMessage(ChatColor.RED + "" + Utils.versionFromProtcolVersion(key) + ChatColor.YELLOW + " : " + ChatColor.RED + value + " (" + Utils.toPercentage((double) value / (double) Bukkit.getOnlinePlayers().size()) + ")"));
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + getUsage(label));
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Incorrect usage, please use: " + getUsage(label));
        }
    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label;
    }

}
