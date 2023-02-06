package org.sevencraft.extremedeaths.subcommands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    String getPermission();

    void execute(CommandSender sender, String[] args);

    List<String> onTabComplete(CommandSender commandSender, String[] args);

}