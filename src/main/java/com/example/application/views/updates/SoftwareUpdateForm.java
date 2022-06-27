package com.example.application.views.updates;

import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.entity.TeslaModel;
import com.example.application.data.service.TeslaModelService;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class SoftwareUpdateForm extends FormLayout {

    private SoftwareUpdate editedSoftwareUpdate;
    private final BeanValidationBinder<SoftwareUpdate> binder;

    public SoftwareUpdateForm(TeslaModelService teslaModelService) {
        TextField version = new TextField("Version");
        DatePicker releaseDate = new DatePicker("Release Date");
        MultiSelectComboBox<TeslaModel> models = new MultiSelectComboBox<>("Models");
        models.setItemLabelGenerator(TeslaModel::getName);
        models.setItems(teslaModelService.listAll());

        binder = new BeanValidationBinder<>(SoftwareUpdate.class);
        binder.forField(version).bind(SoftwareUpdate::getVersion, SoftwareUpdate::setVersion);
        binder.forField(releaseDate).bind(SoftwareUpdate::getReleaseDate, SoftwareUpdate::setReleaseDate);
        binder.forField(models).bind(SoftwareUpdate::getModels, SoftwareUpdate::setModels);

        add(version, releaseDate, models);
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
