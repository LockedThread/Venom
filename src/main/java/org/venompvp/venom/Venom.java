package org.venompvp.venom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.venompvp.venom.adapters.ItemStackAdapter;
import org.venompvp.venom.adapters.LocationAdapter;
import org.venompvp.venom.commands.VenomRootCommand;
import org.venompvp.venom.handlers.CommandHandler;
import org.venompvp.venom.handlers.DatabaseHandler;
import org.venompvp.venom.module.Module;
import org.venompvp.venom.module.ModuleInfo;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@ModuleInfo(name = "Venom", author = "Headshot and LilProteinShake", version = "1.0", description = "Core VenomPVP libraries")
public class Venom extends Module {


    public Random random;
    public ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static Venom instance;
    public final String ERROR_CONTACT_AUTHOR = "Error please contact Lil Protein Shake#3129 or Headshot#7752 on discord.";
    public CommandHandler commandHandler;
    public DatabaseHandler databaseHandler;
    public Gson gson;
    private ArrayList<Module> modules = new ArrayList<>();

    public static Venom getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        setupModule(this);

        random = new Random();
        // Gson
        gson = new GsonBuilder()
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter(getVenom()))
                .registerTypeAdapter(Location.class, new LocationAdapter(getVenom()))
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        // Handlers
        databaseHandler = new DatabaseHandler(getVenom());
        commandHandler = new CommandHandler(getVenom());

        final long startTime = System.currentTimeMillis();
        commandHandler.register(this, new VenomRootCommand(this));


        getLogger().info("Finished loading Venom (" + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
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
}
