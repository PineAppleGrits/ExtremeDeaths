package org.sevencraft.extremedeaths;

import co.aikar.idb.DB;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.sevencraft.extremedeaths.database.DatabaseAPI;

public final class Extremedeaths extends JavaPlugin {

    static Extremedeaths instance;
    DatabaseAPI databaseManager;

    @Override
    public void onEnable() {
        ConfigManager.getInstance().setPlugin(this);
        ConfigManager.getInstance().getConfig("config.yml");
        ConfigManager.getInstance().reloadConfigs();
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EDeathsExpansion(this).register();
        }
        databaseManager = new DatabaseAPI(this);
        databaseManager.initDB();
        this.deathsCache = new DeathsCache();
        getServer().getPluginManager().registerEvents(new ListenerClass(this), this);
        this.getCommand("edeaths").setExecutor(new EDeathsCommand(this));
        this.getCommand("deaths").setExecutor(new DeathsCommand(this));

    }

    @Override
    public void onDisable() {
        DB.close();
    }

    public DatabaseAPI getDB() {
        return databaseManager;
    }

    public static FileConfiguration getConfiguration() {
        return ConfigManager.getInstance().getConfig("config.yml");
    }
}
