package org.venompvp.venom;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.dustplanet.util.SilkUtil;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.venompvp.venom.adapters.ItemStackAdapter;
import org.venompvp.venom.adapters.LazyChunkAdapter;
import org.venompvp.venom.adapters.LocationAdapter;
import org.venompvp.venom.commands.VenomRootCommand;
import org.venompvp.venom.handlers.CommandHandler;
import org.venompvp.venom.module.Module;
import org.venompvp.venom.module.ModuleInfo;
import org.venompvp.venom.objs.LazyChunk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@ModuleInfo(name = "Venom", author = "Headshot and LilProteinShake", version = "1.0", description = "Core VenomPVP libraries")
public class Venom extends Module implements Listener {

    public Random random;
    public ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static Venom instance;
    public final String ERROR_CONTACT_AUTHOR = "Error please contact Lil Protein Shake#3129 or Headshot#7752 on discord.";
    public CommandHandler commandHandler;
    public Gson gson;
    private Permission perms;
    private Economy economy;
    private WorldGuardPlugin worldGuardPlugin;
    private ProtocolManager protocolManager;
    private SilkUtil silkUtil;

    private ArrayList<Module> modules = new ArrayList<>();

    public static Venom getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        if (setupDependencies()) {
            final long startTime = System.currentTimeMillis();
            perms = getServer().getServicesManager().load(Permission.class);
            economy = getServer().getServicesManager().load(Economy.class);
            worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            protocolManager = ProtocolLibrary.getProtocolManager();
            silkUtil = SilkUtil.hookIntoSilkSpanwers();

            instance = this;
            setupModule(this);

            getServer().getPluginManager().registerEvents(this, this);

            random = new Random();
            // Gson
            gson = new GsonBuilder()
                    .registerTypeAdapter(ItemStack.class, new ItemStackAdapter(this))
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .registerTypeAdapter(LazyChunk.class, new LazyChunkAdapter())
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            // Handlers
            commandHandler = new CommandHandler(this);
            commandHandler.register(this, new VenomRootCommand(this));

            File file = new File(getDataFolder().getParentFile().getParent(), "profiles");
            if (!file.exists()) {
                file.mkdirs();
            }

            getLogger().info("Finished loading Venom (" + (System.currentTimeMillis() - startTime) + "ms)");
        }
    }

    @Override
    public void onDisable() {
        disableCommands();
    }

    private boolean setupDependencies() {
        List<String> plugins = new ArrayList<>();
        if (getServer().getPluginManager().getPlugin("Vault") == null) plugins.add("Vault");
        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) plugins.add("WorldGuard");
        if (getServer().getPluginManager().getPlugin("Factions") == null) plugins.add("Factions");
        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) plugins.add("ProtocolLib");
        if (getServer().getPluginManager().getPlugin("SilkSpawners") == null) plugins.add("SilkSpawners");
        if (!plugins.isEmpty()) {
            getLogger().severe(Joiner.on(", ").skipNulls().join(plugins) + " must be installed on the server!");
            getServer().shutdown();
            return false;
        }
        return true;

    }

    public Gson getGson() {
        return gson;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public ItemStack itemStackFromConfig(FileConfiguration fileConfiguration, String section) {
        ItemStack itemStack = new ItemStack(Material.matchMaterial(fileConfiguration.getString(section + ".material")));
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString(section + ".name")));
        meta.setLore(fileConfiguration.getStringList(section + ".lore").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public Permission getPerms() {
        return perms;
    }

    public Economy getEconomy() {
        return economy;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public SilkUtil getSilkUtil() {
        return silkUtil;
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
}
