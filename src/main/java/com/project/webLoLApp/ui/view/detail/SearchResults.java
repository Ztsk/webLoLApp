package com.project.webLoLApp.ui.view.detail;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.project.webLoLApp.backend.SummonerData;
import com.project.webLoLApp.backend.repositories.SummonerDataService;
import com.project.webLoLApp.ui.MainLayout;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route(value = "results", layout = MainLayout.class)
@PageTitle("Results")
public class SearchResults extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

        private SummonerData summonerData;
        private Summoner summoner;
        private int matchCount;
        private VerticalLayout matchHistoryLayout = new VerticalLayout();
        private SummonerDataService summonerDataService;
        private HorizontalLayout bottomSection;
        private String param;

        @Override
        public void setParameter(BeforeEvent beforeEvent, String s) {
            param = s;
        }

        public SearchResults(SummonerDataService summonerDataService){
            this.summonerDataService = summonerDataService;
            addClassName("search-results");
        }

        @Override
        public void afterNavigation(AfterNavigationEvent event) {
            try {
                this.summonerData = this.summonerDataService.findAll(param).get(0);
            }catch (IndexOutOfBoundsException e){
                    UI.getCurrent().navigate(SearchView.class);
           }
            this.matchCount = summonerData.getSummonerMatchHistory().getMatchCount();
            add(getStats(), bottomSection = new HorizontalLayout(getChampionMasteries(), getMatchHistory()));
            setSizeUndefined();
            setSpacing(false);
            bottomSection.setSizeFull();
            bottomSection.setSpacing(false);
            getElement().getStyle().set("background-image", "url(champions/" + summonerData.getSummonerMasteries().getChampionName(0).replace(" ", "") + "_0.jpg)");
        }

        /*Method returning layout with all the basic info for summoner in ranked solo queue*/
        private HorizontalLayout getStats(){

            Image summonerIcon = new Image("http://ddragon.leagueoflegends.com/cdn/11.1.1/img/profileicon/"
                    + summonerData.getIconID() + ".png", summonerData.getName());
            summonerIcon.addClassName("results-icon");
            Label name = new Label(summonerData.getName());
            Label level = new Label("Lvl: " + summonerData.getLevel());
            Label lastUpdated = new Label("Last Updated: " + summonerData.getLastUpdated());
            Label wins = new Label("Won games: " + summonerData.getWins());
            Label loses = new Label("Lost games: " + summonerData.getLoses());
            Label rank = new Label("Ranked position: " + summonerData.getRank() + " " + summonerData.getTier());
            Label lp = new Label("League points: " + summonerData.getLeaguePoints());
            Label winratio = new Label("Win Ratio: " + summonerData.getWinratio() + "%");

            Image rankedImage = new Image("ranked-emblems/Emblem_" + summonerData.getRank() + ".png", "ranked_emblem");
            rankedImage.addClassName("ranked-image");

            Button updateButton = new Button("Update");
            updateButton.addClickListener(click -> {
               summonerData.updateSummonerData();
               summonerDataService.save(summonerData);
                UI.getCurrent().getPage().reload();
            }
            );
            updateButton.addClassName("update-button");

            VerticalLayout basicData = new VerticalLayout(name, level, lastUpdated, updateButton);
            VerticalLayout gameStats = new VerticalLayout(wins, loses, winratio);
            gameStats.setWidth("30%");
            gameStats.addClassName("game-stats");
            VerticalLayout leagueData = new VerticalLayout(rank, lp);
            leagueData.addClassName("league-data");
            leagueData.setDefaultHorizontalComponentAlignment(Alignment.END);

            HorizontalLayout summonerStats = new HorizontalLayout(
                    summonerIcon, basicData,  gameStats, leagueData, rankedImage);
            summonerStats.setSizeFull();
            summonerStats.addClassName("summoner-stats");
            return summonerStats;
        }

        /*Method returning layout with first 5 champions with best masteries level and points*/
        private VerticalLayout getChampionMasteries(){
            Label championMasteriesTitle = new Label("   Best 5 Champion Masteries:");
            championMasteriesTitle.addClassName("championMasteriesTitle");
            VerticalLayout championMasteries = new VerticalLayout(championMasteriesTitle);

            for(int i = 0; i < 5; i++){
                Image champImage = new Image(summonerData.getSummonerMasteries().getImgUrl(i), "Champ");
                champImage.addClassName("champ-image");
                Label champLastPlayed = new Label("Last Played: " + summonerData.getSummonerMasteries().getDate(i).substring(0,10));
                Label champPoints = new Label("Mastery Points: " + summonerData.getSummonerMasteries().getPoints(i));
                Label champBasic = new Label(summonerData.getSummonerMasteries().getChampionName(i) +
                        " - Mastery Level: " + summonerData.getSummonerMasteries().getChampionLevel(i));
                VerticalLayout champInfo = new VerticalLayout(champBasic, champPoints, champLastPlayed);
                champInfo.setPadding(false);
                champInfo.setSpacing(false);
                HorizontalLayout champContent = new HorizontalLayout(champImage, champInfo);
                champContent.addClassName("champ-content");
                champContent.setSizeFull();
                championMasteries.add(champContent);
            }

            championMasteries.addClassName("champion-masteries");
            championMasteries.setSizeFull();
            championMasteries.setWidth("25%");
            championMasteries.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            return championMasteries;
        }

        /*Method that returns layout with summoners match history and its details*/
        private VerticalLayout getMatchHistory(){
            Label matchHistoryTitle = new Label("Ranked Solo/Duo Match History: ");
            matchHistoryLayout.addClassName("matchHistory-layout");
            matchHistoryLayout.add(matchHistoryTitle);
            Button showMoreMatchesButton = new Button("Show more");
            showMoreMatchesButton.addClassName("show-more-button");
            displayMatches(0, matchCount);
            showMoreMatchesButton.addClickListener(click -> {
                    matchHistoryLayout.remove(showMoreMatchesButton);
                    displayMatches(matchCount, 5);
                    matchHistoryLayout.add(showMoreMatchesButton);
            }
            );
            matchHistoryLayout.add(showMoreMatchesButton);
            matchHistoryLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            matchHistoryLayout.setPadding(false);
            matchHistoryLayout.setSizeFull();

            return matchHistoryLayout;
        }

        private void displayMatches(int Count, int number){
            if(Count == matchCount && Count >= 5) {
                summoner = Orianna.summonerNamed(summonerData.getName()).withPlatform(summonerData.getPlatform()).get();
                summonerData.getSummonerMatchHistory().addMoreMatches(summoner, number);
                this.matchCount += number;
            }
            int itemRowCount;
            Image summSpellImg1, summSpellImg2, championImg, primaryRuneImg, secondaryRuneImg, itemImg, itemHolder, playerChampImg;
            HorizontalLayout singleMatchLayout, championWithoutNameLayout, itemRow1, itemRow2, itemsLayout, playerLayout;
            VerticalLayout timeAndResultLayout, championImgNameLayout, summonerSpellsLayout, runesLayout, inGameInfo, kdaLayout, itemRows, controlAndItems, redTeamLayout, blueTeamLayout;
            Tooltip summSpellTooltip1, summSpellTooltip2, rune1ToolTip, rune2ToolTip, itemToolTip;

            for(int i = Count; i<Count+number; i++) {
                singleMatchLayout = new HorizontalLayout();
                summSpellTooltip1 = new Tooltip();
                summSpellTooltip2 = new Tooltip();
                rune1ToolTip = new Tooltip();
                rune2ToolTip = new Tooltip();

                if (summonerData.getSummonerMatchHistory().IsWinner(i)) {
                    singleMatchLayout.add(
                            timeAndResultLayout = new VerticalLayout(
                                    new Label(summonerData.getSummonerMatchHistory().getDaysAgo(i) + " days ago"),
                                    new Label("Victory"),
                                    new Label(summonerData.getSummonerMatchHistory().getMatchLength(i))
                            )
                    );
                    singleMatchLayout.addClassName("single-match-layoutV");
                } else {
                    singleMatchLayout.add(
                            timeAndResultLayout = new VerticalLayout(
                                    new Label(summonerData.getSummonerMatchHistory().getDaysAgo(i) + " days ago"),
                                    new Label("Defeat"),
                                    new Label(summonerData.getSummonerMatchHistory().getMatchLength(i))
                            )
                    );
                    singleMatchLayout.addClassName("single-match-layoutD");
                }
                timeAndResultLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

                singleMatchLayout.addAndExpand(
                        championImgNameLayout = new VerticalLayout(
                                championWithoutNameLayout = new HorizontalLayout(
                                        championImg = new Image(summonerData.getSummonerMatchHistory().getChampionImgUrl(i), "Champion"),
                                        summonerSpellsLayout = new VerticalLayout(
                                                summSpellImg1 = new Image(summonerData.getSummonerMatchHistory().getSummSpellImg1Url(i), "DSpell"),
                                                summSpellImg2 = new Image(summonerData.getSummonerMatchHistory().getSummSpellImg2Url(i), "FSpell")
                                        ),
                                        runesLayout = new VerticalLayout(
                                                primaryRuneImg = new Image(summonerData.getSummonerMatchHistory().getPrimaryRuneImgUrl(i), "Rune1"),
                                                secondaryRuneImg = new Image(summonerData.getSummonerMatchHistory().getSecondaryRuneImgUrl(i), "Rune2")
                                        )
                                ),
                                new Label(summonerData.getSummonerMatchHistory().getChampionName(i))
                        )
                );
                championImgNameLayout.addClassName("match-champion-image-name-layout");

                summSpellTooltip1.attachToComponent(summSpellImg1);
                summSpellTooltip1.setPosition(TooltipPosition.TOP);
                summSpellTooltip1.setAlignment(TooltipAlignment.CENTER);
                summSpellTooltip1.addClassName("summSpellTooltip1");
                summSpellTooltip1.add(
                        new H5(summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,0).substring(0,summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,0).indexOf("||"))),
                        new Paragraph(summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,0).substring(summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,0).indexOf("||")+2))
                );

                summSpellTooltip2.attachToComponent(summSpellImg2);
                summSpellTooltip2.setPosition(TooltipPosition.BOTTOM);
                summSpellTooltip2.setAlignment(TooltipAlignment.CENTER);
                summSpellTooltip2.addClassName("summSpellTooltip2");
                summSpellTooltip2.add(
                        new H5(summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,1).substring(0,summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,1).indexOf("||"))),
                        new Paragraph(summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,1).substring(summonerData.getSummonerMatchHistory().getSummSpellToolTip(i,1).indexOf("||")+2))
                );

                rune1ToolTip.attachToComponent(primaryRuneImg);
                rune1ToolTip.setPosition(TooltipPosition.TOP);
                rune1ToolTip.setAlignment(TooltipAlignment.CENTER);
                rune1ToolTip.addClassName("rune1ToolTip");
                rune1ToolTip.add(
                        new H5(summonerData.getSummonerMatchHistory().getRuneToolTip(i,0).substring(0, summonerData.getSummonerMatchHistory().getRuneToolTip(i,0).indexOf("||"))),
                        new Paragraph(summonerData.getSummonerMatchHistory().getRuneToolTip(i,0).substring(summonerData.getSummonerMatchHistory().getRuneToolTip(i,0).indexOf("||")+2))
                );

                rune2ToolTip.attachToComponent(secondaryRuneImg);
                rune2ToolTip.setPosition(TooltipPosition.BOTTOM);
                rune2ToolTip.setAlignment(TooltipAlignment.CENTER);
                rune2ToolTip.addClassName("rune2ToolTip");
                rune2ToolTip.add(
                    new H5(summonerData.getSummonerMatchHistory().getRuneToolTip(i,1).substring(0,summonerData.getSummonerMatchHistory().getRuneToolTip(i,1).indexOf("||"))),
                    new Paragraph(summonerData.getSummonerMatchHistory().getRuneToolTip(i,1).substring(summonerData.getSummonerMatchHistory().getRuneToolTip(i,1).indexOf("||")+2)),
                    new H5(summonerData.getSummonerMatchHistory().getRuneToolTip(i,2).substring(0,summonerData.getSummonerMatchHistory().getRuneToolTip(i,2).indexOf("||"))),
                    new Paragraph(summonerData.getSummonerMatchHistory().getRuneToolTip(i,2).substring(summonerData.getSummonerMatchHistory().getRuneToolTip(i,2).indexOf("||")+2))
                );
                singleMatchLayout.add(summSpellTooltip1, summSpellTooltip2, rune1ToolTip, rune2ToolTip);

                singleMatchLayout.addAndExpand(
                        kdaLayout = new VerticalLayout(
                                new Label("K/D/A"),
                                new Label(summonerData.getSummonerMatchHistory().getPlayerKDA(i))
                        ),
                        inGameInfo = new VerticalLayout(
                                new Label("Level " + summonerData.getSummonerMatchHistory().getChampionLevel(i)),
                                new Label(summonerData.getSummonerMatchHistory().getCreepScore(i)),
                                new Label("KP: " + summonerData.getSummonerMatchHistory().getKillParticipation(i) + "%"),
                                new Label("Vision Score: " + summonerData.getSummonerMatchHistory().getVisionScore(i))
                        )
                );
                kdaLayout.setSpacing(false);
                kdaLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
                inGameInfo.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
                itemRow1 = new HorizontalLayout();
                itemRow2 = new HorizontalLayout();
                itemRow1.addClassName("itemRow1");
                itemRow2.addClassName("itemRow2");

                if (summonerData.getSummonerMatchHistory().getItemCount(i) < 4) {
                    for (int j = 0; j < summonerData.getSummonerMatchHistory().getItemCount(i); j++) {
                        itemImg = new Image(summonerData.getSummonerMatchHistory().getItemImgUrl(i, j), "Item");

                        itemToolTip = new Tooltip();
                        itemToolTip.addClassName("item-tooltip");
                        itemToolTip.setPosition(TooltipPosition.TOP);
                        itemToolTip.setAlignment(TooltipAlignment.CENTER);
                        itemToolTip.attachToComponent(itemImg);
                        itemToolTip.add(
                                new H5(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(0,summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("||"))),
                                new Paragraph(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(
                                        summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("||")+2,summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("***"))),
                                new H6(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("***")+3))
                        );

                        itemRow1.add(itemImg, itemToolTip);
                        itemImg.addClassName("item-img");
                    }
                } else if (summonerData.getSummonerMatchHistory().getItemCount(i) > 3) {
                    for (int j = 0; j < 3; j++) {
                        itemImg = new Image(summonerData.getSummonerMatchHistory().getItemImgUrl(i, j), "Item");

                        itemToolTip = new Tooltip();
                        itemToolTip.addClassName("item-tooltip");
                        itemToolTip.setAlignment(TooltipAlignment.CENTER);
                        itemToolTip.setPosition(TooltipPosition.TOP);
                        itemToolTip.attachToComponent(itemImg);
                        itemToolTip.add(
                                new H5(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(0,summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("||"))),
                                new Paragraph(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(
                                        summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("||")+2,summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("***"))),
                                new H6(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("***")+3))
                        );

                        itemRow1.add(itemImg, itemToolTip);
                        itemImg.addClassName("item-img");
                    }
                    for (int j = 3; j < summonerData.getSummonerMatchHistory().getItemCount(i); j++) {
                        itemImg = new Image(summonerData.getSummonerMatchHistory().getItemImgUrl(i, j), "Item");

                        itemToolTip = new Tooltip();
                        itemToolTip.addClassName("item-tooltip");
                        itemToolTip.setAlignment(TooltipAlignment.CENTER);
                        itemToolTip.setPosition(TooltipPosition.BOTTOM);
                        itemToolTip.attachToComponent(itemImg);
                        itemToolTip.add(
                                new H5(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(0,summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("||"))),
                                new Paragraph(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(
                                        summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("||")+2,summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("***"))),
                                new H6(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).substring(summonerData.getSummonerMatchHistory().getItemsTooltip(i,j).indexOf("***")+3))
                        );

                        itemRow2.add(itemImg, itemToolTip);
                        itemImg.addClassName("item-img");
                    }
                }
                itemRowCount = itemRow1.getComponentCount();
                if (itemRowCount/2 < 3) {
                    for (int j = 0; j < (3 - itemRowCount/2); j++) {
                        itemRow1.add(itemHolder = new Image("item-holder.jpg", "ItemHolder"));
                        itemHolder.addClassName("item-holder");
                    }
                }
                itemRowCount = itemRow2.getComponentCount();
                if (itemRowCount/2 < 3) {
                    for (int j = 0; j < (3 - itemRowCount/2); j++) {
                        itemRow2.add(itemHolder = new Image("item-holder.jpg", "ItemHolder"));
                        itemHolder.addClassName("item-holder");
                    }
                }

                itemToolTip = new Tooltip();
                itemToolTip.addClassName("item-tooltip");
                itemToolTip.setAlignment(TooltipAlignment.CENTER);
                itemToolTip.setPosition(TooltipPosition.TOP);

                singleMatchLayout.addAndExpand(
                        controlAndItems = new VerticalLayout(
                                itemsLayout = new HorizontalLayout(
                                        itemRows = new VerticalLayout(itemRow1, itemRow2),
                                        new VerticalLayout(itemImg = new Image(summonerData.getSummonerMatchHistory().getTrinketImgUrl(i), "trinket"))
                                ),
                                new Label("Control wards: " + summonerData.getSummonerMatchHistory().getControlWardsPurchased(i))
                        )
                );
                itemToolTip.attachToComponent(itemImg);
                itemToolTip.add(
                        new H5(summonerData.getSummonerMatchHistory().getTrinketTooltip(i).substring(0,summonerData.getSummonerMatchHistory().getTrinketTooltip(i).indexOf("||"))),
                        new Paragraph(summonerData.getSummonerMatchHistory().getTrinketTooltip(i).substring(summonerData.getSummonerMatchHistory().getTrinketTooltip(i).indexOf("||")+2).replaceAll("\\s*\\<[^\\>]*\\>\\s*", " "))
                );
                singleMatchLayout.add(itemToolTip);

                itemRows.addClassName("item-rows");
                blueTeamLayout = new VerticalLayout();
                redTeamLayout = new VerticalLayout();

                for (int j = 0; j < 5; j++) {
                    blueTeamLayout.addAndExpand(
                            playerLayout = new HorizontalLayout(
                                    playerChampImg = new Image(summonerData.getSummonerMatchHistory().getBlueTeamPlayerImgUrl(i, j), ""),
                                    new Label(summonerData.getSummonerMatchHistory().getBlueTeamPlayer(i, j))
                            )
                    );
                    playerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    playerChampImg.addClassName("player-champ-img");

                    redTeamLayout.addAndExpand(
                            playerLayout = new HorizontalLayout(
                                    playerChampImg = new Image(summonerData.getSummonerMatchHistory().getRedTeamPlayerImgUrl(i, j), ""),
                                    new Label(summonerData.getSummonerMatchHistory().getRedTeamPlayer(i, j))
                            )
                    );
                    playerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    playerChampImg.addClassName("player-champ-img");
                }

                singleMatchLayout.addAndExpand(blueTeamLayout, redTeamLayout);

                controlAndItems.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
                itemImg.addClassName("item-img");
                itemsLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                itemsLayout.addClassName("items-layout");
                itemsLayout.setSpacing(false);
                summSpellImg1.addClassName("summ-spell-img1");
                summSpellImg2.addClassName("summ-spell-img1");
                championImg.addClassName("champion-img");
                primaryRuneImg.addClassName("primary-rune-img");
                secondaryRuneImg.addClassName("secondary-rune-img");

                timeAndResultLayout.setWidth("12%");
                championImgNameLayout.setWidth("15%");
                kdaLayout.setWidth("8%");
                inGameInfo.setWidth("12%");
                controlAndItems.setWidth("23%");
                blueTeamLayout.setWidth("15%");
                redTeamLayout.setWidth("15%");

                championWithoutNameLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                singleMatchLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                championImgNameLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
                singleMatchLayout.setSpacing(false);
                singleMatchLayout.setSizeFull();
                championImgNameLayout.setSpacing(false);
                summonerSpellsLayout.setSpacing(false);
                summonerSpellsLayout.setPadding(false);
                runesLayout.setSpacing(false);
                runesLayout.setPadding(false);

                matchHistoryLayout.add(singleMatchLayout);
            }

        }


}
