package com.project.webLoLApp.ui.view.detail;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.project.webLoLApp.backend.SummonerData;
import com.project.webLoLApp.backend.repositories.SummonerDataService;
import com.project.webLoLApp.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.project.webLoLApp.WebLoLAppApplication.ApiKey;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Summoner Search")
@CssImport(value = "./vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport(value = "./vaadin-select.css", themeFor = "vaadin-select")
@CssImport(value = "./vaadin-select-overlay.css", themeFor = "vaadin-select-overlay")
@CssImport(value = "./vaadin-item-styles.css", themeFor = "vaadin-select-item")
public class SearchView extends VerticalLayout{

    private SummonerData summonerData;
    private TextField summonerNameField = new TextField();
    private Select<String> platformSelect = new Select<>();
    private SearchResults searchResults;
    private SummonerDataService summonerDataService;


    public SearchView(SummonerDataService summonerDataService) {
        this.summonerDataService = summonerDataService;
        Orianna.setRiotAPIKey(ApiKey);
        Orianna.setDefaultPlatform(Platform.EUROPE_WEST);
        addClassName("search-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getSearchMenu());
        setSizeFull();
        setSpacing(false);
    }

    private VerticalLayout getSearchMenu() {
        platformSelect.setItems("EUW", "NA", "EUNE", "KOR", "BRA", "JAP", "OCE", "RUS", "TUR", "LAN", "LAS");
        platformSelect.setPlaceholder("Region");
        platformSelect.setErrorMessage("Choose a region!");
        platformSelect.getElement().getStyle().set("background-color", "#4b4b4b");
        platformSelect.setWidth("22%");

        summonerNameField.setPlaceholder("Summoner Name...");
        summonerNameField.setClearButtonVisible(true);
        IronIcon searchIcon = new IronIcon("vaadin", "search");
        searchIcon.setColor("white");
        summonerNameField.setPrefixComponent(searchIcon);
        summonerNameField.setErrorMessage("Type a proper name!");
        summonerNameField.setWidth("55%");

        Button searchButton = new Button("Search");
        searchButton.addClassName("search-button");
        searchButton.addClickListener(click -> searchSummoner());

        Label searchTitle = new Label("Ranked Solo/Duo Stats");
        searchTitle.getElement().getStyle().set("background-color", "#344274");
        searchTitle.getElement().getStyle().set("color", "white");
        searchTitle.getElement().getStyle().set("padding","0");

        HorizontalLayout searchInput;
        VerticalLayout searchMenu = new VerticalLayout(
                searchTitle,
                searchInput = new HorizontalLayout(
                summonerNameField, platformSelect, searchButton
                )
        );
        searchInput.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        searchMenu.addClassName("search-menu");
        searchMenu.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        searchMenu.setSizeUndefined();
        searchMenu.setSpacing(false);
        return searchMenu;
    }

    private void searchSummoner(){
        if(platformSelect.isEmpty()) {
            platformSelect.setInvalid(true); }
        if(summonerNameField.isEmpty()) {
            summonerNameField.setInvalid(true); }
        if(!summonerNameField.isEmpty() && !platformSelect.isEmpty()) {
            summonerData = new SummonerData(Orianna.summonerNamed(summonerNameField.getValue()).withPlatform(getPlatformFromComboBox()).get());

           if(summonerData!=null){
                   summonerDataService.save(summonerData);
                   UI.getCurrent().navigate("results/" + summonerData.getName());
           }
           else {
               summonerNameField.setInvalid(true);
               platformSelect.setInvalid(true);
           }
        }
    }

    private Platform getPlatformFromComboBox(){
        switch (platformSelect.getValue()) {
            default: return Platform.EUROPE_WEST;
            case "EUNE": return Platform.EUROPE_NORTH_EAST;
            case "NA": return Platform.NORTH_AMERICA;
            case "KOR": return Platform.KOREA;
            case "BRA": return Platform.BRAZIL;
            case "JAP": return Platform.JAPAN;
            case "OCE": return Platform.OCEANIA;
            case "RUS": return Platform.RUSSIA;
            case "TUR": return Platform.TURKEY;
            case "LAN": return Platform.LATIN_AMERICA_NORTH;
            case "LAS": return Platform.LATIN_AMERICA_SOUTH;
        }
    }


}
