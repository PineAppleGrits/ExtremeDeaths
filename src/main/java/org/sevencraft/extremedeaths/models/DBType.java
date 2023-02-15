package org.sevencraft.extremedeaths.models;

public interface DBType {
    String getTotalDeaths();

    String getTotalDeathsForever();

    String getForeverDeadPlayers();

    String getForeverDeadPlayersUUIDs();

    String deletePlayerDeathsUUIDs();

    String deletePlayerDeathsUUID();

    String deletePlayerDeathsUsername();

    String deletePlayerDeaths();

    String getPlayerDeathCountUsername();

    String getPlayerDeathCountUUID();

    String insertPlayerDeath();

    String insertPlayerLifes();

    String insertPlayerLifesUsername();

    String setPlayerLifes();

    String setPlayerLifesUsername();

    String getExtraLifes();

    String getExtraLifesUsername();
}