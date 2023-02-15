package org.sevencraft.extremedeaths;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.sevencraft.extremedeaths.subcommands.Lifes;
import org.sevencraft.extremedeaths.subcommands.Reload;
import org.sevencraft.extremedeaths.subcommands.Reset;
import org.sevencraft.extremedeaths.subcommands.SubCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.sevencraft.extremedeaths.Utils.getStrings;
import static org.sevencraft.extremedeaths.Utils.tlc;

public class EDeathsCommand implements CommandExecutor, TabExecutor {

    private HashMap<String, SubCommand> subcommands = new HashMap<>();
    Extremedeaths plugin;

    public EDeathsCommand (Extremedeaths plugin){
        this.plugin = plugin;
        subcommands.put("reset", new Reset(plugin));
        subcommands.put("reload", new Reload(plugin));
        subcommands.put("lifes", new Lifes(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand sc = subcommands.get(args[0]);
            if (sc == null) sender.sendMessage(tlc("&cYou must choose a subcommand."));
            else if (sc.getPermission() != null && !sender.hasPermission(sc.getPermission())) {
                sender.sendMessage(tlc("&cYou dont have permissions."));
            } else {
                sc.execute(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        } else {
            sender.sendMessage();
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand sc = subcommands.get(args[0]);
            if (sc != null) return sc.onTabComplete(sender, args);
        }
        return getStrings(sender, subcommands);
    }
}
