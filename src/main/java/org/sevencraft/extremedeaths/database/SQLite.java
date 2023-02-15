package org.sevencraft.extremedeaths.database;

import org.sevencraft.extremedeaths.models.DBType;

public class SQLite implements DBType {
    public final String getTotalDeaths = "SELECT COUNT(*) from player_death;";
    public final String getTotalDeathsForever = "SELECT COUNT(*) from player p WHERE (SELECT COUNT(pd.uuid) FROM player_death pd WHERE pd.uuid = p.uuid) > p.extra_lifes + ?;";
    public final String getForeverDeadPlayers = "SELECT p.username from player p WHERE SELECT COUNT(pd.uuid) FROM player_death pd WHERE pd.uuid = p.uuid) > p.extra_lifes + ? GROUP BY p.uuid;";
    public final String getForeverDeadPlayersUUIDs = "SELECT p.uuid from player p WHERE SELECT COUNT(pd.uuid) FROM player_death pd WHERE pd.uuid = p.uuid) > p.extra_lifes + ? GROUP BY p.uuid;";
    public final String deletePlayerDeathsUUIDs = "DELETE FROM player_death WHERE uuid IN (%s);";
    public final String deletePlayerDeathsUUID = "DELETE FROM player_death WHERE uuid = ?";
    public final String deletePlayerDeathsUsername = "DELETE FROM player_death WHERE username = ?";
    public final String deletePlayerDeaths = "DELETE FROM player_death";
    public final String getPlayerDeathCountUsername = "SELECT COUNT(uuid) FROM player_death where username = ?";
    public final String getPlayerDeathCountUUID = "SELECT COUNT(uuid) FROM player_death where uuid = ?";
    public final String insertPlayerDeath = "INSERT INTO player_death VALUES (?,?,?)";
    public final String insertPlayerLifes = "INSERT INTO player(uuid,username,extra_lifes) VALUES(?,?,?) ON CONFLICT (uuid) DO UPDATE SET username = excluded.username, extra_lifes = extra_lifes + excluded.extra_lifes;";
    public final String insertPlayerLifesUsername = "UPDATE player SET extra_lifes = extra_lifes + ? WHERE username = ? ;";
    public final String setPlayerLifes = "INSERT INTO player(uuid,username,extra_lifes ) VALUES(?,?,?) ON CONFLICT (uuid) DO UPDATE username = excluded.username, extra_lifes = ?;";
    public final String setPlayerLifesUsername = "UPDATE player SET extra_lifes = ? WHERE username = ? ;";
    public final String getExtraLifes = "SELECT extra_lifes FROM player where uuid = ?";
    public final String getExtraLifesUsername = "SELECT extra_lifes FROM player where username = ?";


    public String getTotalDeaths() {
        return this.getTotalDeaths;
    }

    ;

    public String getTotalDeathsForever() {
        return this.getTotalDeathsForever;
    }

    ;

    public String getForeverDeadPlayers() {
        return this.getForeverDeadPlayers;
    }

    ;

    public String getForeverDeadPlayersUUIDs() {
        return this.getForeverDeadPlayersUUIDs;
    }

    ;

    public String deletePlayerDeathsUUIDs() {
        return this.deletePlayerDeathsUUIDs;
    }

    ;

    public String deletePlayerDeathsUUID() {
        return this.deletePlayerDeathsUUID;
    }

    ;

    public String deletePlayerDeathsUsername() {
        return this.deletePlayerDeathsUsername;
    }

    ;

    public String deletePlayerDeaths() {
        return this.deletePlayerDeaths;
    }

    ;

    public String getPlayerDeathCountUsername() {
        return this.getPlayerDeathCountUsername;
    }

    ;

    public String getPlayerDeathCountUUID() {
        return this.getPlayerDeathCountUUID;
    }

    ;

    public String insertPlayerDeath() {
        return this.insertPlayerDeath;
    }

    ;

    public String insertPlayerLifes() {
        return this.insertPlayerLifes;
    }

    ;

    public String insertPlayerLifesUsername() {
        return this.insertPlayerLifesUsername;
    }

    ;

    public String setPlayerLifes() {
        return this.setPlayerLifes;
    }

    ;

    public String setPlayerLifesUsername() {
        return this.setPlayerLifesUsername;
    }

    ;

    public String getExtraLifes() {
        return this.getExtraLifes;
    }

    ;

    public String getExtraLifesUsername() {
        return this.getExtraLifesUsername;
    }

    ;
}