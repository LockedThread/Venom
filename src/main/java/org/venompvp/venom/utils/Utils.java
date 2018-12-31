package org.venompvp.venom.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.venompvp.venom.Venom;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Utils {

    public static void editBlockType(Location blockLocation, Material updatedMatertial, boolean update) {
        Bukkit.getScheduler().runTaskAsynchronously(Venom.getInstance(), () -> {
            World w = ((CraftWorld) blockLocation.getWorld()).getHandle();
            BlockPosition bp = new BlockPosition(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
            IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(updatedMatertial.getId());
            Bukkit.getScheduler().runTask(Venom.getInstance(), () -> {
                if (update)
                    w.setTypeUpdate(bp, ibd);
                else
                    w.setTypeAndData(bp, ibd, 2);
            });
        });
    }

    public static void editBlockType(Location blockLocation, Material updatedMatertial) {
        editBlockType(blockLocation, updatedMatertial, false);
    }

    public static boolean compareLocations(Location locA, Location locB) {
        return locA.getBlockX() == locB.getBlockX() &&
                locA.getBlockZ() == locB.getBlockZ() &&
                locA.getBlockY() == locB.getBlockY() &&
                locA.getWorld().getName().equals(locB.getWorld().getName());
    }

    public static boolean canEdit(Player player, Location location) {
        MPlayer mPlayer = MPlayer.get(player);
        return Venom.getInstance().getWorldGuardPlugin().canBuild(player, location) && (!getFactionAt(location).isNone() ||
                mPlayer.getFaction().isPermitted(MPerm.getPermBuild(), mPlayer.getRelationTo(BoardColl.get().getFactionAt(PS.valueOf(location)))));
    }

    public static String capitalizeEveryWord(String s) {
        return !s.contains(" ") ?
                StringUtils.capitalize(s.toLowerCase()) :
                Arrays.stream(s.split(" ")).map(word -> StringUtils.capitalize(word.toLowerCase())).collect(Collectors.joining());
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

    public static ItemStack configSectionToItemStack(FileConfiguration c, String where) {
        Material material = Material.matchMaterial(c.getString(where + ".material"));
        short data = material == Material.INK_SACK
                ? DyeColor.valueOf(c.getString(where + ".glasspane.color").toUpperCase()).getDyeData()
                : DyeColor.valueOf(c.getString(where + ".glasspane.color").toUpperCase()).getWoolData();

        ItemStack itemStack = c.getBoolean(where + ".glasspane.enabled") ?
                new ItemStack(Material.matchMaterial(c.getString(where + ".material")), 1, data)
                : new ItemStack(Material.matchMaterial(c.getString(where + ".material")));

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c.getString(where + ".name")));
        if (c.getBoolean(where + ".enchanted")) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(c.getStringList(where + ".lore").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static boolean isItem(ItemStack a, ItemStack b) {
        return a != null && b != null && a.getType() == b.getType() &&
                a.hasItemMeta() &&
                b.hasItemMeta() &&
                a.getItemMeta().hasDisplayName() &&
                b.getItemMeta().hasDisplayName() &&
                a.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', b.getItemMeta().getDisplayName()));
    }

    public static Faction getFactionByPlayer(Player player) {
        return MPlayer.get(player).getFaction();
    }

    public static Faction getFactionAt(Location location) {
        return getFactionAt(location.getChunk());
    }

    public static Faction getFactionAt(Chunk chunk) {
        return BoardColl.get().getFactionAt(PS.valueOf(chunk));
    }

    public static boolean factionIsServerFaction(Faction faction) {
        return faction.getName().equalsIgnoreCase("Wilderness") || faction.getName().equalsIgnoreCase("Warzone") || faction.getName().equalsIgnoreCase("Safezone");
    }

    public static boolean chunkIsInClaims(Faction faction, Chunk chunk) {
        return getFactionAt(chunk).getName().equalsIgnoreCase(faction.getName());
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static String formatTime(long seconds) {
        long dayCount = TimeUnit.SECONDS.toDays(seconds);
        long secondsCount = seconds - TimeUnit.DAYS.toSeconds(dayCount);
        long hourCount = TimeUnit.SECONDS.toHours(secondsCount);
        secondsCount -= TimeUnit.HOURS.toSeconds(hourCount);
        long minutesCount = TimeUnit.SECONDS.toMinutes(secondsCount);
        secondsCount -= TimeUnit.MINUTES.toSeconds(minutesCount);
        StringBuilder stringBuilder = new StringBuilder();
        if (dayCount > 0) {
            stringBuilder.append(String.format("%d %s, ", dayCount, (dayCount == 1) ? "day" : "days"));
        }
        if (hourCount > 0) {
            stringBuilder.append(String.format("%d %s, ", hourCount, (hourCount == 1) ? "hour" : "hours"));
        }
        if (minutesCount > 0) {
            stringBuilder.append(String.format("%d %s, ", minutesCount, (minutesCount == 1) ? "minute" : "minutes"));
        }
        stringBuilder.append(String.format("%d %s.", secondsCount, (secondsCount == 1) ? "second" : "seconds"));
        return stringBuilder.toString();
    }

    public static long getOfflinePlayTime(OfflinePlayer player) {
        File stats = new File(new File(Bukkit.getWorlds().get(0).getWorldFolder(), "stats"), player.getUniqueId().toString() + ".json");
        try {
            if (stats.exists()) {
                return new JsonParser()
                        .parse(new FileReader(stats))
                        .getAsJsonObject()
                        .get("stat.playOneMinute")
                        .getAsLong();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int sub10OrReturn0(int i, int divisor) {
        return i < 0 ? -1 : i % divisor > 0 && i < divisor ? i % divisor : 0;
    }

    public static String saveTextToHastebin(String text) {
        try {
            String url = "https://hastebin.com/";
            URL obj = new URL(url + "documents");

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            con.setDoOutput(true);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8));
            wr.write(text);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.lines().collect(Collectors.joining());

            in.close();

            JsonElement json = new JsonParser().parse(response);
            if (!json.isJsonObject()) throw new IOException("Cannot parse JSON");
            return url + json.getAsJsonObject().get("key").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
