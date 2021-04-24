package com.project.webLoLApp.ui.view.list;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.project.webLoLApp.backend.SummonerData;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

import static com.project.webLoLApp.WebLoLAppApplication.ApiKey;

public class SummonerForm extends FormLayout {

    private SummonerData summonerData;
    private TextField name = new TextField("Summoner name");
    private ComboBox<Platform> platform = new ComboBox<>("Region");
    private Binder<SummonerData> binder = new BeanValidationBinder<>(SummonerData.class);

    private Button save = new Button("Save");
    private Button close = new Button("Cancel");

    public SummonerForm(List<Platform> platforms){
        Orianna.setRiotAPIKey(ApiKey);
        Orianna.setDefaultPlatform(Platform.EUROPE_WEST);
        addClassName("summoner-form");
        binder.bindInstanceFields(this);
        platform.setItems(platforms);

        add(name, platform, createButtonsLayout());
    }

    public void setSummonerData(SummonerData summonerData) {
        this.summonerData = summonerData;
        binder.readBean(summonerData);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave(){
        try {
            binder.writeBean(summonerData);
            fireEvent(new SaveEvent(this, new SummonerData(Orianna.summonerNamed(name.getValue()).withPlatform(platform.getValue()).get())));
        } catch (ValidationException e){
            e.printStackTrace();
        }
    }

    public static abstract class SummonerFormEvent extends ComponentEvent<SummonerForm> {
        private SummonerData summonerData;

        SummonerFormEvent(SummonerForm source, SummonerData summonerData){
            super(source, false);
            this.summonerData = summonerData;
        }

        public SummonerData getSummonerData() {
            return summonerData;
        }
    }

    public static class SaveEvent extends SummonerFormEvent {
        SaveEvent(SummonerForm source, SummonerData summonerData){
            super(source,summonerData);
        }
    }


    public static class CloseEvent extends SummonerFormEvent {
        CloseEvent(SummonerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
          ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
