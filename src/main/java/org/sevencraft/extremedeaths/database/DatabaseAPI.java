package org.sevencraft.extremedeaths.database;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.sevencraft.extremedeaths.Extremedeaths;
import org.sevencraft.extremedeaths.models.DBType;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DatabaseAPI {
    Extremedeaths plugin;

    DBType databaseType;

    public DatabaseAPI(Extremedeaths plugin) {
        this.plugin = plugin;
        ConfigurationSection dbConfig = plugin.getConfig().getConfigurationSection("database");
        String use = dbConfig.getString("use");
        if (use == "mysql") databaseType = new MySQL();
        else if (use == "sqlite") databaseType = new SQLite();

    }

    public Integer getTotalDeaths() {
        CompletableFuture<Long> deadPlayers = DB.getFirstColumnAsync(databaseType.getTotalDeaths());
        try {
            return deadPlayers.get() == null ? null : deadPlayers.get().intValue();
        } catch (InterruptedException | ExecutionException e) {
            plugin.getLogger().severe("Error getting total forever deaths.");
            e.printStackTrace();
        }
        return 0;
    }

    ;


    public Integer getTotalDeathsForever() {
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
        CompletableFuture<Long> deadPlayers = DB.getFirstColumnAsync(databaseType.getTotalDeathsForever(), maxDeaths);
        try {
            return deadPlayers.get() == null ? null : deadPlayers.get().intValue();
        } catch (InterruptedException | ExecutionException e) {
            plugin.getLogger().severe("Error getting total forever deaths.");
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getForeverDeadPlayers() {
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
        CompletableFuture<List<String>> deadPlayers = DB.getFirstColumnAsync(databaseType.getForeverDeadPlayers(), maxDeaths);
        try {
            return deadPlayers.get();
        } catch (InterruptedException | ExecutionException e) {
            plugin.getLogger().severe("Error getting forever dead players.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<UUID> getForeverDeadPlayersUUIDs() {
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
        CompletableFuture<List<String>> deadPlayers = DB.getFirstColumnAsync(databaseType.getForeverDeadPlayersUUIDs(), maxDeaths);
        try {
            return deadPlayers.get().stream().map(p -> UUID.fromString(p)).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            plugin.getLogger().severe("Error getting forever dead players.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Integer getRemainingLifes(UUID uuid) {
        Integer extra = getExtraLifes(uuid);
        Integer count = getPlayerDeathCount(uuid);
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
        return (maxDeaths + extra) - count;
    }

    public Integer getRemainingLifes(String username) {
        Integer extra = getExtraLifes(username);
        Integer count = getPlayerDeathCount(username);
        Integer maxDeaths = plugin.getConfig().getInt("max-deaths");
        return (maxDeaths + extra) - count;
    }

    // PlayerDeaths methods
    //  Delete methods
    public void deletePlayerDeaths(List<UUID> uuids) {
        if (uuids.size() < 1) return;
        String statement = String.format(databaseType.deletePlayerDeathsUUIDs(), uuids.stream().map(uid -> "?").collect(Collectors.joining(",")));
        DB.executeUpdateAsync(statement, uuids.stream().map(uid -> uid.toString()).toArray());
    }

    public void deletePlayerDeaths(UUID uuid) {
        DB.executeUpdateAsync(databaseType.deletePlayerDeathsUUID(), uuid.toString());
    }

    public void deletePlayerDeaths(String username) {
        DB.executeUpdateAsync(databaseType.deletePlayerDeathsUsername(), username);
    }

    public void deletePlayerDeaths() {
        DB.executeUpdateAsync(databaseType.deletePlayerDeaths());
    }

    // Get methods
    public Integer getPlayerDeathCount(String username) {
        try {
            Long deathCount = DB.getFirstColumn(databaseType.getPlayerDeathCountUsername(), username);
            return deathCount == null ? 0 : deathCount.intValue();
        } catch (SQLException e) {
            plugin.getLogger().severe(String.format("Error getting player deaths [%s] from DB", username));
            e.printStackTrace();
        }
        return 0;
    }

    public Integer getPlayerDeathCount(UUID uuid) {
        try {
            Long deathCount = DB.getFirstColumn(databaseType.getPlayerDeathCountUUID(), uuid.toString());
            return deathCount == null ? 0 : deathCount.intValue();
        } catch (SQLException e) {
            plugin.getLogger().severe(String.format("Error getting player deaths [%s] from DB", uuid.toString()));
            e.printStackTrace();
        }
        return 0;
    }

    // Insert methods
    public void insertPlayerDeath(UUID uuid, String username, String message) {
        try {
            DB.executeInsert(databaseType.insertPlayerDeath(), uuid.toString(), username, message);
        } catch (SQLException e) {
            plugin.getLogger().severe(String.format("Error inserting player death [%s] to DB", uuid.toString()));
            e.printStackTrace();
        }
    }

    // Player methods
    public void insertPlayerLifes(UUID uuid, String username, Integer lifes) {
        try {
            DB.executeInsert(databaseType.insertPlayerLifes(), uuid.toString(), username, lifes);
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    public void insertPlayerLifes(String username, Integer lifes) {

        DB.executeUpdateAsync(databaseType.insertPlayerLifesUsername(), lifes, username);
    }

    public void setPlayerLifes(UUID uuid, String username, Integer lifes) {
        DB.executeUpdateAsync(databaseType.setPlayerLifes(), uuid.toString(), username, lifes, lifes);
    }

    public void setPlayerLifes(String username, Integer lifes) {
        DB.executeUpdateAsync(databaseType.setPlayerLifesUsername(), lifes, username);
    }

    public Integer getExtraLifes(UUID uuid) {
        try {
            Integer extraLifes = DB.getFirstColumn(databaseType.getExtraLifes(), uuid.toString());
            return extraLifes == null ? 0 : extraLifes;
        } catch (SQLException e) {
            plugin.getLogger().severe(String.format("Error getting player extra lifes [%s] from DB", uuid.toString()));
            e.printStackTrace();
        }
        return 0;
    }

    public Integer getExtraLifes(String username) {
        try {
            Integer extraLifes = DB.getFirstColumn(databaseType.getExtraLifesUsername(), username);
            return extraLifes == null ? 0 : extraLifes;
        } catch (SQLException e) {
            plugin.getLogger().severe(String.format("Error getting player extra lifes [%s] from DB", username));
            e.printStackTrace();
        }
        return 0;
    }


    public void initDB() {
        ConfigurationSection dbConfig = plugin.getConfig().getConfigurationSection("database");
        String DBType =  dbConfig.getString("use", "sqlite");
        if(DBType.equalsIgnoreCase("mysql")) initMySQL();
        else if (DBType.equalsIgnoreCase("sqlite")) initSQLite();
    }

    public void initSQLite() {
        DatabaseOptions options = DatabaseOptions.builder().sqlite(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "db.sqlite")
                .useOptimizations(true)
                .logger(plugin.getLogger())
                .build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS player_death(uuid VARCHAR(255),username VARCHAR(255),death_message TEXT);");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS player(uuid VARCHAR(255),username VARCHAR(255),extra_lifes INT DEFAULT 0, PRIMARY KEY(uuid));");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating tables;");
            plugin.onDisable();
            throw new RuntimeException(e);
        }
    }

    public void initMySQL() {
        ConfigurationSection dbConfig = plugin.getConfig().getConfigurationSection("database");
        String user = dbConfig.getString("user");
        String password = dbConfig.getString("password");
        Integer port = dbConfig.getInt("port");
        String host = dbConfig.getString("host");
        String database = dbConfig.getString("database");
        DatabaseOptions options = DatabaseOptions.builder()
                .mysql(user, password, database, host + ":" + port)
                .useOptimizations(true)
                .logger(plugin.getLogger())
                .build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS player_death(uuid VARCHAR(255),username VARCHAR(255),death_message TEXT);");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS player(uuid VARCHAR(255),username VARCHAR(255),extra_lifes INT DEFAULT 0, PRIMARY KEY(uuid));");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating tables;");
            plugin.onDisable();
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        DB.close();
        initDB();
    }
}
