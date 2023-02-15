package org.sevencraft.extremedeaths;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

import static org.sevencraft.extremedeaths.Utils.tlc;

public class ListenerClass implements Listener {
    Extremedeaths plugin;

    public ListenerClass(Extremedeaths plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(Utils.Permissions.BYPASS)) return;
        ConfigurationSection overLimit = plugin.getConfig().getConfigurationSection("over-limit");
        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        boolean elegiblePlayer = elegiblePlayer(player.getUniqueId());
        if (elegiblePlayer) {
            Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
            player.sendMessage(tlc(String.format(messages.getString("on-death"), plugin.getDB().getRemainingLifes(player.getUniqueId()))));
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
        if (player.hasPermission(Utils.Permissions.BYPASS)) return;
        ConfigurationSection overLimit = plugin.getConfig().getConfigurationSection("over-limit");
        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        plugin.getDB().insertPlayerDeath(player.getUniqueId(), player.getName(), event.getDeathMessage());
        boolean elegiblePlayer = elegiblePlayer(player.getUniqueId());
        if (elegiblePlayer) {
            plugin.getServer().broadcastMessage(tlc(
                    messages.getString("broadcast")
                            .replaceAll("\\{player}", player.getName()))
                    .replaceAll("\\{cause}", event.getDeathMessage())
                    .replaceAll("\\{total}", String.valueOf(plugin.getDB().getTotalDeathsForever()))
            );
            return;
        }
        plugin.getServer().broadcastMessage(tlc(
                messages.getString("broadcast-forever")
                        .replaceAll("\\{player}", player.getName()))
                .replaceAll("\\{cause}", event.getDeathMessage())
                .replaceAll("\\{total}", String.valueOf(plugin.getDB().getTotalDeathsForever()))
        );
        boolean kickOnDeath = overLimit.getBoolean("kick-on-death");
        if (kickOnDeath)
            player.kickPlayer(tlc(messages.getString("last-death")));
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        boolean elegiblePlayer = elegiblePlayer(event.getUniqueId());
        if (elegiblePlayer) return;
        ConfigurationSection overLimit = plugin.getConfig().getConfigurationSection("over-limit");
        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        boolean allowJoin = overLimit.getBoolean("allow-join");
        if (allowJoin) return;
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, tlc(messages.getString("last-death")));
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        boolean elegiblePlayer = elegiblePlayer(event.getPlayer().getUniqueId());
        plugin.getDB().insertPlayerLifes(event.getPlayer().getUniqueId(), event.getPlayer().getName(), 0);
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
        /*Integer deathCount = plugin.getDB().getPlayerDeathCount(uuid);
        Integer extraLifes = plugin.getDB().getExtraLifes(uuid);
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");*/
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
        if (offlinePlayer == null) return true;
        if (offlinePlayer.getPlayer() != null)
            if (offlinePlayer.getPlayer().hasPermission(Utils.Permissions.BYPASS)) return true;
        //if (deathCount  >= (maxDeaths+extraLifes)) return false;
        return plugin.getDB().getRemainingLifes(uuid) > 0;
    }

}
