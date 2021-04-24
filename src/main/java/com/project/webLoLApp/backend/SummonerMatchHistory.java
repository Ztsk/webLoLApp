package com.project.webLoLApp.backend;

import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import org.joda.time.DateTime;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.persistence.OrderColumn;
import java.io.Serializable;
import java.text.DecimalFormat;

@Embeddable
public class SummonerMatchHistory implements Serializable {

    @OrderColumn(name = "matchCount", nullable = false)
    private int matchCount = 5;
    @ElementCollection
    @OrderColumn(name = "winner")
    private Boolean[] isWinner = new Boolean[matchCount];
    @ElementCollection
    @OrderColumn(name = "daystomatch")
    private int[] daysAgo = new int[matchCount];
    @ElementCollection
    @OrderColumn(name = "duration")
    private String[] matchLength = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "champimgurl")
    private String[] championImgUrl = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "matchchampname")
    private String[] championName = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "summspell1")
    private String[] summSpellImg1Url = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "summspell2")
    private String[] summSpellImg2Url = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "primaryruneimg")
    private String[] primaryRuneImgUrl = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "secondaryruneimg")
    private String[] secondaryRuneImgUrl = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "kda")
    private String[] playerKDA = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "matchchamplevel")
    private int[] championLevel = new int[matchCount];
    @ElementCollection
    @OrderColumn(name = "creeps")
    private String[] creepScore = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "kp")
    private String[] killParticipation = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "visionsc")
    private int[] visionScore = new int[matchCount];
    @ElementCollection
    @OrderColumn(name = "countofitems")
    private int[] itemCount = new int[matchCount];
    @ElementCollection
    @Lob
    @OrderColumn(name = "itemsimg")
    private String[][] itemImgUrl = new String[matchCount][6];
    @ElementCollection
    @OrderColumn(name = "trinketurl")
    private String[] trinketImgUrl = new String[matchCount];
    @ElementCollection
    @OrderColumn(name = "redWards")
    private int[] controlWardsPurchased = new int[matchCount];
    @ElementCollection
    @OrderColumn(name = "redPlayers")
    private String[][] redTeamPlayers =  new String[matchCount][5];
    @ElementCollection
    @OrderColumn(name = "bluePlayers")
    private String[][] blueTeamPlayers =  new String[matchCount][5];
    @ElementCollection
    @Lob
    @OrderColumn(name = "redImgs")
    private String[][] redPlayersImgUrl = new String[matchCount][5];
    @ElementCollection
    @Lob
    @OrderColumn(name = "blueImgs")
    private String[][] bluePlayersImgUrl = new String[matchCount][5];
    @ElementCollection
    @Lob
    @OrderColumn(name = "spelltooltip")
    private String[][] summSpellToolTip = new String[matchCount][2];
    @ElementCollection
    @OrderColumn(name = "runetooltip")
    @Lob
    private String[][] ToolTipRune = new String[matchCount][3];
    @ElementCollection
    @OrderColumn(name = "Tooltip_items")
    @Lob
    private String[][] itemsTooltip = new String[matchCount][6];
    @ElementCollection
    @OrderColumn(name = "Tooltip_trinket")
    @Lob
    private String[] trinketTooltip = new String[matchCount];

    public SummonerMatchHistory() {

    }

    public SummonerMatchHistory(Summoner summoner){
        DecimalFormat df = new DecimalFormat("#.#");
        MatchHistory matchHistory = summoner.matchHistory().withQueues(Queue.RANKED_SOLO).get();
        for(int i = 0; i < matchCount; i++){
            this.isWinner[i] = matchHistory.get(i).getParticipants().find(summoner).getTeam().isWinner();
            this.daysAgo[i] = DateTime.now().getDayOfYear() - (matchHistory.get(i).getCreationTime().getDayOfYear());
            this.matchLength[i] = matchHistory.get(i).getDuration().getStandardMinutes() +
                    "m " + matchHistory.get(i).getDuration().getStandardSeconds()%60 + "s";
            this.championImgUrl[i] = matchHistory.get(i).getParticipants().find(summoner).getChampion().getImage().getURL();
            this.championName[i] = matchHistory.get(i).getParticipants().find(summoner).getChampion().getName();
            this.summSpellImg1Url[i] = matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellD().getImage().getURL();
            matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellD().getDescription();
            this.summSpellImg2Url[i] = matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellF().getImage().getURL();
            this.primaryRuneImgUrl[i] = "http://ddragon.leagueoflegends.com/cdn/img/" + matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(0).getRune().getImage();
            this.secondaryRuneImgUrl[i] = formSecondaryRuneUrl(matchHistory.get(i).getParticipants().find(summoner).getSecondaryRunePath().getId());
            this.playerKDA[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getKills() +
                    "/" + matchHistory.get(i).getParticipants().find(summoner).getStats().getDeaths() +
                    "/" + matchHistory.get(i).getParticipants().find(summoner).getStats().getAssists();
            this.championLevel[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getChampionLevel();
            this.creepScore[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getCreepScore() + " CS (" +
                    df.format((double) matchHistory.get(i).getParticipants().find(summoner).getStats().getCreepScore() / matchHistory.get(i).getDuration().getStandardMinutes()) + "/min)";
            this.killParticipation[i] = df.format(calculateKP(summoner, matchHistory, i)*100);
            this.visionScore[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getVisionScore();
            this.itemCount[i] = matchHistory.get(i).getParticipants().find(summoner).getItems().size()-1;
            this.trinketImgUrl[i] = matchHistory.get(i).getParticipants().find(summoner).getItems().get(itemCount[i]).getImage().getURL();
            this.summSpellToolTip[i][0] = matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellD().getName() + "||" +
                    matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellD().getDescription();
            this.summSpellToolTip[i][1] = matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellF().getName() + "||" +
                    matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellF().getDescription();
            this.ToolTipRune[i][0] = matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(0).getRune().getName() + "||"
                    + matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(0).getRune().getLongDescription().replaceAll("\\s*\\<[^\\>]*\\>\\s*", " ");
            this.ToolTipRune[i][1] = matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(4).getRune().getName() + "||"
                    + matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(4).getRune().getShortDescription().replaceAll("\\s*\\<[^\\>]*\\>\\s*", " ");
            this.ToolTipRune[i][2] = matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(5).getRune().getName() + "||"
                    + matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(5).getRune().getShortDescription().replaceAll("\\s*\\<[^\\>]*\\>\\s*", " ");
            for(int j = 0; j<itemCount[i];j++){
                this.itemImgUrl[i][j] = matchHistory.get(i).getParticipants().find(summoner).getItems().get(j).getImage().getURL();
                this.itemsTooltip[i][j] = matchHistory.get(i).getParticipants().find(summoner).getItems().get(j).getName() + "||" +
                    matchHistory.get(i).getParticipants().find(summoner).getItems().get(j).getDescription().replaceAll("\\s*\\<[^\\>]*\\>\\s*", " ") + "***Cost: " +
                        matchHistory.get(i).getParticipants().find(summoner).getItems().get(j).getTotalPrice() + " (" +
                        matchHistory.get(i).getParticipants().find(summoner).getItems().get(j).getBasePrice() + ")";
            }
            this.trinketTooltip[i] = matchHistory.get(i).getParticipants().find(summoner).getItems().get(itemCount[i]).getName() + "||" +
                    matchHistory.get(i).getParticipants().find(summoner).getItems().get(itemCount[i]).getDescription();
            this.controlWardsPurchased[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getPinkWardsPurchased();
            for(int j = 0; j < 5; j++){
                redTeamPlayers[i][j] = matchHistory.get(i).getRedTeam().getParticipants().get(j).getSummoner().getName();
                redPlayersImgUrl[i][j] = matchHistory.get(i).getRedTeam().getParticipants().get(j).getChampion().getImage().getURL();
                blueTeamPlayers[i][j] = matchHistory.get(i).getBlueTeam().getParticipants().get(j).getSummoner().getName();
                bluePlayersImgUrl[i][j] = matchHistory.get(i).getBlueTeam().getParticipants().get(j).getChampion().getImage().getURL();
            }

        }
    }
    private String formSecondaryRuneUrl(int id){
        switch(id) {
            case 8000:
                return "http://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7201_Precision.png";
            case 8100:
                return "http://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7200_Domination.png";
            case 8200:
                return "http://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7202_Sorcery.png";
            case 8300:
                return "http://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7203_Whimsy.png";
            case 8400:
                return "http://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7204_Resolve.png";
            default:
                return "http://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/RunesIcon.png";
        }
    }

    private double calculateKP(Summoner summoner, MatchHistory matchHistory, int i){
        int kills = 0;
        double kp;
        for(int j = 0; j < 5; j++) {
            kills += matchHistory.get(i).getParticipants().find(summoner).getTeam().getParticipants().get(j).getStats().getKills();
        }
        kp =  (matchHistory.get(i).getParticipants().find(summoner).getStats().getKills() + matchHistory.get(i).getParticipants().find(summoner).getStats().getAssists()) / (double) kills;
        return kp;
    }

    public void addMoreMatches(Summoner summoner, int number) {
        Boolean[] isWinnerTemp = new Boolean[matchCount];
        int[] daysAgoTemp = new int[matchCount];
        String[] matchLengthTemp = new String[matchCount];
        String[] championImgUrlTemp = new String[matchCount];
        String[] championNameTemp = new String[matchCount];
        String[] summSpellImg1UrlTemp = new String[matchCount];
        String[] summSpellImg2UrlTemp = new String[matchCount];
        String[] primaryRuneImgUrlTemp = new String[matchCount];
        String[] secondaryRuneImgUrlTemp = new String[matchCount];
        String[] playerKDATemp = new String[matchCount];
        int[] championLevelTemp = new int[matchCount];
        String[] creepScoreTemp = new String[matchCount];
        String[] killParticipationTemp = new String[matchCount];
        int[] visionScoreTemp = new int[matchCount];
        int[] itemCountTemp = new int[matchCount];
        String[][] itemImgUrlTemp = new String[matchCount][6];
        String[] trinketImgUrlTemp = new String[matchCount];
        int[] controlWardsPurchasedTemp = new int[matchCount];
        String[][] redTeamPlayersTemp =  new String[matchCount][5];
        String[][] blueTeamPlayersTemp =  new String[matchCount][5];
        String[][] redPlayersImgUrlTemp = new String[matchCount][5];
        String[][] bluePlayersImgUrlTemp = new String[matchCount][5];

        for(int i = 0; i < matchCount; i++){
            isWinnerTemp[i] = this.isWinner[i];
            daysAgoTemp[i] = this.daysAgo[i];
            matchLengthTemp[i] = this.matchLength[i];
            championImgUrlTemp[i] = this.championImgUrl[i];
            championNameTemp[i] = this.championName[i];
            summSpellImg1UrlTemp[i] = this.summSpellImg1Url[i];
            summSpellImg2UrlTemp[i] = this.summSpellImg2Url[i];
            primaryRuneImgUrlTemp[i] = this.primaryRuneImgUrl[i];
            secondaryRuneImgUrlTemp[i] = this.secondaryRuneImgUrl[i];
            playerKDATemp[i] = this.playerKDA[i];
            championLevelTemp[i] = this.championLevel[i];
            creepScoreTemp[i] = this.creepScore[i];
            killParticipationTemp[i] = this.killParticipation[i];
            visionScoreTemp[i] = this.visionScore[i];
            itemCountTemp[i] = this.itemCount[i];
            trinketImgUrlTemp[i] = this.trinketImgUrl[i];
            controlWardsPurchasedTemp[i] = this.controlWardsPurchased[i];
            for(int j = 0; j < 6; j++) {
                itemImgUrlTemp[i][j] = this.itemImgUrl[i][j];
            }
            for(int j = 0; j < 5; j++) {
                redTeamPlayersTemp[i][j] = this.redTeamPlayers[i][j];
                blueTeamPlayersTemp[i][j] = this.blueTeamPlayers[i][j];
                redPlayersImgUrlTemp[i][j] = this.redPlayersImgUrl[i][j];
                bluePlayersImgUrlTemp[i][j] = this.bluePlayersImgUrl[i][j];
            }
        }

        this.isWinner = new Boolean[matchCount +number];
        this.daysAgo = new int[matchCount +number];
        this.matchLength = new String[matchCount +number];
        this.championImgUrl = new String[matchCount +number];
        this.championName = new String[matchCount +number];
        this.summSpellImg1Url = new String[matchCount +number];
        this.summSpellImg2Url = new String[matchCount +number];
        this.primaryRuneImgUrl = new String[matchCount +number];
        this.secondaryRuneImgUrl = new String[matchCount +number];
        this.playerKDA = new String[matchCount +number];
        this.championLevel = new int[matchCount +number];
        this.creepScore = new String[matchCount +number];
        this.killParticipation = new String[matchCount +number];
        this.visionScore = new int[matchCount +number];
        this.itemCount = new int[matchCount +number];
        this.itemImgUrl = new String[matchCount +number][6];
        this.trinketImgUrl = new String[matchCount +number];
        this.controlWardsPurchased = new int[matchCount +number];
        this.redTeamPlayers =  new String[matchCount +number][5];
        this.blueTeamPlayers =  new String[matchCount +number][5];
        this.redPlayersImgUrl = new String[matchCount +number][5];
        this.bluePlayersImgUrl = new String[matchCount +number][5];

        for(int i = 0; i < matchCount; i++) {
            this.isWinner[i] = isWinnerTemp[i];
            this.daysAgo[i] = daysAgoTemp[i];
            this.matchLength[i] = matchLengthTemp[i];
            this.championImgUrl[i] = championImgUrlTemp[i];
            this.championName[i] = championNameTemp[i];
            this.summSpellImg1Url[i] = summSpellImg1UrlTemp[i];
            this.summSpellImg2Url[i] = summSpellImg2UrlTemp[i];
            this.primaryRuneImgUrl[i] = primaryRuneImgUrlTemp[i];
            this.secondaryRuneImgUrl[i] = secondaryRuneImgUrlTemp[i];
            this.playerKDA[i] = playerKDATemp[i];
            this.championLevel[i] = championLevelTemp[i];
            this.creepScore[i] = creepScoreTemp[i];
            this.killParticipation[i] = killParticipationTemp[i];
            this.visionScore[i] = visionScoreTemp[i];
            this.itemCount[i] = itemCountTemp[i];
            this.trinketImgUrl[i] = trinketImgUrlTemp[i];
            this.controlWardsPurchased[i] = controlWardsPurchasedTemp[i];

            for (int j = 0; j < 6; j++) {
                this.itemImgUrl[i][j] = itemImgUrlTemp[i][j];
            }
            for (int j = 0; j < 5; j++) {
                this.redTeamPlayers[i][j] = redTeamPlayersTemp[i][j];
                this.blueTeamPlayers[i][j] = blueTeamPlayersTemp[i][j];
                this.redPlayersImgUrl[i][j] = redPlayersImgUrlTemp[i][j];
                this.bluePlayersImgUrl[i][j] = bluePlayersImgUrlTemp[i][j];
            }
        }

        DecimalFormat df = new DecimalFormat("#.#");
        MatchHistory matchHistory = summoner.matchHistory().withQueues(Queue.RANKED_SOLO).get();

        for(int i = matchCount; i < matchCount +number; i++){
            this.isWinner[i] = matchHistory.get(i).getParticipants().find(summoner).getTeam().isWinner();
            this.daysAgo[i] = DateTime.now().getDayOfYear() - (matchHistory.get(i).getCreationTime().getDayOfYear());
            this.matchLength[i] = matchHistory.get(i).getDuration().getStandardMinutes() +
                    "m " + matchHistory.get(i).getDuration().getStandardSeconds()%60 + "s";
            this.championImgUrl[i] = matchHistory.get(i).getParticipants().find(summoner).getChampion().getImage().getURL();
            this.championName[i] = matchHistory.get(i).getParticipants().find(summoner).getChampion().getName();
            this.summSpellImg1Url[i] = matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellD().getImage().getURL();
            this.summSpellImg2Url[i] = matchHistory.get(i).getParticipants().find(summoner).getSummonerSpellF().getImage().getURL();
            this.primaryRuneImgUrl[i] = "http://ddragon.leagueoflegends.com/cdn/img/" + matchHistory.get(i).getParticipants().find(summoner).getRuneStats().get(0).getRune().getImage();
            this.secondaryRuneImgUrl[i] = formSecondaryRuneUrl(matchHistory.get(i).getParticipants().find(summoner).getSecondaryRunePath().getId());
            this.playerKDA[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getKills() +
                    "/" + matchHistory.get(i).getParticipants().find(summoner).getStats().getDeaths() +
                    "/" + matchHistory.get(i).getParticipants().find(summoner).getStats().getAssists();
            this.championLevel[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getChampionLevel();
            this.creepScore[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getCreepScore() + " CS (" +
                    df.format((double) matchHistory.get(i).getParticipants().find(summoner).getStats().getCreepScore() / matchHistory.get(i).getDuration().getStandardMinutes()) + "/min)";
            this.killParticipation[i] = df.format(calculateKP(summoner, matchHistory, i)*100);
            this.visionScore[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getVisionScore();
            this.itemCount[i] = matchHistory.get(i).getParticipants().find(summoner).getItems().size()-1;
            this.trinketImgUrl[i] = matchHistory.get(i).getParticipants().find(summoner).getItems().get(itemCount[i]).getImage().getURL();
            for(int j = 0; j<itemCount[i];j++){
                this.itemImgUrl[i][j] = matchHistory.get(i).getParticipants().find(summoner).getItems().get(j).getImage().getURL();
            }
            this.controlWardsPurchased[i] = matchHistory.get(i).getParticipants().find(summoner).getStats().getPinkWardsPurchased();
            for(int j = 0; j < 5; j++){
                redTeamPlayers[i][j] = matchHistory.get(i).getRedTeam().getParticipants().get(j).getSummoner().getName();
                redPlayersImgUrl[i][j] = matchHistory.get(i).getRedTeam().getParticipants().get(j).getChampion().getImage().getURL();
                blueTeamPlayers[i][j] = matchHistory.get(i).getBlueTeam().getParticipants().get(j).getSummoner().getName();
                bluePlayersImgUrl[i][j] = matchHistory.get(i).getBlueTeam().getParticipants().get(j).getChampion().getImage().getURL();
            }
        }
        this.matchCount += number;
    }


    public Boolean IsWinner(int i) {
        return isWinner[i];
    }

    public int getDaysAgo(int i) {
        return daysAgo[i];
    }

    public String getMatchLength(int i) {
        return matchLength[i];
    }

    public String getChampionImgUrl(int i) {
        return championImgUrl[i];
    }

    public String getChampionName(int i) {
        return championName[i];
    }

    public String getSummSpellImg1Url(int i) {
        return summSpellImg1Url[i];
    }

    public String getSummSpellImg2Url(int i) {
        return summSpellImg2Url[i];
    }

    public String getPrimaryRuneImgUrl(int i) {
        return primaryRuneImgUrl[i];
    }

    public String getSecondaryRuneImgUrl(int i) {
        return secondaryRuneImgUrl[i];
    }

    public String getPlayerKDA(int i) {
        return playerKDA[i];
    }

    public int getChampionLevel(int i) {
        return championLevel[i];
    }

    public String getCreepScore(int i) {
        return creepScore[i];
    }

    public String getKillParticipation(int i){
        return killParticipation[i];
    }

    public int getVisionScore(int i) { return visionScore[i]; }

    public int getItemCount(int i) {
        return itemCount[i];
    }

    public String getItemImgUrl(int i, int j) {
        return itemImgUrl[i][j];
    }

    public String getTrinketImgUrl(int i) {
        return trinketImgUrl[i];
    }

    public int getControlWardsPurchased(int i) { return controlWardsPurchased[i]; }

    public String getRedTeamPlayer(int i, int j) { return redTeamPlayers[i][j]; }

    public String getBlueTeamPlayer(int i, int j) { return blueTeamPlayers[i][j]; }

    public String getRedTeamPlayerImgUrl(int i, int j) { return redPlayersImgUrl[i][j]; }

    public String getBlueTeamPlayerImgUrl(int i, int j) { return bluePlayersImgUrl[i][j]; }

    public int getMatchCount(){
        return matchCount;
    }

    public String getSummSpellToolTip(int i, int j) {
        return summSpellToolTip[i][j];
    }

    public String getRuneToolTip(int i, int j) {
        return ToolTipRune[i][j];
    }

    public String getItemsTooltip(int i, int j) {
        return itemsTooltip[i][j];
    }

    public String getTrinketTooltip(int i) {
        return trinketTooltip[i];
    }
}
