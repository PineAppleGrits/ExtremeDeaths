package org.sevencraft.extremedeaths;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.sevencraft.extremedeaths.database.DatabaseAPI;
import org.sevencraft.extremedeaths.database.DatabaseTypes;
import sun.security.krb5.Config;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Extremedeaths extends JavaPlugin {

    static Extremedeaths instance;
    DatabaseAPI databaseManager;

    @Override
    public void onEnable() {
        ConfigManager.getInstance().setPlugin(this);
        ConfigManager.getInstance().getConfig("config.yml");
        ConfigManager.getInstance().reloadConfigs();
        databaseManager = new DatabaseAPI(this);
        databaseManager.initDB();
        getServer().getPluginManager().registerEvents(new EventsHandler(this), this);
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
