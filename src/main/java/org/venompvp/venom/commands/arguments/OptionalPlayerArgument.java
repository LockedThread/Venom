package org.venompvp.venom.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OptionalPlayerArgument extends OptionalArgument<Player> {

    public OptionalPlayerArgument(String check) {
        super(check);
    }

    public OptionalPlayerArgument() {
        super();
    }

    @Override
    public Player getValue() {
        return Bukkit.getPlayer(check);
    }

    @Override
    public boolean isArgumentType() {
        return Bukkit.getPlayer(check) != null;
    }

    @Override
    public String unableToParse() {
        return check + " is unable to parse as a Player";
    }
}
