package com.example.application.views.updates;

import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.service.TeslaModelService;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class SoftwareUpdateForm extends FormLayout {

    private SoftwareUpdate editedSoftwareUpdate = new SoftwareUpdate();
    private final BeanValidationBinder<SoftwareUpdate> binder;

    public SoftwareUpdateForm(TeslaModelService teslaModelService) {
        TextField version = new TextField("Version");
        DatePicker releaseDate = new DatePicker("Release Date");
        add(version, releaseDate);

        binder = new BeanValidationBinder<>(SoftwareUpdate.class);
        binder.forField(version).bind(SoftwareUpdate::getVersion, SoftwareUpdate::setVersion);
        binder.forField(releaseDate).bind(SoftwareUpdate::getReleaseDate, SoftwareUpdate::setReleaseDate);

        /* Task 1:
         * Allow users to select which car models an update should be applied to, using a multi-select combo box.
         * Users must select at least one model.
         * You can use the TeslaModelService to get a list of all availabe models.
         */



        /* Task 2:
         * Display the currently selected models in a text box (for example a read-only text area) at the end of the form.
         */



        /* Task 3:
         * Add a button that selects all models.
         * The button should only be enabled if no model is selected yet.
         */



    }

    public void edit(SoftwareUpdate softwareUpdate) {
        this.editedSoftwareUpdate = softwareUpdate;
        binder.readBean(this.editedSoftwareUpdate);
    }

    public SoftwareUpdate write() throws ValidationException {
        binder.writeBean(this.editedSoftwareUpdate);
        return this.editedSoftwareUpdate;
    }

    public void clear() {
        this.edit(new SoftwareUpdate());
    }
}
