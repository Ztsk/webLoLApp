package com.project.webLoLApp.ui.view.detail;

import com.project.webLoLApp.backend.SummonerData;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class SearchResults extends VerticalLayout {

        private SummonerData summonerData;
        private Image summonerIcon, rankedImage;
        private Label name, level, wins, loses, rank, lp, winratio;

        public SearchResults(SummonerData summonerData){
            addClassName("search-results");
            this.summonerData = summonerData;
            add(getStats());
        }

        private HorizontalLayout getStats(){
            summonerIcon = new Image(
                    "http://ddragon.leagueoflegends.com/cdn/10.21.1/img/profileicon/"
                            + summonerData.getIconID() + ".png",
                    summonerData.getName());
            summonerIcon.addClassName("results-icon");
            name = new Label(summonerData.getName());
            level = new Label("Level: " + summonerData.getLevel());
            wins = new Label("Won games: " + summonerData.getWins());
            loses = new Label("Lost games: " + summonerData.getLoses());
            rank = new Label("Ranked position: " + summonerData.getRank() + " " + summonerData.getTier());
            lp = new Label("League points: " + summonerData.getLeaguePoints());
            winratio = new Label("Win Ratio: " + summonerData.getWinratio() + "%");

            rankedImage = new Image("ranked-emblems/Emblem_" + summonerData.getRank() + ".png", "ranked_emblem");
            rankedImage.addClassName("ranked-image");

            name.setSizeFull();
            level.setSizeFull();
            wins.setSizeFull();
            loses.setSizeFull();
            rank.setSizeFull();
            lp.setSizeFull();
            winratio.setSizeFull();

            HorizontalLayout summonerStats = new HorizontalLayout(
                    summonerIcon, name, level, wins, loses, winratio, rank, lp, rankedImage);
            summonerStats.setSizeFull();

            return summonerStats;
        }

        private HorizontalLayout getChampionMasteries(){

            HorizontalLayout championMasteries = new HorizontalLayout();
            return championMasteries;
        }
}
