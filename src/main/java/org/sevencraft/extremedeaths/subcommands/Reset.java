package org.sevencraft.extremedeaths.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sevencraft.extremedeaths.Extremedeaths;

import java.util.*;
import java.util.stream.Collectors;

import static org.sevencraft.extremedeaths.Utils.tlc;

public class Reset implements SubCommand {
    Extremedeaths plugin;

    public Reset(Extremedeaths pl) {
        this.plugin = pl;
    }

    @Override
    public String getPermission() {
        return "extremedeaths.reset";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String query = args.length < 1 ? "**" : args[0];
        if (query.equals("**")) {
            plugin.getDB().resetPlayersDeaths();
            sender.sendMessage(tlc("&aThe death count of all players has been reseted."));
        } else if (query.equals("*")) {
            List<UUID> uuids =  sender.getServer().getOnlinePlayers().stream().map(p -> p.getUniqueId()).collect(Collectors.toList());
            plugin.getDB().resetPlayerDeaths(uuids);
            sender.sendMessage(tlc("&aThe death count of all online players has been reseted."));
        } else {
            Player p = sender.getServer().getPlayer(query);
            if(p == null){
                // Offlineplayer find by username
                plugin.getDB().resetPlayerDeaths(query);
            } else {
                plugin.getDB().resetPlayerDeaths(p.getUniqueId());
            }
            sender.sendMessage(tlc(String.format("&aThe death count of &6%s&a has been reseted.", p == null ? query : p.getName())));
            return ;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        tabComplete.add("*");
        tabComplete.add("**");
        tabComplete.addAll(commandSender.getServer().getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList()));
        return tabComplete;
    }
}
