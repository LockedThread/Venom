package org.venompvp.venom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.StringArrayArgument;
import org.venompvp.venom.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VenomEventLookupCommand extends Command {

    public VenomEventLookupCommand(Module module) {
        super(module,
                module.getCommandHandler().getCommand(VenomRootCommand.class),
                "eventlookup",
                "Finds registered events",
                Collections.singletonList(StringArrayArgument.class),
                "venom.eventlook",
                false,
                "lookup");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        String[] stringArgs = (String[]) args.get(0).getValue();
        if (stringArgs.length == 1) {
            String eventName = stringArgs[0];
            final ArrayList<String> plugins = getEvents(eventName);
            if (plugins.isEmpty()) {
                sender.sendMessage(ChatColor.DARK_RED + "No plugins have the event \"" + eventName + "\" registered");
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------[&cEvents&8]----------------------"));
                plugins.stream().map(p -> ChatColor.YELLOW + p).forEach(sender::sendMessage);
            }
        }
    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label;
    }

    private ArrayList<String> getEvents(String whitelist) {
        ArrayList<String> events = new ArrayList<>();
        Arrays.stream(getModule().getServer().getPluginManager().getPlugins()).forEach(plugin -> HandlerList.getRegisteredListeners(plugin).forEach(registeredListener -> Arrays.stream(registeredListener.getListener().getClass().getMethods()).forEach(method -> Arrays.stream(method.getParameterTypes()).filter(parameterType -> parameterType.getSimpleName().equalsIgnoreCase(whitelist) && !events.contains(registeredListener.getPlugin().getName())).forEach(parameterType -> events.add(plugin.getName())))));
        return events;
    }
}
