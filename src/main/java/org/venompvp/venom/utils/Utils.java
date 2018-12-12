package org.venompvp.venom.utils;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.venompvp.venom.Venom;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {

    public static boolean compareLocations(Location locA, Location locB) {
        return locA.getBlockX() == locB.getBlockX() &&
                locA.getBlockZ() == locB.getBlockZ() &&
                locA.getBlockY() == locB.getBlockY() &&
                locA.getWorld().getUID().toString().equals(locB.getWorld().getUID().toString());
    }

    public static boolean canEdit(Player player, Location location) {
        MPlayer mPlayer = MPlayer.get(player);
        return Venom.getInstance().getWorldGuardPlugin().canBuild(player, location) && (!mPlayer.hasFaction() &&
                mPlayer.getRelationTo(BoardColl.get().getFactionAt(PS.valueOf(location))) == Rel.MEMBER ||
                mPlayer.getFaction().isPermitted(MPerm.getPermBuild(), mPlayer.getRelationTo(BoardColl.get().getFactionAt(PS.valueOf(location)))));
    }

    public static String capitalizeEveryWord(String s) {
        return !s.contains(" ") ? StringUtils.capitalize(s.toLowerCase()) : Arrays.stream(s.split(" ")).map(word -> StringUtils.capitalize(word.toLowerCase())).collect(Collectors.joining());
    }

    public static String toPercentage(double n) {
        return String.format("%.0f", n * 100) + "%";
    }

    public static String versionFromProtcolVersion(int protocolVersion) {
        switch (protocolVersion) {
            case 4:
                return "1.7.2";
            case 5:
                return "1.7.10";
            case 47:
                return "1.8";
            case 107:
                return "1.9";
            case 108:
                return "1.9.1";
            case 110:
                return "1.9.4";
            case 210:
                return "1.10.2";
            case 315:
                return "1.11";
            case 316:
                return "1.11.2";
            case 335:
                return "1.12";
            case 338:
                return "1.12.1";
            case 340:
                return "1.12.2";
            case 393:
                return "1.13";
            case 401:
                return "1.13.1";
            case 404:
                return "1.13.2";
        }
        return "UNKNOWN";
    }
}
