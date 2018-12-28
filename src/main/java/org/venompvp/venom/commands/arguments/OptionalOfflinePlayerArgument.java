package org.venompvp.venom.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OptionalOfflinePlayerArgument extends OptionalArgument<OfflinePlayer> {

    public OptionalOfflinePlayerArgument(String check) {
        super(check);
    }

    public OptionalOfflinePlayerArgument() {
        super();
    }

    @Override
    public OfflinePlayer getValue() {
        return Bukkit.getOfflinePlayer(check);
    }

    @Override
    public boolean isArgumentType() {
        return Bukkit.getOfflinePlayer(check) != null;
    }

    @Override
    public String unableToParse() {
        return check + " is unable to parse as a Player";
    }
}
