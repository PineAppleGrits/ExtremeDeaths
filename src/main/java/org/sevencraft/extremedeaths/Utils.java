package org.sevencraft.extremedeaths;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.sevencraft.extremedeaths.subcommands.SubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static List<String> getStrings(CommandSender commandSender, Map<String, SubCommand> subcommands) {
        return new ArrayList<>(subcommands.entrySet().stream().filter(entry -> {
            if (entry.getValue().getPermission() == null) return true;
            else return commandSender.hasPermission(entry.getValue().getPermission());
        }).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())).keySet());
    }

    public static String tlc(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public class Permissions {
        public static final String BYPASS = "extremedeaths.bypass";
        public static final String DEATHS = "extremedeaths.deaths";
        public static final String RESET = "extremedeaths.reset";
        public static final String RELOAD = "extremedeaths.reload";

    }
}
