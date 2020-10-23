package com.project.webLoLApp.ui.view.list;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.project.webLoLApp.backend.SummonerData;
import com.project.webLoLApp.backend.repositories.SummonerDataService;
import com.project.webLoLApp.ui.MainLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;

@Route(value="saved", layout = MainLayout.class)
@PageTitle("Summoner Data Set")
public class ListView extends VerticalLayout {

        private SummonerDataService summonerDataService;
        private Grid<SummonerData> grid = new Grid<>(SummonerData.class);
        private TextField filterText = new TextField();
        private SummonerForm form;

        public ListView(SummonerDataService summonerDataService) {
                this.summonerDataService = summonerDataService;
                addClassName("list-view");
                setSizeFull();

                summonerDataService.save(ComponentUtil.getData(UI.getCurrent(),SummonerData.class));

                configureGrid();

                form = new SummonerForm(new ArrayList<Platform>(Arrays.asList(Platform.values())));
                form.addListener(SummonerForm.SaveEvent.class, this::saveSummoner);
                form.addListener(SummonerForm.CloseEvent.class, e -> closeEditor());
                closeEditor();

                Div content = new Div(grid, form);
                content.addClassName("content");
                content.setSizeFull();

                add(getToolbar(), content);
                updateList();
        }

        private HorizontalLayout getToolbar(){
                filterText.setPlaceholder("Filter by name...");
                filterText.setClearButtonVisible(true);
                filterText.setValueChangeMode(ValueChangeMode.LAZY);
                filterText.addValueChangeListener(e -> updateList());

                Button addSummonerButton = new Button("Add Summoner");
                addSummonerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                addSummonerButton.addClickListener(click -> addSummoner());
                addSummonerButton.addClickShortcut(Key.ENTER);

                Button refreshButton = new Button("Refresh");
                refreshButton.addClickListener(click -> refreshSummoners());

                Button deleteButton = new Button("Delete");
                deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                deleteButton.addClickShortcut(Key.DELETE);
                deleteButton.addClickListener(click -> deleteSummoner(grid.asSingleSelect().getValue()));

                Button detailsButton = new Button("Details of selected");
                detailsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                detailsButton.addClickListener(click -> {
                        ComponentUtil.setData(UI.getCurrent(), SummonerData.class, grid.asSingleSelect().getValue());
                        new Anchor("");
                });
                HorizontalLayout toolbar = new HorizontalLayout(
                        addSummonerButton, refreshButton, deleteButton, filterText, detailsButton);
                toolbar.addClassName("toolbar");
                return toolbar;
        }

        private void refreshSummoners(){
                for(SummonerData s : summonerDataService.findAll()){
                        s = new SummonerData(Orianna.summonerNamed(s.getName()).withPlatform(s.getPlatform()).get());
                }
                updateList();
        }

        private void addSummoner(){
                grid.asSingleSelect().clear();
                editSummoner(new SummonerData());
        }

        private void saveSummoner(SummonerForm.SaveEvent event) {
                summonerDataService.save(event.getSummonerData());
                updateList();
                closeEditor();
        }

        private void deleteSummoner(SummonerData summonerData) {
                summonerDataService.delete(summonerData);
                updateList();
                closeEditor();
        }

        private void configureGrid(){
                grid.addClassName("summoner-grid");
                grid.setSizeFull();
                grid.setColumns("name", "platform", "level", "rank",
                        "tier", "leaguePoints", "games", "wins", "loses", "winratio");
                grid.getColumns().forEach(col -> col.setAutoWidth(true));
        }

        public void editSummoner(SummonerData summonerData) {
                if(summonerData == null) {
                        closeEditor();
                } else {
                        form.setSummonerData(summonerData);
                        form.setVisible(true);
                        addClassName("adding");
                }
        }

        private void closeEditor() {
                form.setSummonerData(null);
                form.setVisible(false);
                removeClassName("adding");
        }

        private void updateList(){
                grid.setItems(summonerDataService.findAll(filterText.getValue()));
        }

}
