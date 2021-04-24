package com.project.webLoLApp.ui;

import com.merakianalytics.orianna.types.common.Platform;
import com.project.webLoLApp.backend.SummonerData;
import com.project.webLoLApp.ui.view.detail.SearchView;
import com.project.webLoLApp.ui.view.list.ListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;


@CssImport("./shared-styles.css")
public class MainLayout extends AppLayout {

    private SummonerData summonerData;
    private TextField summonerNameField = new TextField();
    private ComboBox<Platform> platformComboBox = new ComboBox<>();

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        H1 title = new H1("Summoner Info by ZSK");
        title.addClassName("title");
        Button searchViewButton = new Button("Search");
        Button listViewButton = new Button("Saved list");
        searchViewButton.addClassName("searchviewbutton");
        listViewButton.addClassName("listviewbutton");
        searchViewButton.addClickListener(click -> UI.getCurrent().navigate(SearchView.class));
        listViewButton.addClickListener(click -> UI.getCurrent().navigate(ListView.class));

        HorizontalLayout header = new HorizontalLayout(title, searchViewButton, listViewButton);

        header.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }
}
