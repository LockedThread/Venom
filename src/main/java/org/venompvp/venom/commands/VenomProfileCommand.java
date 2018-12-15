package org.venompvp.venom.commands;

import com.sun.management.HotSpotDiagnosticMXBean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.venompvp.venom.commands.arguments.Argument;
import org.venompvp.venom.commands.arguments.StringArrayArgument;
import org.venompvp.venom.module.Module;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;

public class VenomProfileCommand extends Command implements ParentCommand {

    public VenomProfileCommand(Module module) {
        super(module, "profile", "creates a memory profile", Collections.singletonList(StringArrayArgument.class), "venom.dev", false);
    }

    @Override
    public void execute(CommandSender sender, List<Argument> args, String label) {
        String[] stringArgs = (String[]) args.get(0).getValue();
        sender.sendMessage(stringArgs.length > 1 ?
                ChatColor.DARK_RED + "Incorrect usage, please use " + getUsage(label) :
                dump(stringArgs[0]) ?
                        ChatColor.GREEN + "Memory Profile created." :
                        ChatColor.DARK_RED + "A Memory Profile with that name already exists!");
    }

    @Override
    public String getUsage(String label) {
        return "/venom profile [name]";
    }

    @Override
    public void setupSubCommands() {

    }

    private boolean dump(String name) {
        System.out.println(Bukkit.getWorldContainer().getAbsoluteFile().getName() + File.separator + "profiles" + File.separator + name + ".hprof");
        try {
            ManagementFactory
                    .newPlatformMXBeanProxy(ManagementFactory.getPlatformMBeanServer(), "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class)
                    .dumpHeap(Bukkit.getWorldContainer().getAbsoluteFile().getName() + File.separator + "profiles" + File.separator + name + ".hprof", true);
        } catch (IOException ignored) {
            return false;
        }
        return true;
    }
}
