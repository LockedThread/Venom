package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.OptionalOfflinePlayerArgument;
import org.venompvp.venom.module.Module;
import org.venompvp.venom.utils.Utils;

import java.util.Collections;
import java.util.List;

public class PlayTimeCommand extends Command implements ParentCommand {

    public PlayTimeCommand(Module module) {
        super(module, "playtime", "command to check player's play time", Collections.singletonList(OptionalOfflinePlayerArgument.class), "venom.playtime");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        OptionalOfflinePlayerArgument argument = (OptionalOfflinePlayerArgument) args.get(0);
        if (argument.isPresent()) {
            OfflinePlayer offlinePlayer = argument.getValue();
            if (!offlinePlayer.isOnline()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + offlinePlayer.getName() + "'s PlayTime: &f" + Utils.formatTime(Utils.getOfflinePlayTime(offlinePlayer) / 20)));
            } else if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + player.getName() + "'s PlayTime: &f" + Utils.formatTime(player.getStatistic(Statistic.PLAY_ONE_TICK) / 20)));
            }
        } else if (sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + sender.getName() + "'s PlayTime: &f" + Utils.formatTime(((Player) sender).getStatistic(Statistic.PLAY_ONE_TICK) / 20)));
        } else {
            sender.sendMessage(getUsage(label));
        }
    }

    @Override
    public String getUsage(String label) {
        return "/" + label + " [player]";
    }

    @Override
    public void setupSubCommands() {

    }
}
