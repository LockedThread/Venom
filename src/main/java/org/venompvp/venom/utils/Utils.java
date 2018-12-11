package org.venompvp.venom.utils;

import org.bukkit.Location;

public class Utils {

    public static boolean compareLocations(Location locA, Location locB) {
        return locA.getBlockX() == locB.getBlockX() &&
                locA.getBlockZ() == locB.getBlockZ() &&
                locA.getBlockY() == locB.getBlockY() &&
                locA.getWorld().getUID().toString().equals(locB.getWorld().getUID().toString());
    }
}
