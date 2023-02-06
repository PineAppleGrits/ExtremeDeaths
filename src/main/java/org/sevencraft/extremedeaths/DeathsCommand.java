package org.sevencraft.extremedeaths;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.sevencraft.extremedeaths.Utils.tlc;

public class DeathsCommand implements CommandExecutor, TabExecutor {

    Extremedeaths plugin;

    public DeathsCommand(Extremedeaths plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && sender.hasPermission("extremedeaths.deaths") && sender.getName() != args[0]) {
            // If players online try to find by uuid if not find by username.
            Integer deathCount;
            Player player = sender.getServer().getPlayer(args[0]);
            if (player == null) deathCount = plugin.getDB().getPlayerDeathCount(args[0]);
            else deathCount = plugin.getDB().getPlayerDeathCount(player.getUniqueId());
            sender.sendMessage(tlc(String.format("&aThe death count of &6%s&a is &c%d&a.", player == null ? args[0] : player.getName(), deathCount)));
            return true;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(tlc("&cOnly players can execute this command."));
                return true;
            }
            Player player = ((Player) sender);
            Integer deathCount = plugin.getDB().getPlayerDeathCount(player.getUniqueId());
            sender.sendMessage(tlc(String.format("&aYour death count is %d.", deathCount)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("extremedeaths.deaths"))
            return sender.getServer().getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
