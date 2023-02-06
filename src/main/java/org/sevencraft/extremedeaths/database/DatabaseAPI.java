package org.sevencraft.extremedeaths.database;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import org.sevencraft.extremedeaths.Extremedeaths;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DatabaseAPI {
    Extremedeaths plugin;

    public DatabaseAPI(Extremedeaths plugin){
        this.plugin = plugin;
    }

    public void resetPlayerDeaths(List<UUID> uuids) {
        if (uuids.size() < 1) return;
        String statement = String.format("DELETE FROM player_death WHERE uuid IN (%s);", uuids.stream().map(uid -> "?").collect(Collectors.joining(",")));
        try {
            DB.executeUpdate(statement, uuids.stream().map(uid -> uid.toString()).toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void resetPlayerDeaths(UUID uuid) {
        try {
            DB.executeUpdate("DELETE FROM player_death WHERE uuid = ?", uuid.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetPlayerDeaths(String username) {
        try {
            DB.executeUpdate("DELETE FROM player_death WHERE username = ?", username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetPlayersDeaths() {
        try {
            DB.executeUpdate("DELETE FROM player_death");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getPlayerDeathCount(String username) {
        try {
            Integer deathCount = DB.getFirstColumn("SELECT COUNT(uuid) FROM player_death where username = ?", username);
            return deathCount == null ? 0 : deathCount;
        } catch (SQLException e) {
           plugin.getLogger().severe("Error getting player from DB");
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        return 0;
    }
    public Integer getPlayerDeathCount(UUID uuid) {
        try {
            Integer deathCount = DB.getFirstColumn("SELECT COUNT(uuid) FROM player_death where uuid = ?", uuid.toString());
            return deathCount == null ? 0 : deathCount;
        } catch (SQLException e) {
            plugin.getLogger().severe("Error getting player from DB");
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        return 0;
    }

    public void insertPlayerDeath(UUID uuid, String username, String message) {
        try {
            DB.executeInsert("INSERT INTO player_death VALUES (?,?,?)", uuid.toString(), username, message);
        } catch (SQLException e) {
            plugin.getLogger().severe("Error inserting death to DB");
            e.printStackTrace();
        }
    }
    public void initDB() {
        DatabaseOptions options = DatabaseOptions.builder().sqlite(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "db.sqlite")
                .useOptimizations(true)
                .logger(plugin.getLogger())
                .build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS player_death(uuid VARCHAR(255),username VARCHAR(255),death_message TEXT);");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating tables;");
            plugin.onDisable();
            throw new RuntimeException(e);
        }
    }
    public void reload(){
        DB.close();
        initDB();
    }
}
