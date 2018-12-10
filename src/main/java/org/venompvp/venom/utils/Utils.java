package org.venompvp.venom.utils;

import org.bukkit.Location;

public class Utils {

    public static boolean compareLocations(Location locA, Location locB) {
        return locA.getX() == locB.getX() && locA.getZ() == locB.getZ() && locA.getY() == locB.getY() && locA.getWorld().getUID().toString().equals(locB.getWorld().getUID().toString());
    }
}
