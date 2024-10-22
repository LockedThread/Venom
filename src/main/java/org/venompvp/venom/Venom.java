package org.venompvp.venom;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.dustplanet.util.SilkUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.venompvp.venom.adapters.ItemStackAdapter;
import org.venompvp.venom.adapters.LocationAdapter;
import org.venompvp.venom.commands.PlayTimeCommand;
import org.venompvp.venom.commands.VenomProfileCommand;
import org.venompvp.venom.commands.VenomRootCommand;
import org.venompvp.venom.handlers.CommandHandler;
import org.venompvp.venom.module.Module;
import org.venompvp.venom.module.ModuleInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@ModuleInfo(name = "Venom", author = "Headshot and Simpleness", version = "2.0", description = "Core VenomPVP libraries")
public class Venom extends Module implements Listener {

    public ThreadLocalRandom random;
    public ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static Venom instance;
    public final String ERROR_CONTACT_AUTHOR = "Error please contact Simpleness#6666 on discord.";
    private CommandHandler commandHandler;
    private Gson gson;
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
            economy = getServer().getServicesManager().load(Economy.class);
            worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            protocolManager = ProtocolLibrary.getProtocolManager();
            silkUtil = SilkUtil.hookIntoSilkSpanwers();

            instance = this;
            setupModule(this);

            random = ThreadLocalRandom.current();
            // Gson
            gson = new GsonBuilder()
                    .registerTypeAdapter(ItemStack.class, new ItemStackAdapter(this))
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            // Handlers
            commandHandler = new CommandHandler(this);
            commandHandler.register(this, new VenomRootCommand(this));
            commandHandler.register(this, new PlayTimeCommand(this));
            commandHandler.register(this, new VenomProfileCommand(this));

            File file = new File(getDataFolder().getParentFile().getParent(), "profiles");
            if (!file.exists()) {
                file.mkdirs();
            }

            getServer().getPluginManager().registerEvents(this, this);

            getLogger().info("Finished loading Venom (" + (System.currentTimeMillis() - startTime) + "ms)");
        }
    }

    @Override
    public void onDisable() {
        disableCommands(this);
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

    @Override
    public Gson getGson() {
        return gson;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public ArrayList<Module> getModules() {
        return modules;
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
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.BOAT || event.getEntityType() == EntityType.HORSE) {
            event.setCancelled(true);
        }
    }
}
