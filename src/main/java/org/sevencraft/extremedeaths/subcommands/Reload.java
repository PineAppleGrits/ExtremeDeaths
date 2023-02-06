package org.sevencraft.extremedeaths.subcommands;

import org.bukkit.command.CommandSender;
import org.sevencraft.extremedeaths.Extremedeaths;
import org.sevencraft.extremedeaths.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.sevencraft.extremedeaths.Utils.tlc;

public class Reload implements SubCommand {
    Extremedeaths plugin;

    public Reload(Extremedeaths pl) {
        this.plugin = pl;
    }

    @Override
    public String getPermission() {
        return Utils.Permissions.RELOAD;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        plugin.getDB().reload();
        sender.sendMessage(tlc("&aReloaded configuration and database."));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }
}
