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
            if(entry.getValue().getPermission() == null) return true;
            else if(commandSender.hasPermission(entry.getValue().getPermission())) return true;
            return false;
        }).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())).keySet());
    }
    public static String tlc(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
