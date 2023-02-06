package org.sevencraft.extremedeaths;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

import static org.sevencraft.extremedeaths.Utils.tlc;

public class EventsHandler implements Listener {
    Extremedeaths plugin;

    public EventsHandler(Extremedeaths plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("extremedeaths.bypass")) return;
        ConfigurationSection overLimit = plugin.getConfig().getConfigurationSection("over-limit");
        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        boolean elegiblePlayer = elegiblePlayer(player.getUniqueId());
        if (elegiblePlayer) {
            Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
            player.sendMessage(tlc(String.format(messages.getString("on-death"), maxDeaths - plugin.getDB().getPlayerDeathCount(player.getUniqueId()))));
            return;
        }
        boolean allowJoin = overLimit.getBoolean("allow-join");
        if (!allowJoin) player.kickPlayer(tlc(messages.getString("last-death")));
        if (overLimit.getBoolean("spectator-mode")) player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(tlc(messages.getString("last-death")));
    }

    @EventHandler
    public void onEntityDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        if (player.hasPermission("extremedeaths.bypass")) return;
        plugin.getDB().insertPlayerDeath(player.getUniqueId(), player.getName(), event.getDeathMessage());
        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
        boolean elegiblePlayer = elegiblePlayer(player.getUniqueId());
        if (elegiblePlayer) return;
        ConfigurationSection overLimit = plugin.getConfig().getConfigurationSection("over-limit");
        boolean kickOnDeath = overLimit.getBoolean("kick-on-death");
        if (kickOnDeath)
            player.kickPlayer(tlc(messages.getString("last-death")));
        if (overLimit.getBoolean("spectator-mode")) player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(tlc(messages.getString("last-death")));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        boolean elegiblePlayer = elegiblePlayer(event.getUniqueId());
        if (elegiblePlayer) return;
        ConfigurationSection overLimit = plugin.getConfig().getConfigurationSection("over-limit");
        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        boolean allowJoin = overLimit.getBoolean("allow-join");
        if (allowJoin) return;
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, tlc(messages.getString("last-death")));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        // Second layer in case first one failed.
        boolean elegiblePlayer = elegiblePlayer(event.getPlayer().getUniqueId());
        if (elegiblePlayer) return;
        ConfigurationSection overLimit = plugin.getConfig().getConfigurationSection("over-limit");
        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        boolean allowJoin = overLimit.getBoolean("allow-join");
        if (allowJoin) {
            if (overLimit.getBoolean("spectator-mode")) event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().sendMessage(tlc(messages.getString("last-death")));
            return;
        }
        event.getPlayer().kickPlayer(tlc(messages.getString("last-death")));
    }


    boolean elegiblePlayer(UUID uuid) {
        if (uuid == null) return false;
        Integer deathCount = plugin.getDB().getPlayerDeathCount(uuid);
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
        if (offlinePlayer == null) return true;
        if (offlinePlayer.getPlayer() != null)
            if (offlinePlayer.getPlayer().hasPermission("extremedeaths.bypass")) return true;
        if (deathCount > maxDeaths) return false;
        return true;
    }

}
