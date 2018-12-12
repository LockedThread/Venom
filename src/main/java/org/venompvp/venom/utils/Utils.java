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
}
