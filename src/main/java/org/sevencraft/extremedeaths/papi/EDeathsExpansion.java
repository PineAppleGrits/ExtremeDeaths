package org.sevencraft.extremedeaths.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.sevencraft.extremedeaths.Extremedeaths;

public class EDeathsExpansion extends PlaceholderExpansion {

    private final Extremedeaths plugin;

    public EDeathsExpansion(Extremedeaths plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "PineAppleGrits";
    }

    @Override
    public String getIdentifier() {
        return "edeaths";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("deaths")){
            Integer dc = plugin.getDB().getPlayerDeathCount(player.getUniqueId());
            return String.valueOf(dc == null ? 0 : dc);
        } else if (params.equalsIgnoreCase("total")){
            Integer dc = plugin.getDB().getTotalDeaths();
            return String.valueOf(dc == null ? 0 : dc);
        }  else if (params.equalsIgnoreCase("total_forever")){
            Integer dc = plugin.getDB().getTotalDeathsForever();
            return String.valueOf(dc == null ? 0 : dc);
        } else if (params.equalsIgnoreCase("lifes")){
            Integer dc = plugin.getDB().getRemainingLifes(player.getUniqueId());
            return String.valueOf(dc == null ? 0 : dc);
        }
        return null; // Placeholder is unknown by the Expansion
    }
}