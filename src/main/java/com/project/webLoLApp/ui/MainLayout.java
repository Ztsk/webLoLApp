package com.project.webLoLApp.ui;

import com.project.webLoLApp.ui.view.detail.SearchView;
import com.project.webLoLApp.ui.view.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./shared-styles.css")
public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 title = new H1("Summoner Info by ZSK");
        title.addClassName("title");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), title);

        header.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Saved Summoners", ListView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink searchLink = new RouterLink("Summoner search", SearchView.class);
        searchLink.setHighlightCondition(HighlightConditions.sameLocation());
        setDrawerOpened(false);

        addToDrawer(new VerticalLayout(searchLink, listLink));
    }
}
