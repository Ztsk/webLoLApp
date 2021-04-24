package com.project.webLoLApp.backend;

import com.merakianalytics.orianna.types.core.championmastery.ChampionMasteries;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.OrderColumn;
import java.io.Serializable;
import java.util.Arrays;

@Embeddable
public class SummonerMasteries implements Serializable {

    @ElementCollection
    @OrderColumn(name = "urlimg")
    private String[] imgUrls = new String[5];
    @ElementCollection
    @OrderColumn(name = "champpoints")
    private int[] points = new int[5];
    @ElementCollection
    @OrderColumn(name = "lastplayeddate")
    private String[] dates = new String[5];
    @ElementCollection
    @OrderColumn(name = "champname")
    private String[] championNames = new String[5];
    @ElementCollection
    @OrderColumn(name = "champlvl")
    private int[] championLevels = new int[5];

    public SummonerMasteries(){

    }

    public SummonerMasteries(Summoner summoner) {
        ChampionMasteries championMasteries = summoner.getChampionMasteries();
        for(int i = 0; i<5;i++){
            this.imgUrls[i] = championMasteries.get(i).getChampion().getImage().getURL();
            this.points[i] = championMasteries.get(i).getPoints();
            this.dates[i] = championMasteries.get(i).getLastPlayed().toDateTime().toString().replace("T"," ");
            this.championNames[i] = championMasteries.get(i).getChampion().getName();
            this.championLevels[i] = championMasteries.get(i).getLevel();
        }
    }


    public String getImgUrl(int i){
        return imgUrls[i];
    }


    public int getPoints(int i) {
        return points[i];
    }

    public String getDate(int i) {
        return dates[i];
    }

    public String getChampionName(int i) {
        return championNames[i];
    }

    public int getChampionLevel(int i) {
        return championLevels[i];
    }

    @Override
    public String toString() {
        return "SummonerMasteries{" +
                "imgUrls=" + Arrays.toString(imgUrls) +
                ", points=" + Arrays.toString(points) +
                ", dates=" + Arrays.toString(dates) +
                ", championNames=" + Arrays.toString(championNames) +
                ", championLevels=" + Arrays.toString(championLevels) +
                '}';
    }


}
