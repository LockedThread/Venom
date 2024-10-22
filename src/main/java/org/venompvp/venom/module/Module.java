package org.venompvp.venom.module;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import org.venompvp.venom.Venom;
import org.venompvp.venom.handlers.CommandHandler;

public abstract class Module extends JavaPlugin {

    public void setupModule(Module module) {
        ModuleInfo annotation = getModuleInfo();
        if (annotation == null) {
            getVenom().getLogger().severe("Unable to enable " + module.getDescription().getName() + ", the @ModuleInfo annotation is not present.");
            getVenom().getPluginLoader().disablePlugin(module);
            return;
        }
        getVenom().getLogger().info(annotation.name() + " V" + annotation.version() + " by " + annotation.author() + " has been enabled!");
        getVenom().getModules().add(module);
    }

    public void disableCommands(Module module) {
        getCommandHandler().getModuleCommands().get(module).forEach(command -> getCommandHandler().unregister(command));
    }

    public Gson getGson() {
        return Venom.getInstance().getGson();
    }

    public Venom getVenom() {
        return Venom.getInstance();
    }

    public CommandHandler getCommandHandler() {
        return Venom.getInstance().getCommandHandler();
    }

    public ModuleInfo getModuleInfo() {
        return this.getClass().getAnnotation(ModuleInfo.class);
    }
}
