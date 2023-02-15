package org.sevencraft.extremedeaths.subcommands;


import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sevencraft.extremedeaths.Extremedeaths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.sevencraft.extremedeaths.Utils.tlc;

public class Lifes implements SubCommand {

    Extremedeaths plugin;

    public Lifes(Extremedeaths pl) {
        this.plugin = pl;
    }

    @Override
    public String getPermission() {
        return "extremedeaths.lifes";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(tlc("&6/edeaths lifes [add|take|set|reset] <player> <amount>"));
            return;
        }
        Player p = sender.getServer().getPlayer(args[1]);
        UUID uuid = p == null ? null : p.getUniqueId();
        Integer amount = Integer.valueOf(args[2]);
        switch (args[0]) {
            case "add":
                if (uuid == null) plugin.getDB().insertPlayerLifes(args[1], amount);
                else plugin.getDB().insertPlayerLifes(uuid, p.getName(), amount);
                sender.sendMessage(tlc(String.format("&aAdded %s lifes to %s.", args[2], args[1])));
                return;
            case "take":
                if (uuid == null) plugin.getDB().insertPlayerLifes(args[1], -(amount));
                else plugin.getDB().insertPlayerLifes(uuid, p.getName(), -(amount));
                sender.sendMessage(tlc(String.format("&aTaken %s lifes from %s.", args[2], args[1])));
                return;
            case "reset":
                if (uuid == null) plugin.getDB().setPlayerLifes(args[1], 0);
                else plugin.getDB().setPlayerLifes(uuid, p.getName(), 0);
                sender.sendMessage(tlc(String.format("&aReset lifes for %s", args[2], args[1])));
                return;
            default:
                sender.sendMessage(tlc("&cInvalid action."));
                return;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        switch (args.length) {
            case 0:
            case 1:
                return Arrays.asList("add", "take", "reset");
            case 2:
                return commandSender.getServer().getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
            case 3:
                return Arrays.asList("1", "2", "3", "4", "5");
        }
        return new ArrayList<>();

    }
}
