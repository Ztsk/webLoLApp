package com.project.webLoLApp.ui.view.detail;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.project.webLoLApp.backend.SummonerData;
import com.project.webLoLApp.ui.MainLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Summoner Search")
public class SearchView extends VerticalLayout{

    private String title = "Summoner Search";
    private SummonerData summonerData;
    private TextField summonerNameField = new TextField();
    private ComboBox<Platform> platformComboBox = new ComboBox<>();
    private SearchResults searchResults;
    private Label searchTitle;

    public SearchView() {

        Orianna.setRiotAPIKey("RGAPI-78bd96cb-ddc9-4cf8-8e46-e166ea123ff1");
        Orianna.setDefaultPlatform(Platform.EUROPE_WEST);
        addClassName("search-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        searchTitle = new Label("Ranked Solo/Duo Stats");


        add(searchTitle, getSearchMenu());
        summonerData = ComponentUtil.getData(UI.getCurrent(),SummonerData.class);
        if(summonerData != null)updateResults();
    }

    private HorizontalLayout getSearchMenu() {
        platformComboBox.setItems(new ArrayList<Platform>(Arrays.asList(Platform.values())));
        platformComboBox.setPlaceholder("Region...");
        platformComboBox.setErrorMessage("Choose a region!");

        summonerNameField.setPlaceholder("Summoner Name..");
        summonerNameField.setClearButtonVisible(true);
        summonerNameField.setPrefixComponent(new IronIcon("vaadin", "search"));
        summonerNameField.setErrorMessage("Type a proper name!");

        Button searchButton = new Button("Search");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.addClickListener(click -> searchSummoner());

        HorizontalLayout searchMenu = new HorizontalLayout(summonerNameField, platformComboBox, searchButton);
        searchMenu.addClassName("search-menu");

        return searchMenu;
    }

    private void searchSummoner(){
        if(platformComboBox.isEmpty()) {
            platformComboBox.setInvalid(true); }
        if(summonerNameField.isEmpty()) {
            summonerNameField.setInvalid(true); }
        if(!summonerNameField.isEmpty() && !platformComboBox.isEmpty()) {
            summonerData = new SummonerData(Orianna.summonerNamed(summonerNameField.getValue()).withPlatform(platformComboBox.getValue()).get());
            ComponentUtil.setData(UI.getCurrent(), SummonerData.class, summonerData);
            updateResults();
        }
    }

    private void updateResults(){
        if(searchResults != null) remove(searchResults);
        summonerNameField.setValue("");
        searchResults = new SearchResults(summonerData);
        searchResults.setSizeFull();
        add(searchResults);
    }

}
