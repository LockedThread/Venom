package org.venompvp.venom.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgument extends Argument<Player> {

    public PlayerArgument(String check) {
        super(check);
    }

    @Override
    public boolean isArgumentType() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(check) || player.getUniqueId().toString().equalsIgnoreCase(check)) {
                setValue(player);
                return true;
            }
        }
        return false;
    }
}
