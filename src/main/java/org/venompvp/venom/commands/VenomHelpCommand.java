package org.venompvp.venom.commands;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.module.Module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VenomHelpCommand extends Command {

    public VenomHelpCommand(Module module) {
        super(module, module.getCommandHandler().getCommand(VenomRootCommand.class), "help", "shows VenomPVP administration related tools", Collections.emptyList(), "venom.help", "commands");
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8---------------------[&cVenomHelp&8]---------------------"));
        module.getCommandHandler().getModuleCommands().forEach((key, commands) -> {
            ArrayList<String> lines = new ArrayList<>();
            commands.forEach(command -> {
                if (command.getSubCommands().isEmpty()) {
                    lines.add(ChatColor.YELLOW + command.getUsage(command.getName()));
                } else {
                    command.getSubCommands().stream().map(subCommand -> ChatColor.YELLOW + subCommand.getUsage(subCommand.getName())).forEach(lines::add);
                }
            });
            TextComponent textComponent = new TextComponent(key.getName());
            textComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lines.stream().map(s -> lines.get(lines.size() - 1).equals(s) ? s : s + "\n").collect(Collectors.joining())).create()));
            sender.sendMessage(textComponent);
        });
    }

    @Override
    public String getUsage(String label) {
        return "/venom " + label;
    }
}
