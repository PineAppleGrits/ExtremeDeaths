package org.sevencraft.extremedeaths.subcommands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface SubCommand {

    String getPermission();

    void execute(CommandSender sender, String[] args);

    List<String> onTabComplete(CommandSender commandSender, String[] args);

}