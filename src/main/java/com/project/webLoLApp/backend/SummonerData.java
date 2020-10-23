package com.project.webLoLApp.backend;

import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class SummonerData {

    @Id
    private String name;
    private int level;
    private Platform platform;
    private String puuid;
    private String accountId;
    private String id;
    private String updated;
    private String rank;
    private String tier;
    private int wins;
    private int loses;
    private double winratio;
    private int leaguePoints;
    private int games;
    private int iconID;

    public SummonerData() {
    }

    public SummonerData(Summoner summoner) {
        iconID = summoner.getProfileIcon().getId();
        LeagueEntry le = summoner.getLeaguePosition(Queue.RANKED_SOLO);
        this.name = summoner.getName();
        this.level = summoner.getLevel();
        this.platform = summoner.getPlatform();
        this.puuid = summoner.getPuuid();
        this.accountId = summoner.getAccountId();
        this.id = summoner.getId();
        this.updated = summoner.getUpdated().toString();
        this.wins = le.getWins();
        this.loses = le.getLosses();
        this.games = this.wins + this.loses;
        this.winratio = Math.round(((this.wins * 1.0 /(this.loses + this.wins))*100.0)*100.0)/100.0;
        this.rank = le.getTier().toString();
        this.tier = le.getDivision().toString();
        this.leaguePoints = le.getLeaguePoints();
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Platform getPlatform() { return platform; }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public double getWinratio() {
        return winratio;
    }

    public void setWinratio(double winratio) {
        this.winratio = winratio;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(int leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummonerData that = (SummonerData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SummonerData{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", platform=" + platform +
                ", puuid='" + puuid + '\'' +
                ", accountId='" + accountId + '\'' +
                ", id='" + id + '\'' +
                ", updated='" + updated + '\'' +
                ", rank='" + rank + '\'' +
                ", tier='" + tier + '\'' +
                ", wins=" + wins +
                ", loses=" + loses +
                ", winratio=" + winratio +
                ", leaguePoints=" + leaguePoints +
                ", games=" + games +
                ", iconID=" + iconID +
                '}';
    }
}
